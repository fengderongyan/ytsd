<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/xkweb/list.do"
			class="list" id="row" cellspacing="0" style="width:150%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external">
			
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="客户姓名" property="customer_name" />
			<display:column title="客户电话" property="customer_mobile" />
			
			<display:column title="客户性别" property="customer_sex" />
			<display:column title="客户年龄" property="customer_age" />
			<display:column title="职业" property="customer_job" />
			<display:column title="区域位置" property="customer_address" />
			<display:column title="获知途径" property="customer_hztj" />
			<display:column title="户型" property="door_type" />
			<display:column title="面积" property="door_area" />
			<display:column title="楼号" property="build_num" />
			<display:column title="楼层" property="floor_num" />
			<display:column title="意向程度" property="customer_yxcd" />
			<display:column title="职业顾问" property="sale_name" />
			<display:column title="备注" property="remark" />
			<display:column title="来访时间" property="lf_date" />
			<display:column title="录入时间" property="record_date" />
			<display:column title="操作" media="html" style="width:130px;">
				<c:if test="${user.operatorId != '10003'}">
				<sgy:button cssClass="smallBtn_gray"
						onclick="editInfo('${row.id}','edit');return false;">修改</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="deleteInfo('${row.id}');return false;">删除</sgy:button>&nbsp;
				</c:if>
			</display:column>
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
  		
  		function editInfo(id){
  			MyWindow.OpenCenterWindowScroll('${app}/xkweb/editInfo.do?id=' + id,'edit',500,600);
  		}
  		
  		function deleteInfo(id){
		 	if(!confirm('确定要删除信息？')) {
				return false;
			}
			var url = '${app}/xkweb/deleteInfo.do';
			var params="id="+id;
			var res = new MyJqueryAjax(url,params).request();
			if(res == '"1"') {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
  		
	</script>
</html>