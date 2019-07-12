<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String showTab = "";
	String flag = "";
	String showBut = "";
	if(context.containsKey("showTab")){
		showTab = (String)context.getDataValue("showTab");
	}
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("showBut")){
		showBut = (String)context.getDataValue("showBut");
	}
%>
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
%>

<script type="text/javascript">
	
	function doReturn() {
		var flag='${context.flag}';//历史查询时，查看详情。
 		if(flag=='ls'){
 	 		var cus_id = IqpCoreConNet.cus_id._getValue();
 	 		cus_id = '\''+cus_id+'\'';
			var url = '<emp:url action="queryIqpCoreConNetLs.do"/>';
			url = EMPTools.encodeURI(url);
			window.location=url;
			return false;
		}
		if(flag=='net'){//入网/退网历史查询时
			var url = '<emp:url action="queryIqpNetAppLs.do"/>';
			url = EMPTools.encodeURI(url);
			window.location=url;
			return false;
		}
		var url = '<emp:url action="queryIqpCoreConNetList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
		<emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
			<emp:gridLayout id="IqpCoreConNetGroup" title="客户概况分析" maxColumn="2">
				<emp:text id="IqpCoreConNet.serno" label="业务编号" maxlength="32" required="true" />
					<emp:select id="IqpCoreConNet.app_type" label="申请类型" required="false" dictname="STD_ZB_NET_APP_TYPE" hidden="true"/>
					<emp:pop id="IqpCoreConNet.cus_id" label="核心企业客户码" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo" required="true" />
					<emp:text id="IqpCoreConNet.cus_id_displayname" label="核心企业客户名称"  required="true" readonly="true"/>
					<emp:select id="IqpCoreConNet.cdt_lvl" label="信用等级" dictname="STD_ZB_CREDIT_GRADE" required="true" readonly="true"/>
					<emp:textarea id="IqpCoreConNet.main_prd" label="主要产品" maxlength="20" required="false" colSpan="2" />
					<emp:textarea id="IqpCoreConNet.devlfore" label="发展前景" maxlength="100" required="false" colSpan="2" />
					<emp:textarea id="IqpCoreConNet.app_resn" label="申请理由" maxlength="100" required="false" colSpan="2" />
					<emp:textarea id="IqpCoreConNet.inte_income" label="综合收益概述" maxlength="100" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="tradeGtoup" title="行业前景分析" maxColumn="2">
					 <emp:text id="IqpCoreConNet.trade_type_displayname" label="行业分类"   readonly="true"  required="true" cssElementClass="emp_field_text_input2"/>
					<emp:text id="IqpCoreConNet.trade_type" label="行业分类" required="false" hidden="true"/>
					<emp:textarea id="IqpCoreConNet.trade_devlfore" label="行业发展前景" maxlength="100" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="LmtGroup" title="授信业务情况" maxColumn="2">
				    <emp:text id="IqpCoreConNet.lmt_amt" label="直接授信额度" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>  
					<emp:text id="IqpCoreConNet.batair_lmt_amt" label="间接授信额度" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="IqpCoreConNet.dealer_rebuyamt" label="经销商回购担保额度" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="IqpCoreConNet.buyer_rebuy_amt" label="供应商回购担保额度" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="IqpCoreConNet.fin_bail_perc" label="融资保证金比例" maxlength="16" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
					</emp:gridLayout>
				<emp:gridLayout id="CoopGroup" title="业务合作方案" maxColumn="2">
					<emp:text id="IqpCoreConNet.disp_term" label="发货期限（天）" maxlength="10" required="true" />
					<emp:date id="IqpCoreConNet.net_build_date" label="网络建立时间" required="true" />
					<emp:select id="IqpCoreConNet.coop_term_type" label="合作期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
					<emp:text id="IqpCoreConNet.coop_term" label="合作期限" maxlength="3" required="true" />
					<emp:text id="IqpCoreConNet.sale_reliterm" label="销售调剂期" maxlength="5" required="true" />
					<emp:text id="IqpCoreConNet.rebuy_cond" label="回购条件" maxlength="32" required="true" />
					<emp:text id="IqpCoreConNet.rebuy_perc" label="回购比例" maxlength="10" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
					<emp:checkbox id="IqpCoreConNet.respond_mode" label="承担责任方式" dictname="STD_ZB_RESPOND_MODE" required="true" layout="false" colSpan="2"/>
					<emp:checkbox id="IqpCoreConNet.dealer_lmt_type" label="经销商授信业务种类" required="true" dictname="STD_DEALER_BIZ_TYPE" layout="false" disabled="true"/>
					<emp:checkbox id="IqpCoreConNet.provider_lmt_type" label="供应商授信业务种类" required="true" dictname="STD_PROVIDER_BIZ_TYPE" layout="false" disabled="true"/>
					<emp:checkbox id="IqpCoreConNet.oversee_mode" label="监管方式" required="true" colSpan="2" dictname="STD_ZB_OVERSEE_MODE" layout="false" disabled="true"/>
					<emp:textarea id="IqpCoreConNet.memo" label="备注" maxlength="100" required="false" colSpan="2" />
					<emp:text id="IqpCoreConNet.manager_id_displayname" label="责任人"   required="true" />
					<emp:text id="IqpCoreConNet.manager_br_id_displayname" label="管理机构" required="true" />
					<emp:text id="IqpCoreConNet.input_id_displayname" label="登记人"   required="true" />
					<emp:text id="IqpCoreConNet.input_br_id_displayname" label="登记机构"   required="true" />
					<emp:text id="IqpCoreConNet.manager_id" label="责任人" maxlength="32" required="false" hidden="true"/>
					<emp:text id="IqpCoreConNet.manager_br_id" label="管理机构" maxlength="32" required="false" hidden="true"/>
					<emp:text id="IqpCoreConNet.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
					<emp:text id="IqpCoreConNet.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
					<emp:date id="IqpCoreConNet.input_date" label="登记日期" required="true"/>
					<emp:select id="IqpCoreConNet.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true"/>
					<emp:select id="IqpCoreConNet.flow_type" label="流程类型" hidden="true" defvalue="01"/>
				</emp:gridLayout>
		<div align="center">
			<br>
			<%if("IqpCoreView".equals(showBut)){ %>
			<emp:button id="return" label="返回列表"/>
			<%} %>
		</div>
	</emp:tab>
	
		<%if("net".equals(flag)){ %>
		  <emp:tab label="成员名单" id="subTab5" url="queryIqpAppMemManaList.do?serno=${context.IqpCoreConNet.serno}&op=view&menuId=coreconnet&subMenuId=queryIqpAppMemManaList&showMem=no" initial="false" needFlush="true"/>
		  <emp:tab label="订货计划" id="subTab1" url="queryIqpAppDesbuyPlanList.do?serno=${context.IqpCoreConNet.serno}&op=view&menuId=coreconnet&subMenuId=queryIqpAppDesbuyPlanList" initial="false" needFlush="true"/>
		  <emp:tab label="年度购销合同" id="subTab4" url="queryIqpAppPsaleContList.do?serno=${context.IqpCoreConNet.serno}&op=view&menuId=coreconnet&subMenuId=queryIqpAppPsaleContList" initial="false" needFlush="true"/>
		  <emp:tab label="动产质押监管协议（年度）" id="subTab6" url="queryIqpAppOverseeAgrList.do?serno=${context.IqpCoreConNet.serno}&op=view&menuId=coreconnet&subMenuId=queryIqpAppOverseeAgrList" initial="false" needFlush="true"/>
		  <emp:tab label="银企商合作协议" id="subTab2" url="queryIqpAppBconCoopAgrList.do?serno=${context.IqpCoreConNet.serno}&op=view&menuId=coreconnet&subMenuId=queryIqpAppBconCoopAgrList" initial="false" needFlush="true"/>
		  <emp:tab label="保兑仓协议" id="subTab3" url="queryIqpAppDepotAgrList.do?serno=${context.IqpCoreConNet.serno}&op=view&menuId=coreconnet&subMenuId=queryIqpAppDepotAgrList" initial="false" needFlush="true"/>
		  <emp:tab label="网络变更历史" id="subTab7" url="queryIqpCoreConNetLs.do?subConndition=cus_id=${context.IqpCoreConNet.cus_id}&op=view&menuId=coreconnet&subMenuId=queryIqpAppDepotAgrList" initial="false" needFlush="true"/>
	  	<%}%>
	  	<%if(!"no".equals(showTab)){ %>
			<emp:ExtActTab></emp:ExtActTab>
		<%}%> 
	</emp:tabGroup>  
</body>
</html>
</emp:page>
