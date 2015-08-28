package com.mnmlist.backup.byTag;

import com.mnmlist.backup.byTag.BlogInfo;
import com.mnmlist.backup.systemBegin.SpiderClawering;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BackupByTag implements SpiderClawering
{
	private String urlStr;
	private String tagStr;
	public BackupByTag(String urlStr,String tagStr)
	{
		this.urlStr=urlStr;
		this.tagStr=tagStr;
	}
	@Override
	public void spiderClawering()
	{
		// TODO Auto-generated method stub
		//Spider.articleTag=tagStr;
		final BlogInfo blogInfo=new BlogInfo();//which store the relevant information of the blog
		blogInfo.setArticleTag(tagStr);//it will be used during the backup progress 
		String url=ParseTool.getUrlFromTag(urlStr, tagStr,blogInfo);
		Spider.spiderClawering(url,blogInfo);
	}

}
