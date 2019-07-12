<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		CtrLoanCont._toForm(form);
		CtrLoanContList._obj.ajaxQuery(null,form);
	};
	
	function doSelect(){
		var data = CtrLoanContList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else { 
			alert('请先选择一条记录！');
		} 
	};
	
	function doClose(){
        window.close();
	};

	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};

	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
		CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
	};

	function returnPrdId(data){
		CtrLoanCont.prd_id._setValue(data.id);
		CtrLoanCont.prd_id_displayname._setValue(data.label); 
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
   <form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CtrLoanContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrLoanCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLoanCont.cus_name" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:pop id="CtrLoanCont.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:text id="CtrLoanCont.prd_id" label="产品编号"  hidden="true" />
			<emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>  
		
	<jsp:include page="/queryInclude.jsp" flush="true" />

  <br>    
  <emp:button id="select" label="选取返回"/>
	<emp:table icollName="CtrLoanContList" pageMode="true" url="pageQueryIqpBailSubDisPopList.do" >
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cn_cont_no" label="中文合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />    
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
	</emp:table>
<div align="left">
        <br>
		<emp:button id="select" label="选取返回"/>
		<emp:button id="close" label="关闭" />
	</div>
</body>
</html>
</emp:page>