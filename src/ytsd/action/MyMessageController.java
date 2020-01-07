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
import ytsd.service.DoorInfoService;
import ytsd.service.MyMessageService;

@Controller
@RequestMapping("/myMessage")
public class MyMessageController extends BaseController{
	
	@Autowired
	public MyMessageService myMessageService;
	
	@RequestMapping("/messageFrame.do")
	public String messageFrame(){
		return COM_PATH + "ytsd/mymessage/messageFrame";
	}
	
	/**
	 * 描述：获取小区信息列表
	 * @param request
	 * @return
	 * @see ytsd.action.MyMessageController#getDoorInfoList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/messageList.do")
	public String messageList(HttpServletRequest request){
		request.setAttribute("list", myMessageService.getMessageList(request));
		return COM_PATH + "ytsd/mymessage/messageList";
	} 
	
	@RequestMapping("/messageEdit.do")
	public String messageEdit(HttpServletRequest request){
		String method = req.getValue(request, "method");
		if(!"add".equals(method)){
			request.setAttribute("map", myMessageService.getMessageMap(request));
		}
		
		return COM_PATH + "ytsd/mymessage/messageEdit";
	}
	
	@RequestMapping("/saveOrUpdateInfo.do")
	public String saveOrUpdateInfo(HttpServletRequest request){
		int result = myMessageService.saveOrUpdateInfo(request);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	}
	
	@RequestMapping("/deleteMessage.do")
	public void deleteMessage(HttpServletRequest request, HttpServletResponse response){
		int result = myMessageService.deleteMessage(request);
		this.writeText(result, response);
	}
	
	@RequestMapping("/upload.do")
	@ResponseBody
	public Map upload(HttpServletRequest request, @RequestParam(value="file") CommonsMultipartFile file){
		String door_img = myMessageService.upload(request, file);
		Map map= new HashMap();
		map.put("error", 0);
		map.put("url", door_img);
		return map;
	}
	
	/**
	 * 描述：推送消息
	 * @param request
	 * @see ytsd.action.MyMessageController#tsInfo()
	 * @author zhangyongbin
	 */
	@RequestMapping("/tsInfo.do")
	public void tsInfo(HttpServletRequest request, HttpServletResponse response){
		int result = myMessageService.tsInfo(request);
		this.writeText(result, response);
	}
}
