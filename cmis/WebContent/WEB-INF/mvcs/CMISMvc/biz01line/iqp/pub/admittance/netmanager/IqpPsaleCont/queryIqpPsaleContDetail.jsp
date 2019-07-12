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
		var url = '<emp:url action="queryIqpPsaleContList.do"/>?net_agr_no=${context.net_agr_no}'
													            +"&mem_cus_id=${context.mem_cus_id}"
													            +"&mem_manuf_type=${context.mem_manuf_type}"
													            +"&cus_id=${context.cus_id}";                                                 
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function doClose(){
        window.close();
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpPsaleContGroup" title="购销合同信息" maxColumn="2">
			<emp:text id="IqpPsaleCont.psale_cont" label="购销合同码" required="true" hidden="false" colSpan="2"/>
			<emp:text id="IqpPsaleCont.buyer_cus_id" label="买方客户码" required="true" />
			<emp:text id="IqpPsaleCont.buyer_cus_id_displayname" label="买方客户名称"  required="false" readonly="true"/>
			<emp:text id="IqpPsaleCont.barg_cus_id" label="卖方客户码"  required="true" />
			<emp:text id="IqpPsaleCont.barg_cus_id_displayname" label="卖方客户名称"   required="false" readonly="true"/>
			<emp:text id="IqpPsaleCont.cont_amt" label="合同金额"  required="true" dataType="Currency" colSpan="2"/>
			<emp:date id="IqpPsaleCont.start_date" label="合同起始日" required="true" />
			<emp:date id="IqpPsaleCont.end_date" label="合同到期日" required="true" onblur="chechDt()"/>
			<emp:text id="IqpPsaleCont.commo_name" label="商品名称" required="true" hidden="true"/>
			<emp:pop id="IqpPsaleCont.commo_name_displayname" label="商品名称" required="false" hidden="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpPsaleCont.qnt" label="购买商品数量"  required="false" hidden="true" />
			<emp:select id="IqpPsaleCont.qnt_unit" label="数量单位" required="false" hidden="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpPsaleCont.unit_price" label="购买商品单价（元）"  required="false" hidden="true" dataType="Currency"/>
			<emp:text id="IqpPsaleCont.total" label="购买商品总价（元）"  required="false" hidden="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="IqpPsaleCont.memo" label="备注" required="true" colSpan="2"/>
			<emp:select id="IqpPsaleCont.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true"/>
			<emp:text id="IqpPsaleCont.input_id" label="登记人"  required="false" hidden="true"/>
			<emp:text id="IqpPsaleCont.input_br_id" label="登记机构"  required="false" hidden="true"/>
			<emp:date id="IqpPsaleCont.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:text id="IqpPsaleCont.net_agr_no" label="网络编号"  required="false" hidden="true"/>
			
	</emp:gridLayout>
	
	<div align="center">
		<br>
			<emp:button id="close" label="关闭"/>
	</div>
	<emp:tabGroup id="IqpPsaleCont_tabs" mainTab="IqpPsaleContGood_tab">
		<emp:tab id="IqpPsaleContGood_tab" label="商品明细" url="queryIqpPsaleContGoodList.do" reqParams="op=view&psale_cont=${context.psale_cont}" initial="true"/>
	</emp:tabGroup>
</body>
</html>
</emp:page>
