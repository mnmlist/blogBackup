package com.mnmlist.backup.byTotal.pdf;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class FileTool {
	static String startBeforeTitle="<html>\r\n<head><title>";
	static String startAfterTitleString="</title></head>\r\n<body>\r\n";
	static String endString="</body>\r\n</html>";
	static String proxy_addr = null;
	static int proxy_port = 3128;
	/*
	 * @param url 网页的URL
	 * 
	 * @param type 类型：1为文本，0为二进制
	 * 
	 * @return 内容的字节数组
	 */
	public static byte[] getContent(String url, int type) {
		byte ret[] = null;
		try {
			HttpURLConnection conn = null;
			InputStream urlStream = null;
			URL surl=null;
			try {
				surl = new URL(url);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("url is not ilegal");
				e1.printStackTrace();
			}
			int j = -1;
			if (proxy_addr != null) {
				InetSocketAddress soA = new InetSocketAddress(
						InetAddress.getByName(proxy_addr), proxy_port);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, soA);
				conn = (HttpURLConnection) surl.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) surl.openConnection();
			}
			//伪装成Mozilla浏览器 
			conn.setRequestProperty("User-Agent", "Mozilla/4.0");
			conn.connect();
			try {
				urlStream = conn.getInputStream();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("file is not found.");
			}
			if (type == 1) {
				StringBuilder sBuilder = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlStream, "UTF-8"));
				char[] arr = new char[1024];
				while ((j = reader.read(arr)) != -1)
					sBuilder.append(arr, 0, j);
				ret = sBuilder.toString().getBytes();
			} else {
				/* CSDN允许最大图片有上限 */
				byte imgByte[] = new byte[1024];
				ByteBuffer buffer = ByteBuffer.allocate(5000000);
				while ((j = urlStream.read(imgByte)) != -1) {
					buffer.put(imgByte, 0, j);
				}
				ret = buffer.array();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 追加出错日志
		}
		return ret;
	}
	/*
	 * @param path 文件路径
	 * 
	 * @param content 文件内容的字节数组
	 * 
	 * @return 成功或者失败
	 */
	public static boolean writeFile(String path, byte[] content) {
		try {
			FileOutputStream osw = new FileOutputStream(path);
			if(content!=null&&content.length>0)
				osw.write(content);
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
			// 追加出错日志
			return false;
		}
		return true;
	}
	
	/**
	 * @param path the location where the html file stores
	 * @param title the title of the html article
	 * @param htmlString the String content of html file
	 */
	public static void writeFile(String path, String title,String htmlString) {
		StringBuilder htmlStringBuilder=new StringBuilder();
		htmlStringBuilder.append(startBeforeTitle);
		htmlStringBuilder.append(title);
		htmlStringBuilder.append(startAfterTitleString);
		htmlStringBuilder.append(htmlString);
		htmlStringBuilder.append(endString);
		htmlString=htmlStringBuilder.toString();
		boolean isSuccess=writeFile(path+"/"+title, htmlString.getBytes());
		if(!isSuccess)
			System.out.println("An error occurs when store html file to disk.");
	}
	/*
	 * @param path 目录路径
	 * 
	 * @return 成功或者失败
	 */
	public static boolean makeDir(String path) {
		try {
			File fp = new File(path);
			if (!fp.exists()) {
				fp.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 追加出错日志
			return false;
		}
		return true;
	}
}

