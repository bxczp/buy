package com.bx.action;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Controller;

import com.bx.entity.PageBean;
import com.bx.entity.Product;
import com.bx.entity.ProductBigType;
import com.bx.entity.ProductSmallType;
import com.bx.service.ProductService;
import com.bx.util.DateUtil;
import com.bx.util.NavUtil;
import com.bx.util.PageUtil;
import com.bx.util.PropertiseUtil;
import com.bx.util.ResponseUtil;
import com.bx.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月18日 ProductAction.java
 * @author CZP
 * @parameter
 */

// @Controller对应表现层的Bean，也就是Action
@Controller
public class ProductAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private ProductService productService;

	private HttpServletRequest req;
	private List<Product> productList;
	private Product s_product;
	private String page;
	private Long total;
	private String pageCode;
	private String mainPage;
	private String navCode;
	private String productId;
	private Product product;
	private String rows;
	private String ids;

	// 上传的图片文件(变量名 与 name 相同)
	private File proPic;
	// 上传的图片的文件名
	private String proPicFileName;

	public String showProduct() {
		if (StringUtil.isNotEmpty(productId)) {
			product = productService.getProductById(Integer.parseInt(productId));
			this.saveCurrentBrowse(product);
		}
		navCode = NavUtil.genNavCode("商品详情");
		mainPage = "product/productDetails.jsp";
		return SUCCESS;
	}

	private void saveCurrentBrowse(Product product) {
		HttpSession session = req.getSession();
		List<Product> currentProducts = (List<Product>) session.getAttribute("currentBrowse");
		if (currentProducts == null) {
			currentProducts = new LinkedList<>();
		}

		boolean flag = true;
		for (Product p : currentProducts) {
			if (product.getId() == p.getId()) {
				flag = false;
				break;
			}
		}
		if (flag) {
			// 指定添加的位置
			currentProducts.add(0, product);
		}
		if (currentProducts.size() == 5) {
			currentProducts.remove(4);
		}
		session.setAttribute("currentBrowse", currentProducts);
	}

	@Override
	public String execute() throws Exception {
		if (StringUtil.isEmpty(page)) {
			page = "1";
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiseUtil.getValue("pageSize")));
		productList = productService.findProductList(s_product, pageBean);
		total = productService.productListCount(s_product);
		// 请求后的参数
		StringBuffer param = new StringBuffer();
		// 注意参数 与 = 之间不要加空格 不然会被当做空值
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				param.append("s_product.bigType.id=" + s_product.getBigType().getId());
			}
			if (s_product.getSmallType() != null) {
				param.append("s_product.smallType.id=" + s_product.getSmallType().getId());
			}
			if (StringUtil.isNotEmpty(s_product.getName())) {
				param.append("s_product.name=" + s_product.getName());
			}
		}
		pageCode = PageUtil.genPagination(req.getContextPath() + "/product.action", total, Integer.parseInt(page),
				Integer.parseInt(PropertiseUtil.getValue("pageSize")), param.toString());
		navCode = NavUtil.genNavCode("商品列表");
		mainPage = "product/productList.jsp";
		return super.execute();
	}

	public String list() {
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "orderProductList" });
		// JsonValue处理类
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(ProductBigType.class,
				new ObjectJsonValueProcessor(new String[] { "id", "name" }, ProductBigType.class));
		jsonConfig.registerJsonValueProcessor(ProductSmallType.class,
				new ObjectJsonValueProcessor(new String[] { "id", "name" }, ProductSmallType.class));
		productList = productService.findProductList(s_product, pageBean);
		JSONArray jsonArray = JSONArray.fromObject(productList, jsonConfig);
		total = productService.productListCount(s_product);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String save() throws Exception {
		if (proPic == null) {
			// 没有 上传图片
			if (StringUtil.isEmpty(product.getProPic())) {
				product.setProPic("");
			}
		} else {
			// 上传 图片的 名字
			String ImageName = DateUtil.getCurrentDateStr();
			// 获取 存放图片的绝对路径
			String realPath = ServletActionContext.getServletContext().getRealPath("/images/product");
			// 上传 图片的文件名 有后缀
			// 注意 要 加 转义字符 \ 是 \\. !!
			String imageFile = ImageName + proPicFileName.split("\\.")[1];
			File saveFile = new File(realPath, imageFile);
			// Struts 自带的文件上传 第一个参数是 的上传文件 第二个是 要保存的文件
			// 从 缓存 copy 到 目标地址
			FileUtil.copyFile(proPic, saveFile);
			// 写入数据库的 内容
			// 注意 images前 没有 斜杠！！！
			product.setProPic("images/product/" + imageFile);
		}

		JSONObject jsonObject = new JSONObject();
		productService.saveProduct(product);
		jsonObject.put("success", true);
		ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		return null;
	}

	public String delete() {
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			product = productService.getProductById(Integer.parseInt(id[i]));
			productService.deleteProduct(product);
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

	public String setProductWithHot() {
		JSONObject jsonObject = new JSONObject();
		// 分隔 ， 不需要转义字符
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			productService.setProductWithHot(Integer.parseInt(id[i]));
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String setProductWithSpecialPrice() {
		JSONObject jsonObject = new JSONObject();
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			productService.setProductWithSpecialPrice(Integer.parseInt(id[i]));
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Product getS_product() {
		return s_product;
	}

	public void setS_product(Product s_product) {
		this.s_product = s_product;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.req = request;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public File getProPic() {
		return proPic;
	}

	public void setProPic(File proPic) {
		this.proPic = proPic;
	}

	public String getProPicFileName() {
		return proPicFileName;
	}

	public void setProPicFileName(String proPicFileName) {
		this.proPicFileName = proPicFileName;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
