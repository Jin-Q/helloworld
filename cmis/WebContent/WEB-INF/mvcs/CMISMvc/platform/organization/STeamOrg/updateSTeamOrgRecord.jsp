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
		STeamOrg._toForm(form);
		if(!STeamOrg._checkAll()){
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
			var team_org_id = STeamOrg.team_org_id._getValue();
			var url = '<emp:url action="updateSTeamOrgRecord.do"/>?team_org_id='+team_org_id;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);
		}
	}	
	function doReturn(){
		window.close();
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateSTeamOrgRecord.do" method="POST">
		<emp:gridLayout id="STeamOrgGroup" title="归属机构表" maxColumn="2">
			<emp:text id="STeamOrg.team_no" label="团队编号" maxlength="20" required="true" readonly="true"/>
			<emp:text id="STeamOrg.team_no_displayname" label="团队名称" maxlength="20" required="true" readonly="true"/>
			<emp:text id="STeamOrg.team_org_id" label="机构码" required="true" readonly="true" />
			<emp:text id="STeamOrg.team_org_id_displayname" label="机构名称"  required="false" cssElementClass="emp_field_text_readonly"/>
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
