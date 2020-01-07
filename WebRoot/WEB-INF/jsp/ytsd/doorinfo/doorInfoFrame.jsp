<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>楼栋信息管理</title>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="${app}/doorinfo/getDoorInfoList.do" target="listQuery">
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
								楼栋号：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<sgy:select list="buildNumList" name="build_num" id="build_num" value=""
							        headLabel="请选择..." headValue="" optionLabel="build_num" optionValue="build_num" />
							</td>
							<td class="outDetail" style="width: 70px;">
								单元号：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="unit_num" name="unit_num"/>
							</td>
							<td class="outDetail" style="width: 70px;">
								房间号：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="door_num" name="door_num"/>
							</td>
							<td class="outDetail" style="width: 70px;">
								销售状态：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<select name="status">
									<option value="">请选择...</option>
									<option value="0">未销售</option>
									<option value="1">预定中</option>
									<option value="2">已售出</option>
								</select>
							</td>
						</tr>
					</table>
					<div id="queryPanel_footer" class="queryPanel_footer"
						style="position: absolute; bottom: 1px; right: 10px;">
						<input type="hidden" id="pageSize" name="pageSize" value="" />
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="sch();return false;">查 询</sgy:button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<c:if test="${user.operatorId != '10003'}">
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="add();return false;">新 增</sgy:button>
						</c:if>
					</div>
				</div>
			</div>
		</form>
		<div id="listPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>
				&nbsp;楼栋信息
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
  			if($("#build_num").val() != "" && isNaN($("#build_num").val())){
				alert("楼栋号请填写数字！");
				return false;
  	  	  	}
  			if($("#unit_num").val() != "" && isNaN($("#unit_num").val())){
				alert("单元号请填写数字！");
				return false;
  	  	  	}
  			if($("#door_num").val() != "" && isNaN($("#door_num").val())){
				alert("房间号请填写数字！");
				return false;
  	  	  	}
  			
  			disable(document.getElementById("schbtn"));
  			$('#listQuery').attr('src','${app}/inc/baseLoading.jsp');
			setTimeout('form1.submit();',500);
  		}
  	
		function add() {
			MyWindow.OpenCenterWindowScroll('${app}/doorinfo/doorInfoEdit.do?method=add','doorInfoEdit',400,700);
		}
	</script>
</html>