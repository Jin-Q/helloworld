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
	
	function doReturn() {
		var url = '<emp:url action="queryAccpDetailList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="AccpDetailGroup" title="承兑汇票申请明细" maxColumn="2">
			<emp:text id="AccpDetail.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="AccpDetail.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccpDetail.pyee" label="收款人" maxlength="20" required="false" />
			<emp:text id="AccpDetail.pyee _acct_no" label="收款人账号" maxlength="40" required="false" />
			<emp:pop id="AccpDetail.pyee_acctsvcr_no" label="收款人开户行行号" url="null" required="false" />
			<emp:text id="AccpDetail.pyee_acctsvcr_name" label="收款人开户行行名" maxlength="100" required="false" />
			<emp:text id="AccpDetail.drft_amt" label="票面金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="AccpDetail.term_type" label="期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="AccpDetail.term" label="期限" maxlength="38" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
