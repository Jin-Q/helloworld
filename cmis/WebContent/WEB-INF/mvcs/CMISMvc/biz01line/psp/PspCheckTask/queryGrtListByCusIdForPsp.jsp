<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doViewguar() {
	var paramStr = GrtMortList._obj.getParamStr(['guaranty_no']);
	if (paramStr != null) {
		var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	} else {
		alert('请先选择一条记录！');
	}
};

function doFenxiguar() {
	var paramStr = GrtMortList._obj.getParamStr(['guaranty_no']);
	if (paramStr != null) {
		var guaranty_no = GrtMortList._obj.getParamValue(['guaranty_no']);
		var task_id = '${context.task_id}' + guaranty_no;
		var cus_id = '${context.cus_id}';
		var op = '${context.op}';
		var url;
		if(op=='view'){
			url = '<emp:url action="queryPspCheckListForWin.do"/>?scheme_id=FFFA276A01CBD3827D90989AA0C031FD&task_id='+task_id+'&cus_id='+cus_id+'&op=view';
		}else{
			url = '<emp:url action="savePspCheckListForWin.do"/>?scheme_id=FFFA276A01CBD3827D90989AA0C031FD&task_id='+task_id+'&cus_id='+cus_id+'&op=update';
		}
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	} else {
		alert('请先选择一条记录！');
	}
};
/*
function doViewee() {
	var paramStr = GrtGuaranteeList._obj.getParamStr(['guar_id']);
	if (paramStr != null) {
		var guar_cont_no = GrtGuaranteeList._obj.getParamValue(['guar_cont_no']);
		var url = '<emp:url action="getGrtGuaranteeViewPage.do"/>?op=view&'+paramStr+'&flag=lmt&guar_cont_no='+guar_cont_no;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	} else {
		alert('请先选择一条记录！');
	}
};*/

//查看保证人信息
function doViewee(){
	var paramStr = GrtGuaranteeList._obj.getParamValue(['cus_id']);
	if (paramStr != null) {
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+paramStr;
		url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
}

function doFenxiee() {
	var paramStr = GrtGuaranteeList._obj.getParamStr(['cus_id']);
	if (paramStr != null) {
	//	var guar_id = GrtGuaranteeList._obj.getParamValue(['guar_id']);
	//	var task_id = '${context.task_id}' + guar_id;
		var guar_cus_id = GrtGuaranteeList._obj.getParamValue(['cus_id']);//保证人id
		var task_id = '${context.task_id}' + guar_cus_id;
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="analyGrtGuaranteeForPsp.do"/>?task_id='+task_id+'&cus_id='+cus_id+'&op=${context.op}&guar_cus_id='+guar_cus_id;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	} else {
		alert('请先选择一条记录！');
	}
};

function doChongguguar(){
	var paramStr = GrtMortList._obj.getParamStr(['guaranty_no']);
	if (paramStr != null) {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspGuarantyValueReevalUpdatePage.do"/>?'+paramStr+'&task_id='+task_id+'&cus_id='+cus_id+'&op=${context.op}';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	} else {
		alert('请先选择一条记录！');
	}
}
</script>
</head>
<body class="page_content">
	<div class='emp_gridlayout_title'>担保品信息</div>
	<emp:button id="viewguar" label="查看担保品" op="view"/>
	<emp:button id="fenxiguar" label="分析担保品" op="view"/>
	<emp:button id="chongguguar" label="重估担保品" op="view"/>
	<emp:table icollName="GrtMortList" pageMode="false" url="">
		<emp:text id="guaranty_no" label="担保品编号" />
		<emp:text id="guaranty_name" label="担保品名称" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE"/>
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="cus_id_displayname" label="出质人客户名称" />
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="is_analy" label="分析状态" dictname="STD_PSP_ANALY_STATUS"/>
	</emp:table>
	
	<!--<emp:table icollName="GrtMortList" pageMode="false" url="">
		<emp:text id="cont_no" label="合同编号" hidden="true"/>
		<emp:text id="serno" label="业务申请编号" hidden="true"/>
		<emp:text id="guar_cont_no" label="担保合同号" />
		<emp:text id="guaranty_no" label="担保品编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE"/>
		<emp:text id="guaranty_name" label="担保品名称" />
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="is_analy" label="分析状态" dictname="STD_PSP_ANALY_STATUS"/>
	</emp:table>
	--><br>
	<div class='emp_gridlayout_title'>保证人信息</div>
	<emp:button id="viewee" label="查看保证人" op="view"/>
	<emp:button id="fenxiee" label="分析保证人" op="view"/>
	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="">
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_name" label="保证人" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
		<emp:text id="is_analy" label="分析状态" dictname="STD_PSP_ANALY_STATUS"/>
	</emp:table>
	
<!-- 	
	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="">
		<emp:text id="cont_no" label="合同编号" hidden="true"/>
		<emp:text id="serno" label="业务申请编号 " hidden="true"/>
		<emp:text id="guar_cont_no" label="担保合同号" />
		<emp:text id="guar_id" label="保证编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE"/>
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_id_displayname" label="保证人" />
		<emp:text id="is_analy" label="分析状态" dictname="STD_PSP_ANALY_STATUS"/>
	</emp:table> -->
</body>
</html>
</emp:page>