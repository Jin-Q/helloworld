<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>

<script type="text/javascript">
	
	/*--user code begin--*/
	
	function doReturn() {
		var url = '<emp:url action="queryAccOverseeRemindList.do"/>?&remind_flag=${context.remind_flag}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//异步修改数据
	function doUpdate(){
		if(AccOverseeRemind._checkAll()){
			var form = document.getElementById("submitForm");
			AccOverseeRemind._toForm(form);
			var handleSuccess = function(o){
			if(o.responseText !== undefined) {									
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("修改成功！");
						var url = '<emp:url action="queryAccOverseeRemindListMain.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("发生异常！");
					}
			   }
			};
			var callback = {
				success:handleSuccess,
				failure:null
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateAccOverseeRemindRecord.do" method="POST">
	  <emp:tabGroup mainTab="base_tab" id="mainTab" >
	   <emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
		<emp:gridLayout id="AccOverseeRemindGroup" maxColumn="2" title="基本信息">
			<emp:text id="AccOverseeRemind.oversee_agr_no" label="监管协议号" maxlength="32" required="true" readonly="true"/>
			<emp:text id="AccOverseeRemind.mortgagor_id" label="客户码" readonly="true" required="true"/>
			<emp:text id="AccOverseeRemind.mortgagor_id_displayname" label="客户名称" cssElementClass="emp_field_text_input2" required="true" readonly="true" colSpan="2"/>
			<emp:text id="AccOverseeRemind.identy_total" label="货物价值" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="AccOverseeRemind.risk_open_amt" label="敞口金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="AccOverseeRemind.gap_amt" label="缺口金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:textarea id="AccOverseeRemind.memo" label="提交理由" maxlength="250" required="false" colSpan="2" defvalue="质押率接近平仓线"/>	
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="update" label="提交"/>
			<emp:button id="return" label="返回"/>
		</div>
		 </emp:tab>
		 <emp:ExtActTab></emp:ExtActTab>
		</emp:tabGroup>
	</emp:form>
	
</body>
</html>
</emp:page>
