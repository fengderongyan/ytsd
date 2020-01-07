package ytsd.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.common.DateHelper;
import com.sgy.util.common.FileHelper;
import com.sgy.util.db.BatchSql;

import net.coobird.thumbnailator.Thumbnails;
import web.service.BaseService;

@Service
public class DoorInfoService extends BaseService{
	
	/**
	 * 描述：获取楼栋号
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-06-25
	 */
	public List getBuildNumList(HttpServletRequest request){
		String sql = " select distinct build_num from t_wx_build_sale_status order by build_num ";
		return db.queryForList(sql);
	}
	
	/**
	 * 描述：获取小区数据
	 * @param request
	 * @return
	 * @see ytsd.service.DoorInfoService#getDoorInfoList()
	 * @author zhangyongbin
	 */
	public List getDoorInfoList(HttpServletRequest request){
		String build_num = req.getValue(request, "build_num");
		String unit_num = req.getValue(request, "unit_num");
		String door_num = req.getValue(request, "door_num");
		String status = req.getValue(request, "status");
		List paramList = new ArrayList();
		String sql = "SELECT " +
					"	id, " +
					"	build_num, " +
					"	unit_num, " +
					"	area, " +
					"	CONCAT(door_floor, '/', total_floor) door_floor, " +
					"	door_num, " +
					"	door_type, " +
					"	price, " +
					"	total_price, " +
					"	min_price, " +
					"	direction, " +
					"	CASE " +
					"WHEN `status` = 0 THEN " +
					"	'未售出' " +
					"WHEN `status` = 1 THEN " +
					"	'预定中' " +
					"WHEN `status` = 2 THEN " +
					"	'已售出' " +
					"END status_name, " +
					" status, " +
					" remark, " +
					" real_price, " +
					" is_wx_sale, " +
					" sale_openid, " +
					" sale_apply_id, " +
					" sale_oper_name, " +
					" sale_oper_mobile, " +
					" door_imgs, " +
					" fn_getusername(create_id) create_name, " +
					" DATE_FORMAT( " +
					"	create_date, " +
					"	'%Y-%m-%d %H:%i:%S' " +
					") create_date, " +
					" fn_getusername(update_id) update_name, " +
					" DATE_FORMAT( " +
					"	update_date, " +
					"	'%Y-%m-%d %H:%i:%S' " +
					") update_date " +
					"FROM " +
					"	t_wx_door_info a where 1 = 1 ";
		if(!"".equals(build_num)){
			sql += " and build_num = ? ";
			paramList.add(build_num);
		}
		if(!"".equals(unit_num)){
			sql += " and unit_num = ? ";
			paramList.add(unit_num);
		}
		if(!"".equals(door_num)){
			sql += " and door_num = ? ";
			paramList.add(door_num);
		}
		if(!"".equals(status)){
			sql += " and status = ? ";
			paramList.add(status);
		}
		sql += "	order by build_num, " +
				"	unit_num, " +
				"	a.door_floor, " +
				"	door_num";
		logger.debug(str.getSql(sql, paramList));
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
		
	}
	
	public Map getDoorInfoMap(HttpServletRequest request){
		String id = req.getValue(request, "id");
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
					"	b.sale_oper_mobile " +
					"FROM " +
					"	t_wx_door_info a left join t_wx_sale_print_info b on a.sale_print_info_id = b.id " +
					"WHERE " +
					"	a.id = ? ";
		return db.queryForMap(sql, new Object[]{id});
		
	}
	
