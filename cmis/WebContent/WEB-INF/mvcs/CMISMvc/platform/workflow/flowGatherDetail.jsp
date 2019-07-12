<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.WorkFlowConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIGatherInstanceVO"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>
<html>
<head>
<%
String editFlag = request.getParameter("editFlag");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String currentUserID = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
String orgid = (String)context.getDataValue(CMISConstance.ATTR_ORGID);
WFIGatherInstanceVO instanceVO = (WFIGatherInstanceVO)context.getDataValue("gatherInstance");
String gatherStartUserID = instanceVO.getGatherStartUserId();
String gatherEndUserID = instanceVO.getGatherEndUserId();
String currentGatherUserList = instanceVO.getCurrentGatherUserList()==null?"":instanceVO.getCurrentGatherUserList();
String currentGatherProcessors = instanceVO.getCurrentGatherProcessors();
String gatherEndTime = instanceVO.getGatherEndTime();
//取流程意见
List<WFIGatherCommentVO> gatherWfComment = (List<WFIGatherCommentVO>)context.getDataValue("gatherWfComment");
//取会办意见
List<WFIGatherCommentVO> gatherComment = (List<WFIGatherCommentVO>)context.getDataValue("gatherComment");
int checkFlag = (Integer)context.getDataValue("checkFlag");
//按钮设置：submitGather提交会办，newGather发起新会办，changeGather转办会办，resetGather重置会办参与人，endGather结束会办；有权限设置值为字符串true，否则表示无权限
Map actionMap = (Map)context.getDataValue("actionMap");
String empSid = (String)context.getDataValue("EMP_SID");
%>

<title>会办办理</title>
<jsp:include page="/include.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/workflow/jquery-1.4.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />

