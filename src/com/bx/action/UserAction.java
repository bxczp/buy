package com.bx.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.bx.entity.PageBean;
import com.bx.entity.User;
import com.bx.service.UserService;
import com.bx.util.NavUtil;
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月19日 UserAction.java
 * @author CZP
 * @parameter
 */
@Controller
public class UserAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private UserService userService;

	private String mainPage;
	private User user;
	private HttpServletRequest req;
	private String error;
	private String imageCode;
	private String navCode;
	private String userId;
	private User s_user;
	private String page;
	// easyui发过来的每页的记录数
	private String rows;
	private String ids;

	public String existUserWithUserName() {
		boolean exist = userService.existUserWithUserName(user.getUserName());
		JSONObject jsonObject = new JSONObject();
		if (exist) {
			jsonObject.put("exist", true);
		} else {
			jsonObject.put("exist", false);
		}
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String register() {
		userService.saveUser(user);
		return "register_success";
	}

	public String userCenter() {
		navCode = NavUtil.genNavCode("个人中心");
		mainPage = "userCenter/ucDefault.jsp";
		return "userCenter";
	}

	public String getUserInfo() {
		navCode = NavUtil.genNavCode("个人中心");
		mainPage = "userCenter/userInfo.jsp";
		return "userCenter";
	}

	public String preSave() {
		HttpSession session = req.getSession();
		navCode = NavUtil.genNavCode("修改个人信息");
		user = (User) session.getAttribute("currentUser");
		mainPage = "userCenter/userSave.jsp";
		return "userCenter";
	}

	public String save() {
		// （保存）更新 单个实体 用merge() 有关联实体的话 用save
		userService.saveUser(user);
		navCode = NavUtil.genNavCode("更新个人信息");
		mainPage = "userCenter/userInfo.jsp";
		HttpSession session = req.getSession();
		session.setAttribute("currentUser", user);
		// 有问题 更新 用户时 ，会把原来 与该用户 关联的所有 的order表中的userId设为null
		// 解决方法 ：设置 order表中的userId(外建列) 字段 不可更新！！！（updatable=false）
		return "userCenter";
	}

	public String login() {
		HttpSession session = req.getSession();
		String sRand = (String) session.getAttribute("sRand");
		if (sRand != null) {
			if (sRand.equals(imageCode)) {
				User currentUser = userService.login(user);
				if (currentUser != null) {
					session.setAttribute("currentUser", currentUser);
				} else {
					error = "用户名或密码错误";
					if (user.getStatus() == 2) {
						return "adminError";
					}
					return ERROR;
				}
			} else {
				error = "验证码错误";
				if (user.getStatus() == 2) {
					return "adminError";
				}
				return ERROR;
			}
			if (user.getStatus() == 2) {
				return "adminLogin";
			}
			return "login";
		}
		return ERROR;
	}

	public String list() {
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		List<User> userList = userService.findUserList(s_user, pageBean);
		long total = userService.userListCount(s_user);
		// json配置项
		JsonConfig jsonConfig = new JsonConfig();
		// 过滤orderList项
		jsonConfig.setExcludes(new String[] { "orderList" });
		// 注册jsonConfig的处理类
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray result = JSONArray.fromObject(userList, jsonConfig);
		// 这里的rows是发到前台的数据rows，不是记录数
		jsonObject.put("rows", result);
		jsonObject.put("total", total);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String logout() {
		HttpSession session = req.getSession();
		session.removeAttribute("currentUser");
		session.invalidate();
		return "logout";
	}

	public String logout2() {
		HttpSession session = req.getSession();
		session.removeAttribute("currentUser");
		session.invalidate();
		return "logout2";
	}

	public String modifyPassword() {
		JSONObject jsonObject = new JSONObject();
		User newUser = userService.getUserById(user.getId());
		newUser.setPassword(user.getPassword());
		userService.saveUser(newUser);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String deleteUser() {
		JSONObject jsonObject = new JSONObject();
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			User u = userService.getUserById(Integer.parseInt(idsStr[i]));
			userService.deleteUser(u);
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String saveUser() {
		JSONObject jsonObject = new JSONObject();
		userService.saveUser(user);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.req = request;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public User getS_user() {
		return s_user;
	}

	public void setS_user(User s_user) {
		this.s_user = s_user;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
