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
		LmtAgrDetails._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAgrDetailsPage() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&type=indiv";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="false" url="pageLmtAgrDetailsQuery.do?agr_no=${context.agr_no}">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:select id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:text id="limit_name" label="额度品种名称" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="limit_code" label="授信额度编号" hidden="true"/>
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    