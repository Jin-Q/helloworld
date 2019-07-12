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
%>
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
		var url = '<emp:url action="queryIqpBconCoopAgrList.do"/>'+"&cus_id=${context.cus_id}"
														          +"&mem_cus_id=${context.mem_cus_id}"
														          +"&net_agr_no=${context.net_agr_no}"
														          +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doClose(){
        window.close();
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpBconCoopAgrGroup" title="银企合作协议" maxColumn="2">
			<emp:text id="IqpBconCoopAgr.net_agr_no" label="网络编号" required="false" hidden="true"/>
			<emp:text id="IqpBconCoopAgr.coop_agr_no" label="银企商协议号"  hidden="false" colSpan="2" />
			<emp:text id="IqpBconCoopAgr.borrow_cus_id" label="借款人客户码" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.borrow_cus_id_displayname" label="借款人客户名称"  required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="IqpBconCoopAgr.manuf_cus_id" label="核心企业客户码" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.manuf_cus_id_displayname" label="核心企业客户名称"  required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:pop id="IqpBconCoopAgr.psale_cont" label="购销合同" url="queryIqpPsaleContPop.do" returnMethod="setPsale" required="true"/>
			<emp:text id="IqpPsaleCont.cont_amt" label="合同金额"  required="true" dataType="Currency" readonly="true"/>
			<emp:date id="IqpPsaleCont.start_date" label="合同起始日" required="true" readonly="true"/>
			<emp:date id="IqpPsaleCont.end_date" label="合同到期日"  required="true" readonly="true"/>
			
			<emp:pop id="IqpBconCoopAgr.desgoods_plan_no" label="订货计划" url="queryIqpDesbuyPlanPop.do" returnMethod="setDesgood" required="true" />
			<emp:text id="IqpBconCoopAgr.low_bail_perc" label="最低保证金比例"  required="false" dataType="Rate" />
			<emp:text id="IqpBconCoopAgr.vigi_line" label="警戒线"  required="false" dataType="Percent" />
			<emp:text id="IqpBconCoopAgr.stor_line" label="平仓线"  required="false" dataType="Percent" />
			<emp:text id="IqpBconCoopAgr.froze_line" label="冻结线"  required="false" dataType="Percent" />		
			<emp:pop id="IqpBconCoopAgr.consign_cus_id" label="收货人" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo4consign" required="false" colSpan="2"/>
			<emp:text id="IqpBconCoopAgr.consign_cus_id_displayname" label="收货人"  required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:pop id="IqpBconCoopAgr.consign_addr_displayname" label="收货地点" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" 
			          returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>		
			<emp:text id="IqpBconCoopAgr.refndmt_acct" label="退款账户"  required="false" />
			<emp:text id="IqpBconCoopAgr.refndmt_acct_name" label="退款账户名称"  required="false" />		
			<emp:textarea id="IqpBconCoopAgr.memo" label="备注" required="false" colSpan="2" />
			<emp:select id="IqpBconCoopAgr.status" label="状态" required="true" dictname="STD_ZB_STATUS" defvalue="1" readonly="true"/>
			<emp:date id="IqpBconCoopAgr.input_date" label="登记日期" required="true" defvalue="$OPENDAY"/>		
			<emp:text id="IqpBconCoopAgr.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="IqpBconCoopAgr.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>	
			<emp:text id="IqpBconCoopAgr.input_id" label="登记人"  required="false" hidden="true"/>
			<emp:text id="IqpBconCoopAgr.input_br_id" label="登记机构" required="false" hidden="true"/>		
			<emp:pop id="IqpBconCoopAgr.consign_addr" label="收货地点" url="" required="false" hidden="true"/>
			<emp:date id="IqpBconCoopAgr.start_date" label="协议起始日期" required="false" hidden="true"/>
			<emp:date id="IqpBconCoopAgr.end_date" label="协议到期日期" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if("haveButton".equals(flag)){ %>
		<emp:button id="close" label="关闭"/>
	    <%} %>
	</div>
</body>
</html>
</emp:page>
