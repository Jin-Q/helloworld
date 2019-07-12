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
		ArpCollDebtAcc._toForm(form);
		ArpCollDebtAccList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpCollDebtAccPage() {
		var status = ArpCollDebtAccList._obj.getParamValue(['status']);
		var paramStr = ArpCollDebtAccList._obj.getParamStr(['debt_acc_no']);
		if (paramStr != null) {
			if(status=="01"){
				alert("已执行的记录不能重复进行抵入操作！");
				return;
			}
			var url = '<emp:url action="getArpCollDebtAccUpdatePage.do"/>?op=debt_in&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpCollDebtAcc() {
		var paramStr = ArpCollDebtAccList._obj.getParamStr(['debt_acc_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDebtAccViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpCollDebtAccPage() {
		var url = '<emp:url action="getArpCollDebtAccAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpCollDebtAcc() {
		var paramStr = ArpCollDebtAccList._obj.getParamStr(['debt_acc_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpCollDebtAccRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpCollDebtAccGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpCollDebtAccGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpCollDebtAcc.cus_id" label="客户码" />
			<emp:select id="ArpCollDebtAcc.debt_mode" label="抵债方式" dictname="STD_ZB_DEBT_MODEL" />
			<emp:select id="ArpCollDebtAcc.status" label="状态" dictname="STD_ZB_COLL_ACC_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewArpCollDebtAcc" label="查看" op="view"/>
		<emp:button id="getUpdateArpCollDebtAccPage" label="抵入" op="debt_in"/>
	</div>

	<emp:table icollName="ArpCollDebtAccList" pageMode="true" url="pageArpCollDebtAccQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="debt_acc_no" label="抵债台账编号" />
		<emp:text id="debt_mode" label="抵债方式" dictname="STD_ZB_DEBT_MODEL" />
		<emp:text id="debt_in_amt" label="抵入金额" dataType="Currency"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="end_date" label="办结日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_COLL_ACC_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    