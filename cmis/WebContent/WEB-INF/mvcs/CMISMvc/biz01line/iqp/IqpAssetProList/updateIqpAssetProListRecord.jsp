<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsIqpAssetProList.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function onload(){
		IqpAssetProList.bill_no._obj.addOneButton("bill_no","选择",getBillForm);
		IqpAssetProList.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpAssetProList.cont_no._obj.addOneButton("cont_no","查看",getContForm);
		IqpAssetProList.bill_no._obj.addOneButton("bill_no","查看",getBillNoForm);
		/**过滤掉"客户条线"中不需要的数据     2014-08-04  邓亚辉*/
		removeOpts(IqpAssetProList.belg_line,'BL_ALL','');
    }

	function getBillForm(){
		var cus_id = IqpAssetProList.cus_id._getValue();
		var url = "<emp:url action='queryIqpAssetRegiPop.do'/>&returnMethod=getBill&restrictUsed=flase&regi_type=02";
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
	};

	function getBill(data){
		IqpAssetProList.bill_no._setValue(data.bill_no._getValue());
		IqpAssetProList.cont_no._setValue(data.cont_no._getValue());
		IqpAssetProList.cus_id._setValue(data.cus_id._getValue());
		IqpAssetProList.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		IqpAssetProList.cur_type._setValue(data.cur_type._getValue());
		IqpAssetProList.loan_amt._setValue(data.loan_amt._getValue());
		IqpAssetProList.loan_balance._setValue(data.loan_balance._getValue());
		IqpAssetProList.inner_owe_int._setValue(data.inner_owe_int._getValue());
		IqpAssetProList.out_owe_int._setValue(data.out_owe_int._getValue());
		IqpAssetProList.five_class._setValue(data.five_class._getValue());
		IqpAssetProList.twelve_cls_flg._setValue(data.twelve_cls_flg._getValue());

		IqpAssetProList.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		IqpAssetProList.fina_br_id_displayname._setValue(data.fina_br_id_displayname._getValue());
		IqpAssetProList.manager_br_id._setValue(data.manager_br_id._getValue());
		IqpAssetProList.fina_br_id._setValue(data.fina_br_id._getValue());
	};
	
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetProList._checkAll()){
			IqpAssetProList._toForm(form);
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
						alert("修改成功!");
						var url = '<emp:url action="queryIqpAssetProListList.do"/>?serno='+IqpAssetProList.serno._getValue();
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("修改异常!");
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

	function getAcctOrgID(data){
		IqpAssetProList.fina_br_id._setValue(data.organno._getValue());
		IqpAssetProList.fina_br_id_displayname._setValue(data.organname._getValue());
	};
	function getOrgID(data){
		IqpAssetProList.manager_br_id._setValue(data.organno._getValue());
		IqpAssetProList.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/*--user code end--*/
	/**新增"客户条线"、"到期日期"字段   2014-08-04    邓亚辉*/
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpAssetProListRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetProListGroup" title="资产清单" maxColumn="2">
			<emp:text id="IqpAssetProList.serno" label="业务编号" maxlength="40" required="true" defvalue="${context.serno}" readonly="true" colSpan="2"/>
			<emp:text id="IqpAssetProList.bill_no" label="借据编号" maxlength="40" required="true" />
			
			<emp:text id="IqpAssetProList.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetProList.cus_id" label="客户编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetProList.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetProList.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetProList.loan_amt" label="贷款金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetProList.loan_balance" label="贷款余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetProList.inner_owe_int" label="表内欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetProList.out_owe_int" label="表外欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAssetProList.five_class" label="五级分类" required="false" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="IqpAssetProList.twelve_cls_flg" label="十二级分类" required="false" readonly="true" dictname="STD_ZB_TWELVE_CLASS"/>
			
			
			<emp:date id="IqpAssetProList.end_date" label="到期日期" required="false" dataType="Date" readonly="true"/>
			<emp:text id="IqpAssetProList.agent_asset_acct" label="代理资产资金账号" maxlength="40" required="true"/>
			
			<emp:pop id="IqpAssetProList.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no"/>
			<emp:pop id="IqpAssetProList.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true" readonly="true"/>
		    <emp:text id="IqpAssetProList.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetProList.fina_br_id" label="账务机构" hidden="true" />
			<!--隐藏客户条线页面显示   2014-08-20  王青	 -->
			<emp:select id="IqpAssetProList.belg_line" label="客户条线" required="false" dictname="STD_ZB_BUSILINE" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
