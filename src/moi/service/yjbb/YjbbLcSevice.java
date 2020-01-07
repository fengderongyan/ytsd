package moi.service.yjbb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import moi.service.log.MobileLogService;
import ytsd.action.sys.UserCfgController;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.common.FileHelper;
import com.sgy.util.db.BatchSql;

import util.JPushUtil;

import web.service.BaseService;

/** 
 * 手机端流程处理
 * @author zhang
 * @date 2019-01-15 
 */
@Service("yjbbLcSevice")
public class YjbbLcSevice extends BaseService {
	
	/**
	 * 获取进行中的报备列表
	 * @date 2019-01-15
	 * @return
	 */
    public List<Map<String, Object>> getLcCarryList(HttpServletRequest request, String type, String tabType, String code){
    	String operatorId = this.getUser(request).getOperatorId();//登录人工号
    	int pageNum = Integer.valueOf(request.getParameter("pageNum"));//页
    	//logger.debug("pageNum : " + pageNum);
    	String sql = "";
    	//
    	sql = "select  " +
    			"	id, type, concrete_type, bbr, address, " +
    			"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    			"(case  " +
    			"		when a.type = 1 and IFNULL(a.end_flag, 0) = 0 then '待审核'  " +
    			"	  when a.type = 1 and IFNULL(a.end_flag, 0) = 1 and IFNULL(a.xj_flag, 0) = 0 then '待销假'  " +
    			"		when a.type = 2 and IFNULL(a.end_flag, 0) = 0 then '待审核' " +
    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 0 and ifnull(end_flag, 0) = 1 then '待归家' " +
    			"	 end) bb_state_name, jj_lxr_name, jj_lxr_phone," +
    			"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, " +
    			"   DATE_FORMAT(start_date, '%Y-%m-%d %k:%i') start_date, DATE_FORMAT(end_date, '%Y-%m-%d %k:%i') end_date, " +
    			"   bb_days, ifnull(xj_flag, 0) xj_flag, ifnull(end_flag, 0) end_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark, cp_num, " +
    			"   (select b.name from t_user b where a.bbr = b.operator_id ) bbr_name " +
    			" from t_bb_info a  " +
    			"  where a.bbr = ? " +
    			"  and a.type = ? " +
    			"  and (IFNULL( a.xj_flag, 0 ) = 0 or ifnull( a.end_flag, 0 ) = 0  ) " + //未结束
    			"  and ( select IFNULL(MIN( b.sh_result ), 1) from t_bb_lc_info b where b.bb_id = a.id ) > 0   " + //未存在驳回
    			//"  and (IFNULL(a.end_flag, 0) = 1 or TimeStampDiff(DAY, DATE_FORMAT(IFNULL(a.end_date, sysdate()), '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d')) >= 0) " +
    			"order by a.rec_date desc " ;
    			//"  and TimeStampDiff(DAY, DATE_FORMAT(IFNULL(a.end_date, sysdate()), '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d')) <= 0 ";
    	//logger.debug("getLcCarryList sql : " + str.getSql(sql, new Object[]{tabType, tabType, tabType, code, operatorId, type}));
    	sql = str.getSql(sql, new Object[]{tabType, tabType, tabType, code, operatorId, type});
    	logger.debug("getLcCarryList : " + sql);
    	
    	//return db.queryForList(sql, new Object[]{tabType, tabType, tabType, code, operatorId, type});
    	return db.getForListForMobile(sql, pageNum, PAGE_SIZE);
    }
    
    /**
	 * 获取已结束的报备列表
	 * @date 2019-01-15
	 * @return
	 */
    public List<Map<String, Object>> getLcEndedList(HttpServletRequest request, String type, String tabType, String code){
    	String operatorId = this.getUser(request).getOperatorId();//登录人工号
    	int pageNum = Integer.valueOf(request.getParameter("pageNum"));//页
    	String sql = "";
    	//logger.debug("type : " + type);
    	//报备结束
		sql = "select  " +
    			"	id, type, concrete_type, bbr, address, " +
    			"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    			"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, " +
    			"(case  " +
    			"		when a.type = 1 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) <= 0  then '已销假'  " +
    			"	    when a.type = 1 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) > 0  then '已销假（超时）'  " +
    			"		when a.type = 1 and IFNULL(a.end_flag, 0) = 1 and IFNULL(a.sh_end_flag, 0) = 0  then '驳回' " +
    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) = 0 then '归家' " +
    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) > 0 then '归家(隔天)' " +
    			"		when a.type = 2 and IFNULL(a.end_flag, 0) = 1 and ifnull(a.sh_end_flag, 0) = 0 then '驳回' " +
    			"	 end) bb_state_name, jj_lxr_name, jj_lxr_phone, cp_num, " +
    			"   DATE_FORMAT(start_date, '%Y-%m-%d %k:%i') start_date, DATE_FORMAT(ifnull(end_date, sysdate()), '%Y-%m-%d %k:%i') end_date, " +
    			"   bb_days, ifnull(xj_flag, 0) xj_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark, " +
    			"   (select b.name from t_user b where a.bbr = b.operator_id ) bbr_name " +
    			" from t_bb_info a  " +
    			"where 1 = 1 " +
    			" and (IFNULL(a.xj_flag, 0) = 1 or " + //销假or归家的
    			"  (ifnull(a.end_flag, 0) = 1 and ifnull(a.sh_end_flag, 0) = 0 )) " + //驳回的
    			//"(a.end_flag = 1 or TimeStampDiff(DAY, DATE_FORMAT(IFNULL(a.end_date, sysdate()), '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d')) < 0 )" +
    			"  and a.bbr = ? " +
    			"  and a.type = ? " +
    		    "order by a.rec_date desc " ;
    	//logger.debug("getLcEndedList sql : " + str.getSql(sql, new Object[]{tabType, tabType, tabType, code, operatorId, type}));
    	sql = str.getSql(sql, new Object[]{tabType, tabType, tabType, code, operatorId, type});
    	
