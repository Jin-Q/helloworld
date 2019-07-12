<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpBksyndicList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewIqpBksyndicInfo() {
		var paramStr = IqpBksyndic.IqpBksyndicInfo._obj.getParamStr(['pk1']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIqpBksyndicIqpBksyndicInfoDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="IqpBksyndicGroup" title="银团从表" maxColumn="2">
			<emp:text id="IqpBksyndic.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="IqpBksyndic.bank_syndic_type" label="银团类型" required="false" />
			<emp:select id="IqpBksyndic.agent_org_flag" label="代理行标志" required="false" />
			<emp:text id="IqpBksyndic.bank_syndic_amt" label="银团贷款总金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBksyndic.agent_rate" label="代理费率" maxlength="10" required="false" dataType="Rate" />
			<emp:text id="IqpBksyndic.amt_arra_rate" label="资金安排费率" maxlength="10" required="false" dataType="Rate" />
			<emp:textarea id="IqpBksyndic.bank_syndic_desc" label="银团项目描述" maxlength="250" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<br>

	<emp:tabGroup id="IqpBksyndic_tabs" mainTab="IqpBksyndicInfo_tab">
		<emp:tab id="IqpBksyndicInfo_tab" label="银团信息">
			<div align="left">
				<emp:button id="viewIqpBksyndicInfo" label="查看" op="view_IqpBksyndicInfo"/>
			</div>
			<emp:table icollName="IqpBksyndic.IqpBksyndicInfo" pageMode="false" url="">
		<emp:text id="prtcpt_org_no" label="参与行行号" />
		<emp:text id="prtcpt_org_name" label="参与行行名" />
		<emp:text id="prtcpt_role" label="参与角色" />
		<emp:text id="prtcpt_amt_rate" label="参与金额比例" />
		<emp:text id="prtcpt_curr" label="参与币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="prtcpt_amt" label="参与金额" />
		<emp:text id="pk1" label="PK1" hidden="true"/>
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
