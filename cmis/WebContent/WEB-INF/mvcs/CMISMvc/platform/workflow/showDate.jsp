<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@page import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
//读取非工作日数据
String freeDate=(String)context.getDataValue("freeDate");//"'20090819','20090824','20090826','20090802','20090807','20090808','20090812','20090815','20090817','20090919','20090923','20090924'";
if(freeDate==null)
	freeDate="";
String year=(String)context.getDataValue("year");
String month=(String)context.getDataValue("month");
Calendar c = Calendar.getInstance();
if(year==null||year.equals(""))
	year=String.valueOf(c.get(c.YEAR));
if(month==null||month.equals("")){
	month=String.valueOf(c.get(c.MONTH)+1);
}
if(month.length()==1)
	month="0"+month;
%>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script language="javascript" src="<%=request.getContextPath()%>/scripts/workflow/freeDate/mdate.js"></script>
<style>
BODY{font-size:9pt}
.todayColor{
	font-size:15pt;
	color:red;
}
.freeDateTD{
	background-color:#e0e0e0;
}
.button
{
    font-size: 9pt;
    color: #000000;
    padding: 2px 0px 0px 0px;
	background-color:#F4F9FF;    
    height: 19px;
	BORDER: #B7BAC1 1pt solid;
}
</style>
</head>
<body onload="doInit()">
<div id="form_content">
	<div class="form_unit">&nbsp;节假日设置>>></div><br>
<table bgcolor="#FFFFFF" cellspacing="0" cellpadding="0" style="border-collapse: collapse;border: 1px solid #c2c2c2; padding: 1" align="center" width="630" border="0">
<tr height="30" bgcolor="#EDF2F2">
<td align="center" width="50" style="cursor: hand; color: #1F72D0; font-size:9pt;" title="上一年" onclick="preYear()">&lt;&lt;</td>
<td align="center" width="50" style="cursor: hand; color: #1F72D0; font-size:9pt" title="上一月" onclick="preMonth()">&lt;</td>
<td align="center" width="215" nowrap="nowrap" id="YMBG" style="color: #1F72D0;font-size:9pt"></td>
<td align="center" width="215" nowrap="nowrap" id="GZ" style="color: #1F72D0;font-size:9pt"></td>
<td align="center" width="50" style="cursor: hand; color: #1F72D0; font-size:9pt" title="下一月" onclick="nextMonth()">&gt;</td>
<td align="center" width="50" style="cursor: hand; color: #1F72D0; font-size:9pt" title="下一年" onclick="nextYear()">&gt;&gt;</td>
</tr>
<tr><td colspan="6" width="630">
<div id="ttdiv" align="center" width="630"></div>
</td></tr>
<tr><td colspan="6" height="1"></td></tr>
</table><br>
	<div class="button_area" align="center">
		<button value="初始化年度数据" onclick="doReset(this)">初始化年度数据</button>
	</div><br>
	<div>&nbsp;&nbsp;&nbsp;<font color="#aaaaaa">注：灰色方框背景的为非工作日（周末或节假日），其余的为工作日</font></div><br>
</div>
<script language="javascript">
var arrArtdate=new Array(<%=freeDate%>);
function doInit() {	
	initial("ttdiv",<%=year%>,<%=month%>);
}
function doReset(e) {
	var url = "getFreeDateInitPage.do?EMP_SID="+'<%=request.getParameter("EMP_SID")%>'
	window.location.href=url;
}
//设置为非工作日
function doSetFreeDate(disYMD) {
	document.submitForm.action = "<%=request.getContextPath()%>/freeDateManage.do?subType=setFreeDate&EMP_SID="+'<%=request.getParameter("EMP_SID")%>';
	document.submitForm.curDate.value = disYMD;
	document.submitForm.year.value =(""+disYMD).substring(0,4);
	document.submitForm.month.value =(""+disYMD).substring(4,6);
	document.submitForm.submit();
}
//设置为工作日
function doSetWorkDate(disYMD) {
	document.submitForm.action = "<%=request.getContextPath()%>/freeDateManage.do?subType=setWorkDate&EMP_SID="+'<%=request.getParameter("EMP_SID")%>';
	document.submitForm.curDate.value = disYMD;
	document.submitForm.year.value =(""+disYMD).substring(0,4);
	document.submitForm.month.value =(""+disYMD).substring(4,6);
	document.submitForm.submit();
}
//上一年
function preYear() {
	document.submitForm.action = "<%=request.getContextPath()%>/freeDateManage.do?subType=getFreeDate&EMP_SID="+'<%=request.getParameter("EMP_SID")%>';
	var mYear=document.submitForm.year.value;
	mYear--;
	if(mYear<1900)mYear=1900;
	document.submitForm.year.value =mYear;
	document.submitForm.submit();
}
//上一年
function nextYear() {
	document.submitForm.action = "<%=request.getContextPath()%>/freeDateManage.do?subType=getFreeDate&EMP_SID="+'<%=request.getParameter("EMP_SID")%>';
	var mYear=document.submitForm.year.value;
	mYear++;
	if(mYear>2050)mYear=2050;
	document.submitForm.year.value =mYear;
	document.submitForm.submit();
}
//上一月
function preMonth() {
	document.submitForm.action = "<%=request.getContextPath()%>/freeDateManage.do?subType=getFreeDate&EMP_SID="+'<%=request.getParameter("EMP_SID")%>';
	var mMonth=document.submitForm.month.value;
	mMonth--;
	if(mMonth<1){
		mMonth=11;
		mYear--;
	    document.submitForm.year.value =mYear;
	   	document.submitForm.month.value =mMonth+1;
		document.submitForm.submit();
	}else{
		document.submitForm.month.value =mMonth;
		document.submitForm.submit();
	}
}
//下一月
function nextMonth() {
	document.submitForm.action = "<%=request.getContextPath()%>/freeDateManage.do?subType=getFreeDate&EMP_SID="+'<%=request.getParameter("EMP_SID")%>';
	var mMonth=document.submitForm.month.value;
	mMonth++;
	if(mMonth>12){
		mMonth=0;
		mYear++;
	   	document.submitForm.year.value =mYear;
	   	document.submitForm.month.value =mMonth+1;
		document.submitForm.submit();
	}else{
		document.submitForm.month.value =mMonth;
		document.submitForm.submit();
	}
}
</script>
</body>
<form name="submitForm" method="post" action='<%=request.getContextPath()%>/freeDateManage.do?subType=getFreeDate&EMP_SID=<%=request.getParameter("EMP_SID")%>';>
	<input type="hidden" name="curDate"/>
	<input type="hidden" name="workDayFlg" value="N"/>
	<input type="hidden" name="year" value="<%=year %>"/>
	<input type="hidden" name="month" value="<%=month %>"/>
</form>
</html>