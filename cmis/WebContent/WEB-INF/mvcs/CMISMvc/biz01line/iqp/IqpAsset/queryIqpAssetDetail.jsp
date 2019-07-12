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
		history.go(-1);
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:tabGroup mainTab="mainTab" id="mainTab">
		<emp:tab label="资产包管理信息" id="mainTab">
			<emp:form id="submitForm" action="updateIqpAssetRecord.do" method="POST">
				<emp:gridLayout id="IqpAssetGroup" maxColumn="2" title="资产包管理">
					<emp:text id="IqpAsset.asset_no" label="资产包编号" maxlength="40" required="false" readonly="true" colSpan="2" hidden="false" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAsset.asset_name" label="资产包名称" maxlength="100" required="false" readonly="true"/>
					<emp:text id="IqpAsset.asset_qnt" label="资产数量" maxlength="38" required="false" dataType="Int"/>    
					<emp:select id="IqpAsset.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE"/>
					<emp:select id="IqpAsset.asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE"/>
					<emp:text id="IqpAsset.asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" />
					<emp:text id="IqpAsset.takeover_total_amt" label="转让总额" maxlength="18" required="false" dataType="Currency" />
					<emp:date id="IqpAsset.takeover_date" label="转让日期" required="true"/>
					<emp:select id="IqpAsset.status" label="资产包状态" required="false" dictname="STD_ZB_ASSET_STATUS" readonly="true"/>
					<emp:text id="IqpAsset.input_id" label="登记人" maxlength="40" hidden="true" required="false" />
					<emp:text id="IqpAsset.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false" />
					<emp:date id="IqpAsset.input_date" label="登记日期" required="false" hidden="true"/>
				</emp:gridLayout>
				
				<div align="center">
					<br>
					<emp:button id="return" label="返回到列表页面"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		<emp:tab label="关联业务信息" id="subTab2" url="queryIqpAssetstrsfInfoList.do?asset_no=${context.IqpAsset.asset_no}&op=view" initial="false" needFlush="true" ></emp:tab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
