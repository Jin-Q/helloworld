<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>操作返回页面</title>
<script type="text/javascript">
	
	function doOnLoad(){
		//ymPrompt.alert({message:'${context.message}',title:'系统提示',handler:handler});
		alert('${context.message}'); 
		var url = '<emp:url action="queryPubDocumentInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
		
	};

</script>

<jsp:include page="/include.jsp" />

</head>

<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>

</body>
</html>
