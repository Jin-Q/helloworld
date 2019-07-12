<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%	
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String poType = "";
	poType = context.getDataValue("poType").toString();
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpActrecpoManaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">	
		<emp:gridLayout id="IqpActrecpoManaGroup" title="基本信息" maxColumn="2">
			<emp:text id="IqpActrecpoMana.po_no" label="池编号" maxlength="30" required="true" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="IqpActrecpoMana.cus_id" label="客户码" maxlength="40"  required="true" />
			<emp:text id="IqpActrecpoMana.cus_id_displayname" label="客户名称" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="IqpActrecpoMana.image_guaranty_no" label="影像押品编号" maxlength="40" required="true" hidden="false" readonly="true"/>
			<%if(poType.equals("2")){ %>
			<emp:select id="IqpActrecpoMana.factor_mode" label="保理方式" dictname = "STD_FACTORING_MODE"  required="false" hidden="false"/>
			<emp:select id="IqpActrecpoMana.is_rgt_res" label="是否有追索权"  dictname = "STD_IS_RGT_RES"  required="false" hidden="false"/>
			<%} %>
			<emp:text id="IqpActrecpoMana.invc_quant" label="发票数量" maxlength="16" required="false" hidden="false" readonly="true"/>
			<emp:text id="IqpActrecpoMana.invc_amt" label="发票总金额" maxlength="18" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpActrecpoMana.crd_rgtchg_amt" label="债权总金额" maxlength="18" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpActrecpoMana.pledge_rate" label="质押率" maxlength="10" dataType="Percent" required="true" />
			<emp:text id="IqpActrecpoMana.period_grace" label="宽限期" maxlength="5" required="true" />
			<emp:textarea id="IqpActrecpoMana.memo" label="备注" maxlength="250" required="false" colSpan="2"/>
			
			<emp:pop id="IqpActrecpoMana.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
			<emp:pop id="IqpActrecpoMana.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName2" readonly="true"/>
			
			<emp:pop id="IqpActrecpoMana.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true"/>
			<emp:text id="IqpActrecpoMana.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			
			<emp:text id="IqpActrecpoMana.input_id" label="登记人" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpActrecpoMana.input_br_id" label="登记机构" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpActrecpoMana.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="IqpActrecpoMana.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="IqpActrecpoMana.input_date" label="登记日期"  required="true" readonly="true"/>
			
			<emp:text id="IqpActrecpoMana.po_type" label="业务类型" maxlength="5" required="false"  hidden="true"/>
			<emp:text id="IqpActrecpoMana.po_mode" label="池模式" maxlength="5" required="false" hidden="true"/>
			
			<emp:text id="IqpActrecpoMana.status" label="状态" maxlength="5" required="false" hidden="true"/>				
			</emp:gridLayout>
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin -->
			<div id="bailAccDetail">
		    <div class='emp_gridlayout_title' id="commen">回款保证金明细&nbsp;</div>
		    <!-- 当池类型为【应收账款池】时，回款保证金只需一个账户 -->
				<emp:table icollName="IqpBailaccDetailList" pageMode="false" editable="true" url="">
				    <emp:text id="optType" label="操作方式" hidden="true" />
					<emp:text id="po_no" label="池编号" readonly="true" hidden="true"/>
					<emp:text id="bail_acc_no" label="回款保证金账号"/>
					<emp:text id="bail_acc_name" label="回款保证金账户名" readonly="true"/>
					<emp:text id="bail_acc_amt" label="回款保证金额" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true" required="false"/>
					<emp:text id="cus_id" label="买方客户号" readonly="true"/>
					<emp:text id="cus_id_displayname" label="买方客户名称" readonly="true"/>
					<emp:text id="cur_type" label="币种" readonly="true" hidden="true"/>
				    <emp:text id="pk" label="物理主键" hidden="true"/>
				</emp:table>
			</div>
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end -->
	</emp:tab>        
	<emp:tab label="贸易合同" id="mainTab" needFlush="true" url="queryIqpBuscontInfoList.do?type=view&po_no=${context.po_no}"></emp:tab>
	<emp:tab label="应收账款明细" id="maint" needFlush="true" url="queryIqpActrecbondDetailList.do?type=view&po_no=${context.po_no}"></emp:tab> 
	<%if(poType.equals("2")){ %>
	<emp:tab label="快递信息" id="maint2" needFlush="true" url="queryIqpExpInfoList.do?type=view&po_no=${context.po_no}"></emp:tab> 
	<%} %>
</emp:tabGroup>
</body>
</html>
</emp:page>
