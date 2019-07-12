<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String usedFlow = "";//是否适用流程，1适用
	KeyedCollection kc = (KeyedCollection)context.getDataElement("PrdPreventRisk");
	if(kc.containsKey("used_flow")){
		usedFlow = (String)kc.getDataValue("used_flow");
	}
	if(usedFlow == null){
		usedFlow = "";
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function refreshPrdPvRiskScene() {
		PrdPreventRisk_tabs.tabs.PrdPvRiskScene_tab.refresh();
	};
	/*--user code begin--*/
	//-------------引入拦截项操作--------------
	function doConnPrevent(){
		var preventId = PrdPreventRisk.prevent_id._getValue();
		var url = '<emp:url action="queryPrdPvItemListPop.do"/>?preventId='+preventId;
		url = EMPTools.encodeURI(url);
		var popparam = 'height=500, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',popparam);
	};	
	//-------------移除拦截项操作--------------
	function doDelPrevent(){
		var preventId = PrdPreventRisk.prevent_id._getValue();
		var data = PrdPvRiskItemRelList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择删除拦截方案！");
			return false;
		}else {
			if(confirm("是否确认要删除？")){
				var relArr = "";
				//组装多记录选择返回参数
				for(var i=0;i<data.length;i++){
					relArr += data[i].item_id._getValue()+",";
				}
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
							window.location.reload();
						}else {
							alert("删除失败!");
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

				var url="<emp:url action='delPrdPreventItemRel.do'/>?prevent_id="+preventId+"&relArr="+relArr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		}
	};			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup id="PrdPreventRisk_tabs" mainTab="base_tab">
		<emp:tab label="风险拦截方案" id="base_tab">
			<emp:form id="submitForm" action="updatePrdPreventRiskRecord.do" method="POST">
				<emp:gridLayout id="PrdPreventRiskGroup" title="风险拦截方案" maxColumn="2">
					<emp:text id="PrdPreventRisk.prevent_id" label="方案编号" maxlength="32" required="true" readonly="true" />
					<emp:text id="PrdPreventRisk.prevent_desc" label="方案名称" maxlength="100" required="true" />
					<emp:select id="PrdPreventRisk.used_ind" label="是否使用" required="true" dictname="STD_ZX_YES_NO" />
					<emp:textarea id="PrdPreventRisk.memo" label="备注" maxlength="200" required="true" colSpan="2" />
					<emp:text id="PrdPreventRisk.input_id" label="登记人" maxlength="20" hidden="true" />
					<emp:text id="PrdPreventRisk.input_br_id" label="登记机构" maxlength="20" hidden="true" />
					<emp:date id="PrdPreventRisk.input_date" label="登记日期" hidden="true" />
				</emp:gridLayout>
				<div  class='emp_gridlayout_title'>风险拦截关联配置</div>
				<emp:table icollName="PrdPvRiskItemRelList" pageMode="true" selectType="2" url="queryPrdPvRiskItemRelList.do?prevent_id=${context.prevent_id}">
					<emp:text id="item_id" label="项目编号" />
					<emp:text id="item_name" label="项目名称" />
					<emp:text id="used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
				</emp:table>
			</emp:form>
		</emp:tab>
		<% 
			if(usedFlow.equals("1")){
		%>
		<emp:tab id="PrdPvRiskScene_tab" label="风险拦截场景（适用流程）" url="queryPrdPreventRiskPrdPvRiskSceneList.do?act=view" reqParams="PrdPreventRisk.prevent_id=$PrdPreventRisk.prevent_id;" needFlush="true" initial="false" />
		<% 
			}else {
		%>
		<emp:tab id="PrdPvRiskScene_tab" label="风险拦截场景(无流程)" url="queryPrdPvRiskSceneList.do?act=view" reqParams="prevent_id=$PrdPreventRisk.prevent_id;" needFlush="true" initial="false" />
		<% 
			}
		%>
	</emp:tabGroup>
</body>
</html>
</emp:page>

