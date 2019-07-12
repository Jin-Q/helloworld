<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<html>
<head>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String errorFlag = (String)context.getDataValue("errorInfo");
%>

<%
String url=request.getParameter("url");
%>
<title>操作返回页面</title>
<script type="text/javascript">

	function doOnLoad(){
		var errorInfo = '${context.errorInfo}';
		if(errorInfo=='errorInfo'){
			alert('${context.errorMsg}');
			window.close();
			window.opener.location.reload();
		}else if(errorInfo=='errorInfo1'){
			alert('导入失败');
			window.close();
			window.opener.location.reload();
		}else if(errorInfo=='errorInfo2'){
			alert('客户存在跟批量包信息不一致的客户，只导入相同等级客户！');
			window.close();
			window.opener.location.reload();
		}else if(errorInfo=='errorInfo3'){
			alert('${context.errorMsg}');
			window.close();
			window.opener.location.reload();
		}else{
			var serno = '${context.serno}';	
	        var paramStr = "serno="+serno;
			var url = '<emp:url action="getLmtIntbankBatchListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var link = document.getElementById("returnlink");				
			window.close();
			window.opener.location.reload();	
			link.href = url;
		}
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
			<% 
				if("successInfo".equals(errorFlag)){
			%>	
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作成功！
			<%	
				}else{
			%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;导入失败！
				
			<%} %>
			</span><br>
			<TABLE align="center" width="300px">
        		<TR align="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green"><a id="returnlink" href="">回到列表页面</a></font></span>
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
