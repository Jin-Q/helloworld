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
		STeamInfo._toForm(form);
		STeamInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSTeamInfoPage() {
		var paramStr = STeamInfoList._obj.getParamStr(['team_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getSTeamInfoUpdatePage.do"/>?'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			window.location=url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSTeamInfo() {
		var paramStr = STeamInfoList._obj.getParamStr(['team_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getSTeamInfoViewPage.do"/>?'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			window.location=url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSTeamInfoPage() {
		var url = '<emp:url action="getSTeamInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doDeleteSTeamInfo() {
		var paramStr = STeamInfoList._obj.getParamStr(['team_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var msg = jsonstr.msg;
						if("success" == flag){
							alert(msg);
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteSTeamInfoRecord.do"/>?'+encodeURI(paramStr);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.STeamInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="STeamInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="STeamInfo.team_no" label="团队编号" />
			<emp:text id="STeamInfo.team_name" label="团队名称" />
			<emp:select id="STeamInfo.belg_line" label="归属条线" dictname="STD_ZB_BUSILINE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSTeamInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateSTeamInfoPage" label="修改" op="update"/>
		<emp:button id="deleteSTeamInfo" label="删除" op="remove"/>
		<emp:button id="viewSTeamInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="STeamInfoList" pageMode="true" url="pageSTeamInfoQuery.do">
		<emp:text id="team_no" label="团队编号" />
		<emp:text id="team_name" label="团队名称" />
		<emp:text id="team_type" label="团队类型" />
		<emp:text id="belg_line" label="归属条线" dictname="STD_ZB_BUSILINE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    