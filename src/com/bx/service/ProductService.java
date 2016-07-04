package com.bx.service;

import java.util.List;

import com.bx.entity.PageBean;
import com.bx.entity.Product;

/**
 * @date 2016年3月18日 ProductService.java
 * @author CZP
 * @parameter
 */
public interface ProductService {

	public List<Product> findProductList(Product s_product, PageBean pageBean);

	public long productListCount(Product s_product);

	public Product getProductById(int productId);

	public void saveProduct(Product product);

	public void deleteProduct(Product product);

	public void setProductWithHot(int productId);

	public void setProductWithSpecialPrice(int productId);

	public boolean existProductWithSmallTypeId(int productSmallTypeId);

}
