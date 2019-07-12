<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
.emp_field_select_select {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 210px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	/*--user code begin--*/
	function doReturn() {
		var url = "";
		url = '<emp:url action="queryCusGrpInfoApplyList.do"/>';		
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doload(){
		menuId = "${context.menuId}";
		if(menuId == 'grpCognizApp' || menuId == 'grpCognizChg'|| menuId == 'grpCognizHis'){
			document.getElementById('button_return').style.display = '';
		}else{
			document.getElementById('button_return').style.display = 'none';
		}
	};
	function changeHeight(){
		var iframeid = document.getElementById("rightframe");
		iframeid.height = "80px";
		iframeid.style.height = "80px";
		if(iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){
			iframeid.height = iframeid.contentDocument.body.offsetHeight;
		}else if(iframeid.Document && iframeid.Document.body.scrollHeight){
			iframeid.height = iframeid.Document.body.scrollHeight;
		}
		if(iframeid.height != "undefined")
			iframeid.style.height = iframeid.height + "px";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="基本信息" id="main_tabs">
	<emp:gridLayout id="CusGrpInfoApplyGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfoApply.serno" label="申请流水号" maxlength="40" required="true" />
			<emp:text id="CusGrpInfoApply.grp_no" label="关联(集团)编号" maxlength="30" required="true"  />
			<emp:text id="CusGrpInfoApply.grp_name" label="关联(集团)名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:pop id="CusGrpInfoApply.parent_cus_id" label="主申请关联(集团)客户码" url="queryCusComPopList.do" required="true" returnMethod="onReturn" />
			<emp:text id="CusGrpInfoApply.parent_cus_name" label="主申请关联(集团)客户名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusGrpInfoApply.parent_org_code" label="主申请关联(集团)统一社会信用代码" maxlength="32" required="true" />
			<emp:text id="CusGrpInfoApply.parent_loan_card" label="主申请关联(集团)贷款卡编码" maxlength="16" required="true" />
			<emp:select id="CusGrpInfoApply.grp_finance_type" label="关联(集团)融资形式"  dictname="STD_ZB_GRP_FIN_TYPE"  />
			<emp:select id="CusGrpInfoApply.grp_cus_type" label="集团客户类型"  required="true" dictname="STD_ZB_GRP_CUS_TYPE"  />
			<emp:textarea id="CusGrpInfoApply.grp_detail" label="关联(集团)情况说明" maxlength="250" required="true" colSpan="2"/>	
	</emp:gridLayout>	
	<emp:gridLayout id="CusGrpInfoApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusGrpInfoApply.manager_id_displayname" label="责任人" required="true"  url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusGrpInfoApply.manager_br_id_displayname" label="管理机构"  required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:text id="CusGrpInfoApply.input_id_displayname" label="登记人"  readonly="true" required="true"  defvalue="$currentUserId" />
			<emp:text id="CusGrpInfoApply.input_br_id_displayname" label="登记机构"  readonly="true" required="true"  defvalue="$organNo" />
			<emp:text id="CusGrpInfoApply.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY" colSpan="2" />
			
			<emp:text id="CusGrpInfoApply.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_id" label="责任人" maxlength="20" required="true" readonly="true" hidden="true"/>
			<emp:pop id="CusGrpInfoApply.manager_br_id" label="管理机构"  required="true"  url="" readonly="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
	</emp:gridLayout>
	<br>
	<div align="center">
			<emp:button id="return" label="返回"/>
	</div>

	<div class='emp_gridlayout_title'>关联(集团)客户成员信息</div><br>
	<iframe id="rightframe" name="rightframe" onload="changeHeight()"
	src="<emp:url action='queryCusGrpInfoApplyCusGrpMemberApplyList.do'/>&CusGrpInfoApply.grp_no=${context.CusGrpInfoApply.grp_no}
	&CusGrpInfoApply.parent_cus_id=${context.CusGrpInfoApply.parent_cus_id}&CusGrpInfoApply.serno=${context.CusGrpInfoApply.serno}
	&hidden_button=true"
	frameborder="0" scrolling="auto"  width="100%">
	</iframe>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup >


</body>
</html>
</emp:page>
