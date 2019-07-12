<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>

<emp:page>
<html>
<head>
<title>会办待办事项</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfGatherRecordend._toForm(form);
		WfGatherRecordendList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = WfGatherRecordendList._obj.getSelectedData()[0];
		var mainnodeid=selObj.mainnodeid._getValue();
		var instanceid=selObj.instanceid._getValue();
		var maininstanceid=selObj.maininstanceid._getValue();
		var url = "<emp:url action='getGatherInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&mainNodeId="+mainnodeid+"&mainInstandeId="+maininstanceid+"&editFlag=2&rd="+Math.random();
		url = EMPTools.encodeURI(url);
		var retValue= window.showModalDialog(url,'gather','dialogHeight:600px;dialogWidth:900px;help:no;resizable:no;status:no;');
		if(retValue!=null&&retValue=="resh")
		   location.reload();
	};
	
	function doReset() {
		page.dataGroups.WfGatherRecordendGroup.reset();
	}
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="WfGatherRecordendGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfGatherRecordend.bizseqno" label="申请流水号" />
		<emp:text id="WfGatherRecordend.gathertitle" label="会办主题" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="WfGatherRecordendList" pageMode="true" url="pageEndGatherQuery.do" >
		<emp:link id="bizseqno" label="申请流水号" operation="sel"/>
		<emp:link id="gathertitle" label="会办主题" operation="sel"/>
		<emp:link id="gatherstartusername" label="会办发起人" operation="sel"/>
		<emp:link id="allprocessorname" label="会办参与人" operation="sel"/>
		<emp:link id="gatherendusername" label="会办汇总人" operation="sel"/>
		<emp:link id="gatherstarttime" label="发起时间" operation="sel"/>
	    <emp:text id="instanceid" label="会办实例号" hidden="true"/>
	    <emp:text id="mainnodeid" label="流程节点ID" hidden="true"/>
		<emp:text id="maininstanceid" label="流程实例号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>