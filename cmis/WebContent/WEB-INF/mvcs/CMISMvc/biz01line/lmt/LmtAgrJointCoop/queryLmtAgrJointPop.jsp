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
		LmtAgrJointCoop._toForm(form);
		LmtAgrJointCoopList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAgrJointCoopGroup.reset();
	};
	
	/*--user code begin--*/
	function doViewLmtAgrJointCoop() {
		var paramStr = LmtAgrJointCoopList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrJointCoopViewPage.do"/>?'+paramStr + "&view=yes&openType=pop&menuId=unit_team_crd_agr";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		//	window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	//返回方法
	function doReturnMethod(){
		var data = LmtAgrJointCoopList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function returnCus(data){
		LmtAgrJointCoop.cus_id._setValue(data.cus_id._getValue());
		LmtAgrJointCoop.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="LmtAgrJointCoopGroup" title="输入查询条件" maxColumn="3">
		<emp:pop id="LmtAgrJointCoop.cus_id_displayname" label="组长客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
		<emp:text id="LmtAgrJointCoop.agr_no" label="协议号" />
		<emp:text id="LmtAgrJointCoop.cus_id" label="组长客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:returnButton id="returnMethod" label="选择返回"/>
		<emp:button id="viewLmtAgrJointCoop" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAgrJointCoopList" pageMode="true" url="pageLmtAgrJointPop.do?condition=${context.condition}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="agr_no" label="协议号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="coop_type" label="合作方类型 " dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="share_range" label="共享范围" dictname="STD_SHARED_SCOPE" hidden="true"/>
		<emp:text id="belg_org" label="所属机构" hidden="true"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任人机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS"/>
	</emp:table>
	<div align="left">
		<emp:returnButton id="returnMethod" label="选择返回"/>
	</div>
</body>
</html>
</emp:page>
    