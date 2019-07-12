<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isShow = (String)context.getDataValue("isShow");
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var menuId = '${context.menuId}';
		var url = "";
		if(menuId=='lmtindivapp'){
			url = '<emp:url action="queryLmtAppIndivList.do"/>?type=app&menuId=${context.menuId}';
		}else if(menuId=='IndivRediApply'){
			url = '<emp:url action="queryLmtAppIndivList.do"/>?type=his&menuId=lmtindivapphis';
		}else{
			url = '<emp:url action="queryLmtAppIndivList.do"/>?type=his&menuId=${context.menuId}';
		}
		//复议列表过来
		if("INDIV"=='${context.lx}' || "indiv"=='${context.lx}'){
			url = '<emp:url action="queryLmtRediApplyList.do"/>?menuId=LmtRediApplyList&type=indiv';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnload(){
		LmtAppIndivRedi.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		
		checkOrgOrNot();

		var showButton = '${context.showButton}';  //是否提交按钮
		if("Y"==showButton || "y"==showButton){
			document.getElementById("button_submitLmtRediApply").style.display="";
		}
	}

	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppIndivRedi.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//控制变更时字段的显示
	function checkOrgOrNot(){
		var app_type = LmtAppIndivRedi.app_type._getValue();
		if("05"==app_type){    //如果是授信复议，隐藏原有额度情况   
			LmtAppIndivRedi.org_crd_totl_amt._obj._renderHidden(true);
		}
	} 

	//提交流程
	function doSubmitLmtRediApply(){
		var serno = LmtAppIndivRedi.serno._getValue();
		var approve_status = LmtAppIndivRedi.approve_status._getValue();
		WfiJoin.table_name._setValue("LmtAppIndivRedi");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("0062");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW

		WfiJoin.cus_id._setValue(LmtAppIndivRedi.cus_id._getValue());//客户码
		WfiJoin.cus_name._setValue(LmtAppIndivRedi.cus_id_displayname._getValue());//客户名称
		WfiJoin.amt._setValue(LmtAppIndivRedi.crd_totl_amt._getValue());  //金额
		WfiJoin.prd_name._setValue("个人授信复议申请");//产品名称
		initWFSubmit(false);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
		<emp:gridLayout id="LmtAppIndivRediGroup" title="授信信息" maxColumn="2">
			<emp:text id="LmtAppIndivRedi.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:select id="LmtAppIndivRedi.biz_type" label="授信业务类型 ：内部授信/公开授信" required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01" hidden="true"/>
			<emp:text id="LmtAppIndivRedi.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="LmtAppIndivRedi.cus_id_displayname" label="客户名称" required="true" readonly="true"/>
			<emp:select id="LmtAppIndivRedi.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndivRedi.app_type" label="申请类型" defvalue="01" required="true" dictname="STD_ZB_APP_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndivRedi.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppIndivRedi.org_crd_totl_amt" label="原授信总额(元)" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndivRedi.crd_totl_amt" label="授信总额(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndivRedi.totl_amt" label="非自助总额(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" defvalue="0.00"/>
			<emp:text id="LmtAppIndivRedi.self_amt" label="自助总额(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" defvalue="0.00"/>
			<emp:text id="LmtAppIndivRedi.crd_cir_amt" label="非自助循环额度(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" defvalue="0.00"/>
			<emp:text id="LmtAppIndivRedi.crd_one_amt" label="非自助一次性额度(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" defvalue="0.00"/>
			<emp:select id="LmtAppIndivRedi.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE" readonly="true"/>
			
			<emp:date id="LmtAppIndivRedi.totl_start_date" label="授信起始日" readonly="true" hidden="true"/>
			<emp:date id="LmtAppIndivRedi.totl_end_date" label="授信到期日" readonly="true" hidden="true"/>
			<emp:select id="LmtAppIndivRedi.is_adj_term_totl" label="是否调整期限" dictname="STD_ZX_YES_NO" onchange="showTerm()" colSpan="2" hidden="true"/>
			<emp:select id="LmtAppIndivRedi.term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE" defvalue="003" hidden="true"/>
			<emp:text id="LmtAppIndivRedi.term" label="期限" maxlength="3" dataType="Int" defvalue="0" hidden="true"/>
		</emp:gridLayout>

		<emp:gridLayout id="LmtAppIndivRediGroup" title="其他" maxColumn="2">
			<emp:textarea id="LmtAppIndivRedi.inve_rst" label="调查人结论" maxlength="800" required="true" colSpan="2" />
			<emp:textarea id="LmtAppIndivRedi.memo" label="备注" maxlength="800" colSpan="2" />
			<emp:select id="LmtAppIndivRedi.belg_line" label="所属条线" required="false" dictname="STD_ZB_BUSILINE" readonly="true" hidden="true"/>
			<emp:select id="LmtAppIndivRedi.util_mode" label="提用方式" required="false" dictname="STD_ZB_UTIL_MODE" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivRediGroup" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppIndivRedi.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="LmtAppIndivRedi.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="LmtAppIndivRedi.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="LmtAppIndivRedi.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName" readonly="true"/>
			<emp:date id="LmtAppIndivRedi.input_date" label="登记日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
		
			<emp:text id="LmtAppIndivRedi.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndivRedi.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
			<emp:text id="LmtAppIndivRedi.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="LmtAppIndivRedi.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:select id="LmtAppIndivRedi.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:date id="LmtAppIndivRedi.app_date" label="申请日期" required="true" hidden="true" defvalue="$OPENDAY"/>
			<emp:select id="LmtAppIndivRedi.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" hidden="true" defvalue="01"/>
		</emp:gridLayout>
	
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<emp:button id="submitLmtRediApply" label="提交审批" op="update"/>
		<%if(!"N".equalsIgnoreCase(isShow)){ %>
		<emp:button id="return" label="返回列表" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
