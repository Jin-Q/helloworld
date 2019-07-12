<% 
String guaranty_no = (String)request.getParameter("guaranty_no");
%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>押品详细信息页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
</script>

</head>

<body  class="page_content">	
	
	<emp:tabGroup id="GuarantyBaseInfTabs" mainTab="GuarantyBaseInfo">
	<emp:tab label="基本信息" id="MortGuarantyBaseInfo" initial="true" needFlush="true" url="getGuarantyBaseInfoUpdatePage.do?guaranty_no='<%= guaranty_no%>'" >	
	</emp:tab >
	
	<emp:tab label="详细信息" id="MortGuarantyBaseInfoDetail" needFlush="true" initial="true" url="getDetailInformationPage.do?guaranty_no='<%= guaranty_no%>'" >	
	</emp:tab >
	
	<emp:tab label="权证信息" id="MortGuarantyCertiInfo" needFlush="true" url="queryMortGuarantyCertiInfoList.do?guaranty_no='<%= guaranty_no%>'">
	</emp:tab>
		
	<emp:tab label="押品价值信息" id="MortGuarantyValue" initial="false" needFlush="true" url="getZywjzxxPage.do?guaranty_no='<%= guaranty_no%>'">
	</emp:tab>
	
	<emp:tab label="保险信息" id="MortGuarantyInsurInfo" needFlush="true" url="queryMortGuarantyInsurInfoList.do?guaranty_no='<%= guaranty_no%>'">
	</emp:tab>
	
	<emp:tab label="意外情况" id="MortGuarantyDanger" needFlush="true" url="queryMortGuarantySuddenInfoList.do?guaranty_no='<%= guaranty_no%>'">
	</emp:tab>
	
	</emp:tabGroup >
	<div align="center">
			<br>
			<emp:button id="getGuaranty" label="保存并返回" locked="true"/>
	</div>
	<div align="center">
			<br>
			<emp:button id="closeWindow" label="关闭窗口" locked="true"/>
	</div>
		<div align="center">
				<br>
				<emp:button id="close" label="返回列表" locked="true"/>
		</div>
</body>
</html>
</emp:page>