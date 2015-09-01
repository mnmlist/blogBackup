package com.mnmlist.backup.byTotal.pdf;

import java.io.OutputStreamWriter;

import javax.management.AttributeList;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BlogInfo
{
	private String articleTitle=null;
	//博客名字，创建总目录author -> bloger name
	private AttributeList blogAuthorList=new AttributeList();
	//文章标题
	private AttributeList articleTitleList=new AttributeList();
	//月份列表 month->month link
	private AttributeList monthIndexList=new AttributeList();
	//每个月份对应文章列表 title->title link
	private AttributeList monthArticleList=new AttributeList();
	//每篇文章对应的图片列表 img description->img link
	private AttributeList imageResourceList=new AttributeList();
	//用于生成目录的目录列表 month->articles of a month(list)
	private AttributeList dirIndexList=new AttributeList();
	//用于生成目录的writer
	private OutputStreamWriter dirIndexWriter=null;
	public String getArticleTitle()
	{
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle)
	{
		this.articleTitle = articleTitle;
	}
	public OutputStreamWriter getDirIndexWriter()
	{
		return dirIndexWriter;
	}
	public void setDirIndexWriter(OutputStreamWriter dirIndexWriter)
	{
		this.dirIndexWriter = dirIndexWriter;
	}
	public AttributeList getBlogAuthorList()
	{
		return blogAuthorList;
	}
	public void setBlogAuthorList(AttributeList blogAuthorList)
	{
		this.blogAuthorList = blogAuthorList;
	}
	public AttributeList getArticleTitleList()
	{
		return articleTitleList;
	}
	public void setArticleTitleList(AttributeList articleTitleList)
	{
		this.articleTitleList = articleTitleList;
	}
	public AttributeList getMonthIndexList()
	{
		return monthIndexList;
	}
	public void setMonthIndexList(AttributeList monthIndexList)
	{
		this.monthIndexList = monthIndexList;
	}
	public AttributeList getMonthArticleList()
	{
		return monthArticleList;
	}
	public void setMonthArticleList(AttributeList monthArticleList)
	{
		this.monthArticleList = monthArticleList;
	}
	public AttributeList getImageResourceList()
	{
		return imageResourceList;
	}
	public void setImageResourceList(AttributeList imageResourceList)
	{
		this.imageResourceList = imageResourceList;
	}
	public AttributeList getDirIndexList()
	{
		return dirIndexList;
	}
	public void setDirIndexList(AttributeList dirIndexList)
	{
		this.dirIndexList = dirIndexList;
	}
}
