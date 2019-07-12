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
		if(CtrAssetTransCont._checkAll()){
			CtrAssetTransCont._toForm(form);
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
						alert("新增成功!");
						var url = '<emp:url action="queryCtrAssetTransContList.do"/>';
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCtrAssetTransContRecord.do" method="POST">
		
		<emp:gridLayout id="CtrAssetTransContGroup" title="资产流转协议" maxColumn="2">
			<emp:text id="CtrAssetTransCont.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:text id="CtrAssetTransCont.serno" label="业务编号" maxlength="40" required="false" />
			<emp:text id="CtrAssetTransCont.toorg_no" label="交易对手" maxlength="20" required="false" />
			<emp:text id="CtrAssetTransCont.trans_type" label="业务类型" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.loan_amt_totl" label="贷款总金额" maxlength="16" required="false" />
			<emp:text id="CtrAssetTransCont.loan_balance_totl" label="贷款总余额" maxlength="16" required="false" />
			<emp:text id="CtrAssetTransCont.trans_amt" label="转让金额" maxlength="16" required="false" />
			<emp:text id="CtrAssetTransCont.trans_rate" label="转让比率" maxlength="16" required="false" />
			<emp:text id="CtrAssetTransCont.trans_qnt" label="转让笔数" maxlength="38" required="false" />
			<emp:text id="CtrAssetTransCont.trans_date" label="转让日期" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.interest_type" label="收息方式" maxlength="1" required="false" />
			<emp:text id="CtrAssetTransCont.remarks" label="备注" maxlength="250" required="false" />
			<emp:text id="CtrAssetTransCont.int_start_date" label="起息日" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.deliver_date" label="交割日" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.defray_date" label="支付日" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.inure_date" label="生效日期" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.end_date" label="到期日期" maxlength="10" required="false" />
			<emp:text id="CtrAssetTransCont.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="CtrAssetTransCont.cont_status" label="项目状态" maxlength="5" required="false" />
			<emp:text id="CtrAssetTransCont.input_id" label="登记人" maxlength="40" required="false" />
			<emp:text id="CtrAssetTransCont.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="CtrAssetTransCont.input_date" label="登记日期" maxlength="10" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

