package com.bx.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bx.dao.BaseDAO;
import com.bx.entity.PageBean;
import com.bx.entity.Product;
import com.bx.service.ProductService;
import com.bx.util.StringUtil;

/**
 * @date 2016年3月18日 ProductServiceImpl.java
 * @author CZP
 * @parameter
 */

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Resource
	private BaseDAO<Product> baseDAO;

	@Override
	public List<Product> findProductList(Product s_product, PageBean pageBean) {
		StringBuffer hql = new StringBuffer();
		List<Object> param = new LinkedList<>();
		hql.append("from Product ");
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				hql.append(" and bigType.id =?");
				param.add(s_product.getBigType().getId());
			}
			if (s_product.getSmallType() != null) {
				hql.append(" and smallType.id=?");
				param.add(s_product.getSmallType().getId());
			}
			if (StringUtil.isNotEmpty(s_product.getName())) {
				hql.append(" and name like '%" + s_product.getName() + "%'");
			}
			if (s_product.getSpecialPrice() == 1) {
				hql.append(" and specialPrice =" + s_product.getSpecialPrice() + " order by specialPricetime desc");
			}
			if (s_product.getHot() == 1) {
				hql.append(" and hot =1 order by hotTime desc");
			}
		}
		if (pageBean != null) {
			return baseDAO.find(hql.toString().replaceFirst("and", "where"), param, pageBean);
		} else {
			return null;
		}
	}

	/**
	 * 获取一个大类下 product的数目
	 */
	@Override
	public long productListCount(Product s_product) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from Product ");
		List<Object> param = new LinkedList<>();
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				hql.append(" and bigType.id =?");
				param.add(s_product.getBigType().getId());
			}
			if (s_product.getSmallType() != null) {
				hql.append(" and smallType.id=?");
				param.add(s_product.getSmallType().getId());
			}
			if (StringUtil.isNotEmpty(s_product.getName())) {
				hql.append(" and name like '%" + s_product.getName() + "%'");
			}
		}
		return baseDAO.count(hql.toString().replaceFirst("and", "where"), param);
	}

	@Override
	public Product getProductById(int productId) {
		return baseDAO.get(Product.class, productId);
	}

	@Override
	public void saveProduct(Product product) {
		baseDAO.merge(product);
	}

	@Override
	public void deleteProduct(Product product) {
		baseDAO.delete(product);
	}

	@Override
	public void setProductWithHot(int productId) {
		Product product = baseDAO.get(Product.class, productId);
		product.setHot(1);
		product.setHotTime(new Date());
		baseDAO.merge(product);
	}

	@Override
	public void setProductWithSpecialPrice(int productId) {
		Product product = baseDAO.get(Product.class, productId);
		product.setSpecialPrice(1);
		product.setSpecialPriceTime(new Date());
		baseDAO.merge(product);
	}

	@Override
	public boolean existProductWithSmallTypeId(int productSmallTypeId) {
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from Product where smallType.id=" + productSmallTypeId);
		long num = baseDAO.count(hql.toString());
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}

}
