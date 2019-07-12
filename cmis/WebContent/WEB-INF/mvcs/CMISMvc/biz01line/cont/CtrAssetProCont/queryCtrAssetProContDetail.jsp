<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	String isHistory = "";
	if(context.containsKey("isHistory")){
		isHistory = (String)context.getDataValue("isHistory");
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
		var isHistory = '<%=isHistory %>';
		if(isHistory == "history"){
			var url = '<emp:url action="queryCtrAssetProContHistoryList.do"/>';
		}else{
			var url = '<emp:url action="queryCtrAssetProContList.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	/**添加法定到期日与资产申请的到期日期一致，添加客户条线的字段   2014-08-06   邓亚辉*/
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab">
	<emp:tab label="基本信息" id="base_tab">
	<emp:gridLayout id="CtrAssetProContGroup" maxColumn="2" title="资产项目管理">
			<emp:text id="CtrAssetProCont.cont_no" label="项目编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CtrAssetProCont.serno" label="业务编号" maxlength="40" required="false" readonly="true" />
			<emp:text id="CtrAssetProCont.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
			<emp:text id="CtrAssetProCont.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产证券化" readonly="true"/>
			<emp:text id="CtrAssetProCont.pro_name" label="项目名称" maxlength="80" required="false" readonly="true"/>
			<emp:text id="CtrAssetProCont.pro_short_name" label="项目简称" maxlength="80" required="false" readonly="true"/>
			<emp:select id="CtrAssetProCont.pro_type" label="项目类型" required="false" readonly="true" dictname="STD_ZB_ASSET_PRO_TYPE"/>
			<emp:pop id="CtrAssetProCont.pro_org_displayname" label="资产所属机构" required="false" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getProOrgID" readonly="true"/>
			<emp:text id="CtrAssetProCont.pro_short_memo" label="项目简介" maxlength="200" required="false" readonly="true"/>
			<emp:select id="CtrAssetProCont.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="CtrAssetProCont.pro_amt" label="项目金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrAssetProCont.pro_qnt" label="笔数" maxlength="38" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="CtrAssetProCont.ser_date" label="签订日期"  required="true" readonly="true" />
			<emp:date id="CtrAssetProCont.pack_date" label="封包日期" required="false" />
			<!-- modified by lisj 2015-3-11  需求编号：【XD150303017】关于资产证券化的信贷改造 begin -->
			<emp:text id="CtrAssetProCont.issue_qnt" label="发行总量" maxlength="16" required="false" cssElementClass="emp_currency_text_readonly"/>
			<!-- modified by lisj 2015-3-11  需求编号：【XD150303017】关于资产证券化的信贷改造 end -->
			<emp:date id="CtrAssetProCont.issue_date" label="发行日期" required="false" />
			<emp:date id="CtrAssetProCont.int_start_date" label="起息日" required="false" />
			<emp:date id="CtrAssetProCont.end_date" label="法定到期日" required="false" />
			<emp:date id="CtrAssetProCont.final_date" label="终结日期" required="false" />
			<emp:date id="CtrAssetProCont.approve_date" label="审批通过日期" required="false" readonly="true" />
			<emp:text id="CtrAssetProCont.belg_line" label="客户条线" readonly="true" dictname="STD_ZB_BUSILINE" hidden="true"/>
			<emp:select id="CtrAssetProCont.is_rgt_res" label="是否有追索权" required="false" dictname="STD_ZX_YES_NO" />
			
			<emp:text id="CtrAssetProCont.pro_org" label="资产所属机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:text id="CtrAssetProCont.manager_br_id_displayname" label="管理机构" required="true" readonly="true"/>
			<emp:text id="CtrAssetProCont.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="CtrAssetProCont.cont_status" label="项目状态" maxlength="5" hidden="true" />
		   	<emp:text id="CtrAssetProCont.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="CtrAssetProCont.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="CtrAssetProCont.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="CtrAssetProCont.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="CtrAssetProCont.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!"notHave".equals(flag)){%>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
	</emp:tab>
   	<emp:ExtActTab></emp:ExtActTab>
  	</emp:tabGroup>
</body>
</html>
</emp:page>
