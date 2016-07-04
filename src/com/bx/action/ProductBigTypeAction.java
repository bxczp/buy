package com.bx.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.bx.entity.PageBean;
import com.bx.entity.ProductBigType;
import com.bx.entity.ProductSmallType;
import com.bx.service.ProductBigTypeService;
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
public class ProductBigTypeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private ProductBigTypeService productBigTypeService;
	private List<ProductBigType> productBigTypeList;
	private ProductBigType s_productBigType;
	private String page;
	private String rows;
	private String ids;
	private ProductBigType productBigType;
	@Resource
	private ProductSmallTypeService productSmallTypeService;

	public String comboList() {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		List<ProductBigType> productBigTypeList = productBigTypeService.findAllBigTypeList();
		jsonObject.put("id", "");
		jsonObject.put("name", "请选择...");
		jsonArray.add(jsonObject);
		JsonConfig jsonConfig = new JsonConfig();
		// 设置 过滤字符
		jsonConfig.setExcludes(new String[] { "productList", "smallTypeList", "remarks" });
		// 使用 有过滤器的 jsonArray 生成器
		JSONArray rows = JSONArray.fromObject(productBigTypeList, jsonConfig);
		jsonArray.addAll(rows);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String save() {
		JSONObject jsonObject = new JSONObject();
		productBigTypeService.saveProductBigType(productBigType);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String delete() {
		JSONObject jsonObject = new JSONObject();
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			boolean isExistSmallTypeWithbigTypeId = productSmallTypeService
					.existSmallTypWithBigTypeId(Integer.parseInt(id[i]));
			if (isExistSmallTypeWithbigTypeId) {
				jsonObject.put("exist", "该大类下存在小类，不能删除");

			} else {
				productBigType = productBigTypeService.getProductBigTypeById(Integer.parseInt(id[i]));
				productBigTypeService.deleteProductBigType(productBigType);
			}
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String list() {
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		productBigTypeList = productBigTypeService.finfProductBigTypeList(s_productBigType, pageBean);
		long total = productBigTypeService.finfProductBigTypeListCount(s_productBigType);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "productList", "smallTypeList" });
		JSONArray jsonArray = JSONArray.fromObject(productBigTypeList, jsonConfig);
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

	public List<ProductBigType> getProductBigTypeList() {
		return productBigTypeList;
	}

	public void setProductBigTypeList(List<ProductBigType> productBigTypeList) {
		this.productBigTypeList = productBigTypeList;
	}

	public ProductBigType getS_productBigType() {
		return s_productBigType;
	}

	public void setS_productBigType(ProductBigType s_productBigType) {
		this.s_productBigType = s_productBigType;
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public ProductBigType getProductBigType() {
		return productBigType;
	}

	public void setProductBigType(ProductBigType productBigType) {
		this.productBigType = productBigType;
	}

}
