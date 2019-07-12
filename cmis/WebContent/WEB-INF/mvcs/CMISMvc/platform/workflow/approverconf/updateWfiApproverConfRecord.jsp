<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	
	window.onload = function() {
		changeApplType(false);
		changeConfType(false);
		var url = '<emp:url action="queryNodeByWfsignList.do"/>?returnMethod=setNode&wfsign='+WfiApproverConf.wfsign._getValue();
		WfiApproverConf.nodeid._obj.config.url=EMPTools.encodeURI(url);
	}
	
	var changeConfType = function(flag) {
		var confType = WfiApproverConf.conf_type._getValue();
		if(confType == '3') {
			WfiApproverConf.orgid._obj._renderRequired(true);
			WfiApproverConf.orgid._obj._renderHidden(false);
			WfiApproverConf.orgid_displayname._obj._renderHidden(false);
		} else {
			WfiApproverConf.orgid._obj._renderRequired(false);
			WfiApproverConf.orgid._obj._renderHidden(true);
			WfiApproverConf.orgid_displayname._obj._renderHidden(true);
		}
	}
	
	function getWfsign(data) {
		var oldWfsign = WfiApproverConf.wfsign._getValue();
		WfiApproverConf.wfsign._setValue(data.wfsign._getValue());
		WfiApproverConf.wfname._setValue(data.wfname._getValue());
		if(oldWfsign != data.wfsign._getValue()) {
			//设置节点
			var url = '<emp:url action="queryNodeByWfsignList.do"/>?returnMethod=setNode&wfsign='+data.wfsign._getValue();
			WfiApproverConf.nodeid._obj.config.url=EMPTools.encodeURI(url);
			WfiApproverConf.nodeid._obj._renderReadonly(false);
			WfiApproverConf.nodeid._setValue('');
			WfiApproverConf.nodename._setValue('');
		}
	}
	
	var changeApplType = function(flag) {
		var applType = WfiApproverConf.appl_type._getValue();
		var url = '<emp:url action="queryWorkflow2bizByApplType.do"/>?returnMethod=getWfsign';
		url = EMPTools.encodeURI(url);
		if(applType==null || applType=='') {
			WfiApproverConf.wfsign._obj._renderReadonly(true);
		} else {
			WfiApproverConf.wfsign._obj._renderReadonly(false);
			WfiApproverConf.wfsign._obj.config.url=url+"&appl_type="+applType;
		}
		if(flag) { //非打开页面时执行
			WfiApproverConf.wfsign._setValue('');
			WfiApproverConf.wfname._setValue('');
			WfiApproverConf.nodeid._setValue('');
			WfiApproverConf.nodename._setValue('');
		}
	}
	
	function setNode(data) {
		WfiApproverConf.nodeid._setValue(data.nodeid._getValue());
		WfiApproverConf.nodename._setValue(data.nodename._getValue());
	}
	
	function doMysubmit() {
		if(!WfiApproverConf._checkAll()) {
			return;
		}
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag==1) {
					alert('操作成功！');
				} else {
					alert('操作失败！'+flag);
				}
			}catch(e) {
				alert(o.responseText);
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
		var form = document.getElementById("submitForm");
		WfiApproverConf._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var url = '<emp:url action="updateWfiApproverConfRecord.do" />';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateWfiApproverConfRecord.do" method="POST">
		<emp:gridLayout id="WfiApproverConfGroup" title="均衡审批配置" maxColumn="2">
			<emp:text id="WfiApproverConf.confid" label="配置ID" maxlength="40" required="false" hidden="true"/>
			<emp:select id="WfiApproverConf.conf_type" label="筛选类型" required="true" dictname="STD_ZB_WFI_CONF_TYPE" onchange="changeConfType()"/>
			<emp:select id="WfiApproverConf.appl_type" label="流程申请类型" required="true" dictname="ZB_BIZ_CATE" colSpan="2" onchange="changeApplType()"/>
			<emp:pop id="WfiApproverConf.wfsign" label="流程标识" url="queryWorkflow2bizByApplType.do?returnMethod=getWfsign" returnMethod="getWfsign" required="true" readonly="true"/>
			<emp:text id="WfiApproverConf.wfname" label="流程名称" maxlength="50" required="true" readonly="true"/>
			<emp:pop id="WfiApproverConf.nodeid" label="节点ID" required="true" url="queryNodeByWfsignList.do?returnMethod=setNode" reqParams="wfsign=${context.wfsign }" returnMethod="setNode" readonly=""/>
			<emp:text id="WfiApproverConf.nodename" label="节点名称" maxlength="50" required="true" readonly="true"/>
			<emp:pop id="WfiApproverConf.orgid" label="业务管理机构" returnMethod="getOrgID" required="false" hidden="true" url="querySOrgPop.do?restrictUsed=false"/>
			<emp:text id="WfiApproverConf.orgid_displayname" label="业务管理机构名称"  required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<emp:button id="mysubmit" label="修改" op=""/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
	<emp:tabGroup mainTab="usersTab" id="theTabGroup">
		<emp:tab label="用户列表" id="usersTab" url="queryWfiApproverUserList.do?confid=${context.WfiApproverConf.confid }" needFlush="true"/>
	
	</emp:tabGroup>
	
</body>
</html>
</emp:page>
