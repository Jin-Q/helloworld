<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetPrdEval._checkAll()){
			IqpAssetPrdEval._toForm(form);
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
						alert("保存成功!");
					}else {
						alert("保存异常!");
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

	function doReturn(){
		//window.close();
		//window.parent.location.reload();
		history.go(-1);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpAssetPrdEvalRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetPrdEvalGroup" maxColumn="2" title="产品评级信息">
			<emp:text id="IqpAssetPrdEval.prd_id" label="产品代码" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpAssetPrdEval.eval_org" label="评级机构" maxlength="20" required="false" />
			<emp:text id="IqpAssetPrdEval.cdt_eval" label="信用评级" maxlength="10" required="false" />
			<emp:date id="IqpAssetPrdEval.eval_date" label="评级日期" required="false" dataType="Date" />
			<emp:text id="IqpAssetPrdEval.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
