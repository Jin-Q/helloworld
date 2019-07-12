<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="jsCont.jsp" flush="true" /> 
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String pvp = "";
	String biz_type="";
	String po_no="";
	String cargo_id="";
	String guaranty_no="";
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	String viewtype="";
	if(context.containsKey("viewtype")){
		viewtype = (String)context.getDataValue("viewtype");
	}
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("pvp")){
		pvp = (String)context.getDataValue("pvp");
	}
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	} 
	if(context.containsKey("po_no")){
		po_no = (String)context.getDataValue("po_no");
	}
	if(context.containsKey("cargo_id")){
		cargo_id = (String)context.getDataValue("cargo_id");
	}
	if(context.containsKey("guaranty_no")){
		guaranty_no = (String)context.getDataValue("guaranty_no");
	}
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
function doOnLoad(){ 

	CtrLoanCont.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
	CtrLoanCont.limit_acc_no._obj.addOneButton("limit_acc_no","查看",getLimitAccNo);
	CtrLoanCont.limit_credit_no._obj.addOneButton("limit_credit_no","查看",getLimitCreditNO);
	/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 start **/
	//getHLByCurr();//--加载汇率--	
	/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 end **/
	changeRmbAmt();//--加载折算金额--
	isShowNote();//是否承诺函
	isShowCompany();//是否担保公司
	//doChangLimitInt();//授信额度标识   
	doInitLimit(); //页面加载授信额度标识
	show_net();//显示所属网络
	controlBizType();//业务模式控制
	
    //产品为委托贷款时，贷款性质自动设置为委托贷款
	var prd_id =CtrLoanCont.prd_id._getValue();
	if(prd_id == "100063"){//贷款性质
		CtrLoanCont.CtrLoanContSub.loan_nature._setValue("1");
		CtrLoanCont.CtrLoanContSub.loan_nature._obj._renderReadonly(true);
    }
	 getBizType();//通过业务模式判断是否信托贷款
    var dbfs = CtrLoanCont.assure_main._getValue();
    if(dbfs!="" &&dbfs.substring(0,2)!='2'){
    	CtrLoanCont.assure_main_details._obj._renderReadonly(true);
    }
    getBizLineByCusId();
    initBillInfo();
    checkstampCollectMode();//印花税
    checkAgentDisc();//是否代理贴现
    checkAssureMain();//银行承兑汇票贴现，担保方式，担保方式细分隐藏
    cleanLimitInd();
    /** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 start **/
    //getHLByCurr4Security();//保证金金额
    /** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 end **/
    cleanLimitInd();
    checkstampCollectMode();
};

