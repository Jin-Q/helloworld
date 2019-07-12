<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad(){
		document.getElementById("base_tab").href="javascript:reLoad();";
	}
	
	function doSub(data){
		var form = document.getElementById("submitForm");
		if(IqpAssetTransApp._checkAll()){
			IqpAssetTransApp._toForm(form);
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
						if(data == "subWF"){
							getApplyTypeByPrdId();
						}else{
							alert("保存成功");
						}
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

	//-----------通过产品编号查询产品配置中使用流程类型----------
    function getApplyTypeByPrdId(){
    	var prdId = IqpAssetTransApp.prd_id._getValue();
    	var url = '<emp:url action="getIqpApplyTypeByPrdId.do"/>?prdid='+prdId;
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
    			var apply_type = jsonstr.apply_type;
    			if(flag == "success"){
    				doSubmitWF(apply_type);
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
    	//var postData = YAHOO.util.Connect.setForm(form);	
    	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    };

  	//-----------提交流程----------
    function doSubmitWF(apply_type){
    	var serno = IqpAssetTransApp.serno._getValue();
    	var cus_id = IqpAssetTransApp.toorg_no._getValue();
    	var cus_name = IqpAssetTransApp.toorg_no_displayname._getValue();
    	var approve_status = IqpAssetTransApp.approve_status._getValue(); 
    	WfiJoin.table_name._setValue("IqpAssetTransApp");
    	WfiJoin.pk_col._setValue("serno");
    	WfiJoin.pk_value._setValue(serno);
    	WfiJoin.cus_id._setValue(cus_id);
    	WfiJoin.cus_name._setValue(cus_name);
    	WfiJoin.prd_pk._setValue(IqpAssetTransApp.prd_id._getValue());
    	WfiJoin.prd_name._setValue(IqpAssetTransApp.prd_name._getValue());
    	WfiJoin.amt._setValue(IqpAssetTransApp.trans_amt._getValue());
    	WfiJoin.wfi_status._setValue(approve_status);
    	WfiJoin.status_name._setValue("approve_status");
    	WfiJoin.appl_type._setValue(apply_type);
    	initWFSubmit(false);
    };

	function returnCusId(data){
		IqpAssetTransApp.toorg_no._setValue(data.same_org_no._getValue());
		IqpAssetTransApp.toorg_no_displayname._setValue(data.same_org_cnname._getValue());
	}

	function getOrgID(data){
    	IqpAssetTransApp.manager_br_id._setValue(data.organno._getValue());
		IqpAssetTransApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function reLoad(){
		var serno = IqpAssetTransApp.serno._getValue();
		var url = '<emp:url action="getIqpAssetTransAppUpdatePage.do"/>?op=update&serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location = url;
		//window.location.reload();
	};

	function doSubWF(){
		if(!IqpAssetTransApp._checkAll()){
			return;
		}
		checkAssetInfo();
	};

	//放入流程前先校验资产信息是否完整
	   function checkAssetInfo(){
		   var serno = IqpAssetTransApp.serno._getValue();
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
	       				doSub('subWF');
	       			}else {
	       				alert(msg);
	       				return;
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

	       	var url="<emp:url action='checkAssetInfoForAssetTrans.do'/>?serno="+serno;
	       	url = EMPTools.encodeURI(url);
	       	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	   }

	function checkTransDate(){
		var trans_date=IqpAssetTransApp.trans_date._getValue();
		var openDay='${context.OPENDAY}';
		if(trans_date){
			var flag = CheckDate1BeforeDate2(openDay,trans_date);
			if(!flag){
				alert("【转让日期】必须大于当前营业日期！");
				IqpAssetTransApp.trans_date._setValue("");
				return false;
			}
	     }
	}

	function checkIntStartDate(){
		var int_start_date=IqpAssetTransApp.int_start_date._getValue();
		var openDay='${context.OPENDAY}';
		if(int_start_date){
			var flag = CheckDate1BeforeDate2(openDay,int_start_date);
			if(!flag){
				alert("【起息日】必须大于当前营业日期！");
				IqpAssetTransApp.int_start_date._setValue("");
				return false;
			}
	     }
	}

	function doReturn() {
		var url = '<emp:url action="queryIqpAssetTransAppList.do"/>?menuId=${context.menuId}';  
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="base_tab" id="mainTab">
	<emp:tab label="基本信息" id="base_tab"> 
	<emp:form id="submitForm" action="updateIqpAssetTransAppRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetTransAppGroup" title="资产流转申请" maxColumn="2">
			<emp:text id="IqpAssetTransApp.serno" label="业务编号" maxlength="40" readonly="true" />
			<emp:select id="IqpAssetTransApp.trans_type" label="业务类型" required="true" dictname="STD_ZB_TRANS_TYPE" />
			<emp:text id="IqpAssetTransApp.pro_name" label="项目名称" maxlength="80" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransApp.pro_short_name" label="项目简称" maxlength="80" required="true" />
			<emp:text id="IqpAssetTransApp.prd_id" label="产品编码" maxlength="6" required="false" readonly="true"/>
			<emp:text id="IqpAssetTransApp.prd_name" label="产品名称" maxlength="40" required="false" defvalue="资产流转" readonly="true"/>
			<emp:pop id="IqpAssetTransApp.toorg_no" label="交易对手行号" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId" required="true" />
			<emp:text id="IqpAssetTransApp.toorg_no_displayname" label="交易对手行名" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="IqpAssetTransApp.cur_type" label="币种" readonly="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="IqpAssetTransApp.loan_amt_totl" label="贷款总金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransApp.loan_balance_totl" label="贷款总余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransApp.trans_amt" label="转让金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransApp.trans_rate" label="转让比率" maxlength="16" required="false" readonly="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetTransApp.trans_qnt" label="转让笔数" maxlength="38" required="false" readonly="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpAssetTransApp.trans_date" label="转让日期" required="true" onblur="checkTransDate()"/>
			<emp:date id="IqpAssetTransApp.int_start_date" label="起息日" required="true" onblur="checkIntStartDate()"/>
			<emp:select id="IqpAssetTransApp.interest_type" label="收息方式" hidden="true" dictname="STD_RCV_INT_TYPE"/>
			<emp:textarea id="IqpAssetTransApp.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">
			<emp:pop id="IqpAssetTransApp.manager_br_id_displayname" label="管理机构" required="true"  buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:select id="IqpAssetTransApp.flow_type" label="流程类型"  required="false" defvalue="01" dictname="STD_ZB_FLOW_TYPE" readonly="true"/>
		    <emp:select id="IqpAssetTransApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" readonly="true" required="false" hidden="true" defvalue="000"/>
		   	<emp:text id="IqpAssetTransApp.input_id_displayname" label="登记人" required="false"  readonly="true" defvalue="${context.currentUserName}"/>
			<emp:text id="IqpAssetTransApp.input_br_id_displayname" label="登记机构" required="false"  readonly="true" defvalue="${context.organName}"/>
			
			<emp:date id="IqpAssetTransApp.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="IqpAssetTransApp.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="IqpAssetTransApp.input_id" label="登记人" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.currentUserId}"/>
			<emp:text id="IqpAssetTransApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false"  readonly="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="subWF" label="放入流程" op="update"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	</emp:tab>
   	<emp:ExtActTab></emp:ExtActTab>
  	</emp:tabGroup>
</body>
</html>
</emp:page>
