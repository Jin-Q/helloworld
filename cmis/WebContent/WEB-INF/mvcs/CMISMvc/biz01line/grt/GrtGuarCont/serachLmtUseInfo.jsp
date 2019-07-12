<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<head>
<title>授信使用情况</title>
<jsp:include page="/include.jsp" />
<script type="text/javascript">
function doViewCtrLoanCont() {
	var paramStr = LmtUseList._obj.getParamStr(['cont_no']);
	var prd_id = LmtUseList._obj.getParamValue(['prd_id']); 
	if (paramStr != null) {
		var url;
		if(prd_id==300021||prd_id==300020){
			url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		}else{
			url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		} 
		url = EMPTools.encodeURI(url);
		window.open(url,"viewCtrLoanCont","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=1,location=no,status=no");
	} else {
		alert('请先选择一条记录！');
	}
};
</script>
</head>

<body >
	<div align="left">
		<emp:button id="viewCtrLoanCont" label="查看" />
	</div>

	<emp:table icollName="LmtUseList" pageMode="true" url="pageQueryContListByGuarContNo.do" reqParams="guar_cont_no=${context.guar_cont_no}">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品"  hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="exchange_rate" label="汇率" dataType="Double" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="cont_balance" label="合同余额" dataType="Currency"/>
		<emp:text id="risk_open_amt" label="风险敞口金额" dataType="Currency" hidden="true"/>
		<emp:text id="cont_start_date" label="合同起始日期" />
		<emp:text id="cont_end_date" label="合同到期日期" />
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
	</emp:table>
</body> 
</emp:page>
