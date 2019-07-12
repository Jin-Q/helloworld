<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>授信流程进度（未办结）</title>
<jsp:include page="/include.jsp" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
		/**add by lisj 2014年11月17日 上线需求变更：授信审批流程进度查询 begin**/
		var options = CreditScheduleInfo.appl_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			//判断申请类型，去掉非授信申请类型的选项
			if(options[i].value != "003" && options[i].value != "0061" &&
				options[i].value != "0062" && options[i].value != "015" &&
				options[i].value != "020" && options[i].value != "3221" &&
				options[i].value != "3231" && options[i].value != "3241" &&
				options[i].value != "3251" && options[i].value != "3281" &&
				options[i].value != "371" && options[i].value != "372" && 
				options[i].value != "374" && options[i].value != ""){
					options.remove(i);
			}
		}
		/**add by lisj 2014年11月17日 上线需求变更：授信审批流程进度查询 end**/	 
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		CreditScheduleInfo._toForm(form);
		CreditScheduleList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = CreditScheduleList._obj.getSelectedData()[0];
		var instanceid=selObj.instanceid._getValue();
		var wfsign=selObj.wfsign._getValue();
		var applType = selObj.appl_type._getValue();
		/* added by yangzy 2014/11/25 管理员无条件打回 start */
		var url = "<emp:url action='getInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&mng=3&applType="+applType;
		/* added by yangzy 2014/11/25 管理员无条件打回 end */
		url = EMPTools.encodeURI(url);
		window.location =url;
	};

	function doMyTrack() {
		if(CreditScheduleList._obj.getSelectedData().length == 0) {
			alert("请选择一条记录。");
			return;
		}
		var form = document.getElementById("form");
		var instanceid=CreditScheduleList._obj.getSelectedData()[0].instanceid._getValue();
		form.instanceid.value = instanceid;
		doTrack(form);
	};

	function doReset() {
		page.dataGroups.CreditScheduleInfoGroup.reset();
	}
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="CreditScheduleInfoGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="CreditScheduleInfo.pk_value" label="申请流水号" />
		<emp:select id="CreditScheduleInfo.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" defvalue=""/>
		<emp:text id="CreditScheduleInfo.cus_name" label="客户名称" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
		<emp:button id="myTrack" label="跟踪" />
		
	<emp:table icollName="CreditScheduleList" pageMode="true" url="pageGetCreditScheduleQuery.do" reqParams="flag=${context.flag }">
		
		<emp:link id="pk_value" label="申请流水号" operation="sel"/>
		<emp:link id="cus_name" label="客户名称" operation="sel"/>
		<emp:link id="amt" label="授信金额" operation="sel" dataType="Currency"/>
		<emp:link id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" operation="sel"/>
		<emp:link id="appl_type" label="授信品种" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="prenodename" label="前一节点" operation="sel" hidden="true"/>
		<emp:link id="nodename" label="当前节点" operation="sel" hidden="true"/>
		<emp:link id="author_displayname" label="经办人员" operation="sel" />
		<emp:link id="orgid_displayname" label="经办机构" operation="sel" />
	    <emp:link id="wfname" label="流程名称" operation="sel" hidden="true"/>
	    <emp:link id="wfi_status" label="业务状态" operation="sel" dictname="WF_APP_STATUS"/>
	    <emp:link id="currentnodeuser_displayname" label="审批人员" operation="sel"/>
		<emp:link id="nodestatus" label="节点状态" dictname="WF_NODE_STATUS" operation="sel" hidden="true"/>
		<emp:link id="nodestarttime" label="流程开始时间" operation="sel"/>
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