package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by xiang.li on 2015/2/28. PBE 加解密工具类
 */
public class PBE {
	private final static String ch_rul = "http://62.234.115.18/pollcodecheck/index/ch";

	public static String readLocalPcode() {
		String pcode = null;
		String url = new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"\\tbjdpc";
		File file = new File(url);
		if(file.exists()) {
			file = new File(url+"\\p.pc");
			if(file.exists()) {
				pcode = FileUtils.readFileText(url+"\\p.pc"); 
			}
		}
		return pcode;
	}
	
	/**
	 * 写入本地
	 * @param htmlData
	 */
	public static void writeToLocal(String htmlData) {
		File file = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"\\tbjdpc");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"\\tbjdpc\\p.pc");
		OutputStreamWriter writer = null;
		BufferedWriter bw = null;
		try {
			OutputStream os = new FileOutputStream(file);
			writer = new OutputStreamWriter(os, "UTF-8");
			bw = new BufferedWriter(writer);
			bw.write(htmlData);
			bw.flush();
			if (file.exists()) {
				file.delete();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//注册检查
	public static boolean checkPollCode(String pcode) {
		boolean boo = false;
		// 硬盘编号
		String sn = PBE.getSerialNumber("C");
		// cpu编号
		String cpu = PBE.getCpuSerial();
		// 加密前的原文
		String str = PBE.MD5Encode(sn + cpu, "utf8");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("mcode", str);
		paramMap.put("pcode", pcode.trim());
		
		String msg = HttpUtils.sendPost(ch_rul, paramMap);
		if(msg!=null) {
			if(msg.indexOf("error")==0) {
				JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
				boo = false;
			}else {
				String[] reginfos = msg.split("W5J2Ss7A1Y0s");
				//服务器盐字符串
				String saltStr = null;
				//盐
				byte[] salt = null;
				//服务器机器码字符串
				String mcodeStr = null;
				//解码后的机器码
				String mcode = null;
				// 采用PBE算法解密
				byte[] decData = null;
				if(reginfos.length>1) {
					mcodeStr = reginfos[0];
					saltStr = decodeBase64(reginfos[1]);
					if(saltStr!=null) {
						try {
							//字符串转盐
							salt = new Gson().fromJson(saltStr, new TypeToken<byte[]>() {}.getType());
							decData = PBE.decryptPBE(new BASE64Decoder().decodeBuffer(mcodeStr), "w8j5s0-a1yddd-1s2t05-edfb8", salt);
							mcode = new String(decData, "UTF-8");
							if(mcode.equals(str)) {
								writeToLocal(msg);
								boo = true;
							}
						}catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}catch (IOException e1) {
							e1.printStackTrace();
						}catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "error:网络出错,请稍后重试!", "错误", JOptionPane.ERROR_MESSAGE);
			boo = false;
		}
		return boo;
	}

	/**
	 * MD5加密
	 * 
	 * @param origin
	 *            字符
	 * @param charsetname
	 *            编码
	 * @return
	 */

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (null == charsetname || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception e) {
		}
		return resultString;
	}
	
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	
	private static final String hexDigIts[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigIts[d1] + hexDigIts[d2];
	}

	/**
	 * 
	 * @param drive
	 *            默认"C"
	 * @return
	 */
	public static String getSerialNumber(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("damn", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new FileWriter(file);
			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n" + "Set colDrives = objFSO.Drives\n"
					+ "Set objDrive = colDrives.item(\"" + drive + "\")\n" + "Wscript.Echo objDrive.SerialNumber"; // see note
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;

			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * 获取CPU序列号
	 * 
	 * @return
	 */
	public static String getCpuSerial() {
		String serial = null;
		Scanner sc;
		String property;
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
			process.getOutputStream().close();
			sc = new Scanner(process.getInputStream());
			property = sc.next();
			serial = sc.next();
			// System.out.println(property + ": " + serial);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serial;
	}

	/**
	 * 定义加密方式 支持以下任意一种算法
	 * <p/>
	 * 
	 * <pre>
	 * PBEWithMD5AndDES
	 * PBEWithMD5AndTripleDES
	 * PBEWithSHA1AndDESede
	 * PBEWithSHA1AndRC2_40
	 * </pre>
	 */
	private final static String KEY_PBE = "PBEWithMD5AndDES";

	private final static int SALT_COUNT = 100;

	/**
	 * 转换密钥
	 *
	 * @param key
	 *            字符串
	 * @return
	 */
	private static Key stringToKey(String key) {
		 KeyGenerator kgen = null;
		 SecureRandom random = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
        
         random.setSeed(key.getBytes());
         kgen.init(128, random);
		SecretKey secretKey = null;
		try {
			PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
			SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_PBE);
			secretKey = factory.generateSecret(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return secretKey;
	}

	/**
	 * PBE 解密
	 *
	 * @param data
	 *            需要解密的字节数组
	 * @param key
	 *            密钥
	 * @param salt
	 *            盐
	 * @return
	 */
	private static byte[] decryptPBE(byte[] data, String key, byte[] salt) {
		byte[] bytes = null;
		try {
			// 获取密钥
			Key k = stringToKey(key);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, SALT_COUNT);
			Cipher cipher = Cipher.getInstance(KEY_PBE);
			cipher.init(Cipher.DECRYPT_MODE, k, parameterSpec);
			bytes = cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * BASE64 加密
	 *
	 * @param key
	 *            需要加密的字节数组
	 * @return 字符串
	 * @throws Exception
	 */
	private static String encryptBase64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}
	
	/**
	 * BASE64 解密
	 * @param str
	 * @return
	 */
	private static String decodeBase64(String str) {
		String s = null;
		try {
			s = new String(new BASE64Decoder().decodeBuffer(str), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

}
