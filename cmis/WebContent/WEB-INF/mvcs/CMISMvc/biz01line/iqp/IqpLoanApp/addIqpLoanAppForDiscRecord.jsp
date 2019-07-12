<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String currency = "";
	String guarway = "";
	if(context.containsKey("currency")){
		currency = (String)context.getDataValue("currency");
	}
	if(context.containsKey("guarway")){
		guarway = (String)context.getDataValue("guarway");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:600px;
}
</style>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="jsIqpComm.jsp" flush="true" /> 
<script type="text/javascript">
function doOnLoad(){
	document.getElementById("base_tab").href="javascript:reLoad();";
	IqpLoanApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
	IqpLoanApp.limit_acc_no._obj.addOneButton("limit_acc_no","查看",getLimitAccNo);
	IqpLoanApp.limit_credit_no._obj.addOneButton("limit_credit_no","查看",getLimitCreditNO);
	IqpLoanApp.IqpDiscApp.agent_acct_no._obj.addOneButton("acctNo","获取",getAcctNo);
	var guarwayoptions = IqpLoanApp.assure_main._obj.element.options;
	var currencyoptions = IqpLoanApp.apply_cur_type._obj.element.options;
	
	//根据产品过滤担保，币种
	var currencyList = new Array();	
	var guarwayList = new Array();
	var currency = '<%=currency%>';	
	var guarway = '<%=guarway%>';	
	currencyList = currency.split(",");
	guarwayList = guarway.split(",");
	for(var i=currencyoptions.length-1;i>=0;i--){
		var m =0;
		for(var j=0;j<currencyList.length;j++){
             if(currencyoptions[i].value==currencyList[j] || currencyoptions[i].value=="" ){
                 m=1; 
             }
		}
		if(m!=1){ 
			currencyoptions.remove(i); 
        } 	
	}
	for(var i=guarwayoptions.length-1;i>=0;i--){
		var m = 0;
		for(var j=0;j<guarwayList.length;j++){
             if(guarwayoptions[i].value==guarwayList[j] || guarwayoptions[i].value==""){
                 m=1;
             }
		}
		if(m!=1){
			guarwayoptions.remove(i); 
		}
		
	}

	//加载票据种类
	IqpLoanApp.IqpDiscApp.bill_type._setValue('${context.IqpDiscApp.bill_type}');

	isPay();
	getHLByCurrDscnt();//--加载汇率--
	changeRmbAmt();//--加载折算金额--
	isShowNote();//是否承诺函
	isShowCompany();//是否担保公司
	//doChangLimitInt();//授信额度标识
	doInitLimit(); //页面加载授信额度标识 
	show_net();//显示所属网络
	controlBizType();//业务模式控制

    //产品为委托贷款时，贷款性质自动设置为委托贷款
    IqpLoanApp.is_promissory_note._setValue("2");//默认不是承诺函下
	var prd_id =IqpLoanApp.prd_id._getValue();
    if(prd_id == "400022" || prd_id == "400023" || prd_id == "400024"){//贷款承诺、贷款意向、信贷证明
    	IqpLoanApp.is_promissory_note._setValue("1");
	    IqpLoanApp.is_promissory_note._obj._renderHidden(false);
	    IqpLoanApp.is_promissory_note._obj._renderReadonly(true);
	    IqpLoanApp.promissory_note._obj._renderHidden(false);
    } else{
    	IqpLoanApp.is_promissory_note._setValue("2");
	    IqpLoanApp.is_promissory_note._obj._renderHidden(true);
	    IqpLoanApp.promissory_note._obj._renderHidden(true);
    }

    getBizType();//通过业务模式判断是否信托贷款

    var dbfs = IqpLoanApp.assure_main._getValue();
    if(dbfs!="" &&dbfs.substring(0,2)!='2'){
    	IqpLoanApp.assure_main_details._obj._renderReadonly(true);
    }
	getBelglineByKhm();

	//初始化贴现信息
	initBillInfo();
	checkstampCollectMode();//印花税收取方式，如果是委托贷款字典放开都可选，否则默认"全由借款人支付"
	checkAssureMain();//银行承兑汇票贴现，担保方式，担保方式细分隐藏
	cleanLimitInd();
	/********XD150702049 五级分类默认为正常类，且不可改*******/
	IqpLoanApp.IqpDiscApp.five_classfiy._setValue("10");
	IqpLoanApp.IqpDiscApp.five_classfiy._obj._renderReadonly(true);
   	/***XD150702049 五级分类默认为正常类，且不可改**/
 };  

