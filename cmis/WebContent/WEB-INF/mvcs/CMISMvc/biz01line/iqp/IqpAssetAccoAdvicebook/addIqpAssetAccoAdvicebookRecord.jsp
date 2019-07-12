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
		if(IqpAssetAccoAdvicebook._checkAll()){
			IqpAssetAccoAdvicebook._toForm(form);
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
						var url = '<emp:url action="queryIqpAssetAccoAdvicebookList.do"/>';
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
	
	<emp:form id="submitForm" action="addIqpAssetAccoAdvicebookRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetAccoAdvicebookGroup" title="会计意见书（资产证券化）" maxColumn="2">
			<emp:text id="IqpAssetAccoAdvicebook.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpAssetAccoAdvicebook.acco_advice" label="会计出表意见" maxlength="10" required="false" />
			<emp:text id="IqpAssetAccoAdvicebook.advice_date" label="意见提供日期" maxlength="10" required="false" />
			<emp:text id="IqpAssetAccoAdvicebook.advice_memo" label="会计意见摘要" maxlength="200" required="false" />
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

