<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>共用refreshList</title>
<script src="<emp:file fileName='scripts/emp/pageUtil.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
  function refreshList(){
	  	  if(window.opener){
			var method="window.opener.<%=request.getParameter("method")%>";
			eval(method);
			window.close();
	  	  }else{
		  	var method="window.parent.<%=request.getParameter("method")%>";	
			eval(method);
	  	  }

  	
  }
</script>
</head>
<body  onload="refreshList();">
</body>
</html>
