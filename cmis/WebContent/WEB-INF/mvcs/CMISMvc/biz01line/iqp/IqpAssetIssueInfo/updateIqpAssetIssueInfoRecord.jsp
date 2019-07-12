<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if("view".equals(op)){
			request.setAttribute("canwrite","");
		}    
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

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
						alert("生成结果成功!");
					}else {
						alert("生成结果异常!");
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

	function checkEndDate(){
		var end_date=IqpAssetIssueInfo.end_date._getValue();
		var openDay='${context.OPENDAY}';
		if(end_date){
			var flag = CheckDate1BeforeDate2(openDay,end_date);
			if(!flag){
				alert("【法定到期日期】必须大于当前营业日期！");
				IqpAssetIssueInfo.end_date._setValue("");
				return false;
			}

			var act_issue_date = IqpAssetIssueInfo.act_issue_date._getValue();
			if(act_issue_date){
				flag = CheckDate1BeforeDate2(act_issue_date,end_date);
				if(!flag){
					alert("【法定到期日期】必须大于【实际发行日期】！");
					IqpAssetIssueInfo.end_date._setValue("");
					return false;
				}
			}
	     }

	     
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpAssetIssueInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetIssueInfoGroup" maxColumn="2" title="封包/发行管理">
			<emp:text id="IqpAssetIssueInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" defvalue="${context.serno}" />
			<emp:text id="IqpAssetIssueInfo.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" defvalue="${context.cont_no}"/>
			<emp:select id="IqpAssetIssueInfo.act_issue_type" label="业务类型" required="true" dictname="STD_ZB_ACT_ISSUE_TYPE"/>
			<emp:date id="IqpAssetIssueInfo.act_issue_date" label="实际发行日期" required="true" defvalue="${context.issue_date}"/>
			<emp:text id="IqpAssetIssueInfo.act_issue_amt" label="实际发行总量（万元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:date id="IqpAssetIssueInfo.base_date" label="基准日（起息日）" required="true" />
			<emp:date id="IqpAssetIssueInfo.end_date" label="法定到期日期" required="true" onblur="checkEndDate()"/>
			<emp:text id="IqpAssetIssueInfo.fee_cal_mode" label="服务费计算方式" maxlength="60" required="true"/>
			<emp:text id="IqpAssetIssueInfo.fee_rate" label="服务费率" maxlength="16" required="true" dataType="Percent"/>
			<emp:text id="IqpAssetIssueInfo.fee_min" label="服务费下限（元）" maxlength="16" required="true" dataType="Currency"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="sub" label="生成结果" op="update"/>
			<emp:actButton id="reset" label="取消" op="update"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
