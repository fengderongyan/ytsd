package ytsd.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import web.action.BaseController;
import ytsd.service.SaleRptService;

@Controller
@RequestMapping("/salerpt")
public class SaleRptController extends BaseController{
	
	@Autowired
	private SaleRptService saleRptService;
	
	@RequestMapping("/getRptFrame")
	public String getRptFrame(){
		List saleGroupList = saleRptService.getSaleGroupList(request);
		request.setAttribute("saleGroupList", saleGroupList);
		return COM_PATH + "ytsd/salerpt/rptFrame";
	}
	
	/**
	 * 描述：已成交列表
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-11
	 */
	@RequestMapping("/getSaledList")
	public String getSaledList(){
		request.setAttribute("list", saleRptService.getSaledList(request));
		return COM_PATH + "ytsd/salerpt/saledList";
	}
	
	@RequestMapping("/getYudingList")
	public String getYudingList(){
		request.setAttribute("list", saleRptService.getYudingList(request));
		return COM_PATH + "ytsd/salerpt/yudingList";
	}

	
}
