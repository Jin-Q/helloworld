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
		CtrLimitCont._toForm(form);
		CtrLimitContList._obj.ajaxQuery(null,form);
	};
	
	function doOrderCtrLimitCont() {
		var paramStr = CtrLimitContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var contStatus = CtrLimitContList._obj.getSelectedData()[0].cont_status._getValue();
			if(contStatus == '100'){
				var url = '<emp:url action="getCtrLimitContUpdatePage.do"/>?iqpFlowHis=have&op=view&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else {
				alert("只有未生效的合同才允许签订操作！");
				return;
			}
			
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrLimitCont() {
		var paramStr = CtrLimitContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrLimitContViewPage.do"/>?iqpFlowHis=have&op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	
	function doReset(){
		page.dataGroups.CtrLimitContGroup.reset();
	};

	function returnCus(data){
		CtrLimitCont.cus_id._setValue(data.cus_id._getValue());
		CtrLimitCont.cus_name._setValue(data.cus_name._getValue());
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CtrLimitContGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CtrLimitCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLimitCont.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=main_br_id='${context.organNo}'&returnMethod=returnCus" />
			<emp:select id="CtrLimitCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
			<emp:text id="CtrLimitCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="orderCtrLimitCont" label="签订" op="update"/>
		<emp:button id="viewCtrLimitCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrLimitContList" pageMode="true" url="pageCtrLimitContQuery.do">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_cn" label="中文合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />		
		<emp:text id="memo" label="备注"  hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    