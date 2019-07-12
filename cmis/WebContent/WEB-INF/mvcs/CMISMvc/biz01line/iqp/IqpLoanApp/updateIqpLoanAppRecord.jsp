<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String currency = "";
	String guarway = "";
	String biz_type = "";
	String is_elec_bill = "";
	String totalAmt = "";
	String flag = "";
	String mes = "";
	if(context.containsKey("currency")){
		currency = (String)context.getDataValue("currency");
	}
	if(context.containsKey("guarway")){
		guarway = (String)context.getDataValue("guarway");
	}
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	} 
	if(context.containsKey("is_elec_bill")){
		is_elec_bill = (String)context.getDataValue("is_elec_bill");
	} 
	if(context.containsKey("totalAmt")){
		totalAmt = (String)context.getDataValue("totalAmt");
	}
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("mes")){
		mes = (String)context.getDataValue("mes");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
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

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="jsIqpComm.jsp" flush="true" /> 
<script type="text/javascript">
function doOnLoad(){
	document.getElementById("base_tab").href="javascript:reLoad();";
	
	IqpLoanApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
	IqpLoanApp.is_rfu._obj.addOneButton("is_rfu","查看",getIsRfuHis);
	IqpLoanApp.cus_total_amt._obj.addOneButton("cus_total_amt","查询",getCusTotalAmt);
	IqpLoanApp.limit_acc_no._obj.addOneButton("limit_acc_no","查看",getLimitAccNo);
	IqpLoanApp.limit_credit_no._obj.addOneButton("limit_credit_no","查看",getLimitCreditNO);
	IqpLoanApp.IqpLoanAppSub.repay_type_displayname._obj.addTheButton("repay_type","生成还款方案",getRepayForm);
	IqpLoanApp.IqpLoanAppSub.serno._setValue(IqpLoanApp.serno._getValue());
    /**add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）begin**/
    IqpLoanApp.belong_net._obj.addOneButton("net","查看",getNet);
    /**add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）end**/
	var guarwayoptions = IqpLoanApp.assure_main._obj.element.options;
	var currencyoptions = IqpLoanApp.apply_cur_type._obj.element.options; 
	var securityoptions = IqpLoanApp.security_cur_type._obj.element.options;

	//根据产品过滤担保，币种
	var currencyList = new Array();
	var securityList = new Array();
	var guarwayList = new Array();
	var currency = '<%=currency%>';	
	var guarway = '<%=guarway%>';	
	currencyList = currency.split(",");
	securityList = currency.split(",");
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
	for(var i=securityoptions.length-1;i>=0;i--){
		var m =0;
		for(var j=0;j<securityList.length;j++){
             if(securityoptions[i].value==securityList[j] || securityoptions[i].value=="" ){
                 m=1; 
             }
		}
		if(m!=1){
			securityoptions.remove(i); 
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
	//加载页面支付方式的判断
	var pay_type = IqpLoanApp.IqpLoanAppSub.conf_pay_type._getValue();
	if(pay_type==1){
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderHidden(false);
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderRequired(true);
	}
	setTermType();//如果为贸易融资三种产品，则期限类型默认为日
	isPay();
	
	getHLByCurr();//--加载汇率--
	getRulMounth();//--加载利率--
	changeRmbAmt();//--加载折算金额--
	isShowNote();//是否承诺函
	isShowCompany();//是否担保公司
	
	show_net();//显示所属网络
	ir_accord_typeChange("init");
	changeIrFloatType();//根据利率浮动方式同比调整显示
	changeOverdueFloatType();
	changeDefaultFloatType();
	controlBizType();//业务模式控制
	hiddenIr();
	hiddenBWIr();
	reality_ir_yChange();//通过年利率计算月利率
	setRepayType();//还款方式策略信息
	setRepayTerm();
	is_limit_cont_pay();//是否额度合同项下支用
	is_person_consume();//是否个人消费贷款，贷款投向字段处理
	isPay();//是否额度合同项下支用
	  
	doInitLimit();

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
	/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造 begin**/
	if(sfgjd == "1" && '${context.belg_line}' != "BL300"){
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);
		//剔除不属于无间贷的借款用途
		var options =IqpLoanApp.IqpLoanAppSub.loan_use_type._obj.element.options;		
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value != "10" && options[i].value != "13" && options[i].value != ""){
					options.remove(i);
			}
		}
	}
	/**add by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造 end**/
    //产品为委托贷款时，贷款性质自动设置为委托贷款
	var prd_id =IqpLoanApp.prd_id._getValue();
	if(prd_id == "100063" || prd_id == "100065"){//贷款性质 （个人委托贷款，企业委托贷款）
		IqpLoanApp.IqpLoanAppSub.loan_nature._setValue("2");
		IqpLoanApp.IqpLoanAppSub.loan_nature._obj._renderReadonly(true);
    }else{
    	var loanNature = IqpLoanApp.IqpLoanAppSub.loan_nature._obj.element.options; 
    	for(var i=loanNature.length-1;i>=0;i--){	
    		if(loanNature[i].value=="2"){
    			loanNature.remove(i);
    		}
    	} 
    }
    if(prd_id == "200024"){//银行承兑汇票
    	IqpLoanApp.apply_amount._obj._renderReadonly(true);
    	var is_elec_bill = '<%=is_elec_bill%>';
    	if(is_elec_bill=='1'){
    		//IqpLoanApp.manager_br_id_displayname._obj._renderReadonly(true);
        }
    }
    getFlg();//判断是否定向资管委托贷款
	loan_nature_change();//贷款性质
    var dbfs = IqpLoanApp.assure_main._getValue();
    if(dbfs!="" &&dbfs.substring(0,2)!='2'){
    	IqpLoanApp.assure_main_details._obj._renderReadonly(true);
    }
    
    getBizType();//通过业务模式判断是否信托贷款
    
	getBelglineByKhm();//通过客户码查询客户所属业务条线
	is_off_busi();//表外业务字段隐藏js
	is_show_pad_rate();//贷款承诺、信贷证明、贷款意向：利率信息中的（垫款利率）不显示
	//ir_typeChange();
	checkstampCollectMode();//印花税收取方式，如果是委托贷款字典放开都可选，否则默认"全由借款人支付"
	var repay_mode_type = IqpLoanApp.IqpLoanAppSub.repay_mode_type._getValue();
	//checkFromRepayType(repay_mode_type);
	checkRepayDate(repay_mode_type);
	getBizLineByCusId();//贷款种类，公司/个人（不同的树形字典项）
	isShow();
	changeStamp();
	showLoanBelong();//贷款归属的显示还是隐藏(2014-04-15需求变更)
	checkIsCloseLoan();//无间贷，申请金额校验
	getCusAmt();
	checkIsDelay();//是否节假日顺延
	cleanPayType();//贸易融资表内业务支付方式
	readOnly4CloseLoan();//无间贷，借新还旧 只读
	//setIrAccordType();//贸易融资贴现品种 利率依据方式为牌告利率
	ifRrAccordType();////反向计算利率浮动比 并隐藏 

	/**add by lisj 2014年11月18日 需求:【XD140818051】 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  begin**/
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
	/**add by lisj 2014年11月18日 需求:【XD140818051】 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  end**/
	/**add by lisj 2014-12-1 需求:XD140925064,生活贷需求开发  begin**/
    checkLifeLoans();
    /**add by lisj 2014-12-1 需求:XD140925064,生活贷需求开发  end**/

  //HS141110017_保理业务改造  add by zhaozq start
    if(prd_id=='800021'){
        //默认到期还本还款方式
    	getRepayTypeForBl('A004','04','N','按期付息到期还本');
    	IqpLoanApp.IqpLoanAppSub.repay_type_displayname._obj._renderReadonly(true);
    	IqpLoanApp.IqpLoanAppSub.interest_term._setValue('2');
    	IqpLoanApp.IqpLoanAppSub.interest_term._obj._renderReadonly(true);
    	setRepayTerm();
    	IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(true);//add by lisj 2015-4-14
    	//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，保理业务默认为信用 start
		IqpLoanApp.assure_main._obj._renderReadonly(true);
		IqpLoanApp.assure_main_details._obj._renderReadonly(true);
		//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，保理业务默认为信用 end    
    }
    //HS141110017_保理业务改造  add by zhaozq end
    /**add by lisj 2015-5-27 需求编号：【XD150123005】小微自助循环贷款改造 begin**/
    /**added by wangj 2015/05/07 需求编号:XD141222087,法人账户透支需求变更  begin**/
    var prd_id = IqpLoanApp.prd_id._getValue();
    if(prd_id=="100051" || prd_id =="100088"){
        IqpLoanApp.IqpLoanAppSub.pay_type._setValue('0');//支付类型
	    IqpLoanApp.IqpLoanAppSub.is_close_loan._setValue('2');//是否无间贷 1:是 2：否
	    IqpLoanApp.IqpLoanAppSub.loan_form._setValue('1');//贷款形式
	    IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);
	    IqpLoanApp.IqpLoanAppSub.is_close_loan._obj._renderReadonly(true);
	    IqpLoanApp.IqpLoanAppSub.loan_form._obj._renderReadonly(true);
	    //is_cloas_loan_change();
	    /**added by wangj 2015/08/19   需求编号:XD150825064_源泉宝法人账户透支改造更  begin**/
	    if(prd_id=="100051"){
	    	IqpLoanApp.IqpLoanAppSub.belg_line._obj._renderHidden(false);
	    	IqpLoanApp.IqpLoanAppSub.belg_line._obj._renderRequired(true);
	    	IqpLoanApp.IqpLoanAppSub.belg_line._obj._renderReadonly(false);
			var options4BelgLine = IqpLoanApp.IqpLoanAppSub.belg_line._obj.element.options;
			//去除申请字典项为"小微条线、所有条线"  BL100 法人透支上线的时候打开
			for(var i=options4BelgLine.length-1;i>=0;i--){
				if(options4BelgLine[i].value== "BL100"||options4BelgLine[i].value== "BL200"||options4BelgLine[i].value== "BL_ALL"){
					options4BelgLine.remove(i);
				}
			}
			
	    }
		/**added by wangj 2015/08/19   需求编号:XD150825064_源泉宝法人账户透支改造 end**/
		/**add by wangj 2015/09/16 需求编号：【XD150123005】小微自助循环贷款改造 begin**/
		if(prd_id=="100088"){
			var options = IqpLoanApp.IqpLoanAppSub.ir_accord_type._obj.element.options;
			for(var i=options.length-1;i>=0;i--){
				if(options[i].value == "02"){
					options.remove(i);
				}
			}
		}
		/**add by wangj 2015/09/16  需求编号：【XD150123005】小微自助循环贷款改造 end**/
    }
    /**added by wangj 2015/05/07 需求编号:XD141222087,法人账户透支需求变更  end**/
    /**add by lisj 2015-5-27 需求编号：【XD150123005】小微自助循环贷款改造 end**/
   	/**added by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造 begin**/
   	var sfgjd = IqpLoanApp.IqpLoanAppSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
   	if(sfgjd=="1"){
   		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderReadonly(true);//
   		IqpLoanApp.IqpLoanAppSub.conf_pay_type._obj._renderReadonly(true);
		IqpLoanApp.IqpLoanAppSub.pay_type._obj._renderHidden(false);
		IqpLoanApp.IqpLoanAppSub.conf_pay_type._setValue("1"); 
		IqpLoanApp.IqpLoanAppSub.pay_type._setValue("0"); 
   		var options =IqpLoanApp.IqpLoanAppSub.loan_use_type._obj.element.options;		
		for(var i = options.length - 1; i >= 0; i--) {
			if(options[i].value != "10" && options[i].value != "13" && options[i].value != ""){
				options.remove(i);
					}
			}
		}
   
    /**added by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造 end**/
    /********XD150702049 五级分类默认为正常类，且不可改*******/
   	var appSts = IqpLoanApp.approve_status._getValue();
   	if(appSts=='000' || appSts=='992' || appSts=='993'){
   		IqpLoanApp.IqpLoanAppSub.five_classfiy._setValue("10");
   		IqpLoanApp.IqpLoanAppSub.five_classfiy._obj._renderReadonly(true);
   	}
   	/***XD150702049 五级分类默认为正常类，且不可改**/
   	/**add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
    if(prd_id =="100090" || ("${context.flg}" == "csgn" && prd_id =="100063") || prd_id =="400020" || prd_id =="400021"){
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

/**add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）begin**/
function getNet(){
    var net = IqpLoanApp.belong_net._getValue();        
    if(net != "" && net != null){
    	var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?net_agr_no='+net+'&op=view&menuId=netmanager';  
    	url = EMPTools.encodeURI(url);
    	var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
    	window.open(url,'newWindow',param);     
    }else{
        alert("请选择所属网络!");  
    }
}; 
/**add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）end**/

