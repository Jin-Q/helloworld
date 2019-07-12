<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryWfiBpRightList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
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
	
	<emp:gridLayout id="WfiBpRightGroup" title="审批授权" maxColumn="2">
		<emp:text id="WfiBpRight.pk1" label="主键" maxlength="36" readonly="true" hidden="true" />
		<emp:select id="WfiBpRight.approve_type" label="审批类型" required="true" dictname="STD_ZB_APPROVE_TYPE" onchange="chgApproveType()"/>
		<emp:pop id="WfiBpRight.approve_org_displayname" label="审批机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
		<emp:pop id="WfiBpRight.approve_duty_displayname" label="审批岗位" url="querySDutyList.do?restrictUsed=false" returnMethod="setDuty"/>
		<emp:pop id="WfiBpRight.approver_displayname" label="审批人员" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" colSpan="2"/>
		<emp:select id="WfiBpRight.cus_type" label="客户类型" required="false" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:select id="WfiBpRight.app_type" label="业务分类" required="true" dictname="STD_BPRIGHT_APP_TYPE" cssFakeInputClass="emp_field_select_select1"/>
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
		<input type="button" class="button100" id="btReturn" onclick="window.close()" value="返回到列表页面">
	</div>
	
	
<!--	<div align="center">-->
<!--		<br>-->
<!--		<emp:button id="return" label="返回到列表页面"/>-->
<!--	</div>-->
</body>
</html>
</emp:page>
