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
		var url = '<emp:url action="queryIqpGuarantInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpGuarantInfoGroup" title="保函信息表" maxColumn="2">
			<emp:text id="IqpGuarantInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpGuarantInfo.guarant_type" label="保函种类" required="false" />
			<emp:select id="IqpGuarantInfo.guarant_mode" label="保函类型" required="false" />
			<emp:select id="IqpGuarantInfo.open_type" label="开立类型" required="false" />
			<emp:text id="IqpGuarantInfo.chrg_rate" label="手续费率" required="true" dataType="Rate"/>
			<emp:select id="IqpGuarantInfo.is_bank_format" label="是否我行标准格式" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpGuarantInfo.is_agt_guarant" label="是否转开代理行保函" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpGuarantInfo.agt_bank_no" label="代理行行号" maxlength="20" required="false" />
			<emp:text id="IqpGuarantInfo.agt_bank_name" label="代理行名称" maxlength="100" required="false" />
			<emp:text id="IqpGuarantInfo.item_name" label="项目名称" maxlength="80" required="false" />
			<emp:text id="IqpGuarantInfo.item_amt" label="项目金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpGuarantInfo.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="IqpGuarantInfo.cont_name" label="合同名称" maxlength="100" required="false" />
			<emp:text id="IqpGuarantInfo.ben_name" label="受益人名称" maxlength="80" required="false" />
			<emp:text id="IqpGuarantInfo.ben_addr" label="受益人地址" maxlength="150" required="false" />
			<emp:text id="IqpGuarantInfo.ben_acct_org_no" label="受益人开户行行号" maxlength="20" required="false" />
			<emp:select id="IqpGuarantInfo.guarant_pay_type" label="保函付款方式" required="false" />
			<emp:text id="IqpGuarantInfo.corre_busnes_cont_amt" label="相关贸易合同金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpGuarantInfo.ben_acct_no" label="受益人账号" maxlength="40" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