//半年日均
function getCusAmt(){
    var flag = '<%=flag%>';
    var mes = '<%=mes%>';
    if(flag == "success"){
    	IqpLoanApp.cus_total_amt._setValue('<%=totalAmt%>');
    }else if(flag == "error"){
       alert(mes);
    }else{
    	IqpLoanApp.cus_total_amt._obj._renderHidden(true);
    }
};
function doSave(data){
	var is_elec_bill = '<%=is_elec_bill%>';
	if(is_elec_bill=='1'){
		var termType = IqpLoanApp.IqpLoanAppSub.term_type._getValue();
		var applyTerm = IqpLoanApp.IqpLoanAppSub.apply_term._getValue();
		if(termType == '001'){//年
			if(parseInt(applyTerm)>1){
				alert("电子银行承兑汇票期限最长不超过1年！");
				return;
			}
		}else if(termType == '002'){//月
			if(parseInt(applyTerm)>12){
				alert("电子银行承兑汇票期限最长不超过1年！");
				return;
			}
		}else if(termType == '003'){//日
			if(parseInt(applyTerm)>365){
				alert("电子银行承兑汇票期限最长不超过1年！");
				return;
			}
		}
    }
	var form = document.getElementById("submitForm");
	if(IqpLoanApp._checkAll() && IqpLoanApp.IqpLoanAppSub._checkAll()){
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
				if(flag == "success"){
					if(data == "save"){
					   alert("保存成功!");
					   var url = '<emp:url action="getIqpLoanAppUpdatePage.do"/>?menuId=${context.menuId}&serno=${context.IqpLoanApp.serno}&op=update&biz_type=${context.biz_type}&flg=${context.flg}';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
					if(data == "subWF"){
					   getApplyTypeByPrdId();
					}
				/**modfified by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin**/
				/**modified by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造  begin**/
				}else if(flag == "sucAlertFee"){
					if(data == "save"){
					 alert("申请信息已发生变更，请及时更新费用信息，避免造成支付金额不一致！（点击费用信息页签即可自动刷新新的支付金额）");
					 alert("保存成功!");
					 var url = '<emp:url action="getIqpLoanAppUpdatePage.do"/>?menuId=${context.menuId}&serno=${context.IqpLoanApp.serno}&op=update&biz_type=${context.biz_type}&flg=${context.flg}';
					 url = EMPTools.encodeURI(url);
					 window.location = url;
					}
					if(data == "subWF"){
						getApplyTypeByPrdId();
					}
				}else if(flag =="termError"){
					alert("申请期限不得超过偿还借据的合同期限，请修改!");
				}else {
					alert("保存失败!");
				}
				/**modified by lisj 2015-5-25 需求编号：XD150413030 关于无间贷业务申请改造  end**/
				/**modfified by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end**/
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
		var url = '<emp:url action="updateIqpLoanAppRecord.do"/>?rd='+Math.random();
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	}else {
		return;
	}
};

function doSub(){
	/**add by lisj 2014-12-1 需求:XD140925064,生活贷需求开发 begin**/
	//保存时校验生活贷业务信息
    var prd_id = IqpLoanApp.prd_id._getValue();
	var term_type = IqpLoanApp.IqpLoanAppSub.term_type._getValue();
	var apply_term = IqpLoanApp.IqpLoanAppSub.apply_term._getValue();
	var apply_amount = IqpLoanApp.apply_amount._getValue();
	if(prd_id =="100080" || prd_id =="100081" || prd_id =="100082" || prd_id =="100083"){
    		//校验生活贷期限
			if(term_type =="001" && parseInt(apply_term) > 3){
				alert("生活贷贷款期限最长不能超过3年，请重新输入！");
				return;
			}else if(term_type =="002" && parseInt(apply_term) > 36){
				alert("生活贷贷款期限最长不能超过36个月，请重新输入！");
				return;
			}
			//校验生活贷款金额
			if(parseInt(apply_amount)< 10000 ||parseInt(apply_amount)> 500000){
				alert("生活贷申请金额范围只能为1-50万元，请重新输入！");
				return;
			}
	}
	/**add by lisj 2014-12-1 需求:XD140925064,生活贷需求开发 end**/
	//如果为委托债权投资,必须占用授信额度
    if('${context.menuId}' == "csgnClaimInvestqueryIqpLoanApp"){
       var limit_ind = IqpLoanApp.limit_ind._getValue();
       if(limit_ind  == "1"){
           alert("委托债权投资,必须占用授信额度");
           return;
       }else{
    	   doSave("save");
       }
    }else{
    	doSave("save");
    }
};
function getBill(data){
 	IqpLoanApp.IqpLoanAppSub.repay_bill._setValue(data.bill_no._getValue());
 	IqpLoanApp.apply_amount._setValue(data.bill_bal._getValue());

 	IqpLoanApp.limit_ind._setValue(data.limit_ind._getValue());
	IqpLoanApp.limit_acc_no._setValue(data.limit_acc_no._getValue());
	IqpLoanApp.limit_credit_no._setValue(data.limit_credit_no._getValue());
 	
 	IqpLoanApp.apply_amount._obj._renderReadonly(true);
 	changeRmbAmt();
 	changeRmbAmt4Security();
 	checkCusHalfAmt();
    
 	IqpLoanApp.limit_ind._obj._renderReadonly(true);
	IqpLoanApp.limit_acc_no._obj._renderReadonly(true);
	IqpLoanApp.limit_credit_no._obj._renderReadonly(true);
};


function reLoad(){
	var url = '<emp:url action="getIqpLoanAppUpdatePage.do"/>?menuId=${context.menuId}&serno=${context.IqpLoanApp.serno}&op=update&biz_type=${context.biz_type}&flg=${context.flg}';
	url = EMPTools.encodeURI(url);
	window.location = url;
	//window.location.reload();
};

function doReturn() {
	var url = '<emp:url action="queryIqpLoanAppList.do"/>?menuId=${context.menuId}'+'&biz_type=${context.biz_type}&flg=${context.flg}';  
	url = EMPTools.encodeURI(url);
	window.location=url;
};

	function checkstampCollectMode(){
		var prd_id = IqpLoanApp.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款 
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("4");
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderReadonly(true);
		}
		if(prd_id == '700020' || prd_id == '700021' || prd_id == '400020' || prd_id == '500032'){//信用证业务,外汇保函，提货担保隐藏
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._setValue("2");
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.is_collect_stamp._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._setValue("");
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderRequired(false);
			IqpLoanApp.IqpLoanAppSub.stamp_collect_mode._obj._renderHidden(true);
		}
	}
	/**add by lisj 2014年11月18日 需求:【XD140818051】 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  begin**/
	function doChangeCollaType(){
		if("${context.belg_line}" == "BL300"){
			var assureMain = IqpLoanApp.assure_main._getValue();
			if(assureMain =='300' || assureMain == '400' || assureMain =='500' || assureMain == '510'){
				IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderHidden(true);
				IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderRequired(false);
			}else{
				IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderHidden(false);
				IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderRequired(true);
				IqpLoanApp.IqpLoanAppSub.collateral_type._setValue("");
				}
		}
	};
	/**add by lisj 2014年11月18日需求:【XD140818051】  增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  end**/
	
	/**add by lisj 2014-12-1 需求:XD140925064,生活贷需求开发  begin**/
    function checkLifeLoans(){
		var prd_id = IqpLoanApp.prd_id._getValue();
		var options = IqpLoanApp.IqpLoanAppSub.term_type._obj.element.options;
		//产品编号为：生活贷A,B,C,D款时，默认担保方式为信用
		if(prd_id =="100080" || prd_id =="100081" || prd_id =="100082" || prd_id =="100083"){
			IqpLoanApp.assure_main._setValue("400");
			IqpLoanApp.assure_main._obj._renderReadonly(true);
			IqpLoanApp.assure_main_details._setValue("9");
			IqpLoanApp.assure_main_details._obj._renderReadonly(true);
			IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderHidden(true);
			IqpLoanApp.IqpLoanAppSub.collateral_type._obj._renderRequired(false);
			IqpLoanApp.apply_cur_type._setValue("CNY");
			IqpLoanApp.security_cur_type._setValue("CNY");
			IqpLoanApp.apply_cur_type._obj._renderReadonly(true);
			IqpLoanApp.security_cur_type._obj._renderReadonly(true);
			//去除申请字典项为"日"
			for(var i=options.length-1;i>=0;i--){
				if(options[i].value == "003"){
					options.remove(i);
				}
			}
		}
    };
  	//默认利率浮动比
    function initLifeLoansRulingIr(){
		var ir_accord_type = IqpLoanApp.IqpLoanAppSub.ir_accord_type._getValue();
		var prd_id = IqpLoanApp.prd_id._getValue();
		if(ir_accord_type =="02" || ir_accord_type =="04"){
			if(prd_id =="100080"){
				IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("0");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("0.700000");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);
			}else if(prd_id =="100081"){
				IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("0");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("0.800000");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);
			}else if(prd_id =="100082"){
				IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("0");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("0.900000");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);
			}else if(prd_id =="100083"){
				IqpLoanApp.IqpLoanAppSub.ir_float_type._setValue("0");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._setValue("1.000000");
				IqpLoanApp.IqpLoanAppSub.ir_float_rate._obj._renderHidden(false);
			}
			/**add by lisj 2015-4-14 需求编号：【HS141110017】保理业务改造 begin**/
			if(prd_id =="800021"){
				IqpLoanApp.IqpLoanAppSub.ir_adjust_type._setValue('0');//利率调整方式为固定不变
				IqpLoanApp.IqpLoanAppSub.ir_adjust_type._obj._renderReadonly(true);
			}
			/**add by lisj 2015-4-14 需求编号：【HS141110017】保理业务改造  end**/
		}
    };
    /**add by lisj 2014-12-1 需求:XD140925064,生活贷需求开发 end**/ 
