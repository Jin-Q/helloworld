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
		LmtApply._toForm(form);
		LmtApplyList._obj.ajaxQuery(null,form);
	};
	/*--user code begin--*/
	function doViewLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			//控制 isShow=N协议中不显示冻结解冻按钮 overrule 授信否决标志
			var isShow = "${context.isShow}";
			if(""==isShow){
				isShow = "N";
			}
			var url = '<emp:url action="getLmtApplyViewPage.do"/>?'+paramStr+"&op=view&type=Y&isShow="+isShow+"&cus_id=${context.cus_id}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtApplyGroup.reset();
	};

	function onLoadMethod(){
		var overrule = '${context.overrule}';
		if("Y"==overrule){   //否决历史标志为是时隐藏复议按钮
			document.all.button_submitLmtApply.style.display="none";
			LmtApply.approve_status._obj._renderHidden(true);  //隐藏审批状态查询条件
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoadMethod()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtApplyGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtApply.serno" label="业务编号" />
			<emp:text id="LmtApply.cus_id" label="客户码" hidden="true"/>
			<emp:select id="LmtApply.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtApply" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtApplyList" pageMode="true" url="pageLmtApplyQuery.do?type=cusHis&cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    