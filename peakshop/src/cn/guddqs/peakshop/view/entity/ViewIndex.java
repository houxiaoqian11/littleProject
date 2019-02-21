package cn.guddqs.peakshop.view.entity;

import java.util.List;

import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Type;

public class ViewIndex {
	
	private List<Type> typeList;
	
	private List<Product> hotProduct;
	
	private List<Product> newProduct;
	
	private List<Product> discountProduct;

	public List<Type> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<Type> typeList) {
		this.typeList = typeList;
	}

	public List<Product> getHotProduct() {
		return hotProduct;
	}

	public void setHotProduct(List<Product> hotProduct) {
		this.hotProduct = hotProduct;
	}

	public List<Product> getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(List<Product> newProduct) {
		this.newProduct = newProduct;
	}

	public List<Product> getDiscountProduct() {
		return discountProduct;
	}

	public void setDiscountProduct(List<Product> discountProduct) {
		this.discountProduct = discountProduct;
	}
	
}
