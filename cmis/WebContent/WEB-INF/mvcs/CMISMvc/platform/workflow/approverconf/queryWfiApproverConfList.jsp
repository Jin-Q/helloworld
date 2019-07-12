<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiApproverConf._toForm(form);
		WfiApproverConfList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiApproverConfPage() {
		var paramStr = WfiApproverConfList._obj.getParamStr(['confid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiApproverConfUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiApproverConf() {
		var paramStr = WfiApproverConfList._obj.getParamStr(['confid']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiApproverConfViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiApproverConfPage() {
		var url = '<emp:url action="getWfiApproverConfAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiApproverConf() {
		var paramStr = WfiApproverConfList._obj.getParamStr(['confid']);
		if (paramStr != null) {
			if(confirm("确定要删除此记录？")){
				var url = '<emp:url action="deleteWfiApproverConfRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					try {
						var jsonstr = eval("("+o.responseText+")");
						var flag = jsonstr.flag;
						if(flag==1) {
							alert('操作成功！');
							window.location.reload();
						} else {
							alert('操作失败！'+flag);
						}
					}catch(e) {
						alert(o.responseText);
						return;
					}
				};		
				var handleFailure = function(o){	
					alert(o.responseText);
				};		
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.WfiApproverConfGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="WfiApproverConfGroup" title="输入查询条件" maxColumn="2">
		<emp:select id="WfiApproverConf.conf_type" label="筛选类型" dictname="STD_ZB_WFI_CONF_TYPE"/>
		<emp:select id="WfiApproverConf.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddWfiApproverConfPage" label="新增" op=""/>
		<emp:button id="getUpdateWfiApproverConfPage" label="修改" op=""/>
		<emp:button id="deleteWfiApproverConf" label="删除" op=""/>
		<emp:button id="viewWfiApproverConf" label="查看" op=""/>
	</div>

	<emp:table icollName="WfiApproverConfList" pageMode="true" url="pageWfiApproverConfQuery.do">
		<emp:text id="confid" label="配置ID" hidden="true"/>
		<emp:text id="conf_type" label="筛选类型" dictname="STD_ZB_WFI_CONF_TYPE"/>
		<emp:text id="appl_type" label="流程申请类型" dictname="ZB_BIZ_CATE"/>
		<emp:text id="wfsign" label="流程标识" />
		<emp:text id="wfsign_displayname" label="流程名称" />
		<emp:text id="nodeid" label="节点ID" />
		<emp:text id="nodeid_displayname" label="节点名称" />
		<emp:text id="orgid" label="业务管理机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    