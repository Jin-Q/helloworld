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
	CtrLimitCont._toForm(form);
	CtrLimitContList._obj.ajaxQuery(null,form);
};

function doReset(){
	page.dataGroups.CtrLimitContGroup.reset();
};
	
function doSelect(){
	var data = CtrLimitContList._obj.getSelectedData();
	if(data == null || data.length == 0){
		alert('请先选择一条记录！');
		return;
	}
	window.opener["${context.returnMethod}"](data[0]);
	window.close();
};	

function returnCus(data){
	CtrLimitCont.cus_id._setValue(data.cus_id._getValue());
	CtrLimitCont.cus_name._setValue(data.cus_name._getValue());
};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CtrLimitContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrLimitCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLimitCont.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:text id="CtrLimitCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="引入"/>
	</div>

	<emp:table icollName="CtrLimitContList" pageMode="true" url="pageCtrLimitContQueryPop.do">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_cn" label="中文合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />		
		<emp:text id="memo" label="备注"  hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    