<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
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

	};
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
						var prd_id = IqpExtensionApp.prd_id._getValue();
						if("100051"==prd_id){
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
	function doSubmits(){
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
		            alert('保存成功!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = IqpExtensionApp._checkAll();
		if(result){
			IqpExtensionApp._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryIqpExtensionAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
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
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					IqpExtensionApp.manager_br_id._setValue(jsonstr.org);
					IqpExtensionApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
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
	function doLoad(){ 
		IqpExtensionApp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpExtensionApp.bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
		document.getElementById("main_tabs").href="javascript:reLoad();";

		var prd_id = IqpExtensionApp.prd_id._getValue();
		if(prd_id < '2' ){	//贷款类
			IqpExtensionApp.base_rate._obj._renderHidden(false);
			IqpExtensionApp.base_rate._obj._renderRequired(true);
		}else{
			IqpExtensionApp.base_rate._obj._renderHidden(true);
			IqpExtensionApp.base_rate._obj._renderRequired(false);
		}
		/*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
		if("100051"==prd_id){
			IqpExtensionApp.base_rate._obj._renderReadonly(true);
			IqpExtensionApp.extension_rate._obj._renderReadonly(true);
		}
		/*modified by wangj 需求编号【XD141222087】法人透支改造 end */
	};
	function reLoad(){
		var url = '<emp:url action="getIqpExtensionAppUpdatePage.do"/>?menuIdTab=iqp_extension_app&serno=${context.IqpExtensionApp.serno}&restrictUsed=false&op=update';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function getCusForm(){
		var cus_id = IqpExtensionApp.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
	    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function viewAccInfo(){
		var accNo = IqpExtensionApp.bill_no._getValue();
		if(accNo==null||accNo==''){
			alert('借据编号为空！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+accNo;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="展期申请信息" id="main_tabs">
	<emp:form id="submitForm" action="updateIqpExtensionAppRecord.do" method="POST">
		<emp:gridLayout id="IqpExtensionAppGroup" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionApp.bill_no" label="原借据编号" required="true"  readonly="true" />
			<emp:text id="IqpExtensionApp.cont_no" label="原合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpExtensionApp.cus_id" label="客户码"  required="true" readonly="true" />
			<emp:text id="IqpExtensionApp.cus_id_displayname" label="客户名称" colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="IqpExtensionApp.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="IqpExtensionApp.loan_amt" label="贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpExtensionApp.loan_balance" label="贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="IqpExtensionApp.rate" label="执行利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" colSpan="2"/>
			<emp:date id="IqpExtensionApp.start_date" label="起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionApp.end_date" label="止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionAppGroup" title="展期信息" maxColumn="2">
			<emp:text id="IqpExtensionApp.serno" label="业务编号" maxlength="40" required="true" readonly="true"  colSpan="2" />
			<emp:text id="IqpExtensionApp.extension_amt" label="展期金额" maxlength="18" required="true" readonly="true" dataType="Currency" />
			<emp:date id="IqpExtensionApp.extension_date" label="展期到期日期" required="true"  onblur = "checkDate(this)" />
			<emp:text id="IqpExtensionApp.base_rate" label="基准利率(年)" maxlength="16" readonly="true" required="true" dataType="Rate" />
			<emp:text id="IqpExtensionApp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" />
			<emp:textarea id="IqpExtensionApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>		
		<emp:gridLayout id="IqpExtensionAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="IqpExtensionApp.manager_id_displayname" label="责任人" required="true" readonly="false" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			 <!-- modified by lisj 2015-10-15 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin -->
			<emp:pop id="IqpExtensionApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?manager_id=${context.IqpExtensionApp.manager_id}&restrictUsed=false" 
			 returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			 <!-- modified by lisj 2015-10-15 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end -->
			<emp:text id="IqpExtensionApp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="IqpExtensionApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:date id="IqpExtensionApp.input_date" label="登记日期" required="true"  readonly="true" />	
			<emp:select id="IqpExtensionApp.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS"  readonly="true" />
			<emp:text id="IqpExtensionApp.manager_br_id" label="责任机构"  required="true" hidden="true"/>
			<emp:text id="IqpExtensionApp.manager_id" label="责任人" required="true" hidden="true"  />
			<emp:text id="IqpExtensionApp.input_id" label="登记人" required="true"  hidden="true" />
			<emp:text id="IqpExtensionApp.input_br_id" label="登记机构" required="true"  hidden="true"  />
			<emp:text id="IqpExtensionApp.prd_id" label="业务类型" hidden="true"  readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="修改" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:tab label="原合同信息" id="subTab" 
	url="getCtrLoanContViewPage.do?cont_no=${context.IqpExtensionApp.cont_no}&menuIdTab=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have"
	 initial="false" needFlush="true"/> 
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
