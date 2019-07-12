<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>自动额度页面</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = context.getDataValue("op").toString();
	if("view".equalsIgnoreCase(op)){
		request.setAttribute("canwrite","");
	}
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	//返回共同债务人信息
	function returnDebt(data){
		var debId = data.cus_id._getValue();
		var cusId = "${context.cus_id}";
		if(cusId==debId){
			alert('共同债务人与申请人不能相同，请重新选择！');
			return;
		}
		LmtAppIndiv.same_debtor_id_displayname._setValue(data.cus_name._getValue());
		LmtAppIndiv.same_debtor_id._setValue(debId);
	}
	
	//控制共同债务人
	function checkSameDeb(){
		var isSameDeb = LmtAppIndiv.is_same_debtor._getValue();
		if(isSameDeb=='1'){
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(true);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(false);
		}else{
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(false);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(true);
			LmtAppIndiv.same_debtor_id_displayname._setValue('');
			LmtAppIndiv.same_debtor_id._setValue('');
		}
	}

	//是否开通POS支付
	function checkOpenPos(){
		var openPos = LmtAppIndiv.is_open_pos._getValue();
		if("1"==openPos){  //开通POS
			LmtAppIndiv.pos_pay_type._obj._renderHidden(false);
			LmtAppIndiv.limit_regi_id._obj._renderHidden(false);
			LmtAppIndiv.limit_regi_name._obj._renderHidden(false);
		}else{   //不开通 
			LmtAppIndiv.pos_pay_type._obj._renderHidden(true);
			LmtAppIndiv.limit_regi_id._obj._renderHidden(true);
			LmtAppIndiv.limit_regi_name._obj._renderHidden(true);
		}
	}

	//是否开通自助循环
	function checkSelfRevolv(){
		var selfRevolv = LmtAppIndiv.is_self_revolv._getValue();
		if("1"==selfRevolv){   //开通自助
			//LmtAppIndiv.org_self_amt._obj._renderHidden(false);  //原自助金额(元)
			LmtAppIndiv.self_amt._obj._renderHidden(false);  //自助金额(元)
			LmtAppIndiv.self_term._obj._renderHidden(false);  //自助期限(月)
			
			LmtAppIndiv.ir_float_rate_self._obj._renderHidden(false);  //利率浮动比(自助)
			LmtAppIndiv.overdue_rate._obj._renderHidden(false);  //逾期利率浮动比例
			LmtAppIndiv.ir_adjust_type_self._obj._renderHidden(false);  //利率调整方式(自助)
			LmtAppIndiv.repay_type_self_displayname._obj._renderHidden(false);  //贷款还款方式(自助)

			LmtAppIndiv.self_rate_y._obj._renderHidden(false);   //基准年利率
			LmtAppIndiv.self_rate_m._obj._renderHidden(false);   //基准月利率
			
			LmtAppIndiv.guar_type._obj._renderHidden(false);  //担保方式
			LmtAppIndiv.guar_type_detail._obj._renderHidden(false);  //担保方式细分
			LmtAppIndiv.five_class._obj._renderHidden(false);  //五级分类
			
			LmtAppIndiv.is_open_pos._obj._renderHidden(false); //是否开通POS
			LmtAppIndiv.is_same_debtor._obj._renderHidden(false);//是否有共同债务人

			LmtAppIndiv.self_amt._obj._renderRequired(true);
			LmtAppIndiv.self_term._obj._renderRequired(true);
			LmtAppIndiv.guar_type._obj._renderRequired(true);
			LmtAppIndiv.guar_type_detail._obj._renderRequired(true);
		}else{
			//LmtAppIndiv.org_self_amt._obj._renderHidden(true);  //原自助金额(元)
			LmtAppIndiv.self_amt._obj._renderHidden(true);  //自助金额(元)
			LmtAppIndiv.self_term._obj._renderHidden(true);  //自助期限(月)
			
			LmtAppIndiv.ir_float_rate_self._obj._renderHidden(true);  //利率浮动比(自助)
			LmtAppIndiv.overdue_rate._obj._renderHidden(true);  //逾期利率浮动比例
			LmtAppIndiv.ir_adjust_type_self._obj._renderHidden(true);  //利率调整方式(自助)
			LmtAppIndiv.repay_type_self_displayname._obj._renderHidden(true);  //贷款还款方式(自助)

			LmtAppIndiv.self_rate_y._obj._renderHidden(true);   //基准年利率
			LmtAppIndiv.self_rate_m._obj._renderHidden(true);   //基准月利率
			
			LmtAppIndiv.guar_type._obj._renderHidden(true);  //担保方式
			LmtAppIndiv.guar_type_detail._obj._renderHidden(true);  //担保方式细分
			LmtAppIndiv.five_class._obj._renderHidden(true);  //担保方式细分
			
			LmtAppIndiv.is_open_pos._obj._renderHidden(true);  //是否开通POS
			LmtAppIndiv.is_open_pos._setValue("2"); //设置开通POS业务为否
			
			LmtAppIndiv.is_same_debtor._obj._renderHidden(true);  //是否有共同债务人
			LmtAppIndiv.is_same_debtor._setValue("2"); //设置是否有共同债务人为否
			LmtAppIndiv.self_amt._obj._renderRequired(false);
			LmtAppIndiv.self_term._obj._renderRequired(false);
			LmtAppIndiv.guar_type._obj._renderRequired(false);
			LmtAppIndiv.guar_type_detail._obj._renderRequired(false);

			LmtAppIndiv.org_self_amt._obj._renderHidden(true);  //原自助金额(元)
			LmtAppIndiv.is_adj_term_self._obj._renderHidden(true);  //是否调整期限
			LmtAppIndiv.self_start_date._obj._renderHidden(true);  //起始日期
			LmtAppIndiv.self_end_date._obj._renderHidden(true);  //到期日期
		}
		checkOpenPos();
		checkSameDeb();
	}

	//检查共同债务人是否合规
	function checkCusDeb(){
		var cusId = "${context.cus_id}";
		var debId = LmtAppIndiv.same_debtor_id._getValue();
		if(cusId!=null&&cusId!=''&&debId!=null&&debId!=''){
			if(cusId==debId){
				alert('共同债务人与申请人不能相同，请重新选择！');
				LmtAppIndiv.same_debtor_id_displayname._setValue('');
				LmtAppIndiv.same_debtor_id._setValue('');
				return;
			}
		}
	}

	//设置产品返回 
	function setProds(data){
		LmtAppIndiv.prd_id._setValue(data[0]);
		LmtAppIndiv.prd_id_displayname._setValue(data[1]);
	}


	//主担保方式下拉框值改变事件
	function guarTypeChange(){
		var assureMainValue =LmtAppIndiv.guar_type._getValue();
		var assmainDtsoptions = LmtAppIndiv.guar_type_detail._obj.element.options;
		var a = "0";
		var b = "0";
		var c = "0";
		for(var i=assmainDtsoptions.length-1;i>=0;i--){	
			if(assmainDtsoptions[i].value=="1"){//普通抵押
				a = "1";
			}
			if(assmainDtsoptions[i].value=="8"){//保证
				b = "1";
			}
			if(assmainDtsoptions[i].value=="9"){//信用
				c = "1";
			}
		}
		if(a == "0"){
			var varOption = new Option('普通抵押','1');
			assmainDtsoptions.add(varOption);
		}
		if(b == "0"){
			var varOption = new Option('保证','8');
			assmainDtsoptions.add(varOption);
		}
		if(c == "0"){
			var varOption = new Option('信用','9');
			assmainDtsoptions.add(varOption);
		}
		if(assureMainValue == ""){
			LmtAppIndiv.guar_type_detail._obj._renderReadonly(false);
			LmtAppIndiv.guar_type_detail._setValue("");
		}else if(assureMainValue =="100"){//主担保方式为抵押时，担保方式细分自动赋值为抵押
			LmtAppIndiv.guar_type_detail._setValue("1");
		    LmtAppIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue =="300"){//保证
			LmtAppIndiv.guar_type_detail._setValue("8");
		    LmtAppIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue =="400"){//信用
			LmtAppIndiv.guar_type_detail._setValue("9");
		    LmtAppIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue =="500" || assureMainValue =="510"){//100%保证金  准全额保证金
			LmtAppIndiv.guar_type_detail._setValue("2");
			 LmtAppIndiv.guar_type_detail._obj._renderReadonly(true);
		}else if(assureMainValue.substring(0,1) == "2"){
			LmtAppIndiv.guar_type_detail._obj._renderReadonly(false);
			LmtAppIndiv.guar_type_detail._setValue("");
			var assmainDtsoptions = LmtAppIndiv.guar_type_detail._obj.element.options;
			for(var i=assmainDtsoptions.length-1;i>=0;i--){	
				if(assmainDtsoptions[i].value=="1" || assmainDtsoptions[i].value=="8" ||assmainDtsoptions[i].value=="9"){
					assmainDtsoptions.remove(i);
				}
			}
		}
	};
	
	function doOnload(){
		LmtAppIndiv.limit_regi_id._obj.addOneButton('onlineSearch','获取',onlineSearch);  //额度注册账号增加联机获取功能按钮

		//控制共同债务人隐藏
		var isSameDeb = LmtAppIndiv.is_same_debtor._getValue();
		if(isSameDeb=='2'){
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(false);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(true);
		}else if(isSameDeb=='1'){
			LmtAppIndiv.same_debtor_id_displayname._obj._renderRequired(true);
			LmtAppIndiv.same_debtor_id_displayname._obj._renderHidden(false);
		}
		getRate();
		checkOrgOrNot();  //控制变更时字段的显示
		checkSelfRevolv();  //是否开通自动额度

		//清除担保方式中的全额保证金、准全额保证金 
		var options = LmtAppIndiv.guar_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "500" || options[i].value == "510"){
				options.remove(i);
			}
		}

		//根据月利率设置年利率的值
		var self_rate_m = LmtAppIndiv.self_rate_m._getValue();
		if(null!=self_rate_m && ""!=self_rate_m){ 
			LmtAppIndiv.self_rate_y._setValue(Math.round(self_rate_m*1.2*100000)/100000);   //年利率
		}

		//变更时隐藏自助期限
		var app_type = '${context.app_type}';
		if("02"==app_type){
			LmtAppIndiv.self_term._obj._renderHidden(true);
			LmtAppIndiv.self_term._obj._renderRequired(false);
			//LmtAppIndiv.self_term._setValue(0);
		}

	}

	//控制变更时字段的显示
	function checkOrgOrNot(){
		var app_type = '${context.LmtAppIndiv.app_type}';
		if("01"==app_type){    //如果是授信变更，显示原有额度情况   
			LmtAppIndiv.self_start_date._obj._renderHidden(true);
			LmtAppIndiv.self_end_date._obj._renderHidden(true);
			LmtAppIndiv.org_self_amt._obj._renderHidden(true);
			LmtAppIndiv.is_adj_term_self._obj._renderHidden(true);
		}else{
			LmtAppIndiv.is_adj_term_self._obj._renderRequired(true);
			LmtAppIndiv.self_term._obj._renderHidden(true);
		}
	}
	
	//新增记录
	function doAddLmtAppIndiv(){
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
		            alert('保存成功！');
		           // var serno = jsonstr.serno;
		            //var url = '<emp:url action="getLmtAppIndivUpdatePage.do"/>?serno='+serno+'&op=update';
					//url = EMPTools.encodeURI(url);
					//window.location = url;
				}else{
					alert(jsonstr.msg);
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

		//不开通自助额度时 将自助额度下的信息都清理
		var selfRevolv = LmtAppIndiv.is_self_revolv._getValue();
		if("2"==selfRevolv){   //不开通自助
			LmtAppIndiv.self_amt._setValue(0.00+"");  //自助金额(元)
			LmtAppIndiv.self_term._setValue(0);  //自助期限(月)
			
			LmtAppIndiv.ir_float_rate_self._setValue(0);  //利率浮动比(自助)
			LmtAppIndiv.overdue_rate._setValue(0);  //逾期利率浮动比例
			LmtAppIndiv.ir_adjust_type_self._setValue();  //利率调整方式(自助)
			LmtAppIndiv.repay_type_self._setValue("");  //贷款还款方式(自助)
			LmtAppIndiv.repay_type_self_displayname._setValue("");  //贷款还款方式(自助)

			LmtAppIndiv.self_rate_y._setValue(0);  //基准年利率
			LmtAppIndiv.self_rate_m._setValue(0);  //基准月利率
			
			LmtAppIndiv.guar_type._setValue();  //担保方式 
			LmtAppIndiv.guar_type_detail._setValue();   //担保方式细分
			LmtAppIndiv.five_class._setValue();   //五级分类
			
			LmtAppIndiv.is_open_pos._setValue("2");  //是否开通POS
			LmtAppIndiv.is_same_debtor._setValue("2"); //是否有共同债务人

			LmtAppIndiv.pos_pay_type._setValue();    
			LmtAppIndiv.limit_regi_id._setValue("");    //额度注册账户
			LmtAppIndiv.limit_regi_name._setValue("");   //额度注册户名

			LmtAppIndiv.self_amt._obj._renderRequired(false);
			LmtAppIndiv.self_term._obj._renderRequired(false);
			LmtAppIndiv.guar_type._obj._renderRequired(false);
			LmtAppIndiv.guar_type_detail._obj._renderRequired(false);
		}
		var result = LmtAppIndiv._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	}

	//是否调整授信期限
	function showSelfTerm(){
		var isAdjTermSelf = LmtAppIndiv.is_adj_term_self._getValue();
		if(isAdjTermSelf=='1'){
			LmtAppIndiv.self_term._obj._renderHidden(false);
			LmtAppIndiv.self_term._obj._renderRequired(true);
		}else{
			LmtAppIndiv.self_term._obj._renderHidden(true);
			LmtAppIndiv.self_term._obj._renderRequired(false);
		}
	}

	//联机查询功能
	function onlineSearch(){
		
	}

	//选择还款方式
	function getRepayType(data){
		var repay_mode_id = data.repay_mode_id._getValue();
		LmtAppIndiv.repay_type_self._setValue(repay_mode_id);
		LmtAppIndiv.repay_type_self_displayname._setValue(data.repay_mode_dec._getValue());
	}

	//-----------------------通过异步取利率------------------------------------
	function getRate(){
		var currType = "CNY";  //币种
		var prdId = "9999"; //默认业务品种
		var termType = "002"; //默认期限类型为 月
		var term = LmtAppIndiv.self_term._getValue();//期限
		if(null!=term && term != ""){
			var param = "&prdId="+prdId+"&currType="+currType+"&termType="+termType+"&term="+term;
			var url = '<emp:url action="getRate.do"/>'+param;
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
					var rate = jsonstr.rate;
					if(flag == "success"){
						LmtAppIndiv.self_rate_y._setValue(Math.round(rate*1000*1.2)/1000000);   //年利率
						LmtAppIndiv.self_rate_m._setValue(Math.round(rate*1000)/1000000);   //月利率
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}else{
			LmtAppIndiv.self_rate_y._setValue(0);
			LmtAppIndiv.self_rate_m._setValue(0);
		}
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()" >
	<emp:form id="submitForm" action="updateLmtAppIndivRecord.do?self=self" method="POST">
		<emp:gridLayout id="LmtAppIndivGroup" title="自助额度" maxColumn="2">
			<emp:select id="LmtAppIndiv.is_self_revolv" label="是否开通自助循环" required="true" dictname="STD_ZX_YES_NO" onchange="checkSelfRevolv()" defvalue="2" colSpan="2"/>
			<emp:text id="LmtAppIndiv.org_self_amt" label="原自助金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtAppIndiv.self_amt" label="自助金额" maxlength="18" dataType="Currency" />
			<emp:date id="LmtAppIndiv.self_start_date" label="授信起始日" readonly="true" />
			<emp:date id="LmtAppIndiv.self_end_date" label="授信到期日" readonly="true"/>
			<emp:select id="LmtAppIndiv.is_adj_term_self" label="是否调整期限" required="false" dictname="STD_ZX_YES_NO" onchange="showSelfTerm()" defvalue="2"/>
			<emp:text id="LmtAppIndiv.self_term" label="自助期限(月)" maxlength="3" dataType="Int" onblur="getRate()"/>
			<emp:text id="LmtAppIndiv.self_rate_y" label="基准利率(年)" dataType="Rate4Month" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.self_rate_m" label="基准利率(月)" dataType="Rate4Month" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAppIndiv.ir_float_rate_self" label="利率浮动比(自助)" maxlength="16" required="false" dataType="Percent"/>
			<emp:text id="LmtAppIndiv.overdue_rate" label="逾期利率浮动比例" maxlength="16" required="false" dataType="Percent" />
			<emp:select id="LmtAppIndiv.ir_adjust_type_self" label="利率调整方式(自助)" required="false" dictname="STD_IR_ADJUST_TYPE" />
			
			<emp:pop id="LmtAppIndiv.repay_type_self_displayname" label="还款方式" url="queryPrdRepayModeList.do?prd_id=none&returnMethod=getRepayType" required="false"  buttonLabel="选择" />
			
			<emp:select id="LmtAppIndiv.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" onchange="guarTypeChange()"/>
			<emp:select id="LmtAppIndiv.guar_type_detail" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE"/>
			<emp:select id="LmtAppIndiv.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" colSpan="2"/>
			
			<emp:select id="LmtAppIndiv.is_same_debtor" label="是否有共同债务人" required="true" dictname="STD_ZX_YES_NO" onchange="checkSameDeb()" defvalue="2"/>
			<emp:pop id="LmtAppIndiv.same_debtor_id_displayname" label="共同债务人" url="queryAllCusPop.do?cusTypCondition=belg_line='BL300' and cus_status='20'&returnMethod=returnDebt" />
			
			<emp:select id="LmtAppIndiv.is_open_pos" label="是否开通POS支付" required="false" dictname="STD_ZX_YES_NO" defvalue="2" onchange="checkOpenPos()"/>
			<emp:select id="LmtAppIndiv.pos_pay_type" label="POS机支付方式" required="false" dictname="STD_ZB_POS_PAY_TYPE" />
			<emp:text id="LmtAppIndiv.limit_regi_id" label="额度注册账号" maxlength="40" required="false" />
			<emp:text id="LmtAppIndiv.limit_regi_name" label="额度注册账户名" maxlength="80" required="false" readonly="true"/>
			
			<emp:text id="LmtAppIndiv.same_debtor_id" label="共同债务人" maxlength="30" required="false" hidden="true" />
			<emp:text id="LmtAppIndiv.repay_type_self" label="贷款还款方式(自助)" maxlength="5" required="false" hidden="true"/>
			<emp:text id="LmtAppIndiv.serno" label="业务流水号" required="false" hidden="true"/>
			
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:actButton id="addLmtAppIndiv" label="保存" op="add"/>
			<emp:actButton id="reset" label="重置" op="add"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

