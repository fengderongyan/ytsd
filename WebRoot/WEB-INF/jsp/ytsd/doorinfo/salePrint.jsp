<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<title>打印确认单</title>
	<style>
		.printClass tr{
			height: 30px;
		}
		.printClass tr td {
			border: 1px solid;
			text-align: center;
			font-size:15px;
			height: 40px;
		}
	</style>
</head>

<body>
	<div align="center" style="padding-top: 10px">
		<sgy:button cssClass="ovalbutton" onclick="printHtml();return false;">打印</sgy:button>
		<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
	</div>
	<div id="printDiv">
	<div>
		<div style="float:right;padding-right: 5px;font-size: 15">客<br/>户<br/>联</div>
		<div style="width: 100%;font-size: 36px;font-weight: bold;text-align: center;padding-top: 3%;padding-bottom:10%;letter-spacing: 4px;">
			<div><img src='${app}/images/ytsd_logo.png' width="250px" style="float:left;"/></div>
			<div style="padding-top: 15px"><img src='${app}/images/ljjt_logo.png' width="210px" style="float:right;"/></div>
			<br/><br/><br/>房源交款确认单
		</div>
		<span style="font-size:14px; line-height:2;margin-left:5%;border-bottom:1px solid #dcdcdc">编号：${map.print_num }</span>
		<table class="printClass" cellspacing="0" cellpadding="0" style="width: 90%;margin: auto;">
			<tr>
				<td >
					客户姓名：
				</td >
				<td >
					${map.customer_name }
				</td >
				<td >
					身份证号码：
				</td>
				<td >
					${map.id_card }
				</td>
			</tr>
			<tr  >
				<td >
					联系方式：
				</td>
				<td >
					${map.customer_mobile }
				</td>
				<td >
					通讯地址：
				</td>
				<td >
					${map.tx_address }
				</td>
			</tr>
			<tr  >
				<td >
					认购房源：
				</td>
				<td >
					${map.rgfy }
				</td>
				<td >
					面积：
				</td>
				<td >
					${map.sale_area }
				</td>
			</tr>
			<tr  >
				<td >
					单价：
				</td>
				<td >
					${map.sale_price }
				</td>
				<td >
					总价：
				</td>
				<td >
					${map.sale_total_price }
				</td>
			</tr>
			<tr  >
				<td >
					定金：
				</td>
				<td >
					${map.order_price }
				</td>
				<td >
					付款方式：
				</td>
				<td >
					${map.order_pay_type }
				</td>
			</tr>
			<tr  >
				<td >
					首付款：
				</td>
				<td >
					${map.first_pay }
				</td>
				<td >
					交付时间：
				</td>
				<td >
					${map.pay_date }
				</td>
			</tr>
			<tr  >
				<td >
					剩余房款：
				</td>
				<td >
					${map.surplus_pay }
				</td>
				<td >
					付款方式：
				</td>
				<td >
					${map.surplus_pay_type }
				</td>
			</tr>
			<tr  >
				<td >
					贷款金额：
				</td>
				<td >
					${map.loan_money }
				</td>
				<td >
					办理时间：
				</td>
				<td >
					${map.loan_deal_date }
				</td>
			</tr>
		</table>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;margin-top: 70px'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>客&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>置业顾问：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>财&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</p>
		</div>
		<div style="page-break-after: always;"></div>
	</div>
	<div>
		<div style="float:right;padding-right: 5px;font-size: 15">财<br/>务<br/>联</div>
		<div style="width: 100%;font-size: 36px;font-weight: bold;text-align: center;padding-top: 3%;padding-bottom:10%;letter-spacing: 4px;">
			<div><img src='${app}/images/ytsd_logo.png' width="250px" style="float:left;"/></div>
			<div style="padding-top: 15px"><img src='${app}/images/ljjt_logo.png' width="210px" style="float:right;"/></div>
			<br/><br/><br/>房源交款确认单
		</div>
		<span style="font-size:14px; line-height:2;margin-left:5%;border-bottom:1px solid #dcdcdc">编号：${map.print_num }</span>
		<table class="printClass" cellspacing="0" cellpadding="0" style="width: 90%;margin: auto;">
			<tr>
				<td >
					客户姓名：
				</td >
				<td >
					${map.customer_name }
				</td >
				<td >
					身份证号码：
				</td>
				<td >
					${map.id_card }
				</td>
			</tr>
			<tr  >
				<td >
					联系方式：
				</td>
				<td >
					${map.customer_mobile }
				</td>
				<td >
					通讯地址：
				</td>
				<td >
					${map.tx_address }
				</td>
			</tr>
			<tr  >
				<td >
					认购房源：
				</td>
				<td >
					${map.rgfy }
				</td>
				<td >
					面积：
				</td>
				<td >
					${map.sale_area }
				</td>
			</tr>
			<tr  >
				<td >
					单价：
				</td>
				<td >
					${map.sale_price }
				</td>
				<td >
					总价：
				</td>
				<td >
					${map.sale_total_price }
				</td>
			</tr>
			<tr  >
				<td >
					定金：
				</td>
				<td >
					${map.order_price }
				</td>
				<td >
					付款方式：
				</td>
				<td >
					${map.order_pay_type }
				</td>
			</tr>
			<tr  >
				<td >
					首付款：
				</td>
				<td >
					${map.first_pay }
				</td>
				<td >
					交付时间：
				</td>
				<td >
					${map.pay_date }
				</td>
			</tr>
			<tr  >
				<td >
					剩余房款：
				</td>
				<td >
					${map.surplus_pay }
				</td>
				<td >
					付款方式：
				</td>
				<td >
					${map.surplus_pay_type }
				</td>
			</tr>
			<tr  >
				<td >
					贷款金额：
				</td>
				<td >
					${map.loan_money }
				</td>
				<td >
					办理时间：
				</td>
				<td >
					${map.loan_deal_date }
				</td>
			</tr>
		</table>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;margin-top: 70px'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>客&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>置业顾问：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>财&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</p>
		</div>
		<div style="page-break-after: always;"></div>
	</div>
	<div>
		<div style="float:right;padding-right: 5px;font-size: 15">销<br/>售<br/>联</div>
		<div style="width: 100%;font-size: 36px;font-weight: bold;text-align: center;padding-top: 3%;padding-bottom:10%;letter-spacing: 4px;">
			<div><img src='${app}/images/ytsd_logo.png' width="250px" style="float:left;"/></div>
			<div style="padding-top: 15px"><img src='${app}/images/ljjt_logo.png' width="210px" style="float:right;"/></div>
			<br/><br/><br/>房源交款确认单
		</div>
		<span style="font-size:14px; line-height:2;margin-left:5%;border-bottom:1px solid #dcdcdc">编号：${map.print_num }</span>
		<table class="printClass" cellspacing="0" cellpadding="0" style="width: 90%;margin: auto;">
			<tr>
				<td >
					客户姓名：
				</td >
				<td >
					${map.customer_name }
				</td >
				<td >
					身份证号码：
				</td>
				<td >
					${map.id_card }
				</td>
			</tr>
			<tr  >
				<td >
					联系方式：
				</td>
				<td >
					${map.customer_mobile }
				</td>
				<td >
					通讯地址：
				</td>
				<td >
					${map.tx_address }
				</td>
			</tr>
			<tr  >
				<td >
					认购房源：
				</td>
				<td >
					${map.rgfy }
				</td>
				<td >
					面积：
				</td>
				<td >
					${map.sale_area }
				</td>
			</tr>
			<tr  >
				<td >
					单价：
				</td>
				<td >
					${map.sale_price }
				</td>
				<td >
					总价：
				</td>
				<td >
					${map.sale_total_price }
				</td>
			</tr>
			<tr  >
				<td >
					定金：
				</td>
				<td >
					${map.order_price }
				</td>
				<td >
					付款方式：
				</td>
				<td >
					${map.order_pay_type }
				</td>
			</tr>
			<tr  >
				<td >
					首付款：
				</td>
				<td >
					${map.first_pay }
				</td>
				<td >
					交付时间：
				</td>
				<td >
					${map.pay_date }
				</td>
			</tr>
			<tr  >
				<td >
					剩余房款：
				</td>
				<td >
					${map.surplus_pay }
				</td>
				<td >
					付款方式：
				</td>
				<td >
					${map.surplus_pay_type }
				</td>
			</tr>
			<tr  >
				<td >
					贷款金额：
				</td>
				<td >
					${map.loan_money }
				</td>
				<td >
					办理时间：
				</td>
				<td >
					${map.loan_deal_date }
				</td>
			</tr>
		</table>
		<div style="height: 550px;">
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;margin-top: 70px'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>客&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>置业顾问：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>财&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务：</p>
		</div>
		<div style='border-bottom:1px solid #dcdcdc;width:70%;margin:50px 15%;font-size:20px;padding:10px 0;'>
			<p style='font-size:18px;color:#333;width:100px;display:flex;justify-content:space-between;'>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</p>
		</div>
		</div>
	</div>
	</div>
