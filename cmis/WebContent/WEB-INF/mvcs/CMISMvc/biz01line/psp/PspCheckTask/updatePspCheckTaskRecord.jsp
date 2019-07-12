<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%><emp:page>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kColl = (KeyedCollection)context.getDataElement("PspCheckTask");
	String check_type = "";//检查类型
	if(kColl.containsKey("check_type")){
		check_type = (String)kColl.getDataValue("check_type");
	}
	// added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start
	String check_freq = "";//检查频率
	if(kColl.containsKey("check_freq")){
		check_freq = (String)kColl.getDataValue("check_freq");
	}
	// added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造  end
	String task_type = "";//任务类型
	if(kColl.containsKey("task_type")){
		task_type = (String)kColl.getDataValue("task_type");
	}
	String psp_cus_type = "";//个人客户类型（经营性、消费性）
	if(kColl.containsKey("psp_cus_type")){
		psp_cus_type = (String)kColl.getDataValue("psp_cus_type");
	}
	String proFlag = "";//判断是否显示项目tab
	if(context.containsKey("proFlag")){
		proFlag = (String)context.getDataValue("proFlag");
	}
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
	String batchTaskRel = "";//是否存在批量任务关系标志
	if(context.containsKey("batchTaskRel")){
		batchTaskRel = (String)context.getDataValue("batchTaskRel");
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
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*XD140718027：检查页面添加返回按钮*/
	/*--user code begin--*/
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
		// modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start
		//showCheckInfo();
		// modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start
		//控制合作方情况说明显示
		showRemarks();

		//根据检查类型和任务类型，判断检查方案
		var check_type = PspCheckTask.check_type._getValue();
		var task_type = PspCheckTask.task_type._getValue();
		if(task_type == '03'){
			PspCheckTask.psp_cus_type._obj._renderHidden(false);
		}
	}

	function getCusForm(){
		var cus_id = PspCheckTask.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function showCheckInfo(){
		var check_type = PspCheckTask.check_type._getValue();
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
	}

	function showRemarks(){
		var task_type = PspCheckTask.task_type._getValue();
		/**add by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
		if(task_type=='04'){
			//集团客户
			PspCheckTask.remarks._obj._renderHidden(true);
			$(".emp_field_label:eq(15)").text("集团情况说明");
			PspCheckTask.remarks._obj._renderRequired(false);
			/**add by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 end**/	
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

	function setconId(data){
		PspCheckTask.manager_id._setValue(data.actorno._getValue());
		PspCheckTask.manager_id_displayname._setValue(data.actorname._getValue());
		PspCheckTask.manager_br_id._setValue(data.orgid._getValue());
		PspCheckTask.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//PspCheckTask.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					PspCheckTask.manager_br_id._setValue(jsonstr.org);
					PspCheckTask.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag  || "belg2team" == flag){//客户经理属于多个机构
					PspCheckTask.manager_br_id._setValue("");
					PspCheckTask.manager_br_id_displayname._setValue("");
					PspCheckTask.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = PspCheckTask.manager_id._getValue();
					PspCheckTask.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					PspCheckTask.manager_br_id._setValue("");
					PspCheckTask.manager_br_id_displayname._setValue("");
					PspCheckTask.manager_br_id_displayname._obj._renderReadonly(false);
					PspCheckTask.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = PspCheckTask.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		PspCheckTask.manager_br_id._setValue(data.organno._getValue());
		PspCheckTask.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function doSubwf(){
		var task_id = PspCheckTask.task_id._getValue();
		var task_type = PspCheckTask.task_type._getValue();
		var cus_id;
		var cus_id_displayname;
	// add by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）start
		var app_type;//集团客户与其他类型风险拦截不同，所以申请类型特殊处理
	// add by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）end
		if(task_type=='04'){
			cus_id = PspCheckTask.grp_no._getValue();
			cus_id_displayname = PspCheckTask.grp_no_displayname._getValue();
	// add by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）start	
			app_type = "058";
	// add by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）end
		}else{
			cus_id = PspCheckTask.cus_id._getValue();
			cus_id_displayname = PspCheckTask.cus_id_displayname._getValue();
	// add by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）start		
			app_type = "059";
	// add by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）end		
		}
		var approve_status = PspCheckTask.approve_status._getValue();
		WfiJoin.table_name._setValue("PspCheckTask");
		WfiJoin.pk_col._setValue("task_id");
		WfiJoin.pk_value._setValue(task_id);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
	// modefied by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）start	
		WfiJoin.appl_type._setValue(app_type);  //流程申请类型，对应字典项[ZB_BIZ_CATE]
	// modefied by zhaoxp  2015-01-22 需求编号： XD141222090  贷后管理系统改造（常规检查）end	
		WfiJoin.cus_id._setValue(cus_id);//客户码
		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
		//WfiJoin.amt._setValue(Amt);//金额
		WfiJoin.prd_name._setValue("贷后检查任务审批");//产品名称
		initWFSubmit(false);
	}

	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspCheckTask._checkAll()){
			PspCheckTask._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功!");
						//var url = '<emp:url action="queryPspCheckTaskList.do"/>?PspCheckTask.check_type=${context.PspCheckTask.check_type}&PspCheckTask.task_type=${context.PspCheckTask.task_type}';
						//url = EMPTools.encodeURI(url);
						//window.location = url;
					}else {
						alert("保存异常!");
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};
	/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  begin**/
	function doClose(){
		window.close();
	};
	/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  end**/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="贷后检查" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="updatePspCheckTaskRecord.do" method="POST">
		<emp:gridLayout id="PspCheckTaskGroup" maxColumn="2" title="贷后检查任务">
			<emp:text id="PspCheckTask.task_id" label="任务编号" required="true" readonly="true" />
			<emp:select id="PspCheckTask.check_type" label="检查类型" required="false" dictname="STD_PSP_CHECK_TYPE" readonly="true" />
			<emp:text id="PspCheckTask.check_freq" label="任务频率" hidden="true"  /><!-- added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
			<%if(task_type.equals("04")){ %>
			<emp:text id="PspCheckTask.grp_no" label="集团编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PspCheckTask.grp_no_displayname" label="集团名称"   required="false" readonly="true"/>
			<emp:text id="PspCheckTask.cus_id" label="客户码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.cus_id_displayname" label="客户名称"   required="false" hidden="true"/>
			<%}else{ %>
			<emp:text id="PspCheckTask.grp_no" label="集团编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PspCheckTask.grp_no_displayname" label="集团名称"  required="false" hidden="true"/>
			<emp:text id="PspCheckTask.cus_id" label="客户码" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PspCheckTask.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="PspCheckTask.psp_cus_type" label="客户类别" required="false" hidden="true" readonly="true" colSpan="2" dictname="STD_PSP_CUS_TYPE"/>
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
				<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 start-->	
			<%
			//首次检查
				if(check_type.equals("01")){
			%>
			<emp:date id="PspCheckTask.check_time" label="检查时间"  required="true"/>
			<%} %>
				<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 end-->	
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="2" title="登记信息">
		<!-- modified by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  -->
			<%if(batchTaskRel.equals("Y")){ %>
			<emp:text id="PspCheckTask.manager_id_displayname" label="主管客户经理" required="true"  readonly="true"/>
			<emp:text id="PspCheckTask.manager_br_id_displayname" label="主管机构" required="true"  readonly="true"/>
			<%}else{ %>
			<emp:pop id="PspCheckTask.manager_id_displayname" label="主管客户经理" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" buttonLabel="选择"/>
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin -->
			<emp:pop id="PspCheckTask.manager_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false&manager_id=${context.PspCheckTask.manager_id}" returnMethod="getOrgID" />
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end -->
			<%} %>
			<emp:text id="PspCheckTask.input_id_displayname" label="登记人"   required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="PspCheckTask.input_br_id_displayname" label="登记机构"   required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:date id="PspCheckTask.input_date" label="登记日期"  required="false"  defvalue="${context.OPENDAY}" readonly="true"/>
			
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
			<emp:date id="PspCheckTask.check_time" label="检查时间"  required="true"/>
			<emp:textarea id="PspCheckTask.check_view" label="意见" maxlength="1000" required="true" colSpan="2"/>
		</emp:gridLayout>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 start-->	
		<%} %>
			<!--贷后管理系统需求（首次检查、零售常规检查）  XD141230092   modefied by zhaoxp   2015-01-27 end-->	
		
<!--		<div id="checkinfo">-->
<!--			<emp:gridLayout id="CheckInfoGroup" maxColumn="2" title="本次贷后检查综合评价及风险分类、下期贷后检查频率调整意见">-->
<!--				<emp:date id="PspCheckTask.check_time" label="检查时间"  hidden="true"/>-->
<!--				<emp:textarea id="PspCheckTask.check_addr" label="检查地点" maxlength="200"  colSpan="2" hidden="true"/>-->
<!--				<emp:textarea id="PspCheckTask.agreed_person" label="约见人员" maxlength="40"  colSpan="2" hidden="true"/>-->
<!--				<emp:textarea id="PspCheckTask.check_view" label=" " maxlength="1000" required="true" colSpan="2"/>-->
<!--			</emp:gridLayout>-->
<!--				<emp:gridLayout id="" maxColumn="2" title="本次贷后检查综合评价及风险分类、下期贷后检查频率调整意见">-->
<!--					<emp:textarea id="PspCheckTask.check_view" label="本次贷后检查综合评价及风险分类、下期贷后检查频率调整意见" maxlength="1000" required="true" colSpan="2"/>-->
<!--				</emp:gridLayout>-->
<!--		</div>-->
		<!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end -->
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="update"/>
			<!-- modefied by zhaoxp  2015-01-22 需求： XD141222090，公司小微贷后检查改造 -->
		<%if(!batch_task_flag.equals("1")){ %>
			<emp:button id="subwf" label="提交" op="update"/> 
			<emp:button id="reset" label="重置" op="update"/>
			<!-- 添加返回按钮      2014-08-08   邓亚辉 -->
			<emp:button id="return" label="返回列表"/>
		<%}else{ %>
			<emp:button id="close" label="关闭窗口" />
		<%} %>
		</div>
	</emp:form>
	</emp:tab>
	<%
	//首次检查-对公客户
	if(check_type.equals("01")&&task_type.equals("01")){
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01E32CD6123E4115A0E177DA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%
	}else if(check_type.equals("01")&&task_type.equals("02")){
	//首次检查-小微客户
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01E32CD6123E4115A0E177DA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%}else if(check_type.equals("01")&&task_type.equals("03")&&"001".equals(psp_cus_type)){
	//首次检查-个人客户-个人经营性
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01E32CD6123E4115A0E177DA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%}else if(check_type.equals("01")&&task_type.equals("03")&&"002".equals(psp_cus_type)){
	//首次检查-个人客户-个人消费性
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="资金用途监控" id="capusetab" needFlush="true" url="queryPspCapUseMonitorList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&check_type=${context.PspCheckTask.check_type}"></emp:tab>
	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00708D302141BE5F1ED983EC&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("01")){
	//常规检查-对公客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%if(!"04".equals(check_freq)){
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end
	%>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务分析" id="fnctab" needFlush="true" url="getPspFncAnalyTab.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&check_freq=${context.PspCheckTask.check_freq}"></emp:tab><!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="queryPspPropertyAnalyListForVisit.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	
<% //	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>       %>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="queryPspPropertyAnalyListForZx.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
<% //	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfo.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>     %>
<%// <emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>	
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if("04".equals(check_freq)){ 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
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
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end
	%>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%if(!"YES".equals(checkMicFlag)){ %>
	<emp:tab label="财务报表" id="finatab" needFlush="true" url="getPspFncReportTab.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&check_freq=${context.PspCheckTask.check_freq}"></emp:tab><!-- modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
 
 	<emp:tab label="现场检查" id="visittab" needFlush="true" url="queryPspPropertyAnalyListForVisit.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
<% //	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>   %>
		
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="queryPspPropertyAnalyListForZx.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	
<%//	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfo.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>  %>
<%// <emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<%if("YES".equals(checkMicFlag)){ %>
	<!-- modified by lisj 2015-2-5  需求编号【XD150123004】 小微部关于贷后检查模块的变更需求 -->
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportmicfg.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else{ %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportmicnfg.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%} %>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%if("04".equals(check_freq)){  
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end	
	%>
	<%}else if(check_type.equals("02")&&task_type.equals("03")){
	//常规检查-个人客户
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryLmtIndivInfoByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&psp_cus_type=${context.PspCheckTask.psp_cus_type}&op=update"></emp:tab>
<%//<emp:tab label="检查情况" id="resulttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A016A9D0A8EFE8BA355FCA536&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&type=indiv"></emp:tab>
	<%if("001".equals(psp_cus_type)){//经营性 %>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForWater.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="queryPspPropertyAnalyListForZxGr.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
<%//<emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A007499E39C4E0D6A170A8F9D&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="queryPspPropertyAnalyListForVisitGr.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportindivpe1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("04")){
	//常规检查-集团客户
	%>
	<emp:tab label="成员检查信息" id="grptab" needFlush="true" url="queryGrpListForPsp.do?task_id=${context.PspCheckTask.task_id}&op=update"></emp:tab>
	<!-- add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 ,新增检查集团客户信息情况 begin-->
	<%if("01-01".equals(task_create_date.substring(5,10))){ %>
	<emp:tab label="集团检查信息" id="grpinfotab" needFlush="true" url="queryGrpInfoForPsp.do?task_id=${context.PspCheckTask.task_id}&op=update"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportgrp1View.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%} %>
    <!-- add by lisj 2015-5-19 需求编号：XD150504034 贷后管理常规检查任务改造 ,新增检查集团客户信息情况 end-->
	<%if("04".equals(check_freq)){  
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start	
	%>
	<emp:tab label="风险预警" id="alttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276901EF01EF7001CDC23A63B8CC&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278401D2E04BD5DC10E6A2E55B67&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}"></emp:tab>
	<%} 
	//modified by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end	
	%>
	<%}else if(check_type.equals("02")&&task_type.equals("05")){
	//常规检查-合作方客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="合作方分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01A0D5102854480D8AD824FA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfin.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("06")){
	//常规检查-担保公司客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="融资担保公司分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A004983D88F56E31D8ED70920&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00F8963C9C74909B7BF8ACBA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfin.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<emp:tab label="单一法人风险预警" id="sigltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278400B191CDD61CDC9F820782A0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="关注事件" id="evttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA278401F12D4FD621682ECE4C639E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%}else if(check_type.equals("02")&&task_type.equals("07")){
		//常规检查-房地产/汽车类	
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&task_type=${context.PspCheckTask.task_type}"/>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcarandhouse.raq&task_id=${context.PspCheckTask.task_id}"/>
	<%}else if(check_type.equals("02")&&task_type.equals("08")){ 
		//常规检查-融资100W以下类
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_id=${context.PspCheckTask.task_id}&task_type=${context.PspCheckTask.task_type}"/>
	<!-- add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能   -->
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportpersonloan100.raq&task_id=${context.PspCheckTask.task_id}"/>
	<%}else if(check_type.equals("03")&&task_type.equals("01")){
	//专项检查-对公客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务分析" id="fnctab" needFlush="true" url="getPspFncAnalyTab.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfo.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
<%// <emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("02")){
	//专项检查-小微客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00E225022268ADBB3780CDC0&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%if("Y".equals(proFlag)){ %>
	<emp:tab label="固定资产贷款(项目融资)分析" id="propertytab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00B2557223491A5E0046C80E&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
	<emp:tab label="资产状况调查" id="propertyanalytab" needFlush="true" url="queryPspPropertyAnalyList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务报表" id="finatab" needFlush="true" url="getReportShowPage.do?reportId=psp/pspcomfina.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForOper.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfo.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
<%// <emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A0108205998AA17BF07A4DE33&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportcom1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("03")){
	//专项检查-个人客户
	%>
	<emp:tab label="借据信息" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&cus_type=indiv"></emp:tab>
	<emp:tab label="借款人分析" id="custab" needFlush="true" url="queryLmtIndivInfoByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&psp_cus_type=${context.PspCheckTask.psp_cus_type}&op=update"></emp:tab>
	<emp:tab label="担保分析" id="grttab" needFlush="true" url="queryGrtListByCusIdForPsp.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="监控信息" id="montab" needFlush="true" url="queryPspPropertyAnalyListForMon.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update&type=indiv"></emp:tab>
	<%if("001".equals(psp_cus_type)){//经营性 %>
	<emp:tab label="经营佐证信息" id="opertab" needFlush="true" url="queryPspPropertyAnalyListForWater.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<%} %>
<%//	<emp:tab label="检查情况" id="resulttab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A016A9D0A8EFE8BA355FCA536&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="征信情况" id="zxtab" needFlush="true" url="getReportShowPage.do?reportId=psp/creditinfope.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
<%//<emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A007499E39C4E0D6A170A8F9D&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab> %>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportindivpe1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("04")){
	//专项检查-集团客户
	%>
	<emp:tab label="成员检查信息" id="grptab" needFlush="true" url="queryGrpListForPsp.do?task_id=${context.PspCheckTask.task_id}&op=update"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportgrp1.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("05")){
	//专项检查-合作方客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="合作方分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01A0D5102854480D8AD824FA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfin.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}else if(check_type.equals("03")&&task_type.equals("06")){
	//专项检查-担保公司客户
	%>
	<emp:tab label="用信情况" id="acctab" needFlush="true" url="queryAccListByCusIdForPsp.do?cus_id=${context.PspCheckTask.cus_id}&task_type=${context.PspCheckTask.task_type}"></emp:tab>
	<emp:tab label="融资担保公司分析" id="custab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A004983D88F56E31D8ED70920&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="财务分析" id="finatab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A01818DBC31BBA42C3CE30FC2&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="现场检查" id="visittab" needFlush="true" url="getCurCheckTabHelp.do?task_type=${context.PspCheckTask.task_type}&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="预警信息" id="alttab" needFlush="true" url="queryPspAppAltSignalList.do?task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="综合评价" id="evltab" needFlush="true" url="savePspCheckList.do?scheme_id=FFFA276A00F8963C9C74909B7BF8ACBA&task_id=${context.PspCheckTask.task_id}&cus_id=${context.PspCheckTask.cus_id}&op=update"></emp:tab>
	<emp:tab label="检查报告" id="reptab" needFlush="true" url="getReportShowPage.do?reportId=psp/checkreportfin.raq&task_id=${context.PspCheckTask.task_id}"></emp:tab>
	<emp:tab label="历史检查记录" id="histab" needFlush="true" url="queryPspCheckTaskHistoryListForTask.do?restrictFlag=false&PspCheckTask.cus_id=${context.PspCheckTask.cus_id}&op=view"></emp:tab>
	<%}%>
	</emp:tabGroup>
</body>
</html>
</emp:page>