function getRepayForm(){
	var url = '<emp:url action=""/>';
	url = EMPTools.encodeURI(url);
	var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
	window.open(url,'newWindow',param);
};
function isShow(){
	  var payType = CtrLoanCont.CtrLoanContSub.is_conf_pay_type._getValue();
	  if(payType==1){
		  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderHidden(false);
		  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(true);
	  }else{
		  CtrLoanCont.CtrLoanContSub.pay_type._setValue("");
		  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(false);
		  CtrLoanCont.CtrLoanContSub.pay_type._obj._renderHidden(true);
	   }

};
	
	function doReturn() {
		if('<%=flag%>'=="ctrLoanCont4Tab" && '<%=po_no%>'!=""){
			var url = '<emp:url action="queryCtrLoanCont4TabList.do"/>?po_no='+'<%=po_no %>'; 
		}else if('<%=flag%>'=="ctrLoanCont4Tab" && '<%=cargo_id%>'!=""){
			var url = '<emp:url action="queryCtrLoanCont4TabList.do"/>?cargo_id='+'<%=cargo_id %>'; 
		}else if('<%=flag%>'=="ctrLoanCont4Tab" && '<%=guaranty_no%>'!=""){
			var url = '<emp:url action="queryCtrLoanCont4TabList.do"/>?guaranty_no='+'<%=guaranty_no %>'; 
		}else if('<%=flag%>'=="ctrLoanCont"){
        	var url = '<emp:url action="queryCtrLoanContList.do"/>?biz_type='+'<%=biz_type %>';
        }else{
        	var url = '<emp:url action="queryCtrLoanContHistoryList.do"/>?biz_type='+'<%=biz_type %>';
        }
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function doReturn1() {
		window.close();
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	function initBillInfo(){
		var prd_id = CtrLoanCont.prd_id._getValue(); 
		if(prd_id==300021){
			CtrLoanCont.CtrDiscCont.busdrft_dscnt_mode._obj._renderHidden(true);
		}else if(prd_id=300020){
			CtrLoanCont.CtrDiscCont.busdrft_dscnt_mode._obj._renderHidden(false);
		}
	}
	
	function agricultureReturn(date){
		CtrLoanCont.CtrDiscCont.agriculture_type._obj.element.value=date.id;
		CtrLoanCont.CtrDiscCont.agriculture_type_displayname._setValue(date.label);
	};
	function projectReturn(date){
		CtrLoanCont.CtrDiscCont.ensure_project_loan._obj.element.value=date.id;
		CtrLoanCont.CtrDiscCont.ensure_project_loan_displayname._setValue(date.label);
	};
	function onReturn(date){
		CtrLoanCont.CtrDiscCont.estate_adjust_type._obj.element.value=date.id;
		CtrLoanCont.CtrDiscCont.estate_adjust_type_display._setValue(date.label);
	};
	function strategyReturn(date){
		CtrLoanCont.CtrDiscCont.strategy_new_loan._obj.element.value=date.id;
		CtrLoanCont.CtrDiscCont.strategy_new_loan_display._setValue(date.label);
	};
	function newPrdReturn(date){
		CtrLoanCont.CtrDiscCont.new_prd_loan._obj.element.value=date.id;
		CtrLoanCont.CtrDiscCont.new_prd_loan_display._setValue(date.label);
	};
	function greenPrdReturn(date){
		CtrLoanCont.CtrDiscCont.green_prd._obj.element.value=date.id;
		CtrLoanCont.CtrDiscCont.green_prd_display._setValue(date.label);
	};
	function loanBelong1Return(date){
		CtrLoanCont.CtrDiscCont.loan_belong1._obj.element.value=date.id;
		
	};

	function loanDirectionReturn(date){
		CtrLoanCont.CtrDiscCont.loan_direction._obj.element.value=date.id;
		loan_dirname._setValue(date.label);
	};
	function loanBelong2Return(date){
		CtrLoanCont.CtrDiscCont.loan_belong2._obj.element.value=date.id;
		
	};
	function loanBelong3Return(date){
		CtrLoanCont.CtrDiscCont.loan_belong3._obj.element.value=date.id;
	};
	function ensureProjectReturn(date){
		CtrLoanCont.CtrDiscCont.loan_use_type._obj.element.value=date.id;
	};
	/*--user code begin--*/
	function checkstampCollectMode(){
		var prd_id = CtrLoanCont.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
			CtrLoanCont.CtrDiscCont.stamp_collect_mode._setValue("2");
			CtrLoanCont.CtrDiscCont.stamp_collect_mode._obj._renderReadonly(true);
		}
	};

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrLoanCont.serno._getValue();	//业务编号
		data['cus_id'] = CtrLoanCont.cus_id._getValue();	//客户编号
		data['prd_id'] = CtrLoanCont.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()" >
	
  <emp:tabGroup mainTab="base_tab" id="mainTab" >
	 <emp:tab label="合同信息" id="base_tab" needFlush="true" initial="true" >
          <emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="合同基本信息">
			<emp:text id="CtrLoanCont.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:date id="CtrLoanCont.ser_date" label="签订日期"  required="true" readonly="true"  />			
			<emp:text id="CtrLoanCont.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"  />
			<emp:text id="CtrLoanCont.cn_cont_no" label="中文合同编号" maxlength="100" required="true" />
			<emp:text id="CtrLoanCont.prd_id" label="产品编号" maxlength="6" required="false" readonly="true"/>
			<emp:text id="CtrLoanCont.prd_id_displayname" label="产品名称"  required="false" readonly="true"/>
			<emp:text id="CtrLoanCont.cus_id" label="客户码" maxlength="40" required="false" colSpan="2" readonly="true" />
			<emp:text id="CtrLoanCont.cus_id_displayname" label="客户名称"  required="false" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			
			<emp:select id="CtrLoanCont.CtrDiscCont.bill_type" label="票据种类" required="true" readonly ="true" dictname="STD_DRFT_TYPE" />
			<emp:select id="CtrLoanCont.CtrDiscCont.busdrft_dscnt_mode" label="商票贴现类型" required="true" dictname="STD_BUSDRFT_DISCOUNT_TYPE"/> 
			<emp:select id="CtrLoanCont.CtrDiscCont.is_elec_bill" label="是否电子票据" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="CtrLoanCont.CtrDiscCont.disc_type" label="贴现类型" required="true" dictname="STD_ZB_DISCOUNT_TYPE"/>
			
			<emp:select id="CtrLoanCont.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true"/>
			<emp:select id="CtrLoanCont.assure_main_details" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
			<emp:select id="CtrLoanCont.biz_type" label="业务模式" required="false" dictname="STD_BIZ_TYPE" readonly="true"/>
			<emp:select id="CtrLoanCont.rent_type" label="租赁模式" required="false" dictname="STD_RENT_TYPE" readonly="true"/>
			<emp:select id="CtrLoanCont.is_promissory_note" label="是否承诺函下" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>			
			<emp:text id="CtrLoanCont.promissory_note" label="承诺函" maxlength="80" required="false"  hidden="true" colSpan="2"/>
			<emp:select id="CtrLoanCont.CtrDiscCont.is_collect_stamp" label="是否收取印花税" required="false" dictname="STD_ZX_YES_NO" readonly="true" />
			<emp:select id="CtrLoanCont.CtrDiscCont.stamp_collect_mode" label="印花税收取方式" required="false" hidden="true" readonly="true" dictname="STD_ZB_STAMP_MODE"/>
			<emp:select id="CtrLoanCont.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>						
			<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整  -->
			<emp:text id="CtrLoanCont.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" readonly="true" />
			<emp:text id="CtrLoanCont.trust_company" label="信托公司" maxlength="100" required="false" hidden="true"/>	
			<emp:pop id="CtrLoanCont.belong_net" label="所属网络" url="null" required="false" readonly="true" />
		  </emp:gridLayout>
          <emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:select id="CtrLoanCont.cont_cur_type" label="合同币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		    <emp:text id="CtrLoanCont.exchange_rate" label="汇率" maxlength="10" required="false" dataType="Double" readonly="true"/>
		    <emp:text id="CtrLoanCont.cont_amt" label="合同金额" maxlength="18" required="false" dataType="Currency" readonly="true" />
		    <emp:text id="CtrLoanCont.apply_rmb_amount" label="折合成人民币金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		    
            <emp:select id="CtrLoanCont.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security();" readonly="true" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="CtrLoanCont.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
		   	<emp:text id="CtrLoanCont.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	<emp:text id="CtrLoanCont.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	
		    <emp:text id="CtrLoanCont.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" />
			<emp:text id="CtrLoanCont.ass_sec_multiple" label="担保放大倍数" maxlength="10" required="false" dataType="Double" hidden="true"/>
			<emp:text id="CtrLoanCont.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="CtrLoanCont.risk_open_amt" label="风险敞口金额（元）" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		    <emp:text id="CtrLoanCont.risk_open_rate" label="敞口比率" maxlength="10" required="false" dataType="Percent" readonly="true"/> 
		    
		    <emp:text id="CtrLoanCont.CtrDiscCont.bill_qty" label="票据数量" defvalue="0" maxlength="38" required="true" readonly="true"/>
		    <emp:date id="CtrLoanCont.CtrDiscCont.disc_date" label="贴现日期" required="false" readonly="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.disc_rate" label="贴现总利息" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrDiscCont.net_pay_amt" label="实付总金额" defvalue="0" maxlength="18" required="true" dataType="Currency" readonly="true"/>
		     
		    <emp:text id="CtrLoanCont.cont_balance" label="合同余额" maxlength="18" required="false" dataType="Currency" readonly="false"  hidden="true"/>			
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">   
		    <emp:select id="CtrLoanCont.limit_ind" label="授信额度使用标志" required="false" dictname="STD_LIMIT_IND" colSpan="2" readonly="true" />
		    <emp:text id="CtrLoanCont.limit_acc_no" label="授信台账编号" maxlength="40" required="false" readonly="false"/>
			<emp:text id="CtrLoanCont.limit_credit_no" label="第三方授信编号" maxlength="40" required="false" readonly="false"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="代理信息">
			<emp:select id="CtrLoanCont.CtrDiscCont.is_agent_disc" label="是否代理贴现" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:text id="CtrLoanCont.CtrDiscCont.agent_acct_no" label="代理人账户" maxlength="40" required="true" />
			<emp:text id="CtrLoanCont.CtrDiscCont.agent_acct_name" label="代理人名称" maxlength="80" required="true" />
			<emp:pop id="CtrLoanCont.CtrDiscCont.agent_org_no" url="getPrdBankInfoPopList.do?restrictUsed=false" returnMethod="getOrgNo" label="代理人开户行行号"  required="true"  readonly ="true"/>
			<emp:text id="CtrLoanCont.CtrDiscCont.agent_org_name" label="代理人开户行行名" maxlength="100" required="true" readonly ="true"/>
			<emp:textarea id="CtrLoanCont.CtrDiscCont.pvp_pact_cond_memo" label="出账落实条件说明" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="其他信息">
		   <emp:select id="CtrLoanCont.CtrDiscCont.five_classfiy" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrDiscCont.spe_loan_type" label="特殊贷款类型" required="false" dictname="STD_ZB_LOAN_TYPE_EXT" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrDiscCont.limit_useed_type" label="额度占用来源" required="false" dictname="STD_POSITION_ENGROSS_ORIGIN" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrDiscCont.loan_use_type" label="借款用途" dictname="STD_ZB_USE_TYPE" required="true" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrDiscCont.com_up_indtify" label="工业转型升级标识" dictname="STD_ZX_YES_NO" required="false" readonly="true"/>
			
			<emp:pop id="CtrLoanCont.CtrDiscCont.loan_type_displayname" label="贷款种类" url="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE" returnMethod="loantypeReturn" required="true" readonly="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly" />
			<emp:pop id="CtrLoanCont.CtrDiscCont.agriculture_type_displayname" label="涉农贷款类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_FARME" returnMethod="agricultureReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2" readonly="true"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.ensure_project_loan_displayname" label="保障性安居工程贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="fasle" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2" readonly="true"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.estate_adjust_type_displayname" label="产业结构调整类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_TRD_TYPE" returnMethod="onReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_input2"/> 
			<emp:pop id="CtrLoanCont.CtrDiscCont.strategy_new_loan_displayname" label="战略新兴产业类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_ZLXXCYLX" returnMethod="strategyReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_input2"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.new_prd_loan_displayname" label="新兴产业贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_XXCYDK" returnMethod="newPrdReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_input2"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.loan_direction_displayname" label="贷款投向" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="loanDirectionReturn" required="false" colSpan="2" readonly="true" buttonLabel="选择" cssElementClass="emp_input2"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.loan_belong1_displayname" label="贷款归属1" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS1" returnMethod="loanBelong1Return" required="false" hidden="true" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_input2"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.loan_belong2_displayname" label="贷款归属2" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS2" returnMethod="loanBelong2Return" required="false" hidden="true" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_input2"/>
			<emp:pop id="CtrLoanCont.CtrDiscCont.loan_belong3_displayname" label="贷款归属3" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS3" returnMethod="loanBelong3Return" required="false" hidden="true"  readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_input2"/>
			
			<emp:text id="CtrLoanCont.CtrDiscCont.loan_type" label="贷款种类" hidden="true"/>
			<emp:text id="CtrLoanCont.CtrDiscCont.agriculture_type" label="涉农贷款类型" hidden="true"/>
			<emp:text id="CtrLoanCont.CtrDiscCont.ensure_project_loan" label="保障性安居工程贷款" hidden="true"/>
			<emp:text id="CtrLoanCont.CtrDiscCont.estate_adjust_type" label="产业结构调整类型" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.strategy_new_loan" label="战略新兴产业类型" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.new_prd_loan" label="新兴产业贷款" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.loan_direction" label="贷款投向" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.loan_belong1" label="贷款归属1" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.loan_belong2" label="贷款归属2" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrDiscCont.loan_belong3" label="贷款归属3" hidden="true"/> 
            
			<emp:textarea id="CtrLoanCont.CtrDiscCont.repay_src_des" label="还款来源"  required="false" readonly="true"/>
		  </emp:gridLayout>
 
		  <emp:gridLayout id="" maxColumn="3" title="登记信息">   
		    <emp:text id="CtrLoanCont.manager_br_id_displayname" label="管理机构"  required="false" readonly="true"/>
		    <emp:text id="CtrLoanCont.in_acct_br_id_displayname" label="入账机构" required="false" hidden="true"/>
		    <emp:text id="CtrLoanCont.cont_number" label="评估分数" maxlength="38" required="false" readonly="false" hidden="true"/>  
			
			<emp:text id="CtrLoanCont.input_id_displayname" label="登记人"  required="false"  readonly="true"/>
		    <emp:text id="CtrLoanCont.input_br_id_displayname" label="登记机构"  required="false"  readonly="true"/>
		    <emp:date id="CtrLoanCont.input_date" label="登记日期" required="false" readonly="true"/>
		    <emp:text id="CtrLoanCont.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:text id="CtrLoanCont.in_acct_br_id" label="入账机构" hidden="true" required="true"/>
		    <emp:text id="CtrLoanCont.input_id" label="登记人" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:text id="CtrLoanCont.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:select id="CtrLoanCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" required="false" hidden="true"/> 
		</emp:gridLayout>
		</emp:tab>
	<emp:ExtActTab></emp:ExtActTab> 
	</emp:tabGroup>

	<div align="center">
		<br>
		<%if(!pvp.equals("pvp")&&!"out".equals(viewtype)){ %>
		    <emp:button id="return" label="返回到列表页面"/>
		<%}%>
		<%if(!"pvp".equals(pvp)&&"out".equals(viewtype)){ %>
		    <emp:button id="return1" label="关闭"/>
		<%}%>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
</body>
</html>
</emp:page>
