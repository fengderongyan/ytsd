<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
</head>
	<body>
		<sgy:csrfToken />
		<display:table name="list" requestURI="${app}/yjbbCount/xjDetailList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="true" 
			sort="external" partialList="true" size="size">
			<display:column title="序号" style="width: 50px;">
				<c:out value="${row_rowNum+beginIndex}" />
			</display:column>
			<display:column title="归属组织" property="ORG_NAME"/>
			<display:column title="姓名" property="NAME" style="width: 120px;"/>
			<display:column title="请假类别" property="concrete_type"/>
			<display:column title="总天数" property="bb_days" style="width: 120px;"/>
			<display:column title="总次数" property="totalTimes" style="width: 140px;"/>
			<display:column title="非休假/病假/事假次数" property="countOtherTimes"/>
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
	</script>
</html>