package com.bx.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.bx.entity.PageBean;
import com.bx.entity.ProductBigType;
import com.bx.entity.ProductSmallType;
import com.bx.service.ProductService;
import com.bx.service.ProductSmallTypeService;
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月21日 ProductBigType.java
 * @author CZP
 * @parameter
 */
@Controller
public class ProductSmallTypeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String page;
	private String rows;
	private List<ProductSmallType> productSmallTypeList;
	@Resource
	private ProductSmallTypeService productSmallTypeService;
	private ProductSmallType s_productSmallType;
	private String ids;
	@Resource
	private ProductService productService;
	private ProductSmallType productSmallType;

	public String comboList() {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		List<ProductSmallType> productSmallTypeList = productSmallTypeService.getListByBigTypeId(s_productSmallType,
				null);
		jsonObject.put("id", "");
		jsonObject.put("name", "请选择...");
		jsonArray.add(jsonObject);
		JsonConfig jsonConfig = new JsonConfig();
		// 设置 过滤字符
		jsonConfig.setExcludes(new String[] { "productList", "bigType", "remarks" });
		// 使用 有过滤器的 jsonArray 生成器
		JSONArray rows = JSONArray.fromObject(productSmallTypeList, jsonConfig);
		jsonArray.addAll(rows);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String list() {
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		productSmallTypeList = productSmallTypeService.getListByBigTypeId(s_productSmallType, pageBean);
		long total = productSmallTypeService.finfProductSmallTypeListCount(s_productSmallType);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "productList" });
		jsonConfig.registerJsonValueProcessor(ProductBigType.class,
				new ObjectJsonValueProcessor(new String[] { "id", "name" }, ProductBigType.class));
		JSONArray jsonArray = JSONArray.fromObject(productSmallTypeList, jsonConfig);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String delete() throws Exception {
		JSONObject jsonObject = new JSONObject();
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			if (productService.existProductWithSmallTypeId(Integer.parseInt(id[i]))) {
				jsonObject.put("exist", "该小类下存在商品，不能删除");
			} else {
				productSmallType = productSmallTypeService.getProductSmallTypeById(Integer.parseInt(id[i]));
				productSmallTypeService.deleteProductSmallType(productSmallType);
			}
		}
		jsonObject.put("success", true);
		ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		return null;
	}

	public String save() {
		JSONObject jsonObject = new JSONObject();
		productSmallTypeService.savePeoductSmallType(productSmallType);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ProductSmallType getS_productSmallType() {
		return s_productSmallType;
	}

	public void setS_productSmallType(ProductSmallType s_productSmallType) {
		this.s_productSmallType = s_productSmallType;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public List<ProductSmallType> getProductSmallTypeList() {
		return productSmallTypeList;
	}

	public void setProductSmallTypeList(List<ProductSmallType> productSmallTypeList) {
		this.productSmallTypeList = productSmallTypeList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public ProductSmallType getProductSmallType() {
		return productSmallType;
	}

	public void setProductSmallType(ProductSmallType productSmallType) {
		this.productSmallType = productSmallType;
	}

}
