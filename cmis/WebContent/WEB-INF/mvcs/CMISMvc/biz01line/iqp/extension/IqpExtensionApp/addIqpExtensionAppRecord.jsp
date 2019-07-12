<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
	var prdId="";
	/*modified by wangj 需求编号【XD141222087】法人透支改造 end */
	/*--user code begin--*/
	/** 登记信息begin **/
	function setconId(data){
		IqpExtensionApp.manager_id_displayname._setValue(data.actorname._getValue());
		IqpExtensionApp.manager_id._setValue(data.actorno._getValue());
		IqpExtensionApp.manager_br_id._setValue(data.orgid._getValue());
		IqpExtensionApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//IqpExtensionApp.manager_br_id_displayname._obj._renderReadonly(true);
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
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					IqpExtensionApp.manager_br_id._setValue(jsonstr.org);
					IqpExtensionApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构 /客户经理所属团队机构
					IqpExtensionApp.manager_br_id._setValue("");
					IqpExtensionApp.manager_br_id_displayname._setValue("");
					IqpExtensionApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpExtensionApp.manager_id._getValue();
					IqpExtensionApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpExtensionApp.manager_br_id._setValue("");
					IqpExtensionApp.manager_br_id_displayname._setValue("");
					IqpExtensionApp.manager_br_id_displayname._obj._renderReadonly(false);
					IqpExtensionApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpExtensionApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		IqpExtensionApp.manager_br_id._setValue(data.organno._getValue());
		IqpExtensionApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	function selCusId(data){
		IqpExtensionApp.bill_no._setValue(data.bill_no._getValue());
		IqpExtensionApp.cont_no._setValue(data.cont_no._getValue());
		IqpExtensionApp.cus_id._setValue(data.cus_id._getValue());
		IqpExtensionApp.cus_name._setValue(data.cus_name._getValue());
		IqpExtensionApp.cur_type._setValue(data.cur_type._getValue());
		IqpExtensionApp.loan_amt._setValue(data.loan_amt._getValue());
		IqpExtensionApp.loan_balance._setValue(data.loan_balance._getValue());
		rate = data.rate._getValue();
		rate = rate==''?'0':rate;
		IqpExtensionApp.rate._setValue(rate);
		IqpExtensionApp.start_date._setValue(data.start_date._getValue());
		IqpExtensionApp.end_date._setValue(data.end_date._getValue());					
		//'展期金额'直接设为'贷款余额'
		IqpExtensionApp.extension_amt._setValue(data.loan_balance._getValue());
		
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
		IqpExtensionApp.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		IqpExtensionApp.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		IqpExtensionApp.manager_id._setValue(data.cust_mgr._getValue());
		IqpExtensionApp.manager_br_id._setValue(data.main_br_id._getValue());
		doChangeOrgUrl();
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
		var prd_id = data.prd_id._getValue();		
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
					if(prd_id < '2' ){	//贷款类
						IqpExtensionApp.base_rate._obj._renderHidden(false);
						IqpExtensionApp.base_rate._obj._renderRequired(true);
					}else{
						IqpExtensionApp.base_rate._obj._renderHidden(true);
						IqpExtensionApp.base_rate._obj._renderRequired(false);
					}
				}else {
					alert("此借据已有在途的展期业务!");
					IqpExtensionApp.bill_no._setValue("");
					IqpExtensionApp.cont_no._setValue("");
					IqpExtensionApp.cus_id._setValue("");
					IqpExtensionApp.cus_name._setValue("");
					IqpExtensionApp.cur_type._setValue("");
					IqpExtensionApp.loan_amt._setValue("");
					IqpExtensionApp.loan_balance._setValue("");
					IqpExtensionApp.rate._setValue("");
					IqpExtensionApp.start_date._setValue("");
					IqpExtensionApp.end_date._setValue("");
					IqpExtensionApp.extension_amt._setValue("");
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
		var url="<emp:url action='checkUnique.do'/>&type=ExtensionAppCheck&value="+data.bill_no._getValue();
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		/*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
		prdId=prd_id;
		if("100051"==prdId){
			IqpExtensionApp.base_rate._setValue(data.ruling_ir._getValue());
			IqpExtensionApp.extension_rate._setValue(rate);
			IqpExtensionApp.base_rate._obj._renderReadonly(true);
			IqpExtensionApp.extension_rate._obj._renderReadonly(true);
		}
		/*modified by wangj 需求编号【XD141222087】法人透支改造 end */
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
	function doChangeOrgUrl(){
		var handleSuccess = function(o) {
   			if (o.responseText !== undefined) {
   				try {
   					var jsonstr = eval("(" + o.responseText + ")");
   				} catch (e) {
   					alert("Parse jsonstr define error!" + e.message);
   					return;
   				}
   				var flag = jsonstr.flag;
				if("belg2team" == flag){//客户经理只属于团队
					var manager_id = IqpExtensionApp.manager_id._getValue();
					IqpExtensionApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};

   		var manager_id = IqpExtensionApp.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	/** 根据给定的两个日期，求出它们之间的期数 **/
	function getQs(rqFrom,rqTo){
		var yearF,yearT,monF,monT,dayF,dayT,Qs,Qs1,Qs2,Qs3;
		yaarF = rqFrom.substring(0,4);
		yearT = rqTo.substring(0,4);
		monF = rqFrom.substring(5,7);
		monT = rqTo.substring(5,7);
		dayF = rqFrom.substring(8,10);
		dayT = rqTo.substring(8,10);
		Qs1 = (parseInt(yearT)-parseInt(yaarF))*12;
		Qs2 = parseInt(parseFloat(monT))-parseInt(parseFloat(monF));
		if(parseInt(parseFloat(dayT)) > parseInt(parseFloat(dayF)))
			Qs3 = 1;
		else
			Qs3 = 0;
		Qs = parseInt(Qs1)+parseInt(Qs2)+parseInt(Qs3);
		return Qs;

	}

	/** 展期日期校验 **/
	function checkDate(obj){
		if(obj.value&&IqpExtensionApp.start_date._getValue()&&IqpExtensionApp.end_date._getValue()){
	   		var select_Date=obj.value;
	   		var start_date = IqpExtensionApp.start_date._getValue();
	   		var end_date = IqpExtensionApp.end_date._getValue();
	   		var bill_no = IqpExtensionApp.bill_no._getValue();

	   		if(select_Date <= end_date){
	   			alert('展期日期应大于止贷日期!');
	   			obj.value = "";
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
					var term_type = jsonstr.term_type;
					var cont_term = jsonstr.cont_term;
					var extension_date = jsonstr.extension_date;
					var date_type = jsonstr.date_type;
					if( extension_date >= select_Date){
						var li_qs = getQs(start_date,select_Date);
						/*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
						if("100051"==prdId){
						}else{
							getBaseRate(li_qs);
						}
						/*modified by wangj 需求编号【XD141222087】法人透支改造 end */
					}else{
						if(date_type == 'short'){
							alert('原贷款期限是短期(1年以下，含1年),展期期限累计不能超过原贷款期限!');
						}else if(date_type == 'middle'){
							alert('原贷款期限是中长期(1年以上5年以下，含5年)，展期期限累计不能超过原贷款期限的一半!');
						}else if(date_type == 'long'){
							alert('原贷款期限是长期(5年以上)，展期期限累计不能超过3年!');
						}
						obj.value = '';
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
			var url="<emp:url action='CheckExtensionDate.do'/>&type=CheckExtensionDate&value="+bill_no;
			var postData = YAHOO.util.Connect.setForm();	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	};

	//取基准利率
	function getBaseRate(term){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var rate = jsonstr.rate;
				if(rate != ""){
					IqpExtensionApp.base_rate._setValue(rate);					
				}else {

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
		var currType = IqpExtensionApp.cur_type._getValue();  //币种
		var prdId = "9999"; //默认业务品种
		var termType = "002"; //默认期限类型为 月
		var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
		var url = '<emp:url action="getRate.do"/>'+param;
		var postData = YAHOO.util.Connect.setForm();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};

	function doAddIqpExtensionApp(){
		var form = document.getElementById("submitForm");
		if(IqpExtensionApp._checkAll()){
			IqpExtensionApp._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					if(flag == "success"){
						alert("保存成功！");
						var url = '<emp:url action="getIqpExtensionAppUpdatePage.do"/>?op=update&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert("保存失败！");
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

			var url = '<emp:url action="addIqpExtensionAppRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
		}else {
			return false;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryIqpExtensionAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpExtensionAppRecord.do" method="POST">
		
		<emp:gridLayout id="IqpExtensionAppGroup" title="原借据信息" maxColumn="2">
			<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="IqpExtensionApp.bill_no" label="原借据编号" required="true" url="queryBillNoPop.do?condition= and (manager_br_id='' or manager_br_id is null or manager_br_id='${context.organNo}' or manager_br_id in (select organno from s_org where suporganno='${context.organNo}')) and cont_no in (select cont_no from cus_manager where manager_id = '${context.currentUserId}' and is_main_manager = '1')&moduleId=extIqp&returnMethod=selCusId&flag=3" />
			<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="IqpExtensionApp.cont_no" label="原合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpExtensionApp.cus_id" label="客户码"  required="true" readonly="true" />
			<emp:text id="IqpExtensionApp.cus_name" label="客户名称" colSpan="2"	 readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="IqpExtensionApp.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpExtensionApp.loan_amt" label="贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpExtensionApp.loan_balance" label="贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpExtensionApp.rate" label="执行利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" colSpan="2"/>
			<emp:date id="IqpExtensionApp.start_date" label="起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionApp.end_date" label="止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionAppGroup" title="展期信息" maxColumn="2">
			<emp:text id="IqpExtensionApp.serno" label="业务编号" maxlength="40" required="false" hidden="true" colSpan="2" />
			<emp:text id="IqpExtensionApp.extension_amt" label="展期金额" maxlength="18" required="true" readonly="true" dataType="Currency" />
			<emp:date id="IqpExtensionApp.extension_date" label="展期到期日期" required="true"  onblur = "checkDate(this)" />
			<emp:text id="IqpExtensionApp.base_rate" label="基准利率(年)" maxlength="16" readonly="true" required="true" dataType="Rate" />
			<emp:text id="IqpExtensionApp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" />
			<emp:textarea id="IqpExtensionApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>		
		<emp:gridLayout id="IqpExtensionAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="IqpExtensionApp.manager_id_displayname" label="责任人" required="true" readonly="false" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpExtensionApp.manager_br_id_displayname" label="责任机构"  required="true" url="querySOrgPop.do?restrictUsed=false" 
			 returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpExtensionApp.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName"/>
			<emp:text id="IqpExtensionApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:date id="IqpExtensionApp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true" />	
			<emp:select id="IqpExtensionApp.approve_status" label="申请状态" required="true"
			dictname="WF_APP_STATUS" defvalue="000" readonly="true" />
			<emp:text id="IqpExtensionApp.manager_br_id" label="责任机构"  required="true" hidden="true"/>
			<emp:text id="IqpExtensionApp.manager_id" label="责任人" required="true" hidden="true"  />
			<emp:text id="IqpExtensionApp.input_id" label="登记人" required="true"  hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpExtensionApp.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true"  />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addIqpExtensionApp" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>