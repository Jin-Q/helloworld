<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno = "";
	String prd_id = "";
	String apply_cur_type = "";
	String apply_amount = "";
	String canFeeCode = "";
	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	String modiflg ="";
	String modify_rel_serno ="";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	if(context.containsKey("apply_cur_type")){
		apply_cur_type = (String)context.getDataValue("apply_cur_type");
	}
	if(context.containsKey("apply_amount")){
		apply_amount = (String)context.getDataValue("apply_amount");
	}
	if(context.containsKey("canFeeCode")){
		canFeeCode = (String)context.getDataValue("canFeeCode");
	}
	if(context.containsKey("prd_id")){
		prd_id = (String)context.getDataValue("prd_id");
	}
	if(context.containsKey("modiflg")){
		modiflg = (String)context.getDataValue("modiflg");
	}
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_select_select1 {
	width: 665px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function onload(){
		var fee_code_options = IqpAppendTerms.fee_code._obj.element.options;
		//根据产品过滤费用代码
		var feeCodeList = new Array();
		var canFeeCode = '<%=canFeeCode%>';	
		feeCodeList = canFeeCode.split(",");
		for(var i=fee_code_options.length-1;i>=0;i--){
			var m =0;
			for(var j=0;j<feeCodeList.length;j++){
	             if(fee_code_options[i].value==feeCodeList[j] || fee_code_options[i].value=="" ){
	                 m=1; 
	             }
			}
			if(m!=1){ 
				fee_code_options.remove(i); 
	        } 	
		}
		IqpAppendTerms.fee_cur_type._setValue('${context.apply_cur_type}');

		var prd_id = '${context.prd_id}';
    	if(prd_id == "200024"){
    		IqpAppendTerms.collect_type._setValue("02");
    		checkCollectType();
    		caculateAmtByRate();
        }
        /**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  begin**/
    	if(prd_id == "100051"){
    		IqpAppendTerms.is_cycle_chrg._setValue("2");
    		IqpAppendTerms.is_cycle_chrg._obj._renderReadonly(true);
    		checkIsCycleChrg();
        }
        /**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  end**/
	};
//-----------------计算费用ByRate---------------------
	function caculateAmtByRate(){
		var fee_rate_get = IqpAppendTerms.fee_rate._obj.element.value;
		var fee_code = IqpAppendTerms.fee_code._getValue();
		var fee_rate = parseFloat(fee_rate_get);
		var apply_amount = parseFloat('${context.apply_amount}');
		var chrg_rate = parseFloat('${context.chrg_rate}');
		if(chrg_rate == null || chrg_rate == ""){
			chrg_rate = 0;
		}
        if(fee_rate_get != null && fee_rate_get != "" && apply_amount != null && apply_amount != "" ){
        	var amt;
        	if(fee_code == "01"){
        		amt = (fee_rate/100*apply_amount*chrg_rate).toFixed(2);
            }else if(fee_code == "09"){
            	amt = (fee_rate/100*apply_amount*chrg_rate).toFixed(2);
            }else{
            	amt = (fee_rate/100*apply_amount).toFixed(2);
            }
            
        	IqpAppendTerms.fee_amt._setValue(amt+'');
        }
        /**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  begin**/
		var prd_id = '${context.prd_id}';
		if(prd_id=="100051"){
			if(fee_rate>0.3||fee_rate<0.1){
				alert("透支承诺费按照0.1%-0.3%予以收取");
				IqpAppendTerms.fee_rate._setValue("");
				IqpAppendTerms.fee_amt._setValue("");
				IqpAppendTerms.fee_rate._obj.element.focus();
				return ;
			}else{
				checkAmt();
			}
		}
       /**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  end**/
	};
//-----------------计算费用---------------------
	function caculateAmt(){
		var fee_amt_get = IqpAppendTerms.fee_amt._getValue();
		var fee_amt = parseFloat(fee_amt_get);
		var apply_amount = parseFloat('${context.apply_amount}');
        if(fee_amt_get != null && fee_amt_get != "" && apply_amount != null && apply_amount != "" ){
            var rate = (fee_amt/apply_amount).toFixed(6);
            IqpAppendTerms.fee_rate._setValue(rate+'');
        }
	};
//-----------------控制是否周期---------------------
	function checkIsCycleChrg(){
		var is_cycle_chrg = IqpAppendTerms.is_cycle_chrg._getValue();
        if(is_cycle_chrg == '1'){
        	IqpAppendTerms.chrg_date._obj._renderHidden(false);
        	IqpAppendTerms.chrg_date._obj._renderRequired(true);
        	
        	IqpAppendTerms.chrg_freq._obj._renderHidden(false);
        	IqpAppendTerms.chrg_freq._obj._renderRequired(true);
        }else{
        	IqpAppendTerms.chrg_date._obj._renderHidden(true);
        	IqpAppendTerms.chrg_date._obj._renderRequired(false);
        	IqpAppendTerms.chrg_date._setValue("");
        	
        	IqpAppendTerms.chrg_freq._obj._renderHidden(true);
        	IqpAppendTerms.chrg_freq._obj._renderRequired(false);
        	IqpAppendTerms.chrg_freq._setValue("");
        }
	};
//-----------------提交---------------------
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAppendTerms._checkAll()){
			IqpAppendTerms._toForm(form); 
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
						alert("新增成功!");
						/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
						var modiflg = '<%=modiflg%>';
						var url ="";
						if(modiflg =="yes"){
							url = '<emp:url action="queryIqpAppendTermsList.do"/>?menuIdTab=queryIqpLoanApp&subMenuId=queryIqpAppendTermsList&apply_cur_type=${context.apply_cur_type}&apply_amount=${context.apply_amount}&prd_id=${context.prd_id}&op=update&serno='+'<%=serno%>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'; 
						}else{
							url = '<emp:url action="queryIqpAppendTermsList.do"/>?apply_cur_type=${context.apply_cur_type}&apply_amount=${context.apply_amount}&prd_id=${context.prd_id}&op=update&serno='+'<%=serno%>';
						}
						/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/ 
						url = EMPTools.encodeURI(url);
						window.location = url;
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};

	 //-------------是否我行账户效验，是则需要校验，不是则无需校验------------
    function isThisOrg(){
		var isThis = IqpAppendTerms.is_this_org_acct._getValue();
		if(isThis == "1"){
			IqpAppendTerms.acct_no._setValue("");
			IqpAppendTerms.acct_name._setValue("");
			IqpAppendTerms.acct_name._obj._renderReadonly(true);
			IqpAppendTerms.opac_org_no._setValue("");
			IqpAppendTerms.opan_org_name._setValue("");
			IqpAppendTerms.opan_org_name._obj._renderReadonly(true);
			IqpAppendTerms.opac_org_no._obj._renderReadonly(true);
		}else {
			IqpAppendTerms.acct_no._setValue("");
			IqpAppendTerms.acct_name._setValue("");
			IqpAppendTerms.acct_name._obj._renderReadonly(false);
			IqpAppendTerms.opac_org_no._setValue("");
			IqpAppendTerms.cur_type._setValue("");
			IqpAppendTerms.opan_org_name._setValue("");
			IqpAppendTerms.opan_org_name._obj._renderReadonly(false);
			IqpAppendTerms.opac_org_no._obj._renderReadonly(false);
		}
    };
  //-------------通过账户属性和是否非是否本行账户判断是否币种是否可选------------
	function changeCurTypeReadOnly(){
        var is_this_org_acct = IqpAppendTerms.is_this_org_acct._getValue();
        if(is_this_org_acct == "1" ){  //本行的币种不可选
        	IqpAppendTerms.cur_type._obj._renderReadonly(true);
        	//设为人民币默认
        	IqpAppendTerms.cur_type._setValue("CNY");
        		
        }else if(is_this_org_acct == "2" ){ //非本行的时候 币种可以选
        	IqpAppendTerms.cur_type._obj._renderReadonly(false);
        }
	};
	//-------------开户行行号pop框选择返回函数-----------
    function getOrgNo(data){
    	IqpAppendTerms.opac_org_no._setValue(data.bank_no._getValue());
    	IqpAppendTerms.opan_org_name._setValue(data.bank_name._getValue());
    };
  //-------------通过账号获取在我行的账号信息------------
    function getAcctNo(){
    	var isThis = IqpAppendTerms.is_this_org_acct._getValue();
    	if(isThis == 1){
    		 var acctNo = IqpAppendTerms.acct_no._getValue();
    	        if(acctNo == null || acctNo == ""){
    				alert("请先输入账号信息！");
    				return;
    	        }
    			var handleSuccess = function(o){
    				if(o.responseText !== undefined) {
    					try {
    						var jsonstr = eval("("+o.responseText+")");
    					} catch(e) {
    						alert("Parse jsonstr1 define error!" + e.message);
    						return;
    					}
    					var flag = jsonstr.flag;
    					var retMsg = jsonstr.mes;
    					var ACCT_NO = jsonstr.BODY.ACCT_NO;
    					var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
    					var ACCT_TYPE = jsonstr.BODY.ACCT_TYPE;
    					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
    					var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.OPEN_ACCT_BRANCH_NAME;
    					var ORG_NO = jsonstr.BODY.ORG_NO;
    					var ACCT_GL_CODE = jsonstr.BODY.GL_CODE;//增加科目号
    					var CCY=jsonstr.BODY.CCY;//增加币种
    					if(flag == "success"){
    						IqpAppendTerms.acct_name._setValue(ACCT_NAME);
    						IqpAppendTerms.opac_org_no._setValue(OPEN_ACCT_BRANCH_ID);
    						IqpAppendTerms.opan_org_name._setValue(OPEN_ACCT_BRANCH_NAME);
    						IqpAppendTerms.cur_type._setValue(CCY);
    					}else {
    						alert(retMsg); 
    						IqpAppendTerms.acct_name._setValue("");
    						IqpAppendTerms.opac_org_no._setValue("");
    						IqpAppendTerms.opan_org_name._setValue("");
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
    			var url = '<emp:url action="getIqpCusAcctForEsb.do"/>?acct_no='+acctNo;	
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}else {
			alert("非本行账户，直接输入账户信息！");
		}
    };

    function checkCollectType(){
        var collect_type = IqpAppendTerms.collect_type._getValue();
        if(collect_type == "01"){//按固定金额
        	IqpAppendTerms.fee_amt._obj._renderReadonly(false);
        	IqpAppendTerms.fee_rate._obj._renderReadonly(true);
        	IqpAppendTerms.fee_rate._obj._renderHidden(true);
        	IqpAppendTerms.fee_rate._obj._renderRequired(false);
        }else if(collect_type == "02"){//按比率
        	IqpAppendTerms.fee_amt._obj._renderReadonly(true);
        	IqpAppendTerms.fee_rate._obj._renderReadonly(false);
        	IqpAppendTerms.fee_rate._obj._renderHidden(false);
        	IqpAppendTerms.fee_rate._obj._renderRequired(true);
            var prd_id = '${context.prd_id}';
        	if(prd_id == "200024"){
        		IqpAppendTerms.fee_rate._setValue("0.0005");
            }
        }
    };

    function doClean(){
    	IqpAppendTerms.fee_code._setValue("");
    	IqpAppendTerms.fee_type._setValue("");
    	IqpAppendTerms.fee_cur_type._setValue("");
    	IqpAppendTerms.collect_type._setValue("");
    	IqpAppendTerms.fee_amt._setValue("");
    	IqpAppendTerms.is_cycle_chrg._setValue("");
    	IqpAppendTerms.chrg_date._setValue("");
    	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 注释代码，iqp_append_terms表不存在这个字段 begin**/
    	//IqpAppendTerms.is_this_org_acct._setValue("");
    	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 注释代码，iqp_append_terms表不存在这个字段 end**/
    	IqpAppendTerms.acct_no._setValue("");
    	IqpAppendTerms.acct_name._setValue("");
    	IqpAppendTerms.opac_org_no._setValue("");
    	IqpAppendTerms.opan_org_name._setValue("");
    	IqpAppendTerms.cur_type._setValue("");

    };

    function getAccNo(data){
       IqpAppendTerms.fee_acct_no._setValue(data.acct_no._getValue());
    };
	/*--user code end--*/
	/**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  begin**/
	function checkAmt(){
		var prd_id = '${context.prd_id}';
    	if(prd_id == "100051"){
			var amt=IqpAppendTerms.fee_amt._getValue();
			if(parseFloat(amt)<1500) amt=1500;
			IqpAppendTerms.fee_amt._setValue(amt+'');
        }
	};
	/**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  end**/
	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	function doReturn(){
		var url = '<emp:url action="queryIqpAppendTermsList.do"/>?menuIdTab=queryIqpLoanApp&subMenuId=queryIqpAppendTermsList&apply_cur_type=${context.apply_cur_type}&apply_amount=${context.apply_amount}&prd_id=${context.prd_id}&op=update&serno='+'<%=serno%>'+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>'; 
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	/**modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="addIqpAppendTermsRecord.do?serno=${context.serno}&modiflg=${context.modiflg}&modify_rel_serno=${context.modify_rel_serno}" method="POST">
		
		<emp:gridLayout id="IqpAppendTermsGroup" title="费用信息" maxColumn="2">
			<emp:select id="IqpAppendTerms.fee_code" label="费用描述" required="true" dictname="STD_ZB_FEE_CODE"/>
			<emp:select id="IqpAppendTerms.fee_type" label="费用类型" required="true" dictname="STD_ZB_FEE_MODE" colSpan="2" cssElementClass="emp_field_select_select1"/>
			<emp:select id="IqpAppendTerms.fee_cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			
			<emp:select id="IqpAppendTerms.collect_type" label="收费方式" dictname="STD_ZB_COLLECT_TYPE" onblur="checkCollectType()" required="false"/>
			
			<emp:text id="IqpAppendTerms.fee_rate" label="费用比率" maxlength="16" required="true" onblur="caculateAmtByRate()" dataType="Rate" />
		    <!--/**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  begin**/-->
			<emp:text id="IqpAppendTerms.fee_amt" label="费用总金额" maxlength="16" required="true" onblur="checkAmt();" dataType="Currency" />
		    <!--/**added by wangj 2015/05/12  需求编号:XD141222087,法人账户透支需求变更  end**/-->
			<emp:select id="IqpAppendTerms.is_cycle_chrg" label="是否周期性收费" required="true" onchange="checkIsCycleChrg()" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:select id="IqpAppendTerms.chrg_freq" label="收费频率" required="false" dictname="STD_BACK_CYCLE" hidden="true"/>
			<emp:text id="IqpAppendTerms.chrg_date" label="收费日" required="false" dataType="Int" maxlength="10" hidden="true"/>
		    <emp:pop id="IqpAppendTerms.fee_acct_no" label="费用账号" required="true" url="queryIqpCusAcctPop.do?serno=${context.serno}&returnMethod=getAccNo"  />
		    <emp:text id="IqpAppendTerms.serno" label="业务编号" defvalue="<%=serno%>" hidden="true" maxlength="40" required="false" />
		    <!-- modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
		    <emp:text id="IqpAppendTerms.modify_rel_serno" label="打回业务修改流水编号" defvalue="<%=modify_rel_serno%>" hidden="true" required="false" />
		    <!-- modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<!-- modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
			<%if("yes".equals(modiflg)){ %>
				<emp:button id="sub" label="确定" />
				<emp:button id="clean" label="重置"/>
				<emp:button id="return" label="返回" />
			<%}else{ %>
				<emp:button id="sub" label="确定" op="add"/>
				<emp:button id="clean" label="重置"/>
			<%} %>
			<!-- modified by lisj 2015-8-18 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

