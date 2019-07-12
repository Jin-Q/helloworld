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
		IqpOverseeAgr._toForm(form);
		IqpOverseeAgrList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpOverseeAgrPage() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
		var url = '<emp:url action="getIqpOverseeAgrUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpOverseeAgr() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeAgrViewPage.do"/>?'+paramStr+'&menuId=JGXY&flag=havaButton';
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpOverseeAgrPage() {
		var url = '<emp:url action="getIqpOverseeAgrAddPage.do"/>?cus_id=${context.cus_id}'
			                                                     +"&mem_cus_id=${context.mem_cus_id}"
			                                                     +"&mem_manuf_type=${context.mem_manuf_type}"
			                                                     +"&net_agr_no=${context.net_agr_no}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpOverseeAgr() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("删除成功！");
						window.location.reload();
					}else{
					}
				}
				var handleFailure = function(o){
				alert("异步回调失败！");	
				};
				var url = '<emp:url action="deleteIqpOverseeAgrRecord.do"/>?'+paramStr;
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	      }
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpOverseeAgrGroup.reset();
	};
	
	/*--user code begin--*/
	function doCfirm()
	{
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("确认成功！");
						window.location.reload();
					}else{
					}
				}
				var handleFailure = function(o){
				alert("异步回调失败！");	
				};
				var url = '<emp:url action="sub2UpdateStatus.do"/>?'+paramStr;
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:actButton id="getAddIqpOverseeAgrPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpOverseeAgrPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpOverseeAgr" label="删除" op="remove"/>
		<emp:actButton id="viewIqpOverseeAgr" label="查看" op="view"/>
		<emp:actButton id="cfirm" label="确认" op="update"/>
		<emp:actButton id="rec" label="解除监管" op="update"/>
		<emp:actButton id="cfirmrec" label="解除监管确认" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80" op="update"/>
	</div>

	<emp:table icollName="IqpOverseeAgrList" pageMode="true" url="pageIqpOverseeAgrQuery.do?net_agr_no=${context.net_agr_no}&mem_manuf_type=${context.mem_manuf_type}&mem_cus_id=${context.mem_cus_id}">
		<emp:text id="oversee_agr_no" label="监管协议号" />
		<emp:text id="mortgagor_id" label="客户码" hidden="true"/>
		<emp:text id="mortgagor_id_displayname" label="出质人客户名称" />
		<emp:text id="oversee_con_id" label="监管企业编号" hidden="true"/>
		<emp:text id="oversee_con_id_displayname" label="监管企业名称" />
		<emp:text id="oversee_mode" label="监管方式" dictname="STD_ZB_OVERSEE_TYPE"/>
		<emp:text id="vigi_line" label="警戒线" dataType="Percent" />
		<emp:text id="stor_line" label="平仓线" dataType="Percent" />
		<emp:text id="froze_line" label="冻结线" dataType="Percent" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    