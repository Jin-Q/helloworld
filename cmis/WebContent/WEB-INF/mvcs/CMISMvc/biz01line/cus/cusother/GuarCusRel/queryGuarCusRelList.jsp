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
		GuarCusRel._toForm(form);
		GuarCusRelList._obj.ajaxQuery(null,form);
	};
		
	function doReset(){
		page.dataGroups.GuarCusRelGroup.reset();
	};
	function doOnLoad() {
		var optionJosn = "00,01";
		var options =GuarCusRel.guar_cont_state._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options[i].value)<0){
				options.remove(i);
			}
		}
	};	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm">
		<emp:gridLayout id="GuarCusRelGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="GuarCusRel.borrower_id" label="借款人客户码" />
			<emp:text id="GuarCusRel.guar_cus_id" label="保证人客户码" />
			<emp:select id="GuarCusRel.guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
			<emp:text id="GuarCusRel.borrower_name" label="借款人名称" />
			<emp:text id="GuarCusRel.guar_cus_name" label="保证人名称" />
		</emp:gridLayout>	
</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<br>
	<emp:table icollName="GuarCusRelList" pageMode="true" url="pageGuarCusRelQuery.do">
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="borrower_id" label="借款人客户码" />
		<emp:text id="borrower_name" label="借款人名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="used_amt" label="已用担保金额" dataType="Currency"/>
		<emp:text id="guar_cus_id" label="保证人客户码" />
		<emp:text id="guar_cus_name" label="保证人名称" />
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:table>
	</body>
</html>
</emp:page>
    