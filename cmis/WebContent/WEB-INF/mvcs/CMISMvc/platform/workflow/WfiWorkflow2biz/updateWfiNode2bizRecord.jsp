<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

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
		var url = '<emp:url action="updateWfiNode2bizRecord.do" />';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	}
	function setNode(data) {
		WfiNode2biz.nodeid._setValue(data.nodeid._getValue());
		WfiNode2biz.nodename._setValue(data.nodename._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateWfiNode2bizRecord.do" method="POST">
		<emp:gridLayout id="WfiNode2bizGroup" maxColumn="2" title="流程节点关联业务配置">
			<emp:text id="WfiNode2biz.pk1" label="关联配置主键" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:pop id="WfiNode2biz.nodeid" label="节点ID" required="true" url="queryNodeByWfsignList.do?returnMethod=setNode" reqParams="wfsign=${context.wfsign }" />
			<emp:text id="WfiNode2biz.nodename" label="节点名称" maxlength="50" required="true" readonly="true"/>
			<emp:text id="WfiNode2biz.app_url" label="业务信息页面" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="WfiNode2biz.biz_url" label="业务要素修改页面" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submitAsyn" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
