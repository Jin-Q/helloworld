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
		PrdPvRiskItem._toForm(form);
		PrdPvRiskItemList._obj.ajaxQuery(null,form);
	};
	
	function doConn(){
		var data = PrdPvRiskItemList._obj.getSelectedData();
		if(data.length == 0){
			alert("请先选择拦截项");
		}else {
			var item_id = data[0].item_id._getValue();
			var url = '<emp:url action="importPrdPreventItemRel.do"/>?preventId=${context.preventId}&item_id='+item_id;
			url = EMPTools.encodeURI(url);
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
						alert("引入成功!");
						window.location.reload();
						window.opener.location.reload();
					}else {
						alert("引入失败!");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
		
	}

	function doSelect(){
		var data = FlowList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		window.opener["${context.returnMethod}"](data[0]);
		window.close();
	};
	
	function doReset(){
		page.dataGroups.PrdPvRiskItemGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPvRiskItemGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="flow_id" label="流程编号" />
			<emp:text id="flow_name" label="流程名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选择返回" />
	</div>
	<emp:table icollName="FlowList" pageMode="true" url="pageQueryFlowListPop.do">
		<emp:text id="flow_id" label="流程编号" />
			<emp:text id="flow_name" label="流程名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    