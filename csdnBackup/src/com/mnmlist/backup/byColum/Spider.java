package com.mnmlist.backup.byColum;


import java.util.Date;

import org.htmlparser.util.NodeList;

import com.mnmlist.backup.lib.*;
/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class Spider {

	/*
	 * @param url 
	 * the default home page url of some CSDN colum
	 */
	public static void crawlerBegin(String url,BlogInfo blogInfo)
	{
		try {
			//get the columsName
			NodeList titleList=ParseTool.getNodeList(url, "div", "class", "way_nav");
			ParseTool.parseColumsTitle(titleList,blogInfo);
			String blogTitlesString=blogInfo.getArticleTitle();
			String blogImgDirString=blogTitlesString+"/"+"ByMnmlist";
			FileTool.makeDir(blogTitlesString);
			//get the colum article list
			blogInfo.setBlogImgDir(blogImgDirString);
			FileTool.makeDir(blogImgDirString);
			ParseTool.parseColums(blogImgDirString, url,blogInfo.getColumArticleList(),blogInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HandleTool.handleIndex(blogInfo.getArticleTitle(),blogInfo);
	}
	/*
	 * @param url 
	 * the default home page url of some CSDN colum
	 */
	public static void spiderClawering(String url,BlogInfo blogInfo)
	{
		Date date1=new Date();
		System.out.println("Start time:"+date1.toLocaleString());
		System.out.println("You can find the essays from:"
				+ System.getProperty("user.dir"));
		System.out.println("I'm working hard to get the content of "+url+",about 10 minutes will be OK....");
		Spider.crawlerBegin(url,blogInfo);
		Date date2=new Date();
		System.out.println("End time:"+date2.toLocaleString());
		int second=(int) ((date2.getTime()-date1.getTime())/1000);
		int minute=second/60;
		second=second%60;
		System.out.println("The total time is: "+minute+" minutes,"+second+" seconds.");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//we can get the url form a bookMark.html
   		/*String path="url.txt";
		String html=FileTool.getFileFromDisk(path);
		List<String>urlList=GetURLFromBookmark.getURL(html);
		int len=urlList.size();
		for(int i=0;i<len;i++)
			Spider.spiderClawering(urlList.get(i));
		*/
		//spiderClawering("http://blog.csdn.net/column/details/pearls.html");
		
	}

}
