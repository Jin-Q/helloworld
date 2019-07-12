<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryModifyHistoryList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
    
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >

	<emp:table icollName="ModifyHistoryDetailList" url="pageModifyHistoryViewQuery.do" pageMode="false">
	   <emp:text id="modify_name" label="修改字段"></emp:text>
	   <emp:text id="modify_old_value" label="修改前的值"></emp:text>
	   <emp:text id="modify_new_value" label="修改后的值"></emp:text>
	</emp:table>
	
</body>
</html>
</emp:page>
