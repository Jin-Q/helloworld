<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String iqpFlowHis = "";
	String instanceId = "";
	String approve_status = "";
	if(context.containsKey("iqpFlowHis")){
		iqpFlowHis = (String)context.getDataValue("iqpFlowHis");
	}      
	if(context.containsKey("instanceId")){
		instanceId = (String)context.getDataValue("instanceId");
	}    
	if(context.containsKey("approve_status")){
		approve_status = (String)context.getDataValue("approve_status");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

<script type="text/javascript">


</script>

</head>
<body class="page_content">
<emp:tabGroup mainTab="shouFlowHisTab" id="mainTab" >

<br>
   <emp:tab label="授信审批历史意见" id="shouFlowHisTab" url="getLmtFlowHis.do" reqParams="instanceId1=${context.instanceId1}&instanceId2=${context.instanceId2}" needFlush="true"></emp:tab>
<%if((!"".equals(instanceId) && instanceId!=null) && !"000".equals(approve_status)){ %>
   <emp:tab label="业务审批历史意见" id="iqpFlowHisTab" url="getIqpFlowHis.do" reqParams="instanceId=${context.instanceId }" needFlush="true"></emp:tab>
<%} %>   
</emp:tabGroup> 

<div align="center">

</div>
</body>
</html>
</emp:page>