function doSub(data){
	var form = document.getElementById("submitForm");
	if(IqpLoanApp._checkAll() && IqpLoanApp.IqpDiscApp._checkAll()){
		IqpLoanApp._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag == "success"){
					alert("保存成功!");
					url = '<emp:url action="getIqpLoanAppForDiscUpdatePage.do"/>?menuId=${context.menuId}&op=update&serno='+serno+'&biz_type=${context.IqpLoanApp.biz_type}';
					url = EMPTools.encodeURI(url);    
					window.location = url; 
				}else {
					alert("保存失败!");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	
		var url = '<emp:url action="addIqpLoanAppRecord.do"/>?rd='+Math.random();
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	}else {
		return false;
	}
};


function initBillInfo(){
	var prd_id = IqpLoanApp.prd_id._getValue(); 
	if(prd_id==300021){
		IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode._obj._renderHidden(true);
		IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode._obj._renderRequired(false);
	}else if(prd_id=300020){
		IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode._obj._renderHidden(false);
		IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode._obj._renderRequired(true);
	}

}

function agricultureReturn(date){
	IqpLoanApp.IqpDiscApp.agriculture_type._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.agriculture_type_displayname._setValue(date.label);
};
function projectReturn(date){
	IqpLoanApp.IqpDiscApp.ensure_project_loan._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.ensure_project_loan_displayname._setValue(date.label);
};
function onReturn(date){
	IqpLoanApp.IqpDiscApp.estate_adjust_type._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.estate_adjust_type_displayname._setValue(date.label);
};
function strategyReturn(date){
	IqpLoanApp.IqpDiscApp.strategy_new_loan._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.strategy_new_loan_displayname._setValue(date.label);
};
function newPrdReturn(date){
	IqpLoanApp.IqpDiscApp.new_prd_loan._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.new_prd_loan_displayname._setValue(date.label);
};
function greenPrdReturn(date){
	IqpLoanApp.IqpDiscApp.green_prd._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.green_prd_displayname._setValue(date.label);
};
function loanBelong1Return(date){
	IqpLoanApp.IqpDiscApp.loan_belong1._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.loan_belong1_displayname._setValue(date.label);
};

