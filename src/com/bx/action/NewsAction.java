package com.bx.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.bx.entity.News;
import com.bx.entity.PageBean;
import com.bx.service.NewsService;
import com.bx.util.NavUtil;
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月19日 NewsAction.java
 * @author CZP
 * @parameter
 */
@Controller
public class NewsAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String newsId;
	private String mainPage;
	private News news;
	@Resource
	private NewsService newsService;
	private String navCode;

	private String page;
	private String ids;
	private String rows;
	private News s_news;
	private List<News> newsList;

	public String list() {
		JsonConfig jsonConfig = new JsonConfig();
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		newsList = newsService.findNewsList(s_news, pageBean);
		long total = newsService.getNewsCount(s_news);
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray = JSONArray.fromObject(newsList, jsonConfig);
		jsonObject.put("total", total);
		jsonObject.put("rows", jsonArray);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String save() {
		if (news.getId() == 0) {
			// 添加操作
			news.setCreateTime(new Date());
		}
		newsService.saveNews(news);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String delete() {
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			news = newsService.getNewsById(Integer.parseInt(id[i]));
			newsService.deleteNews(news);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

	public String showNews() {
		news = newsService.getNewsById(Integer.parseInt(newsId));
		navCode = NavUtil.genNavCode("最新新闻");
		mainPage = "news/newsDetails.jsp";
		return SUCCESS;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public News getS_news() {
		return s_news;
	}

	public void setS_news(News s_news) {
		this.s_news = s_news;
	}

	public List<News> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}

}
