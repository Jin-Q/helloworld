<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
	<style type="text/css">
.emp_field_pop_input {
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturnList(){ 
			var url="<emp:url action='queryIndGroupList.do'></emp:url>"; 
			window.location=url; 
			}
	function doload(){
		IndGroup.sup_group_no._obj.config.url=IndGroup.sup_group_no._obj.config.url+"&returnMethod=returnGroupNo";
		IndGroup.trans_id._obj.config.url=IndGroup.trans_id._obj.config.url+"&returnMethod=returnTransId";
	 }
	 function returnGroupNo(data){
		 IndGroup.sup_group_no._setValue(data.group_no._getValue()); 
     }			
     function returnTransId(data){
    	 IndGroup.trans_id._setValue(data.trans_id._getValue());
     }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addIndGroupRecord.do" method="POST">
		<emp:gridLayout id="IndGroupGroup" title="指标组别信息" maxColumn="2">
			<emp:text id="IndGroup.group_no" label="组别编号" maxlength="12" required="true" />
			<emp:text id="IndGroup.group_name" label="组别名称" maxlength="60" required="false" />
			<emp:select id="IndGroup.group_kind" label="组性质" hidden="true" dictname="STD_ZB_GROUP_PROP" defvalue="2"/>
			<emp:pop id="IndGroup.rating_rules" label="组评分规则" required="true" url="rulespop.do?id=IndGroup.rating_rules" returnMethod="returnMod" colSpan="2"/>
			<emp:textarea id="IndGroup.rating_rules_displayname" label="组评分规则描述" readonly="true" colSpan="2"/>
			<emp:pop id="IndGroup.sup_group_no" label="上级组编号"  hidden="true" url="queryIndGroupPopList.do"/>
			<emp:pop id="IndGroup.trans_id" label="规则交易ID" url="querySfTransPopList.do" required="true" />
			<emp:textarea id="IndGroup.memo" label="备注" maxlength="100" required="false" colSpan="2"/>
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="returnList" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
