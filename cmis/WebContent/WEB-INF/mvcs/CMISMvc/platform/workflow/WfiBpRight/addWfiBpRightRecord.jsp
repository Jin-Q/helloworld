<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	//返回审批机构
	function getOrganName(data){
		WfiBpRight.approve_org._setValue(data.organno._getValue());
		WfiBpRight.approve_org_displayname._setValue(data.organname._getValue());
	}

	//返回审批人员
	function setconId(data){
		WfiBpRight.approver._setValue(data.actorno._getValue());
		WfiBpRight.approver_displayname._setValue(data.actorname._getValue());
	}

	//返回审批岗位
	function setDuty(data){
		WfiBpRight.approve_duty._setValue(data.dutyno._getValue());
		WfiBpRight.approve_duty_displayname._setValue(data.dutyname._getValue());
	}

	function chgApproveType(){
		WfiBpRight.approve_org_displayname._setValue('');
		WfiBpRight.approve_org._setValue('');
		WfiBpRight.approve_duty_displayname._setValue('');
		WfiBpRight.approve_duty._setValue('');
		WfiBpRight.approver_displayname._setValue('');
		WfiBpRight.approver._setValue('');
		
		WfiBpRight.approve_org_displayname._obj._renderRequired(false);
		WfiBpRight.approve_duty_displayname._obj._renderRequired(false);
		WfiBpRight.approver_displayname._obj._renderRequired(false);
		WfiBpRight.approve_org_displayname._obj._renderHidden(false);
		WfiBpRight.approve_duty_displayname._obj._renderHidden(false);
		WfiBpRight.approver_displayname._obj._renderHidden(false);
		var approve_type = WfiBpRight.approve_type._getValue();
		if(approve_type=='1'){
			WfiBpRight.approve_org_displayname._obj._renderRequired(true);
			WfiBpRight.approve_duty_displayname._obj._renderHidden(true);
			WfiBpRight.approver_displayname._obj._renderHidden(true);
		}else if(approve_type=='2'){
			WfiBpRight.approve_duty_displayname._obj._renderRequired(true);
			WfiBpRight.approve_org_displayname._obj._renderHidden(true);
			WfiBpRight.approver_displayname._obj._renderHidden(true);
		}else if(approve_type=='3'){
			WfiBpRight.approver_displayname._obj._renderRequired(true);
			WfiBpRight.approve_duty_displayname._obj._renderHidden(true);
			WfiBpRight.approve_org_displayname._obj._renderHidden(true);
		}else if(approve_type=='4'){
			WfiBpRight.approve_org_displayname._obj._renderRequired(true);
			WfiBpRight.approve_duty_displayname._obj._renderRequired(true);
			WfiBpRight.approver_displayname._obj._renderHidden(true);
		}
	}

	function doSaveBpRight(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.flag;
				if(operMsg=='success'){
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
		var result = WfiBpRight._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}

	function doReturn(){
		var url = '<emp:url action="queryWfiBpRightList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	function doLoad(){
		var options = WfiBpRight.cus_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
	   	    if(options[i].value=='BL_ALL'){
	  			options.remove(i);
	   	    }
	    }
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addWfiBpRightRecord.do" method="POST">
		
		<emp:gridLayout id="WfiBpRightGroup" title="审批授权" maxColumn="2">
			<emp:text id="WfiBpRight.pk1" label="主键" maxlength="36" readonly="true" hidden="true" />
			<emp:select id="WfiBpRight.approve_type" label="审批类型" required="true" dictname="STD_ZB_APPROVE_TYPE" onchange="chgApproveType()"/>
			<emp:pop id="WfiBpRight.approve_org_displayname" label="审批机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" hidden="true"/>
			<emp:pop id="WfiBpRight.approve_duty_displayname" label="审批岗位" url="querySDutyList.do?restrictUsed=false" returnMethod="setDuty" hidden="true"/>
			<emp:pop id="WfiBpRight.approver_displayname" label="审批人员" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" colSpan="2" hidden="true"/>
			<emp:select id="WfiBpRight.cus_type" label="客户类型" required="false" dictname="STD_ZB_BUSILINE" hidden="true"/>
			<emp:select id="WfiBpRight.app_type" label="业务分类" required="true" dictname="STD_BPRIGHT_APP_TYPE" cssElementClass="emp_field_select_select1"/>
			<emp:select id="WfiBpRight.biz_type" label="业务标志" required="true" dictname="STD_ZB_BP_BIZ"/>
			<emp:text id="WfiBpRight.sig_amt" label="单笔金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="WfiBpRight.open_amt" label="敞口金额" maxlength="16" required="false" dataType="Currency" />
			<emp:text id="WfiBpRight.single_amt" label="单户金额" maxlength="16" required="true" dataType="Currency" />
			
			<emp:text id="WfiBpRight.approve_org" label="审批机构" maxlength="20" hidden="true" />
			<emp:text id="WfiBpRight.approve_duty" label="审批岗位" maxlength="100" hidden="true" />
			<emp:text id="WfiBpRight.approver" label="审批人员" maxlength="20" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="saveBpRight" label="确定" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

