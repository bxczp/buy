package com.bx.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.bx.entity.PageBean;
import com.bx.entity.Tag;
import com.bx.service.TagService;
import com.bx.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月24日 TagAction.java
 * @author CZP
 * @parameter
 */

@Controller
public class TagAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String page;
	@Resource
	private TagService tagService;

	private String rows;
	private List<Tag> tagList;
	private Tag tag;
	private Tag s_tag;
	private String ids;
	private String tagId;

	public String list() {
		JSONObject jsonObject = new JSONObject();
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		tagList = tagService.finaTagList(s_tag, pageBean);
		JSONArray jsonArray = JSONArray.fromObject(tagList);
		long total = tagService.tagListCount(s_tag);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String save() {
		JSONObject jsonObject = new JSONObject();
		tagService.saveTag(tag);
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String delete() {
		String[] id = ids.split(",");
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < id.length; i++) {
			tag = tagService.getTagById(Integer.parseInt(id[i]));
			tagService.delete(tag);
		}
		jsonObject.put("success", true);
		try {
			ResponseUtil.write(ServletActionContext.getResponse(), jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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

	public List<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Tag getS_tag() {
		return s_tag;
	}

	public void setS_tag(Tag s_tag) {
		this.s_tag = s_tag;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

}
