<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kColl = (KeyedCollection)context.getDataElement("PspCheckTask");
	String check_type = "";//检查类型
	if(kColl.containsKey("check_type")){
		check_type = (String)kColl.getDataValue("check_type");
	}
	/*贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2014-12-31 start*/
	String check_freq = "";//检查频率
	if(kColl.containsKey("check_freq")){
		check_freq = (String)kColl.getDataValue("check_freq");
	}
	/*贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2014-12-31 end*/
	String task_type = "";//任务类型
	if(kColl.containsKey("task_type")){
		task_type = (String)kColl.getDataValue("task_type");
	}
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	String psp_cus_type = "";//个人客户类型（经营性、消费性）
	if(kColl.containsKey("psp_cus_type")){
		psp_cus_type = (String)kColl.getDataValue("psp_cus_type");
	}
	String proFlag = "";//判断是否显示项目tab
	if(context.containsKey("proFlag")){
		proFlag = (String)context.getDataValue("proFlag");
	}
	String openType = "";//若是集团下查看成员，则有值
	if(context.containsKey("openType")){
		openType = (String)context.getDataValue("openType");
	}
	// modified by yangzy 2015/04/10 需求：XD150325024，集中作业扫描岗权限改造 start
	// added by yangzy 2014/09/01 扫描岗增加影像扫描功能 start
	String roles = "";
	if(context.containsKey("roles")){
		roles = (String)context.getDataValue("roles");
	} 
	String flagSM = "";
	// modified by yangzy 2015-6-25 贷后影像扫描由客户经理扫描 start
	if(roles.contains("3002")||roles.contains("3003") ){
		flagSM = "1";
	}
	// modified by yangzy 2015-6-25 贷后影像扫描由客户经理扫描 end
	// added by yangzy 2014/09/01  扫描岗增加影像扫描功能 end
	String viewfrom = "";
	if(context.containsKey("viewfrom")){
		viewfrom = (String)context.getDataValue("viewfrom");
	} 
	// modified by yangzy 2015/04/10 需求：XD150325024，集中作业扫描岗权限改造 end
	// added by yangzy 2014/12/11  需求:XD141015069,小微全额抵押授信业务改造 start
	String task_id = "";//任务流水
	String checkMicFlag = "";//任务流水
	if(kColl.containsKey("task_id")){
		task_id = (String)kColl.getDataValue("task_id");
		if(task_id.startsWith("SQN9")){
			checkMicFlag = "YES";
		}
	}
	// added by yangzy 2014/12/11 需求:XD141015069,小微全额抵押授信业务改造 end
	/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  begin**/
	String batch_task_type ="";//批量任务类型
	if(kColl.containsKey("batch_task_type")){
		batch_task_type = (String)kColl.getDataValue("batch_task_type");
	}
	String batch_task_flag ="";
	if(context.containsKey("batch_task_flag")){
		batch_task_flag = (String)context.getDataValue("batch_task_flag");
	}
	/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  end**/
	/**add by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
	String task_create_date ="";
	if(kColl.containsKey("task_create_date")){
		task_create_date = (String)kColl.getDataValue("task_create_date");
	}
	/**add by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
	
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var menuId = "${context.menuId}" ;
		if(menuId == 'expert_check_task_his' || menuId == 'period_check_task_his' || menuId == 'first_check_task_his'){
			url = '<emp:url action="queryPspCheckTaskHistoryList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}';
		}else{
			url = '<emp:url action="queryPspCheckTaskList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doOnLoad(){
		PspCheckTask.cus_id._obj.addOneButton("cus_id","查看",getCusForm);

		//控制检查情况信息显示
		//showCheckInfo();

		//控制合作方情况说明显示
		showRemarks();

		//根据检查类型和任务类型，判断检查方案
		var check_type = PspCheckTask.check_type._getValue();
		var task_type = PspCheckTask.task_type._getValue();
		if(task_type == '03'){
			PspCheckTask.psp_cus_type._obj._renderHidden(false);
		}

		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}
		/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  begin**/
		showCheckPspBatchTaskInfo();
		/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  end**/
	}

	/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  begin**/
	function showCheckPspBatchTaskInfo(){
		var task_type = PspCheckTask.task_type._getValue();
		if(task_type == '09'){
			PspCheckTask.check_type._obj._renderHidden(true);
			PspCheckTask.cus_id._obj._renderHidden(true);
			PspCheckTask.cus_id_displayname._obj._renderHidden(true);
			PspCheckTask.task_divis_person_displayname._obj._renderHidden(true);
			PspCheckTask.task_divis_org_displayname._obj._renderHidden(true);
			PspCheckTask.task_huser_displayname._obj._renderHidden(true);
			PspCheckTask.task_horg_displayname._obj._renderHidden(true);
		}
	};
	/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  end**/
	
	function getCusForm(){
		var cus_id = PspCheckTask.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	/**function showCheckInfo(){
		var check_type = PspCheckTask.check_type._getValue();
		alert(check_type);
		if(check_type=='02'||check_type=='03'){
			//常规检查、专项检查
			document.getElementById('checkinfo').style.display="";

			PspCheckTask.check_time._obj._renderRequired(true);
			PspCheckTask.check_addr._obj._renderRequired(true);
			PspCheckTask.agreed_person._obj._renderRequired(true);
		}else{
			document.getElementById('checkinfo').style.display="none";
			PspCheckTask.check_time._obj._renderRequired(false);
			PspCheckTask.check_addr._obj._renderRequired(false);
			PspCheckTask.agreed_person._obj._renderRequired(false);
		}
	}**/

	function showRemarks(){
		var task_type = PspCheckTask.task_type._getValue();
		/**modified by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
		if(task_type=='04'){
			//集团客户
			PspCheckTask.remarks._obj._renderHidden(true);
			$(".emp_field_label:eq(15)").text("集团情况说明");
			PspCheckTask.remarks._obj._renderRequired(false);
        /**modified by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 end**/	
		}else if(task_type=='05'){
			//合作方客户
			PspCheckTask.remarks._obj._renderHidden(false);
			$(".emp_field_label:eq(13)").text("合作方情况说明");
			PspCheckTask.remarks._obj._renderRequired(true);
			
		}else{
			PspCheckTask.remarks._obj._renderHidden(true);
			PspCheckTask.remarks._obj._renderRequired(false);
		}
	}

	function doClose(){
		window.close();
	}

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		ImageAction('View27');	//贷后资料查看
	};
	function doImageScan(){
	    // add by yangzy 2015-6-25 贷后影像扫描由客户经理扫描 start
		var manager_id = PspCheckTask.manager_id._getValue();
		var currentUserId = "${context.currentUserId}";
		var rolesList = '${context.roleNoList}';
		if(rolesList.indexOf("3003")>0 && manager_id!=null && manager_id!='' && manager_id != currentUserId){
			alert("当前用户不是主管客户经理!");
			return;
		}
		// add by yangzy 2015-6-25 贷后影像扫描由客户经理扫描 end
		ImageAction('Scan26');	//贷后资料扫描
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = PspCheckTask.task_id._getValue();	//业务编号
		data['cus_id'] = PspCheckTask.cus_id._getValue();	//客户编号
		data['prd_id'] = 'POSTLOAN';	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="贷后检查" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="updatePspCheckTaskRecord.do" method="POST">
		<emp:gridLayout id="PspCheckTaskGroup" maxColumn="2" title="贷后检查任务">
			<emp:text id="PspCheckTask.task_id" label="任务编号" required="true" readonly="true" />
			<!-- add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能   -->
			<%if(task_type.equals("09")){ %>
			<emp:select id="PspCheckTask.batch_task_type" label="批量任务类型" required="false" dictname="STD_BATCH_TASK_TYPE" readonly="true" />
			<%} %>
			<emp:select id="PspCheckTask.check_type" label="检查类型" required="false" dictname="STD_PSP_CHECK_TYPE" readonly="true" />
			<%if(task_type.equals("04")){ %>
			<emp:text id="PspCheckTask.grp_no" label="集团编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PspCheckTask.grp_no_displayname" label="集团名称"   required="false" readonly="true"/>
			<emp:text id="PspCheckTask.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.cus_id_displayname" label="客户名称"   required="false" hidden="true"/>
			<%}else{ %>
			<emp:text id="PspCheckTask.grp_no" label="集团编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.grp_no_displayname" label="集团名称"   required="false" hidden="true"/>
			<emp:text id="PspCheckTask.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PspCheckTask.cus_id_displayname" label="客户名称"   required="false" readonly="true"/>
			<emp:select id="PspCheckTask.psp_cus_type" label="客户类别" required="false" readonly="true" hidden="true" colSpan="2" dictname="STD_PSP_CUS_TYPE"/>
			<%} %>
			
			<emp:date id="PspCheckTask.task_create_date" label="任务生成日期" required="false" readonly="true"/>
			<emp:date id="PspCheckTask.task_request_time" label="要求完成时间" required="false" readonly="true"/>
			<emp:text id="PspCheckTask.qnt" label="检查贷款笔数" maxlength="38" required="false" dataType="Int" readonly="true" colSpan="2"/>
			<emp:text id="PspCheckTask.loan_totl_amt" label="检查贷款总金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="PspCheckTask.loan_balance" label="检查贷款余额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="PspCheckTask.task_divis_person_displayname" label="任务分配人"   required="false" readonly="true"/>
			<emp:text id="PspCheckTask.task_divis_org_displayname" label="任务分配机构"   required="false" readonly="true"/>
			<emp:text id="PspCheckTask.task_huser_displayname" label="任务执行人"   required="false" readonly="true"/>
			<emp:text id="PspCheckTask.task_horg_displayname" label="任务执行机构"   required="false" readonly="true"/>
			
			<emp:textarea id="PspCheckTask.remarks" label="合作方情况说明" maxlength="250" required="false" colSpan="2"/>
			
			<emp:text id="PspCheckTask.task_huser" label="任务执行人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.task_horg" label="任务执行机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.task_divis_org" label="任务分配机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.task_divis_person" label="任务分配人" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.task_type" label="任务类型" required="false" hidden="true" />
			<emp:text id="PspCheckTask.approve_status" label="审批状态" required="false" hidden="true" />
			<emp:text id="PspCheckTask.check_freq" label="任务频率" hidden="true"  /><!-- added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
		<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 start-->	
			<%
			//首次检查
				if(check_type.equals("01")){
			%>
			<emp:date id="PspCheckTask.check_time" label="检查时间"  required="true" readonly="true"/>
			<%} %>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 end-->
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="登记信息">
			<emp:pop id="PspCheckTask.manager_id_displayname" label="主管客户经理" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<emp:pop id="PspCheckTask.manager_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="PspCheckTask.input_id_displayname" label="登记人"   required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspCheckTask.input_br_id_displayname" label="登记机构"   required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspCheckTask.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
			
			<emp:text id="PspCheckTask.manager_id" label="主管客户经理" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.manager_br_id" label="主管机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.input_id" label="登记人" maxlength="10" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspCheckTask.input_br_id" label="登记机构" maxlength="10" required="false" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		<!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start -->
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 start-->	
		<%
		//非首次检查
			if(!check_type.equals("01")){
		%>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 end-->	
		<emp:gridLayout id="" maxColumn="2" title="本次贷后检查综合评价及风险分类、下期贷后检查频率调整意见">
				<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 start-->	
			<emp:date id="PspCheckTask.check_time" label="检查时间" required="true" readonly="true"/>
			<emp:textarea id="PspCheckTask.check_view" label="意见" maxlength="1000" required="true" colSpan="2" readonly="true"/>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 end-->	
		</emp:gridLayout>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 start-->	
		<%} %>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 end-->	
		<!--<div id="checkinfo">
			<emp:gridLayout id="CheckInfoGroup" maxColumn="2" title="检查情况">
				<emp:textarea id="PspCheckTask.check_addr" label="检查地点" maxlength="200" required="true" colSpan="2"/>
				<emp:textarea id="PspCheckTask.agreed_person" label="约见人员" maxlength="40" required="true" colSpan="2"/>
			</emp:gridLayout>
		</div>-->
		<!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end -->
		<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="submit" label="保存" op="update"/>
			<emp:button id="reset" label="重置" op="update"/>
			<%} %>
		</div>
	</emp:form>
	</emp:tab>
	<%
	//首次检查-对公客户
	if(check_type.equals("01")&&task_type.equals("01")){
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01E32CD6123E4115A0E177DA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%
	}else if(check_type.equals("01")&&task_type.equals("02")){
	//首次检查-小微客户
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01E32CD6123E4115A0E177DA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("01")&&task_type.equals("03")&&"001".equals(psp_cus_type)){
	//首次检查-个人客户-个人经营性
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01E32CD6123E4115A0E177DA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("01")&&task_type.equals("03")&&"002".equals(psp_cus_type)){
	//首次检查-个人客户-个人消费性
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00708D302141BE5F1ED983EC&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("01")){
	//常规检查-对公客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%if(!"04".equals(check_freq)){
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end
	%>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="财务分析" id="fnctab" needFlush="true" url="getPspFncAnalyTab.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&check_freq=${context.PspCheckTask.check_freq}"></emp:tab><!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="queryPspPropertyAnalyListForVisit.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//delete by yangzy 2015/01/10 需求:XD141222090,公司小微贷后检查改造 <emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab> %>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="queryPspPropertyAnalyListForZx.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//delete by yangzy 2015/01/10 需求:XD141222090,公司小微贷后检查改造 <emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfoView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>  %>
<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if("04".equals(check_freq)){ 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end	
	%>
	<%}else if(check_type.equals("02")&&task_type.equals("02")){
	//常规检查-小微客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%if(!"04".equals(check_freq)){
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end
	%>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if(!"YES".equals(checkMicFlag)){ %>
	<emp:tab label="财务报表" id="finatab" needFlush="true" url="getPspFncReportTab.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&check_freq=${context.PspCheckTask.check_freq}"></emp:tab><!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="queryPspPropertyAnalyListForVisit.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//delete by yangzy 2015/01/10 需求:XD141222090,公司小微贷后检查改造 <emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab> %>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="queryPspPropertyAnalyListForZx.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//delete by yangzy 2015/01/10 需求:XD141222090,公司小微贷后检查改造 <emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfoView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>	%>
<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<%if("YES".equals(checkMicFlag)){ %>
	<!-- modified by lisj 2015-2-5  需求编号【XD150123004】 小微部关于贷后检查模块的变更需求 -->
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportmicfg_view.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else{ %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportmicnfg_view.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%} %>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if("04".equals(check_freq)){  
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end	
	%>
	<%}else if(check_type.equals("02")&&task_type.equals("03")){
	//常规检查-个人客户
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryLmtIndivInfoByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&psp_cus_type=${context.PspCheckTask.psp_cus_type}&op=view"></emp:tab>
<%//	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A016A9D0A8EFE8BA355FCA536&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&type=indiv"></emp:tab>
	<%if("001".equals(psp_cus_type)){//经营性 %>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForWater.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="queryPspPropertyAnalyListForZxGr.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A007499E39C4E0D6A170A8F9D&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="queryPspPropertyAnalyListForVisitGr.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportindivpe1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("04")){
	//常规检查-集团客户
	//modified by yangzy 2014/05/04  扫描岗增加影像扫描功能 start
	%>
	<emp:tab label="成员检查信息" id="grptab" needFlush="true" url="queryGrpListForPsp.do?task_id=${context.PspCheckTask.task_id}&op=view&viewfrom=${context.viewfrom}"></emp:tab>
		<!-- add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 ,新增检查集团客户信息情况 begin-->
	<%if("01-01".equals(task_create_date.substring(5,10))){ %>
	<emp:tab label="集团检查信息" id="grpinfotab" needFlush="true" url="queryGrpInfoForPsp.do?task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportgrp1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%} %>
	<!-- add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 ,新增检查集团客户信息情况 end-->
	<%if("04".equals(check_freq)){ 
	//modified by yangzy 2014/05/04  扫描岗增加影像扫描功能 end 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="风险预警" id="alttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276901EF01EF7001CDC23A63B8CC&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278401D2E04BD5DC10E6A2E55B67&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end	
	%>
	<%}else if(check_type.equals("02")&&task_type.equals("05")){
	//常规检查-合作方客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="合作方分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01A0D5102854480D8AD824FA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfinView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("06")){
	//常规检查-担保公司客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="融资担保公司分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A004983D88F56E31D8ED70920&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00F8963C9C74909B7BF8ACBA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfinView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("07")){
		//常规检查-房地产/汽车类	
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&task_type=${context.PspCheckTask.task_type}"/>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcarandhouse_view.raq&task_id=${context.PspCheckTask.task_id}"/>
	<%}else if(check_type.equals("02")&&task_type.equals("08")){ 
		//常规检查-融资100W以下类
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&task_type=${context.PspCheckTask.task_type}"/>
	<!-- add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能   -->
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportpersonloan100_view.raq&task_id=${context.PspCheckTask.task_id}"/>
	<%}else if(check_type.equals("03")&&task_type.equals("01")){
	//专项检查-对公客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
<%//	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="财务分析" id="fnctab" needFlush="true" url="getPspFncAnalyTab.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfoView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("02")){
	//专项检查-小微客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="财务报表" id="finatab" needFlush="true" url="getReportShowPage.do?reportId=psp/pspcomfinaView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfoView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("03")){
	//专项检查-个人客户
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryLmtIndivInfoByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&psp_cus_type=${context.PspCheckTask.psp_cus_type}&op=view"></emp:tab>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view&type=indiv"></emp:tab>
	<%if("001".equals(psp_cus_type)){//经营性 %>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForWater.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%} %>
<%//	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A016A9D0A8EFE8BA355FCA536&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfopeView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
<%//	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A007499E39C4E0D6A170A8F9D&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportindivpe1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("04")){
	//专项检查-集团客户
	%>
	<emp:tab label="成员检查信息" id="grptab" needFlush="true" url="queryGrpListForPsp.do?task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportgrp1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("05")){
	//专项检查-合作方客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="合作方分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01A0D5102854480D8AD824FA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfinView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("06")){
	//专项检查-担保公司客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="融资担保公司分析" id="custab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A004983D88F56E31D8ED70920&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="综合评价" id="evltab" needFlush="true" url="queryPspCheckList.do?scheme_id=FFFA276A00F8963C9C74909B7BF8ACBA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfinView.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}%>
	<!-- add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能   -->
	<%if(task_type.equals("09") && batch_task_type.equals("01")){ %>
	<emp:tab label="贷后子任务明细" id="sub_task_tab" initial="false" needFlush="true" url="queryPspBatchTaskList.do?major_task_id=${context.task_id}&manager_id=${context.manager_id}&batch_task_type=${context.PspCheckTask.batch_task_type}&op=view " />
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcarandhouse_view.raq&task_id=${context.PspCheckTask.task_id}"/>
	<%}else if(task_type.equals("09") && batch_task_type.equals("02")){ %>
	<emp:tab label="贷后子任务明细" id="sub_task_tab" initial="false" needFlush="true" url="queryPspBatchTaskList.do?major_task_id=${context.task_id}&manager_id=${context.manager_id}&batch_task_type=${context.PspCheckTask.batch_task_type}&op=view " />
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportpersonloan100_view.raq&task_id=${context.PspCheckTask.task_id}"/>	
	<%} %>
	<!-- add by yezm 2015-7-27 需求编号：XD150625045 贷后管理常规检查任务改造 （否决、启用历史操作查询） begin-->
	<emp:tab label="历史操作查询" id="hisupdatetab" needFlush="true" url="queryPspRejectBatchLogListForPsp.do?task_id=${context.PspCheckTask.task_id}&op=view"></emp:tab>
	<!-- add by yezm 2015-7-27 需求编号：XD150625045 贷后管理常规检查任务改造 （否决、启用历史操作查询） end-->
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if("grp".equals(openType)){ %>
		<emp:button id="close" label="关闭"/>
		<%}else if("1".equals(batch_task_flag)){ %>
		<emp:button id="close" label="关闭窗口"/>
		<%}else{ %>
		<emp:button id="return" label="返回列表"/>
		<%} %>
		<!-- add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能   -->
		<%if(flagSM.equals("1") && !"09".equals(task_type) && !"nohis".equals(viewfrom)){ //是否扫描岗标志%>
		<%-- <emp:button id="ImageScan" label="影像扫描"/> --%>
		<%} %>
		<%if(!"09".equals(task_type)){ %>
		<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		<%} %>
	</div>
</body>
</html>
</emp:page>
