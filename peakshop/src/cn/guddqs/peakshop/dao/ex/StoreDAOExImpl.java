package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.StoreDAOImpl;
import cn.guddqs.peakshop.entity.Store;
import cn.guddqs.peakshop.util.Pagination;

public class StoreDAOExImpl extends StoreDAOImpl implements
StoreDAOEx {

	@Override
	public List<Store> getStoreListEx(Map<String, Object> map,
			Pagination page) {
		 List<Store> list = (List<Store>) getSqlMapClientTemplate()
	      .queryForList("store_ex.getStoreListEx",
				         map, page.getPageStartRow() - 1,
				         page.getPageSize());
		 return list;
	}

	@Override
	public int countByMap(Map<String, Object> map) {
		int count = (Integer) getSqlMapClientTemplate().queryForObject("store_ex.getCountEx", map);
		return count;
	}
	
	

}
