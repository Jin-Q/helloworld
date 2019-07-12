<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String case_no= (String)request.getParameter("case_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 99%;
};
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function doLoad(){
		checkCompromise();
	};
	
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,ArpLawExecuteInfo);
	};

	function doReturn() {
		var url = '<emp:url action="queryArpLawExecuteInfoList.do"/>?case_no=<%=case_no%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/***和解信息控制 ***/
	function checkCompromise(){
		whether_compromise = ArpLawExecuteInfo.whether_compromise._getValue();		
		if(whether_compromise == '1'){
			ArpLawExecuteInfo.compromise_date._obj._renderHidden(false);
			ArpLawExecuteInfo.compromise_agr_summary._obj._renderHidden(false);
		}else{
			ArpLawExecuteInfo.compromise_date._setValue('');
			ArpLawExecuteInfo.compromise_agr_summary._setValue('');
			ArpLawExecuteInfo.compromise_date._obj._renderHidden(true);
			ArpLawExecuteInfo.compromise_agr_summary._obj._renderHidden(true);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addArpLawExecuteInfoRecord.do" method="POST">
		
		<emp:gridLayout id="ArpLawExecuteInfoGroup" title="执行情况" maxColumn="2">
			<emp:text id="ArpLawExecuteInfo.serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpLawExecuteInfo.case_no" label="案件编号" maxlength="40" required="true" defvalue="<%=case_no%>" hidden="true"/>
			<emp:select id="ArpLawExecuteInfo.lawsuit_phase" label="当前诉讼阶段" required="true" dictname="STD_ZB_LAWSUIT_PHASE" />
			<emp:textarea id="ArpLawExecuteInfo.exe_person" label="被执行人" maxlength="80" required="true" colSpan="2" />
			<emp:text id="ArpLawExecuteInfo.exe_totl_sub" label="执行总标的" maxlength="16" required="true" dataType="Currency" />
			<emp:select id="ArpLawExecuteInfo.exe_status" label="执行现状" required="true" dictname="STD_ZB_EXE_STATUS" />
			<emp:text id="ArpLawExecuteInfo.exe_court" label="执行法院" maxlength="80" required="true" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawExecuteInfo.exe_judge" label="执行法官" maxlength="80" required="false" colSpan="2"/>
			<emp:text id="ArpLawExecuteInfo.law_writ_no" label="执行法律文书号" maxlength="40" required="false" />
			<emp:date id="ArpLawExecuteInfo.exe_start_date" label="执行阶段起始日期" required="false" />
			<emp:textarea id="ArpLawExecuteInfo.judgment_summary" label="执行法律文书判决内容" maxlength="250" required="false" colSpan="2" />			
			<emp:select id="ArpLawExecuteInfo.whether_compromise" label="是否和解" required="false" dictname="STD_ZX_YES_NO" onchange="checkCompromise()" />
			<emp:date id="ArpLawExecuteInfo.compromise_date" label="和解日期" required="false" />
			<emp:textarea id="ArpLawExecuteInfo.compromise_agr_summary" label="执行和解协议内容" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawExecuteInfoGroup" maxColumn="2" title="资产信息">
			<emp:select id="ArpLawExecuteInfo.exe_property_type" label="执行财产类型" required="false" dictname="STD_ZB_EXE_PROPERTY_TYPE" />
			<emp:text id="ArpLawExecuteInfo.exe_property_name" label="执行财产名称" maxlength="80" required="false" />
			<emp:text id="ArpLawExecuteInfo.extr_org" label="评估机构" maxlength="80" required="false" />
			<emp:text id="ArpLawExecuteInfo.extr_value" label="评估标的物价值" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpLawExecuteInfo.auction_org" label="拍卖机构" maxlength="80" required="false" />
			<emp:text id="ArpLawExecuteInfo.auction_closing_cost" label="拍卖成交价" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpLawExecuteInfo.eval_fee" label="评估费用" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpLawExecuteInfo.auction_fee" label="拍卖费用" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpLawExecuteInfo.other_fee" label="其他费用" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="ArpLawExecuteInfo.exe_fee" label="执行费用" maxlength="16" required="false" dataType="Currency" />		
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawExecuteInfoGroup" maxColumn="2" title="执行中止情况">
			<emp:date id="ArpLawExecuteInfo.exe_suspend_date" label="执行中止日期" required="false" />
			<emp:textarea id="ArpLawExecuteInfo.exe_suspend_reason" label="执行中止原因" maxlength="250"  required="false" colSpan="2" />
			<emp:text id="ArpLawExecuteInfo.suspend_writ_no" label="中止裁定文书号" maxlength="40" required="false" />
			<emp:date id="ArpLawExecuteInfo.suspend_post_date" label="中止裁定送达日期" required="false" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawExecuteInfoGroup" maxColumn="2" title="执行终结情况">
			<emp:date id="ArpLawExecuteInfo.exe_end_date" label="案件执行终结日期" required="false" />
			<emp:textarea id="ArpLawExecuteInfo.exe_end_reason" label="执行终结原因" maxlength="250" required="false" colSpan="2" />
			<emp:text id="ArpLawExecuteInfo.exe_end_writ_no" label="执行终结文书号" maxlength="40" required="false" />
			<emp:date id="ArpLawExecuteInfo.exe_end_post_date" label="终结裁定送达日期" required="false" />
			<emp:textarea id="ArpLawExecuteInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawExecuteInfoGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawExecuteInfo.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpLawExecuteInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpLawExecuteInfo.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpLawExecuteInfo.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpLawExecuteInfo.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
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