<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title></title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doLoad(){
		var url = '<emp:url action="getReportShowPage.do"/>?reportId=custrustee/cusorgapprove.raq&cus_id=${context.cus_id}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">

</body>
</html>
</emp:page>