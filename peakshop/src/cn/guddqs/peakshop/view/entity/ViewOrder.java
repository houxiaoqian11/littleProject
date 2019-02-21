//package cn.guddqs.peakshop.view.entity;
//
//import cn.guddqs.peakshop.entity.AddressInfo;
//import cn.guddqs.peakshop.entity.Cart;
//import cn.guddqs.peakshop.entity.Color;
//import cn.guddqs.peakshop.entity.Product;
//import cn.guddqs.peakshop.entity.Size;
//import cn.guddqs.peakshop.entity.TradeOrder;
//
//public class ViewOrder extends TradeOrder{
//	
//	public ViewOrder(TradeOrder tradeOrder){
//		this.setId(tradeOrder.getId());
//		this.setUserId(tradeOrder.getUserId());
//		this.setStatus(tradeOrder.getStatus());
//		this.setCartId(tradeOrder.getCartId());
//		this.setAddressId(tradeOrder.getAddressId());
//		this.setStartTime(tradeOrder.getStartTime());
//		this.setEndTime(tradeOrder.getEndTime());
//		this.setWuliuNo(tradeOrder.getWuliuNo());
//		this.setRemark(tradeOrder.getRemark());
//		this.setOrderId(tradeOrder.getOrderId());
//	}
//	
//	private Color color;
//	private Size size;
//	private Product product;
//	private Cart cart;
//	private AddressInfo addressInfo;
//	private Double totalPrice;
//	public Size getSize() {
//		return size;
//	}
//	public void setSize(Size size) {
//		this.size = size;
//	}
//	public Product getProduct() {
//		return product;
//	}
//	public void setProduct(Product product) {
//		this.product = product;
//	}
//	public Cart getCart() {
//		return cart;
//	}
//	public void setCart(Cart cart) {
//		this.cart = cart;
//	}
//	public AddressInfo getAddressInfo() {
//		return addressInfo;
//	}
//	public void setAddressInfo(AddressInfo addressInfo) {
//		this.addressInfo = addressInfo;
//	}
//	public Double getTotalPrice() {
//		return totalPrice;
//	}
//	public void setTotalPrice(Double totalPrice) {
//		this.totalPrice = totalPrice;
//	}
//	public Color getColor() {
//		return color;
//	}
//	public void setColor(Color color) {
//		this.color = color;
//	}
//	
//}
