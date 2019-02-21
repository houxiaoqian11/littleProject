package cn.guddqs.peakshop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.guddqs.peakshop.dao.VideoDAO;
import cn.guddqs.peakshop.entity.Video;
import cn.guddqs.peakshop.entity.VideoExample;


@Service
public class VideoService {
	
	@Autowired
	private VideoDAO videoDAO;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());;
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT,rollbackFor = Throwable.class)
	public void add(HttpServletRequest request, HttpServletResponse resp) throws Exception{
		logger.info("开始添加门店视频");
		try{
			String path = request.getParameter("video");
			VideoExample videoExample = new VideoExample();
			videoDAO.deleteByExample(videoExample);
			Video video = new Video();
			video.setPath(path);
			videoDAO.insert(video);
		}catch(Exception e){
			logger.error("添加门店视频异常",e);
			throw new Exception("添加门店视频异常",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getVideoPath() throws Exception{
		VideoExample videoExample = new VideoExample();
		List<Video> videos = videoDAO.selectByExample(videoExample);
		if(videos.size()<1){
			return null;
		}else{
			String path = videos.get(0).getPath();
			return path;
		}
	}
	
}
