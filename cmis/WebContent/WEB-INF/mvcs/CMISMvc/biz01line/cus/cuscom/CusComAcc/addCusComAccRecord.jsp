<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function loadCusInfo(){
		CusComAcc.acc_no._obj.addOneButton('uniquCheck','获 取', getAcctNo);
	}

	function getAcctNo(){
 		var acctNo = CusComAcc.acc_no._getValue();
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
				var retMsg = jsonstr.retMsg;
 				var ACCT_NO = jsonstr.BODY.AcctNoCrdNo; //账号
				var ACCT_NAME = jsonstr.BODY.AcctNm; //账户名称
				var ACCT_TYPE = jsonstr.BODY.AcctTp; //账户类型
				var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.TxnInstCd; //开户机构
				var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.OPEN_ACCT_BRANCH_NAME; //开户机构名称
				var ORG_NO = jsonstr.BODY.ORG_NO;
				var OPEN_ACCT_DATE = jsonstr.BODY.OpnAcctDt.substr(0,4)+"-"+jsonstr.BODY.OpnAcctDt.substr(4,2)+"-"+jsonstr.BODY.OpnAcctDt.substr(6,2); //账户开户日期
				/*modified by wangj 需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
                                                                                             变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求 begin*/
				acct_type=ACCT_TYPE;
				/*modified by wangj 需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
                                                                                             变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求 end*/
				var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.AcctBlngInstNo;
				var OPEN_ACCT_BRANCH_NAME = jsonstr.BODY.AcctBlngInstNo_displayname;
				var ORG_NO = jsonstr.BODY.TxnInstCd;//结算机构
				var ACCT_GL_CODE = jsonstr.BODY.GL_CODE;//增加科目号
				var CCY=jsonstr.BODY.CCY;//增加币种
				var C_INTERBANK_ID=jsonstr.BODY.C_INTERBANK_ID;//银联行号
				if(flag == "success"){
					var cus_name = CusComAcc.cus_id_displayname._getValue();
					//if(cus_name==ACCT_NAME){
						CusComAcc.acc_name._setValue(ACCT_NAME);
						CusComAcc.acc_type._setValue(ACCT_TYPE);
						CusComAcc.acc_open_org._setValue(OPEN_ACCT_BRANCH_ID);
						CusComAcc.acc_open_orgname._setValue(OPEN_ACCT_BRANCH_NAME);
						CusComAcc.acc_org._setValue(OPEN_ACCT_BRANCH_ID);
						CusComAcc.acc_orgname._setValue(OPEN_ACCT_BRANCH_NAME);//此处核算机构与开户机构一致（核心确认），机构名称取一样
						CusComAcc.acc_date._setValue(OPEN_ACCT_DATE);//账户开户日期
					//}else{
					//	alert('该账号不属于当前客户！');
					//}
				}else {
					alert(retMsg); 
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
		//var url = '<emp:url action="clientTrade4Esb.do"/>?acct_no='+acctNo+'&service_code=11003000007&sence_code=17';
		var url = '<emp:url action="getIqpCusAcctForEsb.do"/>?acct_no='+acctNo;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };
	
	function doBack() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComAcc.cus_id._obj.element.value;
		var paramStr="CusComAcc.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComAccList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doAdd(){
		var form = document.getElementById("submitForm");
        var result = CusComAcc._checkAll();
        if(result){
            CusComAcc._toForm(form);
            toSubmitForm(form);
        }//else alert("请输入必填项！");
	}
	
	function toSubmitForm(form){
        var handleSuccess = function(o){
              if(o.responseText !== undefined) {
	              try {
	                  var jsonstr = eval("("+o.responseText+")");
	              } catch(e) {
	                  alert("Parse jsonstr define error!"+e);
	                  return;
	              }
	              var flag = jsonstr.flag;
	              if(flag=="保存成功"){
	                  alert("保存成功!");
	                  doBack();
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
          var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="loadCusInfo()">
	<emp:form id="submitForm" action="addCusComAccRecord.do" method="POST">
		<emp:gridLayout id="CusComAccGroup" title="结算账户登记" maxColumn="2">
			<emp:text id="CusComAcc.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="CusComAcc.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_no" label="账户帐号" maxlength="32" required="true" />
			<emp:text id="CusComAcc.acc_name" label="账户名称" maxlength="80" required="true" readonly="true"/>
			<emp:date id="CusComAcc.acc_date" label="账户开户日期" readonly="true"/>
			<emp:select id="CusComAcc.acc_type" label="账号类型" dictname="STD_ZB_CUS_ACC_TYPE" readonly="true"/>
			<emp:text id="CusComAcc.acc_open_org" label="开户机构" maxlength="20" required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_open_orgname" label="开户机构名称" maxlength="80" required="false" readonly="true" />
			<emp:text id="CusComAcc.acc_org" label="核算机构" maxlength="20" required="false" readonly="true"/>
			<emp:text id="CusComAcc.acc_orgname" label="核算机构名称" maxlength="80" required="false" readonly="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="add" label="确定"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>