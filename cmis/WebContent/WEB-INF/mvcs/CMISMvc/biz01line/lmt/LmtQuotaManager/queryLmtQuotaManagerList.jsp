<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtQuotaManager._toForm(form);
		LmtQuotaManagerList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtQuotaManagerPage() {
		var paramStr = LmtQuotaManagerList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtQuotaManagerUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtQuotaManager() {
		var paramStr = LmtQuotaManagerList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtQuotaManagerViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtQuotaManagerPage() {
		var url = '<emp:url action="getLmtQuotaManagerAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtQuotaManager() {
		var paramStr = LmtQuotaManagerList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtQuotaManagerRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						var flagInfo=jsonstr.flagInfo;						
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();								
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
		page.dataGroups.LmtQuotaManagerGroup.reset();
	};
	/*--user code begin--*/
	//设置机构代码和名称
	function getOrgID(data){
		LmtQuotaManager.code_id._setValue(data.organno._getValue());
	};
	//设置客户经理的值
	function setconId(data){
		LmtQuotaManager.code_id._setValue(data.actorno._getValue());
	};
	function onReturn(date){
		LmtQuotaManager.prd_id._obj.element.value=date.id;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="LmtQuotaManagerGroup" title="输入查询条件" maxColumn="2">
			<%if("OrgLmtQuota".equals(menuId)){%>
			<emp:pop id="LmtQuotaManager.code_id" label="机构代码" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  />
			<%} else{%>
			<emp:pop id="LmtQuotaManager.code_id" label="客户经理编号" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<%} %>
			<emp:pop id="LmtQuotaManager.prd_id" label="产品代码" url="showDicTree.do?dicTreeTypeId=STD_ZB_BIZ_SIG_LINE" returnMethod="onReturn"/>
	
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtQuotaManagerPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtQuotaManagerPage" label="修改" op="update"/>
		<emp:button id="deleteLmtQuotaManager" label="删除" op="remove"/>
		<emp:button id="viewLmtQuotaManager" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtQuotaManagerList" pageMode="true" url="pageLmtQuotaManagerQuery.do">
		<emp:text id="serno" label="流水号" />
		<%if("OrgLmtQuota".equals(menuId)){%>
		<emp:text id="code_id" label="机构码" />
		<emp:text id="code_id_displayname" label="机构名称" />
		<%} else{%>
		<emp:text id="code_id" label="客户经理编号" />
		<emp:text id="code_id_displayname" label="客户经理名称" />
		<%} %>
		<emp:text id="single_amt_quota" label="授信限额" dataType="Currency" />
		<emp:text id="sig_amt_quota" label="单户授信限额" dataType="Currency" />		
		<emp:text id="sig_loan_quota" label="单笔贷款限额" dataType="Currency" />
		<emp:text id="sig_use_quota" label="单笔支用限额" dataType="Currency" />
		<emp:date id="start_date" label="起始日期" />
		<emp:date id="end_date" label="到期日期"  />
		<emp:text id="manager_id_displayname" label="经办人" />
		<emp:text id="manager_br_id_displayname" label="经办机构" />		
	</emp:table>
	
</body>
</html>
</emp:page>
    