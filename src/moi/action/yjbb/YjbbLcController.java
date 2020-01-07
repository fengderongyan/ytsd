package moi.action.yjbb;

import ytsd.service.login.LoginService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import moi.service.yjbb.YjbbLcSevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.common.StringHelper;

import web.action.BaseController;
import web.model.User;

/** 
 * 手机端流程处理
 * @date 2019-1-15
 */
@Controller
@RequestMapping(value = "/mobile/yjbb/")
public class YjbbLcController extends BaseController{

    @Autowired
    YjbbLcSevice yjbbLcSevice;
    
    private String file;
    private String fileFileName;
    public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

    public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
    /**
     * 流程概述
     * 请假：
     * 中队级发起-----大队审核-------干部科科长审核-----政治处主任审核
     * 大队级发起（）
     * 
     * 
     */
	
    /**
     * 主页判断(进行中、已结束、待审核)
     * @param response
     */
    @RequestMapping(value="yjbbMain.do")
    public void yjbbMain(HttpServletResponse response) {
    	
    	Map returnMap = new HashMap();
		Map map = new HashMap();
		
		//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		//logger.debug("checkUserInfo : " + checkUserInfo);
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
		
		try {
			String type = req.getValue(request, "type");//类型：1 请假； 2 饮酒
			String tabType = req.getValue(request, "tabType");//展示分页标记: carry 进行中； ended  已结束； audit 待审核
			logger.debug("tabType : " + tabType);
			map = new HashMap();
			List <Map<String, Object>> dataList = null;
			//logger.debug("type : " + type);
			Boolean isQj = null;
			Boolean isCarry = null;
			Boolean isEnded = null;
			Boolean isAudit = null;
			//空值处理
			if(!"".equals(type)){
				isQj = "1".equals(type) ? true : false;
			}else{
				returnMap.put("errorCode", -1);
				returnMap.put("errorInfo", "can not get param named 'type'");
				this.writeJsonData(returnMap, response);
				return ;
			}
			if(!"".equals(tabType)){
				isCarry = "carry".equals(tabType) ? true : false;
				isEnded = "ended".equals(tabType) ? true : false;
				isAudit = "audit".equals(tabType) ? true : false;
			}else{
				returnMap.put("errorCode", -1);
				returnMap.put("errorInfo", "can not get param named 'tabType'");
				this.writeJsonData(returnMap, response);
				return ;
			}
			
			String dataTypeCode = type.equals("1") ? "10032" : "10033";
			//待审核条数
			List list = yjbbLcSevice.getLcAuditList(request, type, tabType, dataTypeCode);
			String dshNum = list.size()+"";
			//String dshNum = "1";
			//消息是否展示未读角标
			int tabNum = yjbbLcSevice.getTabPushNum();
			String readNumber = yjbbLcSevice.getReadPushNum(request).equals("") ? "0" : yjbbLcSevice.getReadPushNum(request) ;
			int readNum = Integer.valueOf(readNumber);
			
			String isRead = tabNum > readNum ? "1" : "0";
			logger.debug("isRead : " + isRead);
			if( isCarry){// isQj && isCarry
				//获取本人申请、审核通过、未结束的所有报备信息
				dataList = yjbbLcSevice.getLcCarryList(request, type, tabType, dataTypeCode);
				returnMap.put("errorCode", 0);
				map.put("bbInfoList", dataList);
				map.put("audit_num", dshNum);
				map.put("isRead", isRead);
				returnMap.put("data", map);
				this.writeJsonData(returnMap, response);
				return ;
			}
			//已结束
			if(isEnded){// isQj && isCarry
				//获取本人申请、已结束的所有报备信息（包括已销假和流程通过）
				dataList = yjbbLcSevice.getLcEndedList(request, type, tabType, dataTypeCode);
				returnMap.put("errorCode", 0);
				map.put("bbInfoList", dataList);
				map.put("audit_num", dshNum);
				map.put("isRead", isRead);
				returnMap.put("data", map);
				this.writeJsonData(returnMap, response);
				return ;
			}
			//待审核
			if( isAudit){// isQj && isCarry
				//获取本人申请、审核通过、已结束的所有报备信息
				dataList = yjbbLcSevice.getLcAuditList(request, type, tabType, dataTypeCode);
				returnMap.put("errorCode", 0);
				map.put("bbInfoList", dataList);
				map.put("audit_num", dshNum);
				map.put("isRead", isRead);
				returnMap.put("data", map);
				this.writeJsonData(returnMap, response);
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", e.getMessage());
			this.writeJsonData(returnMap, response);
			return ;
		}
		
    }
    
    /**
     * 信息报备(立即报备)
     * @date 2019-1-16
     * @param response
     */
    @RequestMapping(value="yjbbInfobb.do")
    public void yjbbInfobb(HttpServletResponse response) {
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	try {
			String type = req.getValue(request, "type");//类型：1 请假； 2 饮酒
			//logger.debug("1");
			
			//空值处理
			if("".equals(type)){
				returnMap.put("errorCode", -1);
				returnMap.put("errorInfo", "can not get param named 'type'");
				this.writeJsonData(returnMap, response);
				return ;
			}
			
			List concreteTypeList = new ArrayList();
			concreteTypeList = yjbbLcSevice.getConcreteTypeList(type);
			
			returnMap.put("errorCode", 0);
			map.put("concreteTypeList", concreteTypeList);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", e.getMessage());
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 保存报备提交（立即提交）
     * @date 2019-1-16
     * @param response
     */
    @RequestMapping(value="saveBbInfo.do")
    public void saveBbInfo(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	int saveResult = 0;
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	String type = req.getValue(request, "type");//类型：1 请假； 2 饮酒
    	String cpNum = req.getValue(request, "cpNum");//车牌（饮酒填写）
		//空值处理
		if("".equals(type)){
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "can not get param named 'type'");
			this.writeJsonData(returnMap, response);
			return ;
		}
		if("2".equals(type) && cpNum.equals("")){//饮酒
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "can not get param named 'cpNum'");
			this.writeJsonData(returnMap, response);
			return ;
		}
		
		saveResult = yjbbLcSevice.getSaveBbInfoResult(request, type, cpNum) ;
		if(saveResult == 1){
			returnMap.put("errorCode", 0);
			map.put("saveResult", saveResult);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		}else{
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Save failed!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 报备信息详情
     * 包括：报备基本信息、审核信息
     * @date 2019-1-17
     * @param response
     */
    @RequestMapping(value="showBbInfo.do")
    public void showBbInfo(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	try {
			String type = req.getValue(request, "type");//类型，1：请假；2：饮酒
			
			//空值处理
			if("".equals(type)){
				returnMap.put("errorCode", -1);
				returnMap.put("errorInfo", "can not get param named 'type'");
				this.writeJsonData(returnMap, response);
				return ;
			}
			String dataTypeCode = type.equals("1") ? "10032" : "10033";
			List concreteTypeList = new ArrayList();
			concreteTypeList = yjbbLcSevice.getConcreteTypeList(type);
			
			map.putAll(yjbbLcSevice.getBbDetail(request, type, dataTypeCode));//baseInfo 基本信息  shlcList 审核流程
			map.put("concreteList", concreteTypeList);//类别列表
			returnMap.put("errorCode", 0);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		} catch (Exception e) {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", e.getMessage());
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 报备审核
     * @date 2019-1-17
     * @param response
     */
    @RequestMapping(value="shBbInfo.do")
    public void shBbInfo(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	int saveResult = 0;
    	String type = req.getValue(request, "type");//类型：1 请假； 2 饮酒
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
		//空值处理
		if("".equals(type)){
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "can not get param named 'type'");
			this.writeJsonData(returnMap, response);
			return ;
		}
		saveResult = yjbbLcSevice.getShResult(request, type);
		if(saveResult == 1){//处理成功
			returnMap.put("errorCode", 0);
			map.put("shResult", saveResult);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		}else {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Audit failure!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    
    /**
     * 批量通过
     * @date 2019-1-25
     * @param response
     */
    @RequestMapping(value="plTg.do")
    public void plTg(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	int saveResult = 0;
    	String type = req.getValue(request, "type");//类型：1 请假； 2 饮酒
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
		//空值处理
		if("".equals(type)){
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "can not get param named 'type'");
			this.writeJsonData(returnMap, response);
			return ;
		}
		saveResult = yjbbLcSevice.getPlShResult(request, type);
		if(saveResult == 1){//处理成功
			returnMap.put("errorCode", 0);
			map.put("plShResult", saveResult);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		}else {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Audit failure!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 销假处理
     * @date 2019-1-17
     * @param response
     */
    @RequestMapping(value="dealXj.do")
    public void dealXj(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	int xjResult = 0;
    	int cs = 0;
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
    	xjResult = yjbbLcSevice.getXjResult(request);
    	cs = Integer.valueOf(yjbbLcSevice.getQjcsDays(request));
    	
    	cs = cs < 0 ? 0 : cs;//负数处理
    	
    	if(xjResult == 1){//处理成功
    		returnMap.put("errorCode", 0);
    		map.put("xjResult", xjResult);
    		map.put("csDays", cs);
    		returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		}else {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Audit failure!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 归家处理
     * @date 2019-1-23
     * @param response
     */
    @RequestMapping(value="dealGj.do")
    public void dealGj(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	int xjResult = 0;
    	int cs = 0;
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
    	xjResult = yjbbLcSevice.getGjResult(request);
    	cs = Integer.valueOf(yjbbLcSevice.getYjcsDays(request));
    	cs = cs < 0 ? 0 : cs;//负数处理
    	if(xjResult == 1){//处理成功
    		returnMap.put("errorCode", 0);
    		map.put("gjResult", xjResult);
    		map.put("csDays", cs);
    		returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		}else {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Audit failure!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 已审核主页
     * @date 2019-01-24
     * @param response
     */
    @RequestMapping(value="auditedMain.do")
    public void auditedMain(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
    	try {
			map.put("orgsList", yjbbLcSevice.getOrgsList(request));
			returnMap.put("data", map);
			returnMap.put("errorCode", 0);
			this.writeJsonData(returnMap, response);
			return ;
		} catch (Exception e) {
			returnMap.put("errorInfo", e.toString());
			returnMap.put("errorCode", -1);
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 已审核列表
     * @date 2019-01-26
     * @param response
     */
    @RequestMapping(value="auditedList.do")
    public void auditedList(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	List list = new ArrayList();
    	String type = req.getValue(request, "type");// 	类型：1 请假； 2 饮酒
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
    	String dataTypeCode = type.equals("1") ? "10032" : "10033";
    	try {
			list = yjbbLcSevice.getAuditedList(request, dataTypeCode);
			
			map.put("auditedList", list);
			//map.put("orgsList", yjbbLcSevice.getOrgsList(request));
			returnMap.put("data", map);
			returnMap.put("errorCode", 0);
			this.writeJsonData(returnMap, response);
			return ;
		} catch (Exception e) {
			returnMap.put("errorInfo", e.toString());
			returnMap.put("errorCode", -1);
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    
    /**
     * 上传头像
     * 
     * @param base64
     */
    @RequestMapping(value="scTx.do")
    public void scTx(String base64){
    	
    }
    
    /**
     * 上传头像
     * @date 2019-1-18
     * @return
     */
    @RequestMapping(value="impTx.do")
    public void impTx(@RequestParam(value="file") CommonsMultipartFile file) {
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	
        fileFileName = file.getOriginalFilename();
        int upResult = 0;
        upResult =  yjbbLcSevice.impTxResult(request, file, fileFileName);
        if(upResult == 1){//成功
        	returnMap.put("errorCode", 0);
        	returnMap.put("upResult", upResult);
			this.writeJsonData(returnMap, response);
			return ;
        }else if(upResult == 2){//文件过大
        	returnMap.put("errorCode", -1);
        	returnMap.put("errorInfo", "File is too large!");
			this.writeJsonData(returnMap, response);
			return ;
        }else {
        	returnMap.put("errorCode", -1);
        	returnMap.put("errorInfo", "Upload failure!");
			this.writeJsonData(returnMap, response);
			return ;
        }
    }
    
    /**
     * 消息列表
     * @date 2019-1-21
     * @param response
     */
    @RequestMapping(value="pushMessage.do")
    public void pushMessage(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		//logger.debug("checkUserInfo : " + checkUserInfo);
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
    	try {
			List list = new ArrayList();
			list = yjbbLcSevice.getPushMessageList(request);
			//保存or更新 t_push_operator
			yjbbLcSevice.savePushMessageInfo(request);
			map.put("pushMessageList", list);
			returnMap.put("errorCode", 0);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		} catch (Exception e) {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", e.toString());
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    /**
     * 消息详情
     * @date 2019-2-13
     * @param response
     */
    @RequestMapping(value="messageInfo.do")
    public void messageInfo(HttpServletResponse response){
    	Map map = new HashMap();
    	Map returnMap = new HashMap();
    	
    	//单点登录
		String checkUserInfo = this.getUser(request).getOperatorId();
		checkUserInfo = checkUserInfo == null ? "" : checkUserInfo;
		//logger.debug("checkUserInfo : " + checkUserInfo);
		if(checkUserInfo.equals("")){//没有登录
			returnMap.put("errorCode", -3);
			returnMap.put("errorInfo", "token Invalid!");
			this.writeJsonData(returnMap, response);
			return ;
		}
    	
    	try {
    		Map infoMap = new HashMap();
    		infoMap = yjbbLcSevice.getMessageDetail(request);
			map.put("messageInfo", infoMap);
			returnMap.put("errorCode", 0);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		} catch (Exception e) {
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", e.toString());
			this.writeJsonData(returnMap, response);
			return ;
		}
    }
    
    
}
