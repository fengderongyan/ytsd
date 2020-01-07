package ytsd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sgy.util.db.BatchSql;

import web.service.BaseService;

@Service
public class SaleStatusService extends BaseService{

	/**
	 * 描述：获取楼栋列表
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-21
	 */
	public List getBuildList(HttpServletRequest request) {
		String build_num = req.getValue(request, "build_num");
		List paramList = new ArrayList();
		String sql = "SELECT " +
					"	id, " +
					"	build_num, " +
					"	case when sale_status = 0 then '未出售' when sale_status = 1 then '销售中' when sale_status = 2 then '已售罄' end sale_status , " +
					"	fn_getusername(record_id) record_name, " +
					"  DATE_FORMAT(record_date,'%Y-%m-%d %H:%i:%S') record_date " +
					"FROM " +
					"	t_wx_build_sale_status where 1 = 1 ";
		if(!"".equals(build_num)){
			sql += " and build_num = ? ";
			paramList.add(build_num);
		}
		sql += " ORDER BY build_num ";
		
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
	}

	/**
	 * 描述：获取楼栋详情
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-21
	 */
	public Map buildMap(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String sql =  "SELECT " +
					"	id, " +
					"	build_num, " +
					"	sale_status " +
					"FROM " +
					"	t_wx_build_sale_status " +
					" where id = ? ";
		
		return db.queryForMap(sql, new Object[]{id});
	}

	public int saveOrUpdateInfo(HttpServletRequest request) {
		String method = req.getValue(request, "method");
		String id = req.getValue(request, "id");
		String build_num = req.getValue(request, "build_num");
		String sale_status = req.getValue(request, "sale_status");//销售状态 0：未出售 1：销售中 2:已售罄
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if("add".equals(method)){
			sql = "INSERT INTO t_wx_build_sale_status ( " +
				"	build_num, " +
				"	sale_status, " +
				"	record_id, " +
				"	record_date " +
				") " +
				"VALUES " +
				"	(?, ?, ?, SYSDATE()) ";
			batchSql.addBatch(sql, new Object[]{build_num, sale_status, operatorId});
		}else{
			sql = "INSERT INTO t_wx_build_sale_status_his ( " +
					"	id, " +
					"	build_num, " +
					"	sale_status, " +
					"	record_id, " +
					"	record_date, " +
					"	delete_id, " +
					"	delete_date " +
					") SELECT " +
					"	id, " +
					"	build_num, " +
					"	sale_status, " +
					"	record_id, " +
					"	record_date, " +
					"	?,  " +
					"  SYSDATE() " +
					"FROM " +
					"	t_wx_build_sale_status " +
					" where id = ? " ;
			
			batchSql.addBatch(sql, new Object[]{operatorId, id});
			sql = " UPDATE t_wx_build_sale_status " +
					"	 SET build_num = ?,  " +
					" 		 	 sale_status = ?,  " +
					"			 record_id = ?,  " +
					"			 record_date = SYSDATE() " +
					" where id = ? ";
			batchSql.addBatch(sql, new Object[]{build_num, sale_status, operatorId, id});
		}
		return db.doInTransaction(batchSql);
	}

	public int deleteInfo(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "INSERT INTO t_wx_build_sale_status_his ( " +
				"	id, " +
				"	build_num, " +
				"	sale_status, " +
				"	record_id, " +
				"	record_date, " +
				"	delete_id, " +
				"	delete_date " +
				") SELECT " +
				"	id, " +
				"	build_num, " +
				"	sale_status, " +
				"	record_id, " +
				"	record_date, " +
				"	?,  " +
				"  SYSDATE() " +
				"FROM " +
				"	t_wx_build_sale_status " +
				" where id = ? " ;
		batchSql.addBatch(sql, new Object[]{operatorId, id});
		sql = " delete from t_wx_build_sale_status where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		return db.doInTransaction(batchSql);
	}
	
}
