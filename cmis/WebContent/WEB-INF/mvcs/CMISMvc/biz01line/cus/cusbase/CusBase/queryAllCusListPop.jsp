<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBase._toForm(form);
		CusBaseList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.CusBaseGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = CusBaseList._obj.getSelectedData();
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
	function doCancel(){
		window.close();
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusBaseGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusBase.cus_id" label="客户码" />
		<emp:text id="CusBase.cus_name" label="客户名称" />
		<emp:select id="CusBase.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="CusBase.cert_code" label="证件号码" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	 <emp:returnButton id="s1" label="选择返回"/>
	<!--/*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 begin*/-->
	<emp:table icollName="CusBaseList" pageMode="true" url="pageAllCusQuery.do?cusTypCondition=${context.cusTypCondition}&opt=${context.opt}&cusTypCondition2=${context.cusTypCondition2}&flag=${context.flag}">
	<!--/*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 end*/-->
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:select id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cust_mgr" label="主管客户经理" hidden="true"/>
		<emp:text id="cust_mgr_displayname" label="主管客户经理" />
		<emp:text id="guar_cls" label="担保类别" hidden="true"/>
		<emp:text id="guar_bail_multiple" label="担保放大倍数" hidden="true"/>
		<emp:text id="guar_crd_grade" label="担保信用等级" hidden="true"/>
		<emp:text id="cus_crd_grade" label="信用等级" hidden="true" dictname="STD_ZB_CREDIT_GRADE"/> <!--  2013-11-19  唐顺岩添加  -->
		<emp:text id="main_br_id" label="主管机构" hidden="true"/>
		<emp:text id="main_br_id_displayname" label="主管机构"/>
		<emp:text id="cus_addr" label="客户地址" hidden="true"/>
		<emp:text id="loan_card_id" label="贷款卡编码" hidden="true"/>
		<emp:text id="cus_type" label="客户类型" hidden="true"/>
		<emp:text id="belg_line" label="条线" hidden="false" dictname="STD_ZB_BUSILINE"/>
		<emp:text id="cus_country" label="国籍" hidden="true" dictname="STD_GB_2659-2000" />
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    