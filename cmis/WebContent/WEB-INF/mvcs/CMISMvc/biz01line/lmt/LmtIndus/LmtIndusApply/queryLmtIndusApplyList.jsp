<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIndusApply._toForm(form);
		LmtIndusApplyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtIndusApplyPage() {

		var url;
		var paramStr = LmtIndusApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = LmtIndusApplyList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			paramStr = paramStr + "&action=update&op=update";
			url = '<emp:url action="getLmtIndusApplyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtIndusApply() {
		var paramStr = LmtIndusApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + "&action=view&op=view";
			var url = '<emp:url action="getLmtIndusApplyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtIndusApplyPage() {
		var url;
		if(menuId == 'indus_crd_change'){
			url = '<emp:url action="changeLmtIndusApplyLead.do"/>';
		}else{
			url = '<emp:url action="getLmtIndusApplyAddPage.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtIndusApply() {
		var paramStr = LmtIndusApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = LmtIndusApplyList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtIndusApplyRecord.do"/>?'+paramStr;
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtIndusApplyGroup.reset();
	};
	
	/*--user code begin--*/
	function getOrgID(data){
		LmtIndusApply.belg_org._setValue(data.organno._getValue());
		LmtIndusApply.belg_org_displayname._setValue(data.organname._getValue());
	};
	function doSubmitLmtIndusApply(){
		var is_list_mana = LmtIndusApplyList._obj.getParamValue('is_list_mana');
		if(is_list_mana == '1'){
			checkListMana();
		}else{
			dealSubmits();
		}		
	};
	//是否名单制管理校验
	function checkListMana(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;	
				if(flag != "success"){
					dealSubmits();
				}else {
					alert("名单制管理下必须填写名单信息,请确认!");
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
		var url="<emp:url action='checkUniqueType.do'/>&type=listMana&value="+LmtIndusApplyList._obj.getParamValue('serno');
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};
	function dealSubmits(){
		var paramStr = LmtIndusApplyList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var approve_status = LmtIndusApplyList._obj.getParamValue(['approve_status']);
			var change_list_flag = LmtIndusApplyList._obj.getParamValue(['change_list_flag']);
			WfiJoin.table_name._setValue("LmtIndusApply");					
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			if( change_list_flag == '1' ){
				WfiJoin.appl_type._setValue("020");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
				WfiJoin.prd_name._setValue("行业授信名单变更");
			}else{
				WfiJoin.appl_type._setValue("015");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
				WfiJoin.prd_name._setValue("行业授信申请");
			}
			
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	function doLoad(){
		menuId = "${context.menuId}";
	};

	/**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin**/
	function doPrintAP(){
		var paramStr = LmtIndusApplyList._obj.getParamStr(['serno']);
		if(paramStr != null) {
			var approve_status  = LmtIndusApplyList._obj.getParamValue(['approve_status']);
			if(approve_status == "997"){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=lmt/rcapprovalopinion.raq&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert('仅有审批状态为【通过】的授信审批才允许打印！');
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	/**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end**/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIndusApplyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtIndusApply.serno" label="业务编号" />
			<emp:select id="LmtIndusApply.indus_type" label="行业分类" dictname="STD_ZB_INDUS_TYPE" />
			<emp:select id="LmtIndusApply.shared_scope" label="共享范围" dictname="STD_SHARED_SCOPE" />
			<emp:select id="LmtIndusApply.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtIndusApplyPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtIndusApplyPage" label="修改" op="update"/>
		<emp:button id="deleteLmtIndusApply" label="删除" op="remove"/>
		<emp:button id="viewLmtIndusApply" label="查看" op="view"/>
		<emp:button id="submitLmtIndusApply" label="提交" op="startFlow"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin -->
		<emp:button id="printAP" label="审批意见打印" op="printAP"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end -->
	</div>

	<emp:table icollName="LmtIndusApplyList" pageMode="true" url="pageLmtIndusApplyQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="indus_type" label="行业分类" dictname="STD_ZB_INDUS_TYPE" />
		<emp:text id="shared_scope" label="共享范围" dictname="STD_SHARED_SCOPE" />
		<emp:text id="apply_type" label="申请类型"  dictname="STD_ZB_LMT_APPLY_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="indus_amt" label="行业总额"  dataType="Currency" />
		<emp:text id="single_amt" label="单户限额"  dataType="Currency" />		
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="is_list_mana" label="是否名单制管理" hidden="true" />
		<emp:text id="change_list_flag" label="是否仅变更名单" hidden="true" />
	</emp:table>
	
</body>
</html>
</emp:page>
    