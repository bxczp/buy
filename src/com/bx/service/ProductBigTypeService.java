package com.bx.service;

import java.util.List;

import com.bx.entity.PageBean;
import com.bx.entity.ProductBigType;

/**
 * @date 2016年3月18日 ProductBigType.java
 * @author CZP
 * @parameter
 */
public interface ProductBigTypeService {
	public List<ProductBigType> findAllBigTypeList();

	public List<ProductBigType> finfProductBigTypeList(ProductBigType s_productBigType, PageBean pageBean);

	public Long finfProductBigTypeListCount(ProductBigType s_productBigType);

	public void saveProductBigType(ProductBigType productBigType);

	public void deleteProductBigType(ProductBigType productBigType);

	public ProductBigType getProductBigTypeById(int productBigTypeId);

}
