<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String mem_manuf_type = "";
	if(context.containsKey("IqpAppMemMana.mem_manuf_type")){
		mem_manuf_type = (String)context.getDataValue("IqpAppMemMana.mem_manuf_type");
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
//查看授信台账信息
function doViewLmtAgrDetails() {
	var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code']);
	if (paramStr != null) {
		var url = '<emp:url action="getLmtAgrDetailsViewPage.do"/>?'+paramStr+"&subConndition=${context.subConndition}&menuId=crd_ledger&showButton=N";
		url = EMPTools.encodeURI(url);
		window.open(url,"agrInfoPage","height=650,width=1000,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=1,location=no,status=no");
		//window.location = url;
	} else {
		alert('请先选择一条记录！'); 
	}
};		
	//function doReturn() {
	//	var url = '<emp:url action="queryIqpAppMemManaList.do"/>?serno=${context.IqpAppMemMana.serno}&op=view&showMem=no';
	//  url = EMPTools.encodeURI(url);
	//	window.location=url;
	//};
	
	function doBack(){
		window.history.go(-1);
    }
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
   <emp:tabGroup mainTab="base_tab" id="mainTab" >
     <emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
       <emp:form id="submitForm" action="updateIqpAppMemManaRecord.do" method="POST">
		<emp:gridLayout id="IqpAppMemManaGroup" maxColumn="2" title="网络成员信息">
		<emp:pop id="IqpAppMemMana.mem_cus_id" label="成员企业客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE='BL200'&returnMethod=getCusInfo"  required="true" />
			<emp:text id="IqpAppMemMana.mem_cus_id_displayname" label="成员厂商名称"  required="true" readonly="true"/>
			<emp:select id="IqpAppMemMana.mem_manuf_type" label="成员企业类别" required="true" dictname="STD_ZB_MANUF_TYPE"/>
			
			<emp:text id="IqpAppMemMana.term" label="在途期限（天）" maxlength="10" required="false" />
			<emp:select id="IqpAppMemMana.status" label="成员变更状态" dictname="STD_ZB_MEN_TYPE" readonly="true" required="false" />
			<emp:text id="IqpAppMemMana.lmt_quota" label="授信限额（元）" maxlength="16" required="true" dataType="Currency" />
		    <emp:checkbox id="IqpAppMemMana.lmt_type" label="授信业务种类" dictname="STD_BIZ_TYPE" layout="false" colSpan="2" required="false" disabled="true"/>
		    <emp:text id="IqpAppMemMana.serno" label="业务编号" maxlength="40"  required="true" hidden="true"/> 
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="back" label="返回" />
		</div>
	   </emp:form>
	   
	<div class='emp_gridlayout_title'>供应链下授信情况&nbsp;</div>
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtAgrDetailsList" pageMode="false" url="">
		<emp:text id="limit_code" label="额度编码" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="cus_type" label="客户类型" hidden="true"/>
		<emp:text id="core_corp_duty" label="核心企业责任" dictname="STD_ZB_CORP_DUTY"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="lmt_status" label="额度状态" dictname="STD_LMT_STATUS"/>
	</emp:table>
	</emp:tab>
	  <emp:tab label="订货计划" id="subTab1" url="queryIqpAppDesbuyPlanList.do?serno=${context.IqpAppMemMana.serno}&mem_cus_id=${context.IqpAppMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpAppMemMana.mem_manuf_type}&menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=view" initial="false" needFlush="true"/>
	  <emp:tab label="年度购销合同" id="subTab4" url="queryIqpAppPsaleContList.do?serno=${context.IqpAppMemMana.serno}&mem_cus_id=${context.IqpAppMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpAppMemMana.mem_manuf_type}&menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=view" initial="false" needFlush="true"/>
	   <%if("02".equals(mem_manuf_type)){%>
	  <emp:tab label="动产质押监管协议（年度）" id="subTab5" url="queryIqpAppOverseeAgrList.do?serno=${context.IqpAppMemMana.serno}&mem_cus_id=${context.IqpAppMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpAppMemMana.mem_manuf_type}&menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=view" initial="false" needFlush="true"/>
	  <emp:tab label="银企商合作协议" id="subTab2" url="queryIqpAppBconCoopAgrList.do?serno=${context.IqpAppMemMana.serno}&mem_cus_id=${context.IqpAppMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpAppMemMana.mem_manuf_type}&menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=view" initial="false" needFlush="true"/>
	  <emp:tab label="保兑仓协议" id="subTab3" url="queryIqpAppDepotAgrList.do?serno=${context.IqpAppMemMana.serno}&mem_cus_id=${context.IqpAppMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpAppMemMana.mem_manuf_type}&menuId=${context.menuId}&subMenuId=${context.subMenuId}&op=view" initial="false" needFlush="true"/>
	  <%}%> 
   </emp:tabGroup>
</body> 
</html>
</emp:page>
