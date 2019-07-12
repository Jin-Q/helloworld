<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>

<title>操作返回页面</title>
<script type="text/javascript">
	
	function doReturn(){
		window.close();
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content" >

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			
			<TABLE align="center" width="300px">
        		<TR aligh="center">
          			<TD>
          			<span class="page_welcome_red">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${context.message}
			</span><br><br><br>
          				<button onclick="doReturn(this)" >关闭</button>
          			</TD>
        		</TR>
        		<TR><TD>&nbsp;</TD></TR>
      		</TABLE>
		</div>
	</div>
	<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</body>
</html>
