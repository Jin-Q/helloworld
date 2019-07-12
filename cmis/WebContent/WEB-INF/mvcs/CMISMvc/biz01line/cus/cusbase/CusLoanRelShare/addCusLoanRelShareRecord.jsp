<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function returnOrgId(data) {
	if (data == null) {
		alert('请选择一条记录');
	}
	CusLoanRelShare.br_id._setValue(data.organno._getValue());
	CusLoanRelShare.br_id_name._setValue(data.organname._getValue());
	var orgId=CusLoanRelShare.br_id._getValue();
	CusLoanRelShare.main_cus_mgr._setValue('');
	CusLoanRelShare.main_cus_mgr_name._setValue('');
	CusLoanRelShare.opt_cus_mgr._setValue('');
	CusLoanRelShare.main_cus_mgr._obj._renderReadonly(false);
	var url="<emp:url action='queryCusComLoanSharePopList.do'/>?"+"&returnMethod=returnCusId"+"&orgId="+orgId;
	CusLoanRelShare.main_cus_mgr._obj.config.url = EMPTools.encodeURI(url);
}
function returnCusId(data) {
	if (data == null) {
		alert('请选择一条记录');
	}
	CusLoanRelShare.main_cus_mgr._setValue(data.actorno._getValue());
	CusLoanRelShare.main_cus_mgr_name._setValue(data.actorname._getValue());
	CusLoanRelShare.opt_cus_mgr._setValue(data.actorno._getValue());
}
function doload()
{
	var custype=CusLoanRelShare.cus_type._getValue();
	if(custype=='130')
	{
		CusLoanRelShare.agri_flg._setValue('1');
		}
	main_br_id._setValue(CusLoanRelShare.br_id._getValue());
	CusLoanRelShare.br_id._setValue('');
	CusLoanRelShare.br_id_name._setValue('');
	CusLoanRelShare.main_cus_mgr._setValue('');
	CusLoanRelShare.main_cus_mgr_name._setValue('');
	CusLoanRelShare.opt_cus_mgr._setValue('');
	cus_type._setValue(CusLoanRelShare.cus_type._getValue());
	cus_Id._setValue(CusLoanRelShare.out_cus_id._getValue());
	}
function doReturn() {
	history.go(-1);
};
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addCusLoanRelShareRecord.do" method="POST">
		
		<emp:gridLayout id="CusLoanRelShareGroup" title="共享机构" maxColumn="2">
		    <emp:pop id="CusLoanRelShare.br_id" label="共享机构:" url="querySOrgShareList.do?returnMethod=returnOrgId" required="true" />
		    <emp:text id="CusLoanRelShare.br_id_name" label="机构名称" defvalue=""/>
		    </emp:gridLayout>
		    <emp:gridLayout id="CusLoanRelShareGroup" title="共享客户经理" maxColumn="2">
		    <emp:pop id="CusLoanRelShare.main_cus_mgr" label="客户经理:"
				url="queryCusComLoanSharePopList.do?returnMethod=returnCusId" required="true" readonly="true" />
				<emp:text id="CusLoanRelShare.main_cus_mgr_name" label="客户经理名称" readonly="true" />
				<emp:text id="CusLoanRelShare.opt_cus_mgr" label="托管客户经理" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<emp:text id="CusLoanRelShare.cus_id" label="客户码" readonly="true" hidden="true"/>
		<emp:select id="CusLoanRelShare.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" readonly="true" hidden="true"/>
		<emp:text id="CusLoanRelShare.cert_code" label="证件号码" readonly="true" hidden="true"/>
		<emp:text id="CusLoanRelShare.cus_type" label="客户类型" readonly="true" hidden="true"/>
		<emp:text id="CusLoanRelShare.cus_name" label="客户名称" readonly="true" hidden="true"/>
		<emp:text id="CusLoanRelShare.mng_br_id" label="所属法人机构"  hidden="true"/>
		<emp:text id="CusLoanRelShare.out_cus_id" label="外部客户码" hidden="true"/>
		<emp:text id="CusLoanRelShare.area_code" label="区域编码" hidden="true"/>
		<emp:text id="CusLoanRelShare.area_name" label="区域名称" hidden="true"/>
		<emp:text id="CusLoanRelShare.bank_flg" label="是否主办机构"  defvalue="2" hidden="true"/>
		<emp:text id="CusLoanRelShare.agri_flg" label="是否农户"  defvalue="2" hidden="true"/>
		<emp:text id="main_br_id" label="组织机构号" readonly="true" hidden="true"/>
		<emp:text id="cus_type" label="客户类型" readonly="true" hidden="true"/>
		<emp:text id="cus_Id" label="外部客户码" readonly="true" hidden="true"/>
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

