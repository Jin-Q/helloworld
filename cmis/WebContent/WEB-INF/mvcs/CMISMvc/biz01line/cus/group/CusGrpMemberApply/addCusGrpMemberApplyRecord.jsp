<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String pCusId=request.getParameter("parent_cus_id");
%>
<emp:page>
<html>
<head>
<title>子表添加记录页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 450;
	height: 60;
};
.emp_field_text_readonly {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 210px;
};
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function returnCus(data){                 
		var parentCusId = "<%=pCusId%>";
		var cus_id = data.cus_id._getValue();
		if(parentCusId==cus_id){
			alert("客户码不能与主关联(集团)客户的客户码相同！");
			return;
		}
		CusGrpMemberApply.cus_id._setValue(cus_id);
		CusGrpMemberApply.cus_name._setValue(data.cus_name._getValue());
		CusGrpMemberApply.cus_type._setValue(data.cus_type._getValue());
		CusGrpMemberApply.manager_br_id._setValue(data.main_br_id._getValue());
		CusGrpMemberApply.manager_id._setValue(data.cust_mgr._getValue());
		checkExistInGrpMember(data.cus_id._getValue());
	}

	function checkExistInGrpMember(cus_id){
		var url = "<emp:url  action='checkExistInGrpMemberApply.do'/>&cus_id="+cus_id;
        var callback = {
                success : "doReturnMethod",
                isJSON : true
        };
        EMPTools.ajaxRequest('GET', url, callback);
	}
	function doReturnMethod(json,callback){
		var obj = eval(json);
	    var flag = obj.flag;
	    var com_grp_mode = obj.com_grp_mode;
	    if(flag != "cg"){
	    	CusGrpMemberApply.cus_id._setValue("");
			CusGrpMemberApply.cus_name._setValue("");
			CusGrpMemberApply.cus_type._setValue("");
			CusGrpMemberApply.manager_br_id._setValue("");
			CusGrpMemberApply.manager_id._setValue("");
	    	if(flag == "mgs"){
	    		alert("该客户已作为[主申请关联(集团)客户]存在!");
	    	}else if(flag == "cyls"){
	    		alert("该客户已存在于别的集团中!");
	    	}else if(flag == "sq"){
	    		alert("该客户已存在于集团认定中!");
	    	}else if(flag == "cant_grp"){
	    		alert("[集团客户类型]为：\n[非集团客户]的客户不能进行集团认定!");
	    	}else if(flag == "cant_member"){
	    		alert("[集团客户类型]为[关联集团核心企业(母公司)]的客户不能在此处添加!");
	    	}
		}else{
			CusGrpMemberApply.grp_corre_type._setValue(com_grp_mode);
		}
	}
 
	function doOnload(){
		CusGrpMemberApply.cus_id._obj.config.url=CusGrpMemberApply.cus_id._obj.config.url+"&returnMethod=returnCus";
		removeGrpCorreType();		
	}		
	//屏蔽【集团关联关系类型】中母公司
	function  removeGrpCorreType(){
		var options = CusGrpMemberApply.grp_corre_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '1'){
				options.remove(i);
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusGrpInfoApplyCusGrpMemberApplyRecord.do" method="POST">
		<emp:gridLayout id="CusGrpMemberApplyGroup" title="关联(集团)客户成员" maxColumn="2">
			<emp:text id="CusGrpMemberApply.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusGrpMemberApply.serno" label="申请编号" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:pop id="CusGrpMemberApply.cus_id" label="成员客户码" url="queryAllCusPop.do?cusTypCondition=belg_line in('BL100','BL200') and cus_status='20'&returnMethod=returnCus" required="true" colSpan="2"/>
			<emp:text id="CusGrpMemberApply.cus_name" label="成员客户名称" maxlength="60" required="true" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:select id="CusGrpMemberApply.cus_type" label="成员客户类型" required="true" readonly="true" dictname="STD_ZB_CUS_TYPE" />
			<emp:select id="CusGrpMemberApply.grp_corre_type" label="关联(集团)关联关系类型" required="true" dictname="STD_ZB_GROUP_TYPE" readonly="true" />
			<emp:textarea id="CusGrpMemberApply.grp_corre_detail" label="关联(集团)关联关系描述" maxlength="250" required="false" colSpan="2"/>
			<emp:text id="CusGrpMemberApply.manager_br_id" label="管理机构"  required="true" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="CusGrpMemberApply.manager_id" label="责任人"  required="true" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="CusGrpMemberApply.input_id" label="申请人"  required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusGrpMemberApply.gen_type" label="生成类型" maxlength="2" required="false" hidden="true" defvalue="2"/>
			<emp:text id="CusGrpMemberApply.input_date" label="申请日期" maxlength="10" required="true" defvalue="$OPENDAY" hidden="true"/>
			<emp:text id="CusGrpMemberApply.input_br_id" label="申请机构" required="true" defvalue="$organNo" hidden="true"/>			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" />
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
