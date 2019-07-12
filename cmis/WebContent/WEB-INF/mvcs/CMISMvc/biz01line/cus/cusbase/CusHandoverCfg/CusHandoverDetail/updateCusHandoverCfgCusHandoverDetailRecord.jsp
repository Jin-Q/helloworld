<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusHandoverCfgCusHandoverDetailRecord.do" method="POST">
		<emp:gridLayout id="CusHandoverDetailGroup" title="客户移交配置信息表" maxColumn="2">
			
			<emp:text id="CusHandoverDetail.table_code" label="表编码" maxlength="30" required="false" />
			<emp:text id="CusHandoverDetail.table_name" label="表名称" maxlength="100" required="false" />
			<emp:textarea id="CusHandoverDetail.ext_sql" label="执行语句" maxlength="300" required="true" colSpan="2"/>
			<emp:text id="CusHandoverDetail.sort" label="执行顺序" maxlength="3" required="true" colSpan="2"/>
			<emp:textarea id="CusHandoverDetail.memo" label="备注" maxlength="300" required="false" colSpan="2"/>
			<emp:text id="CusHandoverDetail.sub_serno" label="主键" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusHandoverDetail.serno" label="序列号" maxlength="40" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<fieldSet style="font-weight: 800"><legend>执行语句中变量使用说明：</legend> 
		<blockquote>变量以冒号开头，各变量代表内容如下。</blockquote>
		<blockquote>serno：移交申请流水号，handoverid：移出人，handoversorg：移出机构，<br>
					receiveid：接收人，receivesorg：接收机构，cusid：客户码
		</blockquote>
		</fieldSet>
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" />
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
