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
		doPubUpdate(ArpLawFirstInstance);
	};
	/*** 代理信息控制 ***/
	function checkLawyer(){
		agt_type = ArpLawFirstInstance.agt_type._getValue();
		if(agt_type == '002'){
			ArpLawFirstInstance.lawyer_name._obj._renderHidden(false);
			ArpLawFirstInstance.lawyer_link_mode._obj._renderHidden(false);
			ArpLawFirstInstance.belg_office._obj._renderHidden(false);
		}else{
			ArpLawFirstInstance.lawyer_name._setValue('');
			ArpLawFirstInstance.lawyer_link_mode._setValue('');
			ArpLawFirstInstance.belg_office._setValue('');
			
			ArpLawFirstInstance.lawyer_name._obj._renderHidden(true);
			ArpLawFirstInstance.lawyer_link_mode._obj._renderHidden(true);
			ArpLawFirstInstance.belg_office._obj._renderHidden(true);
		}
	};
	function doLoad(){
		checkLawyer();
	};

	/****** 各种日期校验 *******/
	function checkDate(obj){
		if(obj.value){
			var register_time = ArpLawFirstInstance.register_time._getValue();	//立案时间
			var court_date = ArpLawFirstInstance.court_date._getValue();	//开庭日期
			var suspend_date = ArpLawFirstInstance.suspend_date._getValue();	//中止日期
			var close_case_date = ArpLawFirstInstance.close_case_date._getValue();	//结案日期
			var law_writ_inure_date = ArpLawFirstInstance.law_writ_inure_date._getValue();	//法律文书生效日期

			/*** 立案时间 < 开庭日期 < 中止日期 < 结案日期 < 法律文书生效日期 ***/
			if(obj.name == 'ArpLawFirstInstance.register_time'){
				if( ( court_date != '' && court_date <= register_time ) 
						|| ( suspend_date != '' && suspend_date <= register_time )
						|| ( close_case_date != '' && close_case_date <= register_time ) 
						|| ( law_writ_inure_date != '' && law_writ_inure_date <= register_time ) ){
					obj.value = '';
					alert("[立案时间]应该小于其他日期");
		   		}
			}
			if(obj.name == 'ArpLawFirstInstance.court_date'){
				if( ( register_time != '' && court_date <= register_time ) 
						|| ( suspend_date != '' && suspend_date <= court_date )
						|| ( close_case_date != '' && close_case_date <= court_date ) 
						|| ( law_writ_inure_date != '' && law_writ_inure_date <= court_date )){
					obj.value = '';
					alert("[开庭日期]应该大于[立案时间]，小于[中止日期]、[结案日期]、[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawFirstInstance.suspend_date'){
				if( ( register_time != '' && suspend_date <= register_time ) 
						|| ( court_date != '' && suspend_date <= court_date )
						|| ( close_case_date != '' && close_case_date <= suspend_date ) 
						|| ( law_writ_inure_date != '' && law_writ_inure_date <= suspend_date ) ){
					obj.value = '';
					alert("[中止日期]应该大于[立案时间]、[开庭日期]，小于[结案日期]、[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawFirstInstance.close_case_date'){
				if(( register_time != '' && close_case_date <= register_time ) 
						|| ( court_date != '' && close_case_date <= court_date )
						|| ( suspend_date != '' && close_case_date <= suspend_date ) 
						|| ( law_writ_inure_date != '' && law_writ_inure_date <= close_case_date ) ){
					obj.value = '';
					alert("[结案日期]应该大于[立案时间]、[开庭日期]、[中止日期]，小于[法律文书生效日期]");
		   		}
			}
			if(obj.name == 'ArpLawFirstInstance.law_writ_inure_date'){
				if( ( register_time != '' && law_writ_inure_date <= register_time ) 
						|| ( court_date != '' && law_writ_inure_date <= court_date )
						|| ( suspend_date != '' && law_writ_inure_date <= suspend_date ) 
						|| ( close_case_date != '' && law_writ_inure_date < close_case_date ) ){
					obj.value = '';
					alert("[法律文书生效日期]应该大于其他日期");
		   		}
			}
		}		
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateArpLawFirstInstanceRecord.do" method="POST">
		<emp:gridLayout id="ArpLawFirstInstanceGroup" maxColumn="2" title="基本信息">
			<emp:text id="ArpLawFirstInstance.case_no" label="案件编号" maxlength="40" required="true" hidden="true" />
			<emp:text id="ArpLawFirstInstance.lawsuit_totl_sub" label="诉讼总标的（元）" maxlength="16" required="false" dataType="Currency"/>
			<emp:select id="ArpLawFirstInstance.whether_take_preserve" label="是否采取保全措施" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:date id="ArpLawFirstInstance.register_time" label="立案时间" required="false" onblur="checkDate(this)"/>
			<emp:select id="ArpLawFirstInstance.lawsuit_type" label="诉讼类型" required="false" dictname="STD_ZB_LAWSUIT_TYPE" />
			<emp:text id="ArpLawFirstInstance.rcv_court" label="受理法院" maxlength="80" required="false" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:text id="ArpLawFirstInstance.rcv_judge" label="受理法官" maxlength="80" required="false" />
			<emp:select id="ArpLawFirstInstance.agt_type" label="代理方式" required="false" dictname="STD_ZB_AGT_TYPE_FIRST" colSpan="2" onchange="checkLawyer()"/>
			<emp:text id="ArpLawFirstInstance.lawyer_name" label="律师姓名" maxlength="80" required="false" />
			<emp:text id="ArpLawFirstInstance.lawyer_link_mode" label="律师联系方式" maxlength="30" required="false" dataType="Phone" />
			<emp:text id="ArpLawFirstInstance.belg_office" label="所属事务所" maxlength="80" required="false" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawFirstInstanceGroup" maxColumn="2" title="审理信息">
			<emp:date id="ArpLawFirstInstance.court_date" label="开庭日期" required="false" onblur="checkDate(this)"/>
			<emp:select id="ArpLawFirstInstance.whether_absent_judge" label="是否缺席判决" required="false" dictname="STD_ZX_YES_NO" />
			<emp:date id="ArpLawFirstInstance.suspend_date" label="中止日期" required="false" onblur="checkDate(this)"/>
			<emp:text id="ArpLawFirstInstance.suspend_lawsuit_reason" label="中止诉讼原因" maxlength="250" required="false" 
			cssElementClass="emp_field_text_cusname" colSpan="2"/>
			<emp:date id="ArpLawFirstInstance.close_case_date" label="结案日期" required="false" onblur="checkDate(this)"/>
			<emp:select id="ArpLawFirstInstance.close_case_mode" label="结案方式" required="false" dictname="STD_ZB_CLOSE_MODE" />
			<emp:select id="ArpLawFirstInstance.whether_recover" label="是否胜诉" required="false" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:select id="ArpLawFirstInstance.whether_opp_appeal" label="对方是否上诉" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="ArpLawFirstInstance.whether_bank_appeal" label="我行是否上诉" required="false" dictname="STD_ZX_YES_NO" />
			<emp:date id="ArpLawFirstInstance.law_writ_inure_date" label="法律文书生效日期" required="false" onblur="checkDate(this)"/>
			<emp:text id="ArpLawFirstInstance.law_writ_no" label="法律文书编号" maxlength="40" required="false" />
			<emp:textarea id="ArpLawFirstInstance.memo" label="备注" maxlength="250" required="false" colSpan="2"  />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawFirstInstanceGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawFirstInstance.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="ArpLawFirstInstance.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="ArpLawFirstInstance.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="ArpLawFirstInstance.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="ArpLawFirstInstance.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="submits" label="保存" op="save"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
