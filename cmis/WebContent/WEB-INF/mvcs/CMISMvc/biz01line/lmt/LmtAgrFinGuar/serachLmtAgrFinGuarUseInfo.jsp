<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<head>
<title>授信使用情况</title>
<jsp:include page="/include.jsp" />
<script type="text/javascript">
function doViewCtrLoanCont() {
	var paramStr = LmtUseContList._obj.getParamStr(['cont_no']);
	var prd_id = LmtUseContList._obj.getParamValue(['prd_id']); 
	var biz_type = LmtUseContList._obj.getParamValue(['biz_type']); 
	if (paramStr != null) {
		var url;
		if(prd_id==300021||prd_id==300020){
			url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		}else{
			url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		}
		if(biz_type == '8'){//银租通
			if(prd_id==300021||prd_id==300020){
				url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuIdTab=yztqueryCtrLoanContHistoryList&pvp=pvp";
			}else{
				url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuIdTab=yztqueryCtrLoanContHistoryList&pvp=pvp";
			}
		}
		url = EMPTools.encodeURI(url);
		window.open(url,"viewCtrLoanCont","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=1,location=no,status=no");
	} else {
		alert('请先选择一条记录！');
	}
};
function doViewIqpLoanApp() {
	var paramStr = LmtUseIqpList._obj.getParamStr(['serno']);
	var prd_id = LmtUseIqpList._obj.getParamValue(['prd_id']); 
	var biz_type = LmtUseContList._obj.getParamValue(['biz_type']);
	if (paramStr != null) {
		var url;
		if(prd_id == 300021 || prd_id == 300020){
			var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?menuIdTab=queryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
		}else{
			var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?menuIdTab=queryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
		}
		if(biz_type == '8'){//银租通
			if(prd_id == 300021 || prd_id == 300020){
				var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?menuIdTab=yztqueryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
			}else{
				var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?menuIdTab=yztqueryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
			}
		}
		url = EMPTools.encodeURI(url);
		window.open(url,"viewIqpLoanApp","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=1,location=no,status=no");
	} else {
		alert('请先选择一条记录！');
	}
};
function doQuery(){
	var form = document.getElementById('queryForm');
	CtrLoanCont._toForm(form);
	LmtUseContList._obj.ajaxQuery(null,form);
};

function doReset(){
	page.dataGroups.CtrLoanContGroup.reset();
};
function returnCus(data){
	CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
	CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
};
</script>
</head>

<body class="page_content"> 
<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CtrLoanContGroup" title="输入合同查询条件" maxColumn="2">
	     <emp:pop id="CtrLoanCont.cus_name" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
		 <emp:select id="CtrLoanCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
	     <emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
<b>合同信息列表</b>
	<div align="left">
		<emp:button id="viewCtrLoanCont" label="查看" />
	</div>
	<emp:table icollName="LmtUseContList" pageMode="false" url="pageSearchLmtAgrFinGuarDetails.do" reqParams="cus_id=${context.cus_id}">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品"  hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="exchange_rate" label="汇率" dataType="Double" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="cont_balance" label="合同余额" dataType="Currency" />
		<emp:text id="risk_open_amt" label="风险敞口金额" dataType="Currency" />
		<emp:text id="cont_start_date" label="合同起始日期" />
		<emp:text id="cont_end_date" label="合同到期日期" />
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="biz_type" label="业务模式" hidden="true"/>
	</emp:table>
	<br>
<b>在途申请列表</b>
	<div align="left">
		<emp:button id="viewIqpLoanApp" label="查看" />
	</div>
	<emp:table icollName="LmtUseIqpList" pageMode="false" url="pageSearchLmtUseInfoDetails.do" reqParams="agr_no=${context.agr_no}&biz_area_no=${context.biz_area_no}&limit_code=${context.limit_code}">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品"  hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="apply_cur_type" label="申请币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="exchange_rate" label="汇率" dataType="Double" />
		<emp:text id="apply_amount" label="申请金额" dataType="Currency"/>
		<emp:text id="risk_open_amt" label="风险敞口金额" dataType="Currency" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="biz_type" label="业务模式" hidden="true"/>
	</emp:table>
</body> 
</emp:page>
