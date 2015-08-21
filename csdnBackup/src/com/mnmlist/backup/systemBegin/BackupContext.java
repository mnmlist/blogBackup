package com.mnmlist.backup.systemBegin;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BackupContext implements Runnable
{
	//use the strategy mode to make the program more readable.
	SpiderClawering spiderClawering;
	public BackupContext(SpiderClawering spiderClawering)
	{
		this.spiderClawering=spiderClawering;
	}
	//where the backup system begins.
	public void backupBegin()
	{
		spiderClawering.spiderClawering();
	}
	//each url corresponds to a thread
	@Override
	public  void run()
	{
		backupBegin();
	}
	
}
