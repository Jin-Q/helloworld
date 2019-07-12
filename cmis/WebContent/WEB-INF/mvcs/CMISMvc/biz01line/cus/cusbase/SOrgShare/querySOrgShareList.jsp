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
		SOrgShare._toForm(form);
		SOrgShareList._obj.ajaxQuery(null,form);
	};
	function doSelect(){	
		var data = SOrgShareList._obj.getSelectedData();

		if (data != null) {
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
			
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.SOrgShareGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SOrgShareGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SOrgShare.organno" label="机构码" />
			<emp:text id="SOrgShare.organname" label="机构名称" />
			<emp:text id="SOrgShare.distno" label="地区编号" />
			<emp:text id="SOrgShare.organlevel" label="机构级别" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:table icollName="SOrgShareList" pageMode="true" url="pageSOrgShareQuery.do">
		<emp:text id="organno" label="机构码" />
		<emp:text id="organname" label="机构名称" />
		<emp:text id="distno" label="地区编号" />
		<emp:text id="organlevel" label="机构级别" />
		<emp:text id="state" label="状态" dictname="STD_ZB_ORG_STATUS" />
		<emp:text id="organchief" label="机构负责人" />
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    