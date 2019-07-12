<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIGatherInstanceVO"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>

<html>
<head>
<%
Context context=(Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String currentUserId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
String currentUserName = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME);
String mainInstanceID = request.getParameter("instanceId");
String mainNodeID = request.getParameter("nodeId");
String mainNodeName = (String)context.getDataValue("mainNodeName");
String bizSeqNo = request.getParameter("bizSeqNo");
String beforeInstanceID = request.getParameter("beforeInstanceId");
String gatherTitle = "";
String gatherDesc = "";
if(beforeInstanceID!=null && !beforeInstanceID.equals("")){
	WFIGatherInstanceVO befGatherInst = (WFIGatherInstanceVO)context.get("gahterInstanceIdVO");
	gatherTitle = befGatherInst.getGatherTitle();
	gatherDesc = befGatherInst.getGatherDesc();
}
%>

<title>发起会办</title>
<jsp:include page="/include.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/workflow/jquery-1.4.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/common.js"></script>

<script type="text/javascript">
	//初始化会办发起人
	function doLoad(){
		document.getElementById("gatherStartUserID").value="<%=currentUserId%>";
		document.getElementById("gatherStartUserName").value="<%=currentUserName%>";
		document.getElementById("gatherEndUserID").value="<%=currentUserId%>";
		document.getElementById("gatherEndUserName").value="<%=currentUserName%>"
	}
	
	/*
		选人
		@param count 1-单选 n-多选
		@param nameFlag 将ID赋值给nameFlag+"ID" 将Name赋值给nameFlag+"Name"
	*/
	function doSelectUser(count,nameFlag){
		var contextPath="<%=request.getContextPath()%>";
		//打开选择处理人的界面
		var url = contextPath+'/selectAllUser.do?count='+count+'&EMP_SID=<%=request.getParameter("EMP_SID")%>&rd='+Math.random();
		var retObj = window.showModalDialog(url,'selectGatherPage','dialogHeight:500px;dialogWidth:750px;help:no;resizable:no;status:no;');
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
				document.getElementById(nameFlag+"ID").value =seluser;
			}else{
				document.getElementById(nameFlag+"ID").value = "U."+retObj[1];
			}
		}
		if(retObj[2] != null)
			document.getElementById(nameFlag+"Name").value = retObj[2];
	}

	//发起新会办
	function doStartGather(){
		//校验会办的发起人和汇总人不是是会办参与人
		var gatherEndUserID = document.getElementById("gatherEndUserID").value;
		var gatherStartUserID = document.getElementById("gatherStartUserID").value;
		var currentGatherUserListID = document.getElementById("currentGatherUserListID").value;
		var gatherTitle = document.getElementById("gatherTitle").value;
		if(currentGatherUserListID=="" || currentGatherUserListID==null || currentGatherUserListID=="undefined"){
			alert("会办参与人不能为空!");
			return ;
			
		}
		if(gatherTitle=="" || gatherTitle==null || gatherTitle=="undefined"){
			alert("会办标题不能为空!");
			return ;
		}
		var ids = currentGatherUserListID.split(";");
		for(var i=0; i<ids.length; i++){
			var userid = ids[i];
			if(userid == "U."+gatherEndUserID){
				alert("会办参与人不能是会办的汇总人");
				return ;
			}
			if(userid == "U."+gatherStartUserID){
				alert("会办参与人不能是会办的发起人");
				return ;
			}
		}
		initGather();
	}
	
	function initGather() {
		var form = document.getElementById("form1");
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				var flag = jsonstr.flag;
				if(flag == 0) {
					alert('发起会办成功！');
					window.close();
				} else {
					alert('发起会办失败！'+flag);
				}
			}catch(e) {
				alert('发起会办失败！'+o.responseText);
				return;
			}
		};		
		var handleFailure = function(o){	
			alert(o.responseText);
		};		
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);			
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action, callback,postData);
	}

	//重置
	function doCancel(){
		window.close();
	
	}
</script>
</head>
<body onload="doLoad()">
<form action="startGather.do" name="form1" id="form1">
	<input type="hidden" id="mainInstanceID" name="mainInstanceID" value="<%=mainInstanceID %>"/>
	<input type="hidden" id="mainNodeID" name="mainNodeID"  value="<%=mainNodeID %>"/>
	<input type="hidden" id="mainNodeName" name="mainNodeName" value="<%=mainNodeName %>"/>
	<input type="hidden" id="beforeInstanceID" name="beforeInstanceID" value="<%=beforeInstanceID==null?"":beforeInstanceID %>"/>
	<input type="hidden" id="bizSeqNo" name="bizSeqNo" value="<%=bizSeqNo %>"/>
	<input type="hidden" id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}"/>

	<table border="0" cellspacing="0" cellpadding="0" class="tablemain2">
		<tr>
			<td class="trtitle" colspan="4">发起会办</td>
		</tr>
		<tr>
			<td align="right">会办发起人：</td>
			<td>
				<input type="hidden" id="gatherStartUserID" name="gatherStartUserID">
				<input type="text" id="gatherStartUserName" name="gatherStartUserName" readonly="true" style="background-color:#e3e4e3;width:165">
			</td>
			<td align="right">会办汇总人：</td>
			<td >
				<input type="hidden" id="gatherEndUserID" name="gatherEndUserID" style="width:95px">
				<input type="text" id="gatherEndUserName" readonly="true" style="width:95px">
				<input name="Submit" type="button" style="width:35px" class="button" value="..." onClick="doSelectUser('1','gatherEndUser')">
			</td>
		</tr>
		<tr>
			<td align="right">会办参与人：</td>
			<td colspan="3">
				<input type="hidden" id="currentGatherUserListID" name="currentGatherUserListID" style="width:400px">
				<input type="text" id="currentGatherUserListName" readonly="true" style="width:300px">
				<input name="Submit" type="button" style="width:35px" class="button" value="..." onClick="doSelectUser('n','currentGatherUserList')">
			</td>
		</tr>
		<tr>
			<td align="right">会办主题：</td>
			<td colspan="3"><input type="text" id="gatherTitle" name="gatherTitle" style="width:300px" value="<%=gatherTitle %>"></td>
		</tr>
		<tr>
			<td align="right">会办描述：</td>
			<td colspan="3"><textarea id="gatherDesc" name="gatherDesc" rows="5" cols="50" onblur="checkstr(this,1600,'gatherDescChick')"><%=gatherDesc %></textarea><br><span id="gatherDescChick" style="color: red;"></span></td>
		</tr>
	</table>
	<br>
	  <div align="center">
	  		<input name="Submit" type="button" class="button" value="发起会办" onClick="doStartGather()">&nbsp;&nbsp;
	  		<input name="Submit2" type="button" class="button" value="重置" onClick="doCancel()">
	  </div>
	
</form>
</body>
</html>

