<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String showMem = "";
	if(context.containsKey("showMem")){
		showMem = (String)context.getDataValue("showMem");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAppMemMana._toForm(form);
		IqpAppMemManaList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppMemManaPage() {
		var paramStr = IqpAppMemManaList._obj.getParamStr(['serno','mem_cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppMemManaUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppMemMana() {
		var paramStr = IqpAppMemManaList._obj.getParamStr(['serno','mem_cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppMemManaViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppMemManaPage() {
		var url = '<emp:url action="getIqpAppMemManaAddPage.do"/>?serno=${context.serno}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAppMemMana() {
		var paramStr = IqpAppMemManaList._obj.getParamStr(['serno','mem_cus_id']);
		var msg;
		if (paramStr != null) {
			var status = IqpAppMemManaList._obj.getSelectedData()[0].status._getValue();
			if(status == "3"){
				msg="是否确认要退网？";
			}else if(status == "2"){
				msg="是否确认撤销退网操作？"; 
			}else{
                alert("非原有成员或退网成员不能退网/撤销!");
                return;
			}
			if(confirm(msg)){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var message = jsonstr.message;
						if(flag == "success"){
							if(message == "backNet"){
								alert("成员退网成功!");
							}else{
								alert("成员撤销退网成功!");
							}
							window.location.reload();
						}else {
							alert("操作失败!");
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
				var url = '<emp:url action="deleteIqpAppMemManaRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doRemoveIqpAppMemMana() {
		var paramStr = IqpAppMemManaList._obj.getParamStr(['serno','mem_cus_id']);
		var msg;
		if (paramStr != null) {
			var status = IqpAppMemManaList._obj.getSelectedData()[0].status._getValue();
			if(status == "1"){
				msg="是否确认删除？";
			}else{
				alert("非入网成员不能删除!");
				return;
			}
			if(confirm(msg)){
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
							window.location.reload();
						}else {
							alert("操作失败!");
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
				var url = '<emp:url action="removeIqpAppMemManaRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppMemManaGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
	<%if(!"no".equals(showMem)){ %>
		<emp:button id="getAddIqpAppMemManaPage" label="新增成员" op="add"/>
		<emp:button id="getUpdateIqpAppMemManaPage" label="修改" op="update"/>
		<emp:button id="removeIqpAppMemMana" label="删除" op="remove"/>
		<emp:button id="deleteIqpAppMemMana" label="退网/撤销" op="remove"/>
	<%}%>
		<emp:actButton id="viewIqpAppMemMana" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAppMemManaList" pageMode="true" url="pageIqpAppMemManaQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="mem_cus_id" label="成员企业客户码" />
		<emp:text id="mem_cus_id_displayname" label="成员企业名称" />		
		<emp:text id="mem_manuf_type" label="成员企业类别" dictname="STD_ZB_MANUF_TYPE" />
		<emp:text id="term" label="在途期限（天）" />
		<emp:text id="lmt_quota" label="授信限额（元）" dataType="Currency"/>
		<emp:text id="status" label="成员变更状态" dictname="STD_ZB_MEN_TYPE"/>
		
	</emp:table>
	
</body>
</html>
</emp:page>
    