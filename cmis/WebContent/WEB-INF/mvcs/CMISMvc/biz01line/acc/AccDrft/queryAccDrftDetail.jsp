<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String prd_id = "";
	String flag = "";
	String isHaveButton = "";
	String openNew = "";
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("prd_id")){
		prd_id = (String)context.getDataValue("prd_id"); 
	}     
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag"); 
	}     
	if(context.containsKey("isHaveButton")){
		isHaveButton = (String)context.getDataValue("isHaveButton");
	}
	if(context.containsKey("openNew")){
		openNew = (String)context.getDataValue("openNew");
	}
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	/* function doReturn() {
		var url = '<emp:url action="queryAccDrftList.do"/>?porder_no='+'${context.AccDrft.porder_no}';
		url = EMPTools.encodeURI(url);       
		window.location=url;         
	}; */

	 function doReturn() {
		var url = '<emp:url action="queryAccDrftList.do"/>';
		url = EMPTools.encodeURI(url);       
		window.location=url;         
	};
	
	function onload(){
		if("04" == AccDrft.dscnt_type._getValue()||"02" == AccDrft.dscnt_type._getValue()){//如果转贴现方式为 卖出回购、买入返售
			AccDrft.rebuy_date._obj._renderHidden(false);
        	$(".emp_field_label:eq(10)").text("回购利率");
        	$(".emp_field_label:eq(11)").text("回购利息");
			$(".emp_field_label:eq(20)").text("回购天数");
        }else{
        	AccDrft.rebuy_date._obj._renderHidden(true);
        	$(".emp_field_label:eq(10)").text("贴现利率");
        	$(".emp_field_label:eq(11)").text("贴现利息");
			$(".emp_field_label:eq(20)").text("贴现天数");
        }
        var settl_date = AccDrft.settl_date._getValue();
        var writeoff_date = AccDrft.writeoff_date._getValue();
        if(writeoff_date==null||writeoff_date==""){
        	AccDrft.writeoff_date._setValue(settl_date);
        }
	}
	
	/*--user code begin--*/
	function doClose(){
		window.close();
	}
	/*--user code end--*/
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =AccDrft.discount_per._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
</script>
</head>
<body class="page_content" onload="onload()">
<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="台账基本信息" id="base_tab" needFlush="true" initial="true" >	
	    <emp:gridLayout id="AccDrftGroup" title="票据流水台帐基本信息" maxColumn="2">
	    	<emp:text id="AccDrft.bill_no" label="借据编号" maxlength="40" required="false" />
	    	<emp:text id="AccDrft.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccDrft.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccDrft.prd_id_displayname" label="产品名称" required="false" />    
			<emp:text id="AccDrft.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="AccDrft.dscnt_type" label="贴现方式" required="false" dictname="STD_ZB_BUSI_TYPE" />
			<emp:text id="AccDrft.porder_no" label="汇票号码" maxlength="40" required="false" />
			<emp:text id="AccDrft.discount_per" label="贴现人/交易对手" maxlength="40" required="false" hidden="true" />
			<emp:text id="AccDrft.discount_per_displayname" label="贴现人/交易对手"  required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="AccDrftGroup" title="金额信息" maxColumn="2">	
			<emp:select id="AccDrft.cur_type" label="交易币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccDrft.dscnt_rate" label="贴现利率" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="AccDrft.dscnt_int" label="贴现利息" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccDrft.rpay_amt" label="实付金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="AccDrft.overdue_rebuy_rate" label="逾期回购利率" maxlength="16" required="false" dataType="Rate" />
            <emp:text id="AccDrft.rebuy_rate" label="回购利率" maxlength="18" required="false" hidden="true" dataType="Rate" />
			<emp:text id="AccDrft.rebuy_int" label="回购利息" maxlength="18" required="false" hidden="true" dataType="Currency" />
        </emp:gridLayout>
		<emp:gridLayout id="AccDrftGroup" title="日期信息" maxColumn="2">	
			<emp:text id="AccDrft.acc_day" label="日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="AccDrft.acc_year" label="年份" maxlength="5" required="false" hidden="true"/>
			<emp:text id="AccDrft.acc_mon" label="月份" maxlength="5" required="false" hidden="true"/>
			<emp:date id="AccDrft.dscnt_date" label="贴现日" required="false" />
			<emp:text id="AccDrft.dscnt_day" label="贴现天数" maxlength="10" required="false" />
			<emp:text id="AccDrft.adjust_day" label="调整天数" maxlength="10" required="false" />
			<emp:date id="AccDrft.rebuy_date" label="回购到期日" required="false" hidden="true"/>
			<emp:date id="AccDrft.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccDrft.writeoff_date" label="核销日期" required="false" />
			<emp:date id="AccDrft.settl_date" label="结清日期" required="false" hidden="true"/>
			<emp:text id="AccDrft.rebuy_day" label="回购天数" maxlength="10" required="false" hidden="true" />
		</emp:gridLayout>	
		<emp:gridLayout id="AccDrftGroup" title="风险分类信息" maxColumn="2">	
			<emp:select id="AccDrft.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccDrft.twelve_cls_flg" label="十二级分类标志" dictname="STD_ZB_TWELVE_CLASS" required="false" />
			<emp:date id="AccDrft.twelve_class_time" label="十二级分类时间" required="false" />
		</emp:gridLayout>	
		<emp:gridLayout id="AccDrftGroup" title="登记机构" maxColumn="2">	
			<emp:text id="AccDrft.manager_br_id_displayname" label="管理机构"  required="false" />
			<emp:text id="AccDrft.fina_br_id_displayname" label="账务机构"  required="false" />
			<emp:select id="AccDrft.accp_status" label="台账状态" required="false" dictname="STD_ZB_ACC_TYPE"/>
	    </emp:gridLayout>
     </emp:tab> 
  <%if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id) ){ %>
     <emp:tab label="合同信息" id="subTab" url="getCtrRpddscntContViewPage.do?cont_no=${context.AccDrft.cont_no}&serno=${context.AccDrft.serno}&menuIdTab=queryCtrRpddscntContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
  <%}else{ %>
     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccDrft.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
  <%} %>
</emp:tabGroup>
	
	<div align="center">
		<br> 
		<%if(!"not".equals(isHaveButton)){ %>
			<%if("Y".equals(openNew)){ %>
			<emp:button id="close" label="关闭"/>
			<%}else{ %>
			<emp:button id="return" label="返回列表"/>
			<%} %>
		<%} %>
		<%if(!"".equals(one_key) && one_key != null) {%>
		<emp:button id="returnByOneKey" label="返回" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
