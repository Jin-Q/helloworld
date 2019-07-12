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
		LmtIntbankBatchList._toForm(form);
		LmtIntbankBatchListList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtIntbankBatchListGroup.reset();
	};
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = LmtIntbankBatchListList._obj.getSelectedData();
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
	function doSelect(){
		var data = LmtIntbankBatchListList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form method="POST" action="#" id="queryForm">
		<emp:gridLayout id="LmtIntbankBatchListGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtIntbankBatchList.batch_cus_no" label="批量客户编号" />
			<emp:select id="LmtIntbankBatchList.cdt_lvl" label="信用等级" dictname="STD_ZB_FINA_GRADE" />
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
<emp:button id="select" label="选择返回"/>
	<emp:table icollName="LmtIntbankBatchListList" pageMode="true" url="pageLmtIntbankBatchListQuery.do">
		<emp:text id="batch_cus_no" label="批量客户编号" />
		<emp:select id="batch_cus_type" label="批量客户类型" dictname="STD_ZB_BATCH_CUS_TYPE"/>
		<emp:text id="cdt_lvl" label="信用等级" dictname="STD_ZB_FINA_GRADE" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
<emp:button id="select" label="选择返回"/>
</body>
</html>
</emp:page>