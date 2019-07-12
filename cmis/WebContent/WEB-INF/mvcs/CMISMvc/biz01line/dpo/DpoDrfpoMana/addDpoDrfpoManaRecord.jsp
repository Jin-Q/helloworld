<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
	function doOnLoad(){
		IqpDrfpoMana.bail_acc_no._obj.addOneButton("bailBtn","获取",getOrgCode);
		/**add by lisj 2014年11月10日 增加影像押品编号字段  begin**/
		IqpDrfpoMana.image_guaranty_no._obj.addOneButton("image_guaranty_no","获取",getBillForm);
		/**add by lisj 2014年11月10日 增加影像押品编号字段  end**/
	};
	/**add by lisj 2014年11月10日 增加影像押品编号字段  begin**/
	function getBillForm(){
		var cus_id = IqpDrfpoMana.cus_id._getValue();
		if(cus_id==null||cus_id==''){
			alert('请先录入客户码');
		}else{
			var url = "<emp:url action='delImageGuaranteeNoAction.do'/>&returnMethod=getRate";
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}
	};
	
	function getRate(data){
		IqpDrfpoMana.image_guaranty_no._setValue(data.PLEDGE_NO._getValue());
	};
	/**add by lisj 2014年11月10日 增加影像押品编号字段  end**/
	function getOrgCode(){
		    var acctNo = IqpDrfpoMana.bail_acc_no._getValue();
	        if(acctNo == null || acctNo == ""){
				alert("请先输入保证金账号信息！");
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
					var GUARANTEE_ACCT_NO = jsonstr.BODY.GUARANTEE_ACCT_NO;
					var GUARANTEE_ACCT_NAME = jsonstr.BODY.GUARANTEE_ACCT_NAME;
					var CCY = jsonstr.BODY.CCY;
					var AMT = jsonstr.BODY.AMT;
					var GUARANTEE_TYPE = jsonstr.BODY.GUARANTEE_TYPE;
					var INT_RATE = jsonstr.BODY.INT_RATE;
					var INTER_FLT_RATE = jsonstr.BODY.INTER_FLT_RATE;
					var TERM = jsonstr.BODY.TERM;
					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
					if(flag == "success"){
						IqpDrfpoMana.bail_acc_name._setValue(GUARANTEE_ACCT_NAME);
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
			var url = '<emp:url action="clientTrade4Esb.do"/>?bail_acct_no='+acctNo+'&service_code=11003000007&sence_code=16';	
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	}
	//选择责任人返回方法
	function setconId(data){
		IqpDrfpoMana.manager_id_displayname._setValue(data.actorname._getValue());
		IqpDrfpoMana.manager_id._setValue(data.actorno._getValue());
		IqpDrfpoMana.manager_br_id._setValue(data.orgid._getValue());
		IqpDrfpoMana.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//IqpDrfpoMana.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};	

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
					IqpDrfpoMana.manager_br_id._setValue(jsonstr.org);
					IqpDrfpoMana.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpDrfpoMana.manager_br_id._setValue("");
					IqpDrfpoMana.manager_br_id_displayname._setValue("");
					IqpDrfpoMana.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpDrfpoMana.manager_id._getValue();
					IqpDrfpoMana.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpDrfpoMana.manager_br_id._setValue("");
					IqpDrfpoMana.manager_br_id_displayname._setValue("");
					IqpDrfpoMana.manager_br_id_displayname._obj._renderReadonly(false);
					IqpDrfpoMana.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpDrfpoMana.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//选择机构信息返回方法
	function getOrgID(data){
		IqpDrfpoMana.manager_br_id._setValue(data.organno._getValue());
		IqpDrfpoMana.manager_br_id_displayname._setValue(data.organname._getValue());
	};	
	//选择客户POP框返回方法
	function returnCus(data){
		IqpDrfpoMana.cus_id._setValue(data.cus_id._getValue());
		IqpDrfpoMana.cus_id_displayname._setValue(data.cus_name._getValue());
	}		
	function doReturn() {
		var url = '<emp:url action="queryIqpDrfpoManaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doNext() {
		if(!IqpDrfpoMana._checkAll()){
			alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						alert("保存成功!");
						var drfpo_no = IqpDrfpoMana.drfpo_no._getValue();
						var url = '<emp:url action="getIqpdpoTabHelp.do"/>?op=update&drfpo_no='+drfpo_no;
						url = EMPTools.encodeURI(url);
						window.location=url;
					}else{
						alert("保存失败!");
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			IqpDrfpoMana._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var url = '<emp:url action="addDpoDrfpoManaRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">		
		<emp:form id="submitForm" action="addDpoDrfpoManaRecord.do" method="POST">
			<emp:gridLayout id="IqpDrfpoManaGroup" title="票据池管理" maxColumn="2">
				<emp:text id="IqpDrfpoMana.drfpo_no" label="池编号" maxlength="30" required="false" hidden="true"/>
				<emp:pop id="IqpDrfpoMana.cus_id" label="客户码" required="true" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" buttonLabel="选择"/>
				<emp:text id="IqpDrfpoMana.cus_id_displayname" label="客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
				<emp:text id="IqpDrfpoMana.image_guaranty_no" label="影像押品编号" maxlength="40" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
				<emp:text id="IqpDrfpoMana.bail_acc_no" label="回款保证金账号" maxlength="40" required="true" />
				<emp:text id="IqpDrfpoMana.bail_acc_name" label="回款保证金账户名" maxlength="60" required="false" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
				<emp:select id="IqpDrfpoMana.drfpo_type" label="池类型" required="true" dictname="STD_DRFPO_TYPE" colSpan="2"/>
				<emp:text id="IqpDrfpoMana.bill_amt" label="在池票面总金额" maxlength="18" required="true" dataType="Currency" defvalue="0" readonly="true"/>
				<emp:text id="IqpDrfpoMana.to_bill_amt" label="待入池票面总金额" maxlength="18" required="true" dataType="Currency" defvalue="0" readonly="true"/>
				<emp:textarea id="IqpDrfpoMana.memo" label="备注" maxlength="250" required="false" colSpan="2" />
				<emp:select id="IqpDrfpoMana.status" label="状态" required="false" dictname="STD_DRFPO_STATUS" defvalue="00" readonly="true"/>
			</emp:gridLayout>
			<emp:gridLayout id="IqpDrfpoManaGroup" maxColumn="2" title="登记信息">
				<emp:pop id="IqpDrfpoMana.manager_id_displayname" label="责任人" required="true" readonly="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
				<emp:pop id="IqpDrfpoMana.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
				<emp:text id="IqpDrfpoMana.input_id_displayname" label="登记人" readonly="true" required="true"  />
				<emp:text id="IqpDrfpoMana.input_br_id_displayname" label="登记机构"  readonly="true" required="true" />
				<emp:text id="IqpDrfpoMana.input_date" label="登记日期" required="true" readonly="true"/>
				<emp:text id="IqpDrfpoMana.manager_br_id" label="管理机构"  required="true" hidden="true"/>
				<emp:text id="IqpDrfpoMana.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
				<emp:text id="IqpDrfpoMana.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"   />
				<emp:text id="IqpDrfpoMana.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"  />
			</emp:gridLayout>
			
			<div align="center">
				<br>
				<emp:button id="next" label="确定" op="add"/>
				<emp:button id="return" label="返回"/>
			</div>
		</emp:form>
	</emp:tab>
</emp:tabGroup>
</body>
</html>
</emp:page>

