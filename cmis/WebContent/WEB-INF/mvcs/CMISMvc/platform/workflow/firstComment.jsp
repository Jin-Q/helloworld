<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>

<title>录入流程意见</title>
<jsp:include page="/include.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />

<script type="text/javascript">


window.onbeforeunload=function(event){
     <!--return '若离开将放弃流程提交';-->
     this.returnValue = 'cancelStart';
} 
function doSubmit() {
	var sug = document.getElementById("commentContent").value;
	if(sug==null || sug=='') {
		alert('请输入流程意见！');
		return;
	}
	this.returnValue = sug;
	window.onbeforeunload = null;
	window.close();
}

function doWfFaster() {
	document.getElementById("commentContent").value = wfFaster._getValue();
}

</script>

</head>

<body>

<div>
<form action="" name="form1" id="form1">
<table class=tablemain cellspacing=0 cellpadding=0 border=0 align="center">
<tr>

<td class=trtitle2 colspan="2">
<div><span>快捷意见：</span>
<emp:select id="wfFaster" label="快捷意见：" dictname="WF_APP_SHORTCUT"  onchange="doWfFaster()"></emp:select>
</div>
</td>
</tr>
<tr>
<td><textarea  id="commentContent" name="commentContent" rows="8" cols="55"></textarea></td>
</tr>
</table>

<div align="center" style="margin-top:10px;">
	<input type="button" name="submit12" value=" 确 定 "  class="button" onClick="doSubmit()">
</div>
</form>
</div>
</body>
</html>
</emp:page>