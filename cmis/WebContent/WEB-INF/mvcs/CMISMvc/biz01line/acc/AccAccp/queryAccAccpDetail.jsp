<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	String isHaveButton = "";
	String openNew = "";
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
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

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var biz_type = '<%=biz_type%>';
		if(biz_type!=''){
			var url = '<emp:url action="queryAccAccpList.do"/>?menuId=accAccpList&biz_type='+'<%=biz_type%>';
			url = EMPTools.encodeURI(url);  
			window.location=url;
		}else{
			history.go(-1);
		}
	};
	
	/*--user code begin--*/
	function onload(){
		var accp_status = AccAccp.accp_status._getValue();
		if(accp_status =="6"){
			AccAccp.paydate._renderHidden(true);
		}
	};
	
	function doClose(){
		window.close();
	}
	
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccAccp.fount_serno._getValue();	//业务申请编号，不是出账流水号
		data['cus_id'] = AccAccp.daorg_cusid._getValue();	//客户码
		data['prd_id'] = AccAccp.prd_id._getValue();	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =AccAccp.daorg_cusid._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
</script>
</head>
<body class="page_content" onload="onload()">
<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="台账信息" id="base_tab" needFlush="true" initial="true" >
	    <emp:gridLayout id="AccAccpGroup" title="基本信息" maxColumn="2">
	    	<emp:text id="AccAccp.bill_no" label="借据编号" maxlength="40" required="false" />
	        <emp:text id="AccAccp.cont_no" label="合同编号" maxlength="40" required="false" />				
			<emp:select id="AccAccp.bill_type" label="票据类型" required="false" dictname="STD_DRFT_TYPE" />
			<emp:text id="AccAccp.porder_no" label="汇票号码" maxlength="40" required="false" />
			<emp:text id="AccAccp.utakeover_sign" label="不得转让标记" maxlength="5" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="AccAccp.is_ebill" label="是否电子票据" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="AccAccp.daorg_cusid" label="出票人客户码" maxlength="30" required="false" colSpan="2"/>
			<emp:text id="AccAccp.daorg_cus_name" label="出票人名称" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="AccAccp.drwr_org_code" label="出票人组织机构代码" maxlength="20" required="false" hidden="true"/>
			<emp:text id="AccAccp.daorg_acct" label="出票人开户行账号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="AccAccp.daorg_no" label="出票人开户行行号" maxlength="40" required="false" colSpan="2" hidden="true"/>
			<emp:text id="AccAccp.daorg_name" label="出票人开户行行名" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly" hidden="true"/>
			<emp:select id="AccAccp.aorg_type" label="承兑行类型" required="false" dictname="STD_AORG_ACCTSVCR_TYPE" colSpan="2"/>
			<emp:text id="AccAccp.aorg_no" label="承兑行行号" maxlength="20" required="false" colSpan="2"/>  
			<emp:text id="AccAccp.aorg_name" label="承兑行名称" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="AccAccp.paorg_acct_no" label="收款人账号" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="AccAccp.pyee_name" label="收款人名称" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="AccAccp.paorg_no" label="收款人开户行行号" maxlength="20" required="false" colSpan="2"/>
			<emp:text id="AccAccp.paorg_name" label="收款人开户行行名" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		 	<emp:text id="AccAccp.serno" label="出账业务编号" maxlength="40" required="false" hidden="true" />
		 
		 </emp:gridLayout>
		 <emp:gridLayout id="AccAccpGroup" title="金额信息" maxColumn="2">				
			<emp:select id="AccAccp.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccAccp.drft_amt" label="票面金额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccAccp.pad_rate" label="垫款利率（年）" maxlength="16" required="false" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccAccp.pad_amt" label="垫款金额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="AccAccp.exchange_rate" label="汇率" maxlength="16" required="false" hidden="true"/>
		 </emp:gridLayout>
		 <emp:gridLayout id="AccAccpGroup" title="日期信息" maxColumn="2">		
			<emp:date id="AccAccp.isse_date" label="出票日期" required="false" />
			<emp:date id="AccAccp.porder_end_date" label="到期日期" required="false" />
			<emp:date id="AccAccp.paydate" label="转垫款日" required="false" hidden="true"/>
			<emp:date id="AccAccp.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccAccp.writeoff_date" label="核销日期" required="false" hidden="true"/>
			<emp:text id="AccAccp.acc_day" label="日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="AccAccp.acc_year" label="年份" maxlength="5" required="false" hidden="true"/>
			<emp:text id="AccAccp.acc_mon" label="月份" maxlength="5" required="false" hidden="true"/>
		 </emp:gridLayout>
		 <emp:gridLayout id="AccLoanGroup" maxColumn="2" title="风险分类信息">
		    <emp:select id="AccAccp.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccAccp.twelve_cls_flg" label="十二级分类标志" required="false" dictname="STD_ZB_TWELVE_CLASS"/>
		    <emp:date id="AccAccp.twelve_class_time" label="十二级分类时间" required="false" />
		 </emp:gridLayout>
		 <emp:gridLayout id="AccAccpGroup" title="其他信息" maxColumn="2">			
			<emp:text id="AccAccp.manager_br_id_displayname" label="管理机构" required="false" />
			<emp:text id="AccAccp.fina_br_id_displayname" label="账务机构" required="false" />
			<emp:text id="AccAccp.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="AccAccp.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:select id="AccAccp.accp_status" label="台帐状态" required="false" dictname="STD_ZB_ACC_TYPE"/>
			<emp:text id="AccAccp.fount_serno" label="业务申请编号" hidden="true" />
			<emp:text id="AccAccp.prd_id" label="产品编号" hidden="true" />
	     </emp:gridLayout>
	</emp:tab> 
	<emp:tab label="合同信息" id="subTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccAccp.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
</emp:tabGroup> 
	<div align="center">
		<br>
		<%if(!"not".equals(isHaveButton)){ %>
			<%if("Y".equals(openNew)){ %>
			<emp:button id="close" label="关闭"/>
			<%}else{ %>
			<emp:button id="return" label="返回列表"/>
			<%} %>
			<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		<%} %>
		<%if(!"".equals(one_key) && one_key != null) {%>
		<emp:button id="returnByOneKey" label="返回" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
