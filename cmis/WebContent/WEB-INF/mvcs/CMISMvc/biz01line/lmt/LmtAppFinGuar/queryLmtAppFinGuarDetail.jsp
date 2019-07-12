<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flow = "";
	if(context.containsKey("flow")){
		flow = (String)context.getDataValue("flow");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<style type="text/css">
.emp_field_text_input_user_name { /****** 长度固定 ******/
	width: 451px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

</style>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
	String dcpa = request.getParameter("dcpa");
	String cus_id = request.getParameter("cus_id");
%>

<script type="text/javascript">
	
	function doReturn() {
		var type = '<%=type%>';
		var url = '<emp:url action="queryLmtAppFinGuarList.do"/>?type='+type;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doImageView(){	//客户信息影像查看
		var data = new Array();
		data['serno'] = LmtAppFinGuar.cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppFinGuar.cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="授信基本信息" id="main_tabs">
			<emp:gridLayout id="LmtAppFinGuarGroup" title="担保公司信息" maxColumn="2">
				<emp:text id="LmtAppFinGuar.serno" label="业务编号" maxlength="40" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly"/>
				<emp:pop id="LmtAppFinGuar.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=Com&returnMethod=returnCus" required="true" colSpan="2" defvalue="<%=cus_id%>"/>
				<emp:text id="LmtAppFinGuar.cus_id_displayname" label="客户名称" colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true"/>
				<emp:text id="LmtAppFinGuar.fin_cls" label="融资类别" maxlength="20" required="false" defvalue="融资性担保公司" readonly="true"/>
				<emp:select id="LmtAppFinGuar.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" defvalue="1" readonly="true"/>
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppFinGuarGroup" title="融资额度信息" maxColumn="2">
				<emp:select id="LmtAppFinGuar.guar_cls" label="担保类别" required="true" dictname="STD_ZB_GUAR_TYPE"/>
				<emp:select id="LmtAppFinGuar.eval_rst" label="评级结果" required="true" dictname="STD_ZB_FINA_GRADE"/>
				<emp:text id="LmtAppFinGuar.guar_bail_multiple" label="担保放大倍数" maxlength="10" required="true" />
				<emp:select id="LmtAppFinGuar.fin_cur_type" label="融资币种" required="false" dictname="STD_ZX_CUR_TYPE"/>
				<emp:text id="LmtAppFinGuar.fin_totl_limit" label="融资总额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppFinGuar.single_quota" label="单户限额" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppFinGuar.fin_totl_spac" label="融资总敞口" maxlength="18" required="true" dataType="Currency" onblur="checkSpac()" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="LmtAppFinGuar.lmt_term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE"/>
				<emp:text id="LmtAppFinGuar.term" label="授信期限" maxlength="2" required="false" />
				<emp:date id="LmtAppFinGuar.app_date" label="申请日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:date id="LmtAppFinGuar.end_date" label="办结日期" required="false" hidden="true"/>
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppFinGuarGroup" title="登记信息" maxColumn="2">
				<emp:pop id="LmtAppFinGuar.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
				<emp:pop id="LmtAppFinGuar.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" readonly="true"/>
				<emp:text id="LmtAppFinGuar.input_id_displayname" label="登记人" required="false" defvalue="$currentUserId" readonly="true"/>
				<emp:text id="LmtAppFinGuar.input_br_id_displayname" label="登记机构" required="false" defvalue="$organNo" readonly="true"/>
				<emp:text id="LmtAppFinGuar.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>
				<emp:text id="LmtAppFinGuar.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" hidden="true"/>
				<emp:text id="LmtAppFinGuar.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAppFinGuar.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAppFinGuar.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
				<emp:select id="LmtAppFinGuar.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="true"/>
				<emp:text id="LmtAppFinGuar.app_type" label="申请类型" required="false" readonly="true" hidden="true"/>
			</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if("yes".equals(dcpa)||"wf".equals(flow)){%>
		<%}else{%>
			<emp:button id="return" label="返回页面"/>
		<%}%>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
</body>
</html>
</emp:page>
