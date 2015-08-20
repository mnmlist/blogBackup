package com.mnmlist.backup.byColum;

import com.mnmlist.backup.systemBegin.SpiderClawering;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BackupByColum implements SpiderClawering
{
	private String urlStr;
	public  BackupByColum(String urlStr)
	{
		this.urlStr=urlStr;
	}
	@Override
	public void spiderClawering()
	{
		// TODO Auto-generated method stub
		BlogInfo blogInfo=new BlogInfo();//store the relevant information of the blog
		Spider.spiderClawering(urlStr,blogInfo);
	}

}
