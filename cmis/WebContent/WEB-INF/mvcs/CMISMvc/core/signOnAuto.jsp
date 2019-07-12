<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>

<title>操作返回页面</title>
<script type="text/javascript">
	
	function doOnLoad(){
		 var url = "<emp:url action='signOnAuto.do'/>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

</body>
</html>
