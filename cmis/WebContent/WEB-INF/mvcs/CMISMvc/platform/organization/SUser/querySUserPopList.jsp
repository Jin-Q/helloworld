<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
	<%
		String selectType="1";
		try{
		    selectType= request.getParameter("selectType");
		}catch(Exception e){
		    
		}
	%>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SUser._toForm(form);
		var queryCondition="${context.queryCondition}";
		
		var url = '<emp:url action="pageSUserPopQuery.do"/>&queryCondition='+queryCondition;
		
		url = EMPTools.encodeURI(url);
		
		SUserList._obj.ajaxQuery(url,form);
	};
	function doSelect() {
		var data = SUserList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			<%if(selectType!=null && selectType.equals("1")){%>
				window.opener[methodName](data[0]);
			<%}else{%>
				window.opener[methodName](data);
			<%}%>
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
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
			<emp:text id="SUser.orgid" label="机构码" hidden="true"/>

	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:table icollName="SUserList" selectType="<%=selectType%>" pageMode="true" url="pageSUserQuery.do" reqParams="SUser.orgid=${context.SUser.orgid}">
		<emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
	</emp:table>
	<div align="left"><br>
	<emp:button id="select" label="确认" /> <br>
	</div>
</body>
</html>
</emp:page>
    