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
		if(IqpAssetIssueInfo._checkAll()){
			IqpAssetIssueInfo._toForm(form);
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
						var url = '<emp:url action="queryIqpAssetIssueInfoList.do"/>';
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
	
	<emp:form id="submitForm" action="addIqpAssetIssueInfoRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetIssueInfoGroup" title="封包/发行管理" maxColumn="2">
			<emp:text id="IqpAssetIssueInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpAssetIssueInfo.act_issue_type" label="业务类型" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueInfo.act_issue_date" label="实际发行日期" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueInfo.act_issue_amt" label="实际发行总量（万元）" maxlength="16" required="false" />
			<emp:text id="IqpAssetIssueInfo.base_date" label="基准日（起息日）" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueInfo.end_date" label="法定到期日期" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueInfo.fee_cal_mode" label="服务费计算方式" maxlength="10" required="false" />
			<emp:text id="IqpAssetIssueInfo.fee_rate" label="服务费率" maxlength="16" required="false" />
			<emp:text id="IqpAssetIssueInfo.fee_min" label="服务费下限（元）" maxlength="16" required="false" />
			<emp:text id="IqpAssetIssueInfo.cont_no" label="合同编号" maxlength="40" required="false" />
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

