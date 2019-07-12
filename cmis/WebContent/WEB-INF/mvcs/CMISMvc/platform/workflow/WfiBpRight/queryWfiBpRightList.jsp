<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #CEC7BD;
	border-style: solid;
	text-align: left;
}
</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiBpRight._toForm(form);
		WfiBpRightList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiBpRightPage() {
		var paramStr = WfiBpRightList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiBpRightUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
//			window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiBpRight() {
		var paramStr = WfiBpRightList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiBpRightViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
//			window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiBpRightPage() {
		var url = '<emp:url action="getWfiBpRightAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiBpRight() {
		var paramStr = WfiBpRightList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiBpRightRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败！");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();
						}else{
							alert('删除失败！');
						}
					}	
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiBpRightGroup.reset();
	};
	
	/*--user code begin--*/
	//返回审批机构
	function getOrganName(data){
		WfiBpRight.approve_org._setValue(data.organno._getValue());
	//	WfiBpRight.approve_org_displayname._setValue(data.organname._getValue());
	}
	//返回审批岗位
	function setDuty(data){
		WfiBpRight.approve_duty._setValue(data.dutyno._getValue());
		WfiBpRight.approve_duty_displayname._setValue(data.dutyname._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiBpRightGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="WfiBpRight.approve_org" label="审批机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
		<emp:select id="WfiBpRight.app_type" label="业务分类" dictname="STD_BPRIGHT_APP_TYPE" cssElementClass="emp_field_select_select1"/>
        <emp:select id="WfiBpRight.approve_type" label="审批类型" dictname="STD_ZB_APPROVE_TYPE"/>
        <emp:pop id="WfiBpRight.approve_duty_displayname" label="审批岗位" url="querySDutyList.do?restrictUsed=false" returnMethod="setDuty"/>
        <emp:text id="WfiBpRight.approve_duty" label="审批岗位" maxlength="100" hidden="true" />	
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfiBpRightPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiBpRightPage" label="修改" op="update"/>
		<emp:button id="deleteWfiBpRight" label="删除" op="remove"/>
		<emp:button id="viewWfiBpRight" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiBpRightList" pageMode="true" url="pageWfiBpRightQuery.do">
		<emp:text id="approve_type" label="审批类型" dictname="STD_ZB_APPROVE_TYPE"/>
		<emp:text id="approve_org_displayname" label="审批机构" />
		<emp:text id="approve_duty_displayname" label="审批岗位" />
		<emp:text id="approver_displayname" label="审批人员" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:text id="app_type" label="业务分类" dictname="STD_BPRIGHT_APP_TYPE"/>
		<emp:text id="biz_type" label="业务标志" dictname="STD_ZB_BP_BIZ"/>
		<emp:text id="sig_amt" label="单笔金额" dataType="Currency" hidden="true"/>
		<emp:text id="open_amt" label="敞口金额" dataType="Currency" hidden="true"/>
		<emp:text id="single_amt" label="单户金额" dataType="Currency"/>
		<emp:text id="pk1" label="主键" hidden="true"/>
		<emp:text id="approve_org" label="审批机构" hidden="true"/>
		<emp:text id="approve_duty" label="审批岗位" hidden="true"/>
		<emp:text id="approver" label="审批人员" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    