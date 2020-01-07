<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<script type="text/javascript" src="${app}/js/layui/layui.js"></script> 
	<link rel="stylesheet" type="text/css" href="${app}/js/layui/css/layui.css" />
	<title>上传图片</title>
</head>
<body>
	<div id="queryPanel" class="queryPanel">
		<div id="queryPanel_title" class="queryPanel_title">
			<div class="queryPanel_title_collapse"></div>&nbsp;查看图片
		</div>
	    <div id="queryPanel_content" class="queryPanel_content">
	    	<div class="layui-upload">
			  <blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;">
			   	 预览图：
			  	<div class="layui-upload-list" id="imgDiv">
			  		<c:forEach items="${doorImgs }" var="door_img" varStatus="v">
			  			<img src="<%=request.getContextPath()%>${door_img }" id="door_img_id_${v.index }"/>&nbsp;
			  				<a onclick="delImg('${door_img}', this, 'door_img_id_${v.index }')" 
			  					style="text-decoration: underline;color: red">删除</a><br />
			  		</c:forEach>
			  	</div>
			  	<input type="hidden" name="id" id="id" value="${param.id }"/>
			 </blockquote>
			</div>
	   </div>
	</div>
</body>
<script type="text/javascript" defer="defer">
	
	function delImg(door_img, obj, door_img_id){
		if(!confirm('确定要删除信息？')) {
			return false;
		}
		var url = '${app}/doorinfo/delImg.do';
		var params="door_img=" +door_img + "&id=" + $('#id').val();
		var res = new MyJqueryAjax(url,params).request();
		if(res==1) {
			$(obj).remove();
			$("#" + door_img_id).remove();
			alert('删除成功！');
	  	} else {
			alert('删除失败！');
	  	}

	}
	
</script>
</html>
