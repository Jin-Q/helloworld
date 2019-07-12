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
		var asset_disp_no = ArpDispReclaimInfo.asset_disp_no._getValue();
		var guaranty_no = ArpDispReclaimInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryArpDispReclaimInfoList.do"/>?guaranty_no='+guaranty_no+'&asset_disp_no='+asset_disp_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="ArpDispReclaimInfoGroup" title="处置回收信息" maxColumn="2">
			<emp:text id="ArpDispReclaimInfo.serno" label="业务编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="ArpDispReclaimInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="ArpDispReclaimInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="false" readonly="true"/>
			<emp:select id="ArpDispReclaimInfo.is_cash" label="是否现金" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="ArpDispReclaimInfo.disp_amt" label="处置金额" maxlength="16" required="true" dataType="Currency" />
			<emp:date id="ArpDispReclaimInfo.disp_date" label="处置日期" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="ArpDispReclaimInfoGroup" title="登记信息" maxColumn="2">
			<emp:text id="ArpDispReclaimInfo.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="ArpDispReclaimInfo.input_br_id_displayname" label="登记机构"  required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="ArpDispReclaimInfo.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="ArpDispReclaimInfo.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="ArpDispReclaimInfo.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
