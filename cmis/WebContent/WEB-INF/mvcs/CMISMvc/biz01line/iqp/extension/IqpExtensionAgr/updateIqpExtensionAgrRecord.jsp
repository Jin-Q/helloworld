<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
	    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
		var url = '<emp:url action="queryIqpExtensionAgrList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		window.close();
		/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	};
	function doSubmits(){
		if(confirm("是否确认签订本合同？")){
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){
			            alert('签订成功!');
			            /** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			            window.opener.refresh();
			            /** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			            doReturn();
					}
				}
			};
			var handleFailure = function(o) {
				alert("签订失败!");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById("submitForm");
			var result = IqpExtensionAgr._checkAll();
			if(result){
				IqpExtensionAgr._toForm(form);
				var postData = YAHOO.util.Connect.setForm(form);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
			}else {
	           return ;
			}
		}		
	};
	/** 查看客户信息按钮begin **/
	function doOnLoad(){ 
		IqpExtensionAgr.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpExtensionAgr.fount_bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
		document.getElementById("main_tabs").href="javascript:reLoad();";
		
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
		var url = '<emp:url action="getIqpExtensionAgrUpdatePage.do"/>?menuIdTab=iqp_extension_agr&serno=${context.IqpExtensionAgr.serno}&op=update&restrictUsed=false';
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="展期协议信息" id="main_tabs">
	<emp:form id="submitForm" action="updateIqpExtensionAgrRecord.do" method="POST">
		<emp:gridLayout id="IqpExtensionAgrGroup" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionAgr.fount_bill_no" label="原借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.fount_cont_no" label="原合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.cus_id" label="客户码" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.cus_id_displayname" label="客户名称" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="IqpExtensionAgr.fount_cur_type" label="原币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" readonly="true" />
			<emp:text id="IqpExtensionAgr.fount_loan_amt" label="原贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:text id="IqpExtensionAgr.fount_loan_balance" label="原贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:text id="IqpExtensionAgr.fount_rate" label="原执行利率(年)" maxlength="16" required="true" dataType="Rate" colSpan="2" readonly="true" />
			<emp:date id="IqpExtensionAgr.fount_start_date" label="原起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionAgr.fount_end_date" label="原止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionAgrGroup" title="展期协议信息" maxColumn="2">
			<emp:text id="IqpExtensionAgr.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.cn_cont_no" label="中文合同编号" maxlength="40" required="true" />
			<emp:date id="IqpExtensionAgr.sign_date" label="签订日期" required="false" hidden="true" />
			<emp:text id="IqpExtensionAgr.extension_amt" label="展期金额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:date id="IqpExtensionAgr.extension_date" label="展期到期日期" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.base_rate" label="基准利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:text id="IqpExtensionAgr.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:textarea id="IqpExtensionAgr.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionAgrGroup" maxColumn="2" title="登记信息">
			<emp:text id="IqpExtensionAgr.manager_id_displayname" label="责任人" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgr.manager_br_id_displayname" label="责任机构"  required="true" cssElementClass="emp_pop_common_org" readonly="true" />
			<emp:text id="IqpExtensionAgr.input_id_displayname" label="登记人" readonly="true" required="true"  />
			<emp:text id="IqpExtensionAgr.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="IqpExtensionAgr.manager_id" label="责任人" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpExtensionAgr.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpExtensionAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpExtensionAgr.input_br_id" label="登记机构" required="false" hidden="true"/>
			<emp:date id="IqpExtensionAgr.input_date" label="登记日期"  required="false" readonly="true" />
			<emp:select id="IqpExtensionAgr.status" label="状态" required="false" dictname="STD_ZB_CTRLOANCONT_TYPE" readonly="true" />
			<emp:text id="IqpExtensionAgr.prd_id" label="业务类型" hidden="true"  readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="签订" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:tab label="原合同信息" id="subTab" 
	url="getCtrLoanContViewPage.do?cont_no=${context.IqpExtensionAgr.fount_cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
	 initial="false" needFlush="true"/>
	</emp:tabGroup>
</body>
</html>
</emp:page>
