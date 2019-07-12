<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doOnLoad(){
		var update='${context.update}';
		var scope = '${context.scope}';
		if(scope == "2"){
			document.getElementById('button_deleteCusTrusteeLst').style.display='none';
		}
		if(update=='newView'){
			document.getElementById("button_getAddCusTrusteeLstPage").style.display='none';
			document.getElementById("button_deleteCusTrusteeLst").style.display='none';
		}
	}
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusTrusteeLst._toForm(form);
		CusTrusteeLstList._obj.ajaxQuery(null,form);
	};

	function doGetUpdateCusTrusteeLstPage() {
		var paramStr = CusTrusteeLstList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeLstUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusTrusteeLst() {
		var paramStr = CusTrusteeLstList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeLstViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusTrusteeLstPage() {
		
		 var paramStr="&consignor_br_id="+'${context.consignor_br_id}'
         +"&consignor_id="+'${context.consignor_id}'
         +"&trustee_br_id="+'${context.trustee_br_id}'
         +"&trustee_id="+'${context.trustee_id}'
         +"&serno="+'${context.serno}'
         +"&retract_date="+'${context.retract_date}'
         +"&trustee_date="+'${context.trustee_date}'
         +"&trustee_scope="+'${context.scope}'
         +"&scope="+'${context.scope}';
		var scope = '${context.scope}';
		
		if(scope=='1'){
			var url = '<emp:url action="getCusLoanRelListForTrustee.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else if(scope=='2'){
			if(CusTrusteeLstList._getSize()>0){
                alert("已经添加！");
                return;
			}
			var submit=document.getElementById('button_getAddCusTrusteeLstPage');
			submit.disabled=true;		
			var url = '<emp:url action="getCusLoanRelListForTrusteeMany.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
			setTimeout("restore();",10000);
		}
		
	};
	function restore(){
	       document.getElementById('button_getAddCusTrusteeLstPage').disabled=false;
		
	}
	
	function doDeleteCusTrusteeLst() {
		var paramStr = CusTrusteeLstList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusTrusteeLstRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusTrusteeLstGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">

	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusTrusteeLstGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusTrusteeLst.cus_id" label="客户码" />
			<emp:text id="CusTrusteeLst.cus_name" label="客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusTrusteeLstPage" label="添加托管明细" op="add"/>
		<emp:button id="deleteCusTrusteeLst" label="删除" op="remove"/>
		<!--<emp:button id="getUpdateCusTrusteeLstPage" label="修改" op="update"/>
		<emp:button id="viewCusTrusteeLst" label="查看" op="view"/>-->
	</div>

	<emp:table icollName="CusTrusteeLstList" pageMode="true" url="pageCusTrusteeLstQuery.do" reqParams="CusTrusteeLst.serno=$CusTrusteeLst.serno;">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    