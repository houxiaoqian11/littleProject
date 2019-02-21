package cn.guddqs.peakshop.dao.ex;

import cn.guddqs.peakshop.dao.CartDAOImpl;
import cn.guddqs.peakshop.entity.Cart;

public class CartDAOExImpl extends CartDAOImpl implements CartDAOEx {

	@Override
	public Integer insertReturnKey(Cart cart) {
		Integer id = (Integer) getSqlMapClientTemplate().insert("cart_ex.abatorgenerated_insert", cart);
		 return id;
	}

}
