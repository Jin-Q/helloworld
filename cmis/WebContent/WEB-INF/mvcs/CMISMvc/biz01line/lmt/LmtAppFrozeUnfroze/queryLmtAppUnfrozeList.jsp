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
		LmtAppFrozeUnfroze._toForm(form);
		LmtAppFrozeUnfrozeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppFrozeUnfrozePage() {
		var paramStr = LmtAppFrozeUnfrozeList._obj.getParamStr(['serno']);
		var limit_code = LmtAppFrozeUnfrozeList._obj.getParamValue(['limit_code']);
		var approve_status = LmtAppFrozeUnfrozeList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status == '000'){
				var url = '<emp:url action="getLmtUnfrozeUpdatePage.do"/>?'+paramStr+'&limit_code='+limit_code;
				url = EMPTools.encodeURI(url);
				window.location = url;
				}else{
					alert('只有【待发起】状态可以修改！');
					}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppFrozeUnfroze() {
		var paramStr = LmtAppFrozeUnfrozeList._obj.getParamStr(['serno']);
		var limit_code = LmtAppFrozeUnfrozeList._obj.getParamValue(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppUnfrozeViewPage.do"/>?'+paramStr+'&limit_code='+limit_code;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppFrozeUnfrozePage() {
		var url = '<emp:url action="getLmtAppUnfrozeAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	

	/*异步删除*/
	function doDeleteLmtAppFrozeUnfroze() {
		var paramStr = LmtAppFrozeUnfrozeList._obj.getParamStr(['serno']);
		var approve_status = LmtAppFrozeUnfrozeList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status == '000'){
				if(confirm("是否确认要删除？")){
					
					var url = '<emp:url action="deleteLmtAppFrozeUnfrozeRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					//window.location = url;
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
				}else{
					alert('只有【待发起】状态才可以进行删除操作！');
					}
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*异步提交*/
	function doSubmitLmtAppFrozeUnfroze(){
		var paramStr = LmtAppFrozeUnfrozeList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppFrozeUnfrozeList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "997"){
				alert("此条记录已经提交！");
			}else{
				Change4Status();
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function Change4Status() {
		
		var paramStr = LmtAppFrozeUnfrozeList._obj.getParamStr(['serno']);
		var approve_status = LmtAppFrozeUnfrozeList._obj.getSelectedData()[0].approve_status._getValue();
		
		var limit_code = LmtAppFrozeUnfrozeList._obj.getParamValue(['limit_code']);
		if (paramStr != null){
			if(confirm("是否确认要生效？")){
				var url = '<emp:url action="updateUnfrozeRecordStatus.do"/>?'+paramStr+'&approve_status='+approve_status+'&limit_code='+limit_code;
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
							alert("提交成功!");
							window.location.reload();//重载当前页面
					   }else 
						 alert("提交失败！");
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

	/**add by lisj 2015-7-23 需求编号：【XD150123005】小微自助循环贷款改造 ,修复页面BUG begin**/
	function doReset(){
		page.dataGroups.LmtAppFrozeUnfrozeGroup.reset();
	};
	/**add by lisj 2015-7-23 需求编号：【XD150123005】小微自助循环贷款改造 ,修复页面BUG end**/
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppFrozeUnfroze.serno" label="申请编号" />
			<emp:text id="LmtAppFrozeUnfroze.agr_no" label="授信协议编号" />
			<emp:text id="LmtAppFrozeUnfroze.limit_code" label="授信额度编号" />
			<emp:select id="LmtAppFrozeUnfroze.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtAppFrozeUnfrozePage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtAppFrozeUnfrozePage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppFrozeUnfroze" label="删除" op="remove"/>
		<emp:button id="viewLmtAppFrozeUnfroze" label="查看" op="view"/>
		<emp:button id="submitLmtAppFrozeUnfroze" label="提交" op="submit"/>
	</div>

	<emp:table icollName="LmtAppFrozeUnfrozeList" pageMode="true" url="pageLmtAppFrozeUnfrozeQuery.do">
		<emp:text id="serno" label="申请编号" />
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:select id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:select id="cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="froze_unfroze_amt" label="解冻金额" dataType="Currency"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="over_date" label="解冻日期"  />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    