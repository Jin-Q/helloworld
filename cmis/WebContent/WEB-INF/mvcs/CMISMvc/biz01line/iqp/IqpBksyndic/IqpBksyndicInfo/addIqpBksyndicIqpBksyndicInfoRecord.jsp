<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表添加记录页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css"> 
.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
.emp_field_input {
border: 1px solid #b7b7b7;
text-align:left;
width:200px;
}
</style>
<script type="text/javascript">
  function checkRate(){
	var serno = IqpBksyndicInfo.serno._getValue();
	var amt_rate = IqpBksyndicInfo.prtcpt_amt_rate._getValue();
	if(serno == null || amt_rate == null || serno == "" || amt_rate == ""){
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
				alert("总金额比例超过100%，请重新确认！");
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

	var url = '<emp:url action="checkIqpBksyndicIqpBksyndicInfo.do"/>?serno='+serno+'&prtcpt_amt_rate='+amt_rate;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
 };

 function getOrgNo(data){
	 IqpBksyndicInfo.prtcpt_org_no._setValue(data.bank_no._getValue());
	 IqpBksyndicInfo.prtcpt_org_name._setValue(data.bank_name._getValue());
 };

 function checkRole(){
	var role = IqpBksyndicInfo.prtcpt_role._getValue();
	var serno = IqpBksyndicInfo.serno._getValue();
    if(role == "01"){//主办行
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
    				alert("已存在主办行!");
    				IqpBksyndicInfo.prtcpt_role._setValue("");
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
    	var url = '<emp:url action="checkRole.do"/>?serno='+serno+'&prtcpt_role='+role;
    	url = EMPTools.encodeURI(url);
    	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    }
 };


 function doSave(){
		var form = document.getElementById("submitForm");
		if(IqpBksyndicInfo._checkAll()){
			IqpBksyndicInfo._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var message = jsonstr.message;
					if(flag == "success"){
						alert("保存成功!");
						window.opener.location.reload();
						window.opener.parent.location.reload();
						window.close();
					}else {
						alert(message);
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
			return;
		}
	};
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addIqpBksyndicIqpBksyndicInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpBksyndicInfoGroup" title="银团信息" maxColumn="2">
			<emp:text id="IqpBksyndicInfo.serno" label="业务编号" hidden="true" maxlength="40" required="flase"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpBksyndicInfo.prtcpt_org_no" label="参与行行号" url="getPrdBankInfoPopList.do?status=1" buttonLabel="选择" returnMethod="getOrgNo" required="true" />  
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpBksyndicInfo.prtcpt_org_name" label="参与行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_input2"/>  
			<emp:select id="IqpBksyndicInfo.prtcpt_role" label="参与角色" required="true" dictname="STD_PART_ORG_TYPE" onblur="checkRole()" /> 
			<emp:select id="IqpBksyndicInfo.prtcpt_curr" label="参与币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpBksyndicInfo.prtcpt_amt_rate" label="参与金额比例" maxlength="10" required="true" dataType="Percent" onblur="checkRate()"/>
			<emp:text id="IqpBksyndicInfo.prtcpt_amt" label="参与金额" maxlength="18" required="true" dataType="Currency" colSpan="2"/> 
			<emp:text id="IqpBksyndicInfo.agent_int_acct_no" label="代收利息账号" maxlength="40" required="true" dataType="Acct" cssElementClass="emp_field_input"/>
			<emp:text id="IqpBksyndicInfo.agent_int_acct_name" label="代收利息账户名" maxlength="80" required="true" />
			<emp:text id="IqpBksyndicInfo.bank_acct_no" label="银行账号" maxlength="40" required="true" dataType="Acct" cssElementClass="emp_field_input"/>
			<emp:text id="IqpBksyndicInfo.bank_acct_name" label="银行账户名" maxlength="100" required="true" />
			<emp:text id="IqpBksyndicInfo.pk1" label="PK1" hidden="true" maxlength="40" required="false" />
			<emp:select id="IqpBksyndicInfo.is_this_org" label="是否本行" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
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
