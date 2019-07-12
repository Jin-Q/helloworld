<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>行业名单批量导入页面</title>
<jsp:include page="/include.jsp" />
<script>	
	var page = new EMP.util.Page();
	function doImportReport() {
	    var address = document.getElementsByName("pFile")[0].value;
		if(address == "") {
			alert("请选择要导入的EXCEL文件");
		}
		if(address != "") {
			var form = document.getElementById('improtForm');
			form.submit();
		}
	};
	
	function doReturn(){
		window.close();
	};
	
    //下载模板
    function doDownLoadTemp(){
    	var url = '<emp:url action="downLoadCusBlkTmplate.do"/>?templateName=indusList.xls';
		url = EMPTools.encodeURI(url);
		window.location = url;
    }
</script>
</head>
<body class="page_content" >
	<div  class='emp_gridlayout_title'>批量名单导入</div>
	<form class='emp_group_div' id="improtForm" action="<emp:url action='dealLmtIndusListByExcel.do'/>" method="POST" enctype="multipart/form-data">
	 <table align="center">
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td><input type="file" name="pFile"  ></td>
		</tr>
 	</table>
 	<table align="center">
		<tr>
			<td>目前只支持03版EXCEL表，请按照模板格式操作</td>
		</tr>
	</table>
	</form>
	
	<br>
 <div align="center">
 	<emp:button id="downLoadTemp" label="下载模板"/>&nbsp;
	<emp:button id="importReport" label="导入" />&nbsp;
	<emp:button id="return" label="关闭"/>
	<br>
  <br>
  </div>

</body>
</html>
</emp:page>