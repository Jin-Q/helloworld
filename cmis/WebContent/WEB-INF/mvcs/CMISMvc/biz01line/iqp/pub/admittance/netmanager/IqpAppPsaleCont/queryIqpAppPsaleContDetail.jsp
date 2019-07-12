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
		var url = '<emp:url action="queryIqpAppPsaleContList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
        window.close();
    };
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAppPsaleContGroup" title="购销合同信息" maxColumn="2">
			<emp:text id="IqpAppPsaleCont.psale_cont" label="购销合同编号" maxlength="40" required="false" hidden="false"/>
			<emp:text id="IqpAppPsaleCont.buyer_cus_id" label="买方客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppPsaleCont.buyer_cus_id_displayname" label="买方客户名称"    required="true" readonly="true"/>
			<emp:text id="IqpAppPsaleCont.barg_cus_id" label="卖方客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppPsaleCont.barg_cus_id_displayname" label="卖方客户名称" required="true" readonly="true"/>
			
			
			<emp:text id="IqpAppPsaleCont.cont_amt" label="合同金额" maxlength="16" required="true" dataType="Currency" colSpan="2"/>
			<emp:date id="IqpAppPsaleCont.start_date" label="合同起始日" required="true"  />
			<emp:date id="IqpAppPsaleCont.end_date" label="合同到期日" required="true" />
			
			<emp:pop id="IqpAppPsaleCont.commo_name_displayname" label="商品名称" required="false" hidden="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpAppPsaleCont.commo_name" label="商品名称" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.qnt" label="购买商品数量" maxlength="16" required="false" hidden="true" onblur="clcTotal()" />
			<emp:select id="IqpAppPsaleCont.qnt_unit" label="数量单位" required="false" hidden="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpAppPsaleCont.unit_price" label="购买商品单价（元）" maxlength="16" required="false" hidden="true" dataType="Currency" onblur="clcTotal()" />
			<emp:text id="IqpAppPsaleCont.total" label="购买商品总价（元）" maxlength="16" required="false" hidden="true" dataType="Currency" readonly="true" />
			
			<emp:textarea id="IqpAppPsaleCont.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			
			<emp:text id="IqpAppPsaleCont.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpAppPsaleCont.input_br_id_displayname" label="登记机构"   required="true" readonly="true" />
			<emp:text id="IqpAppPsaleCont.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true" />
			
			
			<emp:text id="IqpAppPsaleCont.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppPsaleCont.mem_cus_id" label="成员编号" maxlength="30" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
	<emp:tabGroup id="IqpAppPsaleCont_tabs" mainTab="IqpAppPsaleContGood_tab">
		<emp:tab id="IqpAppPsaleContGood_tab" label="商品明细" url="queryIqpAppPsaleContGoodList.do" reqParams="op=view&psale_cont=${context.psale_cont}&serno=${context.serno}" initial="true"/>
	</emp:tabGroup>
</body>
</html>
</emp:page>
