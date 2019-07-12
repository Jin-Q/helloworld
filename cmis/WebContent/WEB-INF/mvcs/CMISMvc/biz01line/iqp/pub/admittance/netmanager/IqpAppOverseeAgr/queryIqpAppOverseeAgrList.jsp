<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	var modifyWindow;
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAppOverseeAgr._toForm(form);
		IqpAppOverseeAgrList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppOverseeAgrPage() {
		var paramStr = IqpAppOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var status = IqpAppOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(status == "0"){
                alert("失效状态不能修改 ！");
                return;
			}
			var url = '<emp:url action="getIqpAppOverseeAgrUpdatePage.do"/>?'+paramStr+'&serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.mem_manuf_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppOverseeAgr() {
		if(modifyWindow != null){
			modifyWindow.close();
		}
		var paramStr = IqpAppOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppOverseeAgrViewPage.do"/>?'+paramStr+'&serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.mem_manuf_type}';
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			modifyWindow = window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppOverseeAgrPage() {
		var url = '<emp:url action="getIqpAppOverseeAgrAddPage.do"/>?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.mem_manuf_type}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAppOverseeAgr() {
		var paramStr = IqpAppOverseeAgrList._obj.getParamStr(['oversee_agr_no','serno']);
		if (paramStr != null) {
			var cont_status = IqpAppOverseeAgrList._obj.getSelectedData()[0].cont_status._getValue();
			if(cont_status == "0"){
                alert("原有协议不能删除!");
                return;
			}
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
							alert("删除成功！");
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
				var url = '<emp:url action="deleteIqpAppOverseeAgrRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doRemoveIqpAppOverseeAgr() {
		var paramStr = IqpAppOverseeAgrList._obj.getParamStr(['oversee_agr_no','serno']);
		var msg;
		if (paramStr != null) {
			var cont_status = IqpAppOverseeAgrList._obj.getSelectedData()[0].cont_status._getValue();
			var status = IqpAppOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(cont_status == "0" && status == "1"){
				msg="是否确认要做失效操作？";
			}else if(cont_status == "0" && status == "0"){
				msg="是否确认失效撤销操作？";
			}else{
                alert("非原有协议不能做失效/撤销操作!");
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
						var msg = jsonstr.msg;
						if(flag == "success"){
							if(msg == "success"){
								alert("协议撤销失效操作成功!");
							}else{
								alert("协议失效操作成功!");
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
				var url = '<emp:url action="removeIqpAppOverseeAgrRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppOverseeAgrGroup.reset();
	};
	function closeMdyWin(){
		if(modifyWindow != null){
			modifyWindow.close();
		}
	}	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onunload="closeMdyWin();">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpAppOverseeAgrPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAppOverseeAgrPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAppOverseeAgr" label="删除" op="remove"/>
		<emp:actButton id="removeIqpAppOverseeAgr" label="失效/撤销" op="remove"/>
		<emp:actButton id="viewIqpAppOverseeAgr" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAppOverseeAgrList" pageMode="true" url="pageIqpAppOverseeAgrQuery.do" reqParams="serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.mem_manuf_type}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="oversee_agr_no" label="监管协议号" />
		<emp:text id="mortgagor_id" label="客户码" hidden="true"/>
		<emp:text id="mortgagor_id_displayname" label="出质人客户名称" />
		<emp:text id="oversee_con_id" label="监管企业编号" hidden="true"/>
		<emp:text id="oversee_con_id_displayname" label="监管企业名称" />
		<emp:text id="oversee_mode" label="监管方式" dictname="STD_ZB_OVERSEE_TYPE"/>
		<emp:text id="vigi_line" label="警戒线" dataType="Percent" />
		<emp:text id="stor_line" label="平仓线" dataType="Percent" />
		<emp:text id="froze_line" label="冻结线" dataType="Percent" />
		<emp:text id="status" label="生效状态" dictname="STD_ZB_STATUS"/>
		<emp:text id="cont_status" label="协议状态" dictname="STD_ZB_NET_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    