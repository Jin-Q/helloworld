<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="iqpBatchMngComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doOnLoad(){
		chageBizType();
	}

	function doReturn() {
		var url = '<emp:url action="queryIqpBatchMngList.do"/>?menuId=queryIqpBatchMng';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:tabGroup mainTab="mainTab" id="mainTabs">
		<emp:tab label="票据批次信息" id="mainTab">
			<emp:form id="submitForm" action="updateIqpBatchMngRecord.do" method="POST">
					<emp:gridLayout id="IqpBatchMngGroup" title="票据批次信息" maxColumn="2">
						<emp:text id="IqpBatchMng.batch_no" label="批次号"  readonly="true" maxlength="40" colSpan="2" required="false" hidden="false"/>
						<emp:select id="IqpBatchMng.bill_type" label="票据种类" readonly="true" required="true" dictname="STD_DRFT_TYPE"/>
						<emp:select id="IqpBatchMng.biz_type" onblur="chageBizType();" label="暂存用途" readonly="true" required="false" dictname="STD_ZB_BUSI_TYPE"/>
						<emp:date id="IqpBatchMng.fore_disc_date" label="转/贴现日期"  onblur="discDateCheck()" required="true" />
						<emp:text id="IqpBatchMng.rate" label="转/再贴现利率" maxlength="10" required="false" dataType="Rate" />
						<emp:date id="IqpBatchMng.rebuy_date" label="回购日期" onblur="rebuyDateCheck()" required="false" />
						<emp:text id="IqpBatchMng.rebuy_rate" label="回购利率" maxlength="10" required="false" dataType="Rate" />
						<emp:text id="IqpBatchMng.rebuy_int" label="回购利息" maxlength="18" required="false" readonly="true" dataType="Currency" />
						<emp:text id="IqpBatchMng.due_rebuy_rate" label="逾期回购利率" maxlength="10" required="false" dataType="Rate" />
						<emp:select id="IqpBatchMng.opp_org_type" label="对手行类型" required="false" dictname="STD_AORG_ACCTSVCR_TYPE" colSpan="2"/>
						<emp:pop id="IqpBatchMng.opp_org_no" label="对手行行号" url="getPrdBankInfoPopList.do" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
						<emp:text id="IqpBatchMng.opp_org_name" label="对手行行名" maxlength="100" readonly="true" required="false" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
						<emp:text id="IqpBatchMng.bill_qnt" label="票据数量" maxlength="38" readonly="true" required="false" dataType="Int" />
						<emp:text id="IqpBatchMng.bill_total_amt" label="票据总金额" maxlength="18" readonly="true" required="false" dataType="Currency" />
						<emp:text id="IqpBatchMng.int_amt" label="利息金额" maxlength="18" required="false" readonly="true" dataType="Currency" />
						<emp:text id="IqpBatchMng.rpay_amt" label="实付金额" maxlength="18" required="false" readonly="true" dataType="Currency" />
						
						<emp:text id="IqpBatchMng.input_id" label="登记人" maxlength="40" hidden="true"  required="false" />
						<emp:text id="IqpBatchMng.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false" />
						<emp:date id="IqpBatchMng.input_date" label="登记日期" hidden="true"  required="false" />
						<emp:select id="IqpBatchMng.status" label="状态" hidden="true" required="false" dictname="STD_BATCH_NUM_STATUS" />
						
						<emp:pop id="IqpBatchMng.manager_id_displayname" label="责任人" hidden="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="false" buttonLabel="选择"/>
						<emp:pop id="IqpBatchMng.manager_br_id_displayname" label="责任机构" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="false" buttonLabel="选择"/>
						<emp:text id="IqpBatchMng.manager_br_id" label="责任机构" hidden="true"/>
						<emp:text id="IqpBatchMng.manager_id" label="责任人" hidden="true"/>
					</emp:gridLayout>
			</emp:form>
			</emp:tab>
			<emp:tab label="票据列表" id="subTab" url="queryIqpBillDetailList.do?batch_no=${context.IqpBatchMng.batch_no}&menuId=queryIqpBatchMng&op=view&subMenuId=queryIqpBillDetail" initial="false" needFlush="true" ></emp:tab>
			<emp:tab label="关联业务信息" id="subTab2" url="queryIqpInfoList.do?batch_no=${context.IqpBatchMng.batch_no}&op=view" initial="false" needFlush="true" ></emp:tab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
