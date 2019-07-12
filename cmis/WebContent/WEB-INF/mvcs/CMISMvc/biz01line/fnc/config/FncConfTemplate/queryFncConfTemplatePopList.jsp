<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" />


<script>

	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		FncConfTemplate._toForm(form);
		FncConfTemplateList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.FncConfTemplateGroup.reset();
	};

	function doReturnMethod(methodName){
		if (methodName) {
			var data = FncConfTemplateList._obj.getSelectedData();
			if(data!=null&&data!=''){
				var form = document.getElementById("downLoadForm");
				id2Form(form,'fnc_id',data[0].fnc_id._getValue());
				id2Form(form,'fnc_name',data[0].fnc_name._getValue());
				form.submit();
			}else {
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<form  method="POST" action="<emp:url action="downLoadTmplate.do"/>" id="downLoadForm">
	</form>


		<div id="FncConfTemplateGroup" class="emp_group_div">
		<emp:gridLayout id="FncConfTemplateGroup" maxColumn="2" title="输入查询条件">
			<emp:text id="FncConfTemplate.fnc_id" label="报表编号" />
			<emp:text id="FncConfTemplate.fnc_name" label="财务报表类型" />
		</emp:gridLayout>
		</div>
		<jsp:include page="/queryInclude.jsp" />
	
	<emp:returnButton label="下载模板"></emp:returnButton>

	<emp:table icollName="FncConfTemplateList" pageMode="true" url="pageFncConfTemplatePopQuery.do">
		<emp:text id="fnc_id" label="报表编号" />
		<emp:text id="fnc_name" label="财务报表类型" />
		<emp:text id="fnc_bs_style_id" label="资产负债表样式编号" />
		<emp:text id="fnc_pl_style_id" label="损益表(收入支出总表)" />
		<emp:text id="fnc_cf_style_id" label="现金流量(事业支出明细表)" />
		<emp:text id="fnc_fi_style_id" label="财务指标" />
	</emp:table>
	<emp:returnButton label="下载模板"></emp:returnButton>
	
</body>
</html>
</emp:page>
    