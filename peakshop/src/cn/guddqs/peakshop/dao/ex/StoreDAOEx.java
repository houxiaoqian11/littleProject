package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.StoreDAO;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.util.Pagination;

public interface StoreDAOEx extends StoreDAO {
	/**
	 * 分页获取仓储信息
	 * @param map
	 * @param page
	 * @return
	 */
	public List<Store> getStoreListEx(Map<String, Object> map, Pagination page);
	
	public int countByMap(Map<String, Object> map);

}
