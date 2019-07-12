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
		CusOrgPop._toForm(form);
		resultSet._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.CusOrgPopGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = resultSet._obj.getSelectedData();
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
	<emp:gridLayout id="CusOrgPopGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusOrgPop.cus_id" label="客户码" />
		<emp:text id="CusOrgPop.cus_name" label="客户名称" />
		<emp:select id="CusOrgPop.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="CusOrgPop.cert_code" label="证件号码" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="选择返回"/>
	<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
	<emp:table icollName="resultSet" pageMode="true" url="pageCusOrgPop.do?cusTypCondition=${context.cusTypCondition}">
	<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_code" label="组织机构代码" />
		<emp:text id="extr_eval_addr" label="地址" hidden="true"/>
		<emp:text id="extr_eval_addr_displayname" label="地址" />
		<emp:text id="street" label="街道" />
		<emp:text id="fic_per" label="法定代表人" />
		<emp:text id="real_oper_per" label="实际经营人" />
		<emp:text id="com_str_date" label="成立日期" />
		<emp:text id="reg_cap_amt" label="注册资金" />
		<emp:text id="legal_phone" label="联系电话" />
		<!-- add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin -->
		<emp:text id="cust_mgr_displayname" label="主管客户经理" hidden="true"/>
		<emp:text id="main_br_id_displayname" label="主管机构" hidden="true" />
		<emp:text id="cust_mgr" label="主管客户经理" hidden="true" />
		<emp:text id="main_br_id" label="主管机构" hidden="true" />
		<!-- add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end -->
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>