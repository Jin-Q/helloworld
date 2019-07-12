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
		var url = '<emp:url action="queryArpAssetWriteoffAccInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="ArpAssetWriteoffAccInfoGroup" title="资产核销信息(台账)" maxColumn="2">
			<emp:text id="ArpAssetWriteoffAccInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="ArpAssetWriteoffAccInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" />
			<emp:text id="ArpAssetWriteoffAccInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="true" />
			<emp:text id="ArpAssetWriteoffAccInfo.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpAssetWriteoffAccInfo.writeoff_amt" label="核销金额" maxlength="16" required="false" dataType="Currency" />
			<emp:textarea id="ArpAssetWriteoffAccInfo.disp_resn" label="处置理由" maxlength="200" required="false" colSpan="2" />
			<emp:date id="ArpAssetWriteoffAccInfo.writeoff_date" label="核销日期" required="false" />
			<emp:textarea id="ArpAssetWriteoffAccInfo.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpAssetWriteoffAccInfo.status" label="状态" required="false" dictname="STD_ZX_ASSET_STATUS" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
