package cn.guddqs.peakshop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import cn.guddqs.peakshop.dao.ColorDAO;
import cn.guddqs.peakshop.dao.ProductColorDAO;
import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.ProductDetailDAO;
import cn.guddqs.peakshop.dao.ProductSizeDAO;
import cn.guddqs.peakshop.dao.SizeDAO;
import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.dao.TypeDAO;
import cn.guddqs.peakshop.dao.ex.ProductDAOEx;
import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.ColorExample;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.ProductColor;
import cn.guddqs.peakshop.entity.ProductColorExample;
import cn.guddqs.peakshop.entity.ProductDetail;
import cn.guddqs.peakshop.entity.ProductDetailExample;
import cn.guddqs.peakshop.entity.ProductExample;
import cn.guddqs.peakshop.entity.ProductExample.Criteria;
import cn.guddqs.peakshop.entity.ProductSize;
import cn.guddqs.peakshop.entity.ProductSizeExample;
import cn.guddqs.peakshop.entity.Size;
import cn.guddqs.peakshop.entity.SizeExample;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.entity.StoreExample;
import cn.guddqs.peakshop.entity.Type;
import cn.guddqs.peakshop.entity.TypeExample;
import cn.guddqs.peakshop.util.Pagination;
import cn.guddqs.peakshop.view.entity.ViewColor;
import cn.guddqs.peakshop.view.entity.ViewProduct;
import cn.guddqs.peakshop.view.entity.ViewSize;

