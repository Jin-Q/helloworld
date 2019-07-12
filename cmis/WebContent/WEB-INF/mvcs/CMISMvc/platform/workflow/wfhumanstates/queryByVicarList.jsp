<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>委托登记簿</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">

	.spanStyle {
		line-height: 0px;
	}
	
	.emp_table .tdStyle {
		text-align: center;
		padding-top: 10px;
		width: auto;
	}
	.emp_field_select_select {
		border: 1px solid #b7b7b7;
		text-align: left;
		width: auto;
	}
	
	
</style>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		VicarList._toForm(form);
		VicarListRs._obj.ajaxQuery(null,form);
	};
	function doReset() {
		page.dataGroups.VicarListGroup.reset();
	}
	function doSel(){
		//var wfstatus = VicarListRs._obj.getSelectedData()[0].wfstatus._getValue();
		var instanceid=VicarListRs._obj.getSelectedData()[0].instanceid._getValue();
		//var nodeid=VicarListRs._obj.getSelectedData()[0].nodeid._getValue();
		var nodeid='-';
		var wfsign=VicarListRs._obj.getSelectedData()[0].wfsign._getValue();
		var url = "";

		//当流程结束时表单ID设置为审批表单  1：结束
		//if(wfstatus != '1') {
		//	var formid = VicarListRs._obj.getSelectedData()[0].wfnodeformid._getValue();
		//	url = "<emp:url action='getInstanceView.do'/>"+
		//		"?instanceid="+instanceid+"&nodeid="+nodeid+"&formid="+formid+"&wfsign="+wfsign+"&isEnd=0";
	//	} else {
			//流程结束
		//	var formid = "opinion";
	//		url = "<emp:url action='getInstanceInfo.do'/>"+
	//			"?instanceid="+instanceid+"&nodeid="+nodeid+"&formid="+formid+"&wfsign="+wfsign+"&isEnd=1";
	//	}
		url = "<emp:url action='getInstanceView.do'/>"+"?instanceid="+instanceid+"&wfsign="+wfsign+"&nodeid="+nodeid;
		url += "&menuId=${param.menuId}";
		url = EMPTools.encodeURI(url);
		window.open(url,'newname','width=800,height=600,menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes');
		//window.location =url;
	};

	function doMyTrack() {
		if(VicarListRs._obj.getSelectedData().length == 0) {
			alert("请选择一条记录。");
			return;
		}
		var form = document.getElementById("form");
		var instanceid=VicarListRs._obj.getSelectedData()[0].instanceid._getValue();
		form.instanceid.value = instanceid;
		doTrack(form);
	};
	function doReturnBack() {
		var url = "<emp:url action='queryWfHumanstatesList.do' />?menuId=${context.menuId}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
</script>
</head>
<body class="page_content">
	<form action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="VicarListGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="VicarList.pk_value" label="业务流水号" />
		<emp:text id="VicarList.cus_name" label="客户名称" />
		<emp:text id="VicarList.currentnodeuser" label="被委托人" />
		<emp:select id="VicarList.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
	</emp:gridLayout>
	<table width="100%"  align="center"  class="searchTb">
		<tr>
			<td colspan="4"/>
			<div align="center">
				<emp:button id="query" label="查询"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<emp:button id="reset" label="重置"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<emp:button id="returnBack" label="返回授权管理"/>
			</div>
		</tr>
	</table>

	<emp:table icollName="VicarListRs" pageMode="true" url="pageQueryByVicarList.do" >
		<emp:link id="pk_value" label="业务流水号" operation="sel" />
		<emp:link id="appl_type" label="业务类型" dictname="ZB_BIZ_CATE" operation="sel"/>
		<emp:link id="cus_name" label="客户名称" operation="sel" />
		<emp:text id="amt" label="申请金额" dataType="Currency" />
		<emp:text id="wfi_start_time" label="开始时间"/>
		<emp:text id="wfi_end_time" label="办结时间" />
		<emp:text id="nodename" label="上一办理环节"/>
		<emp:text id="originaluser_displayname" label="授权人" />
		<emp:text id="replacer_displayname" label="被授权人" />
		<emp:text id="originaluser" label="授权人" hidden="true"/>
		<emp:text id="replacer" label="被授权人" hidden="true"/>
	    <emp:text id="wfi_status" label="审批状态" dictname="WF_APP_STATUS" />
		<emp:text id="wfname" label="流程名称" hidden="true" />
	    <emp:text id="instanceid" label="流程实例号" hidden="true"/>
		<emp:text id="nodeid" label="节点号" hidden="true"/>
		<emp:text id="wfnodeformid" label="表单号" hidden="true" />
		<emp:text id="wfsign" label="流程标识" hidden="true"/>
	</emp:table>
	
	<form id="form" action="">
		<input id="instanceid" name="instanceid" value="" type="hidden" />
		<input id="currentuserid" name="currentuserid" value="${context.currentUserId}" type="hidden" />
		<input id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}" type="hidden" />
	</form>

</body>
</html>
</emp:page>