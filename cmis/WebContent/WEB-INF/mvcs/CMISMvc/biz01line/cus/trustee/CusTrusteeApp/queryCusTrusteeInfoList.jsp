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
		CusTrusteeInfo._toForm(form);
		CusTrusteeInfoList._obj.ajaxQuery(null,form);
	};

	function doViewCusTrusteeInfo() {
		var paramStr = CusTrusteeInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusTrusteeInfoGroup.reset();
	};
	function doBack(){
		var paramStr = CusTrusteeInfoList._obj.getParamStr(['serno']);
		
		if (paramStr != null) {
			var sele = CusTrusteeInfoList._obj.getSelectedData()[0].trustee_status._getValue();
			if(sele != '1'){
				alert('托管状态下业务才能回收!');
				return ;
			}
			if(confirm("是否确认要回收？")){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
							alert("Parse jsonstr define error!" + e.message);
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							alert("收回成功!");
							window.location.reload();
						}else {
							alert(jsonstr.msg);
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="backCusTrusteeAppRecord.do"/>?&menuId=custrusteeapp&'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	function setConsignorId(data){
		CusTrusteeInfo.consignor_id._setValue(data.actorno._getValue());
		CusTrusteeInfo.consignor_id_displayname._setValue(data.actorname._getValue());
	}
	function setTrusteeId(data){
		CusTrusteeInfo.trustee_id._setValue(data.actorno._getValue());
		CusTrusteeInfo.trustee_id_displayname._setValue(data.actorname._getValue());
	}

	function doPrintTru(){
		var paramStr = CusTrusteeInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var serno  = CusTrusteeInfoList._obj.getParamValue(['serno']);
			var trustee_status  = CusTrusteeInfoList._obj.getParamValue(['trustee_status']);
			if(trustee_status == '1'){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
							alert("Parse jsonstr define error!" + e.message);
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							var url = '<emp:url action="getReportShowPage.do"/>&reportId=custrustee/rpCusTrusteeApplyNote.raq&serno='+serno;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
						}else {
							alert(jsonstr.msg);
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="getReport4CusTrustee.do"/>?&menuId=custrusteeapp&'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}else{
				alert("非托管状态记录,不能打印！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};	
	function doPrintRct(){
		var paramStr = CusTrusteeInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var serno  = CusTrusteeInfoList._obj.getParamValue(['serno']);
			var trustee_status  = CusTrusteeInfoList._obj.getParamValue(['trustee_status']);
			if(trustee_status == '2'){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
							alert("Parse jsonstr define error!" + e.message);
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "suc"){
							var url = '<emp:url action="getReportShowPage.do"/>&reportId=custrustee/rpCusTrusteeRetractNote.raq&serno='+serno;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
						}else {
							alert(jsonstr.msg);
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="getReport4CusTrustee.do"/>?&menuId=custrusteeapp&'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}else{
				alert("非回收状态记录,不能打印！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusTrusteeInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusTrusteeInfo.serno" label="业务流水号" />
			<emp:text id="CusTrusteeInfo.consignor_id" label="委托人编号" hidden="true"/>
			<emp:pop id="CusTrusteeInfo.consignor_id_displayname" label="委托人名称" url="getAllSUserPopListOp.do" returnMethod="setConsignorId"/>
			<emp:pop id="CusTrusteeInfo.trustee_id_displayname" label="托管人名称" url="getAllSUserPopListOp.do" returnMethod="setTrusteeId"/>
			<emp:text id="CusTrusteeInfo.trustee_id" label="托管人编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCusTrusteeInfo" label="查看" op="view"/>
		<emp:button id="back" label="收回" op="retract"/>
		<emp:button id="printTru" label="托管报告打印" op="printTru" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		<emp:button id="printRct" label="回收报告打印" op="printRet" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		
	</div>

	<emp:table icollName="CusTrusteeInfoList" pageMode="true" url="pageCusTrusteeInfoQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="consignor_id" label="委托人" hidden="true"/>
		<emp:text id="consignor_br_id" label="委托机构" hidden="true"/>
		<emp:text id="trustee_id" label="托管人" hidden="true"/>
		<emp:text id="trustee_br_id" label="托管机构" hidden="true"/>
		<emp:text id="consignor_id_displayname" label="委托人" />
		<emp:text id="consignor_br_id_displayname" label="委托机构" />
		<emp:text id="trustee_id_displayname" label="托管人" />
		<emp:text id="trustee_br_id_displayname" label="托管机构" />
		<emp:text id="trustee_status" label="托管状态" dictname="STD_ZB_TRUSTEE_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    