@Service
public class ProductService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProductDetailDAO productDetailDAO;
	
	@Autowired
	private ColorDAO colorDAO;
	
	@Autowired
	private TypeDAO typeDAO;
	
	@Autowired
	private SizeDAO sizeDAO;
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	private ProductColorDAO productColorDAO;
	
	@Autowired
	private ProductSizeDAO productSizeDAO;
	
	@Autowired
	private ProductDAOEx productDAOEx;
	
	@Autowired
	private ProductDAO productDAO;
	
    /**
     * 根据商品信息获取视图商品
     * @param commodity
     * @throws Exception 
     */
	@SuppressWarnings("unchecked")
	public ViewProduct getViewProduct(Product product, Integer colorId) throws Exception {
		logger.info("开始获取视图商品");
		if(product == null){
			return null;
		}
		ViewProduct viewProduct = new ViewProduct(product);
		try{
			//商品ID
			Integer productId = product.getId();
			//获取该商品对应的商品详情详情
			ProductDetailExample detailExample = new ProductDetailExample();
			detailExample.createCriteria().andProductIdEqualTo(productId);
			List<ProductDetail> details = productDetailDAO.selectByExample(detailExample);
			viewProduct.setDetails(details);
			//获取该商品对应的颜色
			ProductColorExample productColorExample = new ProductColorExample();
			productColorExample.createCriteria().andProductIdEqualTo(productId);
			List<ProductColor> productColors = productColorDAO.selectByExample(productColorExample);
			if(productColors.size() > 0){
				List<String> cols = new ArrayList<String>();
				for(ProductColor productColor : productColors){
					Color color = colorDAO.selectByPrimaryKey(productColor.getColorId());
					cols.add(color.getName());
				}
				viewProduct.setCols(cols);
			}
			viewProduct.setProductColors(productColors);
			//获取该商品对应的尺码
			ProductSizeExample productsizeExample = new ProductSizeExample();
			productsizeExample.createCriteria().andProductIdEqualTo(productId);
			List<ProductSize> productsizes = productSizeDAO.selectByExample(productsizeExample);
			if(productsizes.size() > 0){
				List<String> sizs = new ArrayList<String>();
				for(ProductSize productsize : productsizes){
					Size size = sizeDAO.selectByPrimaryKey(productsize.getSizeId());
					sizs.add(size.getName());
				}
				viewProduct.setSizs(sizs);
			}
			viewProduct.setProductSizes(productsizes);
			if(colorId == null){
				if(productColors.size() >0 && productsizes.size() >0){
					colorId = productColors.get(0).getColorId();
				}else{
					viewProduct.setStore(0);
					return viewProduct;
				}
			}
//				//获取该商品的库存，默认选择第一种颜色、第一种尺码的库存
			Integer sizeId = productsizes.get(0).getSizeId();
			StoreExample storeExample = new StoreExample();
			storeExample.createCriteria().andProductIdEqualTo(productId).andColorIdEqualTo(colorId).andSizeIdEqualTo(sizeId);
			List<Store> stores = storeDAO.selectByExample(storeExample);
			if(stores.size()>0){
				Integer store = stores.get(0).getStore();
				viewProduct.setStore(store);
			}else{
				throw new Exception("获取视图商品库存异常,未添加仓储信息");
			}
		}catch(Exception e){
			logger.error("获取视图商品异常",e);
			throw new Exception("获取视图商品异常",e);
		}
		return viewProduct;
	}
	
	@SuppressWarnings("unchecked")
	public void list(Integer pageNo,String name,Integer typeId,Integer isShow,Model model) throws Exception{
		logger.info("获取商品列表: name="+name+";typeId="+typeId+";isShow="+isShow);
		try{
			//查询品牌结果集
			TypeExample example = new TypeExample();
			example.createCriteria().andIsShowEqualTo(true);
			List<Type> types = typeDAO.selectByExample(example);
			model.addAttribute("types", types);
			
			//获取所有商品总条数
			ProductExample productExample = new ProductExample();
			Map<String,Object> map = new HashMap<String,Object>();
			Criteria criteria = productExample.createCriteria();
			if(typeId != null){
				criteria.andTypeIdEqualTo(typeId);
				map.put("typeId", typeId);
			}
			if(isShow == null){
				isShow = 0;
			}
			map.put("isShow", isShow);
			criteria.andIsShowEqualTo(isShow);
			if(name != null){
				criteria.andNameLike("%" + name + "%");
				map.put("name", name);
			}
			int totalCount = productDAO.countByExample(productExample);
			Pagination pagination = new Pagination(Pagination.cpn(pageNo),10,totalCount);
			//分页查询商品
			List<Product> commodities = productDAOEx.getProductListEx(map, pagination);
			pagination.setList(commodities);
			StringBuilder params = new StringBuilder();
			//页面分页展示
			String url = "/peakshop/product/list.do";
			pagination.pageView(url, params.toString());
			model.addAttribute("pagination", pagination);
			model.addAttribute("name", name);
			model.addAttribute("typeId", typeId);
			model.addAttribute("isShow", isShow);
		}catch(Exception e){
			logger.error("获取商品列表失败",e);
			throw new Exception("获取商品列表失败",e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void addProduct(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		logger.info("开始添加商品");
		request.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=UTF-8");
		try{
			Product product = new Product();
			//商品ID
			Integer productId = null;
			// 商品名称
			String name = request.getParameter("name");
			product.setName(name);
			// 商品价格
			Double prevprice = Double.parseDouble(request.getParameter("prevprice"));
			product.setPrevprice(prevprice);
//				// 商品种类
			Integer typeId = Integer.parseInt(request.getParameter("type"));
			product.setTypeId(typeId);
			//是否上架
			Integer isShow = Integer.parseInt(request.getParameter("isShow"));
			product.setIsShow(isShow);
			//是否热卖
			Integer isHot = Integer.parseInt(request.getParameter("isHot"));
			product.setIsHot(isHot);
			//是否新品
			Integer isNew = Integer.parseInt(request.getParameter("isNew"));
			product.setIsNew(isNew);
			//是否打折
			Integer isDiscount = Integer.parseInt(request.getParameter("isDiscount"));
			product.setIsDiscount(isDiscount);
			//折扣价
			String price = request.getParameter("price");
			if(StringUtils.isEmpty(price)){
				product.setPrice(prevprice);
			}else{
				product.setPrice(Double.parseDouble(price));
			}
//				//商品图片地址
			String picture = request.getParameter("picture");
			product.setPicture(picture);
//				//商品上架时间
			product.setShowTime(new Date());
//				//修改商品时间
			product.setEditTime(new Date());
			//保存商品信息
			productId = productDAOEx.insertReturnKey(product);
			
			//商品颜色
			String[] colors = request.getParameterValues("color");
			addProductColor(colors, productId);
			//商品尺寸
			String[] sizes = request.getParameterValues("size");
			addProductSize(sizes, productId);
			//仓储
			addStore(productId, colors, sizes);
		}catch(Exception e){
			logger.info("添加商品出现异常",e);
			throw new Exception("添加商品出现异常",e);
		}
	}
	
	//添加仓储
	private void addStore(Integer productId, String[] colors, String[] sizes) {
		if(colors==null || sizes==null){
			return;
		}
		for(int i=0 ; i<colors.length; i++){
			for(int j=0; j<sizes.length; j++){
				Integer colorId = Integer.parseInt(colors[i]);
				Integer sizeId = Integer.parseInt(sizes[j]);
				Store store = new Store();
				store.setProductId(productId);
				store.setSizeId(sizeId);
				store.setColorId(colorId);
				store.setStore(0);
				store.setStartTime(new Date());
				store.setEditTime(new Date());
				storeDAO.insert(store);
			}
		}
	}
	
	//添加商品尺码
	private void addProductSize(String[] sizes, Integer productId) {
		for(int i=0; i<sizes.length; i++){
			if(StringUtils.isEmpty(sizes[i])){
				continue;
			}
			Integer sizeId = Integer.parseInt(sizes[i]);
			addProductSize(sizeId, productId);
		}
	}
	
	
	private void addProductSize(Integer sizeId, Integer productId){
		ProductSize productSize = new ProductSize();
		productSize.setSizeId(sizeId);
		productSize.setProductId(productId);
		productSizeDAO.insert(productSize);
	}
	//添加商品颜色
	private void addProductColor(String[] colors, Integer productId) {
		for(int i=0; i<colors.length; i++){
			if(StringUtils.isEmpty(colors[i])){
				continue;
			}
			Integer colorId = Integer.parseInt(colors[i]);
			addProductColor(colorId, productId);
		}
	}
	
	private void addProductColor(Integer colorId, Integer productId){
		ProductColor productColor = new ProductColor();
		productColor.setColorId(colorId);
		productColor.setProductId(productId);
		productColorDAO.insert(productColor);
	}
	//添加商品详情
	private void addProductDetail(String picture, Integer productId) {
		ProductDetail detail = new ProductDetail();
		detail.setProductId(productId);
		detail.setPicture(picture);
		detail.setStartTime(new Date());
		detail.setEditTime(new Date());
		productDetailDAO.insert(detail);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void addProductDetail(HttpServletRequest request) throws Exception {
		logger.info("开始添加商品详情");
		try{
			//商品ID
			Integer productId = Integer.parseInt(request.getParameter("productId"));
			//图片1
			String picture1 = request.getParameter("picture1");
			if(!StringUtils.isEmpty(picture1)){
				addProductDetail(picture1, productId);
			}
			//图片2
			String picture2 = request.getParameter("picture2");
			if(!StringUtils.isEmpty(picture2)){
				addProductDetail(picture2, productId);
			}
			//图片3
			String picture3 = request.getParameter("picture3");
			if(!StringUtils.isEmpty(picture3)){
				addProductDetail(picture3, productId);
			}
			//图片4
			String picture4 = request.getParameter("picture4");
			if(!StringUtils.isEmpty(picture4)){
				addProductDetail(picture4, productId);
			}
			//图片5
			String picture5 = request.getParameter("picture5");
			if(!StringUtils.isEmpty(picture5)){
				addProductDetail(picture5, productId);
			}
			//图片6
			String picture6 = request.getParameter("picture6");
			if(!StringUtils.isEmpty(picture6)){
				addProductDetail(picture6, productId);
			}
			//图片7
			String picture7 = request.getParameter("picture7");
			if(!StringUtils.isEmpty(picture7)){
				addProductDetail(picture7, productId);
			}
			//图片8
			String picture8 = request.getParameter("picture8");
			if(!StringUtils.isEmpty(picture8)){
				addProductDetail(picture8, productId);
			}
			//图片9
			String picture9 = request.getParameter("picture9");
			if(!StringUtils.isEmpty(picture9)){
				addProductDetail(picture9, productId);
			}
			//图片10
			String picture10 = request.getParameter("picture10");
			if(!StringUtils.isEmpty(picture10)){
				addProductDetail(picture10, productId);
			}
		}catch(Exception e){
			logger.error("添加商品详情异常",e);
			throw new Exception("添加商品详情异常",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void toEdit(Integer id,Model model) throws Exception{
		logger.info("跳转至编辑商品id="+id+"页面");
		try{
			//查询品牌结果集
			TypeExample example = new TypeExample();
			example.createCriteria().andIsShowEqualTo(true);
			List<Type> types = typeDAO.selectByExample(example);
			model.addAttribute("types", types);
			//查询商品颜色
			ProductColorExample productColorExample = new ProductColorExample();
			productColorExample.createCriteria().andProductIdEqualTo(id);
			List<ProductColor> productColors = productColorDAO.selectByExample(productColorExample);
			List<Integer> checkedColorIds = new ArrayList<Integer>();
			for(ProductColor productColor : productColors){
				Color color = colorDAO.selectByPrimaryKey(productColor.getColorId());
				checkedColorIds.add(color.getId());
			}
			//查询颜色结果集
			ColorExample colorExample = new ColorExample();
			List<Color> colors = colorDAO.selectByExample(colorExample);
			List<ViewColor> viewColors = new ArrayList<ViewColor>();
			for(Color c : colors){
				ViewColor viewColor = new ViewColor(c);
				if(checkedColorIds.contains(c.getId())){
					viewColor.setChecked(true);
				}else{
					viewColor.setChecked(false);
				}
				viewColors.add(viewColor);
			}
			model.addAttribute("colors", viewColors);
			//查询商品尺寸
			ProductSizeExample productSizeExample = new ProductSizeExample();
			productSizeExample.createCriteria().andProductIdEqualTo(id);
			List<ProductSize> productSizes = productSizeDAO.selectByExample(productSizeExample);
			List<Integer> checkedSizeIds = new ArrayList<Integer>();
			for(ProductSize productSize : productSizes){
				Size size = sizeDAO.selectByPrimaryKey(productSize.getSizeId());
				checkedSizeIds.add(size.getId());
			}
			//查询尺码结果集
			SizeExample sizeExample = new SizeExample();
			List<Size> sizes = sizeDAO.selectByExample(sizeExample);
			List<ViewSize> viewSizes = new ArrayList<ViewSize>();
			for(Size s : sizes){
				ViewSize viewSize = new ViewSize(s);
				if(checkedSizeIds.contains(s.getId())){
					viewSize.setChecked(true);
				}else{
					viewSize.setChecked(false);
				}
				viewSizes.add(viewSize);
			}
			model.addAttribute("sizes", viewSizes);
			//查询要编辑的商品
			Product product = productDAO.selectByPrimaryKey(id);
			ViewProduct viewProduct = getViewProduct(product, null);
			model.addAttribute("viewProduct", viewProduct);
		}catch(Exception e){
			logger.error("跳转至编辑商品页面异常id="+id,e);
			throw new Exception("跳转至编辑商品页面异常id="+id,e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void edit(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=UTF-8");
		
		Integer productId = Integer.parseInt(request.getParameter("id"));;
		logger.info("开始编辑修改商品id="+productId);
		Product product = new Product();
		try{
			//商品ID
			product.setId(productId);
			// 商品名称
			String name = request.getParameter("name");
			product.setName(name);
			// 商品价格
			Double prevprice = Double.parseDouble(request.getParameter("prevprice"));
			product.setPrevprice(prevprice);
			// 商品种类
			Integer typeId = Integer.parseInt(request.getParameter("type"));
			product.setTypeId(typeId);
			//是否上架
			Integer isShow = Integer.parseInt(request.getParameter("isShow"));
			product.setIsShow(isShow);
			//是否热卖
			Integer isHot = Integer.parseInt(request.getParameter("isHot"));
			product.setIsHot(isHot);
			//是否新品
			Integer isNew = Integer.parseInt(request.getParameter("isNew"));
			product.setIsNew(isNew);
			//是否打折
			Integer isDiscount = Integer.parseInt(request.getParameter("isDiscount"));
			product.setIsDiscount(isDiscount);
			//折扣价
			String price = request.getParameter("price");
			if(StringUtils.isEmpty(price)){
				product.setPrice(prevprice);
			}else{
				product.setPrice(Double.parseDouble(price));
			}
			//商品图片地址
			String picture = request.getParameter("picture");
			product.setPicture(picture);
			//修改商品时间
			product.setEditTime(new Date());
			//更新商品信息
			productDAO.updateByPrimaryKeySelective(product);
			
			
			//商品颜色
			String[] colors = request.getParameterValues("color");
			//先删除之前的商品颜色
			ProductColorExample productColorExample = new ProductColorExample();
			productColorExample.createCriteria().andProductIdEqualTo(productId);
			productColorDAO.deleteByExample(productColorExample);
			//再添加商品颜色
			addProductColor(colors, productId);
			//商品尺寸
			String[] sizes = request.getParameterValues("size");
			//先删除之前的商品尺寸
			ProductSizeExample productSizeExample = new ProductSizeExample();
			productSizeExample.createCriteria().andProductIdEqualTo(productId);
			productSizeDAO.deleteByExample(productSizeExample);
			//再添加商品尺寸
			addProductSize(sizes, productId);
			//先删除之前的仓储信息
			StoreExample storeExample = new StoreExample();
			storeExample.createCriteria().andProductIdEqualTo(productId);
			storeDAO.deleteByExample(storeExample);
			//再添加仓储信息
			addStore(productId, colors, sizes);
		}catch(Exception e){
			logger.error("编辑修改商品id="+productId+"异常",e);
			throw new Exception("编辑修改商品id="+productId+"异常",e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void notShow(Integer id) throws Exception{
		logger.info("下架商品id="+id);
		try{
			Product product = new Product();
			product.setId(id);
			product.setIsShow(1);
			productDAO.updateByPrimaryKeySelective(product);
		}catch(Exception e){
			logger.error("下架商品id="+id+"失败",e);
			throw new Exception("下架商品id="+id+"失败",e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void onShow(Integer id) throws Exception{
		logger.info("上架商品id="+id);
		try{
			//先判断该商品对应的商品类型是否上架
			Product product = productDAO.selectByPrimaryKey(id);
			Integer typeId = product.getTypeId();
			Type type = typeDAO.selectByPrimaryKey(typeId);
			if(!type.getIsShow()){
				String typeName = type.getName();
				throw new Exception(typeName+"已下架");
			}
			product.setId(id);
			product.setIsShow(0);
			productDAO.updateByPrimaryKeySelective(product);
		}catch(Exception e){
			logger.error("商品上架异常",e);
			throw new Exception("商品上架异常",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void toAdd(Model model) throws Exception{
		logger.info("跳转至添加商品页面");
		try{
			//查询品牌结果集
			TypeExample example = new TypeExample();
			example.createCriteria().andIsShowEqualTo(true);
			List<Type> types = typeDAO.selectByExample(example);
			model.addAttribute("types", types);
			//查询颜色结果集
			ColorExample colorExample = new ColorExample();
			List<Color> colors = colorDAO.selectByExample(colorExample);
			model.addAttribute("colors", colors);
			//查询尺码结果集
			SizeExample sizeExample = new SizeExample();
			List<Size> sizes = sizeDAO.selectByExample(sizeExample);
			model.addAttribute("sizes", sizes);
		}catch(Exception e){
			logger.error("跳转至添加商品页面异常",e);
			throw new Exception("跳转至添加商品页面异常",e);
		}
		
	}
	
	public void toAddDetail(Model model, Integer id) throws Exception{
		logger.info("跳转至添加商品详情页面");
		try{
			//查询商品
			Product product = productDAO.selectByPrimaryKey(id);
			//查询商品颜色结果集
			ProductColorExample example = new ProductColorExample();
			example.createCriteria().andProductIdEqualTo(id);
			model.addAttribute("product", product);
		}catch(Exception e){
			logger.error("跳转至添加商品详情页面异常",e);
			throw new Exception("跳转至添加商品详情页面异常",e);
		}
	}

	@SuppressWarnings("unchecked")
	public void toEditDetail(Model model, Integer id) throws Exception {
		logger.info("跳转至修改商品详情页面");
		try{
			//获取商品信息
			Product product = productDAO.selectByPrimaryKey(id);
			//查询商品所有详情
			ProductDetailExample productDetailExample = new ProductDetailExample();
			productDetailExample.createCriteria().andProductIdEqualTo(id);
			List<ProductDetail> details = productDetailDAO.selectByExample(productDetailExample);
			model.addAttribute("product", product);
			model.addAttribute("details", details);
		}catch(Exception e){
			logger.error("跳转至修改商品详情页面异常",e);
			throw new Exception("跳转至修改商品详情页面异常",e);
		}
	}

	public void editDetail(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		logger.info("开始修改商品详情");
		try{
			//图片1
			String picture1 = request.getParameter("picture1");
			String detailId0 = request.getParameter("deatilId0");
			if(!StringUtils.isEmpty(picture1) && !StringUtils.isEmpty(detailId0)){
				editProductDetail(picture1, detailId0);
			}
			//图片2
			String picture2 = request.getParameter("picture2");
			String detailId1 = request.getParameter("deatilId1");
			if(!StringUtils.isEmpty(picture2) && !StringUtils.isEmpty(detailId1)){
				editProductDetail(picture2, detailId1);
			}
			//图片3
			String picture3 = request.getParameter("picture3");
			String detailId2 = request.getParameter("deatilId2");
			if(!StringUtils.isEmpty(picture3) && !StringUtils.isEmpty(detailId2)){
				editProductDetail(picture3, detailId2);
			}
			//图片4
			String picture4 = request.getParameter("picture4");
			String detailId3 = request.getParameter("deatilId3");
			if(!StringUtils.isEmpty(picture4) && !StringUtils.isEmpty(detailId3)){
				editProductDetail(picture4, detailId3);
			}
			//图片5
			String picture5 = request.getParameter("picture5");
			String detailId4 = request.getParameter("deatilId4");
			if(!StringUtils.isEmpty(picture5) && !StringUtils.isEmpty(detailId4)){
				editProductDetail(picture5, detailId4);
			}
			//图片6
			String picture6 = request.getParameter("picture6");
			String detailId5 = request.getParameter("deatilId5");
			if(!StringUtils.isEmpty(picture6) && !StringUtils.isEmpty(detailId5)){
				editProductDetail(picture6, detailId5);
			}
			//图片7
			String picture7 = request.getParameter("picture7");
			String detailId6 = request.getParameter("deatilId6");
			if(!StringUtils.isEmpty(picture7) && !StringUtils.isEmpty(detailId6)){
				editProductDetail(picture7, detailId6);
			}
			//图片8
			String picture8 = request.getParameter("picture8");
			String detailId7 = request.getParameter("deatilId7");
			if(!StringUtils.isEmpty(picture8) && !StringUtils.isEmpty(detailId7)){
				editProductDetail(picture8, detailId7);
			}
			//图片9
			String picture9 = request.getParameter("picture9");
			String detailId8 = request.getParameter("deatilId8");
			if(!StringUtils.isEmpty(picture9) && !StringUtils.isEmpty(detailId8)){
				editProductDetail(picture9, detailId8);
			}
			//图片10
			String picture10 = request.getParameter("picture10");
			String detailId9 = request.getParameter("deatilId9");
			if(!StringUtils.isEmpty(picture10) && !StringUtils.isEmpty(detailId9)){
				editProductDetail(picture10, detailId9);
			}
		}catch(Exception e){
			logger.error("修改商品详情异常",e);
			throw new Exception("修改商品详情异常",e);
		}
		
	}

	private void editProductDetail(String picture1, String detailId0) throws Exception {
		try{
			Integer id = Integer.parseInt(detailId0);
			ProductDetail productDetail = new ProductDetail();
			productDetail.setId(id);
			productDetail.setPicture(picture1);
			productDetailDAO.updateByPrimaryKeySelective(productDetail);
		}catch(Exception e){
			throw e;
		}
	}
	
}
