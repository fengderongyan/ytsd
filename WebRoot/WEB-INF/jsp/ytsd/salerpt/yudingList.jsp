<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/salerpt/getYudingList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="true" sort="external"  partialList="true" size="size">
			
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="房号" property="door_info" />
			<display:column title="面积" property="area" />
			<display:column title="市场价" property="price" />
			<display:column title="申请价" property="apply_price" />
			<display:column title="申请人" property="apply_name" />
			<display:column title="申请时间" property="apply_date" />
			<display:column title="客户姓名" property="customer_name" />
			<display:column title="客户电话" property="customer_mobile" />
			<display:column title="申请状态" property="sale_status_name" />
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
	</script>
</html>