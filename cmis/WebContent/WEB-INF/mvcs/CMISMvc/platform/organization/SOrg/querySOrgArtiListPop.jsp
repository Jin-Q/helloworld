<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

/*************** 输入框(input)不可用状态下的样式 ********************/
.emp_field_disabled .emp_field_longtext_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

/*************** 输入框(input)只读状态下的样式 ********************/
.emp_field_readonly .emp_field_longtext_input {
	border-color: #b7b7b7;
}

</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SOrg._toForm(form);
		SOrgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSOrgPage() {
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSOrgUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSOrg() {
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSOrgViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSOrgPage() {
		var url = '<emp:url action="getSOrgAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSOrg() {
		var paramStr = SOrgList._obj.getParamStr(['organno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteSOrgRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SOrgGroup.reset();
	};
	
	/*--user code begin--*/
	function doSelect() {
		var data = SOrgList._obj.getSelectedData();		
		methodName = '${context.returnMethod}';

		if(methodName==null)
		if (data != null && data.length !=0) {
			top.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReturnMethod(methodName){
		if (methodName) {
			var data = SOrgList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			} else {
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SOrg.organno" label="机构码" />
			<emp:text id="SOrg.organname" label="机构名称" cssElementClass="emp_field_longtext_input" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	


	<emp:table icollName="SOrgList" pageMode="true" url="pageSOrgArtiQueryPop.do">
		<emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" />
		<emp:text id="arti_organno" label="所属法人机构码" />
		<emp:text id="organname" label="机构名称" />
		<emp:text id="fincode" label="金融代码" />
	</emp:table>
	
	
	<div align="center"><br>
	<emp:returnButton label="选择返回"/>
	<br>
	</div>
</body>
</html>
</emp:page>
    