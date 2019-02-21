package cn.guddqs.peakshop.view.entity;

import java.util.List;

import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.ProductColor;
import cn.guddqs.peakshop.entity.ProductDetail;
import cn.guddqs.peakshop.entity.ProductSize;


/**
 * 
 * @author hxq
 *用于展示的商品信息，该对象除了包含商品Product所有属性外，还扩展了几个属性，方便页面展示。
 */
public class ViewProduct extends Product {
	
	public ViewProduct(Product product){
		this.setId(product.getId());
		this.setPicture(product.getPicture());
		this.setPrice(product.getPrice());
		this.setShowTime(product.getShowTime());
		this.setEditTime(product.getEditTime());
		this.setName(product.getName());
		this.setTypeId(product.getTypeId());
		this.setPrevprice(product.getPrevprice());
		this.setIsNew(product.getIsNew());
		this.setIsHot(product.getIsHot());
		this.setIsShow(product.getIsShow());
		this.setIsDiscount(product.getIsDiscount());
		this.setProductNo(product.getProductNo());
	}
	//商品详情
    private List<ProductDetail> details;
    //商品颜色
    private List<ProductColor> productColors;
    //商品尺寸
    private List<ProductSize> productSizes;
    //颜色名称
    private List<String> cols;
    //尺寸名称
    private List<String> sizs;
    //库存
    private Integer store;
 
	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}

	public List<String> getCols() {
		return cols;
	}

	public void setCols(List<String> cols) {
		this.cols = cols;
	}

	public List<String> getSizs() {
		return sizs;
	}

	public void setSizs(List<String> sizs) {
		this.sizs = sizs;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public List<ProductColor> getProductColors() {
		return productColors;
	}

	public void setProductColors(List<ProductColor> productColors) {
		this.productColors = productColors;
	}

	public List<ProductSize> getProductSizes() {
		return productSizes;
	}

	public void setProductSizes(List<ProductSize> productSizes) {
		this.productSizes = productSizes;
	}

}
