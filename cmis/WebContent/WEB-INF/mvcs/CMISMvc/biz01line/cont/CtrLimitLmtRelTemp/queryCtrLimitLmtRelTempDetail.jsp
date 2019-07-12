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
		/**
		var url = '<emp:url action="queryCtrLimitLmtRelTempTempList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
		*/
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CtrLimitLmtRelTempTempGroup" title="额度合同占用授信关联表" maxColumn="2">
			<emp:text id="CtrLimitLmtRelTemp.pk_id" label="主键" maxlength="40" required="false" hidden="true"/>
			<emp:text id="CtrLimitLmtRelTemp.limit_serno" label="额度合同业务编号" maxlength="40"  required="false" hidden="false"/>
			<emp:text id="CtrLimitLmtRelTemp.limit_cont_no" label="额度合同合同编号" maxlength="40" required="false" hidden="true" colSpan="2"/>
			<emp:pop id="CtrLimitLmtRelTemp.lmt_code_no" label="授信额度编号" url="queryLmtAgrDetailsPop.do" returnMethod="getLmtMsg"  required="true" />
			<emp:text id="CtrLimitLmtRelTemp.lmt_code_name" label="授信额度品种名称" readonly="true"/>
			<emp:text id="CtrLimitLmtRelTemp.lmt_code_amt" label="授信额度金额" dataType="Currency" readonly="true"/>
			<emp:select id="CtrLimitLmtRelTemp.lmt_type" label="授信额度类型" dictname="STD_ZB_LIMIT_TYPE" readonly="true"/>
			<emp:select id="CtrLimitLmtRelTemp.status" label="状态"  required="false" dictname="STD_ZB_STATUS" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
