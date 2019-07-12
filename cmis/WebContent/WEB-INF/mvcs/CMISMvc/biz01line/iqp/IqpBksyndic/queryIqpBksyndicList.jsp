<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddIqpBksyndicPage(){
		var url = '<emp:url action="getIqpBksyndicAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBksyndic(){		
		var paramStr = IqpBksyndicList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpBksyndicRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateIqpBksyndicPage(){
		var paramStr = IqpBksyndicList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBksyndicUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBksyndic(){
		var paramStr = IqpBksyndicList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryIqpBksyndicDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBksyndic._toForm(form);
		IqpBksyndicList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IqpBksyndicGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBksyndicGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpBksyndic.bank_syndic_type" label="银团类型" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddIqpBksyndicPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBksyndicPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBksyndic" label="删除" op="remove"/>
		<emp:button id="viewIqpBksyndic" label="查看" op="view"/>
	</div>
	<emp:table icollName="IqpBksyndicList" pageMode="true" url="pageIqpBksyndicQuery.do">
		<emp:text id="bank_syndic_type" label="银团类型" />
		<emp:text id="agent_org_flag" label="代理行标志" />
		<emp:text id="bank_syndic_amt" label="银团贷款总金额" />
		<emp:text id="agent_rate" label="代理费率" />
		<emp:text id="amt_arra_rate" label="资金安排费率" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>