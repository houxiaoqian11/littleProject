package cn.guddqs.peakshop.view.entity;

import cn.guddqs.peakshop.entity.ProductColor;

public class ViewProductColor extends ProductColor {
	
	public ViewProductColor(ProductColor productColor){
		this.setId(productColor.getId());
		this.setColorId(productColor.getColorId());
		this.setProductId(productColor.getProductId());
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
