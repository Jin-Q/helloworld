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
		LmtIndusAuthApply._toForm(form);
		LmtIndusAuthApplyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtIndusAuthApplyPage() {
		var paramStr = LmtIndusAuthApplyList._obj.getParamStr(['serno','input_br_id','guar_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtIndusAuthApplyUpdatePage.do"/>?'+paramStr+condition;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtIndusAuthApply() {
		var paramStr = LmtIndusAuthApplyList._obj.getParamStr(['serno','input_br_id','guar_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtIndusAuthApplyViewPage.do"/>?'+paramStr+condition;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtIndusAuthApplyPage() {
		var url = '<emp:url action="getLmtIndusAuthApplyAddPage.do"/>?serno='+serno+condition;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtIndusAuthApply() {
		var paramStr = LmtIndusAuthApplyList._obj.getParamStr(['serno','input_br_id','guar_type']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtIndusAuthApplyRecord.do"/>?'+paramStr;
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
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtIndusAuthApplyGroup.reset();
	};
	
	/*--user code begin--*/
	function getOrgID(data){
		LmtIndusAuthApply.input_br_id._setValue(data.organno._getValue());
		document.all.input_br_id_displayname.value = data.organname._getValue();
	};
	function doLoad(){
		serno = "${context.serno}";
		action = "${context.action}";
		single_amt = "${context.single_amt}";
		change_list_flag = "${context.change_list_flag}";
		condition = '&action='+action+'&change_list_flag='+change_list_flag+'&single_amt='+single_amt;
		if(action == 'view' || change_list_flag == '1'){
			document.getElementById('button_getAddLmtIndusAuthApplyPage').style.display = 'none';
			document.getElementById('button_getUpdateLmtIndusAuthApplyPage').style.display = 'none';
			document.getElementById('button_deleteLmtIndusAuthApply').style.display = 'none';
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIndusAuthApplyGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtIndusAuthApply.serno" label="业务编号" hidden="true" defvalue="${context.serno}"/>
		<emp:text id="LmtIndusAuthApply.input_br_id" label="申请机构" hidden="true" />
		<emp:pop id="input_br_id_displayname" label="申请机构"  required="true"  
		url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />				
		<emp:select id="LmtIndusAuthApply.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtIndusAuthApplyPage" label="新增" />
		<emp:button id="getUpdateLmtIndusAuthApplyPage" label="修改" />
		<emp:button id="deleteLmtIndusAuthApply" label="删除" />
		<emp:button id="viewLmtIndusAuthApply" label="查看" />
	</div>

	<emp:table icollName="LmtIndusAuthApplyList" pageMode="true" url="pageLmtIndusAuthApplyQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="input_br_id_displayname" label="申请机构" />
		<emp:text id="input_br_id" label="申请机构" hidden="true"/>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="single_auth_amt" label="单户授权金额(元)" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_DRFPO_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    