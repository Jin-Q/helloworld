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
		var url = '<emp:url action="queryPrdLiborMaintainList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="PrdLiborMaintainGroup" title="LIBOR维护" maxColumn="2">
			<emp:date id="PrdLiborMaintain.libor_date" label="LIBOR日期" required="true" />
			<emp:select id="PrdLiborMaintain.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />	
			<emp:text id="PrdLiborMaintain.last_ir" label="隔夜" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.one_week_ir" label="1周" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.two_week_ir" label="2周" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.one_month_ir" label="1个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.two_month_ir" label="2个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.three_month_ir" label="3个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.four_month_ir" label="4个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.five_month_ir" label="5个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.six_month_ir" label="6个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.seven_month_ir" label="7个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.eight_month_ir" label="8个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.nine_month_ir" label="9个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.ten_month_ir" label="10个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.eleven_month_ir" label="11个月" dataType="Rate" />
			<emp:text id="PrdLiborMaintain.twelve_month_ir" label="12个月" dataType="Rate" />	
			<emp:date id="PrdLiborMaintain.imp_date" label="导入日期" required="true" />
			<emp:date id="PrdLiborMaintain.start_date" label="生效日期" required="true" />
			<emp:pop id="PrdLiborMaintain.organno_displayname" label="机构码" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="PrdLiborMaintain.status" label="状态" required="false" dictname="STD_ZB_LIBOR_STATUS" />
			<emp:pop id="PrdLiborMaintain.maintain_id_displayname" label="维护人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setMaintainConId" required="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:pop id="PrdLiborMaintain.check_id_displayname" label="复核人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setCheckId" required="true" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="PrdLiborMaintain.organno" label="机构码" required="false" hidden="true"/>
			<emp:text id="PrdLiborMaintain.maintain_id" label="维护人" required="false" hidden="true"/>
			<emp:text id="PrdLiborMaintain.check_id" label="复核人" required="false" hidden="true" />
			<emp:text id="PrdLiborMaintain.pk_id" label="PK_ID" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
