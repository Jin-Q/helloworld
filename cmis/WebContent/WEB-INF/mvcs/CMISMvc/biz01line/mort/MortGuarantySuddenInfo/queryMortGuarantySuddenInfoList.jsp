<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String guaranty_no = (String)context.getDataValue("guaranty_no");
%>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantySuddenInfo._toForm(form);
		MortGuarantySuddenInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortGuarantySuddenInfoPage() {
		var paramStr = MortGuarantySuddenInfoList._obj.getParamStr(['accident_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantySuddenInfoUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantySuddenInfo() {
		var paramStr = MortGuarantySuddenInfoList._obj.getParamStr(['accident_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantySuddenInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortGuarantySuddenInfoPage() {
		var url = '<emp:url action="getMortGuarantySuddenInfoAddPage.do"/>?guaranty_no='+'<%=guaranty_no%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortGuarantySuddenInfo() {
		var paramStr = MortGuarantySuddenInfoList._obj.getParamStr(['accident_no']);
		if (paramStr != null) {
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
		    	var url = '<emp:url action="deleteMortGuarantySuddenInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortGuarantySuddenInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddMortGuarantySuddenInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateMortGuarantySuddenInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteMortGuarantySuddenInfo" label="删除" op="remove"/>
		<emp:actButton id="viewMortGuarantySuddenInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortGuarantySuddenInfoList" pageMode="false" url="pageMortGuarantySuddenInfoQuery.do">
		<emp:text id="accident_no" label="意外情况编码" hidden="true"/>
		<emp:text id="accident_type" label="意外情况类型" dictname="STD_ACCIDENT_INSU_TYPE" />
		<emp:text id="accident_resn" label="意外原因" />
		<emp:text id="occur_date" label="发生日期" />
		
	</emp:table>
	
</body>
</html>
</emp:page>
    