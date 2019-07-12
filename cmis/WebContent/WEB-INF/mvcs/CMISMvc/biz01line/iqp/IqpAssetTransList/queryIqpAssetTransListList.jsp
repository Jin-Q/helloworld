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
		IqpAssetTransList._toForm(form);
		IqpAssetTransListList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetTransListPage() {
		var paramStr = IqpAssetTransListList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTransListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetTransList() {
		var paramStr = IqpAssetTransListList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTransListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetTransListPage() {
		var url = '<emp:url action="getIqpAssetTransListAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetTransList() {
		var paramStr = IqpAssetTransListList._obj.getParamStr(['serno','bill_no']);
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
							var url = '<emp:url action="queryIqpAssetTransListList.do"/>?serno='+'<%=serno%>';
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
				var url = '<emp:url action="deleteIqpAssetTransListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetTransListGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetTransListGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetTransList.bill_no" label="借据编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddIqpAssetTransListPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAssetTransListPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAssetTransList" label="删除" op="remove"/>
		<emp:actButton id="viewIqpAssetTransList" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetTransListList" pageMode="true" url="pageIqpAssetTransListQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="trans_rate" label="转让比率" dataType="Percent"/>
		<emp:text id="trans_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="fina_br_id_displayname" label="账务机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    