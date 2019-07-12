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
		CusBlkList._toForm(form);
		CusBlkListList._obj.ajaxQuery(null,form);
	};
	
	function doSelect(){	
		var data = CusBlkListList._obj.getSelectedData();
		if (data != null) {
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};		
	
	/*--user code begin--*/
	function doReset(){
		page.dataGroups.CusBlkListGroup.reset();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusBlkListGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusBlkList.cus_id" label="客户码" />
			<emp:text id="CusBlkList.cus_name" label="客户名称" />
			<emp:select id="CusBlkList.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusBlkList.cert_code" label="证件号码" />
			<emp:select id="CusBlkList.status" label="状态" dictname="STD_CUS_BLK_STATUS" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
    <!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
	<emp:table icollName="CusBlkListList" pageMode="true" url="pageCusBlkListQueryPop.do?cusTypCondition=${context.cusTypCondition}" reqParams="CusBlkList.status=002">
	<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
		<emp:text id="black_date" label="列入日期" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="black_type" label="客户类型" dictname="STD_ZB_EVENT_TYP" />
		<emp:text id="black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
		<emp:text id="legal_phone" label="联系电话" hidden="true"/>
		<emp:text id="legal_name" label="法定代表人" hidden="true"/>
		<emp:text id="legal_addr" label="通讯地址" hidden="true"/>
		<emp:text id="black_reason" label="客户描述" hidden="true"/>
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="legal_addr_displayname" label="通讯地址" hidden="true"/>
		<emp:text id="street" label="街道" hidden="true"/>
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    