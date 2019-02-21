package cn.guddqs.peakshop.front.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.guddqs.peakshop.dao.VideoDAO;
import cn.guddqs.peakshop.entity.Video;
import cn.guddqs.peakshop.entity.VideoExample;

@Controller
public class VideoControll {

	@Autowired
	private VideoDAO videoDAO;
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/video/get")
	public Map<String, String> get() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		VideoExample videoExample = new VideoExample();
		List<Video> videos = videoDAO.selectByExample(videoExample);
		if(videos.size()<1){
			map.put("flag", "0");
			map.put("errMsg", "没有门店视频");
		}else{
			String path = videos.get(0).getPath();
			map.put("flag", "1");
			map.put("path", path);
		}
		return map;
	}
}
