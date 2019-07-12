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
		LmtQuotaAdjustApp._toForm(form);
		LmtQuotaAdjustAppList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtQuotaAdjust() {
		var paramStr = LmtQuotaAdjustAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtQuotaAdjustAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="viewLmtQuotaAdjust" label="查看" />
	</div>

	<emp:table icollName="LmtQuotaAdjustAppList" pageMode="true" url="pageLmtQuotaAdjustAppQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="fin_totl_limit" label="融资总额" dataType="Currency" />
		<emp:text id="fin_totl_spac" label="融资总敞口" dataType="Currency" />
		<emp:text id="single_quota_his" label="单户限额(存量)" dataType="Currency"/>
		<emp:text id="single_quota_new" label="单户限额(新增)" dataType="Currency"/>
		<emp:text id="inure_date" label="生效日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS_QUO"/>
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="fin_agr_no" label="融资协议编号" hidden="true"/>
		<emp:text id="fin_serno" label="审批流水" hidden="true"/>
		<emp:text id="serno" label="主键" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>
    