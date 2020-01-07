<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>蓄客信息查询</title>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="${app}/xkweb/list.do" target="listQuery">
			<div id="queryPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;查询条件
				</div>
				<div id="queryPanel_content" class="queryPanel_content"
					style="position: relative;">
					<table class="search" cellspacing="0" cellpadding="0">
						<tr>
							<td class="outDetail" style="width: 70px;">
								客户姓名：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="customer_name" name="customer_name"/>
							</td>
							<td class="outDetail" style="width: 70px;">
								客户电话：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="customer_mobile" name="customer_mobile"/>
							</td>
							<td class="outDetail" style="width: 70px;">
								置业顾问：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="sale_name" name="sale_name"/>
							</td>
							<td class="outDetail" style="width: 70px;">
								起始时间：
							</td>
							<td class="outDetail2">
								<input type="text" id="begin_date" name="begin_date" size="11"
									onclick="WdatePicker();" readonly="readonly"/>-
								<input type="text" id="end_date" name="end_date" size="11"
									onclick="WdatePicker();" readonly="readonly"/>
							</td>
						</tr>
					</table>
					<div id="queryPanel_footer" class="queryPanel_footer"
						style="position: absolute; bottom: 1px; right: 10px;">
						<input type="hidden" id="pageSize" name="pageSize" value="" />
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="sch();return false;">查 询</sgy:button>
						<sgy:button id="expbtn" cssClass="ovalbutton" onclick="exp();return false;">导出报表</sgy:button>
					</div>
				</div>
			</div>
		</form>
		<div id="listPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>
				&nbsp;蓄客信息列表
			</div>
			<div id="queryPanel_content" class="queryPanel_content">
				<iframe id="listQuery" allowtransparency="true" name="listQuery"
					frameborder="0" width="100%" scrolling="auto"></iframe>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	 	//先调用设置行数的函数，再调用默认查询函数
		setPageSize('listQuery');
		sch();
  		function sch()
  		{
  			disable(document.getElementById("schbtn"));
  			form1.action = "${app}/xkweb/list.do";
  			$('#listQuery').attr('src','${app}/inc/baseLoading.jsp');
			setTimeout('form1.submit();',500);
  		}
  		function exp(){
  			form1.action = "https://ytsdxk.altmoose.com/mobile/xkinfo/expRpt.do";
  			form1.submit();
  		}
	</script>
</html>
