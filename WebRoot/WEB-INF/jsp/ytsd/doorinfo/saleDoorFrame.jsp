<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<title>出售</title>
	<style>
		.displayTable thead tr th {
			height: 25px;
			font-size:10px;
		}
	</style>
</head>

<body>
	<form id="form1" name="form1" action="${app}/doorinfo/saveSaleInfo.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;出售
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<input type="hidden" name="id" value="${param.id }"/>
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail" width="30%">
							<font color="red">*</font>&nbsp;售出方式：
						</td>
						<td class="outDetail2" colspan="3">
							<select id="is_wx_sale" name="is_wx_sale" onchange="saleChange()" dataType="Require" msg="请选择售出方式！">
								<option value="1">线上售出</option>
							</select>
						</td>
					</tr>
				</table>
				<center style="width:100%;" >
					<display:table name="saleList" class="list" id="row" cellspacing="0" cellpadding="0" style="width:100%;display: none">
				    <display:column title="选择销售信息" >
						<input type="radio" id="apply_id" name="apply_id" value ="${row.apply_id}" onclick="realPriceChange('${row.apply_price}', '${row.apply_id}')" />
					</display:column>
					<display:column title="序号">
						${row_rowNum }
					</display:column>
					<display:column title="销售人姓名"  property="sale_name" />
					<display:column title="申请价格"  property="apply_price"/>
					<display:column title="申请时间"  property="apply_date"/>
					<display:column title="客户姓名"  property="customer_name"/>
					<display:column title="客户联系方式"  property="customer_mobile"/>
					</display:table>
			    </center>
				<input type="hidden" name="wx_real_price" id="wx_real_price"/>
				<br />
				<table id="tbody_id" class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20" id="salePriceTr" >
						<td class="outDetail">
							客户姓名：
						</td>
						<td class="outDetail2">
							<input name="customer_name" id="customer_name"/>
						</td>
						<td class="outDetail">
							身份证号码：
						</td>
						<td class="outDetail2">
							<input name="id_card" id="id_card"/>
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							联系方式：
						</td>
						<td class="outDetail2" >
							<input name="customer_mobile" id="customer_mobile"/>
						</td>
						<td class="outDetail">
							通讯地址：
						</td>
						<td class="outDetail2" colspan="3">
							<input name="tx_address" id="tx_address"/>
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							认购房源：
						</td>
						<td class="outDetail2" >
							<input name="rgfy" id="rgfy"/>
						</td>
						<td class="outDetail">
							面积：
						</td>
						<td class="outDetail2">
							<input name="area" id="area"/>
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							单价：
						</td>
						<td class="outDetail2" >
							<input name="price" id="price"/>
						</td>
						<td class="outDetail">
							总价：
						</td>
						<td class="outDetail2">
							<input name="total_price" id="total_price"/>
							&nbsp;<sgy:button cssClass="smallBtn_gray"
									onclick="doCount();return false;">计算</sgy:button>
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							定金：
						</td>
						<td class="outDetail2" >
							<input name="order_price" id="order_price"/>
						</td>
						<td class="outDetail">
							付款方式：
						</td>
						<td class="outDetail2">
							<input name="order_pay_type" id="order_pay_type"/>
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							首付款：
						</td>
						<td class="outDetail2" >
							<input name="first_pay" id="first_pay"/>
						</td>
						<td class="outDetail">
							交付时间：
						</td>
						<td class="outDetail2">
							<input type="text" name="pay_date" id="pay_date" readonly="readonly" class="Wdate" size="15" onfocus="WdatePicker();" />
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							剩余房款：
						</td>
						<td class="outDetail2" >
							<input name="surplus_pay" id="surplus_pay"/>
						</td>
						<td class="outDetail">
							付款方式：
						</td>
						<td class="outDetail2">
							<input name="surplus_pay_type" id="surplus_pay_type"/>
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							贷款金额：
						</td>
						<td class="outDetail2" >
							<input name="loan_money" id="loan_money"/>
						</td>
						<td class="outDetail">
							办理时间：
						</td>
						<td class="outDetail2">
							<input type="text" name="loan_deal_date" id="loan_deal_date" readonly="readonly" class="Wdate" size="15" onfocus="WdatePicker();" />
						</td>
					</tr>
					<tr height="20" >
						<td class="outDetail">
							置业顾问：
						</td>
						<td class="outDetail2" >
							<input name="sale_oper_name" id="sale_oper_name"/>
						</td>
						<td class="outDetail">
							置业顾问电话：
						</td>
						<td class="outDetail2">
							<input type="text" name="sale_oper_mobile" id="sale_oper_mobile"/>
						</td>
					</tr>
				</table>
		    </div>
		</div>
		<br />
		<p align="center">
			<input type="hidden" name="sale_group_id" id="sale_group_id"/>
			<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">保 存</sgy:button>
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
	</form>
</body>
<script type="text/javascript" defer="defer">
	saleChange();
	function saleChange(){
		var is_wx_sale = $("#is_wx_sale").val();
		if(is_wx_sale == 1){//线上销售
			$("#row").css("display", "");
			
		}else if(is_wx_sale == 2){//线下销售
			$("#row").css("display", "none");
		}
	}

	function realPriceChange(apply_price, apply_id){
		$("#wx_real_price").val(apply_price);
		$.ajax({
				type :'post',
				url : '${app}/doorinfo/saleInfoAjax.do',
				data : {apply_id:apply_id},
				timeout:30000,
				dataType :'json',
				success : function(data) {
					$('#customer_name').val(data.customer_name);
					$('#customer_mobile').val(data.customer_mobile);
					$('#price').val(data.apply_price);
					$('#area').val(data.area);
					$('#sale_oper_name').val(data.sale_oper_name);
					$('#sale_group_id').val(data.group_id);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					alert(网络连接超时);
				}
		});
		
		
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
	    
	    	
	    var total_price =Mul(area, price);
	    $('#total_price').val(total_price);
		
	}
	
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
	
	//保存
	function sav(src) {
		disable(src);
		var obj = document.getElementsByName("apply_id");
    	var flags = false;  
    	var is_wx_sale = $('#is_wx_sale').val();
    	for(var i=0; i<obj.length; i ++){
            if(obj[i].checked==true){
            	flags = true;
            }
        }
    	if (flags == false && is_wx_sale == 1){  
    	    alert("请选择销售信息");
    	    enable(src);
    	    return false;
    	} 
		if(!Validator.Validate('form1')) {
			enable(src);
			return false;
		}
		var price = $('#price').val();
		if(price != ""){
			$("#wx_real_price").val(price);
		}
		
		if(!confirm('确定要出售该房屋吗？')) {
			enable(src);
			return false;
		}
		
		$('#form1').submit();
	}
	
</script>
</html>
