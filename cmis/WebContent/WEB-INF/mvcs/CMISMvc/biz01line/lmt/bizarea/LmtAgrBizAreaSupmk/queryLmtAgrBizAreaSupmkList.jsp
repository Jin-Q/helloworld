<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	function doOnload(){
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form  method="POST" action="updateLmtAgrBizAreaSupmkRecord.do" id="submitForm">
	
		<emp:table icollName="LmtAgrBizAreaSupmkList" pageMode="false" selectType="2" url="pageLmtAgrBizAreaSupmkQuery.do" editable="true">
			<emp:text id="supmk_serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:select id="oper_trade" label="经营行业" dictname="STD_LMT_BIZ_INDUS" />
			<emp:select id="oper_model" label="经营规模" dictname="STD_LMT_BIZ_SIZE" />
			<emp:text id="trade_rank" label="行业排名" />
			<emp:text id="provid_year" label="供货年限" />
			<emp:text id="net_asset" label=" 净资产" dataType="Currency" />
			<emp:text id="other_cond" label="其他准入条件" />
		</emp:table>
	</emp:form>
</body>
</html>
</emp:page>
    