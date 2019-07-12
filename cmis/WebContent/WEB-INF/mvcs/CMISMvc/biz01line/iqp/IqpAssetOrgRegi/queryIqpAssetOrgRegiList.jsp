<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAssetOrgRegi._toForm(form);
		IqpAssetOrgRegiList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetOrgRegiPage() {
		var paramStr = IqpAssetOrgRegiList._obj.getParamStr(['serno','asset_org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetOrgRegiUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetOrgRegi() {
		var paramStr = IqpAssetOrgRegiList._obj.getParamStr(['serno','asset_org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetOrgRegiViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetOrgRegiPage() {
		var url = '<emp:url action="getIqpAssetOrgRegiAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetOrgRegi() {
		var paramStr = IqpAssetOrgRegiList._obj.getParamStr(['serno','asset_org_id']);
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
						if(flag == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryIqpAssetOrgRegiList.do"/>?serno='+'<%=serno%>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除异常!");
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
				var url = '<emp:url action="deleteIqpAssetOrgRegiRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetOrgRegiGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpAssetOrgRegiPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAssetOrgRegiPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAssetOrgRegi" label="删除" op="remove"/>
		<emp:actButton id="viewIqpAssetOrgRegi" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetOrgRegiList" pageMode="true" url="pageIqpAssetOrgRegiQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="asset_org_id" label="机构代码" />
		<emp:text id="asset_org_type" label="机构类型" dictname="STD_ZB_ASSET_ORG_TYPE"/>
		<emp:text id="acct_no" label="账号" />
		<emp:text id="acct_name" label="账户名" />
		<emp:text id="acctsvcr_no" label="开户行行号" />
		<emp:text id="acctsvcr_name" label="开户行行名" />
	</emp:table>
	
</body>
</html>
</emp:page>
    