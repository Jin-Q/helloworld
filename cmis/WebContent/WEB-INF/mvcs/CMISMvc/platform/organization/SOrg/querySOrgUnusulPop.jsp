<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 250px;
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
	function doQuery() {
		var form = document.getElementById('queryForm');
		SOrg._toForm(form);
		SOrgList._obj.ajaxQuery(null, form);
	};

	function doReset() {
		page.dataGroups.SOrgGroup.reset();
	};

	/*--user code begin--*/

	function doSelect() {
		var data = SOrgList._obj.getSelectedData();		
		if (data != null) {
			top.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
			return;
		}
	};

	function doReturnMethod(methodName){
			var data = SOrgList._obj.getSelectedData();
			if(data != null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else {
				alert('请先选择一条记录！');
			}
	};	
	/*--user code end--*/
</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="SOrg.organno" label="机构码" />
		<emp:text id="SOrg.organname" label="机构名称" cssElementClass="emp_field_longtext_input" />
		<emp:text id="SOrg.distno" label="地区编号" hidden="true"/>
		<emp:text id="SOrg.fincode" label="金融代码" hidden="true" />
		<emp:text id="SOrg.arti_organno" label="所属法人机构码" hidden="true"/>
		<emp:text id="SOrg.suporganno" label="上级机构码" hidden="true"/>
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />



	<emp:table icollName="SOrgList" pageMode="true" url="pageSOrgUnusulQuery.do">
		<emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" />
		<emp:text id="organname" label="机构名称" />
		<emp:text id="distno" label="地区编号" />
		<emp:text id="fincode" label="金融代码" />
		<emp:text id="arti_organno" label="所属法人机构码" />
	</emp:table>

	<div align="center"><br>
	<emp:returnButton label="选择返回"/>
	<br>
	</div>
	</body>
	</html>
</emp:page>
