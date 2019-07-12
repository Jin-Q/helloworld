<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>

<html>
<head>
<title>子任务引入列表页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspBatchTask._toForm(form);
		PspBatchTaskList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.PspBatchTaskGroup.reset();
	};

	function doSelect(){
		doReturnMethod();
	}
	
	function doReturnMethod(){
		var data = PspBatchTaskList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择引入项");
		}else {

			var subTaskIdStr="";//子任务编号字符串，以","间隔
			var num = PspBatchTaskList._obj.getSelectedData().length;
			if (num != 0) {
				for(var i=0;i<num;i++){
					subTaskIdStr = subTaskIdStr+PspBatchTaskList._obj.getSelectedData()[i].task_id._getValue()+",";
				}
			}
			var handleSuccess = function(o){
				if(o.responseText !== undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("引入成功!");
						window.location.reload();
						window.opener.location.reload();
					}else if(flag =="timeDiff"){
						alert("贷后子任务的任务起始日期不一致，不允许引入!");
						window.location.reload();
						window.opener.location.reload();
					}else {
						alert("引入失败!");
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = '<emp:url action="importPspBatchTaskRel.do"/>?manager_id=${context.manager_id}&subTaskIdStr='+subTaskIdStr+"&batch_task_type=${context.batch_task_type}"+"&major_task_id=${context.major_task_id}";
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspBatchTaskGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspBatchTask.task_id" label="任务编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:returnButton id="s1" label="引入"/>
	</div>

	<emp:table icollName="PspBatchTaskList" pageMode="true" url="pagePspBatchTaskPop.do" reqParams="manager_id=${context.manager_id}&batch_task_type=${context.batch_task_type}" selectType="2">
			<emp:text id="task_id" label="任务编号" />
		    <emp:text id="cus_id" label="客户码"/>
		    <emp:text id="cus_id_displayname" label="客户名称" />
			<emp:text id="check_type" label="检查类型" dictname="STD_PSP_CHECK_TYPE" hidden="true" />
			<emp:text id="qnt" label="笔数" />
			<emp:text id="loan_totl_amt" label="贷款总金额" dataType="Currency"/>
			<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
			<emp:text id="task_create_date" label="任务生成日期"/>
			<emp:text id="task_request_time" label="要求完成时间"/>
			<emp:text id="check_freq" label="检查频率" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    