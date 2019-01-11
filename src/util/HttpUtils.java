package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class HttpUtils {
	
	/**
	 * Jsoup Get请求方法
	 * @param url
	 * @param headerMap
	 * @return
	 */
	public static String sendGet(String url,Map<String,String> headerMap) {
		Connection connect = Jsoup.connect(url);
		//设置请求头
		for (String name : headerMap.keySet()) {
			connect.header( name,headerMap.get(name));
		}
		Response response = null;
		String body = null;
		try {
			System.getProperties().setProperty("http.proxySet", "false");  
			
			response = connect.timeout(5000).ignoreContentType(true).execute();
			body = response.body();
		} catch (SocketTimeoutException e) {
			System.err.println("HttpUtils.java 32:连接超时!***********************************************");
			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("请求失败地址:"+url);
		}
		return body;
	}
	
	/**
	 * Jsoup Post方法
	 * @return
	 */
	public static String jsoupPost(String url,Map<String,String> paramMap,Map<String,String> headerMap) {
		// 获取请求连接
		Connection connect = Jsoup.connect(url);
		// 遍历生成参数
		if (paramMap != null) {
			for (Entry<String, String> entry : paramMap.entrySet()) {
				// 添加参数
				connect.data(entry.getKey(), entry.getValue());
			}
		}
		// 设置请求头
		for (String name : headerMap.keySet()) {
			connect.header(name, headerMap.get(name));
		}
		connect.method(Method.POST);
		connect.followRedirects(false);
		connect.ignoreContentType(true);//返回格式为json
		Response response = null;
		String body = null;
		try {
			response = connect.timeout(5000).execute();
			body = response.body();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	public static String payLoadPost(String requestUrl,String param, Map<String,String> headerMap) {
		StringBuffer result = new StringBuffer();// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		OutputStreamWriter out = null;
		try {
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(requestUrl);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置请求头
			for (String name : headerMap.keySet()) {
				httpConn.setRequestProperty(name, headerMap.get(name));
			}
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new OutputStreamWriter(httpConn.getOutputStream(),"UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		if(result!=null) {
			return result.toString();
		}
		return null;
	}
	
	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendPost(String url, Map<String, String> parameters) {
		String result = "";// 返回的结果
		BufferedReader in = null;// 读取响应输入流
		PrintWriter out = null;
		StringBuffer sb = new StringBuffer();// 处理请求参数
		String params = "";// 编码之后的参数
		try {
			// 编码请求参数
			if (parameters.size() == 1) {
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8"));
				}
				params = sb.toString();
			} else if(parameters.size()!=0){
				for (String name : parameters.keySet()) {
					sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8")).append("&");
				}
				String temp_params = sb.toString();
				params = temp_params.substring(0, temp_params.length() - 1);
			}
			// 创建URL对象
			java.net.URL connURL = new java.net.URL(url);
			// 打开URL连接
			java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
