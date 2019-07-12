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
		doPubUpdate(ArpLawSecondInstance);
	};
	/****** 各种日期校验 *******/
	function checkDate(obj){
		if(obj.value){
			var court_date = ArpLawSecondInstance.court_date._getValue();	//二审开庭日期
			var close_case_date = ArpLawSecondInstance.close_case_date._getValue();	//结案日期
			var law_writ_inure_date = ArpLawSecondInstance.law_writ_inure_date._getValue();	//法律文书生效日期
		
			/*** 二审开庭日期 < 结案日期 < 法律文书生效日期 ***/
			if(obj.name == 'ArpLawSecondInstance.court_date'){
				if( ( close_case_date <= court_date && close_case_date != '' ) 
						|| (court_date >=law_writ_inure_date && law_writ_inure_date != '' )){
					obj.value = '';
					alert("[二审开庭日期]应该小于[结案日期]与[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawSecondInstance.close_case_date'){
				if(( court_date != '' && close_case_date <= court_date )
						||( law_writ_inure_date != '' && close_case_date >law_writ_inure_date )){
					obj.value = '';
					alert("[结案日期]应该大于[二审开庭日期]小于[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawSecondInstance.law_writ_inure_date'){
				if(( court_date != '' && law_writ_inure_date <= court_date )
						||( close_case_date != '' && close_case_date >law_writ_inure_date )){
					obj.value = '';
					alert("[法律文书生效日期]应该大于[二审开庭日期]与[结案日期]");
		   		}
			}
		}		
	};

	/*** 代理信息控制 ***/
	function checkLawyer(){
		agt_type = ArpLawSecondInstance.agt_type._getValue();
		if(agt_type == '002'){
			ArpLawSecondInstance.lawyer_name._obj._renderHidden(false);
			ArpLawSecondInstance.lawyer_link_mode._obj._renderHidden(false);
			ArpLawSecondInstance.belg_office._obj._renderHidden(false);
		}else{
			ArpLawSecondInstance.lawyer_name._setValue('');
			ArpLawSecondInstance.lawyer_link_mode._setValue('');
			ArpLawSecondInstance.belg_office._setValue('');
			
			ArpLawSecondInstance.lawyer_name._obj._renderHidden(true);
			ArpLawSecondInstance.lawyer_link_mode._obj._renderHidden(true);
			ArpLawSecondInstance.belg_office._obj._renderHidden(true);
		}
	};
	function doLoad(){
		checkLawyer();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateArpLawSecondInstanceRecord.do" method="POST">
		<emp:gridLayout id="ArpLawSecondInstanceGroup" maxColumn="2" title="基本信息">
			<emp:text id="ArpLawSecondInstance.case_no" label="案件编号" maxlength="40" required="true" readonly="true" hidden="true" colSpan="2"/>
			<emp:text id="ArpLawSecondInstance.appellor" label="上诉人" maxlength="80" required="true"/>
			<emp:select id="ArpLawSecondInstance.lawsuit_type" label="诉讼类型" required="true" dictname="STD_ZB_LAWSUIT_TYPE" />
			<emp:textarea id="ArpLawSecondInstance.charge_reason" label="起诉原因" maxlength="250" required="false" colSpan="2" />
			<emp:text id="ArpLawSecondInstance.second_court" label="二审法院" maxlength="80" required="false" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawSecondInstance.second_judge" label="二审法官" maxlength="80" required="false" />
			<emp:select id="ArpLawSecondInstance.agt_type" label="代理方式" required="false" dictname="STD_ZB_AGT_TYPE_FIRST" colSpan="2" onchange="checkLawyer()"/>
			<emp:text id="ArpLawSecondInstance.lawyer_name" label="律师姓名" maxlength="80" required="false" />
			<emp:text id="ArpLawSecondInstance.lawyer_link_mode" label="律师联系方式" maxlength="30" required="false" dataType="Phone" />
			<emp:text id="ArpLawSecondInstance.belg_office" label="所属事务所" maxlength="80" required="false" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawFirstInstanceGroup" maxColumn="2" title="审理信息">
			<emp:date id="ArpLawSecondInstance.court_date" label="二审开庭日期" required="false" colSpan="2" onblur="checkDate(this)"/>
			<emp:date id="ArpLawSecondInstance.close_case_date" label="结案日期" required="false" onblur="checkDate(this)"/>
			<emp:select id="ArpLawSecondInstance.close_case_mode" label="结案方式" required="false" dictname="STD_ZB_CLOSE_MODE" />			
			<emp:select id="ArpLawSecondInstance.judge_result" label="二审判决结果" required="false" dictname="STD_ZB_JUDGE_RESULT" colSpan="2"/>
			<emp:date id="ArpLawSecondInstance.law_writ_inure_date" label="法律文书生效日期" required="false" onblur="checkDate(this)"/>
			<emp:text id="ArpLawSecondInstance.law_writ_no" label="法律文书编号" maxlength="40" required="true" />
			<emp:textarea id="ArpLawSecondInstance.judgment_summary" label="判决内容摘要" maxlength="250" required="false" colSpan="2" />
			<emp:textarea id="ArpLawSecondInstance.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawSecondInstanceGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawSecondInstance.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpLawSecondInstance.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpLawSecondInstance.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpLawSecondInstance.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpLawSecondInstance.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="submits" label="保存" op="save"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
