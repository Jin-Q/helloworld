<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCcrBizModelList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CcrBizModelGroup" title="行业数据模型" maxColumn="2">
			<emp:select id="CcrBizModel.com_cll_typ" label="行业类型" required="false" dictname="STD_ZB_ASS_CREDIT"/>
			<emp:text id="CcrBizModel.index_no" label="指标编号" maxlength="12" required="true" />
			<emp:text id="CcrBizModel.index_name" label="指标名称" maxlength="60" required="false" />
			<emp:text id="CcrBizModel.excellent_score" label="优秀值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.good_score" label="良好值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.average_score" label="平均值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.lower_score" label="较低值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.worse_score" label="较差值" maxlength="16" required="false" />
			<emp:text id="CcrBizModel.worst_score" label="最差值" maxlength="16" required="false" />
			<emp:select id="CcrBizModel.com_opt_scale" label="企业规模" required="true" dictname="STD_ZB_ENTERPRISE"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
