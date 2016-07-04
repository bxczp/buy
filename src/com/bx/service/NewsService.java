package com.bx.service;

import java.util.List;

import com.bx.entity.News;
import com.bx.entity.Notice;
import com.bx.entity.PageBean;

/**
 * @date 2016年3月18日 NewsService.java
 * @author CZP
 * @parameter
 */
public interface NewsService {

	public List<News> findNewsList(News s_news, PageBean pageBean);

	// get方法的主键值类型 要和 实体类中的 主键属性类型 相同
	public News getNewsById(int newsId);

	public long getNewsCount(News s_news);

	public void saveNews(News news);

	public void deleteNews(News news);

}
