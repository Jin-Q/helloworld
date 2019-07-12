<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIntbankBatchListList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewLmtBatchCorre() {
		var paramStr = LmtIntbankBatchList.LmtBatchCorre._obj.getParamStr(['batch_cus_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryLmtIntbankBatchListLmtBatchCorreDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="LmtIntbankBatchListGroup" title="同业客户批量名单维护" maxColumn="2">
			
			<emp:text id="LmtIntbankBatchList.batch_cus_no" label="批量客户编号" maxlength="32" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.batch_cus_type" label="批量客户类型" maxlength="32" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.cdt_lvl" label="信用等级" required="true" dictname="STD_ZB_FINA_GRADE" readonly="true"/>		
			<emp:text id="LmtIntbankBatchList.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="LmtIntbankBatchList.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:text id="LmtIntbankBatchList.input_id" label="登记人" maxlength="20"  hidden="true"/>
			<emp:text id="LmtIntbankBatchList.input_br_id" label="登记机构" maxlength="32" hidden="true"/>
			<emp:date id="LmtIntbankBatchList.start_date" label="生效日期"  hidden="true"/>
			<emp:date id="LmtIntbankBatchList.end_date" label="到期日期"  hidden="true"/>
			<emp:select id="LmtIntbankBatchList.state" label="状态"  hidden="true"/>
			<emp:text id="LmtIntbankBatchList.serno" label="业务编号" maxlength="32" hidden="true"/>						
			<emp:text id="LmtIntbankBatchList.manager_id" label="责任人" required="false" hidden="true"/>
			<emp:text id="LmtIntbankBatchList.manager_br_id" label="管理机构"  required="false" hidden="true"/>
			<emp:select id="LmtIntbankBatchList.approve_status" label="审批状态"  hidden="true"/>
	</emp:gridLayout>
	<emp:tabGroup id="LmtIntbankBatchList_tabs" mainTab="LmtBatchCorre_tab">
		<emp:tab id="LmtBatchCorre_tab" label="批量客户名单关联表" url="queryLmtIntbankBatchListLmtBatchCorreList.do" reqParams="LmtIntbankBatchList.batch_cus_no=$LmtIntbankBatchList.batch_cus_no;" initial="true"/>
          <div align="center">
		      <emp:button id="return" label="返回到列表页面"/>
	        </div>
	</emp:tabGroup>

			




</body>
</html>
</emp:page>
