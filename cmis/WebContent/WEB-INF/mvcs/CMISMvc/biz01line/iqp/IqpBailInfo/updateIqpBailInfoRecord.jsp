<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad(){
		IqpBailInfo.bail_acct_no._obj.addOneButton("acctNo","获取",getAcctNo);
	};
	//-------------获取保证金账号信息-------------
	function getAcctNo(){
		alert("获取保证金接口待开发！");
	};
	
	function doReturn() {
		var serno = IqpBailInfo.serno._getValue();
		var url = '<emp:url action="queryIqpBailInfoList.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};	
	//--------保存事件操作---------
	function doSave(){
		var serno = IqpBailInfo.serno._getValue();
		var form = document.getElementById("submitForm");
		var result = IqpBailInfo._checkAll();
		if(!result){
			return;
		}
		IqpBailInfo._toForm(form); 
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
					var url = '<emp:url action="queryIqpBailInfoList.do"/>?serno='+serno; 
					url = EMPTools.encodeURI(url);
					window.location = url; 
				}else {
					alert("修改失败!"); 
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
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	
	<emp:form id="submitForm" action="updateIqpBailInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpBailInfoGroup" maxColumn="2" title="保证金信息表">
			<emp:text id="IqpBailInfo.serno" label="业务编号" colSpan="2" maxlength="40" hidden="true" required="false"/>
			<emp:text id="IqpBailInfo.bail_acct_no" label="保证金账号"  required="true" />
			<emp:text id="IqpBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="true" readonly="true"/>
			<emp:select id="IqpBailInfo.cur_type" label="币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="IqpBailInfo.rate" label="利率" maxlength="10" required="true" dataType="Rate" readonly="true"/>
			<emp:text id="IqpBailInfo.up_rate" label="上浮比例" maxlength="10" required="true" dataType="Percent" readonly="true"/>
			<emp:select id="IqpBailInfo.bail_type" label="保证金类型" required="true" dictname="STD_ZB_BAIL_STATUS" readonly="true"/>
			<emp:text id="IqpBailInfo.dep_term" label="存期" maxlength="10" required="true" dataType="Int" readonly="true"/>
			<emp:text id="IqpBailInfo.open_org_displayname" label="开户机构"  required="true" readonly="true"/>
			<emp:text id="IqpBailInfo.open_org" label="开户机构" required="false" hidden="true"/>
			<emp:text id="IqpBailInfo.cus_id" label="客户码" maxlength="40" hidden="true" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="svae" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
