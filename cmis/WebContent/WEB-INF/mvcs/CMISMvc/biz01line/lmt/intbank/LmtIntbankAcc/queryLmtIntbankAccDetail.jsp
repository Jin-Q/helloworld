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
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIntbankAccList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
        window.close();
	};
	function doLoad()
	{
		LmtIntbankAcc.cus_id._obj.addOneButton("cus_id","查看",queryCusForm);
	}
	function queryCusForm()
	{
		var cus_id = LmtIntbankAcc.cus_id._getValue();
		var url = '<emp:url action="getIntbankViewPage.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	}
	

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup id="LmtIntbankAcc_tabs" mainTab="LmtIntbankAcc_tabs">
	<emp:tab id="LmtIntbankAcc_tabs" label="同业授信额度台帐" needFlush="true" initial="true">
	
	<emp:gridLayout id="LmtIntbankAccGroup" title="客户额度综合信息" maxColumn="2">
  		<emp:text id="LmtIntbankAcc.agr_no" label="协议编号" maxlength="32" required="true" readonly="true" colSpan="2" />
		<emp:text id="LmtIntbankAcc.serno" label="业务编号" maxlength="32" required="true" readonly="true" colSpan="2" hidden="true" />
		<emp:text id="LmtIntbankAcc.batch_cus_no" label="批量客户编号" maxlength="32" required="false" readonly="true"/>	
		<emp:text id="LmtIntbankAcc.same_org_cnname" label="客户名称" required="false" cssElementClass="emp_field_text_long_readonly" colSpan="2"/>
		<emp:select id="LmtIntbankAcc.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" />
		<emp:select id="LmtIntbankAcc.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="LmtIntbankAcc.lmt_amt" label="授信总额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtIntbankAcc.bal_amt" label="可用额度(元)" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtIntbankAcc.froze_amt" label="冻结额度(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="LmtIntbankAcc.unfroze_amt" label="解冻额度(元)" maxlength="18" required="false" hidden="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:date id="LmtIntbankAcc.start_date" label="授信起始日期" required="false" />
		<emp:date id="LmtIntbankAcc.end_date" label="授信到期日期" required="false" />
		<emp:text id="LmtIntbankAcc.manager_id_displayname" label="责任人" required="false" />
		<emp:text id="LmtIntbankAcc.manager_br_id_displayname" label="管理机构" required="false" />
		<emp:select id="LmtIntbankAcc.lmt_status" label="额度状态" required="false" dictname="STD_LMT_STATUS" />
		<emp:date id="LmtIntbankAcc.break_date" label="终止日期" required="false" hidden="true"/>
		<emp:text id="LmtIntbankAcc.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
		<emp:pop id="LmtIntbankAcc.manager_br_id" label="管理机构" url="null" required="false" hidden="true"/>
		
		<emp:text id="LmtIntbankAcc.cus_id" label="客户码" maxlength="32" required="false" hidden="true"/>
	</emp:gridLayout>     
	<emp:table icollName="LmtIntbankDetailList" pageMode="false" url="pageQueryLmtIntbankAccDetail.do?serno=${context.LmtIntbankAcc.serno}">
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称"/>
		<emp:text id="variet_no" label="品种编号" />
		<emp:text id="variet_name" label="品种名称" />
		<emp:text id="lmt_amt" label="授信额度(元)"  dataType="Currency"/>
	</emp:table>	
	</emp:tab>	
	<emp:ExtActTab></emp:ExtActTab> 
	</emp:tabGroup>

	<div align="center">
	  <%if("LmtIntbankView".equals(flag)){%>
		<emp:button id="return" label="返回列表"/>
	  <%}else{%>
	    <emp:button id="close" label="关闭"/>
	  <%} %>
	</div>

</body>
</html>
</emp:page>
