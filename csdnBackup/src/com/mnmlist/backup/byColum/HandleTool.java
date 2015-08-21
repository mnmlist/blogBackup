package com.mnmlist.backup.byColum;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class HandleTool {
	/*
	 * @param path 文件路径
	 * 
	 * @param url 文章在blog上的URL
	 * 
	 * @param articles 保存本月存档的列表
	 */
	public static void handleHtml(String titleName, String url,final BlogInfo blogInfo) {// 用户名/月份，月份的url或文章的url，文章的列表
		
		String pathName=blogInfo.getBlogImgDir()+"/"+titleName;//存放图片和html
		blogInfo.setNewArticlePath(titleName);
		blogInfo.setDirPath("ByMnmlist"+"/"+titleName);
		try {
			StringBuffer text = new StringBuffer();
			Parser parser=ParserInstance.getParserInstance(url);
			NodeList nodes = parser.extractAllNodesThatMatch(new NodeFilter() {
				public boolean accept(Node node) {
					if (node instanceof Div) {
						Div dv = (Div) node;
						NodeList nlist = dv.getChildren();
						if (dv.getAttribute("id") != null && nlist != null) {
							if (dv.getAttribute("id").equalsIgnoreCase(
									"article_content")) {
								ParseTool.parseImg(nlist, 0,blogInfo);//parse img
								return true;
							}
						}
					}
					return false;
				}
			});
			Node node = nodes.elementAt(0);
			
			/* 加入meta信息 */
			text.append(new String(
					"<meta http-equiv=\"Content-Type\" content=\"text/html; chaset=utf-8\"/>"));
			text.append("\r\n");
			text.append("<h1>" + titleName + "</h1>");
			text.append("\r\n");
			if (node != null) {
				Div dv = (Div) node;
				text.append(new String(dv.toHtml().getBytes("UTF-8"), "UTF-8"));
				text.append("\r\n");
			} else {
				text.append("<h3>Download error</h3>");
				text.append("\r\n");
			}
			FileTool.makeDir(pathName + "_files");
			List<Attribute> li =blogInfo.getImageResourceList().asList();
			for (int i = 0; i < li.size(); i++) {
				byte[] imgString = FileTool.getContent((String) li.get(i).getValue(), 0);
				FileTool.writeFile(pathName + "_files/" + li.get(i).getName()
						+ ".gif", imgString);
			}
			blogInfo.getImageResourceList().clear();
			blogInfo.getDirIndexList().add(new Attribute(blogInfo.getDirPath(), titleName));//第一个参数为以index.html
			//为参照文件去.html后的完整路径，第二个参数为要显示的文本
			FileTool.writeFile(pathName+".html", text.toString().getBytes());
		} catch (Exception e) {
			// 追加出错日志
			e.printStackTrace();
		}
	}
	/*
	 * @param dir 本地存档根路径名称
	 * 
	 * @return 无
	 */
	static void handleIndex(String dir,BlogInfo blogInfo) {
		try {
			blogInfo.setDirIndexWriter(new OutputStreamWriter(new FileOutputStream(dir
					+ "/index.html"), "utf-8"));
			StringBuilder htmlContentBulider=new StringBuilder();
			StringBuilder htmlheaderBulider=new StringBuilder();
			StringBuilder blogIndexBuilder=new StringBuilder();
			htmlheaderBulider.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>CSDN文章归档</title></head><body bgcolor=\"white\" text=\"black\" link=\"#0000FF\" vlink=\"#840084\" alink=\"#0000FF\"><hr></div><div><h1 class=\"title\"><a name=\"id2747881\"></a>");
			htmlheaderBulider.append(dir);
			htmlheaderBulider.append("CSDN文章归档</h1></div></div><hr></div><div class=\"toc\"><p><b>目录</b></p><dl><dt><span class=\"preface\"><a href=\"preface.html\">摘要</a></span></dt>");
			List<Attribute> li =blogInfo.getDirIndexList().asList();
			for (int i = 0; i < li.size(); i++) {
				blogIndexBuilder.append("<dt><span class=\"part\"><a href=\"");
				blogIndexBuilder.append(li.get(i).getName());
				blogIndexBuilder.append(".html\">");
				blogIndexBuilder.append(li.get(i).getValue());
				blogIndexBuilder.append("</a></span></dt>");
			}
			String tailer = "</div></div><hr></body></html>";
			htmlContentBulider.append(htmlheaderBulider);
			htmlContentBulider.append(blogIndexBuilder);
			htmlContentBulider.append(tailer);
			OutputStreamWriter writer=blogInfo.getDirIndexWriter();
			writer.write( new String(htmlContentBulider.toString().getBytes("utf-8"),"utf-8"));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

