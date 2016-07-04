package com.bx.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2016年3月19日 ShoppingCart.java
 * @author CZP
 * @parameter
 */

// 购物车类
public class ShoppingCart {

	private int userId;
	private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}

	public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}

}
