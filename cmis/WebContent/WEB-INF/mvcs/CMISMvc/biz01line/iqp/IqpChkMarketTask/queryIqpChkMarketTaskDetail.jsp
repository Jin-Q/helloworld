<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpChkMarketTaskList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="盯市任务基本信息" id="base_tab" needFlush="true" initial="true" >
	<emp:form id="submitForm" action="updateIqpChkMarketTaskRecord.do" method="POST">
		<emp:gridLayout id="IqpChkMarketTaskGroup" title="盯市任务信息" maxColumn="2">
			<emp:text id="IqpMortValueAdj.value_no" label="价格编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="IqpMortValueAdj.status" label="状态" required="false" dictname="STD_CHKMARKET_TASK_STATUS" readonly="true" />
			<emp:pop id="IqpMortValueMana.catalog_no_displayname" label="目录编号" url="showCatalogManaTree.do" returnMethod="setCatalogPath" required="true" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
			<emp:pop id="IqpMortValueMana.produce_area_displayname" label="产地" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" returnMethod="setProduceArea" colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true"/>
			<emp:pop id="IqpMortValueMana.produce_vender_displayname" label="生产厂家" url="" required="true" cssElementClass="emp_field_text_readonly" defvalue="11" colSpan="2" readonly="true"/>
			<emp:pop id="IqpMortValueMana.sale_area_displayname" label="销售区域" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setSaleArea" required="false" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
			<emp:select id="IqpMortValueMana.freq_unit" label="盯市频率单位" required="true" dictname="STD_ZX_FREQ_UNIT" readonly="true" />
			<emp:text id="IqpMortValueMana.freq" label="盯市频率" maxlength="1" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly" readonly="true" />
			<emp:date id="IqpMortValueMana.auth_date" label="价格核准时间" required="true" readonly="true" />
			<emp:text id="IqpMortValueMana.change_valve" label="价格变动阀值" maxlength="16" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="IqpMortValueMana.is_qual_judge" label="是否需要品质鉴定" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="IqpMortValueMana.info_sour" label="价格信息来源" required="true" dictname="STD_ZB_INFO_SOUR" />
			<emp:text id="IqpMortValueAdj.org_valve" label="上次商品核准价格" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="IqpMortValueAdj.change_valve" label="此次商品核准价格" maxlength="18" required="true" dataType="Currency" />
			<emp:textarea id="IqpMortValueAdj.change_resn" label="价格变动原因" maxlength="200" required="true" colSpan="2" />
			<emp:textarea id="IqpMortValueAdj.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="IqpMortValueAdj.input_id_displayname" label="登记人" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpMortValueAdj.input_br_id_displayname" label="登记机构" required="true" readonly="true" hidden="true"/> 
			<emp:date id="IqpMortValueAdj.input_date" label="登记日期" required="true" readonly="true" hidden="true"/>
			
			<emp:text id="IqpMortValueAdj.inure_date" label="变动生效时间" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpMortValueAdj.pk_id" label="物理主键" hidden="true" readonly="true" />
		</emp:gridLayout>
	</emp:form>
	
	<div class='emp_gridlayout_title'>价格变动历史&nbsp;</div>
	<emp:table icollName="IqpMortValueAdjList" pageMode="true" url="pageIqpMortValueAdjQuery.do?value_no=${context.IqpMortValueAdj.value_no}&pk_id=${context.IqpMortValueAdj.pk_id}">
		<emp:text id="org_valve" label="原有押品单价" dataType="Currency"/>
		<emp:text id="change_valve" label="此次核准单价" dataType="Currency"/>
		<emp:text id="info_sour" label="信息来源" dictname="STD_ZB_INFO_SOUR" />
		<emp:text id="inure_date" label="生效时间" />
	</emp:table>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<%//如果是流程中查看 不显示返回按钮  2014-09-30 唐顺岩
	if(!context.containsKey("flow")){ %>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
	<%} %>
</body>
</html>
</emp:page>
