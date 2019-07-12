<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>日终跑批</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	//跑批调用
	function doReRunTaskWD(){
		//跑批前先查跑批信息表里面的任务状态以及启用标志看是否符合重跑要求
		var url1 = '<emp:url action="queryTaskStatusWD.do"/>';
		url1 = EMPTools.encodeURI(url1);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var status = jsonstr.status;
				if(status == "success"){
					//检查结束，符合跑批要求调用跑批方法
					var url = '<emp:url action="reRunTaskWD.do"/>';
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == "success"){
								alert("跑批成功!");
								window.location.reload();
							}else {
								alert("跑批失败!");
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
					var obj2 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}else {
					return;
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url1, callback);
	}

	//每隔30秒给界面发送一次查询列表的请求
	function reFlush(){
			var url = '<emp:url action="queryBatTaskCfgList.do"/>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	setInterval("reFlush()",30000);
</script>

</head>
<body class="page_content">
	<div align="left">
		<span>当前业务时间:</span><emp:date id="DaybatTaskInfo.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
	</div>
	<div align="left">
		<emp:button id="reRunTaskWD" label="重新跑批"/>	
	</div>
	<emp:table icollName="BatTaskCfgList" pageMode="false" url="queryBatTaskCfgList.do" >
		<emp:text id="task_no" label="任务序号" />
		<emp:text id="task_class" label="任务执行类" />
		<emp:text id="task_name" label="任务名称" />
		<emp:text id="run_time_flag" label="任务执行时点标志" />
		<emp:text id="mutex_no_list" label="互斥编号清单" />
		<emp:text id="task_type" label="任务类型" />
		<emp:text id="ignore_signal_flag" label="是否忽略信号" />
		<emp:text id="use_flag" label="启用标志" />
		<emp:text id="signal_type" label="信号类型" />
		<emp:text id="first_run_time" label="最早开始时间" />
		<emp:text id="last_run_time" label="最晚开始时间" />
		<emp:text id="run_time_flag" label="任务执行点标志" /> 
	</emp:table>
</body>
</html>
</emp:page>
    