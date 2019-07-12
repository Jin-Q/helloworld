<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsIqpAssetTrans.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAssetTransListList.do"/>?serno='+IqpAssetTransList.serno._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function onload(){
		IqpAssetTransList.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpAssetTransList.cont_no._obj.addOneButton("cont_no","查看",getContForm);
		IqpAssetTransList.bill_no._obj.addOneButton("bill_no","查看",getBillNoForm);
		getAcctNo();
    }
	//-------------通过账号获取在我行的账号信息------------
    function getAcctNo(){
    	var agent_asset_acct = IqpAssetTransList.agent_asset_acct._getValue();
        if(agent_asset_acct == null || agent_asset_acct == ""){
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
				if(flag == "success"){
					IqpAssetTransList.agent_asset_acct_name._setValue(ACCT_NAME);
				}else {
					alert(retMsg); 
					IqpAssetTransList.agent_asset_acct._setValue("");
					IqpAssetTransList.agent_asset_acct_name._setValue("");
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
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:gridLayout id="IqpAssetTransListGroup" title="资产清单" maxColumn="2">
			<emp:text id="IqpAssetTransList.serno" label="业务编号" maxlength="40" required="true" defvalue="${context.serno}" readonly="true" colSpan="2"/>
			<emp:text id="IqpAssetTransList.bill_no" label="借据编号" maxlength="40" required="true" />
			
			<emp:text id="IqpAssetTransList.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransList.cus_id" label="客户编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransList.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetTransList.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetTransList.loan_amt" label="贷款金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransList.loan_balance" label="贷款余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransList.inner_owe_int" label="表内欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransList.out_owe_int" label="表外欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAssetTransList.five_class" label="五级分类" required="false" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="IqpAssetTransList.twelve_cls_flg" label="十二级分类" required="false" readonly="true" dictname="STD_ZB_TWELVE_CLASS"/>
			
			<emp:text id="IqpAssetTransList.trans_rate" label="转让比率" maxlength="16" required="true" dataType="Percent" onchange="calTransAmt()"/>
			<emp:text id="IqpAssetTransList.trans_amt" label="转让金额" maxlength="16" required="true" dataType="Currency" onchange="calTransRate()"/>
			<emp:text id="IqpAssetTransList.agent_asset_acct" label="代理资产资金账号" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="IqpAssetTransList.agent_asset_acct_name" label="代理资产资金账号名" maxlength="80" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			
			<emp:pop id="IqpAssetTransList.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpAssetTransList.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true" readonly="true"/>
		    <emp:text id="IqpAssetTransList.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetTransList.fina_br_id" label="账务机构" hidden="true" />
			
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
