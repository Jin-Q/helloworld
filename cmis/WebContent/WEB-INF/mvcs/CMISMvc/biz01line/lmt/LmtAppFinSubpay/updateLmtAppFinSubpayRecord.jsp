<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
	<html>
	<head>
	<title>修改页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<%
		Context context = (Context) request
					.getAttribute(EMPConstance.ATTR_CONTEXT);
			String type = "";
			if (context.containsKey("type")) {
				type = context.getDataValue("type").toString();
			}
	%>
	<script type="text/javascript">
	
	/*--user code begin--*/
	function setconId(data){
		LmtAppFinSubpay.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppFinSubpay.manager_id._setValue(data.actorno._getValue());
		LmtAppFinSubpay.manager_br_id._setValue(data.orgid._getValue());
		LmtAppFinSubpay.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppFinSubpay.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					LmtAppFinSubpay.manager_br_id._setValue(jsonstr.org);
					LmtAppFinSubpay.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppFinSubpay.manager_br_id._setValue("");
					LmtAppFinSubpay.manager_br_id_displayname._setValue("");
					LmtAppFinSubpay.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppFinSubpay.manager_id._getValue();
					LmtAppFinSubpay.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppFinSubpay.manager_br_id._setValue("");
					LmtAppFinSubpay.manager_br_id_displayname._setValue("");
					LmtAppFinSubpay.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppFinSubpay.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppFinSubpay.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		LmtAppFinSubpay.manager_br_id._setValue(data.organno._getValue());
		LmtAppFinSubpay.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtSubpayList._toForm(form);
		LmtSubpayListList._obj.ajaxQuery(null,form);
	};

	function doGetAddLmtSubpayListPage() {//代偿明细新增
		var serno = LmtAppFinSubpay.serno._getValue();
		var cus_id = LmtAppFinSubpay.cus_id._getValue();
		var url = '<emp:url action="getLmtSubpayListAddPage.do"/>?serno='+serno+"&cus_id="+cus_id;
		url = EMPTools.encodeURI(url);
		var param = 'dialogWidth:800px';
		window.showModalDialog(url,'',param);
		window.location.reload();
	};
	
	function doGetUpdateLmtSubpayListPage() {//代偿明细修改
		var paramStr = LmtSubpayList._obj.getParamStr(['pk']);
		if (paramStr != null) {
			var cus_id = LmtAppFinSubpay.cus_id._getValue();
			var url = '<emp:url action="getLmtSubpayListUpdatePage.do"/>?'+paramStr+"&cus_id="+cus_id;
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:800px';
			window.showModalDialog(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtSubpayList() {//代偿明细查看
		var paramStr = LmtSubpayList._obj.getParamStr(['pk']);
		if (paramStr != null) {
			var cus_id = LmtAppFinSubpay.cus_id._getValue();
			var url = '<emp:url action="getLmtSubpayListViewPage.do"/>?'+paramStr+"&cus_id="+cus_id;
			url = EMPTools.encodeURI(url);
			var param = 'dialogWidth:800px';
			window.showModalDialog(url,'',param);
			window.location.reload();
		} else {
			alert('请先选择一条记录！');
		}
	};

	function returnCus(data){
		LmtAppFinSubpay.cus_id._setValue(data.cus_id._getValue());//客户码
		LmtAppFinSubpay.cus_id_displayname._setValue(data.cus_name._getValue());//客户名称
	}

	function doAdd() {
		var form = document.getElementById("submitForm");
		var result = LmtAppFinSubpay._checkAll();
		if(result){
			LmtAppFinSubpay._toForm(form)
			toSubmitFormAdd(form);
		}else alert("请输入必填项！");
	}
	
	function toSubmitFormAdd(form){
		var url = '<emp:url action="addLmtAppFinSubpayRecord.do"/>';
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="success"){
						alert("新增成功！");
						var type = '<%=type%>';
						var url = '<emp:url action="queryLmtAppFinSubpayList.do"/>?type='+type;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else {
					   alert(flag);
					   return;
				     }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	
	function doPauseSave(){
		var form = document.getElementById("submitForm");
		form.action = "getLmtAppFinSubpayAddPage.do";
		LmtAppFinSubpay._toForm(form);
		form.submit();
	}

	function checkEndDate(){
		var appDate = LmtAppFinSubpay.subpay_app_date._getValue();
		var endDate = LmtAppFinSubpay.subpay_end_date._getValue();
		if(appDate>endDate){
			alert("办结日期应大于等于申请日期！");
			LmtAppFinSubpay.subpay_end_date._setValue("");
		}
	}

	function doUpdatee() {
		var form = document.getElementById("submitForm");
		var result = LmtAppFinSubpay._checkAll();
		if(result){
			LmtAppFinSubpay._toForm(form)
			toSubmitFormUpate(form);
		}else alert("请输入必填项！");
	}
	
	function toSubmitFormUpate(form){
		var type = '<%=type%>';
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="success"){
						alert("修改成功！");
				     }else {
					   alert(flag);
					   return;
				     }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};

	function doReturn2List(){
		var type='<%=type%>';
		var url = '<emp:url action="queryLmtAppFinSubpayList.do"/>?type='+type;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	
	function doDeleteLmtSubpayList(){
		var serno = LmtSubpayList._obj.getSelectedData()[0].serno._getValue();
		var form = document.getElementById("submitForm");
		LmtAppFinSubpay._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var porderno = jsonstr.porderno;
				if(flag == "success"){
					alert("删除成功！");
					var url = '<emp:url action="getLmtAppFinSubpayUpdatePage.do"/>?serno='+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("删除失败！");
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
		
		var pk = LmtSubpayList._obj.getSelectedData()[0].pk._getValue();
		var url = '<emp:url action="deleteLmtSubpayListRecord.do"/>?pk='+pk+'&serno='+serno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
	};

	function doLoad(){
		LmtAppFinSubpay.pyee_acct._obj.addOneButton("acctNo","获取",getAcctNo);
	};

	function getAcctNo(){
		 var acctNo = LmtAppFinSubpay.pyee_acct._getValue();
	        if(acctNo == null || acctNo == ""){
				alert("请先输入账号信息！");
				return;
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
					var retMsg = jsonstr.mes;
					var ACCT_NAME = jsonstr.BODY.AcctNm;
					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.TxnInstCd; //开户机构
					var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.AcctBlngInstNo_displayname; //开户机构名称
					if(flag == "success"){
						LmtAppFinSubpay.pyee_name._setValue(ACCT_NAME);
						LmtAppFinSubpay.paorg_no._setValue(OPEN_ACCT_BRANCH_ID);
						LmtAppFinSubpay.paorg_name._setValue(OPEN_ACCT_BRANCH_NAME);
					}else {
						alert(retMsg); 
						LmtAppFinSubpay.pyee_name._setValue('');
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
			var url = '<emp:url action="getIqpCusAcctForEsb.do"/>?acct_no='+acctNo;	
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
   };
   
	function getOrgNo(data){
    	LmtAppFinSubpay.paorg_no._setValue(data.bank_no._getValue());
    	LmtAppFinSubpay.paorg_name._setValue(data.bank_name._getValue());
    };
	/*--user code end--*/
	
</script>
	</head>
	<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="基本信息" id="main_tabs">
			<emp:form id="submitForm" action="updateLmtAppFinSubpayRecord.do"
				method="POST">
				<emp:gridLayout id="LmtAppFinSubpayGroup" title="融资性担保公司代偿" maxColumn="2">
					<emp:text id="LmtAppFinSubpay.serno" label="业务编号" maxlength="40" required="true" colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true" />
					<emp:pop id="LmtAppFinSubpay.cus_id" label="担保公司客户码" url="queryAllCusPop.do?cusTypCondition=FinGuar&returnMethod=returnCus" required="true"  readonly="true"/>
					<emp:text id="LmtAppFinSubpay.cus_id_displayname" label="担保公司客户名称" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_cusname" />
					<emp:select id="LmtAppFinSubpay.subpay_cur_type" label="代偿币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" />
					<emp:text id="LmtAppFinSubpay.subpay_totl_limit" label="代偿总额" maxlength="18" required="false" readonly="true" dataType="Currency" />
					<emp:text id="LmtAppFinSubpay.subpay_times" label="本次代偿笔数" maxlength="10" required="false" readonly="true" colSpan="2" />
				<emp:text id="LmtAppFinSubpay.pyee_acct" label="收款人账号" required="true"/>
				<emp:text id="LmtAppFinSubpay.pyee_name" label="收款人账户名" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_cusname"/>
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
				<emp:pop id="LmtAppFinSubpay.paorg_no" label="收款人开户行行号"  required="true" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="getOrgNo" />
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
				<emp:text id="LmtAppFinSubpay.paorg_name" label="收款人开户行行名" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_cusname"/>
					<emp:date id="LmtAppFinSubpay.subpay_app_date" label="代偿申请日期" required="true" />
					<emp:date id="LmtAppFinSubpay.subpay_end_date" label="代偿办结日期" required="false" hidden="true" />
				</emp:gridLayout>
				<emp:gridLayout id="LmtAppFinSubpayGroup" title="登记信息" maxColumn="2">
					<emp:pop id="LmtAppFinSubpay.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" />
					<emp:pop id="LmtAppFinSubpay.manager_br_id_displayname" label="责任机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
					<emp:text id="LmtAppFinSubpay.input_id_displayname" label="登记人" required="false" readonly="true" />
					<emp:text id="LmtAppFinSubpay.input_br_id_displayname" label="登记机构" required="false" readonly="true" />
					<emp:text id="LmtAppFinSubpay.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.manager_id" label="责任人" maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true" />
					<emp:text id="LmtAppFinSubpay.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true" />
					<emp:select id="LmtAppFinSubpay.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true" />
				</emp:gridLayout>
				<div align="center"><br>
				<emp:button id="updatee" label="修改" op="update" /> 
				<emp:button id="return2List" label="返回" op="update" /></div>
			</emp:form>
			<div align="left">
			<%if("query".equals(type)){%> 
				<emp:button id="viewLmtSubpayList" label="查看" op="view" /> 
			<%}else{%> 
				<emp:button id="getAddLmtSubpayListPage" label="新增" op="add" /> 
				<emp:button id="getUpdateLmtSubpayListPage" label="修改" op="update" /> 
				<emp:button id="deleteLmtSubpayList" label="删除" op="remove" /> 
				<emp:button id="viewLmtSubpayList" label="查看" op="view" /> 
			<%}%>
			</div>
			<emp:table icollName="LmtSubpayList" pageMode="false" url="">
				<emp:text id="serno" label="流水号" />
				<emp:text id="prd_id_displayname" label="业务品种" />
				<emp:text id="cont_no" label="合同编号" />
				<emp:text id="subpay_bill_no" label="代偿借据编号" />
				<emp:text id="guar_mode" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" hidden="true"/>
				<emp:text id="bill_amt" label="借据金额" dataType="Currency" />
				<emp:text id="bill_bal" label="借据余额" dataType="Currency" />
				<emp:text id="int_cumu" label="欠息累计" dataType="Currency" />
				<emp:text id="subpay_cap" label="代偿本金" dataType="Currency" />
				<emp:text id="subpay_int" label="代偿利息" dataType="Currency" />
				<emp:text id="pk" label="主键" hidden="true" />
			</emp:table>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	</body>
	</html>
</emp:page>
