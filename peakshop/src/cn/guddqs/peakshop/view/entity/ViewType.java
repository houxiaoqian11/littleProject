package cn.guddqs.peakshop.view.entity;

import java.util.List;

import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Type;

public class ViewType extends Type {

	public ViewType(Type type) {
		this.setId(type.getId());
		this.setPicture(type.getPicture());
		this.setStartTime(type.getStartTime());
		this.setEditTime(type.getEditTime());
		this.setName(type.getName());
		this.setIsShow(type.getIsShow());
	}

	private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
