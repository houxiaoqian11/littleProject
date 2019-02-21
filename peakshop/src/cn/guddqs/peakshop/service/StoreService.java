package cn.guddqs.peakshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.guddqs.peakshop.dao.ColorDAO;
import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.SizeDAO;
import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Size;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.view.entity.ViewStore;


@Service
public class StoreService {
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ColorDAO colorDAO;
	
	@Autowired
	private SizeDAO sizeDAO;
	
	public ViewStore getViewStore(Store store) throws Exception{
		if(store == null){
			return null;
		}
		ViewStore viewStore = new ViewStore(store);
		try{
			//商品ID
			Integer productId = store.getProductId();
			Product product = productDAO.selectByPrimaryKey(productId);
			viewStore.setProduct(product);
			//颜色ID
			Integer colorId = store.getColorId();
			Color color = colorDAO.selectByPrimaryKey(colorId);
			viewStore.setColor(color);
			//尺码ID
			Integer sizeId = store.getSizeId();
			Size size = sizeDAO.selectByPrimaryKey(sizeId);
			viewStore.setSize(size);
//			//图片
			viewStore.setPicture(product.getPicture());
			
		}catch(Exception e){
			throw new Exception("获取视图仓储信息异常",e);
		}
		return viewStore;
	}
	
}
