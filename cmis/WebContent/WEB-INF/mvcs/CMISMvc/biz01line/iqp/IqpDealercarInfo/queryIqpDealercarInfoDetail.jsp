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
		var url = '<emp:url action="queryIqpDealercarInfoList.do"/>?serno=${context.IqpDealercarInfo.serno}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpDealercarInfoGroup" title="经销商汽车信息" maxColumn="2">
			<emp:text id="IqpDealercarInfo.serno" label="业务编号" maxlength="40" hidden="true" />
			<emp:text id="IqpDealercarInfo.car_serno" label="车辆信息编号" maxlength="40" hidden="true"/>
			<emp:text id="IqpDealercarInfo.car_name" label="车辆名称" maxlength="100" required="true" /> 
			<emp:text id="IqpDealercarInfo.car_num" label="数量（辆）" maxlength="38" required="true" dataType="Int"/> 
			<emp:text id="IqpDealercarInfo.car_amt" label="单价" maxlength="16" required="true" dataType="Currency" />
			<emp:select id="IqpDealercarInfo.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
		    <emp:text id="IqpDealercarInfo.car_sign" label="车辆品牌" maxlength="40" required="true" />
		    <emp:select id="IqpDealercarInfo.car_type" label="车辆类型" required="true" dictname="STD_ZB_CAR_TYPE" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
