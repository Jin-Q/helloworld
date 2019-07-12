<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	String belg_line = "";//客户所属条线
	if(context.containsKey("belg_line")){
		belg_line = (String)context.getDataValue("belg_line");
	}
	String fin_type = "";//财报类型
	if(context.containsKey("fin_type")){
		fin_type = (String)context.getDataValue("fin_type");
	}
%>
<style type="text/css">
 .table_td_show{
	border: 1px solid  #BCD7E2;
	padding: 0px 3px;
	cursor: pointer;
	cursor: hand;
	white-space: nowrap;
	overflow: visible;
}

.table_show{
	width: 100%;
	border-collapse: collapse;
	border-spacing: 0;
	border: 1px solid #7BAFC5;  
}

</style>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

</script>
</head>
<body class="page_content">
<%if(!"BL300".equals(belg_line)&&"PB0001".equals(fin_type)){//2002版财报 %>
	<emp:tabGroup id="FinInfo_tabs" mainTab="FinInfo_tab">
		<emp:tab id="FinInfo_tab" label=" " url="getReportShowPage.do?reportId=psp/bzrcwqk.raq&cus_id=${context.guar_cus_id}" initial="true"/>
	</emp:tabGroup>
<%}else if(!"BL300".equals(belg_line)&&"PB0015".equals(fin_type)){//事业单位财报 %>
	<emp:tabGroup id="FinInfo_tabs" mainTab="FinInfo_tab">
		<emp:tab id="FinInfo_tab" label=" " url="getReportShowPage.do?reportId=psp/sydwbzrcwqk.raq&cus_id=${context.guar_cus_id}" initial="true"/>
	</emp:tabGroup>
<%} %>
	<emp:tabGroup id="IqpBksyndic_tabs" mainTab="IqpBksyndicInfo_tab">
	<%if("BL300".equals(belg_line)){//个人 %>
		<%if(op.equals("view")){ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA2787002D8A2F4F8F9A072E082729&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
		<%}else{ %>
		    <!-- modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,担保分析start -->
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA2787002D8A2F4F8F9A072E082729&task_id=${context.task_id}&cus_id=${context.cus_id}&guar_cus_id=${context.guar_cus_id }&op=update" initial="true"/>
		     <!-- modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,担保分析end -->
		<%} %>
	<%}else{ %>
		<%if(op.equals("view")){ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA276A016098037E87488513680CA2&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
		<%}else{ %>
		    <!-- modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,担保分析start -->
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA276A016098037E87488513680CA2&task_id=${context.task_id}&cus_id=${context.cus_id}&guar_cus_id=${context.guar_cus_id }&op=update" initial="true"/>
			<!-- modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,担保分析end -->
		<%} %>
	<%} %>
	</emp:tabGroup>
</body>
</html>
</emp:page>
    