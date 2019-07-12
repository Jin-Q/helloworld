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
		SOrg._toForm(form);
		SOrgList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.SOrgGroup.reset();
	};

	function doSelectreturn(){
		var data = SOrgList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data==null||data.length==0) {
			alert('请先选择一条记录！');
			return;
		}
		top.opener[methodName](data[0]);
		window.close();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SOrg.organno" label="机构码" />
			<emp:text id="SOrg.organname" label="机构名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="selectreturn" label="选取返回"/>
	</div>

	<emp:table icollName="SOrgList" pageMode="true" url="pageSOrgQuery.do">
		<emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" />
		<emp:text id="arti_organno" label="所属法人机构码" />
		<emp:text id="organname" label="机构名称" />
		<emp:text id="fincode" label="金融代码" />
	</emp:table>
	
</body>
</html>
</emp:page>
    