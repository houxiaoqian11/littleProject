package cn.guddqs.peakshop.front.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.AddressInfoDAO;
import cn.guddqs.peakshop.entity.AddressInfo;
import cn.guddqs.peakshop.entity.AddressInfoExample;

@Controller
public class AddressController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AddressInfoDAO addressInfoDAO;

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/address/getlist")
	public Map<String, Object> addr_getList(Integer userId) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		AddressInfoExample example = new AddressInfoExample();
		List<AddressInfo> alist = null;
		if(userId != null){
			example.createCriteria().andUserIdEqualTo(userId);
			try{
				alist = addressInfoDAO.selectByExample(example);
			}catch(Exception e){
				logger.error("查询所有地址出现异常",e);
			}
		}
		returnMap.put("addrlist", alist);
		logger.info("共有"+alist.size()+"条地址数据");
		return returnMap;
	}

	@ResponseBody
	@RequestMapping("/address/get")
	public AddressInfo get(Integer id) {
		AddressInfo addressInfo = new AddressInfo();
		if(id == null){
			return addressInfo;
		}else{
			addressInfo = addressInfoDAO.selectByPrimaryKey(id);
			return addressInfo;
		}
	}

	@ResponseBody
	@RequestMapping("/address/bedefault")
	public Map<String, String> addr_bedefault(Integer id, Integer userId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("flag", "false");
		if (id != null) {
			cancelDefault(userId);
			AddressInfo addressInfo = new AddressInfo();
			addressInfo.setId(id);
			addressInfo.setIsDefault(true);
			addressInfoDAO.updateByPrimaryKeySelective(addressInfo);
			map.put("flag", "true");
		}
		return map;

	}

	@ResponseBody
	@RequestMapping("/address/del")
	public Map<String, String> addr_del(Integer id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("flag", "false");
		if (id != null) {
			addressInfoDAO.deleteByPrimaryKey(id);
			map.put("flag", "true");
		}
		return map;

	}

	@ResponseBody
	@RequestMapping("/address/saveOrUpdate")
	public Map<String, String> saveOrUpdate(@RequestBody AddressInfo addr) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("flag", "false");
		try{
			if (addr.getId() != null) {
				//如果该地址设为了默认地址，则之前的默认地址设置为不默认
				if(addr.getIsDefault() !=null && addr.getIsDefault()){
					cancelDefault(addr.getUserId());
				}
				addressInfoDAO.updateByPrimaryKeySelective(addr);
				map.put("flag", "true");
			} else {
				addr.setStartTime(new Date());
				addr.setEndTime(new Date());
				addressInfoDAO.insert(addr);
			}
		}catch(Exception e){
			logger.error("保存/修改地址异常",e);
		}
		map.put("flag", "true");
		return map;

	}
	
	//取消默认
	@SuppressWarnings("unchecked")
	private void cancelDefault(Integer userId){
		AddressInfoExample example = new AddressInfoExample();
		example.createCriteria().andUserIdEqualTo(userId).andIsDefaultEqualTo(true);
		List<AddressInfo> infos = addressInfoDAO.selectByExample(example);
		for(AddressInfo info : infos){
			info.setIsDefault(false);
			addressInfoDAO.updateByPrimaryKeySelective(info);
		}
	}

}
