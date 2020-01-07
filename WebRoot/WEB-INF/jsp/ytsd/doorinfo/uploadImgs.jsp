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
			<div class="queryPanel_title_collapse"></div>&nbsp;上传图片
		</div>
	    <div id="queryPanel_content" class="queryPanel_content">
	    	<div class="layui-upload">
			 <button type="button" class="layui-btn layui-btn-sm layui-btn-normal" id="uploadBtn">上传图片</button> 
			  <blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;">
			   	 预览图：
			  	<div class="layui-upload-list" id="imgDiv"></div>
			  	<input type="hidden" name="id" id="id" value="${param.id }"/>
			  	<input type="hidden" name="door_imgs" id="door_imgs"/>
			 </blockquote>
			</div>
	   </div>
	</div>
</body>
<script type="text/javascript" defer="defer">
	layui.use('upload', function(){
	  var upload = layui.upload;
	  //上传图片
	  upload.render({
		    elem: '#uploadBtn'
		    ,url: '${app}/doorinfo/upload.do'
		    ,multiple: true
		    ,acceptMime:'image/*'
			,size:2048//单位kb
		    ,before: function(obj){
		      //预读本地文件示例，不支持ie8
		      //obj.preview(function(index, file, result){
		        //$('#imgDiv').append('<img src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img">')
		      //});
		    }
		    ,done: function(res){
		      //上传完毕
			 if(res.code == 0){
				var realPath = '<%=request.getContextPath()%>' + res.door_img;
				$('#imgDiv').append('<img src="'+ realPath +'" class="layui-upload-img">');
				var door_imgs = $('#door_imgs').val() + ',' + res.door_img;
				$('#door_imgs').val(door_imgs);
			 }
			  
		    }
		    ,allDone: function(obj){ //当文件全部被提交后，才触发
				var url = '${app}/doorinfo/saveDoorImgs.do';
				
				var params="id=" + $('#id').val() + "&door_imgs=" + $('#door_imgs').val();
				var res = new MyJqueryAjax(url,params).request();
				if(res==1) {
					alert('上传成功！');
			  	} else {
					alert('上传失败！');
			  	}
		    }
      });
	});
	
	
</script>
</html>
