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
		LmtIndusListApply._toForm(form);
		LmtIndusListApplyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtIndusListApplyPage() {
		var paramStr = LmtIndusListApplyList._obj.getSelectedData();
		if(paramStr.length>1){
			alert("请选择单笔记录!");
			return;
		}else{
			if (paramStr.length > 0) {
				cus_id = paramStr[0].cus_id._getValue();
				paramStr = '?cus_id='+cus_id+'&serno='+serno;
				var url = '<emp:url action="getLmtIndusListApplyUpdatePage.do"/>'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			} else {
				alert('请先选择一条记录！');
			}
		}
	};
	
	function doViewLmtIndusListApply() {
		var paramStr = LmtIndusListApplyList._obj.getSelectedData();
		if(paramStr.length>1){
			alert("请选择单笔记录!");
			return;
		}else{					
			if (paramStr.length > 0) {
				cus_id = paramStr[0].cus_id._getValue();
				paramStr = '?cus_id='+cus_id+'&serno='+serno;
				var url = '<emp:url action="getLmtIndusListApplyViewPage.do"/>'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			} else {
				alert('请先选择一条记录！');
			}
		}
	};

	function doImportLmtIndusListApply() {
		var url = '<emp:url action="importLmtIndusListPop.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
	};
	
	function doGetAddLmtIndusListApplyPage() {
		var url = '<emp:url action="getLmtIndusListApplyAddPage.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doReset(){
		page.dataGroups.LmtIndusListApplyGroup.reset();
	};
	
	/*--user code begin--*/
	function doSubmitLmtIndusApply(){
		var paramStr = LmtIndusApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="submitLmtIndusFlow.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			doSubmitRecord(url);
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doSubmitRecord(url){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='1'){
					alert('提交成功!');
		            window.location.reload();
				}else if(operMsg=='2'){
					alert('操作失败!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("操作失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	};
	
	function doLoad(){
		serno = "${context.serno}";
	};

	function doDeleteLmtIndusListApply() {
		var paramStr = LmtIndusListApplyList._obj.getSelectedData();
		if (paramStr.length > 0) {
			if(confirm("是否确认要删除？")){
				var cus_id = new Array();
				for(var i=0;i<paramStr.length;i++){
					cus_id.push(paramStr[i].cus_id._getValue());
				}
				paramStr = '?cus_id='+cus_id+'&serno='+serno;
				var url = '<emp:url action="deleteLmtIndusListApplyRecord.do"/>'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						var flagInfo=jsonstr.flagInfo;						
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();
						}else{
							alert('所选客户已有在途业务申请，暂不能从行业名单中删除');
						}
					}
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择记录！');
		}
	};
	function returnCus(data){
		LmtIndusListApply.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIndusListApplyGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="LmtIndusListApply.cus_id" label="客户码" buttonLabel="选择" 
		url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:actButton id="getAddLmtIndusListApplyPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateLmtIndusListApplyPage" label="修改" op="update"/>
		<emp:actButton id="deleteLmtIndusListApply" label="删除" op="remove"/>
		<emp:actButton id="viewLmtIndusListApply" label="查看" op="view"/>
		<emp:actButton id="importLmtIndusListApply" label="导入" op="imports"/>
	</div>

	<emp:table icollName="LmtIndusListApplyList" selectType="2" pageMode="true" url="pageLmtIndusListApplyQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="is_do_limit" label="是否进行额度设置" dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_LIST_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    