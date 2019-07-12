<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryIqpNetMagInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function doClose() {
		window.close();  
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
         <emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
			<emp:gridLayout id="IqpNetMagInfoGroup" title="客户概况分析" maxColumn="2">
				<emp:text id="IqpNetMagInfo.net_agr_no" label="网络协议编号" maxlength="32" required="true" hidden="false"/>
		       	<emp:select id="IqpNetMagInfo.app_type" label="申请类型" required="false" dictname="STD_ZB_NET_APP_TYPE" hidden="true"/>
				<emp:pop id="IqpNetMagInfo.cus_id" label="核心企业客户码" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo" required="true" />
				<emp:text id="IqpNetMagInfo.cus_id_displayname" label="核心企业客户名称"  required="true" readonly="true"/>
				<emp:select id="IqpNetMagInfo.cdt_lvl" label="信用等级" dictname="STD_ZB_CREDIT_GRADE" required="true" readonly="true"/>
				<emp:textarea id="IqpNetMagInfo.main_prd" label="主要产品" maxlength="20" required="false" colSpan="2" />
				<emp:textarea id="IqpNetMagInfo.devlfore" label="发展前景" maxlength="100" required="false" colSpan="2" />
				<emp:textarea id="IqpNetMagInfo.app_resn" label="申请理由" maxlength="100" required="false" colSpan="2" />
				<emp:textarea id="IqpNetMagInfo.inte_income" label="综合收益概述" maxlength="100" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="tradeGtoup" title="行业前景分析" maxColumn="2">
				<emp:text id="IqpNetMagInfo.trade_type_displayname" label="行业分类"   readonly="true"  required="true" cssElementClass="emp_field_text_input2"/>
				<emp:text id="IqpNetMagInfo.trade_type" label="行业分类" required="false" hidden="true"/>
				<emp:textarea id="IqpNetMagInfo.trade_devlfore" label="行业发展前景" maxlength="100" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtGroup" title="授信业务情况" maxColumn="2">
			    <emp:text id="IqpNetMagInfo.lmt_amt" label="直接授信额度" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>  
				<emp:text id="IqpNetMagInfo.batair_lmt_amt" label="间接授信额度" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="IqpNetMagInfo.dealer_rebuyamt" label="经销商回购担保额度" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="IqpNetMagInfo.buyer_rebuy_amt" label="供应商回购担保额度" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="IqpNetMagInfo.fin_bail_perc" label="融资保证金比例" maxlength="16" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
				</emp:gridLayout>
			<emp:gridLayout id="CoopGroup" title="业务合作方案" maxColumn="2">
				<emp:text id="IqpNetMagInfo.disp_term" label="发货期限（天）" maxlength="10" required="true" />
				<emp:date id="IqpNetMagInfo.net_build_date" label="网络建立日期" required="true" />
				<emp:select id="IqpNetMagInfo.coop_term_type" label="合作期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="IqpNetMagInfo.coop_term" label="合作期限" maxlength="3" required="true" />
				<emp:text id="IqpNetMagInfo.sale_reliterm" label="销售调剂期" maxlength="5" required="true" />
				<emp:text id="IqpNetMagInfo.rebuy_cond" label="回购条件" maxlength="32" required="true" />
				<emp:text id="IqpNetMagInfo.rebuy_perc" label="回购比例" maxlength="10" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
				<emp:checkbox id="IqpNetMagInfo.respond_mode" label="承担责任方式" dictname="STD_ZB_RESPOND_MODE" required="true" layout="false" colSpan="2"/>
				<emp:checkbox id="IqpNetMagInfo.dealer_lmt_type" label="经销商授信业务种类" required="true" dictname="STD_DEALER_BIZ_TYPE" layout="false" disabled="true"/>
				<emp:checkbox id="IqpNetMagInfo.provider_lmt_type" label="供应商授信业务种类" required="true" dictname="STD_PROVIDER_BIZ_TYPE" layout="false" disabled="true"/>
				<emp:checkbox id="IqpNetMagInfo.oversee_mode" label="监管方式" required="true" colSpan="2" dictname="STD_ZB_OVERSEE_MODE" layout="false" disabled="true"/>
				
				<emp:textarea id="IqpNetMagInfo.memo" label="备注" maxlength="100" required="false" colSpan="2" />
				<emp:pop id="IqpNetMagInfo.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" 
				           popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
				<emp:pop id="IqpNetMagInfo.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  required="true" 
				          popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
				<emp:text id="IqpNetMagInfo.input_id_displayname" label="登记人"  required="true" />
				<emp:text id="IqpNetMagInfo.input_br_id_displayname" label="登记机构"  required="true" />
				<emp:text id="IqpNetMagInfo.manager_id" label="责任人" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpNetMagInfo.manager_br_id" label="管理机构" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpNetMagInfo.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpNetMagInfo.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
				<emp:date id="IqpNetMagInfo.input_date" label="登记日期" required="true"/>
				<emp:select id="IqpNetMagInfo.status" label="网络状态" required="false" dictname="STD_ZB_STATUS" readonly="true"/>
				<emp:text id="IqpNetMagInfo.serno" label="业务流水号" maxlength="32" required="false"  hidden="true"/>
				<emp:select id="IqpNetMagInfo.flow_type" label="流程类型" hidden="true"/>
				<emp:select id="IqpNetMagInfo.approve_status" label="审批状态" hidden="true"/>
			</emp:gridLayout>
		<div align="center">
		<br>
			<%if("netMag".equals(type)) {%>
				<emp:button id="return" label="返回列表"/>
			<%}else {%>
				<emp:button id="close" label="关闭"/>
			<%} %>
		</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>
</body>
</html>
</emp:page>
