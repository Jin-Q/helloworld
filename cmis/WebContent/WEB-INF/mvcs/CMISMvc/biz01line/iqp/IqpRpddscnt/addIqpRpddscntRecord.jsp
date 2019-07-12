<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript">
</script>
<script type="text/javascript" >

	/*--user code begin--*/
	//-------异步保存主表单页面信息-------
	function doSave(){
		if(!IqpRpddscnt._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpRpddscnt._toForm(form);
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
				var msg = jsonstr.msg;
				if(flag == "success"){
					var url = '<emp:url action="getIqpRpddscntUpdatePage.do"/>?serno='+serno;
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

		var url = '<emp:url action="addIqpRpddscntRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}			

	function doOnLoad(){
		IqpRpddscnt.batch_no._obj.addOneButton("batch","选择",getBatch);
		IqpRpddscnt.topp_acct_no._obj.addOneButton("toppAcctNo","获取",getToppAcctNo);
		IqpRpddscnt.this_acct_no._obj.addOneButton("thisAcctNo","获取",getThisAcctNo);
		IqpRpddscnt.prd_id._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline=BL100';
		IqpRpddscnt.batch_no._obj._renderReadonly(true);
		//控制业务类型字典
		removeOpts(IqpRpddscnt.rpddscnt_type,'07','');
		IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
    	IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
    	IqpRpddscnt.limit_acc_no._setValue('');
    	IqpRpddscnt.limit_acc_no._obj.config.url='';

    	IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
    	IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
    	IqpRpddscnt.limit_credit_no._setValue('');
    	IqpRpddscnt.limit_credit_no._obj.config.url='';

    	var limitIndOptions = IqpRpddscnt.limit_ind._obj.element.options;
    	for(var i=limitIndOptions.length-1;i>=0;i--){	
			if(limitIndOptions[i].value=="3"){//
				limitIndOptions.remove(i);
			}
		}
	}; 
	
	function getBatch(){
		var url = '<emp:url action="importIqpBatchPage.do"/>&serno=${context.serno}&prd_id=${context.prd_id}&restrictUsed=false';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};


	function batchnoChange(){
       var batch_no = IqpRpddscnt.batch_no._getValue();
		if(batch_no == null || batch_no == ""){
			alert("请选择批次号！");
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
				if(jsonstr.biz_type == "07"){//直贴
                   alert("该批次为直贴批次，不能使用！");
				   IqpRpddscnt.batch_no._setValue("");
				   return false;
				}
				if(jsonstr.serno != "" && jsonstr.serno != null){
                   alert("该批次已被其他业务引入，不能使用！");
                   IqpRpddscnt.batch_no._setValue("");
                   return false;
				}
                if(jsonstr.biz_type == "03" || jsonstr.biz_type == "04"){
                	IqpRpddscnt.rpay_amt_2._obj._renderHidden(false);
                	IqpRpddscnt.rpay_amt_2._setValue(jsonstr.rpay_amt);
                	IqpRpddscnt.rpay_amt_1._obj._renderHidden(true);
                }else{
                	IqpRpddscnt.rpay_amt_1._obj._renderHidden(false);
                	IqpRpddscnt.rpay_amt_1._setValue(jsonstr.rpay_amt);
                	IqpRpddscnt.rpay_amt_2._obj._renderHidden(true);
                }
                
				IqpRpddscnt.rpddscnt_type._setValue(jsonstr.biz_type);//转贴现方式
				IqpRpddscnt.bill_type._setValue(jsonstr.bill_type);//票据种类
				IqpRpddscnt.bill_curr._setValue('CNY');//票据币种
				IqpRpddscnt.bill_qnt._setValue(jsonstr.bill_qnt);//票据数量
				IqpRpddscnt.bill_total_amt._setValue(jsonstr.bill_total_amt);//票据总金额
				IqpRpddscnt.rpddscnt_date._setValue(jsonstr.fore_disc_date);//转贴现日期
				IqpRpddscnt.rpddscnt_rate._setValue(jsonstr.rate);//转贴现利率
				IqpRpddscnt.rpddscnt_int._setValue(jsonstr.int_amt);//总贴现利息
				IqpRpddscnt.rpay_amt._setValue(jsonstr.rpay_amt);//总实付金额
				IqpRpddscnt.rebuy_date._setValue(jsonstr.rebuy_date);//回购日期
				IqpRpddscnt.rebuy_rate._setValue(jsonstr.rate);//回购利率
				IqpRpddscnt.rebuy_int._setValue(jsonstr.rebuy_int);//总回购利息
				IqpRpddscnt.toorg_no._setValue(jsonstr.opp_org_no);//交易对手行号
				IqpRpddscnt.toorg_name._setValue(jsonstr.opp_org_name);//交易对手行名
				var biz_type = jsonstr.biz_type;
				if(biz_type=='06'){//再贴现
					IqpRpddscnt.prd_id._setValue('300024');
					prd_name._setValue('再贴现');
				}else if(biz_type=='05'){
					IqpRpddscnt.prd_id._setValue('300022');
					prd_name._setValue('内部转贴现');
					IqpRpddscnt.limit_ind._setValue("1");
					IqpRpddscnt.limit_ind._obj._renderReadonly(true);
				}else{
					IqpRpddscnt.prd_id._setValue('300023');
					prd_name._setValue('外部转贴现');
				}
				setReadonly();
				cleanLimitInd();//转贴现转出 授信占承兑人
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		var url = '<emp:url action="getBatchMsgByBatchNo.do"/>?batch_no='+batch_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
		
	}
	
    //设置页面要素只读
    function setReadonly(){
    	IqpRpddscnt.rpddscnt_type._obj._renderReadonly(true);
		IqpRpddscnt.bill_type._obj._renderReadonly(true);
		IqpRpddscnt.bill_curr._obj._renderReadonly(true);
		IqpRpddscnt.bill_qnt._obj._renderReadonly(true);
		IqpRpddscnt.bill_total_amt._obj._renderReadonly(true);
		IqpRpddscnt.rpddscnt_date._obj._renderReadonly(true);
		IqpRpddscnt.rpddscnt_rate._obj._renderReadonly(true);
		IqpRpddscnt.rpddscnt_int._obj._renderReadonly(true);
		IqpRpddscnt.rpay_amt._obj._renderReadonly(true);
		IqpRpddscnt.rebuy_date._obj._renderReadonly(true);
		IqpRpddscnt.rebuy_rate._obj._renderReadonly(true);
		IqpRpddscnt.rebuy_int._obj._renderReadonly(true);
		IqpRpddscnt.approve_status._obj._renderReadonly(true);
        if("04" == IqpRpddscnt.rpddscnt_type._getValue()||"02" == IqpRpddscnt.rpddscnt_type._getValue()){//如果转贴现方式为 卖出回购、买入返售
        	//非卖出回购式，隐藏回购日期、回购利率、总回购利息、总回购金额
        	IqpRpddscnt.rebuy_date._obj._renderHidden(false);
        	IqpRpddscnt.rebuy_rate._obj._renderHidden(true);
        	IqpRpddscnt.rebuy_int._obj._renderHidden(true);
        	$(".emp_field_label:eq(11)").text("总回购利息");
        	$(".emp_field_label:eq(15)").text("回购起始日");
			$(".emp_field_label:eq(14)").text("回购利率");
        }else{
            //非卖出回购式，隐藏回购日期、回购利率、总回购利息、总回购金额
        	IqpRpddscnt.rebuy_date._obj._renderHidden(true);
        	IqpRpddscnt.rebuy_rate._obj._renderHidden(true);
        	IqpRpddscnt.rebuy_int._obj._renderHidden(true);
        	$(".emp_field_label:eq(11)").text("总贴现利息");
        	$(".emp_field_label:eq(15)").text("转贴现日期");
			$(".emp_field_label:eq(14)").text("转贴现利率");
        }
    }
    function returnPrdId(data){ 
    	IqpRpddscnt.prd_id._setValue(data.id);
    	prd_name._setValue(data.label);
	}

	function getOrgID(data){
		IqpRpddscnt.manager_br_id._setValue(data.organno._getValue());
		IqpRpddscnt.manager_br_id_displayname._setValue(data.organname._getValue());
	}	

	//-------------对手行行号pop框选择返回函数-----------
    function getToorgNo(data){
    	IqpRpddscnt.toorg_no._setValue(data.bank_no._getValue());
    	IqpRpddscnt.toorg_name._setValue(data.bank_name._getValue());
    };

    function getTooorgNo(data){
    	IqpRpddscnt.tooorg_no._setValue(data.bank_no._getValue());
    	IqpRpddscnt.tooorg_name._setValue(data.bank_name._getValue());
    };
    function getAcctsvcrNo(data){
    	IqpRpddscnt.acctsvcr_no._setValue(data.bank_no._getValue());
    	IqpRpddscnt.acctsvcr_name._setValue(data.bank_name._getValue());
    };
    function isUseLimt(){
        var limit_ind = IqpRpddscnt.limit_ind._getValue();
        var cus_id = IqpRpddscnt.toorg_no._getValue();
    	var lmt_type = "02";//02-同业客户
    	var prd_id = IqpRpddscnt.prd_id._getValue();
    	var outstnd_amt = IqpRpddscnt.rpay_amt._getValue();
    	var rpddscnt_type = IqpRpddscnt.rpddscnt_type._getValue();
        //必输项控制
        if(cus_id == null || cus_id == ""){
           alert("请输入交易对手行号");
           IqpRpddscnt.limit_ind._setValue();
           return;
        }
        if(limit_ind=='1'||limit_ind==''){
        	IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
        	IqpRpddscnt.limit_acc_no._setValue('');
        	IqpRpddscnt.limit_acc_no._obj.config.url='';

        	IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
        	IqpRpddscnt.limit_credit_no._setValue('');
        	IqpRpddscnt.limit_credit_no._obj.config.url='';
        }else if(limit_ind=='4'){
        	 
         	IqpRpddscnt.limit_credit_no._obj._renderHidden(false);
         	IqpRpddscnt.limit_credit_no._obj._renderRequired(true);
         	
         	IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
         	IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
         	IqpRpddscnt.limit_acc_no._setValue('');
         	IqpRpddscnt.limit_credit_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do"/>&cus_id='+cus_id+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt+"&returnMethod=getLmtCoopAmt&lmt_type=04&rpddscnt_type="+rpddscnt_type;
         	IqpRpddscnt.limit_acc_no._obj.config.url='';
         	var rpddscnt_type = '';
            var bill_type = IqpRpddscnt.bill_type._getValue();
        	if(prd_id=='300023' && bill_type=='100'){//外部转贴现 ,银票需控制
    			rpddscnt_type = IqpRpddscnt.rpddscnt_type._getValue();
    		}
         	if(rpddscnt_type == "01"){
         		IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
         		IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
             	
         		IqpRpddscnt.limit_acc_no._obj._renderHidden(true);
         		IqpRpddscnt.limit_acc_no._obj._renderRequired(false);
             }
        }else{
        	IqpRpddscnt.limit_acc_no._obj._renderHidden(false);
        	IqpRpddscnt.limit_acc_no._obj._renderRequired(true);
        	IqpRpddscnt.limit_credit_no._obj._renderHidden(true);
        	IqpRpddscnt.limit_credit_no._obj._renderRequired(false);
        	IqpRpddscnt.limit_credit_no._setValue('');
        	IqpRpddscnt.limit_credit_no._obj.config.url='';
        	
        	if(limit_ind == "2"){
            	limit_ind = "01";
            }else if(limit_ind == "3"){
            	limit_ind = "02";
            }
        	IqpRpddscnt.limit_acc_no._obj.config.url='<emp:url action="selectLmtAgrDetails.do?returnMethod=getLmtAmt"/>&limit_ind='+limit_ind+'&cus_id='+cus_id+'&lmt_type='+lmt_type+"&prd_id="+prd_id+"&outstnd_amt="+outstnd_amt;
        }
    };
    function getLmtAmt(data){
    	var lmtContNo = data[0];//授信协议编号
    	var lmtAmt = data[1];//授信余额暂时先去授信金额
    	IqpRpddscnt.limit_acc_no._setValue(lmtContNo);
    	//remain_amount._setValue(lmtAmt+"");//剩余额度
    };
    function getLmtCoopAmt(data){
    	var lmtContNo = data[0];//授信协议编号
    	var lmtAmt = data[1];//授信余额暂时先去授信金额
    	IqpRpddscnt.limit_credit_no._setValue(lmtContNo); 
    	//remain_amount._setValue(lmtAmt+"");//剩余额度
    };

  //-------------通过账号获取在我行的账号信息------------
    function getToppAcctNo(){//交易对手账号 
        var acctNo = IqpRpddscnt.topp_acct_no._getValue();
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
					IqpRpddscnt.topp_acct_name._setValue(ACCT_NAME);
					IqpRpddscnt.tooorg_no._setValue(OPEN_ACCT_BRANCH_ID);
					IqpRpddscnt.tooorg_name._setValue(OPEN_ACCT_BRANCH_NAME);
				}else {
					alert(retMsg);
					IqpRpddscnt.topp_acct_name._setValue("");
					IqpRpddscnt.tooorg_no._setValue("");
					IqpRpddscnt.tooorg_name._setValue("");
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
    };
    
  //-------------通过账号获取在我行的账号信息------------
    function getThisAcctNo(){
        var acctNo = IqpRpddscnt.this_acct_no._getValue();
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
					IqpRpddscnt.this_acct_name._setValue(ACCT_NAME);
					IqpRpddscnt.acctsvcr_no._setValue(OPEN_ACCT_BRANCH_ID);
					IqpRpddscnt.acctsvcr_name._setValue(OPEN_ACCT_BRANCH_NAME);
				}else {
					alert(retMsg);
					IqpRpddscnt.this_acct_name._setValue("");
					IqpRpddscnt.acctsvcr_no._setValue("");
					IqpRpddscnt.acctsvcr_name._setValue("");
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
    };

    function cleanLimitInd(){
    	var rpddscnt_type = IqpRpddscnt.rpddscnt_type._getValue();
    	var limitIndOptions = IqpRpddscnt.limit_ind._obj.element.options;
    	//银票贴现,商票贴现 2014-03-15wangs添加
    	//'1':'不使用额度', '2':'使用循环额度', '3':'使用一次性额度', '4':'使用第三方额度'
    	var bill_type = IqpRpddscnt.bill_type._getValue();
    	if(bill_type=='100'){//仅银票需控制
    		if(rpddscnt_type == "01"){//'01':'买入买断',
        		for(var i=limitIndOptions.length-1;i>=0;i--){	
        			if(limitIndOptions[i].value=="4"){//
        				limitIndOptions.remove(i);
        			}
        		}
        		var varOption = new Option('使用承兑人额度','4');
        		limitIndOptions.add(varOption);
        		IqpRpddscnt.limit_ind._setValue("4");
        		IqpRpddscnt.limit_ind._obj._renderReadonly(true);
        	}
        }else if(bill_type=='200'){//商票需控制
        	if(rpddscnt_type == "01" || rpddscnt_type == "02"){//'01':'买入买断', '02':'买入返售'
        		var limitIndOptions = IqpRpddscnt.limit_ind._obj.element.options;
        		for(var i=limitIndOptions.length-1;i>=0;i--){	
        			if(limitIndOptions[i].value=="4"){//
        				limitIndOptions.remove(i);
        			}
        		}
        		var varOption = new Option('使用第三方额度','4');
        		limitIndOptions.add(varOption);
        	}
        }
    };
	/*--user code end--*/    
	
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
	<emp:form id="submitForm" action="addIqpRpddscntRecord.do" method="POST">
		<emp:gridLayout id="IqpRpddscntGroup" title="转贴现申请信息" maxColumn="2">
			<emp:text id="IqpRpddscnt.serno" label="业务编号" maxlength="40" colSpan="2" hidden="true" required="false" />
			<emp:text id="IqpRpddscnt.batch_no" label="批次号" required="true" onchange="batchnoChange()" colSpan="2"/>
			<emp:text id="IqpRpddscnt.prd_id" label="产品编码" required="true"  readonly="true"/>
			<emp:text id="prd_name" label="产品名称" readonly="true" />
			<emp:select id="IqpRpddscnt.rpddscnt_type" label="转贴现方式" required="true"  dictname="STD_ZB_BUSI_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpRpddscnt.toorg_no" label="交易对手行号"  readonly="true" required="true" />
			<emp:text id="IqpRpddscnt.toorg_name" label="交易对手行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		</emp:gridLayout>
		            <emp:text id="IqpRpddscnt.topp_acct_no" label="交易对手账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input" hidden="true"/>
			        <emp:text id="IqpRpddscnt.topp_acct_name" label="交易对手户名" maxlength="40" required="false" readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.tooorg_no" label="交易对手开户行行号"  required="false"  readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.tooorg_name" label="交易对手开户行行名" maxlength="100" required="false" readonly="true" hidden="true"/>
			
			        <emp:text id="IqpRpddscnt.this_acct_no" label="本行账号" maxlength="40" required="false" dataType="Acct" cssElementClass="emp_field_input" hidden="true"/>
			        <emp:text id="IqpRpddscnt.this_acct_name" label="本行账户名" maxlength="100" required="false" readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.acctsvcr_no" label="本行开户行行号"  required="false" readonly="true" hidden="true"/>
			        <emp:text id="IqpRpddscnt.acctsvcr_name" label="本行开户行行名" maxlength="100" required="false" readonly="true" hidden="true"/>
				
		<emp:gridLayout id="IqpRpddscntGroup" title="票据信息" maxColumn="2">
			<emp:select id="IqpRpddscnt.bill_type" label="票据种类" required="false"  dictname="STD_DRFT_TYPE" readonly="true"/>
			<emp:select id="IqpRpddscnt.bill_curr" label="票据币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="IqpRpddscnt.bill_total_amt" label="票据总金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpRpddscnt.bill_qnt" label="票据数量" maxlength="38" required="false" readonly="true"/>
			<emp:text id="IqpRpddscnt.rpddscnt_int" label="总贴现利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpRpddscnt.rpay_amt_1" label="总实付金额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
			<emp:text id="IqpRpddscnt.rpay_amt_2" label="总实收金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpRpddscnt.rpddscnt_rate" label="转贴现利率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
			<emp:date id="IqpRpddscnt.rpddscnt_date" label="转贴现日期" required="false" readonly="true"/>
			<emp:date id="IqpRpddscnt.rebuy_date" label="回购日期" required="false" readonly="true"/>
			<emp:text id="IqpRpddscnt.rebuy_rate" label="回购利率" maxlength="10" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="IqpRpddscnt.rebuy_int" label="总回购利息" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:textarea id="IqpRpddscnt.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpRpddscnt.rpay_amt" label="总实付金额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="额度信息">
			<emp:select id="IqpRpddscnt.limit_ind" label="授信额度使用标志" required="true" onchange="isUseLimt()" dictname="STD_INTBANK_LIMIT_IND" colSpan="2"/>	   
		    <emp:pop id="IqpRpddscnt.limit_acc_no" label="授信台账编号"  url="selectLmtAgrDetails.do" returnMethod="getLmtAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
		    <emp:pop id="IqpRpddscnt.limit_credit_no" label="第三方授信编号" url="selectLmtAgrDetails.do" returnMethod="getLmtCoopAmt" required="false" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:pop id="IqpRpddscnt.manager_br_id_displayname" label="管理机构" required="true"  buttonLabel="选择" url="querySOrgPop.do?yewu=is&restrictUsed=false" returnMethod="getOrgID" />
		    <emp:select id="IqpRpddscnt.flow_type" label="流程类型"  required="false" readonly="true" defvalue="01" dictname="STD_ZB_FLOW_TYPE" />
		    <emp:select id="IqpRpddscnt.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" required="false" defvalue="000" hidden="true"/>
		    <emp:text id="IqpRpddscnt.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="IqpRpddscnt.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="IqpRpddscnt.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="IqpRpddscnt.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpRpddscnt.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="IqpRpddscnt.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="save" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

