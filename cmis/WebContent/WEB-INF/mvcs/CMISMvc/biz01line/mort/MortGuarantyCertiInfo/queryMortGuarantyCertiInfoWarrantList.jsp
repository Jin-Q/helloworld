<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantyCertiInfo._toForm(form);
		MortGuarantyCertiInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortGuarantyCertiInfoPage() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyCertiInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantyCertiInfo() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyCertiInfoViewPage.do"/>?exwa=exwa&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doStorageMortGuarantyCertiInfo() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		if (paramStr != null) {
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						alert("已成功入库");
						//var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?status=finish&menuId=stay_storage';
						//url = EMPTools.encodeURI(url);
						window.location.reload();
					}else{
						alert("入库失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("入库失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			
			var url = '<emp:url action="storageMortGuarantyCertiInfoRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doGetAddMortGuarantyCertiInfoPage() {
		var url = '<emp:url action="getMortGuarantyCertiInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortGuarantyCertiInfo() {
		var paramStr = MortGuarantyCertiInfoList._obj.getParamStr(['warrant_no','warrant_type']);
		var is_main_warrant = MortGuarantyCertiInfoList._obj.getParamStr(['is_main_warrant']);
		if (paramStr != null) {
			if(is_main_warrant=="is_main_warrant=2"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o) {
						EMPTools.unmask();
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("Parse jsonstr define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if("success" == flag){
								alert("已删除！");
								window.location.reload();
							}else{
								alert("删除失败！");
							}
						}
					};
					var handleFailure = function(o) {
						alert("删除失败!");
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					var url = '<emp:url action="deleteMortGuarantyCertiInfoRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert('主权证信息不能被删除！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortGuarantyCertiInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="viewMortGuarantyCertiInfo" label="查看"/>
	</div>

	<emp:table icollName="MortGuarantyCertiInfoList" pageMode="false" url="">
		<emp:text id="warrant_cls" label="权证类别" dictname="STD_WARRANT_TYPE" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="warrant_type" label="权证类型" hidden="true"/>
		<emp:text id="warrant_no" label="权证编号" />
		<emp:text id="warrant_name" label="权证名称" />
		<emp:text id="is_main_warrant" label="是否主权证" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_no_displayname" label="押品名称" hidden="true"/>
		<emp:text id="keep_org_no" label="保管机构" hidden="true"/>
		<emp:text id="hand_org_no" label="经办机构" hidden="true"/>
		<emp:text id="keep_org_no_displayname" label="保管机构" />
		<emp:text id="hand_org_no_displayname" label="经办机构" hidden="true"/>
		<emp:text id="warrant_state" label="权证状态" dictname="STD_WARRANT_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    