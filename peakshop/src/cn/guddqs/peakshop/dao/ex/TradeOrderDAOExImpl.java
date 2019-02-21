package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.TradeOrderDAOImpl;
import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.util.Pagination;

public class TradeOrderDAOExImpl extends TradeOrderDAOImpl implements TradeOrderDAOEx {

	@Override
	public List<TradeOrder> getOrderListEx(Map<String, Object> map, Pagination page) {
		List<TradeOrder> list = (List<TradeOrder>) getSqlMapClientTemplate().queryForList("trade_order_ex.getOrderListEx", map,
				page.getPageStartRow() - 1, page.getPageSize());
		return list;
	}

}
