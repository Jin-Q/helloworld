<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>办结事项</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiWorklistEnd._toForm(form);
		WfiWorklistEndList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = WfiWorklistEndList._obj.getSelectedData()[0];
		var instanceid=selObj.instanceid._getValue();
		var wfsign=selObj.wfsign._getValue();
		var applType = selObj.appl_type._getValue();
		var url = "<emp:url action='getInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&applType="+applType;
		url = EMPTools.encodeURI(url);
		window.location =url;
	};

	function doMyTrack() {
		if(WfiWorklistEndList._obj.getSelectedData().length == 0) {
			alert("请选择一条记录。");
			return;
		}
		var form = document.getElementById("form");
		var instanceid=WfiWorklistEndList._obj.getSelectedData()[0].instanceid._getValue();
		form.instanceid.value = instanceid;
		doTrack(form);
	};

	function doReset() {
		page.dataGroups.WfiWorklistEndGroup.reset();
	}
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="WfiWorklistEndGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="WfiWorklistEnd.pk_value" label="业务流水号" />
		<emp:select id="WfiWorklistEnd.appl_type" label="业务类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="WfiWorklistEnd.cus_name" label="客户名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="WfiWorklistEndList" pageMode="true" url="pageEndWorkQuery.do" >
		<emp:link id="appl_type" label="业务类型" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="pk_value" label="业务流水号" operation="sel"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称/项目名称" />
		<emp:text id="prd_name" label="产品名称" />
		<emp:text id="amt" label="申请金额" dataType="Currency" />
		<emp:text id="wfstarttime" label="业务提交时间" />
		<emp:text id="wfendtime" label="审批结束时间" />
		<emp:text id="costtimes" label="花费时间" />
		<emp:text id="lastuser_displayname" label="最后办理人" />
	    <emp:text id="wfjobname" label="流程名称"  hidden="true"/>
	    <emp:text id="wfi_status" label="审批状态" dictname="WF_APP_STATUS" hidden="true"/>
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
		<emp:text id="lastuser" label="最后办理人" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>