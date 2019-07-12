<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpLawMemberMana._toForm(form);
		ArpLawMemberManaList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.ArpLawMemberManaGroup.reset();
	};

	function doGetAddArpLawMemberInfoPage() {
		var url = '<emp:url action="getArpLawDefendantManaAddPage.do"/>?case_no='+case_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpLawMemberInfo_Com() {
		var paramStr = ArpLawMemberInfoComList._obj.getParamStr(['pk_serno']);
		doDeal(paramStr,'delete');
	};

	function doDeleteArpLawMemberInfo_Indiv() {
		var paramStr = ArpLawMemberInfoIndivList._obj.getParamStr(['pk_serno']);
		doDeal(paramStr,'delete');
	};

	function doViewArpLawMemberInfo_Com() {
		var paramStr = ArpLawMemberInfoComList._obj.getParamStr(['pk_serno','cus_type']);
		doDeal(paramStr,'view');
	};

	function doViewArpLawMemberInfo_Indiv() {
		var paramStr = ArpLawMemberInfoIndivList._obj.getParamStr(['pk_serno','cus_type']);
		doDeal(paramStr,'view');
	};

	function doDeal(paramStr,type){
		if (paramStr != null) {
			if(type == 'delete'){
				url = '<emp:url action="deleteArpLawMemberManaRecord.do"/>?'+paramStr;
				doPubDelete(url);
			}else{
				url = '<emp:url action="getArpLawDefendantManaViewPage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}			
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
	function doLoad(){
		case_no = "${context.case_no}";
		op = "${context.op}";
		if(1 == 1){
			document.getElementById('button_submits').style.display = 'none';
			document.getElementById('button_reset').style.display = 'none';
			ArpLawLawsuitInfo.other_indictee._obj._renderReadonly(true);
			ArpLawLawsuitInfo.indictee_status._obj._renderReadonly(true);
		}
	};
	function doSubmits(){
		doPubUpdate(ArpLawLawsuitInfo);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:form id="submitForm" action="updateArpLawLawsuitInfoRecord.do"  method="POST">

	<div class='emp_gridlayout_title'>公司客户被告人</div>
	<div align="left">
		<emp:actButton id="getAddArpLawMemberInfoPage" label="新增" op="add"/>
		<emp:actButton id="deleteArpLawMemberInfo_Com" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawMemberInfo_Com" label="查看" op="view"/>
	</div>
	<emp:table icollName="ArpLawMemberInfoComList" pageMode="false" url="">
		<emp:text id="pk_serno" label="流水号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="acu_addr_displayname" label="公司地址" />	
		<emp:text id="cus_type" label="类型" defvalue="com" hidden="true"/>
	</emp:table>
	
	<br>
	<div class='emp_gridlayout_title'>个人客户被告人</div>
	<div align="left">
		<emp:actButton id="getAddArpLawMemberInfoPage" label="新增" op="add"/>
		<emp:actButton id="deleteArpLawMemberInfo_Indiv" label="删除" op="remove"/>
		<emp:actButton id="viewArpLawMemberInfo_Indiv" label="查看" op="view"/>
	</div>
	<emp:table icollName="ArpLawMemberInfoIndivList" pageMode="false" url="" >
		<emp:text id="pk_serno" label="流水号" hidden="true" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="indiv_sex" label="性别" dictname="STD_ZX_SEX" />
		<emp:text id="indiv_ntn" label="民族" dictname="STD_ZB_NATION" />
		<emp:text id="indiv_rsd_addr_displayname" label="住址" />
		<emp:text id="indiv_dt_of_birth" label="出生日期" />
		<emp:text id="cus_type" label="类型" defvalue="indiv" hidden="true"/>
	</emp:table>
	
	<br>
	<emp:gridLayout id="ArpLawLawsuitInfoGroup"  title="其他被告信息" maxColumn="2">
		<emp:text id="ArpLawLawsuitInfo.case_no" label="业务编号" maxlength="40" required="true" hidden="true" colSpan="2"/>
		<emp:textarea id="ArpLawLawsuitInfo.other_indictee" label="其他被告" maxlength="250" required="false" colSpan="2" />
		<emp:textarea id="ArpLawLawsuitInfo.indictee_status" label="被告现状" maxlength="250" required="false" colSpan="2" />
	</emp:gridLayout>
	<div align="center">
			<br>
			<emp:button id="submits" label="保存" />
			<emp:button id="reset" label="重置"/>
	</div>
	</emp:form>
</body>
</html>
</emp:page>
    