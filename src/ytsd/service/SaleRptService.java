package ytsd.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

@Service
public class SaleRptService extends BaseService{
	
	public List getSaleGroupList(HttpServletRequest request){
		String sql = " select id group_id, group_name from t_wx_salegroup_info order by record_date desc ";
		return db.queryForList(sql);
	}

	public List getSaledList(HttpServletRequest request) {
		String group_id = req.getValue(request, "group_id");
		String begin_date = req.getValue(request, "begin_date");
		String end_date = req.getValue(request, "end_date");
		List paramList = new ArrayList();
		String sql = "SELECT " +
				"	a.id, " +
				"	a.build_num, " +
				"	a.unit_num, " +
				"	a.area, " +
				"	a.door_floor, " +
				"	a.door_num, " +
				"	a.door_type, " +
				"	a.price, " +
				"	a.total_price, " +
				"	a.direction, " +
				"	a.STATUS, " +
				"	a.min_price, " +
				"	CASE " +
				"WHEN a.status = 0 THEN " +
				"	'未售出' " +
				"WHEN a.status = 1 THEN " +
				"	'预定中' " +
				"WHEN a.status = 2 THEN " +
				"	'已售出' " +
				"END status_name, " +
				"	a.door_imgs, " +
				"	a.remark, " +
				"	a.real_price, " +
				"	case when a.is_wx_sale = 1 then '线上售出' when a.is_wx_sale = 2 then '线下售出' end is_wx_sale, " +
				"	a.sale_openid, " +
				"	a.sale_apply_id, " +
				"	a.create_id, " +
				"	a.create_date, " +
				"	a.update_id, " +
				"	a.update_date, " +
				" DATE_FORMAT( " +
				"	a.sale_date, " +
				"	'%Y-%m-%d %H:%i:%S' " +
				") sale_date, " +
				"	a.total_floor, " +
				"	b.print_num, " +
				"	b.customer_name, " +
				"	b.customer_mobile, " +
				"	b.id_card, " +
				"	b.tx_address, " +
				"	b.rgfy, " +
				"	b.area sale_area, " +
				"	b.price sale_price, " +
				"	b.total_price sale_total_price, " +
				"	b.order_price, " +
				"	b.order_pay_type, " +
				"	b.first_pay, " +
				"	DATE_FORMAT(b.pay_date, '%Y-%m-%d') pay_date, " +
				"	b.surplus_pay, " +
				"	b.surplus_pay_type, " +
				"	b.loan_money, " +
				"	DATE_FORMAT(b.loan_deal_date, '%Y-%m-%d') loan_deal_date , " +
				"	b.sale_oper_name, " +
				"	b.sale_oper_mobile, " +
				"	DATE_FORMAT(b.record_date, '%Y-%m-%d %H:%i:%S') record_date " +
				"FROM " +
				"	t_wx_door_info a, t_wx_sale_print_info b " +
				"WHERE  a.sale_print_info_id = b.id " +
				"	and b.status = 1 ";
		if(!"".equals(group_id)){
			sql += " and b.sale_group_id = ? ";
			paramList.add(group_id);
		}
		if(!"".equals(begin_date)){
			sql += " and DATE_FORMAT(b.record_date, '%Y-%m-%d') >= ? ";
			paramList.add(begin_date);
		}
		if(!"".equals(end_date)){
			sql += " and DATE_FORMAT(b.record_date, '%Y-%m-%d') <= ? ";
			paramList.add(end_date);
		}
		sql += " order by b.record_date desc ";
		
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
	}

	public List getYudingList(HttpServletRequest request) {
		String group_id = req.getValue(request, "group_id");
		String begin_date = req.getValue(request, "begin_date");
		String end_date = req.getValue(request, "end_date");
		List paramList = new ArrayList();
		String sql = " select " +
				"   concat(b.build_num, '-', b.unit_num, '-', b.door_num) door_info, " +
				"   b.area, " +
				"   b.price, " +
				"   a.apply_price, " +
				"   fn_getNameByOpenid(a.openid) apply_name, " +
				"   DATE_FORMAT(a.apply_date, '%Y-%m-%d %H:%i:%S') apply_date, " +
				"   a.customer_name, " +
				"   a.customer_mobile, " +
				"   case when a.apply_status = 1 then '销售经理审核中' when a.apply_status = 4 then '销售总监审核中' "
				+ " when a.apply_status = 2 then '董事长审核中' when a.apply_status = 3 then '已同意' end sale_status_name " +
				"FROM " +
				"	t_wx_apply_info a, " +
				"	t_wx_door_info b " +
				"WHERE " +
				"	a.door_info_id = b.id " +
				"AND a.status = 1 " +
				"AND b.status = 1 ";
		if(!"".equals(group_id)){
			sql += " and a.group_id = ? ";
			paramList.add(group_id);
		}
		if(!"".equals(begin_date)){
			sql += " and DATE_FORMAT(a.apply_date, '%Y-%m-%d') >= ? ";
			paramList.add(begin_date);
		}
		if(!"".equals(end_date)){
			sql += " and DATE_FORMAT(a.apply_date, '%Y-%m-%d') <= ? ";
			paramList.add(end_date);
		}
		sql += " order by a.apply_date desc " ;
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
	}

	
}
