<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryHousePurInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="HousePurInfoGroup" title="厂房按揭购置信息" maxColumn="2">
			<emp:text id="HousePurInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="HousePurInfo.totle_times" label="总期数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="HousePurInfo.settl_acct" label="结算账户" maxlength="20" required="false" />
			<emp:text id="HousePurInfo.settl_acct_name" label="结算账户户名" maxlength="80" required="false" />
			<emp:text id="HousePurInfo.guar_debit_acct" label="担保方扣款账号" maxlength="20" required="false" />
			<emp:text id="HousePurInfo.guar_name" label="担保方户名" maxlength="80" required="false" />
			<emp:text id="HousePurInfo.pur_amt" label="购买金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="HousePurInfo.pur_time" label="购买时间" required="false" />
			<emp:text id="HousePurInfo.loan_perc" label="贷款比例" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="HousePurInfo.fst_pyr_perc" label="首付款比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="HousePurInfo.invc_no" label="发票号码" maxlength="20" required="false" />
			<emp:select id="HousePurInfo.house_type" label="房屋类型" required="false" />
			<emp:select id="HousePurInfo.get_houproper_get_type" label="房产取得方式" required="false" />
			<emp:select id="HousePurInfo.houproper_status" label="房产状态" required="false" />
			<emp:select id="HousePurInfo.houproper_cha" label="房产性质" required="false" />
			<emp:text id="HousePurInfo.house_name" label="房屋名称" maxlength="80" required="false" />
			<emp:text id="HousePurInfo.house_addr" label="房屋位置" maxlength="100" required="false" />
			<emp:text id="HousePurInfo.house_squ" label="房屋面积" maxlength="10" required="false" dataType="Double" />
			<emp:text id="HousePurInfo.house_buildprice" label="房屋构建单价" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="HousePurInfo.house_eval_value" label="房屋评估价" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="HousePurInfo.is_fst_pur_house" label="是否首购房" required="false" dictname="STD_ZX_YES_NO" />
			<emp:date id="HousePurInfo.house_build_year" label="房屋建成年份" required="false" />
			<emp:text id="HousePurInfo.hourec_no" label="商品房买卖合同网上备案登记号" maxlength="20" required="false" />
			<emp:select id="HousePurInfo.dispute_mode" label="纠纷解决方式" required="false" />
			<emp:text id="HousePurInfo.house_qnt" label="住房套数" maxlength="38" required="false" dataType="Int" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
