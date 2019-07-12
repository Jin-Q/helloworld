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
	
	function doReturn() {
		var url = '<emp:url action="queryFncLoanList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function checkmoney(){
		var amt=FncLoan.fnc_amt._getValue();
		var blc=FncLoan.fnc_blc._getValue();
		if(amt<blc)
		{
			alert("余额应小于等于金额！");
			FncLoan.fnc_blc._setValue("");
			}
	}
	function checkZero(data){
		var x=data.length;
		if(x !=6){
	 		alert("年月的位数必须为6位,格式为YYYYMM！");
	 		FncLoan.fnc_ym._setValue("");
		}
		else{
			var nn;
			nn=new Date();
			var m = data.substring(4,6);
			var y = Number(data.substring(0,4));
			var ny = nn.getFullYear();
			if(y<=ny){
					if(m>'12'||m=='00'){
					alert("月份应该在1-12月之间！");
					FncLoan.fnc_ym._setValue("");
					}
			}else {		
				alert("年份不应该大于当前年份");
				FncLoan.fnc_ym._setValue("");
			}
			
		}
	}
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="FncLoanGroup" title="主要借款明细" maxColumn="2">
			<emp:pop id="FncLoan.cus_id" label="客户码" url="selectCusInfoPop.do?type=indiv" returnMethod="returnCus"  required="true" />
			<emp:text id="FncLoan.cus_name" label="客户名称" maxlength="80" required="true"/>
			<emp:select id="FncLoan.fnc_per_typ" label="报表周期类型"  required="true" dictname="STD_ZB_FNC_STAT"/>
			<emp:text id="FncLoan.fnc_ym" label="年月" maxlength="6" dataType="Int" required="true" onblur="checkZero(value)"/>
			<emp:text id="FncLoan.fnc_loan_name" label="债权人名称" maxlength="10" required="true" />
			<emp:select id="FncLoan.fnc_loan_typ" label="借款类别"  required="true" dictname="STD_ZB_FNC_LAN_TYP" />
			<emp:textarea id="FncLoan.fnc_loan_use" label="借款用途"  required="false" colSpan="2" />
			
			<emp:text id="FncLoan.fnc_amt" label="金额" maxlength="18" required="true" dataType="Currency"  />
			<emp:text id="FncLoan.fnc_blc" label="余额" maxlength="18" required="true" dataType="Currency" onblur="checkmoney()"/>
			<emp:text id="FncLoan.fnc_unit" label="单位" maxlength="2" required="true" />
			
			<emp:textarea id="FncLoan.remark" label="备注" maxlength="250" required="false" colSpan="2"/>
			<emp:text id="FncLoan.input_id_displayname" label="登记人"  defvalue="$actorname" readonly="true" />
			<emp:text id="FncLoan.input_br_id_displayname" label="登记机构"  defvalue="$organName" readonly="true" required="true" />
			<emp:date  id="FncLoan.input_date" label="登记日期" defvalue="$OPENDAY" readonly="true" required="true" />
			
		
			

				<!-- 下面是真实值字段 -->	
				<emp:text id="FncLoan.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
			    <emp:text id="FncLoan.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			  	<emp:text id="FncLoan.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
			    <emp:text id="FncLoan.last_upd_date" label="更新日期" maxlength="10" required="false" hidden="true"/>
			    <emp:text id="FncLoan.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
