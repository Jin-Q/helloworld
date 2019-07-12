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
		IqpAssetIssueInfo._toForm(form);
		IqpAssetIssueInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetIssueInfoPage() {
		var paramStr = IqpAssetIssueInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetIssueInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetIssueInfo() {
		var paramStr = IqpAssetIssueInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetIssueInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetIssueInfoPage() {
		var url = '<emp:url action="getIqpAssetIssueInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetIssueInfo() {
		var paramStr = IqpAssetIssueInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryIqpAssetIssueInfoList.do"/>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除异常!");
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteIqpAssetIssueInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetIssueInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpAssetIssueInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetIssueInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetIssueInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetIssueInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetIssueInfoList" pageMode="true" url="pageIqpAssetIssueInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="act_issue_type" label="业务类型" />
		<emp:text id="act_issue_date" label="实际发行日期" />
		<emp:text id="act_issue_amt" label="实际发行总量（万元）" />
		<emp:text id="base_date" label="基准日（起息日）" />
		<emp:text id="end_date" label="法定到期日期" />
		<emp:text id="fee_cal_mode" label="服务费计算方式" />
		<emp:text id="fee_rate" label="服务费率" />
		<emp:text id="fee_min" label="服务费下限（元）" />
		<emp:text id="cont_no" label="合同编号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    