function loanDirectionReturn(date){
	IqpLoanApp.IqpDiscApp.loan_direction._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.loan_direction_displayname._setValue(date.label);
};
function loanBelong2Return(date){
	IqpLoanApp.IqpDiscApp.loan_belong2._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.loan_belong2_displayname._setValue(date.label);
	
};
function loanBelong3Return(date){
	IqpLoanApp.IqpDiscApp.loan_belong3._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.loan_belong3_displayname._setValue(date.label);
};
function ensureProjectReturn(date){
	IqpLoanApp.IqpDiscApp.loan_use_type._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.loan_use_type_displayname._setValue(date.label);
};
function discTypeReturn(date){
	IqpLoanApp.IqpDiscApp.loan_type._obj.element.value=date.id;
	IqpLoanApp.IqpDiscApp.loan_type_displayname._setValue(date.label);
}
	/*--user code begin--*/
	function reLoad(){
		var url = '<emp:url action="getIqpLoanAppForDiscUpdatePage.do"/>?menuId=queryIqpLoanApp&serno=${context.IqpLoanApp.serno}&op=update';
		url = EMPTools.encodeURI(url);
		window.location = url;
		//window.location.reload();
	}

	function checkstampCollectMode(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
			IqpLoanApp.IqpDiscApp.stamp_collect_mode._setValue("2");
			IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderReadonly(true);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">	
  <emp:form id="submitForm" action="addIqpLoanAppRecord.do?menuId=${context.menuId}" method="POST">
	 <emp:tabGroup mainTab="base_tab" id="mainTabs">
	  <emp:tab label="申请基本信息" id="base_tab" needFlush="true" >
		<emp:gridLayout id="IqpLoanAppGroup" maxColumn="2" title="基本信息" >
			<emp:date id="IqpLoanApp.apply_date" label="申请日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpLoanApp.prd_id" label="产品编号" maxlength="6" required="true" readonly="true"/>
			<emp:text id="IqpLoanApp.prd_id_displayname" label="产品名称" required="true" readonly="true"/>   
			<emp:text id="IqpLoanApp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="IqpLoanApp.cus_id_displayname" label="客户名称" required="false"  readonly="true" colSpan="2" cssElementClass="emp_input2"/>
			<emp:select id="IqpLoanApp.is_rfu" label="是否曾被拒绝" required="false" dictname="STD_ZX_YES_NO" colSpan="1"/>	 
            <emp:select id="IqpLoanApp.is_spe_cus" label="是否特殊客户" required="false" dictname="STD_ZX_YES_NO" />
			
			<emp:select id="IqpLoanApp.IqpDiscApp.bill_type" label="票据种类" required="true" readonly ="true" dictname="STD_DRFT_TYPE" />
			<emp:select id="IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode" label="商票贴现类型" required="true" dictname="STD_BUSDRFT_DISCOUNT_TYPE"/> 
			<emp:select id="IqpLoanApp.IqpDiscApp.is_elec_bill" label="是否电子票据" required="true" readonly ="true" defvalue = "2" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpLoanApp.IqpDiscApp.disc_type" label="贴现类型" required="true" dictname="STD_ZB_DISCOUNT_TYPE"/>
			
			<emp:select id="IqpLoanApp.assure_main" label="担保方式" required="true" onclick="assure_mainChange();doChangLimitInt();cleanLimitInt();" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="IqpLoanApp.assure_main_details" label="担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" />
			<emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" hidden="true" required="false" defvalue="7" onclick="show_net();controlBizType();"/>
			<emp:select id="IqpLoanApp.rent_type" label="租赁模式" dictname="STD_RENT_TYPE" defvalue="0" required="false" />
			<emp:pop id="IqpLoanApp.belong_net" label="所属网络" url="queryNetMagInfoPop.do?returnMethod=getNetMagInfo" required="false" />   	   
			<emp:select id="IqpLoanApp.is_promissory_note" label="是否承诺函下" required="false" dictname="STD_ZX_YES_NO" onclick="isShowNote()"/> 
			<emp:text id="IqpLoanApp.promissory_note" label="承诺函" maxlength="80" required="false" hidden="true" colSpan="2"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.is_collect_stamp" label="是否收取印花税" required="true" onchange="changeStampForDis()" dictname="STD_ZX_YES_NO" readonly="true" defvalue="2"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.stamp_collect_mode" label="印花税收取方式" required="false" readonly="false" dictname="STD_ZB_STAMP_MODE" hidden="true"/>
			<emp:select id="IqpLoanApp.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" onclick="isShowCompany()"/>
			<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整  -->
			<emp:text id="IqpLoanApp.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" readonly="true" />
			<emp:text id="IqpLoanApp.trust_company" label="信托公司" maxlength="100" required="false" hidden="true" />	
			<emp:select id="IqpLoanApp.is_limit_cont_pay" label="是否额度合同项下支用" onchange="isPay();" defvalue="2" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="IqpLoanApp.limit_cont_no" label="额度合同编号" url="queryCtrLimitContListPop.do?returnMethod=getContMsg" required="false" hidden="true" />
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:select id="IqpLoanApp.apply_cur_type" label="票据币种" required="true" onchange="getHLByCurr()" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		    <emp:text id="IqpLoanApp.exchange_rate" label="汇率" maxlength="16" readonly="true" required="true" />
		    <emp:text id="IqpLoanApp.apply_amount" label="票面总金额" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.apply_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		   	
		   	<emp:select id="IqpLoanApp.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security();" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpLoanApp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
		   	<emp:text id="IqpLoanApp.security_rate" label="保证金比例" maxlength="16" readonly="false" onfocus="_doKeypressDown()" onchange="changeSecRate();changeRmbAmt4SecurityChange()" required="true" dataType="Rate" />
		   	<emp:text id="IqpLoanApp.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true"/>
		   	
		    <emp:text id="IqpLoanApp.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.ass_sec_multiple" label="担保放大倍数" maxlength="10" defvalue="1" required="false" hidden="true" dataType="Double" />
		    <emp:text id="IqpLoanApp.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" onblur="changeRmbAmt()" dataType="Currency" />
		    <emp:text id="IqpLoanApp.risk_open_amt" label="风险敞口金额（元）" maxlength="18" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.risk_open_rate" label="敞口比率" maxlength="10" readonly="true" required="false" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
		    
		    <emp:text id="IqpLoanApp.IqpDiscApp.bill_qty" label="票据数量" defvalue="0" maxlength="38" required="true" readonly="true"/>
		    <emp:date id="IqpLoanApp.IqpDiscApp.disc_date" label="贴现日期" required="false" readonly="true"/> 
			<emp:text id="IqpLoanApp.IqpDiscApp.disc_rate" label="贴现总利息" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.net_pay_amt" label="实付总金额" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
			<emp:select id="IqpLoanApp.limit_ind" label="授信额度使用标志" required="true" onchange="doChangLimitInt()" dictname="STD_LIMIT_IND" colSpan="2"/>	   
		    <emp:pop id="IqpLoanApp.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" />
		    <emp:pop id="IqpLoanApp.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" buttonLabel="选择" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="代理信息">
			<emp:select id="IqpLoanApp.IqpDiscApp.is_agent_disc" label="是否代理贴现" required="true" onchange="checkAgentDisc()" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.agent_acct_no" label="代理人账户" maxlength="40" required="true" />
			<emp:text id="IqpLoanApp.IqpDiscApp.agent_acct_name" label="代理人名称" maxlength="80" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpLoanApp.IqpDiscApp.agent_org_no" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="getOrgNo" label="代理人开户行行号"  required="true"  />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpLoanApp.IqpDiscApp.agent_org_name" label="代理人开户行行名" maxlength="100" required="true" />
			<emp:textarea id="IqpLoanApp.IqpDiscApp.pvp_pact_cond_memo" label="出账落实条件说明" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="其他信息">
		    <emp:select id="IqpLoanApp.IqpDiscApp.five_classfiy" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpLoanApp.IqpDiscApp.spe_loan_type" label="特殊贷款类型" required="true" dictname="STD_ZB_LOAN_TYPE_EXT"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.limit_useed_type" label="额度占用类型" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" />
			<emp:select id="IqpLoanApp.IqpDiscApp.loan_use_type" label="借款用途" dictname="STD_ZB_USE_TYPE" required="true"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.com_up_indtify" label="工业转型升级标识" dictname="STD_ZX_YES_NO" required="true" />
			
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_type_displayname" label="贷款种类" url="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE" returnMethod="discTypeReturn" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2" />
			<emp:pop id="IqpLoanApp.IqpDiscApp.agriculture_type_displayname" label="涉农贷款类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_FARME" returnMethod="agricultureReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.ensure_project_loan_displayname" label="保障性安居工程贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.estate_adjust_type_displayname" label="产业结构调整类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_TRD_TYPE" returnMethod="onReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.strategy_new_loan_displayname" label="战略新兴产业类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_ZLXXCYLX" returnMethod="strategyReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.new_prd_loan_displayname" label="新兴产业贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_XXCYDK" returnMethod="newPrdReturn" required="false" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_direction_displayname" label="贷款投向" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="loanDirectionReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_belong1_displayname" label="贷款归属1" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS1" returnMethod="loanBelong1Return" required="false" hidden="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_belong2_displayname" label="贷款归属2" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS2" returnMethod="loanBelong2Return" required="false" hidden="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_belong3_displayname" label="贷款归属3" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS3" returnMethod="loanBelong3Return" required="false" hidden="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			
			<emp:text id="IqpLoanApp.IqpDiscApp.loan_type" label="贷款种类" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.agriculture_type" label="涉农贷款类型" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.ensure_project_loan" label="保障性安居工程贷款" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.estate_adjust_type" label="产业结构调整类型" hidden="true" />
			<emp:text id="IqpLoanApp.IqpDiscApp.strategy_new_loan" label="战略新兴产业类型" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.new_prd_loan" label="新兴产业贷款" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.loan_direction" label="贷款投向" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.loan_belong1" label="贷款归属1" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.loan_belong2" label="贷款归属2" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.loan_belong3" label="贷款归属3" hidden="true"/>
			
			<emp:textarea id="IqpLoanApp.IqpDiscApp.repay_src_des" label="还款来源"  required="true" colSpan="2"/>
			<emp:textarea id="IqpLoanApp.IqpDiscApp.biz_sour" label="业务来源"  required="false" colSpan="2"/>
			<emp:textarea id="IqpLoanApp.IqpDiscApp.sour_memo" label="来源说明"  required="false" />	
					
			<emp:date id="IqpLoanApp.end_date" label="办结日期" required="false" hidden="true"/>					
			<emp:textarea id="IqpLoanApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" hidden="true"/>	
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:pop id="IqpLoanApp.manager_br_id_displayname" label="管理机构" required="true" buttonLabel="选择" url="querySOrgPop.do?yewu=is&restrictUsed=false" returnMethod="getOrgID" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpLoanApp.in_acct_br_id_displayname" label="入账机构" required="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" />
		    <emp:select id="IqpLoanApp.flow_type" label="流程类型"  required="false" hidden="true" defvalue="01" dictname="STD_ZB_FLOW_TYPE" />
		    <emp:text id="IqpLoanApp.input_id_displayname" label="登记人" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id_displayname" label="登记机构" required="false"  readonly="true"/>
			<emp:date id="IqpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true" readonly="true" required="false" defvalue="000"/>
			<emp:text id="IqpLoanApp.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpLoanApp.in_acct_br_id" label="入账机构" hidden="true" />
			<emp:text id="IqpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="update"/>
			<emp:button id="clean" label="重置"/>
		</div>
		</emp:tab>
		</emp:tabGroup>
		
	</emp:form>
</body>
</html>
<script type="text/javascript"> 
//-----------通过产品编号查询产品配置中使用流程类型----------
function getApplyTypeByPrdId(){
	var prdId = IqpLoanApp.prd_id._getValue();
	var url = '<emp:url action="getIqpApplyTypeByPrdId.do"/>?prdid='+prdId;
	url = EMPTools.encodeURI(url);
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var msg = jsonstr.msg;
			var apply_type = jsonstr.apply_type;
			if(flag == "success"){
				doSubmitWF(apply_type);
			}else {
				alert(msg);
			}
		}
	};
	var handleFailure = function(o){
		alert("异步请求出错！");	
	};
	var callback = {
		success:handleSuccess,
		failure:handleFailure
	};
	//var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};
