package cn.guddqs.peakshop.dao;

import cn.guddqs.peakshop.entity.TradeOrder;
import cn.guddqs.peakshop.entity.TradeOrderExample;
import java.util.List;

public interface TradeOrderDAO {

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	void insert(TradeOrder record);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int updateByPrimaryKey(TradeOrder record);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int updateByPrimaryKeySelective(TradeOrder record);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	List selectByExample(TradeOrderExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	TradeOrder selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int deleteByExample(TradeOrderExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int countByExample(TradeOrderExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int updateByExampleSelective(TradeOrder record, TradeOrderExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table trade_order
	 * @abatorgenerated  Fri Jan 25 20:43:09 CST 2019
	 */
	int updateByExample(TradeOrder record, TradeOrderExample example);
}