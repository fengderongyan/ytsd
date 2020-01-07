package ytsd.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;
import ytsd.service.SaleStatusService;

@Controller
@RequestMapping("/salestatus")
public class SaleStatusController extends BaseController{
	@Autowired
	private SaleStatusService saleStatusService;
	
	@RequestMapping("/frame")
	public String frame(){
		return COM_PATH + "ytsd/salestatus/frame";
	}
	
	/**
	 * 描述：获取楼栋列表
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-21
	 */
	@RequestMapping("/list.do")
	public String list(HttpServletRequest request){
		request.setAttribute("list", saleStatusService.getBuildList(request));
		return COM_PATH + "ytsd/salestatus/list";
	}
	
	@RequestMapping("/edit.do")
	public String edit(HttpServletRequest request){
		String method = req.getValue(request, "method");
		if(!"add".equals(method)){
			request.setAttribute("map", saleStatusService.buildMap(request));
		}
		return COM_PATH + "ytsd/salestatus/edit";
	}
	
	@RequestMapping("/saveOrUpdateInfo.do")
	public String saveOrUpdateInfo(HttpServletRequest request){
		int result = saleStatusService.saveOrUpdateInfo(request);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	} 
	
	@RequestMapping("/deleteInfo.do")
	public void deleteInfo(HttpServletRequest request, HttpServletResponse response){
		int result = saleStatusService.deleteInfo(request);
		this.writeText(result, response);
	} 
}
