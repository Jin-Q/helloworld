<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String acc = "";
if(context.containsKey("acc")){
	acc = (String)context.getDataValue("acc");
}
String prd_id = "";
if(context.containsKey("AccPad.prd_id")){
	prd_id = (String)context.getDataValue("AccPad.prd_id");
}
String biz_type = "";
if(context.containsKey("biz_type")){
	biz_type = (String)context.getDataValue("biz_type");
}
String isSpecialAcc = "";
if(context.containsKey("isSpecialAcc")){
	isSpecialAcc = (String)context.getDataValue("isSpecialAcc");
}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryArpDbtWriteoffAccList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		<%if("accpad".equals(acc)){ %>
		addCusForm(AccPad);
		addContForm(AccPad);
		addBillForm(AccPad);
		<%}else{ %>
		addCusForm(AccLoan);
		addContForm(AccLoan);
		addBillForm(AccLoan);
		<%}%>
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
<%if("accpad".equals(acc)){ %>
	<emp:tabGroup mainTab="base_tab" id="mainTab">
   <emp:tab label="垫款台账信息" id="base_tab" needFlush="true" initial="true" >
	<emp:gridLayout id="AccPadGroup" title="基本信息" maxColumn="2">
	        <emp:text id="AccPad.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccPad.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="AccPad.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccPad.prd_id_displayname" label="产品名称" required="false" />
			<emp:text id="AccPad.cus_id" label="客户码" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="AccPad.cus_id_displayname" label="客户名称" required="false" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="AccPad.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="AccPad.pad_bill_no" label="垫款业务借据编号" maxlength="40" required="false" hidden="true"/>
	    </emp:gridLayout>		
	    <emp:gridLayout id="AccPadGroup" title="金额信息" maxColumn="2">	
	        <emp:select id="AccPad.pad_type" label="垫款种类" required="false" dictname="STD_ZB_PAD_TYPE"/>
			<emp:select id="AccPad.pad_cur_type" label="垫款币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccPad.pad_amt" label="垫款金额" maxlength="18" required="false" dataType="Currency" />
	        <emp:text id="AccPad.pad_bal" label="垫款余额" maxlength="18" required="false" dataType="Currency" />
	    </emp:gridLayout>		
	    <emp:gridLayout id="AccPadGroup" title="日期信息" maxColumn="2">	
			<emp:date id="AccPad.pad_date" label="垫款日期" required="false" />
			<emp:date id="AccPad.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccPad.writeoff_date" label="核销日期" required="false" />
			<emp:text id="AccPad.acc_day" label="日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="AccPad.acc_year" label="年份" maxlength="5" required="false" hidden="true"/>
			<emp:text id="AccPad.acc_mon" label="月份" maxlength="5" required="false" hidden="true"/>
	    </emp:gridLayout>		
	    <emp:gridLayout id="AccLoanGroup" maxColumn="2" title="风险分类信息">
		    <emp:select id="AccPad.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccPad.twelve_cls_flg" label="十二级分类标志" required="false" dictname="STD_ZB_TWELVE_CLASS"/>
		    <emp:date id="AccPad.twelve_class_time" label="十二级分类时间" required="false" />
		</emp:gridLayout>
	    <emp:gridLayout id="AccPadGroup" title="其他信息" maxColumn="2">	
			<emp:text id="AccPad.manager_br_id_displayname" label="管理机构" required="false" />
			<emp:text id="AccPad.fina_br_id_displayname" label="账务机构" required="false" />
			<emp:text id="AccPad.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="AccPad.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true"/>
			<emp:select id="AccPad.accp_status" label="台账状态" required="false" dictname="STD_ZB_ACC_TYPE"/>
	  </emp:gridLayout>
	  <emp:gridLayout id="ArpDbtWriteoffAccGroup" title="呆账核销台账信息" maxColumn="2">
			<emp:text id="ArpDbtWriteoffAcc.writeoff_co_amt" label="核销总金额" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtWriteoffAcc.writeoff_cap" label="核销本金" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffAcc.writeoff_int" label="核销利息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffAcc.bill_writeoff_date" label="核销日期" maxlength="10" required="false" />
			<emp:text id="ArpDbtWriteoffAcc.writeoff_org_displayname" label="核销机构"  required="false" />
			<emp:text id="ArpDbtWriteoffAcc.writeoff_cap_bal" label="核销本金余额" maxlength="16" required="false" dataType="Currency" defvalue="0.00" hidden="true"/>
			<emp:text id="ArpDbtWriteoffAcc.writeoff_int_bal" label="核销利息余额" maxlength="16" required="false" dataType="Currency" defvalue="0.00" hidden="true"/>
	</emp:gridLayout>
   </emp:tab>
   <%if("600020".equals(prd_id)){ %>
	    <emp:tab label="资产转受让合同信息" id="assetContSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrAssetstrsfContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
		<%}else if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){%>
		<emp:tab label="转贴现合同信息" id="rpContsubTab" url="getCtrRpddscntContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrRpddscntContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	    <%}else if("8".equals(biz_type) && ("300021".equals(prd_id)||"300020".equals(prd_id))){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else if("8".equals(biz_type) && !"300021".equals(prd_id) && !"300020".equals(prd_id)){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if("300021".equals(prd_id)||"300020".equals(prd_id)){%> 
	     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else{%>
	     <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}%>

</emp:tabGroup>	
	
<%}else{ %>
<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="台账信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="基本信息">
			<emp:text id="AccLoan.bill_no" label="借据编号" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="AccLoan.serno" label="出账流水号" maxlength="40" required="true" readonly="true" />		
			<emp:text id="AccLoan.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccLoan.prd_id_displayname" label="产品名称" required="false" />
			<emp:text id="AccLoan.cus_id" label="客户码" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="AccLoan.cus_id_displayname" label="客户名称" required="false" cssElementClass="emp_field_text_long_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="金额信息">
			<emp:select id="AccLoan.cur_type" label="币种"  required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccLoan.loan_amt" label="贷款金额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="AccLoan.ruling_ir" label="基准利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="AccLoan.reality_ir_y" label="执行年利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="AccLoan.overdue_rate_y" label="逾期利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="AccLoan.default_rate_y" label="违约利率" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="AccLoan.inner_owe_int" label="表内欠息" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.out_owe_int" label="表外欠息" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.rec_int_accum" label="应收利息累计" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.recv_int_accum" label="实收利息累计" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="AccLoan.loan_balance" label="贷款余额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.normal_balance" label="正常余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.overdue_balance" label="逾期余额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.comp_int_balance" label="复利余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.slack_balance" label="呆滞余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccLoan.bad_dbt_balance" label="呆账余额" maxlength="18" hidden="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="日期信息">	
			<emp:text id="AccLoan.post_count" label="展期次数" maxlength="38" required="false" />
			<emp:text id="AccLoan.overdue" label="逾期期数" maxlength="38" required="false" />
			<emp:date id="AccLoan.distr_date" label="起始日期" required="false" />
			<emp:date id="AccLoan.end_date" label="到期日期" required="false" />
			<emp:date id="AccLoan.ori_end_date" label="原到期日期" required="false" />
			<emp:date id="AccLoan.overdue_date" label="逾期日期" required="false" />
			<emp:date id="AccLoan.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccLoan.settl_date" label="结清日期" required="false" />
			<emp:date id="AccLoan.writeoff_date" label="核销日期" required="true" />			
			<emp:date id="AccLoan.paydate" label="转垫款日" required="true" />
			
			
			<emp:text id="AccLoan.acc_day" label="日期" maxlength="10" required="true" hidden="true"/>
			<emp:text id="AccLoan.acc_year" label="年份" maxlength="5" required="true" hidden="true"/>
			<emp:text id="AccLoan.acc_mon" label="月份" maxlength="5" required="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="风险分类信息">
		    <emp:select id="AccLoan.five_class" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccLoan.twelve_cls_flg" label="十二级分类标志" required="false" dictname="STD_ZB_TWELVE_CLASS"/>
		    <emp:date id="AccLoan.twelve_class_time" label="十二级分类时间" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="AccLoanGroup" maxColumn="2" title="其他信息">		
			<emp:text id="AccLoan.manager_br_id_displayname" label="管理机构" required="false" />
			<emp:text id="AccLoan.fina_br_id_displayname" label="账务机构" required="false" />
			<emp:text id="AccLoan.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="AccLoan.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true" />
			<emp:select id="AccLoan.acc_status" label="台账状态" required="true" dictname="STD_ZB_ACC_TYPE"/> 
			<emp:text id="AccLoan.fount_serno" label="业务申请编号" hidden="true" />
		</emp:gridLayout>
	
	<emp:gridLayout id="ArpDbtWriteoffAccGroup" title="呆账核销台账信息" maxColumn="2">
			<emp:text id="ArpDbtWriteoffAcc.writeoff_co_amt" label="核销总金额" maxlength="16" required="false" dataType="Currency" colSpan="2"/>
			<emp:text id="ArpDbtWriteoffAcc.writeoff_cap" label="核销本金" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffAcc.writeoff_int" label="核销利息" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpDbtWriteoffAcc.bill_writeoff_date" label="核销日期" maxlength="10" required="false" />
			<emp:text id="ArpDbtWriteoffAcc.writeoff_org_displayname" label="核销机构"  required="false" />
			<emp:text id="ArpDbtWriteoffAcc.writeoff_cap_bal" label="核销本金余额" maxlength="16" required="false" dataType="Currency" defvalue="0.00" hidden="true"/>
			<emp:text id="ArpDbtWriteoffAcc.writeoff_int_bal" label="核销利息余额" maxlength="16" required="false" dataType="Currency" defvalue="0.00" hidden="true"/>
	</emp:gridLayout>
	  </emp:tab>
	    <%if("AccAssetstrsf".equals(isSpecialAcc)){%>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrAssetstrsfHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if("AccAssetTrans".equals(isSpecialAcc)){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrAssetProContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=zczqhxmcx&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if("8".equals(biz_type) && (prd_id.equals("300021")||prd_id.equals("300020"))){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else if("8".equals(biz_type) && !prd_id.equals("300021") && !prd_id.equals("300020")){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if(prd_id.equals("300021")||prd_id.equals("300020")){%> 
	     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else{%>
	     <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccLoan.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}%>
      <emp:ExtActTab></emp:ExtActTab>
      
    </emp:tabGroup>
<%} %>
	
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>