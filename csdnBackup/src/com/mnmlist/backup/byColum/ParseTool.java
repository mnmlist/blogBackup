package com.mnmlist.backup.byColum;

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

/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
public class ParseTool {
	/*
	 * @param url html页面url
	 * 
	 * @param tagNameFilter tagName of a filter
	 *
	 * @param attributeKey 标签名
	 *
	 * @param attributeValue 标签值
	 * 
	 * @return 返回某个结点的子节点,如果标签名为blog_list，直接返回标签名为blog_list对应的所有结点
	 */
	public static NodeList getNodeList(String url, String tagNameFilter,
			String attributeKey, String attributeValue) {
		Parser parser = ParserInstance.getParserInstance(url);
		NodeFilter andFilter = new AndFilter(new TagNameFilter(tagNameFilter),
				new HasAttributeFilter(attributeKey, attributeValue));
		NodeList list = null;
		try {
			list = parser.extractAllNodesThatMatch(andFilter);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList nlist = null;
		if (list.size() > 0)
			if(attributeValue.equals("blog_list"))
			return list;
			nlist = list.elementAt(0).getChildren();
		return nlist;
	}
	/*
	 * @param nlist 文章结点
	 */
	public static void parseTitle(NodeList nlist,BlogInfo blogInfo) {
		Node tit = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++) {
			tit = nlist.elementAt(i);
			if (tit instanceof Span) {
				Span span = (Span) tit;
				if (span.getAttribute("class") != null
						&& span.getAttribute("class").equalsIgnoreCase(
								"link_title")) {
					LinkTag link = (LinkTag) span.childAt(0);
					String title = link.getLinkText();
					/* 将文件名中不允许的字符替换成允许的字符 */
					title = title.trim();
					title = title.replaceAll("[\\?/:*|<>\"]","_");
					blogInfo.setArticleTitle(title);
				}
			} else {
				NodeList slist = tit.getChildren();
				if (slist != null && slist.size() > 0) {
					parseTitle(slist,blogInfo);
				}
			}
		}
	}
	/*
	 * @param nlist 专栏的的首页结点
	 */
	public static void parseColumsTitle(NodeList nlist,BlogInfo blogInfo) {
		Node tit = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++) {
			tit = nlist.elementAt(i);
			if (tit instanceof Span) {
				Span span = (Span) tit;
				Node node = span.childAt(0);
				String title = node.getText();
				/* 将文件名中不允许的字符替换成允许的字符 */
				title = title.trim();
				title = title.replaceAll("[\\?/:*|<>\"]", "_");
				title=title.replaceAll("/", "//");
				blogInfo.setArticleTitle(title);
			} else {
				NodeList slist = tit.getChildren();
				if (slist != null && slist.size() > 0) {
					parseTitle(slist,blogInfo);
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
	public static int parseImg(NodeList nlist, int index,BlogInfo blogInfo) {
		String path=blogInfo.getNewArticlePath();
		Node img = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++) {
			img = nlist.elementAt(i);
			if (img instanceof ImageTag) {
				ImageTag imgtag = (ImageTag) img;
				if (!imgtag.isEndTag()) {
					/* 将图片的URL映射成本地路径 */
					blogInfo.getImageResourceList().add(new Attribute("" + index,
							new String(imgtag.extractImageLocn().getBytes())));
					imgtag.setImageURL(path + "_files/" + index + ".gif");
					/* 递增本地路径序列 */
					index++;
				}
			} else {
				NodeList slist = img.getChildren();
				if (slist != null && slist.size() > 0) {
					index = ParseTool.parseImg(slist, index,blogInfo);
				}
			}
		}
		return index;
	}
	/*
	 * @param nlist HTML每月份存档的子标签链表
	 */
	public static boolean parsePerArticle(NodeList nlist,BlogInfo blogInfo) {
		boolean flag=false;
		int count = nlist.size();
		Node atls=null;
		String string="http://blog.csdn.net";
		for (int i = 0; i < count; i++) {
			atls = nlist.elementAt(i);
			if (atls instanceof LinkTag) {
				LinkTag link = (LinkTag) atls;
				String urlString=link.extractLink();
				String urlText = link.getLinkText().replaceAll("[\\?/:*|<>\"]","_");
				if(urlString.startsWith(string))
				{
					blogInfo.getColumArticleList().add(new Attribute(urlText, urlString));
					flag=true;
					break;
				}
			}else {
				NodeList slist = atls.getChildren();
				if (slist != null && slist.size() > 0) {
					flag=parsePerArticle(slist,blogInfo);
					if(flag)
						break;
				}
			}
		}
		return flag;
	}

	/*
	 * @param nlist HTML分页显示标签的子标签链表
	 */
	public static void parsePage(NodeList nlist,BlogInfo blogInfo) {// from parseMonth
		Node pg = null;
		int count = nlist.size();
		for (int i = 0; i < count; i++) {
			pg = nlist.elementAt(i);
			if (pg instanceof LinkTag) {
				LinkTag lt = (LinkTag) pg;
				if (lt.getLinkText().equalsIgnoreCase("下一页")) {
					String url = "http://blog.csdn.net" + lt.extractLink();
					NodeList titleList = getNodeList(url, "div", "class", "blog_list");
					int size=titleList.size();
					for(int j=0;j<size;j++)
					{
						parsePerArticle(titleList.elementAt(j).getChildren(),blogInfo);
					}
					NodeList fenYeList = getNodeList(url, "div", "class", "page_nav");
					if (fenYeList != null)
						parsePage(fenYeList,blogInfo);
					else
						break;
				}
			}
		}
	}

	/*
	 * @param filepath 本地存档的路径
	 * 
	 * @param url 保存本月存档的网页的URL
	 * 
	 * @param articles 保存本月存档的链表
	 */
	public static void parseColums(String filepath, String url,
			AttributeList articles,BlogInfo blogInfo) {
		NodeList titleList = getNodeList(url, "div", "class", "blog_list");
		int size=titleList.size();
		for(int i=0;i<size;i++)
		{
			parsePerArticle(titleList.elementAt(i).getChildren(),blogInfo);
		}
		
		NodeList fenYeList = getNodeList(url, "div", "class", "page_nav");
		if (fenYeList != null)
			parsePage(fenYeList,blogInfo);
		/* 慢一点，否则会被认为是恶意行为 */
		List<Attribute> li =blogInfo.getColumArticleList().asList();
		for (int i = 0; i < li.size(); i++) {
			String titleName=li.get(i).getName();
			HandleTool.handleHtml(titleName, (String) li.get(i).getValue(),blogInfo);
			try {
				/* 慢一点，否则会被认为是恶意行为 */
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
