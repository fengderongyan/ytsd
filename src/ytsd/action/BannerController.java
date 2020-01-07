package ytsd.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import web.action.BaseController;
import ytsd.service.BannerService;
import ytsd.service.DoorInfoService;
import ytsd.service.MyMessageService;

@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController{
	
	@Autowired
	public BannerService bannerService;
	
	@RequestMapping("/bannerFrame.do")
	public String bannerFrame(){
		return COM_PATH + "ytsd/banner/bannerFrame";
	}
	
	/**
	 * 描述：获取小区信息列表
	 * @param request
	 * @return
	 * @see ytsd.action.BannerController#getDoorInfoList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/bannerList.do")
	public String bannerList(HttpServletRequest request){
		request.setAttribute("list", bannerService.getBannerList(request));
		return COM_PATH + "ytsd/banner/bannerList";
	} 
	
	@RequestMapping("/bannerEdit.do")
	public String bannerEdit(HttpServletRequest request){
		String method = req.getValue(request, "method");
		if(!"add".equals(method)){
			request.setAttribute("map", bannerService.getBannerMap(request));
		}
		
		return COM_PATH + "ytsd/banner/bannerEdit";
	}
	
	@RequestMapping("/saveOrUpdateInfo.do")
	public String saveOrUpdateInfo(HttpServletRequest request, @RequestParam(value="file") CommonsMultipartFile file){
		int result = bannerService.saveOrUpdateInfo(request, file);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	}
	
	@RequestMapping("/deleteMessage.do")
	public void deleteMessage(HttpServletRequest request, HttpServletResponse response){
		int result = bannerService.deleteMessage(request);
		this.writeText(result, response);
	}
	
	
}
