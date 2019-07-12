<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function refreshCusGrpMember() {
		CusGrpInfo_tabs.tabs.CusGrpMember_tab.refresh();
	};

	/*--user code begin--*/
	function onReturn(data){
		CusGrpInfo.parent_cus_id._setValue(data.cus_id._getValue());
		CusGrpInfo.parent_cus_name._setValue(data.cus_name._getValue());	
	}		
	function doReturn() {
		var url = '<emp:url action="queryCusGrpInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusGrpInfoRecord.do" method="POST">
		<emp:gridLayout id="CusGrpInfoGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" maxlength="60" required="true" />
			<emp:pop id="CusGrpInfo.parent_cus_id" label="主关联(集团)客户码" url="queryCusComPopList.do" required="true" returnMethod="onReturn" readonly="true"/>
			<emp:text id="CusGrpInfo.parent_cus_name" label="主关联(集团)客户名称" maxlength="60" required="true" readonly="true"/>
			<emp:text id="CusGrpInfo.parent_org_code" label="主关联(集团)组织机构代码" maxlength="10" required="true" readonly="true"/>
			<emp:text id="CusGrpInfo.parent_loan_card" label="主关联(集团)贷款卡编码" maxlength="16" required="false" readonly="true"/>
			<emp:select id="CusGrpInfo.grp_finance_type" label="关联(集团)融资形式" required="false" hidden="true" dictname="STD_ZB_GRP_FIN_TYPE"/>
			<emp:select id="CusGrpInfo.grp_cus_type" label="集团客户类型"  required="true" dictname="STD_ZB_GRP_CUS_TYPE"  />
			<emp:textarea id="CusGrpInfo.grp_detail" label="关联(集团)情况说明" maxlength="250" required="false" colSpan="2" />
			
			
			<emp:text id="CusGrpInfo.main_br_id" label="主办行"  required="false" readonly="true" hidden="true"/>
			<emp:text id="CusGrpInfo.cus_manager" label="主办客户经理" required="true" readonly="true" hidden="true"/>
			<emp:pop id="CusGrpInfo.manager_br_id" label="管理机构" url=""  required="true" />			
			<emp:text id="CusGrpInfo.input_id" label="登记人" maxlength="20" required="true" readonly="true" />	
			<emp:text id="CusGrpInfo.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true" />
			<emp:text id="CusGrpInfo.input_date" label="登记日期" maxlength="10" required="true" readonly="true"/>
		</emp:gridLayout>
	

		<div align="center">
			<emp:button id="submit" label="确定" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	<emp:tabGroup id="CusGrpInfo_tabs" mainTab="CusGrpMember_tab">
		<emp:tab id="CusGrpMember_tab" label="关联(集团)客户成员" url="queryCusGrpInfoCusGrpMemberList.do" reqParams="CusGrpInfo.grp_no=$CusGrpInfo.grp_no;&CusGrpInfo.parent_cus_id=$CusGrpInfo.parent_cus_id;" initial="true" needFlush="true"/>
				
	</emp:tabGroup>
</body>
</html>
</emp:page>