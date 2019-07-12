<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doLoad(){
		dealAmtArea();
		setContValue();
		var options = PspTaskCheckSet.task_freq_unit1._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "05" || options[i].value == "06" || options[i].value == "07"){
				options.remove(i);
		
			}
		}
		var options = PspTaskCheckSet.task_freq_unit2._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "05" || options[i].value == "06" || options[i].value == "07"){
				options.remove(i);
		
			}
		}
		var options = PspTaskCheckSet.task_freq_unit3._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "01" || options[i].value == "05" || options[i].value == "06" || options[i].value == "07"){
				options.remove(i);
		
			}
		}
		var check_mode=PspTaskCheckSet.check_mode._getValue();
		if("00"==check_mode){//首次
			PspTaskCheckSet.risk_type._obj._renderRequired(false);
			PspTaskCheckSet.cdt_eval._obj._renderRequired(false);
			PspTaskCheckSet.trade_ter._obj._renderRequired(false);
			PspTaskCheckSet.task_freq._obj._renderRequired(false);
			PspTaskCheckSet.task_freq_unit._obj._renderRequired(false);
			PspTaskCheckSet.is_special._obj._renderRequired(false);
			PspTaskCheckSet.risk_type._obj._renderHidden(true);
			PspTaskCheckSet.cdt_eval._obj._renderHidden(true);
			PspTaskCheckSet.trade_ter._obj._renderHidden(true);
			PspTaskCheckSet.task_freq._obj._renderHidden(true);
			PspTaskCheckSet.task_freq_unit._obj._renderHidden(true);
			PspTaskCheckSet.is_special._obj._renderHidden(true);
			PspTaskCheckSet.cus_id._setValue("");
			PspTaskCheckSet.cus_id_displayname._setValue("");
			PspTaskCheckSet.cus_id._obj._renderRequired(false);
			PspTaskCheckSet.cus_id_displayname._obj._renderRequired(false);
			PspTaskCheckSet.cus_id._obj._renderHidden(true);
			PspTaskCheckSet.cus_id_displayname._obj._renderHidden(true);
			PspTaskCheckSet.memo._setValue("");
			PspTaskCheckSet.memo._obj._renderRequired(false);
			PspTaskCheckSet.memo._obj._renderHidden(true);
			PspTaskCheckSet.task_status._obj._renderHidden(true);

			PspTaskCheckSet.prd_id._setValue("");
			PspTaskCheckSet.prd_id._obj._renderRequired(false);
			PspTaskCheckSet.prd_id._obj._renderHidden(true);
			PspTaskCheckSet.prd_id_displayname._setValue("");
			PspTaskCheckSet.prd_id_displayname._obj._renderRequired(false);
			PspTaskCheckSet.prd_id_displayname._obj._renderHidden(true);
			PspTaskCheckSet.amt_area._setValue("");
			PspTaskCheckSet.amt_area._obj._renderRequired(false);
			PspTaskCheckSet.amt_area._obj._renderHidden(true);
			PspTaskCheckSet.loan_bal._setValue("");
			PspTaskCheckSet.loan_bal._obj._renderRequired(false);
			PspTaskCheckSet.loan_bal._obj._renderHidden(true);
		}else if("01"==check_mode){//常规
			PspTaskCheckSet.risk_type._obj._renderRequired(false);
			PspTaskCheckSet.cdt_eval._obj._renderRequired(false);
			//PspTaskCheckSet.risk_type._obj._renderRequired(true);
			//PspTaskCheckSet.cdt_eval._obj._renderRequired(true);
			PspTaskCheckSet.is_special._obj._renderRequired(true);
			PspTaskCheckSet.cus_type._obj._renderRequired(false);
			PspTaskCheckSet.finish_term._obj._renderRequired(false);
			PspTaskCheckSet.risk_type._obj._renderHidden(false);
			PspTaskCheckSet.cdt_eval._obj._renderHidden(false);
			PspTaskCheckSet.trade_ter._obj._renderHidden(false);
			PspTaskCheckSet.finish_term._obj._renderHidden(true);
			PspTaskCheckSet.cus_type._obj._renderHidden(true);
			PspTaskCheckSet.is_special._obj._renderHidden(false);
			var is_special = PspTaskCheckSet.is_special._getValue();
			if("1"==is_special){
				PspTaskCheckSet.cus_id._obj._renderHidden(false);
				PspTaskCheckSet.cus_id_displayname._obj._renderHidden(false);
				PspTaskCheckSet.risk_type._obj._renderHidden(true);
				PspTaskCheckSet.cdt_eval._obj._renderHidden(true); 
				PspTaskCheckSet.trade_ter._obj._renderHidden(true);
				PspTaskCheckSet.cus_id._obj._renderRequired(true);
				PspTaskCheckSet.cus_id_displayname._obj._renderRequired(true);
				PspTaskCheckSet.risk_type._obj._renderRequired(false);
				PspTaskCheckSet.cdt_eval._obj._renderRequired(false); 
				PspTaskCheckSet.trade_ter._obj._renderRequired(false);
				PspTaskCheckSet.cus_type._setValue("");
				PspTaskCheckSet.cus_type._obj._renderHidden(true);
				PspTaskCheckSet.cus_type._obj._renderRequired(false);

				PspTaskCheckSet.task_freq_unit1._setValue("");
				PspTaskCheckSet.task_freq_unit1._obj._renderRequired(false);
				PspTaskCheckSet.task_freq_unit1._obj._renderHidden(true);
				PspTaskCheckSet.task_freq_unit2._setValue(PspTaskCheckSet.task_freq_unit._getValue());
				PspTaskCheckSet.task_freq_unit2._obj._renderRequired(true);
				PspTaskCheckSet.task_freq_unit2._obj._renderHidden(false);
				PspTaskCheckSet.task_freq._obj._renderRequired(false);
				PspTaskCheckSet.task_freq._obj._renderHidden(true);
				PspTaskCheckSet.task_freq_unit3._setValue("");
				PspTaskCheckSet.task_freq_unit3._obj._renderRequired(false);
				PspTaskCheckSet.task_freq_unit3._obj._renderHidden(true);
				PspTaskCheckSet.prd_id._setValue("");
				PspTaskCheckSet.prd_id_displayname._setValue("");
				PspTaskCheckSet.amt_area._setValue("");
				PspTaskCheckSet.loan_bal._setValue("");
				PspTaskCheckSet.prd_id._obj._renderHidden(true);
				PspTaskCheckSet.prd_id._obj._renderRequired(false);
				PspTaskCheckSet.prd_id_displayname._obj._renderHidden(true);
				PspTaskCheckSet.prd_id_displayname._obj._renderRequired(false);
				PspTaskCheckSet.amt_area._obj._renderHidden(true);
				PspTaskCheckSet.amt_area._obj._renderRequired(false);
				PspTaskCheckSet.loan_bal._obj._renderHidden(true);
				PspTaskCheckSet.loan_bal._obj._renderRequired(false);
				PspTaskCheckSet.finish_term._obj._renderRequired(true);
				PspTaskCheckSet.finish_term._obj._renderHidden(false);
			}else{
				PspTaskCheckSet.finish_term._setValue("");
				PspTaskCheckSet.finish_term._obj._renderRequired(false);
				PspTaskCheckSet.finish_term._obj._renderHidden(true);
				PspTaskCheckSet.cus_id._obj._renderHidden(true);
				PspTaskCheckSet.cus_id_displayname._obj._renderHidden(true);
				PspTaskCheckSet.risk_type._obj._renderHidden(false);
				PspTaskCheckSet.cdt_eval._obj._renderHidden(false); 
				PspTaskCheckSet.trade_ter._obj._renderHidden(false);
				PspTaskCheckSet.cus_id._obj._renderRequired(false);
				PspTaskCheckSet.cus_id_displayname._obj._renderRequired(false);
				PspTaskCheckSet.risk_type._obj._renderRequired(false);
				PspTaskCheckSet.cdt_eval._obj._renderRequired(false); 
				PspTaskCheckSet.trade_ter._obj._renderRequired(false);
				//PspTaskCheckSet.risk_type._obj._renderRequired(true);
				//PspTaskCheckSet.cdt_eval._obj._renderRequired(true); 
				//PspTaskCheckSet.trade_ter._obj._renderRequired(true);
				PspTaskCheckSet.cus_type._obj._renderRequired(true);
				PspTaskCheckSet.cus_type._obj._renderHidden(false);

				
				PspTaskCheckSet.task_freq_unit2._setValue("");
				PspTaskCheckSet.task_freq_unit2._obj._renderRequired(false);
				PspTaskCheckSet.task_freq_unit2._obj._renderHidden(true);
				PspTaskCheckSet.task_freq._setValue("");
				PspTaskCheckSet.task_freq._obj._renderRequired(false);
				PspTaskCheckSet.task_freq._obj._renderHidden(true);
				doChange1();
			}
		}
	}
	function doChange(){
		var is_special = PspTaskCheckSet.is_special._getValue();
		if("1"==is_special){
			PspTaskCheckSet.cus_id._setValue("");
			PspTaskCheckSet.cus_id_displayname._setValue("");
			PspTaskCheckSet.risk_type._setValue("");
			PspTaskCheckSet.cdt_eval._setValue(""); 
			PspTaskCheckSet.trade_ter._setValue("");
			PspTaskCheckSet.cus_id._obj._renderHidden(false);
			PspTaskCheckSet.cus_id_displayname._obj._renderHidden(false);
			PspTaskCheckSet.risk_type._obj._renderHidden(true);
			PspTaskCheckSet.cdt_eval._obj._renderHidden(true); 
			PspTaskCheckSet.trade_ter._obj._renderHidden(true);
			PspTaskCheckSet.cus_id._obj._renderRequired(true);
			PspTaskCheckSet.cus_id_displayname._obj._renderRequired(true);
			PspTaskCheckSet.risk_type._obj._renderRequired(false);
			PspTaskCheckSet.cdt_eval._obj._renderRequired(false); 
			PspTaskCheckSet.trade_ter._obj._renderRequired(false);
			PspTaskCheckSet.cus_type._setValue("");
			PspTaskCheckSet.cus_type._obj._renderHidden(true);
			PspTaskCheckSet.cus_type._obj._renderRequired(false);
			PspTaskCheckSet.task_freq_unit3._setValue("");
			PspTaskCheckSet.task_freq_unit3._obj._renderRequired(false);
			PspTaskCheckSet.task_freq_unit3._obj._renderHidden(true);
			PspTaskCheckSet.task_freq_unit1._setValue("");
			PspTaskCheckSet.task_freq_unit1._obj._renderRequired(false);
			PspTaskCheckSet.task_freq_unit1._obj._renderHidden(true);
			PspTaskCheckSet.task_freq_unit2._obj._renderRequired(true);
			PspTaskCheckSet.task_freq_unit2._obj._renderHidden(false);
			PspTaskCheckSet.task_freq._obj._renderRequired(false);
			PspTaskCheckSet.task_freq._obj._renderHidden(true);

			PspTaskCheckSet.prd_id._setValue("");
			PspTaskCheckSet.prd_id_displayname._setValue("");
			PspTaskCheckSet.amt_area._setValue("");
			PspTaskCheckSet.loan_bal._setValue("");
			PspTaskCheckSet.prd_id._obj._renderHidden(true);
			PspTaskCheckSet.prd_id._obj._renderRequired(false);
			PspTaskCheckSet.prd_id_displayname._obj._renderHidden(true);
			PspTaskCheckSet.prd_id_displayname._obj._renderRequired(false);
			PspTaskCheckSet.amt_area._obj._renderHidden(true);
			PspTaskCheckSet.amt_area._obj._renderRequired(false);
			PspTaskCheckSet.loan_bal._obj._renderHidden(true);
			PspTaskCheckSet.loan_bal._obj._renderRequired(false);
			PspTaskCheckSet.finish_term._obj._renderRequired(true);
			PspTaskCheckSet.finish_term._obj._renderHidden(false);
		}else{
			PspTaskCheckSet.cus_id._setValue("");
			PspTaskCheckSet.cus_id_displayname._setValue("");
			PspTaskCheckSet.cus_id._obj._renderHidden(true);
			PspTaskCheckSet.cus_id_displayname._obj._renderHidden(true);
			PspTaskCheckSet.risk_type._obj._renderHidden(false);
			PspTaskCheckSet.cdt_eval._obj._renderHidden(false); 
			PspTaskCheckSet.trade_ter._obj._renderHidden(false);
			PspTaskCheckSet.cus_id._obj._renderRequired(false);
			PspTaskCheckSet.cus_id_displayname._obj._renderRequired(false);
			PspTaskCheckSet.risk_type._obj._renderRequired(false);
			PspTaskCheckSet.cdt_eval._obj._renderRequired(false); 
			PspTaskCheckSet.trade_ter._obj._renderRequired(false);
			//PspTaskCheckSet.risk_type._obj._renderRequired(true);
			//PspTaskCheckSet.cdt_eval._obj._renderRequired(true); 
			//PspTaskCheckSet.trade_ter._obj._renderRequired(true);
			PspTaskCheckSet.cus_type._obj._renderRequired(true);
			PspTaskCheckSet.cus_type._obj._renderHidden(false);

			PspTaskCheckSet.task_freq_unit2._setValue("");
			PspTaskCheckSet.task_freq_unit2._obj._renderRequired(false);
			PspTaskCheckSet.task_freq_unit2._obj._renderHidden(true);
			PspTaskCheckSet.task_freq._setValue("");
			PspTaskCheckSet.task_freq._obj._renderRequired(false);
			PspTaskCheckSet.task_freq._obj._renderHidden(true);
			PspTaskCheckSet.finish_term._setValue("");
			PspTaskCheckSet.finish_term._obj._renderRequired(false);
			PspTaskCheckSet.finish_term._obj._renderHidden(true);
			doChange1();
		}
	}
	//选择客户POP框返回方法
	function returnCus(data){
		PspTaskCheckSet.cus_id._setValue(data.cus_id._getValue());
		PspTaskCheckSet.cus_id_displayname._setValue(data.cus_name._getValue());
	}
	function doUpdate(){
		var amt_area = PspTaskCheckSet.amt_area._getValue();
		var loan_bal = PspTaskCheckSet.loan_bal._getValue();
		if(amt_area!=null&&amt_area!=''){
			if(loan_bal == null || loan_bal==''){
				alert("请输入正确的金额格式");
				return;
			}
		}
		if(!PspTaskCheckSet._checkAll()){
			return;
		}
		
		var form = document.getElementById("submitForm");
		PspTaskCheckSet._toForm(form);
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
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	function doChangeUnit1(){
		var value = PspTaskCheckSet.task_freq_unit1._getValue();
		PspTaskCheckSet.task_freq_unit._setValue(value);
	}
	function doChangeUnit2(){
		var value = PspTaskCheckSet.task_freq_unit2._getValue();
		PspTaskCheckSet.task_freq_unit._setValue(value);
	}
	function doChangeUnit3(){
		var value = PspTaskCheckSet.task_freq_unit3._getValue();
		PspTaskCheckSet.task_freq_unit._setValue(value);
	}
	function doChange1(){
		var check_mode = PspTaskCheckSet.check_mode._getValue();
		var value = PspTaskCheckSet.cus_type._getValue();
		if("01"==check_mode){
			if("02"==value){
				PspTaskCheckSet.prd_id._obj._renderHidden(false);
				PspTaskCheckSet.prd_id._obj._renderRequired(true);
				PspTaskCheckSet.prd_id_displayname._obj._renderHidden(false);
				PspTaskCheckSet.prd_id_displayname._obj._renderRequired(true);
				PspTaskCheckSet.amt_area._obj._renderHidden(false);
				PspTaskCheckSet.amt_area._obj._renderRequired(false);
				//PspTaskCheckSet.amt_area._obj._renderRequired(true);
				PspTaskCheckSet.loan_bal._obj._renderHidden(true);
				PspTaskCheckSet.loan_bal._obj._renderRequired(false);
				//PspTaskCheckSet.loan_bal._obj._renderRequired(true);
				PspTaskCheckSet.risk_type._setValue("");
				PspTaskCheckSet.cdt_eval._setValue("");
				PspTaskCheckSet.risk_type._obj._renderHidden(true);
				PspTaskCheckSet.cdt_eval._obj._renderHidden(true); 
				PspTaskCheckSet.risk_type._obj._renderRequired(false);
				PspTaskCheckSet.cdt_eval._obj._renderRequired(false);
				PspTaskCheckSet.task_freq_unit3._setValue("");
				PspTaskCheckSet.task_freq_unit3._obj._renderRequired(false);
				PspTaskCheckSet.task_freq_unit3._obj._renderHidden(true);
				PspTaskCheckSet.task_freq_unit1._obj._renderRequired(true);
				PspTaskCheckSet.task_freq_unit1._obj._renderHidden(false);
				PspTaskCheckSet.task_freq_unit1._setValue(PspTaskCheckSet.task_freq_unit._getValue());
				
			}else{
				PspTaskCheckSet.prd_id._setValue("");
				PspTaskCheckSet.prd_id_displayname._setValue("");
				PspTaskCheckSet.amt_area._setValue("");
				PspTaskCheckSet.loan_bal._setValue("");
				window.document.getElementById("PspTaskCheckSet.amt_area4bal").value = "";
				PspTaskCheckSet.prd_id._obj._renderHidden(true);
				PspTaskCheckSet.prd_id._obj._renderRequired(false);
				PspTaskCheckSet.prd_id_displayname._obj._renderHidden(true);
				PspTaskCheckSet.prd_id_displayname._obj._renderRequired(false);
				PspTaskCheckSet.amt_area._obj._renderHidden(true);
				PspTaskCheckSet.amt_area._obj._renderRequired(false);
				PspTaskCheckSet.loan_bal._obj._renderHidden(true);
				PspTaskCheckSet.loan_bal._obj._renderRequired(false);

				PspTaskCheckSet.risk_type._obj._renderHidden(false);
				PspTaskCheckSet.cdt_eval._obj._renderHidden(false); 
				PspTaskCheckSet.risk_type._obj._renderRequired(false);
				PspTaskCheckSet.cdt_eval._obj._renderRequired(false);
				//PspTaskCheckSet.risk_type._obj._renderRequired(true);
				//PspTaskCheckSet.cdt_eval._obj._renderRequired(true);
				PspTaskCheckSet.task_freq_unit1._setValue("");
				PspTaskCheckSet.task_freq_unit1._obj._renderRequired(false);
				PspTaskCheckSet.task_freq_unit1._obj._renderHidden(true);
				PspTaskCheckSet.task_freq_unit3._obj._renderRequired(true);
				PspTaskCheckSet.task_freq_unit3._obj._renderHidden(false);
				PspTaskCheckSet.task_freq_unit3._setValue(PspTaskCheckSet.task_freq_unit._getValue());
			}
		}
	}
	function setProds(data){
		PspTaskCheckSet.prd_id._setValue(data[0]);
		PspTaskCheckSet.prd_id_displayname._setValue(data[1]);
	}
	function doReturn() {
		var check_mode = PspTaskCheckSet.check_mode._getValue();
		var url = '<emp:url action="queryPspTaskCheckSetList.do"/>?check_mode='+check_mode;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};


	//处理收费条件
	function dealAmtArea(){
		var newNode = document.createElement("INPUT");
		newNode.type = "text";
		newNode.id = 'PspTaskCheckSet.amt_area4bal';
		newNode.onblur = setValue;
		//newNode.disabled = true;
		newNode.value = "";
		window.document.getElementById("PspTaskCheckSet.amt_area").parentElement.appendChild(newNode);
		
	};
	//给输入域赋值
	function setValue(){
		ismoney();
		var value2 = window.document.getElementById("PspTaskCheckSet.amt_area4bal").value;
		
	};
	//页面加载时给追加的input输入域赋值
	function setContValue(){
		var amt_area = PspTaskCheckSet.amt_area._getValue();
		var loan_bal = PspTaskCheckSet.loan_bal._getValue();  
		window.document.getElementById("PspTaskCheckSet.amt_area4bal").value = formatCurrency(loan_bal);
	};
	function ismoney() {
		var oInput = window.document.getElementById("PspTaskCheckSet.amt_area4bal").value;
		var regu = /^[0-9][\d,]{0,11}\.?\d{0,11}$/; //;金额
		var re = new RegExp(regu);
		if (re.test(oInput)) {
			window.document.getElementById("PspTaskCheckSet.amt_area4bal").value = formatCurrency(oInput);
			PspTaskCheckSet.loan_bal._setValue(formatCurrency(oInput));	
		} else {
			alert("请输入正确的金额格式");
			window.document.getElementById("PspTaskCheckSet.amt_area4bal").value = "" ;
			PspTaskCheckSet.loan_bal._setValue("");	
		}
	}
	function formatCurrency(num) {
	    num = num.toString().replace(/\$|\,/g,'');
	    if(isNaN(num))
	    num = "0";
	    sign = (num == (num = Math.abs(num)));
	    num = Math.floor(num*100+0.50000000001);
	    cents = num%100;
	    num = Math.floor(num/100).toString();
	    if(cents<10)
	    cents = "0" + cents;
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	    num = num.substring(0,num.length-(4*i+3))+','+
	    num.substring(num.length-(4*i+3));
	    return (((sign)?'':'-') + num + '.' + cents);
	}
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updatePspTaskCheckSetRecord.do" method="POST">
		<emp:gridLayout id="PspTaskCheckSetGroup" title="贷后检查任务设置" maxColumn="2">
			<emp:text id="PspTaskCheckSet.task_name" label="任务名称" maxlength="60" required="true"/>
			<emp:select id="PspTaskCheckSet.is_special" label="频率类型" required="true" dictname="STD_ZX_FREQ_TYPE" onchange="doChange()"/>
			<emp:pop id="PspTaskCheckSet.cus_id" label="客户码" required="true" url="queryAllCusPop.do?cusTypCondition=cus_status in ('20','04')&returnMethod=returnCus" />
			<emp:text id="PspTaskCheckSet.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly"/> 
			<emp:select id="PspTaskCheckSet.cus_type" label="客户类型" required="true" dictname="STD_ZB_PSP_CUS_TYPE" onchange="doChange1()"/>
			<emp:select id="PspTaskCheckSet.check_mode" label="检查方式" required="false" dictname="STD_ZB_CHECK_TYPE" readonly="true"/>
			<emp:text id="PspTaskCheckSet.finish_term" label="完成期限（日）" maxlength="6" required="true" dataType="Int" colSpan="2" />
			<emp:checkbox id="PspTaskCheckSet.risk_type" label="风险分类" required="false" dictname="STD_ZB_FIVE_SORT" layout="false" colSpan="2" />
			<emp:checkbox id="PspTaskCheckSet.cdt_eval" label="信用等级" required="false" dictname="STD_ZB_CREDIT_GRADE" layout="false" colSpan="2" />
			<emp:checkbox id="PspTaskCheckSet.trade_ter" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" layout="false" colSpan="2" />
			<emp:pop id="PspTaskCheckSet.prd_id" label="适用产品编号" url='showPrdCheckTreeDetails.do?bizline=BL300' returnMethod="setProds" required="false" colSpan="2" cssElementClass="emp_field_text_long"/> 
			<emp:textarea id="PspTaskCheckSet.prd_id_displayname" label="适用产品名称" readonly="true"  colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="PspTaskCheckSet.amt_area" label="贷款余额" required="false" dictname="STD_ZB_CHECK_AREA" />
			<emp:text id="PspTaskCheckSet.loan_bal" label="贷款余额" required="false" dataType="Currency" colSpan="2" hidden="true"/>
			<emp:text id="PspTaskCheckSet.task_freq" label="任务频率" maxlength="10" required="true" dataType="Int" />
			<emp:select id="PspTaskCheckSet.task_freq_unit2" label="任务频率单位" dictname="STD_ZB_CHECK_UNIT" hidden="true" onchange="doChangeUnit2()"/>
			<emp:select id="PspTaskCheckSet.task_freq_unit1" label="任务频率" dictname="STD_ZB_CHECK_UNIT" hidden="true" onchange="doChangeUnit1()"/>
			<emp:select id="PspTaskCheckSet.task_freq_unit3" label="任务频率" dictname="STD_ZB_CHECK_UNIT" hidden="true" onchange="doChangeUnit3()"/>
			<emp:select id="PspTaskCheckSet.task_status" label="任务状态" required="false" dictname="STD_ZB_TASK_STATUS" readonly="true" defvalue="00" hidden="true"/>
			<emp:textarea id="PspTaskCheckSet.memo" label="备注" maxlength="200" required="false" colSpan="2" hidden="true"/>
			<emp:text id="PspTaskCheckSet.serno" label="任务编号" maxlength="60" required="false" hidden="true"/>
			<emp:select id="PspTaskCheckSet.task_freq_unit" label="任务频率单位" readonly="true" dictname="STD_ZB_CHECK_UNIT" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspTaskCheckSetGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspTaskCheckSet.input_id_displayname" label="登记人"  required="false" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="PspTaskCheckSet.input_br_id_displayname" label="登记机构"   required="false" readonly="true" defvalue="$organName"/>
			<emp:text id="PspTaskCheckSet.input_id" label="登记人" maxlength="10" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="PspTaskCheckSet.input_br_id" label="登记机构" maxlength="10" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="PspTaskCheckSet.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="update" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
