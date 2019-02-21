package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.TypeDAOImpl;
import cn.guddqs.peakshop.entity.Type;
import cn.guddqs.peakshop.util.Pagination;

public class TypeDAOExImpl extends TypeDAOImpl implements
		TypeDAOEx {

	@Override
	public List<Type> getTypeListEx(Map<String, Object> map,
			Pagination page) {
		 List<Type> list = (List<Type>) getSqlMapClientTemplate()
	      .queryForList("type_ex.getTypeListEx",
				         map, page.getPageStartRow() - 1,
				         page.getPageSize());
		 return list;
	}

}
