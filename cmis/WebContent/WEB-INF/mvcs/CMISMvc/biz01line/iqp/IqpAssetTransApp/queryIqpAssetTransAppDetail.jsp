<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	String isHistory = "";
	if(context.containsKey("isHistory")){
		isHistory = (String)context.getDataValue("isHistory");
	}
%>
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
		var isHistory = '<%=isHistory %>';
		if(isHistory == "history"){
			var url = '<emp:url action="queryIqpAssetTransAppHistoryList.do"/>';
		}else{
			var url = '<emp:url action="queryIqpAssetTransAppList.do"/>';	
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab">
	<emp:tab label="基本信息" id="base_tab">
	<emp:gridLayout id="IqpAssetTransAppGroup" title="资产流转申请" maxColumn="2">
			<emp:text id="IqpAssetTransApp.serno" label="业务编号" maxlength="40" readonly="true" />
			<emp:select id="IqpAssetTransApp.trans_type" label="业务类型" required="true" dictname="STD_ZB_TRANS_TYPE" />
			<emp:text id="IqpAssetTransApp.pro_name" label="项目名称" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransApp.pro_short_name" label="项目简称" maxlength="80" required="true" />
			<emp:text id="IqpAssetTransApp.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransApp.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产流转" readonly="true"/>
			<emp:pop id="IqpAssetTransApp.toorg_no" label="交易对手行号" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId" required="true" />
			<emp:text id="IqpAssetTransApp.toorg_no_displayname" label="交易对手行名" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="IqpAssetTransApp.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetTransApp.loan_amt_totl" label="贷款总金额" maxlength="16" required="false" readonly="true" dataType="Currency"/>
			<emp:text id="IqpAssetTransApp.loan_balance_totl" label="贷款总余额" maxlength="16" required="false" readonly="true" dataType="Currency"/>
			<emp:text id="IqpAssetTransApp.trans_amt" label="转让金额" maxlength="16" required="false" readonly="true" dataType="Currency"/>
			<emp:text id="IqpAssetTransApp.trans_rate" label="转让比率" maxlength="16" required="false" readonly="true" dataType="Percent"/>
			<emp:text id="IqpAssetTransApp.trans_qnt" label="转让笔数" maxlength="38" required="false" readonly="true"/>
			<emp:date id="IqpAssetTransApp.trans_date" label="转让日期" required="true" />
			<emp:date id="IqpAssetTransApp.int_start_date" label="起息日" required="true" />
			<emp:select id="IqpAssetTransApp.interest_type" label="收息方式" hidden="true" dictname="STD_RCV_INT_TYPE"/>
			<emp:textarea id="IqpAssetTransApp.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:pop id="IqpAssetTransApp.manager_br_id_displayname" label="管理机构" required="true"  buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:select id="IqpAssetTransApp.flow_type" label="流程类型"  required="false" defvalue="01" dictname="STD_ZB_FLOW_TYPE" readonly="true"/>
		    <emp:select id="IqpAssetTransApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" required="false" hidden="true" defvalue="000"/>
		   	<emp:text id="IqpAssetTransApp.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="IqpAssetTransApp.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="IqpAssetTransApp.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="IqpAssetTransApp.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="IqpAssetTransApp.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="IqpAssetTransApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!"notHave".equals(flag)){%>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
	</emp:tab>
   	<emp:ExtActTab></emp:ExtActTab>
  	</emp:tabGroup>
</body>
</html>
</emp:page>
