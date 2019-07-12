<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>日终跑批</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	//跑批调用
	function doReRunTask(){
		//跑批前先查跑批信息表里面的任务状态以及启用标志看是否符合重跑要求
		var url1 = '<emp:url action="queryTaskStatus.do"/>';
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
					var url = '<emp:url action="reRunTask.do"/>';
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
		var url = '<emp:url action="queryDaybatTaskInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	setInterval("reFlush()",30000);	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	<emp:tab label="信贷跑批" id="base_tab" needFlush="true" initial="true" >
	<div align="left">
		<span>当前业务时间:</span><emp:date id="DaybatTaskInfo.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
	</div>
	<div align="left">
		<emp:button id="reRunTask" label="重新跑批"/>	
	</div>
	<emp:table icollName="DaybatTaskInfoList" pageMode="false" url="pageDaybatTaskInfoQuery.do">
		<emp:text id="rule_sn" label="任务序号" />
		<emp:text id="rule_num" label="任务代码" />
		<emp:text id="rule_name" label="任务名称" />
		<emp:text id="exec_res" label="任务状态(1.待执行2.执行中3.执行成功4.任务失败)" />
		<emp:text id="depend_obj" label="任务依赖" />
		<emp:text id="run_date" label="运行日期" />
		<emp:text id="rule_type" label="任务类型" />
		<emp:text id="err_msg" label="错误描述" />
		<emp:text id="use_flag" label="启用标志" />
		<emp:text id="change_flag" label="转变标志" />
		<emp:text id="start_time" label="开始时间" />
		<emp:text id="end_time" label="结束时间" />
	</emp:table>
	</emp:tab>
	<emp:tab label="网贷跑批" id="" url="queryBatTaskCfgList.do" needFlush="true" initial="false"></emp:tab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
    