<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SUser._toForm(form);
		var queryCondition="${context.queryCondition}";
		
		var url = '<emp:url action="pageSTeamUserIntroQuery.do"/>&returnMethod=getTeamUser&team_no=${context.team_no}';
		//alert(queryCondition);
		url = EMPTools.encodeURI(url);
		
		SUserList._obj.ajaxQuery(url,form);
	};
	function doReturnMethod(){
		var data = SUserList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data)");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doCancel(){
		window.close();
	};
	function doReset(){
		page.dataGroups.SUserGroup.reset();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SUserGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SUser.actorno" label="用户码" />
			<emp:text id="SUser.actorname" label="姓名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="引入"/>
	<emp:table icollName="SUserList" pageMode="true" url="pageSTeamUserIntroQuery.do"  reqParams="returnMethod=getTeamUser&team_no=${context.team_no}" selectType="2" >
	    <emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="orgid" label="所属机构" hidden="true" />
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
	</emp:table>
	<div align="left">
		<br>
		<emp:returnButton id="s2" label="引入"/>
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>