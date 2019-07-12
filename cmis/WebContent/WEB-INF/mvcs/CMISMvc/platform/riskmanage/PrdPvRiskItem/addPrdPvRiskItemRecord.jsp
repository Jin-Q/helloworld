<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function choose_one_js(){
	var chooseOne = PrdPvRiskItem.choose_one._getValue();
	if(chooseOne == 1){
		PrdPvRiskItem.item_rules._obj._renderHidden(false);
		PrdPvRiskItem.item_rules._obj._renderRequired(true);
		PrdPvRiskItem.item_rules_displayname._obj._renderHidden(false);
		PrdPvRiskItem.item_rules_displayname._obj._renderRequired(true);
		PrdPvRiskItem.item_class._obj._renderHidden(true);
		PrdPvRiskItem.item_class._obj._renderRequired(false);
		PrdPvRiskItem.item_class._setValue("");
	}else if(chooseOne == 2){
		PrdPvRiskItem.item_rules._obj._renderHidden(true);
		PrdPvRiskItem.item_rules._obj._renderRequired(false);
		PrdPvRiskItem.item_rules_displayname._obj._renderHidden(true);
		PrdPvRiskItem.item_rules_displayname._obj._renderRequired(false);
		PrdPvRiskItem.item_class._obj._renderHidden(false);
		PrdPvRiskItem.item_class._obj._renderRequired(true);
		PrdPvRiskItem.item_rules._setValue("");
		PrdPvRiskItem.item_rules_displayname._setValue("");
	}
};
	
</script>
</head>
<body class="page_content" >
	<emp:form id="submitForm" action="addPrdPvRiskItemRecord.do" method="POST">
		<emp:gridLayout id="PrdPvRiskItemGroup" title="风险拦截项目" maxColumn="2">
			<emp:text id="PrdPvRiskItem.item_id" label="项目编号" maxlength="32" hidden="true" required="false" />
			<emp:text id="PrdPvRiskItem.item_name" label="项目名称" maxlength="80" required="true" cssElementClass="emp_field_text_long"/>
			<emp:textarea id="PrdPvRiskItem.item_desc" label="检查说明" maxlength="500" required="false" colSpan="2" />
			<emp:select id="PrdPvRiskItem.choose_one" label="是否使用规则" required="true" onchange="choose_one_js();" dictname="STD_ZX_YES_NO" />
			<emp:pop id="PrdPvRiskItem.item_rules" label="检查规则" required="true" url="rulespop.do?id=PrdPvRiskItem.item_rules" colSpan="2"/>
			<emp:textarea id="PrdPvRiskItem.item_rules_displayname" label="检查规则描述"  readonly="true" colSpan="2" />
			<emp:textarea id="PrdPvRiskItem.item_class" label="风险拦截实现类" maxlength="300" required="false" colSpan="2" />
			<emp:textarea id="PrdPvRiskItem.link_url" label="外部链接实现类" maxlength="1000" required="false" colSpan="2" />
			<emp:select id="PrdPvRiskItem.used_ind" label="启用标志" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="PrdPvRiskItem.input_id" label="登记人" maxlength="20" defvalue="$currentUserId" hidden="true" />
			<emp:text id="PrdPvRiskItem.input_br_id" label="登记机构" maxlength="20" defvalue="$organNo" hidden="true" />
			<emp:date id="PrdPvRiskItem.input_date" label="登记日期" hidden="true" defvalue="$OPENDAY"/>
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