	public int saveOrUpdateInfo(HttpServletRequest request){
		String method = req.getValue(request, "method");
		String id = req.getValue(request, "id");
		String build_num = req.getValue(request, "build_num");
		String unit_num = req.getValue(request, "unit_num");
		String door_floor = req.getValue(request, "door_floor");
		String total_floor = req.getValue(request, "total_floor");
		String door_num = req.getValue(request, "door_num");
		String door_type = req.getValue(request, "door_type");
		String area = req.getValue(request, "area");
		String price = req.getValue(request, "price");
		String total_price = req.getValue(request, "total_price");
		String direction = req.getValue(request, "direction");
		String min_price = req.getValue(request, "min_price");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if("add".equals(method)){
			sql = "INSERT INTO t_wx_door_info ( " +
				"	build_num, " +
				"	unit_num, " +
				"	door_floor, " +
				"	total_floor, " +
				"	door_num, " +
				"	door_type, " +
				"	area, " +
				"	price, " +
				"	total_price, " +
				"	direction, " +
				"	status, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date, " +
				"	min_price " +
				") " +
				"VALUES " +
				"	( " +
				"		?,?,?,?,?,?,?,?,?,?,0,?, SYSDATE() ,?, SYSDATE(), ? " +
				"	) ";
			batchSql.addBatch(sql, new Object[]{build_num, unit_num, door_floor, total_floor, door_num,
					door_type, area, price, total_price, direction, operatorId, operatorId, min_price});
		}else{
			sql = "INSERT INTO t_wx_door_info_his ( " +
				"	id, " +
				"	build_num, " +
				"	unit_num, " +
				"	area, " +
				"	door_floor, " +
				"	door_num, " +
				"	door_type, " +
				"	price, " +
				"	total_price, " +
				"	direction, " +
				"	STATUS, " +
				"	door_imgs, " +
				"	remark, " +
				"	real_price, " +
				"	is_wx_sale, " +
				"	sale_openid, " +
				"	sale_apply_id, " +
				"	sale_oper_name, " +
				"	sale_oper_mobile, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date, " +
				"	total_floor, " +
				"	del_id, " +
				"	del_date " +
				") select id, " +
				" build_num, " +
				" unit_num, " +
				" area, " +
				" door_floor, " +
				" door_num, " +
				" door_type, " +
				" price, " +
				" total_price, " +
				" direction, " +
				" STATUS, " +
				" door_imgs, " +
				" remark, " +
				" real_price, " +
				" is_wx_sale, " +
				" sale_openid, " +
				" sale_apply_id, " +
				" sale_oper_name, " +
				" sale_oper_mobile, " +
				" create_id, " +
				" create_date, " +
				" update_id, " +
				" update_date, " +
				" total_floor, " +
				" ?, " +
				" SYSDATE() " +
				"FROM " +
				"	t_wx_door_info " +
				"WHERE " +
				"	id = ? ";
			logger.debug(str.getSql(sql, new Object[]{operatorId, id}));
			batchSql.addBatch(sql, new Object[]{operatorId, id});
			sql = "UPDATE t_wx_door_info " +
				"SET 	build_num = ?, " +
				"		unit_num = ?, " +
				"		door_floor = ?, " +
				"		total_floor = ?, " +
				"		door_num = ?, " +
				"		door_type = ?, " +
				"		area = ?, " +
				"		price = ?, " +
				"		total_price = ?, " +
				"		direction = ?, " +
				"		update_id = ?, " +
				"		update_date = SYSDATE(), " +
				"		min_price = ? " +
				" where id = ? ";
			batchSql.addBatch(sql, new Object[]{build_num, unit_num, door_floor, total_floor, door_num,
					door_type, area, price, total_price, direction, operatorId, min_price, id});
		}
		
		return db.doInTransaction(batchSql);
	}
	
	public int deleteDoorInfo(HttpServletRequest request){
		BatchSql batchSql = new BatchSql();
		String operatorId = this.getOperatorId(request);
		String id = req.getValue(request, "id");
		String sql = "INSERT INTO t_wx_door_info_his ( " +
				"	id, " +
				"	build_num, " +
				"	unit_num, " +
				"	area, " +
				"	door_floor, " +
				"	door_num, " +
				"	door_type, " +
				"	price, " +
				"	total_price, " +
				"	direction, " +
				"	STATUS, " +
				"	door_imgs, " +
				"	remark, " +
				"	real_price, " +
				"	is_wx_sale, " +
				"	sale_openid, " +
				"	sale_apply_id, " +
				"	sale_oper_name, " +
				"	sale_oper_mobile, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date, " +
				"	total_floor, " +
				"	del_id, " +
				"	del_date " +
				") select id, " +
				" build_num, " +
				" unit_num, " +
				" area, " +
				" door_floor, " +
				" door_num, " +
				" door_type, " +
				" price, " +
				" total_price, " +
				" direction, " +
				" STATUS, " +
				" door_imgs, " +
				" remark, " +
				" real_price, " +
				" is_wx_sale, " +
				" sale_openid, " +
				" sale_apply_id, " +
				" sale_oper_name, " +
				" sale_oper_mobile, " +
				" create_id, " +
				" create_date, " +
				" update_id, " +
				" update_date, " +
				" total_floor, " +
				" ?, " +
				" SYSDATE() " +
				"FROM " +
				"	t_wx_door_info " +
				"WHERE " +
				"	id = ? ";
		batchSql.addBatch(sql, new Object[]{operatorId, id});
		sql = " delete from t_wx_door_info where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		return  db.doInTransaction(batchSql);
	}
	
