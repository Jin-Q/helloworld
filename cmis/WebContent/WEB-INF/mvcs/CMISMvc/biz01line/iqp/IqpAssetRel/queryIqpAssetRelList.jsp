<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String asset_no="";
	if(context.containsKey("asset_no")){
		asset_no =(String)context.getDataValue("asset_no");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doOnLoad(){
		IqpAssetRel.asset_no._setValue('${context.asset_no}');
		IqpAssetRel.asset_qnt._setValue('${context.asset_qnt}');
		IqpAssetRel.asset_total_amt._setValue('${context.asset_total_amt}');
		IqpAssetRel.takeover_total_amt._setValue('${context.takeover_total_amt}');
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAssetRel._toForm(form);
		IqpAssetRelList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetRelPage() { 
		var paramStr = IqpAssetRelList._obj.getParamStr(['pk_id','asset_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetRelUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetRel() {
		var paramStr = IqpAssetRelList._obj.getParamStr(['pk_id','asset_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetRelViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetRelPage() {
		var url = '<emp:url action="getIqpAssetRelAddPage.do"/>?takeover_date=${context.takeover_date}&asset_no='+'<%=asset_no%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetRel() {
		var paramStr = IqpAssetRelList._obj.getParamStr(['pk_id','asset_no']);
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
							var url = '<emp:url action="queryIqpAssetRelList.do"/>?takeover_date=${context.takeover_date}&asset_no='+'<%=asset_no%>'; 
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
				var url = '<emp:url action="deleteIqpAssetRelRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};   
	
	function doReset(){
		page.dataGroups.IqpAssetRelGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpAssetRelGroup" title="当前资产包汇总" maxColumn="2">
			<emp:text id="IqpAssetRel.asset_no" label="资产包编号"  readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="IqpAssetRel.asset_qnt" label="资产数量" dataType="Int"  readonly="true" />
			<emp:text id="IqpAssetRel.asset_total_amt" label="资产总额" dataType="Currency"  readonly="true" />
			<emp:text id="IqpAssetRel.takeover_total_amt" label="转让总额" dataType="Currency"  readonly="true" />
	</emp:gridLayout>
	
	<div align="left">
		<emp:actButton id="getAddIqpAssetRelPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAssetRelPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAssetRel" label="删除" op="remove"/>
		<emp:actButton id="viewIqpAssetRel" label="查看" op="view"/>
	</div> 

	<emp:table icollName="IqpAssetRelList" pageMode="true" url="pageIqpAssetRelQuery.do" reqParams="asset_no=${context.asset_no}">   
	    <emp:text id="pk_id" label="PK_ID" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/> 
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_bal" label="贷款余额" dataType="Currency"/>
		<emp:text id="takeover_amt" label="转让本金" dataType="Currency"/>
		<emp:text id="loan_start_date" label="起始日期" />
		<emp:text id="loan_end_date" label="到期日期" />
		<emp:text id="asset_no" label="资产包编号" hidden="true"/>  
	</emp:table>
	
</body>
</html>
</emp:page>
    