<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
String pCusId=request.getParameter("parent_cus_id");
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

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
		CusGrpMemberApply.main_br_id._setValue(data.br_id._getValue());
		CusGrpMemberApply.cus_manager._setValue(data.main_cus_mgr._getValue());
		CusGrpMemberApply.main_br_id_displayname._setValue(data.br_id_displayname._getValue());
		CusGrpMemberApply.cus_manager_displayname._setValue(data.main_cus_mgr_displayname._getValue());
		CusGrpMemberApply.cus_type._setValue(data.cus_type._getValue());
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
	function doReturn(){
		window.close();
	};
	function doSubmits(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
		            alert('保存成功!');
		            doReturn();
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = CusGrpMemberApply._checkAll();
		if(result){
			CusGrpMemberApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="updateCusGrpInfoApplyCusGrpMemberApplyRecord.do" method="POST">
		<emp:gridLayout id="CusGrpMemberApplyGroup" title="关联(集团)客户成员" maxColumn="2" >
			<emp:text id="CusGrpMemberApply.serno" label="申请编号"  required="true" defvalue="2" readonly="true"/>
			<emp:text id="CusGrpMemberApply.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:pop id="CusGrpMemberApply.cus_id" label="成员客户码" url="queryCusLoanRelList.do?cusTypCondition=cusGrp" returnMethod="returnCus" readonly="true" required="true" colSpan="2"/>
			<emp:text id="CusGrpMemberApply.cus_name" label="成员客户名称"  cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:select id="CusGrpMemberApply.cus_type" label="成员客户类型" required="true" readonly="true" dictname="STD_ZB_CUS_TYPE" />
			<emp:select id="CusGrpMemberApply.grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GROUP_TYPE" readonly="true" required="true" />
			<emp:textarea id="CusGrpMemberApply.grp_corre_detail" label="关联(集团)关联关系描述" maxlength="250" required="false" colSpan="2" readonly="false"/>
			
			
			<emp:text id="CusGrpMemberApply.manager_id_displayname" label="主管客户经理" required="false" readonly="true"/>
			<emp:text id="CusGrpMemberApply.manager_br_id_displayname" label="主管机构"  required="false" readonly="true"/>
			<emp:text id="CusGrpMemberApply.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="CusGrpMemberApply.input_br_id_displayname" label="登记机构"  required="false" readonly="true" />
			<emp:date id="CusGrpMemberApply.input_date" label="申请日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			
			<emp:text id="CusGrpMemberApply.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>	
			<emp:text id="CusGrpMemberApply.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			<emp:text id="CusGrpMemberApply.manager_id" label="责任人" required="true" readonly="true" hidden="true"/>			
			<emp:text id="CusGrpMemberApply.manager_br_id" label="责任机构"  required="false" readonly="true" hidden="true"/>
			<emp:text id="CusGrpMemberApply.gen_type" label="生成类型" maxlength="2" required="true" defvalue="2" hidden="true"/>
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="修改"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
