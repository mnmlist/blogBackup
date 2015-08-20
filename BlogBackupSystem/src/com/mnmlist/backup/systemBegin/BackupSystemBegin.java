package com.mnmlist.backup.systemBegin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.mnmlist.backup.byColum.BackupByColum;
import com.mnmlist.backup.byTag.BackupByTag;
import com.mnmlist.backup.byTotal.BackupByTotal;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class BackupSystemBegin
{
	//backup the blog depends on which kind of the backup method
	public static void startClawing() throws IOException
	{
		System.out.println("If url.txt have the url or the relevent information,\n Please input the number of the backup type...");
		System.out.println("1:backupByTotal,2:backupByTag,3:backupByColum.");
		int type=0;
		//read the number from the console to determine which method will adopt to back the blog
		Scanner scanner=new Scanner(System.in);
		type=scanner.nextInt();
		scanner.close();
		File urlFile=new File("url.txt");
		//read the url from the url.txt,there is a thread corresponded to a url
		BufferedReader fileReader=null;
		try
		{
			fileReader=new BufferedReader(new FileReader(urlFile));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		String urlStr,tagStr;
		if(type==1)
		{
			while((urlStr=fileReader.readLine())!=null)
			{
				//new BackupContext(new BackupByTotal(urlStr)).backupBegin();
				new Thread(new BackupContext(new BackupByTotal(urlStr))).start();;
			}
			fileReader.close();
		}else if(type==2)
		{
			while((urlStr=fileReader.readLine())!=null)
			{
				String strArr[]=urlStr.trim().split(" ");
				urlStr=strArr[0].trim();
				tagStr=strArr[strArr.length-1].trim();//it will still work correctly if there ara a lot of spsaces between the url and the tag
				new Thread(new BackupContext(new BackupByTag(urlStr,tagStr))).start();
			}
			fileReader.close();
		}else if(type==3)
		{
			while((urlStr=fileReader.readLine())!=null)
			{
				new Thread(new BackupContext(new BackupByColum(urlStr))).start();;
			}
			fileReader.close();
		}
	}
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		try
		{
			BackupSystemBegin.startClawing();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
