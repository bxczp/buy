package com.bx.entity;

/**
 * @date 2016年3月17日 ShoppingCartItem.java
 * @author CZP
 * @parameter
 */
// 购物车 里的条目类
public class ShoppingCartItem {

	private int id;
	private Product product;
	private int count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