	public String upload(HttpServletRequest request, CommonsMultipartFile comFile){
		String fileName =comFile.getOriginalFilename();
		String save_dir = request.getRealPath("/pic/");
		String to_file_name = FileHelper.getToFileName(fileName);
		String door_img = "/pic/" + to_file_name;//相对路径
		DiskFileItem diskFileItem = (DiskFileItem)comFile.getFileItem();
		File file = diskFileItem.getStoreLocation();
		try {
			Thumbnails.of(file).scale(1).toFile(save_dir + to_file_name);//绝对路径
			return door_img;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	public int saveDoorImgs(HttpServletRequest request, HttpServletResponse response){
		String id = req.getValue(request, "id");
		String door_imgs = req.getValue(request, "door_imgs");
		String door_imgs2 = door_imgs.substring(1);
		String sql = " update t_wx_door_info set door_imgs = case when (ISNULL(door_imgs) = 1 or door_imgs = '') then ? else concat(door_imgs, ?) end " + 
					 "   where id = ? ";
		logger.debug(str.getSql(sql, new Object[]{door_imgs2, door_imgs, id}));
		return db.update(sql, new Object[]{door_imgs2, door_imgs, id});
	}
	
	public String[] showImgs(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String sql = " select door_imgs from t_wx_door_info a where a.id = ? ";
		String door_imgs = db.queryForString(sql, new Object[]{id});
		String[] doorImgs = door_imgs.split(",");
		if("".equals(doorImgs[0])){
			return null;
		}
		return doorImgs;
	}
	
	public int delImg(HttpServletRequest request){
		String door_img = req.getValue(request, "door_img");
		String id = req.getValue(request, "id");
		String sql = " select door_imgs from t_wx_door_info where id = ? ";
		String door_imgs = db.queryForString(sql, new Object[]{id});
		String[] doorImgs = door_imgs.split(",");
		String temImgs = "";
		int index = 0;
		for(int i = 0;i <doorImgs.length; i++){
			if(!doorImgs[i].equals(door_img)){
				if(index == 0){
					temImgs = doorImgs[i];
				}else{
					temImgs += "," + doorImgs[i];
				}
				index++;
			}
		}
		sql = " update t_wx_door_info set door_imgs = ? where id = ? ";
		return db.update(sql, new Object[]{temImgs, id});
	}
	
	/**
	 * 描述：获取销售人员列表
	 * @param request
	 * @return
	 * @see ytsd.service.DoorInfoService#getSaleList()
	 * @author zhangyongbin
	 */
	public List getSaleList(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String sql = "SELECT " +
					"	id apply_id, " +
					"	openid, " +
					"	door_info_id, " +
					"	fn_getNameByOpenid (openid) sale_name, " +
					"	apply_price, " +
					"	DATE_FORMAT( " +
					"		apply_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) apply_date, " +
					"	customer_name, " +
					"	customer_mobile " +
					"FROM " +
					"	t_wx_apply_info a " +
					"WHERE " +
					"	door_info_id = ? " +
					"AND apply_status = 3 "
					+ " and status = 1 ";
		return db.queryForList(sql, new Object[]{id});
		
	}
	
	public int saveSaleInfo(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String is_wx_sale = req.getValue(request, "is_wx_sale");
		String apply_id = req.getValue(request, "apply_id");
		String real_price = req.getValue(request, "real_price"); 
		String wx_real_price = req.getValue(request, "wx_real_price"); 
		
		String customer_name = req.getValue(request, "customer_name");
		String id_card = req.getValue(request, "id_card");
		String customer_mobile = req.getValue(request, "customer_mobile");
		String tx_address = req.getValue(request, "tx_address");
		String rgfy = req.getValue(request, "rgfy");
		String area = req.getValue(request, "area");
		String price = req.getValue(request, "price");
		String total_price = req.getValue(request, "total_price");
		String order_price = req.getValue(request, "order_price");
		String order_pay_type = req.getValue(request, "order_pay_type");
		String first_pay = req.getValue(request, "first_pay");
		String pay_date = req.getValue(request, "pay_date");
		String surplus_pay = req.getValue(request, "surplus_pay");
		String surplus_pay_type = req.getValue(request, "surplus_pay_type");
		String loan_money = req.getValue(request, "loan_money");
		String loan_deal_date = req.getValue(request, "loan_deal_date");
		String sale_oper_name = req.getValue(request, "sale_oper_name");
		String sale_oper_mobile = req.getValue(request, "sale_oper_mobile");
		String sale_group_id = req.getValue(request, "sale_group_id");
		List paramList =  new ArrayList();
		String sql = "";
		String sale_print_id = UUID.randomUUID().toString().trim().replaceAll("-", "");
		String print_num = "";
		String today = DateHelper.getToday("yyyyMMdd");
		sql = " select ifnull(max(print_num), 0) from t_wx_sale_print_info where SUBSTRING(print_num, 1, 8) = ? ";
		String max_print_num = db.queryForString(sql, new Object[]{today});
		if("0".equals(max_print_num)){
			print_num = today + "0001";
		}else{
			Long print_num_long =  Long.valueOf(max_print_num) + 1;
			print_num = String.valueOf(print_num_long);
		}
		sql = "insert into t_wx_sale_print_info " +
				"(id,sale_group_id, door_info_id, print_num, customer_name, id_card, customer_mobile, tx_address, rgfy, area, price, total_price, " +
				"order_price, order_pay_type, first_pay, pay_date, surplus_pay, surplus_pay_type, loan_money, loan_deal_date, sale_oper_name, sale_oper_mobile, record_date) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?, " +
				"		   ?,?,?,case when ? = '' then null else STR_TO_DATE(?, '%Y-%m-%d %H:%i:%s') end,?,?,?,case when ? = '' then null else STR_TO_DATE(?, '%Y-%m-%d %H:%i:%s') end, ?, ?, SYSDATE()) ";
		db.update(sql, new Object[]{sale_print_id,sale_group_id,id,print_num, customer_name, id_card, customer_mobile, tx_address, rgfy, area, price, total_price,
				order_price, order_pay_type, first_pay, pay_date, pay_date, surplus_pay, surplus_pay_type, loan_money, loan_deal_date, loan_deal_date, sale_oper_name, sale_oper_mobile});
		if("1".equals(is_wx_sale)){//线上销售
			sql = " select openid, name, mobile from t_wx_user_info a " +
					 "  where a.openid = (select openid from t_wx_apply_info b where b.id = ?) ";
			Map map = db.queryForMap(sql, new Object[]{apply_id});
			String openid = str.get(map, "openid");
			String name = str.get(map, "name");
			String mobile = str.get(map, "mobile");
			if("".equals(sale_oper_name)){
				sale_oper_name = name;
			}
			if("".equals(mobile)){
				sale_oper_mobile = mobile;
			}
			sql = " update t_wx_door_info " +
				  "    set status = 2, " +
				  "    	   is_wx_sale = ?, " +
				  "	 	   sale_date = SYSDATE(), " +
				  "    	   sale_oper_name = ?, " +
				  "    	   sale_oper_mobile = ?, " +
				  "   	   sale_openid = ?, " +
				  "  	   sale_apply_id = ?, " +
				  "	   	   real_price = ?, " +
				  "	       sale_print_info_id = ? " +
				  "  where id = ? ";
			return db.update(sql, new Object[]{is_wx_sale, sale_oper_name, sale_oper_mobile, openid, apply_id, wx_real_price, sale_print_id, id});
		}else{//线下销售
			sql = " update t_wx_door_info " +
				  "    set status = 2, " +
				  "    	   is_wx_sale = ?, " +
				  "	 	   sale_date = SYSDATE(), " +
				  "    	   sale_oper_name = ?, " +
				  "    	   sale_oper_mobile = ?, " +
				 "    	   sale_print_info_id = ? ";
			paramList.add(is_wx_sale);
			paramList.add(sale_oper_name);
			paramList.add(sale_oper_mobile);
			paramList.add(sale_print_id);
			if(!"".equals(price)){
				sql += " ,real_price = ? ";
				paramList.add(price);
			}else{
				sql += " ,real_price = price ";
			}
			sql += "  where id = ? ";
			paramList.add(id);
			return db.update(sql, paramList.toArray());
		}
		
	}

	public String getSaleStatus(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String sql = " select status from t_wx_door_info where id = ? ";
		return db.queryForString(sql, new Object[]{id});
	}

	public Map saleInfoAjax(HttpServletRequest request) {
		String apply_id = req.getValue(request, "apply_id");
		String sql = "SELECT " +
				"	a.customer_name, " +
				"	a.customer_mobile, " +
				"	a.apply_price, " +
				"	fn_getNameByOpenid(a.openid) sale_oper_name, " +
				"	a.group_id, " +
				"	b.area " +
				"FROM " +
				"	t_wx_apply_info a, " +
				"	t_wx_door_info b " +
				"WHERE " +
				"	a.door_info_id = b.id " +
				"AND a.id = ? ";
		return db.queryForMap(sql, new Object[]{apply_id});
	}

	public int tuiFang(HttpServletRequest request) {
		String id = req.getValue(request, "id");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		
		String sql = "INSERT INTO t_wx_door_info_his ( " +
				"	id, " +
				"	build_num, " +
				"	unit_num, " +
				"	area, " +
				"	door_floor, " +
				"	door_num, " +
				"	door_type, " +
				"	price, " +
				"	total_price, " +
				"	direction, " +
				"	STATUS, " +
				"	door_imgs, " +
				"	remark, " +
				"	real_price, " +
				"	is_wx_sale, " +
				"	sale_openid, " +
				"	sale_apply_id, " +
				"	sale_oper_name, " +
				"	sale_oper_mobile, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date, " +
				"	total_floor, " +
				"	del_id, " +
				"	del_date " +
				") select id, " +
				" build_num, " +
				" unit_num, " +
				" area, " +
				" door_floor, " +
				" door_num, " +
				" door_type, " +
				" price, " +
				" total_price, " +
				" direction, " +
				" STATUS, " +
				" door_imgs, " +
				" remark, " +
				" real_price, " +
				" is_wx_sale, " +
				" sale_openid, " +
				" sale_apply_id, " +
				" sale_oper_name, " +
				" sale_oper_mobile, " +
				" create_id, " +
				" create_date, " +
				" update_id, " +
				" update_date, " +
				" total_floor, " +
				" ?, " +
				" SYSDATE() " +
				"FROM " +
				"	t_wx_door_info " +
				"WHERE " +
				"	id = ? ";
		batchSql.addBatch(sql, new Object[]{operatorId, id});
		sql = " update t_wx_door_info "
				+ " set status = 0, "
				+ "     real_price = null, "
				+ "		is_wx_sale = null, "
				+ "		sale_openid = null, "
				+ "		sale_apply_id = null, "
				+ "		sale_oper_name = null, "
				+ "		sale_oper_mobile = null, "
				+ "		sale_date = null, "
				+ "		sale_print_info_id = null, "
				+ "		status_update_time = null "
				+ " where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		sql = " update t_wx_apply_info set status = 0 where door_info_id = ? and status = 1 ";
		batchSql.addBatch(sql, new Object[]{id});
		sql = " update t_wx_sale_print_info set status = 0 where door_info_id = ? and status = 1 ";
		batchSql.addBatch(sql, new Object[]{id});
		return db.doInTransaction(batchSql);
	}
}
