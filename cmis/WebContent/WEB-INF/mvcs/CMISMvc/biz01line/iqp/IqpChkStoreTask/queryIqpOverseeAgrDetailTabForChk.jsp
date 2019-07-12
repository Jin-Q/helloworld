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
	text-align: left;
	width: 450px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpOverseeAgrList.do"/>?cus_id=${context.cus_id}'+"&mem_cus_id=${context.mem_cus_id}"+"&mem_manuf_type=${context.mem_manuf_type}"
														        +"&net_agr_no=${context.net_agr_no}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	       function getAuth()
			{
				var mode=IqpOverseeAgr.oversee_mode._getValue();
				if(mode=='01')
				{
					IqpOverseeAgr.low_auth_value._obj._renderHidden(false);
					IqpOverseeAgr.low_auth_value._obj._renderRequired(true);
				}else
				{
					IqpOverseeAgr.low_auth_value._obj._renderHidden(true);
					IqpOverseeAgr.low_auth_value._obj._renderRequired(false);
				}
			}
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="getAuth()">
	
	<emp:gridLayout id="IqpOverseeAgrGroup" title="监管协议" maxColumn="2">
			<emp:text id="IqpOverseeAgr.oversee_agr_no" label="监管协议号" maxlength="32" required="true" colSpan="2"/>
			<emp:pop id="IqpOverseeAgr.mortgagor_id" label="出质人客户码" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo" required="false" colSpan="2" />
			<emp:text id="IqpOverseeAgr.mortgagor_id_displayname" label="出质人客户名称" cssElementClass="emp_field_text_input2" required="true" colSpan="2" readonly="true"/>
			<emp:pop id="IqpOverseeAgr.oversee_con_id" label="监管企业编号" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo4over" required="false" colSpan="2"/>
			<emp:text id="IqpOverseeAgr.oversee_con_id_displayname" label="监管企业名称" cssElementClass="emp_field_text_input2" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpOverseeAgr.bail_acct" label="保证金账号" maxlength="32" required="false" />
			<emp:text id="IqpOverseeAgr.bail_acct_name" label="保证金户名" maxlength="32" required="false" />		
			<emp:text id="IqpOverseeAgr.oversee_store" label="监管仓库" maxlength="32" required="false" />
			<emp:select id="IqpOverseeAgr.insure_mode" label="投保方式" required="false" dictname="STD_ZB_INSURE_MODE" />
			<emp:text id="IqpOverseeAgr.vigi_line" label="警戒线" maxlength="20" required="false" dataType="Percent" />
			<emp:text id="IqpOverseeAgr.stor_line" label="平仓线" maxlength="20" required="false" dataType="Percent" />
			<emp:text id="IqpOverseeAgr.froze_line" label="冻结线" maxlength="20" required="false" dataType="Percent" colSpan="2"/>
			<emp:select id="IqpOverseeAgr.oversee_mode" label="监管方式" required="false" dictname="STD_ZB_OVERSEE_TYPE" onblur="getAuth()"/>
			<emp:text id="IqpOverseeAgr.low_auth_value" label="最低核准价值" maxlength="18" required="false" dataType="Currency"/>
			<emp:textarea id="IqpOverseeAgr.memo" label="备注" maxlength="100" required="false" colSpan="2" />			
			<emp:date id="IqpOverseeAgr.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpOverseeAgr.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="IqpOverseeAgr.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:text id="IqpOverseeAgr.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeAgr.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>	
			<emp:date id="IqpOverseeAgr.start_date" label="监管起始日期" required="false" hidden="true"/>
			<emp:date id="IqpOverseeAgr.end_date" label="监管到期日期" required="false" hidden="true"/>
			<emp:select id="IqpOverseeAgr.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true"/>
			<emp:text id="IqpOverseeAgr.net_agr_no" label="网络协议编号" maxlength="32" hidden="true"/>
	</emp:gridLayout>
</body>
</html>
</emp:page>
