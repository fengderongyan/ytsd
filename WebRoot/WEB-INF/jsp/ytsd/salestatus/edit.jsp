<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改楼栋"></c:set>
		</c:when>
		<c:when test="${param.method=='add'}">
			<c:set var="title" value="新增楼栋"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/salestatus/saveOrUpdateInfo.do" method="post" ENCTYPE="multipart/form-data">
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
							<font color="red">*</font>&nbsp;楼栋号：
						</td>
						<td class="outDetail2">
							<input type="text" id="build_num" name="build_num" value="${map.build_num }" 
								dataType="Number" msg="请填写楼栋号且为数字"/>
						</td>
					</tr>
					<tr height="60">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;销售状态：
						</td>
						<td class="outDetail2">
							<sgy:select name="sale_status" dataType="Require" msg="请选择销售状态" value="${map.sale_status }">
								<sgy:option value="">请选择...</sgy:option>
								<sgy:option value="0">未出售</sgy:option>
								<sgy:option value="1">销售中</sgy:option>
								<sgy:option value="2">已售罄</sgy:option>
							</sgy:select>
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
		$('#form1').submit();
	}
	
</script>
</html>
