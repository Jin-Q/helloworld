<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad(){
		document.getElementById("mainTab").href="javascript:reLoad();";
		isUseLimt();
		checkTakeoverType();
		checkInterestType();

		var limitIndOptions = IqpAssetstrsf.limit_ind._obj.element.options;
    	for(var i=limitIndOptions.length-1;i>=0;i--){	
			if(limitIndOptions[i].value=="3"){//
				limitIndOptions.remove(i);
			}
		}
		/**add by lisj 2014-12-3 资产转让改造，修改交易对手、交易对手开户信息为可手工录入  begin**/
    	IqpAssetstrsf.toorg_no._obj.addOneButton("toorg_no","选择",getToorgNo);
    	IqpAssetstrsf.tooorg_no._obj.addOneButton("tooorg_no","选择",getTooorgNo);
    	/**add by lisj 2014-12-3 资产转让改造，修改交易对手、交易对手开户信息为可手工录入  end**/
	}
	/**add by lisj 2014-12-3 资产转让改造，修改交易对手、交易对手开户信息为可手工录入  begin**/
	 function getToorgNo(){
		 var url = "<emp:url action='queryCusSameOrgForPopList.do'/>&restrictUsed=false&popReturnMethod=returnCusId";
		 url=EMPTools.encodeURI(url); 
	     window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	 };

	 function getTooorgNo(){
		  /*modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin*/
		  var url = "<emp:url action='getPrdBankInfoPopList.do'/>&popReturnMethod=returnTooorgNo&status=1";
		  /*modified by wangj 需求编号：ED150612003 ODS系统取数需求  end*/
		  url=EMPTools.encodeURI(url); 
		  window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};    
	/**add by lisj 2014-12-3 资产转让改造，修改交易对手、交易对手开户信息为可手工录入  end**/
	  
    // 根据转让方式显示不同的字段
	function checkTakeoverType(){
		var takeover_type = IqpAssetstrsf.takeover_type._getValue();
		//'01':'转出卖断式', '02':'转出回购式', '03':'转入买断式', '04':'转入回购式'
		//转出买断式业务展示 本行账户、本行账户名、开户行行号、开户行行名
		if(takeover_type == "01"){
        	IqpAssetstrsf.this_acct_no._obj._renderHidden(false);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderHidden(false);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderHidden(false);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderHidden(false);//开户行名
        	//IqpAssetstrsf.tooorg_city._obj._renderHidden(true);//交易对手所在城市
        	IqpAssetstrsf.topp_acct_no._obj._renderHidden(true);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderHidden(true);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderHidden(true);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderHidden(true);//交易对手开户行行名
        	IqpAssetstrsf.topp_acct_no._setValue('');//交易对手账号
        	IqpAssetstrsf.topp_acct_name._setValue('');;//交易对手户名
        	IqpAssetstrsf.tooorg_no._setValue('');;//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._setValue('');;//交易对手开户行行名
        	
        	IqpAssetstrsf.this_acct_no._obj._renderRequired(true);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderRequired(true);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderRequired(true);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderRequired(true);//开户行名
        	//IqpAssetstrsf.tooorg_city._obj._renderRequired(false);//交易对手所在城市
        	IqpAssetstrsf.topp_acct_no._obj._renderRequired(false);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderRequired(false);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderRequired(false);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderRequired(false);//交易对手开户行行名

        //转入买断式业务展示 交易对手账号、交易对手户名、交易对手开户行行号、交易对手开户行行名    
        }else if(takeover_type == "03"){
        	IqpAssetstrsf.this_acct_no._obj._renderHidden(true);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderHidden(true);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderHidden(true);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderHidden(true);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderHidden(false);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderHidden(false);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderHidden(false);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderHidden(false);//交易对手开户行行名
        	//IqpAssetstrsf.tooorg_city._obj._renderHidden(false);//交易对手所在城市
        	IqpAssetstrsf.this_acct_no._setValue('');//本行账户
        	IqpAssetstrsf.this_acct_name._setValue('');//本行账户名
        	IqpAssetstrsf.acctsvcr_no._setValue('');//开户行号
        	IqpAssetstrsf.acctsvcr_name._setValue('');//开户行名
        	
        	IqpAssetstrsf.this_acct_no._obj._renderRequired(false);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderRequired(false);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderRequired(false);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderRequired(false);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderRequired(true);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderRequired(true);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderRequired(true);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderRequired(true);//交易对手开户行行名
        	//IqpAssetstrsf.tooorg_city._obj._renderRequired(true);//交易对手所在城市
        }else{
        	IqpAssetstrsf.this_acct_no._obj._renderHidden(false);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderHidden(false);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderHidden(false);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderHidden(false);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderHidden(false);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderHidden(false);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderHidden(false);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderHidden(false);//交易对手开户行行名

        	IqpAssetstrsf.this_acct_no._obj._renderRequired(true);//本行账户
        	IqpAssetstrsf.this_acct_name._obj._renderRequired(true);//本行账户名
        	IqpAssetstrsf.acctsvcr_no._obj._renderRequired(true);//开户行号
        	IqpAssetstrsf.acctsvcr_name._obj._renderRequired(true);//开户行名
        	IqpAssetstrsf.topp_acct_no._obj._renderRequired(true);//交易对手账号
        	IqpAssetstrsf.topp_acct_name._obj._renderRequired(true);//交易对手户名
        	IqpAssetstrsf.tooorg_no._obj._renderRequired(true);//交易对手开户行行号
        	IqpAssetstrsf.tooorg_name._obj._renderRequired(true);//交易对手开户行行名
        }
		};
	
	function reLoad(){
		var url = '<emp:url action="getIqpAssetstrsfUpdatePage.do"/>?menuId=queryIqpAssetstrsfList&serno=${context.IqpAssetstrsf.serno}&op=update&asset_no=${context.IqpAssetstrsf.asset_no}';
		url = EMPTools.encodeURI(url);
		window.location = url;
		//window.location.reload();
	}
	
    function doSave(data){
		if(!IqpAssetstrsf._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpAssetstrsf._toForm(form);
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
					if(data == "subWF"){
						getApplyTypeByPrdId();
					}else{
						alert("保存成功");
					}
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpAssetstrsfRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}

    function getAssetMsg(data){
        IqpAssetstrsf.asset_no._setValue(data.asset_no._getValue());//资产包编号
        IqpAssetstrsf.takeover_qnt._setValue(data.asset_qnt._getValue());//转让笔数
        IqpAssetstrsf.takeover_type._setValue(data.takeover_type._getValue());//转让方式
        IqpAssetstrsf.asset_total_amt._setValue(data.asset_total_amt._getValue());//资产包总额
        IqpAssetstrsf.takeover_total_amt._setValue(data.takeover_total_amt._getValue());//转让总额
        IqpAssetstrsf.takeover_int._setValue(data.takeover_total_int._getValue());//转让利息

        IqpAssetstrsf.takeover_qnt._obj._renderReadonly(true);
        IqpAssetstrsf.takeover_type._obj._renderReadonly(true);
        IqpAssetstrsf.asset_total_amt._obj._renderReadonly(true);
        IqpAssetstrsf.takeover_total_amt._obj._renderReadonly(true);
        IqpAssetstrsf.takeover_int._obj._renderReadonly(true);

        checkTakeoverType();
    }

  //-----------通过产品编号查询产品配置中使用流程类型----------
    function getApplyTypeByPrdId(){
    	var prdId = IqpAssetstrsf.prd_id._getValue();
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
    	var serno = IqpAssetstrsf.serno._getValue();
    	var cus_id = IqpAssetstrsf.toorg_no._getValue();
    	var cus_name = IqpAssetstrsf.toorg_name._getValue();
    	var approve_status = IqpAssetstrsf.approve_status._getValue(); 
    	WfiJoin.table_name._setValue("IqpAssetstrsf");
    	WfiJoin.pk_col._setValue("serno");
    	WfiJoin.pk_value._setValue(serno);
    	WfiJoin.cus_id._setValue(cus_id);
    	WfiJoin.cus_name._setValue(cus_name);
    	WfiJoin.prd_pk._setValue(IqpAssetstrsf.prd_id._getValue());
    	WfiJoin.prd_name._setValue(IqpAssetstrsf.prd_name._getValue());
    	WfiJoin.amt._setValue(IqpAssetstrsf.takeover_total_amt._getValue());
    	WfiJoin.wfi_status._setValue(approve_status);
    	WfiJoin.status_name._setValue("approve_status");
    	WfiJoin.appl_type._setValue(apply_type);
    	initWFSubmit(false);
    };

    function isUseLimt(){
        var limit_ind = IqpAssetstrsf.limit_ind._getValue();
        if(limit_ind=='1'||limit_ind==''){
        	IqpAssetstrsf.limit_acc_no._obj._renderHidden(true);
        	IqpAssetstrsf.limit_acc_no._obj._renderRequired(false);
        	IqpAssetstrsf.limit_acc_no._setValue('');

        	IqpAssetstrsf.limit_acc_no._obj.config.url='';
        }else{
        	IqpAssetstrsf.limit_acc_no._obj._renderHidden(false);
        	IqpAssetstrsf.limit_acc_no._obj._renderRequired(true);

        	var cus_id = IqpAssetstrsf.toorg_no._getValue();
        	var lmt_type = "02";//02-同业客户
        	var prd_id = IqpAssetstrsf.prd_id._getValue();
        	var outstnd_amt = IqpAssetstrsf.takeover_total_amt._getValue();
        	if(cus_id == null || cus_id == ""){
        		alert("请确认交易对手行号!");
        		IqpAssetstrsf.limit_ind._setValue("");
        		isUseLimt();
            }
        	if(outstnd_amt == null || outstnd_amt == ""){
               alert("请确认转让金额!");
               IqpAssetstrsf.limit_ind._setValue("");
               isUseLimt();
            }
        	if(limit_ind == "2"){
            	limit_ind = "01";
            }else if(limit_ind == "3"){
            	limit_ind = "02";
            }else if(limit_ind == "4"){
            	lmt_type = "04";//使用第三方同业额度
            }
        	IqpAssetstrsf.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do?returnMethod=getLmtAmt"/>&limit_ind='+limit_ind+'&cus_id='+cus_id+'&lmt_type='+lmt_type+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
        }
    }

    function getLmtAmt(data){
    	var lmtContNo = data[0];//授信协议编号
    	var lmtAmt = data[1];//授信余额暂时先去授信金额
    	IqpAssetstrsf.limit_acc_no._setValue(lmtContNo);
    	//remain_amount._setValue(lmtAmt+"");//剩余额度
    }
	/*--user code end--*/
	function returnCusId(data){
		IqpAssetstrsf.toorg_no._setValue(data.same_org_no._getValue());
		IqpAssetstrsf.toorg_name._setValue(data.same_org_cnname._getValue());
	};
	function returnTooorgNo(data){
		IqpAssetstrsf.tooorg_no._setValue(data.bank_no._getValue());
		IqpAssetstrsf.tooorg_name._setValue(data.bank_name._getValue());
    };
	function getAcctsvcrNo(data){
		IqpAssetstrsf.acctsvcr_no._setValue(data.bank_no._getValue());
		IqpAssetstrsf.acctsvcr_name._setValue(data.bank_name._getValue());
    };
    function getOrgID(data){
    	IqpAssetstrsf.manager_br_id._setValue(data.organno._getValue());
		IqpAssetstrsf.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function checkInterestType(){
    	var interest_type = IqpAssetstrsf.interest_type._getValue();
    	if(interest_type =="1"){//自主 
    		IqpAssetstrsf.trust_rate._obj._renderHidden(true);
    		IqpAssetstrsf.trust_rate._obj._renderRequired(false);
        }else{
        	IqpAssetstrsf.trust_rate._obj._renderHidden(false);
    		IqpAssetstrsf.trust_rate._obj._renderRequired(true);
        }
    };
</script>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
</style>
</head>
<body class="page_content" onload="doOnLoad()">
  <emp:tabGroup mainTab="mainTab" id="mainTab">
   		<emp:tab label="基本信息" id="mainTab"> 
			<emp:form id="submitForm" action="updateIqpAssetstrsfRecord.do" method="POST">
				<emp:gridLayout id="IqpAssetstrsfGroup" title="基本信息" maxColumn="2">
					<emp:text id="IqpAssetstrsf.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
					<emp:pop id="IqpAssetstrsf.asset_no" label="资产包编号" url="queryIqpAssetMngListPop.do?returnMethod=getAssetMsg" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAssetstrsf.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
					<emp:text id="IqpAssetstrsf.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产转受让" readonly="true"/>
					<emp:select id="IqpAssetstrsf.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE" colSpan="2" readonly="true"/>
					<emp:text id="IqpAssetstrsf.tooorg_city" label="交易对手所在城市" maxlength="50" required="true" colSpan="2"/>
					<emp:text id="IqpAssetstrsf.toorg_no" label="交易对手行号" required="true" />
					<emp:text id="IqpAssetstrsf.toorg_name" label="交易对手行名" maxlength="100" required="true" readonly="false" colSpan="2" cssElementClass="emp_field_text_long"/>
					<emp:text id="IqpAssetstrsf.topp_acct_no" label="交易对手账户" maxlength="40" dataType="Acct" required="false" cssElementClass="emp_field_input"/>
					<emp:text id="IqpAssetstrsf.topp_acct_name" label="交易对手户名" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long"/>
					<emp:text id="IqpAssetstrsf.tooorg_no" label="交易对手开户行行号" required="true" />
					<emp:text id="IqpAssetstrsf.tooorg_name" label="交易对手开户行行名" maxlength="100" required="true" readonly="false" colSpan="2" cssElementClass="emp_field_text_long"/>
					<emp:text id="IqpAssetstrsf.this_acct_no" label="本行账户" maxlength="40" dataType="Acct" required="true" cssElementClass="emp_field_input"/> 
					<emp:text id="IqpAssetstrsf.this_acct_name" label="本行账户名" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_long"/>
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
					<emp:pop id="IqpAssetstrsf.acctsvcr_no" label="开户行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getAcctsvcrNo" required="true" buttonLabel="选择" />
					<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
					<emp:text id="IqpAssetstrsf.acctsvcr_name" label="开户行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAssetstrsf.takeover_qnt" label="转让笔数" maxlength="38" required="false" dataType="Int" readonly="true"/>
					<emp:select id="IqpAssetstrsf.acct_curr" label="转让币种" required="false" dictname="STD_ZX_CUR_TYPE"/>
					<emp:text id="IqpAssetstrsf.asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpAssetstrsf.takeover_total_amt" label="转让金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:text id="IqpAssetstrsf.takeover_int" label="转让利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
					<emp:date id="IqpAssetstrsf.takeover_date" label="转让日期" required="true" />
					<emp:select id="IqpAssetstrsf.interest_type" label="收息方式" required="true" onchange="checkInterestType()" dictname="STD_RCV_INT_TYPE"/>
					<emp:text id="IqpAssetstrsf.trust_rate" label="委托费率" maxlength="10" required="true" dataType="Rate" />
					<emp:textarea id="IqpAssetstrsf.memo" label="备注" maxlength="250" required="false" colSpan="2"/>
					<emp:select id="IqpAssetstrsf.is_risk_takeover" label="风险是否转移" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="2" title="额度信息">
					<emp:select id="IqpAssetstrsf.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt()" dictname="STD_INTBANK_LIMIT_IND" colSpan="2"/>	   
		    		<emp:pop id="IqpAssetstrsf.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
		    		<emp:text id="remain_amount" label="剩余额度" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="3" title="登记信息">
					<emp:pop id="IqpAssetstrsf.manager_br_id_displayname" label="管理机构" required="true"  buttonLabel="选择" url="querySOrgPop.do?yewu=is&restrictUsed=false" returnMethod="getOrgID" />
		    		<emp:select id="IqpAssetstrsf.flow_type" label="流程类型" readonly="true"  required="false" dictname="STD_ZB_FLOW_TYPE" />
		    		<emp:select id="IqpAssetstrsf.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" required="false" hidden="true"/>
		   			<emp:text id="IqpAssetstrsf.input_id_displayname" label="登记人" defvalue="${context.currentUserName}" required="false"  readonly="true" />
					<emp:text id="IqpAssetstrsf.input_br_id_displayname" label="登记机构" defvalue="${context.organName}" required="false"  readonly="true" />
			
					<emp:date id="IqpAssetstrsf.input_date" label="登记日期" required="false" readonly="true" />
					<emp:text id="IqpAssetstrsf.manager_br_id" label="管理机构" hidden="true" />
					<emp:text id="IqpAssetstrsf.input_id" label="登记人" defvalue="${context.currentUserId}" hidden="true" maxlength="20" required="false"  readonly="true" />
					<emp:text id="IqpAssetstrsf.input_br_id" label="登记机构" defvalue="${context.organNo}" hidden="true" maxlength="20" required="false"  readonly="true" />
				</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存" op="update"/>
			<emp:button id="subWF" label="放入流程" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
   </emp:tab>
   <emp:ExtActTab></emp:ExtActTab>
  </emp:tabGroup>
</body>
</html>
</emp:page>


<script type="text/javascript">
   function doSubWF(){
	   if(!IqpAssetstrsf._checkAll()){
			return;
		}

	   checkAssetInfo();
	  
		/**
		var form = document.getElementById("submitForm");
		IqpAssetstrsf._toForm(form);
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
					alert("提交成功！");
				}else {
					alert("提交失败！");
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

		var url = '<emp:url action="tempProcessIqpAssetstrsf.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
		*/
   }

   //放入流程前先校验资产信息是否完整
   function checkAssetInfo(){
   	var asset_no = IqpAssetstrsf.asset_no._getValue();
   	var serno = IqpAssetstrsf.serno._getValue();
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
   			 	doSave('subWF');
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

   	var url="<emp:url action='checkAssetInfo.do'/>?asset_no="+asset_no+"&serno="+serno;
   	url = EMPTools.encodeURI(url);
   	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
   }
</script>