<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	
	function doImportReport(){
		var address = document.getElementsByName("pFile")[0].value;                                 	   		
		if(address == ""){
			alert("请选择要导入的EXCEL文件!");
			return false;
		}else{
			//先校验扩展名是不是.xls，不支持其他格式导入
			var addresses = address.split('.');
			if(addresses.length>0){
				var expen = addresses[addresses.length-1];
				if("XLS"==expen.toUpperCase()){
					var form = document.getElementById('improtForm');
					//LmtBatchCorre._toForm(form);
					improtForm.submit();
				}else{
					alert("选择文件格式不正确，请重新选择！");
					return;
				}
			}
	    }
     }

</script>
</head>
	<body class="page_content" >
	<form id="improtForm" name="improtForm" action="<emp:url action='importMortGuarantyCertiInfo.do?guaranty_no=${param.guaranty_no}'/>"  method="POST" enctype="multipart/form-data">
		<table align="center">
		<br>
		<br>
		<br>
		<br>
		<br>
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td>
			<div>
			  <input type="file" name="pFile" onChange="" >
			  <emp:button id="importReport" label="导入数据"/>
			</div>
			</td>
		</tr>
		</table>
		<br>
		<table align="center">
		<tr>
			<td>(说明：需以系统内提供的同业客户基本信息导入模板为模板，导入同业客户基本信息。）</td>
		</tr>
	 	</table>
	</form>	
</body>
</html>
</emp:page>