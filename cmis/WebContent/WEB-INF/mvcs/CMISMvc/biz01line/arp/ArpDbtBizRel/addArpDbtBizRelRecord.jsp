<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryArpDbtBizRelList.do"/>'+postStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpDbtBizRel);
	};

	/*** 选择借据并校验begin ***/
	function selCusId(data){
		bill_no = data.bill_no._getValue();	//借据编号
		cont_no = data.cont_no._getValue();	//合同编号
		distr_date = data.start_date._getValue();	//起始日期
		end_date = data.end_date._getValue();		//到期日期
		loan_amt = data.bill_amt._getValue();	//借据金额
		loan_balance = data.bill_bal._getValue();	//借据余额
		rec_int_accum = data.rec_int_accum._getValue();	//应收利息累计
		recv_int_accum = data.recv_int_accum._getValue();	//实收利息累计
		inner_owe_int = data.inner_owe_int._getValue();	//表内欠息
		out_owe_int = data.out_owe_int._getValue();	//表外欠息
		owe_int = parseFloat(inner_owe_int)+parseFloat(out_owe_int);	//取：欠息累计 = 表内欠息+表外欠息
		normal_balance = data.normal_balance._getValue();	//正常余额
		overdue_balance = data.overdue_balance._getValue();	//逾期余额
		slack_balance = data.slack_balance._getValue();	//呆滞余额
		bad_dbt_balance = data.bad_dbt_balance._getValue();	//呆账余额
		five_class = data.five_class._getValue();	//五级分类
		twelve_cls_flg = data.twelve_cls_flg._getValue();	//十二级分类
		prd_id_displayname = data.prd_id_displayname._getValue(); //产品名称
		four_class = "";
		if(normal_balance > 0){/*** 四级分类计算 ***/
			four_class = "1";
		}else if(overdue_balance > 0){
			four_class = "3";
		}else if(slack_balance > 0){
			four_class = "7";
		}else if(bad_dbt_balance > 0){
			four_class = "8";
		}

		var url="<emp:url action='checkAssetPreserve.do'/>&type=DbtCongnizDetail&value="+bill_no;
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpDbtBizRel.bill_no._setValue(bill_no);
			ArpDbtBizRel.cont_no._setValue(cont_no);
			ArpDbtBizRel.distr_date._setValue(distr_date);
			ArpDbtBizRel.end_date._setValue(end_date);	
			ArpDbtBizRel.loan_amt._setValue(loan_amt);
			ArpDbtBizRel.loan_balance._setValue(loan_balance);
			ArpDbtBizRel.rec_int_accum._setValue(rec_int_accum);
			ArpDbtBizRel.recv_int_accum._setValue(recv_int_accum);
			ArpDbtBizRel.owe_int._setValue(owe_int+"");
			ArpDbtBizRel.four_class._setValue(four_class);
			ArpDbtBizRel.five_class._setValue(five_class);
			ArpDbtBizRel.twelve_class._setValue(twelve_cls_flg);
			ArpDbtBizRel.prd_type._setValue(prd_id_displayname);
		}else{
			alert("此借据已发起过呆账认定!");
		}
	};
	/*** 选择借据并校验end ***/
	
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addArpDbtBizRelRecord.do" method="POST">
		
		<emp:gridLayout id="ArpDbtBizRelGroup" title="呆账业务关系" maxColumn="2">
			<emp:text id="ArpDbtBizRel.serno" label="业务编号" maxlength="40" hidden="true" defvalue="${context.serno}"/>
			<emp:text id="ArpDbtBizRel.pk_serno" label="流水号" maxlength="40" hidden="true" />
			<emp:pop id="ArpDbtBizRel.bill_no" label="借据编号" required="true" readonly="false"
			url="queryArpBadassetAccPop.do?outCondition=and (bill_bal%2Binner_owe_int%2Bout_owe_int)>0 and cus_id='${context.cus_id}'&returnMethod=selCusId" />
			<emp:text id="ArpDbtBizRel.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpDbtBizRel.prd_type" label="产品类型" readonly="true"  colSpan="2"/>
			<emp:date id="ArpDbtBizRel.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpDbtBizRel.end_date" label="到期日期" required="false" />
			<emp:text id="ArpDbtBizRel.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtBizRel.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtBizRel.owe_int" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtBizRel.rec_int_accum" label="应收利息累计" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtBizRel.recv_int_accum" label="实收利息累计" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="ArpDbtBizRel.four_class" label="四级分类" required="false" dictname="STD_ZB_FOUR_SORT" />
			<emp:select id="ArpDbtBizRel.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="ArpDbtBizRel.twelve_class" label="十二级分类" required="false" dictname="STD_ZB_TWELVE_CLASS" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>