<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>

<title>跳转到修改界面</title>
<script type="text/javascript">
	
	function doOnLoad(){
		var url = '<emp:url action="queryGrtGuaranteeList.do"/>&guar_cont_no=${context.guar_cont_no}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">
</body>
</html>
