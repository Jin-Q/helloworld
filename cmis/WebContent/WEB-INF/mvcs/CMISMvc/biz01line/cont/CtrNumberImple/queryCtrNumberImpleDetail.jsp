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
		var url = '<emp:url action="queryCtrNumberImpleList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CtrNumberImpleGroup" title="合同评分配置表" maxColumn="2">
			<emp:select id="CtrNumberImple.score_type" label="评分类型" dictname="STD_CTR_SCORE_TYPE" required="true" />
			<emp:text id="CtrNumberImple.score_code" label="评分字段" maxlength="10" required="true" />
			<emp:text id="CtrNumberImple.score_name" label="评分字段名称" maxlength="300" required="true" />
			<emp:text id="CtrNumberImple.auto_score" label="得分" maxlength="16" required="true" />
			<emp:date id="CtrNumberImple.input_date" label="登记日期" readonly="true" />
			<emp:date id="CtrNumberImple.update_date" label="修改日期" readonly="true" />
			<emp:text id="CtrNumberImple.score_id" label="主键" maxlength="40" required="false" hidden="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
