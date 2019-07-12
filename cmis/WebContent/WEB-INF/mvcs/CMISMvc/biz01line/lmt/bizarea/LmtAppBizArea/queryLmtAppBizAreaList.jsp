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
		LmtAppBizArea._toForm(form);
		LmtAppBizAreaList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppBizAreaPage() {
		var paramStr = LmtAppBizAreaList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppBizAreaList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
			    var sel = LmtAppBizAreaList._obj.getSelectedData()[0].biz_area_type._getValue();
			    var url = '<emp:url action="getLmtAppBizAreaUpdatePage.do"/>?'+paramStr + "&biz_area_type=" + sel ;
			    url = EMPTools.encodeURI(url);
			    window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppBizArea() {
		var paramStr = LmtAppBizAreaList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="queryLmtAppBizAreaDetails.do"/>?'+paramStr + "&canWr=false" ;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppBizAreaPage() {
		var url = '<emp:url action="getLmtAppBizAreaAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtAppBizArea() {
		var paramStr = LmtAppBizAreaList._obj.getParamStr(['serno','biz_area_type']);
		if (paramStr != null) {
			var appStatus = LmtAppBizAreaList._obj.getParamValue(['approve_status']);
			if(appStatus!='000'){
				alert('只有状态为待发起的申请才能删除！');
				return;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtAppBizAreaRecord.do"/>?'+paramStr;
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
						if(flag=="success"){
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
		page.dataGroups.LmtAppBizAreaGroup.reset();
	};
	
	/*--user code begin--*/
	//新增重新写
	function doAddWiz(){
		var url = '<emp:url action="getLmtAppBizAreaAddWizPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//提交流程
	function doSubm(){
		var paramStr = LmtAppBizAreaList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var biz_area_name = LmtAppBizAreaList._obj.getParamValue(['biz_area_name']);//圈商名称
			var lmt_totl_amt = LmtAppBizAreaList._obj.getParamValue(['lmt_totl_amt']);//授信总额
			var _status = LmtAppBizAreaList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("LmtAppBizArea");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3251");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
		//	WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(biz_area_name);
			WfiJoin.amt._setValue(lmt_totl_amt);
			WfiJoin.prd_name._setValue("圈商准入申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppBizAreaGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAppBizArea.serno" label="申请编号" />
		<emp:text id="LmtAppBizArea.biz_area_name" label="圈商名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="addWiz" label="新增" op="add"/>
		<emp:button id="getUpdateLmtAppBizAreaPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppBizArea" label="删除" op="remove"/>
		<emp:button id="viewLmtAppBizArea" label="查看" op="view"/>
		<emp:button id="subm" label="提交" op="subm"/>
	</div>

	<emp:table icollName="LmtAppBizAreaList" pageMode="true" url="pageLmtAppBizAreaQuery.do">
		<emp:text id="serno" label="申请编号" />
		<emp:text id="biz_area_name" label="圈商名称" />
		<emp:text id="biz_area_type" label="圈商类型" dictname="STD_LMT_BIZ_AREA_TYPE" />
		<emp:text id="cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    