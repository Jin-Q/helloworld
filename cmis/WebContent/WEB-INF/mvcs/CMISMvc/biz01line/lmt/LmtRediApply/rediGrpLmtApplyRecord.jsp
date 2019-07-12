<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%><emp:page>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String showButton = (String)context.getDataValue("showButton");
	String isShow = (String)context.getDataValue("isShow");
	
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	
	
	/*--user code begin--*/
	function onLoad(){
		LmtAppGrpRedi.grp_no._obj.addOneButton('view12','查看',viewGrpCusInfo);  //集团编号加查看按钮
		//给主页签增加重载事件
		document.getElementById("main_tabs").href="javascript:reLoad()";
	}
	//重加载页面
	function reLoad(){
		window.location.reload();
	}
	function viewGrpCusInfo(){
		var url = "<emp:url action='queryCusGrpInfoPopDetial.do'/>&grp_no="+LmtAppGrpRedi.grp_no._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=700,width=1024,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
	}
	//提交流程
	function doSubmitLmtAppGrpRedi(){
		var serno = LmtAppGrpRedi.serno._getValue();
		var approve_status = LmtAppGrpRedi.approve_status._getValue();
		WfiJoin.table_name._setValue("LmtAppGrpRedi");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("3221");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
		WfiJoin.cus_id._setValue(LmtAppGrpRedi.grp_no._getValue());//客户码
		WfiJoin.cus_name._setValue(LmtAppGrpRedi.grp_no_displayname._getValue());//客户名称
		WfiJoin.amt._setValue(LmtAppGrpRedi.crd_totl_amt._getValue());//金额
		WfiJoin.prd_name._setValue("集团客户授信复议申请");//产品名称
		
		initWFSubmit(false);
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtAppGrpRediList.do"/>?menuId=LmtAppGrpRedi';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="授信基本信息" id="main_tabs">
			<emp:gridLayout id="LmtGrpApplyGroup" title="集团授信复议申请/变更" maxColumn="2">
				<emp:text id="LmtAppGrpRedi.serno" label="业务编号" maxlength="40" required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
				<emp:text id="LmtAppGrpRedi.grp_no" label="集团编号" maxlength="40" required="true" />
				<emp:text id="LmtAppGrpRedi.grp_no_displayname" label="集团名称"  required="true" cssElementClass="emp_field_text_readonly"/>
				<emp:select id="LmtAppGrpRedi.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" />
				<emp:select id="LmtAppGrpRedi.biz_type" label="授信业务类型 " required="true" dictname="STD_ZB_BIZ_TYPE" />
				<emp:select id="LmtAppGrpRedi.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" />
				<emp:text id="LmtAppGrpRedi.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:date id="LmtAppGrpRedi.app_date" label="申请日期" required="true" />
				<emp:date id="LmtAppGrpRedi.over_date" label="办结日期" required="false" hidden="true"/>
				<emp:select id="LmtAppGrpRedi.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" hidden="true"/>
				<emp:textarea id="LmtAppGrpRedi.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppGrpGroup" maxColumn="2" title="登记信息">
				<emp:text id="LmtAppGrpRedi.manager_id_displayname" label="责任人"  required="true" />
				<emp:text id="LmtAppGrpRedi.manager_br_id_displayname" label="责任机构" required="true" />
				<emp:text id="LmtAppGrpRedi.input_id_displayname" label="登记人" required="true" />
				<emp:text id="LmtAppGrpRedi.input_br_id_displayname" label="登记机构" required="true" />
				<emp:date id="LmtAppGrpRedi.input_date" label="登记日期" required="true" />
				<emp:select id="LmtAppGrpRedi.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" hidden="true"/>
				<emp:text id="LmtAppGrpRedi.grp_agr_no" label="集团协议编号" maxlength="40" required="false" hidden="true"/>
				<emp:text id="LmtAppGrpRedi.org_crd_totl_amt" label="变更前授信总额 " maxlength="18" required="false" dataType="Currency" hidden="true"/>
				<emp:text id="LmtAppGrpRedi.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
				<emp:text id="LmtAppGrpRedi.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
				<emp:text id="LmtAppGrpRedi.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
				<emp:text id="LmtAppGrpRedi.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			</emp:gridLayout>
		</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<%if(!"N".equalsIgnoreCase(showButton)){ %>
		<emp:button id="submitLmtAppGrpRedi" label="提交审批" />
		<%} %>
		<%if(!"N".equalsIgnoreCase(isShow)){ %>
		<emp:button id="return" label="返回列表" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
