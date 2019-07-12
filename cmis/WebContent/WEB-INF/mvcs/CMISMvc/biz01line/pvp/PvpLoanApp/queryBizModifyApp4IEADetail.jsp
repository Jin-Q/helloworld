<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String op = "";
    String wf_flag= "";
    if(context.containsKey("op")){
    	op = (String)context.getDataValue("op");
    }
    if(context.containsKey("wf_flag")){
    	wf_flag = (String)context.getDataValue("wf_flag");
    }
    
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<script type="text/javascript">	
	function doOnLoad(){ 
		IqpExtensionAgrTmp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpExtensionAgrTmp.fount_bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
		
		var prd_id = IqpExtensionAgrTmp.prd_id._getValue();
		if(prd_id < '2' ){	//贷款类
			IqpExtensionAgrTmp.base_rate._obj._renderHidden(false);
			IqpExtensionAgrTmp.base_rate._obj._renderRequired(true);
		}else{
			IqpExtensionAgrTmp.base_rate._obj._renderHidden(true);
			IqpExtensionAgrTmp.base_rate._obj._renderRequired(false);
		}
	};

	function getCusForm(){
		var cus_id = IqpExtensionAgrTmp.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
	    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function viewAccInfo(){
		var accNo = IqpExtensionAgrTmp.fount_bill_no._getValue();
		if(accNo==null||accNo==''){
			alert('借据编号为空！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+accNo;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};

	/** 展期日期校验 **/
	function checkDate(obj){
		if(obj.value&&IqpExtensionAgrTmp.fount_start_date._getValue()&&IqpExtensionAgrTmp.fount_end_date._getValue()){
	   		var select_Date=obj.value;
	   		var start_date = IqpExtensionAgrTmp.fount_start_date._getValue();
	   		var end_date = IqpExtensionAgrTmp.fount_end_date._getValue();
	   		var bill_no = IqpExtensionAgrTmp.fount_bill_no._getValue();

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
						getBaseRate(li_qs);
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

	function doReturn(){
		var op='<%=op%>';
		if(op!="his"){
			var url = '<emp:url action="queryBizModifyAppList.do"/>?';
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			var url = '<emp:url action="queryBizModifyAppHisList.do"/>?';
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	};
</script>
<jsp:include page="/include.jsp" flush="true"/>
</head>
<body class="page_content" onload="doOnLoad()">
<emp:form id="submitForm" action="#" method="POST">
	<emp:tabGroup mainTab="main_tabs" id="mainTab">
	<emp:tab label="展期信息" id="main_tabs" needFlush="true" initial="true">
		<emp:gridLayout id="" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionAgrTmp.fount_bill_no" label="原借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgrTmp.fount_cont_no" label="原合同编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpExtensionAgrTmp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionAgrTmp.cus_id_displayname" label="客户名称" colSpan="2" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpExtensionAgrTmp.fount_cur_type" label="原币种" required="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" readonly="true" />
			<emp:text id="IqpExtensionAgrTmp.fount_rate" label="原执行利率(年)" maxlength="16" required="true" dataType="Rate" colSpan="2" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpExtensionAgrTmp.fount_loan_amt" label="原贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpExtensionAgrTmp.fount_loan_balance" label="原贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpExtensionAgrTmp.fount_start_date" label="原起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionAgrTmp.fount_end_date" label="原止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="" title="展期协议信息" maxColumn="2">
			<emp:text id="IqpExtensionAgrTmp.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpExtensionAgrTmp.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpExtensionAgrTmp.cn_cont_no" label="中文合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:date id="IqpExtensionAgrTmp.sign_date" label="签订日期" required="false" hidden="true" />
			<emp:text id="IqpExtensionAgrTmp.extension_amt" label="展期金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpExtensionAgrTmp.extension_date" label="展期到期日期" required="true" onblur="checkDate(this)" readonly="true"/>
			<emp:text id="IqpExtensionAgrTmp.base_rate" label="基准利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpExtensionAgrTmp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="IqpExtensionAgrTmp.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true" />
			<emp:text id="IqpExtensionAgrTmp.prd_id" label="业务品种" hidden="true"  readonly="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<%if(!"1".equals(wf_flag)){ %>
			<emp:button id="return" label="返回列表页面" />
			<%} %>
		</div>
	</emp:tab>
	<%if("1".equals(wf_flag)){ %>
			<emp:tab id="CompareHis" label="与上一次修改值对比" url="getModifyHisViewPage.do" reqParams="cont_no=${context.IqpExtensionAgrTmp.agr_no}&prd_id=${context.IqpExtensionAgrTmp.prd_id}&modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}&op=his4iea&wf_flag=${context.wf_flag}"/>
	<%} %>
	</emp:tabGroup>
	</emp:form>
</body>
</html>
</emp:page>
