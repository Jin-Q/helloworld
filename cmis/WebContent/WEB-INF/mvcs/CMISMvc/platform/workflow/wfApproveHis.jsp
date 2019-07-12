<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%
String instanceId = request.getParameter("instanceId");
String serno = request.getParameter("serno");
//申请查看时挂接审批意见历史 instanceId 放在Attribute中    2013-09-26  唐顺岩
if(null==instanceId || "null".equals(instanceId) || "".equals(instanceId)){
	instanceId = request.getAttribute("instanceId").toString();
}
String nodeId = request.getParameter("nodeId");
%>

<html>
<head>
<title>流程审批历史</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/workflow/jquery-1.4.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />

<script type="text/javascript">

var win;
function showta(value){
	if(typeof win!='undefined')
	win.close();
	win=new Ext.Window({title:'<div style="height:15px;padding-top:8px">意见查看</div>',width:400,height:250,html:value});
	win.show();
}

function hiddenShow(obj, divId) {
	var disp = document.getElementById(divId).style.display;
	if(disp == 'none') {
		obj.src = "<%=request.getContextPath()%>/images/workflow/up.png";
		obj.alt = "收起";
		document.getElementById(divId).style.display = 'block';
	} else {
		obj.src = "<%=request.getContextPath()%>/images/workflow/down.png";
		obj.alt = "展开";
		document.getElementById(divId).style.display = 'none';
	}
}

</script>

</head>
<body>
<!-- 
<fieldset><legend>流程审批变更信息</legend>
<img alt="收起" src="<%=request.getContextPath()%>/images/workflow/up.png" align="right" onclick="hiddenShow(this,'div_wfibizvar_wf')" onmouseover="this.style.cursor='hand'">
<div id="div_wfibizvar_wf">
<iframe id="wfibizvar_wf" frameborder="0" width="98%" scrolling="no" src='<%=request.getContextPath()%>/getWfiBizVarHis.do?instanceId=<%=instanceId %>&EMP_SID=<%=request.getParameter("EMP_SID")%>'></iframe>
</div>
</fieldset>
<br/>

<fieldset><legend>流程意见信息</legend>
<img alt="收起" src="<%=request.getContextPath()%>/images/workflow/up.png" align="right" onclick="hiddenShow(this,'div_advice_wf')" onmouseover="this.style.cursor='hand'">
<div id="div_advice_wf">
<iframe id="advice_wf" frameborder="0" width="98%" scrolling="no" src='<%=request.getContextPath()%>/getWFComment.do?instanceId=<%=instanceId %>&nodeId=<%=nodeId%>&EMP_SID=<%=request.getParameter("EMP_SID")%>'></iframe>
</div>
</fieldset>
<br/>
<fieldset><legend>流程跟踪信息</legend>
<img alt="展开" src="<%=request.getContextPath()%>/images/workflow/down.png" align="right" onclick="hiddenShow(this,'div_advice_lcgz')" onmouseover="this.style.cursor='hand'">
<div id="div_advice_lcgz">
<iframe id="advice_wf_lcgz" frameborder="0" width="98%" scrolling="no" src='<%=request.getContextPath()%>/getWorkFlowHistory.do?&instanceId=<%=instanceId %>&nodeId=<%=nodeId%>&EMP_SID=<%=request.getParameter("EMP_SID")%>'></iframe>
</div>
</fieldset><br>
 -->

<fieldset><legend>流程审批历史</legend>
	<img alt="收起" src="<%=request.getContextPath()%>/images/workflow/up.png" align="right" onclick="hiddenShow(this,'div_advice_lcgz')" onmouseover="this.style.cursor='hand'">
	<div id="div_advice_lcgz">
	<iframe id="advice_wf_lcgz" frameborder="0" width="100%" src='<%=request.getContextPath()%>/getWorkFlowSimHistory.do?&instanceId=<%=instanceId%>&nodeId=<%=nodeId%>&serno=<%=serno%>&EMP_SID=<%=request.getParameter("EMP_SID")%>'></iframe>
	</div>
	</fieldset>
</body>
</html>