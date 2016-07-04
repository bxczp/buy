package com.bx.service;

import java.util.List;

import com.bx.entity.Order;
import com.bx.entity.PageBean;

/**
 * @date 2016年3月20日 OrderService.java
 * @author CZP
 * @parameter
 */
public interface OrderService {

	public void saveOrder(Order order);

	public List<Order> findOrder(Order s_order, PageBean pageBean);

	public long orderListCount(Order s_order);

	public void updateOrderStatus(int status, String orderNo);

	public Order getOrderByNo(String orderNo);

	public Order getOrderById(int orderId);

}
