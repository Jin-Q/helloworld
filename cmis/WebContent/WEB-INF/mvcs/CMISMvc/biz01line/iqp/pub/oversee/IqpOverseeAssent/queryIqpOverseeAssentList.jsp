<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<% String type=(String)request.getParameter("type"); %>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpOverseeAssent._toForm(form);
		IqpOverseeAssentList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpOverseeAssentPage() {
		var paramStr = IqpOverseeAssentList._obj.getParamStr(['assent_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeAssentUpdatePage.do"/>?'+paramStr+'&type='+'<%=type%>';
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpOverseeAssent() {
		var paramStr = IqpOverseeAssentList._obj.getParamStr(['assent_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeAssentViewPage.do"/>?'+paramStr+'&type='+'<%=type%>';
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpOverseeAssentPage() {
		var serno = "${context.serno}";
		var url = '<emp:url action="getIqpOverseeAssentAddPage.do"/>?type='+'<%=type%>&serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		EMPTools.openWindow(url,'newwindow',param);
	};
	
	function doDeleteIqpOverseeAssent() {
		var paramStr = IqpOverseeAssentList._obj.getParamStr(['assent_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
							var flag = jsonstr.flag;
							if(flag == "success" ){
								alert("删除成功！");
								window.location.reload();
							}else{
								alert("删除失败！");
							}
						}
						var handleFailure = function(o){
						alert("异步回调失败！");	
						};
						var url = '<emp:url action="deleteIqpOverseeAssentRecord.do"/>?'+paramStr;
						var callback = {
							success:handleSuccess,
							failure:null
						};
						url = EMPTools.encodeURI(url);
						var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);			
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpOverseeAssentGroup.reset();
	};

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpOverseeAssentPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpOverseeAssentPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpOverseeAssent" label="删除" op="remove"/>
		<emp:actButton id="viewIqpOverseeAssent" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpOverseeAssentList" pageMode="true" url="pageIqpOverseeAssentQuery.do" reqParams="type=${context.type}">
		<emp:text id="assent_name" label="资产名称" />
		<emp:text id="assent_qnt" label="资产数量" />
		<emp:text id="fore_value" label="账面净值" dataType="Currency"/>
		<emp:text id="reckon_value" label="估计现值" dataType="Currency"/>
		<emp:text id="util_case" label="目前使用情况" dictname="STD_ZX_FIELD_OWNER" />
		<emp:text id="util_term" label="已使用年限(年)" />
		<emp:text id="wrr_proof" label="权利凭证" />
		<emp:text id="is_pldimn" label="是否已抵质押" dictname="STD_ZX_YES_NO" />
		<emp:text id="assent_type" label="资产类别" hidden="true"/>
		<emp:text id="assent_id" label="资产编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    