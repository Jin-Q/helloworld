<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>承兑汇票导入页面</title>
<jsp:include page="/include.jsp" />
<script>	
	var page = new EMP.util.Page();
	
	function doImportYpReport() {
	    var address = document.getElementsByName("pFile1")[0].value;
		if(address == "") {
			alert("请选择要导入的EXCEL文件");
		}
		if(address != "") {
			var form = document.getElementById('improtForm1');
			form.submit();
		}
	};
	function doImportSpReport() {
	    var address = document.getElementsByName("pFile2")[0].value;
		if(address == "") {
			alert("请选择要导入的EXCEL文件");
		}
		if(address != "") {
			var form = document.getElementById('improtForm2');
			form.submit();
		}
	};
	
	function doReturn(){
		window.close();
	};
	
    //下载模板
    function doDownLoadYpTemp(){
    	var url = '<emp:url action="downLoadIqpBillDetailTmplate.do"/>?bill_type=100';
		url = EMPTools.encodeURI(url);
		window.location = url;
    };
    function doDownLoadSpTemp(){
    	var url = '<emp:url action="downLoadIqpBillDetailTmplate.do"/>?bill_type=200';
		url = EMPTools.encodeURI(url);
		window.location = url;
    };
</script>
</head>
<body class="page_content" >
	<div  class='emp_gridlayout_title'>银行承兑汇票导入</div>
	<form class='emp_group_div' id="improtForm1" action="<emp:url action='importDpoBillDetailOp.do'/>&drfpo_no=${context.drfpo_no}&bill_type=100" method="POST" enctype="multipart/form-data">
	 <table align="center">
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td><input type="file" name="pFile1"  ></td>
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
 	<emp:button id="downLoadYpTemp" label="下载模板"/>&nbsp;
	<emp:button id="importYpReport" label="导入" />&nbsp;
	<emp:button id="return" label="关闭"/>
	<br>
  <br>
  </div>
  
  <div  class='emp_gridlayout_title'>商业承兑汇票导入</div>
	<form class='emp_group_div' id="improtForm2" action="<emp:url action='importDpoBillDetailOp.do'/>&drfpo_no=${context.drfpo_no}&bill_type=200" method="POST" enctype="multipart/form-data">
	 <table align="center">
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td><input type="file" name="pFile2"  ></td>
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
 	<emp:button id="downLoadSpTemp" label="下载模板"/>&nbsp;
	<emp:button id="importSpReport" label="导入" />&nbsp;
	<emp:button id="return" label="关闭"/>
	<br>
  <br>
  </div>

</body>
</html>
</emp:page>