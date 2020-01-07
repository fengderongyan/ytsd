package ytsd.action;

import java.util.HashMap;
import java.util.List;
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
import ytsd.service.DoorInfoService;

@Controller
@RequestMapping("/doorinfo")
public class DoorInfoController extends BaseController{
	
	@Autowired
	public DoorInfoService doorInfoService;
	
	@RequestMapping("/doorInfoFrame.do")
	public String doorInfoFrame(HttpServletRequest request){
		request.setAttribute("buildNumList", doorInfoService.getBuildNumList(request));
		return COM_PATH + "ytsd/doorinfo/doorInfoFrame";
	}
	
	/**
	 * 描述：获取小区信息列表
	 * @param request
	 * @return
	 * @see ytsd.action.DoorInfoController#getDoorInfoList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getDoorInfoList.do")
	public String getDoorInfoList(HttpServletRequest request){
		request.setAttribute("list", doorInfoService.getDoorInfoList(request));
		return COM_PATH + "ytsd/doorinfo/doorInfoList";
	} 
	
	@RequestMapping("/doorInfoEdit.do")
	public String doorInfoEdit(HttpServletRequest request){
		String method = req.getValue(request, "method");
		request.setAttribute("buildNumList", doorInfoService.getBuildNumList(request));
		if(!"add".equals(method)){
			request.setAttribute("map", doorInfoService.getDoorInfoMap(request));
		}
		
		return COM_PATH + "ytsd/doorinfo/doorInfoEdit";
	}
	
	@RequestMapping("/saveOrUpdateInfo.do")
	public String saveOrUpdateInfo(HttpServletRequest request){
		int result = doorInfoService.saveOrUpdateInfo(request);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	}
	
	@RequestMapping("/deleteDoorInfo.do")
	public void deleteDoorInfo(HttpServletRequest request, HttpServletResponse response){
		int result = doorInfoService.deleteDoorInfo(request);
		this.writeText(result, response);
	}
	
	@RequestMapping("/uploadImgs.do")
	public String uploadImgs(HttpServletRequest request){
		return COM_PATH + "ytsd/doorinfo/uploadImgs";
	}
	
	@RequestMapping("/upload.do")
	@ResponseBody
	public Map upload(HttpServletRequest request, @RequestParam(value="file") CommonsMultipartFile file){
		String door_img = doorInfoService.upload(request, file);
		Map map = new HashMap();
		if(!"".equals(door_img)){
			map.put("code", 0);
			map.put("door_img", door_img);
		}
		return map;
	}
	
	/**
	 * 描述：保存图片路径
	 * @param request
	 * @param response
	 * @see ytsd.action.DoorInfoController#saveDoorImgs()
	 * @author zhangyongbin
	 */
	@RequestMapping("/saveDoorImgs.do")
	public void saveDoorImgs(HttpServletRequest request, HttpServletResponse response){
		int result = doorInfoService.saveDoorImgs(request, response);
		this.writeText(result, response);
	}
	
	/**
	 * 描述：查看 户型图
	 * @param request
	 * @return
	 * @see ytsd.action.DoorInfoController#showImgs()
	 * @author zhangyongbin
	 */
	@RequestMapping("/showImgs.do")
	public String showImgs(HttpServletRequest request){
		request.setAttribute("doorImgs", doorInfoService.showImgs(request));
		return COM_PATH + "ytsd/doorinfo/showImgs";
	}
	
	/**
	 * 描述：删除户型图
	 * @param request
	 * @param response
	 * @see ytsd.action.DoorInfoController#delImg()
	 * @author zhangyongbin
	 */
	@RequestMapping("/delImg.do")
	public void delImg(HttpServletRequest request, HttpServletResponse response){
		int result = doorInfoService.delImg(request);
		this.writeText(result, response);
	}
	
	/**
	 * 描述：获取房屋销售状态
	 * @param request
	 * @param response
	 * @author yanbs
	 * @Date : 2019-07-04
	 */
	@RequestMapping("/getSaleStatus.do")
	public void getSaleStatus(HttpServletRequest request,  HttpServletResponse response){
		String status = doorInfoService.getSaleStatus(request);
		this.writeText(status, response);
	}
	
	/**
	 * 描述：填写售出界面
	 * @param request
	 * @return
	 * @see ytsd.action.DoorInfoController#saleDoorFrame()
	 * @author zhangyongbin
	 */
	@RequestMapping("/saleDoorFrame.do")
	public String saleDoorFrame(HttpServletRequest request){
		request.setAttribute("saleList", doorInfoService.getSaleList(request));
		return COM_PATH + "ytsd/doorinfo/saleDoorFrame";
	}
	

	@RequestMapping("/saleInfoAjax.do")
	@ResponseBody
	public Map saleInfoAjax(HttpServletRequest request){
		return doorInfoService.saleInfoAjax(request);
	}
	
	/**
	 * 描述：保存售出信息
	 * @param request
	 * @return
	 * @see ytsd.action.DoorInfoController#saveSaleInfo()
	 * @author zhangyongbin
	 */
	@RequestMapping("/saveSaleInfo.do")
	public String saveSaleInfo(HttpServletRequest request){
		int result = doorInfoService.saveSaleInfo(request);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	}
	
	/**
	 * 描述：打印回执
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-24
	 */
	@RequestMapping("/salePrint.do")
	public String salePrint(HttpServletRequest request){
		request.setAttribute("map", doorInfoService.getDoorInfoMap(request));
		return COM_PATH + "ytsd/doorinfo/salePrint";
	}
	
	/**
	 * 描述：退房
	 * @param request
	 * @param response
	 * @author yanbs
	 * @Date : 2019-07-05
	 */
	@RequestMapping("/tuiFang.do")
	@ResponseBody
	public void tuiFang(HttpServletRequest request,  HttpServletResponse response){
		int result = doorInfoService.tuiFang(request);
		this.writeText(result, response);
	}

}
