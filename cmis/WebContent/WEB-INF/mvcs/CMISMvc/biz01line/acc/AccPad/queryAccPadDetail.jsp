<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String isHaveButton = "";
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("isHaveButton")){
		isHaveButton = (String)context.getDataValue("isHaveButton");
	} 
	String biz_type = "";
	String prd_id = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	}
	if(context.containsKey("AccPad.prd_id")){
		prd_id = (String)context.getDataValue("AccPad.prd_id");
	}
	String dunCount = "";
	if(context.containsKey("dunCount")){
		dunCount = (String)context.getDataValue("dunCount");
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

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryAccPadList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function onload(){
		AccPad.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
    };
    function getCusForm(){
		var cus_id = AccPad.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	 function doReturnByOneKey() {
			var cus_id  =AccPad.cus_id._obj.element.value;
			var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
			url = EMPTools.encodeURI(url);
			window.location=url;
		};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/	
</script>
</head>
<body class="page_content" onload="onload()">
 <emp:tabGroup mainTab="base_tab" id="mainTab">
   <emp:tab label="垫款台账信息" id="base_tab" needFlush="true" initial="true" >
	<emp:gridLayout id="AccPadGroup" title="基本信息" maxColumn="2">
	        <emp:text id="AccPad.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccPad.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="AccPad.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccPad.prd_id_displayname" label="产品名称" required="false" />
			<emp:text id="AccPad.cus_id" label="客户码" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="AccPad.cus_id_displayname" label="客户名称" required="false" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="AccPad.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="AccPad.pad_bill_no" label="垫款业务借据编号" maxlength="40" required="false" hidden="true"/>
	    </emp:gridLayout>		
	    <emp:gridLayout id="AccPadGroup" title="金额信息" maxColumn="2">	
	        <emp:select id="AccPad.pad_type" label="垫款种类" required="false" dictname="STD_ZB_PAD_TYPE"/>
			<emp:select id="AccPad.pad_cur_type" label="垫款币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccPad.pad_amt" label="垫款金额" maxlength="18" required="false" dataType="Currency" />
	        <emp:text id="AccPad.pad_bal" label="垫款余额" maxlength="18" required="false" dataType="Currency" />
	    </emp:gridLayout>		
	    <emp:gridLayout id="AccPadGroup" title="日期信息" maxColumn="2">	
			<emp:date id="AccPad.pad_date" label="垫款日期" required="false" />
			<emp:date id="AccPad.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccPad.writeoff_date" label="核销日期" required="false" />
			<emp:text id="AccPad.acc_day" label="日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="AccPad.acc_year" label="年份" maxlength="5" required="false" hidden="true"/>
			<emp:text id="AccPad.acc_mon" label="月份" maxlength="5" required="false" hidden="true"/>
	    </emp:gridLayout>		
	    <emp:gridLayout id="AccLoanGroup" maxColumn="2" title="风险分类信息">
		    <emp:select id="AccPad.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:select id="AccPad.twelve_cls_flg" label="十二级分类标志" required="false" dictname="STD_ZB_TWELVE_CLASS"/>
		    <emp:date id="AccPad.twelve_class_time" label="十二级分类时间" required="false" />
		</emp:gridLayout>
	    <emp:gridLayout id="AccPadGroup" title="其他信息" maxColumn="2">	
			<emp:text id="AccPad.manager_br_id_displayname" label="管理机构" required="false" />
			<emp:text id="AccPad.fina_br_id_displayname" label="账务机构" required="false" />
			<emp:text id="AccPad.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="AccPad.fina_br_id" label="账务机构" maxlength="20" required="false" hidden="true"/>
			<emp:select id="AccPad.accp_status" label="台账状态" required="false" dictname="STD_ZB_ACC_TYPE"/>
	  </emp:gridLayout>
   </emp:tab>
   <%if("600020".equals(prd_id)){ %>
	    <emp:tab label="资产转受让合同信息" id="assetContSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrAssetstrsfContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
		<%}else if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){%>
		<emp:tab label="转贴现合同信息" id="rpContsubTab" url="getCtrRpddscntContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrRpddscntContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	    <%}else if("8".equals(biz_type) && ("300021".equals(prd_id)||"300020".equals(prd_id))){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else if("8".equals(biz_type) && !"300021".equals(prd_id) && !"300020".equals(prd_id)){ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=yztqueryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/>
	    <%}else if("300021".equals(prd_id)||"300020".equals(prd_id)){%> 
	     <emp:tab label="合同信息" id="subTab" url="getCtrLoanContForDiscViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}else{%>
	     <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.AccPad.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%}%>

	<%if("Y".equals(dunCount)){ %>
      <emp:tab label="催收及回执" id="dunTab" url="queryPspDunningRecordList.do?bill_no=${context.AccPad.bill_no}&menuIdTab=PspDunningTaskInfo&subMenuId=PspDunningRecord&op=view" initial="false" needFlush="true"/>
      <%} %>
</emp:tabGroup>		
	<div align="center">
		<br>
		<%if(!"not".equals(isHaveButton)){ %>
		<emp:button id="return" label="返回列表"/>
		<%}%>
		<%if(!"".equals(one_key) && one_key != null) {%>
		<emp:button id="returnByOneKey" label="返回" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
