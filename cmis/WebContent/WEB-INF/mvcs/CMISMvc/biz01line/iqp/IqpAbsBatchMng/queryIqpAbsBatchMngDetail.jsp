<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%><emp:page>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAbsBatchMngList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};	
	function doClose() {
		window.close();
	};	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAbsBatchMngGroup" title="批次管理表" maxColumn="2">
			<emp:text id="IqpAbsBatchMng.batch_no" label="批次号" maxlength="18" required="true" />
			<emp:text id="IqpAbsBatchMng.batch_name" label="证券化批次名称" maxlength="80" required="false" />
			<emp:text id="IqpAbsBatchMng.trust_org_cert_type" label="受托机构证件类型" maxlength="2" required="false" />
			<emp:text id="IqpAbsBatchMng.trust_org_cert_no" label="受托机构证件号码" maxlength="20" required="false" />
			<emp:text id="IqpAbsBatchMng.trust_org_no" label="受托机构名称" maxlength="80" required="false" />
			<emp:text id="IqpAbsBatchMng.manager_br_id" label="所属行社" maxlength="80" required="false" />
			<emp:text id="IqpAbsBatchMng.fund_acct_no" label="信托财产资金账号" maxlength="30" required="false" />
			<emp:text id="IqpAbsBatchMng.fund_acct_name" label="信托财产资金户名" maxlength="30" required="false" />
			<emp:text id="IqpAbsBatchMng.keep_org_no" label="资金保管机构行号" maxlength="20" required="false" />
			<emp:text id="IqpAbsBatchMng.keep_org_name" label="资金保管机构行名" maxlength="80" required="false" />
			<emp:text id="IqpAbsBatchMng.is_this_org_service" label="是否本机构服务" maxlength="1" required="false" />
			<emp:text id="IqpAbsBatchMng.service_org_no" label="贷款服务机构行号" maxlength="20" required="false" />
			<emp:text id="IqpAbsBatchMng.service_org_name" label="贷款服务机构行名" maxlength="80" required="false" />
			<emp:text id="IqpAbsBatchMng.service_fee_rate" label="服务费率" maxlength="2" required="false" />
			<emp:text id="IqpAbsBatchMng.service_fee" label="总服务费" maxlength="11" required="false" />
			<emp:text id="IqpAbsBatchMng.trust_date" label="信托成立日" maxlength="10" required="false" />
			<emp:text id="IqpAbsBatchMng.cash_date" label="最后兑付日期" maxlength="10" required="false" />
			<emp:text id="IqpAbsBatchMng.input_id" label="操作人员" maxlength="10" required="false" />
			<emp:text id="IqpAbsBatchMng.input_br_id" label="操作机构" maxlength="10" required="false" />
			<emp:text id="IqpAbsBatchMng.update_date" label="修改日期" maxlength="10" required="false" />
			<emp:text id="IqpAbsBatchMng.acct_status" label="账务状态" maxlength="2" required="false" />
			<emp:text id="IqpAbsBatchMng.mark" label="备注" maxlength="80" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if("".equals(flag) || flag == null) {%>
		   <emp:button id="return" label="返回到列表页面"/>
		<% }else{%> 
			<emp:button id="close" label="返回"/>
		<% }%>
	</div>
</body>
</html>
</emp:page>
