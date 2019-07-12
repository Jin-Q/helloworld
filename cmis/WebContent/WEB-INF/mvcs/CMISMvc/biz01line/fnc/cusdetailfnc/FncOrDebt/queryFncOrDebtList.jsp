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
		FncOrDebt._toForm(form);
		FncOrDebtList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateFncOrDebtPage() {
		var paramStr = FncOrDebtList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncOrDebtUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncOrDebt() {
		var paramStr = FncOrDebtList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncOrDebtViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddFncOrDebtPage() {
		var url = '<emp:url action="getFncOrDebtAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncOrDebt() {
		var paramStr = FncOrDebtList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncOrDebtRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.FncOrDebtGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="FncOrDebtGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="FncOrDebt.cus_id" label="客户码" />
			<emp:text id="fnc_ym" label="年月" />
			<emp:select id="FncOrDebt.fnc_or_debt_typ" label="或有负债类型" dictname="STD_ZB_FNC_ORDT_TYP"  />
			<emp:text id="fnc_amt" label="金额" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddFncOrDebtPage" label="新增" op="add"/>
		<emp:button id="getUpdateFncOrDebtPage" label="修改" op="update"/>
		<emp:button id="deleteFncOrDebt" label="删除" op="remove"/>
		<emp:button id="viewFncOrDebt" label="查看" op="view"/>
	</div>

	<emp:table icollName="FncOrDebtList" pageMode="true" url="pageFncOrDebtQuery.do">
		<emp:text id="cus_id" label="客户码" />
		
        <emp:text id="fnc_or_debt_typ" label="或有负债类型" dictname="STD_ZB_FNC_ORDT_TYP"  />
       
		<emp:text id="fnc_amt" label="金额" dataType="Currency"/>
		
		<emp:text id="fnc_input_id" label="干系人" />
		<emp:text id="input_id_displayname" label="登记人"/>
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="fnc_input_date" label="日期" />
	    <emp:text id="remark" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" hidden="true" />
		 <emp:text id="fnc_or_debt_des" label="或有负债明细" hidden='true'/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="pk_id" label="主键" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    