<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<% 
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = "";
	if(context.containsKey("type")){
		type = context.getDataValue("type").toString();
	}
	String cus_id = "";
	if(context.containsKey("cus_id")){
		cus_id = context.getDataValue("cus_id").toString();
	}
%>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	function doViewLmtAppFinSubpay() {
		var paramStr = LmtAppFinSubpayList._obj.getParamStr(['serno','cus_id']); 
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppFinSubpayViewPage.do"/>?'+paramStr+'&dcpa=yes'+'&type=tab&op=view&act=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	

	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="viewLmtAppFinSubpay" label="查看" />
	</div>

	<emp:table icollName="LmtAppFinSubpayList" pageMode="true" url="pageLmtAppFinSubpayQuery.do" reqParams="type=tab&rt=${context.rt}&LmtAppFinSubpay.cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号"/>
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称"/>
		<emp:text id="subpay_times" label="代偿笔数" dataType="Int"/>
		<emp:text id="subpay_totl_limit" label="代偿总额" dataType="Currency"/>
		<emp:text id="subpay_app_date" label="代偿申请日期"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    