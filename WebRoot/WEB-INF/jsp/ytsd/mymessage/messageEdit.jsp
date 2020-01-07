<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改我的消息"></c:set>
		</c:when>
		<c:when test="${param.method=='add'}">
			<c:set var="title" value="新增我的消息"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="我的消息详细"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
	<script type="text/javascript" src="${app}/kindeditor/kindeditor-all.js"></script>
	<script type="text/javascript" src="${app}/kindeditor/lang/zh-CN.js"></script>
	
</head>
<body>
	<form id="form1" name="form1" action="${app}/myMessage/saveOrUpdateInfo.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<input type="hidden" name="id" value="${map.id }"/>
		    	<input type="hidden" name="method" value="${param.method }"/>
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;标题：
						</td>
						<td class="outDetail2">
							<input type="text" id="title" name="title" value="${map.title }" 
								dataType="Require" msg="请填写标题" size="50"/>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail" colspan="2" width="100%" height="100%">
							<textarea id="detail_info" name="detail_info" style="width: 100%;height: 100%">${map.detail_info }</textarea>
						</td>
					</tr>
				</table>
				
		    </div>
		</div>
		<br />
		<p align="center">
			<c:if test="${param.method!='show'}">
				<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">保 存</sgy:button>
			</c:if>&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
	</form>
</body>
<script type="text/javascript" defer="defer">
	var ke = KindEditor.create('#detail_info',{
		urlType:"domain", //带域名的绝对路径
		filePostName  : "file",
		//指定上传文件请求的url。
		uploadJson : '/myMessage/upload.do',
		height: '400px',
		//上传类型，分别为image、flash、media、file
		dir : "image"
	});
	
	$(document).ready(function(){
		//window.document.getElementById('addTypeInfo').style.display="none";
		$('#addInfo').hide();
	});

	//保存
	function sav(src) {
		disable(src);
		if(!Validator.Validate('form1')) {
			enable(src);
			return false;
		}
		ke.sync();
		$('#form1').submit();
	}
	
</script>
</html>
