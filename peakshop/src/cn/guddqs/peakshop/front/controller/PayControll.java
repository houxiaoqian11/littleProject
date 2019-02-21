package cn.guddqs.peakshop.front.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

import cn.guddqs.peakshop.dao.AddressInfoDAO;
import cn.guddqs.peakshop.dao.CartDAO;
import cn.guddqs.peakshop.dao.ColorDAO;
import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.dao.SizeDAO;
import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.dao.TradeOrderDAO;
import cn.guddqs.peakshop.dao.UserInfoDAO;
import cn.guddqs.peakshop.entity.AddressInfo;
import cn.guddqs.peakshop.entity.AddressInfoExample;
import cn.guddqs.peakshop.entity.Cart;
import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.entity.Size;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.entity.StoreExample;
import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.util.CommonUtil;
import cn.guddqs.peakshop.util.HttpUtil;
import cn.guddqs.peakshop.util.MD5;
import cn.guddqs.peakshop.util.SignUtils;
import cn.guddqs.peakshop.util.XmlUtils;

@Controller
public class PayControll {

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private CartDAO cartDAO;
	
	@Autowired
	private SizeDAO sizeDAO;
	
	@Autowired
	private ColorDAO colorDAO;
	
	@Autowired
	private TradeOrderDAO tradeOrderDAO;
	
