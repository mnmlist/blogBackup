package com.mnmlist.backup.byTotal.pdf;


import java.util.Date;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;

import org.htmlparser.util.NodeList;


/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class Spider {

	/*
	 * @param url 某CSDN博客的默认首页
	 */
	public static void crawlerBegin(String url,BlogInfo blogInfo)
	{
		try {
			//get the author
			NodeList authorList=ParseTool.getNodeList(url, "div", "id", "blog_title");
			String author=ParseTool.parseAuthor(authorList);
			FileTool.makeDir(author);
			FileTool.makeDir(author+"/pdf");
			//get the month index
			NodeList monthList=ParseTool.getNodeList(url, "div", "id", "archive_list");
			ParseTool.parseMonthArticle(monthList,blogInfo);
			List<Attribute> li =blogInfo.getMonthIndexList().asList();//get the month text and the link corresponded
			for (int i = 0; i < li.size(); i++) {
				AttributeList articles = new AttributeList();
				blogInfo.getDirIndexList().add(new Attribute(li.get(i).getName(), articles));//month and essays corresponded 
				ParseTool.parseMonth(author + "/" + li.get(i).getName(),// author/month,link,essayList
						"http://blog.csdn.net"+(String) li.get(i).getValue(), articles,blogInfo);
			}
			//create the directory which we can browse the whole blog
			HandleTool.handleIndex(author,blogInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		blogInfo.getMonthIndexList().clear();
	}
	//print the start time,end time and timespan of the clawering
	/*
	 * @param url 某CSDN博客的默认首页
	 */
	public static void spiderClawering(String url,BlogInfo blogInfo)
	{
		Date date1=new Date();
		System.out.println("For "+url+" the start time is :"+date1.toLocaleString());
		System.out.println("\nYou can find the essays from:"
				+ System.getProperty("user.dir"));
		System.out.println("I'm working hard to get the content of "+url+ ",about 10 minutes will be OK....");
		Spider.crawlerBegin(url,blogInfo);
		Date date2=new Date();
		System.out.println("\nFor "+url+" the end time is :"+date2.toLocaleString());
		int second=(int) ((date2.getTime()-date1.getTime())/1000);
		int minute=second/60;
		second=second%60;
		System.out.println("For "+url+" the total backup time is :"+minute+" minutes,"+second+" seconds.");
	}
	//the spider begins....
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//we can also clawer a list of URL from the bookmark
		/*String path="url1.txt";
		String html=FileTool.getFileFromDisk(path);
		List<String>urlList=GetURLFromBookmark.getURL(html);
		int len=urlList.size();
		for(int i=0;i<len;i++)
			Spider.spiderClawering(urlList.get(i));
		*/
		//to simplyfy the demo,we clawer the blog from the url given as below.
		Spider.spiderClawering("http://blog.csdn.net/mnmlist",new BlogInfo());
		
		
	}

}
