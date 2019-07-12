<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.ext.tag.page.EMPExtPageObjects"%>

<% 
//request = (HttpServletRequest) pageContext.getRequest();
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);

//----1.页面加载时，列出当前操作选择的流程信息----
String flowValue = "";
if(context.containsKey("flowValue")){
	flowValue = (String)context.getDataValue("flowValue");
}
//----2.页面加载时，列出当前操作的已选流程信息----
IndexedCollection selectFlowListBySchemeId = (IndexedCollection)context.getDataElement("SelectFlowListBySchemeId");
//----3.页面加载时，列出当前操作选择的流程节点信息----
String flowNode = "";
if(context.containsKey("flownode")){
	flowNode = (String)context.getDataValue("flownode");
}
String flowNodeType = "";
IndexedCollection ic = null;
int icLength = 0;
if(flowValue == "" || flowValue == null){
}else {
	flowNodeType = flowValue+"_FLOWNODE_TYPE";
	ic = (IndexedCollection)((KeyedCollection)context.getDataElement("dictColl")).getDataElement(flowNodeType);
	icLength = ic.size();
}
//----4.页面加载时，列出当前操作政策资料审核信息----

//----5.获取修改操作时的政策ID----

String schemeId = "";
if(context.containsKey("schemeId")){
	schemeId = (String)context.getDataValue("schemeId");
}
%>   
<style type="text/css">
 
</style> 


<emp:page>
	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />

	<script type="text/javascript">


