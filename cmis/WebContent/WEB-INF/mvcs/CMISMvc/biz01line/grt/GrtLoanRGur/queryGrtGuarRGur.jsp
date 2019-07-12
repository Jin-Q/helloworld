<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String flag ="";
if(context.containsKey("flag")){
	flag = (String)context.getDataValue("flag"); 
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
		var guar_cont_type = GrtGuarCont.guar_cont_type._getValue();
		var action="";
		if(guar_cont_type=="01"){
			action="zg";
		}else{
			action="yb";
		}
		var url = '<emp:url action="queryGrtGuarContList.do"/>?action='+action;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
       window.close();
	}
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">	
		<emp:gridLayout id="GrtGuarContGroup" maxColumn="2" title="基本信息">
			<emp:pop id="GrtGuarCont.cus_id" label="借款人客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="true"/>
			<emp:text id="GrtGuarCont.cus_id_displayname" label="借款人客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
		    <emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文合同编号" maxlength="60" required="true"  readonly="true"/>
			<emp:select id="GrtGuarCont.guar_cont_type" label="担保合同类型" required="false" dictname="STD_GUAR_CONT_TYPE"  readonly="true"/>
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" required="false" dictname="STD_GUAR_TYPE"  readonly="true" />
			<emp:select id="GrtGuarCont.lmt_grt_flag" label="是否授信项下" required="true" dictname="STD_ZX_YES_NO"  readonly="true"/>
			<emp:text id="GrtGuarCont.agr_no" label="授信协议编号" maxlength="40" required="false"  readonly="true"/>
			<emp:select id="GrtGuarCont.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE"  readonly="true"/>
			<emp:text id="GrtGuarCont.guar_amt" label="担保金额" maxlength="18" required="false"  readonly="true" dataType="Currency"/>
			<emp:date id="GrtGuarCont.guar_start_date" label="担保起始日" required="false"  readonly="true"/>
			<emp:date id="GrtGuarCont.guar_end_date" label="担保终止日" required="false"  readonly="true"/>
			<emp:date id="GrtGuarCont.sign_date" label="签订日期" required="false" readonly="true"/>
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" required="false" dictname="STD_CONT_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="GrtGuarCont.memo" label="备注信息" maxlength="200" required="false"  readonly="true" />
			<emp:select id="GrtGuarCont.guar_model" label="担保模式" required="false" dictname="STD_GUAR_MODEL" hidden="true" />
			<emp:text id="GrtGuarCont.guar_term" label="担保期限" maxlength="0" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.limit_code" label="授信台账编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarCont.guar_term_type" label="担保期限类型" maxlength="2" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="GrtGuarContGroup" maxColumn="2" title="登记信息">
			<emp:pop id="GrtGuarCont.manager_id_displayname" label="主管客户经理" required="true" hidden="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"  readonly="true"/>
			<emp:pop id="GrtGuarCont.manager_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" readonly="true"/>
			<emp:text id="GrtGuarCont.input_id_displayname" label="登记人" required="false" readonly="true" hidden="false" />
			<emp:text id="GrtGuarCont.input_br_id_displayname" label="登记机构" required="false" readonly="true" hidden="false" />
			<emp:text id="GrtGuarCont.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:text id="GrtGuarCont.manager_id" label="主管客户经理" required="false" readonly="true" hidden="true" />
			<emp:text id="GrtGuarCont.manager_br_id" label="主管机构" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarCont.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="GrtGuarCont.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		</emp:tab>
  <emp:ExtActTab></emp:ExtActTab>
</emp:tabGroup>

	<div align="center">
		<br>
		<%if(flag.equals("loan")){%>
		   <emp:button id="close" label="关闭"/>
		<%}else{ %>
		  <emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
