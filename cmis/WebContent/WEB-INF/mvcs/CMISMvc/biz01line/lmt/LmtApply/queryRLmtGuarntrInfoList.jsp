<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
 <% String type=(String)request.getParameter("type"); %>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doViewRLmtGuarntrInfo() {
		var paramStr = RLmtGuarntrInfoList._obj.getParamStr(['guar_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuaranteeViewPage.do"/>?'+paramStr+"&flag=lmt";
			url = EMPTools.encodeURI(url);
			var param = 'height=600, width=800, top=40, left=300, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddRLmtGuarntrInfoPage() {
		var url = '<emp:url action="queryGrtGuaranteePopList.do"/>?returnMethod=getGuaranty';
		url = EMPTools.encodeURI(url);
		var param = 'height=600, width=800, top=80, left=400, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function getGuaranty(data){
		var serno='${context.serno}';
		var limit_code='${context.limit_code}';
		var guar_id = data.guar_id._getValue();
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if("success" == flag){
					alert(msg);
					window.location.reload();
				}else{
					alert(msg);
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="insert2RLmtGuarNtr.do"/>?serno='+serno+'&guar_id='+guar_id+'&limit_code='+limit_code;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);	
	}
	//解除保证人与授信关系
    function doDeleteRLmtGuarntrInfo(){
    	var paramStr = RLmtGuarntrInfoList._obj.getParamStr(['guar_id']);
    	if (paramStr != null) {	
    	     if(confirm("是否确认要解除与此保证人的关系？")){
				 var handleSuccess = function(o){ 		
					 if(o.responseText != undefined){
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success" ){
							alert("删除成功！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o){
					alert("异步回调失败！");	
				};
				var limit_code = "${context.limit_code}";
				var url = '<emp:url action="deleteRLmtGuarntrInfoRecord.do"/>?'+paramStr+"&limit_code="+limit_code;
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
	}
		
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<% if("view".equals(type)) {%>
	<div align="left">
		<emp:actButton id="viewRLmtGuarntrInfo" label="查看" op="view"/>
	</div>
	<%}else {%>
	<div align="left">
		<emp:button id="getAddRLmtGuarntrInfoPage" label="引入" op="add"/>
		<emp:button id="deleteRLmtGuarntrInfo" label="删除" op="remove"/>
		<emp:button id="viewRLmtGuarntrInfo" label="查看" op="view"/>
	</div>
	<% }%>
	<emp:table icollName="RLmtGuarntrInfoList" pageMode="true" url="pageRLmtGuarntrInfoQuery.do?limit_code=${context.limit_code}">
		<emp:text id="guar_id" label="保证编号"/>
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_id_displayname" label="保证人名称" />
		<emp:text id="guar_type" label="保证形式" dictname="STD_GUAR_FORM"/>
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_bur_id" label="管理机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    