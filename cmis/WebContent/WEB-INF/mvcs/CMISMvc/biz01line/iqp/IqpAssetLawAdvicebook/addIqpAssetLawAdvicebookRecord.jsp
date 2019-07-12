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
		if(IqpAssetLawAdvicebook._checkAll()){
			IqpAssetLawAdvicebook._toForm(form);
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
						var url = '<emp:url action="queryIqpAssetLawAdvicebookList.do"/>';
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
	
	<emp:form id="submitForm" action="addIqpAssetLawAdvicebookRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetLawAdvicebookGroup" title="法律意见书（资产证券化）" maxColumn="2">
			<emp:text id="IqpAssetLawAdvicebook.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpAssetLawAdvicebook.law_office" label="律师事务所" maxlength="80" required="false" />
			<emp:text id="IqpAssetLawAdvicebook.lawer" label="律师" maxlength="80" required="false" />
			<emp:text id="IqpAssetLawAdvicebook.advice_date" label="意见提供日期" maxlength="10" required="false" />
			<emp:text id="IqpAssetLawAdvicebook.advice_memo" label="意见摘要" maxlength="200" required="false" />
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

