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
		ArpDbtCongnizApp._toForm(form);
		ArpDbtCongnizAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpDbtCongnizAppPage() {
		var paramStr = ArpDbtCongnizAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpDbtCongnizAppList._obj.getParamValue('approve_status');
			if(status != '000' && status != '992' && status!= '991'&& status!= '993'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="getArpDbtCongnizAppUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpDbtCongnizApp() {
		var paramStr = ArpDbtCongnizAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDbtCongnizAppViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpDbtCongnizAppPage() {
		var url = '<emp:url action="getArpDbtCongnizAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpDbtCongnizApp() {
		var paramStr = ArpDbtCongnizAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = ArpDbtCongnizAppList._obj.getParamValue('approve_status');
			if(status != '000'){
			    alert("该记录已提交审批！");
			    return ;
			}
			var url = '<emp:url action="deleteArpDbtCongnizAppRecord.do"/>?'+paramStr;
			doPubDelete(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpDbtCongnizAppGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		ArpDbtCongnizApp.cus_id._setValue(data.cus_id._getValue());
    };
    
	function doSumbitArpDbtCongnizApp(){
		var paramStr = ArpDbtCongnizAppList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			qnt = ArpDbtCongnizAppList._obj.getParamValue(['qnt']); //笔数
			if(qnt == '0'){
				alert("请先录入呆账明细！");
				return false;
			}
			
			var _status = ArpDbtCongnizAppList._obj.getParamValue(['approve_status']);
			cus_id = ArpDbtCongnizAppList._obj.getParamValue(['cus_id']);//客户码
			cus_name = ArpDbtCongnizAppList._obj.getParamValue(['cus_id_displayname']);//客户名称
			amt = ArpDbtCongnizAppList._obj.getParamValue(['loan_balance']);//展期金额
			WfiJoin.table_name._setValue("ArpDbtCongnizApp");					
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("025");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("呆账认定申请流程");
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

	<emp:gridLayout id="ArpDbtCongnizAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpDbtCongnizApp.serno" label="业务编号" />
			<emp:pop id="ArpDbtCongnizApp.cus_id" label="客户码"  url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddArpDbtCongnizAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateArpDbtCongnizAppPage" label="修改" op="update"/>
		<emp:button id="deleteArpDbtCongnizApp" label="删除" op="remove"/>
		<emp:button id="viewArpDbtCongnizApp" label="查看" op="view"/>
		<emp:button id="sumbitArpDbtCongnizApp" label="提交" op="startFlow"/>
	</div>

	<emp:table icollName="ArpDbtCongnizAppList" pageMode="true" url="pageArpDbtCongnizAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="qnt" label="笔数" dataType="Int" />
		<emp:text id="loan_balance" label="总贷款余额" dataType="Currency" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    