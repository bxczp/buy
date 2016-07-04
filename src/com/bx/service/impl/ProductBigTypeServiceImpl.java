package com.bx.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.PageBean;
import com.bx.entity.ProductBigType;
import com.bx.service.ProductBigTypeService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月18日 ProductBigTypeImpl.java
 * @author CZP
 * @parameter
 */

@Service("productBigTypeService")
public class ProductBigTypeServiceImpl implements ProductBigTypeService {

	// 自动泛型注入
	@Resource
	private BaseDAO<ProductBigType> baseDAO;

	@Override
	public List<ProductBigType> findAllBigTypeList() {
		return baseDAO.find(" from ProductBigType");
	}

	@Override
	public List<ProductBigType> finfProductBigTypeList(ProductBigType s_productBigType, PageBean pageBean) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append("from ProductBigType ");
		if (s_productBigType != null) {
			if (StringUtil.isNotEmpty(s_productBigType.getName())) {
				hql.append(" and name like '%" + s_productBigType.getName() + "%'");
			}
		}
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param);
		}
	}

	@Override
	public Long finfProductBigTypeListCount(ProductBigType s_productBigType) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append("select count(*) from ProductBigType ");
		if (s_productBigType != null) {
			if (StringUtil.isNotEmpty(s_productBigType.getName())) {
				hql.append(" and name like ?");
				param.add("%" + s_productBigType.getName() + "%");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public void saveProductBigType(ProductBigType productBigType) {
		baseDAO.merge(productBigType);
	}

	@Override
	public void deleteProductBigType(ProductBigType productBigType) {
		baseDAO.delete(productBigType);
	}

	@Override
	public ProductBigType getProductBigTypeById(int productBigTypeId) {
		return baseDAO.get(ProductBigType.class, productBigTypeId);
	}

}
