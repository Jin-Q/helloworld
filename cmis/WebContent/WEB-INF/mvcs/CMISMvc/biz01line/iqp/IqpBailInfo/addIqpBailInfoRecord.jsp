<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String serno = request.getParameter("serno");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

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
	
	function doSave(){
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
					alert("新增成功!");
					var url = '<emp:url action="queryIqpBailInfoList.do"/>?serno='+'<%=serno%>'; 
					url = EMPTools.encodeURI(url);
					window.location = url; 
				}else {
					alert("新增失败!"); 
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
	};

	//-----------开户机构回调函数-----------
	function getOrgID(data){
		IqpBailInfo.open_org._setValue(data.organno._getValue());
		IqpBailInfo.open_org_displayname._setValue(data.organname._getValue());
	};	
	/*--user code end--*/
	
	
</script>
</head>
<body class="page_content" onload="doOnLoad();" >
	<emp:form id="submitForm" action="addIqpBailInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpBailInfoGroup" title="保证金信息表" maxColumn="2">
			<emp:text id="IqpBailInfo.serno" label="业务编号" colSpan="2" defvalue="<%=serno %>" maxlength="40" hidden="true" required="false"/>
			<emp:text id="IqpBailInfo.bail_acct_no" label="保证金账号"  required="true" />
			<emp:text id="IqpBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="true" readonly="false"/>
			<emp:select id="IqpBailInfo.cur_type" label="币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="IqpBailInfo.rate" label="利率" maxlength="10" required="true" dataType="Rate" readonly="false"/>
			<emp:text id="IqpBailInfo.up_rate" label="上浮比例" maxlength="10" required="true" dataType="Percent" readonly="false"/>
			<emp:select id="IqpBailInfo.bail_type" label="保证金类型" required="true" dictname="STD_ZB_BAIL_STATUS" readonly="false"/>
			<emp:text id="IqpBailInfo.dep_term" label="存期" maxlength="10" required="true" dataType="Int" readonly="false"/>
			<emp:text id="IqpBailInfo.open_org_displayname" label="开户机构"  required="true" readonly="false"/>
			<emp:text id="IqpBailInfo.open_org" label="开户机构" required="false" hidden="false"/>
			<emp:text id="IqpBailInfo.cus_id" label="客户码" maxlength="40" hidden="false" required="false" />
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

