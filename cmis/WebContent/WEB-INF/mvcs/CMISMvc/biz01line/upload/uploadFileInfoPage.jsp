<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
	//从emp常量中取值。
	Context context = (Context) request
			.getAttribute(EMPConstance.ATTR_CONTEXT);
	//登陆人
	String currentUserId = (String) context.getDataValue("currentUserId");
	System.out.print(currentUserId);
	//登陆人所属机构
	String organno = (String) context.getDataValue("organNo");
	String serno = "";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	String file_type = "";
	if(context.containsKey("file_type")){
		file_type = (String)context.getDataValue("file_type");
	}
%>
<emp:page>
	<html>
	<head>
	<title>新增页面</title>

	<jsp:include page="/include.jsp" flush="true" />

	<script type="text/javascript">

	window.onload=function(){
		doSet();
	}
	
	function doSet(){
	   	//设置人员为当前登录用户
		create_user._obj.element.value="<%=currentUserId%>";
 	   	//设置信息录入日期为营业日期
 	  	var timeVal='${context.OPENDAY}';
 	  	create_date._obj.element.value = timeVal;
    }
	
	var page = new EMP.util.Page();
	//上传文件用
	function doUpload() {
	    var address = document.getElementsByName("pFile")[0].value;
		if(address == "") {
			alert("请选择要上传的文件");
			return;
		}
		if(filesizeValidate()){
			var form = document.getElementById('improtForm');
			form.submit();			
		}
	}
	
     //返回
     function doBack(){
    	history.back();
	 }
     
     function filesizeValidate(){
 	    var address = document.getElementsByName("pFile")[0].value;
 		var fso = new ActiveXObject( "Scripting.FileSystemObject" );
 		var file = fso.GetFile(address);
 		//大于10M
 		if( file.size > 10 * 1024 *1024 ){
 			//ymPrompt.alert({message:'文件最大不能超过10M！',title:'系统提示',handler:null});
 			alert('文件最大不能超过10M！');
 			return false;
 		}
 		return true;
 	}

</script>
	</head>
	<body class="page_content">
	<div id='dataGroup_in_formsubmitForm' class='emp_group_div' style=''>

	<div class="current_postion">
	<div class="current_icon"></div>
	相关文档
	<form id="improtForm"
		action="<emp:url action='addUploadFileInfoRecord.do'/>"
		method="POST" enctype="multipart/form-data">
	<table class="result_table" width="100%" border="1"
		bordercolor="transparent">

		<tr class="result_head">

			<td width="15%">文档业务类型</td>
			<td width="15%">业务流水号</td>
			<td width="4%">附加文档</td>
		</tr>
		<tr>

			<td><emp:select id="file_type" label="文件业务类型代码" required="false" readonly="true" defvalue="<%=file_type %>" dictname="STD_ZB_IMAGE_TYPE"/></td>
			<td width="8%"><emp:text id="serno" label="业务流水号" defvalue="<%=serno %>" maxlength="20" required="true" readonly="true"/></td>
			<td><input type="file" name="pFile" ContentEditable="false" /></td>
		</tr>
		<tr>
			<td>创建时间</td>
			<td>创建人</td>
		</tr>
		<tr>
			<td><emp:text id="create_date" label="创建日期" maxlength="10" required="false" readonly="true" />
			<td><emp:text id="create_user" label="创建人" maxlength="100" required="false" readonly="true"/></td>
		</tr>
	</table>
	</form>
	<div align="center"><emp:button id="upload" label="上传" /><emp:button
		id="back" label="返回" /></div>
	</div>

	</body>
	</html>
</emp:page>
