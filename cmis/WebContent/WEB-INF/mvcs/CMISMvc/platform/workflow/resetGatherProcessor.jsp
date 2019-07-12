<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIVO"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<title>重置会办参与人</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	WFIVO wfiVo = (WFIVO)context.getDataValue("WFIVO");
	int sign = wfiVo.getSign();
	String tip = wfiVo.getMessage();
%>


<script type="text/javascript">

	function doLoad(){
		var retObj = [];
		var sign = "<%=sign%>";
		var tip = "<%=tip%>";

		retObj[0] = sign;
		retObj[1] = tip;

		window.returnValue = retObj;
		window.close();
	}
</script>

</head>
<body onload="doLoad()">

</body>
</html>