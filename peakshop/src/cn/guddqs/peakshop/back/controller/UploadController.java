package cn.guddqs.peakshop.back.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sumavision.tvpay.spring.resources.ReloadablePlaceHolderResourceBundleMessageSource;

/**
 * 上传图片
 * @author lx
 *
 */
@Controller
public class UploadController {
	
	@Autowired
	private ReloadablePlaceHolderResourceBundleMessageSource ppc;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	//批量上传图片   file
	@RequestMapping(value = "/upload/batchUploadPic.do")
	public void batchUploadPic(@RequestParam(required = false) MultipartFile[] file, HttpServletResponse response) throws Exception{
		//每次都取最后一个图片
		if(file.length<1){
			return;
		}
		MultipartFile lastFile = file[file.length-1];
		String fileOriName = lastFile.getOriginalFilename();
		String fileName = UUID.randomUUID().toString()
				+ fileOriName.substring(fileOriName.lastIndexOf("."),
						fileOriName.length());
		String path = ppc.getMessage("filePath");
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			lastFile.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jo = new JSONObject();
		jo.put("path", ppc.getMessage("accessPath") + targetFile.getName());
		jo.put("url", ppc.getMessage("accessPath") + targetFile.getName());
		//设置成json
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		
	}
	
//	//上传图片   file
//	@RequestMapping(value = "/upload/uploadPic.do")
//	public void uploadPic(@RequestParam(required = false) MultipartFile file, HttpServletResponse response) throws Exception{
//		String fileOriName = file.getOriginalFilename();
//		String fileName = UUID.randomUUID().toString()
//				+ fileOriName.substring(fileOriName.lastIndexOf("."),
//						fileOriName.length());
//		String path = "E:\\apache-tomcat-7.0.65\\webapps\\peakshop\\pictures\\";
//		File targetFile = new File(path, fileName);
//		if (!targetFile.exists()) {
//			targetFile.mkdirs();
//		}
//		try {
//			file.transferTo(targetFile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		JSONObject jo = new JSONObject();
//		jo.put("path", "http://127.0.0.1:8080/peakshop/pictures/" + targetFile.getName());
//		jo.put("url", "http://127.0.0.1:8080/peakshop/pictures/" + targetFile.getName());
//		//设置成json
//		response.setContentType("application/json;charset=UTF-8");
//		response.getWriter().write(jo.toString());
//		
//	}
	
	//上传图片   file
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(@RequestParam(required = false) MultipartFile file, HttpServletResponse response) throws Exception{
		logger.info("开始上传图片");
		String fileOriName = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString()
				+ fileOriName.substring(fileOriName.lastIndexOf("."),
						fileOriName.length());
		String path = ppc.getMessage("filePath");
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jo = new JSONObject();
		jo.put("path", ppc.getMessage("accessPath") + targetFile.getName());
		jo.put("url", ppc.getMessage("accessPath") + targetFile.getName());
		//设置成json
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		
	}
	
	//上传门店视频
	@RequestMapping(value = "/upload/uploadVideo.do")
	public void uploadVideo(@RequestParam(required = false) MultipartFile file, HttpServletResponse response) throws Exception{
		logger.info("开始上传门店视频");
		String fileOriName = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString()
				+ fileOriName.substring(fileOriName.lastIndexOf("."),
						fileOriName.length());
		String path = ppc.getMessage("filePath");
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jo = new JSONObject();
		jo.put("path", ppc.getMessage("accessPath") + targetFile.getName());
		jo.put("url", ppc.getMessage("accessPath") + targetFile.getName());
		//设置成json
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		
	}
	
}
