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
		CusGrpInfo._toForm(form);
		CusGrpInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusGrpInfoGroup.reset();
	};

	function doSelect(){	
		var data = CusGrpInfoList._obj.getSelectedData();
		if (data != null && data != "" ) {
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusGrpInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" />
			<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" />
	</emp:gridLayout>
		
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<button onclick="doSelect()">选取返回</button>
	<emp:table icollName="CusGrpInfoList" pageMode="true" url="pageCusGrpInfoQuery.do">
		<emp:text id="grp_no" label="关联(集团)编号" />
        <emp:text id="grp_name" label="关联(集团)名称" />
        <emp:text id="parent_cus_id" label="主关联(集团)公司客户码" hidden="true"/>
        <emp:text id="parent_cus_id_displayname" label="主关联(集团)公司名称" />
        <emp:text id="manager_id" label="责任人" hidden="true"/>
        <emp:text id="manager_br_id" label="责任机构" hidden="true"/>
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>