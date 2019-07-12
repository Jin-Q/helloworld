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
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:450px;
}
.emp_field_text_onerow {
    display: inline;
    border-color: #b7b7b7;
    width:450px;/*这里是需要修改的宽度*/
}
</style>
<script type="text/javascript">
	
	function doReturn(){
		window.close();
	};
	
	/*--user code begin--*/

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusGrpMemberGroup" title="关联(集团)客户成员" maxColumn="2">
			<emp:text id="CusGrpMember.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:pop id="CusGrpMember.cus_id" label="成员客户码" url="queryCusLoanRelList.do?cusTypCondition=cusGrp" returnMethod="returnCus" readonly="true" required="true" />
			<emp:text id="CusGrpMember.cus_name" label="成员客户名称"  cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:select id="CusGrpMember.cus_type" label="成员客户类型" required="true" readonly="true" dictname="STD_ZB_CUS_TYPE" />
			<emp:select id="CusGrpMember.grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GROUP_TYPE" readonly="true" required="true" />
			<emp:textarea id="CusGrpMember.grp_corre_detail" label="关联(集团)关联关系描述" maxlength="250" required="false" colSpan="2" />
			
			
			<emp:text id="CusGrpMember.manager_id_displayname" label="主管客户经理" required="false" readonly="true"/>
			<emp:text id="CusGrpMember.manager_br_id_displayname" label="主管机构"  required="false" readonly="true"/>
			<emp:text id="CusGrpMember.input_id_displayname" label="登记人"   required="false" readonly="true"/>
			<emp:text id="CusGrpMember.input_br_id_displayname" label="登记机构"  required="false" readonly="true" />
			<emp:date id="CusGrpMember.input_date" label="申请日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			
			<emp:text id="CusGrpMember.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>	
			<emp:text id="CusGrpMember.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			<emp:text id="CusGrpMember.manager_id" label="责任人" required="true" readonly="true" hidden="true"/>			
			<emp:text id="CusGrpMember.manager_br_id" label="责任机构"  required="false" readonly="true" hidden="true"/>
			<emp:text id="CusGrpMember.gen_type" label="生成类型" maxlength="2" required="true" defvalue="2" hidden="true"/>
	</emp:gridLayout>
		
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
