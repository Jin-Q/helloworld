<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String pk1 = request.getParameter("pk1");
String wfsign = request.getParameter("wfsign");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
context.put("pk1", pk1);
context.put("wfsign", wfsign);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:510px;
}

</style>

<script type="text/javascript">

	/*--user code begin--*/
	
	window.onload = function() {
	}
			
	function setNode(data) {
		WfiNode2biz.nodeid._setValue(data.nodeid._getValue());
		WfiNode2biz.nodename._setValue(data.nodename._getValue());
	}
	
	function doSubmitAsyn() {
		var appUrl = WfiNode2biz.app_url._getValue();
		var bizUrl = WfiNode2biz.biz_url._getValue();
		if(appUrl=='' && bizUrl=='') {
			alert('[业务信息页面]与[业务要素修改页面]，不能同时为空！');
			return;
		}
		
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 1) {
					alert('操作成功！');
					var url = '<emp:url action="queryWfiNode2bizList.do"/>?pk1=${context.pk1}&wfsign=${context.wfsign}';
					url = EMPTools.encodeURI(url);
					window.location = url;
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
		WfiNode2biz._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var url = '<emp:url action="addWfiNode2bizRecord.do" />';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addWfiNode2bizRecord.do" method="POST">
		
		<emp:gridLayout id="WfiNode2bizGroup" title="流程节点关联业务配置" maxColumn="2">
			<emp:text id="WfiNode2biz.pk1" label="关联配置主键" maxlength="40" required="true" readonly="true" defvalue="${context.pk1 }" colSpan="2"/>
			<emp:pop id="WfiNode2biz.nodeid" label="节点ID" required="true" url="queryNodeByWfsignList.do?returnMethod=setNode" reqParams="wfsign=${context.wfsign }" returnMethod="setNode"/>
			<emp:text id="WfiNode2biz.nodename" label="节点名称" maxlength="50" required="true" readonly="true"/>
			<emp:text id="WfiNode2biz.app_url" label="业务信息页面" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiNode2biz.biz_url" label="业务要素修改页面" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submitAsyn" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

