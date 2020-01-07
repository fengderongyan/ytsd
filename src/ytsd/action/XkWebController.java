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

import com.alibaba.druid.support.json.JSONUtils;
import com.sgy.util.net.HttpClient;

import web.action.BaseController;
import ytsd.service.DoorInfoService;

@Controller
@RequestMapping("/xkweb")
public class XkWebController extends BaseController{
	
	@RequestMapping("/frame")
	public String frame() {
		
		return COM_PATH + "ytsd/xkweb/frame";
	}
	
	@RequestMapping("/list")
	public String list() throws Exception{
		String customer_name = req.getValue(request, "customer_name");
		String customer_mobile = req.getValue(request, "customer_mobile");
		String sale_name = req.getValue(request, "sale_name");
		String begin_date = req.getValue(request, "begin_date");
		String end_date = req.getValue(request, "end_date");
		int pageSize = req.getPageSize(request, "pageSize");
		Map paramMap = new HashMap();
		paramMap.put("customer_name", customer_name);
		paramMap.put("customer_mobile", customer_mobile);
		paramMap.put("sale_name", sale_name);
		paramMap.put("pageSize", pageSize + "");
		paramMap.put("begin_date", begin_date);
		paramMap.put("end_date", end_date);
		HttpClient httpClient = new HttpClient("https://ytsdxk.altmoose.com/mobile/xkinfo/getXkWebList.do");
		httpClient.setHttps(true);
		httpClient.setParameter(paramMap);
		httpClient.post();
		String result = httpClient.getContent();
		List list = (List)JSONUtils.parse(result);
		request.setAttribute("list", list);
		return COM_PATH + "ytsd/xkweb/list";
	}
	
	@RequestMapping("/editInfo")
	public String editInfo() throws Exception{
		String id = req.getValue(request, "id");
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		HttpClient httpClient = new HttpClient("https://ytsdxk.altmoose.com/mobile/xkinfo/getXkinfoById.do");
		httpClient.setHttps(true);
		httpClient.setParameter(paramMap);
		httpClient.post();
		String result = httpClient.getContent();
		Map map = (Map)JSONUtils.parse(result);
		System.out.println("map: " + map);
		request.setAttribute("map", map);
		return COM_PATH + "ytsd/xkweb/edit";
	}
	
	@RequestMapping("/saveEdit")
	public String saveEdit() throws Exception{
		String id = req.getValue(request, "id");
		String lf_date = req.getValue(request, "lf_date");
		String customer_name = req.getValue(request, "customer_name");
		String customer_mobile = req.getValue(request, "customer_mobile");
		String customer_sex = req.getValue(request, "customer_sex");
		String customer_age = req.getValue(request, "customer_age");
		String customer_job = req.getValue(request, "customer_job");
		String customer_address = req.getValue(request, "customer_address");
		String customer_hztj = req.getValue(request, "customer_hztj");
		String door_type = req.getValue(request, "door_type");
		String door_area = req.getValue(request, "door_area");
		String build_num = req.getValue(request, "build_num");
		String floor_num = req.getValue(request, "floor_num");
		String customer_yxcd = req.getValue(request, "customer_yxcd");
		String sale_name = req.getValue(request, "sale_name");
		String remark = req.getValue(request, "remark");
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		paramMap.put("lf_date", lf_date);
		paramMap.put("customer_name", customer_name);
		paramMap.put("customer_mobile", customer_mobile);
		paramMap.put("customer_sex", customer_sex);
		paramMap.put("customer_age", customer_age);
		paramMap.put("customer_job", customer_job);
		paramMap.put("customer_address", customer_address);
		paramMap.put("customer_hztj", customer_hztj);
		paramMap.put("door_type", door_type);
		paramMap.put("door_area", door_area);
		paramMap.put("build_num", build_num);
		paramMap.put("floor_num", floor_num);
		paramMap.put("customer_yxcd", customer_yxcd);
		paramMap.put("sale_name", sale_name);
		paramMap.put("remark", remark);
		HttpClient httpClient = new HttpClient("https://ytsdxk.altmoose.com/mobile/xkinfo/saveEdit.do");
		httpClient.setHttps(true);
		httpClient.setParameter(paramMap);
		httpClient.post();
		String result = httpClient.getContent();
		request.setAttribute("result", result);
		return COM_PATH + "ytsd/global/saveResult";
	}
	
	@RequestMapping("/deleteInfo")
	@ResponseBody
	public String deleteInfo() throws Exception{
		String id = req.getValue(request, "id");
		Map paramMap = new HashMap();
		paramMap.put("id", id);
		HttpClient httpClient = new HttpClient("https://ytsdxk.altmoose.com/mobile/xkinfo/deleteInfo.do");
		httpClient.setHttps(true);
		httpClient.setParameter(paramMap);
		httpClient.post();
		String result = httpClient.getContent();
		return result;
	}
}
