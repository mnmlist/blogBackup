package com.mnmlist.backup.byTag;

import java.io.File;
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

import com.mnmlist.backup.lib.*;
/**
 * @author mnmlist@163.com
 * @blog http://blog.csdn.net/mnmlist
 * @version v1.0
 */
/**
 * @author Sting
 *
 */
/**
 * @author Sting
 *
 */
/**
 * @author Sting
 *
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
	 * @return 返回某个结点的子节点,如果标签名为article_title，直接返回标签名为article_title对应的所有结点
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
			e.printStackTrace();
		}
		NodeList nlist = null;
		//System.out.println(list.size());
		if (list.size() > 0)
		{
			if (attributeValue.equals("article_title"))
				return list;
			nlist = list.elementAt(0).getChildren();
		}
		return nlist;
	}
	
	
	/**
	 * @param url html页面url
	 * @param tagNameFilter tagName of a filter
	 * @param attributeKey 标签名
	 * @param attributeValue 标签值
	 * @return 返回文章标签下面的子节点
	 * 用来获取tagName对应的网址,由于文章标签和专栏使用的是<div id="panel_Category">,所以会
	 * 获得不止一个元素，这里需要特别留意一下
	 */
	public static NodeList getNodeLists(String url, String tagNameFilter,
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
			e.printStackTrace();
		}
		NodeList nlist = null;
		if (list.size() > 0)
		{
			nlist = choooseOneNodeList(list);//用来获取tagName对应的网址,由于文章标签和专栏使用的是<div id="panel_Category">,所以会
			//获得不止一个元素，这里需要特别留意一下
		}
		return nlist;
	}
	
	/**
	 * @param list 可能包括专栏和博客分类两种节点，所以还需要做选择
	 * @return 某个博客分类所在的网址
	 */
	public static NodeList choooseOneNodeList(NodeList list)
	{
		NodeList nlist = null;
		int listSize=list.size();
		for(int i=0;i<listSize;i++)
		{
			Node node=list.elementAt(i);
			NodeList childrenList=node.getChildren();
			//可以通过node.getChildren()获取node的所有子节点
			//然后再用toString()打印出所有的子节点，然后用下面的方法获得相应的节点的值
			String string=childrenList.elementAt(1).getFirstChild().getFirstChild().toPlainTextString();
			if(string.equals("文章分类"))//过滤掉博客专栏
				return node.getChildren();
		}
		return nlist;
	}
	/*
	 * @param urlString 某博客的url
	 * 
	 * @param tagString 博客文章的某个类别
	 * 
	 * @return 某类别对应的url
	 */
	public static String getUrlFromTag(String urlString, String tagString,
			BlogInfo blogInfo)
	{
		NodeList nList = ParseTool.getNodeLists(urlString, "div", "id",
				"panel_Category");
		NodeFilter andFilter = new AndFilter(new TagNameFilter("ul"),
				new HasAttributeFilter("class", "panel_body"));
		nList = nList.extractAllNodesThatMatch(andFilter);
		nList = nList.elementAt(0).getChildren();
		boolean flag = ParseTool.parseUrl(nList, tagString, blogInfo);
		if (flag)
			return blogInfo.getTagCorrespondedURL();
		return null;
	}

	/*
	 * @param nlist 博客目录的子标签链表
	 * 
	 * @param tag 博客文章的某个类别
	 */
	public static boolean parseUrl(NodeList nList, String tag, BlogInfo blogInfo)
	{
		int size = nList.size();
		boolean flag = false;
		for (int j = 0; j < size; j++)
		{
			Node atls = nList.elementAt(j);
			if (atls instanceof LinkTag)
			{
				LinkTag link = (LinkTag) atls;
				if (link.getLinkText().equals(tag))
				{
					blogInfo.setTagCorrespondedURL(link.extractLink());// 获得tag对应的url
					flag = true;
					break;
				}
			} else
			{
				NodeList slist = atls.getChildren();
				if (slist != null && slist.size() > 0)
				{
					flag = parseUrl(slist, tag, blogInfo);
					if (flag)
						break;
				}
			}
		}
		return flag;
	}

	/*
	 * @param nlist HTML正文的子标签链表
	 * 
	 * @param index 用于索引图片的个数以及当前的图片数
	 * 
	 * @return 当前的图片数
	 */
	public static int parseImg(NodeList nlist, int index,
			final BlogInfo blogInfo)
	{
		String path = blogInfo.getNewArticlePath();
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
					imgtag.setImageURL(path + "_files/" + index + ".gif");
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
	 * @param nlist HTML每月份存档的子标签链表
	 * 
	 * @param index 无用
	 * 
	 * @return 无用
	 */
	public static void parsePerArticle(NodeList nlist, BlogInfo blogInfo)
	{
		Node atl = null;
		int count = nlist.size();
		for (int i = 1; i < count; i += 2)
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
					String urlDescripString = link.getLinkText().trim()
							.replaceAll("[\\?/:*|<>\"]", "_");
					blogInfo.getColumArticleList()
							.add(new Attribute(urlDescripString,
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
	 * 
	 * @param index 无用
	 * 
	 * @return 无用
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
	 * @param filepath 本地存档的路径
	 * 
	 * @param url 保存本月存档的网页的URL
	 * 
	 * @param articles 保存本月存档的链表
	 * 
	 * @return 无
	 */
	public static void parseColums(String filepath, String url,
			AttributeList articles, BlogInfo blogInfo)
	{
		NodeList titleList = getNodeList(url, "div", "id", "article_list");// list
		int size = titleList.size();
		for (int i = 1; i < size; i += 2)
		{
			NodeList sList = titleList.elementAt(i).getChildren();
			sList = sList.elementAt(1).getChildren();// article title的子节点
			parsePerArticle(sList, blogInfo);
		}
		NodeList fenYeList = getNodeList(url, "div", "id", "papelist");
		if (fenYeList != null)
			parsePage(fenYeList, blogInfo);
		StringBuilder sb = new StringBuilder();
		/* 加入meta信息 */
		sb.append("<html>\r\n");
		sb.append("<head><title>" + filepath.replace('/', ' ')
				+ "</title></head>\r\n<body <font face=\"microsoft yahei\"> >\r\n");
		String htmlString=null;
		/* 慢一点，否则会被认为是恶意行为 */
		List<Attribute> li = blogInfo.getColumArticleList().asList();
		for (int i = 0; i < li.size(); i++)
		{
			String titleName = li.get(i).getName();
			htmlString=HandleTool.handleHtml(titleName, (String) li.get(i).getValue(),
					blogInfo);
			sb.append(htmlString);
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
		htmlString = sb.toString();
		//将整个月份的文章保存于.html文件中
		FileTool.writeFile(filepath+"/"+fileNameString + ".html", htmlString.getBytes());
		//将整个月份的博客文章转换成pdf文件
		try
		{
			//将pdf文件整合到用户名目录下
			File file=new File(filepath);
			file=file.getParentFile();
			//FileTool.makeDir(file.getPath()+"/pdf");
			fileNameString=file.getPath()+"/"+fileNameString;
			FileTool.generatePDF(htmlString,fileNameString);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
