<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
//	String app_type = context.getDataValue("app_type").toString();
%>

	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppDetails._toForm(form);
		LmtAppDetailsList._obj.ajaxQuery(null,form);
	};

	//设置成员额度
	function doGetUpdateLmtApplyPage() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno','cus_id','lmt_type']);
		if (paramStr != null) {
			var group_serno = '${context.serno}';  //集团授信流水号

			var lmt_type = LmtApplyList._obj.getParamValue(['lmt_type']);
			var url = "";
			if("" != lmt_type){
				url = '<emp:url action="getLmtApplyUpdatePage.do"/>?'+paramStr+"&lmt_type="+lmt_type+"&menuId=corp_crd_apply&op=update&isShow=N"; //控制 isShow=Y协议中不显示冻结解冻按钮
			}else{
				url = '<emp:url action="getLmtGrpMemberLeadPage.do"/>?group_serno='+group_serno+"&"+paramStr;
			}
			//var url = '<emp:url action="getLmtApplyUpdatePage.do"/>?'+paramStr+"&menuId=corp_crd_apply&op=update&isShow=N"; //控制 isShow=Y协议中不显示冻结解冻按钮
			//var url = '<emp:url action="getLmtApplyUpdatePage.do"/>?'+paramStr+"&menuId=corp_crd_apply&op=update&isShow=N&type=surp"; //控制 isShow=Y协议中不显示冻结解冻按钮
			//var url = '<emp:url action="getLmtGrpMemberLeadPage.do"/>?group_serno='+group_serno+"&"+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//变更条线
	function doGetUpdateLineLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno','cus_id','lmt_type']);
		if (paramStr != null) {
			var group_serno = '${context.serno}';  //集团授信流水号
			
			var lmt_type = LmtApplyList._obj.getParamValue(['lmt_type']);
			var url = '<emp:url action="getLmtGrpMemberLeadPage.do"/>?group_serno='+group_serno+"&"+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看
	function doViewLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtApplyViewPage.do"/>?'+paramStr+"&menuId=corp_crd_apply&op=view&isShow=N&restrict_tab=Y&showButton=N";  //控制 isShow=Y协议中不显示冻结解冻按钮
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppDetailsGroup.reset();
	};
	
	/*--user code begin--*/
	//查看协议
	function doViewLmtAgrInfo() {
		var paramStr = LmtAgrInfoList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrInfoViewPage.do"/>?'+paramStr+"&menuId=crd_agr&type=surp";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		//	window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewCusInfo(){
		var selObj = LmtApplyList._obj.getSelectedData()[0];
		var cus_id=selObj.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
		window.open(url,'viewCusInfo','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>

	<div class='emp_gridlayout_title'>集团成员原授信协议&nbsp;</div>
	<div align="left">
		<emp:button id="viewLmtAgrInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAgrInfoList" pageMode="false" url="">
		<emp:text id="agr_no" label="成员协议编号" />
		<emp:text id="grp_agr_no" label="集团协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="biz_type" label="授信业务类型" dictname="STD_ZB_BIZ_TYPE"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
	</emp:table>
	<br>
	<div class='emp_gridlayout_title'>集团成员授信申请&nbsp;</div>
	<div align="left">
		<emp:actButton id="getUpdateLmtApplyPage" label="设置额度" op="update"/>
		<%--<emp:actButton id="getUpdateLineLmtApply" label="变更条线" op="updateline"/>--%>
		<emp:actButton id="viewLmtApply" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtApplyList" pageMode="false" url="">
		<emp:text id="serno" label="业务流水号" />		
		<emp:link id="cus_id" label="客户码" operation="viewCusInfo"/>
		<emp:link id="cus_id_displayname" label="客户名称" operation="viewCusInfo"/>
		<emp:text id="biz_type" label="授信业务类型" dictname="STD_ZB_BIZ_TYPE"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE"/>
		<emp:text id="lmt_type" label="授信类别" dictname="STD_ZX_LMT_PRD" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    