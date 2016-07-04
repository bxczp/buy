package com.bx.service;

import java.util.List;

import com.bx.entity.PageBean;
import com.bx.entity.User;

/**
 * @date 2016年3月19日 UserService.java
 * @author CZP
 * @parameter
 */
public interface UserService {

	public void saveUser(User user);

	public boolean existUserWithUserName(String userName);

	public User login(User user);

	public List<User> findUserList(User user, PageBean pageBean);

	public Long userListCount(User user);

	public void deleteUser(User user);

	public User getUserById(int userId);
}
