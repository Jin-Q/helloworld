<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsCont.jsp" flush="true" /> 
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String pvp = "";
	String menuId="";
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
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
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
.emp_field_label{vertical-align:middle;}
.button90{margin:0 3px;}
</style>
<script type="text/javascript">
function doOnLoad(){ 
	CtrLoanCont.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
	CtrLoanCont.limit_acc_no._obj.addOneButton("limit_acc_no","查看",getLimitAccNo);
	CtrLoanCont.limit_credit_no._obj.addOneButton("limit_credit_no","查看",getLimitCreditNO);
	CtrLoanCont.CtrLoanContSub.repay_type_displayname._obj.addTheButton("repay_type","生成还款方案",getRepayForm);
	//加载页面支付方式的判断
	var pay_type = CtrLoanCont.CtrLoanContSub.is_conf_pay_type._getValue();
	if(pay_type==1){
		CtrLoanCont.CtrLoanContSub.pay_type._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.pay_type._obj._renderRequired(true);
	}
	getFlg();//判断是否定向资管委托贷款
	loan_nature_change();//贷款性质
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
	reality_ir_yChange();//年利率转换月利率
	setRepayType(); 
	setRepayTerm();
	is_person_consume();//是否个人消费贷款，贷款投向字段处理 
	is_limit_cont_pay();//是否额度合同项下支用
	isPay();//是否额度合同项下支用
	checkIsDelay();
    /***********************************************
    ** 2014-07-03 Edited by FCL 增加公司条线无间贷业务
    ************************************************/
	if('${context.belg_line}' == "BL100" ||'${context.belg_line}' == "BL200" || "${context.belg_line}" == "BL300"){
		CtrLoanCont.CtrLoanContSub.is_close_loan._obj._renderHidden(true);
	}else{
		CtrLoanCont.CtrLoanContSub.is_close_loan._obj._renderHidden(true);
		CtrLoanCont.CtrLoanContSub.is_close_loan._obj._renderRequired(false);
	}
	var sfgjd = CtrLoanCont.CtrLoanContSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
	var dkxs = CtrLoanCont.CtrLoanContSub.loan_form._getValue();//贷款形式
	if(sfgjd == "1" || dkxs == "3"){
		CtrLoanCont.CtrLoanContSub.repay_bill._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.is_close_loan._obj._renderHidden(true);   
	}else{
		CtrLoanCont.CtrLoanContSub.repay_bill._obj._renderHidden(true);
	}
    //产品为委托贷款时，贷款性质自动设置为委托贷款
	var prd_id =CtrLoanCont.prd_id._getValue();
	if(prd_id == "100063"){//贷款性质
		CtrLoanCont.CtrLoanContSub.loan_nature._setValue("2");
		CtrLoanCont.CtrLoanContSub.loan_nature._obj._renderReadonly(true);
    }else{
    	var loanNature = CtrLoanCont.CtrLoanContSub.loan_nature._obj.element.options; 
    	for(var i=loanNature.length-1;i>=0;i--){	
    		if(loanNature[i].value=="2"){ 
    			loanNature.remove(i);
    		}
    	} 
    }
    var dbfs = CtrLoanCont.assure_main._getValue();
    if(dbfs!="" &&dbfs.substring(0,2)!='2'){
    	CtrLoanCont.assure_main_details._obj._renderReadonly(true);
    }
    is_off_busi();//表外业务字段隐藏js
    is_show_pad_rate();//贷款承诺、信贷证明、贷款意向：利率信息中的（垫款利率）不显示

    setContNumber();
    checkstampCollectMode();//印花税
    var repay_mode_type = CtrLoanCont.CtrLoanContSub.repay_mode_type._getValue();
	//checkFromRepayType(repay_mode_type);
	checkRepayDate(repay_mode_type);
	getBizLineByCusId();
	/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 start **/
	//getHLByCurr4Security(); 
	/** added by yangzy 2015/07/09 需求：XD150407026，查看页面不需要实时获取汇率 end **/
	changeStamp();
	showLoanBelong();//贷款归属的显示还是隐藏(2014-04-15需求变更)
	ifRrAccordType();////反向计算利率浮动比 并隐藏 
	
	/**add by lisj 2014年11月18日 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  begin**/
	if("${context.belg_line}" == "BL300"){
		var assureMain = CtrLoanCont.assure_main._getValue();
		if(assureMain =='300' || assureMain == '400'){
			CtrLoanCont.CtrLoanContSub.collateral_type._obj._renderHidden(true);
			CtrLoanCont.CtrLoanContSub.collateral_type._obj._renderRequired(false);
		}else{
		CtrLoanCont.CtrLoanContSub.collateral_type._obj._renderHidden(false);
		CtrLoanCont.CtrLoanContSub.collateral_type._obj._renderRequired(true);
		}
	}
	/**add by lisj 2014年11月18日 增加抵质押类型字段，当业务申请为个人业务时，字段为必输可选  end**/
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
        	var url = '<emp:url action="queryCtrLoanContList.do"/>?biz_type='+${context.CtrLoanCont.biz_type}+'&menuId='+'<%=menuId %>'; 
        }else{
        	var url = '<emp:url action="queryCtrLoanContHistoryList.do"/>?biz_type='+${context.CtrLoanCont.biz_type}+'&menuId='+'<%=menuId %>'; 
        }
		url = EMPTools.encodeURI(url); 
		window.location=url; 
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function doReturn1() {
		window.close();
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	function setContNumber(){
		var prd_id =CtrLoanCont.prd_id._getValue();
		if(prd_id=='200024'||prd_id=='500032'||prd_id=='400020'||prd_id=='400024'||prd_id=='400023'||prd_id=='400022'||
				prd_id=='700021'||prd_id=='700020'||prd_id=='400021'||prd_id=='100063'||prd_id=='300020'||prd_id=='300021'||
				prd_id=='300023'||prd_id=='600020'||prd_id=='300024'||prd_id=='300022'){
			CtrLoanCont.cont_number._obj._renderHidden(true);
		}
		/**delete by lisj 2014年11月24日 无间贷有评分，需放开校验  begin **/ 
		/**	else{
			var sfgjd = CtrLoanCont.CtrLoanContSub.is_close_loan._getValue();//是否无间贷 1:是 2：否
			if(sfgjd=='1'){
				CtrLoanCont.cont_number._obj._renderHidden(true);
			}else{
				CtrLoanCont.cont_number._obj._renderHidden(false);
			}
		}**/
		/**delete by lisj 2014年11月24日 无间贷有评分，需放开校验  end **/ 
	}
	/*--user code begin--*/
	function checkstampCollectMode(){
		var prd_id = CtrLoanCont.prd_id._getValue();//产品编号
		if(prd_id != '100063' && prd_id != '100065'){//如果不是企业委托贷款并且不是个人委托贷款
			CtrLoanCont.CtrLoanContSub.stamp_collect_mode._setValue("4");
			CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderReadonly(true);
		}
		if(prd_id == '700020' || prd_id == '700021' || prd_id == '400020' || prd_id == '500032'){//信用证业务,外汇保函，提货担保隐藏
			CtrLoanCont.CtrLoanContSub.is_collect_stamp._setValue("2");
			CtrLoanCont.CtrLoanContSub.is_collect_stamp._obj._renderRequired(false);
			CtrLoanCont.CtrLoanContSub.is_collect_stamp._obj._renderHidden(true);
			CtrLoanCont.CtrLoanContSub.stamp_collect_mode._setValue("");
			CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderRequired(false);
			CtrLoanCont.CtrLoanContSub.stamp_collect_mode._obj._renderHidden(true);
		}
	}		

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrLoanCont.serno._getValue();	//业务编号
		data['cus_id'] = CtrLoanCont.cus_id._getValue();	//客户码
		data['prd_id'] = CtrLoanCont.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	
	function doAdd(){
		var form = document.getElementById("submitForm");
		var result = CtrLoanCont._checkAll();
		
		//if(result){
			CtrLoanCont._toForm(form)
			toSubmitForm(form);

		//}//else alert("请输入必填项！");
	}
	function toSubmitForm(form){
		//if(!CtrLoanCont._checkAll()){
		    //   return;
			//}
			//if(!CtrLoanCont.CtrLoanContSub._checkAll()){
		    //   return;
			//}
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="修改成功"){
					alert("修改成功!");
					doReturn();
			     }else {
				   alert(flag);
				   return;
			     }
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad()" >
	
  <emp:tabGroup mainTab="base_tab" id="mainTab" >
  
	 <emp:tab label="合同信息" id="base_tab" needFlush="true" initial="true" >
	 <emp:form id="submitForm" action="updateCtrLoanContRecord1.do" method="POST">
          <emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="合同基本信息">
			<emp:text id="CtrLoanCont.serno" label="业务编号" maxlength="40" required="true" readonly="false"   />
			<emp:date id="CtrLoanCont.ser_date" label="签订日期"  required="true" readonly="true" />
			<emp:text id="CtrLoanCont.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"  />
			<emp:text id="CtrLoanCont.cn_cont_no" label="中文合同编号" maxlength="100" required="true" />
			<emp:text id="CtrLoanCont.prd_id" label="产品编号" maxlength="6" required="true" readonly="true"/>
			<emp:text id="CtrLoanCont.prd_id_displayname" label="产品名称"  required="true" readonly="true"/>
			<emp:text id="CtrLoanCont.cus_id" label="客户码" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="CtrLoanCont.cus_id_displayname" label="客户名称"  required="false" cssElementClass="emp_field_text_long_readonly" readonly="true" colSpan="2"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.loan_form" label="贷款形式" required="true" dictname="STD_LOAN_FORM" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.loan_nature" label="贷款性质" required="true" dictname="STD_LOAN_NATYRE" readonly="true"/>
			<emp:select id="CtrLoanCont.assure_main" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" readonly="true"/>
			<emp:select id="CtrLoanCont.assure_main_details" label="担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.is_close_loan" label="是否无间贷" hidden="true" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.repay_bill" label="偿还借据" url="queryAccPop.do?returnMethod=getBill&cus_id=${context.CtrLoanCont.cus_id}"  required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="CtrLoanCont.is_promissory_note" label="是否承诺函下" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>			
			<emp:text id="CtrLoanCont.promissory_note" label="承诺函" maxlength="80" required="false"  hidden="true" readonly="true" />  
			<emp:select id="CtrLoanCont.CtrLoanContSub.is_collect_stamp" label="是否收取印花税" required="true" dictname="STD_ZX_YES_NO" readonly="true" />
			<emp:select id="CtrLoanCont.CtrLoanContSub.stamp_collect_mode" label="印花税收取方式" required="true" readonly="true" dictname="STD_ZB_STAMP_MODE"/>
			<emp:select id="CtrLoanCont.is_trust_loan" label="是否信托贷款" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>						
			<!-- add by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整  -->
			<emp:text id="CtrLoanCont.trust_pro_name" label="信托项目名称" maxlength="100" required="false" hidden="true" readonly="true" />
			<emp:text id="CtrLoanCont.trust_company" label="信托公司" maxlength="100" required="false" hidden="true" readonly="true" />	
			<emp:select id="CtrLoanCont.is_limit_cont_pay" label="是否额度合同项下支用" onchange="isPay();" defvalue="2" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:pop id="CtrLoanCont.limit_cont_no" label="额度合同编号" url="queryCtrLimitContListPop.do?returnMethod=getContMsg" required="false" hidden="true" readonly="true"/>
		    <emp:select id="CtrLoanCont.biz_type" label="业务模式" hidden="true" required="false" dictname="STD_BIZ_TYPE" />
			<emp:select id="CtrLoanCont.rent_type" label="租赁模式" required="false" dictname="STD_RENT_TYPE" readonly="true"/>
			<emp:pop id="CtrLoanCont.belong_net" label="所属网络" url="" returnMethod="" required="false" readonly="true" />
			<emp:select id="CtrLoanCont.CtrLoanContSub.collateral_type" label="抵质押类型" dictname="STD_COLLATERAL_TYPE" required="false" hidden="true" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.actp_status" label="税票提供状态" dictname="STD_ACTP_STATUS" required="true" readonly="false" defvalue="1"/>
		  </emp:gridLayout>
		  </emp:form>
		  
		  <div id="payInfo" >
          <emp:gridLayout id="" maxColumn="2" title="支付信息">	    		
		    <emp:select id="CtrLoanCont.CtrLoanContSub.is_conf_pay_type" label="是否确定支付方式" required="true" dictname="STD_ZX_YES_NO" onclick="isShow()" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.pay_type" label="支付方式" required="false" dictname="STD_IQP_PAY_TYPE" hidden="true" readonly="true"/>
		  </emp:gridLayout>
		  </div>
		  
         <emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:select id="CtrLoanCont.cont_cur_type" label="合同币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		    <emp:text id="CtrLoanCont.exchange_rate" label="汇率" maxlength="10" required="true" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="CtrLoanCont.cont_amt" label="合同金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="CtrLoanCont.apply_rmb_amount" label="折合成人民币金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    
		     <emp:select id="CtrLoanCont.security_cur_type" label="保证金币种" defvalue="CNY" onchange="getHLByCurr4Security();" readonly="true" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="CtrLoanCont.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="true" />
		   	<emp:text id="CtrLoanCont.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	<emp:text id="CtrLoanCont.security_amt" label="保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	
		    <emp:text id="CtrLoanCont.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrLoanCont.ass_sec_multiple" label="担保放大倍数" hidden="true" maxlength="10" required="false" dataType="Double" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrLoanCont.same_security_amt" label="视同保证金" maxlength="18" defvalue="0" hidden="true" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrLoanCont.risk_open_amt" label="风险敞口金额（元）" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="CtrLoanCont.risk_open_rate" label="敞口比率" maxlength="10" required="true" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly"/>  
		    <emp:text id="CtrLoanCont.cont_balance" label="合同余额" maxlength="18" required="false" dataType="Currency" readonly="true"  hidden="true"/>			
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="期限信息">
		    <emp:date id="CtrLoanCont.cont_start_date" label="合同起始日期" required="true"  readonly="true"/>
		    <emp:date id="CtrLoanCont.cont_end_date" label="合同到期日期" required="true"  readonly="true"/>					
		    <emp:text id="CtrLoanCont.CtrLoanContSub.cont_term" label="合同期限" required="true"  readonly="true"/>					
		    <emp:select id="CtrLoanCont.CtrLoanContSub.term_type" label="期限类型" required="true"  readonly="true" dictname="STD_ZB_TERM_TYPE" />	
		    <emp:select id="CtrLoanCont.CtrLoanContSub.is_delay" label="是否节假日顺延" required="true"  readonly="true" dictname="STD_ZX_YES_NO"/>					
		    <emp:date id="CtrLoanCont.cancel_date" label="合同注销日期" required="false"  readonly="true" hidden="true"/>					
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
		    <emp:select id="CtrLoanCont.limit_ind" label="授信额度使用标志" required="true" dictname="STD_LIMIT_IND" colSpan="2" readonly="true" />
		    <emp:text id="CtrLoanCont.limit_acc_no" label="授信台账编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="CtrLoanCont.limit_credit_no" label="第三方授信编号" maxlength="40" required="false" readonly="true"/>
		</emp:gridLayout> 
		
		<div id="rateInfo" >
		<emp:gridLayout id="" maxColumn="2" title="利率信息">    
		    <emp:select id="CtrLoanCont.CtrLoanContSub.ir_accord_type" label="利率依据方式"  required="true" dictname="STD_ZB_IR_ACCORD_TYPE" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.ir_type" label="利率种类"  required="false" dictname="STD_ZB_RATE_TYPE"  readonly="true" />
		    <emp:text id="CtrLoanCont.CtrLoanContSub.ruling_ir" cssElementClass="emp_currency_text_readonly" label="基准利率（年）" maxlength="16" required="false" dataType="Rate" readonly="true"/>  
			<emp:text id="ruling_mounth" label="对应基准利率(月)" cssElementClass="emp_currency_text_readonly" maxlength="16" required="false" dataType="Rate" readonly="true"/>		
			<emp:text id="CtrLoanCont.CtrLoanContSub.pad_rate_y" cssElementClass="emp_currency_text_readonly" label="垫款利率（年）" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.ir_adjust_type" label="利率调整方式"  required="false" dictname="STD_IR_ADJUST_TYPE" readonly="true"/>
			
			<emp:text id="CtrLoanCont.CtrLoanContSub.ir_next_adjust_term" cssElementClass="emp_currency_text_readonly" label="下一次利率调整间隔" hidden="true" required="false" dataType="Int" readonly="true" />
			<emp:select id="CtrLoanCont.CtrLoanContSub.ir_next_adjust_unit" label="下一次利率调整单位" hidden="true" required="false" dictname="STD_BACK_CYCLE" readonly="true"/>
			<emp:date id="CtrLoanCont.CtrLoanContSub.fir_adjust_day" label="第一次调整日" hidden="true" required="false" />
			
			<emp:select id="CtrLoanCont.CtrLoanContSub.ir_float_type" label="利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.ir_float_rate" cssElementClass="emp_currency_text_readonly" label="利率浮动比" hidden="true" colSpan="2" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.ir_float_point" cssElementClass="emp_currency_text_readonly" label="贷款利率浮动点数" hidden="true" colSpan="2" maxlength="38" required="false" readonly="true"/>
			
			<emp:text id="CtrLoanCont.CtrLoanContSub.reality_ir_y" cssElementClass="emp_currency_text_readonly" label="执行利率（年）" hidden="true" readonly="true" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="reality_mounth" cssElementClass="emp_currency_text_readonly" label="执行利率(月)" maxlength="16" hidden="true" required="false" dataType="Rate4Month" readonly="true"/>	
			<emp:select id="CtrLoanCont.CtrLoanContSub.overdue_float_type" label="逾期利率浮动方式" hidden="true" required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.overdue_rate" cssElementClass="emp_currency_text_readonly" label="逾期利率浮动比" hidden="true" colSpan="2" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.overdue_point" cssElementClass="emp_currency_text_readonly" label="逾期利率浮动点数" hidden="true" colSpan="2" maxlength="38" required="false" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.overdue_rate_y" cssElementClass="emp_currency_text_readonly" label="逾期利率（年）" hidden="true" colSpan="2" maxlength="16" readonly="true" required="false" dataType="Rate"/>
			
			<emp:select id="CtrLoanCont.CtrLoanContSub.default_float_type" label="违约利率浮动方式" hidden="true" colSpan="2" required="false" dictname="STD_RATE_FLOAT_TYPE" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.default_rate" cssElementClass="emp_currency_text_readonly" label="违约利率浮动比" maxlength="16" hidden="true" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.default_point" cssElementClass="emp_currency_text_readonly" label="违约利率浮动点数" maxlength="38" hidden="true" required="false" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.default_rate_y" cssElementClass="emp_currency_text_readonly" label="违约利率（年）" hidden="true" maxlength="16" required="false" dataType="Rate" readonly="true"/>
		    <emp:text id="CtrLoanCont.CtrLoanContSub.ruling_ir_code" cssElementClass="emp_currency_text_readonly" label="基准利率代码" hidden="true" maxlength="40"  required="false"/>
		</emp:gridLayout>
		</div>
		
		<div id="returnType" >
        <emp:gridLayout id="" maxColumn="2" title="还款方式信息">    
			            <emp:pop id="CtrLoanCont.CtrLoanContSub.repay_type_displayname" label="还款方式" url="queryPrdRepayModeList.do?returnMethod=getRepayType" required="true" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.interest_term" label="计息周期" required="true" dictname="STD_IQP_RATE_CYCLE" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.repay_term" label="还款间隔周期" required="true" dictname="STD_BACK_CYCLE" onchange="cleanSpace()" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.repay_space" label="还款间隔" maxlength="10" required="true" dataType="Int" readonly="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.repay_date" label="还款日" required="true" readonly="true"/> 
			<emp:select id="CtrLoanCont.CtrLoanContSub.is_term" label="是否期供" required="true" readonly="true" dictname="STD_ZX_YES_NO" />
		    <emp:text id="CtrLoanCont.CtrLoanContSub.repay_type" label="还款方式" required="false" hidden="true"/> 
		    <emp:date id="CtrLoanCont.CtrLoanContSub.fir_repay_date" label="首次还款日" required="false" readonly="false" hidden="true"/> 
		    <emp:text id="CtrLoanCont.CtrLoanContSub.repay_mode_type" label="还款方式类型" required="false" hidden="true"/>
		</emp:gridLayout>
		   
		<%-- <div class='emp_gridlayout_title'>还款方式策略信息</div>
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
		    <emp:select id="CtrLoanCont.CtrLoanContSub.five_classfiy" label="五级分类" required="true" dictname="STD_ZB_FIVE_SORT" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.spe_loan_type" label="特殊贷款类型" required="true" dictname="STD_ZB_LOAN_TYPE_EXT" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.limit_useed_type" label="额度占用来源" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.loan_use_type" label="借款用途" dictname="STD_ZB_USE_TYPE" required="true" readonly="true"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.com_up_indtify" label="工业转型升级标识" dictname="STD_ZX_YES_NO" required="false" readonly="true"/>
            <emp:select id="CtrLoanCont.CtrLoanContSub.principal_loan_typ" label="委托贷款种类" required="true" dictname="STD_ZB_COMMISS_TYPE" readonly="true"/>
            
			<emp:pop id="CtrLoanCont.CtrLoanContSub.loan_type_displayname" label="贷款种类" url="showDicTree.do?dicTreeTypeId=STD_COM_POSITIONTYPE" returnMethod="loantypeReturn" required="true" readonly="true"  buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly" />
            <emp:pop id="CtrLoanCont.CtrLoanContSub.agriculture_type_displayname" label="涉农贷款类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_FARME" returnMethod="agricultureReturn" required="true" readonly="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.ensure_project_loan_displayname" label="保障性安居工程贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS5" returnMethod="projectReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
            <emp:pop id="CtrLoanCont.CtrLoanContSub.estate_adjust_type_displayname" label="产业结构调整类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_TRD_TYPE" returnMethod="onReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_field_text_long_readonly"/> 
			<emp:pop id="CtrLoanCont.CtrLoanContSub.strategy_new_type_displayname" label="战略新兴产业类型" url="showDicTree.do?dicTreeTypeId=STD_ZB_ZLXXCYLX" returnMethod="strategyReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.new_prd_loan_displayname" label="新兴产业贷款" url="showDicTree.do?dicTreeTypeId=STD_ZB_XXCYDK" returnMethod="newPrdReturn" required="false" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.loan_direction_displayname" label="贷款投向" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="loanDirectionReturn" required="true" colSpan="2" readonly="true" buttonLabel="选择" cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.loan_belong1_displayname" label="贷款归属1" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS1" returnMethod="loanBelong1Return" required="true" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.loan_belong2_displayname" label="贷款归属2" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS2" returnMethod="loanBelong2Return" required="true" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:pop id="CtrLoanCont.CtrLoanContSub.loan_belong3_displayname" label="贷款归属3" url="showDicTree.do?dicTreeTypeId=STD_ZB_DKGS3" returnMethod="loanBelong3Return" required="true" readonly="true" buttonLabel="选择" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="CtrLoanCont.CtrLoanContSub.loan_belong4" label="贷款归属4" required="true" dictname="STD_ZB_DKGS4" />
			
			<emp:text id="CtrLoanCont.CtrLoanContSub.loan_type" label="贷款种类" hidden="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.agriculture_type" label="涉农贷款类型" hidden="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.ensure_project_loan" label="保障性安居工程贷款" hidden="true"/>
			<emp:text id="CtrLoanCont.CtrLoanContSub.estate_adjust_type" label="产业结构调整类型" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrLoanContSub.strategy_new_type" label="战略新兴产业类型" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrLoanContSub.new_prd_loan" label="新兴产业贷款" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrLoanContSub.loan_direction" label="贷款投向" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrLoanContSub.loan_belong1" label="贷款归属1" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrLoanContSub.loan_belong2" label="贷款归属2" hidden="true"/> 
			<emp:text id="CtrLoanCont.CtrLoanContSub.loan_belong3" label="贷款归属3" hidden="true"/> 
			
			<emp:textarea id="CtrLoanCont.CtrLoanContSub.repay_src_des" label="还款来源"  required="true" readonly="true"/>
			<emp:text id="CtrLoanCont.prd_details" label="产品细分" maxlength="10" required="false" hidden="true"/>			
			<emp:textarea id="CtrLoanCont.remarks" label="备注" maxlength="250" required="false" colSpan="2" hidden="true"/>		
		  </emp:gridLayout>

		  <emp:gridLayout id="" maxColumn="3" title="登记信息">   
		    <emp:text id="CtrLoanCont.manager_br_id_displayname" label="管理机构"  required="true" readonly="true"/>
		    <emp:text id="CtrLoanCont.in_acct_br_id_displayname" label="入账机构" required="false" hidden="true"/>
		    <emp:text id="CtrLoanCont.cont_number" label="评估分数" maxlength="38" required="false" readonly="true"/>  
			
			<emp:text id="CtrLoanCont.input_id_displayname" label="登记人"  required="false"  readonly="true"/>
		    <emp:text id="CtrLoanCont.input_br_id_displayname" label="登记机构"  required="false"  readonly="true"/>
		    <emp:date id="CtrLoanCont.input_date" label="登记日期" required="false" readonly="true"/>
		    <emp:text id="CtrLoanCont.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:text id="CtrLoanCont.in_acct_br_id" label="入账机构" hidden="true" required="true"/>
		    <emp:text id="CtrLoanCont.input_id" label="登记人" maxlength="20" hidden="true" required="false"  readonly="true"/>
		    <emp:text id="CtrLoanCont.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false"  readonly="true"/>
		    <emp:select id="CtrLoanCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" required="false" hidden="true"/> 
		</emp:gridLayout>
		</emp:tab>
	<emp:ExtActTab></emp:ExtActTab> 
	</emp:tabGroup>

	<div align="center">
		<br>
		<%if(!"pvp".equals(pvp)&&!"out".equals(viewtype)){ %>
		    <emp:button id="return" label="返回到列表页面"/>
		<%}%>
		<%if(!"pvp".equals(pvp)&&"out".equals(viewtype)){ %>
		    <emp:button id="return1" label="关闭"/>
		<%}%>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		<emp:button id="Add" label="保存"/>
	</div>
	
</body>
</html>
</emp:page>
