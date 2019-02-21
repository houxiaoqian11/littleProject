package cn.guddqs.peakshop.dao.ex;

import cn.guddqs.peakshop.dao.CartDAO;
import cn.guddqs.peakshop.entity.Cart;

public interface CartDAOEx extends CartDAO {
	
	/**
	 * 保存购物车信息并返回主键
	 * @param map
	 * @param page
	 * @return
	 */
	public Integer insertReturnKey(Cart cart);
	
}
