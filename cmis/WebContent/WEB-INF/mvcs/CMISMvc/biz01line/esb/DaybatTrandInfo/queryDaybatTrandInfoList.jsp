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
		DaybatTrandInfo._toForm(form);
		DaybatTrandInfoList._obj.ajaxQuery(null,form);
	};
	
	function doViewDaybatTrandInfo() {
		var paramStr = DaybatTrandInfoList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getDaybatTrandInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.DaybatTrandInfoGroup.reset();
	};
	function doDealDaybatTrandInfo() {
		var paramStr = DaybatTrandInfoList._obj.getParamStr(['pk1']);
		if (paramStr != null) { 
			if(confirm("是否确认要发起交易？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("交易执行失败!");
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("交易执行成功!");
							var url = '<emp:url action="queryDaybatTrandInfoList.do"/>';  
							url = EMPTools.encodeURI(url);
							window.location=url;
						}else {
							alert("交易执行失败!"); 
						}
					}
				};
				var handleFailure = function(o){
					alert("交易执行失败！");	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="reDealFailTrade.do"/>?'+paramStr;	
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}     
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="DaybatTrandInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="DaybatTrandInfo.consumer_seq_no" label="交易流水" />
			<emp:text id="DaybatTrandInfo.service_code" label="交易码" />
			<emp:text id="DaybatTrandInfo.service_scene" label="交易场景" />
			<emp:date id="DaybatTrandInfo.tran_date" label="交易日期" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewDaybatTrandInfo" label="查看" />
		<emp:button id="dealDaybatTrandInfo" label="处理" />
	</div>

	<emp:table icollName="DaybatTrandInfoList" pageMode="true" url="pageDaybatTrandInfoQuery.do">
	    <emp:text id="pk1" label="主键" />
		<emp:text id="consumer_seq_no" label="交易流水" />
		<emp:text id="service_code" label="交易码" />
		<emp:text id="service_scene" label="交易场景" />
		<emp:text id="tran_date" label="交易日期" />
		<emp:text id="trand_msg" label="交易信息" />
	</emp:table>
	
</body>
</html>
</emp:page>
    