<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.DataField"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	IndexedCollection iColl = (IndexedCollection) context.getDataElement("CapStsList");
	String[] nums = {"","","","","","","","","","","","","","","","","",""};
	for(int i =0 ; i < iColl.size() ; i++){
		nums[i] = ((KeyedCollection)iColl.get(i)).getDataValue("nums") + "";
	}
	
%>
<emp:page>

<html>
<head>
<title>风险预警信息</title>
<jsp:include page="/include.jsp" flush="true" />


<script type="text/javascript"></script>
</head>

<body class="page_content">
	<table class='emp_rpt_whole_table' width="100%" cellpadding="0" cellspacing="0">

	<tr height="30" align="left"><td class="emp_gridlayout_titles"  >新资本统计</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_titles_sub" >1、资本充足率</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;核心一级资本充足率&nbsp;≥&nbsp;<%=nums[0]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一级资本充足率&nbsp;≥&nbsp;<%=nums[1]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总资本充足率&nbsp;≥&nbsp;<%=nums[2]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;杠杆率=核心资本/总资产&nbsp;≥&nbsp;<%=nums[3]%></td></tr>
		
		<tr height="30" align="left""><td  class="emp_gridlayout_titles_sub" >2、资产质量</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;贷款拨备率&nbsp;≥&nbsp;<%=nums[4]%>&nbsp;&nbsp;损失准备/贷款</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;拨备覆盖率&nbsp;≥&nbsp;<%=nums[5]%>&nbsp;&nbsp;损失准备/不良贷款</td></tr>
		
		<tr height="30" align="left""><td  class="emp_gridlayout_titles_sub" >3、集中度风险</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单一集团客户授信集中度&nbsp;≤&nbsp;<%=nums[6]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单一客户贷款集中度&nbsp;≤&nbsp;<%=nums[7]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;全部关联度指标&nbsp;≤&nbsp;<%=nums[8]%></td></tr>
		
		<tr height="30" align="left""><td  class="emp_gridlayout_titles_sub" >4、盈利性</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;资产利润率&nbsp;≥&nbsp;<%=nums[9]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;资本利润率&nbsp;≥&nbsp;<%=nums[10]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;成本收入比&nbsp;≤&nbsp;<%=nums[11]%></td></tr>
		
		<tr height="30" align="left""><td  class="emp_gridlayout_titles_sub" >5、流动性风险</td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;流动性比例&nbsp;≥&nbsp;<%=nums[12]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;流动性覆盖率&nbsp;≥&nbsp;<%=nums[13]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;净稳定资金比例&nbsp;≥&nbsp;<%=nums[14]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;流动性缺口率&nbsp;≥&nbsp;<%=nums[15]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;核心负债依存度&nbsp;≥&nbsp;<%=nums[16]%></td></tr>
		<tr height="30" align="left""><td  class="emp_gridlayout_td" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;存贷比&nbsp;≥&nbsp;<%=nums[17]%></td></tr>

	</table>
</body>
</html>
</emp:page>