package ytsd.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sgy.util.db.BatchSql;

import web.service.BaseService;

@Service
public class WxUserInfoService extends BaseService{
	
	/**
	 * @param 获取小组信息
	 * @return
	 */
	public List getSaleGroupList(HttpServletRequest request){
		String sql = " select id group_id, group_name from t_wx_salegroup_info order by record_date desc ";
		return db.queryForList(sql);
	}
	
	/**
	 * 获取微信用户列表
	 * @param request
	 * @return
	 */
	public List getWxUserList(HttpServletRequest request){
		String name = req.getValue(request, "name");
		String status = req.getValue(request, "status");
		String mobile = req.getValue(request, "mobile");
		String group_id = req.getValue(request, "group_id");
		List paramList = new ArrayList();
		String sql = "SELECT " +
					"	id, " +
					"	NAME, " +
					"	STATUS, " +
					"	case when status = 1 then '有效' else '失效' end status_name, " +
					"	mobile, " +
					"	CASE " +
					"WHEN role_id = 1 THEN " +
					"	'董事长' " +
					"WHEN role_id = 2 THEN " +
					"	'销售经理' " +
					"WHEN role_id = 3 THEN " +
					"	'销售人员' " +
					"WHEN role_id = 4 THEN " +
					"	'销售总监' " +
					"END role_name, " +
					" DATE_FORMAT( " +
					"	update_date, " +
					"	'%Y-%m-%d %H:%i:%S' " +
					") update_date, " +
					" fn_getusername (update_id) update_name , "
					+ " (select group_name from t_wx_salegroup_info b where a.group_id = b.id) group_name " +
					"FROM " +
					"	t_wx_user_info a " +
					"WHERE " +
					"	1 = 1 ";
		if(!"".equals(name)){
			sql += " and name like ? ";
			paramList.add("%" + name + "%");
		}
		if(!"".equals(status)){
			sql += " and status = ? ";
			paramList.add(status);
		}
		if(!"".equals(mobile)){
			sql += " and mobile = ? ";
			paramList.add(mobile);
		}
		if(!"".equals(group_id)){
			sql += " and group_id = ? ";
			paramList.add(group_id);
		}
		sql += " order by role_id, update_date desc ";
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
	}
	
	public Map getWxUserMap(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String sql = " select id, NAME, status, mobile, group_id, role_id from t_wx_user_info where id = ? ";
		return db.queryForMap(sql, new Object[]{id});
	}

	public int saveOrUpdateInfo(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String method = req.getValue(request, "method");
		String name = req.getValue(request, "name");
		String mobile = req.getValue(request, "mobile");
		String role_id = req.getValue(request, "role_id");
		String group_id = req.getValue(request, "group_id");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if("add".equals(method)){
			sql = "INSERT INTO t_wx_user_info ( " +
				"	NAME, " +
				"	STATUS, " +
				"	mobile, " +
				"	role_id, " +
				"   group_id, " +
				"	record_id, " +
				"	record_date, " +
				"	update_id, " +
				"	update_date " +
				") " +
				"VALUES " +
				"	( " +
				"		?, 1 ,?,?,?, ?, SYSDATE() ,?, SYSDATE() " +
				"	) ";
			batchSql.addBatch(sql, new Object[]{name, mobile, role_id, group_id, operatorId, operatorId});
		}else{
			sql = "INSERT INTO t_wx_user_info_his ( " +
				"	id, " +
				"	openid, " +
				"	NAME, " +
				"	STATUS, " +
				"	mobile, " +
				"	role_id, " +
				"	record_id, " +
				"	record_date, " +
				"	update_id, " +
				"	update_date, " +
				"	del_id, " +
				"	del_date " +
				") SELECT " +
				"	id, " +
				"	openid, " +
				"	NAME, " +
				"	STATUS, " +
				"	mobile, " +
				"	role_id, " +
				"	record_id, " +
				"	record_date, " +
				"	update_id, " +
				"	update_date, " +
				"	?, SYSDATE() " +
				"FROM " +
				"	t_wx_user_info " +
				"WHERE " +
				"	id = ? ";
			batchSql.addBatch(sql, new Object[]{operatorId, id});
			sql = "UPDATE t_wx_user_info " +
				"SET NAME =?, mobile =?, role_id =?, group_id = ?, update_id =?, update_date = SYSDATE() " +
				"WHERE " +
				"	id = ? ";
			batchSql.addBatch(sql, new Object[]{name, mobile, role_id, group_id, operatorId, id});
		}
		return db.doInTransaction(batchSql);
	}

	public int deleteWxUserInfo(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "INSERT INTO t_wx_user_info_his ( " +
				"	id, " +
				"	openid, " +
				"	NAME, " +
				"	STATUS, " +
				"	mobile, " +
				"	role_id, " +
				"	record_id, " +
				"	record_date, " +
				"	update_id, " +
				"	update_date, " +
				"	del_id, " +
				"	del_date " +
				") SELECT " +
				"	id, " +
				"	openid, " +
				"	NAME, " +
				"	STATUS, " +
				"	mobile, " +
				"	role_id, " +
				"	record_id, " +
				"	record_date, " +
				"	update_id, " +
				"	update_date, " +
				"	?, SYSDATE() " +
				"FROM " +
				"	t_wx_user_info " +
				"WHERE " +
				"	id = ? ";
		batchSql.addBatch(sql, new Object[]{operatorId, id});
		sql = " update t_wx_user_info set status = 0, openid = null where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		return db.doInTransaction(batchSql);
	}

	public int backWxUserInfo(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String sql = " update t_wx_user_info set status = 1 where id = ? ";
		return db.update(sql, new Object[]{id});
	}
	
	public int checkMobile(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String mobile = req.getValue(request, "mobile");
		List paramList = new ArrayList();
		String sql = " select count(1) from t_wx_user_info where mobile = ? ";
		paramList.add(mobile);
		if(!"".equals(id)){
			sql += " and id != ? ";
			paramList.add(id);
		}
		return db.queryForInt(sql, paramList);
	}
}
