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
		ArpDbtWriteoffApp._toForm(form);
		ArpDbtWriteoffAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpDbtWriteoffAppPage() {
		var paramStr = ArpDbtWriteoffAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpDbtWriteoffAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getArpDbtWriteoffAppUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpDbtWriteoffApp() {
		var paramStr = ArpDbtWriteoffAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtWriteoffAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpDbtWriteoffAppPage() {
		var url = '<emp:url action="getArpDbtWriteoffAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpDbtWriteoffApp() {
		var paramStr = ArpDbtWriteoffAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpDbtWriteoffAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="deleteArpDbtWriteoffAppRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpDbtWriteoffAppGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		ArpDbtWriteoffApp.cus_id._setValue(data.cus_id._getValue());
    };
    function doSumbitArpDbtWriteoffApp(){
    	var paramStr = ArpDbtWriteoffAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			qnt = ArpDbtWriteoffAppList._obj.getParamValue(['nums']); //笔数
			if(qnt == '0'){
				alert("请先录入呆账明细！");
				return false;
			}
			alert("实际核销金额以核心系统为准！");
			var _status = ArpDbtWriteoffAppList._obj.getParamValue(['approve_status']);
			cus_id = ArpDbtWriteoffAppList._obj.getParamValue(['cus_id']);//客户码
			cus_name = ArpDbtWriteoffAppList._obj.getParamValue(['cus_id_displayname']);//客户名称
			amt = ArpDbtWriteoffAppList._obj.getParamValue(['loan_balance_sum']);//展期金额
			WfiJoin.table_name._setValue("ArpDbtWriteoffApp");					
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("026");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("呆账核销申请流程");
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

	<emp:gridLayout id="ArpDbtWriteoffAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpDbtWriteoffApp.serno" label="业务编号" />
			<emp:pop id="ArpDbtWriteoffApp.cus_id" label="客户码"  url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpDbtWriteoffAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpDbtWriteoffAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpDbtWriteoffApp" label="删除" op="remove"/>
		<emp:button id="viewArpDbtWriteoffApp" label="查看" op="view"/>
		<emp:button id="sumbitArpDbtWriteoffApp" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="ArpDbtWriteoffAppList" pageMode="true" url="pageArpDbtWriteoffAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="nums" label="笔数" dataType="Int" defvalue="0"/>
		<emp:text id="loan_balance_sum" label="贷款余额" dataType="Currency" defvalue="0"/>
		<emp:text id="owe_int_sum" label="欠息累计" dataType="Currency" defvalue="0"/>
		<emp:text id="writeoff_cap_sum" label="核销本金" dataType="Currency" defvalue="0"/>
		<emp:text id="writeoff_int_sum" label="核销利息" dataType="Currency" defvalue="0"/>
		<emp:text id="writeoff_amt_sum" label="核销总金额" dataType="Currency" defvalue="0"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>