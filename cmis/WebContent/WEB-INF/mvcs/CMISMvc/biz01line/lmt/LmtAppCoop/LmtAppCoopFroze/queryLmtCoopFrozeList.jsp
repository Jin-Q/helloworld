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
		LmtAppJointCoop._toForm(form);
		LmtAppJointCoopList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppJointCoopPage() {
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppJointCoopList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getLmtCoopFrozenUpdatePage.do"/>?'+paramStr+"&op=view";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppJointCoop() {
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppJointCoopViewPage.do"/>?'+paramStr+"&op=view&type=froze";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppJointCoopPage() {
		var url = '<emp:url action="getLmtJointCoopLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doReset(){
		page.dataGroups.LmtApplyGroup.reset();
	};
	
	/*--user code begin--*/
	//异步删除合作方授信申请 
	function doDeleteLmtAppJointCoop() {
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno','coop_type']);
		if (paramStr != null) {
			var approve_status = LmtAppJointCoopList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status != "000"){
				alert("只有状态为'待发起'的申请才可以进行删除！");
				return ;
			}
			if(confirm("该操作会关联删除合作方信息，是否确认要删除？")){
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
				var url = '<emp:url action="deleteLmtAppJointCoopRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	//提交流程
	function doStartLmtAppJointCoop(){
		var paramStr = LmtAppJointCoopList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var app_type = LmtAppJointCoopList._obj.getParamValue(['app_type']);//申请类型
			if(app_type=='03'){//冻结
				var froze_amt = LmtAppJointCoopList._obj.getParamValue(['froze_amt']);
				WfiJoin.amt._setValue(froze_amt);//冻结金额
			}else{//解冻
				var unfroze_amt = LmtAppJointCoopList._obj.getParamValue(['unfroze_amt']);
				WfiJoin.amt._setValue(unfroze_amt);//解冻金额
			}
			var cus_id = LmtAppJointCoopList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = LmtAppJointCoopList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var approve_status = LmtAppJointCoopList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("LmtAppJointCoop");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3232");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.prd_name._setValue("合作方额度申请");//产品名称
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
	<emp:gridLayout id="LmtApplyGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="LmtAppJointCoop.serno" label="业务编号" />
		<emp:text id="LmtAppJointCoop.cus_id" label="合作方客户码" />
		<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
	<!-- 	<emp:button id="getAddLmtAppJointCoopPage" label="新增" op="add"/> -->
		<emp:button id="getUpdateLmtAppJointCoopPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppJointCoop" label="删除" op="remove"/>
		<emp:button id="viewLmtAppJointCoop" label="查看" op="view"/>
		<emp:button id="startLmtAppJointCoop" label="提交" op="subm"/>
	</div>
	<emp:table icollName="LmtAppJointCoopList" url="pageLmtAppJointCoopQuery.do" reqParams="type=${context.type}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="合作方客户码" />
		<emp:text id="cus_id_displayname" label="合作方客户名称" />
		<emp:text id="coop_type" label="合作方类型" dictname="STD_ZB_COOP_TYPE" />
		<emp:select id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="froze_amt" label="冻结金额" hidden="true"/>
		<emp:text id="unfroze_amt" label="解冻金额" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="flow_type" label="流程类型" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>