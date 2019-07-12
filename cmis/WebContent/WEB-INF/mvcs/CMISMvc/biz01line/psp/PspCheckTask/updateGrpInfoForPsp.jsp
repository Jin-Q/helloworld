<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
 function doSave(){
	if(PspCheckAnaly._checkAll()){
		var form = document.getElementById("submitForm");
		PspCheckAnaly._toForm(form);
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
					window.location.reload();
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

 function doReset(){
		page.dataGroups.PspCheckAnalyGroup.reset();
 };
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateGrpInfoRecord.do" method="POST">	
		<emp:gridLayout id="PspCheckAnalyGroup" title="集团信息情况" maxColumn="2">
			<emp:textarea id="PspCheckAnaly.grp_member_infos" label="集团客户整体及各成员企业的基本情况、成员构成与股权结构的检查" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_intra_grouptrans" label="报告期内该集团的内部关联交易情况" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_major_events" label="报告期内集团客户的发展及内部重大事项" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_credit_actualsituation" label="报告期内我行对该集团客户提供的各类授信及实际发生情况" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_biz_circumstance" label="集团整体经营情况、集团整体财务情况" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_compre_assessment" label="对该集团的综合评价及今后授信策略的建议" maxlength="2000" required="true" />
			<emp:text id="PspCheckAnaly.task_id" label="集团任务号" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="save" label="保存" />
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
