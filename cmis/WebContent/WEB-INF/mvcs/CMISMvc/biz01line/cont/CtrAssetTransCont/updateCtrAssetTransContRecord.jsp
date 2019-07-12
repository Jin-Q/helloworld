<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

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
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert("签订成功!");
						var url = '<emp:url action="queryCtrAssetTransContList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert(msg);
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

	function checkEndDate(){
		var end_date=CtrAssetTransCont.end_date._getValue();
		var openDay='${context.OPENDAY}';
		if(end_date){
			var flag = CheckDate1BeforeDate2(openDay,end_date);
			if(!flag){
				alert("【到期日期】必须大于当前营业日期！");
				CtrAssetTransCont.end_date._setValue("");
				return false;
			}

			var int_start_date = CtrAssetTransCont.int_start_date._getValue();
			if(int_start_date){
				flag = CheckDate1BeforeDate2(int_start_date,end_date);
				if(!flag){
					alert("【到期日期】必须大于【起息日】！");
					CtrAssetTransCont.end_date._setValue("");
					return false;
				}
			}
	     }
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab">
	<emp:tab label="基本信息" id="base_tab"> 
	<emp:form id="submitForm" action="updateCtrAssetTransContRecord.do" method="POST">
		<emp:gridLayout id="CtrAssetTransContGroup" maxColumn="2" title="资产流转协议">
			<emp:text id="CtrAssetTransCont.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CtrAssetTransCont.serno" label="业务编号" maxlength="40" readonly="true" />
			<emp:text id="CtrAssetTransCont.pro_name" label="项目名称" maxlength="80" required="false" readonly="true"/>
			<emp:text id="CtrAssetTransCont.pro_short_name" label="项目简称" maxlength="80" required="true" />
			<emp:text id="CtrAssetTransCont.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
			<emp:text id="CtrAssetTransCont.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产流转" readonly="true"/>
			<emp:select id="CtrAssetTransCont.trans_type" label="业务类型" required="true" dictname="STD_ZB_TRANS_TYPE" readonly="true"/>
			<emp:date id="CtrAssetTransCont.ser_date" label="签订日期"  required="true" readonly="true" defvalue="${context.OPENDAY}" />
			<emp:text id="CtrAssetTransCont.toorg_no" label="交易对手行号" readonly="true" required="true" />
			<emp:text id="CtrAssetTransCont.toorg_no_displayname" label="交易对手行名" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="CtrAssetTransCont.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CtrAssetTransCont.loan_amt_totl" label="贷款总金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrAssetTransCont.loan_balance_totl" label="贷款总余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrAssetTransCont.trans_amt" label="转让金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrAssetTransCont.trans_rate" label="转让比率" maxlength="16" required="false" readonly="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CtrAssetTransCont.trans_qnt" label="转让笔数" maxlength="38" required="false" readonly="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="CtrAssetTransCont.trans_date" label="转让日期" required="true" readonly="true" colSpan="2"/>
			<emp:date id="CtrAssetTransCont.int_start_date" label="起息日" required="true" readonly="true"/>
			<emp:date id="CtrAssetTransCont.defray_date" label="支付日" required="false" />
			<emp:date id="CtrAssetTransCont.end_date" label="到期日期" required="false"  onblur="checkEndDate()"/>
			<emp:date id="CtrAssetTransCont.inure_date" label="生效日期" required="false" readonly="true"/>
			<emp:date id="CtrAssetTransCont.deliver_date" label="交割日" required="false" readonly="true"/>
			<emp:textarea id="CtrAssetTransCont.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
			<emp:select id="CtrAssetTransCont.interest_type" label="收息方式" hidden="true" dictname="STD_RCV_INT_TYPE" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:text id="CtrAssetTransCont.manager_br_id_displayname" label="管理机构" required="true" readonly="true"/>
			<emp:text id="CtrAssetTransCont.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="CtrAssetTransCont.cont_status" label="项目状态" maxlength="5" hidden="true" />
		   	<emp:text id="CtrAssetTransCont.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="CtrAssetTransCont.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="CtrAssetTransCont.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="CtrAssetTransCont.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="CtrAssetTransCont.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="签订" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	</emp:tab>
   	<emp:ExtActTab></emp:ExtActTab>
  	</emp:tabGroup>
</body>
</html>
</emp:page>
