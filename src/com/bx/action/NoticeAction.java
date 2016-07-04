package com.bx.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.bx.entity.Notice;
import com.bx.entity.PageBean;
import com.bx.service.NoticeService;
import com.bx.util.NavUtil;
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月19日 NoticeAction.java
 * @author CZP
 * @parameter
 */

@Controller
public class NoticeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private NoticeService noticeService;
	private String noticeId;
	private Notice notice;
	private String mainPage;
	private String navCode;
	private String page;
	private String rows;
	private Notice s_notice;
	private List<Notice> noticeList;
	private String ids;

	public String list() {
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		noticeList = noticeService.findnoticeList(s_notice, pageBean);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray = JSONArray.fromObject(noticeList, jsonConfig);
		long total = noticeService.getNoticeCount(s_notice);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);

		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String delete() {
		JSONObject jsonObject = new JSONObject();
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			notice = noticeService.getNoticeById(Integer.parseInt(id[i]));
			noticeService.deleteNotcie(notice);
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String save() {
		JSONObject jsonObject = new JSONObject();
		if (notice.getId() == 0) {
			// 添加操作
			notice.setCreateTime(new Date());
		}
		noticeService.saveNotice(notice);
		jsonObject.put("success", true);

		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

	public String showNotice() {
		notice = noticeService.getNoticeById(Integer.parseInt(noticeId));
		mainPage = "notice/noticeDetails.jsp";
		navCode = NavUtil.genNavCode("最新公告");
		return SUCCESS;
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

	public Notice getS_notice() {
		return s_notice;
	}

	public void setS_notice(Notice s_notice) {
		this.s_notice = s_notice;
	}

	public List<Notice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
