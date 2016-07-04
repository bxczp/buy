package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.Order;
import com.bx.entity.PageBean;
import com.bx.service.OrderService;
import com.bx.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * @date 2016年3月20日 OrserServiceImpl.java
 * @author CZP
 * @parameter
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Resource
	private BaseDAO<Order> baseDAO;

	/**
	 * 注意：重要！！！！！ 当保存的对象有关联实体时，使用save 当保存单个对象时 用 merge
	 */

	@Override
	public void saveOrder(Order order) {
		// merge() 出错 只保存关联连实体orderProduct中的userId，
		// baseDAO.merge(order);

		baseDAO.save(order);

	}

	@Override
	public List<Order> findOrder(Order s_order, PageBean pageBean) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from Order ");
		List<Object> param = new LinkedList<>();
		if (s_order != null) {
			if (s_order.getUser() != null && s_order.getUser().getId() != 0) {
				hql.append(" and user.id = ?");
				param.add(s_order.getUser().getId());
			}
			if (s_order.getUser() != null && StringUtil.isNotEmpty(s_order.getUser().getUserName())) {
				hql.append(" and user.userName like ?");
				param.add("%" + s_order.getUser().getUserName() + "%");
			}
			if (StringUtil.isNotEmpty(s_order.getOrderNo())) {
				hql.append(" and orderNo like '%" + s_order.getOrderNo() + "%'");
			}
		}
		hql.append("order by createTime desc");
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
		}
	}

	@Override
	public void updateOrderStatus(int status, String orderNo) {
		List<Object> param = new LinkedList<>();
		StringBuffer hql = new StringBuffer();
		hql.append(" update Order set status = ? where orderNo =?");
		param.add(status);
		param.add(orderNo);
		baseDAO.executeHql(hql.toString(), param);
	}

	@Override
	public long orderListCount(Order s_order) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from Order ");
		List<Object> param = new LinkedList<>();
		if (s_order != null) {
			if (StringUtil.isNotEmpty(s_order.getOrderNo())) {
				hql.append(" and orderNo like '%" + s_order.getOrderNo() + "%'");
			}
			if (s_order.getUser() != null && StringUtil.isNotEmpty(s_order.getUser().getUserName())) {
				hql.append(" and user.userName like ?");
				param.add("%" + s_order.getUser().getUserName() + "%");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public Order getOrderByNo(String orderNo) {
		StringBuffer hql = new StringBuffer();
		hql.append("select * from Order");
		List<Object> param = new LinkedList<>();
		if (StringUtil.isNotEmpty(orderNo)) {
			hql.append(" and orderNo =" + orderNo);
		}
		return baseDAO.get(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public Order getOrderById(int orderId) {
		return baseDAO.get(Order.class, orderId);
	}

}
