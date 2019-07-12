<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn(){
		//var serno = PubBailInfo.serno._getValue();   
		//var url = '<emp:url action="queryPubBailInfo_jointList.do"/>?serno='+serno; 
		//url = EMPTools.encodeURI(url);  
		//window.location=url;
		window.close();
	}
	
	/*--user code begin--*/
	//-------------通过账号获取在我行的保证金信息------------
    function onload(){
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
  	  					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.TxnInstCd;
  	  					var OPEN_ACCT_BRANCH_ID_displayname = jsonstr.BODY.TxnInstCd_displayname;
  	  					var ACCT_STATUS = jsonstr.BODY.AcctSt;
  	  				    var BAIL_ACCT_GL_CODE = jsonstr.BODY.AcctSeqNo;//增加科目号
	  					var C_INTERBANK_ID = jsonstr.BODY.C_INTERBANK_ID;
						PubBailInfo.bail_acct_name._setValue(GUARANTEE_ACCT_NAME);
						PubBailInfo.bail_acct_gl_code._setValue(BAIL_ACCT_GL_CODE);
						PubBailInfo.cur_type._setValue(CCY);
						PubBailInfo.amt._setValue(AMT);
						PubBailInfo.rate._setValue(parseFloat(INT_RATE)/100);
						if(INTER_FLT_RATE == "0.0"){
							PubBailInfo.up_rate._obj.element.value=0.00;
	  					}else{
	  					    PubBailInfo.up_rate._setValue(parseFloat(INTER_FLT_RATE)/100);
	  	  				}
						
						PubBailInfo.bail_type._setValue(GUARANTEE_TYPE);
						PubBailInfo.dep_term._setValue(TERM);
						PubBailInfo.open_org._setValue(OPEN_ACCT_BRANCH_ID);
						PubBailInfo.open_org_displayname._setValue(OPEN_ACCT_BRANCH_ID_displayname);
						PubBailInfo.interbank_id._setValue(C_INTERBANK_ID);
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:gridLayout id="PubBailInfoGroup" title="保证金信息表" maxColumn="2">
			<emp:text id="PubBailInfo.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>			
			<emp:text id="PubBailInfo.cus_id" label="客户码" maxlength="40" required="false" readonly="true" hidden="true"/>			
			<emp:text id="PubBailInfo.bail_acct_no" label="保证金账户账号" maxlength="40" required="true" />
			<emp:text id="PubBailInfo.bail_acct_name" label="保证金账户名称" maxlength="80" required="true" readonly="true"/>
			<emp:select id="PubBailInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
			<emp:text id="PubBailInfo.amt" label="账户余额" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="PubBailInfo.rate" label="利率" maxlength="10" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly" />			
			<emp:text id="PubBailInfo.up_rate" label="上浮比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="PubBailInfo.bail_type" label="保证金类型" required="false" dictname="STD_PUB_BAIL_TYPE" readonly="true"/>
			<emp:select id="PubBailInfo.dep_term" label="存期" required="false" dictname="STD_BAIL_DEP_TERM" readonly="true" />
			<emp:text id="PubBailInfo.open_org_displayname" label="开户机构" required="false" readonly="true" />
		    <emp:select id="PubBailInfo.bail_status" label="状态" dictname="STD_BAIL_INFO_STATUS" required="false" readonly="true"/>
		    <emp:text id="PubBailInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/>
		    <emp:text id="PubBailInfo.cus_id_displayname" label="客户名称"  required="true" readonly="true" hidden="true"/>
		   <emp:text id="PubBailInfo.open_org" label="开户机构" required="false" readonly="true" hidden="true"/>
		   <emp:text id="PubBailInfo.bail_acct_gl_code" label="科目号" maxlength="20" hidden="true"/>
		    <emp:text id="PubBailInfo.interbank_id" label="银联行号" maxlength="40" hidden="true"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
