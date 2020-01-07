package ytsd.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.jfree.util.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.common.FileHelper;
import com.sgy.util.db.BatchSql;

import net.coobird.thumbnailator.Thumbnails;
import web.service.BaseService;

@Service
public class MyMessageService extends BaseService{
	
	/**
	 * 描述：获取我的消息
	 * @param request
	 * @return
	 * @see ytsd.service.MyMessageService#getDoorInfoList()
	 * @author zhangyongbin
	 */
	public List getMessageList(HttpServletRequest request){
		String title = req.getValue(request, "title");
		List paramList = new ArrayList();
		String sql = " select id, title, status, case when IFNULL(status,0) = 0 then '未推送' else '已推送' end status_name, fn_getusername(update_id) update_name,  "
				+ "     DATE_FORMAT(a.update_date,'%Y-%m-%d %H:%i:%S') update_date from t_wx_my_message_ts a where 1 = 1 ";
		if(!"".equals(title)){
			sql += " and title like ? ";
			paramList.add("%" + title + "%");
		}
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
		
	}
	
	/**
	 * 描述：获取我的消息编辑页面
	 * @param request
	 * @return
	 * @see ytsd.service.MyMessageService#getMessageMap()
	 * @author zhangyongbin
	 */
	public Map getMessageMap(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String sql = " select id, title, detail_info  from t_wx_my_message_ts a where id = ? ";
		return db.queryForMap(sql, new Object[]{id});
	}
	
	/**
	 * 描述：保存
	 * @param request
	 * @return
	 * @see ytsd.service.MyMessageService#saveOrUpdateInfo()
	 * @author zhangyongbin
	 */
	public int saveOrUpdateInfo(HttpServletRequest request){
		String method = req.getValue(request, "method");
		String id = req.getValue(request, "id");
		String title = req.getValue(request, "title");
		String detail_info = req.getValue(request, "detail_info");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if("add".equals(method)){
			sql = "INSERT INTO t_wx_my_message_ts ( " +
				"	title, " +
				"	detail_info, " +
				"	status, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date " +
				") " +
				"VALUES " +
				"	( " +
				"		?,?, 0, ?, SYSDATE() ,?, SYSDATE() " +
				"	) ";
			logger.debug(str.getSql(sql, new Object[]{title, detail_info, operatorId, operatorId}));
			batchSql.addBatch(sql, new Object[]{title, detail_info, operatorId, operatorId});
		}else{
			sql = "INSERT INTO t_wx_my_message_ts_his ( " +
					"	id, " +
					"	title, " +
					"	detail_info, " +
					"	status, " +
					"	create_id, " +
					"	create_date, " +
					"	update_id, " +
					"	update_date, "
					+ " del_id, "
					+ " del_date " +
				") select id, " +
				"	title, " +
				"	detail_info, " +
				"	status, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date, "
				+ " ?, "
				+ " SYSDATE() " +
				"FROM " +
				"	t_wx_my_message_ts " +
				"WHERE " +
				"	id = ? ";
			logger.debug(str.getSql(sql, new Object[]{operatorId, id}));
			batchSql.addBatch(sql, new Object[]{operatorId, id});
			sql = "UPDATE t_wx_my_message_ts " +
				"SET 	title = ?, " +
				"		detail_info = ?, " +
				"		update_id = ?, " +
				"		update_date = SYSDATE() " +
				" where id = ? ";
			batchSql.addBatch(sql, new Object[]{title, detail_info, operatorId, id});
		}
		return db.doInTransaction(batchSql);
	}
	
	public int deleteMessage(HttpServletRequest request){
		BatchSql batchSql = new BatchSql();
		String operatorId = this.getOperatorId(request);
		String id = req.getValue(request, "id");
		String sql = "INSERT INTO t_wx_my_message_ts_his ( " +
				"	title, " +
				"	detail_info, " +
				"	status, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date, "
				+ " del_id, "
				+ " del_date " +
			") select id, " +
			"	title, " +
			"	detail_info, " +
			"	status, " +
			"	create_id, " +
			"	create_date, " +
			"	update_id, " +
			"	update_date, "
			+ " ?, "
			+ " SYSDATE() " +
			"FROM " +
			"	t_wx_my_message_ts " +
			"WHERE " +
			"	id = ? ";
		batchSql.addBatch(sql, new Object[]{operatorId, id});
		sql = " delete from t_wx_my_message_ts where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		return  db.doInTransaction(batchSql);
	}

	public String upload(HttpServletRequest request, CommonsMultipartFile comFile) {
		String fileName =comFile.getOriginalFilename();
		String save_dir = request.getRealPath("/pic/");
		String to_file_name = FileHelper.getToFileName(fileName);
		//String door_img = "/ytsd/pic/" + to_file_name;//相对路径
		String door_img = "https://sales.altmoose.com/pic/" + to_file_name;//相对路径--线上
		DiskFileItem diskFileItem = (DiskFileItem)comFile.getFileItem();
		File file = diskFileItem.getStoreLocation();
		try {
			Thumbnails.of(file).size(750, 320).keepAspectRatio(true).toFile(save_dir + to_file_name);//绝对路径
			return door_img;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public int tsInfo(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String operatorId = this.getUser(request).getOperatorId();
		String userSql = " select openid from t_wx_user_info where status = 1 and IFNULL(LENGTH(openid), 0) > 0 ";
		List userList = db.queryForList(userSql);
		BatchSql batchSql = new BatchSql();
		String sql = " update t_wx_my_message_ts set status = 1 where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		if(userList != null && userList.size() > 0){
			for (int i = 0; i < userList.size(); i++) {
				Map tmpMap = (Map)userList.get(i);
				String openid = str.get(tmpMap, "openid");
				sql = " insert into t_wx_my_message(read_status, title, detail_info, openid, create_id, create_date) "
						+ " select 0, title, detail_info, ?, ?, SYSDATE() from t_wx_my_message_ts where id = ? ";
				Log.debug(str.getSql(sql, new Object[]{openid, operatorId , id}));
				batchSql.addBatch(sql, new Object[]{openid, operatorId, id});
			}
			
		}
		return db.doInTransaction(batchSql);
	}
	
}
