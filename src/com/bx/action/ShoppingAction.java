package com.bx.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.bx.entity.Product;
import com.bx.entity.ShoppingCart;
import com.bx.entity.ShoppingCartItem;
import com.bx.entity.User;
import com.bx.service.ProductService;
import com.bx.util.NavUtil;
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**
 * @date 2016年3月19日 shoppingAction.java
 * @author CZP
 * @parameter
 */
@Controller
public class ShoppingAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;
	private String productId;
	@Resource
	private ProductService productService;
	private String mainPage;
	private String navCode;
	// 商品数目
	private int count;

	/**
	 * 
	 * @return
	 */
	public String addShoppingCartItem() {
		JSONObject jsonObject = new JSONObject();
		HttpSession session = req.getSession();
		Product product = productService.getProductById(Integer.parseInt(productId));
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new ShoppingCart();
			User currentUser = (User) session.getAttribute("currentUser");
			if (currentUser != null) {
				shoppingCart.setUserId(currentUser.getId());
			}
		}
		// 原购物车中有 相同的商品条目（ShoppingCartItem） 数量加一
		List<ShoppingCartItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
		boolean flag = true;
		for (ShoppingCartItem s : shoppingCartItems) {
			if (s.getProduct().getId() == product.getId()) {
				s.setCount(s.getCount() + 1);
				flag = false;
				break;
			}
		}

		// 原购物车里 没有该商品条目（ShoppingCartItem） 新建一个并设数量为1
		ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
		if (flag) {
			shoppingCartItem.setCount(1);
			shoppingCartItem.setProduct(product);
			shoppingCartItems.add(shoppingCartItem);
		}
		session.setAttribute("shoppingCart", shoppingCart);

		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return
	 */
	public String updateShoppingItem() {
		JSONObject jsonObject = new JSONObject();
		HttpSession session = req.getSession();
		Product product = productService.getProductById(Integer.parseInt(productId));

		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
		List<ShoppingCartItem> shoppingCartItems = shoppingCart.getShoppingCartItems();

		for (ShoppingCartItem s : shoppingCartItems) {
			if (s.getProduct().getId() == product.getId()) {
				s.setCount(count);
				break;
			}
		}

		session.setAttribute("shoppingcart", shoppingCart);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String deleteShoppingCartItem() {
		HttpSession session = req.getSession();
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
		List<ShoppingCartItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
		for (int i = 0; i < shoppingCartItems.size(); i++) {
			if (shoppingCartItems.get(i).getProduct().getId() == Integer.parseInt(productId)) {
				shoppingCartItems.remove(i);
				break;
			}
		}
		session.setAttribute("shoppingcart", shoppingCart);
		return "list";
	}

	public String buy() {
		JSONObject jsonObject = new JSONObject();
		HttpSession session = req.getSession();
		Product product = productService.getProductById(Integer.parseInt(productId));
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new ShoppingCart();
			User currentUser = (User) session.getAttribute("currentUser");
			if (currentUser != null) {
				shoppingCart.setUserId(currentUser.getId());
			}
		}

		List<ShoppingCartItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
		boolean flag = true;
		for (ShoppingCartItem s : shoppingCartItems) {
			if (s.getProduct().getId() == product.getId()) {
				s.setCount(s.getCount() + 1);
				flag = false;
				break;
			}
		}

		ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
		if (flag) {
			shoppingCartItem.setCount(1);
			shoppingCartItem.setProduct(product);
			shoppingCartItems.add(shoppingCartItem);
		}

		session.setAttribute("shoppingCart", shoppingCart);

		mainPage = "shopping/shopping.jsp";
		navCode = NavUtil.genNavCode("购物车");
		return SUCCESS;
	}

	public String list() {
		mainPage = "shopping/shopping.jsp";
		navCode = NavUtil.genNavCode("购物车");
		return SUCCESS;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
