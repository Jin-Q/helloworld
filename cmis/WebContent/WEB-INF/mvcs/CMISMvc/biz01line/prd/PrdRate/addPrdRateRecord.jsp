<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	 function setconId(data){
		 PrdRate.manager_id_displayname._setValue(data.actorname._getValue());
		 PrdRate.manager_id._setValue(data.actorno._getValue());
	 }	  
	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdRateRecord.do" method="POST">
		
		<emp:gridLayout id="PrdRateGroup" title="利率表" maxColumn="2">
			<emp:text id="PrdRate.biz_type" label="业务品种" maxlength="10" required="true" />
			<emp:select id="PrdRate.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="PrdRate.base_remit_type" label="基准利率项目类型" maxlength="5" required="true" />
			<emp:text id="PrdRate.base_rate_m" label="基准利率（年）" maxlength="20" required="true" dataType="Rate" />
			<emp:text id="PrdRate.rate_float_max" label="利率浮动上限" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="PrdRate.rate_float_min" label="利率浮动下限" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="PrdRate.term_max" label="期限上限（月）" maxlength="22" required="true" dataType="Int"/>
			<emp:text id="PrdRate.term_min" label="期限下限（月）" maxlength="22" required="true" dataType="Int"/>
			<emp:textarea id="PrdRate.term_memo" label="期限说明" maxlength="250" required="false" colSpan="2" />  
			<emp:pop id="PrdRate.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"  required="true"/>
			<emp:text id="PrdRate.manager_id" label="责任人" maxlength="40" required="false" readonly="true" hidden="true"/>  
			<emp:date id="PrdRate.inure_date" label="生效日期" required="true" /> 
			<emp:date id="PrdRate.regi_date" label="登记日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
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

