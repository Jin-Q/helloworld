<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

//机构pop返回方法
function getOrgID(data){
	PrdLiborMaintain.organno._setValue(data.organno._getValue());
	PrdLiborMaintain.organno_displayname._setValue(data.organname._getValue());
};
//pop返回方法
function setMaintainConId(data){
	PrdLiborMaintain.maintain_id_displayname._setValue(data.actorname._getValue());
	PrdLiborMaintain.maintain_id._setValue(data.actorno._getValue());
};
//pop返回方法
function setCheckId(data){
	PrdLiborMaintain.check_id_displayname._setValue(data.actorname._getValue());
	PrdLiborMaintain.check_id._setValue(data.actorno._getValue());
};

function sub(){
	var form = document.getElementById('queryForm');
	if(!PrdLiborMaintain._check()){
       return;
	}
	PrdLiborMaintain._toForm(form);
	form.submit();
}
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdLiborMaintainRecord.do" method="POST">
		<emp:gridLayout id="PrdLiborMaintainGroup" title="LIBOR维护" maxColumn="2">
			<emp:date id="PrdLiborMaintain.libor_date" label="LIBOR日期" required="true" />
			<emp:select id="PrdLiborMaintain.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:select id="PrdLiborMaintain.rate_type" label="利率种类" required="true" dictname="STD_ZB_RATE_TYPE" />
			<emp:text id="PrdLiborMaintain.ruling_ir" label="基准利率" maxlength="12" required="true" dataType="Rate" />
			<emp:date id="PrdLiborMaintain.imp_date" label="导入日期" required="false" readonly="true"/>
			<emp:date id="PrdLiborMaintain.start_date" label="生效日期" required="false" readonly="true"/>
			<emp:pop id="PrdLiborMaintain.organno_displayname" label="机构码" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="PrdLiborMaintain.status" label="状态" required="false" dictname="STD_ZB_LIBOR_STATUS" readonly="true"/>
			<emp:pop id="PrdLiborMaintain.maintain_id_displayname" label="维护人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setMaintainConId" required="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:pop id="PrdLiborMaintain.check_id_displayname" label="复核人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setCheckId" required="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="PrdLiborMaintain.organno" label="机构码" required="false" hidden="true"/>
			<emp:text id="PrdLiborMaintain.maintain_id" label="维护人" required="false" hidden="true"/>
			<emp:text id="PrdLiborMaintain.check_id" label="复核人" required="false" hidden="true" />
			<emp:text id="PrdLiborMaintain.pk_id" label="PK_ID" maxlength="40" required="false" hidden="true"/>
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

