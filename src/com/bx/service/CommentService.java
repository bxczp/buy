package com.bx.service;

import java.util.List;

import com.bx.entity.Comment;
import com.bx.entity.PageBean;

/**
 * @date 2016年3月19日 CommentService.java
 * @author CZP
 * @parameter
 */
public interface CommentService {

	public List<Comment> findCommentList(Comment s_comment, PageBean pageBean);

	public long commentListCount(Comment s_comment);

	public void saveComment(Comment comment);

	public void deleteComment(Comment comment);

	public Comment getCommentById(int id);

}
