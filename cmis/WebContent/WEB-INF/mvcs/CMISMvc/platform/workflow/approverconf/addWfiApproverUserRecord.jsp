<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	function getActor(data){
		WfiApproverUser.actorno._setValue(data.actorno._getValue());
		WfiApproverUser.actorname._setValue(data.actorname._getValue());
	}
	
	function doMysubmit() {
		if(!WfiApproverUser._checkAll()) {
			return;
		}
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 1) {
					alert('操作成功！');
					var url = '<emp:url action="queryWfiApproverUserList.do"/>?confid=${param.confid}';
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
		WfiApproverUser._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var url = '<emp:url action="addWfiApproverUserRecord.do" />';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addWfiApproverUserRecord.do" method="POST">
		
		<emp:gridLayout id="WfiApproverUserGroup" title="均衡用户" maxColumn="2">
			<emp:text id="WfiApproverUser.confid" label="配置ID" maxlength="40" required="true" defvalue="${param.confid }" hidden="true"/>
			<emp:pop id="WfiApproverUser.actorno" label="用户ID" url="querySUserPopList.do?returnMethod=getActor&selectType=1" required="true" />
			<emp:text id="WfiApproverUser.actorname" label="用户名称" maxlength="20" required="true" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="mysubmit" label="确定" op=""/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

