<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function returnInd(data){
		CcrBizModel.index_no._setValue(data.index_no._getValue());
		CcrBizModel.index_name._setValue(data.index_name._getValue());
	}		
	function retCom_cll_typ(data){
		CcrBizModel.com_cll_typ._setValue(data.id);
	}		

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCcrBizModelRecord.do" method="POST">
		
		<emp:gridLayout id="CcrBizModelGroup" title="行业数据模型" maxColumn="2">
			<emp:select id="CcrBizModel.com_cll_typ" label="行业类型" required="true" dictname="STD_ZB_ASS_CREDIT"/>
			<emp:pop id="CcrBizModel.index_no" label="指标编号" returnMethod="returnInd" url="queryIndLibPopList.do" required="true" />
			<emp:text id="CcrBizModel.index_name" label="指标名称" maxlength="60" required="false" />
			<emp:text id="CcrBizModel.excellent_score" label="优秀值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.good_score" label="良好值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.average_score" label="平均值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.lower_score" label="较低值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.worse_score" label="较差值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.worst_score" label="最差值" maxlength="16" required="false" hidden="true" defvalue="0"/>
			<emp:select id="CcrBizModel.com_opt_scale" label="企业规模" required="true"  dictname="STD_ZB_ENTERPRISE"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

