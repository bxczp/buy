package com.bx.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @date 2016年3月17日 ProductBigType.java
 * @author CZP
 * @parameter
 */
@Entity
@Table(name = "t_bigType")
public class ProductBigType {

	private int id;
	private String name;
	private String remarks;
	// 多个 Product 对应 一个 bigType
	private List<Product> productList = new ArrayList<>();
	// 多个 smallType对应 一个 bigType
	private List<ProductSmallType> smallTypeList = new ArrayList<>();

	@OneToMany(mappedBy = "bigType", targetEntity = Product.class)
	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	@OneToMany(targetEntity = ProductSmallType.class, mappedBy = "bigType", fetch = FetchType.EAGER)
	public List<ProductSmallType> getSmallTypeList() {
		return smallTypeList;
	}

	public void setSmallTypeList(List<ProductSmallType> smallTypeList) {
		this.smallTypeList = smallTypeList;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
