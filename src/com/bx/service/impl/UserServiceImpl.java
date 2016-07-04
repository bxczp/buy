package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.PageBean;
import com.bx.entity.User;
import com.bx.service.UserService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月19日 UserServiceImpl.java
 * @author CZP
 * @parameter
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private BaseDAO<User> baseDAO;

	@Override
	public void saveUser(User user) {
		baseDAO.merge(user);
	}

	@Override
	public boolean existUserWithUserName(String userName) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from User where userName ='" + userName + "'");
		long count = baseDAO.count(hql.toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User login(User user) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<Object>();
		hql.append("from User ");
		if (user != null) {
			hql.append(" where userName = ? and password = ?");
			param.add(user.getUserName());
			param.add(user.getPassword());
		}
		if (user.getStatus() == 2) {
			hql.append(" and status = 2");
		}
		return baseDAO.get(hql.toString(), param);
	}

	@Override
	public List<User> findUserList(User user, PageBean pageBean) {
		StringBuffer hql = new StringBuffer();
		hql.append("from User");
		List<Object> param = new LinkedList<>();
		if (user != null) {
			if (StringUtil.isNotEmpty(user.getUserName())) {
				// 这样写 要加
				hql.append(" and userName like '%" + user.getUserName() + "%'");
				// hql.append(" and userName like ?");
				// //不要 加 单引号 占位符 ？本身就是个 字符串
				// param.add("%"+user.getUserName()+"%");
			}
		}
		hql.append(" and status =1");
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
		}
	}

	@Override
	public Long userListCount(User user) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from User ");
		List<Object> param = new LinkedList<>();
		if (user != null) {
			if (StringUtil.isNotEmpty(user.getUserName())) {
				hql.append(" and userName like ?");
				// 不要 加 单引号
				param.add("%" + user.getUserName() + "%");
			}
		}
		hql.append(" and status =1");
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public void deleteUser(User user) {
		baseDAO.delete(user);
	}

	@Override
	public User getUserById(int userId) {
		return baseDAO.get(User.class, userId);
	}

}