	@Autowired
	private AddressInfoDAO addressInfoDAO;
	
	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/pay/buy")
	public Map<String, Object> buy(String cartIdStr, String addrId) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> productDataList = new ArrayList<Map<String, Object>>();
		returnMap.put("productData", productDataList);
		try{
			String[] cartIds = cartIdStr.split(",");
			//订单总价
			Double total = 0.0;
			Integer userId = null;
			for(String id : cartIds){
				//购物车ID
				Integer cartId = Integer.parseInt(id);
		
				Map<String, Object> productData = new HashMap<String, Object>();
				Cart cart = cartDAO.selectByPrimaryKey(cartId);
				userId = cart.getUserId();
				//商品数量
				Integer number = cart.getNumber();
				productData.put("num", number);
				Integer productId = cart.getProductId();
				Product product = productDAO.selectByPrimaryKey(productId);
				//商品名称
				String productName = product.getName();
				productData.put("name", productName);
				//商品价格
				Double productPrice = product.getPrice();
				productData.put("price", productPrice);
				//商品图片
				String picture = product.getPicture();
				productData.put("picture", picture);
				
				productDataList.add(productData);
				
				total = total + productPrice*number;
			}
			
			if(!StringUtils.isEmpty(addrId)){
				Integer id = Integer.parseInt(addrId);
				AddressInfo address = addressInfoDAO.selectByPrimaryKey(id);
				returnMap.put("address", address);
				returnMap.put("addrId", id);
				returnMap.put("addemt", 0);
			}else{
				AddressInfoExample addressInfoExample = new AddressInfoExample();
				addressInfoExample.createCriteria().andUserIdEqualTo(userId).andIsDefaultEqualTo(true);
				List<AddressInfo> addressInfos = addressInfoDAO.selectByExample(addressInfoExample);
				if(addressInfos.size()>0){
					AddressInfo addressInfo = (AddressInfo) addressInfoDAO.selectByExample(addressInfoExample).get(0);
					Integer addressId = addressInfo.getId();
					returnMap.put("address", addressInfo);
					returnMap.put("addrId", addressId);
					returnMap.put("addemt", 0);
				}else{
					returnMap.put("addemt", 1);
				}
			}
			
			returnMap.put("total", total);
		}catch(Exception e){
			logger.error("支付异常",e);
		}
		return returnMap;
	}
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/pay/createOrder")
	public Map<String, Object> createOrder(String cartIdStr,String remark, String addrId) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try{
			String[] cartIds = cartIdStr.split(",");
			Integer userId = 1;
			AddressInfo addressInfo = null;
			//订单总价
			Double total = 0.0;
			if(cartIds.length>0){
				Integer cartId = Integer.parseInt(cartIds[0]);
				Cart cart = cartDAO.selectByPrimaryKey(cartId);
				userId = cart.getUserId();
				if(!StringUtils.isEmpty(addrId)){
					Integer id = Integer.parseInt(addrId);
					addressInfo = addressInfoDAO.selectByPrimaryKey(id);
				}else{
					AddressInfoExample addressInfoExample = new AddressInfoExample();
					addressInfoExample.createCriteria().andUserIdEqualTo(userId).andIsDefaultEqualTo(true);
					List<AddressInfo> addressInfos = addressInfoDAO.selectByExample(addressInfoExample);
					if(addressInfos.size()>0){
						addressInfo = (AddressInfo) addressInfoDAO.selectByExample(addressInfoExample).get(0);
					}
				}
			}
			//订单号
			String orderId = userId + new Date().getTime() + "";
			
			for(String id : cartIds){
				//购物车ID
				Integer cartId = Integer.parseInt(id);
				Cart cart = cartDAO.selectByPrimaryKey(cartId);
				//商品
				Integer productId = cart.getProductId();
				Product product = productDAO.selectByPrimaryKey(productId);
				//颜色
				Integer colorId = cart.getColorId();
				Color color = colorDAO.selectByPrimaryKey(colorId);
				//尺寸
				Integer sizeId = cart.getSizeId();
				Size size = sizeDAO.selectByPrimaryKey(sizeId);
				//判断库存是否足够
				if(!checkStore(cartId)){
					returnMap.put("status", 0);
					returnMap.put("errorMsg", "库存不足");
					return returnMap;
				}
				
				//添加订单信息
				TradeOrder tradeOrder = new TradeOrder();
				tradeOrder.setUserId(userId);
				tradeOrder.setAddressInfo(addressInfo.getAddress());
				tradeOrder.setName(addressInfo.getName());
				tradeOrder.setProductName(product.getName());
				tradeOrder.setNumber(cart.getNumber());
				tradeOrder.setColor(color.getName());
				tradeOrder.setSize(size.getName());
				tradeOrder.setTotalPrice(product.getPrice()*cart.getNumber());
				tradeOrder.setPhone(addressInfo.getPhone());
				tradeOrder.setPrice(product.getPrice());
				tradeOrder.setStartTime(new Date());
				tradeOrder.setEndTime(new Date());
				tradeOrder.setStatus(0);//0未支付
				tradeOrder.setRemark(remark);
				tradeOrder.setOrderId(orderId);
				tradeOrder.setPicture(product.getPicture());
				tradeOrder.setCartId(cartId);
				tradeOrderDAO.insert(tradeOrder);
				//计算订单总价
				total = total + tradeOrder.getTotalPrice();
			}
			
			//统一下单
			//组织上送报文
			String reqXml = null;
			try{
				SortedMap<String, String> map = new TreeMap<String, String>();
				//小程序ID
				String appid = ppc.getMessage("appid");
				map.put("appid", appid);
				//商户号
				String mch_id = ppc.getMessage("mch_id");
				map.put("mch_id", mch_id);
				//随机字符串
				String nonce_str = String.valueOf(new Date().getTime());
				map.put("nonce_str", nonce_str);
				//签名类型
				String sign_type = "MD5";
				map.put("sign_type", sign_type);
				//商品描述
				String body = ppc.getMessage("body");
				map.put("body", body);
				//商户订单号
				String out_trade_no = orderId;
				map.put("out_trade_no", out_trade_no);
				//总金额
				BigDecimal fee = new BigDecimal(total);
				String total_fee =  CommonUtil.yuan2fenAsString(fee);
				map.put("total_fee", total_fee);
				//终端IP
				String spbill_create_ip = "123.207.30.64";
				map.put("spbill_create_ip", spbill_create_ip);
				//通知地址
				String notify_url = ppc.getMessage("notify_url");
				map.put("notify_url", notify_url);
				//交易类型
				String trade_type = "JSAPI";
				map.put("trade_type", trade_type);
				//用户标识
				String openid = userInfoDAO.selectByPrimaryKey(userId).getOpenid();
				map.put("openid", openid);
				StringBuilder buf = new StringBuilder((map.size() + 1) * 10);
				SignUtils.buildPayParams(buf, map, false);
				String preStr = buf.toString();
				String key = ppc.getMessage("key");
				String sign = MD5.sign(preStr, "&key=" + key, "UTF-8");
				map.put("sign", sign);
				reqXml = XmlUtils.toXml(map);
				logger.info("orderId="+orderId+"上送至银行报文：" + reqXml);
			}catch(Exception e){
				throw e;
			}
			
			//与银行通讯
			Map<String, String> respMap = null;
			try{
				String reqUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
				Integer readTimeout = 30000;
				Integer connTimeOut = 30000;
				respMap = HttpUtil.send(reqUrl, reqXml, readTimeout, connTimeOut, "UTF-8");
				logger.info("orderId="+orderId+"银行返回报文：" + respMap);
				System.out.println("银行返回报文：" + respMap);
			}catch(Exception e){
				logger.error("与银行通讯异常!,orderId:" + orderId,e);
			}
			
			try{
				//返回状态码
				String returnCode = respMap.get("return_code");
				if("SUCCESS".equals(returnCode)){
					if (!SignUtils.checkParam(respMap, ppc.getMessage("key"))) {
						logger.warn("验证签名不通过");
					}
					//业务结果
					String resultCode = respMap.get("result_code");
					if("SUCCESS".equals(resultCode)){
						returnMap.put("status", 1);
						
						Map<String, String> order = new HashMap<String, String>();
						order.put("timeStamp", new Date().getTime()+"");
						order.put("nonceStr", respMap.get("nonce_str"));
						order.put("package", "prepay_id="+respMap.get("prepay_id"));
						order.put("signType", "MD5");
						order.put("appId", ppc.getMessage("appid"));
						
						StringBuilder buf = new StringBuilder((order.size() + 1) * 10);
						SignUtils.buildPayParams(buf, order, false);
						String preStr = buf.toString();
						String key = ppc.getMessage("key");
						String sign = MD5.sign(preStr, "&key=" + key, "UTF-8");
						order.put("paySign", sign);
						returnMap.put("order", order);
						returnMap.put("pay_type", "weixin");
					}
				}
			}catch(Exception e){
				
			}
			
		}catch(Exception e){
			logger.error("统一预下单异常",e);
		}
		logger.info("finish:" + returnMap);
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping("/pay/pay")
	public Map<String, Object> pay(Integer id) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		logger.info("支付订单id="+id);
		try{
			TradeOrder tradeOrder = tradeOrderDAO.selectByPrimaryKey(id);
			Integer userId = tradeOrder.getUserId();
			//重新生成商户订单号
			String orderId = userId + new Date().getTime() + "";
			tradeOrder.setOrderId(orderId);
			tradeOrderDAO.updateByPrimaryKeySelective(tradeOrder);
			//userId
			//总金额
			Double total = tradeOrder.getTotalPrice();
			
			//统一下单
			//组织上送报文
			String reqXml = null;
			try{
				SortedMap<String, String> map = new TreeMap<String, String>();
				//小程序ID
				String appid = ppc.getMessage("appid");
				map.put("appid", appid);
				//商户号
				String mch_id = ppc.getMessage("mch_id");
				map.put("mch_id", mch_id);
				//随机字符串
				String nonce_str = String.valueOf(new Date().getTime());
				map.put("nonce_str", nonce_str);
				//签名类型
				String sign_type = "MD5";
				map.put("sign_type", sign_type);
				//商品描述
				String body = "定陶PEAK专卖-服装";
				map.put("body", body);
				//商户订单号
				String out_trade_no = orderId;
				map.put("out_trade_no", out_trade_no);
				//总金额
				BigDecimal fee = new BigDecimal(total);
				String total_fee =  CommonUtil.yuan2fenAsString(fee);
				map.put("total_fee", total_fee);
				//终端IP
				String spbill_create_ip = "123.207.30.64";
				map.put("spbill_create_ip", spbill_create_ip);
				//通知地址
				String notify_url = ppc.getMessage("notify_url");
				map.put("notify_url", notify_url);
				//交易类型
				String trade_type = "JSAPI";
				map.put("trade_type", trade_type);
				//用户标识
				String openid = userInfoDAO.selectByPrimaryKey(userId).getOpenid();
				map.put("openid", openid);
				StringBuilder buf = new StringBuilder((map.size() + 1) * 10);
				SignUtils.buildPayParams(buf, map, false);
				String preStr = buf.toString();
				String key = ppc.getMessage("key");
				String sign = MD5.sign(preStr, "&key=" + key, "UTF-8");
				System.out.println("preStr: " + preStr);
				map.put("sign", sign);
				reqXml = XmlUtils.toXml(map);
				logger.info("orderId="+orderId+"上送至银行报文：" + reqXml);
			}catch(Exception e){
				throw e;
			}
			
			//与银行通讯
			Map<String, String> respMap = null;
			try{
				String reqUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
				Integer readTimeout = 30000;
				Integer connTimeOut = 30000;
				respMap = HttpUtil.send(reqUrl, reqXml, readTimeout, connTimeOut, "UTF-8");
				logger.info("orderId="+orderId+"银行返回报文：" + respMap);
				System.out.println("银行返回报文：" + respMap);
			}catch(Exception e){
				logger.error("与银行通讯异常!,orderId:" + orderId,e);
			}
			
			try{
				//返回状态码
				String returnCode = respMap.get("return_code");
				if("SUCCESS".equals(returnCode)){
					if (!SignUtils.checkParam(respMap, ppc.getMessage("key"))) {
						logger.warn("验证签名不通过");
					}
					//业务结果
					String resultCode = respMap.get("result_code");
					if("SUCCESS".equals(resultCode)){
						returnMap.put("status", 1);
						
						Map<String, String> order = new HashMap<String, String>();
						order.put("timeStamp", new Date().getTime()+"");
						order.put("nonceStr", respMap.get("nonce_str"));
						order.put("package", "prepay_id="+respMap.get("prepay_id"));
						order.put("signType", "MD5");
						order.put("appId", ppc.getMessage("appid"));
						
						StringBuilder buf = new StringBuilder((order.size() + 1) * 10);
						SignUtils.buildPayParams(buf, order, false);
						String preStr = buf.toString();
						String key = ppc.getMessage("key");
						String sign = MD5.sign(preStr, "&key=" + key, "UTF-8");
						order.put("paySign", sign);
						returnMap.put("order", order);
						returnMap.put("pay_type", "weixin");
					}
				}
			}catch(Exception e){
				
			}
			
		}catch(Exception e){
			logger.error("支付订单id="+id+"异常",e);
		}
		logger.info("finish:" + returnMap);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkStore(Integer cartId) throws Exception {
		//购物车xinx
		Cart cart = cartDAO.selectByPrimaryKey(cartId);
		//商品ID
		Integer productId = cart.getProductId();
		//颜色ID
		Integer colorId = cart.getColorId();
		//尺码ID
		Integer sizeId = cart.getSizeId();
		StoreExample storeExample = new StoreExample();
		storeExample.createCriteria().andSizeIdEqualTo(sizeId).andProductIdEqualTo(productId).andColorIdEqualTo(colorId);
		List<Store> stores = storeDAO.selectByExample(storeExample);
		if(stores==null || stores.size()<1){
			throw new Exception("没有库存信息");
		}else{
			Store store = stores.get(0);
			if(store.getStore() < cart.getNumber()){
				return false;
			}
		}
		return true;
	}
	
}
