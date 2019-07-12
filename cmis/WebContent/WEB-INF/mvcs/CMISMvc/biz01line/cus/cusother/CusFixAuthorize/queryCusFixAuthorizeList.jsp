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
		CusFixAuthorize._toForm(form);
		CusFixAuthorizeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusFixAuthorizePage() {
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusFixAuthorizeList._obj.getSelectedData()[0].status._getValue();
			if(status=="2"){
				var url = '<emp:url action="getCusFixAuthorizeUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("只有状态为'未完成'的授权才能进行修改操作！");
				return;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusFixAuthorize() {
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusFixAuthorizeViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusFixAuthorizePage() {
		var url = '<emp:url action="getCusFixAuthorizeAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusFixAuthorize() {
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusFixAuthorizeList._obj.getSelectedData()[0].status._getValue();
			if(status=="1"){
				alert("生效的记录不允许被删除！");
			}else{
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteCusFixAuthorizeRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusFixAuthorizeGroup.reset();
	};
	
	/*--user code begin--*/
	//修改密码
	function doGetUpdatePassWord(){//getCusFixPassUpdatePage
		var serno = CusFixAuthorizeList._obj.getSelectedData()[0].serno._getValue();
		if(serno!=null&&serno!=''){
			var url = '<emp:url action="getCusFixPassUpdatePage.do"/>?CusFixAuthorize.serno='+serno;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=238,width=524,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}else {
			alert('请选择一条数据！');
		}
	}

	function doInure(){
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusFixAuthorizeList._obj.getSelectedData()[0].status._getValue();
			if(status == "1"){
				alert("此条记录已经为'生效'！")
			}else{
				Change4Status();
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doAbate(){
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusFixAuthorizeList._obj.getSelectedData()[0].status._getValue();
			if(status != "1"){
				alert("此条记录不为'生效'状态！")
			}else{
				Change4Status4Abate();
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function Change4Status4Abate() {
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		var status = CusFixAuthorizeList._obj.getSelectedData()[0].status._getValue();
		if (paramStr != null){
			if(confirm("是否确认要失效？")){
				var url = '<emp:url action="update4Status.do"/>?'+paramStr+'&status='+status;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="success"){
							alert("失效成功!");
							window.location.reload();
					   }else 
						 alert("失效失败!");
						 return;
					}
				};
				var handleFailure = function(o){	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		}
	};

	function Change4Status() {
		var paramStr = CusFixAuthorizeList._obj.getParamStr(['serno']);
		var status = CusFixAuthorizeList._obj.getSelectedData()[0].status._getValue();
		if (paramStr != null){
			if(confirm("是否确认要生效？")){
				var url = '<emp:url action="update4Status.do"/>?'+paramStr+'&status='+status;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="success"){
							alert("生效成功!");
							window.location.reload();
					   }else 
						 alert("当前用户存在一笔有效的授权！");
						 return;
					}
				};
				var handleFailure = function(o){	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		}
	};
	
	function setconId(data){
		CusFixAuthorize.auth_id._setValue(data.actorno._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusFixAuthorizeGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="CusFixAuthorize.auth_id" label="授权客户码" url="getAllSUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:select id="CusFixAuthorize.status" label="状态" dictname="STD_ZB_STATUS"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddCusFixAuthorizePage" label="新增" op="add"/>
		<emp:button id="getUpdateCusFixAuthorizePage" label="修改" op="update"/>
		<emp:button id="deleteCusFixAuthorize" label="删除" op="remove"/>
		<emp:button id="viewCusFixAuthorize" label="查看" op="view"/>
		<emp:button id="getUpdatePassWord" label="修改密码" op="updpass"/>
		<emp:button id="inure" label="生效" op="inure"/>
		<emp:button id="abate" label="失效" op="abate"/>
	</div>
	<emp:table icollName="CusFixAuthorizeList" pageMode="true" url="pageCusFixAuthorizeQuery.do">
		<emp:text id="checkcode" label="校验码" hidden="true"/>
		<emp:text id="auth_id" label="授权客户码" />
		<emp:text id="auth_id_displayname" label="授权人" />
		<emp:text id="startdate" label="开始日期" />
		<emp:text id="enddate" label="结束日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:select id="status" label="状态" dictname="STD_ZB_STATUS"/>
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>