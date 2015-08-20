package com.mnmlist.backup.byColum;

import java.io.OutputStreamWriter;

import javax.management.AttributeList;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BlogInfo
{
	// the article title of some page
	private String articleTitle = null;
	// the path where the backup articles store
	private String newArticlePath = null;
	// the path where the backup images store
	private String blogImgDir = null;
	// the path where the catalog store
	private String dirPath = null;
	// the article list
	private AttributeList columArticleList = new AttributeList();
	// the catalog index list
	private AttributeList dirIndexList = new AttributeList();
	// the image rosource list of one article
	private AttributeList imageResourceList = new AttributeList();
	// used to create the catalog of the whole blog
	private OutputStreamWriter dirIndexWriter = null;

	public String getArticleTitle()
	{
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle)
	{
		this.articleTitle = articleTitle;
	}

	public String getNewArticlePath()
	{
		return newArticlePath;
	}

	public void setNewArticlePath(String newArticlePath)
	{
		this.newArticlePath = newArticlePath;
	}

	public String getBlogImgDir()
	{
		return blogImgDir;
	}

	public void setBlogImgDir(String blogImgDir)
	{
		this.blogImgDir = blogImgDir;
	}

	public String getDirPath()
	{
		return dirPath;
	}

	public void setDirPath(String dirPath)
	{
		this.dirPath = dirPath;
	}

	public AttributeList getColumArticleList()
	{
		return columArticleList;
	}

	public void setColumArticleList(AttributeList columArticleList)
	{
		this.columArticleList = columArticleList;
	}

	public AttributeList getDirIndexList()
	{
		return dirIndexList;
	}

	public void setDirIndexList(AttributeList dirIndexList)
	{
		this.dirIndexList = dirIndexList;
	}

	public AttributeList getImageResourceList()
	{
		return imageResourceList;
	}

	public void setImageResourceList(AttributeList imageResourceList)
	{
		this.imageResourceList = imageResourceList;
	}

	public OutputStreamWriter getDirIndexWriter()
	{
		return dirIndexWriter;
	}

	public void setDirIndexWriter(OutputStreamWriter dirIndexWriter)
	{
		this.dirIndexWriter = dirIndexWriter;
	}

}
