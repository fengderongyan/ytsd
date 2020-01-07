<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/salestatus/list.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="true" size="size">
			
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="楼栋号" property="build_num" />
			<display:column title="销售状态" property="sale_status" />
			<display:column title="最近更新人" property="record_name" />
			<display:column title="最近更新时间" property="record_date" />
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
		
    	function editInfo(id, method) {	
      　　		MyWindow.OpenCenterWindowScroll('${app}/salestatus/edit.do?method='+method+'&id='+id,'edit',400,700);
    	}
    
	    function deleteInfo(id){
		 	if(!confirm('确定要删除信息？')) {
				return false;
			}
			var url = '${app}/salestatus/deleteInfo.do';
			var params="id="+id;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
		
	</script>
</html>