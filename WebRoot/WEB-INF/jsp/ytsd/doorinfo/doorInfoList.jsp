<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/doorinfo/getDoorInfoList.do"
			class="list" id="row" cellspacing="0" style="width:140%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="true" size="size">
			<display:column title="操作" media="html" style="width:180px;">
				<c:if test="${user.operatorId != '10003'}">
				<c:if test="${row.status != 2}">
					<c:if test="${user.operatorId == '10001' || user.operatorId == '159999'}">
						<sgy:button cssClass="smallBtn_gray"
							onclick="saleDoorFrame('${row.id}')" >出售</sgy:button>&nbsp;
					</c:if>
					<c:if test="${user.operatorId == '10002' || user.operatorId == '159999'}">
						<sgy:button cssClass="smallBtn_gray"
							onclick="editInfo('${row.id}','edit');return false;">修改</sgy:button>&nbsp;
						<sgy:button cssClass="smallBtn_gray"
							onclick="deleteInfo('${row.id}');return false;">删除</sgy:button>&nbsp;
					</c:if>
				</c:if>
				<c:if test="${row.status == 2  && (user.operatorId == '10001' || user.operatorId == '159999')}">
					<sgy:button cssClass="smallBtn_gray"
						onclick="tuiFang('${row.id}')" >退房</sgy:button>&nbsp;
				</c:if>
				</c:if>
				<sgy:button cssClass="smallBtn_gray"
						onclick="editInfo('${row.id}','show');return false;">详情</sgy:button>&nbsp;
			</display:column>
			<display:column title="打印" media="html">
				<c:if test="${row.status == 2 }">
					<sgy:button cssClass="smallBtn_gray"
						onclick="salePrint('${row.id}')" >打印</sgy:button>
					
				</c:if>
			</display:column>
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="楼栋号" property="build_num" />
			<display:column title="单元号" property="unit_num" />
			<display:column title="楼层" property="door_floor"  />
			<display:column title="房号" property="door_num" />
			<display:column title="销售状态" property="status_name" />
			<display:column title="面积" property="area" />
			<display:column title="房型" property="door_type" />
			<display:column title="单价" property="price" />
			<display:column title="总价" property="total_price" />
			<display:column title="最低申请价" property="min_price" />
			<display:column title="朝向" property="direction" />
			<display:column title="最近更新人" property="update_name" />
			<display:column title="最近更新时间" property="update_date" />
			<display:column title="户型图" media="html" style="width:100px;">
				<a onclick="uploadImgs('${row.id}');">上传图片</a>&nbsp;&nbsp;<a onclick="showImgs('${row.id}');">查看</a>
			</display:column>
			
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
    	function editInfo(id, method) {	
      　　		MyWindow.OpenCenterWindowScroll('${app}/doorinfo/doorInfoEdit.do?method='+method+'&id='+id,'doorInfoEdit',400,700);
    	}

    	function uploadImgs(id) {	
	  　　		MyWindow.OpenCenterWindowScroll('${app}/doorinfo/uploadImgs.do?id=' + id,'uploadImgs',600,900);
		}

    	function showImgs(id) {	
	  　　		MyWindow.OpenCenterWindowScroll('${app}/doorinfo/showImgs.do?id='+id,'showImgs',600,900);
		}

		function saleDoorFrame(id){
			var url = '${app}/doorinfo/getSaleStatus.do';
			var params="id="+id;
			var status = new MyJqueryAjax(url,params).request();
			if(status == 2){
				alert("该房屋已被售出");
				return false;
			}
			MyWindow.OpenCenterWindowScroll('${app}/doorinfo/saleDoorFrame.do?id='+id,'saleDoorFrame',600,800);
		}
		
		function salePrint(id){
			MyWindow.OpenCenterWindowScroll('${app}/doorinfo/salePrint.do?id='+id,'salePrint',700,900);
		}
    
	    function deleteInfo(id){
		 	if(!confirm('确定要删除信息？')) {
				return false;
			}
			var url = '${app}/doorinfo/deleteDoorInfo.do';
			var params="id="+id;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
	    function tuiFang(id){
	    	if(!confirm('确定要退房吗？')) {
				return false;
			}
	    	var url = '${app}/doorinfo/tuiFang.do';
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