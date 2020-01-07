package ytsd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sgy.util.db.BatchSql;

import web.service.BaseService;

@Service
public class SaleGroupService extends BaseService{

	/**
	 * 描述：获取小组信息
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-21
	 */
	public List getGroupList(HttpServletRequest request) {
		String group_name = req.getValue(request, "group_name");
		List paramList = new ArrayList();
		String sql = "SELECT " +
				"	a.id, " +
				"	a.group_name, " +
				"	DATE_FORMAT( " +
				"		a.record_date, " +
				"		'%Y-%m-%d %H:%i:%S' " +
				"	) record_date " +
				"FROM " +
				"	t_wx_salegroup_info a " +
				"WHERE " +
				"	1 = 1 ";
		if(!"".equals(group_name)){
			sql += " and group_name like concat('%',?,'%') ";
			paramList.add(group_name);
		}
		sql += " ORDER BY a.record_date desc ";
		
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
	}
	

	/**
	 * 描述：获取小组详情
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-21
	 */
	public Map groupMap(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String sql =  "SELECT " +
				"	a.id, " +
				"	a.group_name " +
				"FROM " +
				"	t_wx_salegroup_info a " +
				"WHERE " +
				"	 a.id = ? ";
		
		return db.queryForMap(sql, new Object[]{id});
	}
	

	public int saveOrUpdateInfo(HttpServletRequest request) {
		String method = req.getValue(request, "method");
		String id = req.getValue(request, "id");
		String group_name = req.getValue(request, "group_name");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if("add".equals(method)){
			sql = "INSERT INTO t_wx_salegroup_info ( " +
				"	group_name, " +
				"	record_id, " +
				"	record_date " +
				") " +
				"VALUES " +
				"	(?, ?, SYSDATE()) ";
			batchSql.addBatch(sql, new Object[]{group_name, operatorId});
		}else{
			sql = "INSERT INTO t_wx_salegroup_info_his ( " +
					"	id, " +
					"	group_name, " +
					"	record_id, " +
					"	record_date, " +
					"	delete_id, " +
					"	delete_date " +
					") SELECT " +
					"	id, " +
					"	group_name, " +
					"	record_id, " +
					"	record_date, " +
					"	?,  " +
					"  SYSDATE() " +
					"FROM " +
					"	t_wx_salegroup_info " +
					" where id = ? " ;
			
			batchSql.addBatch(sql, new Object[]{operatorId, id});
			sql = " UPDATE t_wx_salegroup_info " +
					"	 SET group_name = ?,  " +
					"			 record_id = ?,  " +
					"			 record_date = SYSDATE() " +
					" where id = ? ";
			batchSql.addBatch(sql, new Object[]{group_name, operatorId, id});
		}
		return db.doInTransaction(batchSql);
	}

	public int deleteInfo(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String usercntSql = " select count(1) from t_wx_user_info where group_id = ? and status = 1 ";
		int usercnt = db.queryForInt(usercntSql, new Object[]{id});
		if(usercnt > 0){
			return 2;
		}
		String sql = "INSERT INTO t_wx_salegroup_info_his ( " +
				"	id, " +
				"	group_name, " +
				"	record_id, " +
				"	record_date, " +
				"	delete_id, " +
				"	delete_date " +
				") SELECT " +
				"	id, " +
				"	group_name, " +
				"	record_id, " +
				"	record_date, " +
				"	?,  " +
				"  SYSDATE() " +
				"FROM " +
				"	t_wx_salegroup_info " +
				" where id = ? " ;
		
		batchSql.addBatch(sql, new Object[]{operatorId, id});
		sql = " delete from t_wx_salegroup_info where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		return db.doInTransaction(batchSql);
	}
	
}
