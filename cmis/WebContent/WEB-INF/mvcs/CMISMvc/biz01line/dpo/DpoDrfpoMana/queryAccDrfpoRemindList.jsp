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
		AccDrfpoRemind._toForm(form);
		AccDrfpoRemindList._obj.ajaxQuery(null,form);
	};
	
	function doViewDpoDrfpoMana() {
		var paramStr = AccDrfpoRemindList._obj.getParamStr(['drfpo_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getDpoDrfpoManaViewPage.do"/>?op=view&oper=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.AccDrfpoRemindGroup.reset();
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccDrfpoRemindGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="AccDrfpoRemind.drfpo_no" label="池编号" />
		<emp:text id="AccDrfpoRemind.porder_no" label="汇票号码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<%--<emp:button id="viewDpoDrfpoMana" label="查看" op="view"/>--%>
	</div>

	<emp:table icollName="AccDrfpoRemindList" pageMode="true" url="pageAccDrfpoRemindQuery.do">
		<emp:link id="drfpo_no" label="池编号" operation="viewDpoDrfpoMana"/>
		<emp:text id="bill_type" label="票据类型" dictname="STD_DRFT_TYPE" />
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="bill_isse_date" label="出票日期" />
		<emp:text id="porder_end_date" label="到期日期" />
		<emp:text id="isse_name" label="出票人名称" />
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="aorg_name" label="承兑行名称" />
		<emp:text id="aorg_no" label="承兑行行号" />
		
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />		
		<emp:text id="manager_id" label="登记人" hidden="true"/>
		<emp:text id="manager_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    