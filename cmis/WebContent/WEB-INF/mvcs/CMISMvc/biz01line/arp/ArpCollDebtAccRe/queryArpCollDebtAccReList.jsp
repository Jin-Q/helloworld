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
		ArpCollDebtAccRe._toForm(form);
		ArpCollDebtAccReList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpCollDebtAccRePage() {
		var paramStr = ArpCollDebtAccReList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpCollDebtAccReUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpCollDebtAccRe() {
		var paramStr = ArpCollDebtAccReList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doGetAddArpCollDebtAccRePage() {
		var url = '<emp:url action="getArpCollDebtAccReAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpCollDebtAccRe() {
		var paramStr = ArpCollDebtAccReList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteArpCollDebtAccReRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpCollDebtAccReGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddArpCollDebtAccRePage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpCollDebtAccRePage" label="修改" op="update"/>
		<emp:actButton id="deleteArpCollDebtAccRe" label="删除" op="remove"/>
		<emp:actButton id="viewArpCollDebtAccRe" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpCollDebtAccReList" pageMode="true" url="pageArpCollDebtAccReQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="debt_acc_no" label="抵债台账编号" hidden="true" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="guaranty_type_displayname" label="押品类型" />
		<emp:text id="debt_in_amt" label="抵入金额" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_DEBT_RE_STATUS" />
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    