</script>
</head>
<body class="page_content" onload="doOnLoad()">
  <emp:form id="submitForm" action="updateIqpLoanAppRecord.do?menuId=${context.menuId}&&flg=${context.flg }" method="POST">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="IqpLoanAppGroup" maxColumn="2" title="基本信息" >
			<emp:text id="IqpLoanApp.serno" label="业务流水号" maxlength="40" required="true" readonly="true"/>
			<emp:date id="IqpLoanApp.apply_date" label="申请日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpLoanApp.prd_id" label="产品编号" maxlength="6" required="true" readonly="true"/>
			<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="80" required="true" readonly="true"/>   
			<emp:text id="IqpLoanApp.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpLoanApp.cus_id_displayname" label="客户名称" required="false"  readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="IqpLoanApp.is_rfu" label="是否曾被拒绝" required="true" dictname="STD_ZX_YES_NO" colSpan="1"/>	 
            <emp:select id="IqpLoanApp.is_spe_cus" label="是否特殊客户" required="true" dictname="STD_ZX_YES_NO" />     
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_form" label="贷款形式" onchange="is_cloas_loan_change();" required="true" dictname="STD_LOAN_FORM" defvalue="1" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_nature" label="贷款性质" onchange="loan_nature_change();" required="true" dictname="STD_LOAN_NATYRE" defvalue="1"/>
			<emp:select id="IqpLoanApp.assure_main" label="担保方式" required="true" onchange="assure_mainChange();doChangLimitInt();cleanLimitInt();doChangeCollaType();" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="IqpLoanApp.assure_main_details" label="担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.is_close_loan" label="是否无间贷" hidden="true" required="false" dictname="STD_ZX_YES_NO" onchange="is_cloas_loan_change()"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.repay_bill" label="偿还借据" url="queryAccPop.do?returnMethod=getBill&cus_id=${context.IqpLoanApp.cus_id}"  required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:text id="IqpLoanApp.cus_total_amt" label="客户半年日均" maxlength="40" hidden="false" colSpan="2" required="false" readonly="true" dataType="Currency"/>
			<emp:select id="IqpLoanApp.is_promissory_note" label="是否承诺函下" defvalue="2" required="false" dictname="STD_ZX_YES_NO" onclick="isShowNote()"/> 
			<emp:pop id="IqpLoanApp.promissory_note" label="承诺函" url="queryPromissoryPopList.do?returnMethod=getPromissory&cus_id=${context.IqpLoanApp.cus_id}" required="false" hidden="true" />  
			<emp:select id="IqpLoanApp.IqpLoanAppSub.is_collect_stamp" label="是否收取印花税" required="true" dictname="STD_ZX_YES_NO" readonly="true" defvalue="1" onchange="changeStamp()"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.stamp_collect_mode" label="印花税收取方式" required="true" readonly="false" dictname="STD_ZB_STAMP_MODE" />
			<emp:select id="IqpLoanApp.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" onclick="isShowCompany()"/>
			<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整-->
			<emp:text id="IqpLoanApp.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" />
			<emp:text id="IqpLoanApp.trust_company" label="信托公司" maxlength="100" required="false" hidden="true" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
			<emp:select id="IqpLoanApp.is_structured_fin" label="是否结构化融资" dictname="STD_ZX_YES_NO" required="false" hidden="true" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
			<emp:select id="IqpLoanApp.is_limit_cont_pay" label="是否额度合同项下支用" onchange="isPay();" defvalue="2" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="IqpLoanApp.limit_cont_no" label="额度合同编号" url="queryCtrLimitContListPop.do?cus_id=${context.IqpLoanApp.cus_id}&returnMethod=getContMsg" required="false" hidden="true" />
		    <emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" hidden="true" required="false" readonly="true" onclick="show_net();controlBizType();"/>
			<emp:select id="IqpLoanApp.rent_type" label="租赁模式" dictname="STD_RENT_TYPE" defvalue="0" required="false" />
			<!-- add by lisj 2015-2-5 需求编号【HS141110017】保理业务改造（信贷应用）begin -->
			<emp:text id="IqpLoanApp.belong_net" label="所属网络"  required="false"  readonly="true"/>
			<!-- add by lisj 2014-12-15 需求:【XD140818051】 增加抵质押类型字段-->
			<emp:select id="IqpLoanApp.IqpLoanAppSub.collateral_type" label="抵质押类型" dictname="STD_COLLATERAL_TYPE" required="false" hidden="true" /> 
		
			<!--  /**added by wangj 2015/08/19  需求编号:XD150825064  源泉宝法人账户透支改造  begin**/	-->
			<emp:select id="IqpLoanApp.IqpLoanAppSub.belg_line" label="业务条线" dictname="STD_ZB_BUSILINE" required="false" hidden="true" readonly="true"/>
		  	<!--  /**added by wangj 2015/08/19  需求编号:XD150825064  源泉宝法人账户透支改造  end**/	--> 
		</emp:gridLayout>
		
		<div id="payInfo" >
		<emp:gridLayout id="" maxColumn="2" title="支付信息">
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.conf_pay_type" label="是否确定支付方式" readonly="true" required="true" dictname="STD_ZX_YES_NO" onclick="isShow()"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.pay_type" label="支付方式" required="false" dictname="STD_IQP_PAY_TYPE" hidden="true"/>	    
		</emp:gridLayout>
		</div>
		 
		<emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:select id="IqpLoanApp.apply_cur_type" label="申请币种" required="true" onblur="getHLByCurr()" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
		    <emp:text id="IqpLoanApp.exchange_rate" label="汇率" maxlength="16" readonly="true" required="true"/>
		    <emp:text id="IqpLoanApp.apply_amount" label="申请金额" maxlength="18" required="true" onblur="changeRmbAmt();changeRmbAmt4Security()" dataType="Currency" />
		    <emp:text id="IqpLoanApp.apply_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" />
		   
		    <emp:select id="IqpLoanApp.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security()" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpLoanApp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
		   	<emp:text id="IqpLoanApp.security_rate" label="保证金比例" maxlength="16" readonly="false"  onfocus="_doKeypressDown()" onchange="changeSecRate();changeRmbAmt4SecurityChange()" required="true" dataType="Rate" />
		   	<emp:text id="IqpLoanApp.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true"/>
		    <emp:text id="IqpLoanApp.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" />
		    <emp:text id="IqpLoanApp.ass_sec_multiple" label="担保放大倍数" maxlength="10" defvalue="1" required="false" hidden="true" dataType="Double" />
		    <emp:text id="IqpLoanApp.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" onblur="changeRmbAmt()" dataType="Currency" />
		    <emp:text id="IqpLoanApp.risk_open_amt" label="风险敞口金额（元）" maxlength="18" onchange="riskOpenAmtChange()" readonly="true" required="true" dataType="Currency"/>
		    <emp:text id="IqpLoanApp.risk_open_rate" label="敞口比率" maxlength="10" readonly="true" required="true" dataType="Percent" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="期限信息">
		   <emp:select id="IqpLoanApp.IqpLoanAppSub.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" onblur="cleanDate()"/>
		   <emp:text id="IqpLoanApp.IqpLoanAppSub.apply_term" label="申请期限" required="true" dataType="Int" onchange="getRate();"/>
		   <emp:select id="IqpLoanApp.IqpLoanAppSub.is_delay" label="是否节假日顺延" required="true" dictname="STD_ZX_YES_NO"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
			<emp:select id="IqpLoanApp.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt();doChangLimitInt();" dictname="STD_LIMIT_IND" colSpan="2"/>	   
		    <emp:pop id="IqpLoanApp.limit_acc_no" label="  授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择"/>
		    <emp:pop id="IqpLoanApp.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" buttonLabel="选择"/>
		</emp:gridLayout>
		
		<div id="rateInfo" >
		<emp:gridLayout id="" maxColumn="2" title="利率信息"> 
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.ir_accord_type" label="利率依据方式"  onchange="ir_accord_typeChange('change');initLifeLoansRulingIr();" required="true" dictname="STD_ZB_IR_ACCORD_TYPE" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_type" label="利率种类" hidden="true" required="false" onchange="ir_typeChange();" dictname="STD_ZB_RATE_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ruling_ir" label="基准利率（年）" hidden="true" maxlength="16" readonly="true" required="false" dataType="Rate"/>
			<emp:text id="ruling_mounth" label="对应基准利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="false"/>  
			<emp:text id="IqpLoanApp.IqpLoanAppSub.pad_rate_y" label="垫款利率（年）" hidden="true" maxlength="16" colSpan="2" readonly="false" required="false" dataType="Rate"/>
			  
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_adjust_type" label="利率调整方式" hidden="true" required="false" colSpan="1"  dictname="STD_IR_ADJUST_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ir_next_adjust_term" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" />
			<emp:date id="IqpLoanApp.IqpLoanAppSub.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
			        
			<emp:select id="IqpLoanApp.IqpLoanAppSub.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" onchange="changeIrFloatType();"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ir_float_rate" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" onblur="getRelYM();" required="false" dataType="Rate" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ir_float_point" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getRelYM();" required="false" />

			<emp:text id="IqpLoanApp.IqpLoanAppSub.reality_ir_y" label="执行利率（年）" hidden="true" onchange="reality_ir_yChange();caculateOverdueRate();caculateDefaultRate();" readonly="true" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="reality_mounth" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			<emp:select id="IqpLoanApp.IqpLoanAppSub.overdue_float_type" label="逾期利率浮动方式" hidden="true" readonly="true" onchange="changeOverdueFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.overdue_rate" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" onchange="getOverdueRateY();" required="false" dataType="Rate" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.overdue_point" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" onchange="getOverdueRateY();" required="false" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.overdue_rate_y" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" onchange="caculateOverdueRate();" readonly="true" required="false" dataType="Rate"/>
			
			<emp:select id="IqpLoanApp.IqpLoanAppSub.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" readonly="true" onchange="changeDefaultFloatType();" required="false" dictname="STD_RATE_FLOAT_TYPE" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.default_rate" label="违约利率浮动比" maxlength="16" hidden="true" onchange="getDefaultRateY();" required="false" dataType="Rate" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.default_point" label="违约利率浮动点数" maxlength="38" hidden="true" onchange="getDefaultRateY();" required="false" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.default_rate_y" label="违约利率（年）" hidden="true" maxlength="16" onchange="caculateDefaultRate();" readonly="true" required="false" dataType="Rate"/>
			
			<emp:text id="IqpLoanApp.IqpLoanAppSub.ruling_ir_code" label="基准利率代码" hidden="true" maxlength="40"  required="false"/>
		</emp:gridLayout>
		</div>
		
		<div id="returnType" > 
		<emp:gridLayout id="" maxColumn="2" title="还款方式信息">
		    <emp:pop id="IqpLoanApp.IqpLoanAppSub.repay_type_displayname" label="还款方式" url="queryPrdRepayModeList.do?prd_id=${context.IqpLoanApp.prd_id}&returnMethod=getRepayType" required="true" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.interest_term" label="计息周期" required="true" dictname="STD_IQP_RATE_CYCLE" onblur="setRepayTerm();"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.repay_term" label="还款间隔周期" required="true" dictname="STD_BACK_CYCLE" onchange="cleanSpace()" readonly="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.repay_space" label="还款间隔" maxlength="10" required="true" dataType="Int" onblur="checkTerm()"  readonly="false"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.repay_date" label="还款日" required="true" dataType="Int" /> 
			<emp:select id="IqpLoanApp.IqpLoanAppSub.is_term" label="是否期供" required="true" readonly="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpLoanApp.IqpLoanAppSub.repay_type" label="还款方式" required="false" hidden="true"/>  
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
			</emp:table>  --%>
		</div>
		<emp:gridLayout id="" maxColumn="2" title="其他信息">
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.five_classfiy" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.spe_loan_type" label="特殊贷款类型" required="true" dictname="STD_ZB_LOAN_TYPE_EXT"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.limit_useed_type" label="额度占用来源" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.loan_use_type" label="借款用途" dictname="STD_ZB_USE_TYPE" required="true"/>
			<emp:select id="IqpLoanApp.IqpLoanAppSub.com_up_indtify" label="工业转型升级标识" dictname="STD_ZX_YES_NO" required="true" />
			<emp:select id="IqpLoanApp.IqpLoanAppSub.principal_loan_typ" label="委托贷款种类" required="true" dictname="STD_ZB_COMMISS_TYPE" />
			
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_type_displayname" label="贷款种类" url="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE" returnMethod="loantypeReturn" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2" />
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.agriculture_type_displayname" label="涉农贷款类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_FARME" returnMethod="agricultureReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<%
			    // 个人条线 "将保障性安居工程贷款"变更为"个人经营性贷款归属"  Edited by FCL 20141222
			    String belg_line = "";
		     	if(context.containsKey("belg_line")){
		     		belg_line = (String)context.getDataValue("belg_line");
			    }
		     	if("BL300".equals(belg_line)){%>
		    	    <emp:pop id="IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname" label="个人经营性贷款归属" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
		    <%
		     	}else{
			%>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.ensure_project_loan_displayname" label="保障性安居工程贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<%} %>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.estate_adjust_type_displayname" label="产业结构调整类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_TRD_TYPE" returnMethod="onReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.strategy_new_loan_displayname" label="战略新兴产业类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_ZLXXCYLX" returnMethod="strategyReturn" required="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.new_prd_loan_displayname" label="新兴产业贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_XXCYDK" returnMethod="newPrdReturn" required="false" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_direction_displayname" label="贷款投向" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="loanDirectionReturn" required="false" buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_belong1_displayname" label="贷款归属1" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS1" returnMethod="loanBelong1Return" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_belong2_displayname" label="贷款归属2" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS2" returnMethod="loanBelong2Return" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
			<emp:pop id="IqpLoanApp.IqpLoanAppSub.loan_belong3_displayname" label="贷款归属3" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS3" returnMethod="loanBelong3Return" required="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_input2"/>
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
			
			
			<emp:textarea id="IqpLoanApp.IqpLoanAppSub.repay_src_des" label="还款来源" maxlength="250"  required="true" colSpan="2"/>
			<emp:textarea id="IqpLoanApp.IqpLoanAppSub.biz_sour" label="业务来源" maxlength="250"  required="false" colSpan="2"/>
			<emp:textarea id="IqpLoanApp.IqpLoanAppSub.sour_memo" label="来源说明" maxlength="250"  required="false" />	
					
			<emp:date id="IqpLoanApp.end_date" label="办结日期" required="false" hidden="true"/>					
			<emp:textarea id="IqpLoanApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" hidden="true"/>
			<emp:text id="IqpLoanApp.IqpLoanAppSub.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>					
		</emp:gridLayout>
		
		
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 begin -->
		    <emp:select id="IqpLoanApp.IqpLoanAppSub.marketing_br_id" label="营销机构" dictname="STD_MARKETING_ORG_CODE" required="false"/>
			<emp:pop id="IqpLoanApp.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?yewu=is&restrictUsed=false" returnMethod="getOrgID" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpLoanApp.in_acct_br_id_displayname" label="入账机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" />
		    <emp:text id="IqpLoanApp.input_id_displayname" label="登记人" defvalue="${context.currentUserName}" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id_displayname" label="登记机构" defvalue="${context.organName}" required="false"  readonly="true"/>
			<emp:date id="IqpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:select id="IqpLoanApp.flow_type" label="流程类型"  required="false" hidden="true" defvalue="01" dictname="STD_ZB_FLOW_TYPE" />
			<!-- add by lisj 2015-10-13 需求编号：XD150409029 信贷保函及资产模块改造需求 end -->
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true" readonly="true" required="false" defvalue="000"/>
			<emp:text id="IqpLoanApp.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpLoanApp.in_acct_br_id" label="入账机构" hidden="true" />
			<emp:text id="IqpLoanApp.input_id" label="登记人" defvalue="${context.currentUserId}" hidden="true" maxlength="20" required="false"  readonly="true"/>
			<emp:text id="IqpLoanApp.input_br_id" label="登记机构" defvalue="${context.organNo}" hidden="true" maxlength="20" required="false"  readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="update"/>
			<emp:button id="subWF" label="放入流程" op="update"/>
			<emp:button id="return" label="返回" op="update"/>
		</div>
		</emp:tab>
		<%
		String flg ="";
		if(context.containsKey("flg")){		     		
			flg = (String)context.getDataValue("flg");
	    }
		if("trust".equals(flg)){%>
			<emp:tab label="费用信息" id="fee_info_tabs" url="getIqpTrustFeeInfoAddPage.do?serno=${context.IqpLoanApp.serno}"  initial="false" needFlush="true" />
		<% }%>
		<emp:ExtActTab></emp:ExtActTab>
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
	var biz_type = IqpLoanApp.biz_type._getValue();
	var serno = IqpLoanApp.serno._getValue();
	var cus_id = IqpLoanApp.cus_id._getValue();
	var cus_name = IqpLoanApp.cus_id_displayname._getValue();
	var approve_status = IqpLoanApp.approve_status._getValue();
	var prd_id = IqpLoanApp.prd_id._getValue();
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
	if('${context.menuId}'=="csgnClaimInvestqueryIqpLoanApp"){
		if(prd_id == "100065"){//个人委托贷款
			WfiJoin.appl_type._setValue("0017");
		}else if(prd_id == "100063"){//企业委托贷款
			WfiJoin.appl_type._setValue("0016");
		}
	}else if('${context.menuId}'=="csgnqueryIqpLoanApp"){	
		WfiJoin.appl_type._setValue("0028");
	}else if(biz_type == "0" || biz_type == "2" || biz_type == "1"){
		WfiJoin.appl_type._setValue("0021");//公司/小微业务申请(预付款类融资)  三种业务模式下 先票后货，阶段性担保+货押,保兑仓
	}else if(biz_type == "8"){
		WfiJoin.appl_type._setValue("0022");//公司/小微业务申请(银租通)
	}else{
		WfiJoin.appl_type._setValue(apply_type);
	}
	initWFSubmit(false);
};
//-----------提交流程(目前只做保存和生成合同记录，暂时未加入流程以及修改授信台帐记录)----------
function doSubWF(){ 
	//------执行保存操作------
	var prd_id = IqpLoanApp.prd_id._getValue();

	if(prd_id=='200024'){
		checkAccpBillInfo();
	}else{
		doSave("subWF");
	}
	
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
}

//第三方授信
function getLmtCoopAmt(data){
	var lmtContNo = data[0];//授信协议编号
	var lmtAmt = data[1];//授信余额暂时先去授信金额
	IqpLoanApp.limit_credit_no._setValue(lmtContNo);
	//together_remain_amount._setValue(lmtAmt+"");
}

//放入流程前先校验银票信息是否完整
function checkAccpBillInfo(){
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
				doSave("subWF");
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

	var url="<emp:url action='checkAccpBillInfo.do'/>?serno="+serno;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
}
</script>
</emp:page>
