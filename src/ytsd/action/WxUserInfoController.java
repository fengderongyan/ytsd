package ytsd.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;
import ytsd.service.WxUserInfoService;

@Controller
@RequestMapping("/wxuserinfo")
public class WxUserInfoController extends BaseController{
	
	@Autowired
	public WxUserInfoService wxUserInfoService;
	
	@RequestMapping("/getWxUserFrame.do")
	public String getWxUserFrame(){
		List saleGroupList = wxUserInfoService.getSaleGroupList(request);
		request.setAttribute("saleGroupList", saleGroupList);
		return COM_PATH + "ytsd/wxuserinfo/wxUserFrame";
	}
	
	/**
	 * 描述：获取小区信息列表
	 * @param request
	 * @return
	 * @see ytsd.action.DoorInfoController#getDoorInfoList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getWxUserList.do")
	public String getWxUserList(HttpServletRequest request){
		request.setAttribute("list", wxUserInfoService.getWxUserList(request));
		return COM_PATH + "ytsd/wxuserinfo/wxUserList";
	} 
	
	@RequestMapping("/wxUserEdit.do")
	public String wxUserEdit(HttpServletRequest request){
		String method = req.getValue(request, "method");
		List saleGroupList = wxUserInfoService.getSaleGroupList(request);
		request.setAttribute("saleGroupList", saleGroupList);
		if(!"add".equals(method)){
			request.setAttribute("map", wxUserInfoService.getWxUserMap(request));
		}
		
		return COM_PATH + "ytsd/wxuserinfo/wxUserEdit";
	}
	
	@RequestMapping("/saveOrUpdateInfo.do")
	public String saveOrUpdateInfo(HttpServletRequest request){
		int result = wxUserInfoService.saveOrUpdateInfo(request);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	}
	
	/**
	 * 描述：删除
	 * @param request
	 * @param response
	 * @see ytsd.action.WxUserInfoController#deleteWxUserInfo()
	 * @author zhangyongbin
	 */
	@RequestMapping("/deleteWxUserInfo.do")
	public void deleteWxUserInfo(HttpServletRequest request, HttpServletResponse response){
		int result = wxUserInfoService.deleteWxUserInfo(request);
		this.writeText(result, response);
	}
	
	/**
	 * 描述：恢复
	 * @param request
	 * @param response
	 * @see ytsd.action.WxUserInfoController#backWxUserInfo()
	 * @author zhangyongbin
	 */
	@RequestMapping("/backWxUserInfo.do")
	public void backWxUserInfo(HttpServletRequest request, HttpServletResponse response){
		int result = wxUserInfoService.backWxUserInfo(request);
		this.writeText(result, response);
	}
	
	/**
	 * 描述：检查号码是否已存在
	 * @see ytsd.action.WxUserInfoController#checkMobile()
	 * @author zhangyongbin
	 */
	@RequestMapping("/checkMobile.do")
	public void checkMobile(HttpServletRequest request, HttpServletResponse response){
		int result = wxUserInfoService.checkMobile(request);
		this.writeText(result, response);
	}
	

}
