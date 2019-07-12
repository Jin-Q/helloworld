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
			<emp:text id="PrdPvRiskItem.item_id" label="项目编号" />
			<emp:text id="PrdPvRiskItem.item_name" label="项目名称" />
			<emp:select id="PrdPvRiskItem.used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="conn" label="引入" />
	</div>
	<emp:table icollName="PrdPvRiskItemList" pageMode="true" url="pageQueryPrdPvItemListPop.do?preventId=${context.preventId}">
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="used_ind" label="启用标志" dictname="STD_ZX_YES_NO" />
	</emp:table>
	
</body>
</html>
</emp:page>
    