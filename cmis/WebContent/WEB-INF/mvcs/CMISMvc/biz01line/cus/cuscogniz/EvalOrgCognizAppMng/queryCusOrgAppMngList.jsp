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
		CusOrgAppMng._toForm(form);
		CusOrgAppMngList._obj.ajaxQuery(null,form);
	};
		
	function doViewCusOrgAppMng() {
		var paramStr = CusOrgAppMngList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusOrgAppMngViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**modified by lisj 2014-12-15 需求：【XD140918061】新增评估机构管理修改功能 begin**/
	function doGetCusOrgAppMngUpdatePage() {
		var paramStr = CusOrgAppMngList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusOrgAppMngUpdatePage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};	
	function doReset(){
		page.dataGroups.CusOrgAppMngGroup.reset();
	};
	
	function returnCus(data){
		CusOrgAppMng.cus_id._setValue(data.cus_id._getValue());
		CusOrgAppMng.cus_name._setValue(data.cus_name._getValue());
	};
	/**modified by lisj 2014-12-15 需求：【XD140918061】新增评估机构管理修改功能 end**/	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusOrgAppMngGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="CusOrgAppMng.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
  		<emp:text id="CusOrgAppMng.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCusOrgAppMng" label="查看" op="view"/>
		<emp:button id="getCusOrgAppMngUpdatePage" label="评估机构维护" op="update"/>
	</div>

	<emp:table icollName="CusOrgAppMngList" pageMode="true" url="pageCusOrgAppMngQuery.do">
		<emp:text id="cus_id" label="评估机构客户码" />
		<emp:text id="cus_name" label="评估机构名称" />
		<emp:text id="extr_eval_quali" label="资质等级" dictname="STD_ZB_EXTR_EVAL_QUALI"/>
		<emp:text id="extr_eval_rng" label="评估范围" />
		<emp:text id="inure_date" label="生效日期" />
		<emp:text id="end_date" label="到期日期"  />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    