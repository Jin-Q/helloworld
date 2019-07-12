<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		CcrRatDirect._toForm(form);
		CcrRatDirectList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCcrRatDirectPage() {
		var paramStr = CcrRatDirectList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrRatDirectUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCcrRatDirect() {
		var paramStr = CcrRatDirectList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrRatDirectViewPage.do"/>?'+paramStr+'&flag=ls';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCcrRatDirectPage() {
		var url = '<emp:url action="getCcrRatDirectAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCcrRatDirect() {
		var paramStr = CcrRatDirectList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCcrRatDirectRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CcrRatDirectGroup.reset();
	};
	
	
	function doLoad(){
		//document.getElementById('button_getUpdateCcrRatDirectPage').style.display='none';	
		//document.getElementById('button_deleteCcrRatDirect').style.display='none';
	}		
	/*--user code end--*/
	

	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CcrRatDirectGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CcrRatDirect.cus_id" label="客户码" />
			<emp:text id="CcrRatDirect.cus_name" label="客户名称 " />
			<emp:text id="CcrRatDirect.cust_mgr" label="客户经理" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCcrRatDirect" label="查看" op="view"/>
	</div>

	<emp:table icollName="CcrRatDirectList" pageMode="true" url="pageCcrRatDirectLSQuery.do">
		<emp:text id="serno" label="业务申请编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称 " />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="input_date" label="认定日期" />
		
		<emp:text id="manager_id" label="客户经理" hidden="true"/>
		<emp:text id="manager_id_displayname" label="责任人"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:select id="approve_status" label="审批状态" dictname="WF_APP_STATUS"  />
	</emp:table>
	
</body>
</html>
</emp:page>
    