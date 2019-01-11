package util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

public class FileUtils {

	/**
	 * 获取文件文本编码
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String codeString(String fileName) throws Exception {
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(JChardetFacade.getInstance());

		Charset charset = null;

		File f = new File(fileName);

		try {
			charset = detector.detectCodepage(f.toURI().toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset.toString();
	}

	/**
	 * 根据编码读取文本
	 * @param url
	 * @return
	 */
	public static String readFileText(String url) {
		String code = "UTF-8";
		File file = new File(url);
		StringBuilder text = new StringBuilder();
		if (file.isFile() && file.exists()) {
			try {
				code = codeString(url);
				if(code.equals("windows-1252")) {
					code = "gb2312";
				}
				// System.out.println("----" + code);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
//				byte[] buffer = new byte[(int) file.length()];
				 byte[] buffer = new byte[1024];
				int cnt = 0;
				if(buffer.length != 0) {
					while ((cnt = bis.read(buffer)) != -1) {
						text.append(new String(buffer, 0, cnt, code));
					}
				}
				bis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return text.toString();
		}else {
			return null;
		}
	}
	
	/**
	 * 写入本地
	 * @param fileName
	 * @param htmlData
	 */
	public static void writeToLocal(String fileName, String htmlData) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\淘宝商品数据采集\\测试");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File("C:\\Users\\Administrator\\Desktop\\淘宝商品数据采集\\测试\\" + fileName + ".txt");
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
}
