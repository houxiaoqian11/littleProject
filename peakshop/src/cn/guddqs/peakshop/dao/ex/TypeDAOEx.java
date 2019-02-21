package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.TypeDAO;
import cn.guddqs.peakshop.entity.Type;
import cn.guddqs.peakshop.util.Pagination;

public interface TypeDAOEx extends TypeDAO {
	/**
	 * 分页获取产品类型信息
	 * @param map
	 * @param page
	 * @return
	 */
	public List<Type> getTypeListEx(Map<String, Object> map, Pagination page);
}
