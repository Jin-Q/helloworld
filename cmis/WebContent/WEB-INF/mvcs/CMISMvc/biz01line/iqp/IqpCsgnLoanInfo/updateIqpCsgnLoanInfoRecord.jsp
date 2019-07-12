<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont = "";
	String flag = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}     
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	} 
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnload(){
		IqpCsgnLoanInfo.csgn_acct_no._obj.addOneButton("csgn_acct_no","获取",getAcctNoForm);
		IqpCsgnLoanInfo.csgn_inner_dep_no._obj.addOneButton("csgn_inner_dep_no","获取",getDepNoForm);
		IqpCsgnLoanInfo.csgn_cus_id._obj.addTheButton("csgn_cus_id","委托人影像查看",getImageView);
    }
    
    /**add by lisj 2014年10月29日  关于新信贷系统增加影响查看功能  begin**/
	function getImageView(){
		var csgn_cus_id =IqpCsgnLoanInfo.csgn_cus_id._getValue();
		if(csgn_cus_id != null && csgn_cus_id != ""){
			ImageAction('View24');	//2.4 客户资料查看
		}else{
			alert('委托人客户码不能为空！');
			}
	};

	function ImageAction(image_action){
		var data = new Array();
		data['serno'] =  IqpCsgnLoanInfo.csgn_cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = IqpCsgnLoanInfo.csgn_cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/**add by lisj 2014年10月29日  关于新信贷系统增加影响查看功能  end**/
	
	function getAcctNoForm(){
		var cusName = IqpCsgnLoanInfo.csgn_cus_id_displayname._getValue();
		var acctNo = IqpCsgnLoanInfo.csgn_acct_no._getValue();
		var flag1 ="<%=flag%>";
		if(cusName == null || cusName == ""){
			alert("请先选择委托人！");
			return;
		}
		if(acctNo == null || acctNo == ""){
			alert("请先输入委托人一般账号信息！");
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
				var retMsg = jsonstr.retMsg;
				var ACCT_NO = jsonstr.BODY.ACCT_NO;
				var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
				var ACCT_TYPE = jsonstr.BODY.ACCT_TYPE;
				var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
				var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.OPEN_ACCT_BRANCH_NAME;
				var ORG_NO = jsonstr.BODY.ORG_NO;
				if(flag == "success"){
					if(flag1 == '1'){
						IqpCsgnLoanInfo.csgn_acct_name._setValue(ACCT_NAME);
					}else{
						if(ACCT_NAME==cusName){
							IqpCsgnLoanInfo.csgn_acct_name._setValue(ACCT_NAME);
						}else{
	                        alert("委托人客户名称与委托人一般账户名不一致!");
	                        IqpCsgnLoanInfo.csgn_acct_no._setValue("");
	                        IqpCsgnLoanInfo.csgn_acct_name._setValue("");
						}
					}
				}else {
					//alert("无此委托人一般账号信息"); 
					alert(retMsg);
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
		var url = '<emp:url action="clientTrade4Esb.do"/>?acct_no='+acctNo+'&service_code=11003000007&sence_code=17';	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
   };

	function getDepNoForm(){
		var cusName = IqpCsgnLoanInfo.csgn_cus_id_displayname._getValue();
		var acctNo = IqpCsgnLoanInfo.csgn_inner_dep_no._getValue();
		var flag1 ="<%=flag%>";
		if(cusName == null || cusName == ""){
			alert("请先选择委托人！");
			return;
		}
		if(acctNo == null || acctNo == ""){
			alert("请先输入委托人内部存款账号信息！");
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
				var retMsg = jsonstr.retMsg;
				var ACCT_NO = jsonstr.BODY.ACCT_NO;
				var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
				var ACCT_TYPE = jsonstr.BODY.ACCT_TYPE;
				var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
				var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.OPEN_ACCT_BRANCH_NAME;
				var ORG_NO = jsonstr.BODY.ORG_NO;
				if(flag == "success"){
					if(flag1 == '1'){
						IqpCsgnLoanInfo.csgn_inner_dep_name._setValue(ACCT_NAME);
					}else{
						if(ACCT_NAME==cusName){
							IqpCsgnLoanInfo.csgn_inner_dep_name._setValue(ACCT_NAME);
						}else{
	                        alert("委托人客户名称与委托人内部存款账户名不一致!");
	                        IqpCsgnLoanInfo.csgn_inner_dep_no._setValue("");
	                        IqpCsgnLoanInfo.csgn_inner_dep_name._setValue("");
						}
					}
					
				}else {
					//alert("无此委托人内部存款账号信息!"); 
					alert(retMsg);
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
		var url = '<emp:url action="clientTrade4Esb.do"/>?acct_no='+acctNo+'&service_code=11003000007&sence_code=17';	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
   };
   
	function checkAmtApp(){
		var csgn_amt = IqpCsgnLoanInfo.csgn_amt._getValue();
		if(csgn_amt == null || csgn_amt == ""){
			alert("请先输入委托金额！");
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
				if(flag == "error"){
					alert("委托贷款委托人总金额不等于贷款申请金额!");
					IqpCsgnLoanInfo.csgn_amt._setValue();
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
		var url = '<emp:url action="checkIqpCsgnLoanAmtApp.do"/>?serno=${context.serno}&csgn_amt='+csgn_amt;	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
   };
    
	function doSave(){
		if(!IqpCsgnLoanInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpCsgnLoanInfo._toForm(form);
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
					alert("保存成功！");
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

		var url = '<emp:url action="updateIqpCsgnLoanInfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}		
	function returnCus(data){
		  IqpCsgnLoanInfo.csgn_cus_id._setValue(data.cus_id._getValue());
		  IqpCsgnLoanInfo.csgn_cus_id_displayname._setValue(data.cus_name._getValue());   
	}; 

	function checkCsgnRate(){
		var csgn_chrg_pay_rate = IqpCsgnLoanInfo.csgn_chrg_pay_rate._getValue();//委托人手续费支付比例
		if(csgn_chrg_pay_rate != "" && csgn_chrg_pay_rate != null){
			if(1-(parseFloat(csgn_chrg_pay_rate)/100)==0){
			   IqpCsgnLoanInfo.debit_chrg_pay_rate._obj.element.value="0.00%";
			}else{
				IqpCsgnLoanInfo.debit_chrg_pay_rate._setValue(1-(parseFloat(csgn_chrg_pay_rate)/100));
			}
		}
    };
    
	function checkDebitRate(){
		var debit_chrg_pay_rate = IqpCsgnLoanInfo.debit_chrg_pay_rate._getValue();//借款人手续费支付比例
		if(debit_chrg_pay_rate != "" && debit_chrg_pay_rate != null){
			if(1-(parseFloat(debit_chrg_pay_rate)/100)==0){
				IqpCsgnLoanInfo.csgn_chrg_pay_rate._obj.element.value="0.00%";
			}else{
				IqpCsgnLoanInfo.csgn_chrg_pay_rate._setValue(1-(parseFloat(debit_chrg_pay_rate)/100));
		    }
		}
    };

    function check(value){
    	var reg = /^(?:[1-9][0-9](\.\d*)?|100(\.\d*)?|0(\.\d*)|[0-9](\.\d*))$/;
    	var checkres = reg.test(value);
		if (!checkres) {
			alert("请输入0-100之间的数值!");
			return true;
		}else{
			return false;
		}
    };
    function checkChrgRate(){
    	var chrg_rate = IqpCsgnLoanInfo.chrg_rate._getValue();
    	if(check(chrg_rate)){
    		IqpCsgnLoanInfo.chrg_rate._setValue("");
        }
    };
    function checkCsgnChrgPayRate(){
    	var chrg_rate = IqpCsgnLoanInfo.csgn_chrg_pay_rate._getValue();
    	if(check(chrg_rate)){
    		IqpCsgnLoanInfo.csgn_chrg_pay_rate._setValue("");
        }
    };
    function checkDebitChrgPayRate(){
    	var chrg_rate = IqpCsgnLoanInfo.debit_chrg_pay_rate._getValue();
    	if(check(chrg_rate)){
    		IqpCsgnLoanInfo.debit_chrg_pay_rate._setValue("");
        }
    };
    
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="updateIqpCsgnLoanInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpCsgnLoanInfoGroup" maxColumn="2" title="委托人信息">
			<emp:text id="IqpCsgnLoanInfo.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" colSpan="2" required="false" readonly="true" hidden="true"/>
			<emp:pop id="IqpCsgnLoanInfo.csgn_cus_id" label="委托人客户码" url="queryAllCusPop.do?returnMethod=returnCus" required="true" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" buttonLabel="选择"/> 
			<emp:text id="IqpCsgnLoanInfo.csgn_cus_id_displayname" label="委托人客户名称 " required="false" readonly="true"/>     
			<emp:text id="IqpCsgnLoanInfo.csgn_amt" label="委托金额" maxlength="18" required="true" dataType="Currency" colSpan="2" onblur="checkAmtApp()"/> 
			<emp:text id="IqpCsgnLoanInfo.csgn_acct_no" label="委托人一般账号" maxlength="40" required="true" />
			<emp:text id="IqpCsgnLoanInfo.csgn_acct_name" label="委托人一般账户名" maxlength="100" required="true" readonly="true"/>  
			<emp:text id="IqpCsgnLoanInfo.chrg_rate" label="手续费率" maxlength="10" required="true" dataType="Rate" colSpan="2" onblur="checkChrgRate()"/>
			<emp:text id="IqpCsgnLoanInfo.csgn_chrg_pay_rate" label="委托人手续费支付比例" maxlength="10" required="true" dataType="Rate" onchange="checkCsgnChrgPayRate(),checkCsgnRate()"/>
			<emp:text id="IqpCsgnLoanInfo.debit_chrg_pay_rate" label="借款人手续费支付比例" maxlength="10" required="true" dataType="Rate" onchange="checkDebitChrgPayRate(),checkDebitRate()"/> 
			<emp:text id="IqpCsgnLoanInfo.csgn_inner_dep_no" label="委托人内部存款账号"  maxlength="40" required="true" /> 
			<emp:text id="IqpCsgnLoanInfo.csgn_inner_dep_name" label="委托人内部存款账户名" maxlength="100" required="true" readonly="true" />  
		</emp:gridLayout> 
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
