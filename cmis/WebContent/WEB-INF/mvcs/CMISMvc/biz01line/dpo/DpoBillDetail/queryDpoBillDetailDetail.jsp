<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<jsp:include page="DpoBillDetailComm.jsp" flush="true" /> 
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		
		if("tab"=='${context.tab}'){
		window.close();
		}else{
		var url = '<emp:url action="queryDpoBillDetailList.do"/>?drfpo_no=${context.drfpo_no}&oper=${context.oper}';
		url = EMPTools.encodeURI(url);
		window.location=url;
		}
	};
	
	function doLoad(){
		chageBillType();
	}
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:gridLayout id="IqpBillDetailInfoGroup" title="票据基本信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.porder_no" label="汇票号码" hidden="false" onblur="getPorderMsg();" colSpan="2" maxlength="40" readonly="true" required="true" />
				<emp:select id="IqpBillDetailInfo.bill_type" label="票据种类" onblur="chageBillType();" required="true" dictname="STD_DRFT_TYPE"/>
				<emp:select id="IqpBillDetailInfo.porder_curr" label="汇票币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
				<emp:text id="IqpBillDetailInfo.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" />
				<emp:select id="IqpBillDetailInfo.porder_addr" label="汇票签发地" dictname="STD_ZB_DRFT_ADDR" required="true" />    
				<emp:select id="IqpBillDetailInfo.is_ebill" label="是否电票" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
				<emp:date id="IqpBillDetailInfo.bill_isse_date" label="票据签发日" required="true" />
				<emp:date id="IqpBillDetailInfo.porder_end_date" label="汇票到期日" required="true" />
				<emp:select id="IqpBillDetailInfo.utakeover_sign" label="不得转让标记" required="true" dictname="STD_ZX_YES_NO"/> 
				<emp:select id="IqpBillDetailInfo.status" label="票据状态" readonly="true" required="true" dictname="STD_ZB_DRFT_STATUS"/>
			</emp:gridLayout>
			<emp:gridLayout id="IqpBillDetailInfoGroup"  title="出票人信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.drwr_org_code" label="出票人组织机构代码" maxlength="20" required="true" colSpan="2"/>
				<emp:text id="IqpBillDetailInfo.isse_name" label="出票/付款人名称" maxlength="80" required="true" />  
				<emp:text id="IqpBillDetailInfo.daorg_no" label="出票人开户行行号"  required="true" />
				<emp:text id="IqpBillDetailInfo.daorg_name" label="出票人开户行行名" maxlength="100" required="true"  colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				<emp:text id="IqpBillDetailInfo.daorg_acct" label="出票人开户行账号"  required="true" />
			</emp:gridLayout>
			<emp:gridLayout id="IqpBillDetailInfoGroup"  title="贸易合同信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.tcont_no" label="贸易合同编号" maxlength="40" required="true" />
				<emp:text id="IqpBillDetailInfo.tcont_amt" label="贸易合同金额" maxlength="18" required="true" dataType="Currency" />
				<emp:textarea id="IqpBillDetailInfo.tcont_content" label="贸易合同内容" maxlength="500" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="IqpBillDetailInfoGroup"  title="收款人信息" maxColumn="2">
				<emp:text id="IqpBillDetailInfo.pyee_name" label="收款人名称" maxlength="80" required="true" />
				<emp:text id="IqpBillDetailInfo.paorg_no" label="收款人开户行行号"  required="true" />
				<emp:text id="IqpBillDetailInfo.paorg_name" label="收款人开户行行名" maxlength="100" required="true"  colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				<emp:text id="IqpBillDetailInfo.paorg_acct_no" label="收款人开户行账号"  required="true" /> 
			</emp:gridLayout>
			<emp:gridLayout id="IqpBillDetailInfoGroup"  title="承兑方信息" maxColumn="2">
				<emp:select id="IqpBillDetailInfo.aorg_type" label="承兑行类型" required="false" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
				<emp:text id="IqpBillDetailInfo.aorg_no" label="承兑行行号" maxlength="20" required="false" />
				<emp:text id="IqpBillDetailInfo.aorg_name" label="承兑行名称" maxlength="100" required="false"  colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				<emp:select id="IqpBillDetailInfo.aaorg_type" label="承兑人开户行类型" required="false" colSpan="2" dictname="STD_AORG_ACCTSVCR_TYPE"/>
				<emp:text id="IqpBillDetailInfo.aaorg_no" label="承兑人开户行行号"  required="false" />
				<emp:text id="IqpBillDetailInfo.aaorg_name" label="承兑人开户行名称" maxlength="100" required="false"  colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				<emp:text id="IqpBillDetailInfo.accptr_cmon_code" label="承兑人组织机构代码" maxlength="20" required="false" />
				<emp:text id="IqpBillDetailInfo.aaorg_acct_no" label="承兑人开户行账号" maxlength="40" required="false" />
				<emp:text id="IqpBillDetailInfo.flag" label="标志位（用来标记此汇票是否是可以新增的1--可以，2--不可以）" maxlength="40" required="false" hidden="true" defvalue="1"/>
			</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
