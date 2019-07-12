<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>抄送已办结事项</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

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
		var url = "<emp:url action='getInstanceInfo.do'/>?instanceId="+instanceid+"&mng=4&applType="+applType;
		url = EMPTools.encodeURI(url);
		window.location =url;
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
		<emp:text id="WfiWorklistEnd.pk_value" label="申请流水号" />
		<emp:select id="WfiWorklistEnd.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
		<emp:text id="WfiWorklistEnd.cus_name" label="客户名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="WfiWorklistEndList" pageMode="true" url="pageAnnounceEndListQuery.do">
		<emp:link id="appl_type" label="申请类型" operation="sel" dictname="ZB_BIZ_CATE"/>
		<emp:link id="pk_value" label="申请流水号" operation="sel"/>
		<emp:link id="cus_name" label="客户名称" operation="sel"/>
		<emp:link id="amt" label="授信金额" operation="sel" dataType="Currency"/>
		<emp:link id="appl_type" label="授信品种" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="prenodename" label="前一节点" operation="sel" hidden="true"/>
		<emp:link id="nodename" label="当前节点" operation="sel" hidden="true"/>
		<emp:link id="author_displayname" label="经办人员" operation="sel" />
		<emp:link id="orgid_displayname" label="经办机构" operation="sel" />
	    <emp:link id="wfname" label="流程名称" operation="sel" hidden="true"/>
	    <emp:link id="wfi_status" label="业务状态" operation="sel" dictname="WF_APP_STATUS"/>
	    <emp:link id="wfstarttime" label="流程开始时间" operation="sel" />
	    <emp:link id="wfendtime" label="流程结束时间" operation="sel" />
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
	    <emp:text id="prenodeid" label="前一节点ID" hidden="true"/>
	    <emp:text id="nodeid" label="节点ID" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
	</emp:table>
	<form id="form" action="">
		<input id="instanceid" name="instanceid" value="" type="hidden" />
		<input id="currentuserid" name="currentuserid" value="${context.currentUserId}" type="hidden" />
		<input id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}" type="hidden" />
	</form>
	
</body>
</html>
</emp:page>