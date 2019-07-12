<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdPolcyScheme._toForm(form);
		PrdPolcySchemeList._obj.ajaxQuery(null,form);
	};
	function doSelect(){
		var data = PrdPolcySchemeList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdPolcySchemeGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPolcySchemeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPolcyScheme.schemeid" label="方案编号" />
			<emp:text id="PrdPolcyScheme.schemename" label="方案名称" />
			<emp:select id="PrdPolcyScheme.effectived" dictname="STD_ZX_YES_NO" label="是否启用" />
			<emp:text id="PrdPolcyScheme.inputid" label="登记人员" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
  
		<emp:button id="select" label="选择返回" />
	</div>

	<emp:table icollName="PrdPolcySchemeList" pageMode="true" url="pagePrdPolcySchemeQuery.do">
		<emp:text id="schemeid" label="方案编号" />
		<emp:text id="schemename" label="方案名称" />
		<emp:select id="effectived" label="是否启用" dictname="STD_ZX_YES_NO" />
		<emp:text id="comments" label="备注" />
		<emp:text id="inputid" label="登记人员" />
		<emp:text id="inputdate" label="登记日期" />
		<emp:text id="orgid" label="登记机构" />
	</emp:table>
	<emp:button id="select" label="选择返回"/>
</body>
</html>
</emp:page>
    