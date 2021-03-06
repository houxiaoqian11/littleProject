package cn.guddqs.peakshop.dao;

import cn.guddqs.peakshop.entity.Wuliu;
import cn.guddqs.peakshop.entity.WuliuExample;
import java.util.List;

public interface WuliuDAO {

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	void insert(Wuliu record);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int updateByPrimaryKey(Wuliu record);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int updateByPrimaryKeySelective(Wuliu record);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	List selectByExample(WuliuExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	Wuliu selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int deleteByExample(WuliuExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int countByExample(WuliuExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int updateByExampleSelective(Wuliu record, WuliuExample example);

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table wuliu
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	int updateByExample(Wuliu record, WuliuExample example);
}