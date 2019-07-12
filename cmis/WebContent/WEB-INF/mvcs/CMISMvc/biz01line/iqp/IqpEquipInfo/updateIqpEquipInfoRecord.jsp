<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input2{
border:1px solid #b7b7b7;
width:660px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
		var serno = IqpEquipInfo.serno._getValue();
		var form = document.getElementById("submitForm");
		if(IqpEquipInfo._checkAll()){
			IqpEquipInfo._toForm(form); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var message = jsonstr.message;
					if(flag == "success"){
						alert("修改成功!");
						var url = '<emp:url action="queryIqpEquipInfoList.do"/>?op=update&serno='+serno; 
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert(message);
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
	//计算货物总价
	function clcTotal(){
	    var equip_unit_price=IqpEquipInfo.equip_unit_price._getValue();
	    var qnt=IqpEquipInfo.qnt._getValue();
	    if(equip_unit_price!=null && equip_unit_price!="" &&qnt!=null &&qnt!=""){
		   	var qntp=parseInt(qnt);
		   	var price=parseFloat(equip_unit_price);
		   	var total=parseFloat(qntp*price).toFixed(2);
		   	IqpEquipInfo.pur_total_amt._setValue(total);
	    }
	};			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpEquipInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpEquipInfoGroup" maxColumn="2" title="银租通设备信息">
			<emp:text id="IqpEquipInfo.equip_name" label="设备名称" maxlength="150" required="true" />
			<emp:text id="IqpEquipInfo.produce_manuf" label="生产厂商" maxlength="150" required="false" />
			<emp:text id="IqpEquipInfo.equip_brand" label="设备品牌" maxlength="150" required="true" />
			<emp:text id="IqpEquipInfo.equip_model" label="设备型号" maxlength="100" required="true" />
			<emp:text id="IqpEquipInfo.produce_addr" label="产地" maxlength="250" required="false" cssElementClass="emp_input2" colSpan="2"/>
			<emp:select id="IqpEquipInfo.cur_type" label="购置币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpEquipInfo.equip_unit_price" label="设备单价" maxlength="16" required="false" dataType="Currency" onblur="clcTotal()"/>
			<emp:text id="IqpEquipInfo.qnt" label="数量" maxlength="38" required="false" dataType="Int" onblur="clcTotal()"/>
			<emp:text id="IqpEquipInfo.pur_total_amt" label="购置总价" maxlength="16" readonly="true" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpEquipInfo.produce_date" label="出厂日期" required="false" />
			<emp:date id="IqpEquipInfo.pur_date" label="购入日期" required="false" />
			<emp:select id="IqpEquipInfo.is_new" label="是否全新" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="IqpEquipInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="IqpEquipInfo.equip_pk" label="设备编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpEquipInfo.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
