<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<html>
<head>
<%
java.lang.String url=request.getParameter("url");
%>
<title>操作返回页面</title>
<script type="text/javascript">
	
	function doOnLoad(){
		var action = '<%=url%>';
		var url = '<emp:url action="' + action + '"/>';
		
		url = EMPTools.encodeURI(url);
		var link = document.getElementById("returnlink");
		link.href = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;规则分析完成，操作已成功！
			</span><br>
			<TABLE align="center" width="300px">
        		<TR aligh="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green"><a id="returnlink" href="">查看任务列表页面</a></font></span>
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
