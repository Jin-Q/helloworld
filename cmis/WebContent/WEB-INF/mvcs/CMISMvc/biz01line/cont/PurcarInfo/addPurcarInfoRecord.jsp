<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPurcarInfoRecord.do" method="POST">
		
		<emp:gridLayout id="PurcarInfoGroup" title="机构法人购车信息" maxColumn="2">
			<emp:text id="PurcarInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="PurcarInfo.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="PurcarInfo.car_brand" label="汽车品牌" maxlength="60" required="false" />
			<emp:text id="PurcarInfo.car_name" label="汽车名称" maxlength="100" required="false" />
			<emp:text id="PurcarInfo.car_model" label="汽车型号" maxlength="20" required="false" />
			<emp:text id="PurcarInfo.car_shelf_no" label="车架号" maxlength="40" required="false" />
			<emp:select id="PurcarInfo.car_type" label="汽车种类" required="false" />
			<emp:select id="PurcarInfo.car_collect_type" label="汽车取得方式" required="false" />
			<emp:select id="PurcarInfo.car_use" label="汽车用途" required="false" />
			<emp:text id="PurcarInfo.car_sale" label="汽车销售商" maxlength="100" required="false" />
			<emp:text id="PurcarInfo.pur_amt" label="购买金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="PurcarInfo.loan_perc" label="贷款比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="PurcarInfo.fst_pyr_perc" label="首付款比率" maxlength="10" required="false" dataType="Percent" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

