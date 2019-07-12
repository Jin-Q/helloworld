<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function onload(){
		//alert(asset_scale._getValue());
	};
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAssetPrdInfo._toForm(form);
		IqpAssetPrdInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetPrdInfoPage() {
		var paramStr = IqpAssetPrdInfoList._obj.getParamStr(['prd_id','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetPrdInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetPrdInfo() {
		var paramStr = IqpAssetPrdInfoList._obj.getParamStr(['prd_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetPrdInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetPrdInfoPage() {
		var url = '<emp:url action="getIqpAssetPrdInfoAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetPrdInfo() {
		var paramStr = IqpAssetPrdInfoList._obj.getParamStr(['prd_id']);
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
							var url = '<emp:url action="queryIqpAssetPrdInfoList.do"/>?serno='+'<%=serno%>';
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
				var url = '<emp:url action="deleteIqpAssetPrdInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetPrdInfoGroup.reset();
	};

	function doUpdateIqpAssetPrdEval() {
		var paramStr = IqpAssetPrdInfoList._obj.getParamStr(['prd_id','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetPrdEvalUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpAssetPrdEval() {
		var paramStr = IqpAssetPrdInfoList._obj.getParamStr(['prd_id','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetPrdEvalViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	/**添加"资产占比"、"资产规模"的信息显示  2014-08-04   邓亚辉*/
</script>
</head>
<body class="page_content" onload="onload()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpAssetPrdInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAssetPrdInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAssetPrdInfo" label="删除" op="remove"/>
		<emp:actButton id="viewIqpAssetPrdInfo" label="查看" op="view"/>
		<emp:actButton id="updateIqpAssetPrdEval" label="产品评级信息" op="update" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
		<emp:actButton id="viewIqpAssetPrdEval" label="产品评级信息" op="visit" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
	</div>

	<emp:table icollName="IqpAssetPrdInfoList" pageMode="true" url="pageIqpAssetPrdInfoQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="prd_id" label="产品代码" />
		<emp:text id="prd_type" label="产品档次" />
		<emp:text id="prd_short_name" label="产品简称" />
		<emp:text id="prd_qlty" label="产品性质" dictname="STD_ZB_PRD_QLTY"/>
		<emp:text id="prd_cur_type" label="产品币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="asset_perc" label="资产占比" dataType="Percent"/>
		<emp:text id="asset_scale" label="资产规模" dataType="Currency" />
		<emp:text id="fore_end_date" label="预期到期日期" />
		<emp:text id="int_bill_type" label="息票品种" dictname="STD_ZB_INT_BILL_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    