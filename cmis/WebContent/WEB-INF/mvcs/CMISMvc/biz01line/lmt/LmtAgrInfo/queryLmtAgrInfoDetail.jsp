<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = request.getParameter("type");
%>
<script type="text/javascript">
	
	function doReturn() {
		var url = "<emp:url action='queryLmtAgrInfoList.do'/>&subConndition=${context.subConndition}&cus_id=${context.cus_id}&isShow=${context.isShow}&menuId=crd_agr";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onLoad(){
		if("none"=='${context.show}'){  //如果show字段为none则隐藏返回按钮 
			document.all.button_return.style.display = 'none';
		}
		//客户码增加查看按钮
		LmtAgrInfo.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrInfo.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function doClose(){
		window.close();
	}

	function doImageView(){	//客户信息影像查看
		var data = new Array();
		data['serno'] = LmtAgrInfo.cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAgrInfo.cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="协议信息" id="main_tabs">
			<emp:gridLayout id="LmtAgrInfoGroup" title="授信协议信息" maxColumn="2">
				<emp:text id="LmtAgrInfo.serno" label="业务编号" maxlength="40" required="true" />
				<emp:text id="LmtAgrInfo.agr_no" label="协议编号" maxlength="40" required="false"/>
				<emp:text id="LmtAgrInfo.cus_id" label="客户码" maxlength="30" required="true" />
				<emp:select id="LmtAgrInfo.biz_type" label="授信业务类型" required="true" dictname="STD_ZB_BIZ_TYPE"/>
				<emp:text id="LmtAgrInfo.cus_id_displayname" label="客户名称" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
				
				<emp:select id="LmtAgrInfo.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" /> 
				<emp:text id="LmtAgrInfo.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrInfo.crd_bal_amt" label="授信余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true" colSpan="2"/>
				<emp:text id="LmtAgrInfo.totl_amt" label="非低风险授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
				<emp:text id="LmtAgrInfo.crd_cir_amt" label="非低风险循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrInfo.crd_one_amt" label="非低风险一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<%if(!"GRP".equalsIgnoreCase((String)context.getDataValue("origin"))){ %>
				<emp:text id="LmtAgrInfo.lrisk_total_amt" label="低风险授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
				<emp:text id="LmtAgrInfo.lrisk_cir_amt" label="低风险循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrInfo.lrisk_one_amt" label="低风险一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<%} %>
				<emp:date id="LmtAgrInfo.start_date" label="授信起始日" required="false" />
				<emp:date id="LmtAgrInfo.end_date" label="授信到期日" required="false" />
				<emp:textarea id="LmtAgrInfo.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
				<emp:text id="LmtAgrInfo.grp_agr_no" label="集团授信编号" maxlength="40" hidden="true" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAgrInfoGroup_input" title="登记信息" maxColumn="2">
				<emp:text id="LmtAgrInfo.manager_id_displayname" label="责任人" required="false" />
				<emp:text id="LmtAgrInfo.manager_br_id_displayname" label="责任机构" required="false" />
				<emp:text id="LmtAgrInfo.input_id_displayname" label="登记人" required="true"/>
				<emp:text id="LmtAgrInfo.input_br_id_displayname" label="登记机构" required="true"/>
				<emp:text id="LmtAgrInfo.input_date" label="登记日期" maxlength="10" required="true" />
				<emp:text id="LmtAgrInfo.input_id" label="登记人" maxlength="20" required="true" hidden="true" />
				<emp:text id="LmtAgrInfo.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" />
				<emp:text id="LmtAgrInfo.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAgrInfo.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtAgrInfo.restrict_tab" label="restrict_tab" defvalue="false" hidden="true"/>
			</emp:gridLayout>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if("surp".equals(type)){ %>
		<emp:button label="关闭" id="close" />
		<%}else{ %>
		<emp:button label="返回列表" id="return" />
		<%} %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
</body>
</html>
</emp:page>
