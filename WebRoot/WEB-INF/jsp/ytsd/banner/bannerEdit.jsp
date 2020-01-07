<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改图片"></c:set>
		</c:when>
		<c:when test="${param.method=='add'}">
			<c:set var="title" value="新增图片"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/banner/saveOrUpdateInfo.do" method="post" ENCTYPE="multipart/form-data">
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
					<tr height="60">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;图片：
						</td>
						<td class="outDetail2">
							<c:if test="${param.method != 'add' }">
								<img src="${map.img_url }" alt="${map.title }" style="height: 60px;width: 60px"/><br/>
							</c:if>
							<input type="file" name="file" accept="image/*"/>
							<input type="hidden" name="img_url" value="${map.img_url }"/>
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
