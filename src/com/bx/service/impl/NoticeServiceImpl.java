package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.Notice;
import com.bx.entity.PageBean;
import com.bx.service.NoticeService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月18日 NoticeServiceImpl.java
 * @author CZP
 * @parameter
 */
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

	// 默认按名称进行注入，若没有找到 则按类型 注入
	@Resource
	private BaseDAO<Notice> baseDAO;

	@Override
	public List<Notice> findnoticeList(Notice s_notice, PageBean pageBean) {
		// 存放查询参数
		List<Object> param = new LinkedList<>();
		StringBuffer hql = new StringBuffer();
		hql.append("from Notice ");
		if (s_notice != null) {
			if (StringUtil.isNotEmpty(s_notice.getTitle())) {
				hql.append("and title like ?");
				param.add("%" + s_notice.getTitle() + "%");
			}
		}
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return null;
		}
	}

	@Override
	// get方法的主键值类型 要和 实体类中的 主键属性类型 相同
	public Notice getNoticeById(int noticeId) {
		return baseDAO.get(Notice.class, noticeId);
	}

	@Override
	public long getNoticeCount(Notice s_notice) {
		List<Object> param = new LinkedList<>();
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from Notice ");
		if (s_notice != null) {
			if (StringUtil.isNotEmpty(s_notice.getTitle())) {
				hql.append("and title like ?");
				param.add("%" + s_notice.getTitle() + "%");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);

	}

	@Override
	public void saveNotice(Notice notice) {
		baseDAO.merge(notice);
	}

	@Override
	public void deleteNotcie(Notice notice) {
		baseDAO.delete(notice);
	}

}
