<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String restrictUsed = "";
	if(context.containsKey("restrictUsed")){
		restrictUsed = (String)context.getDataValue("restrictUsed");
	}
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	String viewtype="";
	if(context.containsKey("viewtype")){
		viewtype = (String)context.getDataValue("viewtype");
	}
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	String sub_button= (String)request.getParameter("sub_button");
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
		var url = '<emp:url action="queryIqpExtensionAgrList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function doReturn1() {
		window.close();
	};	
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	/*--user code begin--*/
	/** 查看客户信息按钮begin **/
	function doLoad(){
		sub_button = "<%=sub_button%>";
		IqpExtensionAgr.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpExtensionAgr.fount_bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
		document.getElementById("main_tabs").href="javascript:reLoad();";
		if(sub_button == 'true'){			
			document.getElementById('button_return').style.display = 'none';
		}

		var prd_id = IqpExtensionAgr.prd_id._getValue();
		if(prd_id < '2' ){	//贷款类
			IqpExtensionAgr.base_rate._obj._renderHidden(false);
			IqpExtensionAgr.base_rate._obj._renderRequired(true);
		}else{
			IqpExtensionAgr.base_rate._obj._renderHidden(true);
			IqpExtensionAgr.base_rate._obj._renderRequired(false);
		}
	};
	function reLoad(){
		var url = '<emp:url action="getIqpExtensionAgrViewPage.do"/>?menuIdTab=iqp_extension_agr&serno=${context.IqpExtensionAgr.serno}&op=view&sub_button='+sub_button+'&restrictUsed=false';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function getCusForm(){
		var cus_id = IqpExtensionAgr.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
	    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function viewAccInfo(){
		var accNo = IqpExtensionAgr.fount_bill_no._getValue();
		if(accNo==null||accNo==''){
			alert('借据编号为空！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+accNo;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};
	/** 查看客户信息按钮end **/
	
	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View25');
	};
	function ImageAction(image_action){
		var data = new Array();
		/**modified by lisj 2014年11月12日 修改展期prd_id字段为'zqyw' begin**/
		data['serno'] = IqpExtensionAgr.serno._getValue();	//展期同时取原业务编号和展期编号
		data['cus_id'] = IqpExtensionAgr.cus_id._getValue();	//客户码
		//data['prd_id'] = IqpExtensionAgr.prd_id._getValue();	//展期业务品种传原业务编号
		data['prd_id'] = 'zqyw';	//业务品种
		/**modified by lisj 2014年11月12日 修改展期prd_id字段为'zqyw' end**/
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="展期协议信息" id="main_tabs">
	<emp:gridLayout id="IqpExtensionAgrGroup" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionAgr.fount_bill_no" label="原借据编号" maxlength="40" required="true" />
			<emp:text id="IqpExtensionAgr.fount_cont_no" label="原合同编号" maxlength="40" required="true" />
			<emp:text id="IqpExtensionAgr.fount_serno" label="原流水号" hidden="true"  readonly="true" />
			<emp:text id="IqpExtensionAgr.prd_id" label="业务类型" hidden="true"  readonly="true" />
			<emp:text id="IqpExtensionAgr.cus_id" label="客户码" maxlength="40" required="true" />
			<emp:text id="IqpExtensionAgr.cus_id_displayname" label="客户名称" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="IqpExtensionAgr.fount_cur_type" label="原币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2"/>
			<emp:text id="IqpExtensionAgr.fount_loan_amt" label="原贷款金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpExtensionAgr.fount_loan_balance" label="原贷款余额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpExtensionAgr.fount_rate" label="原执行利率(年)" maxlength="16" required="true" dataType="Rate" colSpan="2" />
			<emp:date id="IqpExtensionAgr.fount_start_date" label="原起贷日期" required="true" />
			<emp:date id="IqpExtensionAgr.fount_end_date" label="原止贷日期" required="true" />
	</emp:gridLayout>
	<emp:gridLayout id="IqpExtensionAgrGroup" title="展期协议信息" maxColumn="2">
			<emp:text id="IqpExtensionAgr.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpExtensionAgr.agr_no" label="协议编号" maxlength="40" required="true" />
			<emp:text id="IqpExtensionAgr.cn_cont_no" label="中文合同编号" maxlength="40" required="true" />
			<emp:date id="IqpExtensionAgr.sign_date" label="签订日期" required="false" />
			<emp:text id="IqpExtensionAgr.extension_amt" label="展期金额" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="IqpExtensionAgr.extension_date" label="展期到期日期" required="true" />
			<emp:text id="IqpExtensionAgr.base_rate" label="基准利率(年)" maxlength="16" required="true" dataType="Rate" />
			<emp:text id="IqpExtensionAgr.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" />
			<emp:textarea id="IqpExtensionAgr.memo" label="备注" maxlength="250" required="false" colSpan="2" />
	</emp:gridLayout>		
	<emp:gridLayout id="IqpExtensionAgrGroup" maxColumn="2" title="登记信息">
			<emp:text id="IqpExtensionAgr.manager_id_displayname" label="责任人" required="true" />
			<emp:text id="IqpExtensionAgr.manager_br_id_displayname" label="责任机构"  required="true" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpExtensionAgr.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="IqpExtensionAgr.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="IqpExtensionAgr.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpExtensionAgr.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpExtensionAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpExtensionAgr.input_br_id" label="登记机构" required="false" hidden="true"/>
			<emp:date id="IqpExtensionAgr.input_date" label="登记日期"  required="false" />
			<emp:select id="IqpExtensionAgr.status" label="状态" required="false" dictname="STD_ZB_CTRLOANCONT_TYPE" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!"false".equals(restrictUsed)&&!"out".equals(viewtype)){%>
		<emp:button id="return" label="返回列表"/>
		<%} %>
		<%if(!"false".equals(restrictUsed)&&"out".equals(viewtype)){ %>
		    <emp:button id="return1" label="关闭"/>
		<%}%>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
	</emp:tab>
	<emp:tab label="原合同信息" id="subTab" 
	url="getCtrLoanContViewPage.do?cont_no=${context.IqpExtensionAgr.fount_cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
	 initial="false" needFlush="true"/> 
	</emp:tabGroup>
</body>
</html>
</emp:page>
