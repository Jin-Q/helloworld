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
		IqpCoreConNet._toForm(form);
		IqpCoreConNetList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpCoreConNet() {
		var paramStr = IqpCoreConNetList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url;
			var app_type = IqpCoreConNetList._obj.getSelectedData()[0].app_type._getValue();
			if(app_type == "01"){
				url = '<emp:url action="getIqpCoreConNetViewPage.do"/>?showBut=IqpCoreView&flag=net&'+paramStr+"&op=view";
			}else{
				url = '<emp:url action="getIqpCoreConNetViewPage.do"/>?showBut=IqpCoreView&flag=ls&'+paramStr;
			}
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	
	function doReset(){
		page.dataGroups.IqpCoreConNetGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="IqpCoreConNetGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpCoreConNet.cus_id" label="客户码"/>
			<emp:text id="IqpCoreConNet.cus_id_displayname" label="客户名称"/>
			<emp:select id="IqpCoreConNet.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpCoreConNet" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpCoreConNetList" pageMode="true" url="pageIqpCoreConNetAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:select id="app_type" label="申请类型" dictname="STD_ZB_NET_APP_TYPE"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:select id="flow_type" label="流程类型" hidden="true"/>
	    <emp:select id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    