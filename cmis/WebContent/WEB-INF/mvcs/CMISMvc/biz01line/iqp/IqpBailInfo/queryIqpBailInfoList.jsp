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
		IqpBailInfo._toForm(form);
		IqpBailInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBailInfoPage() {
		var paramStr = IqpBailInfoList._obj.getParamStr(['serno','bail_acct_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBailInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBailInfo() {
		var paramStr = IqpBailInfoList._obj.getParamStr(['serno','bail_acct_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBailInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBailInfoPage() {
		var url = '<emp:url action="getIqpBailInfoAddPage.do"/>?serno=${context.serno}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBailInfo() {
		var paramStr = IqpBailInfoList._obj.getParamStr(['serno','bail_acct_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var serno = jsonstr.serno;
						if(flag == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryIqpBailInfoList.do"/>?serno='+serno; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除失败!"); 
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteIqpBailInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	
	function doReset(){
		page.dataGroups.IqpBailInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:button id="getAddIqpBailInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBailInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBailInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpBailInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="IqpBailInfoList" pageMode="true" url="pageIqpBailInfoQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="bail_acct_no" label="保证金账号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="rate" label="利率" />
		<emp:text id="dep_term" label="存期" />
		<emp:text id="open_org_displayname" label="开户机构" />
		<emp:text id="open_org" label="开户机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    