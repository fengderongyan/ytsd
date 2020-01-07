<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/salerpt/getSaledList.do"
			class="list" id="row" cellspacing="0" style="width:150%"
			cellpadding="0" pagesize="${param.pageSize}" export="true" sort="external"  partialList="true" size="size">
			
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="客户姓名" property="customer_name" />
			<display:column title="身份证号码" property="id_card" />
			<display:column title="联系方式" property="customer_mobile" />
			<display:column title="通讯地址" property="tx_address" />
			<display:column title="认购房源" property="rgfy" />
			<display:column title="面积" property="sale_area" />
			<display:column title="单价" property="sale_price" />
			<display:column title="总价" property="sale_total_price" />
			<display:column title="定金" property="order_price" />
			<display:column title="付款方式" property="order_pay_type" />
			<display:column title="首付款" property="first_pay" />
			<display:column title="交付时间" property="pay_date" />
			<display:column title="剩余房款" property="surplus_pay" />
			<display:column title="付款方式" property="surplus_pay_type" />
			<display:column title="贷款金额" property="loan_money" />
			<display:column title="办理时间" property="loan_deal_date" />
			<display:column title="置业顾问" property="sale_oper_name" />
			<display:column title="销售时间" property="record_date" />
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
	</script>
</html>