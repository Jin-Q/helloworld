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
		LmtAppJointCoopRedi._toForm(form);
		LmtAppJointCoopRediList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewLmtAppJointCoopRedi() {
		var paramStr = LmtAppJointCoopRediList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppCoopRediPage.do"/>?'+paramStr+"&menuId=LmtAppJointCoopRedi&op=view&isShow=Y&showButton=N";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	//异步删除
	function doDeleteLmtAppJointCoopRedi(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					window.location.reload();
				}else{
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var paramStr = LmtAppJointCoopRediList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppJointCoopRediList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){
				if(confirm("该操作不可恢复，是否确认要删除？")){
					var url = '<emp:url action="deleteLmtRediApplyRecord.do"/>?'+paramStr+'&type=coop';
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
		
	};
	
	function doReset(){
		page.dataGroups.LmtAppJointCoopGroup.reset();
	};
	
	
	//提交流程
	//var url = '<emp:url action="submitLmtApply.do"/>?'+paramStr;
	function doSubmitLmtAppJointCoopRedi(){
		var paramStr = LmtAppJointCoopRediList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = LmtAppJointCoopRediList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = LmtAppJointCoopRediList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var crd_totl_amt = LmtAppJointCoopRediList._obj.getSelectedData()[0].lmt_totl_amt._getValue();
			var approve_status = LmtAppJointCoopRediList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("LmtAppJointCoopRedi");
			WfiJoin.prd_name._setValue("联保小组授信复议申请");
			WfiJoin.appl_type._setValue("3241");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue(crd_totl_amt);//金额
			
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
		<emp:gridLayout id="LmtAppJointCoopGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppJointCoopRedi.serno" label="业务编号" />
			<emp:text id="LmtAppJointCoopRedi.cus_id" label="组长客户码" />
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="deleteLmtAppJointCoopRedi" label="删除" op="remove"/>
		<emp:button id="viewLmtAppJointCoopRedi" label="查看" op="view"/>
		<emp:button id="submitLmtAppJointCoopRedi" label="提交" op="update"/>
	</div>

	<emp:table icollName="LmtAppJointCoopRediList" pageMode="true" url="pageLmtAppJointCoopRediQuery.do" >
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="组长客户码" />
		<emp:text id="cus_id_displayname" label="组长客户名称" />
		<emp:text id="coop_type" label="类别" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    