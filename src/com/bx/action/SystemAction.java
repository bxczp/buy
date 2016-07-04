package com.bx.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.stereotype.Controller;

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
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**
 * @date 2016年3月24日 SystemAction.java
 * @author CZP
 * @parameter
 */
@Controller
// ServletRequestAware为耦合 依赖注入，获取的是原生的HttpServletRequest类型的request
// public class UserAction extends ActionSupport implements
// ServletRequestAware,ServletResponseAware{
// AppliactionAware为非耦合 依赖注入 获取的值是Map类型的，只能对其 进行存取值的操作
public class SystemAction extends ActionSupport implements ApplicationAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private ProductBigTypeService productBigTypeService;
	@Resource
	private TagService tagService;
	@Resource
	private NewsService newsService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private ProductService productService;

	// 这是Map类型的web应用的application
	private Map<String, Object> application;

	public String refreshSystem() {
		// ServletActionContext;
		// ActionContext;
		JSONObject jsonObject = new JSONObject();
		List<ProductBigType> productBigTypeList = productBigTypeService.findAllBigTypeList();
		// 由于 是 fetch属性，查询 bigType时也查询了 smallType，所有其中包含了smallType
		application.put("bigTypeList", productBigTypeList);

		List<Tag> tagList = tagService.finaTagList(null, null);
		application.put("tagList", tagList);

		List<News> newsList = newsService.findNewsList(null,
				new PageBean(1, Integer.parseInt(PropertiseUtil.getValue("pageSize"))));
		application.put("newsList", newsList);

		List<Notice> noticeList = noticeService.findnoticeList(null,
				new PageBean(1, Integer.parseInt(PropertiseUtil.getValue("pageSize"))));
		application.put("noticeList", noticeList);

		Product s_product = new Product();
		s_product.setHot(1);
		List<Product> hotProductList = productService.findProductList(s_product, new PageBean(1, 6));
		application.put("hotProductList", hotProductList);

		s_product.setHot(0);
		s_product.setSpecialPrice(1);
		List<Product> specialProductList = productService.findProductList(s_product, new PageBean(1, 8));
		application.put("specialPriceProductList", specialProductList);
		jsonObject.put("success", true);

		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setApplication(Map<String, Object> app) {
		this.application = app;
	}

}
