<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	

	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:form id="submitform" action='.do' method="POST">	</emp:form>			
	<emp:table icollName="LmtSubAppList" pageMode="false" url="pageLmtSubAppDetailQuery.do" reqParams="LmtSigLmt.serno=${context.LmtSigLmt.serno}">
		<emp:text id="serno" label="业务编号"/>
		<emp:text id="variet_no" label="品种编号" />
		<emp:text id="variet_name" label="品种名称" />
		<emp:text id="lmt_amt" label="授信额度"  dataType="Currency" />
	</emp:table>
</body>
</html>
</emp:page>
