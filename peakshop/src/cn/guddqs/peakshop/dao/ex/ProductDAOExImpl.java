package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.ProductDAOImpl;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.util.Pagination;

public class ProductDAOExImpl extends ProductDAOImpl implements
		ProductDAOEx {

	@Override
	public Integer insertReturnKey(Product record) {
		Integer id = (Integer) getSqlMapClientTemplate().insert("product_ex.abatorgenerated_insert", record);
		 return id;
	}
	
	@Override
	public List<Product> getProductListEx(
			Map<String, Object> map, Pagination page) {
		 List<Product> list = (List<Product>) getSqlMapClientTemplate()
									      .queryForList("product_ex.getProductListEx",
												         map, page.getPageStartRow() - 1,
												         page.getPageSize());
		return list;
	}

}
