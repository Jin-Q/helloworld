<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表添加记录页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function changeScence(){
		var prevent_id = PrdPvRiskScene.prevent_id._getValue();
		var flowId = PrdPvRiskScene.wfid._getValue();
		var url = '<emp:url action="getPrdPreventRiskPrdPvRiskSceneAddPage.do"/>?prevent_id='+prevent_id+'&flow_id='+flowId;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	//-----------异步保存------------
	function doSave(){
		if(!PrdPvRiskScene._checkAll()){
			return;
		}
		/** 选取配置的场景ID、流程、流程节点 */
		var prevent_id = PrdPvRiskScene.prevent_id._getValue();
		var wfid = PrdPvRiskScene.wfid._getValue();
		var scene_id = PrdPvRiskScene.scene_id._getValue();
		PrdPvRiskSceneList._obj.selectAll();//设置状态为全选状态
		/** 组装选中记录 */
		var sceneListValue = PrdPvRiskSceneList._obj.getSelectedData();
		var paramStr1 = "";
		var paramStr2 = "";
		var hasCheck = false;
		for(var i=0;i<sceneListValue.length;i++){
			var sceneValue = PrdPvRiskSceneList[i];
			if(sceneValue.is_check._getValue() == 1){//选中状态
				paramStr1 += sceneValue.item_id._getValue()+",";
				paramStr2 += sceneValue.risk_level._getValue()+",";
				hasCheck = true;
			}
		}
		if(!hasCheck){
			alert("请勾选需要配置的拦截项！");
			return;
		}
		var param = "prevent_id="+prevent_id+"&wfid="+wfid+"&scene_id="+scene_id+"&itemList="+paramStr1+"&levelList="+paramStr2;
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
					alert("保存成功！");
					window.opener.location.reload();
					window.close();
				}else {
					alert("异步发生异常！");
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
		var url='<emp:url action="addPrdPreventRiskPrdPvRiskSceneRecord.do"/>?'+param;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup id="PrdPreventRisk" mainTab="base_tab">
		<emp:form id="submitForm" action="addPrdPreventRiskPrdPvRiskSceneRecord.do" method="POST">
			<emp:gridLayout id="PrdPvRiskSceneGroup" title="风险拦截场景" maxColumn="2"  >
			<emp:text id="PrdPvRiskScene.prevent_id" label="场景编号" maxlength="32" readonly="false" required="true" colSpan="2" />
			<emp:select id="PrdPvRiskScene.wfid" label="流程标识" dictname="FLOW_TYPE" required="true" onchange="changeScence();" />
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
							<emp:checkbox id="PrdPvRiskScene.scene_id" label="流程节点" required="true" dictname="FLOW_SCENE_ID" colSpan="2" />
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
				<emp:button id="save" label="确定" />
				<emp:button id="reset" label="重置"/>
			</div>
		</emp:form>
	</emp:tabGroup>
	
</body>
</html>
</emp:page>
