<%@page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>共用refreshList</title>
<script type="text/javascript">
  function refreshList(){
  	  if(window.opener){
		var method="window.opener.parent.<%=request.getParameter

("method")%>";
		
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
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</body>
</html>
