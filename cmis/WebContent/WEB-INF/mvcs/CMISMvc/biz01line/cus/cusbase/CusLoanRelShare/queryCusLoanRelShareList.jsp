<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />

	<script type="text/javascript">
	function doQuery() {
		var form = document.getElementById('queryForm');
		CusLoanRelShare._toForm(form);
		CusLoanRelShareList._obj.ajaxQuery(null, form);
	};

	function doGetUpdateCusLoanRelSharePage() {
		var paramStr = CusLoanRelShareList._obj.getParamStr( [ 'cus_id',
				'br_id' ]);
		if (paramStr != null) {
			var url = '<emp:url action="getCusLoanRelShareUpdatePage.do"/>?' + paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewCusLoanRelShare() {
		var paramStr = CusLoanRelShareList._obj.getParamStr( [ 'cus_id',
				'br_id' ]);
		if (paramStr != null) {
			var url = '<emp:url action="getCusLoanRelShareViewPage.do"/>?' + paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetAddCusLoanRelSharePage() {
		var form =document.getElementById('queryForm');
		CusLoanRelShare._toForm(form);
		var url = '<emp:url action="getCusLoanRelShareAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		form.action=url;
		form.submit();
	};

	function doDeleteCusLoanRelShare() {
		var paramStr = CusLoanRelShareList._obj.getParamStr( [ 'cus_id','br_id','cus_type']);
		var cusid=CusLoanRelShare.cus_id._getValue();
		var brid=CusLoanRelShare.br_id._getValue();
		paramStr=paramStr+'&cus_id='+cusid+'&main_br_id='+brid;
		if (paramStr != null) {
			if (confirm("是否确认要收回？")) {
				var url = '<emp:url action="deleteCusLoanRelShareRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset() {
		page.dataGroups.CusLoanRelShareGroup.reset();
	};

	/*--user code begin--*/

	/*--user code end--*/
</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="CusLoanRelShareGroup1" title="客户共享" maxColumn="2" >
		<emp:text id="CusLoanRelShare.cus_id" label="客户码" readonly="true"/>
		<emp:text id="CusLoanRelShare.cus_name" label="客户名称" readonly="true"/>
		<emp:select id="CusLoanRelShare.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" readonly="true"/>
		<emp:text id="CusLoanRelShare.cert_code" label="证件号码" readonly="true"/>
		<emp:select id="CusLoanRelShare.cus_type" label="客户类型" readonly="true" dictname="STD_ZB_CUS_TYPE"/>
		<emp:text id="CusLoanRelShare.br_id" label="主管机构" readonly="true"/>
		<emp:text id="CusLoanRelShare.main_cus_mgr" label="客户经理" readonly="true"/>
		<emp:text id="CusLoanRelShare.mng_br_id" label="所属法人机构" hidden="true" readonly="true"/>
		<emp:text id="CusLoanRelShare.area_code" label="区域编码" hidden="true"/>
		<emp:text id="CusLoanRelShare.area_name" label="区域名称" hidden="true"/>
		<emp:text id="CusLoanRelShare.out_cus_id" label="外部客户码" hidden="true"/>
	</emp:gridLayout>
	<emp:gridLayout id="CusLoanRelShareGroup" title="共享信息列表" maxColumn="2" >
	<div align="right" ><emp:button id="getAddCusLoanRelSharePage"
		label="添加共享" op="add" /><emp:button id="deleteCusLoanRelShare"
		label="收回" op="remove" /></div>
</emp:gridLayout>
	<emp:table icollName="CusLoanRelShareList" pageMode="false"
		url="pageCusLoanRelShareQuery.do">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="br_id" label="共享机构" />
		<emp:text id="bank_flg" label="是否主办机构" dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:text id="agri_flg" label="是否农户" dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:text id="cus_name" label="客户名称" hidden="true"/>
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" hidden="true"/>
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" hidden="true"/>
		<emp:text id="cert_code" label="证件号码" hidden="true"/>
		<emp:text id="main_cus_mgr" label="主管客户经理" hidden="true"/>
		<emp:text id="opt_cus_mgr" label="托管客户经理/共享客户经理" />
		<emp:text id="area_code" label="区域编码" hidden="true"/>
		<emp:text id="area_name" label="区域编码" hidden="true"/>
		<emp:text id="out_cus_id" label="外部客户码" hidden="true"/>
	</emp:table>

	</body>
	</html>
</emp:page>
