<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title></title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
  function refreshList(){
  	  if(window.opener){
  		data = "${context.errorInfo}";
  		flag = "${context.flag}";
		var method="window.opener.parent.<%=request.getParameter("returnMethod")%>";
  		eval(method+"(data,flag)");
		window.close();
  	  }else{
	  	var method="window.parent.<%=request.getParameter("returnMethod")%>";	
		eval(method);
  	  }
  }
</script>

</head>
<body onload="refreshList();">
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</body>
</html>
</emp:page>