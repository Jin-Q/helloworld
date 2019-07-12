<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspDunningRecord._toForm(form);
		PspDunningRecordList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspDunningRecordPage() {
		var paramStr = PspDunningRecordList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspDunningRecordUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'window','height='+window.screen.availHeight*0.9+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看
	function doViewPspDunningRecord() {
		var paramStr = PspDunningRecordList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspDunningRecordViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'window','height='+window.screen.availHeight*0.9+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};

	//新增
	function doGetAddPspDunningRecordPage() {
	//	var task_serno = '${context.serno}';
		var task_serno = '${context.bill_no}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspDunningRecordAddPage.do"/>?task_serno='+task_serno+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.open(url,'window','height='+window.screen.availHeight*0.9+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};

	//删除
	function doDeletePspDunningRecord() {
		var paramStr = PspDunningRecordList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspDunningRecordRecord.do"/>?'+paramStr;
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
							alert("删除成功！");
							window.location.reload();
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspDunningRecordGroup.reset();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspDunningRecordGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="PspDunningRecord.serno" label="催收函编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddPspDunningRecordPage" label="新增" op="add"/>
		<emp:actButton id="getUpdatePspDunningRecordPage" label="修改" op="update"/>
		<emp:actButton id="deletePspDunningRecord" label="删除" op="remove"/>
		<emp:actButton id="viewPspDunningRecord" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspDunningRecordList" pageMode="true" url="pagePspDunningRecordQuery.do?bill_no=${context.bill_no}">
		<emp:text id="serno" label="催收函编号" />
		<emp:text id="dunning_date" label="催收日期" />
		<emp:text id="dunning_obj_type" label="催收对象类型" dictname="STD_ZB_DUNNING_OBJ_TYPE"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="dunning_type" label="催收方式" dictname="STD_ZB_DUNNING_TYPE" />
		<emp:text id="dunning_id" label="催收人" />
		<emp:text id="dunning_amt" label="催收金额" dataType="Currency"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    