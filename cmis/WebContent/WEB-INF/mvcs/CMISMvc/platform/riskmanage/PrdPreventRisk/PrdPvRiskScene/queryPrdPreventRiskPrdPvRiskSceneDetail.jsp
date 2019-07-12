<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表添加记录页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	//------------选择流程节点触发的操作--------------
	function changeScence(data){
		var prevent_id = PrdPvRiskScene.prevent_id._getValue();
		var flowId = PrdPvRiskScene.wfid._getValue();
		var scene_id = data;
		var url = '<emp:url action="getPrdPvRiskSceneChangeQueryPage.do"/>?prevent_id='+prevent_id+'&wfid='+flowId+'&scene_id='+scene_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doClose(){
		window.close();
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:tabGroup id="PrdPreventRisk" mainTab="base_tab">
		<emp:form id="submitForm" action="addPrdPreventRiskPrdPvRiskSceneRecord.do" method="POST">
			<emp:gridLayout id="PrdPvRiskSceneGroup" title="风险拦截场景" maxColumn="2"  >
			<emp:text id="PrdPvRiskScene.prevent_id" label="场景编号" maxlength="32" readonly="false" required="true" colSpan="2" />
			<emp:select id="PrdPvRiskScene.wfid" label="流程标识" dictname="FLOW_TYPE" required="true" readonly="true" />
		</emp:gridLayout>
			<div  class='emp_gridlayout_title'>节点拦截方案设置</div>
			<div>
				<table class="emp_table" align="center">
					<tr class="emp_table_title">
						<td width="16%">流程节点</td>
						<td>拦截方案选项</td>  
					</tr> 
					<tr valign="top"> 
						<td >
						
							<emp:radio id="PrdPvRiskScene.scene_id" label="流程节点" required="true" onclick="changeScence(this.value);" dictname="FLOW_SCENE_ID" colSpan="2" />
						</td>  
						<td >  
							<emp:table icollName="PrdPvRiskSceneList" url="" pageMode="false"  >
								<emp:text id="item_id" label="项目编号" />
								<emp:text id="item_name" label="项目名称" />
								<emp:select id="used_ind" label="是否启用" dictname="STD_ZX_YES_NO" />
								<emp:select id="risk_level" label="拦截类型" dictname="STD_ZB_RISK_LEVEL" defvalue="1" flat="true"/>
								<emp:checkbox id="is_check" label="选中状态" dictname="STD_ZB_CHECK_STATE" flat="true"/>
							</emp:table>
			             </td>
					</tr>
				</table>
				</div>
			
			<div align="center" style="position:absolute;left:500px; z-index:16;margin:30px auto;">
				<br>  
				<emp:button id="close" label="关闭"/>
			</div>
		</emp:form>
	</emp:tabGroup>
	
</body>
</html>
</emp:page>

