<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
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
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusOrgAppList.do?resourceType=app"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doOnload(){
		LmtApp_tabs.tabs.pro_tab._clickLink();
		CusOrgApp.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	};
   
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+CusOrgApp.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

    function onReturnRegStateCode(date){
    	CusOrgApp.extr_eval_addr._obj.element.value=date.id;
    	CusOrgApp.extr_eval_addr_displayname._obj.element.value=date.label;
	};
	function getOrgID(data){
		CusOrgApp.manager_br_id._setValue(data.organno._getValue());
		CusOrgApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	function setconId(data){
		CusOrgApp.manager_id_displayname._setValue(data.actorname._getValue());
		CusOrgApp.manager_id._setValue(data.actorno._getValue());
		CusOrgApp.manager_br_id._setValue(data.orgid._getValue());
		CusOrgApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusOrgApp.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusOrgApp.manager_br_id._setValue(jsonstr.org);
					CusOrgApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusOrgApp.manager_br_id._setValue("");
					CusOrgApp.manager_br_id_displayname._setValue("");
					CusOrgApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusOrgApp.manager_id._getValue();
					CusOrgApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusOrgApp.manager_br_id._setValue("");
					CusOrgApp.manager_br_id_displayname._setValue("");
					CusOrgApp.manager_br_id_displayname._obj._renderReadonly(false);
					CusOrgApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusOrgApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//保存
	function doUpdateCusOrgApp(){
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
		            //doReturn();
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
		var result = CusOrgApp._checkAll();
		if(result){
			CusOrgApp._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup id="LmtApp_tabs" mainTab="tab1" >
	<emp:tab id="pro_tab" label="评估机构申请信息" initial="true">
	<emp:form id="submitForm" action="updateCusOrgAppRecord.do" method="POST">
		
		<emp:gridLayout id="CusOrgAppGroup" title="评估机构认定" maxColumn="2">
			<emp:text id="CusOrgApp.serno" label="申请流水号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CusOrgApp.cus_id" label="评估机构客户码" required="true" readonly="true" colSpan="2" />
			<emp:text id="CusOrgApp.cus_name" label="评估机构名称" maxlength="60" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgApp.extr_eval_org" label="评估机构组织机构代码" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusOrgApp.extr_eval_addr" label="评估机构地址" required="false" hidden="true"/>
			<emp:pop id="CusOrgApp.extr_eval_addr_displayname" label="评估机构地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" required="true" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgApp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2" readonly="true"/>
			<emp:select id="CusOrgApp.extr_eval_quali" label="评估机构资质等级" required="true" dictname="STD_ZB_EXTR_EVAL_QUALI" />
			<emp:text id="CusOrgApp.extr_eval_rng" label="评估范围" maxlength="80" required="true" />
			<emp:text id="CusOrgApp.fic_per" label="法定代表人" maxlength="60" required="false" hidden="true" />
			<emp:text id="CusOrgApp.real_oper_per" label="实际经营人" maxlength="60" required="false" hidden="true"/>
			<emp:date id="CusOrgApp.founded_date" label="成立日期"  required="false" hidden="true" />
			<emp:text id="CusOrgApp.reg_cap_amt" label="注册资金" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="CusOrgApp.phone" label="联系电话" maxlength="35" required="false" dataType="Phone" colSpan="2" hidden="true"/>
			<emp:select id="CusOrgApp.extr_eval_exp_type" label="评估机构有效期类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="CusOrgApp.extr_eval_exp_term" label="评估机构有效期期限" maxlength="3" required="true" dataType="Int" />
			<emp:text id="CusOrgApp.realty_validation_lice_no" label="房地产价格评估资质证书号" maxlength="40" required="true" />
			<emp:text id="CusOrgApp.realty_validation_lice_name" label="房地产价格评估资质证书名称" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.land_extr_eval_reg_lice_no" label="土地评估中介机构注册证书号" maxlength="40" required="true" />
			<emp:text id="CusOrgApp.land_extr_eval_reg_lice_name" label="土地评估中介机构注册证书名称" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.cap_eval_quali_lice_no" label="资产评估资格证书号" maxlength="40" required="true" />
			<emp:text id="CusOrgApp.cap_eval_quali_lice_name" label="资产评估资格证书名称" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.reg_realty_estimater" label="注册房地产估价师" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.reg_land_estimater" label="注册土地估价师" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.cap_realty_estimater" label="注册资产评估师" maxlength="60" required="true" />
			<emp:textarea id="CusOrgApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" onblur="this.value = this.value.substring(0, 250)"  />
		</emp:gridLayout>
		<emp:gridLayout id="CusOrgAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusOrgApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusOrgApp.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusOrgApp.input_id_displayname" label="登记人" readonly="true" required="true"  defvalue="$currentUserId" />
			<emp:text id="CusOrgApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  defvalue="$organNo" />
			<emp:text id="CusOrgApp.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusOrgApp.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  hidden="true"/>
			<emp:text id="CusOrgApp.manager_id" label="责任人" maxlength="20" required="true" colSpan="2" readonly="true" hidden="true"/>
			<emp:text id="CusOrgApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="CusOrgApp.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" />
			<emp:text id="CusOrgApp.input_date" label="登记日期" colSpan="2" required="true" readonly="true" defvalue="$OPENDAY" />
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="updateCusOrgApp" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup >	
	</body>
</html>
</emp:page>
