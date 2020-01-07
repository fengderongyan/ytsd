package ytsd.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;
import ytsd.service.SaleGroupService;

@Controller
@RequestMapping("/salegroup")
public class SaleGroupController extends BaseController{
	@Autowired
	private SaleGroupService saleGroupService;
	
	@RequestMapping("/frame")
	public String frame(){
		return COM_PATH + "ytsd/salegroup/frame";
	}
	
	/**
	 * 描述：获取销售小组列表
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-21
	 */
	@RequestMapping("/list.do")
	public String list(HttpServletRequest request){
		request.setAttribute("list", saleGroupService.getGroupList(request));
		return COM_PATH + "ytsd/salegroup/list";
	}
	
	@RequestMapping("/edit.do")
	public String edit(HttpServletRequest request){
		String method = req.getValue(request, "method");
		if(!"add".equals(method)){
			request.setAttribute("map", saleGroupService.groupMap(request));
		}
		return COM_PATH + "ytsd/salegroup/edit";
	}
	
	
	@RequestMapping("/saveOrUpdateInfo.do")
	public String saveOrUpdateInfo(HttpServletRequest request){
		int result = saleGroupService.saveOrUpdateInfo(request);
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	} 
	
	@RequestMapping("/deleteInfo.do")
	public void deleteInfo(HttpServletRequest request, HttpServletResponse response){
		int result = saleGroupService.deleteInfo(request);
		this.writeText(result, response);
	} 
}
