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
	function doClose(){
        window.close(); 
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAppPsaleContGoodGroup" title="购销合同商品表" maxColumn="2">
			<emp:text id="IqpAppPsaleContGood.serno" label="业务流水号" maxlength="40" required="true" />
			<emp:text id="IqpAppPsaleContGood.psale_cont" label="购销合同编号" maxlength="40" required="true" />
			
			<emp:pop id="IqpAppPsaleContGood.commo_name_displayname" label="商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpAppPsaleContGood.commo_name" label="商品名称" required="false" hidden="true"/>
			
		    <emp:text id="IqpAppPsaleContGood.qnt" label="购买商品数量" maxlength="16" required="true"  />
			<emp:select id="IqpAppPsaleContGood.qnt_unit" label="数量单位" required="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpAppPsaleContGood.unit_price" label="购买商品单价（元）" maxlength="16" required="true" dataType="Currency"  />
			<emp:text id="IqpAppPsaleContGood.total" label="购买商品总价（元）" maxlength="16" required="true" dataType="Currency" readonly="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
