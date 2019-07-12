<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>自动额度页面</title>
<%
	request.setAttribute("canwrite","");
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	//控制共同债务人
	function checkSameDeb(){
		var isSameDeb = LmtAgrIndiv.is_same_debtor._getValue();
		if(isSameDeb=='1'){
			LmtAgrIndiv.same_debtor_id_displayname._obj._renderRequired(true);
			LmtAgrIndiv.same_debtor_id_displayname._obj._renderHidden(false);
		}else{
			LmtAgrIndiv.same_debtor_id_displayname._obj._renderRequired(false);
			LmtAgrIndiv.same_debtor_id_displayname._obj._renderHidden(true);
			LmtAgrIndiv.same_debtor_id_displayname._setValue('');
			LmtAgrIndiv.same_debtor_id._setValue('');
		}
	}

	//是否开通POS支付
	function checkOpenPos(){
		var openPos = LmtAgrIndiv.is_open_pos._getValue();
		if("1"==openPos){  //开通POS
			LmtAgrIndiv.pos_pay_type._obj._renderHidden(false);
			LmtAgrIndiv.limit_regi_id._obj._renderHidden(false);
			LmtAgrIndiv.limit_regi_name._obj._renderHidden(false);
		}else{   //不开通 
			LmtAgrIndiv.pos_pay_type._obj._renderHidden(true);
			LmtAgrIndiv.limit_regi_id._obj._renderHidden(true);
			LmtAgrIndiv.limit_regi_name._obj._renderHidden(true);
		}
	}

	//是否开通自助循环
	function checkSelfRevolv(){
		var selfRevolv = LmtAgrIndiv.is_self_revolv._getValue();
		if("1"==selfRevolv){   //开通自助
			//LmtAgrIndiv.org_self_amt._obj._renderHidden(false);  //原自助金额(元)
			LmtAgrIndiv.self_amt._obj._renderHidden(false);  //自助金额(元)
			
			LmtAgrIndiv.ir_float_rate_self._obj._renderHidden(false);  //利率浮动比(自助)
			LmtAgrIndiv.overdue_rate._obj._renderHidden(false);  //逾期利率浮动比例
			LmtAgrIndiv.ir_adjust_type_self._obj._renderHidden(false);  //利率调整方式(自助)
			LmtAgrIndiv.repay_type_self_displayname._obj._renderHidden(false);  //贷款还款方式(自助)

			LmtAgrIndiv.self_rate_y._obj._renderHidden(false);   //基准年利率
			LmtAgrIndiv.self_rate_m._obj._renderHidden(false);   //基准月利率
			
			LmtAgrIndiv.guar_type._obj._renderHidden(false);  //担保方式
			LmtAgrIndiv.guar_type_detail._obj._renderHidden(false);  //担保方式细分
			LmtAgrIndiv.five_class._obj._renderHidden(false);  //五级分类
			
			LmtAgrIndiv.is_open_pos._obj._renderHidden(false); //是否开通POS
			LmtAgrIndiv.is_same_debtor._obj._renderHidden(false);//是否有共同债务人

			LmtAgrIndiv.self_amt._obj._renderRequired(true);
			LmtAgrIndiv.guar_type._obj._renderRequired(true);
			LmtAgrIndiv.guar_type_detail._obj._renderRequired(true);
		}else{
			//LmtAgrIndiv.org_self_amt._obj._renderHidden(true);  //原自助金额(元)
			LmtAgrIndiv.self_amt._obj._renderHidden(true);  //自助金额(元)
			
			LmtAgrIndiv.ir_float_rate_self._obj._renderHidden(true);  //利率浮动比(自助)
			LmtAgrIndiv.overdue_rate._obj._renderHidden(true);  //逾期利率浮动比例
			LmtAgrIndiv.ir_adjust_type_self._obj._renderHidden(true);  //利率调整方式(自助)
			LmtAgrIndiv.repay_type_self_displayname._obj._renderHidden(true);  //贷款还款方式(自助)

			LmtAgrIndiv.self_rate_y._obj._renderHidden(true);   //基准年利率
			LmtAgrIndiv.self_rate_m._obj._renderHidden(true);   //基准月利率
			
			LmtAgrIndiv.guar_type._obj._renderHidden(true);  //担保方式
			LmtAgrIndiv.guar_type_detail._obj._renderHidden(true);  //担保方式细分
			LmtAgrIndiv.five_class._obj._renderHidden(true);  //担保方式细分
			
			LmtAgrIndiv.is_open_pos._obj._renderHidden(true);  //是否开通POS
			LmtAgrIndiv.is_open_pos._setValue("2"); //设置开通POS业务为否
			
			LmtAgrIndiv.is_same_debtor._obj._renderHidden(true);  //是否有共同债务人
			LmtAgrIndiv.is_same_debtor._setValue("2"); //设置是否有共同债务人为否
			LmtAgrIndiv.self_amt._obj._renderRequired(false);
			LmtAgrIndiv.guar_type._obj._renderRequired(false);
			LmtAgrIndiv.guar_type_detail._obj._renderRequired(false);

			LmtAgrIndiv.self_start_date._obj._renderHidden(true);  //起始日期
			LmtAgrIndiv.self_end_date._obj._renderHidden(true);  //到期日期
		}
		checkOpenPos();
		checkSameDeb();
	}

	//检查共同债务人是否合规
	function checkCusDeb(){
		var cusId = "${context.cus_id}";
		var debId = LmtAgrIndiv.same_debtor_id._getValue();
		if(cusId!=null&&cusId!=''&&debId!=null&&debId!=''){
			if(cusId==debId){
				alert('共同债务人与申请人不能相同，请重新选择！');
				LmtAgrIndiv.same_debtor_id_displayname._setValue('');
				LmtAgrIndiv.same_debtor_id._setValue('');
				return;
			}
		}
	}

	//设置产品返回 
	function setProds(data){
		LmtAgrIndiv.prd_id._setValue(data[0]);
		LmtAgrIndiv.prd_id_displayname._setValue(data[1]);
	}


	//主担保方式下拉框值改变事件
	function guarTypeChange(){
		var assureMainValue =LmtAgrIndiv.guar_type._getValue();
		if(assureMainValue == ""){
			LmtAgrIndiv.guar_type_detail._obj._renderReadonly(false);
			LmtAgrIndiv.guar_type_detail._setValue("");
		}else if(assureMainValue =="100"){//主担保方式为抵押时，担保方式细分自动赋值为抵押
			LmtAgrIndiv.guar_type_detail._setValue("1");
			LmtAgrIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue =="300"){//保证
			LmtAgrIndiv.guar_type_detail._setValue("8");
			LmtAgrIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue =="400"){//信用
			LmtAgrIndiv.guar_type_detail._setValue("9");
			LmtAgrIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue.substring(0,1) == "2"){
			LmtAgrIndiv.guar_type_detail._obj._renderReadonly(false);
			LmtAgrIndiv.guar_type_detail._setValue("");
		}
	};
	
	function doOnload(){
		checkSelfRevolv();  //是否开通自动额度

		//根据月利率设置年利率的值
		var self_rate_m = LmtAgrIndiv.self_rate_m._getValue();
		if(null!=self_rate_m && ""!=self_rate_m){ 
			LmtAgrIndiv.self_rate_y._setValue(Math.round(self_rate_m*1.2*100000)/100000);   //年利率
		}

	}

	function showSelfTerm(){
		var isAdjTermSelf = LmtAgrIndiv.is_adj_term_self._getValue();
		if(isAdjTermSelf=='1'){
			LmtAgrIndiv.self_term._obj._renderHidden(false);
			LmtAgrIndiv.self_term._obj._renderRequired(true);
		}else{
			LmtAgrIndiv.self_term._obj._renderHidden(true);
			LmtAgrIndiv.self_term._obj._renderRequired(false);
		}
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()" >
	<emp:form id="submitForm" action="updateLmtAgrIndivRecord.do" method="POST">
		<emp:gridLayout id="LmtAgrIndivGroup" title="自助额度" maxColumn="2">
			<emp:select id="LmtAgrIndiv.is_self_revolv" label="是否开通自助循环" required="true" dictname="STD_ZX_YES_NO" onchange="checkSelfRevolv()" defvalue="2" colSpan="2"/>
			<emp:text id="LmtAgrIndiv.self_amt" label="自助金额" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtAgrIndiv.self_rate_y" label="基准利率(年)" dataType="Rate4Month" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrIndiv.self_rate_m" label="基准利率(月)" dataType="Rate4Month" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:date id="LmtAgrIndiv.self_start_date" label="授信起始日" readonly="true" />
			<emp:date id="LmtAgrIndiv.self_end_date" label="授信到期日" readonly="true"/>
			
			<emp:text id="LmtAgrIndiv.ir_float_rate_self" label="利率浮动比(自助)" maxlength="16" required="false" dataType="Percent" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtAgrIndiv.overdue_rate" label="逾期利率浮动比例" maxlength="16" required="false" dataType="Percent" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="LmtAgrIndiv.ir_adjust_type_self" label="利率调整方式(自助)" required="false" dictname="STD_IR_ADJUST_TYPE" />
			
			<emp:pop id="LmtAgrIndiv.repay_type_self_displayname" label="还款方式" url="queryPrdRepayModeList.do?returnMethod=getRepayType" required="false"  buttonLabel="选择" />
			
			<emp:select id="LmtAgrIndiv.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" onchange="guarTypeChange()"/>
			<emp:select id="LmtAgrIndiv.guar_type_detail" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE"/>
			<emp:select id="LmtAgrIndiv.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" colSpan="2"/>
			
			<emp:select id="LmtAgrIndiv.is_same_debtor" label="是否有共同债务人" required="true" dictname="STD_ZX_YES_NO" onchange="checkSameDeb()" defvalue="2"/>
			<emp:pop id="LmtAgrIndiv.same_debtor_id_displayname" label="共同债务人" url="queryAllCusPop.do?cusTypCondition=belg_line='BL300' and cus_status='20'&returnMethod=returnDebt" />
			
			<emp:select id="LmtAgrIndiv.is_open_pos" label="是否开通POS支付" required="false" dictname="STD_ZX_YES_NO" defvalue="2" onchange="checkOpenPos()"/>
			<emp:select id="LmtAgrIndiv.pos_pay_type" label="POS机支付方式" required="false" dictname="STD_ZB_POS_PAY_TYPE" />
			<emp:text id="LmtAgrIndiv.limit_regi_id" label="额度注册账号" maxlength="40" required="false" />
			<emp:text id="LmtAgrIndiv.limit_regi_name" label="额度注册账户名" maxlength="80" required="false" readonly="true"/>
			
			<emp:text id="LmtAgrIndiv.same_debtor_id" label="共同债务人" maxlength="30" required="false" hidden="true" />
			<emp:text id="LmtAgrIndiv.repay_type_self" label="贷款还款方式(自助)" maxlength="5" required="false" hidden="true"/>
			<emp:text id="LmtAgrIndiv.serno" label="业务流水号" required="false" hidden="true"/>
			
		</emp:gridLayout>
	</emp:form>
	
</body>
</html>
</emp:page>

