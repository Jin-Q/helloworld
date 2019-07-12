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
		IqpOverseeUnderstore._toForm(form);
		IqpOverseeUnderstoreList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpOverseeUnderstorePage() {
		var paramStr = IqpOverseeUnderstoreList._obj.getParamStr(['store_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeUnderstoreUpdatePage.do"/>?' + encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpOverseeUnderstore() {
		var paramStr = IqpOverseeUnderstoreList._obj.getParamStr(['store_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeUnderstoreViewPage.do"/>?' + encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpOverseeUnderstorePage() {
		var serno = "${context.serno}";
		var oversee_org_id = "${context.oversee_org_id}";
		var url = '<emp:url action="getIqpOverseeUnderstoreAddPage.do"/>&serno='+serno+'&oversee_org_id='+oversee_org_id;
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		EMPTools.openWindow(url,'newwindow',param);
		window.location.reload();
	};	
	function doDeleteIqpOverseeUnderstore() {
		var paramStr = IqpOverseeUnderstoreList._obj.getParamStr(['store_id']);
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
						var url = '<emp:url action="deleteIqpOverseeUnderstoreRecord.do"/>?' + encodeURI(paramStr);
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
		page.dataGroups.IqpOverseeUnderstoreGroup.reset();
	};
	/*--user code begin--*/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpOverseeUnderstorePage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpOverseeUnderstorePage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpOverseeUnderstore" label="删除" op="remove"/>
		<emp:actButton id="viewIqpOverseeUnderstore" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpOverseeUnderstoreList" pageMode="true" url="pageIqpOverseeUnderstoreQuery.do">		
		<emp:text id="store_id" label="仓库编号" />
		<emp:text id="store_name" label="仓库名称" />
		<emp:text id="store_addr_displayname" label="仓库地址" />
		<emp:select id="store_cha" label="仓库性质" dictname="STD_ZB_STORE_CHA" />
		<emp:text id="store_bend" label="仓库容量(吨)" />
		<emp:text id="transfer_abi" label="吞吐能力(吨)" />
		<emp:text id="open_squ" label="露天面积(m²)" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="store_addr" label="仓库地址" hidden="true"/>
		<emp:text id="serno" label="业务流水号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    