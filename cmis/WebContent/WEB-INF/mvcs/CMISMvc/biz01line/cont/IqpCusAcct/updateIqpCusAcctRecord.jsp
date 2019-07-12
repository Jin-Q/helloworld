<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String is_agent_disc="";
	if(context.containsKey("is_agent_disc")){
		is_agent_disc =(String)context.getDataValue("is_agent_disc");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_input2{
border:1px solid #b7b7b7;
width:600px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	var acct_no_all = "";//Edited by FCL 20140412
	function load(){
		IqpCusAcct.acct_no._obj.addOneButton("acctNo","获取",getAcctNo);
		//IqpCusAcct.opac_org_no._obj.addOneButton("orgNo","获取",getOrgNoAct);
		acct_attr_change();
		changeCurTypeReadOnly();
		changeIsThisOrgAcct();
		acct_no_all = IqpCusAcct.acct_no._getValue();
		/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) start **/
		//var options = IqpCusAcct.acct_attr._obj.element.options;
		//for ( var i = options.length - 1; i >= 0; i--) {
		//	//判断出账号属性，若为"受托支付账号"，去掉选项
		//	if(options[i].value == "04"){
		//	options.remove(i);
		//	}
		//} 
		/** modified by yangzy 20150112 受托支付需求开发(个贷特殊处理) end **/
    };

    function getOrgNoForm(){
		var url = "<emp:url action=''/>";
		url=EMPTools.encodeURI(url);      
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}; 
	
	function doSub(){
		var form = document.getElementById("submitForm");
		var serno = IqpCusAcct.serno._getValue();
		IqpCusAcct._checkAll();
		if(acct_no_all!=IqpCusAcct.acct_no._getValue()){//Edited By FCL 20140412
			alert("账号信息有变动，请重新获取户名");
			return;
	 	}
		if(IqpCusAcct._checkAll()){
			IqpCusAcct._toForm(form); 
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
						alert("修改成功!");
						var url = '<emp:url action="queryIqpCusAcctList.do"/>?serno='+serno+'&is_agent_disc=${context.is_agent_disc}';
						url = EMPTools.encodeURI(url);
						window.location = url; 
					}else {
						alert("发生异常!");
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
		var isThis = IqpCusAcct.is_this_org_acct._getValue();
		if(isThis == 1){
			IqpCusAcct.acct_no._setValue("");
			IqpCusAcct.acct_name._setValue("");
			IqpCusAcct.acct_name._obj._renderReadonly(true);
			IqpCusAcct.opac_org_no._setValue("");
			IqpCusAcct.opan_org_name._setValue("");
			IqpCusAcct.opan_org_name._obj._renderReadonly(true);
		}else {
			IqpCusAcct.acct_no._setValue("");
			IqpCusAcct.acct_name._setValue("");
			IqpCusAcct.acct_name._obj._renderReadonly(false);
			IqpCusAcct.opac_org_no._setValue("");
			IqpCusAcct.opan_org_name._setValue("");
			IqpCusAcct.opan_org_name._obj._renderReadonly(false);
		}
    	
    };
  //-------------通过账号获取在我行的账号信息------------
    function getAcctNo(){
    	var isThis = IqpCusAcct.is_this_org_acct._getValue();
    	var acct_attr = IqpCusAcct.acct_attr._getValue();
    	if(isThis == 1){
    		 var acctNo = IqpCusAcct.acct_no._getValue();
    	        if(acctNo == null || acctNo == ""){
    				alert("请先输入账号信息！");
    				return;
    	        }
    	        acct_no_all = acctNo;//Edited by FCL 20140412
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
    						if(CCY != "CNY" && (acct_attr=="02" || acct_attr=="06")){
                                alert("印花税账号的币种需是人民币!");
                                IqpCusAcct.acct_no._setValue("");
                            }else{
                            	IqpCusAcct.acct_name._setValue(ACCT_NAME);
        						IqpCusAcct.opac_org_no._setValue(OPEN_ACCT_BRANCH_ID);
        						IqpCusAcct.opan_org_name._setValue(OPEN_ACCT_BRANCH_NAME);
        						IqpCusAcct.acct_gl_code._setValue(ACCT_GL_CODE);
        						IqpCusAcct.cur_type._setValue(CCY);
                            }
    					}else {
    						alert(retMsg); 
    						IqpCusAcct.acct_name._setValue("");
    						IqpCusAcct.opac_org_no._setValue("");
    						IqpCusAcct.opan_org_name._setValue("");
    						IqpCusAcct.acct_gl_code._setValue("");
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
    //-------------通过开户行行号获取在我行的开户行信息------------
    function getOrgNo(){
    	var isThis = IqpCusAcct.is_this_org_acct._getValue();
    	if(isThis == 1){
    		var orgNo = IqpCusAcct.opac_org_no._getValue();
   	        if(orgNo == null || orgNo == ""){
   				alert("请先输入账号信息！");
   				return;
   	        }
		}else {
			alert("非本行账户，直接输入开户行信息！");
		}
    };
  //-------------通过账户属性判断是否本行账户------------
    function changeIsThisOrgAcct(){
    	var acct_attr = IqpCusAcct.acct_attr._getValue();
    	//账户属性为“放款账号”、“借款人印花税账号”委托人印花税账号'、“收款收息账号” 时
    	if(acct_attr == "01" || acct_attr == "02" || acct_attr == "06" || acct_attr == "03"){
    		IqpCusAcct.is_this_org_acct._setValue("1");
    		IqpCusAcct.is_this_org_acct._obj._renderReadonly(true);
    		
    		IqpCusAcct.acct_name._obj._renderReadonly(true);
			IqpCusAcct.opac_org_no._obj._renderReadonly(true);
			IqpCusAcct.opan_org_name._obj._renderReadonly(true);

			//贴现代理账户
			if('${context.is_agent_disc}' == "1"){
				IqpCusAcct.is_this_org_acct._setValue("");
	    		IqpCusAcct.is_this_org_acct._obj._renderReadonly(false);
	    		
	    		IqpCusAcct.acct_name._obj._renderReadonly(false);
				IqpCusAcct.opac_org_no._obj._renderReadonly(false);
				IqpCusAcct.opan_org_name._obj._renderReadonly(false);
			}
    		
        }else if(acct_attr == "04" || acct_attr == "05"){
        	//IqpCusAcct.is_this_org_acct._setValue("");
        	IqpCusAcct.is_this_org_acct._obj._renderReadonly(false);

        	IqpCusAcct.acct_name._obj._renderReadonly(false);
			IqpCusAcct.opac_org_no._obj._renderReadonly(false);
			IqpCusAcct.opan_org_name._obj._renderReadonly(false);
        }
    };
    
    //-------------通过账户属性和是否非是否本行账户判断是否币种是否可选------------
	function changeCurTypeReadOnly(){
		var acct_attr = IqpCusAcct.acct_attr._getValue();
    	if(acct_attr == "04"||acct_attr == "05"){
    		var is_this_org_acct = IqpCusAcct.is_this_org_acct._getValue();
        	if(is_this_org_acct == "1" ){  //本行的币种不可选
        		IqpCusAcct.cur_type._obj._renderReadonly(true);
        		//设为人民币默认
        		IqpCusAcct.cur_type._setValue("CNY");
        		
        	}else if(is_this_org_acct == "2" ){ //非本行的时候 币种可以选
        		IqpCusAcct.cur_type._obj._renderReadonly(false);
        	}
    	}else{
    		IqpCusAcct.cur_type._obj._renderReadonly(true);
    		//设为人民币默认
    		IqpCusAcct.cur_type._setValue("CNY");
    	}
	};  
    function getOrgNoAct(){
    	var isThis = IqpCusAcct.is_this_org_acct._getValue();
    	if(isThis == 1){
    		var orgNo = IqpCusAcct.opac_org_no._getValue();
   	        if(orgNo == null || orgNo == ""){
   				alert("请先输入账号信息！");
   				return;
   	        }
   	    	 var url = "<emp:url action='getValueQueryUpOrgEstList.do?popReturnMethod=getOrgNo'/>";
 			url=EMPTools.encodeURI(url);      
       		window.open(url,'newwindow','height=538,width=1024,top=170,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}else {
			alert("非本行账户，直接输入开户行信息！");
		}
    	
    };
    //-------------通过账户属性判断支付金额是否显示------------
    function acct_attr_change(){
    	var acct_attr = IqpCusAcct.acct_attr._getValue();
    	if(acct_attr == "04"){
    		IqpCusAcct.pay_amt._obj._renderHidden(false);
    		IqpCusAcct.pay_amt._obj._renderRequired(true);
        }else {
        	IqpCusAcct.pay_amt._setValue("");
        	IqpCusAcct.pay_amt._obj._renderHidden(true);
        	IqpCusAcct.pay_amt._obj._renderRequired(false);
        }
    };
    //-------------开户行行号pop框选择返回函数-----------
     function getOrgNo(data){
    	IqpCusAcct.opac_org_no._setValue(data.bank_no._getValue());
    	IqpCusAcct.opan_org_name._setValue(data.bank_name._getValue());
    };

    function doBack(){
    	window.history.go(-1);
    };       
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="load()"> 
	
	<emp:form id="submitForm" action="updateIqpCusAcctRecord.do" method="POST">
		<emp:gridLayout id="IqpCusAcctGroup" maxColumn="2" title="账户信息">
		  <emp:text id="IqpCusAcct.pk_id" label="主键" maxlength="40" colSpan="2" required="false" hidden="true"/>
			<emp:text id="IqpCusAcct.serno" label="业务编号" maxlength="40" colSpan="2" required="false" hidden="true"/>    
			<emp:text id="IqpCusAcct.cont_no" label="合同编号" maxlength="40" colSpan="2" required="false" hidden="true"/>
			<emp:select id="IqpCusAcct.acct_attr" label="账户属性" required="true" readonly="true" onchange="acct_attr_change();changeIsThisOrgAcct();changeCurTypeReadOnly();" dictname="STD_ZB_BR_ID_ATTR"/>
			<emp:select id="IqpCusAcct.is_this_org_acct" label="是否本行账户" required="true" dictname="STD_ZX_YES_NO" onchange="isThisOrg();changeCurTypeReadOnly();" />
			<emp:text id="IqpCusAcct.acct_no" label="账号"  required="true"/>
			<emp:text id="IqpCusAcct.acct_name" label="账户名称" maxlength="80" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpCusAcct.opac_org_no" label="开户行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getOrgNo" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpCusAcct.opan_org_name" label="开户行行名" maxlength="100" required="true" colSpan="2" readonly="true" cssElementClass="emp_input2"/>
			<emp:text id="IqpCusAcct.pay_amt" label="支付金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="IqpCusAcct.acct_gl_code" label="科目号" maxlength="20" hidden="true"/>
			<emp:select id="IqpCusAcct.cur_type" label="币种" required="true"  defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
		</emp:gridLayout> 
		
		<div align="center">  
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
