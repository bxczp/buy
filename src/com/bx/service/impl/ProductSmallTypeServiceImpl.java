package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.PageBean;
import com.bx.entity.ProductSmallType;
import com.bx.service.ProductSmallTypeService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月21日 ProductSmallTypeServiceImpl.java
 * @author CZP
 * @parameter
 */

@Service
public class ProductSmallTypeServiceImpl implements ProductSmallTypeService {

	@Resource
	private BaseDAO<ProductSmallType> baseDAO;

	@Override
	public List<ProductSmallType> getListByBigTypeId(ProductSmallType s_productSmallType, PageBean pageBean) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append("from ProductSmallType");
		if (s_productSmallType != null) {
			if (s_productSmallType.getBigType() != null) {
				if (StringUtil.isNotEmpty(String.valueOf(s_productSmallType.getBigType().getId()))
						&& s_productSmallType.getBigType().getId() != 0) {
					hql.append(" and bigType.id =" + s_productSmallType.getBigType().getId());
				}
			}
			if (StringUtil.isNotEmpty(s_productSmallType.getName())) {
				hql.append(" and name like '%" + s_productSmallType.getName() + "%'");
				// hql.append(" and name like ?");
				// 不要加 单引号
				// param.add("%" + s_productSmallType.getName() + "%");
			}
		}
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
		}
	}

	@Override
	public Long finfProductSmallTypeListCount(ProductSmallType s_productSmallType) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append("select count(*) from ProductSmallType");
		if (s_productSmallType != null) {
			if (s_productSmallType.getBigType() != null) {
				if (StringUtil.isNotEmpty(String.valueOf(s_productSmallType.getBigType().getId()))
						&& s_productSmallType.getBigType().getId() != 0) {
					hql.append(" and bigType.id =" + s_productSmallType.getBigType().getId());
				}
			}
			if (StringUtil.isNotEmpty(s_productSmallType.getName())) {
				hql.append(" and name like '%" + s_productSmallType.getName() + "%'");
				// hql.append(" and name like ?");
				// 不要加 单引号
				// param.add("%" + s_productSmallType.getName() + "%");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public boolean existSmallTypWithBigTypeId(int bigTypeId) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from ProductSmallType where bigType.id=" + bigTypeId);
		long num = baseDAO.count(hql.toString());
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void savePeoductSmallType(ProductSmallType productSmallType) {
		baseDAO.merge(productSmallType);
	}

	@Override
	public void deleteProductSmallType(ProductSmallType productSmallType) {
		baseDAO.delete(productSmallType);
	}

	@Override
	public ProductSmallType getProductSmallTypeById(int productSmallTypeId) {
		return baseDAO.get(ProductSmallType.class, productSmallTypeId);
	}

}
