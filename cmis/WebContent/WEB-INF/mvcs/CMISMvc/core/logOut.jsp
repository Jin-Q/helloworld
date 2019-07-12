<%@page import="java.util.ResourceBundle"%>
<%
ResourceBundle res = ResourceBundle.getBundle("cmis");
session.invalidate();
%>
<script language='javascript'>
	var url = '<%=res.getString("LOG_CONVERT_PAGE")%>';
	window.location=url;
</script>