<script type="text/javascript">
//异步请求通用方法
	function doGather() {
		var form = document.getElementById("form1");
		var handleSuccess = function(o){
			try {
				var jsonstr = eval("("+o.responseText+")");
				if(jsonstr==null||jsonstr==''){
			  		  alert("操作失败");
			    } else{
				    var result = jsonstr.result;
				    var sign = result.sign;
				    var msg = result.msg;
				   	if(sign==0){
				   		alert(msg);
				   		window.returnValue='resh';
				   	} else{
				   		alert(msg);
				   	}
				}
			    window.close();
			      
			}catch(e) {
				alert('操作异常！'+o.responseText);
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

	//流程意见时，调用交办；会办意见，调用提交汇总人
	function doCIBChange(){
		var sugTypeEle = document.getElementsByName("suggest_type");
		var sugTypeValue = "";
		for(var i=0; i<sugTypeEle.length; i++){
			if(sugTypeEle[i].checked){
				sugTypeValue = sugTypeEle[i].value;
			}
		}
		
		//会办意见
		if(sugTypeValue=="1"){
			doSubmit();
		}
		//流程意见
		else if(sugTypeValue=="0"){
			doChangeGather();
		}
	}
	
	//交办
	function doChangeGather(){
		var suggest = document.getElementById("suggest").value;
		if(suggest==null || suggest==""){
			alert("会办意见不能为空");
			return;
		}
		
		var contextPath="<%=request.getContextPath()%>";
		var orgid = "<%=orgid%>";
		//打开选择处理人的界面
		var url = contextPath+"/selectAllUser.do?count=1&orgIdTmp="+orgid+"&EMP_SID=<%=empSid%>";
		var retObj = window.showModalDialog(url,'selectPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;');
			
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;

		var seluserID="";//用户编号
		var seluserName="";//用户名称
		
		if(retObj[1] != null){
			if(retObj[1].indexOf(";")!=-1){//返回多值
				var list=retObj[1].split(";");
				
				for(var i=0;i<list.length;i++){
					if(i>0)
						seluserID+=";U."+list[i];
					else
						seluserID+="U."+list[i];
				}
			}else{
				seluserID = "U."+retObj[1];
			}
		}
		if(retObj[2] != null)
			seluserName = retObj[2];

		//将用户更新到会办实例表
		if(seluserID == null || seluserID =="")
			return;

		//校验：不能交办给会办发起人和汇总人
		var gatherStartUserID = "<%=gatherStartUserID%>";
		var gatherEndUserID = "<%=gatherEndUserID%>";
		if(seluserID == "U."+gatherStartUserID){
			alert("如需将会办交办给会办发起人，请选择意见类型：【会办意见】");
			return ;
		}

		if(seluserID == "U."+gatherEndUserID){
			alert("如需将会办交办给会办汇总人，请选择意见类型：【会办意见】");
			return ;
		}

		if(!confirm("确定将会办交办给：【"+seluserName+"】？")){
			return ;
		}
		document.getElementById("nextUserID").value=seluserID;
		document.forms[0].actionType.value="changeGather";
		doGather();
	}
	
	//提交汇总人
	function doSubmit(){
		var suggest = document.getElementById("suggest").value;
		if(suggest==null || suggest==""){
			alert("会办意见不能为空");
			return;
		}
		
		if(confirm("确定将会办提交给汇总人？")){
			document.forms[0].actionType.value="gatherSubmit";
			doGather();
		}
	}

	//结束会办
	function doEndGather(){
		//校验：需填意见
		var suggest = document.getElementById("suggest").value;
		if(suggest==null || suggest==""	|| suggest=="undefined"){
			alert("请填写完意见后再结束会办");
			return ;
		}
		
		//校验：该会办参与人是否全部办理过，否给提提醒
		var sign = "<%=checkFlag%>";
		if(sign != 0){
			if(!confirm("还有会办参与人未填写意见！\n您确定要结束会办吗？")){
				return;
			}
		}

		if(confirm("您确定要结束会办吗？")){
			document.forms[0].actionType.value="endGather";
			doGather();
		}
	}
	
	//重置
	function doCancle(){
		window.close();
	}

	//重复会办参与人
	function doResetProcessor(){
		var contextPath="<%=request.getContextPath()%>";
		//打开选择处理人的界面
		var url = contextPath+'/selectAllUser.do?&count=n'+'&EMP_SID=<%=empSid%>';
		var retObj = window.showModalDialog(url,'selectPage','dialogHeight:460px;dialogWidth:700px;help:no;resizable:no;status:no;');
			
		//返回数组:[状态:true/false;意见;下一节点;下一处理人];若没有返回值,或返回状态不为true,则表示重置
		if(retObj == null)
			return;
		var status = retObj[0];
		if(status != true)
			return;

		var seluserID="";//用户编号
		var seluserName="";//用户名称
		
		if(retObj[1] != null){
			if(retObj[1].indexOf(";")!=-1){//返回多值
				var list=retObj[1].split(";");
				
				for(var i=0;i<list.length;i++){
					if(i>0)
						seluserID+=";U."+list[i];
					else
						seluserID+="U."+list[i];
				}
			}else{
				seluserID = "U."+retObj[1];
			}
		}
		if(retObj[2] != null)
			seluserName = retObj[2];

		//将用户更新到会办实例表
		var instanceID = "<%=instanceVO.getInstanceId()%>";
		var url = contextPath+"/resetGatherProcessor.do?&instanceID="+instanceID+"&currentGatherUserList="+seluserID+'&EMP_SID=<%=empSid%>';
		var retObj2 = window.showModalDialog(url,'selectPage','dialogHeight:400px;dialogWidth:600px;help:no;resizable:no;status:no;');
		var sign = retObj2[0];
		if(sign!=0){
			alert("重置会办参会人员失败！已经有参会人员提交意见");
		}else{
			alert("重置会办参会人员成功");
			document.getElementById("currentGatherUserListID").value=seluserID;
			document.getElementById("currentGatherUserListName").value=seluserName;
		}
		
	}

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

	function doLoad(){
		//会办按钮权限、
		var currentUserID = "<%=currentUserID%>";
		var gatherStartUserID = "<%=gatherStartUserID%>";
		var gatherEndUserID = "<%=gatherEndUserID%>";
	}

	//0-所有人可见
	function change_control(){
		if(document.getElementById("suggestControl").checked)
			document.getElementById("suggestControl").value="0";
		else
			document.getElementById("suggestControl").value="1";
	}

	//默认会办意见：其它部门可读；流程意见：其它部门不可读
	function suggestTypeChange(ele){
		var sugType = ele.value;
		var sugControl = document.getElementById("suggestControl");
		if(sugType=="1"){
			sugControl.checked=true;
		}else{
			sugControl.checked=false;
		}
	}
	
	//字符长度校验
	function checkstr(obj, maxLength,checkSpan) {
		var str = obj.value;
		charLength = str.length ;
		var arr = str.match(/[^\x00-\x80]/ig);
		if (arr != null)
			charLength += arr.length;
		var oInput = document.getElementById(obj.id);
		var msgChick = document.getElementById(checkSpan);
		if (charLength > maxLength) {
			msgChick.innerHTML = '超过字符串长度：'
					+ maxLength;
			if (oInput == null) {
				oInput = event.srcElement;
			}
			var rtextRange = oInput.createTextRange();
			rtextRange.moveStart('character', oInput.value.length);
			rtextRange.collapse(true);
			rtextRange.select();
		} else {
			msgChick.innerHTML="";
		}
	}
		var win;
	function showta(id){
		if(typeof win!='undefined')
		win.close();
		win=new Ext.Window({title:'<div style="height:15px;padding-top:8px">意见查看</div>',width:400,height:250,html:document.getElementById(id).value});
		win.show();
	}
</script>
</head>
<body onload="doLoad()">
<form action="submitGather.do" id="form1">
	<input type="hidden" id="actionType" name="actionType" />
	<input type="hidden" id="instanceId" name="instanceId" value="<%=instanceVO.getInstanceId() %>">
	<input type="hidden" id="nextUserId" name="nextUserId" />
	<input type="hidden" id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}"/>

	<br>
	<fieldset><legend>会办处理</legend>
	<br>
	<table cellspacing=1 cellpadding=0>
		<tr>
			<td align="right" style="width:80px">会办发起人：</td>
			<td>
				<input type="hidden" id="gatherStartUserID" name="gatherStartUserID value="<%=instanceVO.getGatherStartUserId() %>">
				<input type="text" id="gatherStartUserName" readonly="true" style="background-color:#e3e4e3;width:165" value="<%=instanceVO.getGatherStartUserName() %>">
			</td>
			<td align="right" style="width:80px">会办汇总人：</td>
			<td >
				<input type="hidden" id="gatherEndUserID" name="gatherEndUserID" value="<%=instanceVO.getGatherEndUserId() %>">
				<input type="text" id="gatherEndUserName" readonly="true" style="background-color:#e3e4e3;width:140px" readonly="true" value="<%=instanceVO.getGatherEndUserName() %>">
			</td>
		</tr>
		<tr>
			<td align="right" style="width:80px">会办参与人：</td>
			<td colspan="3">
				<input type="hidden" id="currentGatherUserListID" name="currentGatherUserListID" style="background-color:#F7FEA5;width:400px">
				<input type="text" id="currentGatherUserListName" readonly="true" style="background-color:#e3e4e3;width:440px" readonly="true" value="<%=instanceVO.getAllProcessorName() %>">
			</td>
		</tr>
		<tr>
			<td align="right" style="width:80px">会办主题：</td>
			<td colspan="3"><input type="text" id="gatherTitle" name="gatherTitle" style="background-color:#e3e4e3;width:440px" readonly="true" value="<%=instanceVO.getGatherTitle() %>"></td>
		</tr>
		<tr>
			<td align="right" style="width:80px">会办描述：</td>
			<td colspan="3"><textarea id="gatherDesc" name="gatherDesc" rows="5" cols="60" style="background-color:#e3e4e3" readonly="true" ><%=instanceVO.getGatherDesc()==null?"":instanceVO.getGatherDesc() %></textarea></td>
		</tr>
	</table>
	<br></fieldset><br>
	
	<fieldset><legend>流程意见列表</legend><br>
		<table class=tablemain cellspacing=1 cellpadding=0>
			<tr class=trtitle>
				<td width="5%">序号</td>
				<td width="18%">会办时间</td>
				<td width="17%">办理人</td>
				<td width="60%">流程意见</td>
			</tr>
			
			<%
				WFIGatherCommentVO actionVO = null;
				boolean tr = true;
				int k=1;
				if(gatherWfComment==null || gatherWfComment.isEmpty()){
					out.print("<tr class=trclass><td colspan='4'>没有流程意见信息</td></tr>");
				}
				
				for(int i=0; i<gatherWfComment.size(); i++){
					actionVO = (WFIGatherCommentVO)gatherWfComment.get(i);
					if(tr){
						out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}else{
						out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}
			%>
				<td><%=k %></td>
				<td><%=actionVO.getActTime() %></td>
				<td><%=actionVO.getTransActorName() %></td>
				<td><a href="#" onclick="showta('wf_<%=k%>')"><%=actionVO.getSuggest()==null?"":actionVO.getSuggest().length()>30?actionVO.getSuggest().substring(0,30)+"...":actionVO.getSuggest() %></a></td>
				<input type="hidden" id="wf_<%=k%>" value="<%=actionVO.getSuggest()%>">
			</tr>	
			<%
					tr = !tr;
					k++;
				}	
			%>
			
		</table>
		<br>
	</fieldset>
	<br>
	
	<fieldset><legend>会办意见列表</legend><br>
		<table class=tablemain cellspacing=1 cellpadding=0>
			<tr class=trtitle>
				<td width="5%">序号</td>
				<td width="18%">会办时间</td>
				<td width="17%">办理人</td>
				<td width="60%">会办意见</td>
			</tr>
			
			<%
			WFIGatherCommentVO actionVOb = null;
				tr = true;
				k=1;
				
				if(gatherComment==null || gatherComment.isEmpty()){
					out.print("<tr class=trclass><td colspan='4'>没有会办意见信息</td></tr>");
				}
				
				for(int i=0; i<gatherComment.size(); i++){
					actionVOb = (WFIGatherCommentVO)gatherComment.get(i);
					if(tr){
						out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}else{
						out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}
			%>
				<td><%=k %></td>
				<td><%=actionVOb.getActTime() %></td>
				<td><%=actionVOb.getTransActorName() %></td>
				<td><a href="#" onclick="showta('wfg_hb_<%=k%>')"><%=actionVOb.getSuggest()==null?"":actionVOb.getSuggest().length()>30?actionVOb.getSuggest().substring(0,30)+"...":actionVOb.getSuggest() %></a></td>
				<input type="hidden" id="wfg_hb_<%=k%>" value='<%=actionVOb.getSuggest()==null?"":actionVOb.getSuggest()%>'>
			</tr>	
			<%
					tr = !tr;
					k++;
				}	
			%>
			
		</table>
		<br>
	</fieldset>
		<%
		if(editFlag!=null && editFlag.equals("2")){
			
		}else{
	%>
	<br>
	<fieldset><legend align="right" style="font-style:italic">意见填写</legend><br>
		意见类型：<input type="radio" id="suggest_type" name="suggest_type" value="1" checked="checked"  onclick="suggestTypeChange(this)">会办意见
		<input type="radio" id="suggest_type" name="suggest_type" value="0"     onclick="suggestTypeChange(this)">流程意见
		<br><br>
		<b>填写意见内容：</b><br>
		<textarea id="suggest" name="suggest" rows="5" cols="100" onblur="checkstr(this,800,'suggestChick')"></textarea><span id="suggestChick" style="color: red;"></span>
		<br>
		<input type="checkbox" id="suggestControl" name="suggestControl" value="0" checked="true" onchange="change_control()" >其它部门可读<br><br>
		<br>	
	</fieldset>
	<br>
	<%
	}
	%>	
	<div align="center">
	<%
		if(editFlag!=null && editFlag.equals("2")){
			
		}else{
	%>
		
		<% if(actionMap!=null && (actionMap.get("submitGather")!=null)||actionMap.get("changeGather")!=null) {%>
				<input name="Submit2" type="button" class="button" value="交　　办" onClick="doCIBChange()">
		<%}%>
		<% if(actionMap!=null && actionMap.get("endGather")!=null) {%>
				<input name="Submit3" type="button" class="button" value="结束会办" onClick="doEndGather()">
		<%}%>
		<% if(actionMap!=null && actionMap.get("resetGather")!=null) {%>
				<input name="Submit4" type="button" class="button" value="重置会办参与人" onClick="doResetProcessor()">
		<%}%>
	<%
	}
	%>	
	</div>

</form>
</body>
</html>
