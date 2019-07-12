<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*XD140718027：常规检查小微客户财报页面新增*/
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<emp:tabGroup mainTab="mainTab" id="mainTabs">
	<!-- modified by lisj 2015-1-28 需求编号【XD150123004】 小微部关于贷后检查模块的变更需求  begin-->	
		<emp:tab label="财务指标分析" id="fnctab1" needFlush="true" url="subjectAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
		<emp:tab label="杜邦财务分析" id="fnctab2" needFlush="true" url="dupontAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
		<emp:tab label="财务比率分析" id="fnctab3" needFlush="true" url="finaRateAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
		<emp:tab label="结构分析" id="fnctab4" needFlush="true" url="struAnaly.do?FncStatBase.cus_id=${context.cus_id}"></emp:tab>
	<%if("view".equals(op)){ %>
		<emp:tab label="财务分析结论" id="mainTab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA274301455148F65B5310D8FCF6B1&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view"></emp:tab>
	<%}else{ %>
		<emp:tab label="财务分析结论" id="mainTab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA274301455148F65B5310D8FCF6B1&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update"></emp:tab>
	<%} %>
	<!-- modified by lisj 2015-1-28 需求编号【XD150123004】 小微部关于贷后检查模块的变更需求  end-->
	</emp:tabGroup>
</body>
</html>
</emp:page>
