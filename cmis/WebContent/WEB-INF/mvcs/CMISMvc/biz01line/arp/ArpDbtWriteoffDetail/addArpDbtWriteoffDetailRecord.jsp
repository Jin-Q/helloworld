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
	function doLoad(){
		serno = "${context.serno}";
		cus_id = "${context.cus_id}";
		postStr = "&serno="+serno+"&cus_id="+cus_id;
		//modify by jiangcuihua url参数后面带有空格等特殊字符，需要过滤
        var url="<emp:url action='queryArpBadassetAccPop.do'/>&outCondition=and cus_id = '${context.cus_id}' and (table_model='AccPad' or (table_model='AccLoan' and (prd_id <200000 or prd_id in (select prdid from prd_basicinfo where supcatalog = 'PRD20120802669' )))) and cont_no not in ( select ass.cont_no from acc_assetstrsf ass union all select asset.cont_no from acc_asset_trans asset)&returnMethod=selCusId";
        ArpDbtWriteoffDetail.bill_no._obj.config.url = EMPTools.encodeURI(url); 
	};

	/*** 选择借据并校验begin ***/
	function selCusId(data){
		bill_no = data.bill_no._getValue();	//借据编号
		cont_no = data.cont_no._getValue();	//合同编号
		distr_date = data.start_date._getValue();	//起始日期
		end_date = data.end_date._getValue();		//到期日期
		loan_amt = data.bill_amt._getValue();	//借据金额
		loan_balance = data.bill_bal._getValue();	//借据余额
		inner_owe_int = data.inner_owe_int._getValue();	//表内欠息
		out_owe_int = data.out_owe_int._getValue();	//表外欠息
		prd_id_displayname = data.prd_id_displayname._getValue(); //产品名称

		/*** 计算项处理 **/
		owe_int = parseFloat(inner_owe_int)+parseFloat(out_owe_int);		//取：欠息累计 = 表内欠息+表外欠息
		writeoff_cap = loan_balance;										//取：核销本金 = 借据余额
		writeoff_int = owe_int;												//取：核销利息 = 欠息累计
		writeoff_amt = parseFloat(writeoff_cap)+parseFloat(writeoff_int);	//取：核销总金额 = 核销本金 + 核销利息
		
		var url="<emp:url action='checkAssetPreserve.do'/>&type=DbtWriteoffDetail&value="+bill_no;
		doPubCheck(url,result);
	};
	function result(flag){
		if(flag == 'success'){
			ArpDbtWriteoffDetail.bill_no._setValue(bill_no);
			ArpDbtWriteoffDetail.cont_no._setValue(cont_no);
			ArpDbtWriteoffDetail.distr_date._setValue(distr_date);
			ArpDbtWriteoffDetail.end_date._setValue(end_date);	
			ArpDbtWriteoffDetail.loan_amt._setValue(loan_amt);
			ArpDbtWriteoffDetail.loan_balance._setValue(loan_balance);
			ArpDbtWriteoffDetail.owe_int._setValue(owe_int+"");
			ArpDbtWriteoffDetail.inner_owe_int._setValue(inner_owe_int);
			ArpDbtWriteoffDetail.out_owe_int._setValue(out_owe_int);
			ArpDbtWriteoffDetail.writeoff_cap._setValue(writeoff_cap+"");
			ArpDbtWriteoffDetail.writeoff_int._setValue(writeoff_int+"");
			ArpDbtWriteoffDetail.writeoff_amt._setValue(writeoff_amt+"");
			ArpDbtWriteoffDetail.prd_type._setValue(prd_id_displayname);
		}else{
			alert("此借据已发起过呆账核销!");
		}
	};
	
	function doReturn() {
		var url = '<emp:url action="queryArpDbtWriteoffDetailList.do"/>'+postStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpDbtWriteoffDetail);
	};
	/*** 选择借据并校验end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addArpDbtWriteoffDetailRecord.do" method="POST">		
		<emp:gridLayout id="ArpDbtWriteoffDetailGroup" title="呆账核销明细" maxColumn="2">
			<emp:text id="ArpDbtWriteoffDetail.serno" label="业务编号" maxlength="40" hidden="true"  defvalue="${context.serno}" />
			<emp:text id="ArpDbtWriteoffDetail.pk_serno" label="流水号" maxlength="40" hidden="true" />
			<!-- add by yangzy 2015-6-24  需求编号【XD150625044】贸易融资呆账核销改造 begin -->
			<emp:pop id="ArpDbtWriteoffDetail.bill_no" label="借据编号" required="true" readonly="false"
			url="queryArpBadassetAccPop.do?outCondition=and cus_id = '${context.cus_id}' and (table_model='AccPad' or (table_model='AccLoan' and (prd_id <200000 or prd_id in (select prdid from prd_basicinfo where supcatalog = 'PRD20120802669')))) and cont_no not in(select ass.cont_no from acc_assetstrsf ass union all select asset.cont_no from acc_asset_trans asset)&returnMethod=selCusId" />
			<!-- add by yangzy 2015-6-24  需求编号【XD150625044】贸易融资呆账核销改造 end -->
			<emp:text id="ArpDbtWriteoffDetail.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="ArpDbtWriteoffDetail.prd_type" label="产品类型" readonly="true"  colSpan="2"/>
			<emp:date id="ArpDbtWriteoffDetail.distr_date" label="起始日期" required="false" />
			<emp:date id="ArpDbtWriteoffDetail.end_date" label="到期日期" required="false" />
			<emp:text id="ArpDbtWriteoffDetail.loan_amt" label="借据金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.loan_balance" label="借据余额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.inner_owe_int" label="表内欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.out_owe_int" label="表外欠息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.owe_int" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtWriteoffDetail.writeoff_cap" label="核销本金" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.writeoff_int" label="核销利息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffDetail.writeoff_amt" label="核销总金额" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
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