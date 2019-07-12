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
		PrdBankInfo._toForm(form);
		PrdBankInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdBankInfoPage() {
		var paramStr = PrdBankInfoList._obj.getParamStr(['bank_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdBankInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdBankInfo() {
		var paramStr = PrdBankInfoList._obj.getParamStr(['bank_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdBankInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdBankInfoPage() {
		var url = '<emp:url action="getPrdBankInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdBankInfo() {
		var paramStr = PrdBankInfoList._obj.getParamStr(['bank_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdBankInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdBankInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdBankInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdBankInfo.bank_no" label="行号" />
			<emp:text id="PrdBankInfo.bank_name" label="行名" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdBankInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdBankInfoPage" label="修改" op="update"/>
		<emp:button id="deletePrdBankInfo" label="删除" op="remove"/>
		<emp:button id="viewPrdBankInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdBankInfoList" pageMode="true" url="pagePrdBankInfoQuery.do">
		<emp:text id="bank_no" label="行号" />
		<emp:text id="bank_name" label="行名" />
		<emp:text id="area_code" label="地区代码" />
		<emp:text id="phone" label="联系电话" />
		<emp:text id="pcode" label="邮政编码" />
		<emp:text id="addr" label="地址" />
		<emp:text id="last_bank_no" label="上级行号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    