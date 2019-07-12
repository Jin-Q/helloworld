<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetRegiApp._checkAll()){
			IqpAssetRegiApp._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					//alert(o.responseText+"ggggg");
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("新增成功!");
						var url = '<emp:url action="queryIqpAssetRegiAppList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};

	//选择借据POP
	function returnAcc(data){
		IqpAssetRegiApp.bill_no._setValue(data.bill_no._getValue());
		IqpAssetRegiApp.cont_no._setValue(data.cont_no._getValue());
		IqpAssetRegiApp.cus_id._setValue(data.cus_id._getValue());
		IqpAssetRegiApp.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		IqpAssetRegiApp.cur_type._setValue(data.cur_type._getValue());
		IqpAssetRegiApp.loan_amt._setValue(data.bill_amt._getValue());
		IqpAssetRegiApp.loan_balance._setValue(data.bill_bal._getValue());
		IqpAssetRegiApp.inner_owe_int._setValue(data.inner_owe_int._getValue());
		IqpAssetRegiApp.out_owe_int._setValue(data.out_owe_int._getValue());
		IqpAssetRegiApp.five_class._setValue(data.five_class._getValue());
		IqpAssetRegiApp.twelve_cls_flg._setValue(data.twelve_cls_flg._getValue());

		IqpAssetRegiApp.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		IqpAssetRegiApp.fina_br_id_displayname._setValue(data.fina_br_id_displayname._getValue());
		IqpAssetRegiApp.manager_br_id._setValue(data.manager_br_id._getValue());
		IqpAssetRegiApp.fina_br_id._setValue(data.fina_br_id._getValue());

		var bill_no = data.bill_no._getValue();
		var url = '<emp:url action="checkIqpAssetRegiApp.do"/>?bill_no='+bill_no;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					
				}else {
					alert(msg);

					IqpAssetRegiApp.bill_no._setValue("");
					IqpAssetRegiApp.cont_no._setValue("");
					IqpAssetRegiApp.cus_id._setValue("");
					IqpAssetRegiApp.cus_id_displayname._setValue("");
					IqpAssetRegiApp.cur_type._setValue("");
					IqpAssetRegiApp.loan_amt._setValue("");
					IqpAssetRegiApp.loan_balance._setValue("");
					IqpAssetRegiApp.inner_owe_int._setValue("");
					IqpAssetRegiApp.out_owe_int._setValue("");
					IqpAssetRegiApp.five_class._setValue("");
					IqpAssetRegiApp.twelve_cls_flg._setValue("");
					IqpAssetRegiApp.manager_br_id_displayname._setValue("");
					IqpAssetRegiApp.fina_br_id_displayname._setValue("");
					IqpAssetRegiApp.manager_br_id._setValue("");
					IqpAssetRegiApp.fina_br_id._setValue("");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}

	//异步校验所选产品是否可做资产证券化登记 
	function doCheck(){
		var bill_no = IqpAssetRegiApp.bill_no._getValue();
		var url = '<emp:url action="checkIqpAssetRegiPrd.do"/>?bill_no='+bill_no;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				//alert(o.responseText+"hhhhhh");
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					alert("可以做登记");
					doSub();
				}else {
					//alert("不可以做登记");
					alert(msg);
					IqpAssetRegiApp.bill_no._setValue("");
					IqpAssetRegiApp.cont_no._setValue("");
					IqpAssetRegiApp.cus_id._setValue("");
					IqpAssetRegiApp.cus_id_displayname._setValue("");
					IqpAssetRegiApp.cur_type._setValue("");
					IqpAssetRegiApp.loan_amt._setValue("");
					IqpAssetRegiApp.loan_balance._setValue("");
					IqpAssetRegiApp.inner_owe_int._setValue("");
					IqpAssetRegiApp.out_owe_int._setValue("");
					IqpAssetRegiApp.five_class._setValue("");
					IqpAssetRegiApp.twelve_cls_flg._setValue("");
					IqpAssetRegiApp.manager_br_id_displayname._setValue("");
					IqpAssetRegiApp.fina_br_id_displayname._setValue("");
					IqpAssetRegiApp.manager_br_id._setValue("");
					IqpAssetRegiApp.fina_br_id._setValue("");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
	function doReturn() {
		history.go(-1);
	};

	function getAcctOrgID(data){
		IqpAssetRegiApp.fina_br_id._setValue(data.organno._getValue());
		IqpAssetRegiApp.fina_br_id_displayname._setValue(data.organname._getValue());
	};
	function getOrgID(data){
		IqpAssetRegiApp.manager_br_id._setValue(data.organno._getValue());
		IqpAssetRegiApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAssetRegiAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAssetRegiAppGroup" title="信贷资产登记" maxColumn="2">
			<emp:select id="IqpAssetRegiApp.regi_type" label="业务类别" required="true" dictname="STD_ZB_REGI_TYPE"/>
			<emp:pop id="IqpAssetRegiApp.bill_no" label="借据编号" url="AccViewPop.do?from=Average&returnMethod=returnAcc" required="true" />
			<emp:text id="IqpAssetRegiApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.cus_id" label="客户编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetRegiApp.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetRegiApp.loan_amt" label="贷款金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetRegiApp.loan_balance" label="贷款余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetRegiApp.inner_owe_int" label="表内欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetRegiApp.out_owe_int" label="表外欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAssetRegiApp.five_class" label="五级分类" required="false" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="IqpAssetRegiApp.twelve_cls_flg" label="十二级分类" required="false" readonly="true" dictname="STD_ZB_TWELVE_CLASS"/>
			
			<emp:pop id="IqpAssetRegiApp.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"  readonly="true"/>
			<emp:pop id="IqpAssetRegiApp.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true" readonly="true"/>
		    <emp:text id="IqpAssetRegiApp.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetRegiApp.fina_br_id" label="账务机构" hidden="true" />
			
			<emp:textarea id="IqpAssetRegiApp.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="1" title="登记信息" maxColumn="2">
			<emp:text id="IqpAssetRegiApp.input_id_displayname" label="登记人"  required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_br_id_displayname" label="登记机构"  required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}"  readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_id" label="登记人" maxlength="40" required="false" defvalue="${context.currentUserId}" hidden="true" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true" readonly="true"/>
			<emp:select id="IqpAssetRegiApp.approve_status" label="审批状态" required="false" defvalue="000" readonly="true" hidden="true" dictname="WF_APP_STATUS" />
		    <emp:text id="IqpAssetRegiApp.serno" label="业务流水号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="check" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

