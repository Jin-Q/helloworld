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
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
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
		/*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
		if("100051"==prd_id){
			IqpExtensionAgrTmp.base_rate._obj._renderReadonly(true);
			IqpExtensionAgrTmp.extension_rate._obj._renderReadonly(true);
		}
		/*modified by wangj 需求编号【XD141222087】法人透支改造 end */
	};
	function reLoad(){
		var url = '<emp:url action="getIqpExtensionAgrTmpUpdatePage.do"/>?menuIdTab=iqp_extension_agr&serno=${context.IqpExtensionAgrTmp.serno}&op=update&restrictUsed=false';
		url = EMPTools.encodeURI(url);
		window.location = url;
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
					IqpExtensionAgrTmp.base_rate._setValue(rate);					
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
		var currType = IqpExtensionAgrTmp.fount_cur_type._getValue();  //币种
		var prdId = "9999"; //默认业务品种
		var termType = "002"; //默认期限类型为 月
		var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
		var url = '<emp:url action="getRate.do"/>'+param;
		var postData = YAHOO.util.Connect.setForm();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
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
						/*modified by wangj 需求编号【XD141222087】法人透支改造 begin */
						var prd_id = IqpExtensionAgrTmp.prd_id._getValue();
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

	//保存
	function doSave(){
		var form = document.getElementById("submitForm");
		var result = IqpExtensionAgrTmp._checkAll();
		if(!result){
			alert('还有信息未保存！');
			return;
		}
		IqpExtensionAgrTmp._toForm(form);
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
					alert("保存成功!");
					window.location.reload();
				}else {
					alert("保存失败!");
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
		var url = '<emp:url action="updateBizModifyApp4IEARecord.do"/>?modify_rel_serno=${context.PvpBizModifyRel.modify_rel_serno}';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};
	
	function doReturn(){
		var url = '<emp:url action="queryBizModifyAppList.do"/>?';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="展期协议信息" id="main_tabs">
	<emp:form id="submitForm" action="#" method="POST">
		<emp:gridLayout id="PvpBizModifyRelGroup" maxColumn="2" title="打回业务修改申请信息">
	  		<emp:text id="PvpBizModifyRel.modify_rel_serno" label="打回业务修改申请流水号" required="false" readonly="true" />
	  		<emp:select id="PvpBizModifyRel.biz_cate" label="业务申请类型" dictname="ZB_BIZ_CATE" required="false" readonly="true" />
	    </emp:gridLayout>
		<emp:gridLayout id="IqpExtensionAgrTmpGroup" title="原借据信息" maxColumn="2">
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
		<emp:gridLayout id="IqpExtensionAgrTmpGroup" title="展期协议信息" maxColumn="2">
			<emp:text id="IqpExtensionAgrTmp.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpExtensionAgrTmp.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpExtensionAgrTmp.cn_cont_no" label="中文合同编号" maxlength="40" required="true" />
			<emp:date id="IqpExtensionAgrTmp.sign_date" label="签订日期" required="false" hidden="true" />
			<emp:text id="IqpExtensionAgrTmp.extension_amt" label="展期金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpExtensionAgrTmp.extension_date" label="展期到期日期" required="true" onblur="checkDate(this)"/>
			<emp:text id="IqpExtensionAgrTmp.base_rate" label="基准利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpExtensionAgrTmp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" />
			<emp:textarea id="IqpExtensionAgrTmp.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true" />
			<emp:text id="IqpExtensionAgrTmp.prd_id" label="业务类型" hidden="true"  readonly="true" />
			<emp:text id="IqpExtensionAgrTmp.modify_rel_serno" label="打回业务修改申请流水号" hidden="true" readonly="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="save" label="保存" />
			<emp:button id="return" label="返回列表页面" />
		</div>
	</emp:form>
	</emp:tab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
