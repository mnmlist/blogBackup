# CSDN BlogBackupSystem
     You can use my system to backup the CSDN blog in three ways,namely totally backup,
  backup by tag,backup by column.You can run my program in eclipse,the backup articles
  will be found in the home directory of the program,you can find the directory as the
  the console message in eclipse,and you can view the articles via the Chrome,Firefox.
  PS.I'm recently add an option that you can convert the articles to .pdf file,namely 
  byTotal in month format,byTag of all the articles,byColumn of all the articles.You 
  can find the .pdf file in the pdf folder or in the root directory of the program.
     The blog backup system start from the main method of com.mnmlist.backup.systemBein.
  BackupSystemBegin.java,as you can see,there are three kinds of backuping blog method
  ,namly 1:backupByTotal,2:backupByTag,3:backupByColum.You should first choose one me-
  thod by type the number of corresponding method.For example,if you want to backup the
  blog by colum,you should type the number 3 in the console of eclipse or Myeclipse.
     The second thing is to type the url to the url.txt,one url for a line,the program
  will backup the blog in multi-thread way,each blog responds to a thread.The url exam-
  ple is as below:
     1.if you want to backup the blog by total,namely you want to backup all the articl-
  es of someone's CSDN blog,you can type the url as below:
	http://blog.csdn.net/mnmlist
	http://blog.csdn.net/fumier
	http://blog.csdn.net/chenhanzhun
	http://blog.csdn.net/sheepmu
	......
	
     2.backup the blog by tag,you can type the url and the tag as below,the url and the 
   tag are splited by the spaces.
	http://blog.csdn.net/fumier JAVA
	http://blog.csdn.net/fumier Linux
	http://blog.csdn.net/mnmlist jiudu
	http://blog.csdn.net/mnmlist leetcode
	......
	
     3.backup the blog by the colum,you can type the url of colums as below:
	http://blog.csdn.net/column/details/cq-struts2.html
	http://blog.csdn.net/column/details/javaleetcodesol.html
	http://blog.csdn.net/column/details/linux----unix.html
	......

