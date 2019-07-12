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
		SDuty._toForm(form);
		SDutyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSDutyPage() {
		var paramStr = SDutyList._obj.getParamStr(['dutyno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDutyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewSDuty() {
		var paramStr = SDutyList._obj.getParamStr(['dutyno']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDutyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddSDutyPage() {
		var url = '<emp:url action="getSDutyAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		//window.location = url;
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteSDuty() {
		var paramStr = SDutyList._obj.getParamStr(['dutyno']);
		if (paramStr != null) {
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
						var msg = jsonstr.msg;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}else {
							alert(msg);
							return;
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
				var url = '<emp:url action="deleteSDutyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.SDutyGroup.reset();
	};
	
	/*--user code begin--*/
	function doExcelSDuty(){
		var url = '<emp:url action="SDutyExcel.do"/>&pageFalg=false';
		window.open(url);
	}
		
	function init(){
		var menuId = "${context.menuId}";
		if(menuId == 'gwgl'){
			document.getElementById('button_null').style.display = 'none';
		}else{
			var buttonGroup=document.getElementById('buttonGroup');
		//	var buttonReturn=document.getElementById('button_select');
			if(window.opener){
				buttonGroup.style.display='none';
		//		buttonReturn.style.display='inline';
			}else{
				buttonGroup.style.display='inline';
		//		buttonReturn.style.display='none';
			}
		}
	}
/*	function doSelect() {
		var data = SDutyList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			window.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};*/

	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = SDutyList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			} else {
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="init()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SDutyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SDuty.dutyno" label="岗位码" />
			<emp:text id="SDuty.dutyname" label="岗位名称" />
			<emp:text id="SDuty.organno" label="机构码" />
			<emp:text id="SDuty.depno" label="部门码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div id="buttonGroup" align="left">
		<emp:button id="getAddSDutyPage" label="新增" op="add"/>
		<emp:button id="getUpdateSDutyPage" label="修改" op="update"/>
		<emp:button id="deleteSDuty" label="删除" op="remove"/>
		<emp:button id="viewSDuty" label="查看" op="view"/>
	</div>

	<emp:table icollName="SDutyList" pageMode="true" url="pageSDutyQuery.do">
		<emp:text id="dutyno" label="岗位码" />
		<emp:text id="dutyname" label="岗位名称" />
		<emp:text id="organno" label="机构码" />
		<emp:text id="depno" label="部门码" />
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/>
	<br>
	</div>
</body>
</html>
</emp:page>
    