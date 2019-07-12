<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function onReturn(data){
		CusGrpInfoApply.parent_cus_id._setValue(data.cus_id._getValue());
		CusGrpInfoApply.parent_cus_name._setValue(data.cus_name._getValue());
		CusGrpInfoApply.parent_org_code._setValue(data.cert_code._getValue());
		CusGrpInfoApply.parent_loan_card._setValue(data.loan_card_id._getValue());

		CusGrpInfoApply.manager_id._setValue(data.cust_mgr._getValue());
		CusGrpInfoApply.manager_br_id._setValue(data.main_br_id._getValue());	
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
	    if(flag != "cg" && flag !="cant_member"){
	    	CusGrpInfoApply.parent_cus_id._setValue("");
	    	CusGrpInfoApply.parent_cus_name._setValue("");
	    	CusGrpInfoApply.parent_org_code._setValue("");
	    	CusGrpInfoApply.parent_loan_card._setValue(""); 
	    	if(flag == "mgs"){
	    		alert("该客户已作为[主申请关联(集团)客户]存在!");
	    	}else if(flag == "cyls"){
	    		alert("该客户已存在于别的集团中!");
	    	}else if(flag == "sq"){
	    		alert("该客户已存在于集团认定中!");
	    	}else if(flag == "cant_grp"){
	    		alert("[集团客户类型]为：\n[非集团客户]的客户不能进行集团认定!");
	    	}
		}
	}
	
	function doload(){
		CusGrpInfoApply.parent_cus_id._obj.config.url=CusGrpInfoApply.parent_cus_id._obj.config.url+"&CusCom.cus_status=1&returnMethod=onReturn";
	}	

	function doReturn(){
		var paramStr="CusGrpInfoApply.cus_manager="+CusGrpInfoApply.manager_id._obj.element.value;
		var stockURL = '<emp:url action="queryCusGrpInfoApplyList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}	

	function doSubmitt(){
		var form = document.getElementById("submitForm");
   	    var result = CusGrpInfoApply._checkAll();
   	    if(result){
   	    	CusGrpInfoApply._toForm(form);
   	        form.submit();
   	    }
	};
	
	function getOrgID(data){
		CusGrpInfoApply.manager_br_id._setValue(data.organno._getValue());
		CusGrpInfoApply.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	
	function setconId(data){
		CusGrpInfoApply.manager_id_displayname._setValue(data.actorname._getValue());
		CusGrpInfoApply.manager_id._setValue(data.actorno._getValue());
		CusGrpInfoApply.manager_br_id._setValue(data.orgid._getValue());
		CusGrpInfoApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusGrpInfoApply.manager_br_id_displayname._obj._renderReadonly(true);
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
				var serno = jsonstr.serno;
				if(flag=='success'){
		            alert('新增成功!');
		            if(url == 'doReturn'){
		            	doReturn();
		            }else{
		            	var url = '<emp:url action="getCusGrpInfoApplyUpdatePage.do"/>?serno='+serno;
			    		url = EMPTools.encodeURI(url);
			    		window.location=url;
		            }		            
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = CusGrpInfoApply._checkAll();
		if(result){
			CusGrpInfoApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:form id="submitForm" action="addCusGrpInfoApplyRecord.do" method="POST">
		<emp:gridLayout id="CusGrpInfoApplyGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfoApply.serno" label="申请流水号" hidden="true" maxlength="40"  readonly="true" />
			<emp:text id="CusGrpInfoApply.grp_no" label="关联(集团)编号" maxlength="30" required="false"  hidden="true" />
			<emp:text id="CusGrpInfoApply.grp_name" label="关联(集团)名称" maxlength="60" required="true" onchange="checkCusid(this)"  />
			<emp:pop id="CusGrpInfoApply.parent_cus_id" label="主申请关联(集团)客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL100','BL200')and cus_status='20'&returnMethod=onReturn" required="true" />
			<emp:text id="CusGrpInfoApply.parent_cus_name" label="主申请关联(集团)名称" maxlength="60" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusGrpInfoApply.parent_org_code" label="主申请关联(集团)统一社会信用代码" maxlength="32" required="true" readonly="true"/>
			<emp:text id="CusGrpInfoApply.parent_loan_card" label="主申请关联(集团)贷款卡编码" maxlength="16" required="true" readonly="true"/>
			<emp:select id="CusGrpInfoApply.grp_finance_type" label="关联(集团)融资形式"  required="true" defvalue="02" readonly="true" dictname="STD_ZB_GRP_FIN_TYPE"  />
			<emp:select id="CusGrpInfoApply.grp_cus_type" label="集团客户类型"  required="true" dictname="STD_ZB_GRP_CUS_TYPE"  />
			<emp:textarea id="CusGrpInfoApply.grp_detail" label="关联(集团)情况说明" maxlength="250" required="true"  colSpan="2"/>
			<emp:text id="CusGrpInfoApply.is_change" label="是否变更标志" maxlength="20" hidden="true" required="false" defvalue="2" />
		</emp:gridLayout>
		<emp:gridLayout id="CusGrpInfoApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusGrpInfoApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusGrpInfoApply.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusGrpInfoApply.input_id_displayname" label="登记人" readonly="true" required="true"  defvalue="$currentUserName" />
			<emp:text id="CusGrpInfoApply.input_br_id_displayname" label="登记机构"  readonly="true" required="true"  defvalue="$organName" />
			<emp:text id="CusGrpInfoApply.input_date" label="登记日期" required="true" readonly="true" colSpan="2" defvalue="$OPENDAY" />
			<emp:text id="CusGrpInfoApply.input_id" label="登记人" maxlength="20" readonly="true" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusGrpInfoApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  defvalue="$organNo" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_id" label="责任人" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusGrpInfoApply.manager_br_id" label="管理机构"  required="true" readonly="false" hidden="true"/>
			<emp:text id="CusGrpInfoApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="submits" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
