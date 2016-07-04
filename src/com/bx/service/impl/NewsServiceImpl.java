package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.News;
import com.bx.entity.Notice;
import com.bx.entity.PageBean;
import com.bx.service.NewsService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月18日 NewsServiceImpl.java
 * @author CZP
 * @parameter
 */
// @Service对应的是业务层Bean
@Service("newsService")
public class NewsServiceImpl implements NewsService {

	@Resource
	private BaseDAO<News> baseDAO;

	@Override
	public List<News> findNewsList(News s_news, PageBean pageBean) {
		List<Object> param = new LinkedList<>();
		StringBuffer hql = new StringBuffer();
		hql.append("from News ");
		if (s_news != null) {
			if (StringUtil.isNotEmpty(s_news.getTitle())) {
				hql.append(" and  title like ?");
				param.add("%" + s_news.getTitle() + "%");
			}
		}
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
		}
	}

	@Override
	// get方法的主键值类型 要和 实体类中的 主键属性类型 相同
	public News getNewsById(int newsId) {
		return baseDAO.get(News.class, newsId);
	}

	@Override
	public long getNewsCount(News s_news) {
		List<Object> param = new LinkedList<>();
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from News ");
		if (s_news != null) {
			if (StringUtil.isNotEmpty(s_news.getTitle())) {
				hql.append(" and  title like ?");
				param.add("%" + s_news.getTitle() + "%");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public void saveNews(News news) {
		baseDAO.merge(news);
	}

	@Override
	public void deleteNews(News news) {
		baseDAO.delete(news);
	}

}
