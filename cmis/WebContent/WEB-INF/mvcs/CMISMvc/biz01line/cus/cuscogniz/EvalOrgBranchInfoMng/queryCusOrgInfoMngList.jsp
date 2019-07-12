<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doViewCusOrgInfoMng() {
		var paramStr = CusOrgInfoMngList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusOrgInfoMngViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusOrgInfoMng._toForm(form);
		CusOrgInfoMngList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusOrgInfoMngPage() {
		var paramStr = CusOrgInfoMngList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			EMPTools.setCookie('returnParams', "cus_id="+cus_id, 0, '/');
			var url = '<emp:url action="getCusOrgInfoMngUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
		
	function doGetAddCusOrgInfoMngPage() {
		EMPTools.setCookie('returnParams', "cus_id="+cus_id, 0, '/');
		var url = '<emp:url action="getCusOrgInfoMngAddPage.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusOrgInfoMng() {
		var paramStr = CusOrgInfoMngList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				EMPTools.setCookie('returnParams', "cus_id="+cus_id, 0, '/');
				var url = '<emp:url action="deleteCusOrgInfoMngRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						var flagInfo=jsonstr.flagInfo;						
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();								
						}
					}
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusOrgInfoMngGroup.reset();
	};
	
	/*--user code begin--*/
	function doload(){
		menuId = "${context.menuId}";
		cus_id = "${context.cus_id}";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<form  method="POST" action="#" id="queryForm">
	</form>
		
	<div align="left">
		<emp:actButton id="getAddCusOrgInfoMngPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateCusOrgInfoMngPage" label="修改" op="update"/>
		<emp:actButton id="deleteCusOrgInfoMng" label="删除" op="remove"/>
		<emp:actButton id="viewCusOrgInfoMng" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusOrgInfoMngList" pageMode="true" url="pageCusOrgInfoMngQuery.do?cus_id=${context.cus_id}">
		<emp:text id="serno" label="申请流水号" hidden="true" />
		<emp:text id="branch_type" label="类型" dictname="STD_ZB_BRANCH_TYPE" />
		<emp:text id="duty_man" label="负责人" />
		<emp:text id="phone" label="联系电话" />
		<emp:text id="cus_id" label="评估机构客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>