<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>异常事项</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<script type="text/javascript">

	function doOnLoad() {
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiMsgQueueView._toForm(form);
		WfiMsgQueueViewList._obj.ajaxQuery(null,form);
	};
	
	function doSel(){
		var selObj = WfiMsgQueueViewList._obj.getSelectedData()[0];
		var nodeid=selObj.nodeid._getValue();
		var instanceid=selObj.instanceid._getValue();
		var wfsign=selObj.wfsign._getValue();
		var applType = selObj.appl_type._getValue();
		var url = "<emp:url action='getInstanceInfo.do'/>"+
			"?instanceId="+instanceid+"&applType="+applType+"&opType=1";
		url = EMPTools.encodeURI(url);
		window.location =url;
	};

	function doMyTrack() {
		if(WfiMsgQueueViewList._obj.getSelectedData().length == 0) {
			alert("请选择一条记录。");
			return;
		}
		var form = document.getElementById("form");
		var instanceid=WfiMsgQueueViewList._obj.getSelectedData()[0].instanceid._getValue();
		form.instanceid.value = instanceid;
		doTrack(form);
	};

	function doProcBiz() {
		var selObj = WfiMsgQueueViewList._obj.getSelectedData()[0];
		if(selObj == null) {
			alert('请选择一条记录！');
			return;
		}
		var msgid = selObj.msgid._getValue();
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var result = jsonstr.result;
				var flag = result.flag;
				var msg = result.msg;
				if(flag == '1') {
					alert(msg);
					window.location.reload();
				} else {
					alert('操作失败！' + msg);
				}
			}catch(e) {
				alert('操作异常！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = null;
		var url = '<emp:url action="procBizExcetion.do" />?msgid='+msgid+'&rd='+Math.random();
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback, postData);
	}
	
	function doReset() {
		page.dataGroups.WfiMsgQueueViewGroup.reset();
	}
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="WfiMsgQueueViewGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiMsgQueueView.pk_value" label="申请流水号" />
		<emp:select id="WfiMsgQueueView.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:button id="procBiz" label="业务处理" op="dobiz"/>
	<emp:table icollName="WfiMsgQueueViewList" pageMode="true" url="pageExceptionWorkQuery.do" >
		<emp:link id="appl_type" label="申请类型" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="pk_value" label="申请流水号" operation="sel"/>
		<emp:text id="user_id_displayname" label="办理人" />
		<emp:text id="org_id" label="办理机构" />
		<emp:select id="wfi_result" label="审批结论" dictname="WF_APP_RESULT"/>
		<emp:select id="opstatus" label="消息状态" dictname="WF_MSG_STATUS"/>
		<emp:text id="optime" label="处理时间" />
	    
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
	    <emp:text id="msgid" label="消息ID" hidden="true"/>
	    <emp:text id="nodeid" label="节点ID" hidden="true"/>
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
		<emp:text id="scene" label="场景" hidden="true"/>
		<emp:text id="table_name" label="业务表模型ID" hidden="true"/>
		<emp:text id="pk_col" label="业务主键字段名" hidden="true"/>
		<emp:text id="user_id" label="办理人" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>