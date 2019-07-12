<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>还款计划</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	function doload(){

			var selectObj = ReportInfo.report_type._obj.element;

			var recordCount = ExistReportList._obj.recordCount;
			recordCount = Number(recordCount);
			if( recordCount > 0 ){
				for( var i=0;i < recordCount;i++){
					var selectLen = selectObj.options.length;
					var reportType = ExistReportList._obj.data[i].report_type._getValue();
					for(var j=0;j<selectLen;j++){
						var val = selectObj.options[j].value;
						if(val == reportType){
							selectObj.remove(j);
							break;
						}
					}
				}
			}

			if(selectObj.options.length <= 1){
				ReportInfo.report_type._obj._renderReadonly(true);
				ReportInfo.report_type._obj._renderRequired(false);
				var div = document.getElementById("impDiv");
				div.style.display = "none";
			}
	}

	function doImportReport(){

		//保存 之前先校验必须项目
		var result = ReportInfo._checkAll();
		
		if(result){
			var address = document.getElementsByName("pFile")[0].value;
			if(address == "") {
				alert("请选择要导入的EXCEL文件!!!!!");
				return false;
			}
		
			ReportInfo.pFile._setValue(address);
			
			var form = document.getElementById('improtForm');
			ReportInfo._toForm(form);
			form.submit();	
			
		}else{
			alert("请输入必输项！");
		}
		
	}


	function doLinkViewReport(){
		var data = ExistReportList._obj.getSelectedData()[0];
		if (data != null ) {
			var reportType = data.report_type._getValue();

			var serno = ReportInfo.serno._getValue();
			if( serno == "" ){
				alert("流水号不能为空！");
				return;
			}
			var url = '<emp:url action="getReportViewPage.do"/>&serno='+serno+"&report_type="+reportType;
			url=encodeURI(url);
			window.location = url;
		}
	}

	function doDelete(){
		var paramStr = ExistReportList._obj.getParamStr(['serno','report_type']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="doDeleteReport.do"/>';
				url = EMPTools.encodeURI(url);
				doDel(url,paramStr);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doDel(url,paramStr){
		var handleSuccess = function(o){
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == 'success'){
					window.location.reload();
				}else{
					alert("删除失败！");
				}
			}
		};
		var callback = {
				success:handleSuccess,
				failure:function(){alert("删除失败！");}
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback, paramStr);
	}

</script>
</head>
<body class="page_content" onload="doload()">
	<form id="improtForm"  action="<emp:url action='importReportData.do'/>"  method="POST" enctype="multipart/form-data">
		<emp:gridLayout id="TableInfoGroup" title="报表信息" maxColumn="2">
			<emp:text id="ReportInfo.serno" label="流水号" hidden="false" required="true"/>
			<emp:select id="ReportInfo.report_type" label="报表类型" dictname="STD_ZB_REPORT_TYPE" required="true"/>
			<emp:text id="ReportInfo.pFile" label="路径" hidden="true"/>
		</emp:gridLayout>
		
	 	<table align="center" style="display:${context.flag eq 'query' ? 'none' : ''}">
			<tr>
				<td>请选择要导入的EXCEL表的路径：</td>
				<td><input type="file" name="pFile"></td>
				<td>(说明：目前只支持03版EXCEL表)</td>
			</tr>
		 </table>
	</form>
	<div id ="impDiv" align="center" >
		<emp:button id="importReport" label="导入报表" op="${context.flag eq 'query' ? 'op' : ''}"/>
	</div>
	<br/>
	
	<div class='emp_gridlayout_title'>
		<emp:label text="已导入报表"></emp:label>
	</div>
	<br/>
	
	<div align="left">
			<emp:button id="delete" label="删除"/>
	</div>
	<emp:table icollName="ExistReportList" pageMode="false" url='' >
			<emp:link id="serno" label="业务流水号" operation="linkViewReport"/>
			<emp:link id="report_name" label="已导入表单" operation="linkViewReport" />
			<emp:text id="report_type" label="报表类型" hidden="true" />
	</emp:table>
</body>
</html>
</emp:page>
