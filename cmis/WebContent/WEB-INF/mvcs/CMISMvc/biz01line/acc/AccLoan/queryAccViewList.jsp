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
		AccView._toForm(form);
		AccViewPopList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	
	/*--user code begin--*/
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
	function returnCus(data){
		AccView.cus_id._setValue(data.cus_id._getValue());
    	AccView.cus_name._setValue(data.cus_name._getValue());
    };

    function doViewAccViewPopList(){
    	var paramStr = AccViewPopList._obj.getParamStr(['bill_no']);
    	if (paramStr != null) {
    		var table_model = AccViewPopList._obj.getSelectedData()[0].table_model._getValue();
    		var url = '<emp:url action="getAccViewPage.do"/>?isHaveButton=not&op=view&'+paramStr+'&table_model='+table_model;
    		url = EMPTools.encodeURI(url);
    		window.open(url,'viewWindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
    	} else {
    		alert('请先选择一条记录！');
    	}
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:button label="查看" id="viewAccViewPopList"/>
	
	<emp:table icollName="AccViewPopList" pageMode="true" url="pageAccViewList.do?cont_no=${context.cont_no}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/> 
		<emp:text id="bill_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="bill_bal" label="贷款余额" dataType="Currency"/>
		<emp:text id="start_date" label="发放日期"  hidden="false"/>
		<emp:text id="end_date" label="到期日期"  hidden="false"/>
		<emp:text id="status" label="台账状态" dictname="STD_ZB_ACC_TYPE" />
		
		<emp:text id="cur_type" label="币种" hidden="true"/>
		<emp:text id="inner_owe_int" label="表内欠息" hidden="true"/>
		<emp:text id="out_owe_int" label="表外欠息"  hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构"  hidden="true"/>
		<emp:text id="fina_br_id_displayname" label="账务机构"  hidden="true"/>
		<emp:text id="reality_ir_y" label="执行利率" hidden="true"/>
		<emp:text id="five_class" label="五级分类" hidden="true"/>
		<emp:text id="twelve_cls_flg" label="十二级分类" hidden="true"/>
		<emp:text id="table_model" label="台账表" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>