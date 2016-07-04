package com.bx.service;

import java.util.List;

import com.bx.entity.Notice;
import com.bx.entity.PageBean;

/**
 * @date 2016年3月18日 NoticeService.java
 * @author CZP
 * @parameter
 */
public interface NoticeService {

	public List<Notice> findnoticeList(Notice s_notice, PageBean pageBean);

	// get方法的主键值类型 要和 实体类中的 主键属性类型 相同
	public Notice getNoticeById(int noticeId);

	public long getNoticeCount(Notice s_notice);

	public void saveNotice(Notice notice);

	public void deleteNotcie(Notice notice);

}
