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
		CusSameOrg._toForm(form);
		CusSameOrgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusSameOrgPage() {
		var paramStr = CusSameOrgList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusSameOrgUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		/*	var form=document.getElementById('submitForm');
			var same_org_no=CusSameOrgList._obj.getSelectedData()[0].same_org_no._getValue();
			id2Form(form,'same_org_no',same_org_no);
			form.action="getCusSameOrgUpdatePage.do";
			form.submit();*/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusSameOrg() {
		var paramStr = CusSameOrgList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusSameOrgViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		/*	var form=document.getElementById('submitForm');
			var same_org_no=CusSameOrgList._obj.getSelectedData()[0].same_org_no._getValue();
			id2Form(form,'same_org_no',same_org_no);
			form.action="getCusSameOrgViewPage.do";
			form.submit();*/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusSameOrgPage() {
		var url = '<emp:url action="getCusSameOrgAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doReset(){
		page.dataGroups.CusSameOrgGroup.reset();
	};
	
	function doExcelSDuty(){
	/*	var same_org_cnname = document.getElementById('CusSameOrg.same_org_cnname').value;
		var same_org_no = document.getElementById('CusSameOrg.same_org_no').value;
		var swift_no = document.getElementById('CusSameOrg.swift_no').value
		var crd_grade = document.getElementById('CusSameOrg.crd_grade').value
		var url = '<emp:url action="expBatchToExcel.do"/>'+'?same_org_cnname='
		+same_org_cnname+'&same_org_no='+same_org_no+'&swift_no='+swift_no+'&crd_grade='+crd_grade;
		url = EMPTools.encodeURI(url);
		window.location=url;*/
		var form = document.getElementById("queryForm");
		CusSameOrg._toForm(form);
		form.submit();
	}

	/**add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 begin**/
	//下载导入模板
	function doDownLoadSameCusBaseInfoTemplate(){
		var url = '<emp:url action="downLoadSameCusBaseInfoTmplate.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	//导入EXCEL
    function doImportSameCusBaseInfoByExcel(){
    	var url = '<emp:url action="querySameCusBaseInfoImport.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow1');
    }
    /**add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 end**/
    
</script>
</head>
<body class="page_content">
	<emp:form method="POST" action="expBatchToExcel.do" id="queryForm">
		<emp:gridLayout id="CusSameOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusSameOrg.same_org_no" label="同业机构(行)号" />
			<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" />
			<emp:select id="CusSameOrg.crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE"/>
		</emp:gridLayout>
	</emp:form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusSameOrgPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusSameOrgPage" label="修改" op="update"/>
		<emp:button id="viewCusSameOrg" label="查看" op="view"/>
		<emp:button id="excelSDuty" label="导出" op="putout"/>
		<!-- add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 begin -->
		<emp:button id="downLoadSameCusBaseInfoTemplate" label="下载导入模板" op="downLoad"/>
		<emp:button id="importSameCusBaseInfoByExcel" label="导入" op="imp"/>
		<!-- add by lisj 2015-2-28 需求编号：【XD150213011】增加同业授信批量导入功能 end -->
	</div>

	<emp:table icollName="CusSameOrgList" pageMode="true" url="pageCusSameOrgQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_no" label="同业机构(行)号" />
		<emp:text id="same_org_cnname" label="同业机构(行)名称" />
		<emp:text id="com_ins_code" label="组织机构代码" />
		<emp:text id="same_org_type" label="机构类型" hidden="true"/>
		<emp:text id="country" label="国别" dictname="STD_GB_2659-2000"/>
		<emp:text id="reg_cap_amt" label="注册资金(万元)" dataType="Currency"/>
		<emp:text id="assets" label="资产总额(万元)" dataType="Currency"/>
		<emp:text id="crd_grade" label="信用等级" />
		<emp:text id="reg_no" label="登记注册号" hidden="true" />
		<emp:text id="swift_no" label="SWIFT编号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    