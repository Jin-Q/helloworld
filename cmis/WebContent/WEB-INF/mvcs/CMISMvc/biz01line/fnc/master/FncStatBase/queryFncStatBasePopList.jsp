<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>

<%
String cus_id=request.getParameter("FncStatBase.cus_id");
%>




<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>
	<jsp:include page="/include.jsp" />


	<script>

	var page = new EMP.util.Page();
	function doOnLoad() {
		//page.renderEmpObjects();
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncStatBase._toForm(form);
		FncStatBaseList._obj.ajaxQuery(null,form);
	};
	
	
	
	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = FncStatBaseList._obj.getSelectedData()[0];
			if(data!=null&&data!=''){
				var parentWin = EMPTools.getWindowOpener();
				eval("parentWin."+methodName+"(data)");
				window.close();
			}else {
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<form method="POST" action="#" id="queryForm"></form>
	

	<div id="FncStatBaseGroup" class="emp_group_div">
	<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="输入查询条件">
		<emp:text id="FncStatBase.cus_id" label="客户代码" hidden="true"/>
		<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:text id="FncStatBase.stat_prd" label="报表期间" />
		<emp:select id="FncStatBase.stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
	</emp:gridLayout></div>
	<jsp:include page="/queryInclude.jsp" />
	<emp:returnButton label="选取并返回"></emp:returnButton>
	<emp:table icollName="FncStatBaseList" pageMode="true" url="pageSelectFncPop.do" reqParams="cusId=${context.cusId}&termType=${context.termType}">
		<emp:text id="cus_id" label="客户码" hidden="false"/>
	   <emp:text id="cus_name" label="客户名称" hidden="false"/>
		<emp:text id="stat_prd_style" label="报表周期类型" dictname="STD_ZB_FNC_STAT"/>
		<emp:text id="stat_prd" label="报表期间" />
		<emp:text id="stat_style" label="报表口径" dictname="STD_ZB_FNC_STYLE"/>
		<emp:text id="stat_bs_style_id" label="资产样式编号" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true" />
		<emp:text id="input_br_id" label="登记机构" hidden="true" />
		<emp:text id="stat_pl_style_id" label="损益表编号" hidden="true"/>
		<emp:text id="stat_cf_style_id" label="现金流量表编号" hidden="true"/>
		<emp:text id="stat_fi_style_id" label="财务指标表编号" hidden="true"/>
		<emp:text id="stat_soe_style_id" label="所有者权益变动表编号" hidden="true"/>
		<emp:text id="stat_sl_style_id" label="财务简表编号" hidden="true"/>	
		<emp:text id="stat_acc_style_id" label="会计科目余额表编号" hidden="true"/>
		<emp:text id="stat_de_style_id" label="经济合作社财务收支明细表编号" hidden="true"/>
	</emp:table>
	<emp:returnButton label="选取并返回"></emp:returnButton>


	</body>
	</html>
</emp:page>
