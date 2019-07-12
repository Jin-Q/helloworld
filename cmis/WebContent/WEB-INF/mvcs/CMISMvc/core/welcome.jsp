<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
/*function doLogin(){
	UserLoginList._obj.selectAll();
	var data = UserLoginList._obj.getSelectedData();
	if(data == null || data == ""){
		alert("请先选择一条登录信息！");
		return;
	}
	var num = 0;
	var isSelect = false;
	
	for(var i=0;i<data.length;i++){
		var isChecked = data[i].isChecked._getValue();
		if(isChecked == 1){
			isSelect = true;
			num = i;
		}
	}
	if(!isSelect){
		alert("请先选择登录人员！");
		return;
	}
	
	var orgid = data[num].orgNo._getValue();
	if(orgid == null || orgid == "" || orgid == "undefined"){
		alert("请先选择当前登录人员所属机构！");
		return;
	}
	var url = '<emp:url action="userSignOnLogin.do"/>?orgNo='+orgid;
	url = EMPTools.encodeURI(url);
	window.location = url;
};	*/	

	function doLogin(){
		if(UserLogList._checkAll()){
			var orgid = UserLogList.orgNo._getValue();
			var url = '<emp:url action="userSignOnLogin.do"/>?orgNo='+orgid;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	};

function doBackLogin(){
	var url = '<emp:url action="signOn.do"/>';
	url = EMPTools.encodeURI(url);
	window.location = url;
};
	
</script>
</head>
<body class="page_content QZ_loginBpdy">
	<!-- <div style="padding-top: 200px;width: 60%;padding-left: 200px;"> -->
	<div id="QZ_loginBox">
		<emp:form id="submitForm" action="#" method="POST">
			<emp:gridLayout id="UserLoginGroup" title="用户登录：" maxColumn="1">
				<emp:radio id="UserLogList.isChecked" label="选中状态：" flat="false" dictname="STD_ZB_CHECK_STATE" defvalue="1"/>
				<emp:text id="UserLogList.userId" label="用户ID：" readonly="true"/>
				<emp:text id="UserLogList.userName" label="登录用户：" readonly="true"/>
				<emp:select id="UserLogList.orgNo" label="登录机构：" dictname="STD_ORG_HELP_TYPE" required="true" />
				<emp:text id="UserLogList.orgName" hidden="true" label="机构名称:" />
			</emp:gridLayout>
		</emp:form>
		
		<div align="center">
			<emp:button id="login" label="登录" />
			<emp:button id="backLogin" label="返回首页"/>
		</div>
	</div>
</body>
</html>
</emp:page> 