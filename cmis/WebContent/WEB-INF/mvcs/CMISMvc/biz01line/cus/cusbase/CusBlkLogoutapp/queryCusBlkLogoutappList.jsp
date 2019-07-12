<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBlkLogoutapp._toForm(form);
		CusBlkLogoutappList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusBlkLogoutappPage() {
		var paramStr = CusBlkLogoutappList._obj.getParamStr(['serno']);
		if (paramStr != null) {
		    var status = CusBlkLogoutappList._obj.getParamValue('approve_status');
            if(status != '000'&& status != '991'&& status != '992'&& status != '993') {
                alert("该申请所处状态不是【待发起】、【追回】、【打回】不能修改！");
                return false;
            }
			var url = '<emp:url action="getCusBlkLogoutappUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusBlkLogoutapp() {
		var paramStr = CusBlkLogoutappList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusBlkLogoutappViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusBlkLogoutappPage() {
		var url = '<emp:url action="getCusBlkLogoutappAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusBlkLogoutapp() {
		var paramStr = CusBlkLogoutappList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = CusBlkLogoutappList._obj.getParamValue('approve_status');
			if(status != '000') {
			    alert("该申请所处状态不是【待发起】不能删除！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusBlkLogoutappRecord.do"/>?'+paramStr;
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
		page.dataGroups.CusBlkLogoutappGroup.reset();
	};

    //提交流程
	function doSumbitCusBlk(){
		var paramStr = CusBlkLogoutappList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = CusBlkLogoutappList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = CusBlkLogoutappList._obj.getParamValue(['cus_name']);//客户名称
			var approve_status = CusBlkLogoutappList._obj.getParamValue(['approve_status']);//流程审批状态
			WfiJoin.table_name._setValue("CusBlkLogoutapp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("010");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("共享客户注销申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusBlkLogoutappGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="CusBlkLogoutapp.serno" label="业务流水号"/>
		<emp:text id="CusBlkLogoutapp.cus_id" label="客户码" />	
		<emp:text id="CusBlkLogoutapp.cus_name" label="客户名称" />
		<emp:text id="CusBlkLogoutapp.cert_code" label="证件号码" />	
		<emp:select id="CusBlkLogoutapp.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:select id="CusBlkLogoutapp.approve_status" label="审批状态" dictname="WF_APP_STATUS" />
		<emp:text id="CusBlkLogoutapp.legal_name" label="法定代表人" />	
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusBlkLogoutappPage" label="新增" op="add"/>
		<emp:button id="viewCusBlkLogoutapp" label="查看" op="view"/>
		<emp:button id="getUpdateCusBlkLogoutappPage" label="修改" op="update"/>
		<emp:button id="deleteCusBlkLogoutapp" label="删除" op="remove"/>
		<emp:button id="sumbitCusBlk" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="CusBlkLogoutappList" pageMode="true" url="pageCusBlkLogoutappQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="black_level" label="不宜贷款户级别" dictname="STD_ZB_BLACKLIST_TYP" hidden="true"/>
		<emp:text id="black_date" label="登记日期" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id" label="登记(受理人)" hidden="true"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    