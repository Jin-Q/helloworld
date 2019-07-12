<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doViewCtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']); 
		if (paramStr != null) {
			var url;
			if(prd_id==300021||prd_id==300020){
				url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&cont=cont&'+paramStr+'&flag=ctrLoanCont';
			}else{
				url = '<emp:url action="getCtrLoanContViewPage.do"/>?menuIdTab=queryCtrLoanContHistoryList&pvp=pvp&op=view&cont=cont&'+paramStr+'&flag=ctrLoanCont';  
			} 
			url = EMPTools.encodeURI(url);  
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
  <div align="left">
		<emp:button id="viewCtrLoanCont" label="查看" op="view"/>
	</div>
	<emp:table icollName="CtrLoanContList" pageMode="true" url="pageGetPromissoryTabListOp.do?cont_no=${context.cont_no}" >
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
		<emp:text id="iqpFlowHis" label="业务审批标识" hidden="true" defvalue="have"/>
	</emp:table>
</body>
</html>
</emp:page>