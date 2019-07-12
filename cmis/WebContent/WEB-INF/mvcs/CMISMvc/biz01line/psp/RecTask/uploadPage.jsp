<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.Map,com.yucheng.cmis.platform.common.upload.util.UploadUtil"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%
	//说明：文件上传页面约束条件，用户自定义优先级高于系统默认；如果通过上传API提供的URL传入则，直接获取，否则获取系统默认配置upload.xml文件中的值
	//获取配置参数
	UploadUtil util = UploadUtil.getInstance(application.getRealPath("/"));
	Map<String,String> uploadParams = util.getUploadParam();
	
	//1.声明文件上传过程中的约束条件
	String allowDoc = null;//文件上传类型；
	String allowImage = null;//图片上传类型限制
	String allowSize = null;//文件上传大小限制 ,单位为M
	String allowNum = null;//文件数量限制（必须为自然数）,如果为0则代表不限制
	String filePath = null;//文件存储根路径，文件存储路径顺序为 模块/功能/年/月/日
	String isModule = "true";//是否根据模块划分存储路径，默认为true
	String isFunction = "true";//是否根据功能划分存储路径，默认为true
	String isYear = "true";//是否根据Year划分存储路径，默认为true
	String isMonth = "true";//是否根据Month划分存储路径,默认为true
	String isDay = "false";//是否根据Day划分存储路径，默认为false
	String isRecover = "true";//到同名文件是否覆盖，默认覆盖，不覆盖则生成新文件名：XXXXX_year||month||day_hour||minute||second.xxx
	String afterUrl = "uploaderFiles.do";//上传后续处理URL
	boolean isMultiple = true;//是否允许多文件上传
	boolean isSelection = true;//是否允许多文件选择
	
	//2.从request请求中获取用户自定义约束，如果没有，则取系统默认配置；
	if(request.getParameter("allow-doc") != null){
		allowDoc = request.getParameter("allow-doc").toString();
	}else {
		allowDoc = uploadParams.get("allow-doc");
	}
	if(request.getParameter("allow-image") != null){
		allowImage = request.getParameter("allow-image").toString();
	}
	if(request.getParameter("allow-size") != null){
		allowSize = request.getParameter("allow-size").toString();
	}else {
		allowSize = uploadParams.get("allow-size");
	}
	if(request.getParameter("allow-num") != null){
		allowNum = request.getParameter("allow-num").toString();
	}else {
		allowNum = uploadParams.get("allow-num");
	}
	if(request.getParameter("filePath") != null){
		filePath = request.getParameter("filePath").toString();
	}else {
		filePath = uploadParams.get("filePath");
	}
	if(request.getParameter("isModule") != null){
		isModule = request.getParameter("isModule").toString();
	}else {
		isModule = uploadParams.get("isModule");
	}
	if(request.getParameter("isFunction") != null){
		isFunction = request.getParameter("isFunction").toString();
	}else {
		isFunction = uploadParams.get("isFunction");
	}
	if(request.getParameter("isYear") != null){
		isYear = request.getParameter("isYear").toString();
	}else {
		isYear = uploadParams.get("isYear");
	}
	if(request.getParameter("isMonth") != null){
		isMonth = request.getParameter("isMonth").toString();
	}else {
		isMonth = uploadParams.get("isMonth");
	}
	if(request.getParameter("isDay") != null){
		isDay = request.getParameter("isDay").toString();
	}else {
		isDay = uploadParams.get("isDay");
	}
	if(request.getParameter("isRecover") != null){
		isRecover = request.getParameter("isRecover").toString();
	}else {
		isRecover = uploadParams.get("isRecover");
	}
	if(request.getParameter("afterUrl") != null){
		afterUrl = request.getParameter("afterUrl").toString();
	}
	if(request.getParameter("isMultiple") != null){
		isMultiple = Boolean.valueOf(request.getParameter("isMultiple").toString());
	}
	if(request.getParameter("isSelection") != null){
		isSelection = Boolean.valueOf(request.getParameter("isSelection").toString());
	}
	
	
	//3.封装请求参数
	String params = " ";
	StringBuffer reqParams = new StringBuffer();
	if(allowDoc != null){
		reqParams.append("&allow-doc=").append(allowDoc);
	}
	if(allowImage != null){
		reqParams.append("&allow-image="+allowImage);
	}
	if(allowSize != null){
		reqParams.append("&allow-size="+allowSize);
	}
	if(allowNum != null){
		reqParams.append("&allow-num="+allowNum);
	}
	if(filePath != null){
		reqParams.append("&filePath="+filePath);
	}
	if(isModule != null){
		reqParams.append("&isModule="+isModule);
	}
	if(isFunction != null){
		reqParams.append("&isFunction="+isFunction);
	}
	if(isYear != null){
		reqParams.append("&isYear="+isYear);
	}
	if(isMonth != null){
		reqParams.append("&isMonth="+isMonth);
	}
	if(isDay != null){
		reqParams.append("&isDay="+isDay);
	}
	if(isRecover != null){
		reqParams.append("&isRecover="+isRecover);
	}
	if(reqParams != null && reqParams.length() > 0){
		params = reqParams.toString();
	}
	
	//文档类型标题
	String docTitle = "Docuemnt("+ allowDoc +")";
	//图片类型标题
	String   imageTitle = "";
	if(allowImage != null){
		imageTitle = "Image(" +allowImage + ")";
		
	}
	
	// 运行环境优先级
	String runtimes = uploadParams.get("runtimes");
	//获取服务器请求路径
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	
%>
<emp:page>
	<html>
