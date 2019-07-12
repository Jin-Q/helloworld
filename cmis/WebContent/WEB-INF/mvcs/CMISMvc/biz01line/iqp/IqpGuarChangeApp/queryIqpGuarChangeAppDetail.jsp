<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String showFlow = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("showFlow")){
		showFlow = (String)context.getDataValue("showFlow");
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
		if("queryIqpGuarChangeAppList" == '${context.menuId}'){
			var url = '<emp:url action="queryIqpGuarChangeAppList.do"/>';
	    }else{
	    	var url = '<emp:url action="queryIqpGuarChangeAppHisList.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function onload(){
		IqpGuarChangeApp.cont_no._obj.addOneButton("cont_no","查看",getCont);
    };
    function getCont(){
		var cont_no = IqpGuarChangeApp.cont_no._getValue();
		url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&cont_no='+cont_no+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};   
	
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()"> 
	
	<emp:tabGroup mainTab="base_tab" id="mainTab" > 
	   <emp:tab label="担保变更申请基本信息" id="base_tab" needFlush="true" initial="true" >  
		<emp:gridLayout id="IqpGuarChangeAppGroup" maxColumn="2" title="担保变更申请信息 ">
			<emp:text id="IqpGuarChangeApp.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:date id="IqpGuarChangeApp.apply_date" label="申请日期" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.old_serno" label="原业务编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.prd_id" label="产品编号" maxlength="6" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.prd_id_displayname" label="产品名称" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true" colSpan="2"/>
			<emp:text id="IqpGuarChangeApp.cus_id_displayname" label="客户名称" required="false" readonly="true" colSpan="2"/>
			<emp:select id="IqpGuarChangeApp.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true"/>
			<emp:select id="IqpGuarChangeApp.assure_main_details" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
			<emp:select id="IqpGuarChangeApp.cont_cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.exchange_rate" label="汇率" maxlength="16" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.cont_amt" label="合同金额" maxlength="16" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.same_security_amt" label="视同保证金" maxlength="16" required="false" hidden="true" readonly="true" dataType="Currency"/>
			<emp:text id="IqpGuarChangeApp.risk_open_amt" label="风险敞口金额" maxlength="16" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.risk_open_rate" label="敞口比例" maxlength="10" required="false" dataType="Percent" readonly="true"/>
			<emp:date id="IqpGuarChangeApp.cont_start_date" label="起始日期" required="false" readonly="true"/> 
			<emp:date id="IqpGuarChangeApp.cont_end_date" label="到期日期" required="false" readonly="true"/>
		</emp:gridLayout>     
		<emp:gridLayout id="IqpGuarChangeAppGroup" title="担保修改信息" maxColumn="2">
			<emp:select id="IqpGuarChangeApp.new_assure_main" label="修改后担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" onchange="assure_mainChange" />
			<emp:select id="IqpGuarChangeApp.new_assure_main_details" label="修改后担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" />
			<emp:textarea id="IqpGuarChangeApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" />
        </emp:gridLayout>     
		<emp:gridLayout id="IqpGuarChangeAppGroup" title="登记信息" maxColumn="3">	 	
		    <emp:pop id="IqpGuarChangeApp.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do" returnMethod="getOrgID"/>
			<emp:text id="IqpGuarChangeApp.input_id_displayname" label="登记人" required="true" readonly="true"/>   
			<emp:text id="IqpGuarChangeApp.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>   
			<emp:date id="IqpGuarChangeApp.input_date" label="登记日期" required="true" readonly="true" />
			<emp:select id="IqpGuarChangeApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>     

			<emp:text id="IqpGuarChangeApp.manager_br_id" label="管理机构" required="false" hidden="true" />   
			<emp:text id="IqpGuarChangeApp.input_id" label="登记人" maxlength="20" required="false" hidden="true" /> 
			<emp:text id="IqpGuarChangeApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		</emp:gridLayout> 
		</emp:tab>   
		<emp:ExtActTab></emp:ExtActTab>
		<%if("is".equals(showFlow)){%>
		  <emp:tab label="担保变更审批历史意见" id="pvpFlowHisTab" url="getIqpFlowHis.do" reqParams="instanceId=${context.instanceIdPvp}" needFlush="true"></emp:tab>
		<%}%>
		</emp:tabGroup>   
	
	<div align="center">
		<br>
		<%if(!"noButton".equals(flag)){%>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
