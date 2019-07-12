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
		//var url = '<emp:url action="queryPspIostoreDocList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PspIostoreDocGroup" title="出入库清单" maxColumn="2">
			<emp:select id="PspIostoreDoc.doc_type" label="类别" required="true" dictname="STD_ZB_PSP_IOSTORE_TYPE"/>
			<emp:text id="PspIostoreDoc.goods_name" label="货物名称" maxlength="100" required="true" />
			<emp:select id="PspIostoreDoc.qnt_unit" label="数量单位" required="true" dictname="STD_ZB_UNIT"/>
			<emp:text id="PspIostoreDoc.qnt" label="数量" maxlength="38" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="PspIostoreDoc.total_price" label="总价值" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="PspIostoreDoc.check_freq" label="检查频率" required="true" dictname="STD_ZB_PSP_CHECK_UNIT" colSpan="2"/>
			<emp:textarea id="PspIostoreDoc.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspIostoreDocGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspIostoreDoc.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspIostoreDoc.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspIostoreDoc.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspIostoreDoc.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspIostoreDoc.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="PspIostoreDoc.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspIostoreDoc.pk_id" label="主键" maxlength="32" readonly="true" required="false" hidden="true"/>
			<emp:text id="PspIostoreDoc.task_id" label="任务编号" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
