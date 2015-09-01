package com.mnmlist.backup.byTag;


import java.util.Date;


/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class Spider {

	/*
	 * @param url 某CSDN博客的默认首页url
	 */
	public static void crawlerBegin(String url,BlogInfo blogInfo)
	{
		try {
			String articleTag=blogInfo.getArticleTag();
			String blogImgDir=articleTag+"/"+"ByMnmlist";
			//get the columsName
			FileTool.makeDir(articleTag);
			//get the colum article list
			blogInfo.setBlogImgDir(blogImgDir);
			//blogImgDir=articleTag+"/"+"ByMnmlist";
			FileTool.makeDir(blogImgDir);
			ParseTool.parseColums(blogImgDir, url,blogInfo.getColumArticleList(),blogInfo);
			HandleTool.handleIndex(articleTag,blogInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * @param url 某CSDN博客的默认首页url
	 */
	public static void spiderClawering(String url,BlogInfo blogInfo)
	{
		url="http://blog.csdn.net"+url;
		Date date1=new Date();
		System.out.println("For the "+url+"the start time is:"+date1.toLocaleString());
		System.out.println("\nYou can find the essays for "+url+" from:"
				+ System.getProperty("user.dir"));
		System.out.println("I'm working hard to get the content of "+url+",about 10 minutes will be OK....");
		Spider.crawlerBegin(url,blogInfo);
		Date date2=new Date();
		System.out.println("\nFor the "+url+"the end time is:"+date2.toLocaleString());
		int second=(int) ((date2.getTime()-date1.getTime())/1000);
		int minute=second/60;
		second=second%60;
		System.out.println("For the "+url+"the total backup time is :"+minute+" minutes,"+second+" seconds.");
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
		//spiderClawering("http://blog.csdn.net/column/details/job-school.html?&page=1");
//		String urlString="http://blog.csdn.net/doc_sgl";
//		String tagString="算法与数据结构";
//		articleTag=tagString;
//		String url=ParseTool.getUrlFromTag(urlString, tagString);
//		if(url!=null)
//			spiderClawering(url);
	}

}
