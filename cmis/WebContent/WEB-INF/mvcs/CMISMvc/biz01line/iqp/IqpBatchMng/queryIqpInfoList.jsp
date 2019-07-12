<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isRpsscnt = "";
	if(context.containsKey("isRpsscnt")){
		isRpsscnt = (String)context.getDataValue("isRpsscnt");
	} 
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doViewCtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']); 
		var isRpsscnt = '<%=isRpsscnt%>'; 
		if (paramStr != null) {
			var url;
		    if(isRpsscnt == "is"){
				var url = '<emp:url action="getCtrRpddscntContViewPage.do"/>?'+paramStr+"&op=view&flag=CtrRpddscntCont&pvp=pvp&menuId=queryCtrRpddscntContList";
			}else{
				if(prd_id==300021||prd_id==300020){
					url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuId=queryCtrLoanContHistoryList&pvp=pvp";
				}else{
					url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuId=queryCtrLoanContHistoryList&pvp=pvp";
				} 
			}
			url = EMPTools.encodeURI(url);
			window.open(url,"viewCtrLoanCont","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=1,location=no,status=no");
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpLoanApp() {
		var paramStr = IqpLoanAppList._obj.getParamStr(['serno']);
		var prd_id = IqpLoanAppList._obj.getParamValue(['prd_id']); 
		var isRpsscnt = '<%=isRpsscnt%>'; 
		if (paramStr != null) {
			var url;
		    if(isRpsscnt == "is"){
				var url = '<emp:url action="getIqpRpddscntViewPage.do"/>?'+paramStr+"&op=view&menuId=queryIqpRpddscnt&flag=notHave";
			}else{
				if(prd_id == 300021 || prd_id == 300020){
					var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?menuId=queryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
				}else{
					var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?menuId=queryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
				}
			}
			url = EMPTools.encodeURI(url);
			window.open(url,"viewIqpLoanApp","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=1,location=no,status=no");
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewRpddscntCont() {
		var paramStr = CtrRpddscntContList._obj.getParamStr(['cont_no']);
		var prd_id = CtrRpddscntContList._obj.getParamValue(['prd_id']); 
		var isRpsscnt = '<%=isRpsscnt%>'; 
		if (paramStr != null) {
			var url;
		    if(isRpsscnt == "is"){
				var url = '<emp:url action="getCtrRpddscntContViewPage.do"/>?'+paramStr+"&op=view&flag=CtrRpddscntCont&pvp=pvp&menuId=queryCtrRpddscntContList";
			}else{
				if(prd_id==300021||prd_id==300020){
					url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuId=queryCtrLoanContHistoryList&pvp=pvp";
				}else{
					url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&flag=ctrLoanCont&menuId=queryCtrLoanContHistoryList&pvp=pvp";
				} 
			}
			url = EMPTools.encodeURI(url);
			window.open(url,"viewCtrLoanCont","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=1,location=no,status=no");
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpRpddscnt() {
		var paramStr = IqpRpddscntList._obj.getParamStr(['serno']);
		var prd_id = IqpRpddscntList._obj.getParamValue(['prd_id']); 
		var isRpsscnt = '<%=isRpsscnt%>'; 
		if (paramStr != null) {
			var url;
		    if(isRpsscnt == "is"){
				var url = '<emp:url action="getIqpRpddscntViewPage.do"/>?'+paramStr+"&op=view&menuId=queryIqpRpddscnt&flag=notHave";
			}else{
				if(prd_id == 300021 || prd_id == 300020){
					var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?menuId=queryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
				}else{
					var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?menuId=queryIqpLoanApp&op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}';
				}
			}
			url = EMPTools.encodeURI(url);
			window.open(url,"viewIqpLoanApp","height=600,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=1,location=no,status=no");
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<%if(!"is".equals(isRpsscnt)){ %>
	    <div align="left">
		   <emp:button id="viewCtrLoanCont" label="查看" op="view"/>
	    </div>
		<emp:table icollName="CtrLoanContList" pageMode="false" url="">
		   <emp:text id="cont_no" label="合同编号" />
		   <emp:text id="cus_id" label="客户码" hidden="true"/>
		   <emp:text id="cus_id_displayname" label="客户名称" />
		   <emp:text id="prd_id" label="产品"  hidden="true"/>
		   <emp:text id="prd_id_displayname" label="产品名称" />
		   <emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" hidden="true"/>
		   <emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		   <emp:text id="exchange_rate" label="汇率" dataType="Double" hidden="true"/>
		   <emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		   <emp:text id="cont_balance" label="合同余额" dataType="Currency" hidden="true"/>
		   <emp:text id="risk_open_amt" label="风险敞口金额" dataType="Currency" hidden="true"/>
		   <emp:text id="cont_start_date" label="合同起始日期" hidden="true"/>
		   <emp:text id="cont_end_date" label="合同到期日期" hidden="true"/>
		   <emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
	    </emp:table>
	    <br>
	    <div align="left">
		   <emp:button id="viewIqpLoanApp" label="查看" />
	    </div>
	    <emp:table icollName="IqpLoanAppList" pageMode="false" url="">
		   <emp:text id="serno" label="业务流水号" />
		   <emp:text id="cus_id" label="客户码" hidden="true"/>
		   <emp:text id="cus_id_displayname" label="客户名称" />
		   <emp:text id="prd_id" label="产品"  hidden="true"/>
	       <emp:text id="prd_id_displayname" label="产品名称" />
	       <emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" hidden="true"/>
		   <emp:text id="apply_cur_type" label="申请币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		   <emp:text id="exchange_rate" label="汇率" dataType="Double" hidden="true"/>
		   <emp:text id="apply_amount" label="申请金额" dataType="Currency"/>
		   <emp:text id="risk_open_amt" label="风险敞口金额" dataType="Currency" hidden="true"/>
		   <emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	    </emp:table>
	<%}else{%>
	<div align="left">
		<emp:button id="viewRpddscntCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrRpddscntContList" pageMode="true" url="">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="batch_no" label="批次号" hidden="true"/>
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id" label="产品编码" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE" />
		<emp:text id="rpddscnt_type" label="转贴现方式" dictname="STD_ZB_BUSI_TYPE" />
		<emp:text id="bill_total_amt" label="票据总金额" dataType="Currency"/>
		<emp:text id="bill_qnt" label="票据数量" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />	
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
	</emp:table>
	<br>
	
	<div align="left">
		<emp:button id="viewIqpRpddscnt" label="查看" op="view"/>
	</div>
	<emp:table icollName="IqpRpddscntList" pageMode="true" url="">
		<emp:text id="serno" label="业务编号" hidden="false"/>
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id" label="产品编码" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE"/>
		<emp:text id="rpddscnt_type" label="转贴现方式" dictname="STD_ZB_BUSI_TYPE"/>
		<emp:text id="rpddscnt_rate" label="转贴现利率" dataType="Rate"/>
		<emp:text id="bill_total_amt" label="票据总金额" dataType="Currency"/>
		<emp:text id="rpddscnt_date" label="转贴现日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="batch_no" label="批次号" hidden="true"/>
	</emp:table>
	<%}%>
</body>
</html>
</emp:page>
    