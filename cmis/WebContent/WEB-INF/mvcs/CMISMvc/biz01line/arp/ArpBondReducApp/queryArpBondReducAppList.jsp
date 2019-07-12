<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpBondReducApp._toForm(form);
		ArpBondReducAppList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.ArpBondReducAppGroup.reset();
	};
	
	function doGetUpdateArpBondReducAppPage() {
		var paramStr = ArpBondReducAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpBondReducAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getArpBondReducAppUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpBondReducApp() {
		var paramStr = ArpBondReducAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBondReducAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpBondReducAppPage() {
		var url = '<emp:url action="getArpBondReducAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpBondReducApp() {
		var paramStr = ArpBondReducAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpBondReducAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="deleteArpBondReducAppRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function returnCus(data){
		ArpBondReducApp.cus_id._setValue(data.cus_id._getValue());
    };
    function doSumbitArpBondReducApp(){
    	var paramStr = ArpBondReducAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			qnt = ArpBondReducAppList._obj.getParamValue(['reduc_qnt']); //笔数
			if(qnt == '0'){
				alert("请先录入债权减免明细！");
				return false;
			}
			
			var _status = ArpBondReducAppList._obj.getParamValue(['approve_status']);
			cus_id = ArpBondReducAppList._obj.getParamValue(['cus_id']);//客户码
			cus_name = ArpBondReducAppList._obj.getParamValue(['cus_id_displayname']);//客户名称
			amt = parseFloat(ArpBondReducAppList._obj.getParamValue(['reduc_cap']))
			+ parseFloat(ArpBondReducAppList._obj.getParamValue(['reduc_int'])); //减免本金加利息
			WfiJoin.table_name._setValue("ArpBondReducApp");					
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("027");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("债权减免申请流程");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpBondReducAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpBondReducApp.serno" label="业务编号" />
			<emp:pop id="ArpBondReducApp.cus_id" label="客户码"  url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
			<emp:select id="ArpBondReducApp.reduc_type" label="减免类型" dictname="STD_ZB_REDUC_TYPE" />
			<emp:select id="ArpBondReducApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpBondReducAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpBondReducAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpBondReducApp" label="删除" op="remove"/>
		<emp:button id="viewArpBondReducApp" label="查看" op="view"/>
		<emp:button id="sumbitArpBondReducApp" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="ArpBondReducAppList" pageMode="true" url="pageArpBondReducAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="reduc_type" label="减免类型" dictname="STD_ZB_REDUC_TYPE" />
		<emp:text id="reduc_qnt" label="减免笔数" defvalue="0" dataType="Int"/>
		<emp:text id="reduc_cap" label="减免本金" defvalue="0.00" dataType="Currency"/>
		<emp:text id="reduc_int" label="减免利息" defvalue="0.00" dataType="Currency"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>