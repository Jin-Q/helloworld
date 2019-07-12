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
		HousePurInfo._toForm(form);
		HousePurInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateHousePurInfoPage() {
		var paramStr = HousePurInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getHousePurInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewHousePurInfo() {
		var paramStr = HousePurInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getHousePurInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddHousePurInfoPage() {
		var url = '<emp:url action="getHousePurInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteHousePurInfo() {
		var paramStr = HousePurInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteHousePurInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.HousePurInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddHousePurInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateHousePurInfoPage" label="修改" op="update"/>
		<emp:button id="deleteHousePurInfo" label="删除" op="remove"/>
		<emp:button id="viewHousePurInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="HousePurInfoList" pageMode="false" url="pageHousePurInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="totle_times" label="总期数" />
		<emp:text id="settl_acct" label="结算账户" />
		<emp:text id="settl_acct_name" label="结算账户户名" />
		<emp:text id="guar_debit_acct" label="担保方扣款账号" />
		<emp:text id="guar_name" label="担保方户名" />
		<emp:text id="pur_amt" label="购买金额" />
		<emp:text id="pur_time" label="购买时间" />
		<emp:text id="loan_perc" label="贷款比例" />
		<emp:text id="fst_pyr_perc" label="首付款比例" />
		<emp:text id="invc_no" label="发票号码" />
		<emp:text id="house_type" label="房屋类型" />
		<emp:text id="get_houproper_get_type" label="房产取得方式" />
		<emp:text id="houproper_status" label="房产状态" />
		<emp:text id="houproper_cha" label="房产性质" />
		<emp:text id="house_name" label="房屋名称" />
		<emp:text id="house_addr" label="房屋位置" />
		<emp:text id="house_squ" label="房屋面积" />
		<emp:text id="house_buildprice" label="房屋构建单价" />
		<emp:text id="house_eval_value" label="房屋评估价" />
		<emp:text id="is_fst_pur_house" label="是否首购房" dictname="STD_ZX_YES_NO" />
		<emp:text id="house_build_year" label="房屋建成年份" />
		<emp:text id="hourec_no" label="商品房买卖合同网上备案登记号" />
		<emp:text id="dispute_mode" label="纠纷解决方式" />
		<emp:text id="house_qnt" label="住房套数" />
	</emp:table>
	
</body>
</html>
</emp:page>
    