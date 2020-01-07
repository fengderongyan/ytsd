<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改楼栋信息"></c:set>
		</c:when>
		<c:when test="${param.method=='add'}">
			<c:set var="title" value="新增楼栋信息"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="楼栋信息详细"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/doorinfo/saveOrUpdateInfo.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<input type="hidden" name="id" value="${map.id }"/>
		    	<input type="hidden" name="method" value="${param.method }"/>
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;楼栋号：
						</td>
						<td class="outDetail2">
							<sgy:select list="buildNumList" name="build_num" id="build_num" value="${map.build_num }" dataType="Require" msg="请选择楼栋号！"
							        headLabel="请选择..." headValue="" optionLabel="build_num" optionValue="build_num" />
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;单元号：
						</td>
						<td class="outDetail2">
							<input type="text" id="unit_num" name="unit_num" value="${map.unit_num }" 
								dataType="Number" msg="请填写单元号, 且为数字！" />
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;楼层数：
						</td>
						<td class="outDetail2">
							<input type="text" id="door_floor" name="door_floor" value="${map.door_floor }" 
								dataType="Number" msg="请填写楼层, 且为数字！" />
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;总楼层数：
						</td>
						<td class="outDetail2">
							<input type="text" id="total_floor" name="total_floor" value="${map.total_floor }" 
								dataType="Number" msg="请填写总楼层数, 且为数字！" />
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;房间号：
						</td>
						<td class="outDetail2">
							<input type="text" id="door_num" name="door_num" value="${map.door_num }" 
								dataType="Number" msg="请填写房间号, 且为数字！" />
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;房型：
						</td>
						<td class="outDetail2">
							<input type="text" id="door_type" name="door_type" value="${map.door_type }" 
								dataType="Require" msg="请填写房型！" />
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;面积(平方米)：
						</td>
						<td class="outDetail2">
							<input type="text" id="area" name="area" value="${map.area }" dataType="Double" msg="请填写面积, 且为数字！" />
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;单价(元/平方米)：
						</td>
						<td class="outDetail2">
							<input type="text" id="price" name="price" value="${map.price }" 
								dataType="Double" msg="请填写单价！" />
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;总价(元)：
						</td>
						<td class="outDetail2">
							<input type="text" id="total_price" name="total_price" value="${map.total_price }" 
								dataType="Double" msg="请填写面积, 且为数字！" />
							&nbsp;<sgy:button cssClass="smallBtn_gray"
									onclick="doCount();return false;">计算</sgy:button>
						</td>
						<td class="outDetail">
							<font color="red">*</font>&nbsp;朝向：
						</td>
						<td class="outDetail2">
							<input type="text" id="direction" name="direction" value="${map.direction }" 
								dataType="Require" msg="请填写朝向！" />
						</td>
					</tr>
					
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;最低申请价(元/平米)：
						</td>
						<td class="outDetail2">
							<input type="text" id="min_price" name="min_price" value="${map.min_price }" 
								dataType="Double" msg="请填写最低申请价, 且为数字！" />
						</td>
						<td class="outDetail">
						</td>
						<td class="outDetail2">
						</td>
					</tr>
					
					
					<c:if test="${map.status == 2 && param.method=='show'}">
						<tr height="20">
							<td class="outDetail" >
								<font color="red">*</font>&nbsp;销售状态：
							</td>
							<td class="outDetail2">
								${map.status_name }
							</td>
							<td class="outDetail" >
								<font color="red">*</font>&nbsp;销售方式：
							</td>
							<td class="outDetail2" >
								${map.is_wx_sale }
							</td>
						</tr>
						<tr height="20">
							<td class="outDetail">
								&nbsp;销售人：
							</td>
							<td class="outDetail2">
								${map.sale_oper_name }
							</td>
							<td class="outDetail">
								&nbsp;销售时间：
							</td>
							<td class="outDetail2">
								${map.sale_date }
							</td>
						</tr>
						<tr height="20" id="salePriceTr" >
						<td class="outDetail">
							客户姓名：
						</td>
						<td class="outDetail2">
							${map.customer_name }
						</td>
						<td class="outDetail">
							身份证号码：
						</td>
						<td class="outDetail2">
							${map.id_card }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							联系方式：
						</td>
						<td class="outDetail2" >
							${map.customer_mobile }
						</td>
						<td class="outDetail">
							通讯地址：
						</td>
						<td class="outDetail2" colspan="3">
							${map.tx_address }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							认购房源：
						</td>
						<td class="outDetail2" >
							${map.rgfy }
						</td>
						<td class="outDetail">
							面积：
						</td>
						<td class="outDetail2">
							${map.sale_area }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							单价：
						</td>
						<td class="outDetail2" >
							${map.sale_price }
						</td>
						<td class="outDetail">
							总价：
						</td>
						<td class="outDetail2">
							${map.sale_total_price }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							定金：
						</td>
						<td class="outDetail2" >
							${map.order_price }
						</td>
						<td class="outDetail">
							付款方式：
						</td>
						<td class="outDetail2">
							${map.order_pay_type }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							首付款：
						</td>
						<td class="outDetail2" >
							${map.first_pay }
						</td>
						<td class="outDetail">
							交付时间：
						</td>
						<td class="outDetail2">
							${map.pay_date }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							剩余房款：
						</td>
						<td class="outDetail2" >
							${map.surplus_pay }
						</td>
						<td class="outDetail">
							付款方式：
						</td>
						<td class="outDetail2">
							${map.surplus_pay_type }
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							贷款金额：
						</td>
						<td class="outDetail2" >
							${map.loan_money }
						</td>
						<td class="outDetail">
							办理时间：
						</td>
						<td class="outDetail2">
							${map.loan_deal_date }
						</td>
					</tr>
					</c:if>
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
	
	function doCount(){
		var area = $('#area').val();
		var price = $('#price').val();
		if(area == ''){
			alert('面积不能为空！');
			return false;
		}
		if(price == ''){
			alert('单价不能为空！');
			return false;
		}
		var re = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/; 
	    if (!re.test(area)){ 
	        alert("非法数字"); 
	        return false;
		}
	    if (!re.test(price)){ 
	        alert("非法数字"); 
	        return false;
		}
	    var total_price = Mul(area, price);
	    $('#total_price').val(total_price);
		
	}
	
	//精确计算两个数相乘
	function Mul(arg1, arg2){
    	var m=0,s1=arg1.toString(),s2=arg2.toString();
    	//获取两个相乘数据的小数点后面的数据的长度，即获取小数的位数，因为最终相乘计算的结果：结果小数的位数=第一个数的小数位数+第二个数的小数位数
    	try{
    	m+=s1.split(".")[1].length;
    	}catch(e){}
    	try{
    	m+=s2.split(".")[1].length;
    	}catch(e){}
    	//将两个小数去掉小数点，强制转换整个值（即进行数值放开小数点位数的倍数），然后进行相乘的操作，相乘的结果除去10的m次方
    	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    }
</script>
</html>
