<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(AccAssetTrans._checkAll()){
			AccAssetTrans._toForm(form);
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
						var url = '<emp:url action="queryAccAssetTransList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("新增异常!"); 
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
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addAccAssetTransRecord.do" method="POST">
		
		<emp:gridLayout id="AccAssetTransGroup" title="资产台账" maxColumn="2">
			<emp:text id="AccAssetTrans.bill_no" label="借据编号" maxlength="40" required="true" />
			<emp:text id="AccAssetTrans.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccAssetTrans.asset_type" label="资产类型" maxlength="5" required="false" />
			<emp:text id="AccAssetTrans.rebuy_date" label="赎回日期" maxlength="10" required="false" />
			<emp:text id="AccAssetTrans.acc_status" label="资产状态" maxlength="5" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

