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
		IqpAbsProApp._toForm(form);
		IqpAbsProAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetApplyIqpAbsProAppPage() {
		var paramStr = IqpAbsProAppList._obj.getParamStr(['pre_package_serno']);
		if (paramStr != null) {
			var biz_type = '${context.biz_type}';
			var prc_status = IqpAbsProAppList._obj.getParamValue(['prc_status']);
			if(biz_type =='2' && (prc_status=='5')){
				alert('该状态下的预封包信息不能复核!');
				return;
			}
			var url = '<emp:url action="getIqpAbsProAppUpdatePage.do"/>?'+paramStr+'&biz_type=${context.biz_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAbsProApp() {
		var paramStr = IqpAbsProAppList._obj.getParamStr(['pre_package_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAbsProAppViewPage.do"/>?'+paramStr+'&biz_type=${context.biz_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAbsProAppPage() {
		var url = '<emp:url action="getIqpAbsProAppAddPage.do"/>?biz_type=${context.biz_type}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAbsProApp() {
		var paramStr = IqpAbsProAppList._obj.getParamStr(['pre_package_serno']);
		if (paramStr != null) {
			var prc_status = IqpAbsProAppList._obj.getParamValue(['prc_status']);
			if(prc_status!='1'){
				alert('该状态下的预封包信息不能删除!');
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined){
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
				var url = '<emp:url action="deleteIqpAbsProAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)	
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAbsProAppGroup.reset();
	};

	function doGetTrade4QueryIqpAbsPro(){
		var paramStr = IqpAbsProAppList._obj.getParamStr(['pre_package_serno','batch_no']);
		if (paramStr != null) {
			var prc_status = IqpAbsProAppList._obj.getParamValue(['prc_status']);
			if(prc_status!='5'){
				alert('【发送核心成功】状态的记录才能发起核心预封包查询！');
				return;
			}
			//调用核心查询交易
			var handleSuccess = function(o){
				if(o.responseText !== undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("交易成功!");
						window.location.reload();
					}else {
						alert("交易失败!");
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
			var url = '<emp:url action="getTrade4QueryIqpAbsPro.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			
		} else {
			alert('请先选择一条记录！');
		}
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="IqpAbsProAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAbsProApp.batch_no" label="批次号" />
			<emp:text id="IqpAbsProApp.batch_name" label="证券化批次名称"/>
		    <emp:text id="IqpAbsProApp.trust_org_no" label="受托机构名称"/>
	        <emp:select id="IqpAbsProApp.is_this_org_service" label="是否本机构服务" dictname="STD_ZX_YES_NO"/>
	        <emp:date id="IqpAbsProApp.pre_package_date" label="预封包日期" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" /> 
	
	<div align="left">
		<%
			String biz_type=request.getParameter("biz_type");
			if(biz_type!=null && biz_type.equals("1")){
		%>
		<emp:button id="getAddIqpAbsProAppPage" label="预封包新增" />
		<emp:button id="getApplyIqpAbsProAppPage" label="申请" />
		<emp:button id="deleteIqpAbsProApp" label="删除" />
		<%
			}
		%>
		<%
			if(biz_type!=null && biz_type.equals("2")){
		%>
		<emp:button id="getApplyIqpAbsProAppPage" label="受理" />
		<emp:button id="getTrade4QueryIqpAbsPro" label="核心查询" />
		<%
			}
		%>
		<emp:button id="viewIqpAbsProApp" label="查看" />
	</div>

	<emp:table icollName="IqpAbsProAppList" pageMode="true" url="pageIqpAbsProAppQuery.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="pre_package_serno" label="预封包流水号" />
		<emp:text id="batch_name" label="证券化批次名称"/>
		<emp:text id="trust_org_no" label="受托机构名称"/>
	    <emp:select id="is_this_org_service" label="是否本机构服务" dictname="STD_ZX_YES_NO"/>
		<emp:text id="pre_package_date" label="预封包日期" />
		<emp:text id="input_id" label="操作人员" />
		<emp:text id="prc_status" label="处理状态" dictname="STD_ABS_PRC_STATUS"/>
		<emp:text id="input_br_id" label="操作机构" hidden="true"/>
		<emp:text id="update_date" label="修改日期" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    