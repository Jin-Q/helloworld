<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryFncAssureList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};		
	function returnCus(data){
		FncAssure.cus_id._setValue(data.cus_id._getValue());
		FncAssure.cus_name._setValue(data.cus_name._getValue());
	};
	function returnRelCus(data){
		FncAssure.rel_cus_id._setValue(data.cus_id._getValue());
		FncAssure.rel_cus_name._setValue(data.cus_name._getValue());
	};
	function checkM(){
		var valAmt= FncAssure.fnc_amt._getValue();//金额
		var valBlc= FncAssure.fnc_blc._getValue();//余额
		var valOpen= FncAssure.fnc_open_amt._getValue();//敞口金额 
			if(parseFloat(valAmt) < parseFloat(valBlc)){
				alert("金额不能小于余额");
				FncAssure.fnc_blc._setValue('');
			}
			if(parseFloat(valAmt) < parseFloat(valOpen)){
				alert("金额不能小于敞口金额 ");
				FncAssure.fnc_open_amt._setValue('');
			}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addFncAssureRecord.do" method="POST">
		
		<emp:gridLayout id="FncAssureGroup" title="主要对外担保及表外业务明细" maxColumn="2">

			<emp:pop id="FncAssure.cus_id" label="客户码" url="selectCusInfoPop.do?type=com" returnMethod="returnCus" required="true" />
			<emp:text id="FncAssure.cus_name" label="客户名称" maxlength="80" required="true" />
			<emp:pop id="FncAssure.rel_cus_id" label="对方客户码" url="selectCusInfoPop.do?type=all" returnMethod="returnRelCus" required="true" />
			<emp:text id="FncAssure.rel_cus_name" label="对方客户名称" maxlength="60" required="true" />
			
			
			<emp:text id="FncAssure.fnc_amt" label="金额" maxlength="18" required="true" onchange="checkM()" dataType="Currency" defvalue="0"/>
			<emp:text id="FncAssure.fnc_blc" label="余额" maxlength="18" required="true" onchange="checkM()" dataType="Currency" defvalue="0"/>
			<emp:text id="FncAssure.fnc_open_amt" label="敞口金额" maxlength="18" required="true" onchange="checkM()" dataType="Currency" defvalue="0"/>
			
			<emp:text id="FncAssure.sort_detail" label="明细类别" maxlength="20" required="true" />
			<emp:textarea id="FncAssure.remark" label="备注" maxlength="100" required="false" colSpan="2"/>
			
		
		   <emp:text id="FncAssure.input_id_displayname" label="登记人"  required="false" defvalue="$actorname" readonly="true" />
			
			<emp:text id="FncAssure.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="FncAssure.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
				<!-- 下面是真实值字段 -->	
			<emp:text id="FncAssure.last_upd_id" label="更新人" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="FncAssure.last_upd_date" label="更新日期" defvalue="$OPENDAY" hidden="true"/>
			
		    <emp:text id="FncAssure.input_id" label="登记人" defvalue="${context.currentUserId}" hidden="true"/>
			<emp:text id="FncAssure.input_br_id" label="登记机构" defvalue="${context.organNo}" hidden="true"/>
			<emp:text id="FncAssure.pk_id" label="主键" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

