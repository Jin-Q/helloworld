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
          window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpOverseeCusinfoGroup" title="监管客户信息" maxColumn="2">
			<emp:text id="IqpOverseeCusinfo.cus_name" label="客户名称" maxlength="32" required="true" />
			<emp:text id="IqpOverseeCusinfo.goods_name" label="储运货物名称" maxlength="32" required="true" />
			<emp:text id="IqpOverseeCusinfo.transfer_qnt" label="年吞吐量(吨)" maxlength="32" required="true" />
			<emp:text id="IqpOverseeCusinfo.avg_storage" label="平均仓储量(吨)" maxlength="20" required="true" />
			<emp:text id="IqpOverseeCusinfo.biz_yearn" label="年均业务收入(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpOverseeCusinfo.coop_bank" label="合作银行" maxlength="32" required="false" />
			<emp:text id="IqpOverseeCusinfo.cusinfo_id" label="客户信息编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeCusinfo.serno" label="流水主键" maxlength="32" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
	</div>
</body>
</html>
</emp:page>
