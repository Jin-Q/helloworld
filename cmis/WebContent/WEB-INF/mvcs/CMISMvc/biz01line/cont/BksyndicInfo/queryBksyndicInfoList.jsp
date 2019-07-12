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
		BksyndicInfo._toForm(form);
		BksyndicInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateBksyndicInfoPage() {
		var paramStr = BksyndicInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getBksyndicInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewBksyndicInfo() {
		var paramStr = BksyndicInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getBksyndicInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddBksyndicInfoPage() {
		var url = '<emp:url action="getBksyndicInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteBksyndicInfo() {
		var paramStr = BksyndicInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteBksyndicInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.BksyndicInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="BksyndicInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="BksyndicInfo.serno" label="业务编号" />
			<emp:text id="BksyndicInfo.cont_no" label="合同编号" />
			<emp:text id="BksyndicInfo.prtcpt_bank_no" label="参与行行号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddBksyndicInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateBksyndicInfoPage" label="修改" op="update"/>
		<emp:button id="deleteBksyndicInfo" label="删除" op="remove"/>
		<emp:button id="viewBksyndicInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="BksyndicInfoList" pageMode="true" url="pageBksyndicInfoQuery.do">
		<emp:text id="prtcpt_bank_no" label="参与行行号" />
		<emp:text id="prtcpt_bank_name" label="参与行行名" />
		<emp:text id="prtcpt_role" label="参与角色" />
		<emp:text id="prtcpt_amt_rate" label="参与金额比例" />
		<emp:text id="prtcpt _cur_type" label="参与币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="prtcpt _amt" label="参与金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    