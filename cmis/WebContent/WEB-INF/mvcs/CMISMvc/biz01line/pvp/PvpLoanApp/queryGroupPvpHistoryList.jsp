<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input{
border: 1px solid #b7b7b7;
width:80px;
}
</style>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CtrLoanCont._toForm(form);
		CtrLoanContHistoryList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};

	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
		CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
	};
	//查看合同信息的js
	function doOutCtrLoanContview(){
		var url;
		var selObj = CtrLoanContHistoryList._obj.getSelectedData()[0];
		var cont_no = selObj.cont_no._getValue();
		var prd_id = selObj.prd_id._getValue();
		var biz_type = selObj.biz_type._getValue();
		if(biz_type == "8"){
			if(prd_id == "300021" || prd_id == "300020"){
			 	url="<emp:url action='getCtrLoanContForDiscViewPage.do'/>?cont_no="+cont_no+"&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
		    }else{
		    	url="<emp:url action='getCtrLoanContViewPage.do'/>?cont_no="+cont_no+"&menuId=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
			}
		}else{
			if(prd_id == "300021" || prd_id == "300020"){
			 	url="<emp:url action='getCtrLoanContForDiscViewPage.do'/>?cont_no="+cont_no+"&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
		    }else{
		    	url="<emp:url action='getCtrLoanContViewPage.do'/>?cont_no="+cont_no+"&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
			}
		}
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}; 

</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CtrLoanContGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CtrLoanCont.cont_no" label="合同编号" />
		<emp:pop id="CtrLoanCont.cus_name" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
		<emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>    
		
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="CtrLoanContHistoryList" pageMode="false" url="pageGroupPvpHistoryQuery.do"> 
		
		<emp:link id="cont_no" label="合同编号" operation="outCtrLoanContview"/>
		<emp:text id="cn_cont_no" label="中文合同编号" hidden="true" />
		<emp:text id="cus_id" label="客户码" hidden="true" /> 
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="is_close_loan" label="续贷标志" dictname="STD_ZX_YES_NO" />
		
		<emp:text id="dep_ln_rate" label="支行存贷比" />
		<emp:text id="cont_number" label="评分" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_balance" label="合同余额" dataType="Currency" />     
		<emp:text id="pvp_amt" label="放款金额" flat="false" dataType="Currency" cssElementClass="emp_input" readonly="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true" />
		<emp:text id="manager_br_id" label="管理机构" required="false" readonly="true" hidden="true" />
		<emp:text id="input_br_id" label="登记机构" required="false" hidden="true" />
		<emp:text id="input_date" label="登记日期" required="false" hidden="true" />
		<emp:text id="biz_type" label="业务模式" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>