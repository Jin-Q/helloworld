<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
	String ggcbl ="";
	if(context.containsKey("ggcbl")){
		ggcbl = context.getDataValue("ggcbl").toString();
	}
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/iqp/IqpBatchMng/iqpBatchMngComm.jsp" flush="true" />

<script type="text/javascript">
	
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<emp:tabGroup mainTab="mainTab" id="mainTabs">
	<%if(ggcbl.equals("y")){ %>
		<emp:tab label="基本信息" id="mainTab" needFlush="true" url="getIqpActrecpoManaAddPage.do?menuId=blcgl&PO_TYPE=${context.PO_TYPE}&type=${context.type}&po_no=${context.po_no}&openType=1&ggcbl=y"></emp:tab>
	<%}else{%>
		<emp:tab label="基本信息" id="mainTab" needFlush="true" url="getIqpActrecpoManaAddPage.do?menuId=${context.menuId}&PO_TYPE=${context.PO_TYPE}&type=${context.type}&po_no=${context.po_no}&openType=1"></emp:tab>
	<%} %>
			<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
