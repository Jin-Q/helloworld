<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspAltSignal._toForm(form);
		PspAltSignalList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.PspAltSignalGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = PspAltSignalList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspAltSignalGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspAltSignal.cus_id" label="客户编码" />
			<emp:select id="PspAltSignal.signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="PspAltSignalList" pageMode="true" url="pagePspAltSignalPopQuery.do?signalCond=${context.signalCond}">
		<emp:text id="pk_id" label="业务编号" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="signal_info" label="风险预警信息内容及影响" />
		<emp:text id="signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
		<emp:text id="last_date" label="预计持续时间（天）" />
		<emp:text id="disp_mode" label="处置措施及进展情况" />
		<emp:text id="signal_status" label="信号状态" dictname="STD_ZB_COMM_STATUS" />
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    