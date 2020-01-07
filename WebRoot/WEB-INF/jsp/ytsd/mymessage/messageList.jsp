<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/myMessage/messageList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="true" size="size">
			
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="标题" property="title" />
			<display:column title="状态" property="status_name" />
			<display:column title="最近更新人" property="update_name" />
			<display:column title="最近更新时间" property="update_date" />
			<display:column title="详情" media="html" style="width:45px;">
				<sgy:button cssClass="smallBtn_gray"
					onclick="editInfo('${row.id}','show');return false;">详情</sgy:button>&nbsp;
			</display:column>
			<display:column title="操作" media="html" style="width:130px;">
				<c:if test="${user.operatorId != '10003'}">
				<c:if test="${row.status == 0 }">
					<sgy:button cssClass="smallBtn_gray"
						onclick="tsInfo('${row.id}');return false;">推送</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="editInfo('${row.id}','edit');return false;">修改</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="deleteInfo('${row.id}');return false;">删除</sgy:button>&nbsp;
				</c:if>
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
      　　		MyWindow.OpenCenterWindowScroll('${app}/myMessage/messageEdit.do?method='+method+'&id='+id,'messageEdit',600,1000);
    	}
    
	    function deleteInfo(id){
		 	if(!confirm('确定要删除信息？')) {
				return false;
			}
			var url = '${app}/myMessage/deleteMessage.do';
			var params="id="+id;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}

		function tsInfo(id){
			if(!confirm('确定要推送该信息吗？')) {
				return false;
			}
			var url = '${app}/myMessage/tsInfo.do';
			var params="id="+id;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('推送成功！');
		  	} else {
				alert('推送失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
	</script>
</html>