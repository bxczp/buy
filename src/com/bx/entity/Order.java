package com.bx.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

/**
 * @date 2016年3月17日 Order.java
 * @author CZP
 * @parameter
 */

@Entity
@Table(name = "t_order")
public class Order {

	private int id;
	private String orderNo;
	private Date createTime;
	// 总金额
	private float cost;
	// 订单状态
	// 1 待审核
	// 2 审核通过
	// 3 卖家已发货
	// 4 已收货
	private int status;
	// 一个Order 对应 一个User
	// 是双向关联
	private User user;

	// 一个 Order 对应 多个OrderProduct
	// 双向关联
	private List<OrderProduct> orderProductList = new ArrayList<>();

	@Id
	@GeneratedValue(generator = "_native")
	@GenericGenerator(name = "_native", strategy = "native")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "userId", updatable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(fetch = FetchType.EAGER)
	// 是 hibernate的Cascade
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "orderId")
	public List<OrderProduct> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<OrderProduct> orderProductList) {
		this.orderProductList = orderProductList;
	}

}
