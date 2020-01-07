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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.common.FileHelper;
import com.sgy.util.db.BatchSql;

import net.coobird.thumbnailator.Thumbnails;
import web.service.BaseService;

@Service
public class BannerService extends BaseService{
	
	/**
	 * 描述：获取我的消息
	 * @param request
	 * @return
	 * @see ytsd.service.BannerService#getDoorInfoList()
	 * @author zhangyongbin
	 */
	public List getBannerList(HttpServletRequest request){
		String title = req.getValue(request, "title");
		List paramList = new ArrayList();
		String sql = " select id, title,  img_url, fn_getusername(update_id) update_name,  "
				+ "     DATE_FORMAT(a.update_date,'%Y-%m-%d %H:%i:%S') update_date from t_wx_banner a where 1 = 1 ";
		if(!"".equals(title)){
			sql += " and title like ? ";
			paramList.add("%" + title + "%");
		}
		sql += " order by status desc, update_date desc ";
		return db.getForList(sql, paramList, req.getPageSize(request, "pageSize"), request);
		
	}
	
	/**
	 * 描述：获取我的消息编辑页面
	 * @param request
	 * @return
	 * @see ytsd.service.BannerService#getMessageMap()
	 * @author zhangyongbin
	 */
	public Map getBannerMap(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String sql = " select id, title, img_url  from t_wx_banner a where id = ? ";
		return db.queryForMap(sql, new Object[]{id});
	}
	
	/**
	 * 描述：保存
	 * @param request
	 * @return
	 * @see ytsd.service.BannerService#saveOrUpdateInfo()
	 * @author zhangyongbin
	 */
	public int saveOrUpdateInfo(HttpServletRequest request, CommonsMultipartFile comFile){
		String method = req.getValue(request, "method");
		String id = req.getValue(request, "id");
		String title = req.getValue(request, "title");
		String img_url = req.getValue(request, "img_url");
		String operatorId = this.getOperatorId(request);
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if(comFile != null){
			String fileName =comFile.getOriginalFilename();
			String save_dir = request.getRealPath("/pic/");
			String to_file_name = FileHelper.getToFileName(fileName);
			//String door_img = "/ytsd/pic/" + to_file_name;//相对路径
			String door_img = "https://sales.altmoose.com/pic/" + to_file_name;//相对路径--线上
			DiskFileItem diskFileItem = (DiskFileItem)comFile.getFileItem();
			File file = diskFileItem.getStoreLocation();
			try {
				Thumbnails.of(file).size(750, 320).keepAspectRatio(true).toFile(save_dir + to_file_name);//绝对路径
				img_url = door_img;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if("add".equals(method)){
			sql = "INSERT INTO t_wx_banner ( " +
				"	title, " +
				"	status, " +
				"	img_url, " +
				"	create_id, " +
				"	create_date, " +
				"	update_id, " +
				"	update_date " +
				") " +
				"VALUES " +
				"	( " +
				"		?, 1, ?, ?, SYSDATE() ,?, SYSDATE() " +
				"	) ";
			logger.debug(str.getSql(sql, new Object[]{title, img_url, operatorId, operatorId}));
			batchSql.addBatch(sql, new Object[]{title, img_url, operatorId, operatorId});
		}else{
			sql = "UPDATE t_wx_banner " +
					" SET 	title = ?, " +
					"		img_url = ?, " +
					"		update_id = ?, " +
					"		update_date = SYSDATE() " +
					" where id = ? ";
				batchSql.addBatch(sql, new Object[]{title, img_url, operatorId, id});
		}
		return db.doInTransaction(batchSql);
	}
	
	public int deleteMessage(HttpServletRequest request){
		BatchSql batchSql = new BatchSql();
		String id = req.getValue(request, "id");
		String sql = " delete from t_wx_banner where id = ? ";
		batchSql.addBatch(sql, new Object[]{id});
		return  db.doInTransaction(batchSql);
	}
	
}
