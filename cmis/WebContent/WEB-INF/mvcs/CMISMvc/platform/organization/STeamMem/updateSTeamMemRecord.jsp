<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doNext(){
		var form = document.getElementById('submitForm');
		STeamMem._toForm(form);
		if(!STeamMem._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						alert("修改成功！");
						window.opener.location.reload();
						window.close();
					}else{
						alert("修改失败！");
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var mem_no = STeamMem.mem_no._getValue();
			var url = '<emp:url action="updateSTeamMemRecord.do"/>?mem_no='+mem_no;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);
		}
	}	
	function doReturn(){
		window.close();
	};
	function setconId(data){
		STeamMem.mem_no._setValue(data.actorno._getValue());
		STeamMem.mem_no_displayname._setValue(data.actorname._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSTeamMemRecord.do" method="POST">
		<emp:gridLayout id="STeamMemGroup" title="团队成员表" maxColumn="2">
			<emp:text id="STeamMem.team_no" label="团队编号" maxlength="20" required="true" readonly="true"/>
			<emp:select id="STeamMem.team_role" label="团队角色" required="true" dictname="STD_TEAM_ROLE"/>
			<emp:text id="STeamMem.mem_no" label="成员编号" required="true" colSpan="2" readonly="true" />
			<emp:text id="STeamMem.mem_no_displayname" label="成员名称"  required="false" cssElementClass="emp_field_text_readonly"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="修改" op="update"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
