<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../SHolidayRegister/SPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SHolidayRegister._toForm(form);
		SHolidayRegisterList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.SHolidayRegisterGroup.reset();
	};
	
	function doGetUpdateSHolidayRegisterPage() {
		var paramStr = SHolidayRegisterList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = SHolidayRegisterList._obj.getParamValue('status');
			if(status != '00'){
			    alert("只能操作登记状态的记录！");
			    return ;
			}
			
			var url = '<emp:url action="getSHolidayRegisterUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSHolidayRegister() {
		var paramStr = SHolidayRegisterList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSHolidayRegisterViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSHolidayRegisterPage() {
		var url = '<emp:url action="getSHolidayRegisterAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSHolidayRegister() {
		var paramStr = SHolidayRegisterList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = SHolidayRegisterList._obj.getParamValue('status');
			if(status != '00'){
			    alert("只能操作登记状态的记录！");
			    return ;
			}
			
			var url = '<emp:url action="deleteSHolidayRegisterRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
	function doOprantSHolidayRegister(){
		var paramStr = SHolidayRegisterList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = SHolidayRegisterList._obj.getParamValue('status');
			if(status != '00'){
			    alert("只能操作登记状态的记录！");
			    return ;
			}

			var actorno = SHolidayRegisterList._obj.getParamValue(['actorno']);
			var url="<emp:url action='CheckSHolidayRegister.do'/>&type=checkUser&value="+actorno;
			doPubCheck(url,resultOprant);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function resultOprant(flag){
		if(flag == 'success'){
			var serno = SHolidayRegisterList._obj.getParamValue(['serno']);
			var url="<emp:url action='CheckSHolidayRegister.do'/>&type=oprant&value="+serno;
			doPubCheck(url,result);
		}else{
			alert("此用户存在休假日期重复的记录!");
		}
	};

	function result(flag){
		if(flag == 'success'){
			alert("休假记录已生效!");
			window.location.reload();
		}else{
			alert("操作失败!");
		}
	};
	
	function doLoseSHolidayRegister(){
		var paramStr = SHolidayRegisterList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = SHolidayRegisterList._obj.getParamValue('status');
			if(status != '01'){
			    alert("只能操作有效状态的记录！");
			    return ;
			}
			
			var serno = SHolidayRegisterList._obj.getParamValue(['serno']);
			var url="<emp:url action='CheckSHolidayRegister.do'/>&type=lose&value="+serno;
			doPubCheck(url,resultLose);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function resultLose(flag){
		if(flag == 'success'){
			alert("休假记录已终止!");
			window.location.reload();
		}else{
			alert("操作失败!");
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SHolidayRegisterGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SHolidayRegister.actorno" label="用户码" />
			<emp:select id="SHolidayRegister.status" label="状态" dictname="STD_DRFPO_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSHolidayRegisterPage" label="新增" op="add"/>
		<emp:button id="getUpdateSHolidayRegisterPage" label="修改" op="update"/>
		<emp:button id="deleteSHolidayRegister" label="删除" op="remove"/>
		<emp:button id="viewSHolidayRegister" label="查看" op="view"/>
		<emp:button id="oprantSHolidayRegister" label="生效" op="oprant"/>
		<emp:button id="loseSHolidayRegister" label="终止" op="lose"/>
	</div>

	<emp:table icollName="SHolidayRegisterList" pageMode="true" url="pageSHolidayRegisterQuery.do">
		<emp:text id="serno" label="流水号" hidden="true"/>
		<emp:text id="actorno" label="用户码" />
		<emp:text id="actorno_displayname" label="用户名称" />
		<emp:text id="begin_date" label="开始日期" />
		<emp:text id="plan_end_date" label="预计到期日期" />
		<emp:text id="real_end_date" label="实际到期日期" />
		<emp:text id="input_date" label="登记日期" hidden="true" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="status" label="状态" dictname="STD_DRFPO_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    