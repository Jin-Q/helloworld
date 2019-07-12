<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
List list = (List)context.getDataValue("WorkFlowHisList");

String serno = (String)request.getParameter("serno");
String instanceId = (String)request.getParameter("instanceId");
String nodeId = (String)request.getParameter("nodeId");
String intransfer=request.getParameter("intransfer");

boolean pageDispart =  false;
int pageSize = 5;  //默认5条记录每页
if(intransfer!=null&&intransfer.equals("true"))//当内部调用时每页8条
	pageSize=8;
int begin, end, currentPage = 1;

int size =0;
if(list!=null)
size=list.size();
int amountPage = (size+pageSize-1)/pageSize;
if(size > pageSize) {
	pageDispart = true;
	try {
		currentPage = Integer.valueOf((String)request.getParameter("currentPage"));
	} catch(Exception e) {
	}
	begin = (currentPage-1)*pageSize;
	end = currentPage*pageSize<=size? currentPage*pageSize:size;

} else {
	begin = 0;
	end = list.size();
}
%>
<html>
<head>
<title>流程跟踪</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/workflow/jquery-1.4.4.js"></script>

<script type="text/javascript">
window.onload = function() {
		//调整iframe高度
		var parentObj=parent.document.getElementById("advice_wf_lcgz");
		if(typeof parentObj != "undefined" && parentObj != null){
			var height = document.body.scrollHeight;
			if(height<200){
				parentObj.style.height = height+100;
			}else{
				parentObj.style.height = height;
			}
		}
}

function linkpage(tag) {
	var amount = parseInt("<%=amountPage%>");
	if(amount < 1)
		return;
	var curPage = parseInt("<%=currentPage%>");
	var pageTmp;
	var contextURL = "<%=request.getContextPath()%>";
	var url = contextURL+'/getWorkFlowSimHistory.do?&instanceId=<%=instanceId %>&nodeId=<%=nodeId%>&serno=<%=serno%>&intransfer=<%=intransfer %>&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	if(tag == "first") {
		if(curPage == 1)
			return;
		url = url + "&currentPage=1";
	} else if(tag == "forward") {
		if(curPage == 1)
			return;
		else
			pageTmp = curPage-1;
		url =  url + "&currentPage="+pageTmp;
	} else if(tag == "behind") {
		if(curPage == amount)
			return;
		else
			pageTmp = parseInt(curPage) + 1;
		url =  url + "&currentPage="+pageTmp;	
	} else if(tag == "last") {
		if(curPage == amount)
			return;
		url =  url + "&currentPage="+amount;
	} else if(tag == "go") {
		var goPage = document.getElementById("goPage").value;
		if(!isNaN(goPage)) {
			if(goPage < 1) {
				goPage = 1;
			} else if(goPage > amount) {
				goPage = amount;
			} else if(goPage == curPage) {
				document.getElementById("goPage").value = "";
				return;
			}
			url =  url + "&currentPage="+goPage;
		} else if(goPage == "") {
			return;
		} else{
			alert("输入的页码非法，请重新输入！");
			document.getElementById("goPage").value = "";
			document.getElementById("goPage").focus();
			return;
		}
	}
	window.location=url;
}


var originClassName;
function invertmenu(){
	if (event.srcElement.tagName == 'TD'){
		originClassName=event.srcElement.parentElement.className;
		event.srcElement.parentElement.className = 'trclass3';
	}
	else{
		originClassName=event.srcElement.parentElement.parentElement.className;
		event.srcElement.parentElement.parentElement.className = 'trclass3';
	}
}
function resumemenu(){
	if (event.srcElement.tagName == 'TD')
		event.srcElement.parentElement.className=originClassName;
	else
		event.srcElement.parentElement.parentElement.className =originClassName;
}

//查看子流程/会办相关信息
function showSubTip(subType,subID){
	var contextPath="<%=request.getContextPath()%>";
	var url = contextPath+"/showSubTip.do?subType="+subType+"&subID="+subID+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	window.showModalDialog(url,'selectPage','dialogHeight:500px;dialogWidth:900px;help:yes;resizable:yes;status:no;');
}

function exExcel(){
document.getElementById("exExcel_div").src="<%=request.getContextPath()%>/ccServlet?instanceid=<%=instanceId%>&actionType=exExcel";
}

