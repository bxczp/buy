package com.bx.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.bx.entity.Order;
import com.bx.entity.OrderProduct;
import com.bx.entity.PageBean;
import com.bx.entity.Product;
import com.bx.entity.ShoppingCart;
import com.bx.entity.ShoppingCartItem;
import com.bx.entity.User;
import com.bx.service.OrderService;
import com.bx.util.DateUtil;
import com.bx.util.NavUtil;
import com.bx.util.ResponseUtil;
import com.bx.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月20日 OrderAction.java
 * @author CZP
 * @parameter
 */

@Controller
public class OrderAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private OrderService orderService;
	private HttpServletRequest req;
	private String mainPage;
	private String navCode;
	private Order s_order;
	private List<Order> orderList;
	private String status;
	private String orderNos;
	private Order order;
	private String orderId;
	private String page;
	private String rows;
	private String orderNo;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.req = request;
	}

	public String findOrder() {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("currentUser");
		if (s_order == null) {
			s_order = new Order();
		}
		s_order.setUser(user);
		orderList = orderService.findOrder(s_order, null);
		navCode = NavUtil.genNavCode("个人中心");
		mainPage = "userCenter/orderList.jsp";
		return "orderList";
	}

	public String confirmReceive() {
		JSONObject jsonObject = new JSONObject();
		orderService.updateOrderStatus(Integer.parseInt(status), orderNo);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String save() throws Exception {
		HttpSession session = req.getSession();
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
		Order order = new Order();
		User currentUser = (User) session.getAttribute("currentUser");
		order.setUser(currentUser);
		order.setCreateTime(new Date());
		order.setOrderNo(DateUtil.getCurrentDateStr());

		order.setStatus(1);
		List<OrderProduct> orderProductList = new ArrayList();
		float total = 0;
		for (ShoppingCartItem item : shoppingCart.getShoppingCartItems()) {
			total += item.getCount() * item.getProduct().getPrice();
			OrderProduct orderProduct = new OrderProduct();
			orderProduct.setProduct(item.getProduct());
			orderProduct.setNum(item.getCount());
			orderProduct.setOrder(order);
			orderProductList.add(orderProduct);
		}
		order.setCost(total);
		order.setOrderProductList(orderProductList);
		orderService.saveOrder(order);
		navCode = NavUtil.genNavCode("购物");
		mainPage = "shopping/shopping-result.jsp";

		// 购物完后，要清除session
		session.removeAttribute("shoppingCart");
		return SUCCESS;
	}

	public String list() {
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		orderList = orderService.findOrder(s_order, pageBean);
		long total = orderService.orderListCount(s_order);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "orderProductList" });
		jsonConfig.registerJsonValueProcessor(User.class,
				new ObjectJsonValueProcessor(new String[] { "id", "userName" }, User.class));
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));

		JSONArray jsonArray = JSONArray.fromObject(orderList, jsonConfig);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String findProductListByOrderId() {
		JSONObject jsonObject = new JSONObject();
		if (StringUtil.isEmpty(orderId)) {
			return null;
		}
		order = orderService.getOrderById(Integer.parseInt(orderId));
		// order=orderService.getOrderByNo(orderNo);
		List<OrderProduct> orderProductList = order.getOrderProductList();
		JSONArray jsonArray = new JSONArray();
		for (OrderProduct op : orderProductList) {
			Product p = op.getProduct();
			JSONObject json = new JSONObject();
			json.put("productName", p.getName());
			json.put("proPic", p.getProPic());
			json.put("price", p.getPrice());
			json.put("num", op.getNum());
			json.put("subtotal", op.getNum() * p.getPrice());
			jsonArray.add(json);
		}
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", jsonArray.size());
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String modifyOrderStatus() {
		String[] no = orderNos.split(",");
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < no.length; i++) {
			orderService.updateOrderStatus(Integer.parseInt(status), no[i]);
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public Order getS_order() {
		return s_order;
	}

	public void setS_order(Order s_order) {
		this.s_order = s_order;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNos() {
		return orderNos;
	}

	public void setOrderNos(String orderNos) {
		this.orderNos = orderNos;
	}

}
