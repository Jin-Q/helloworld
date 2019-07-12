<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:180px;
}
</style>

<%
	request.setAttribute("canwrite","");
	String isShow = request.getParameter("isShow");
%>

<script type="text/javascript">
	
	function doReturn() {
		var flag = '${context.flag}';
		var type = '${context.type}';
		if(flag == 'view'){
             window.close();
		}else if(type == 'his'){
			var url = '<emp:url action="queryLmtSigLmtLs.do"/>';
			url = EMPTools.encodeURI(url);
			window.location=url
		}else{
			var url = '<emp:url action="queryLmtSigLmtList.do"/>';
			url = EMPTools.encodeURI(url);
			window.location=url
		}

	};
	
	function doViewLmtSubApp() {
		var paramStr = LmtSigLmt.LmtSubApp._obj.getParamStr(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryLmtSigLmtLmtSubAppDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
    <emp:tabGroup id="LmtSigLmt_tabs" mainTab="tab1">
    	<emp:tab id="tab1" label="基本信息" needFlush="true" initial="true">
			<emp:form id="submitForm" action="updateLmtSigLmtRecord.do" method="POST">
				<emp:gridLayout id="LmtSigLmtGroup" title="申請信息" maxColumn="2">
					<emp:text id="LmtSigLmt.serno" label="业务编号" maxlength="32" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_readonly"/>
					<emp:text id="LmtSigLmt.cus_id" label="客户码" maxlength="32" readonly="true" />
					<emp:text id="LmtSigLmt.same_org_cnname" label="同业机构(行)名称" maxlength="32" readonly="true"/>
					<emp:select id="LmtSigLmt.same_org_type" label="同业机构类型"  readonly="true" dictname="STD_ZB_INTER_BANK_ORG"/>
					<emp:select id="LmtSigLmt.app_cls" label="申请类别"  readonly="true" required="false" dictname="STD_ZB_APP_CLS"/>		
					<emp:select id="LmtSigLmt.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" />
					<emp:select id="LmtSigLmt.crd_grade" label="我行评级"  readonly="true" dictname="STD_ZB_FINA_GRADE" hidden="true"/>					
					<emp:text id="LmtSigLmt.owner_wrr" label="所有者权益(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtSigLmt.asserts" label="总资产(万元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:select id="LmtSigLmt.cur_type" label="授信币种"  required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
					<emp:text id="LmtSigLmt.risk_quota" label="风险限额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtSigLmt.lmt_amt" label="授信金额(元)" maxlength="18" required="true" colSpan="2" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:select id="LmtSigLmt.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" defvalue="001"/>
					<emp:text id="LmtSigLmt.term" label="期限" maxlength="6" required="true" defvalue="1"/>
					<emp:textarea id="LmtSigLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="LmtSigLmtGroup" title="登记信息" maxColumn="2">
					<emp:pop id="LmtSigLmt.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" />
					<emp:pop id="LmtSigLmt.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
					<emp:text id="LmtSigLmt.input_id_displayname" label="登记人" required="false" readonly="true"/>
					<emp:text id="LmtSigLmt.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
					<emp:date id="LmtSigLmt.input_date" label="登记日期" required="false" />
					<emp:date id="LmtSigLmt.app_date" label="申请日期" required="true"/>
					<emp:pop id="LmtSigLmt.manager_id" label="责任人" url="" hidden="true" />
					<emp:pop id="LmtSigLmt.manager_br_id" label="管理机构" url="null" hidden="true" />
					<emp:text id="LmtSigLmt.input_id" label="登记人" maxlength="20" hidden="true" />
					<emp:text id="LmtSigLmt.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
					<emp:select id="LmtSigLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
					<emp:text id="LmtSigLmt.flow_type" label="流程类型" maxlength="6" required="false" hidden="true"/>
					<emp:date id="LmtSigLmt.over_date" label="办结日期" hidden="true" required="false"/>
				    <emp:select id="LmtSigLmt.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" hidden="true" required="false"/>
				</emp:gridLayout>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<%if("N".equals(isShow)){%>
		<%}else{%>
			<emp:button id="return" label="返回列表"/>
		<%}%>
	</div>
</body>
</html>
</emp:page>
