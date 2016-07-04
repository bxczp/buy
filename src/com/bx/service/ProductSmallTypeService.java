package com.bx.service;

import java.util.List;

import com.bx.entity.PageBean;
import com.bx.entity.ProductBigType;
import com.bx.entity.ProductSmallType;

/**
 * @date 2016年3月21日 ProductSmallTypeService.java
 * @author CZP
 * @parameter
 */
public interface ProductSmallTypeService {

	public List<ProductSmallType> getListByBigTypeId(ProductSmallType s_productSmallType, PageBean pageBean);

	public Long finfProductSmallTypeListCount(ProductSmallType s_productSmallType);

	public boolean existSmallTypWithBigTypeId(int bigTypeId);

	public void savePeoductSmallType(ProductSmallType productSmallType);

	public void deleteProductSmallType(ProductSmallType productSmallType);

	public ProductSmallType getProductSmallTypeById(int productSmallTypeId);

}