function showtDetail(commentid,curnodeid) {
/*	var nodeId = '<%=nodeId%>';
	if(nodeId==null||nodeId==''||nodeId=='null'){
		nodeId = curnodeid;
	}*/
	var url = 'getWfiBizVarDetailByCommentId.do?instanceId=<%=instanceId%>&nodeId='+curnodeid+'&serno=<%=serno%>&commentid='+commentid+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	window.showModalDialog(url,'varDetailPage','dialogWidth:800px;dialogHeight:300px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
}
//过时了
function createWin1(id){
	var comment = document.getElementById(id).value ;
    var win = new Ext.Window({
        title: '流程意见',
        layout: 'fit',    //设置布局模式为fit，能让frame自适应窗体大小
        modal: false,    //打开遮罩层
        border: 0,    //无边框
        frame: true,    //去除窗体的panel框架
        html: comment
    });
    win.setSize(300, 150);    //w为设置的宽度，h为设置的高度
    win.setPosition(400, 20);    //x为设置的x坐标，y为设置的y坐标
    win.show();    //显示窗口
}
//流程意见
function createWin(id){
	var comment = document.getElementById(id).value;
	window.showModalDialog("liucheng.jsp",comment,"dialogHeight=500px;dialogWidth=850px;");
}
// added by yangzy 2014/12/11 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start
function createWinOther(id){
	var comment = document.getElementById(id).value;
	window.showModalDialog("liucheng.jsp",comment,"dialogHeight=500px;dialogWidth=850px;");
}
// added by yangzy 2014/12/11 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end
//由于是新建的模式窗口，所以会显示到最上层，不会被遮盖，下面为调用代码，在10，20的位置创建高宽都为500的模式窗体


function doKeyDownMy(){
if(event.keyCode==13){
linkpage('go');
return false;
}
}
</script>
</head>
<body>
<%-- <img alt="导出excel" src="<%=request.getContextPath()%>/images/workflow/xls.gif"  onclick="exExcel();" onmouseover="this.style.cursor='hand'"> --%>
<br>
<table class=tablemain cellspacing=0 cellpadding=0>
<tr class=trtitle>
<td>序号</td>
<td>所处岗位</td>
<td>办理人</td>
<td>办理岗位</td>
<!-- <td width="12%">办理机构</td> -->
<td>开始时间</td>
<td>结束时间</td>
<td>操作记录</td>
<td>流程意见</td>
<td>流程意见(附)</td>
 <td>变更历史</td></tr> 
<%
if(list==null||list.isEmpty()){
%>
<tr class=trclass><td colspan='10'>没有流程办理信息</td></tr>
<% 
}else{
	Map obj;
	boolean tr = true;
	int k=0;
	for(int i=begin;i<end;i++){
		obj=(Map)list.get(i);
	    if(tr){
			out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}else{
			out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}
	    k++;
%>
<td><%=(i+1)%></td>
<td><%=(String)obj.get("nodename")%></td>
<td><%=(String)obj.get("username")%></td>
<td><%=(String)obj.get("nodenametmp")%></td>
<!-- <td><%=obj.get("orgname")==null?"-":(String)obj.get("orgname")%></td> -->
<td><%=(String)obj.get("nodestarttime")%></td>
<td><%="".equals((String)obj.get("nodeendtime"))?"-":(String)obj.get("nodeendtime")%></td>
<td><%=(String)obj.get("methods")%></td>
<td><a href="#" onclick="createWin('wf_<%=k%>')"><%=obj.get("commentcontent")==null?"":obj.get("commentcontent").toString().length()>30?obj.get("commentcontent").toString().substring(0,16)+"......":(String)obj.get("commentcontent")%></a></td>
<!-- added by yangzy 2014/12/11 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start -->
<td><a href="#" onclick="createWinOther('wfo_<%=k%>')"><%=obj.get("commentcontentother")==null?"":obj.get("commentcontentother").toString().length()>30?obj.get("commentcontentother").toString().substring(0,16)+"......":(String)obj.get("commentcontentother")%></a></td>
<!-- added by yangzy 2014/12/11 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end -->
<td><%
        if("1".equals((String)obj.get("modifiedbizvar"))){
			out.print("<a href='#' onclick=showtDetail('"+obj.get("commentid")+"','"+obj.get("curnodeid")+"');>点击查看</a>");
		}else{
			out.print("无变更");
		} 
	%>
</td>
<input type="hidden" id="wf_<%=k%>" value='<%=obj.get("commentcontent")==null?"":obj.get("commentcontent")%>'>
<!-- added by yangzy 2014/12/11 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 start -->
<input type="hidden" id="wfo_<%=k%>" value='<%=obj.get("commentcontentother")==null?"":obj.get("commentcontentother")%>'>
<!-- added by yangzy 2014/12/11 需求:XD141107075_关于新信贷系统中增加、修改部分查询信息等功能 end -->
</tr>
<%
		tr=!tr;
	}
}
%>
</table>
<br>
<%
if (pageDispart) {
%>
	<div align="right">
	<font><a href="javascript:linkpage('first')" class="A2">首页</a></font>&nbsp;
	<font><a href="javascript:linkpage('forward')" class="A2 a2_pre">上一页</a></font>&nbsp;
	<font><a href="javascript:linkpage('behind')" class="A2 a2_next">下一页</a></font>&nbsp;
	<font><a href="javascript:linkpage('last')" class="A2">尾页</a></font>&nbsp;&nbsp;
	<input type="text" id="goPage" name="goPage" maxlength="5" size="3" onkeydown="doKeyDownMy();"/>
	<font><a href="javascript:linkpage('go')" class="A2">跳转</a></font>&nbsp;&nbsp;
	<font>当前第[<%=currentPage%>]页&nbsp;共[<%=amountPage%>页]&nbsp;[<%=size%>条记录]</font>
	</div>
<%
}
%>
<!-- <iframe id="exExcel_div" name="exExcel_div" style="display: none"
			width="10" height="10"></iframe> -->
<div align="center">	

<!-- input name="Submit52" type="button"  value="图形跟踪" class="button" onClick="window.open('<%=request.getContextPath()%>/echain/studio/eChainMonitor.jsp?instanceid=<%=instanceId%>','name','left=0,top=0,width=1024,height=768,menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes');"-->		
</div>	  

</body>
</html>
