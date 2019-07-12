<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
	<html>
	<head>
	<title>详情查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />

	<%
		request.setAttribute("canwrite", "");
		Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String type = "";
		if (context.containsKey("type")) {
			type = context.getDataValue("type").toString();
		}
		String showButton = "";
		if (context.containsKey("showButton")) {
			showButton = context.getDataValue("showButton").toString();
		}
		String serno = request.getParameter("serno");
		String cus_id = request.getParameter("cus_id");
		String rt = request.getParameter("rt");
	%>
	<script type="text/javascript">
	
	function doReturn() {
		var type = '<%=type%>';
		var url = '<emp:url action="queryLmtAppFinSubpayList.do"/>?type='+type;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doReturn1() {
		var type = '<%=type%>';
		var cus_id = '<%=cus_id%>';
		var url = '<emp:url action="queryLmtAppFinSubpayListForTab.do"/>?type='+type+'&LmtAppFinSubpay.cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doViewLmtSubpayList() {
		var paramStr = LmtSubpayList._obj.getParamStr(['pk']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtSubpayListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:800px';
			window.showModalDialog(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};		
	/*--user code end--*/
	
</script>
	</head>
	<body class="page_content">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="基本信息" id="main_tabs">
			<emp:form id="submitForm" action="updateLmtAppFinSubpayRecord.do"
				method="POST">
				<emp:gridLayout id="LmtAppFinSubpayGroup" title="融资性担保公司代偿"
					maxColumn="2">
					<emp:text id="LmtAppFinSubpay.serno" label="业务编号" maxlength="40"
						required="true" colSpan="2"
						cssElementClass="emp_field_text_readonly" readonly="true" />
					<emp:pop id="LmtAppFinSubpay.cus_id" label="担保公司客户码"
						url="queryAllCusPop.do?cusTypCondition=FinGuar&returnMethod=returnCus"
						required="true" defvalue="<%=cus_id%>"/>
					<emp:text id="LmtAppFinSubpay.cus_id_displayname" label="担保公司客户名称"
						required="true" colSpan="2" cssElementClass="emp_field_text_cusname" />
					<emp:select id="LmtAppFinSubpay.subpay_cur_type" label="代偿币种"
						required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"
						readonly="true" />
					<emp:text id="LmtAppFinSubpay.subpay_totl_limit" label="代偿总额"
						maxlength="18" required="false" readonly="true"
						dataType="Currency" cssElementClass="emp_currency_text_readonly" />
					<emp:text id="LmtAppFinSubpay.subpay_times" label="本次代偿笔数"
						maxlength="10" required="false" readonly="true" colSpan="2" />
					<emp:text id="LmtAppFinSubpay.pyee_acct" label="收款人账号" 
						required="true" />
					<emp:text id="LmtAppFinSubpay.pyee_name" label="收款人账户名"
						required="true" colSpan="2" cssElementClass="emp_field_text_cusname" />
					<emp:pop id="LmtAppFinSubpay.paorg_no" label="收款人开户行行号" url=""
						required="true" />
					<emp:text id="LmtAppFinSubpay.paorg_name" label="收款人开户行行名"
						required="true" colSpan="2" cssElementClass="emp_field_text_cusname" />
					<emp:date id="LmtAppFinSubpay.subpay_app_date" label="代偿申请日期"
						required="true" />
					<emp:date id="LmtAppFinSubpay.subpay_end_date" label="代偿办结日期"
						required="false" hidden="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="LmtAppFinSubpayGroup" title="登记信息" maxColumn="2">
					<emp:pop id="LmtAppFinSubpay.manager_id_displayname" label="责任人"
						url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"
						required="true" />
					<emp:pop id="LmtAppFinSubpay.manager_br_id_displayname"
						label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"
						required="true" />
					<emp:text id="LmtAppFinSubpay.input_id_displayname" label="登记人"
						maxlength="20" required="false" readonly="true" />
					<emp:text id="LmtAppFinSubpay.input_br_id_displayname" label="登记机构"
						maxlength="20" required="false" readonly="true" />
					<emp:text id="LmtAppFinSubpay.input_id" label="登记人" maxlength="20"
						required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.input_br_id" label="登记机构"
						maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.manager_id" label="责任人"
						maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.manager_br_id" label="责任机构"
						maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.input_date" label="登记日期"
						maxlength="10" required="false" defvalue="$OPENDAY"
						readonly="true" />
					<emp:select id="LmtAppFinSubpay.approve_status" label="申请状态"
						required="false" dictname="WF_APP_STATUS" hidden="true" />
				</emp:gridLayout>
				<div align="center"><br>
				<%if("yes".equals(rt) || "N".equals(showButton)) {%>
				<%}else if("tab".equals(type)){ %>
				<input type="button" class="button100" onclick="doReturn1(this)"
					value="返回到列表页面">
				<%}else{ %>
				<input type="button" class="button100" onclick="doReturn(this)"
					value="返回到列表页面">
				<%} %>
				</div>
			</emp:form>
			<div align="left">
				<emp:button id="viewLmtSubpayList" label="查看" op="view" />
			</div>
			<emp:table icollName="LmtSubpayList" pageMode="false" url="">
				<emp:text id="serno" label="流水号" />
				<emp:text id="prd_id_displayname" label="业务品种" />
				<emp:text id="cont_no" label="合同编号" />
				<emp:text id="subpay_bill_no" label="代偿借据编号" />
				<emp:text id="guar_mode" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" hidden="true"/>
				<emp:text id="bill_amt" label="借据金额" dataType="Currency" />
				<emp:text id="bill_bal" label="借据余额" dataType="Currency" />
				<emp:text id="int_cumu" label="欠息累计" dataType="Currency" />
				<emp:text id="subpay_cap" label="代偿本金" dataType="Currency" />
				<emp:text id="subpay_int" label="代偿利息" dataType="Currency" />
				<emp:text id="pk" label="主键" hidden="true" />
			</emp:table>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	</body>
	</html>
</emp:page>
