<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	var cus_id;
	var app_serno;

	function doOnload(){
		cus_id = "${context.cus_id}";
		app_serno = "${context.serno}";
		ops = "${context.ops}";
		if(ops == 'view'){
			document.getElementById('button_getAddCusOrgInfoPage').style.display = 'none';
			document.getElementById('button_getUpdateCusOrgInfoPage').style.display = 'none';
			document.getElementById('button_deleteCusOrgInfo').style.display = 'none';
		}
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusOrgInfo._toForm(form);
		CusOrgInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusOrgInfoPage() {
		var paramStr = CusOrgInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusOrgInfoUpdatePage.do"/>?'+paramStr+'&cus_id='+cus_id+
			'&app_serno='+app_serno;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusOrgInfo() {
		var paramStr = CusOrgInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusOrgInfoViewPage.do"/>?'+paramStr+'&cus_id='+cus_id+
				'&app_serno='+app_serno+'&ops='+ops;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusOrgInfoPage() {
		var url = '<emp:url action="getCusOrgInfoAddPage.do"/>?cus_id='+cus_id+
		'&app_serno='+app_serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusOrgInfo() {
		var paramStr = CusOrgInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusOrgInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				doDeleteRecord(url);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doDeleteRecord(url){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='1'){
		            alert('删除成功!');
		            window.location.reload();
				}else if(operMsg=='2'){
					alert('删除失败!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	};
	
	function doReset(){
		page.dataGroups.CusOrgInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<form  method="POST" action="#" id="queryForm">
	</form>
		
	<div align="left">
		<emp:button id="getAddCusOrgInfoPage" label="新增" />
		<emp:button id="getUpdateCusOrgInfoPage" label="修改" />
		<emp:button id="deleteCusOrgInfo" label="删除" />
		<emp:button id="viewCusOrgInfo" label="查看" />
	</div>
	<emp:table icollName="CusOrgInfoList" pageMode="true" url="pageCusOrgInfoQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="申请流水号" hidden="true" />
		<emp:text id="branch_type" label="类型" dictname="STD_ZB_BRANCH_TYPE" />
		<emp:text id="duty_man" label="负责人" />
		<emp:text id="phone" label="联系电话" />
	</emp:table>
	
</body>
</html>
</emp:page>
    