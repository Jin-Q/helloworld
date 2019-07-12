<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryArpDbtCongnizAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		addCusForm(ArpDbtCongnizApp);
		
		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}

		var approve_status = ArpDbtCongnizApp.approve_status._getValue();
		if(approve_status == '997'){
			ArpDbtCongnizApp.app_date._obj._renderHidden(false);
			ArpDbtCongnizApp.over_date._obj._renderHidden(false);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="呆账认定信息" id="main_tabs">
	<emp:gridLayout id="ArpDbtCongnizAppGroup" title="呆账认定申请" maxColumn="2">
			<emp:text id="ArpDbtCongnizApp.serno" label="业务编号" maxlength="40" readonly="true" />
			<emp:text id="ArpDbtCongnizApp.cus_id" label="客户码" required="true"  colSpan="2" readonly="true"/>
			<emp:text id="ArpDbtCongnizApp.cus_id_displayname" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />
			<emp:text id="ArpDbtCongnizApp.qnt" label="笔数" maxlength="38" required="true" dataType="Int" />
			<emp:text id="ArpDbtCongnizApp.loan_balance" label="总贷款余额" maxlength="16" required="true" 
			dataType="Currency" readonly="true" />			
			<emp:textarea id="ArpDbtCongnizApp.congniz_resn" label="认定理由" maxlength="250" required="true" colSpan="2" />
			<emp:textarea id="ArpDbtCongnizApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpDbtCongnizApp.app_date" label="申请日期" required="false"  hidden="true"/>
			<emp:date id="ArpDbtCongnizApp.over_date" label="办结日期" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpDbtCongnizAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="ArpDbtCongnizApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="ArpDbtCongnizApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" readonly="true"/>
			<emp:text id="ArpDbtCongnizApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpDbtCongnizApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpDbtCongnizApp.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="ArpDbtCongnizApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="ArpDbtCongnizApp.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpDbtCongnizApp.input_br_id" label="登记机构" required="true"  hidden="true" />
			<emp:date id="ArpDbtCongnizApp.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpDbtCongnizApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"   readonly="true" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>