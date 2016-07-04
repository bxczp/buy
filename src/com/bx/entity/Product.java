package com.bx.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @date 2016年3月17日 Product.java
 * @author CZP
 * @parameter
 */

@Entity
@Table(name = "t_product")
public class Product {

	private int id;
	private String name;
	private int price;
	// 库存
	private int stock;
	// 图片的路径名
	private String proPic;
	private String description;
	private int hot;
	private Date hotTime;
	private int specialPrice;
	private Date specialPriceTime;

	// 多个Product 对应 一个smallType
	private ProductBigType bigType;
	// 多个 Product 对应 一个bigtype
	private ProductSmallType smallType;

	// 一个Product 对应 多个 商品订单
	private List<OrderProduct> orderProductList = new ArrayList<>();

	// @JoinColumn 定义的只是本列是外建列，是列名，并没有具体定义和其他表中的哪个列关联
	// 而具体和其他表的哪个列关联 是看 类中的属性名(默认是 关联实体的 主键列)
	@ManyToOne
	@JoinColumn(name = "bigTypeId")
	public ProductBigType getBigType() {
		return bigType;
	}

	public void setBigType(ProductBigType bigType) {
		this.bigType = bigType;
	}

	@ManyToOne
	@JoinColumn(name = "smallTypeId")
	public ProductSmallType getSmallType() {
		return smallType;
	}

	public void setSmallType(ProductSmallType smallType) {
		this.smallType = smallType;
	}

	@Id
	@GeneratedValue(generator = "_native")
	@GenericGenerator(name = "_native", strategy = "native")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getProPic() {
		return proPic;
	}

	public void setProPic(String proPic) {
		this.proPic = proPic;
	}

	@Column(length = 2000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public Date getHotTime() {
		return hotTime;
	}

	public void setHotTime(Date hotTime) {
		this.hotTime = hotTime;
	}

	public int getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(int specialPrice) {
		this.specialPrice = specialPrice;
	}

	public Date getSpecialPriceTime() {
		return specialPriceTime;
	}

	public void setSpecialPriceTime(Date specialPriceTime) {
		this.specialPriceTime = specialPriceTime;
	}

	// 如果一端设置了@JoinColumn，那么它的name和多端的@joinColumn的name是一样的。
	// 但如果设置的是mappedBy的话，那么它的值和多端的getXxxx的"xxxx"是一样的。
	// mappedBy和@JoinColumn还有一个区别是,在一端设置了@JoinColumn的时候，主控制方在一端,
	// 也就是在一端set多端的时候，数据库会为你保存级联关系（在多端指向一端的外键设置值了）
	@OneToMany
	@JoinColumn(name = "productId")
	public List<OrderProduct> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<OrderProduct> orderProductList) {
		this.orderProductList = orderProductList;
	}

}
