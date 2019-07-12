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
		PrdPreventRisk._toForm(form);
		PrdPreventRiskList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.PrdPreventRiskGroup.reset();
	};
	
	function doSelect(){
		var data = PrdPreventRiskList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		window.opener["${context.returnMethod}"](data[0]);
		window.close();
	}
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPreventRiskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPreventRisk.prevent_id" label="方案编号" />
			<emp:text id="PrdPreventRisk.prevent_desc" label="方案名称" />
			<emp:select id="PrdPreventRisk.used_ind" label="是否使用" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="select" label="选取返回" />
	</div>
	<emp:table icollName="PrdPreventRiskList" pageMode="true" url="pagePrdPreventRiskQuery.do">
		<emp:text id="prevent_id" label="方案编号" />
		<emp:text id="prevent_desc" label="方案名称" />
		<emp:text id="used_ind" label="是否使用" dictname="STD_ZX_YES_NO" />
	</emp:table>
</body>
</html>
</emp:page>