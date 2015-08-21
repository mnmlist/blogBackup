package com.mnmlist.backup.byTotal;

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
	 * 
	 * @return 无
	 */
	public static void handleHtml(String path, String url,
			AttributeList articles,final BlogInfo blogInfo) {// 用户名/月份，月份的url或文章的url，文章的列表
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
							} else if (dv.getAttribute("id").equalsIgnoreCase(
									"article_details")) {
								ParseTool.parseTitle(nlist,blogInfo);//parse title
							} 
						}
					}
					return false;
				}
			});
			Node node = nodes.elementAt(0);
			String title =blogInfo.getArticleTitle();//(String) ((List<Attribute>) Spider.imageResourceList.asList()).get(0).getValue();
			String filepath = path + "/" + title;
			List<Attribute> li =blogInfo.getImageResourceList().asList();
			/* 加入meta信息 */
			text.append(new String(
					"<meta http-equiv=\"Content-Type\" content=\"text/html; chaset=utf-8\"/>"));
			text.append("\r\n");
			text.append("<h1>" + title + "</h1>");
			text.append("\r\n");
			if (node != null) {
				Div dv = (Div) node;
				text.append(new String(dv.toHtml().getBytes("UTF-8"), "UTF-8"));
				text.append("\r\n");
			} else {
				text.append("<h3>Download error</h3>");
				text.append("\r\n");
			}

			FileTool.makeDir(filepath + "_files");
			articles.add(new Attribute(filepath.split("/", 2)[1], title));

			for (int i = 0; i < li.size(); i++) {
				byte[] imgString = FileTool.getContent((String) li.get(i).getValue(), 0);
				FileTool.writeFile(filepath + "_files/" + li.get(i).getName()
						+ ".gif", imgString);
			}
			blogInfo.getImageResourceList().clear();
			FileTool
					.writeFile(filepath + ".html", text.toString().getBytes());
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
				//append mindex
				blogIndexBuilder.append("<dt><span class=\"part\"><h4>");
				blogIndexBuilder.append(li.get(i).getName());
				blogIndexBuilder.append("</span></dt><dd><dl>");
				AttributeList articles = (AttributeList) li.get(i).getValue();
				List<Attribute> al = articles.asList();
				for (int j = 0; j < al.size(); j++) {
					//append per
					blogIndexBuilder.append("<dt><span class=\"part\"><a href=\"");
					blogIndexBuilder.append(al.get(j).getName());
					blogIndexBuilder.append(".html\">");
					blogIndexBuilder.append(al.get(j).getValue());
					blogIndexBuilder.append("</a></span></dt>");
				}
				blogIndexBuilder.append("</dl></dd>");
			}
			String tailer = "</div></div><hr></body></html>";
			htmlContentBulider.append(htmlheaderBulider);
			htmlContentBulider.append(blogIndexBuilder);
			htmlContentBulider.append(tailer);
			OutputStreamWriter writer=blogInfo.getDirIndexWriter();
			writer.write(new String(htmlContentBulider.toString().getBytes("utf-8"),"utf-8"));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