//-----------提交流程----------
function doSubmitWF(apply_type){
	var serno = IqpLoanApp.serno._getValue();
	var cus_id = IqpLoanApp.cus_id._getValue();
	var cus_name = CusBase.cus_name._getValue();
	var approve_status = IqpLoanApp.approve_status._getValue();
	WfiJoin.table_name._setValue("IqpLoanApp");
	WfiJoin.pk_col._setValue("serno");
	WfiJoin.pk_value._setValue(serno);
	WfiJoin.cus_id._setValue(cus_id);
	WfiJoin.cus_name._setValue(cus_name);
	WfiJoin.prd_pk._setValue(IqpLoanApp.prd_id._getValue());
	WfiJoin.prd_name._setValue(PrdBasicinfo.prdname._getValue());
	WfiJoin.amt._setValue(IqpLoanApp.apply_amount._getValue());
	WfiJoin.wfi_status._setValue(approve_status);
	WfiJoin.status_name._setValue("approve_status");
	WfiJoin.appl_type._setValue(apply_type);
	initWFSubmit(false);
};
//-----------提交流程(目前只做保存和生成合同记录，暂时未加入流程以及修改授信台帐记录)----------
function doSubWF(){ 
	var form = document.getElementById("submitForm");
	var result = IqpLoanApp._checkAll();
	if(!result){
		return;
	}

	//校验票面总金额、折合人民币金额、保证金金额、敞开金额（由于这些金额是由tab票据明细中统计的，所以在基本信息中不予录入，所以不为必输项）
	var apply_amount = IqpLoanApp.apply_amount._getValue();
	var apply_rmb_amount = IqpLoanApp.apply_rmb_amount._getValue();
	var security_amt = IqpLoanApp.security_amt._getValue();
	var risk_open_amt = IqpLoanApp.risk_open_amt._getValue();
	if(apply_amount==''||apply_rmb_amount==''||security_amt==''||risk_open_amt==''){
		alert("未录入票据明细，无法获取票面总金额等信息！");
		return;
	}

	//------执行保存操作------
	checkBillInfo();
	//------风险拦截操作------
	/**
	var serno = IqpLoanApp.serno._getValue();
	var _applType="";
	var _modelId="IqpLoanApp";
	var _pkVal=serno;
	var _preventIdLst="FFFA278501D0540CAF4ACC8A1F445D4B";
	var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
    if(!_retObj || _retObj == '2' || _retObj == '5'){
		if( _retObj == '5'){
			alert("执行风险拦截有错误，请检查！");
		}
		return;
	}
	*/
	//------放入流程保留------
	//getApplyTypeByPrdId();
}	
//自有授信
function getLmtAmt(data){
	var lmtContNo = data[0];//授信协议编号
	var lmtAmt = data[1];//授信余额暂时先去授信金额
	IqpLoanApp.limit_acc_no._setValue(lmtContNo);
	//remain_amount._setValue(lmtAmt+"");//剩余额度
	//alert("lmtContNo="+data.agr_no._getValue());
}

//第三方授信
function getLmtCoopAmt(data){
	var lmtContNo = data[0];//授信协议编号
	var lmtAmt = data[1];//授信余额暂时先去授信金额
	IqpLoanApp.limit_credit_no._setValue(lmtContNo);
	//together_remain_amount._setValue(lmtAmt+""); 
}

//放入流程前先校验票据信息是否完整
function checkBillInfo(){
	var serno = IqpLoanApp.serno._getValue();
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var msg = jsonstr.msg;
			if(flag == "success"){
				doSub('subWF');
			}else {
				alert(msg);
				return;
			}
		}
	};
	var handleFailure = function(o){
		alert("异步请求出错！");	
	};
	var callback = {
		success:handleSuccess,
		failure:handleFailure
	};

	var url="<emp:url action='checkBillInfo.do'/>?serno="+serno;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
}
</script>
</emp:page>
