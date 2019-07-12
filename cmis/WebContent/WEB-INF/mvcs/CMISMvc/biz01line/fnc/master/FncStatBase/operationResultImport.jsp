<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<html>
<head>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String errorFlag = (String)context.getDataValue("errorInfo");
%>

<%
String url=request.getParameter("url");
%>
<title>操作返回页面</title>
<script type="text/javascript">

	function doOnLoad(){
		var errorInfo = '${context.errorInfo}';
		if(errorInfo=='errorInfo'){
			alert("您导入的报表样式和客户现在的样式不一致或者EXCEL表不是03版的，请核实！");
			}
		var cus_id = '${context.cus_id}';
		var statPrdStyle = '${context.stat_prd_style}';
		var statPrd = '${context.stat_prd}';
		var statStyle = '${context.stat_style}';
		var editFlag = '${context.EditFlag}';
		if(editFlag!=null&&editFlag=='cusFnc'){
			var url = '<emp:url action="queryCusFncStatBaseList.do"/>';
		}else{
			var paramStr = "FncStatBase.cus_id="+cus_id+"&EditFlag="+editFlag;
			var url = '<emp:url action="queryFncStatBaseList.do"/>&'+paramStr;
		}
  //      var paramStr = "FncStatBase.cus_id="+cus_id+"&FncStatBase.stat_prd_style="+statPrdStyle+"&FncStatBase.stat_prd="+statPrd+"&FncStatBase.stat_style="+statStyle;
        
		url = EMPTools.encodeURI(url);
		var link = document.getElementById("returnlink");
		link.href = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
			<% 
				if("errorInfo".equals(errorFlag)){
			%>	
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;导入失败！
			<%	
				}else{
			%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作已成功！
			<%} %>
			</span><br>
			<TABLE align="center" width="300px">
        		<TR align="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green"><a id="returnlink" href="">回到列表页面</a></font></span>
          			</TD>
        		</TR>
        		<TR><TD>&nbsp;</TD></TR>
      		</TABLE>
		</div>
	</div>
	<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</body>
</html>
