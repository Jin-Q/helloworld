<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>上传页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

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
		//大于500M
		if( file.size > 500 * 1024 *1024 ){
			//ymPrompt.alert({message:'文件最大不能超过10M！',title:'系统提示',handler:null});
			alert('文件最大不能超过500M！');
			return false;
		}
		return true;
	}

</script>
</head>
<body>
	<form id="improtForm" action="<emp:url action='addPubDocumentInfoRecordOp.do'/>" method="POST" enctype="multipart/form-data">
		<div class='emp_gridlayout_title' >文档上传</div>
		<div>
            <span style="font-size:12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上传文件(最大500M)：</span> 
        	<input type="file" name="pFile" ContentEditable="false" />
        </div>
		 <emp:gridLayout id="fileMemoGroup" title="文档描述" maxColumn="2">
	      	<emp:textarea id="file_memo" label="文档描述：" maxlength="2000" required="false" cols="90" />
	     </emp:gridLayout>
	</form>
	<div align="center">
		<emp:button id="upload" label="上传" />
		<emp:button id="back" label="返回" />
	</div>
</body>
</html>
</emp:page>
