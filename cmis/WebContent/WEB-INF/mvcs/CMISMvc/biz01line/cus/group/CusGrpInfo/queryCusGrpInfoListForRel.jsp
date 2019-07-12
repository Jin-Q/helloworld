<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<% 
String cus_id = (String)request.getParameter("CusGrpInfo.cus_id");
%>

<script type="text/javascript">
	
	function doViewCusGrpInfo(){
		//alert(<%=cus_id%>);
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryCusGrpInfoDetail.do"/>?'+paramStr+'&CusGrpInfo.cus_id=<%=cus_id%>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusGrpInfo._toForm(form);
		CusGrpInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusGrpInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<div align="left">
		<emp:button id="viewCusGrpInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="CusGrpInfoList" pageMode="false" url="pageCusGrpInfoQuery.do">
		<emp:text id="grp_no" label="关联(集团)编号" />
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主关联(集团)公司客户码" />
		<emp:text id="parent_cus_id_displayname" label="主关联(集团)公司名称" />
		<emp:text id="manager_id_displayname" label="主办客户经理" />
		<emp:text id="manager_br_id_displayname" label="主办行" />
	</emp:table>
</body>
</html>
</emp:page>