<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改微信用户信息"></c:set>
		</c:when>
		<c:when test="${param.method=='add'}">
			<c:set var="title" value="新增微信用户信息"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="微信用户信息详细"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/wxuserinfo/saveOrUpdateInfo.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<input type="hidden" name="id" id="id" value="${map.id }"/>
		    	<input type="hidden" name="method" value="${param.method }"/>
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;姓名：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="name" name="name" value="${map.name }" 
								dataType="Require" msg="请填写姓名！"/>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;手机号码：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="mobile" name="mobile" value="${map.mobile }"
								dataType="Number" msg="请填写手机号码，且为数字！"/>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;职位：
						</td>
						<td class="outDetail2" colspan="3">
							<sgy:select name="role_id" value="${map.role_id }" dataType="Require" msg="请选择职位！">
								<sgy:option value="">请选择...</sgy:option>
								<sgy:option value="1">董事长</sgy:option>
								<sgy:option value="4">销售总监</sgy:option>
								<sgy:option value="2">销售经理</sgy:option>
								<sgy:option value="3">销售人员</sgy:option>
							</sgy:select>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							销售小组：
						</td>
						<td class="outDetail2" colspan="3">
							<sgy:select list="saleGroupList" name="group_id" id="group_id" value="${map.group_id }"
							        headLabel="请选择..." headValue="0" optionLabel="group_name" optionValue="group_id" />
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

		//检查号码是否存在
		var mobile = $("#mobile").val();
		var id = $("#id").val();
		var url = '${app}/wxuserinfo/checkMobile.do';
		var params="id=" + id + "&mobile=" + mobile;
		var res = new MyJqueryAjax(url,params).request();
		if(res == 0) {
			$('#form1').submit();
	  	} else {
	  		enable(src);
			alert('手机号码已存在！');
	  	}
	}
	
</script>
</html>
