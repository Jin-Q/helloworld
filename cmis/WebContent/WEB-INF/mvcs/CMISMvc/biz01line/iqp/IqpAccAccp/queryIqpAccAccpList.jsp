<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddIqpAccAccpPage(){
		var url = '<emp:url action="getIqpAccAccpAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAccAccp(){		
		var paramStr = IqpAccAccpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpAccAccpRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateIqpAccAccpPage(){
		var paramStr = IqpAccAccpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAccAccpUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAccAccp(){
		var paramStr = IqpAccAccpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryIqpAccAccpDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAccAccp._toForm(form);
		IqpAccAccpList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IqpAccAccpGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAccAccpGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAccAccp.actp_org_no" label="承兑行行号" />
			<emp:text id="IqpAccAccp.actp_org_name" label="承兑行名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddIqpAccAccpPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAccAccpPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAccAccp" label="删除" op="remove"/>
		<emp:button id="viewIqpAccAccp" label="查看" op="view"/>
	</div>
	<emp:table icollName="IqpAccAccpList" pageMode="true" url="pageIqpAccAccpQuery.do">
		<emp:text id="opac_org" label="签发行" />
		<emp:text id="is_elec_bill" label="是否电子票据" dictname="STD_ZX_YES_NO" />
		<emp:text id="bill_qty" label="汇票数量" />
		<emp:text id="actp_org_name" label="承兑行名称" />
		<emp:text id="actp_org_name" label="承兑行名称" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>