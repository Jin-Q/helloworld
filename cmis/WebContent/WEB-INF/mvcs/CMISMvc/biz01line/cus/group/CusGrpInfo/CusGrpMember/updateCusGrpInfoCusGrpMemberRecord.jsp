<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<%
String pCusId=request.getParameter("parent_cus_id");
%>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_onerow {
    display: inline;
    border-color: #b7b7b7;
    width:450px;/*这里是需要修改的宽度*/
}
</style>
<script type="text/javascript">

	function doOnload(){
		var parentCusId = "<%=pCusId%>";
		if(CusGrpMember.cus_id._getValue()!=parentCusId) 
		removeGrpCorreType();
		CusGrpMember.cus_id._obj.config.url=CusGrpMember.cus_id._obj.config.url+"&returnMethod=returnCus";
	}

	//屏蔽【集团关联关系类型】中母公司
	function  removeGrpCorreType(){
		var options = CusGrpMember.grp_corre_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '1'){
				options.remove(i);
			}
		}
	}
	
	function doReturn(){
		window.close();
	};
	
	function returnCus(data){               
		var parentCusId = "<%=pCusId%>";
		var cus_id = data.cus_id._getValue();
		if(parentCusId==cus_id){
			alert("客户码不能与主关联(集团)客户的客户码相同！");
			return;
		}
		CusGrpMember.cus_id._setValue(cus_id);
		CusGrpMember.cus_name._setValue(data.cus_name._getValue());
		CusGrpMember.main_br_id._setValue(data.br_id._getValue());
		CusGrpMember.cus_manager._setValue(data.main_cus_mgr._getValue());
		CusGrpMember.main_br_id_displayname._setValue(data.br_id_displayname._getValue());
		CusGrpMember.cus_manager_displayname._setValue(data.main_cus_mgr_displayname._getValue());
		CusGrpMember.cus_type._setValue(data.cus_type._getValue());
	}
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="updateCusGrpInfoCusGrpMemberRecord.do" method="POST">
		<emp:gridLayout id="CusGrpMemberGroup" title="关联(集团)客户成员" maxColumn="2">
			<emp:text id="CusGrpMember.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" colSpan="2"/>
			
			<emp:pop id="CusGrpMember.cus_id" label="成员客户码" url="queryCusLoanRelList.do?cusTypCondition=cusGrp" returnMethod="returnCus" readonly="true" required="true" colSpan="2"/>
			<emp:text id="CusGrpMember.cus_name" label="成员客户名称" maxlength="60" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_onerow"/>
 			<emp:select id="CusGrpMember.cus_type" label="成员客户类型" required="true" readonly="true" dictname="STD_ZB_CUS_TYPE" />
 			
			<emp:select id="CusGrpMember.grp_corre_type" label="关联(集团)关联关系类型" required="true" dictname="STD_ZB_GROUP_TYPE" readonly="true"/>
			<emp:textarea id="CusGrpMember.grp_corre_detail" label="关联(集团)关联关系描述" maxlength="250" required="false" colSpan="2" />
			
			<emp:text id="CusGrpMember.main_br_id_displayname" label="主办行"  required="false" readonly="true"/>
			<emp:text id="CusGrpMember.cus_manager_displayname" label="主办客户经理" required="false" readonly="true"/>
			<emp:text id="CusGrpMember.input_user_id_displayname" label="登记人"  required="false" readonly="true"/>
			<emp:text id="CusGrpMember.input_br_id_displayname" label="登记机构"  required="false" readonly="true" />
			<emp:text id="CusGrpMember.input_date" label="登记日期" maxlength="10" required="true" readonly="true"/>
			
			<emp:text id="CusGrpMember.main_br_id" label="主办行"  required="false" readonly="true" hidden="true"/>
			<emp:text id="CusGrpMember.cus_manager" label="主办客户经理" required="true" readonly="true" hidden="true"/>			
			<emp:text id="CusGrpMember.input_user_id" label="登记人" maxlength="20" required="true" readonly="true" hidden="true"/>	
			<emp:text id="CusGrpMember.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true" hidden="true"/>

			<emp:text id="CusGrpMember.gen_type" label="生成类型" maxlength="2" required="true" defvalue="2" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<!--<emp:button id="submit" label="修改" op="update_CusGrpMember"/>
			-->
			<emp:button id="submit" label="修改"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
