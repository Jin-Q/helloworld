<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<% String cus_id=(String)request.getParameter("cus_id");%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpMemMana._toForm(form);
		IqpMemManaList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewIqpMemMana() {
		var paramStr = IqpMemManaList._obj.getParamStr(['mem_cus_id','mem_manuf_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpMemManaViewPage.do"/>?'+paramStr+"&cus_id=${context.cus_id}"+"&menuId=IqpMemMana"
			+"&op=view"+"&net_agr_no=${context.net_agr_no}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	

	function doReset(){
		page.dataGroups.IqpMemManaGroup.reset();
	};
	

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="viewIqpMemMana" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpMemManaList" pageMode="true" url="pageIqpMemManaQuery.do?net_agr_no=${context.net_agr_no}&cus_id=${context.cus_id}">
	    <emp:text id="cus_id" label="核心企业客户码" hidden="true"/>
	    <emp:text id="cus_id_displayname" label="核心企业客户名称" hidden="true"/>
		<emp:text id="mem_cus_id" label="成员企业客户码" />
		<emp:text id="mem_cus_id_displayname" label="成员企业客户名称" />
		<emp:text id="mem_manuf_type" label="成员企业类别" dictname="STD_ZB_MANUF_TYPE" />
		<emp:text id="term" label="在途期限(日)" hidden="false"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_MEM_STATUS" />
		<emp:text id="lmt_quota" label="授信限额" hidden="true"/>
		<emp:checkbox id="lmt_type" label="授信业务种类" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    