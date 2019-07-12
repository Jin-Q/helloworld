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
		IqpOverseeManager._toForm(form);
		IqpOverseeManagerList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpOverseeManagerPage() {
		var paramStr = IqpOverseeManagerList._obj.getParamStr(['manager_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeManagerUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpOverseeManager() {
		var paramStr = IqpOverseeManagerList._obj.getParamStr(['manager_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeManagerViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpOverseeManagerPage() {
		var serno = "${context.serno}";
		var url = '<emp:url action="getIqpOverseeManagerAddPage.do"/>&serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		EMPTools.openWindow(url,'newwindow',param);
	};
	
	function doDeleteIqpOverseeManager() {
		var paramStr = IqpOverseeManagerList._obj.getParamStr(['manager_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
							var flag = jsonstr.flag;
							if(flag == "success" ){
								alert("删除成功！");
								window.location.reload();
							}else{
								alert("删除失败！");
							}
						}
						var handleFailure = function(o){
						alert("异步回调失败！");	
						};
						var url = '<emp:url action="deleteIqpOverseeManagerRecord.do"/>?'+paramStr;
						var callback = {
							success:handleSuccess,
							failure:null
						};
						url = EMPTools.encodeURI(url);
						var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);			
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpOverseeManagerGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpOverseeManagerPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpOverseeManagerPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpOverseeManager" label="删除" op="remove"/>
		<emp:actButton id="viewIqpOverseeManager" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpOverseeManagerList" pageMode="true" url="pageIqpOverseeManagerQuery.do">
		<emp:text id="name" label="姓名" />
		<emp:text id="duty" label="职务" dictname="STD_ZX_DUTY" />
		<emp:text id="edu" label="学历" dictname="STD_ZX_EDU" />
		<emp:text id="term" label="本公司工作（从业）年限" />
		<emp:text id="manager_id" label="主要管理人员信息编号" hidden="true"/>
		<emp:text id="serno" label="业务流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    