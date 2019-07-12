<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
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
<script type="text/javascript">
	
	function refreshCusGrpMemberApply() {
		var url = "<emp:url action='queryCusGrpInfoApplyCusGrpMemberApplyList.do'/>&CusGrpInfoApply.grp_no=${context.CusGrpInfoApply.grp_no}"+
			"&CusGrpInfoApply.parent_cus_id=${context.CusGrpInfoApply.parent_cus_id}&CusGrpInfoApply.serno=${context.CusGrpInfoApply.serno}";		
		url = EMPTools.encodeURI(url);
		document.getElementById("rightframe").src=url;
	};

	/*--user code begin--*/
	function onReturn(data){
		CusGrpInfoApply.parent_cus_id._setValue(data.cus_id._getValue());
		CusGrpInfoApply.parent_cus_name._setValue(data.cus_name._getValue());	
	}		
	function doReturn() {
		var url = '<emp:url action="queryCusGrpInfoApplyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function toSubmitForm(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("保存成功");
					}
				} catch(e) {
				  alert("保存失败！");
				  return;
				}
	       	}
		};
		var handleFailure = function(o){
		};
		var callback = {
		    success:handleSuccess,
		    failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	function doSubmitt(){
	    var form = document.getElementById("submitForm");
		var result = CusGrpInfoApply._checkAll();
	    if(result){
	    	CusGrpInfoApply._toForm(form);
	        toSubmitForm(form);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	};

	function doSaveAndSubmit(){
		var form = document.getElementById("submitForm");    	     
	  	var result = CusGrpInfoApply._checkAll();
	    if(result){
	    	CusGrpInfoApply._toForm(form);
	        toSubmitForm(form);
	      	doSumbitCusCognizApply();
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	};
	
	function doSumbitCusCognizApply(){
		var paramStr = document.getElementById("CusGrpInfoApply.serno").value;
		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			doSumbitFlow(paramStr);
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="checkCusidApplyed.do"/>?type=singleSubmit&cus_id='+paramStr;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};

	function doSumbitFlow(paramStr){
		var _status = document.getElementById("CusGrpInfoApply.approve_status").value;
        if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'&&_status!= '993'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
		}
		var cus_id = document.getElementById("CusGrpInfoApply.grp_no").value;
		var cus_name = document.getElementById("CusGrpInfoApply.grp_name").value;
		var is_change = document.getElementById("CusGrpInfoApply.is_change").value;
		if (paramStr != null) {
			WfiJoin.table_name._setValue("CusGrpInfoApply");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			if(is_change == '1'){
				WfiJoin.appl_type._setValue("014");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
				WfiJoin.prd_name._setValue("集团客户变更申请");
			}else{
				WfiJoin.appl_type._setValue("008");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
				WfiJoin.prd_name._setValue("集团客户认定申请");
			}
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
    };

	function setconId(data){
		CusGrpInfoApply.manager_id_displayname._setValue(data.actorname._getValue());
		CusGrpInfoApply.manager_id._setValue(data.actorno._getValue());
		CusGrpInfoApply.manager_br_id._setValue(data.orgid._getValue());
		CusGrpInfoApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusGrpInfoApply.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusGrpInfoApply.manager_br_id._setValue(jsonstr.org);
					CusGrpInfoApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusGrpInfoApply.manager_br_id._setValue("");
					CusGrpInfoApply.manager_br_id_displayname._setValue("");
					CusGrpInfoApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusGrpInfoApply.manager_id._getValue();
					CusGrpInfoApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusGrpInfoApply.manager_br_id._setValue("");
					CusGrpInfoApply.manager_br_id_displayname._setValue("");
					CusGrpInfoApply.manager_br_id_displayname._obj._renderReadonly(false);
					CusGrpInfoApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusGrpInfoApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		CusGrpInfoApply.manager_br_id._setValue(data.organno._getValue());
		CusGrpInfoApply.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	
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

	/*** 集团名称唯一校验 ***/
	function checkCusid(obj){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == "false" ){
				alert("此关联(集团)名称已被使用，请重新填写：");
				obj.value='';
			}
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var form = document.getElementById("queryForm");
    	CusGrpInfoApply._toForm(form);
		var url = '<emp:url action="checkCusidApplyed.do"/>?cus_id=';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);
		form.action = url;
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="基本信息" id="main_tabs">
	<emp:form id="submitForm" action="updateCusGrpInfoApplyRecord.do" method="POST">
		<emp:gridLayout id="CusGrpInfoApplyGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfoApply.serno" label="申请流水号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusGrpInfoApply.grp_name" label="关联(集团)名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2" onchange="checkCusid(this)"/>
			<emp:pop id="CusGrpInfoApply.parent_cus_id" label="主申请关联(集团)客户码" url="queryCusComPopList.do" required="true" returnMethod="onReturn" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_cus_name" label="主申请关联(集团)客户名称" maxlength="60" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusGrpInfoApply.parent_org_code" label="主申请关联(集团)统一社会信用代码" maxlength="32" required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_loan_card" label="主申请关联(集团)贷款卡编码" maxlength="16" required="true" readonly="true"/>
			<emp:select id="CusGrpInfoApply.grp_finance_type" label="关联(集团)融资形式" defvalue="02" readonly="true" dictname="STD_ZB_GRP_FIN_TYPE" required="true" />
			<emp:select id="CusGrpInfoApply.grp_cus_type" label="集团客户类型"  required="true" dictname="STD_ZB_GRP_CUS_TYPE"  />
			<emp:textarea id="CusGrpInfoApply.grp_detail" label="关联(集团)情况说明" required="true" maxlength="250"  colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusGrpInfoApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusGrpInfoApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusGrpInfoApply.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusGrpInfoApply.input_id_displayname" label="登记人" readonly="true" required="true"  defvalue="$currentUserId" />
			<emp:text id="CusGrpInfoApply.input_br_id_displayname" label="登记机构"  readonly="true" required="true"  defvalue="$organNo" />
			<emp:text id="CusGrpInfoApply.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY" colSpan="2" />
			
			<emp:text id="CusGrpInfoApply.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_id" label="责任人" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_br_id" label="管理机构"  required="true" readonly="false" hidden="true"/>
			<emp:text id="CusGrpInfoApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusGrpInfoApply.is_change" label="是否变更标志" maxlength="20" hidden="true" />
		</emp:gridLayout>

		<div align="center">
			<emp:button id="submitt" label="保存" op="update"/>
			<emp:button id="saveAndSubmit" label="保存并提交" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	<div class='emp_gridlayout_title'>关联(集团)客户成员信息</div><br>
	<iframe id="rightframe" name="rightframe" onload="changeHeight()"
	src="<emp:url action='queryCusGrpInfoApplyCusGrpMemberApplyList.do'/>&CusGrpInfoApply.grp_no=${context.CusGrpInfoApply.grp_no}
	&CusGrpInfoApply.parent_cus_id=${context.CusGrpInfoApply.parent_cus_id}&CusGrpInfoApply.serno=${context.CusGrpInfoApply.serno}"
	frameborder="0" scrolling="auto"  width="100%">
	</iframe>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup >
</body>
</html>
</emp:page>
