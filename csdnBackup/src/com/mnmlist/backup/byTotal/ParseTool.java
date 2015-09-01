package com.mnmlist.backup.byTotal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
/**
 * @author Sting
 *
 */
public class ParseTool
{
	/*
	 * @param url html页面url
	 * 
	 * @param tagNameFilter tagName of a filter
	 * 
	 * @param attributeKey 标签名
	 * 
	 * @param attributeValue 标签值
	 * 
	 * @return 返回某个结点的子节点
	 */
	public static NodeList getNodeList(String url, String tagNameFilter,
			String attributeKey, String attributeValue)
	{
		Parser parser = ParserInstance.getParserInstance(url);
		NodeFilter andFilter = new AndFilter(new TagNameFilter(tagNameFilter),
				new HasAttributeFilter(attributeKey, attributeValue));
		NodeList list = null;
		try
		{
			list = parser.extractAllNodesThatMatch(andFilter);
		} catch (ParserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList nlist = null;
		if (list.size() > 0)
			nlist = list.elementAt(0).getChildren();
		return nlist;
	}

	/*
	 * @param nlist 文章结点
	 */
	public static void parseTitle(NodeList nlist, BlogInfo blogInfo)
	{
		Node tit = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++)
		{
			tit = nlist.elementAt(i);
			if (tit instanceof Span)
			{
				Span span = (Span) tit;
				if (span.getAttribute("class") != null
						&& span.getAttribute("class").equalsIgnoreCase(
								"link_title"))
				{
					LinkTag link = (LinkTag) span.childAt(0);
					String title = link.getLinkText();
					/* 将文件名中不允许的字符替换成允许的字符 */
					title = title.trim();
					title = title.replaceAll("[\\?/:*|<>\"]", "_");
					blogInfo.setArticleTitle(title);
					// Spider.articleTitle=title;
					// Spider.imageResourceList.add(new Attribute("title",
					// title));
				}
			} else
			{
				NodeList slist = tit.getChildren();
				if (slist != null && slist.size() > 0)
				{
					parseTitle(slist, blogInfo);
				}
			}
		}
	}