function doOnLoad(){
}
//----选择流程触发的异步操作，刷新当前页面----
function doSelectFlowIdChange(){
	//流程选择项改变，触发该函数，异步调用生成流程节点，生成流程节点所有拦截策略以及拦截方案
	var flowValue = flow._getValue();
	//此处申明流程字典项、流程节点字典项生成规则：流程字典项【FLOW_TYPE】，流程节点字典项【流程值_FLOWNODE_TYPE】
	//var flowNodeValue = flowValue+'_FLOWNODE_TYPE';
	//flownode._obj.config.dictname = flowNodeValue;
	//var paramStr = "?flowValue="+flowValue+"&flowNodeValue="+flowNodeValue;
	/*异步请求刷新流程节点字典项*/
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var flowValue = jsonstr.flowValue;
			var flownode = jsonstr.flownode;
			
			if(flag == "success"){
				var url = '<emp:url action="getPrdPolcySchemeFirstAddPage.do"/>?flowValue='+flowValue+"&schemeId=<%=schemeId %>"+"&flownode="+flownode;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else {
				alert("异步获取流程字典项发生异常！");
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
	var url="<emp:url action='getFlowNodeDicByFlowDic.do'/>&flowValue="+flowValue+"&schemeId=<%=schemeId %>";
	var postData = YAHOO.util.Connect.setForm();	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
};

function confirmSubmit(){
	var flowValue = flow._getValue();
	var icSize = <%=icLength %>;
	if(flowValue == null || flowValue == ""){
		alert("请选择配置的流程");
		return false;
	}
	//---对流程节点进行遍历封装---
	var cc = "";
	var flowNodeValue = "";
	for(var a=0;a<icSize;a++){
		flowNodeValue = document.getElementById("flownode"+a).value;
		var isChecked = document.getElementById("flownode"+a).checked;
		if(isChecked){
			flowNodeValue = flowNodeValue+",";
			cc +=  flowNodeValue;
		}
	}
	if(cc.indexOf(',') == -1){
		flowNodeValue = "";
	}else {
		var arr = cc.split(",");
		var result = "";
		for(var d=0;d<arr.length-1;d++){
			if(d == (arr.length-2)){
				result += arr[d];
			}else {
				result += arr[d]+",";
			}
		}
		flowNodeValue = result;
	}
	if(flowNodeValue == null || flowNodeValue == ""){
		alert("请选择配置的流程节点");
		return false;
	}
	PrdCatalogList._obj.selectAll();
	//循环每一条记录单独发送
	var parCatalogListValue = PrdCatalogList._obj.getSelectedData();
	var listLength = parCatalogListValue.length;
	if(listLength == 0 || listLength == null){
		alert("请在政策方案配置中引入政策项");
		return;
	}
	for(var i=0;i<listLength;i++){
		var paramStrOne = parCatalogListValue[i];
		//alert(paramStrOne.ifSelect._getValue());
		var param1 = "schemecode="+paramStrOne.schemecode._getValue();
		var param2 = "&schemeId=<%=schemeId %>";
		var param4 = "&ifSelect="+paramStrOne.ifSelect._getValue();
		var param3 = "";
		if(paramStrOne.schemetype._getValue() == null || paramStrOne.schemetype._getValue() == ""){
			param3 = "&schemetype=";
		}else {
			param3 = "&schemetype="+paramStrOne.schemetype._getValue();
		}
		paramStrSelectOne = param1+param2+param4+param3;
		var url="<emp:url action='addPlocyRelFirst.do'/>?"+paramStrSelectOne+"&flowValue="+flowValue+"&flowNodeValue="+flowNodeValue;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
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
			var flowValue = jsonstr.flowValue;
			if(flag == "success"){
				//var url = '<emp:url action="getPrdPolcySchemeFirstAddPage.do"/>?flowValue='+flowValue;
				//url = EMPTools.encodeURI(url);
				//window.location = url;
			}else {
				alert("异步获取流程字典项发生异常！");
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
	

	
	var url = '<emp:url action="getSpaceApplyListBySchemeId.do"/>?schemeId=<%=schemeId %>';
	url = EMPTools.encodeURI(url);
	window.location = url;
	window.location.reload();
}

function selectFlowChange(data){
	var flowValue = data;
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var flowValue = jsonstr.flowValue;
			var flownode = jsonstr.flownode;
			
			if(flag == "success"){
				var url = '<emp:url action="getPrdPolcySchemeFirstAddPage.do"/>?flowValue='+flowValue+"&schemeId=<%=schemeId %>"+"&flownode="+flownode;
				url = EMPTools.encodeURI(url);
				window.location = url;
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
	var url="<emp:url action='getFlowNodeDicByFlowDic.do'/>&flowValue="+flowValue+"&schemeId=<%=schemeId %>";
	var postData = YAHOO.util.Connect.setForm();	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
}
</script>
	</head>
	<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="" method="POST">
		<emp:tabGroup mainTab="base_tab" id="mainTab">
			<emp:gridLayout id="liucheng" maxColumn="2" title="选择流程">
				<div style="position: absolute; left: 20px; top: 35px; z-index: 16">
				<table>
					<tr>
						<td><emp:select id="flow" label="选择流程"
							defvalue="<%=flowValue %>" dictname="FLOW_TYPE" required="true"
							onchange="doSelectFlowIdChange();" /></td>
					</tr>
				</table>
				</div>
			</emp:gridLayout>
			<div class='emp_gridlayout_title'>流程选项</div>
			<div>
			<table class="emp_table" >  
				<tr class="emp_table_title" align="center">
					<td width="15%" >已选流程</td>
					<td width="15%">流程节点</td> 
					<td>政策选项</td> 
				</tr>
				<tr>
					<td valign="top">
					<% 
						for(int i=0;i<selectFlowListBySchemeId.size();i++){
						KeyedCollection kc = (KeyedCollection)selectFlowListBySchemeId.get(i);
						String flowIdDone = (String)kc.getDataValue("flowid");
						String flowNameDone = (String)kc.getDataValue("flowname");
					%> <label
						style="font-size: 13px; color: #325D87; font-weight: bold;"
						id="<%=flowIdDone %>" onclick="selectFlowChange(this.id);">&nbsp;<%=flowNameDone %></label><br />
					<%  
						} 
					%>
					</td>
					<td valign="top">
					<% 
						//页面手动封装流程节点信息
						String flowNodeArr[] = flowNode.split(",");
						if(flowValue == "" || flowValue == null){
							
						}else {
							out.print("<span id='emp_field_flownode' title='流程节点' type='CheckBox' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='false' colSpan='2' readonly='false' onlyControlElement='false' dictname='"+flowNodeType+"' value='"+flowValue+"'  rendered='false'><table>");
							for(int i=0;i<ic.size();i++){
								KeyedCollection kc = (KeyedCollection)ic.get(i);
								String enname = (String)kc.getDataValue("enname");
								String cnname = (String)kc.getDataValue("cnname");
								out.print("<tr><td><input name='flownode"+i+"' type='checkbox' value='"+enname+"' ");
								for(int j=0;j<flowNodeArr.length;j++){
									if(enname.equals(flowNodeArr[j])){
										out.print(" checked='true' ");
									}
								}
								out.print(" class='emp_field_checkbox_input'/>"+cnname+"</td></tr>");
							}
							out.print("</table></span>");
						}
					%>
					</td>
					<td valign="top">
					  <emp:table icollName="PrdCatalogList" selectType="2" pageMode="false" url="">
						<emp:text id="schemecode" label="政策资料编号" required="false" />
						<emp:text id="schemedesc" label="政策资料" required="false" />
						<emp:select id="schemetype" label="审核状态" dictname="STD_ZB_RISK_LEVEL" flat="false" required="false" />
						<emp:checkbox id="ifSelect" label="选中状态" flat="true" dictname="STD_ZB_CHECK_STATE" />
					</emp:table></td>
				</tr>
			</table>
			</div>

			<br />
			<div align="center"
				style="display:${param.optype};position:absolute;left:500px;z-index:16;margin:30px auto;"> 
			<br>
			<button onclick="confirmSubmit()">保存</button>
			</div>
		</emp:tabGroup>
	</emp:form>
	</body>
	</html>
</emp:page>