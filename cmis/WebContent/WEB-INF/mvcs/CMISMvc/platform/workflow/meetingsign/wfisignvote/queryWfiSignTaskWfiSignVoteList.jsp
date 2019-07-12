<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doResetWfiSignVote(){
	var dataRow =  WfiSignVoteList._obj.getSelectedData()[0];
	var approveStatus = dataRow.sv_status._getValue() ;
	if(checkStatus(approveStatus)){
	if(!confirm("复位操作将会把已经投票的任务,置为未投票状态以便贷审会成员重新投票! 是否继续?")){
			return ;
	}
	var paramStr = WfiSignVoteList._obj.getParamStr(['sv_vote_id']);
	if (paramStr!=null) {
		var url = '<emp:url action="resetSignVote.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){ 
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("投票记录复位失败,请联系管理员!");
					return;
				}
				var flag = jsonstr.success;
				if(flag=="true"){
						alert("投票记录复位成功!");
						window.parent.location.reload();
			    }else {
			     		alert("投票记录复位失败!");
			  	}      
			}
		};
		var handleFailure = function(o){ 
			EMPTools.unmask(); 
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		EMPTools.mask();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}else {
		alert('请先选择一条记录！');
	}
	}
	
}
	
	function doViewWfiSignVote() {
		var paramStr = WfiSignVoteList._obj.getParamStr(['sv_vote_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryWfiSignTaskWfiSignVoteDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function checkStatus(approve){
		if(approve=='211') {
			alert("对不起,该任务未投票!") ;
			return false ;
		}
		return true ;
	};
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
<%String status=request.getParameter("status"); %>
	<div align="left">
		<emp:button id="viewWfiSignVote" label="查看" />
		<%if(status.equals("212")) {%>
		<emp:button id="resetWfiSignVote" label="复位" />
		<%} %>
	</div>
	<emp:table icollName="WfiSignVoteList" pageMode="true" url="pageWfiSignTaskWfiSignVoteQuery.do" reqParams="WfiSignTask.st_task_id=$WfiSignTask.st_task_id;">
		<emp:text id="sv_vote_id" label="投票ID" hidden="true"/>
		<emp:text id="sv_exe_user" label="任务执行人" hidden="true"/>
		<emp:text id="sv_exe_user_displayname" label="任务执行人" />
		<emp:text id="sv_result" label="投票结果" dictname="WF_SIGN_RESULT" />
		<emp:text id="sv_status" label="任务状态" dictname="WF_SIGN_STATUS" />
		<emp:text id="sv_start_time" label="任务开始时间" />
		<emp:text id="sv_end_time" label="任务结束时间" />
		<emp:text id="sv_request_time" label="要求完成时间" hidden="true"/>
	</emp:table>
				
</body>
</html>
</emp:page>