<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturnCusIndivIncome(){
		goback();
	}
	
	function goback(){
		var editFlag = '${context.EditFlag}';
		var paramStr="CusIndivIncome.cus_id="+CusIndivIncome.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusIndivIncomeList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivIncomeGroup" title="个人收入情况" maxColumn="2">
		<emp:text id="CusIndivIncome.indiv_sur_year" label="调查年份" maxlength="4" required="true" />
		<emp:select id="CusIndivIncome.indiv_deposits" label="收入来源" required="true" dictname="STD_ZB_INDIV_DEPOS"/>
		<emp:text id="CusIndivIncome.indiv_ann_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency" />
		<emp:textarea id="CusIndivIncome.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CusIndivIncome.input_id_displayname" label="登记人"  hidden="true" />
		<emp:text id="CusIndivIncome.input_br_id_displayname" label="登记机构"  hidden="true" />
		<emp:date id="CusIndivIncome.input_date" label="登记日期" hidden="true" />
		<emp:text id="CusIndivIncome.last_upd_id_displayname" label="更新人"  hidden="true" />
		<emp:date id="CusIndivIncome.last_upd_date" label="更新日期" hidden="true" />
		<emp:text id="CusIndivIncome.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		<emp:text id="CusIndivIncome.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
		<emp:text id="CusIndivIncome.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		<emp:text id="CusIndivIncome.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivIncome" label="返回"/>
	</div>
</body>
</html>
</emp:page>