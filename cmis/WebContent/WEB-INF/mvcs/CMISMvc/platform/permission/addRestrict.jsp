<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	String resourceid = request.getParameter("resourceid");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">
	function doAdd(){
		if(!SRowright._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		SRowright._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var resourceid = jsonstr.resourceid;
				if(flag == "success"){
					var url = '<emp:url action="queryRestrictList.do"/>?resourceid='+resourceid;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("新增失败！");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		var url = '<emp:url action="addRestrict.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
</script>
</head>
<body  class="page_content">
	<emp:form id="submitForm" action="addRestrict.do" method="POST">
		<emp:gridLayout id="SRowrightGroup" maxColumn="1" title="记录集权限配置信息">
			<emp:text id="SRowright.pk1" label="主键" maxlength="32" hidden="true" required="false" readonly="true" />
			<emp:text id="SRowright.resourceid" label="资源ID" maxlength="32" defvalue="<%=resourceid %>" required="true" readonly="true" />
			<emp:text id="SRowright.cnname" label="资源名称" maxlength="60" required="false" />
			<emp:textarea id="SRowright.readtemp" label="读权限配置" maxlength="200" required="false" />
			<emp:textarea id="SRowright.writetemp" label="写权限配置" maxlength="200" required="false" />
			<emp:textarea id="SRowright.memo" label="描述" maxlength="40" required="false" />
		</emp:gridLayout>
		<div align="center">
				<br>
			<emp:button id="add" label="确定"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

