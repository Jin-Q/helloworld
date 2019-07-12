<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>客户存量业务页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CtrLoanCont._toForm(form);
		CtrLoanContList._obj.ajaxQuery(null,form);
	};
	
	function doViewCtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url;
			var biz_type = CtrLoanContList._obj.getSelectedData()[0].biz_type._getValue();
			var prd_id = CtrLoanContList._obj.getSelectedData()[0].prd_id._getValue();
		    if("8"==biz_type && (prd_id == "300021" || prd_id == "300020")){
		       url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?menuId=yztqueryCtrLoanContHistoryList&'+paramStr+'&pvp=pvp';
		    }else if("8" == biz_type && prd_id != "300021" && prd_id != "300020"){
		       url = '<emp:url action="getCtrLoanContViewPage.do"/>?menuId=yztqueryCtrLoanContHistoryList&'+paramStr+'&pvp=pvp';
		    }else if(prd_id == "300021" || prd_id == "300020"){
		       url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?menuId=queryCtrLoanContHistoryList&'+paramStr+'&pvp=pvp';
		    }else{
		       url = '<emp:url action="getCtrLoanContViewPage.do"/>?menuId=queryCtrLoanContList&'+paramStr+'&pvp=pvp';
		    }
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};

	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:actButton id="viewCtrLoanCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrLoanContList" pageMode="true" url="pageQueryCustContHistoryList.do?serno=${context.serno}&cus_id=${context.cus_id}">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cn_cont_no" label="中文合同编号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="prd_id" label="产品名称编号" hidden="true"/>
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="biz_type" label="业务模式" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>