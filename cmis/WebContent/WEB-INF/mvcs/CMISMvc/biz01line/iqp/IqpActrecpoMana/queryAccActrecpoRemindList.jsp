<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccActrecpoRemind._toForm(form);
		AccActrecpoRemindList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpActrecpoMana() {
		var paramStr = AccActrecpoRemindList._obj.getParamStr(['po_no']);
		if (paramStr != null) {
			var po_type = AccActrecpoRemindList._obj.getParamValue('po_type');
			var url = '<emp:url action="getIqpActrecpoManaTabHelp.do"/>?'+paramStr+'&type=view&PO_TYPE='+po_type;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccActrecpoRemindGroup.reset();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccActrecpoRemindGroup" title="输入查询条件" maxColumn="3">		
		<emp:text id="AccActrecpoRemind.po_no" label="池编号" />
		<emp:text id="AccActrecpoRemind.invc_no" label="发票号" />
		<emp:select id="AccActrecpoRemind.po_type" label="池类型" dictname="STD_ACTRECPO_TYPE"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<%--<emp:button id="viewIqpActrecpoMana" label="查看" op="view"/> --%>
	</div>
	<emp:table icollName="AccActrecpoRemindList" pageMode="true" url="pageAccActrecpoRemindQuery.do">
		<emp:link id="po_no" label="池编号" operation="viewIqpActrecpoMana"/>
		<emp:text id="po_type" label="池类型" dictname="STD_ACTRECPO_TYPE"/>
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="bond_pay_date" label="到期日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />	
		<emp:text id="manager_id" label="登记人" hidden="true"/>
		<emp:text id="manager_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    