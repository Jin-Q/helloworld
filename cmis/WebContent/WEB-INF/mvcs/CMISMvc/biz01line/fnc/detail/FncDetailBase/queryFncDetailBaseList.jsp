<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String cus_id=request.getParameter("FncDetailBase.cus_id");
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddFncDetailBasePage(){

		
		var url = '<emp:url action="getFncDetailBaseAddPage.do"/>&FncDetailBase.cus_id=<%=cus_id%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteFncDetailBase(){		
		var paramStr = FncDetailBaseList._obj.getParamStr(['pk','cus_id']);
		alert(paramStr);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteFncDetailBaseRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateFncDetailBasePage(){
		var paramStr = FncDetailBaseList._obj.getParamStr(['pk','cus_id','fnc_ym']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncDetailBaseUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//取得维护界面
	function doUpdate_xyp(){
		var paramStr = FncDetailBaseList._obj.getParamStr(['pk']);
		if (paramStr != null) {
			var url = '<emp:url action="getFncDetailBaseUpdate_xypPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewFncDetailBase(){
		var paramStr = FncDetailBaseList._obj.getParamStr(['pk']);
		if (paramStr != null) {
			var url = '<emp:url action="queryFncDetailBaseDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncDetailBase._toForm(form);
		FncDetailBaseList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.FncDetailBaseGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<div align="left" style="display:${context.buttonFlag eq 'edit'? '' : 'none'}">
		<emp:button id="getAddFncDetailBasePage" label="新增" />
		<emp:button id="getUpdateFncDetailBasePage" label="维护" />
		<emp:button id="deleteFncDetailBase" label="删除" />
		<emp:button id="viewFncDetailBase" label="查看" />
		<emp:button id="update_xyp" label="维护" />
	</div>
	
	<div align="left" style="display:${context.buttonFlag eq 'edit'? 'none' : ''}">
		<emp:button id="viewFncDetailBase" label="查看" />
	</div>
	<emp:table icollName="FncDetailBaseList" pageMode="true" url="pageFncDetailBaseQuery.do" reqParams="FncDetailBase.cus_id=${context.FncDetailBase.cus_id}">
		<emp:text id="pk" label="PK" hidden="true" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="fnc_ym" label="年月" />
		<emp:text id="input_id" label="登记人" readonly="true" />
		<emp:text id="input_br_id" label="登记机构" readonly="true" />
		<emp:text id="input_date" label="登记日期" readonly="true"  />
		<emp:text id="last_upd_id" label="更新人" readonly="true" hidden="true" />
		<emp:text id="last_upd_date" label="更新日期"  readonly="true" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>