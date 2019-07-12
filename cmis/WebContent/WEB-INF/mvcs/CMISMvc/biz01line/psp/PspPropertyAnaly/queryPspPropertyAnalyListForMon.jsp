<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	String type = "";
	if(context.containsKey("type")){
		type = (String)context.getDataValue("type");
	}
	// added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start
	String check_freq = "";//检查频率
	if(context.containsKey("check_freq")){
		check_freq = (String)context.getDataValue("check_freq");
	}
	// added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造  end
	if(op.equals("view")){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doload(){
		var task_id = '${context.task_id}';
		rq_cus_id = '${context.cus_id}';
		rq_task_id = '${context.task_id}';
		//PspCheckAnaly.task_id._setValue(task_id);

		//setCapReason();

		//var op = '${context.op}';
		//if(op=='view'){
			//PspCheckAnaly.is_cap_normal_back._obj._renderDisabled(true);
		//}
	}
	
	function doResetvalue(){
		PspCheckAnaly.is_cap_normal_back._obj.reset();
		PspCheckAnaly.cap_back_reason._obj.reset();
		PspCheckAnaly.cap_back_dec._obj.reset();
	};

	function doSaveanaly(){
		var form = document.getElementById("submitForm");
		if(PspCheckAnaly._checkAll()){
			PspCheckAnaly._toForm(form);
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
					}else {
						alert("保存失败!");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}

	function setCapReason(){
		var is_cap_normal_back = PspCheckAnaly.is_cap_normal_back._getValue();
		if(is_cap_normal_back=='1'){
			PspCheckAnaly.cap_back_reason._obj._renderHidden(true);
			PspCheckAnaly.cap_back_reason._obj._renderRequired(false);
			PspCheckAnaly.cap_back_reason._setValue('');
		}else{
			PspCheckAnaly.cap_back_reason._obj._renderHidden(false);
			PspCheckAnaly.cap_back_reason._obj._renderRequired(true);
		}
	}

	function doViewPspCapUseMonitor() {
		var paramStr = PspCapUseMonitorList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCapUseMonitorViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doPrintPspCapUseMonitor() {
		var bill_no = PspCapUseMonitorList._obj.getParamValue(['bill_no']);
		if('<%=op%>' == 'view'){
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=psp/pspcurcheckView.raq&cus_id='+rq_cus_id+'&task_id='+rq_task_id+'&bill_no='+bill_no;
		}else{
			var url = '<emp:url action="getReportShowPage.do"/>&reportId=psp/pspcurcheck.raq&cus_id='+rq_cus_id+'&task_id='+rq_task_id+'&bill_no='+bill_no;
		}		
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};

	function doGetUpdatePspRepaySrcPage() {
		var paramStr = PspRepaySrcList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRepaySrcUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspRepaySrc() {
		var paramStr = PspRepaySrcList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRepaySrcViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspRepaySrcPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspRepaySrcAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspRepaySrc() {
		var paramStr = PspRepaySrcList._obj.getParamStr(['pk_id']);
		var task_id = PspRepaySrcList._obj.getParamValue(['task_id']);
		var cus_id = PspRepaySrcList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryPspPropertyAnalyListForMon.do"/>?task_id='+task_id+'&cus_id='+cus_id+"&op=update"; 
							url = EMPTools.encodeURI(url);
							window.location = url; 
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspRepaySrcRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
	function doDeletePspCapUseMonitor() {
		var paramStr = PspCapUseMonitorList._obj.getParamStr(['pk_id']);
		var task_id = PspCapUseMonitorList._obj.getParamValue(['task_id']);
		var cus_id = '${context.cus_id}';
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){

				if(task_id != rq_task_id){
					alert("只能操作此检查任务下的资金用途记录！");
					return false;
				}
				
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
							alert("删除成功!");
					/*		var url = '<emp:url action="queryPspCapUseMonitorList.do"/>?task_id='+task_id+'&cus_id='+cus_id; 
							url = EMPTools.encodeURI(url);
							window.location = url; */
							window.location.reload();
						}else {
							alert("删除异常!"); 
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
				var url = '<emp:url action="deletePspCapUseMonitorRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};  

	function doGetAddPspCapUseMonitorPage() {
		var cus_id = '${context.cus_id}';
		var task_id = '${context.task_id}';
		var url = '<emp:url action="getPspCapUseMonitorAddPage.do"/>?'+'cus_id='+cus_id+'&task_id='+task_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doGetUpdatePspCapUseMonitorPage() {
		var paramStr = PspCapUseMonitorList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {

			var task_id = PspCapUseMonitorList._obj.getParamValue(['task_id']);
			if(task_id != rq_task_id){
				alert("只能操作此检查任务下的资金用途记录！");
				return false;
			}
			
			var cus_id = '${context.cus_id}';
			var url = '<emp:url action="getPspCapUseMonitorUpdatePage.do"/>?'+paramStr+'&cus_id='+cus_id;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:gridLayout id="" title="资金用途" maxColumn="3">
		<emp:text id="disb_amt" label="资金合计数" dataType="Currency" defvalue="${context.disb_amt}" readonly="true" />
		<emp:text id="loan_amt" label="总贷款金额" dataType="Currency" defvalue="${context.loan_amt}" readonly="true"/>
		<emp:text id="loan_balance" label="总贷款余额" dataType="Currency" defvalue="${context.loan_balance}" readonly="true"/>
	</emp:gridLayout>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspCapUseMonitorPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspCapUseMonitorPage" label="修改" op="update"/>
		<emp:button id="deletePspCapUseMonitor" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspCapUseMonitor" label="查看" op="view"/>
		<emp:button id="printPspCapUseMonitor" label="打印" />
	</div>
	<emp:table icollName="PspCapUseMonitorList" pageMode="false" url="pagePspCapUseMonitorQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="disb_date" label="用款日期" />
		<emp:text id="disb_amt" label="用款金额" dataType="Currency"/>
		<emp:text id="pyee_name" label="收款人名称" />
		<emp:text id="paorg_acct_no" label="收款人账号" />
		<emp:text id="paorg_no" label="收款人开户行行号" hidden="true"/>
		<emp:text id="paorg_name" label="收款人开户行名称" />
		<emp:text id="use_type" label="用途" dictname="STD_CAP_USE_TYPE"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	<br>
	
	<div class="emp_gridLayout_title">还款来源 </div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspRepaySrcPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspRepaySrcPage" label="修改" op="update"/>
		<emp:button id="deletePspRepaySrc" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspRepaySrc" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspRepaySrcList" pageMode="false" url="pagePspRepaySrcQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="task_id" label="任务编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="asgn_date" label="转入日期" />
		<emp:text id="asgn_amt" label="转入金额" dataType="Currency"/>
		<emp:text id="sour" label="来源" hidden="true"/>
		<emp:text id="asgn_acct_no" label="转入方账号" />
		<emp:text id="asgn_acct_name" label="转入方账户名" />
		<emp:text id="asgn_acctsvcr_no" label="转入方开户行行号" />
		<emp:text id="asgn_acctsvcr_name" label="转入方开户行行名" />
		<emp:text id="proof_type" label="凭证种类和编号" hidden="true"/>
		<emp:text id="asgn_acct_bal" label="转入后账户余额" dataType="Currency"/>
		<emp:text id="cus_rela" label="转入方和借款人关系" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	<br>
	<!--  
	<emp:form id="submitForm" action="updatePspCheckAnalyRecord.do" method="POST">
	<emp:gridLayout id="" title="资金回笼情况" maxColumn="2">
		<emp:radio id="PspCheckAnaly.is_cap_normal_back" label="资金是否正常回笼" required="true" dictname="STD_ZX_YES_NO" colSpan="2" onclick="setCapReason()" />
		<emp:textarea id="PspCheckAnaly.cap_back_reason" label="资金没有正常回笼原因" maxlength="250" hidden="true" colSpan="2"/>
		<emp:textarea id="PspCheckAnaly.cap_back_dec" label="资金回笼总结(须体现我行回笼量及回笼占比、是否与该客户在我行授信总额相匹配)" maxlength="250" required="true" colSpan="2"/>
		<emp:text id="PspCheckAnaly.task_id" label="任务编号"  required="false" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="saveanaly" label="确定" />
			<emp:button id="resetvalue" label="重置"/>
			<%} %>
	</div>
	</emp:form>
	-->
	<emp:tabGroup id="IqpBksyndic_tabs" mainTab="IqpBksyndicInfo_tab">
	<%if("indiv".equals(type)){%>
		<%if(op.equals("view")){ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA278700603B7CFDD2235452DAF996&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
		<%}else{ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA278700603B7CFDD2235452DAF996&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update" initial="true"/>
		<%} %>
	<%}else{ %>
	<%if(!"04".equals(check_freq)){ %><!-- added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 -->
		<%if(op.equals("view")){ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA276A002897AC98FC2E65ADAD25B5&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
		<%}else{ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA276A002897AC98FC2E65ADAD25B5&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update" initial="true"/>
		<%} %>
	<%} %>
	<%} %>
	</emp:tabGroup>
</body>
</html>
</emp:page>
    