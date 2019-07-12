<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String mem_manuf_type = "";
	if(context.containsKey("IqpMemMana.mem_manuf_type")){
		mem_manuf_type = (String)context.getDataValue("IqpMemMana.mem_manuf_type");
	} 
%>
<emp:page>
<% String cus_id=(String)request.getParameter("cus_id"); %>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doReturn(){
		window.history.go(-1);
	}
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
	/*--user code begin--*/
      //获取成员厂商信息
    function getCusInfo(data){
  	   IqpMemMana.mem_cus_id._setValue(data.cus_id._getValue());
  	   IqpMemMana.mem_cus_id_displayname._setValue(data.cus_name._getValue());
    }

   	window.onload = function(){
   		IqpMemMana.cus_id._obj.addOneButton('viewCus','查看',viewCusInfo);
   		IqpMemMana.mem_cus_id._obj.addOneButton('viewMem','查看',viewMemCusInfo);
   	}

  	//查看客户综合信息
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+IqpMemMana.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow_cus','height=600,width=1024,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//查看客户综合信息
	function viewMemCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+IqpMemMana.mem_cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow_cus','height=600,width=1024,top=70,left=70,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
         <emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
			<emp:gridLayout id="IqpMemManaGroup" title="成员管理" maxColumn="2">
				<emp:text id="IqpMemMana.cus_id" label="中心厂商编号" maxlength="32" required="true" />
			    <emp:text id="IqpMemMana.cus_id_displayname" label="中心厂商名称"   required="false" />		
				<emp:text id="IqpMemMana.mem_cus_id" label="成员厂商编号" required="true" />
				<emp:text id="IqpMemMana.mem_cus_id_displayname" label="成员厂商名称"   required="true" />
				<emp:select id="IqpMemMana.mem_manuf_type" label="成员厂商类别" required="false" dictname="STD_ZB_MANUF_TYPE" />
				<emp:text id="IqpMemMana.term" label="在途期限(日)" maxlength="16" required="false" />
				<emp:text id="IqpMemMana.lmt_quota" label="授信限额" maxlength="18" required="false" dataType="Currency" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
				<emp:checkbox id="IqpMemMana.lmt_type" label="授信业务种类" required="false" dictname="STD_BIZ_TYPE" layout="false" colSpan="2" disabled="true"/>
				<emp:select id="IqpMemMana.status" label="状态" required="false" dictname="STD_ZB_MEM_STATUS" />
				<emp:text id="IqpMemMana.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpMemMana.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
				<emp:date id="IqpMemMana.input_date" label="登记日期" required="false" hidden="true"/>
				<emp:text id="IqpMemMana.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpMemMana.core_corp_duty" label="核心企业责任" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpMemMana.serno" label="业务编号" maxlength="32" required="false" hidden="true"/>
				<emp:text id="IqpMemMana.pk1" label="主键" maxlength="32" required="false" hidden="true"/>
			</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
	
	<div class='emp_gridlayout_title'>供应链下授信情况&nbsp;</div>
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" />
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
	<emp:tab label="订货计划" id="subTab1" url="queryIqpDesbuyPlanList.do?net_agr_no=${context.IqpMemMana.net_agr_no}&mem_cus_id=${context.IqpMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpMemMana.mem_manuf_type}&op=view&menuId=netmanager&subMenuId=IqpDesbuyPlan" initial="false" needFlush="false"/>
	  <emp:tab label="年度购销合同" id="subTab4" url="queryIqpPsaleContList.do?net_agr_no=${context.IqpMemMana.net_agr_no}&mem_cus_id=${context.IqpMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpMemMana.mem_manuf_type}&op=view&menuId=netmanager&subMenuId=IqpPsaleCont" initial="false" needFlush="false"/>
	   <%if("02".equals(mem_manuf_type)){%>
	  <emp:tab label="动产质押监管协议（年度）" id="subTab5" url="queryIqpOverseeAgrList.do?net_agr_no=${context.IqpMemMana.net_agr_no}&mem_cus_id=${context.IqpMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpMemMana.mem_manuf_type}&op=view&menuId=netmanager&subMenuId=IqpOverseeAgr" initial="false" needFlush="false"/>
	  <emp:tab label="银企商合作协议" id="subTab2" url="queryIqpBconCoopAgrList.do?net_agr_no=${context.IqpMemMana.net_agr_no}&mem_cus_id=${context.IqpMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpMemMana.mem_manuf_type}&op=view&menuId=netmanager&subMenuId=IqpBconCoopAgr" initial="false" needFlush="false"/>
	  <emp:tab label="保兑仓协议" id="subTab3" url="queryIqpDepotAgrList.do?net_agr_no=${context.IqpMemMana.net_agr_no}&mem_cus_id=${context.IqpMemMana.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.IqpMemMana.mem_manuf_type}&op=view&menuId=netmanager&subMenuId=iqpdepotagr" initial="false" needFlush="false"/>
	  <%}%> 
	</emp:tabGroup>  
</body>
</html>
</emp:page>