<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	function doSaveRecord(){
		var form = document.getElementById("submitForm");
		IqpExpInfo._checkAll();
		if(IqpExpInfo._checkAll()){
			IqpExpInfo._toForm(form);
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
						alert("保存成功！");
						window.close();
					}else {
						alert("保存失败！");
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
			return false;
		}
	}

	function doReturn(){
		window.close();
		}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpExpInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpExpInfoGroup" maxColumn="2" title="快递信息">
			<emp:text id="IqpExpInfo.express_no" label="快递单号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpExpInfo.express_cprt" label="快递公司" maxlength="80" required="true" />
			<emp:text id="IqpExpInfo.invc_no" label="发票号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpExpInfo.invc_amt" label="发票金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:date id="IqpExpInfo.invc_date" label="开票日期"   required="true" colSpan="2"/>
			<emp:date id="IqpExpInfo.start_date" label="快递发出日期"   required="true" />
			<emp:date id="IqpExpInfo.receive_date" label="快递接收日期"   required="true" />
			<emp:textarea id="IqpExpInfo.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			<emp:text id="IqpExpInfo.input_id" label="登记人" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpExpInfo.input_br_id" label="登记机构" maxlength="30" required="false" hidden="true"/>
			<emp:text id="IqpExpInfo.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpExpInfo.po_no" label="池编号" maxlength="30" required="true"  hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="saveRecord" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
