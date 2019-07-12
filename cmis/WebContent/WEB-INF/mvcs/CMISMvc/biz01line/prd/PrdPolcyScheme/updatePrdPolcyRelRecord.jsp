<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.ext.tag.page.EMPExtPageObjects"%>
<%@page import="com.yucheng.cmis.pub.PUBConstant"%>

<% 
//request = (HttpServletRequest) pageContext.getRequest();
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);

//----1.页面加载时，列出当前操作选择的流程信息----
String flowValue = "";
if(context.containsKey("flowValue")){
	flowValue = (String)context.getDataValue("flowValue");
}

//----2.页面加载时，列出当前操作选择的流程节点信息----
String flowNode = "";
if(context.containsKey("flownode")){
	flowNode = (String)context.getDataValue("flownode");
}
//----3.页面加载时，列出当前操作政策资料审核信息----

//----4.获取修改操作时的政策ID----
String schemeId = "";
if(context.containsKey("schemeId")){
	schemeId = (String)context.getDataValue("schemeId");
}
%>
<emp:page>
	<html>
	<head>
	<title>列表查询页面</title>
	<jsp:include page="/include.jsp" flush="true" />

	<script type="text/javascript">
function confirmSubmit(){
	var flowId = flow._getValue();
	
	//var flownodeValue = "";
	//flownodeValue = document.getElementById("flownode").value;
	var flownodeValue = "<%=flowNode%>";
	if(flownodeValue == "" || flownodeValue == "null" || flownodeValue == null){
		alert("请先选择流程节点");
		return false;
	}
	
	PrdCatalogList._obj.selectAll();
	var parCatalogListValue = PrdCatalogList._obj.getSelectedData();
	var listLength = parCatalogListValue.length;
	for(var i=0;i<listLength;i++){
		var paramStrOne = parCatalogListValue[i];
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
		var url="<emp:url action='updatePlocyRelFirst.do'/>?"+paramStrSelectOne+"&flowValue="+flowId+"&flowNodeValue="+flownodeValue;
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
			if(flag == "success"){
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
	var url = "<emp:url action='updatePrdPolcyRelRecord.do'/>?schemeid=<%=schemeId %>&flowid="+flowId+"&flownode="+flownodeValue;
	url = EMPTools.encodeURI(url);
	window.location = url;
	window.location.reload();
};

//---异步通过流程节点加载挂接政策资料信息---
function flowNodeChange(data){
	var flowId = flow._getValue();
	var flownodeValue = "";
	flownodeValue = data;
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
	var url = "<emp:url action='updatePrdPolcyRelRecord.do'/>?schemeid=<%=schemeId %>&flowId="+flowId+"&flownode="+flownodeValue;
	url = EMPTools.encodeURI(url);
	window.location = url;
}

</script>
	</head>
	<body class="page_content">
	<emp:form id="submitForm" action="" method="POST">
		<emp:tabGroup mainTab="base_tab" id="mainTab">
			<div class='emp_gridlayout_title'>流程选项</div>
			<br />
			<div >
			<table class="emp_table">
				<tr class="emp_table_title">
					<td width="15%">已选流程</td>
					<td width="15%">流程节点</td>
					<td>政策选项</td>
				</tr>  
				<tr valign="top">
					<td align="center"><br />
					&nbsp;&nbsp;<emp:select id="flow" label="选择流程" defvalue="<%=flowValue %>" dictname="FLOW_TYPE" readonly="true" />
					</td>  

					<td>
					<% 
						//页面手动封装流程节点信息
						String flowNodeType = flowValue+"_FLOWNODE_TYPE";
						IndexedCollection ic = (IndexedCollection)((KeyedCollection)context.getDataElement("dictColl")).getDataElement(flowNodeType);
						out.print("<span id='emp_field_flownode' title='流程节点' type='Radio' class='emp_field' cssErrorClass='emp_field_error' cssRequiredClass='emp_field_required' required='false' colSpan='2' readonly='false' onlyControlElement='false' dictname='"+flowNodeType+"' value='"+flowNode+"'  rendered='false'><table>");
						for(int i=0;i<ic.size();i++){
							KeyedCollection kc = (KeyedCollection)ic.get(i);
							String enname = (String)kc.getDataValue("enname");
							String cnname = (String)kc.getDataValue("cnname");
							out.print("<tr><td><input name='flownode' type='radio' value='"+enname+"' ");
							if(enname.equals(flowNode)){
								out.print(" checked='true' ");
							}
							out.print(" class='emp_field_radio_input' onclick='flowNodeChange(this.value);'/>"+cnname+"</td></tr>");
						}
						out.print("</table></span>");
					%>
					</td>

					<td><emp:table icollName="PrdCatalogList" selectType="2"
						pageMode="false" url="">
						<emp:text id="schemecode" label="政策资料编号" required="false" />
						<emp:text id="schemedesc" label="政策资料" required="false" />
						<emp:select id="schemetype" label="审核状态"
							dictname="STD_ZB_RISK_LEVEL" flat="false" required="false" />
						<emp:checkbox id="ifSelect" label="选中状态" flat="true"
							dictname="STD_ZB_CHECK_STATE" />
					</emp:table></td>
			</table>
			</div>

			<br />
			<div align="center"
				style="display:${param.optype};position:absolute;left:500px;z-index:16; margin:30px auto;"><br>
			<button onclick="confirmSubmit()">保存</button>
			</div>
		</emp:tabGroup>
	</emp:form>
	</body>
	</html>
</emp:page>