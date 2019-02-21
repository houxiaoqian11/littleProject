package cn.guddqs.peakshop.dao.ex;

import java.util.List;
import java.util.Map;

import cn.guddqs.peakshop.dao.ProductDAO;
import cn.guddqs.peakshop.entity.Product;
import cn.guddqs.peakshop.util.Pagination;

public interface ProductDAOEx extends ProductDAO {
	/**
	 * 保存产品信息并返回主键
	 * @param map
	 * @param page
	 * @return
	 */
	public Integer insertReturnKey(Product record);
	
	/**
	 * 分页获取产品信息
	 * @param map
	 * @param page
	 * @return
	 */
	public List<Product> getProductListEx(Map<String, Object> map, Pagination page);
	
}
