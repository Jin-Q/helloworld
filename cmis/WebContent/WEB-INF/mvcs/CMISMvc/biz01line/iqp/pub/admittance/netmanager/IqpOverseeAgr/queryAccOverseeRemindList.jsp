<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.yucheng.cmis.pub.PUBConstant"%>

<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccOverseeRemind._toForm(form);
		AccOverseeRemindList._obj.ajaxQuery(null,form);
	};
	
	function doGetDJBC() {
		var paramStr = AccOverseeRemindList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryMortInfo4AccOverseeRemindList.do"/>?&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doGetTJZCBQ() {
		var paramStr = AccOverseeRemindList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccOverseeRemindUpdatePage.do"/>?&'+paramStr+'&remind_flag=${context.remind_flag}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.AccOverseeRemindGroup.reset();
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccOverseeRemindGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="AccOverseeRemind.oversee_agr_no" label="监管协议号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    
		<emp:button id="getDJBC" label="跌价补偿" op="djbc"/>
		<%--<emp:button id="getTJZCBQ" label="提交资产保全" op="tjzcbq"/>--%>
	</div>

	<emp:table icollName="AccOverseeRemindList" pageMode="true" url="pageAccOverseeRemindQuery.do?remind_flag=${context.remind_flag}">
		<emp:text id="mortgagor_id" label="客户码" />
		<emp:text id="mortgagor_id_displayname" label="客户名称" />
		<emp:text id="oversee_agr_no" label="监管协议号" />
		<emp:text id="identy_total" label="货物价值" dataType="Currency"/>
		<emp:text id="risk_open_amt" label="敞口金额" dataType="Currency"/>
		<% if(request.getParameter("remind_flag").equals("1")){ %>
		<emp:text id="vigi_line" label="补仓线(质押率)" />
		<%}else{ %>
		<emp:text id="stor_line" label="平仓线(质押率)" />
		<%} %>
		<emp:text id="mort_rate" label="当前质押率" />
		<emp:text id="gap_amt" label="缺口金额" dataType="Currency"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    