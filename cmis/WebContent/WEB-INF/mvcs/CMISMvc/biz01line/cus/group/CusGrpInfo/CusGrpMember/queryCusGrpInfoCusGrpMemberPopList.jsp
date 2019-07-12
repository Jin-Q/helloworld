<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doSelect(){	
		var data = CusGrpMemberList._obj.getSelectedData();

		if (data != null) {
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
	<button onclick="doSelect()">选取返回</button>
	<emp:table icollName="CusGrpMemberList" pageMode="false" url="" reqParams="">
		<emp:text id="cus_id" label="成员客户码" />
		<emp:text id="cus_name" label="成员客户名称" />
		<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GRP_CO_TYPE"/>
		<emp:text id="cus_manager" label="客户经理" />
		<emp:text id="main_br_id" label="主管机构" />
		<emp:text id="grp_no" label="关联(集团)编号" hidden="true"/>
		<emp:text id="cus_type" label="客户类型" hidden="true"/>
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>