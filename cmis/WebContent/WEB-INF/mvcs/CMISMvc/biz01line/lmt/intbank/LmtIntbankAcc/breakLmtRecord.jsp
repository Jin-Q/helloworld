<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshLmtIntbankDetail() {
		LmtIntbankAcc_tabs.tabs.LmtIntbankDetail_tab.refresh();
	}
	
	function doReturn(){		
		var url = '<emp:url action="queryLmtIntbankAccList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function doBreak(){
		if(LmtIntbankAcc._checkAll()){
			var form = document.getElementById("submitForm");
			LmtIntbankAcc._toForm(form);
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
						var url = '<emp:url action="queryLmtIntbankAccList.do"/>?';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("终止日期前没有可以终止的授信！！");
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
	}

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="breakLmtIntbankAccRecord.do" method="POST">
		<emp:gridLayout id="LmtIntbankAccGroup" title="同业客户授信台帐" maxColumn="2">
			<emp:date id="LmtIntbankAcc.start_date" label="授信起始日期" required="false" hidden="true"/>
			<emp:date id="LmtIntbankAcc.end_date" label="授信到期日期" required="false" hidden="true"/>
			<emp:date id="LmtIntbankAcc.break_date" label="终止日期" required="true"/>
		</emp:gridLayout>	
		<div align="center">
			<emp:button id="break" label="终止授信"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
