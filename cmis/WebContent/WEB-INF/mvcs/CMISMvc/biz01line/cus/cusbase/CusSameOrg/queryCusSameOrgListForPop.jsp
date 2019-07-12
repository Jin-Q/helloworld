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
	
	/*--user code begin--*/
	function doReset(){
		page.dataGroups.CusSameOrgGroup.reset();
	};
	
	function doSelect(){
		var methodName="${context.popReturnMethod}";
		doReturnMethod(methodName);
	};
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = CusSameOrgList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else {
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
		<emp:gridLayout id="CusSameOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusSameOrg.same_org_no" label="同业机构(行)号" />
			<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" />
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton label="选择返回"/>
		<emp:table icollName="CusSameOrgList" pageMode="true" url="pageCusSameOrgQueryPop.do">
			<emp:text id="same_org_no" label="同业机构(行)号" />
			<emp:text id="same_org_cnname" label="同业机构(行)名称" />
			<emp:text id="same_org_type" label="同业机构类型" dictname="STD_ZB_INTER_BANK_ORG"/>
			<emp:select id="crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE"/>
			<emp:text id="reg_cap_amt" label="注册/开办资金(万元)" dataType="Currency"/>
			<emp:text id="reg_no" label="登记注册号" hidden="true"/>
			<emp:text id="cus_id" label="客户码" hidden="true"/>
			
		</emp:table>
	<emp:returnButton label="选择返回"/>
</body>
</html>
</emp:page>