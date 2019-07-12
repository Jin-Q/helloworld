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
	
	function doClose(){
         window.close();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpPsaleContGoodGroup" title="购销合同商品结果表" maxColumn="2">
	        <emp:text id="IqpPsaleContGood.psale_cont" label="购销合同编号" maxlength="40" required="true" colSpan="2"/>
			
			<emp:pop id="IqpPsaleContGood.commo_name_displayname" label="商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpPsaleContGood.commo_name" label="商品名称" required="false" hidden="true"/>
			
		    <emp:text id="IqpPsaleContGood.qnt" label="购买商品数量" maxlength="16" required="true" />
			<emp:select id="IqpPsaleContGood.qnt_unit" label="数量单位" required="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpPsaleContGood.unit_price" label="购买商品单价（元）" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="IqpPsaleContGood.total" label="购买商品总价（元）" maxlength="16" required="true" dataType="Currency" readonly="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
