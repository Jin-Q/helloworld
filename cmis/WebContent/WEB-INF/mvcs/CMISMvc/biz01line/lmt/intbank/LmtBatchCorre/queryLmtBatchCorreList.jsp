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
		LmtBatchCorre.pFile._setValue(address);
		var batch_cus_no = '${context.batch_cus_no}';
		var cdt_lvl = '${context.cdt_lvl}';
		var serno = '${context.serno}';
		LmtBatchCorre.batch_cus_no._setValue(batch_cus_no);
		LmtBatchCorre.cdt_lvl._setValue(cdt_lvl);
		LmtBatchCorre.serno._setValue(serno);                                    	   		
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
					LmtBatchCorre._toForm(form);
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
	<form id="improtForm" name="improtForm" action="<emp:url action='importBatchReport.do'/>"  method="POST" enctype="multipart/form-data">
		<emp:text id="LmtBatchCorre.cus_id" label="客户码" hidden="true"/>
		<emp:text id="LmtBatchCorre.batch_cus_no" label="批量客户码" hidden="true"/>	
		<emp:text id="LmtBatchCorre.pFile" label="路径" hidden="true" />	
		<emp:text id="LmtBatchCorre.cdt_lvl" label="信用等级" hidden="true"/>
		<emp:text id="LmtBatchCorre.serno" label="业务编号" hidden="true"/>	
	 	<table align="center">
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td>
			<div>
			<!--<input type="text" id="txt" name="txt" size="50">   
            <input type="button" onmousemove="pFile.style.pixelLeft=event.x-60;pFile.style.pixelTop=this.offsetTop;" value="请选择文件"  class="button80" onclick="pFile.click()">
			<input type="file" id="pFile" onchange="txt.value=this.value" style="position:absolute;filter:alpha(opacity=0);" size="1"> -->
			  <input type="file" name="pFile" onChange="">
			  <emp:button id="importReport" label="导入数据"/>
			</div>
			</td>
			<td>(说明：目前只支持03版EXCEL表)</td>
		</tr>
	 </table>
	</form>	
</body>
</html>
</emp:page>