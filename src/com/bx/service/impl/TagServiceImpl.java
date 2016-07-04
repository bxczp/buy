package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.PageBean;
import com.bx.entity.Tag;
import com.bx.service.TagService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月18日 TagServiceImpl.java
 * @author CZP
 * @parameter
 */

@Service("tagService")
public class TagServiceImpl implements TagService {

	@Resource
	private BaseDAO<Tag> baseDAO;

	@Override
	public List<Tag> finaTagList(Tag s_tag, PageBean pageBean) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append(" from Tag ");
		if (s_tag != null) {
			if (StringUtil.isNotEmpty(s_tag.getName())) {
				hql.append(" and name like ?");
				param.add("%" + s_tag.getName() + "%");
			}
		}
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
		}

	}

	@Override
	public long tagListCount(Tag s_tag) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append("select count(*) from Tag ");
		if (s_tag != null) {
			if (StringUtil.isNotEmpty(s_tag.getName())) {
				hql.append(" and name like ?");
				param.add("%" + s_tag.getName() + "%");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);

	}

	@Override
	public Tag getTagById(int tagId) {
		return baseDAO.get(Tag.class, tagId);
	}

	@Override
	public void saveTag(Tag tag) {
		baseDAO.merge(tag);
	}

	@Override
	public void delete(Tag tag) {
		baseDAO.delete(tag);
	}

}
