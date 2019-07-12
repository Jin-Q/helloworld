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
		var url = '<emp:url action="queryLmtIndusAuthApplyList.do"/>?'+
			'serno=${context.serno}&single_amt=${context.single_amt}&action=${context.action}&change_list_flag=${context.change_list_flag}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content"> 

	<emp:gridLayout id="LmtIndusAuthApplyGroup" maxColumn="2" title="行业授权申请表">
			<emp:text id="LmtIndusAuthApply.serno" label="业务编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtIndusAuthApply.input_br_id_displayname" label="申请机构"  required="true" readonly="true" colSpan="2"/>
			<emp:select id="LmtIndusAuthApply.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" colSpan="2" />
			<emp:text id="LmtIndusAuthApply.single_auth_amt" label="单户授权金额(元)" maxlength="18" required="true"	 dataType="Currency" colSpan="2"/>
			<emp:select id="LmtIndusAuthApply.status" label="状态" required="false" hidden="true" dictname="STD_DRFPO_STATUS" />
			<emp:text id="LmtIndusAuthApply.input_br_id" label="申请机构" maxlength="20" required="true" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>	

</body>
</html>
</emp:page>
