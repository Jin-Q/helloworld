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
		
	function doReturnMethod(){
		var data = CusOrgAppMngList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect()
	{
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};

	/*--user code begin--*/
	function doReset(){
		page.dataGroups.CusOrgAppMngGroup.reset();
	};
	function returnCus(data){
		CusOrgAppMng.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusOrgAppMngGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusOrgAppMng.cus_id" label="评估机构客户码" />	
			<emp:text id="CusOrgAppMng.cus_name" label="评估机构名称" />	
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		 <emp:returnButton id="s1" label="选择返回"/>
	</div>

	<emp:table icollName="CusOrgAppMngList" pageMode="true" url="pageCusOrgAppMngQuery.do?restrictUsed=false">
		<emp:text id="cus_id" label="评估机构客户码" />
		<emp:text id="cus_name" label="评估机构名称" />
		<emp:text id="extr_eval_org" label="组织机构代码" />
		<emp:text id="extr_eval_quali" label="资质等级" dictname="STD_ZB_EXTR_EVAL_QUALI"/>
		<emp:text id="extr_eval_rng" label="评估范围" />
		<emp:text id="inure_date" label="生效日期" />
		<emp:text id="end_date" label="到期日期"  />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    