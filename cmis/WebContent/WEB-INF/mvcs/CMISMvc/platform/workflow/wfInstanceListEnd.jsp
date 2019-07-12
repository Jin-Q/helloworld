<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>实例管理（办结）</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiWorkList._toForm(form);
		WfiWorkListList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = WfiWorkListList._obj.getSelectedData()[0];
		var instanceid=selObj.instanceid._getValue();
		var wfsign=selObj.wfsign._getValue();
		var applType = selObj.appl_type._getValue();
		var url = "<emp:url action='getInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&mng=2&applType="+applType;
		url = EMPTools.encodeURI(url);
		window.location =url;
	};

	function doMyTrack() {
		if(WfiWorkListList._obj.getSelectedData().length == 0) {
			alert("请选择一条记录。");
			return;
		}
		var form = document.getElementById("form");
		var instanceid=WfiWorkListList._obj.getSelectedData()[0].instanceid._getValue();
		form.instanceid.value = instanceid;
		doTrack(form);
	};

	function doReset() {
		page.dataGroups.WfiWorkListGroup.reset();
	}
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="WfiWorkListGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiWorkList.pk_value" label="申请流水号" />
		<emp:select id="WfiWorkList.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="WfiWorkList.cus_name" label="客户名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="WfiWorkListList" pageMode="true" url="pageWfInstanceQuery.do" reqParams="flag=${context.flag }">
		<emp:link id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="pk_value" label="申请流水号" operation="sel"/>
		<emp:link id="cus_name" label="客户名称" operation="sel"/>
		<emp:link id="wfstarttime" label="流程开始时间" operation="sel"/>
		<emp:link id="wfendtime" label="流程结束时间" operation="sel"/>
		<emp:link id="costtimes" label="花费时间" operation="sel"/>
		<emp:link id="lastuser" label="最后办理人" operation="sel"/>
	    <emp:link id="wfjobname" label="流程名称" operation="sel"/>
	    <emp:link id="wfi_status" label="审批状态" operation="sel" dictname="WF_APP_STATUS"/>
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>