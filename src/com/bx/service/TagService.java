package com.bx.service;

import java.util.List;

import com.bx.entity.PageBean;
import com.bx.entity.Tag;

import sun.net.www.content.text.plain;

/**
 * @date 2016年3月18日 TagService.java
 * @author CZP
 * @parameter
 */
public interface TagService {

	public List<Tag> finaTagList(Tag s_tag, PageBean pageBean);

	public long tagListCount(Tag s_tag);

	public Tag getTagById(int tagId);

	public void saveTag(Tag tag);

	public void delete(Tag tag);

}
