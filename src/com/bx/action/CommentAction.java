package com.bx.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.bx.entity.Comment;
import com.bx.entity.PageBean;
import com.bx.service.CommentService;
import com.bx.util.PageUtil;
import com.bx.util.PropertiseUtil;
import com.bx.util.ResponseUtil;
import com.bx.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @date 2016年3月19日 CommentAction.java
 * @author CZP
 * @parameter
 */

@Controller
public class CommentAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Comment> commentList;
	private String navCode;
	private String page;
	private Comment s_comment;
	private HttpServletRequest req;
	private String pageCode;
	private long total;
	private Comment comment;
	private String rows;
	private String commentId;
	private String replyContent;
	private String ids;

	public String list() {
		if (StringUtil.isEmpty(page)) {
			page = "1";
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiseUtil.getValue("pageSize")));
		commentList = commentService.findCommentList(s_comment, pageBean);
		total = commentService.commentListCount(s_comment);
		StringBuffer param = new StringBuffer();
		param.append("");
		pageCode = PageUtil.genPagination(req.getContextPath() + "/comment_list.action", total, Integer.parseInt(page),
				Integer.parseInt(PropertiseUtil.getValue("pageSize")), param.toString());
		return SUCCESS;
	}

	public String listComment() {
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		long total = commentService.commentListCount(s_comment);
		commentList = commentService.findCommentList(s_comment, pageBean);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
		jsonObject.put("total", total);
		jsonObject.put("rows", jsonArray);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String loadCommentById() {
		comment = commentService.getCommentById(Integer.parseInt(commentId));
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONObject jsonObject = JSONObject.fromObject(comment, jsonConfig);

		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String replay() {
		comment.setReplyTime(new Date());
		commentService.saveComment(comment);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);

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
			comment = commentService.getCommentById(Integer.parseInt(id[i]));
			commentService.deleteComment(comment);
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String preSave() {
		return null;
	}

	public String save() {
		if (comment.getCreateTime() == null) {
			comment.setCreateTime(new Date());
		}
		commentService.saveComment(comment);
		return "commentSave";
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

	public Comment getS_comment() {
		return s_comment;
	}

	public void setS_comment(Comment s_comment) {
		this.s_comment = s_comment;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	@Resource
	private CommentService commentService;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.req = request;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	// public String
}
