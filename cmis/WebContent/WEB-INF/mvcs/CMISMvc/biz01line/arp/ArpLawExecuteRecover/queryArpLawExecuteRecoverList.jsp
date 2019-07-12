<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpLawExecuteRecover._toForm(form);
		ArpLawExecuteRecoverList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpLawExecuteRecoverPage() {
		var paramStr = ArpLawExecuteRecoverList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawExecuteRecoverUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpLawExecuteRecover() {
		var paramStr = ArpLawExecuteRecoverList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpLawExecuteRecoverViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpLawExecuteRecoverPage() {
		var url = '<emp:url action="getArpLawExecuteRecoverAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawExecuteRecover() {
		var paramStr = ArpLawExecuteRecoverList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteArpLawExecuteRecoverRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpLawExecuteRecoverGroup.reset();
	};
	
	/*--user code begin--*/
	function doLoad(){
		case_no = "${context.case_no}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpLawExecuteRecoverGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="ArpLawExecuteRecover.reclaim_type" label="回收类型" dictname="STD_ZB_RECLAIM_TYPE" />
			<emp:select id="ArpLawExecuteRecover.phase" label="阶段" dictname="STD_ZB_LAWSUIT_PHASE" />
	</emp:gridLayout>
 	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddArpLawExecuteRecoverPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpLawExecuteRecoverPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpLawExecuteRecover" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawExecuteRecover" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpLawExecuteRecoverList" pageMode="true" url="pageArpLawExecuteRecoverQuery.do?case_no=${context.case_no}">
		<emp:text id="reclaim_type" label="回收类型" dictname="STD_ZB_RECLAIM_TYPE" />
		<emp:text id="phase" label="阶段" dictname="STD_ZB_LAWSUIT_PHASE" />
		<emp:text id="pay_amt" label="支出金额" dataType="Currency"/>
		<emp:text id="reclaim_amt" label="回收金额" dataType="Currency"/>
		<emp:text id="reclaim_date" label="回收日期" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    