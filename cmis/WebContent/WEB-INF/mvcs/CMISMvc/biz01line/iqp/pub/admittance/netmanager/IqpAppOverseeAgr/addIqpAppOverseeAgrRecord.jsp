<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	//检查警戒线和平仓线的大小。警戒线大于平仓线
	function  checkLine(){
		var vigiLine=parseFloat(IqpAppOverseeAgr.vigi_line._obj.element.value);//警戒线
		var storLine= parseFloat(IqpAppOverseeAgr.stor_line._obj.element.value);//平仓线
		if(null!=vigiLine && ""!=vigiLine && null!=storLine && ""!=storLine){
			if(vigiLine < storLine){
				alert("警戒线要大于平仓线！");
				IqpAppOverseeAgr.stor_line._setValue("");
				return false;
			}else{
                return true;
			}
		}
	};		

	function getAuth(){
		var mode=IqpAppOverseeAgr.oversee_mode._getValue();
		if(mode=='01'){
			IqpAppOverseeAgr.low_auth_value._obj._renderHidden(false);
			IqpAppOverseeAgr.low_auth_value._obj._renderRequired(true);
		}else{
			IqpAppOverseeAgr.low_auth_value._obj._renderHidden(true);
			IqpAppOverseeAgr.low_auth_value._obj._renderRequired(false);
			IqpAppOverseeAgr.low_auth_value._setValue("");
		}
	};

	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAppOverseeAgr._checkAll()){
			if(!checkLine()){
                return;
			}
			IqpAppOverseeAgr._toForm(form);
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
						var url = '<emp:url action="queryIqpAppOverseeAgrList.do"/>?cus_id=${context.cus_id}'
                            +"&mem_cus_id=${context.mem_cus_id}"
                            +"&mem_manuf_type=${context.mem_manuf_type}"
                            +"&serno=${context.serno}";
                        url = EMPTools.encodeURI(url);
                        window.location = url;
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return;
		}
	};

	function getConId(data){
		IqpAppOverseeAgr.oversee_con_id._setValue(data.oversee_org_id._getValue());
		IqpAppOverseeAgr.oversee_con_id_displayname._setValue(data.oversee_org_id_displayname._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAppOverseeAgrRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAppOverseeAgrGroup" title="监管协议申请表" maxColumn="2">
			
			<emp:text id="IqpAppOverseeAgr.mortgagor_id" label="出质人客户码" maxlength="30" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpAppOverseeAgr.mortgagor_id_displayname" label="出质人客户名称"   cssElementClass="emp_field_text_input2" required="true" colSpan="2" readonly="true"/>
			<emp:pop id="IqpAppOverseeAgr.oversee_con_id" label="监管企业客户码" url="IqpOverseeOrg4PopList.do?restrictUsed=false&returnMethod=getConId" required="true" colSpan="2" />
			<emp:text id="IqpAppOverseeAgr.oversee_con_id_displayname" label="监管企业名称"   cssElementClass="emp_field_text_input2" required="true" colSpan="2" readonly="true"/>
			
			<emp:text id="IqpAppOverseeAgr.bail_acct" label="保证金账户账号" maxlength="40" required="false" />
			<emp:text id="IqpAppOverseeAgr.bail_acct_name" label="保证金账户名称" maxlength="80" required="false" />
			<emp:text id="IqpAppOverseeAgr.oversee_store" label="监管仓库" maxlength="100" required="false" />
			<emp:select id="IqpAppOverseeAgr.insure_mode" label="投保方式" required="false" dictname="STD_ZB_INSURE_MODE"/>
			<emp:text id="IqpAppOverseeAgr.vigi_line" label="警戒线" maxlength="10" required="true" dataType="Percent"  />
			<emp:text id="IqpAppOverseeAgr.stor_line" label="平仓线" maxlength="10" required="true" dataType="Percent"  />    
			<emp:text id="IqpAppOverseeAgr.froze_line" label="冻结线" maxlength="10" required="true" dataType="Percent" colSpan="2"/>
			<emp:select id="IqpAppOverseeAgr.oversee_mode" label="监管方式" required="true" dictname="STD_ZB_OVERSEE_TYPE" onchange="getAuth()" />
			<emp:text id="IqpAppOverseeAgr.low_auth_value" label="最低核准价值" maxlength="16" required="true" dataType="Currency" />
			
            <emp:textarea id="IqpAppOverseeAgr.memo" label="备注" maxlength="500" required="false" colSpan="2"/>			
			<emp:text id="IqpAppOverseeAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppOverseeAgr.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppOverseeAgr.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpAppOverseeAgr.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:date id="IqpAppOverseeAgr.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true" />
			
			<emp:text id="IqpAppOverseeAgr.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppOverseeAgr.oversee_agr_no" label="监管协议号" maxlength="40" required="false" hidden="true"/>
		    <emp:date id="IqpAppOverseeAgr.start_date" label="监管起始日期" required="false" hidden="true"/>
			<emp:date id="IqpAppOverseeAgr.end_date" label="监管到期日期" required="false" hidden="true"/>
		</emp:gridLayout>
		 
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

