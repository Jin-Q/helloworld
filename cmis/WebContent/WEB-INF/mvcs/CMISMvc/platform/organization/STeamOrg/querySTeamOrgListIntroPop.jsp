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
		SOrg._toForm(form);
		var queryCondition="${context.queryCondition}";
		
		var url = '<emp:url action="pageSTeamOrgIntroQuery.do"/>&returnMethod=getTeamOrg&team_no=${context.team_no}';
		//alert(queryCondition);
		url = EMPTools.encodeURI(url);
		
		SOrgList._obj.ajaxQuery(url,form);
	};
	function doReturnMethod(){
		var data = SOrgList._obj.getSelectedData();
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

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SOrg.organno" label="机构码" />
			<emp:text id="SOrg.organname" label="机构名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="引入"/>
	<emp:table icollName="SOrgList" pageMode="true" url="pageSTeamOrgIntroQuery.do"  reqParams="returnMethod=getTeamOrg&team_no=${context.team_no}" selectType="2" >
	    <emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" hidden="true"/>
		<emp:text id="organname" label="机构名称" />
		<emp:text id="distno" label="地区编号" />
		<emp:text id="fincode" label="金融代码" />
		<emp:text id="arti_organno" label="所属法人机构码" />
	</emp:table>
	<div align="left">
		<br>
		<emp:returnButton id="s2" label="引入"/>
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>