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
	/*XD140718027：公司客户和小微客户经营佐证信息的修改*/
	function doload(){
		var task_id = '${context.task_id}';
		this_task_id = '${context.task_id}';
		//convert_sfsxdybacl();
		//convert_sfzybhgjbtz();
		//convert_jkrsfzqtdyzhj();
		//convert_jkrsfzwtwyjl();
		//convert_jkrzwtdkfxxj();
		//convert_jkrddyzffdx();
		convert_jkrzxbccdqs();
		convert_sfjkryqtzwhdb();
		var op = "<%=op%>";
		if(op=='view'){
			radioSet();
		}
	}
	function radioSet(){
		//PspZxqkDetail.sfsxdybacl._obj._renderDisabled(true);
		//PspZxqkDetail.sfzybhgjbtz._obj._renderDisabled(true);
		//PspZxqkDetail.jkrsfzqtdyzhj._obj._renderDisabled(true);
		//PspZxqkDetail.jkrsfzwtwyjl._obj._renderDisabled(true);
		//PspZxqkDetail.jkrzwtdkfxxj._obj._renderDisabled(true);
		//PspZxqkDetail.jkrddyzffdx._obj._renderDisabled(true);
		PspZxqkDetail.jkrzxbccdqs._obj._renderDisabled(true);
		PspZxqkDetail.sfjkryqtzwhdb._obj._renderDisabled(true);
		PspZxqkDetail.dwrz_sfyqb._obj._renderDisabled(true);
		PspZxqkDetail.dwdb_sfyqb._obj._renderDisabled(true);
	};
	function doQuery(){
		var form = document.getElementById('queryForm');
		PspPropertyAnaly._toForm(form);
		PspPropertyAnalyList._obj.ajaxQuery(null,form);
	};

	
	function doViewPspZxqkLoan() {
		var paramStr = PspZxqkLoanList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspZxqkLoanViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspZxqkLoanPage() {
		var task_id = '${context.task_id}';
		var cus_id = '${context.cus_id}';
		var url = '<emp:url action="getPspZxqkLoanAddPage.do"/>?task_id='+task_id+'&cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspZxqkLoan() {
		var paramStr = PspZxqkLoanList._obj.getParamStr(['pk_id']);
		var task_id = PspZxqkLoanList._obj.getParamValue(['task_id']);
		var cus_id = PspZxqkLoanList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var choic_task_id = PspZxqkLoanList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
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
							var url = '<emp:url action="queryPspPropertyAnalyListForZx.do"/>?task_id='+task_id+'&cus_id='+cus_id;
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
				var url = '<emp:url action="deletePspZxqkLoanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}
 		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doResetvalue(){
		PspZxqkDetail.dwrz_amt._obj.reset();
		PspZxqkDetail.credit_bank_num._obj.reset();
		PspZxqkDetail.dwdb_amt._obj.reset();
		PspZxqkDetail.lawsuit_enp._obj.reset();
		//PspZxqkDetail.sfsxdybacl._obj.reset();
		//PspZxqkDetail.sfsxdybacl_sm._obj.reset();
		//PspZxqkDetail.sfzybhgjbtz._obj.reset();
		//PspZxqkDetail.sfzybhgjbtz_sm._obj.reset();
		//PspZxqkDetail.jkrsfzqtdyzhj._obj.reset();
		//PspZxqkDetail.jkrsfzqtdyzhj_sm._obj.reset();
		//PspZxqkDetail.jkrsfzwtwyjl._obj.reset();
		//PspZxqkDetail.jkrsfzwtwyjl_sm._obj.reset();
		//PspZxqkDetail.jkrzwtdkfxxj._obj.reset();
		//PspZxqkDetail.jkrzwtdkfxxj_sm._obj.reset();
		//PspZxqkDetail.jkrddyzffdx._obj.reset();
		//PspZxqkDetail.jkrddyzffdx_sm._obj.reset();
		PspZxqkDetail.jkrzxbccdqs._obj.reset();
		PspZxqkDetail.jkrzxbccdqs_sm._obj.reset();
		PspZxqkDetail.sfjkryqtzwhdb._obj.reset();
		PspZxqkDetail.sfjkryqtzwhdb_sm._obj.reset();
		PspZxqkDetail.other_sm._obj.reset();
		
	};
	function doSaveanaly(){
		var form = document.getElementById("submitForm");
		if(PspZxqkDetail._checkAll()){
			PspZxqkDetail._toForm(form);
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
		}else {
			return false;
		}
	}

	function doGetUpdatePspZxqkLoanPage() {
		var paramStr = PspZxqkLoanList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var choic_task_id = PspZxqkLoanList._obj.getParamValue(['task_id']);
			if(this_task_id != choic_task_id){
				alert("只能操作本笔贷后检查任务下的记录！");
				return false;
			}
			var url = '<emp:url action="getPspZxqkLoanUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
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
	function convert_sfsxdybacl(){
		if(PspZxqkDetail.sfsxdybacl._getValue()=="1"){
			PspZxqkDetail.sfsxdybacl_sm._obj._renderHidden(false);
			PspZxqkDetail.sfsxdybacl_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.sfsxdybacl_sm._obj._renderHidden(true);
			PspZxqkDetail.sfsxdybacl_sm._obj._renderRequired(false);
			PspZxqkDetail.sfsxdybacl_sm._setValue("");
		}
	}
	function convert_sfzybhgjbtz(){
		if(PspZxqkDetail.sfzybhgjbtz._getValue()=="1"){
			PspZxqkDetail.sfzybhgjbtz_sm._obj._renderHidden(false);
			PspZxqkDetail.sfzybhgjbtz_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.sfzybhgjbtz_sm._obj._renderHidden(true);
			PspZxqkDetail.sfzybhgjbtz_sm._obj._renderRequired(false);
			PspZxqkDetail.sfzybhgjbtz_sm._setValue("");
		}
	}
	function convert_jkrsfzqtdyzhj(){
		if(PspZxqkDetail.jkrsfzqtdyzhj._getValue()=="1"){
			PspZxqkDetail.jkrsfzqtdyzhj_sm._obj._renderHidden(false);
			PspZxqkDetail.jkrsfzqtdyzhj_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.jkrsfzqtdyzhj_sm._obj._renderHidden(true);
			PspZxqkDetail.jkrsfzqtdyzhj_sm._obj._renderRequired(false);
			PspZxqkDetail.jkrsfzqtdyzhj_sm._setValue("");
		}
	}
	function convert_jkrsfzwtwyjl(){
		if(PspZxqkDetail.jkrsfzwtwyjl._getValue()=="1"){
			PspZxqkDetail.jkrsfzwtwyjl_sm._obj._renderHidden(false);
			PspZxqkDetail.jkrsfzwtwyjl_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.jkrsfzwtwyjl_sm._obj._renderHidden(true);
			PspZxqkDetail.jkrsfzwtwyjl_sm._obj._renderRequired(false);
			PspZxqkDetail.jkrsfzwtwyjl_sm._setValue("");
		}
	}
	function convert_jkrzwtdkfxxj(){
		if(PspZxqkDetail.jkrzwtdkfxxj._getValue()=="1"){
			PspZxqkDetail.jkrzwtdkfxxj_sm._obj._renderHidden(false);
			PspZxqkDetail.jkrzwtdkfxxj_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.jkrzwtdkfxxj_sm._obj._renderHidden(true);
			PspZxqkDetail.jkrzwtdkfxxj_sm._obj._renderRequired(false);
			PspZxqkDetail.jkrzwtdkfxxj_sm._setValue("");
		}
	}
	function convert_jkrddyzffdx(){
		if(PspZxqkDetail.jkrddyzffdx._getValue()=="1"){
			PspZxqkDetail.jkrddyzffdx_sm._obj._renderHidden(false);
			PspZxqkDetail.jkrddyzffdx_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.jkrddyzffdx_sm._obj._renderHidden(true);
			PspZxqkDetail.jkrddyzffdx_sm._obj._renderRequired(false);
			PspZxqkDetail.jkrddyzffdx_sm._setValue("");
		}
	}
	function convert_jkrzxbccdqs(){
		if(PspZxqkDetail.jkrzxbccdqs._getValue()=="1"){
			PspZxqkDetail.jkrzxbccdqs_sm._obj._renderHidden(false);
			PspZxqkDetail.jkrzxbccdqs_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.jkrzxbccdqs_sm._obj._renderHidden(true);
			PspZxqkDetail.jkrzxbccdqs_sm._obj._renderRequired(false);
			PspZxqkDetail.jkrzxbccdqs_sm._setValue("");
		}
	}
	function convert_sfjkryqtzwhdb(){
		if(PspZxqkDetail.sfjkryqtzwhdb._getValue()=="1"){
			PspZxqkDetail.sfjkryqtzwhdb_sm._obj._renderHidden(false);
			PspZxqkDetail.sfjkryqtzwhdb_sm._obj._renderRequired(true);
		}else{
			PspZxqkDetail.sfjkryqtzwhdb_sm._obj._renderHidden(true);
			PspZxqkDetail.sfjkryqtzwhdb_sm._obj._renderRequired(false);
			PspZxqkDetail.sfjkryqtzwhdb_sm._setValue("");
		}
	}
	/*--user code end--*/
	
	//查看担保品
	function doViewguar() {
		var paramStr = GrtMortList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看保证人信息
	function doViewee(){
		var paramStr = GrtGuaranteeList._obj.getParamValue(['cus_id']);
		if (paramStr != null) {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+paramStr;
			url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	}

	function doBzrxccheck(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		
		var param = GrtGuaranteeList._obj.getParamStr(['cus_name']);
		if (data!=null&&data!='') {
				var task_id = '${context.task_id}';
				var cus_id = '${context.cus_id}';
				var cus_id_bzr = GrtGuaranteeList._obj.getSelectedData()[0].cus_id._getValue();              
				var url = '<emp:url action="getPspZxqkLoanAddPagebzr.do"/>?task_id='+task_id+'&cus_id_bzr='+cus_id_bzr+'&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}	
	}
   /*--贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2015-01-22--*/
	function doBzrxccheckview(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		
		var param = GrtGuaranteeList._obj.getParamStr(['cus_name']);
		if (data!=null&&data!='') {
				var task_id = '${context.task_id}';
				var cus_id = '${context.cus_id}';
				var cus_id_bzr = GrtGuaranteeList._obj.getSelectedData()[0].cus_id._getValue();              
				var url = '<emp:url action="getPspZxqkLoanAddPagebzrView.do"/>?task_id='+task_id+'&cus_id_bzr='+cus_id_bzr+'&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}	
	}

/*--贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2015-01-22--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<div class="emp_gridLayout_title">借款企业关键个人征信检查 </div>
	<div align="left">
		<%if(!op.equals("view")){ %>
		<emp:button id="getAddPspZxqkLoanPage" label="新增" op="update"/>
		<emp:button id="getUpdatePspZxqkLoanPage" label="修改" op="update"/>
		<emp:button id="deletePspZxqkLoan" label="删除" op="update"/>
		<%} %>
		<emp:button id="viewPspZxqkLoan" label="查看" op="view"/>
	</div>
	<emp:table icollName="PspZxqkLoanList" pageMode="false" url="pagePspZxqkLoanQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true" />
		<emp:text id="cus_id" label="客户码"  hidden="true"/>
		<emp:text id="task_id" label="任务号" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_JBR_TYPE"/>
		<emp:text id="jqgr_name" label="借款企业关键个人姓名" />
		<emp:text id="jqgr_type" label="借款企业关键个人类别" dictname="STD_ZX_JQGG_TYP"/>
		
	</emp:table>
	
	<div class='emp_gridlayout_title'>保证人征信检查</div>
	<emp:button id="viewee" label="查看保证人" op="view"/>
	 
	<%if(!op.equals("view")){ %>
	 
	<emp:button id="bzrxccheck" label="征信检查" op="view"/>
	 
	<%} %>
	<%if(op.equals("view")){ %>
	<emp:button id="bzrxccheckview" label="征信检查查看" op="view"/>
	<%} %>
	
	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="">
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_name" label="保证人" />
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
	</emp:table>
	
	
	
	<emp:gridLayout id="PspGroup" title="借款人" maxColumn="2">
		<emp:text id="PspZxqkDetail.dwrz_amt" label="对外融资总额（万元）"  required="true" dataType="Currency"/>
		<emp:text id="PspZxqkDetail.dwrz_jsqzje" label="较上期增加（或减少）额" required="true" dataType="Currency" />
		<emp:radio id="PspZxqkDetail.dwrz_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO"  layout="false" />
		<emp:text id="PspZxqkDetail.credit_bank_num" label="授信合作银行数量"  required="true" dataType="Int"/>
		<emp:text id="PspZxqkDetail.dwdb_amt" label="对外担保总额（万元）"  required="true" dataType="Currency"/>
		<emp:text id="PspZxqkDetail.dwdb_jsqzje" label="较上期增加（或减少）额" required="true" dataType="Currency" />
		<emp:radio id="PspZxqkDetail.dwdb_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO"  layout="false" colSpan="2" />
		
		<emp:radio id="PspZxqkDetail.jkrzxbccdqs" label="借款人征信被查询次数是否在短期内骤升" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_jkrzxbccdqs()" />
		<emp:textarea id="PspZxqkDetail.jkrzxbccdqs_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2"  />
		<emp:radio id="PspZxqkDetail.sfjkryqtzwhdb" label="是否发现借款人有银行之外的其他债务或担保信息" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_sfjkryqtzwhdb()" />
		<emp:textarea id="PspZxqkDetail.sfjkryqtzwhdb_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" />
		
		<emp:textarea id="PspZxqkDetail.lawsuit_enp" label="产生不良的原因及诉讼情况" maxlength="250" required="true" colSpan="2" />	
		<emp:text id="PspZxqkDetail.task_id" label="任务编号"  required="true" hidden="true"  defvalue="${context.task_id}"/>
	</emp:gridLayout>
	
<!--		<emp:radio id="PspZxqkDetail.sfsxdybacl" label="客户在我行及他行授信是否出现贷款逾期、不能按时偿还利息等情况" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_sfsxdybacl()" hidden="true"/>-->
<!--		<emp:textarea id="PspZxqkDetail.sfsxdybacl_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" hidden="true"/>-->
<!--		<emp:radio id="PspZxqkDetail.sfzybhgjbtz" label="是否因信贷资金使用或用途不合规进行级别调整" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_sfzybhgjbtz()" hidden="true"/>-->
<!--		<emp:textarea id="PspZxqkDetail.sfzybhgjbtz_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" hidden="true"/>-->
<!--		<emp:radio id="PspZxqkDetail.jkrsfzqtdyzhj" label="借款人是否在其他金融机构贷款余额大幅增加或减少" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_jkrsfzqtdyzhj()" hidden="true"/>-->
<!--		<emp:textarea id="PspZxqkDetail.jkrsfzqtdyzhj_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" hidden="true"/>-->
<!--		<emp:radio id="PspZxqkDetail.jkrsfzwtwyjl" label="借款人在我行或他行是否存在违约历史记录" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_jkrsfzwtwyjl()" hidden="true"/>-->
<!--		<emp:textarea id="PspZxqkDetail.jkrsfzwtwyjl_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" hidden="true"/>-->
<!--		<emp:radio id="PspZxqkDetail.jkrzwtdkfxxj" label="借款人在我行和他行的贷款风险分类等级是否下降" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_jkrzwtdkfxxj()" hidden="true"/>-->
<!--		<emp:textarea id="PspZxqkDetail.jkrzwtdkfxxj_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" hidden="true"/>-->
<!--		<emp:radio id="PspZxqkDetail.jkrddyzffdx" label="借款人对外担保余额是否大幅增加，对外担保的贷款客户风险分类等级是否下降" required="true" dictname="STD_ZX_YES_NO" colSpan="2" layout="false" onclick="convert_jkrddyzffdx()" hidden="true"/>-->
<!--		<emp:textarea id="PspZxqkDetail.jkrddyzffdx_sm" label="如有请说明：" maxlength="250" required="true" colSpan="2" hidden="true"/>-->
<!--	-->
<!--		<emp:textarea id="PspZxqkDetail.other_sm" label="其他情况说明:" maxlength="250" required="true" colSpan="2"  hidden="true"/>-->

	<br>
	<br>
	<emp:form id="submitForm" action="updatePspZxqkDetailRecord.do" method="POST">
	<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="saveanaly" label="确定" />
			<emp:button id="resetvalue" label="重置"/>
<!--			<emp:button id="return" label="返回列表"/>-->
			<%} %>
	</div>
	</emp:form>
</body>
</html>
</emp:page>