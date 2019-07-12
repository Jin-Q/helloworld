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
		CusHandoverLst._toForm(form);
		CusHandoverLstList._obj.ajaxQuery(null,form);
	};
	
	function doViewCusHandoverLst() {
		var paramStr = CusHandoverLstList._obj.getParamStr(['serno','handover_type','business_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusHandoverLstViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doExportCusHandoverLst(){
		var serno = '${context.serno}'; 
		var url = '<emp:url action="getExportCusHandoverLstOp.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	function doReset(){
		page.dataGroups.CusHandoverLstGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusHandoverLstGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusHandoverLst.business_code" label="业务编码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<!--<emp:button id="viewCusHandoverLst" label="查看" op="view"/>-->
		<emp:button id="exportCusHandoverLst" label="导出移交明细" op="view"/>
	</div>

	<emp:table icollName="CusHandoverLstList" pageMode="true" url="pageCusHandoverLstQuery.do" reqParams="CusHandoverLst.serno=$CusHandoverLst.serno;">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="handover_type" label="业务类型" dictname="STD_ZB_HAND_TYPE"/>
		<emp:text id="business_code" label="业务编码" />
		<emp:text id="business_detail" label="移交业务说明" />
	</emp:table>
	
</body>
</html>
</emp:page>
    