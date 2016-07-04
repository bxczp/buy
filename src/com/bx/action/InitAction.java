package com.bx.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bx.entity.News;
import com.bx.entity.Notice;
import com.bx.entity.PageBean;
import com.bx.entity.Product;
import com.bx.entity.ProductBigType;
import com.bx.entity.Tag;
import com.bx.service.NewsService;
import com.bx.service.NoticeService;
import com.bx.service.ProductBigTypeService;
import com.bx.service.ProductService;
import com.bx.service.TagService;
import com.bx.util.PropertiseUtil;

/**
 * @date 2016年3月18日 InintAction.java
 * @author CZP
 * @parameter
 */
// spring注解 组件的意思
// 是所有受Spring 管理组件的通用形式（通常 分有 service controller Repository
// 不能分的时候用component），@Component注解可以放在类的头上
@Component
public class InitAction implements ServletContextListener, ApplicationContextAware {

	// 实现ApplicationContextAware 目的是为注入 spring的application
	private static ApplicationContext applicationContent;

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

	// 实现ServletContextListener，目的是为spring容器初始化时注入必要的bean
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// 把一些 必要的内容放入application中
		// 此处的application是web的application
		ServletContext application = servletContextEvent.getServletContext();

		ProductBigTypeService productBigTypeService = (ProductBigTypeService) applicationContent
				.getBean("productBigTypeService");
		List<ProductBigType> productBigTypeList = productBigTypeService.findAllBigTypeList();
		// 由于 是 fetch属性，查询 bigType时也查询了 smallType，所有其中包含了smallType
		application.setAttribute("bigTypeList", productBigTypeList);

		TagService tagService = (TagService) applicationContent.getBean("tagService");
		List<Tag> tagList = tagService.finaTagList(null, null);
		application.setAttribute("tagList", tagList);

		NewsService newsService = (NewsService) applicationContent.getBean("newsService");
		List<News> newsList = newsService.findNewsList(null,
				new PageBean(1, Integer.parseInt(PropertiseUtil.getValue("pageSize"))));
		application.setAttribute("newsList", newsList);

		NoticeService noticeService = (NoticeService) applicationContent.getBean("noticeService");
		List<Notice> noticeList = noticeService.findnoticeList(null,
				new PageBean(1, Integer.parseInt(PropertiseUtil.getValue("pageSize"))));
		application.setAttribute("noticeList", noticeList);

		Product s_product = new Product();
		s_product.setHot(1);
		ProductService productService = (ProductService) applicationContent.getBean("productService");
		List<Product> hotProductList = productService.findProductList(s_product, new PageBean(1, 6));
		application.setAttribute("hotProductList", hotProductList);

		s_product.setHot(0);
		s_product.setSpecialPrice(1);
		List<Product> specialProductList = productService.findProductList(s_product, new PageBean(1, 8));
		application.setAttribute("specialPriceProductList", specialProductList);

	}

	// 使用static 修饰applicationContent
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 这是 Spring容器中的 application
		this.applicationContent = applicationContext;
	}

}
