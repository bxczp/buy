package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.Comment;
import com.bx.entity.PageBean;
import com.bx.service.CommentService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月19日 CommentServiceImpl.java
 * @author CZP
 * @parameter
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Resource
	private BaseDAO<Comment> baseDAO;

	@Override
	public List<Comment> findCommentList(Comment s_comment, PageBean pageBean) {
		List<Object> param = new LinkedList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from Comment ");
		if (s_comment != null) {
			if (StringUtil.isNotEmpty(s_comment.getContent())) {
				hql.append(" and content like '%" + s_comment.getContent() + "%'");
			}
		}
		hql.append(" order by createTime desc");
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		}
		return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public long commentListCount(Comment s_comment) {
		List<Object> param = new LinkedList<>();
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from Comment ");
		if (s_comment != null) {
			if (StringUtil.isNotEmpty(s_comment.getContent())) {
				hql.append(" and content like '%" + s_comment.getContent() + "%'");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public void saveComment(Comment comment) {
		baseDAO.merge(comment);
	}

	@Override
	public void deleteComment(Comment comment) {
		baseDAO.delete(comment);
	}

	@Override
	public Comment getCommentById(int id) {
		return baseDAO.get(Comment.class, id);
	}

}
