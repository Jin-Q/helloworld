<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doBack() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComAcc.cus_id._obj.element.value;
		var paramStr="CusComAcc.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComAccList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doUpdate() {
	    var form = document.getElementById("submitForm");
	    var result = CusComAcc._checkAll();
	    if(result){
	    	CusComAcc._toForm(form);
	        toSubmitForm(form);
	    }//else alert("请输入必填项！");
	};
	
	function toSubmitForm(form){
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
                         var paramStr="CusComAcc.cus_id="+CusComAcc.cus_id._obj.element.value;
                         var url = '<emp:url action="queryCusComAccList.do"/>&'+paramStr;
                         url = EMPTools.encodeURI(url);
                         window.location = url;
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
<body class="page_content">
	<emp:form id="submitForm" action="updateCusComAccRecord.do" method="POST">
		<emp:gridLayout id="CusComAccGroup" maxColumn="2" title="结算账户登记">
			<emp:text id="CusComAcc.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusComAcc.cus_name" label="客户名称" maxlength="80" required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_no" label="结算账户帐号" maxlength="32" required="true" readonly="true" />
			<emp:text id="CusComAcc.acc_name" label="账户名称" maxlength="80" required="true" readonly="true"/>
			<emp:date id="CusComAcc.acc_date" label="账户开户日期" required="false" />
			<emp:text id="CusComAcc.acc_open_org" label="开户机构" maxlength="20" required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_open_orgname" label="开户机构名称" maxlength="80" required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_org" label="核算机构" maxlength="20" required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_orgname" label="核算机构名称" maxlength="80" required="false" readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="update" label="保存"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>