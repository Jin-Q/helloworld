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
		LmtAgrDetails._toForm(form);
		LmtAgrDetailsList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAgrDetailsPage() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAgrDetails() {
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger&op=view&main_menuId=${context.menuId}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		LmtAgrDetails.cus_id._setValue(data.cus_id._getValue());
		LmtAgrDetails.cus_id_displayname._setValue(data.cus_name._getValue());
    };
  	
    /*根据授信台账获取信息弹出Pop*/
    function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = LmtAgrDetailsList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAgrDetailsGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="LmtAgrDetails.agr_no" label="授信协议编号" />
		<emp:pop id="LmtAgrDetails.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:select id="LmtAgrDetails.lmt_status" label="额度状态" dictname="STD_LMT_STATUS" />
		<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号" />
		<emp:select id="LmtAgrDetails.sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:select id="LmtAgrDetails.limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="LmtAgrDetails.cus_id" label="客户码" hidden="true"/>
		
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="true" url="pageLmtAppFrozeUnfrozePopQuery.do?subConndition=${context.subConndition}">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="limit_name" label="额度品种名称" hidden="true" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="prd_id" label="适用产品编号" hidden="true"/>
		<emp:text id="froze_amt_hq" label="总部冻结金额" dataType="Currency" hidden="true"/>		
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
	</emp:table>
	<emp:returnButton label="确定"/>
	
</body>
</html>
</emp:page>
    