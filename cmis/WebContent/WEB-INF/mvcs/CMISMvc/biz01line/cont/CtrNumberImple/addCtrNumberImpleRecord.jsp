<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doReset(){
		page.dataGroups.CtrNumberImpleGroup.reset();
	};
	
	function doReturn() {
		var url = '<emp:url action="queryCtrNumberImpleList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCtrNumberImpleRecord.do" method="POST">
		
		<emp:gridLayout id="CtrNumberImpleGroup" title="合同评分配置表" maxColumn="2">
			<emp:select id="CtrNumberImple.score_type" label="评分类型" dictname="STD_CTR_SCORE_TYPE" required="true" />
			<emp:text id="CtrNumberImple.score_code" label="评分字段" maxlength="10" required="true" />
			<emp:text id="CtrNumberImple.score_name" label="评分字段名称" maxlength="300" required="true" />
			<emp:text id="CtrNumberImple.auto_score" label="得分" maxlength="16" required="true" />
			<emp:date id="CtrNumberImple.input_date" label="登记日期" readonly="true" defvalue="$OPENDAY"/>
			<emp:date id="CtrNumberImple.update_date" label="修改日期" readonly="true" hidden="true"/>
			<emp:text id="CtrNumberImple.score_id" label="主键" maxlength="40" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

