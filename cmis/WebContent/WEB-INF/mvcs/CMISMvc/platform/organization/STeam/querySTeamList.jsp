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
		STeam._toForm(form);
		STeamList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSTeamPage() {
		var paramStr = STeamList._obj.getParamStr(['team_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getSTeamUpdatePage.do"/>?'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			window.location=url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSTeam() {
		var paramStr = STeamList._obj.getParamStr(['team_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getSTeamViewPage.do"/>?'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			window.location=url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSTeamPage() {
		var url = '<emp:url action="getSTeamAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doDeleteSTeam() {
		var paramStr = STeamList._obj.getParamStr(['team_no']);
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
				var url = '<emp:url action="deleteSTeamRecord.do"/>?'+encodeURI(paramStr);
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.STeamGroup.reset();
	};

	function getOrganName(data){
		STeam.team_org_id._setValue(data.organno._getValue());
		STeam.team_org_id_displayname._setValue(data.organname._getValue());
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="STeamGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="STeam.team_no" label="团队编号" />
			<emp:text id="STeam.team_name" label="团队名称" />
			<emp:select id="STeam.belg_line" label="归属条线" dictname="STD_ZB_BUSILINE" />
			<emp:pop id="STeam.team_org_id_displayname" label="归属机构"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"  />
			<emp:text id="STeam.team_org_id" label="归属机构" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSTeamPage" label="新增" op="add"/>
		<emp:button id="getUpdateSTeamPage" label="修改" op="update"/>
		<emp:button id="deleteSTeam" label="删除" op="remove"/>
		<emp:button id="viewSTeam" label="查看" op="view"/>
	</div>

	<emp:table icollName="STeamList" pageMode="true" url="pageSTeamQuery.do">
		<emp:text id="team_no" label="团队编号" />
		<emp:text id="team_name" label="团队名称" />
		<emp:text id="team_type" label="团队类型" />
		<emp:text id="belg_line" label="归属条线" dictname="STD_ZB_BUSILINE" />
		<emp:text id="team_org_id" label="归属机构" hidden="true"/>
		<emp:text id="team_org_id_displayname" label="归属机构" />
		<emp:select id="status" label="是否有效" dictname="STD_ZX_YES_NO"  />
	</emp:table>
	
</body>
</html>
</emp:page>
    