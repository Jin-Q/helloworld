<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String cus_id = request.getParameter("cus_id");
	String dataType = request.getParameter("dataType");//判断是否是首次检查
	//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String check_type = "";//检查类型
	if(context.containsKey("check_type")){
		check_type = (String)context.getDataValue("check_type");
	}
	//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doload(){
		var cus_id = '<%=cus_id%>';
		var urls = "&condition=cus_id = '"+cus_id+"' and acc_status = '1'";
		PspCapUseMonitor.bill_no._obj.config.url=PspCapUseMonitor.bill_no._obj.config.url+urls;
		var is_cash = PspCapUseMonitor.is_cash._getValue();
		if("01" == is_cash){
			PspCapUseMonitor.paorg_acct_no._obj._renderRequired(false);
			PspCapUseMonitor.paorg_no._obj._renderRequired(false);
			PspCapUseMonitor.paorg_name._obj._renderRequired(false);
		}else{
			PspCapUseMonitor.paorg_acct_no._obj._renderRequired(true);
			PspCapUseMonitor.paorg_no._obj._renderRequired(true);
			PspCapUseMonitor.paorg_name._obj._renderRequired(true);
		}

		onChange();
		changeIs_Exchang_Setl();
	}

	function selAccInfo(data){
		PspCapUseMonitor.bill_no._setValue(data.bill_no._getValue());
	}

	function getToorgNo(data){
		PspCapUseMonitor.paorg_no._setValue(data.bank_no._getValue());
		PspCapUseMonitor.paorg_name._setValue(data.bank_name._getValue());
    };

    function doSub(){
		if(PspCapUseMonitor._checkAll()){
			checkBillAmt();
		}
	};

	function checkBillAmt(){
		var cus_id = PspCapUseMonitor.cus_id._getValue();
		var bill_no = PspCapUseMonitor.bill_no._getValue();
		var disb_amt = PspCapUseMonitor.disb_amt._getValue();
		var pk_id = PspCapUseMonitor.pk_id._getValue();
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
					var form = document.getElementById("submitForm");
					PspCapUseMonitor._toForm(form); 
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
								doReturn();
							}else {
								alert("新增异常!"); 
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
				}else {
					if(flag == "failed"){
						alert("校验检查借据金额出错");
					}else{
						alert("用款金额不可超出未检查借据金额："+flag);
					}					
					return false;
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

		var url = '<emp:url action="checkAccLoanForPspCap.do"/>?cus_id='+cus_id+'&bill_no='+bill_no+'&disb_amt='+disb_amt+'&pk_id='+pk_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	};

	function doReturn(){
		var dataType = '<%=dataType%>';
		var cus_id = '<%=cus_id%>';
		var url = "";
		if(dataType=="first"){
			var task_id = PspCapUseMonitor.pk_id._getValue();
			url = '<emp:url action="queryPspCapUseMonitorList.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		}else{
			var task_id = PspCapUseMonitor.task_id._getValue();
			url = '<emp:url action="queryPspPropertyAnalyListForMon.do"/>?task_id='+task_id+'&cus_id='+cus_id+'&op=update';
		}
		url = EMPTools.encodeURI(url);
		window.location = url; 
	}
	function doChange(){
		var is_cash = PspCapUseMonitor.is_cash._getValue();
		if("01" == is_cash){
			PspCapUseMonitor.paorg_acct_no._obj._renderRequired(false);
			PspCapUseMonitor.paorg_no._obj._renderRequired(false);
			PspCapUseMonitor.paorg_name._obj._renderRequired(false);
		}else{
			PspCapUseMonitor.paorg_acct_no._obj._renderRequired(true);
			PspCapUseMonitor.paorg_no._obj._renderRequired(true);
			PspCapUseMonitor.paorg_name._obj._renderRequired(true);
		}
	}
	/*根据【放款币种】显示界面*/
	function onChange(){
		var cur_type = PspCapUseMonitor.cur_type._getValue();
		var is_exchang_setl = PspCapUseMonitor.is_exchang_setl._getValue();
		if(cur_type == 'CNY'){
			/*隐藏不需要的页面元素*/
			PspCapUseMonitor.is_exchang_setl._obj._renderHidden(true);
			PspCapUseMonitor.exchang_setl_amt._obj._renderHidden(true);

			PspCapUseMonitor.is_exchang_setl._obj._renderRequired(false);
			PspCapUseMonitor.exchang_setl_amt._obj._renderRequired(false);
			}else {
				if(is_exchang_setl == '2'){
					PspCapUseMonitor.is_exchang_setl._obj._renderHidden(false);
					PspCapUseMonitor.exchang_setl_amt._obj._renderHidden(true);

					PspCapUseMonitor.is_exchang_setl._obj._renderRequired(true);
					PspCapUseMonitor.exchang_setl_amt._obj._renderRequired(false);
					
					}else{
						/*显示需要的页面元素*/
						PspCapUseMonitor.is_exchang_setl._obj._renderHidden(false);
						PspCapUseMonitor.exchang_setl_amt._obj._renderHidden(false);

						PspCapUseMonitor.is_exchang_setl._obj._renderRequired(true);
						PspCapUseMonitor.exchang_setl_amt._obj._renderRequired(true);
						}
				}
		}
	/*根据【是否结汇】显示界面*/
	function changeIs_Exchang_Setl(){
		var is_exchang_setl = PspCapUseMonitor.is_exchang_setl._getValue();
		if(is_exchang_setl == '2'){
			/*隐藏不需要的页面元素*/
			PspCapUseMonitor.exchang_setl_amt._obj._renderHidden(true);
			PspCapUseMonitor.paorg_no._obj._renderHidden(true);
			PspCapUseMonitor.paorg_name._obj._renderHidden(true);

			PspCapUseMonitor.exchang_setl_amt._obj._renderRequired(false);
			PspCapUseMonitor.paorg_no._obj._renderRequired(false);
			PspCapUseMonitor.paorg_name._obj._renderRequired(false);
			}else{
				/*显示需要的页面元素*/
				PspCapUseMonitor.exchang_setl_amt._obj._renderHidden(false);
				PspCapUseMonitor.paorg_no._obj._renderHidden(false);
				PspCapUseMonitor.paorg_name._obj._renderHidden(false);

				PspCapUseMonitor.exchang_setl_amt._obj._renderRequired(true);
				PspCapUseMonitor.paorg_no._obj._renderRequired(true);
				PspCapUseMonitor.paorg_name._obj._renderRequired(true);
				}
		}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updatePspCapUseMonitorRecord.do" method="POST">
		<emp:gridLayout id="PspCapUseMonitorGroup" title="资金用途监控" maxColumn="2">
			<emp:pop id="PspCapUseMonitor.bill_no" label="借据编号" readonly="true" url="queryAccLoanPop.do?returnMethod=selAccInfo" colSpan="2" />
			<emp:date id="PspCapUseMonitor.disb_date" label="用款日期" required="true" />
			<emp:select id="PspCapUseMonitor.cur_type" label="放款币种"  dictname="STD_ZX_CUR_TYPE" onchange="onChange()" required="true"/>
			<emp:select id="PspCapUseMonitor.is_exchang_setl" label="是否结汇"  dictname="STD_ZX_YES_NO"  onchange="changeIs_Exchang_Setl()"/>
			<emp:text id="PspCapUseMonitor.exchang_setl_amt" label="结汇金额" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="PspCapUseMonitor.disb_amt" label="用款金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:select id="PspCapUseMonitor.use_type" label="实际用途" required="true" dictname="STD_CAP_USE_TYPE"/>
			<emp:select id="PspCapUseMonitor.is_cash" label="支取方式" required="true" dictname="STD_ZB_RENT_COLL_TYPE" onchange="doChange()"/>
			<emp:text id="PspCapUseMonitor.pyee_name" label="收款人名称" maxlength="100" required="true" />
			<emp:text id="PspCapUseMonitor.paorg_acct_no" label="收款人账号" maxlength="40" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="PspCapUseMonitor.paorg_no" label="收款人开户行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getToorgNo" required="true" buttonLabel="选择" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="PspCapUseMonitor.paorg_name" label="收款人开户行名称" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_cusname_readonly"/>
			
			<emp:text id="PspCapUseMonitor.proof_type" label="证明材料" maxlength="250" required="true" cssElementClass="emp_field_text_cusname" colSpan="2" />
			<emp:select id="PspCapUseMonitor.is_cap_back" label="是否短期内资金回流借款人" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:select id="PspCapUseMonitor.debit_percn_rela" label="收款人是否上游往来客户" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:select id="PspCapUseMonitor.is_move_loan" label="是否符合计划及约定用途" required="true" dictname="STD_ZX_YES_NO"/>
			<%if("01".equals(check_type)){ 
			//modified by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
			%>
			<emp:textarea id="PspCapUseMonitor.memo" label="补充说明" maxlength="250" required="false" colSpan="2"/>
			<%}else{ 
			%>
			<emp:textarea id="PspCapUseMonitor.memo" label="首次检查结论" maxlength="250" required="false" colSpan="2"/>
			<%}
			//modified by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
			%>
			<emp:text id="PspCapUseMonitor.pk_id" label="主键" maxlength="32" readonly="true" hidden="true" />
			<emp:text id="PspCapUseMonitor.task_id" label="任务编号" hidden="true" />
			<emp:text id="PspCapUseMonitor.cus_id" label="客户码" maxlength="40" hidden="true" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" title="登记信息" maxColumn="2">
			<emp:text id="PspCapUseMonitor.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspCapUseMonitor.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspCapUseMonitor.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:text id="PspCapUseMonitor.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspCapUseMonitor.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
