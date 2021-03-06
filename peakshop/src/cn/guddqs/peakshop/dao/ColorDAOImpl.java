package cn.guddqs.peakshop.dao;

import cn.guddqs.peakshop.entity.Color;
import cn.guddqs.peakshop.entity.ColorExample;
import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class ColorDAOImpl extends SqlMapClientDaoSupport implements ColorDAO {

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public ColorDAOImpl() {
		super();
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public void insert(Color record) {
		getSqlMapClientTemplate()
				.insert("color.abatorgenerated_insert", record);
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int updateByPrimaryKey(Color record) {
		int rows = getSqlMapClientTemplate().update(
				"color.abatorgenerated_updateByPrimaryKey", record);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int updateByPrimaryKeySelective(Color record) {
		int rows = getSqlMapClientTemplate().update(
				"color.abatorgenerated_updateByPrimaryKeySelective", record);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public List selectByExample(ColorExample example) {
		List list = getSqlMapClientTemplate().queryForList(
				"color.abatorgenerated_selectByExample", example);
		return list;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public Color selectByPrimaryKey(Integer id) {
		Color key = new Color();
		key.setId(id);
		Color record = (Color) getSqlMapClientTemplate().queryForObject(
				"color.abatorgenerated_selectByPrimaryKey", key);
		return record;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int deleteByExample(ColorExample example) {
		int rows = getSqlMapClientTemplate().delete(
				"color.abatorgenerated_deleteByExample", example);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int deleteByPrimaryKey(Integer id) {
		Color key = new Color();
		key.setId(id);
		int rows = getSqlMapClientTemplate().delete(
				"color.abatorgenerated_deleteByPrimaryKey", key);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int countByExample(ColorExample example) {
		Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
				"color.abatorgenerated_countByExample", example);
		return count.intValue();
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int updateByExampleSelective(Color record, ColorExample example) {
		UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
		int rows = getSqlMapClientTemplate().update(
				"color.abatorgenerated_updateByExampleSelective", parms);
		return rows;
	}

	/**
	 * This method was generated by Abator for iBATIS. This method corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	public int updateByExample(Color record, ColorExample example) {
		UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
		int rows = getSqlMapClientTemplate().update(
				"color.abatorgenerated_updateByExample", parms);
		return rows;
	}

	/**
	 * This class was generated by Abator for iBATIS. This class corresponds to the database table color
	 * @abatorgenerated  Fri Jan 25 20:43:10 CST 2019
	 */
	private static class UpdateByExampleParms extends ColorExample {
		private Object record;

		public UpdateByExampleParms(Object record, ColorExample example) {
			super(example);
			this.record = record;
		}

		public Object getRecord() {
			return record;
		}
	}
}