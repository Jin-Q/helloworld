<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.WorkFlowConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIUserVO"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	List<WFIUserVO> wfUserList = null;
	try {
		wfUserList = (List<WFIUserVO>) context.getDataValue(WorkFlowConstance.WF_NEXT_USER_LIST);
	} catch(Exception e) {
		e.printStackTrace();
		out.println("<center>转办出错，请联系管理员！错误信息："+e.getMessage()+"</center>");
		return;
	}

%>

<emp:page>
<html>
<head>
<TITLE>转办人员选择</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/include.jsp" flush="true"/>

<link href="<%=request.getContextPath()%>/styles/workflow/default.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">

//选择用户
function selUser(count){
	var contextPath="<%=request.getContextPath()%>";
	//打开选择处理人的界面
	var url = contextPath+'/selectAllUser.do?count='+count+'&EMP_SID=<%=request.getParameter("EMP_SID")%>&rd='+Math.random();
	var retObj = window.showModalDialog(url,'selectPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;');
		
	//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
	if(retObj == null)
		return;
	var status = retObj[0];
	if(status != true)
		return;
	if(retObj[1] != null){
		if(retObj[1].indexOf(";")!=-1){//返回多值
			var list=retObj[1].split(";");
			var seluser="";
			for(var i=0;i<list.length;i++){
				if(i>0)
					seluser+=";U."+list[i];
				else
					seluser+="U."+list[i];
			}
			document.getElementById("seluser").value =seluser;
		}else{
			document.getElementById("seluser").value = "U."+retObj[1];
		}
	}
	if(retObj[2] != null){
		document.getElementById("userid").value = retObj[2];		
	}
}

function retSuc(){
	var retObj = [];
	retObj[0] = true;
	var user = document.getElementById("seluser").value;
	if(user==null||user==""){
		list = document.getElementsByName("userid");
		for(var i=0;i<list.length;i++){
			if(!list[i].checked)
				continue;
			if(user == null)
				user = list[i].value;
			else
				user = user + ";" + list[i].value;
		}
	}		
	retObj[1] = user;
	if(user==null||user.length==0){
		alert("请选择要转给的人");
		return ;
	}
	window.returnValue = retObj;
	window.close();
};

function retFail(){
	var retObj = [];
	retObj[0] = false;
	window.returnValue = retObj;
	window.close();
};

</script>

</head>
<body>
<input type="hidden" id="seluser" value=""/>
<div class="selectNextNodeStyle">
<fieldset>
<br>
<table border="0" width="500px" cellspacing="1" cellpadding="0" bgcolor="000000">
	<tr>
		<td width="50%" class="tdtitle">
		<%
		if(wfUserList!=null && wfUserList.size()>0) {
			if(wfUserList.get(0).getUserId().equals(WorkFlowConstance.ALL_USER)) {
				out.println("<input type=\"text\" id=\"userid\" name=\"userid\" value=\"\" readonly=true/>&nbsp;&nbsp;<a href=# onclick=\"selUser('1')\">选择</a><br/>");
			} else {
				for(int i=0; i<wfUserList.size(); i++) {
					WFIUserVO u = wfUserList.get(i);
					out.println("<input type=\"radio\" name=\"userid\" value=\"" + u.getUserId() + "\">" + u.getUserName()+"<br/>");
				}
			}
		} else {
		%>
		<font color=red>查询未找到合适用户或当前流程设置不允许转办</font>
		<%} %>
		</td>
	</tr>
</table>
<br>
</fieldset>
</div>
<center>
<input type="button" class="button" value="&nbsp;&nbsp;确&nbsp;&nbsp;&nbsp;&nbsp;定&nbsp;&nbsp;" onclick="retSuc()">&nbsp;&nbsp;
<input type="button" class="button" value="&nbsp;&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;&nbsp;" onclick="retFail()"></center>

</body>
</html>
</emp:page>