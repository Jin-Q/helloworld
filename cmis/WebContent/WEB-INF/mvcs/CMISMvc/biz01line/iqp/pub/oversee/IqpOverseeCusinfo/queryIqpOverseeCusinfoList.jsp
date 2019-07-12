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
		IqpOverseeCusinfo._toForm(form);
		IqpOverseeCusinfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpOverseeCusinfoPage() {
		var paramStr = IqpOverseeCusinfoList._obj.getParamStr(['cusinfo_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeCusinfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpOverseeCusinfo() {
		var paramStr = IqpOverseeCusinfoList._obj.getParamStr(['cusinfo_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeCusinfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			EMPTools.openWindow(url,'newwindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpOverseeCusinfoPage() {
		var serno = "${context.serno}";
		var url = '<emp:url action="getIqpOverseeCusinfoAddPage.do"/>&serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=1000, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		EMPTools.openWindow(url,'newwindow',param);
	};
	
	function doDeleteIqpOverseeCusinfo() {
		var paramStr = IqpOverseeCusinfoList._obj.getParamStr(['cusinfo_id']);
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
						var url = '<emp:url action="deleteIqpOverseeCusinfoRecord.do"/>?'+paramStr;
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
		page.dataGroups.IqpOverseeCusinfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpOverseeCusinfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpOverseeCusinfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpOverseeCusinfo" label="删除" op="remove"/>
		<emp:actButton id="viewIqpOverseeCusinfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpOverseeCusinfoList" pageMode="true" url="pageIqpOverseeCusinfoQuery.do">
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="goods_name" label="储运货物名称" />
		<emp:text id="transfer_qnt" label="年吞吐量(吨)" />
		<emp:text id="avg_storage" label="平均仓储量(吨)" />
		<emp:text id="biz_yearn" label="年均业务收入(元)" dataType="Currency"/>
		<emp:text id="coop_bank" label="合作银行" hidden="true"/>
		<emp:text id="serno" label="流水主键" hidden="true"/>
		<emp:text id="cusinfo_id" label="客户信息编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    