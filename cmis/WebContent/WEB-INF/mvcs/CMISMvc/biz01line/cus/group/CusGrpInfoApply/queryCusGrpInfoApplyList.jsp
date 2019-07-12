<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function doGetAddCusGrpInfoApplyPage(){
		var url;
		if(menuId == 'grpCognizChg'){
			url = '<emp:url action="getCusGrpInfoApplyModAddPage.do"/>';
		}else{
			url = '<emp:url action="getCusGrpInfoApplyAddPage.do"/>';
		}		
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusGrpInfoApply(){
		var paramStr = CusGrpInfoApplyList._obj.getParamStr(['grp_no','serno']);
		if (paramStr != null) {
			var status = CusGrpInfoApplyList._obj.getParamValue('approve_status');
			if(status != '000' ){
			    alert("该记录已提交审批！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusGrpInfoApplyRecord.do"/>?'+paramStr;
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
	
	function doGetUpdateCusGrpInfoApplyPage(){
		var paramStr = CusGrpInfoApplyList._obj.getParamStr(['grp_no','serno']);
		if (paramStr != null) {
			var status = CusGrpInfoApplyList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getCusGrpInfoApplyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGrpInfoApply(){
		var paramStr = CusGrpInfoApplyList._obj.getParamStr(['grp_no','serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryCusGrpInfoApplyDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusGrpInfoApply._toForm(form);
		CusGrpInfoApplyList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CusGrpInfoApplyGroup.reset();
	};

	function checkCusid(cus_id){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			doSumbitFlow(cus_id);
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="checkCusidApplyed.do"/>?type=singleSubmit&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};

	function doSumbitCusCognizApply(){
		var paramStr = CusGrpInfoApplyList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			checkCusid(paramStr);
		}else {
			alert('请先选择一条记录！');
		}
	};

	function doSumbitFlow(paramStr){
		var grp_name = CusGrpInfoApplyList._obj.getParamValue(['grp_name']);
		var grp_finance_type = CusGrpInfoApplyList._obj.getParamValue(['grp_finance_type']);
		if(grp_name==''||grp_finance_type==''){
			alert('信息不完整，请填写必输项');
			return;
		}	
		var _status = CusGrpInfoApplyList._obj.getParamValue(['approve_status']);
		var cus_id = CusGrpInfoApplyList._obj.getParamValue(['grp_no']);//集团编号
		var cus_name = CusGrpInfoApplyList._obj.getParamValue(['grp_name']);//集团名称
		var is_change = CusGrpInfoApplyList._obj.getParamValue(['is_change']);//是否变更
		WfiJoin.table_name._setValue("CusGrpInfoApply");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(paramStr);
		WfiJoin.wfi_status._setValue(_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		if(is_change == '1'){
			WfiJoin.appl_type._setValue("014");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.prd_name._setValue("集团客户变更申请");
		}else{
			WfiJoin.appl_type._setValue("008");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.prd_name._setValue("集团客户认定申请");
		}			
		initWFSubmit(false);
    };
    
	function returnCus(data){
		CusGrpInfoApply.parent_cus_id._setValue(data.cus_id._getValue());
    };
	//主管客户经理
	function setconId(data){
		CusGrpInfoApply.manager_id._setValue(data.actorno._getValue());
		CusGrpInfoApply.manager_id_displayname._setValue(data.actorname._getValue());
	};
	function doLoad(){
		menuId = "${context.menuId}";
		if(menuId == 'grpCognizHis'){
			CusGrpInfoApply.manager_id_displayname._obj._renderHidden(false);
		}
	};
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" onload="doLoad()">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusGrpInfoApplyGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusGrpInfoApply.serno" label="申请流水号" />
		<emp:pop id="CusGrpInfoApply.parent_cus_id" label="主(集团)客户码"  
		buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
		<emp:text id="CusGrpInfoApply.grp_no" label="关联(集团)编号" />
		<emp:text id="CusGrpInfoApply.grp_name" label="关联(集团)名称" />
		<emp:text id="CusGrpInfoApply.grp_member_name" label="关联(集团)成员名称" />
		<emp:pop id="CusGrpInfoApply.manager_id_displayname" label="主管客户经理" 
		url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" hidden="true"/>
		<emp:text id="CusGrpInfoApply.manager_id" label="主管客户经理编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddCusGrpInfoApplyPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusGrpInfoApplyPage" label="修改" op="update"/>
		<emp:button id="deleteCusGrpInfoApply" label="删除" op="remove"/>
		<emp:button id="viewCusGrpInfoApply" label="查看" op="view"/>
		<emp:button id="sumbitCusCognizApply" label="提交" op = "startFlow"/>
	</div>
	<emp:table icollName="CusGrpInfoApplyList" pageMode="true" url="pageCusGrpInfoApplyQuery.do" >
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="grp_no" label="关联(集团)编号" />
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主申请(集团)客户码" />
		<emp:text id="parent_cus_id_displayname" label="主申请(集团)名称" />
		<emp:text id="is_change" label="是否变更" dictname="STD_ZX_YES_NO"/>
		<emp:text id="manager_id" label="主办客户经理" hidden="true"/>
		<emp:text id="manager_br_id" label="主办行" hidden="true"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="grp_finance_type" label="关联(集团)融资形式" hidden="true" />
	</emp:table>
</body>
</html>
</emp:page>