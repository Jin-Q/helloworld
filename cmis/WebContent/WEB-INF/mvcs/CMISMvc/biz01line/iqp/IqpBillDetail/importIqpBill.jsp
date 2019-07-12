<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBillDetail._toForm(form);
		IqpBillDetailList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.IqpBillDetailGroup.reset();
	};
	//--------导入操作---------
	function doImportIqpBillRecord(){
		var data = IqpBillDetailList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		var porderno = data[0].porder_no._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					alert("导入成功！");
					window.location.reload();
					window.opener.location.reload();
					//window.close();
				}else {
					alert(msg);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		var url="<emp:url action='importIqpBill.do'/>?serno=${context.serno}&batch_no=${context.batch_no}&porderno="+porderno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	};			

	function doSelect(){
		//var methodName="${context.popReturnMethod}";
		doImportIqpBillRecord();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpBillDetailGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpBillDetail.porder_no" label="汇票号码" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="importIqpBillRecord" label="导入"/>
	</div>

	<emp:table icollName="IqpBillDetailList" pageMode="true" url="pageImportIqpBillQuery.do">
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="bill_isse_date" label="票据签发日" />
		<emp:text id="porder_end_date" label="汇票到期日" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency" />
		<emp:text id="isse_name" label="出票/付款人名称" />
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="aorg_name" label="承兑行名称" />
		<emp:text id="aorg_no" label="承兑行行号" />
		<emp:text id="aorg_type" label="承兑行类型" dictname="STD_AORG_ACCTSVCR_TYPE"/>
		<emp:text id="status" label="票据状态" dictname="STD_ZB_DRFT_STATUS"/>
	</emp:table>
</body>
</html>
</emp:page>

