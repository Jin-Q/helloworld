<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style>
BODY{font-size:9pt}
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
<script language="javascript">		
function doSubmit() {
	var year = document.getElementById('year').value;
	if (year =='') {
		alert("请输入年度");
		return false;
	}
	else if ( year.length != 4 ) {
		alert("您输入的不是一个有效的年份数字");
		return false;
	}
	else if(true)
	{
		var vUserInput = Number(year);
		if(isNaN(vUserInput)){
			alert("您输入的不是一个有效的年份数字");
			return false;
		}
		else if(vUserInput<1995||vUserInput>2030)
		{
			alert("年份范围为：1995~2030，请重新输入！");
			return false;
		}
	     
	}
	if (! confirm("您确认要初始化 "+year+" 年度非工作日信息吗？"))
		return;
	document.submitForm.year.value =year;
	document.submitForm.submit();
}
</script>
</head>
<body>
<div id="form_content">
	<div class="form_unit">初始化非工作日信息>>></div><br>
	初始年度：<input id="year" type="text" class="text_area" maxlength="4"/><br><br>
	<font color="red"><b>警告：该操作将清除并初始化所设置年度的节假日设置数据</b></font><br><br>
	<div>
	    <button class="button" onClick="doSubmit()">提交</button>
	</div>
</div>
</body>
<form name="submitForm" method="post" action='<%=request.getContextPath()%>/freeDateManage.do?subType=init&EMP_SID=<%=request.getParameter("EMP_SID")%>'>
	<input type="hidden" name="year"/>
</form>
</html>