    	return db.getForListForMobile(sql, pageNum, PAGE_SIZE);
    	
    }
    
    /**
	 * 获取待审核的报备列表
	 * 分层级讨论权限：大队级、机关领导级、支队级、支队主官级（中队级、机关级和系统级不在讨论范围）
	 * @date 2019-01-16
	 * @return
	 */
    public List<Map<String, Object>> getLcAuditList(HttpServletRequest request, String type, String tabType, String code){
    	String operatorId = this.getUser(request).getOperatorId();//登录人工号
    	String maxLev = this.getUser(request).getMaxRoleLevel();//登录人最大角色层级
    	String orgId = this.getUser(request).getOrgId();//登录人组织层级
    	String roleIds = this.getUser(request).getRoleIds();//登录人角色id
    	String sql = " ";
    	List paramsList = new ArrayList();
    	
    	int pageNum = Integer.valueOf(request.getParameter("pageNum"));//页
    	
    	Boolean isBmz = null;//是否为部门长
    	Boolean isGbk = null; //组织是否为干部科
    	Boolean isZzc = null;//是否为政治处主任
    	Boolean isJwk = null;//组织是否为警务科
    	//填入所有部门长角色对应id
    	isBmz = this.getUser(request).hasRoles(GLOBA_BMZ_ROLES) ? true : false;
    	//
    	isGbk = this.getUser(request).hasRoles(GLOBA_GBK_ROLES) ? true : false;
    	//填入政治处主任的角色id
    	isZzc = this.getUser(request).hasRoles(GLOBA_ZZCZR_ROLES) ? true : false;
    	//
    	isJwk = this.getUser(request).hasRoles(GLOBA_JWK_ROLES) ? true : false;
    	logger.debug("maxLev : " + maxLev );
    	if(maxLev.equals("7")){//中队级
    		//队员提出、报备未结束、审核流程未结束、流程状态为中队审核的(0)
    		sql = "select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, " +
    				"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    				"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, '待审核' bb_state_name, " +
    				"   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
    			  "       DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, a.dd_view view_flag, a.dd_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
				  "  from t_bb_info a, t_user b, t_organization c, t_bb_lc_state d " +
				  " where a.bbr = b.OPERATOR_ID	 " +
				  "   and b.ORG_ID = c.ORG_ID " +
				  "   and a.id = d.bb_id " +
				  "   and a.zd_view = 1  " +
				  "	  and a.zd_cl = 1  " +//中队权限
				  "	  and IFNULL(a.end_flag, 0) = 0 " +
				  "	  and IFNULL(a.sh_end_flag, 0) = 0 " +
				  "	  and a.type = ? " +
				  "	  and c.org_id =  ? " + 
				  "   and d.lc_id = 0  order by a.rec_date desc ";
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(code);
    		paramsList.add(type);
    		paramsList.add(orgId);
    	}else if(maxLev.equals("6") || this.getUser(request).hasRoles(GLOBA_DDZG_ROLES)){//大队级
    		
    		//下属归属中队提出、报备未结束、审核流程未结束、流程状态为大队审核的
    		sql = "select aa.* from (select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, " +
    				"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    				"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, '待审核' bb_state_name, " +
    				"   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num,  " +
    			  "       DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, a.dd_view view_flag, a.dd_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark  " +
				  "  from t_bb_info a, t_user b, t_organization c, t_bb_lc_state d " +
				  " where a.bbr = b.OPERATOR_ID	 " +
				  "   and b.ORG_ID = c.ORG_ID " +
				  "   and a.id = d.bb_id " +
				  "   and a.dd_view = 1  " +
				  "	  and a.dd_lc = 1  " +
				  "	  and IFNULL(a.end_flag, 0) = 0 " +
				  "	  and IFNULL(a.sh_end_flag, 0) = 0 " +
				  "	  and a.type = ? " +
				  "	  and c.SUPERIOR_ID =  ? " + 
				  "   and d.lc_id = 1 order by a.rec_date desc ) as aa ";
    		
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(code);
    		paramsList.add(type);
    		paramsList.add(orgId);
    		
    		//所属一般干部提出、报备未结束、审核流程未结束、流程状态为大队审核的
    		String ybSql = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, " +
    				"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    				"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, '待审核' bb_state_name, " +
    				"   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num,  " +
    			  "       DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, a.dd_view view_flag, a.dd_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark  " +
				  "  from t_bb_info a, t_user b, t_organization c, t_bb_lc_state d " +
				  " where a.bbr = b.OPERATOR_ID	 " +
				  "   and b.ORG_ID = c.ORG_ID " +
				  "   and a.id = d.bb_id " +
				  "   and a.dd_view = 1  " +
				  "	  and a.dd_lc = 1  " +
				  "	  and IFNULL(a.end_flag, 0) = 0 " +
				  "	  and IFNULL(a.sh_end_flag, 0) = 0 " +
				  "	  and a.type = ? " +
				  "	  and c.ORG_ID =  ? " + 
				  "   and d.lc_id = 1 order by a.rec_date desc) as bb ";
    		paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(code);
    		paramsList.add(type);
    		paramsList.add(orgId);
    		
    		sql = sql + " union " + ybSql;
    		
    		//logger.debug("dd sh : " + str.getSql(sql, paramsList.toArray()));
    	}else if(maxLev.equals("4") && isBmz){//机关领导级 && 机关部门长
    		logger.debug("机关部门长");
    		//部门内提出的、报备未结束的、审核流程未结束的、流程状态为机关部门长审核的
    		sql = "select aa.* from (select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, " +
    				"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    				"   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
    				"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
    			  "       DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, a.jg_view view_flag, a.jg_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark  " +
    			  "  from t_bb_info a, t_user b, t_organization c, t_bb_lc_state d " +
    			  " where a.jg_lc = 1 " +
    			  "   and a.jg_view = 1 " +
    			  "	  and a.bbr = b.OPERATOR_ID " +
    			  "	  and b.ORG_ID = c.ORG_ID " +
    			  "	  and IFNULL(a.end_flag, 0) = 0  " +
    			  "	  and IFNULL(a.sh_end_flag, 0) = 0 " +
    			  "	  and a.type = ? " +
    			  "	  and c.org_id = ? " +
    			  "	  and a.id = d.bb_id " +
    			  "	  and d.lc_id = 2 " +
    			  "	  and IFNULL(a.dd_lc, 0) = 0  " +
    			  " order by a.rec_date desc) as aa ";;
    		paramsList.add(tabType);
 		    paramsList.add(tabType);
 		    paramsList.add(tabType);
 		    paramsList.add(code);
    		paramsList.add(type);
    		paramsList.add(orgId);
    		if(isGbk && type.equals("1")){//干部科&& 请假
    			//由中队提出、大队审核通过、状态为机关部门长审核的、报备未结算的、审核流程未结束的
    			String sqlZdbb = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, " +
    							 "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    							 "   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
    							 "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
    					         "       DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, a.jg_view view_flag, a.jg_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
		    					 "	from t_bb_info a , t_bb_lc_state b " +
		    					 " where a.dd_lc = 1 " +
		    					 "   and a.jg_lc = 1	 " +
		    					 "   and ifnull(a.jwk_view, 0) = 0	 " +
		    					 "   and ifnull(a.jwk_lc, 0) = 0	 " +
		    					 "   and a.jg_view = 1 " +
		    					 "	 and a.id = b.bb_id " +
		    					 "	 and IFNULL(a.sh_end_flag, 0) = 0 " +
		    					 "	 and IFNULL(a.end_flag, 0) = 0 " +
		    					 "	 and b.lc_id = 2  " +
		    					 "	 and a.type = ? order by a.rec_date desc) as bb ";
    			paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(code);
    			paramsList.add(type);
    			sql = sql + " union " + sqlZdbb ;
    		}
    		if(isJwk && type.equals("1")){//请假  警务科
    			//队员、大队审核通过、状态为机关部门长审核的、报备未结算的、审核流程未结束的
    			String sqlZdbb = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, " +
    							 "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    							 "   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
    							 "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
    					         "       DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, a.jg_view view_flag, a.jg_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
		    					 "	from t_bb_info a , t_bb_lc_state b " +
		    					 " where a.dd_lc = 1 " +
		    					 "   and a.jg_lc = 1	 " +
		    					 "   and a.jwk_view = 1	 " +
		    					 "   and a.jwk_lc = 1	 " +// 警务科
		    					 "   and a.jg_lc = 1	 " +
		    					 "   and a.jg_view = 1 " +
		    					 "	 and a.id = b.bb_id " +
		    					 "	 and IFNULL(a.sh_end_flag, 0) = 0 " +
		    					 "	 and IFNULL(a.end_flag, 0) = 0 " +
		    					 "	 and b.lc_id = 2  " +
		    					 "	 and a.type = ? order by a.rec_date desc) as bb ";
    			paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(code);
    			paramsList.add(type);
    			sql = sql + " union " + sqlZdbb ;
    		}
    	}else if(this.getUser(request).hasRoles(GLOBA_ZZCZR_ROLES) ){//政治处主任 && 请假
    		//报备未结束的、审核流程未结束的、流程状态为政治处审核的
    		sql = "select a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date,  " +
    			  "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    			  "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
    			  "   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
    			  "		  DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, " +
    			  "		  a.zzc_view view_flag, a.zzc_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
    			  "  from t_bb_info a, t_bb_lc_state b " +
    			  " where a.id = b.bb_id " +
    			  "   and a.zzc_view = 1 " +
    			  "   and a.zzc_lc = 1 " +
    			  "	  and IFNULL(a.end_flag, 0) = 0 " +
    			  "	  and IFNULL(a.sh_end_flag, 0) = 0 and a.type = ? " +
    			  "	  and b.lc_id = 3 order by a.rec_date desc ";
    		//报备未结束的、审核流程未结束的、流程状态不为政治处审核的
    		/*String sqlZzcView = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr,  " +
    							"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    							"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '' bb_state_name, " +
			    				"	    a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i:%s') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i:%s') end_date, a.bb_days, " +
			    				"		a.zzc_view view_flag, 0 lc_flag, ifnull(sh_end_flag, 0) sh_end_flag " +
			    				"  from t_bb_info a, t_bb_lc_state b " +
			    				" where a.id = b.bb_id " +
			    				"   and a.zzc_view = 1 " +
			    				"	and IFNULL(a.end_flag, 0) = 0 " +
			    				"	and IFNULL(a.sh_end_flag, 0) = 0 " +
			    				"	and b.lc_id in(1, 2, 4, 6) order by a.rec_date desc) as bb ";
    		sql = sql + " union " + sqlZzcView;*/
    		paramsList.add(tabType);
 		    paramsList.add(tabType);
 		    paramsList.add(tabType);
 		    paramsList.add(code);
 		    paramsList.add(type);
 		    /*paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(code);*/
    		
    	}else if(this.getUser(request).hasRoles(GLOBA_CMZ_ROLES) ){//参谋长 && 请假
    		//报备未结束的、审核流程未结束的、流程状态为参谋长审核的
    		sql = "select a.id, a.type, a.concrete_type, a.bbr,  " +
    			  "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    			  "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
    			  "   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
    			  "		  a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, " +
    			  "		  a.cmz_view view_flag, a.cmz_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
    			  "  from t_bb_info a, t_bb_lc_state b " +
    			  " where a.id = b.bb_id " +
    			  "   and a.cmz_view = 1 " +
    			  "   and a.cmz_lc = 1 " +
    			  "	  and IFNULL(a.end_flag, 0) = 0 " +
    			  "	  and IFNULL(a.sh_end_flag, 0) = 0 and type = ? " +
    			  "	  and b.lc_id = 6 order by a.rec_date desc ";
    		//报备未结束的、审核流程未结束的、流程状态不为参谋长审核的
    		/*String sqlZzcView = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr,  " +
    							"	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
    							"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '' bb_state_name, " +
			    				"	    a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i:%s') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i:%s') end_date, a.bb_days, " +
			    				"		a.zzc_view view_flag, 0 lc_flag, ifnull(sh_end_flag, 0) sh_end_flag " +
			    				"  from t_bb_info a, t_bb_lc_state b " +
			    				" where a.id = b.bb_id " +
			    				"   and a.zzc_view = 1 " +
			    				"	and IFNULL(a.end_flag, 0) = 0 " +
			    				"	and IFNULL(a.sh_end_flag, 0) = 0 " +
			    				"	and b.lc_id in(1, 2, 4, 3) order by a.rec_date desc) as bb ";
    		sql = sql + " union " + sqlZzcView;*/
    		paramsList.add(tabType);
 		    paramsList.add(tabType);
 		    paramsList.add(tabType);
 		    paramsList.add(code);
 		   paramsList.add(type);
 		    /*paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(code);*/
    		
    	}else if(maxLev.equals("2")){//支队主官级
    		if(this.getUser(request).hasRoles(GLOBA_BOSS_ROLES)){//两boss
    			//报备未结束的、审核流程未结束的、流程状态为支队主官审核的
        		sql = "select a.id, a.type, a.concrete_type, a.bbr,  " +
        			  "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
        			  "   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
        			  "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
        			  "			 a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, " +
        			  "			 a.zg_view view_flag, a.zg_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
        			  "  from t_bb_info a, t_bb_lc_state b " +
        			  " where a.id = b.bb_id " +
        			  "   and a.zg_view = 1 " +
        			  "   and a.zg_lc = 1 " +
        			  "	  and IFNULL(a.end_flag, 0) = 0 " +
        			  "	  and IFNULL(a.sh_end_flag, 0) = 0 and a.type = ? and a.bbr <> ? " +
        			  "	  and b.lc_id = 4 order by a.rec_date desc ";
        		/*String sqlZgView = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr,  " +
        						   "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
        						   "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '' bb_state_name, " +
    			    			   "			 a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i:%s') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i:%s') end_date, a.bb_days, " +
    			    			   "			 a.zg_view view_flag, 0 lc_flag, ifnull(sh_end_flag, 0) sh_end_flag " +
    			    			   "  from t_bb_info a, t_bb_lc_state b " +
    			    			   " where a.id = b.bb_id " +
    			    			   "   and a.zg_view = 1 " +
    			    			   "   and IFNULL(a.end_flag, 0) = 0 " +
    			    			   "   and IFNULL(a.sh_end_flag, 0) = 0 " +
    			    			   "   and b.lc_id in (1, 2, 3)  order by a.rec_date desc) as bb ";
        		sql = sql + " union " + sqlZgView;*/
        		paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(code);
     		    paramsList.add(type);
     		    paramsList.add(operatorId);
    		}else {
    			//报备未结束的、审核流程未结束的、流程状态为支队主官审核的
        		sql = "select a.id, a.type, a.concrete_type, a.bbr,  " +
        			  "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
        			  "   (select e.name from t_user e where a.bbr = e.operator_id ) bbr_name, a.jj_lxr_name, a.jj_lxr_phone, cp_num, " +
        			  "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '待审核' bb_state_name, " +
        			  "			 a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i') end_date, a.bb_days, " +
        			  "			 a.zg_view view_flag, a.zg_lc lc_flag, ifnull(sh_end_flag, 0) sh_end_flag, info_remark " +
        			  "  from t_bb_info a, t_bb_lc_state b " +
        			  " where a.id = b.bb_id " +
        			  "   and a.zg_view = 1 " +
        			  "   and a.zg_lc = 1 " +
        			  "	  and IFNULL(a.end_flag, 0) = 0 " +
        			  "	  and IFNULL(a.sh_end_flag, 0) = 0 and a.type = ? and a.bbr <> ? and a.bbr not in (612001, 612002) " +
        			  "	  and b.lc_id = 4 order by a.rec_date desc ";
        		/*String sqlZgView = "select bb.* from (select a.id, a.type, a.concrete_type, a.bbr,  " +
        						   "	(case when ? = 'carry' then '进行中' when ? = 'ended' then '已结束' when ? = 'audit' then '待审核' end) tab_type_name, " +
        						   "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name,  '' bb_state_name, " +
    			    			   "			 a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i:%s') start_date, DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i:%s') end_date, a.bb_days, " +
    			    			   "			 a.zg_view view_flag, 0 lc_flag, ifnull(sh_end_flag, 0) sh_end_flag " +
    			    			   "  from t_bb_info a, t_bb_lc_state b " +
    			    			   " where a.id = b.bb_id " +
    			    			   "   and a.zg_view = 1 " +
    			    			   "   and IFNULL(a.end_flag, 0) = 0 " +
    			    			   "   and IFNULL(a.sh_end_flag, 0) = 0 " +
    			    			   "   and b.lc_id in (1, 2, 3)  order by a.rec_date desc) as bb ";
        		sql = sql + " union " + sqlZgView;*/
        		paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(tabType);
     		    paramsList.add(code);
     		    paramsList.add(type);
     		    paramsList.add(operatorId);
    		}
    		
 		    /*paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(tabType);
		    paramsList.add(code);*/
    	}
    	
    	sql = str.getSql(sql, paramsList.toArray());
    	logger.debug("sh sql : " + sql);
    	return db.getForListForMobile(sql, pageNum, PAGE_SIZE);
    	//return db.queryForList(sql, paramsList.toArray());
    	
    }
    
    /**
     * 获取具体类别
     * @param type 1：请假； 2：饮酒
     * @return
     */
    public List<Map<String, Object>> getConcreteTypeList(String type){
    	String dataTypeCode = type.equals("1") ? "10032" : "10033";
    	String sql = " select DD_ITEM_CODE type_code, DD_ITEM_NAME type_name  from t_ddw where DATA_TYPE_CODE = ? order by DD_ITEM_CODE ";
    	//logger.debug("get lb : " + str.getSql(sql, new Object[]{dataTypeCode}));
    	return db.queryForList(sql, new Object[]{dataTypeCode});
    } 
    
    /**
     * 获取具体类别名称
     * @param type 1：请假； 2：饮酒
     * @return
     */
    public String getConcreteTypeName(String type, String ddItemCode){
    	String dataTypeCode = type.equals("1") ? "10032" : "10033";
    	String sql = " select DD_ITEM_NAME type_name  from t_ddw where DATA_TYPE_CODE = ? and DD_ITEM_CODE = ? ";
    	return db.queryForString(sql, new Object[]{dataTypeCode, ddItemCode});
    	//logger.debug("get lb : " + str.getSql(sql, new Object[]{dataTypeCode}));
    	//return db.queryForList(sql, new Object[]{dataTypeCode});
    } 
    
    /**
     * 获取具体类别名称
     * @param type 1：请假； 2：饮酒
     * @return
     */
    public String getConcreteTypeNameByBbId(String bbId, String type){
    	String dataTypeCode = type.equals("1") ? "10032" : "10033";
    	String sql = "select " +
	    			"	a.DD_ITEM_NAME type_name  " +
	    			"from " +
	    			"	t_ddw a, " +
	    			"	t_bb_info b  " +
	    			"where " +
	    			"	b.id = ?  " +
	    			"	and a.DATA_TYPE_CODE = ?  " +
	    			"	and a.DD_ITEM_CODE = b.concrete_type ";
    	sql = str.getSql(sql, new Object[]{bbId, dataTypeCode});
    	return db.queryForString(sql);
    	//logger.debug("get lb : " + str.getSql(sql, new Object[]{dataTypeCode}));
    	//return db.queryForList(sql, new Object[]{dataTypeCode});
    } 
    
    /**
     * 保存报备信息(立即提交)
     * @param request
     * @param type
     * @return
     */
    public int getSaveBbInfoResult(HttpServletRequest request, String type, String cpNum){
    	String startDate = req.getValue(request, "startDate");//开始时间（传入格式yyyy-mm-dd hh:mi）
    	String endDate = req.getValue(request, "endDate");//结束时间
    	String bbDays = req.getValue(request, "bbDays");//报备天数
    	String concreteType = req.getValue(request, "concreteType");//类别
    	String address = req.getValue(request, "address");//地址
    	String remark = req.getValue(request, "remark");//备注
    	String jjLxrName = req.getValue(request, "jjLxrName");//紧急联系人姓名
    	String jjLxrPhone = req.getValue(request, "jjLxrPhone");//紧急联系人号码
    	
    	String operatorId = this.getUser(request).getOperatorId();//登录人工号
    	String operatorName = this.getUser(request).getName();//登录人姓名
    	
    	String maxLev = this.getUser(request).getMaxRoleLevel();//登录人角色最大层级
    	String orgId = this.getUser(request).getOrgId();//获取登录人组织编码
    	
    	if(bbDays.equals("")){
    		bbDays = "0";
    	}
    	
    	Boolean isSx = null;
    	Boolean isQj = null;
    	Boolean isZzc = null;
    	
    	isSx = concreteType.equals("4") ? true : false;//其他类型:双休、节假日
    	isQj = type.equals("1") ? true : false;
    	//填入政治处主任所在机关编号（应该是政治处）
    	isZzc = (GLOBA_ZZC_ORGIDS).equals(orgId) ? true : false;
    	
    	//饮酒结束时间处理(饮酒没有结束时间；解决时间空值保存失败问题)
    	if(!isQj){
    		endDate = "2000-01-01";
    	}
    	String sql = "";
    	BatchSql batchSql = new BatchSql();
    	String bbId = db.getMysqlNextSequenceValue("t_bb_info_sid");
    	String lcId = db.getMysqlNextSequenceValue("t_bb_lc_state_sid");
    	//logger.debug("maxLev : " + maxLev + "  endDate : " + endDate);
    	if(maxLev.equals("8")){//队员级
    		//logger.debug("队员保存");
    		//保存信息
    		if(orgId.equals(GLOBA_JQ_ORGID) || orgId.equals(GLOBA_ZB_ORGID)){//警勤 or 战保
    			sql = "insert into t_bb_info " +
        				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
        				"	 dd_view, dd_lc, jg_view, jg_lc, zg_view, rec_date, info_remark, zd_view,  cmz_view, cmz_lc, jwk_view, jwk_lc, jj_lxr_name, jj_lxr_phone) " +
        				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
        				"		?, 1, 1, 1, 1, 1, SYSDATE(), ?, 1, 1, ?, 1, 1, ?, ?)";
        		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
        		
        		//logger.debug("save : " + str.getSql(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone}));
        		//流程更新为 上级大队主官审核
        		sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
          			  "values(?, ?, 1) ";
          		batchSql.addBatch(sql, new Object[]{lcId, bbId});
          		//事物处理
          		
          		String pushId = this.getDdzgRegistrationIds(orgId);
				this.pushMessage(bbId, type, pushId, operatorName, concreteType);
    		}else{
				sql = "insert into t_bb_info " +
	    				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
	    				"	 dd_view, dd_lc, jg_view, jg_lc, zg_view, rec_date, info_remark, zd_view, zd_cl, cp_num, cmz_view, cmz_lc, jwk_view, jwk_lc, jj_lxr_name, jj_lxr_phone) " +
	    				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
	    				"		?, 1, 1, 1, 1, 1, SYSDATE(), ?, 1, 1, ?, 1, 1, 1, 1, ?, ?)";
	    		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
	    		
	    		//logger.debug("save : " + str.getSql(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone}));
	    		//流程更新为中队审核（请假、饮酒下一审核流程相同）
	    		sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
	    			  "values(?, ?, 0) ";
	    		batchSql.addBatch(sql, new Object[]{lcId, bbId});
	    		//事物处理
          		
	    		String pushId = this.getZdzgRegistrationIds(orgId);
				this.pushMessage(bbId, type, pushId, operatorName, concreteType);
	    		
    		}
    	}else if(maxLev.equals("7")){//中队、大队一般干部级
    		//保存信息
    		if(this.getUser(request).hasRoles(GLOBA_ZB_ROLES) && isQj){//徐圩、战保, 参谋长审核标记、警务科审核标记
    			sql = "insert into t_bb_info " +
        				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
        				"	 dd_view, dd_lc, jg_view, jg_lc, zg_view, rec_date, info_remark, cp_num, cmz_view, cmz_lc, jwk_view, jwk_lc, jj_lxr_name, jj_lxr_phone) " +
        				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
        				"		?, 1, 1, 1, 1, 1, SYSDATE(), ?, ?, 1, 1, 1, 1, ?, ?) ";
    			batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    			
    		}else if(isQj){//请假
	    		sql = "insert into t_bb_info " +
	    				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
	    				"	 dd_view, dd_lc, jg_view, jg_lc, zzc_view, zzc_lc, zg_view, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
	    				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
	    				"		?, 1, 1, 1, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
	    		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
	    		
    		}else if(!isQj){//饮酒
    			sql = "insert into t_bb_info " +
	    				"	(id, type, concrete_type, bbr, address, start_date,   " +
	    				"	 dd_view, dd_lc, jg_view, jg_lc, zzc_view, zzc_lc, zg_view, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
	    				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), " +
	    				"		 1, 1, 1, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
	    		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, remark, cpNum, jjLxrName, jjLxrPhone});
	    		
    		}
    		
			//更新为大队处理
			sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
    			  "values(?, ?, 1) ";
			batchSql.addBatch(sql, new Object[]{lcId, bbId});
			
      		if(this.getUser(request).hasRoles(GLOBA_DDYB_ROLES)){//大队一般
      			String pushId = this.getDdzgRegistrationIdsDdyb(orgId);
    			this.pushMessage(bbId, type, pushId, operatorName, concreteType);
      		}else {
      			String pushId = this.getDdzgRegistrationIds(orgId);
    			this.pushMessage(bbId, type, pushId, operatorName, concreteType);
      		}
			
    		
    	}else if(maxLev.equals("6") || this.getUser(request).hasRoles(GLOBA_DDZG_ROLES)){//大队级
    		logger.debug("大队");
    		//周末（节假日）
    		if(isSx && isQj){
    			//保存信息
    			sql = "insert into t_bb_info " +
    					"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
    					"	 dd_view, zzc_view, zzc_lc, zg_view, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
    					"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
    					"		?, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?) ";
    			batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    			
    		}else if(isQj){//休假、事假、病假
    			//保存信息
    			sql = "insert into t_bb_info " +
    					"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
    					"	 dd_view, zzc_view, zzc_lc, zg_view, zg_lc, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
    					"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
    					"		?, 1, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?) ";
    			batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    		}else if(!isQj){//饮酒
    			//保存信息
    			sql = "insert into t_bb_info " +
    					"	(id, type, concrete_type, bbr, address, start_date,  " +
    					"	 dd_view, zg_view, zg_lc, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
    					"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), " +
    					"		1, 1, 1, SYSDATE(), ?, ?, ?, ?) ";
    			batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, remark, cpNum, jjLxrName, jjLxrPhone});
    		}
    		//流程更新
    		if(isQj){//请假
    			//流程更新为政治处主任审核
    			sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
        			  "values(?, ?, 3) ";
    			batchSql.addBatch(sql, new Object[]{lcId, bbId});
    			//事物处理
          		
    			String pushId = this.getRegistrationIdByRoleId(GLOBA_ZZCZR_ROLES, request);
				this.pushMessage(bbId, type, pushId, operatorName, concreteType);
        		
    		}else{//饮酒
    			
    			//流程更新为支队主官审核
    			sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
        			  "values(?, ?, 4) ";
    			batchSql.addBatch(sql, new Object[]{lcId, bbId});
    			//事物处理
          		
    			String pushId = this.getRegistrationIdByRoleId(GLOBA_ZDZG_ROLES, request);
				this.pushMessage(bbId, type, pushId, operatorName, concreteType);
    		}
    	}else if(maxLev.equals("5")){//机关级（机关一般干部）
    		//保存信息
    		if(isQj){//请假
    			sql = "insert into t_bb_info " +
        				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
        				"	 jg_view, jg_lc, zzc_view, zzc_lc, zg_view, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
        				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
        				"		?, 1, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
        		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    		}else if(!isQj){//饮酒
    			sql = "insert into t_bb_info " +
        				"	(id, type, concrete_type, bbr, address, start_date," +
        				"	 jg_view, jg_lc, zzc_view, zzc_lc, zg_view, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
        				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), " +
        				"		1, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
        		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, remark, cpNum, jjLxrName, jjLxrPhone});
    		}
    		if(isZzc){//政治处内部人员，直接提交政治处主任审核
    			sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
	    			  "values(?, ?, 3) ";
	    		batchSql.addBatch(sql, new Object[]{lcId, bbId});
	    		//事物处理
          		
	    		String pushId = this.getRegistrationIdByRoleId(GLOBA_ZZCZR_ROLES, request);
				this.pushMessage(bbId, type, pushId, operatorName, concreteType);
        		
    		}else{
	    		//流程更新为机关部门长审核（请假、饮酒下一审核流程相同）
	    		sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
	    			  "values(?, ?, 2) ";
	    		batchSql.addBatch(sql, new Object[]{lcId, bbId});
	    		//事物处理
          		
	    		//推送信息，推送目标--上级部门长
				String pushId = this.getjgLdregistRationId(orgId);
				this.pushMessage(bbId, type, pushId, operatorName, concreteType);
    		}
    	}else if(maxLev.equals("4")){//机关领导级
    		//保存信息
    		if(isQj){//请假
    			sql = "insert into t_bb_info " +
        				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
        				"	 jg_view, jg_lc, zzc_view, zg_view, zg_lc, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
        				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
        				"		?, 1, 1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
        		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    		}else if(!isQj){//饮酒
    			sql = "insert into t_bb_info " +
        				"	(id, type, concrete_type, bbr, address, start_date, " +
        				"	 jg_view, zzc_view, zg_view, zg_lc, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
        				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), " +
        				"		1, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
        		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, remark, cpNum, jjLxrName, jjLxrPhone});
    		}
    		
    		//流程更新为支队主官审核（请假、饮酒下一审核流程相同）
    		sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
    			  "values(?, ?, 4) ";
    		batchSql.addBatch(sql, new Object[]{lcId, bbId});
    		//事物处理
      		
    		//推送信息，推送目标--支队主官
			String pushId = this.getRegistrationIdByRoleId(GLOBA_ZDZG_ROLES, request);
			this.pushMessage(bbId, type, pushId, operatorName, concreteType);
    		
    	}else if(maxLev.equals("3")){//支队级
    		//保存信息
    		sql = "insert into t_bb_info " +
    				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
    				"	 zzc_view, zg_view, zg_lc, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
    				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
    				"		?, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
    		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    		//流程更新为支队主官审核（请假、饮酒下一审核流程相同）
    		sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
    			  "values(?, ?, 4) ";
    		batchSql.addBatch(sql, new Object[]{lcId, bbId});
    		//事物处理
      		
    		//推送信息，推送目标--支队主官
			String pushId = this.getRegistrationIdByRoleId(GLOBA_ZDZG_ROLES, request);
			this.pushMessage(bbId, type, pushId, operatorName, concreteType);
    		
    	}else if(maxLev.equals("2")){//支队主官
    		//保存信息
    		sql = "insert into t_bb_info " +
    				"	(id, type, concrete_type, bbr, address, start_date, end_date, bb_days,  " +
    				"	 zzc_view, zg_view, zg_lc, rec_date, info_remark, cp_num, jj_lxr_name, jj_lxr_phone) " +
    				"	values(?, ?, ?, ?, ?, str_to_date(?, '%Y-%m-%d %k:%i'), str_to_date(?, '%Y-%m-%d %k:%i'), " +
    				"		?, 1, 1, 1, SYSDATE(), ?, ?, ?, ?)";
    		batchSql.addBatch(sql, new Object[]{bbId, type, concreteType, operatorId, address, startDate, endDate, bbDays, remark, cpNum, jjLxrName, jjLxrPhone});
    		//流程更新为支队主官审核（请假、饮酒下一审核流程相同）
    		sql = "insert into t_bb_lc_state (id, bb_id, lc_id) " +
    			  "values(?, ?, 4) ";
    		batchSql.addBatch(sql, new Object[]{lcId, bbId});
    		
    		//推送信息，推送目标--支队主官
			String pushId = this.getRegistrationIdByRoleId(GLOBA_ZDZG_ROLES, request);
			this.pushMessage(bbId, type, pushId, operatorName, concreteType);
    	}
    	
    	if(!isQj){//饮酒
    		//结束日期更新为 null
    		sql = " update t_bb_info set end_date = null where id = ? ";
    		batchSql.addBatch(sql, new Object[]{bbId});
    		//更新车牌号
    		sql = " update t_bb_info set cp_num = ? where id = ? ";
    		batchSql.addBatch(sql, new Object[]{cpNum ,bbId});
    	}
    	
    	//更新紧急联系人信息
    	sql = " update t_user set jj_lxr_name = ?, jj_lxr_phone = ? where operator_id = ? ";
    	batchSql.addBatch(sql, new Object[]{jjLxrName, jjLxrPhone, this.getUser(request).getOperatorId()});
    	
    	//报备人总流程
    	String lcNum = "";
    	String roleLev = this.getUser(request).getMaxRoleLevel();
    	if(type.equals("1")){//请假
    		if(roleLev.equals("7")){
    			lcNum = "3";
    		}else if(roleLev.equals("8")){
    			lcNum = "4";
    		}else if(this.getUser(request).hasRoles(GLOBA_DDZG_ROLES) && isQj && !concreteType.equals("4")){//大队 & 请假 & 正常
    			lcNum = "2";
    		}else if(this.getUser(request).hasRoles(GLOBA_DDZG_ROLES) && isQj && concreteType.equals("4")){//大队 & 请假 & 双休
    			lcNum = "1";
    		}else if(roleLev.equals("5")){
    			lcNum = "2";
    		}else if(roleLev.equals("4")){
    			lcNum = "1";
    		}else if(roleLev.equals("3")){
    			lcNum = "1";
    		}else if(roleLev.equals("2")){
    			lcNum = "1";
    		}
    	}
    	if(type.equals("2")){//饮酒
    		lcNum = "1";
    	}
    	
    	if(isQj){
    		//更新报备天数
    		sql = " update t_bb_info a " +
    			  "    set bb_days = TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(a.end_date, '%Y-%m-%d')) + 1 " +
    			  "  where id = ? ";
    		batchSql.addBatch(sql, new Object[]{bbId});
    	}
    	//最大流程步骤
    	sql = " update t_bb_info set lc_num = ? where id = ? ";
    	batchSql.addBatch(sql, new Object[]{lcNum, bbId});
    	
    	return db.doInTransaction(batchSql);
    }
    
    /**
     * 获取审核详细信息，包括审核各流程信息
     * @param request
     * @return
     */
    public Map<String, Object> getBbDetail(HttpServletRequest request, String type, String code){
    	String id = req.getValue(request, "bbId");//报备信息表id
    	String sql = "";
    	
    	Map<String, Object> map = new HashMap();
    	//基本信息
    	sql = "select a.id, a.type, a.concrete_type, a.bbr, a.address, " +
    		  "		  DATE_FORMAT( a.start_date, '%Y-%m-%d %k:%i' ) start_date, " +
    		  " (select b.name from t_user b where a.bbr = b.operator_id ) bbr_name, " +
    		  "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, " +
    		  "       DATE_FORMAT( a.end_date, '%Y-%m-%d %k:%i' ) end_date, a.bb_days, IFNULL(a.end_flag, 0) end_flag,  " +
    		  "		  IFNULL(a.xj_flag, 0) xj_flag, IFNULL(a.sh_end_flag, 0) sh_end_flag, a.info_remark, jj_lxr_name, jj_lxr_phone, cp_num   " +
    		  "	 from t_bb_info a  " +
    		  " where a.id = ? ";
    	//logger.debug("baseInfo : " + str.getSql(sql, new Object[]{code, id}));
    	map.put("baseInfo", db.queryForMap(sql, new Object[]{code, id}));
    	//审核流程
    	sql = "select a.id, a.bb_id, a.shr, DATE_FORMAT( a.sh_date, '%Y-%m-%d %k:%i:%s' ) sh_date, a.sh_result, a.sh_remark, a.sh_lc_name, IFNULL(b.end_flag, 0) end_flag, a.shr_orgid, a.shr_name " +
    			"  from t_bb_lc_info a, t_bb_info b " +
    			" where a.bb_id = ? " +
    			"   and a.bb_id = b.id " +
    			" order by a.sh_date ";
    	map.put("shlcList", db.queryForList(sql, new Object[]{id}));
    	//logger.debug("map : " + map);
    	//logger.debug("shlcList : " + str.getSql(sql, new Object[]{id}));
    	//报备人信息
    	sql = "select  " +
    			"	a.OPERATOR_ID, a.NAME, CONCAT('http://139.224.15.52/sys/pic/', a.qm_name_d) tx_url, b.ORG_NAME, c.ROLE_ID, d.ROLE_NAME  " +
    			" from t_user a, t_organization b, t_user_role c, t_role d " +
    			"where a.ORG_ID = b.ORG_ID " +
    			"	and a.OPERATOR_ID = c.OPERATOR_ID " +
    			"	and c.ROLE_ID = d.ROLE_ID " +
    			"  and a.OPERATOR_ID = (select e.bbr from t_bb_info e where id = ?) order by d.ROLE_LEVEL limit 1 ";
    	map.put("bbrInfo", db.queryForMap(sql, new Object[]{id}));
    	//logger.debug("shbbrinfo : " + str.getSql(sql, new Object[]{id}));
    	
    	/*//报备人总流程
    	String lcNum = "";
    	String roleLev = this.getUser(request).getMaxRoleLevel();
    	if(type.equals("1")){//请假
    		if(roleLev.equals("7")){
    			lcNum = "3";
    		}else if(roleLev.equals("8")){
    			lcNum = "3";
    		}else if(roleLev.equals("6")){
    			lcNum = "3";
    		}else if(roleLev.equals("5")){
    			lcNum = "2";
    		}else if(roleLev.equals("4")){
    			lcNum = "1";
    		}else if(roleLev.equals("3")){
    			lcNum = "1";
    		}else if(roleLev.equals("2")){
    			lcNum = "1";
    		}
    	}
    	if(type.equals("2")){//饮酒
    		lcNum = "1";
    	}*/
    	map.put("lcNum", this.getLcNum(id));
    	
    	return map;
    }
    
    /**
     * 获取lcNum
     * @param id
     * @return
     */
    public String getLcNum(String id){
    	String sql = " select lc_num from t_bb_info where id = ? ";
    	return db.queryForString(sql, new Object[]{id});
    }
    
    /**
     * 审核报备信息
     * @date 2019-1-17
     * @param request
     * @return 1 成功  0 失败
     */
    public int getShResult(HttpServletRequest request, String type){
    	String bbId = req.getValue(request, "bbId");//报备信息ID
    	String shResult = req.getValue(request, "shResult");//审核结果：1 通过； 0 驳回
    	String shRemark = req.getValue(request, "shRemark");//备注
    	
    	String bbrId = this.getbbrIdByid(bbId);//报备人工号
    	
    	String operatorId = this.getUser(request).getOperatorId();//登录人工号
    	String operatorName = this.getUser(request).getName();//登录人姓名
    	String maxLev = this.getUser(request).getMaxRoleLevel();//登录人角色最大层级
    	String orgId = this.getUser(request).getOrgId();//登录人归属组织编号
    	
    	String sql = "";
    	BatchSql batchSql = new BatchSql();    	
    	Boolean isQj = null;//是否请假
    	Boolean isBmz = null;//是否为部门长
    	Boolean isGbk = null; //组织是否为干部科
    	Boolean isZzc = null;//是否为政治处主任
    	Boolean isTg = null;//是否审核通过
    	
    	JPushUtil push = new JPushUtil();
    	
    	//
    	logger.debug(type);
    	isQj = type.equals("1") ? true : false;
    	//填入所有部门长角色对应id
    	isBmz = this.getUser(request).hasRoles(GLOBA_BMZ_ROLES) ? true : false;
    	//填入干部科
    	isGbk = this.getUser(request).hasRoles(GLOBA_GBK_ROLES) ? true : false;
    	//填入政治处主任的角色id
    	isZzc = this.getUser(request).hasRoles(GLOBA_ZZCZR_ROLES) ? true : false;
    	isTg = shResult.equals("1") ? true : false;
    	
    	if(maxLev.equals("7")){//中队级
    		//流程信息表更新
			sql = "insert into t_bb_lc_info " +
				  "	(id, bb_id, shr, sh_date, sh_result, sh_remark, sh_lc_name, shr_orgid, shr_name ) " +
				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, ?, '中队审核', ?, ? ) ";
			batchSql.addBatch(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName});
			logger.debug("isQj : " + isQj);
			logger.debug("isTg : " + isTg);
			if(isQj && isTg){//请假&&通过
    			//流程状态更新(更新为大队审核)
    			sql = " update t_bb_lc_state set lc_id = 1 where bb_id = ? ";
    			batchSql.addBatch(sql, new Object[]{ bbId});
    			//推送信息，推送目标--上级大队
				String pushId = this.getDdzgRegistrationIds(orgId);
				this.pushMessage(bbId, type, pushId, "", "");
				
    		}else if(!isQj && isTg){//饮酒 && 通过
    			//流程结束（更新流程结束标记、报备结束标记）
    			sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
    			batchSql.addBatch(sql, new Object[]{ bbId});
    		}
    	}
    	if(maxLev.equals("6") || this.getUser(request).hasRoles(GLOBA_DDZG_ROLES)){//大队级
    		//流程信息表更新
			sql = "insert into t_bb_lc_info " +
				  "	(id, bb_id, shr, sh_date, sh_result, sh_remark, sh_lc_name, shr_orgid, shr_name ) " +
				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, ?, '大队审核', ?, ? ) ";
			batchSql.addBatch(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName});
			//logger.debug("dd lc : " + str.getSql(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName}));
    		if(isQj && isTg){//请假 &&通过
    			//流程状态更新(更新为 机关部门长审核)（干部科）
    			sql = " update t_bb_lc_state set lc_id = 2 where bb_id = ? ";
    			batchSql.addBatch(sql, new Object[]{ bbId});
    			//推送信息，推送目标--干部科科长
				String pushId = this.getRegistrationIdByRoleId(GLOBA_GBK_ROLES, request);
				this.pushMessage(bbId, type, pushId, "", "");
    		}else if(!isQj && isTg){//饮酒 && 通过
    			//流程结束（更新流程结束标记、报备结束标记）
    			sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
    			batchSql.addBatch(sql, new Object[]{ bbId});
    		}
    	}else if(maxLev.equals("4")){//机关领导级
    		//流程信息表更新
			sql = "insert into t_bb_lc_info " +
				  "	(id, bb_id, shr, sh_date, sh_result, sh_remark, sh_lc_name, shr_orgid, shr_name) " +
				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, ?, '机关部门长审核', ?, ? ) ";
			batchSql.addBatch(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName});
			if(isQj && isTg){//请假&&通过
				//流程状态更新（更新为参谋长审核）
				sql = " update t_bb_lc_state a set a.lc_id = 6 where a.bb_id = ? and exists(select 1 from t_bb_info b where a.bb_id = b.id and b.cmz_lc = 1 ) ";
				batchSql.addBatch(sql, new Object[]{ bbId});
				//流程状态更新（更新为政治处主任审核）
				sql = " update t_bb_lc_state a set a.lc_id = 3 where a.bb_id = ? and exists(select 1 from t_bb_info b where a.bb_id = b.id and b.zzc_lc = 1 ) ";
				batchSql.addBatch(sql, new Object[]{ bbId});
				//1 政治处； 2 参谋长； 0 其他
				sql = " select (case when a.zzc_lc = 1 then 1 when a.cmz_lc = 1 then 2 else 0 end ) push_flag from t_bb_info a where id = ? ";
				logger.debug("text " + str.getSql(sql, new Object[]{bbId}));
				int pushFlag = db.queryForInt(sql, new Object[]{bbId});
				String pushId = "";
				
				//推送信息，推送目标--参谋长or政治处主任
				if(pushFlag == 1){//政治处主任
					pushId = this.getRegistrationIdByRoleId(GLOBA_ZZCZR_ROLES, request);
					this.pushMessage(bbId, type, pushId, "", "");
					
				}else if(pushFlag == 2){//参谋长
					pushId = this.getRegistrationIdByRoleId(GLOBA_CMZ_ROLES, request);
					this.pushMessage(bbId, type, pushId, "", "");
					
				}else if(pushFlag == 1){
					
				}
			
			}else if(!isQj && isTg){//饮酒&&通过
				//流程结束（更新流程结束标记、报备结束标记）
				sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
				batchSql.addBatch(sql, new Object[]{ bbId});
			}
    	}else if(this.getUser(request).hasRoles(GLOBA_ZZCZR_ROLES)){//政治处主任
    		//流程信息表更新
			sql = "insert into t_bb_lc_info " +
				  "	(id, bb_id, shr, sh_date, sh_result, sh_remark, sh_lc_name, shr_orgid, shr_name) " +
				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, ?, '政治处主任审核', ?, ? ) ";
			batchSql.addBatch(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName});
			String ddxj = this.getDdxj(bbId);
			//logger.debug("ddxj : " + ddxj);
			//logger.debug(isTg);
			if(ddxj.equals("1") && isTg && isQj){//大队休假、病假需要提交支队主官&& 通过
				sql = "update t_bb_lc_state  set lc_id = 4  " +
					  " where bb_id = ? ";
				batchSql.addBatch(sql, new Object[]{ bbId});
				//推送信息，推送目标--支队主官
				String pushId = this.getRegistrationIdByRoleId(GLOBA_ZDZG_ROLES, request);
				this.pushMessage(bbId, type, pushId, "", "");
				
			}else if(isTg ){//政治处主任为流程最后一流程&&通过
				//流程结束（更新流程结束标记、报备结束标记）
				sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
				batchSql.addBatch(sql, new Object[]{ bbId});
			}
    	}else if(this.getUser(request).hasRoles(GLOBA_CMZ_ROLES)){//参谋长
    		//if(isQj && isTg){//请假&通过
			sql = "insert into t_bb_lc_info " +
				  "	(id, bb_id, shr, sh_date, sh_result, sh_remark, sh_lc_name, shr_orgid, shr_name) " +
				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, ?, '参谋长审核', ?, ? ) ";
			batchSql.addBatch(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName});
			//流程结束（流程更新结束标记、报备结束标记）
			sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
			batchSql.addBatch(sql, new Object[]{ bbId});
    		//}
    		
    	}else if(this.getUser(request).hasRoles(GLOBA_BOSS_ROLES) ){//支队主官级
    		//流程信息表更新
			sql = "insert into t_bb_lc_info " +
				  "	(id, bb_id, shr, sh_date, sh_result, sh_remark, sh_lc_name, shr_orgid, shr_name) " +
				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, ?, '支队主官审核', ?, ? ) ";
			batchSql.addBatch(sql, new Object[]{ bbId, operatorId, shResult, shRemark, orgId, operatorName});
			if(isTg){//通过
				//流程结束（更新流程结束标记、报备结束标记）
				sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
				batchSql.addBatch(sql, new Object[]{ bbId});
			}
    	}
    	
    	if(!isTg){//驳回
    		//更新报备结束标记，流程结束标记更新为0（没有完整走完通过的审核流程） 
    		sql = " update t_bb_info a set a.end_flag = 1, a.sh_end_flag = 0 where id = ? ";
    		batchSql.addBatch(sql, new Object[]{bbId});
    		
    		//推送信息，推送目标--报备发起人
			String pushId = this.getbbrIdByBbid(bbId);
			this.pushBhMessage(bbId, type, pushId, "", "");
			
    	}else if(!isTg && !isQj){//饮酒驳回
    		//更新结束日期 为 2099-01-01
    		sql = " update t_bb_info set end_date = STR_TO_DATE('2099-01-01', '%Y-%m-%d') where id = ? ";
    		batchSql.addBatch(sql, new Object[]{bbId});
    		//推送信息，推送目标--报备发起人
			String pushId = this.getbbrIdByBbid(bbId);
			this.pushBhMessage(bbId, type, pushId, "", "");
    	}
    	
    	return db.doInTransaction(batchSql);
    }
    
    
    /**
     * 批量通过
     * @date 2019-1-25
     * @param request
     * @return 1 成功  0 失败
     */
    public int getPlShResult(HttpServletRequest request, String type){
    	String bbId = req.getValue(request, "bbIds");//报备信息IDs
    	String operatorId = this.getUser(request).getOperatorId();
    	//String shRemark = req.getValue(request, "shRemark");//备注
    	String orgId = this.getUser(request).getOrgId();
    	String operatorName = this.getUser(request).getName();
    	String shResult = "1";//审核结果：1 通过； 0 驳回
    	String maxLev = this.getUser(request).getMaxRoleLevel();
    	
    	Boolean isTg = null;//是否审核通过
    	isTg =  true;
    	Boolean isQj = null;//是否请假
    	isQj = type.equals("1") ? true : false;
    	String sql = "";
    	BatchSql batchSql = new BatchSql();
    	
    	String[] bbIds = bbId.split(",");
    	
    	if(this.getUser(request).hasRoles(GLOBA_ZZCZR_ROLES)){//政治处主任
    		for(String ids : bbIds){
        		//流程信息表更新
    			sql = "insert into t_bb_lc_info " +
    				  "	(id, bb_id, shr, sh_date, sh_result, sh_lc_name, shr_orgid, shr_name) " +
    				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?,  '政治处主任审核', ?, ? ) ";
    			//logger.debug("save : " + str.getSql(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName}));
    			batchSql.addBatch(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName});
    			String ddxj = this.getDdxj(ids);
    			if(ddxj.equals("1") ){//大队休假、病假需要提交支队主官&& 通过
    				sql = "update t_bb_lc_state  set lc_id = 4  " +
    					  " where bb_id = ? ";
    				batchSql.addBatch(sql, new Object[]{ ids});
    				//事物处理
              		db.doInTransaction(batchSql);
    				//推送信息，推送目标--支队主官
					String pushId = this.getRegistrationIdByRoleId(GLOBA_ZDZG_ROLES, request);
					this.pushMessage(bbId, type, pushId, "", "");
    			}
    			//政治处主任为流程最后一流程&&通过
				//流程结束（更新流程结束标记、报备结束标记）
				sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
				//logger.debug("update : " + str.getSql(sql,  new Object[]{ ids}));
				batchSql.addBatch(sql, new Object[]{ ids});
    			
    			this.savePushLog(type, ids, GLOBA_ZZCZR_ROLES);
        	}
    	}else if(maxLev.equals("7")){//中队
    		for(String ids : bbIds){
    			//流程信息表更新
    			sql = "insert into t_bb_lc_info " +
    				  "	(id, bb_id, shr, sh_date, sh_result, sh_lc_name, shr_orgid, shr_name ) " +
    				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, '中队审核', ?, ? ) ";
    			batchSql.addBatch(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName});
    			if(isQj ){//请假&&通过
        			//流程状态更新(更新为大对审核)
        			sql = " update t_bb_lc_state set lc_id = 1 where bb_id = ? ";
        			batchSql.addBatch(sql, new Object[]{ ids});
        			//事物处理
              		db.doInTransaction(batchSql);
        			//推送信息，推送目标--大队主官
					String pushId = this.getDdzgRegistrationIds(orgId);
					this.pushMessage(bbId, type, pushId, "", "");
        		}else if(!isQj ){//饮酒 && 通过
        			//流程结束（更新流程结束标记、报备结束标记）
        			sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
        			batchSql.addBatch(sql, new Object[]{ ids});
        		}
    		}
    	}else if(maxLev.equals("6")){//大队级
    		for(String ids : bbIds){
    			//流程信息表更新
    			sql = "insert into t_bb_lc_info " +
    				  "	(id, bb_id, shr, sh_date, sh_result, sh_lc_name, shr_orgid, shr_name ) " +
    				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, '大队审核', ?, ? ) ";
    			batchSql.addBatch(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName});
    			//1 警务科  2 干部科
    			sql = "select (case  " +
    					"				when a.jg_lc = 1 and IFNULL(a.jwk_lc, 0) = 1 then 1  " +
    					"				when a.jg_lc = 1 and a.dd_lc = 1 then 2  " +
    					"				else 0 end ) push_flag  " +
    					"	from t_bb_info a where id = ?";
				int pushFlag = db.queryForInt(sql, new Object[]{bbId});
				String pushId = "";
				
        		if(isQj ){//请假&&通过
        			//流程状态更新(更新为 机关部门长审核)
        			sql = " update t_bb_lc_state set lc_id = 2 where bb_id = ? ";
        			batchSql.addBatch(sql, new Object[]{ ids});
        			//警务科科长or干部科科长
        			try {
    					//推送信息，推送目标--警务科科长
    					if(pushFlag == 1){//警务科科长
    						//事物处理
    		          		db.doInTransaction(batchSql);
    						pushId = this.getRegistrationIdByRoleId(GLOBA_JWK_ROLES, request);
    						this.pushMessage(bbId, type, pushId, "", "");
    					}else if(pushFlag == 2){//干部科
    						//事物处理
    		          		db.doInTransaction(batchSql);
    						pushId = this.getRegistrationIdByRoleId(GLOBA_GBK_ROLES, request);
    						this.pushMessage(bbId, type, pushId, "", "");
    					}else if(pushFlag == 1){
    						
    					}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        		}else if(!isQj ){//饮酒 && 通过
        			//流程结束（更新流程结束标记、报备结束标记）
        			sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
        			batchSql.addBatch(sql, new Object[]{ ids});
        		}
    		}
    		
    	}else if(maxLev.equals("4")){//机关领导级
    		for(String ids : bbIds){
    			//流程信息表更新
    			sql = "insert into t_bb_lc_info " +
    				  "	(id, bb_id, shr, sh_date, sh_result, sh_lc_name, shr_orgid, shr_name) " +
    				  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, '机关部门长审核', ?, ? ) ";
    			batchSql.addBatch(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName});
    			if(isQj ){//请假&&通过
    				//流程状态更新（更新为参谋长审核）
    				sql = " update t_bb_lc_state a set a.lc_id = 6 where a.bb_id = ? and exists(select 1 from t_bb_info b where a.bb_id = b.id and b.cmz_lc = 1 ) ";
    				batchSql.addBatch(sql, new Object[]{ ids});
    				//流程状态更新（更新为政治处主任审核）
    				sql = " update t_bb_lc_state a set a.lc_id = 3 where a.bb_id = ? and exists(select 1 from t_bb_info b where a.bb_id = b.id and b.zzc_lc = 1 ) ";
    				batchSql.addBatch(sql, new Object[]{ ids});
    				//1 政治处； 2 参谋长； 0 其他
    				sql = " select (case when a.zzc_lc = 1 then 1 when a.cmz_lc = 1 then 2 else 0 end ) push_flag from t_bb_info a where id = ? ";
    				int pushFlag = db.queryForInt(sql, new Object[]{bbId});
    				String pushId = "";
    				try {
    					//推送信息，推送目标--参谋长or政治处主任
    					if(pushFlag == 1){//政治处主任
    						//事物处理
    		          		db.doInTransaction(batchSql);
    						pushId = this.getRegistrationIdByRoleId(GLOBA_ZZCZR_ROLES, request);
    						this.pushMessage(bbId, type, pushId, "", "");
    						
    					}else if(pushFlag == 2){//参谋长
    						//事物处理
    		          		db.doInTransaction(batchSql);
    						pushId = this.getRegistrationIdByRoleId(GLOBA_CMZ_ROLES, request);
    						this.pushMessage(bbId, type, pushId, "", "");
    						
    					}else if(pushFlag == 1){
    						
    					}
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}else if(!isQj){//饮酒&&通过
    				//流程结束（更新流程结束标记、报备结束标记）
    				sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
    				batchSql.addBatch(sql, new Object[]{ ids});
    			}
    		}
    	}else if(this.getUser(request).hasRoles(GLOBA_CMZ_ROLES)){//参谋长
    		for(String ids : bbIds){
        			sql = "insert into t_bb_lc_info " +
    					  "	(id, bb_id, shr, sh_date, sh_result, sh_lc_name, shr_orgid, shr_name) " +
    					  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, '参谋长审核', ?, ? ) ";
        			batchSql.addBatch(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName});
        			//流程结束（流程更新结束标记、报备结束标记）
        			sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
    				batchSql.addBatch(sql, new Object[]{ ids});
    		}
    	}else if(maxLev.equals("2")){//支队主官级
    		for(String ids : bbIds){
    				//流程信息表更新
    				sql = "insert into t_bb_lc_info " +
    					  "	(id, bb_id, shr, sh_date, sh_result,  sh_lc_name, shr_orgid, shr_name) " +
    					  "values(nextval('t_bb_lc_info_sid'), ?, ?, SYSDATE(), ?, '支队主官审核', ?, ? ) ";
    				batchSql.addBatch(sql, new Object[]{ ids, operatorId, shResult, orgId, operatorName});
					//流程结束（更新流程结束标记、报备结束标记）
					sql = " update t_bb_info set end_flag = 1, sh_end_flag = 1 where id = ? ";
					batchSql.addBatch(sql, new Object[]{ ids});
    		}
    	}
   
    	
    	return db.doInTransaction(batchSql);
    }
    
    /**
     * 主官是否有处理权限
     * @date 2019-1-17
     * @param bbId
     * @return 1 是； 0 否
     */
    public String getDdxj(String bbId){
    	String sql = " select count(1) from t_bb_info a where a.id = ? and zg_lc = 1 ";
    	return db.queryForString(sql, new Object[]{bbId});
    }
    
    /**
     * 销假操作
     * 更新更新结束日期、更新销假标记、更新报备天数
     * @date 2019-1-17
     * @param request
     * @return
     */
    public int getXjResult(HttpServletRequest request){
    	String bbId = req.getValue(request, "bbId");//报备ID
    	String sql = "";
    	BatchSql batch = new BatchSql();
    	//更新结束日期
    	sql = " update t_bb_info a set a.end_date = sysdate() where a.id = ? ";
    	batch.addBatch(sql, new Object[]{bbId});
    	//更新销假日期
    	sql = " update t_bb_info a set a.xj_date = sysdate() where a.id = ? ";
    	batch.addBatch(sql, new Object[]{bbId});
    	//更新销假标记
    	sql = " update t_bb_info a set a.xj_flag = 1 where a.id = ?  ";
    	batch.addBatch(sql, new Object[]{bbId});
    	//更新报备天数、报备日期
    	sql = "update t_bb_info a " +
    		  "	  set a.bb_days = TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d')) + 1, " +
    		  "	  xj_date = sysdate()  " +
    		  " where a.id = ? " +
    		  "   and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d')) > 0 ";//结束日期小于当前日期
    	
    	batch.addBatch(sql, new Object[]{bbId});
    	return db.doInTransaction(batch);
    }
    
    /**
     * 请假超时天数
     * @date 2019-1-24
     * @param request
     * @return
     */
    public String getQjcsDays(HttpServletRequest request){
    	String bbId = req.getValue(request, "bbId");//报备ID
    	String sql = "";
    	//
    	sql = "select  " +
    			"	TimeStampDiff(DAY, DATE_FORMAT(IFNULL(a.end_date, SYSDATE()), '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d'))  " +
    			"from t_bb_info a  " +
    			"where a.id = ? ";
    	return db.queryForString(sql, new Object[]{bbId});
    }
    
    /**
     * 饮酒超时天数
     * @date 2019-1-24
     * @param request
     * @return
     */
    public String getYjcsDays(HttpServletRequest request){
    	String bbId = req.getValue(request, "bbId");//报备ID
    	String sql = "";
    	//
    	sql = "select  " +
    			"	TimeStampDiff(DAY, DATE_FORMAT(IFNULL(a.start_date, SYSDATE()), '%Y-%m-%d'), DATE_FORMAT(SYSDATE(), '%Y-%m-%d'))  " +
    			"from t_bb_info a  " +
    			"where a.id = ? ";
    	return db.queryForString(sql, new Object[]{bbId});
    }
    
    /**
     * 归家操作
     * 更新、更新销假标记（使用销假标记）
     * @date 2019-1-23
     * @param request
     * @return
     */
    public int getGjResult(HttpServletRequest request){
    	String bbId = req.getValue(request, "bbId");//报备ID
    	String sql = "";
    	BatchSql batch = new BatchSql();
    	//更新销假标记
    	sql = " update t_bb_info a set a.xj_flag = 1 where a.id = ?  ";
    	batch.addBatch(sql, new Object[]{bbId});
    	//更新归家日期
    	sql = " update t_bb_info set xj_date = sysdate() where id = ? ";
    	batch.addBatch(sql, new Object[]{bbId});
    	return db.doInTransaction(batch);
    	
    }
    
    /**
     * 头像上传
     * @param request
     * @return 2 文件大于2M 1 上传成功 0 上传失败
     */
   public int impTxResult(HttpServletRequest request, CommonsMultipartFile file, String fileFileName) {
        String operator = this.getUser(request).getOperatorId();
        
        int rst = 0;
        //logger.debug(file.getSize());
        
        if((int)file.getSize() / 1024 >= 2048){//图片大于2M
        	rst = 0;
        	return rst;
        }
        
        String qm_name = "";
        String qm_url = "";
        String fileName = "";
        
        try {
			// 上传文件存档
			String save_dir = request.getRealPath("/pic/");
			FileHelper fileHelper = new FileHelper();
			qm_name = fileHelper.getToFileName(fileFileName);//后追加时间
			fileName = save_dir + qm_name;
			qm_url = save_dir + qm_name;
			fileHelper.copyFile(file, fileName);
		} catch (Exception e) {
			rst = 0;
		}
        
        BatchSql batchSql = new BatchSql();
        
        String sql = "update t_user  " +
	        		"set qm_name = ?, qm_name_d = ?, qm_url = ?  " +
	        		"where operator_id = ? ";
        batchSql.addBatch(sql, new Object[]{ fileFileName, qm_name, qm_url, operator});
        rst = db.doInTransaction(batchSql);
        return rst;
    }
   
   /**
    * 获取中队主官工号（根据当前登录人组织归属）
    * @date 2019-1-24
    * @return
    */
   public String getZdzgId(String orgId){
	   String sql = "select GROUP_CONCAT(a.registration_id  )  " +
				   "from t_user a, t_user_role b   " +
				   "where a.OPERATOR_ID = b.OPERATOR_ID " +
				   "  and a.ORG_ID = ? " +
				   "	and b.ROLE_ID = ? ";
	   //logger.debug("get zd id : " + str.getSql(sql, new Object[]{orgId, GLOBA_ZD_ROLES}));
	   return db.queryForString(sql, new Object[]{orgId, GLOBA_ZD_ROLES});
   }
   
   /**
    * 获取中队主官设备ID（根据当前登录人组织归属）
    * @date 2019-2-12
    * @return
    */
   public String getZdzgRegistrationIds(String orgId){
	   String sql = "select GROUP_CONCAT(a.registration_id  )    " +
				   "from t_user a, t_user_role b   " +
				   "where a.OPERATOR_ID = b.OPERATOR_ID " +
				   "  and a.ORG_ID = ? " +
				   "	and b.ROLE_ID in ( " + GLOBA_ZD_ROLES + " )";
	   //logger.debug("get zd id : " + str.getSql(sql, new Object[]{orgId, GLOBA_ZD_ROLES}));
	   //return db.queryForList(sql, new Object[]{orgId, GLOBA_ZD_ROLES});
	   return db.queryForString(sql, new Object[]{orgId});
   }
   
   /**
    * 获取大队主官工号（根据当前登录人组织归属）
    * @date 2019-1-21
    * @return
    */
   public String getDdzgId(String orgId){
	   String sql = "select " +
				   "	GROUP_CONCAT(a.registration_id  )  " +
				   "from " +
				   "	t_user a, " +
				   "	t_user_role c  " +
				   "where " +
				   "	a.ORG_ID = ( select b.SUPERIOR_ID from t_organization b where b.ORG_ID = ? )  " +
				   "	and a.OPERATOR_ID = c.OPERATOR_ID  " +
				   "	and c.ROLE_ID = ? and a.STATUS = 1 ";
	   //logger.debug("get ddzg id : " + str.getSql(sql, new Object[]{orgId, GLOBA_DDZG_ROLES}));
	   return db.queryForString(sql, new Object[]{orgId, GLOBA_DDZG_ROLES});
   }
   
   /**
    * 获取大队主官设备ID（根据当前登录人组织归属）
    * @date 2019-2-11
    * @return
    */
   public String getDdzgRegistrationIds(String orgId){
	   String sql = "select " +
			   		"	GROUP_CONCAT(a.registration_id  )  " +
				   "from " +
				   "	t_user a, " +
				   "	t_user_role c  " +
				   "where " +
				   "	a.ORG_ID = ( select b.SUPERIOR_ID from t_organization b where b.ORG_ID = ? )  " +
				   "	and a.OPERATOR_ID = c.OPERATOR_ID  " +
				   "	and c.ROLE_ID = ? and a.STATUS = 1 ";
	   //logger.debug("get ddzg id : " + str.getSql(sql, new Object[]{orgId, GLOBA_DDZG_ROLES}));
	   //return db.queryForList(sql, new Object[]{orgId, GLOBA_DDZG_ROLES});
	   return db.queryForString(sql, new Object[]{orgId, GLOBA_DDZG_ROLES});
   }
   
   /**
    * 获取大队主官设备ID（根据当前登录人组织归属）--一般干部
    * @date 2019-2-11
    * @return
    */
   public String getDdzgRegistrationIdsDdyb(String orgId){
	   String sql = "select " +
			   		"	GROUP_CONCAT(a.registration_id  )  " +
				   "from " +
				   "	t_user a, " +
				   "	t_user_role c  " +
				   "where " +
				   "	a.ORG_ID = ?  " +
				   "	and a.OPERATOR_ID = c.OPERATOR_ID  " +
				   "	and c.ROLE_ID in (  " + GLOBA_DDZG_ROLES + " ) and a.STATUS = 1 ";
	   //logger.debug("get ddzg id : " + str.getSql(sql, new Object[]{orgId}));
	   //return db.queryForList(sql, new Object[]{orgId, GLOBA_DDZG_ROLES});
	   return db.queryForString(sql, new Object[]{orgId});
   }
   
   /**
    * 获取机关领导设备ID（根据当前登录人组织归属）
    * @date 2019-1-21
    * @return
    */
   public String getjgLdregistRationId(String orgId){
	   String sql = "select " +
				   "	GROUP_CONCAT(a.registration_id  )  " +
				   "from " +
				   "	t_user a, " +
				   "	t_user_role c  " +
				   "where " +
				   "	a.ORG_ID = ( select b.SUPERIOR_ID from t_organization b where b.ORG_ID = ? )  " +
				   "	and a.OPERATOR_ID = c.OPERATOR_ID  " +
				   "	and c.ROLE_ID = ? ";
	   return db.queryForString(sql, new Object[]{orgId, GLOBA_BMZ_ROLES});
   }
   
   /**
    * 根据角色获取设备ID
    * @date 2019-1-21
    * @return
    */
   public String getRegistrationIdByRoleId(String roleId, HttpServletRequest request){
	   String operatorId = this.getUser(request).getOperatorId();
	   String sql =" select  GROUP_CONCAT(bb.aa  )   from ( " + 
			   	   "select " +
				   "	DISTINCT( a.registration_id)  aa   " +
				   "from " +
				   "	t_user a, " +
				   "	t_user_role b  " +
				   "where " +
				   "	a.OPERATOR_ID = b.OPERATOR_ID  " +
				   "	and b.ROLE_ID in( " + roleId +  ")  and a.STATUS = 1 and b.OPERATOR_ID <> ? GROUP BY registration_id) as bb " ;
	   logger.debug("getRegistrationIdByRoleId : " + str.getSql(sql, new Object[]{operatorId}));
	   return db.queryForString(sql, new Object[]{operatorId});
   }
   
   /**
    * 获取报备人工号
    * @date 2019-1-21
    * @return
    */
   public String getbbrIdByid(String bbid){
	   String sql = " select a.bbr from t_bb_info a where a.id = ? ";
	   return db.queryForString(sql, new Object[]{bbid});
   }
   
   /**
    * 获取报备人设备ID
    * @date 2019-2-12
    * @return
    */
   public String getbbrIdByBbid(String bbid){
	   String sql = " select a.registration_id from t_user a, t_bb_info b where b.id = ? and b.bbr = a.OPERATOR_ID and a.STATUS = 1 ";
	   logger.debug("getbbrIdByBbid : " + str.getSql(sql, new Object[]{bbid}));
	   return db.queryForString(sql, new Object[]{bbid});
   }
   
   /**
    * 获取报备人姓名
    * @date 2019-2-12
    * @return
    */
   public String getbbrNameByBbid(String bbid){
	   String sql = " select a.name from t_user a, t_bb_info b where b.id = ? and b.bbr = a.OPERATOR_ID and a.STATUS = 1 ";
	   sql = str.getSql(sql, new Object[]{bbid});
	   return db.queryForString(sql);
   }
   
   /**
    * 获取已审核列表
    * @date 2019-1-24
    * @param request
    * @return
    */
   public List getAuditedList(HttpServletRequest request, String code){
	   String opertorId = this.getOperatorId(request);
	   String maxLev = this.getUser(request).getMaxRoleLevel();//登录人最大角色层级
	   String type = req.getValue(request, "type");// 	类型：1 请假； 2 饮酒
	   String sDate = req.getValue(request, "sDate");//查询时间 1：一周内； 2：一个月内； 3 3月内； 4 半年内； 5 一年内
	   String sOrgId = req.getValue(request, "sOrgId");//待查询组织ID 
	   String sName = req.getValue(request, "sName");//待查询名称
	   List paramsList = new ArrayList();
	   String sql = "";
	   String dateSql = "";
	   
	   if(sDate.equals("")){
		   dateSql = " and DATE_FORMAT(a.start_date, '%Y-%m-%d') = DATE_FORMAT(sysdate(), '%Y-%m-%d') ";
	   }else if(sDate.equals("1")){//一周内
		   dateSql = " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 DAY), '%Y-%m-%d')  and DATE_FORMAT(a.start_date, '%Y-%m-%d') <= DATE_FORMAT(sysdate(), '%Y-%m-%d') ";
	   }else if(sDate.equals("2")){//一月内
		   dateSql = " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 MONTH), '%Y-%m-%d') and DATE_FORMAT(a.start_date, '%Y-%m-%d') <= DATE_FORMAT(sysdate(), '%Y-%m-%d') ";
	   }else if(sDate.equals("3")){//3月内
		   dateSql = " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -3 MONTH), '%Y-%m-%d') and DATE_FORMAT(a.start_date, '%Y-%m-%d') <= DATE_FORMAT(sysdate(), '%Y-%m-%d') ";
	   }else if(sDate.equals("4")){//半年内
		   dateSql = " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -6 MONTH), '%Y-%m-%d') and DATE_FORMAT(a.start_date, '%Y-%m-%d') <= DATE_FORMAT(sysdate(), '%Y-%m-%d') ";
	   }else if(sDate.equals("4")){//一年内
		   dateSql = " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -12 MONTH), '%Y-%m-%d') and DATE_FORMAT(a.start_date, '%Y-%m-%d') <= DATE_FORMAT(sysdate(), '%Y-%m-%d') ";
	   }
	   
	   //logger.debug("sOrgId : " + sOrgId);
	   
	   if(!this.getUser(request).hasRoles(GLOBA_ZDZG_ROLES) ){//非支队主官
		   sql = "select  " +
				   "	a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i:%s') start_date, " +
				   "(case  " +
	    			"		when a.type = 1 and IFNULL(a.end_flag, 0) = 0 then '待审核'  " +
	    			"	  when a.type = 1 and IFNULL(a.end_flag, 0) = 1 and IFNULL(a.xj_flag, 0) = 0 then '待销假'  " +
	    			"		when a.type = 2 and IFNULL(a.end_flag, 0) = 0 then '待审核' " +
	    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 0 and ifnull(end_flag, 0) = 1 then '待归家' " +
	    			"		when a.type = 1 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) <= 0  then '已销假'  " +
	    			"	    when a.type = 1 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) > 0  then '已销假（超时）'  " +
	    			"		when a.type = 1 and IFNULL(a.end_flag, 0) = 1 and IFNULL(a.sh_end_flag, 0) = 0  then '驳回' " +
	    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) = 0 then '归家' " +
	    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) > 0 then '归家(隔天)' " +
	    			"		when a.type = 2 and IFNULL(a.end_flag, 0) = 1 and ifnull(a.sh_end_flag, 0) = 0 then '驳回' else '待审核' " +
	    			"	 end) bb_state_name, a.jj_lxr_name, a.jj_lxr_phone, b.name bbr_name, a.cp_num, " +
	    			"	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, " +
				   "	DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i:%s') end_date, a.bb_days, ifnull(sh_end_flag, 0) sh_end_flag, ifnull(end_flag, 0) end_flag, ifnull(xj_flag, 0) xj_flag " +
				   "from t_bb_info a, t_user b, ( select c.bb_id from t_bb_lc_info c where c.shr = ? ) as cc " +
				   "where a.id = cc.bb_id " +
				   "  and a.type = ? " +
				   "  and a.bbr = b.operator_id and b.STATUS = 1 " ;
		   paramsList.add(code);
		   paramsList.add(opertorId);
		   paramsList.add(type);
		   
		  if(!"".equals(sName)){
			  sql += " and b.name like ? ";
			  paramsList.add("%" + sName + "%");
		  } 
		  if(!"".equals(sOrgId)){
			  sql += " and b.org_id = ? ";
			  paramsList.add(sOrgId);
		  }
		   
		   //logger.debug("2 : " + str.getSql(sql, paramsList.toArray()));
	   }else if(this.getUser(request).hasRoles(GLOBA_ZDZG_ROLES) || this.getUser(request).hasRoles(GLOBA_ALLSH_ROLES)){//支队主官、可配权限
		   sql = "select  " +
				   "	a.id, a.type, a.concrete_type, a.bbr, a.address, DATE_FORMAT(a.start_date, '%Y-%m-%d %k:%i:%s') start_date, b.name bbr_name, a.cp_num, " +
				   "(case  " +
	    			"		when a.type = 1 and IFNULL(a.end_flag, 0) = 0 then '待审核'  " +
	    			"	  when a.type = 1 and IFNULL(a.end_flag, 0) = 1 and IFNULL(a.xj_flag, 0) = 0 then '待销假'  " +
	    			"		when a.type = 2 and IFNULL(a.end_flag, 0) = 0 then '待审核' " +
	    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 0 and ifnull(end_flag, 0) = 1 then '待归家' " +
	    			"		when a.type = 1 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) <= 0  then '已销假'  " +
	    			"	    when a.type = 1 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.end_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) > 0  then '已销假（超时）'  " +
	    			"		when a.type = 1 and IFNULL(a.end_flag, 0) = 1 and IFNULL(a.sh_end_flag, 0) = 0  then '驳回' " +
	    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) = 0 then '归家' " +
	    			"		when a.type = 2 and IFNULL(a.xj_flag, 0) = 1 and TimeStampDiff(DAY, DATE_FORMAT(a.start_date, '%Y-%m-%d'), DATE_FORMAT(xj_date, '%Y-%m-%d')) > 0 then '归家(隔天)' " +
	    			"		when a.type = 2 and IFNULL(a.end_flag, 0) = 1 and ifnull(a.sh_end_flag, 0) = 0 then '驳回' else '待审核' " +
	    			"	 end) bb_state_name, " + 
				   "	(select b.DD_ITEM_NAME from t_ddw b where b.DD_ITEM_CODE = a.concrete_type and b.DATA_TYPE_CODE = ?) concrete_type_name, b.name bbr_name, a.jj_lxr_name, a.jj_lxr_phone,  " +
				   "	DATE_FORMAT(a.end_date, '%Y-%m-%d %k:%i:%s') end_date, a.bb_days,ifnull(sh_end_flag, 0) sh_end_flag, ifnull(end_flag, 0) end_flag, ifnull(xj_flag, 0) xj_flag " +
				   "from t_bb_info a, t_user b " +
				   "where 1 = 1 " +
				   "  and a.type = ? " +
				   "  and a.bbr = b.operator_id " +
				   "	and IFNULL(a.zd_cl, 0) = 0 "; //非战士提出"
		   paramsList.add(code);
		   paramsList.add(type);
		   if(!"".equals(sName)){
			  sql += " and b.name like ? ";
			  paramsList.add("%" + sName + "%");
		  } 
		  if(!"".equals(sOrgId)){
			  sql += " and b.org_id = ? ";
			  paramsList.add(sOrgId);
		  }
		 
	   }
	   sql += dateSql;
	   sql += " order by a.rec_date desc ";
	   logger.debug("getAuditedList : " + str.getSql(sql, paramsList.toArray()));
	   return db.queryForList(sql, paramsList.toArray());
   }
   
   /**
    * 获取所有组织
    * @date 2019-1-24
    * @param request
    * @return
    */
   public List getOrgsList(HttpServletRequest request){
	   String opertorId = this.getOperatorId(request);
	   String sql = "select '' org_id, '全部' org_name from dual " +
				   "union " +
				   "select aa.* from (select org_id, org_name  from t_organization a where a.STATUS = 1 and org_id not in(10000, 10002, 10003, 10048, 10049, 10050, 10051, 10052, 10067, 10065) order by org_lev, org_id " +
				   ") as aa" ;
	   //logger.debug("getOrgsList : " + sql);
	   return db.queryForList(sql);
   }
   
   /**
    * 获取消息列表
    * @date 2019-1-21
    * @param request
    * @return
    */
   public List getPushMessageList(HttpServletRequest request){
	   String opertorId = this.getOperatorId(request);
	   int pageNum = Integer.valueOf(request.getParameter("pageNum"));//页
	   
	   String sql = " select a.id, a.title, a.content, DATE_FORMAT(a.push_date, '%Y-%m-%d %k:%i:%s') push_date from t_push_all_info a order by a.id desc ";
	   sql = str.getSql(sql, new Object[]{opertorId});
   	   return db.getForListForMobile(sql, pageNum, PAGE_SIZE);
	   //return db.queryForList(sql, new Object[]{opertorId});
   }
   
   /**
    * 获取审核详细信息，包括审核各流程信息
    * @param request
    * @return
    */
   public Map<String, Object> getMessageDetail(HttpServletRequest request){
   	String id = req.getValue(request, "messageId");//消息ID
   	String sql = "";
   	
   	Map<String, Object> map = new HashMap();
   	//
   	sql = "select a.id, a.title, a.content, DATE_FORMAT(a.push_date, '%Y-%m-%d') push_date from t_push_all_info a where 1 = 1 " +
   		  "   and  a.id = ? ";
   	map.put("messageInfo", db.queryForMap(sql, new Object[]{id}));
   	
   	return map;
   }
   
   /**
    * 保存or 更新 消息条数
    * @date 2019-1-21
    * @param request
    * @return
    */
   public void savePushMessageInfo(HttpServletRequest request){
	   String opertorId = this.getOperatorId(request);
	   BatchSql batch = new BatchSql();
	   
	   String sql = " select count(1) from t_push_operator a where a.operator_id = ? ";
	   String isExist = db.queryForString(sql, new Object[]{opertorId});
	   isExist = isExist.equals("") ? "0" : isExist;
	   sql = " select count(1) from t_push_all_info where 1 = 1 ";
	   String pushCountNum = db.queryForString(sql);
	   
	   if(isExist.equals("0")){//新增
		   sql = " insert into t_push_operator values(?, ?) ";
		   batch.addBatch(sql, new Object[]{opertorId, pushCountNum});
		   db.doInTransaction(batch);
	   }else{//更新
		   sql = " update t_push_operator a set a.push_num = ? where a.operator_id = ? ";
		   batch.addBatch(sql, new Object[]{pushCountNum, opertorId});
		   db.doInTransaction(batch);
	   }
	   
	   
   	   //return db.getForListForMobile(sql, pageNum, PAGE_SIZE);
	   //return db.queryForList(sql, new Object[]{opertorId});
   }
   
   /**
    * 保存推送日志
    * @date 2019-1-21
    */
   public void savePushLog(String type, String bbId, String pushId){
	   BatchSql batch = new BatchSql();
	   String sql = "insert into t_push_info " +
				   "	(id, push_operatorid, push_date, type, bb_id) " +
				   "values(nextval('t_push_info_sid'), ?, SYSDATE(), ?, ?) ";
	   batch.addBatch(sql, new Object[]{pushId, type, bbId});
	   //logger.debug("save push log : " + str.getSql(sql, new Object[]{pushId, type, bbId}));
	   db.doInTransaction(batch);
   }
   
   /**
    * 获取推送消息总条数
    * @return
    */
   public int getTabPushNum(){
	   String sql = " select count(1) from t_push_all_info ";
	   return db.queryForInt(sql);
   }
   
   /**
    * 获取已读条数
    * @return
    */
   public String getReadPushNum(HttpServletRequest request){
	   String operatId = this.getUser(request).getOperatorId();
	   String sql = " select ifnull(a.push_num, 0) from t_push_operator a where a.operator_id = ? ";
	   sql = str.getSql(sql, new Object[]{operatId});
	   return db.queryForString(sql);
	   
   }
   
   /**
    * 获取类别名称
    * @param type
    * @param concreteType
    * @return
    */
   public String getConcreteTypeNameByTypeAndConcreteType(String type, String concreteType){
	   String dataTypeCode = type.equals("1") ? "10032" : "10033";
	   String sql = " select a.DD_ITEM_NAME from t_ddw a where a.DATA_TYPE_CODE = ? and a.DD_ITEM_CODE = ?  ";
	   return db.queryForString(sql, new Object[]{dataTypeCode, concreteType});
   }
   
   /**
    * 推送信息
    * @date 2019-2-12 
    */
   public void pushMessage(String bbId, String type, String pushValues, String bbrName, String concreteType){
	   //推送消息
	   String typeName = type.equals("1") ? "请假报备" : "饮酒报备";//报备类别
	   String concreteTypeName = concreteType.equals("") ? this.getConcreteTypeNameByBbId(bbId, type) : this.getConcreteTypeNameByTypeAndConcreteType(type, concreteType) ;//类别名称
	   String operatorName = bbrName.equals("") ? this.getbbrNameByBbid(bbId) : bbrName;//报备人姓名
	   
	   logger.debug("operatorName : " + operatorName);
	   logger.debug("concreteTypeName : " + concreteTypeName);
	   String msgContent = operatorName + "发起类别为：" + concreteTypeName + "的" + typeName + "申请。";
	   String notificationTitle = "您有一条" + typeName + "信息待处理";
	   logger.debug("msgContent : " + msgContent);
	   logger.debug("notificationTitle : " + notificationTitle);
	   
	   JPushUtil push = new JPushUtil();
	  logger.debug("pushValues : " + pushValues);
	   try {
		   //推送
		   String[] pushIds = pushValues.split(",");
		   for(String pId : pushIds){
			   if(!pId.equals("")){//非空
				   push.pushMsg(pId, msgContent, "", "", notificationTitle);
			   }
		   }
	  } catch (Exception e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	   
   }
   
   /**
    * 推送信息
    * @date 2019-2-14
    */
   public void pushBhMessage(String bbId, String type, String pushValues, String bbrName, String concreteType){
	   //推送消息
	   String typeName = type.equals("1") ? "请假报备" : "饮酒报备";//报备类别
	   String concreteTypeName = concreteType.equals("") ? this.getConcreteTypeNameByBbId(bbId, type) : this.getConcreteTypeNameByTypeAndConcreteType(type, concreteType) ;//类别名称
	   String operatorName = bbrName.equals("") ? this.getbbrNameByBbid(bbId) : bbrName;//报备人姓名
	   
	   logger.debug("operatorName : " + operatorName);
	   logger.debug("concreteTypeName : " + concreteTypeName);
	   String msgContent = "您发起类别为：" + concreteTypeName + "的" + typeName + "申请被驳回";
	   String notificationTitle = "您有一条报备申请被驳回";
	   logger.debug("msgContent : " + msgContent);
	   logger.debug("notificationTitle : " + notificationTitle);
	   logger.debug("pushIds : " + pushValues);
	   JPushUtil push = new JPushUtil();
	  
	   try {
		   //推送
		   String[] pushIds = pushValues.split(",");
		   for(String pId : pushIds){
			   logger.debug("Pid : " + pId);
			   if(!pId.equals("")){//非空
				   push.pushMsg(pId, msgContent, "", "", notificationTitle);
			   }
		   }
	  } catch (Exception e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	   
   }

}
