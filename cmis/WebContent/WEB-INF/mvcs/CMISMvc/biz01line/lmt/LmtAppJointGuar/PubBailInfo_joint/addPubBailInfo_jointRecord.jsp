<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	/**add by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String modiflg = "";
    if(context.containsKey("modiflg")){
    	modiflg = (String)context.getDataValue("modiflg");
    }
    String modify_rel_serno = "";
    if(context.containsKey("modify_rel_serno")){
    	modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
    }
    /**add by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doOnload(){
		var serno = "${context.serno}";
		var cus_id = '${context.cus_id}';
		var cont_no = '${context.cont_no}';
		PubBailInfo.bail_acct_no._obj.addOneButton('uniquCheck','获 取', getBailNo);
	}
	function setCusInfo(){
		
	}
	function doReturn(){
		//window.close();
		//window.parent.location.reload();
		history.go(-1);
	}
	//确定保存
	function doSure(){
		if(!PubBailInfo._checkAll()){
			return ;
		}
		var handSuc = function(o){
			if(o.responseText !== undefined) {
				try { var jsonstr = eval("("+o.responseText+")"); } 
					catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "suc"){
					alert("保存成功!");
					var serno = "${context.serno}";
					var cus_id = "${context.cus_id}";
					var cont_no = '${context.cont_no}';
					/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
					var modiflg = '<%=modiflg%>';
					var modify_rel_serno = '<%=modify_rel_serno%>';
					if(modiflg =="yes"){
						var url = '<emp:url action="queryPubBailInfo_jointList.do"/>?op=update&cus_id='+cus_id+'&serno='+serno+'&cont_no='+cont_no+"&modify_rel_serno="+modify_rel_serno+"&menuIdTab=queryIqpLoanApp&subMenuId=queryPubBailInfo_jointList&modiflg=yes"; 
					}else{
						var url = '<emp:url action="queryPubBailInfo_jointList.do"/>?op=update&cus_id='+cus_id+'&serno='+serno+'&cont_no='+cont_no; 
					}
					/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
					url = EMPTools.encodeURI(url);
					window.location = url; 
				}
			}
		};
	    var handFail = function(o){
	    };
	    var callback = {
	    	success:handSuc,
	    	failure:handFail
	    };
	    var form = document.getElementById("submitForm");
		PubBailInfo._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action, callback,postData);
	};

	//-------------通过账号获取在我行的保证金信息------------
    function getBailNo(){
  		 var acctNo = PubBailInfo.bail_acct_no._getValue();
  	        if(acctNo == null || acctNo == ""){
  				alert("请先输入保证金账号信息！");
  				return;
  	        }
  			var handleSuccess = function(o){
  				if(o.responseText !== undefined){
  					try {
  						var jsonstr = eval("("+o.responseText+")");
  					} catch(e) {
  						alert("Parse jsonstr1 define error!" + e.message);
  						return;
  					}
  					var flag = jsonstr.flag;
  					var retMsg = jsonstr.mes;
  					if(flag == "success"){
  						var GUARANTEE_ACCT_NO = jsonstr.BODY.AcctNoCrdNo;
  	  					var GUARANTEE_ACCT_NAME = jsonstr.BODY.AcctNm;
  	  					var CCY = jsonstr.BODY.Ccy;
  	  					var AMT = jsonstr.BODY.AcctBal;
  	  					var GUARANTEE_TYPE = jsonstr.BODY.PdTp;
  	  					var INT_RATE = jsonstr.BODY.BnkInnrIntRate;
  	  					var INTER_FLT_RATE = jsonstr.BODY.FltIntRate;
  	  					var TERM = jsonstr.BODY.Trm;
  	  					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.AcctBlngInstNo;
  	  					var OPEN_ACCT_BRANCH_ID_displayname = jsonstr.BODY.AcctBlngInstNo_displayname;
  	  					var ACCT_STATUS = jsonstr.BODY.AcctSt;
  	  				    var BAIL_ACCT_GL_CODE = jsonstr.BODY.AcctSeqNo;//增加科目号
  	  				    var C_INTERBANK_ID = jsonstr.BODY.C_INTERBANK_ID;
  						PubBailInfo.bail_acct_name._setValue(GUARANTEE_ACCT_NAME);
  						PubBailInfo.bail_acct_gl_code._setValue(BAIL_ACCT_GL_CODE);
  						PubBailInfo.cur_type._setValue(CCY);
  						PubBailInfo.amt._setValue(AMT);
  						PubBailInfo.bail_type._setValue(GUARANTEE_TYPE);
  						PubBailInfo.dep_term._setValue(TERM);
  						PubBailInfo.open_org._setValue(OPEN_ACCT_BRANCH_ID);
  						PubBailInfo.open_org_displayname._setValue(OPEN_ACCT_BRANCH_ID_displayname);
  						PubBailInfo.interbank_id._setValue(C_INTERBANK_ID);
  						if(INT_RATE == ""){
  							PubBailInfo.rate._setValue("");
  	  					}else{
  	  					    PubBailInfo.rate._setValue(parseFloat(INT_RATE)/100);
  	  	  	  			}
  						if(INTER_FLT_RATE == "0.0"){
  							PubBailInfo.up_rate._obj.element.value=0.00;
  	  					}else if(INTER_FLT_RATE == ""){
  	  					    PubBailInfo.up_rate._setValue("");
  	  	  				}else{
  	  	  				    PubBailInfo.up_rate._setValue(parseFloat(INTER_FLT_RATE)/100);
  	  	  	  			}
  						//PubBailInfo.bail_status._setValue(ACCT_STATUS);
  					}else {
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
  			var url = '<emp:url action="getPubBailInfoForEsb.do"/>?bail_acct_no='+acctNo+'&service_code=30130001&sence_code=01';	
  			url = EMPTools.encodeURI(url);
  			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };

    function doCheckBailAcctNo(){
		var acctNo = PubBailInfo.bail_acct_no._getValue();
		if(acctNo == null || acctNo == ""){
			alert("请先输入保证金账号信息！");
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
					alert("已存在此保证金账号");
					PubBailInfo.bail_acct_no._setValue("");
					PubBailInfo.bail_acct_name._setValue("");
					PubBailInfo.bail_acct_gl_code._setValue("");
					PubBailInfo.cur_type._setValue("");
					PubBailInfo.rate._setValue("");
					PubBailInfo.amt._setValue("");
					PubBailInfo.up_rate._setValue("");
					PubBailInfo.bail_type._setValue("");
					PubBailInfo.dep_term._setValue("");
					PubBailInfo.open_org._setValue("");
					PubBailInfo.open_org_displayname._setValue("");
					//PubBailInfo.bail_status._setValue("");
				}else{
				    doSure();
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
		/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		var modiflg = '<%=modiflg%>';
		var modify_rel_serno = '<%=modify_rel_serno%>';
		var url = '<emp:url action="checkPubBailInfo.do"/>?cont_no=${context.cont_no}&serno=${context.serno}&bail_acct_no='+acctNo+"&modiflg="+modiflg+"&modify_rel_serno="+modify_rel_serno;
		/**modified by lisj 2015-8-31 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="addPubBailInfo_jointRecord.do?modiflg=${context.modiflg}&modify_rel_serno=${context.modify_rel_serno}" method="POST">
		
		<emp:gridLayout id="PubBailInfoGroup" title="保证金信息表" maxColumn="2">
			<emp:text id="PubBailInfo.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="PubBailInfo.cus_id" label="客户码" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="PubBailInfo.bail_acct_no" label="保证金账号" maxlength="40" required="true"/>
			<emp:text id="PubBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="true" readonly="true"/>
			<emp:select id="PubBailInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
			<emp:text id="PubBailInfo.amt" label="账户余额" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="PubBailInfo.rate" label="利率" maxlength="10" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly" />			
			<emp:text id="PubBailInfo.up_rate" label="上浮比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="PubBailInfo.bail_type" label="保证金类型" required="false" dictname="STD_PUB_BAIL_TYPE" readonly="true"/>
			<emp:select id="PubBailInfo.dep_term" label="存期"  required="false" dictname="STD_BAIL_DEP_TERM" readonly="true" />
			<emp:text id="PubBailInfo.open_org_displayname" label="开户机构" required="false" readonly="true" />
			<emp:select id="PubBailInfo.bail_status" label="状态" dictname="STD_BAIL_INFO_STATUS" readonly="true" required="false" defvalue="0" />
			<emp:text id="PubBailInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/> 
			<emp:text id="PubBailInfo.cus_id_displayname" label="客户名称"  required="false" readonly="false" hidden="true"/>
		    <emp:text id="PubBailInfo.open_org" label="开户机构" required="false" hidden="true"/> 
		    <emp:text id="PubBailInfo.bail_acct_gl_code" label="科目号" maxlength="20" hidden="true"/>
		    
		    <emp:text id="PubBailInfo.interbank_id" label="银联行号" maxlength="40" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="checkBailAcctNo" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

