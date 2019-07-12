<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>璇︽儏鏌ヨ椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn(){
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="LmtIntbnkAppDetailsGroup" title="同业授信申请明细" maxColumn="2">
			<emp:text id="LmtIntbnkAppDetails.serno" label="业务流水号" maxlength="40" required="true" />
			<emp:text id="LmtIntbnkAppDetails.bank_no" label="同业机构行号" maxlength="30" required="false" />
			<emp:text id="LmtIntbnkAppDetails.bank_name" label="同业机构名称" maxlength="60" required="false" />
			<emp:text id="LmtIntbnkAppDetails.crd_item_id" label="授信明细" maxlength="30" required="true" />
			<emp:text id="LmtIntbnkAppDetails.cur_type" label="币种" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkAppDetails.crd_lmt" label="授信额度" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkAppDetails.margin_ratio" label="保证金比例" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkAppDetails.mini_rate" label="最低利率" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkAppDetails.mini_fare" label="最低费率" maxlength="16" required="false" />
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="鍏抽棴"/>
	</div>
</body>
</html>
</emp:page>