</body>
<script type="text/javascript" defer="defer">
	
	function printHtml(){
    	var headstr = "<html><head><title></title></head><body>";  
    	var footstr = "</body>";  
    	var printData = document.getElementById("printDiv").innerHTML; //获得 div 里的所有 html 数据
    	var oldstr = document.body.innerHTML;  
    	document.body.innerHTML = printData;  
    	window.print();  
    	document.body.innerHTML = oldstr; 
    	return false;
    }

	//水印
	watermark({watermark_txt: "<p style='text-align: center;'><img src='${app}/images/ytsd_log.png'/><br/>云台山第花园</p>", watermark_objName: "printDiv"})//传入动态水印内容
	//watermark({watermark_txt: "<p style='text-align: center;'>${operName}<br>${orgName}<br>${nowDate}</p>"})//传入动态水印内容
	function watermark(settings) {
	  //默认设置
	  var defaultSettings={
	  	watermark_objName:"id",//容器
	      watermark_txt:"text",
	      watermark_x:10,//水印起始位置x轴坐标
	      watermark_y:50,//水印起始位置Y轴坐标
	      watermark_rows:10000,//水印行数
	      watermark_cols:10000,//水印列数
	      watermark_x_space:80,//水印x轴间隔
	      watermark_y_space:100,//水印y轴间隔
	      watermark_color:'#000000',//水印字体颜色
	      watermark_alpha:0.1,//水印透明度
	      watermark_fontsize:'18px',//水印字体大小
	      watermark_font:'微软雅黑',//水印字体
	      watermark_width:200,//水印宽度
	      watermark_height:80,//水印长度
	      watermark_angle:30//水印倾斜度数
	  };
	  //采用配置项替换默认值，作用类似jquery.extend
	  if(arguments.length===1&&typeof arguments[0] ==="object" )
	  {
	      var src=arguments[0]||{};
	      for(key in src)
	      {
	          if(src[key]&&defaultSettings[key]&&src[key]===defaultSettings[key])
	              continue;
	          else if(src[key])
	              defaultSettings[key]=src[key];
	      }
	  }
	  //页面
	  var oTemp = document.createDocumentFragment();
	  //获取页面最大宽度
	  //var page_width = Math.max(document.body.scrollWidth,document.body.clientWidth);
	  //获取页面最大长度
	  //var page_height = Math.max(document.body.scrollHeight,document.body.clientHeight);
	  
	  //id
	  var obj=document.getElementById(defaultSettings.watermark_objName);
	  //获取页面最大宽度
	  var page_width = obj.offsetWidth; //Math.max(document.body.scrollWidth,document.body.clientWidth);
	  //获取页面最大长度
	  var page_height = obj.offsetHeight ;//Math.max(document.body.scrollHeight,document.body.clientHeight);
	  
	  
	  //如果将水印列数设置为0，或水印列数设置过大，超过页面最大宽度，则重新计算水印列数和水印x轴间隔
	  if (defaultSettings.watermark_cols == 0 ||
	 　　　　(parseInt(defaultSettings.watermark_x 
	　　+ defaultSettings.watermark_width *defaultSettings.watermark_cols 
	　　+ defaultSettings.watermark_x_space * (defaultSettings.watermark_cols - 1)) 
	　　> page_width)) {
	      defaultSettings.watermark_cols = 
	　　　　parseInt((page_width-defaultSettings.watermark_x
	　　　　　　　　+defaultSettings.watermark_x_space) 
	　　　　　　　　/ (defaultSettings.watermark_width 
	　　　　　　　　+ defaultSettings.watermark_x_space));
	      defaultSettings.watermark_x_space = 
	　　　　parseInt((page_width 
	　　　　　　　　- defaultSettings.watermark_x 
	　　　　　　　　- defaultSettings.watermark_width 
	　　　　　　　　* defaultSettings.watermark_cols) 
	　　　　　　　　/ (defaultSettings.watermark_cols - 1));
	  }
	
	  //如果将水印行数设置为0，或水印行数设置过大，超过页面最大长度，则重新计算水印行数和水印y轴间隔
	  if (defaultSettings.watermark_rows == 0 ||
	 　　　　(parseInt(defaultSettings.watermark_y 
	　　+ defaultSettings.watermark_height * defaultSettings.watermark_rows 
	　　+ defaultSettings.watermark_y_space * (defaultSettings.watermark_rows - 1)) 
	　　> page_height)) {
	      defaultSettings.watermark_rows = 
	　　　　parseInt((defaultSettings.watermark_y_space 
	　　　　　　　　　+ page_height - defaultSettings.watermark_y) 
	　　　　　　　　　/ (defaultSettings.watermark_height + defaultSettings.watermark_y_space));
	      defaultSettings.watermark_y_space = 
	　　　　parseInt((page_height 
	　　　　　　　　- defaultSettings.watermark_y 
	　　　　　　　　- defaultSettings.watermark_height 
	　　　　　　　　* defaultSettings.watermark_rows) 
	　　　　　　　/ (defaultSettings.watermark_rows - 1));
	  }
	  var x;
	  var y;
	  for (var i = 0; i < defaultSettings.watermark_rows; i++) {
	      y = defaultSettings.watermark_y + (defaultSettings.watermark_y_space + defaultSettings.watermark_height) * i;
	      for (var j = 0; j < defaultSettings.watermark_cols; j++) {
	          x = defaultSettings.watermark_x + (defaultSettings.watermark_width + defaultSettings.watermark_x_space) * j;
	          var mask_div = document.createElement('div');
	          mask_div.id = 'mask_div' + i + j;
	          var _text = document.createElement("span");
	          _text.innerHTML = defaultSettings.watermark_txt;
	          mask_div.appendChild(_text);
	          //mask_div.appendChild(document.createTextNode(defaultSettings.watermark_txt));
	          //设置水印div倾斜显示
	          
	          mask_div.style.webkitTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
	          mask_div.style.MozTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
	          mask_div.style.msTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
	          mask_div.style.OTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
	          mask_div.style.transform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
	          mask_div.style.visibility = "";
	          mask_div.style.position = "absolute";
	          mask_div.style.left = x + 'px';
	          mask_div.style.top = y + 'px';
	          mask_div.style.overflow = "hidden";
	          mask_div.style.zIndex = "9999";
	          //mask_div.style.border="solid #eee 1px";
	          mask_div.style.opacity = defaultSettings.watermark_alpha;
	          mask_div.style.filter = 'alpha(opacity=30)';
	          mask_div.style.fontSize = defaultSettings.watermark_fontsize;
	          mask_div.style.fontFamily = defaultSettings.watermark_font;
	          mask_div.style.color = defaultSettings.watermark_color;
	          mask_div.style.textAlign = "center";
	          mask_div.style.width = defaultSettings.watermark_width + 'px';
	          mask_div.style.height = defaultSettings.watermark_height + 'px';
	          mask_div.style.display = "block";
	          oTemp.appendChild(mask_div);
	      };
	  };
	  //document.body.appendChild(oTemp);
	  obj.appendChild(oTemp);
	}
	
</script>
</html>
