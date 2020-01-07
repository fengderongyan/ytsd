<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<title>修改</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/xkweb/saveEdit.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;修改
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<input type="hidden" name="id" value="${map.id }"/>
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;来访时间：
						</td>
						<td class="outDetail2">
							<input type="text" id="begin_date" name="lf_date" size="18"
									onclick="WdatePicker();" readonly="readonly" value="${map.lf_date }"/>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;客户姓名：
						</td>
						<td class="outDetail2">
							<input type="text" id="customer_name" name="customer_name" value="${map.customer_name }"/>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;客户电话：
						</td>
						<td class="outDetail2">
							<input type="text" id="customer_mobile" name="customer_mobile" value="${map.customer_mobile }"/>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;客户性别：
						</td>
						<td class="outDetail2">
							<sgy:select name="customer_sex" value="${map.customer_sex }">
								<sgy:option value="">请选择...</sgy:option>
								<sgy:option value="男">男</sgy:option>
								<sgy:option value="女">女</sgy:option>
							</sgy:select>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;客户年龄：
						</td>
						<td class="outDetail2">
							<input type="text" id="customer_age" type="number" name="customer_age" value="${map.customer_age }"/>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;职业：
						</td>
						<td class="outDetail2">
							<sgy:select name="customer_job" value="${map.customer_job }">
								<sgy:option value="个体">个体</sgy:option>
								<sgy:option value="公务员">公务员</sgy:option>
								<sgy:option value="事业编">事业编</sgy:option>
								<sgy:option value="事业编">职员</sgy:option>
								<sgy:option value="事业编">务农</sgy:option>
								<sgy:option value="事业编">其他</sgy:option>
							</sgy:select>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;区域位置：
						</td>
						<td class="outDetail2">
							<sgy:select name="customer_address" value="${map.customer_address }">
								<sgy:option value="城中">城中</sgy:option>
								<sgy:option value="城东">城东</sgy:option>
								<sgy:option value="城西">城西</sgy:option>
								<sgy:option value="城南">城南</sgy:option>
								<sgy:option value="城北">城北</sgy:option>
								<sgy:option value="连云">连云</sgy:option>
								<sgy:option value="灌南">灌南</sgy:option>
								<sgy:option value="赣榆">赣榆</sgy:option>
								<sgy:option value="东海">东海</sgy:option>
								<sgy:option value="老海州">老海州</sgy:option>
								<sgy:option value="灌云">灌云</sgy:option>
							</sgy:select>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;获知途径：
						</td>
						<td class="outDetail2">
							<sgy:select name="customer_hztj" value="${map.customer_hztj }">
								<sgy:option value="单页">单页</sgy:option>
								<sgy:option value="广告">广告</sgy:option>
								<sgy:option value="横幅">横幅</sgy:option>
								<sgy:option value="介绍">介绍</sgy:option>
								<sgy:option value="路过">路过</sgy:option>
								<sgy:option value="自然来访">自然来访</sgy:option>
								<sgy:option value="工地">工地</sgy:option>
								<sgy:option value="电台">电台</sgy:option>
								<sgy:option value="网络">网络</sgy:option>
								<sgy:option value="老带新">老带新</sgy:option>
								<sgy:option value="户外看板">户外看板</sgy:option>
								<sgy:option value="推广">推广</sgy:option>
								<sgy:option value="房展会">房展会</sgy:option>
								<sgy:option value="短信">短信</sgy:option>
								<sgy:option value="电转访">电转访</sgy:option>
								<sgy:option value="其他">其他</sgy:option>
							</sgy:select>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;户型：
						</td>
						<td class="outDetail2">
							<sgy:select name="door_type" value="${map.door_type }">
								<sgy:option value="小高层">小高层</sgy:option>
								<sgy:option value="多层">多层</sgy:option>
								<sgy:option value="其他">其他</sgy:option>
							</sgy:select>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;面积：
						</td>
						<td class="outDetail2">
							<sgy:select name="door_area" value="${map.door_area }">
								<sgy:option value="120㎡以下">120㎡以下</sgy:option>
								<sgy:option value="120㎡-130㎡">120㎡-130㎡</sgy:option>
								<sgy:option value="130㎡-140㎡">130㎡-140㎡</sgy:option>
								<sgy:option value="140㎡-150㎡">140㎡-150㎡</sgy:option>
								<sgy:option value="150㎡-160㎡">150㎡-160㎡</sgy:option>
								<sgy:option value="160㎡-170㎡">160㎡-170㎡</sgy:option>
								<sgy:option value="170㎡以上">170㎡以上</sgy:option>
							</sgy:select>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;楼号：
						</td>
						<td class="outDetail2">
							<input type="text" id="build_num" name="build_num" value="${map.build_num }"/>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;楼层：
						</td>
						<td class="outDetail2">
							<input type="text" id="floor_num" name="floor_num" value="${map.floor_num }"/>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;意向程度：
						</td>
						<td class="outDetail2">
							<sgy:select name="customer_yxcd" value="${map.customer_yxcd }">
								<sgy:option value="A类">A类</sgy:option>
								<sgy:option value="B类">B类</sgy:option>
								<sgy:option value="C类">C类</sgy:option>
							</sgy:select>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;职业顾问：
						</td>
						<td class="outDetail2">
							<input type="text" id="sale_name" name="sale_name" value="${map.sale_name }"/>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;备注：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="remark" name="remark" value="${map.remark }" size="50"/>
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
