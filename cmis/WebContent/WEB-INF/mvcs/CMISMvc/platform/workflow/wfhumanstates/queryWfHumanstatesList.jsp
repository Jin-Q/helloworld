<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>工作委托设置列表</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfHumanstates._toForm(form);
		WfHumanstatesList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfHumanstatesPage() {
		var paramStr = WfHumanstatesList._obj.getParamStr(['pkey']);
		if (paramStr != null) {
			if(!checkSelf()) {
				alert('对不起，您不是委托人，不能进行此操作！');
				return;
			}
			var url = '<emp:url action="getWfHumanstatesUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfHumanstates() {
		var paramStr = WfHumanstatesList._obj.getParamStr(['pkey']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfHumanstatesViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfHumanstatesPage() {
		var url = '<emp:url action="getWfHumanstatesAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfHumanstates() {
		var paramStr = WfHumanstatesList._obj.getParamStr(['pkey']);
		if (paramStr != null) {
			if(!checkSelf()) {
				alert('对不起，您不是委托人，不能进行此操作！');
				return;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfHumanstatesRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function checkSelf() {
		var userid = WfHumanstatesList._obj.getParamValue(['userid']);
		var curUser = '${context.currentUserId}';
		//alert(curUser+'='+userid);
		if(userid == curUser) {
			return true;
		}
		return false;
	}
	function doReset(){
		page.dataGroups.WfHumanstatesGroup.reset();
	};
	
	/*--user code begin--*/
	function doQueryByVicarList() {
		var url = "<emp:url action='queryByVicarList.do'/>?menuId=${context.menuId}"
		url = EMPTools.encodeURI(url);
		//window.open(url);
		window.location = url;
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="WfHumanstatesGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="WfHumanstates.vicar" label="被委托人用户号" />
			<emp:text id="WfHumanstates.vicarname" label="被委托人姓名" />
			<emp:text id="WfHumanstates.userid" label="委托人用户号" defvalue="${context.currentUserId}" hidden="true"/>
			<%/*<emp:datespace id="WfHumanstates.begintime" label="开始时间" colSpan="2"/>*/%>
			<%/*<emp:datespace id="WfHumanstates.endtime" label="结束时间" colSpan="2"/>*/%>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfHumanstatesPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfHumanstatesPage" label="修改" op="update"/>
		<emp:button id="deleteWfHumanstates" label="删除" op="remove"/>
		<emp:button id="viewWfHumanstates" label="查看" op="view"/>
		<%-- <emp:button id="queryByVicarList" label="委托登记簿"/> --%>
	</div>

	<emp:table icollName="WfHumanstatesList" pageMode="true" url="pageWfHumanstatesQuery.do">
		<emp:text id="pkey" label="流水号" hidden="true"/>
		<emp:text id="userid" label="委托人用户名" />
		<emp:text id="username" label="委托人姓名" />
		<emp:text id="vicar" label="被委托人用户名" />
		<emp:text id="vicarname" label="被委托人姓名" />
		<emp:text id="vicarioustype" label="代办类型" hidden="true"/>
		<emp:text id="vicarmemo" label="说明" hidden="true"/>
		<emp:text id="onoff" label="是否启用" hidden="true"/>
		<emp:text id="appid" label="APPID" hidden="true"/>
		<emp:text id="appname" label="APPNAME" hidden="true"/>
		<emp:text id="wfid" label="WFID" hidden="true"/>
		<emp:text id="wfname" label="WFNAME" hidden="true"/>
		<emp:text id="begintime" label="开始时间" />
		<emp:text id="endtime" label="结束时间" />
		<emp:text id="orgid" label="ORGID" hidden="true"/>
		<emp:text id="sysid" label="SYSID" hidden="true"/>
		<emp:select id="appid" label="申请类型" dictname="ZB_BIZ_CATE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    