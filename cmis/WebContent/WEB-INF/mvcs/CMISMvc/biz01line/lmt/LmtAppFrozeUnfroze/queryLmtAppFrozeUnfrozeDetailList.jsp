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
		LmtAppFrozeUnfroze._toForm(form);
		LmtAppFrozeUnfrozeList._obj.ajaxQuery(null,form);
	};
	
	function doViewLmtAppFrozeUnfroze() {
		var paramStr = LmtAppFrozeUnfrozeList._obj.getParamStr(['serno']);
		var limit_code = LmtAppFrozeUnfrozeList._obj.getParamValue(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppFrozeUnfrozeViewPage.do"/>?'+paramStr+'&viewflag=yes&limit_code='+limit_code;
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

	<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppFrozeUnfroze.serno" label="申请编号" />
			<emp:text id="LmtAppFrozeUnfroze.agr_no" label="授信协议编号" />
			<emp:text id="LmtAppFrozeUnfroze.limit_code" label="授信额度编号" />
			<emp:select id="LmtAppFrozeUnfroze.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAppFrozeUnfroze" label="查看"/>
	</div>

	<emp:table icollName="LmtAppFrozeUnfrozeList" pageMode="true" url="pageLmtAppFrozeUnfrozeDetailListQuery.do?limit_code=${context.limit_code}">
		<emp:text id="serno" label="申请编号" />
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:select id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:select id="cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="froze_unfroze_amt" label="冻结金额" dataType="Currency"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="over_date" label="冻结日期"  />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    