package com.bx.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
 * @date 2016年3月17日 ProductSmallType.java
 * @author CZP
 * @parameter
 */
@Entity
@Table(name = "t_smallType")
public class ProductSmallType {

	private int id;
	private String name;
	private String remarks;
	// 多个 smallType对应 一个 bigType
	private ProductBigType bigType;
	// 对个 Product对应一个smallType
	private List<Product> productList = new ArrayList<Product>();

	@ManyToOne(targetEntity = ProductBigType.class, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "bigTypeId")
	public ProductBigType getBigType() {
		return bigType;
	}

	public void setBigType(ProductBigType bigType) {
		this.bigType = bigType;
	}

	// 通常 在 一端 加上 mappedBy属性 指向的是其他类 中的属性，不是 列名
	@OneToMany(mappedBy = "smallType")
	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	@Id
	@GeneratedValue(generator = "_native")
	@GenericGenerator(strategy = "native", name = "_native")
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

	@Column(length = 1000)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
