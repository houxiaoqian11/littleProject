package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.TradeOrderDAO;
import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.util.Pagination;

public interface TradeOrderDAOEx extends TradeOrderDAO {

	/**
	 * 分页获取订单信息
	 * @param map
	 * @param page
	 * @return
	 */
	public List<TradeOrder> getOrderListEx(Map<String, Object> map, Pagination page);
	
}
