<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtCont._toForm(form);
		LmtContList._obj.ajaxQuery(null,form);
	};

	//查看 
	function doViewLmtAgrInfo() {
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrInfoViewPage.do"/>?show=none&'+paramStr+"&menuId=crd_agr";
			url = EMPTools.encodeURI(url);
			window.open(url,'LmtAgr_show',"height=650,width=1000,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no’");
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset(){
		page.dataGroups.LmtContGroup.reset();
	};
	
	/*--user code end--*/
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="LmtContGroup" title="输入查询条件" maxColumn="2">
        <emp:text id="LmtCont.lmt_serno" label="授信合同编号" />
		<emp:select id="LmtCont.crd_lmt_type" label="授信业务类型" dictname="STD_ZB_BIZ_TYPE" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />

	<div id="LmtAgrInfoQuery" >
		<emp:button id="viewLmtAgrInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtAgrInfoList" pageMode="true" url="pageLmtContQuery.do?">
		<emp:text id="agr_no" label="授信协议号" />
		<emp:text id="biz_type" label="授信业务类型" dictname="STD_ZB_BIZ_TYPE"/>
		<emp:text id="cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
	</emp:table>
</body>
</html>
</emp:page>
