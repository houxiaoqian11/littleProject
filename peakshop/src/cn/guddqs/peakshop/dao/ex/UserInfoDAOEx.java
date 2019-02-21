package cn.guddqs.peakshop.dao.ex;

import cn.guddqs.peakshop.dao.UserInfoDAO;
import cn.guddqs.peakshop.entity.Cart;
import cn.guddqs.peakshop.entity.UserInfo;

public interface UserInfoDAOEx extends UserInfoDAO {

	/**
	 * 保存用户信息并返回主键
	 * @param map
	 * @param page
	 * @return
	 */
	public Integer insertReturnKey(UserInfo userInfo);
	
}
