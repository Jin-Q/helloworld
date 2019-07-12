<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	String flag = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	}
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
<jsp:include page="jsIqpComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
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
<script type="text/javascript">
	
	function doReturn() {
		var flag = '<%=flag %>';
		if(flag == "iqpLoanApp"){
			var url = '<emp:url action="queryIqpLoanAppList.do"/>?biz_type='+'<%=biz_type%>';  
		}else{
			var url = '<emp:url action="queryIqpLoanAppHistoryList.do"/>?biz_type='+'<%=biz_type%>';  
		}  
		url = EMPTools.encodeURI(url);  
		window.location=url; 
	};
	function doOnLoad(){ 
		IqpLoanApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpLoanApp.belong_net._obj.addOneButton("belong_net","查看",getBelongForm);
		IqpLoanApp.limit_acc_no._obj.addOneButton("limit_acc_no","查看",getLimitAccNo);
		IqpLoanApp.limit_credit_no._obj.addOneButton("limit_credit_no","查看",getLimitCreditNO);
		var promissory_note = IqpLoanApp.is_promissory_note._getValue();
		if(promissory_note==1){
			IqpLoanApp.promissory_note._obj._renderHidden(false);
		}
		var trust_loan = IqpLoanApp.is_trust_loan._getValue();
		if(trust_loan==1){
			IqpLoanApp.trust_company._obj._renderHidden(false);
		}
		/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 start **/
		//--加载汇率--
		  //getHLByCurrDscnt();
		/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 end **/
		//--加载折算金额--
		  changeRmbAmt();
		//显示所属网络
		  show_net();
		//是否承诺函
		  isShowNote();
		//是否担保公司
		  isShowCompany();
	    //doChangLimitInt();//授信额度标识  
		  doInitLimit(); //页面加载授信额度标识 
		  controlBizType();//业务模式控制
		  getBizType();//通过业务模式判断是否信托贷款
	    IqpLoanApp.is_promissory_note._obj._renderHidden(true);//隐藏 是否承诺函下
	    IqpLoanApp.promissory_note._obj._renderHidden(true);//隐藏承诺函
	    checkstampCollectMode();//印花税收取方式，如果是委托贷款字典放开都可选，否则默认"全由借款人支付"
	  	getBelglineByKhm();
	  	getBizLineByCusIdDisc();
	  	initBillInfo();
	  	checkAgentDisc();//是否代理贴现
	  	checkAssureMain();//银行承兑汇票贴现，担保方式，担保方式细分隐藏
	  	isPay();
	  	cleanLimitInd();
	}; 
	
	function getBelongForm(){
		var belong_net = IqpLoanApp.belong_net._getValue();
		var url = '<emp:url action=""/>';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	function getRepayForm(){
		var url = '<emp:url action=""/>';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};


	function initBillInfo(){
		var prd_id = IqpLoanApp.prd_id._getValue(); 
		if(prd_id==300021){
			IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode._obj._renderHidden(true);
		}else if(prd_id=300020){
			IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode._obj._renderHidden(false);
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
	/*--user code begin--*/
	function checkstampCollectMode(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
			IqpLoanApp.IqpDiscApp.stamp_collect_mode._setValue("2");
			IqpLoanApp.IqpDiscApp.stamp_collect_mode._obj._renderReadonly(true);
		}
	};
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpLoanApp.serno._getValue();	//业务编号
		data['cus_id'] = IqpLoanApp.cus_id._getValue();	//客户编号
		data['prd_id'] = IqpLoanApp.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpLoanAppGroup" maxColumn="2" title="基本信息" >
			<emp:text id="IqpLoanApp.serno" label="业务流水号" maxlength="40" required="true" readonly="true"/>
			<emp:date id="IqpLoanApp.apply_date" label="申请日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpLoanApp.prd_id" label="产品编号" maxlength="6" required="true" readonly="true"/>
			<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="80" required="true" readonly="true"/>   
			<emp:text id="IqpLoanApp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="CusBase.cus_name" label="客户名称" maxlength="80" required="false"  readonly="true" colSpan="2" cssElementClass="emp_input2"/>
			<emp:select id="IqpLoanApp.is_rfu" label="是否曾被拒绝" required="false" dictname="STD_ZX_YES_NO" colSpan="1"/>	 
            <emp:select id="IqpLoanApp.is_spe_cus" label="是否特殊客户" required="false" dictname="STD_ZX_YES_NO" />
			
			<emp:select id="IqpLoanApp.IqpDiscApp.bill_type" label="票据种类" required="true" readonly ="true" dictname="STD_DRFT_TYPE" />
			<emp:select id="IqpLoanApp.IqpDiscApp.busdrft_dscnt_mode" label="商票贴现类型" required="true" dictname="STD_BUSDRFT_DISCOUNT_TYPE"/> 
			<emp:select id="IqpLoanApp.IqpDiscApp.is_elec_bill" label="是否电子票据" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpLoanApp.IqpDiscApp.disc_type" label="贴现类型" required="true" dictname="STD_ZB_DISCOUNT_TYPE"/>
			
			<emp:select id="IqpLoanApp.assure_main" label="担保方式" required="true" onclick="assure_mainChange()" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="IqpLoanApp.assure_main_details" label="担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" />
			<emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" required="false"  defvalue="7" onclick="show_net();controlBizType();"/>
			<emp:select id="IqpLoanApp.rent_type" label="租赁模式" dictname="STD_RENT_TYPE" defvalue="0" required="false" />
			<emp:pop id="IqpLoanApp.belong_net" label="所属网络" url="" returnMethod="" required="false" />	   
			<emp:select id="IqpLoanApp.is_promissory_note" label="是否承诺函下" required="false" dictname="STD_ZX_YES_NO" onclick="isShowNote()"/> 
			<emp:text id="IqpLoanApp.promissory_note" label="承诺函" maxlength="80" required="false" hidden="true" colSpan="2"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.is_collect_stamp" label="是否收取印花税" required="true" dictname="STD_ZX_YES_NO" readonly="true" defvalue="2"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.stamp_collect_mode" label="印花税收取方式" required="false" hidden="true" readonly="true" dictname="STD_ZB_STAMP_MODE" />
			<emp:select id="IqpLoanApp.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" onclick="isShowCompany()"/>
			<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整-->
			<emp:text id="IqpLoanApp.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" />
			<emp:text id="IqpLoanApp.trust_company" label="信托公司" maxlength="100" required="false" hidden="true" />	
			<emp:select id="IqpLoanApp.is_limit_cont_pay" label="是否额度合同项下支用" onchange="isPay();" defvalue="2" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="IqpLoanApp.limit_cont_no" label="额度合同编号" url="queryCtrLimitContListPop.do?returnMethod=getContMsg" required="false" hidden="true" />
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:select id="IqpLoanApp.apply_cur_type" label="票据币种" required="true" onchange="getHLByCurr()" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
		    <emp:text id="IqpLoanApp.exchange_rate" label="汇率" maxlength="16" readonly="true" required="true" dataType="Double" />
		    <emp:text id="IqpLoanApp.apply_amount" label="票面总金额" maxlength="18" required="true" onblur="changeRmbAmt()" dataType="Currency" />
		    <emp:text id="IqpLoanApp.apply_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" />
		   	
		   	<emp:select id="IqpLoanApp.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security();" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpLoanApp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
		   	<emp:text id="IqpLoanApp.security_rate" label="保证金比例" maxlength="16" readonly="false" onchange="changeSecRate();changeRmbAmt4SecurityChange()" required="true" dataType="Rate" />
		   	<emp:text id="IqpLoanApp.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true"/>

		    <emp:text id="IqpLoanApp.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" />
		    <emp:text id="IqpLoanApp.ass_sec_multiple" label="担保放大倍数" maxlength="10" defvalue="1" required="false" hidden="true" dataType="Double" />
		    <emp:text id="IqpLoanApp.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" onblur="changeRmbAmt()" dataType="Currency" />
		    <emp:text id="IqpLoanApp.risk_open_amt" label="风险敞口金额（元）" maxlength="18" readonly="true" required="true" dataType="Currency"/>
		    <emp:text id="IqpLoanApp.risk_open_rate" label="敞口比率" maxlength="10" readonly="true" required="true" dataType="Percent" />
		    
		    <emp:text id="IqpLoanApp.IqpDiscApp.bill_qty" label="票据数量" defvalue="0" maxlength="38" required="true" readonly="true"/>
		    <emp:date id="IqpLoanApp.IqpDiscApp.disc_date" label="贴现日期" required="false" readonly="true"/> 
			<emp:text id="IqpLoanApp.IqpDiscApp.disc_rate" label="贴现总利息" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.net_pay_amt" label="实付总金额" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
			<emp:select id="IqpLoanApp.limit_ind" label="授信额度使用标志" required="true" onchange="doChangLimitInt()" dictname="STD_LIMIT_IND" colSpan="2"/>	   
		    <emp:pop id="IqpLoanApp.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" />
		    <emp:pop id="IqpLoanApp.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" buttonLabel="选择" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="代理信息">
			<emp:select id="IqpLoanApp.IqpDiscApp.is_agent_disc" label="是否代理贴现" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:text id="IqpLoanApp.IqpDiscApp.agent_acct_no" label="代理人账户" maxlength="40" required="true" />
			<emp:text id="IqpLoanApp.IqpDiscApp.agent_acct_name" label="代理人名称" maxlength="80" required="true" />
			<emp:pop id="IqpLoanApp.IqpDiscApp.agent_org_no" url="getPrdBankInfoPopList.do?restrictUsed=false" returnMethod="getOrgNo" label="代理人开户行行号"  required="false"  />
			<emp:text id="IqpLoanApp.IqpDiscApp.agent_org_name" label="代理人开户行行名" maxlength="100" required="false" />
			<emp:textarea id="IqpLoanApp.IqpDiscApp.pvp_pact_cond_memo" label="出账落实条件说明" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="其他信息">
		    <emp:select id="IqpLoanApp.IqpDiscApp.five_classfiy" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpLoanApp.IqpDiscApp.spe_loan_type" label="特殊贷款类型" required="true" dictname="STD_ZB_LOAN_TYPE_EXT"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.limit_useed_type" label="额度占用来源" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" />
			<emp:select id="IqpLoanApp.IqpDiscApp.loan_use_type" label="借款用途" dictname="STD_ZB_USE_TYPE" required="true"/>
			<emp:select id="IqpLoanApp.IqpDiscApp.com_up_indtify" label="工业转型升级标识" dictname="STD_ZX_YES_NO" required="true" />

			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_type_displayname" label="贷款种类" url="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE" returnMethod="loantypeReturn" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2" />
			<emp:pop id="IqpLoanApp.IqpDiscApp.agriculture_type_displayname" label="涉农贷款类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_FARME" returnMethod="agricultureReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.ensure_project_loan_displayname" label="保障性安居工程贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.estate_adjust_type_displayname" label="产业结构调整类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_TRD_TYPE" returnMethod="onReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.strategy_new_loan_displayname" label="战略新兴产业类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_ZLXXCYLX" returnMethod="strategyReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.new_prd_loan_displayname" label="新兴产业贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_XXCYDK" returnMethod="newPrdReturn" required="false" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_direction_displayname" label="贷款投向" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="loanDirectionReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_belong1_displayname" label="贷款归属1" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS1" returnMethod="loanBelong1Return" required="false" hidden="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_belong2_displayname" label="贷款归属2" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS2" returnMethod="loanBelong2Return" required="false" hidden="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpDiscApp.loan_belong3_displayname" label="贷款归属3" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS3" returnMethod="loanBelong3Return" required="false" hidden="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			
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
			<emp:text id="IqpLoanApp.IqpDiscApp.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>					
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:pop id="IqpLoanApp.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpLoanApp.in_acct_br_id_displayname" label="入账机构"  required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" />
		    <emp:select id="IqpLoanApp.flow_type" label="流程类型"  required="false" hidden="true" defvalue="01" dictname="STD_ZB_FLOW_TYPE" />
		    <emp:text id="IqpLoanApp.input_id_displayname" label="登记人" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id_displayname" label="登记机构" required="false"  readonly="true"/>
			<emp:date id="IqpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true" readonly="true" required="false" defvalue="000"/>
			<emp:text id="IqpLoanApp.manager_br_id" label="管理机构" hidden="true"  />
			<emp:text id="IqpLoanApp.in_acct_br_id" label="入账机构" hidden="true"  />
			<emp:text id="IqpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true"/>
		</emp:gridLayout>
		
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		</emp:tabGroup>
	
	<div align="center">
		<br>
		<%if(!"".equals(biz_type) && biz_type != null) {%>
		<emp:button id="return" label="返回到列表页面"/>
		<% }%> 
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
</body>
</html>
</emp:page>
