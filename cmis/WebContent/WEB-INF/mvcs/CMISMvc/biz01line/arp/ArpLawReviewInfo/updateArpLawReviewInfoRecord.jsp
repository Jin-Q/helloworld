<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}
	}	
%>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 100%;
};
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doSubmits(){
		doPubUpdate(ArpLawReviewInfo);
	};
	/****** 各种日期校验 *******/
	function checkDate(obj){
		if(obj.value){
			var court_date = ArpLawReviewInfo.court_date._getValue();	//再审开庭日期
			var judgment_date = ArpLawReviewInfo.judgment_date._getValue();	//再审判决(裁定)日期
			var law_writ_inure_date = ArpLawReviewInfo.law_writ_inure_date._getValue();	//法律文书生效日期
		
			/*** 再审开庭日期 < 再审判决(裁定)日期 < 法律文书生效日期 ***/
			if(obj.name == 'ArpLawReviewInfo.court_date'){
				if( ( judgment_date <= court_date && judgment_date != '' ) 
						|| (court_date >=law_writ_inure_date && law_writ_inure_date != '' )){
					obj.value = '';
					alert("[再审开庭日期]应该小于[再审判决(裁定)日期]与[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawReviewInfo.judgment_date'){
				if(( court_date != '' && judgment_date <= court_date )
						||( law_writ_inure_date != '' && judgment_date >=law_writ_inure_date )){
					obj.value = '';
					alert("[再审判决(裁定)日期]应该大于[再审开庭日期]小于[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawReviewInfo.law_writ_inure_date'){
				if(( court_date != '' && law_writ_inure_date <= court_date )
						||( judgment_date != '' && judgment_date > law_writ_inure_date )){
					obj.value = '';
					alert("[法律文书生效日期]应该大于[再审开庭日期]与[再审判决(裁定)日期]");
		   		}
			}
		}		
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateArpLawReviewInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpLawReviewInfoGroup" maxColumn="2" title="再审情况">
			<emp:text id="ArpLawReviewInfo.case_no" label="案件编号" maxlength="40" required="true" hidden="true" colSpan="2"/>
			<emp:text id="ArpLawReviewInfo.proposer" label="申请人" maxlength="80" required="true" />
			<emp:select id="ArpLawReviewInfo.review_reason" label="再审原因" required="true" dictname="STD_ZB_REVIEW_REASON" />
			<emp:text id="ArpLawReviewInfo.review_court" label="再审法院" maxlength="80" required="true" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawReviewInfo.main_judge" label="主审法官" maxlength="80" required="false" colSpan="2" />			
			<emp:date id="ArpLawReviewInfo.court_date" label="再审开庭日期" required="false" onblur="checkDate(this)"/>
			<emp:date id="ArpLawReviewInfo.judgment_date" label="再审判决(裁定)日期" required="false" onblur="checkDate(this)" colSpan="2"/>
			<emp:date id="ArpLawReviewInfo.law_writ_inure_date" label="法律文书生效日期" required="false" onblur="checkDate(this)"/>
			<emp:text id="ArpLawReviewInfo.law_writ_no" label="法律文书编号" maxlength="40" required="true" />
			<emp:select id="ArpLawReviewInfo.whether_appeal" label="是否上诉" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="ArpLawReviewInfo.whether_final_judgment" label="是否为终审判决(裁定)" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="ArpLawReviewInfo.judgment_summary" label="判决内容摘要" maxlength="250" required="false" colSpan="2" />			
			<emp:textarea id="ArpLawReviewInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawReviewInfoGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawReviewInfo.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpLawReviewInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpLawReviewInfo.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpLawReviewInfo.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpLawReviewInfo.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="submits" label="保存" op="save"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
