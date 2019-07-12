<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>testOp</title>
<jsp:include page="/include.jsp" />
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp/rpt.js'/>" type="text/javascript" language="javascript"></script>

<script type="text/javascript">
    function doCheckButton(){
    	var stockURL = '<emp:url action="testOp.do"/>';
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
    }
</script>
</head>
<body>
	<div align="center">
		<br>
		<emp:button id="checkButton" label="查看财报样式加载情况"/>&nbsp;&nbsp;
	</div>
</body>
		

</html>
</emp:page>

