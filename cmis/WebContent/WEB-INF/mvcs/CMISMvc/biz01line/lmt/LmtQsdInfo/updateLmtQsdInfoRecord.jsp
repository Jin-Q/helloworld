<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doload(){
		LmtQsdInfo.crd_amt._obj.addOneButton('cal','测算',calCrdAmt);
	}

	function doSub(){
		var form = document.getElementById("submitForm");
		if(LmtQsdInfo._checkAll()){
			LmtQsdInfo._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var data = jsonstr.value;
					if(flag == "success"){
						//alert("保存成功!");
						window.opener["${context.returnMethod}"](data);
						window.close();
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

	function calCrdAmt(){
		var cus_inte_contribute = LmtQsdInfo.cus_inte_contribute._getValue();
		var i_three_mon_cr_flux = LmtQsdInfo.i_three_mon_cr_flux._getValue();
		var i_three_mon_dep_aday = LmtQsdInfo.i_three_mon_dep_aday._getValue();
		var o_three_mon_cr_flux = LmtQsdInfo.o_three_mon_cr_flux._getValue();
		if(i_three_mon_cr_flux==''){
			alert('请先录入我行近三月账户贷方流水！');
			return;
		}
		if(i_three_mon_dep_aday==''){
			alert('请先录入我行近三月存款日均数！');
			return;
		}
		if(o_three_mon_cr_flux==''){
			alert('请先录入他行近三月账户贷方流水！');
			return;
		}
		var crd_amt = 0;
		if(cus_inte_contribute==0){
			crd_amt = i_three_mon_cr_flux/3*0.05+i_three_mon_dep_aday*2+o_three_mon_cr_flux/3*0.05*0.5;
		}else if(cus_inte_contribute<0.1){
			crd_amt = i_three_mon_cr_flux/3*0.05+i_three_mon_dep_aday*2;
		}else if(cus_inte_contribute>=0.1&&cus_inte_contribute<0.2){
			crd_amt = i_three_mon_cr_flux/3*0.05+i_three_mon_dep_aday*2;
		}else if(cus_inte_contribute>=0.2&&cus_inte_contribute<0.4){
			crd_amt = i_three_mon_cr_flux/3*0.05+i_three_mon_dep_aday*2.5;
		}else{
			crd_amt = i_three_mon_cr_flux/3*0.05+i_three_mon_dep_aday*3;
		}
		crd_amt = Math.round(crd_amt*100)/100;
		//如果超过100W 直接赋值100W   2014-09-04 tsy
		if(crd_amt>1000000){
			crd_amt = 1000000.00;
		}
		LmtQsdInfo.crd_amt._setValue(crd_amt+'');
	}
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updateLmtQsdInfoRecord.do" method="POST">
		<emp:gridLayout id="LmtQsdInfoGroup" title="泉水贷客户综合贡献度统计信息" maxColumn="2">
			<emp:text id="LmtQsdInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="LmtQsdInfo.org_limit_code" label="台账额度品种编码" maxlength="40" required="true" readonly="true" />
			
			<emp:text id="LmtQsdInfo.cus_id" label="客户号" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtQsdInfo.i_three_mon_cr_flux" label="我行近三月账户贷方流水" maxlength="40" required="true" dataType="Currency"/>
			<emp:text id="LmtQsdInfo.i_three_mon_dep_aday" label="我行近三月存款日均数" maxlength="40" required="true" dataType="Currency"/>
			<emp:text id="LmtQsdInfo.o_three_mon_cr_flux" label="他行近三月账户贷方流水" maxlength="40" required="true" dataType="Currency"/>
			<emp:text id="LmtQsdInfo.cus_half_year_aday" label="客户半年日均" required="true" readonly="true" dataType="Currency"/>
			<emp:text id="LmtQsdInfo.cus_inte_contribute" label="客户综合贡献度" required="true" readonly="true" dataType="Percent" />
			<emp:text id="LmtQsdInfo.crd_amt" label="授信额度" required="true" readonly="true" dataType="Currency"/>
			<emp:text id="LmtQsdInfo.inure_date" label="生效日期" required="false" hidden="true" />
			<emp:text id="LmtQsdInfo.inure_time" label="生效时间" required="false" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" />
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
