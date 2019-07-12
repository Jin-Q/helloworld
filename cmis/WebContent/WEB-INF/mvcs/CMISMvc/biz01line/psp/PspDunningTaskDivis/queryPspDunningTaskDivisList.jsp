<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspDunningTaskDivis._toForm(form);
		PspDunningTaskDivisList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspDunningTaskDivisPage() {
		var paramStr = PspDunningTaskDivisList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="checkPspDunningTaskIsRegi.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("修改失败！");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="exists"){
						alert('该任务已登记，不能修改！');
					}else if(flag=="notexists"){
						var url = '<emp:url action="getPspDunningTaskDivisUpdatePage.do"/>?'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert("修改失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("修改失败，请联系管理员！");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspDunningTaskDivis() {
		var paramStr = PspDunningTaskDivisList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspDunningTaskDivisViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspDunningTaskDivisPage() {
		var url = '<emp:url action="getPspDunningTaskDivisLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspDunningTaskDivis() {
		var paramStr = PspDunningTaskDivisList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				deletePspDun(paramStr);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspDunningTaskDivisGroup.reset();
	};
	
	/*--user code begin--*/
	//删除记录
	function deletePspDun(paramStr){
		var url = '<emp:url action="deletePspDunningTaskDivisRecord.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){ 
			EMPTools.unmask();
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
				}else if(flag=="exists"){
					alert('该任务已登记，不能删除！');
				}else{
					alert("删除失败！");
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("删除失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspDunningTaskDivisGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspDunningTaskDivis.serno" label="业务编号" />
			<emp:text id="PspDunningTaskDivis.cus_id" label="客户码" />
			<emp:text id="PspDunningTaskDivis.acc_no" label="借据编号" />
			<emp:text id="PspDunningTaskDivis.cont_no" label="合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspDunningTaskDivisPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspDunningTaskDivisPage" label="修改" op="update"/>
		<emp:button id="deletePspDunningTaskDivis" label="删除" op="remove"/>
		<emp:button id="viewPspDunningTaskDivis" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspDunningTaskDivisList" pageMode="true" url="pagePspDunningTaskDivisQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="acc_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="task_create_date" label="任务生成日期" />
		<emp:text id="need_end_date" label="要求完成日期" />
		<emp:text id="exe_id" label="任务执行人" hidden="true"/>
		<emp:text id="divis_id" label="任务分配人" hidden="true" />
		<emp:text id="exe_id_displayname" label="任务执行人" />
		<emp:text id="divis_id_displayname" label="任务分配人" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    