package com.mnmlist.backup.byTotal;

import com.mnmlist.backup.systemBegin.SpiderClawering;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BackupByTotal implements SpiderClawering
{
	private String urlStr;
	public  BackupByTotal(String urlStr)
	{
		this.urlStr=urlStr;
	}
	@Override
	public void spiderClawering()
	{
		// TODO Auto-generated method stub
		final BlogInfo blogInfo=new BlogInfo();
		Spider.spiderClawering(urlStr,blogInfo);
	}

}
