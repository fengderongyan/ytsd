package web.interceptor;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import weixin.entity.WxUserInfo;
import weixin.service.WxLoginService;
import ytsd.service.login.LoginService;

import com.sgy.util.Constants;
import com.sgy.util.common.StringHelper;
import com.sgy.util.spring.SpringUtil;

/**
 * URL拦截，判断当前用户是否认证，如果没有认证则跳转到登录页面
 * @author zhang
 * @createDate 2019-01-22
 * @description
 */
public class MobileURLInterceptor extends URLInterceptor {
	//日志记录
	protected Logger logger = Logger.getLogger(getClass());
	private static Map<String,String> operIdMd5Map = new HashMap<String, String>();
	//不需要拦截的URL
	private String mappingURL = "";   
	//DES加密解密key
	private String key = Constants.APP_HTTP_KEY;
	
    public void setMappingURL(String mappingURL) {    
    	this.mappingURL = mappingURL;    
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURL().toString();
		WxLoginService wxLoginService = (WxLoginService) SpringUtil.getSpringBean("wxLoginService");
		/*String token = request.getHeader("token");
		logger.debug("token : " + token);
		String operId = loginService.getOperIdByToken(token);
		this.initUser(operId, request);*/
		//logger.debug("url is ：" + url);
		if (!"".equals(mappingURL) && StringHelper.isContains(url, mappingURL.split(","))) {
			return true;
		}
		//微信小程序
		if (url.indexOf("/mobile/applet/") > -1) {
			String openid = request.getHeader("openid");
			logger.debug("openId:" + openid);
			System.out.println("".equals(openid));
			if("[object Undefined]".equals(openid) || "".equals(openid)){
				Map errorMap = new HashMap();
				errorMap.put("errorCode", 11004);
				errorMap.put("errorInfo", "openid验证失败");
				response.setCharacterEncoding("UTF-8");
				System.out.println("openid为空");
				response.getWriter().print(JSONObject.fromObject(errorMap));
				return false;
			}
			//根据openId查询用户
			WxUserInfo wxUserInfo =wxLoginService.getWxUserInfo(openid);
			//用户存在
			if(wxUserInfo != null){
				this.initWxUser(wxUserInfo, request);
				return true;
			}else{//用户不存在
				Map errorMap = new HashMap();
				errorMap.put("errorCode", 11006);
				errorMap.put("errorInfo", "未绑定手机信息");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(JSONObject.fromObject(errorMap));
				return false;
			}
		}
		return true;
	}
	
	/** 
	 * 系统初始化用户信息
	 * @param operator_id
	 * @param request
	 */ 
	public void initWxUser(WxUserInfo wxUserInfo, HttpServletRequest request)  {
		Subject currentUser = SecurityUtils.getSubject();
		logger.debug("token验证");
		// 设置用户信息 user
		Session session = currentUser.getSession();
		session.setAttribute("wxUserInfo", wxUserInfo);
		
	}

	public String getOperIdByMd5(String operator_md5){
		LoginService loginService = (LoginService) SpringUtil.getSpringBean("loginService");
		return loginService.getOperIdByMd5(operator_md5);
	}
}