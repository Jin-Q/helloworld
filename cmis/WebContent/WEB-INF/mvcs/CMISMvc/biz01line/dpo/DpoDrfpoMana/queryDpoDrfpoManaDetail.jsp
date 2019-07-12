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
		var menuId = '${context.menuId}';
		if(menuId == 'pjcgl'){
			var url = '<emp:url action="queryIqpDrfpoManaList.do"/>?menuId=pjcgl';
		}else{
			var url = '<emp:url action="queryAccDrfpoRemindList.do"/>?menuId=pjdqtx_ywtx';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLoad(){

	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">			
		<emp:gridLayout id="IqpDrfpoManaGroup" title="票据池管理" maxColumn="2">
				<emp:text id="IqpDrfpoMana.drfpo_no" label="池编号" maxlength="30" required="false" hidden="true"/>
				<emp:pop id="IqpDrfpoMana.cus_id" label="客户码" required="true" url="queryAllCusPop.do?cusTypCondition=Com&returnMethod=returnCus" buttonLabel="选择"/>
				<emp:text id="IqpDrfpoMana.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
				<emp:text id="IqpDrfpoMana.image_guaranty_no" label="影像押品编号" maxlength="40" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
				<emp:text id="IqpDrfpoMana.bail_acc_no" label="回款保证金账号" maxlength="40" required="false" />
				<emp:text id="IqpDrfpoMana.bail_acc_name" label="回款保证金账户名" maxlength="60" required="false" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
				<emp:select id="IqpDrfpoMana.drfpo_type" label="池类型" required="true" dictname="STD_DRFPO_TYPE" colSpan="2"/>
				<emp:text id="IqpDrfpoMana.bill_amt" label="在池票面总金额" maxlength="18" required="true" dataType="Currency" defvalue="0" readonly="true"/>
				<emp:text id="IqpDrfpoMana.to_bill_amt" label="待入池票面总金额" maxlength="18" required="true" dataType="Currency" defvalue="0" readonly="true"/>
				<emp:select id="IqpDrfpoMana.status" label="状态" required="false" dictname="STD_DRFPO_STATUS" defvalue="00" readonly="true"/>
				<emp:textarea id="IqpDrfpoMana.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="IqpDrfpoManaGroup" maxColumn="2" title="登记信息">
				<emp:pop id="IqpDrfpoMana.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
				<emp:pop id="IqpDrfpoMana.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
				<emp:text id="IqpDrfpoMana.input_id_displayname" label="登记人" readonly="true" required="true"  />
				<emp:text id="IqpDrfpoMana.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
				<emp:text id="IqpDrfpoMana.input_date" label="登记日期" required="true" readonly="true"/>
				<emp:text id="IqpDrfpoMana.manager_br_id" label="管理机构"  required="true" hidden="true"/>
				<emp:text id="IqpDrfpoMana.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
				<emp:text id="IqpDrfpoMana.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"   />
				<emp:text id="IqpDrfpoMana.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"  />
			</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="return" label="返回到列表页面"/>
		</div>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