<head>
<jsp:include page="/EUIInclude.jsp" flush="true" />
<link rel="stylesheet" href="scripts/platform/common/upload/plupload/queue/css/jquery.plupload.queue.css" type="text/css"></link>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/plupload.js"></script>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/plupload.html4.js"></script>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/plupload.html5.js"></script>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/plupload.flash.js"></script>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/zh_CN.js"></script>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/queue/jquery.plupload.queue.js"></script>
<script type="text/javascript" src="scripts/platform/common/upload/plupload/queue/jquery.plupload.empqueue.js"></script>
</head>
<body id="managerFile" style="padding: 0px;">
	<div id="uploader">&nbsp;</div>
	<script type="text/javascript">
		var reqParams = '<%=params%>';//可选参数
		var isMultiple = <%=isMultiple%>;//是否允许多文件上传
		var isSelection = <%=isSelection%>;//是否允许多文件选择
		var afterUrl = '<%=afterUrl%>';//上传后续路径
		var returnMethod = '${param.returnMethod}';//获取回调方法
		var filterArr = [];
		var imageTitle = '<%=imageTitle%>';
		//存储返回对象
		var returnResponses = [];
		// 上传成功文件数组
		var files = [];
		// 上传出错文件数组
		var errors = [];
		// 是否分多次上传
		var chunk = false;
		// 最大文件
		var max_file_size = '<%=allowSize%>'+'m';
		// 过滤DOC类型      
		var docFilter = {
			title : "<%=docTitle%>",
			extensions : "<%=allowDoc%>"
		};
		filterArr.push(docFilter);
		if(imageTitle!=''){
			var imageFilter = {
					title : "<%=imageTitle%>",
					extensions : "<%=allowImage%>"  
				    };
			filterArr.push(imageFilter);
			}
		
		// 过滤Image类型
	
		var url = '<emp:url action="'+afterUrl+ '"/>?module_id=${param.module_id}&func_id=${param.func_id}&data_id=${param.data_id}&content=${param.content}'+reqParams;
		url = EMPTools.encodeURI(url);
		$("#uploader").pluploadQueue(
				$.extend({
					runtimes : '<%=runtimes%>',
					url : url,
					max_file_size : max_file_size,
					//设置上传字段的名称,默认情况下被设置为文件(file)
					file_data_name : 'file',
					unique_names : true,
					multiple_queues : true,
					multi_selection: isSelection,
					rename:true,
				
					filters : [ docFilter ],
					flash_swf_url : 'scripts/platform/common/upload/plupload/plupload.flash.swf', 
					init : {
						//文件上传成功的时候触发，多文件上传时，会触发多次
						FileUploaded : function(uploader, file, response) {
							try{
								var json = eval("("+response.response+")");
								returnResponses.push(json);
								if (json.status == "true") {
									files.push(file.name);
								}else if(json.status == "SizeLimitExceededException") {
									uploader.removeFile(file);
									EMP.alertMessage("SYS0000013");
									// $.messager.alert('提示','文件太大','info');
								}
							}catch(e){
								EMP.alertMessage("SYS0000056");
								// $.messager.alert('提示','系统发生异常，请您联系管理员');
							}
							
						},
						FilesAdded: function (up, files) {
							
						    $.each(up.files, function (i, file) {
						        if (up.files.length <= 1) {
						            return;
						        }
						        if(isMultiple != true){
						        	up.removeFile(file);
							    }
						    });
						},
						//当队列中所有文件被上传完时触发
						UploadComplete : function(uploader, fs) {
							//回调函数，判断是否存在回调方法设定，如果存在则直接调用回调函数
							try{
// 								EMP.alertMessage("SYS0000014");
								// $.messager.alert('提示', "上传完成", 'info');
								if(returnMethod != null){
									eval('window.parent.${param.returnMethod}(fs,returnResponses)'); 
									window.location.reload();
								}
							}catch(e){};
						},
						//上传出错的时候触发
						Error : function(l, o) {
							var m = o.file, n;
							if(m){
								n = o.message;
								if(o.details){
									n += " (" + o.details + ")";
								}
								if (o.code == plupload.FILE_SIZE_ERROR) {
									var params = new Array();
									params[0] = m.name;
									params[1] = max_file_size;
									EMP.alertMessage("SYS0000015", params);
									// $.messager.alert('提示', m.name+'过大，上传限制：'+max_file_size+'m', 'info');
								}
								if (o.code == plupload.FILE_EXTENSION_ERROR) {
									EMP.alertMessage("SYS0000016", params);
									// $.messager.alert("错误","Invalid file extension: "+ m.name,'error')
								}
								m.hint = n;
								$("#" + m.id).attr("class","plupload_failed").find("a").css("display","block").attr("title", n);
								}
							}
						}
				}, (chunk ? {
					chunk_size : '1gb'
				} : {})));

		$("#uploader").bind();
	</script>
</body>
	</html>
</emp:page>