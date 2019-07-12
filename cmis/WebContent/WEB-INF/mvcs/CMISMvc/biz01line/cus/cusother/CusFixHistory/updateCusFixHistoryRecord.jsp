<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doOk(){
		//异步校验 
	 	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="suc"){
					alert("校验通过");
					window.returnValue = flag;
					window.close();
			     }else {
					alert("授权人或授权码不正确或校验已过期/失效！");
			     }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		//设置form
		var form = document.getElementById("submitForm");
		var result = CusFixHistory._checkAll();
		if(result){
			CusFixHistory._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}

	function doColseThis(){
		window.close();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusFixHistoryRecord.do" method="POST">
		<emp:gridLayout id="CusFixHistoryGroup" maxColumn="2" title="客户信息修改授权校验">
			<emp:text id="CusFixHistory.serno" label="流水号" maxlength="32" required="false" readonly="true" hidden="true" colSpan="2"/>
			<emp:text id="CusFixHistory.cus_id" label="客户码" maxlength="20" required="true" readonly="true"/>
			<emp:text id="CusFixHistory.cus_name" label="客户名称" maxlength="80" required="true" readonly="true"/>
			<emp:text id="CusFixHistory.auth_id" label="授权人" maxlength="20" required="true" />
			<emp:password id="CusFixHistory.checkcode" label="授权码" maxlength="32" required="true" />
			<emp:select id="CusFixHistory.update_type" label="修改类型" required="true" colSpan="2" dictname="STD_CUS_UPDATE_TYPE" />
			<emp:textarea id="CusFixHistory.memo" label="备注" maxlength="400" required="true" rows="2" colSpan="2"/>
			<emp:date id="CusFixHistory.update_date" label="修改日期" required="true" dataType="Date" readonly="true" defvalue="$OPENDAY"/>
			<emp:text id="CusFixHistory.update_id" label="修改人" maxlength="20" required="true"  readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusFixHistory.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" readonly="true" defvalue="$organNo"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="ok" label="确定" />
			<emp:button id="colseThis" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
