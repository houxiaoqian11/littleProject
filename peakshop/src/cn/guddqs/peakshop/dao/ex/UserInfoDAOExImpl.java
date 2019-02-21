package cn.guddqs.peakshop.dao.ex;

import cn.guddqs.peakshop.dao.UserInfoDAOImpl;
import cn.guddqs.peakshop.entity.UserInfo;

public class UserInfoDAOExImpl extends UserInfoDAOImpl implements UserInfoDAOEx {

	@Override
	public Integer insertReturnKey(UserInfo userInfo) {
		Integer id = (Integer) getSqlMapClientTemplate().insert("user_info_ex.abatorgenerated_insert", userInfo);
		 return id;
	}

}
