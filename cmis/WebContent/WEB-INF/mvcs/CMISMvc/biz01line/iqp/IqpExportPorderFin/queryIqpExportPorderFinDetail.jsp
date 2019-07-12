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
		var url = '<emp:url action="queryIqpExportPorderFinList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpExportPorderFinGroup" title="出口商票融资从表" maxColumn="2">
			<emp:text id="IqpExportPorderFin.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="IqpExportPorderFin.is_limit_cont_pay" label="是否额度合同项下支用" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpExportPorderFin.limit_cont_no" label="额度合同编号" maxlength="40" required="false" />
			<emp:select id="IqpExportPorderFin.is_replace" label="是否置换" required="false" />
			<emp:text id="IqpExportPorderFin.rpled_serno" label="被置换业务编号" maxlength="40" required="false" />
			<emp:select id="IqpExportPorderFin.invc_cur_type" label="发票币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpExportPorderFin.invc_amt" label="发票金额" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="IqpExportPorderFin.biz_settl_mode" label="原业务结算方式" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