	/*
	 * @param nlist HTML正文的子标签链表
	 * 
	 * @param index 用于索引图片的个数以及当前的图片数
	 * 
	 * @return 当前的图片数
	 */
	public static int parseImg(NodeList nlist, int index, BlogInfo blogInfo)
	{
		String title = blogInfo.getArticleTitle();
		Node img = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++)
		{
			img = nlist.elementAt(i);
			if (img instanceof ImageTag)
			{
				ImageTag imgtag = (ImageTag) img;
				if (!imgtag.isEndTag())
				{
					/* 将图片的URL映射成本地路径 */
					blogInfo.getImageResourceList().add(
							new Attribute("" + index, new String(imgtag
									.extractImageLocn().getBytes())));
					imgtag.setImageURL(title + "_files/" + index + ".gif");
					/* 递增本地路径序列 */
					index++;
				}
			} else
			{
				NodeList slist = img.getChildren();
				if (slist != null && slist.size() > 0)
				{
					index = ParseTool.parseImg(slist, index, blogInfo);
				}
			}
		}
		return index;
	}

	/*
	 * @param nlist HTML月份存档的子标签链表
	 */
	public static void parseMonthArticle(NodeList nlist, BlogInfo blogInfo)
	{
		Node atls = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++)
		{
			atls = nlist.elementAt(i);
			if (atls instanceof LinkTag)
			{
				LinkTag link = (LinkTag) atls;
				blogInfo.getMonthIndexList().add(
						new Attribute(link.getLinkText(), link.extractLink()));
			} else
			{
				NodeList slist = atls.getChildren();
				if (slist != null && slist.size() > 0)
				{
					parseMonthArticle(slist, blogInfo);
				}
			}
		}

	}

	/*
	 * @param nlist HTML每月份存档的子标签链表
	 */
	public static void parsePerArticle(NodeList nlist, BlogInfo blogInfo)
	{
		Node atl = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++)
		{
			atl = nlist.elementAt(i);
			if (atl instanceof Span)
			{
				Span span = (Span) atl;
				if (span.getAttribute("class") != null
						&& span.getAttribute("class").equalsIgnoreCase(
								"link_title"))
				{
					LinkTag link = (LinkTag) span.childAt(0);
					blogInfo.getMonthArticleList()
							.add(new Attribute(link.getLinkText(),
									"http://blog.csdn.net" + link.extractLink()));
				}
			} else
			{
				NodeList slist = atl.getChildren();
				if (slist != null && slist.size() > 0)
				{
					parsePerArticle(slist, blogInfo);
				}
			}
		}
	}

	/*
	 * @param nlist HTML分页显示标签的子标签链表
	 */
	public static void parsePage(NodeList nlist, BlogInfo blogInfo)
	{// from parseMonth
		Node pg = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++)
		{
			pg = nlist.elementAt(i);
			if (pg instanceof LinkTag)
			{
				LinkTag lt = (LinkTag) pg;
				if (lt.getLinkText().equalsIgnoreCase("下一页"))
				{
					String url = "http://blog.csdn.net" + lt.extractLink();
					NodeList titleList = getNodeList(url, "div", "id",
							"article_list");
					parsePerArticle(titleList, blogInfo);
					NodeList fenYeList = getNodeList(url, "div", "id",
							"papelist");
					if (fenYeList != null)
						parsePage(fenYeList, blogInfo);
					else
						break;
				}
			}
		}
	}

	/*
	 * @param nlist HTML作者信息标签的子标签链表
	 * 
	 * @return 博主名字
	 */
	public static String parseAuthor(NodeList nlist)
	{
		Node aut = null;
		String author = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++)
		{
			aut = nlist.elementAt(i);
			if (aut instanceof LinkTag)
			{
				LinkTag link = (LinkTag) aut;
				return link.getLinkText();
			} else
			{
				NodeList slist = aut.getChildren();
				if (slist != null && slist.size() > 0)
				{
					return ParseTool.parseAuthor(slist);
				}
			}
		}
		return author;
	}

	/*
	 * @param filepath 本地存档的路径
	 * 
	 * @param url 保存本月存档的网页的URL
	 * 
	 * @param articles 保存本月存档的链表
	 */
	public static void parseMonth(String filepath, String url,
			AttributeList articles, BlogInfo blogInfo)
	{
		NodeList titleList = getNodeList(url, "div", "id", "article_list");
		StringBuilder sb = new StringBuilder();
		/* 加入meta信息 */
		sb.append("<html>\r\n");
		sb.append("<head><title>" + filepath.replace('/', ' ')
				+ "</title></head>\r\n<body <font face=\"microsoft yahei\"> >\r\n");
		String articleString = null;
		parsePerArticle(titleList, blogInfo);
		NodeList fenYeList = getNodeList(url, "div", "id", "papelist");
		if (fenYeList != null)
			parsePage(fenYeList, blogInfo);
		FileTool.makeDir(filepath);// bloger name//month Index
		List<Attribute> li = blogInfo.getMonthArticleList().asList();
		//分别保存每一篇博客文章并将整个月份的文章结集打包为.html文件和.pdf文件
		for (int i = 0; i < li.size(); i++)
		{
			articleString = HandleTool.handleHtml(filepath, (String) li.get(i)
					.getValue(), articles, blogInfo);
			sb.append(articleString);
			try
			{
				/* 慢一点，否则会被认为是恶意行为 */
				Thread.sleep(500);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		sb.append("</body>\r\n</html>");
		String fileNameString =filepath.replace('/', '_');
		String htmlString = sb.toString();
		//将整个月份的文章保存于.html文件中
		FileTool.writeFile(filepath+"/"+fileNameString + ".html", htmlString.getBytes());
		//将整个月份的博客文章转换成pdf文件
		try
		{
			//将pdf文件整合到用户名目录下的pdf子目录下
			File file=new File(filepath);
			file=file.getParentFile();
			fileNameString=file.getPath()+"/pdf/"+fileNameString;
			FileTool.generatePDF(htmlString,fileNameString);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 将html文件转换为pdf文件
		blogInfo.getMonthArticleList().clear();
	}
}
