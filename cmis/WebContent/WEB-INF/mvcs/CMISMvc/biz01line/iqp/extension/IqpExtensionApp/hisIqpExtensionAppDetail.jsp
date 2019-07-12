<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String hidden_button= (String)request.getParameter("hidden_button");
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
		var url = '<emp:url action="queryIqpExtensionAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doload(){
		hidden_button = "<%=hidden_button%>";
		IqpExtensionApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpExtensionApp.bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
		document.getElementById("main_tabs").href="javascript:reLoad();";

		var prd_id = IqpExtensionApp.prd_id._getValue();
		if(prd_id < '2' ){	//贷款类
			IqpExtensionApp.base_rate._obj._renderHidden(false);
			IqpExtensionApp.base_rate._obj._renderRequired(true);
		}else{
			IqpExtensionApp.base_rate._obj._renderHidden(true);
			IqpExtensionApp.base_rate._obj._renderRequired(false);
		}
	};
	function reLoad(){
		var url = '<emp:url action="getIqpExtensionAppViewHis.do"/>?menuId=iqp_extension_app_query&restrictUsed=false&serno=${context.IqpExtensionApp.serno}&op=view';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	/** 查看客户信息按钮begin **/
	function getCusForm(){
		var cus_id = IqpExtensionApp.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
	    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function viewAccInfo(){
		var accNo = IqpExtensionApp.bill_no._getValue();
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
		ImageAction('View25');	//业务资料查看
	};
	function ImageAction(image_action){	//目前展期处理参照旧系统
		var data = new Array();
		/**modified by lisj 2014年11月12日 修改展期prd_id字段为'zqyw' begin**/
		data['serno'] = IqpExtensionApp.serno._getValue();	//展期同时取原业务编号和展期编号
		data['cus_id'] = IqpExtensionApp.cus_id._getValue();	//客户码
		//data['prd_id'] = IqpExtensionApp.prd_id._getValue();	//展期业务品种传原业务编号
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
<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="展期申请信息" id="main_tabs">
		<emp:gridLayout id="IqpExtensionAppGroup" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionApp.bill_no" label="原借据编号" required="true"  readonly="true" />
			<emp:text id="IqpExtensionApp.cont_no" label="原合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpExtensionApp.fount_serno" label="原流水号" hidden="true"  readonly="true" />
			<emp:text id="IqpExtensionApp.prd_id" label="业务类型" hidden="true"  readonly="true" />
			<emp:text id="IqpExtensionApp.cus_id" label="客户码"  required="true" readonly="true" />
			<emp:text id="IqpExtensionApp.cus_id_displayname" label="客户名称" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="IqpExtensionApp.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpExtensionApp.loan_amt" label="贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpExtensionApp.loan_balance" label="贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpExtensionApp.rate" label="执行利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" colSpan="2"/>
			<emp:date id="IqpExtensionApp.start_date" label="起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionApp.end_date" label="止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionAppGroup" title="展期信息" maxColumn="2">
			<emp:text id="IqpExtensionApp.serno" label="业务编号" maxlength="40" required="true" colSpan="2" />
			<emp:text id="IqpExtensionApp.extension_amt" label="展期金额" maxlength="18" required="true" readonly="true" dataType="Currency" />
			<emp:date id="IqpExtensionApp.extension_date" label="展期到期日期" required="true"  />
			<emp:text id="IqpExtensionApp.base_rate" label="基准利率(年)" maxlength="16" readonly="true" required="true" dataType="Rate" />
			<emp:text id="IqpExtensionApp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" />
			<emp:textarea id="IqpExtensionApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>		
		<emp:gridLayout id="IqpExtensionAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="IqpExtensionApp.manager_id_displayname" label="责任人" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpExtensionApp.manager_br_id_displayname" label="责任机构"  required="true" url="querySOrgPop.do?restrictUsed=false" 
			 returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpExtensionApp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="IqpExtensionApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:date id="IqpExtensionApp.input_date" label="登记日期" required="true"  readonly="true" />	
			<emp:select id="IqpExtensionApp.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS"  readonly="true" />
			<emp:text id="IqpExtensionApp.manager_br_id" label="责任机构"  required="true" hidden="true"/>
			<emp:text id="IqpExtensionApp.manager_id" label="责任人" required="true" hidden="true"  />
			<emp:text id="IqpExtensionApp.input_id" label="登记人" required="true"  hidden="true" />
			<emp:text id="IqpExtensionApp.input_br_id" label="登记机构" required="true"  hidden="true"  />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
	</emp:tab>
	<emp:tab label="原合同信息" id="subTab" 
	url="getCtrLoanContViewPage.do?cont_no=${context.IqpExtensionApp.cont_no}&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
	 initial="false" needFlush="true"/> 
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>