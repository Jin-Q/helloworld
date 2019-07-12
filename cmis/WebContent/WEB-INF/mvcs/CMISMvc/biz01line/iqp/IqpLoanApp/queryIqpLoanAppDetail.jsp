<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	String flag = "";
	String menuId = "";
	String flow = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	}
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
    if(context.containsKey("flow")){
    	flow = (String)context.getDataValue("flow");
    }
%>
<jsp:include page="jsIqpComm.jsp" flush="true" /> 
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>
<!--added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  begin-->
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<!--added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  end-->
<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
.emp_input{
border: 1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border: 1px solid #b7b7b7;
width:600px;
}
</style>  
<script type="text/javascript">
	
	function doReturn() {
		var flag = '<%=flag %>';
		if(flag == "iqpLoanApp"){
			var url = '<emp:url action="queryIqpLoanAppList.do"/>?menuId='+'<%=menuId %>'+'&biz_type='+'<%=biz_type%>'+'&flg=${context.flg}';   
		}else{
			var url = '<emp:url action="queryIqpLoanAppHistoryList.do"/>?menuId='+'<%=menuId %>'+'&biz_type='+'<%=biz_type%>'+'&flg=${context.flg}';
		}
		url = EMPTools.encodeURI(url); 
		window.location=url; 
	};
	function doOnLoad(){ 
		IqpLoanApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpLoanApp.belong_net._obj.addOneButton("belong_net","查看",getBelongForm);
		IqpLoanApp.limit_acc_no._obj.addOneButton("limit_acc_no","查看",getLimitAccNo);
		IqpLoanApp.limit_credit_no._obj.addOneButton("limit_credit_no","查看",getLimitCreditNO);
		IqpLoanApp.IqpLoanAppSub.repay_type_displayname._obj.addTheButton("repay_type","生成还款方案",getRepayForm);
		IqpLoanApp.IqpLoanAppSub.serno._setValue(IqpLoanApp.serno._getValue()); 
		
		//加载页面支付方式的判断
		var pay_type = IqpLoanApp.IqpLoanAppSub.conf_pay_type._getValue();
		if(pay_type==1){
			IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderHidden(false);
			IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderRequired(true);
		}
		isPay();
		/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 start **/
		//getHLByCurr();//--加载汇率--
		/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 end **/
		getRulMounth();//--加载利率--
		changeRmbAmt();//--加载折算金额--
		isShowNote();//是否承诺函
		isShowCompany();//是否担保公司
		//doChangLimitInt();//授信额度标识  
		doInitLimit(); //页面加载授信额度标识 
		show_net();//显示所属网络
		ir_accord_typeChange("init");
		changeIrFloatType();//根据利率浮动方式同比调整显示
		changeOverdueFloatType();
		changeDefaultFloatType();
		controlBizType();//业务模式控制
		hiddenIr();
		hiddenBWIr();
		reality_ir_yChange();//通过年利率计算月利率
		is_limit_cont_pay();//是否额度合同项下支用
		is_person_consume();//是否个人消费贷款，贷款投向字段处理
		/**************************************************
		* 2014-07-01 Edited by  FCL  增加公司无间贷业务*****
		***************************************************/
		if('${context.belg_line}' == "BL100" || '${context.belg_line}' == "BL200" || "${context.belg_line}" == "BL300"){
			IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderHidden(true);
		}else{
			IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderRequired(false);
		} 
		/*************END Edited by FCL 2014-07-01*****************/
		var sfgjd = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
		var dkxs = IqpLoanApp.IqpLoanAppSub.loan_form._getValue();//贷款形式
		if(sfgjd == "1" || dkxs == "3"){
			IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderHidden(false);
			IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderHidden(true); 
			IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderRequired(true);     
		}else{
			IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.repay_bill._obj._renderRequired(false);
		}
	    //产品为委托贷款时，贷款性质自动设置为委托贷款
		var prd_id =IqpLoanApp.prd_id._getValue();
		if(prd_id == "100063" || prd_id == "100065"){//贷款性质 （个人委托贷款，企业委托贷款）
			IqpLoanApp.IqpLoanAppSub.loan_nature._setValue("2");
			IqpLoanApp.IqpLoanAppSub.loan_nature._obj._renderReadonly(true);
	    }
		getFlg();//判断是否定向资管委托贷款
		loan_nature_change();//贷款性质
	    var dbfs = IqpLoanApp.assure_main._getValue();
	    if(dbfs!="" &&dbfs.substring(0,2)!='2'){
	    	IqpLoanApp.assure_main_details._obj._renderReadonly(true);
	    }
		getBelglineByKhm();
		setRepayType();//还款方式策略信息
		setRepayTerm();//还款方式信息 
		is_off_busi();//表外业务字段隐藏js  
		is_show_pad_rate();//贷款承诺、信贷证明、贷款意向：利率信息中的（垫款利率）不显示
		//ir_typeChange();
		checkstampCollectMode();//印花税收取方式，如果是委托贷款字典放开都可选，否则默认"全由借款人支付"
		var repay_mode_type = IqpLoanApp.IqpLoanAppSub.repay_mode_type._getValue();
		//checkFromRepayType(repay_mode_type);
		checkRepayDate(repay_mode_type);
		readonly4ReturnType();
		readonly4RateInfo();
		getBizLineByCusId();
		changeStamp();
		showLoanBelong();//贷款归属的显示还是隐藏(2014-04-15需求变更)
		isShow();//是否隐藏支付方式
		ifRrAccordType();////反向计算利率浮动比 并隐藏 

		/**add by lisj 2014年11月18日需求:【XD140818051】 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  begin**/
		if("${context.belg_line}" == "BL300"){
			var assureMain = IqpLoanApp.assure_main._getValue();
			if(assureMain =='300' || assureMain == '400' || assureMain =='500' || assureMain == '510'){
				IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderHidden(true);
				IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderRequired(false);
			}else{
			IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderHidden(false);
			IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderRequired(true);
			}
		}
		/**add by lisj 2014年11月18日需求:【XD140818051】 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  end**/
		/**added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  begin**/
		isSelfLoans();
		/**added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  end**/

		 /**added by wangj 2015/08/19  需求编号:XD150825064_源泉宝法人账户透支改造  begin**/
		 if(prd_id=="100051"){
		    	IqpLoanApp.IqpLoanAppSub.belg_line._obj._renderHidden(false);
		    	IqpLoanApp.IqpLoanAppSub.belg_line._obj._renderRequired(true);
		 }
		 /**added by wangj 2015/08/19 需求编号:XD150825064_源泉宝法人账户透支改造  end**/
		 /**add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
	     if(prd_id=="100090" || ("${context.flg}" == "csgn" && prd_id =="100063") || prd_id =="400020" || prd_id =="400021"){
		     IqpLoanApp.is_structured_fin._obj._renderRequired(true);
		     IqpLoanApp.is_structured_fin._obj._renderHidden(false);
		     IqpLoanApp.IqpLoanAppSub.marketing_br_id._obj._renderHidden(false);
		     IqpLoanApp.IqpLoanAppSub.marketing_br_id._obj._renderRequired(true);
		 }else{
			 IqpLoanApp.is_structured_fin._obj._renderRequired(false);
		     IqpLoanApp.is_structured_fin._obj._renderHidden(true);
		     IqpLoanApp.IqpLoanAppSub.marketing_br_id._obj._renderHidden(true);
		     IqpLoanApp.IqpLoanAppSub.marketing_br_id._obj._renderRequired(false);
		 }
	    /**add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
	};
	/**added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  begin**/
	function isSelfLoans(){
	//隐藏自助信息
		var prd_id =IqpLoanApp.prd_id._getValue();
		if(prd_id=="100084"||prd_id=="100085"){
			$(".emp_field_label:eq(44)").text("自助贷款期限");
			$(".emp_field_label:eq(45)").text("自助贷款期限类型");
			IqpLoanApp.IqpLoanAppSub.cus_card_code._obj._renderHidden(false);
			IqpLoanApp.IqpLoanAppSub.cus_card_code._obj._renderRequired(true);
		}
	}
	/**added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  end**/
	/**add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）begin**/
	function getBelongForm(){
		var belong_net = IqpLoanApp.belong_net._getValue();
		var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?net_agr_no='+belong_net+'&op=view&menuId=netmanager';  
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	/**add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）end**/

	function checkstampCollectMode(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("4");
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderReadonly(true);
		}
		if(prd_id == '700020' || prd_id == '700021' || prd_id == '400020' || prd_id == '500032'){//信用证业务,外汇保函，提货担保隐藏
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
		}
	}	

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpLoanApp.serno._getValue();	//业务编号
		data['cus_id'] = IqpLoanApp.cus_id._getValue();	//客户码
		data['prd_id'] = IqpLoanApp.prd_id._getValue();	//业务品种
		data['prd_stage'] = "<%=flow%>" =='wf'?'YWSP':'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="aform" >    
	  <emp:text id="IqpLoanApp.IqpLoanAppSub.repay_type" label="还款方式"  required="false" hidden="true"/>
	</emp:form>
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
       <emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpLoanAppGroup" maxColumn="2" title="基本信息" > 
			<emp:text id="IqpLoanApp.serno" label="业务流水号" maxlength="40" required="true" readonly="true"/>
			<emp:date id="IqpLoanApp.apply_date" label="申请日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpLoanApp.prd_id" label="产品编号" maxlength="6" required="true" readonly="true"/>
			<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="80" required="true" readonly="true"/>   
			<!--added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  begin-->
			<emp:text id="IqpLoanApp.IqpLoanAppSub.cus_card_code" label="自助卡号" required="false" hidden="true" readonly="true"/>	
			<!--added by yangzy 2015/04/02 需求编号:XD150318023,微贷平台零售自助贷款改造  end-->
			<emp:text id="IqpLoanApp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="IqpLoanApp.cus_id_displayname" label="客户名称" required="false"  readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="IqpLoanApp.is_rfu" label="是否曾被拒绝" required="false" dictname="STD_ZX_YES_NO" colSpan="1"/>	 
            <emp:select id="IqpLoanApp.is_spe_cus" label="是否特殊客户" required="false" dictname="STD_ZX_YES_NO" />     
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_form" label="贷款形式" onchange="is_cloas_loan_change();" required="true" dictname="STD_LOAN_FORM" defvalue="0" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_nature" label="贷款性质" onchange="loan_nature_change();" required="true" dictname="STD_LOAN_NATYRE" defvalue="0"/>
			<emp:select id="IqpLoanApp.assure_main" label="担保方式" required="true" onchange="assure_mainChange();" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="IqpLoanApp.assure_main_details" label="担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.is_close_loan" label="是否无间贷" hidden="true" required="false" dictname="STD_ZX_YES_NO" defvalue="2" onclick="is_cloas_loan_change()"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.repay_bill" label="偿还借据" url="queryAccPop.do?returnMethod=getBill&cus_id=${context.IqpLoanApp.cus_id}"  required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:text id="IqpLoanApp.cus_total_amt" label="客户半年日均" maxlength="40" hidden="true" colSpan="2" required="false" readonly="true" dataType="Currency"/>
			<emp:select id="IqpLoanApp.is_promissory_note" label="是否承诺函下" defvalue="2" required="false" dictname="STD_ZX_YES_NO" onclick="isShowNote()"/> 
			<emp:pop id="IqpLoanApp.promissory_note" label="承诺函" url="queryPromissoryPopList.do?returnMethod=getPromissory&cus_id=${context.IqpLoanApp.cus_id}" required="false" hidden="true" />  
			<emp:select id="IqpLoanApp.IqpLoanAppSub.is_collect_stamp" label="是否收取印花税" required="true" dictname="STD_ZX_YES_NO" readonly="true" defvalue="1"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.stamp_collect_mode" label="印花税收取方式" required="true" readonly="true" dictname="STD_ZB_STAMP_MODE" />
			<emp:select id="IqpLoanApp.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" onclick="isShowCompany()"/>
			<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整-->
			<emp:text id="IqpLoanApp.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" />
			<emp:text id="IqpLoanApp.trust_company" label="信托公司" maxlength="100" required="false" hidden="true" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
			<emp:select id="IqpLoanApp.is_structured_fin" label="是否结构化融资" dictname="STD_ZX_YES_NO" required="false" hidden="true" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
			<emp:select id="IqpLoanApp.is_limit_cont_pay" label="是否额度合同项下支用" onchange="isPay();" defvalue="2" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="IqpLoanApp.limit_cont_no" label="额度合同编号" url="queryCtrLimitContListPop.do?returnMethod=getContMsg" required="false" hidden="true" />
		    <emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" hidden="true" required="false"  onclick="show_net();controlBizType();"/>
			<emp:select id="IqpLoanApp.rent_type" label="租赁模式" dictname="STD_RENT_TYPE" defvalue="0" required="false" />
			<emp:pop id="IqpLoanApp.belong_net" label="所属网络" url="queryNetMagInfoPop.do?returnMethod=getNetMagInfo" required="false" />
			<!-- add by lisj 2014-12-15 需求:【XD140818051】 增加抵质押类型字段-->
			<emp:select id="IqpLoanApp.IqpLoanAppSub.collateral_type" label="抵质押类型" dictname="STD_COLLATERAL_TYPE" required="false" hidden="true" readonly="true"/>  
			
			<!--  /**added by wangj 2015/08/19  需求编号:XD150825064  源泉宝法人账户透支改造  begin**/	-->
			<emp:select id="IqpLoanApp.IqpLoanAppSub.belg_line" label="业务条线" dictname="STD_ZB_BUSILINE" required="false" hidden="true" readonly="true"/>
		  	<!--  /**added by wangj 2015/08/19  需求编号:XD150825064  源泉宝法人账户透支改造  end**/	--> 
		</emp:gridLayout>   
		
		<div id="payInfo" >
		<emp:gridLayout id="" maxColumn="2" title="支付信息">
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.conf_pay_type" label="是否确定支付方式" required="true" dictname="STD_ZX_YES_NO" onclick="isShow()"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.pay_type" label="支付方式" required="false" dictname="STD_IQP_PAY_TYPE" hidden="true"/>	    
		</emp:gridLayout>
		</div>
		
		<emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:select id="IqpLoanApp.apply_cur_type" label="申请币种" required="true" onchange="getHLByCurr()" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
		    <emp:text id="IqpLoanApp.exchange_rate" label="汇率" maxlength="16" readonly="true" required="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.apply_amount" label="申请金额" maxlength="18" required="true" onblur="changeRmbAmt()" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.apply_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		   	
		   	<emp:select id="IqpLoanApp.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security();" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpLoanApp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
		   	<emp:text id="IqpLoanApp.security_rate" label="保证金比例" maxlength="16" readonly="false" onchange="changeSecRate();changeRmbAmt4Security()" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		   	<emp:text id="IqpLoanApp.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.ass_sec_multiple" label="担保放大倍数" maxlength="10" defvalue="1" required="false" hidden="true" dataType="Double" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" onblur="changeRmbAmt()" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.risk_open_amt" label="风险敞口金额（元）" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpLoanApp.risk_open_rate" label="敞口比率" maxlength="10" readonly="true" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="期限信息">
		   <emp:select id="IqpLoanApp.IqpLoanAppSub.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" onblur="cleanDate()"/>
		   <emp:text id="IqpLoanApp.IqpLoanAppSub.apply_term" label="申请期限" required="true" dataType="Int" onchange="getRate();" cssElementClass="emp_currency_text_readonly"/>
		   <emp:select id="IqpLoanApp.IqpLoanAppSub.is_delay" label="是否节假日顺延" required="true" dictname="STD_ZX_YES_NO"/>
		   
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
			<emp:select id="IqpLoanApp.limit_ind" label="授信额度使用标志" required="true" onchange="doChangLimitInt()" dictname="STD_LIMIT_IND" colSpan="2" readonly="true"/>
		    <emp:pop id="IqpLoanApp.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" />
		    <emp:pop id="IqpLoanApp.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" buttonLabel="选择" />
		</emp:gridLayout>
		
		<div id="rateInfo" >
		<emp:gridLayout id="" maxColumn="2" title="利率信息">
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.ir_accord_type" label="利率依据方式"  onchange="ir_accord_typeChange('change');" required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_type" label="利率种类" hidden="true" required="false" onchange="ir_typeChange();" dictname="STD_ZB_RATE_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ruling_ir" cssElementClass="emp_currency_text_readonly" label="基准利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate"/>
			<emp:text id="ruling_mounth" cssElementClass="emp_currency_text_readonly" label="对应基准利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>  
			<emp:text id="IqpLoanApp.IqpLoanAppSub.pad_rate_y" cssElementClass="emp_currency_text_readonly" label="垫款利率（年）" hidden="true" maxlength="16" colSpan="2" readonly="false" required="false" dataType="Rate"/>  
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_adjust_type" label="利率调整方式" hidden="true" required="false" colSpan="2" dictname="STD_IR_ADJUST_TYPE" readonly="true"/>
			
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ir_next_adjust_term" cssElementClass="emp_currency_text_readonly" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" />
			<emp:date id="IqpLoanApp.IqpLoanAppSub.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
			
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType();"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ir_float_rate" cssElementClass="emp_currency_text_readonly" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getRelYM();" required="false" dataType="Percent2" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ir_float_point" cssElementClass="emp_currency_text_readonly" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getRelYM();" required="false" />
			
			<emp:text id="IqpLoanApp.IqpLoanAppSub.reality_ir_y" cssElementClass="emp_currency_text_readonly" label="执行利率（年）" hidden="true" onchange="reality_ir_yChange()" readonly="true" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="reality_mounth" label="执行利率(月)" cssElementClass="emp_currency_text_readonly" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			<emp:select id="IqpLoanApp.IqpLoanAppSub.overdue_float_type" label="逾期利率浮动方式" hidden="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.overdue_rate" cssElementClass="emp_currency_text_readonly" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getOverdueRateY();" required="false" dataType="Percent2" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.overdue_point" cssElementClass="emp_currency_text_readonly" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getOverdueRateY();" required="false" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.overdue_rate_y" cssElementClass="emp_currency_text_readonly" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" readonly="true" required="false" dataType="Rate"/>
			
			<emp:select id="IqpLoanApp.IqpLoanAppSub.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.default_rate" cssElementClass="emp_currency_text_readonly" label="违约利率浮动比" maxlength="16" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Percent2" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.default_point" cssElementClass="emp_currency_text_readonly" label="违约利率浮动点数" maxlength="38" hidden="true" onchange="getDefaultRateY();" required="false" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.default_rate_y" cssElementClass="emp_currency_text_readonly" label="违约利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate"/>
			
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ruling_ir_code" cssElementClass="emp_currency_text_readonly" label="基准利率代码" hidden="true" maxlength="40"  required="false"/>
		</emp:gridLayout>
		</div>
		
		<div id="returnType" >   
		<emp:gridLayout id="" maxColumn="2" title="还款方式信息">
		    <emp:pop id="IqpLoanApp.IqpLoanAppSub.repay_type_displayname" label="还款方式" url="queryPrdRepayModeList.do?returnMethod=getRepayType" required="true" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.interest_term" label="计息周期" required="true" dictname="STD_IQP_RATE_CYCLE" onblur="setRepayTerm();" readonly="true"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.repay_term" label="还款间隔周期" required="true" dictname="STD_BACK_CYCLE" onchange="cleanSpace()" readonly="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.repay_space" cssElementClass="emp_currency_text_readonly" label="还款间隔" maxlength="10" required="true" dataType="Int" onblur="checkTerm()"  readonly="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.repay_date" label="还款日" required="true" readonly="true"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.is_term" label="是否期供" required="true" readonly="true" dictname="STD_ZX_YES_NO" />
			<emp:date id="IqpLoanApp.IqpLoanAppSub.fir_repay_date" label="首次还款日" required="false" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.repay_mode_type" label="还款方式类型" required="false" hidden="true"/>
		</emp:gridLayout>
		<%-- <div  class='emp_gridlayout_title'>还款方式策略信息</div>
			<emp:table icollName="PrdRepayPlanList" pageMode="true" url="getPrdRepayPlanUpdatePage.do" reqParams="repay_mode_id=${context.IqpLoanAppSub.repay_type}" >	
				<emp:text id="serno" label="编号" hidden="true"/>
				<emp:text id="exe_times" label="执行期数" />
				<emp:text id="repay_mode" label="还款方式" dictname="STD_ZB_RPYM_OPT"/> 
				<emp:text id="repay_type" label="还款类型" dictname="STD_ZB_SETL_TYPE"/>
				<emp:text id="rate_sprd" label="利差" dataType="Rate"/>    
				<emp:text id="rate_pefloat" label="利率浮动比例" dataType="Rate"/>  
				<emp:text id="rate_cal_basic" label="利率计算基础" dictname="STD_ZB_RATE_BASE"/>
				<emp:text id="int_cal_basic" label="利息计算基础" dictname="STD_ZB_INT_BASE"/>
				<emp:text id="repay_interval" label="还款间隔" />
				<emp:text id="repay_trem" label="还款间隔周期" dictname="STD_BACK_CYCLE"/> 
			</emp:table> --%>
		</div> 
		<emp:gridLayout id="" maxColumn="2" title="其他信息">
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.five_classfiy" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.spe_loan_type" label="特殊贷款类型" required="true" dictname="STD_ZB_LOAN_TYPE_EXT"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.limit_useed_type" label="额度占用来源" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_use_type" label="借款用途" dictname="STD_ZB_USE_TYPE" required="true"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.com_up_indtify" label="工业转型升级标识" dictname="STD_ZX_YES_NO" required="true" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.principal_loan_typ" label="委托贷款种类" required="true" dictname="STD_ZB_COMMISS_TYPE" readonly="true"/>
			
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_type_displayname" label="贷款种类" url="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE" returnMethod="loantypeReturn" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2" />
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.agriculture_type_displayname" label="涉农贷款类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_FARME" returnMethod="agricultureReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<%
			    // 个人条线 "将保障性安居工程贷款"变更为"个人经营性贷款归属"  Edited by FCL 20141222
			    String belg_line = "";
		     	if(context.containsKey("belg_line")){
		     		belg_line = (String)context.getDataValue("belg_line");
			    }
		     	if("BL300".equals(belg_line)){%>
		    	    <emp:pop id="IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname" label="个人经营性贷款归属" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
		    <%
		     	}else{
			%>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname" label="保障性安居工程贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<%} %>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.estate_adjust_type_displayname" label="产业结构调整类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_TRD_TYPE" returnMethod="onReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.strategy_new_loan_displayname" label="战略新兴产业类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_ZLXXCYLX" returnMethod="strategyReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.new_prd_loan_displayname" label="新兴产业贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_XXCYDK" returnMethod="newPrdReturn" required="false" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_direction_displayname" label="贷款投向" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="loanDirectionReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname" label="贷款归属1" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS1" returnMethod="loanBelong1Return" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname" label="贷款归属2" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS2" returnMethod="loanBelong2Return" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname" label="贷款归属3" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS3" returnMethod="loanBelong3Return" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_belong4" label="贷款归属4" required="true" dictname="STD_ZB_DKGS4" />
			
			<emp:text id="IqpLoanApp.IqpLoanAppSub.loan_type" label="贷款种类" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.agriculture_type" label="涉农贷款类型" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ensure_project_loan" label="保障性安居工程贷款" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.estate_adjust_type" label="产业结构调整类型" hidden="true" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.strategy_new_loan" label="战略新兴产业类型" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.new_prd_loan" label="新兴产业贷款" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.loan_direction" label="贷款投向" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.loan_belong1" label="贷款归属1" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.loan_belong2" label="贷款归属2" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.loan_belong3" label="贷款归属3" hidden="true"/>
			
			<emp:textarea id="IqpLoanApp.IqpLoanAppSub.repay_src_des" label="还款来源"  required="true" colSpan="2"/>
			<emp:textarea id="IqpLoanApp.IqpLoanAppSub.biz_sour" label="业务来源"  required="false" colSpan="2"/>
			<emp:textarea id="IqpLoanApp.IqpLoanAppSub.sour_memo" label="来源说明"  required="false" />	
					
			<emp:date id="IqpLoanApp.end_date" label="办结日期" required="false" hidden="true"/> 				
			<emp:textarea id="IqpLoanApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>					
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.marketing_br_id" label="营销机构" dictname="STD_MARKETING_ORG_CODE" required="false"/>
			<emp:pop id="IqpLoanApp.manager_br_id_displayname" label="管理机构" defvalue="${context.organNo}" required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpLoanApp.in_acct_br_id_displayname" label="入账机构" defvalue="${context.organNo}" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" />
		    <emp:text id="IqpLoanApp.input_id_displayname" label="登记人" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id_displayname" label="登记机构" required="false"  readonly="true"/>
			<emp:date id="IqpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:select id="IqpLoanApp.flow_type" label="流程类型"  required="true" defvalue="01" dictname="STD_ZB_FLOW_TYPE" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true" readonly="true" required="false" defvalue="000"/>
			<emp:text id="IqpLoanApp.manager_br_id" label="管理机构" hidden="true" defvalue="${context.organNo}" />
			<emp:text id="IqpLoanApp.in_acct_br_id" label="入账机构" hidden="true" defvalue="${context.organNo}" />
			<emp:text id="IqpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true"/>
		</emp:gridLayout>
		
		</emp:tab>
		<!-- modified by lisj 2015-10-20 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
		<%
			String flg ="";
		if(context.containsKey("flg")){		     		
			flg = (String)context.getDataValue("flg");
	    }
		String prd_id ="";
		if(context.containsKey("prd_id")){		     		
			prd_id = (String)context.getDataValue("prd_id");
	    }
		if("trust".equals(flg) || "100090".equals(prd_id)){%>
			<emp:tab label="费用信息" id="fee_info_tabs" url="getIqpTrustFeeInfoViewPage.do?serno=${context.IqpLoanApp.serno}"  initial="false" needFlush="true" />
		<% }%>
		<!-- modified by lisj 2015-10-20 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
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
