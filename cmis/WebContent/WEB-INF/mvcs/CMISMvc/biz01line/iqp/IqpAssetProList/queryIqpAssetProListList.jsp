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
		IqpAssetProList._toForm(form);
		IqpAssetProListList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetProListPage() {
		var paramStr = IqpAssetProListList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetProListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetProList() {
		var paramStr = IqpAssetProListList._obj.getParamStr(['serno','bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetProListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetProListPage() {
		var url = '<emp:url action="getIqpAssetProListAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetProList() {
		var paramStr = IqpAssetProListList._obj.getParamStr(['serno','bill_no']);
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
							var url = '<emp:url action="queryIqpAssetProListList.do"/>?serno='+'<%=serno%>';
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
				var url = '<emp:url action="deleteIqpAssetProListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetProListGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetProListGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetProList.bill_no" label="借据编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddIqpAssetProListPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAssetProListPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAssetProList" label="删除" op="remove"/>
		<emp:actButton id="viewIqpAssetProList" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetProListList" pageMode="true" url="pageIqpAssetProListQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<!--新添"贷款余额"、"到期日期"页面显示   2014-08-20  王青	 start-->
		<emp:text id="loan_balance" label="贷款余额" />
		<emp:text id="end_date" label="到期日期" />
		<!--新添"贷款余额"、"到期日期"页面显示   2014-08-20  王青	 end-->
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="fina_br_id_displayname" label="账务机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    