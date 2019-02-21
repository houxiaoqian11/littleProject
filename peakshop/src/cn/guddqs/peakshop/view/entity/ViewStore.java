package cn.guddqs.peakshop.view.entity;

import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Size;
import cn.guddqs.peakshop.entity.Store;

public class ViewStore extends Store {

	public ViewStore(Store store) {
		this.setId(store.getId());
		this.setStartTime(store.getStartTime());
		this.setEditTime(store.getEditTime());
		this.setColorId(store.getColorId());
		this.setProductId(store.getProductId());
		this.setSizeId(store.getSizeId());
		this.setStore(store.getStore());
	}
	
	private Product product;
	
	private Size size;
	
	private Color color;
	
	private String picture;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}
