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
	function getAuth(){
		var mode=IqpOverseeAgr.oversee_mode._getValue();
		if(mode=='01'){
			IqpOverseeAgr.low_auth_value._obj._renderHidden(false);
			IqpOverseeAgr.low_auth_value._obj._renderRequired(true);
		}else{
			IqpOverseeAgr.low_auth_value._obj._renderHidden(true);
			IqpOverseeAgr.low_auth_value._obj._renderRequired(false);
			IqpOverseeAgr.low_auth_value._setValue("");
		}
	}
      //异步保存数据
	function doSub(){
		if(IqpOverseeAgr._checkAll()){
			var form = document.getElementById("submitForm");
			IqpOverseeAgr._toForm(form);
			var handleSuccess = function(o){
			if(o.responseText !== undefined) {									
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("保存成功！");
						var url = '<emp:url action="queryIqpOverseeAgrListMain.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("发生异常！");
					}
			   }
			};
			var callback = {
				success:handleSuccess,
				failure:null
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}
	};
	function doReturn(){
		var url = '<emp:url action="queryIqpOverseeAgrListMain.do"/>';
			url = EMPTools.encodeURI(url);
			window.location = url;
	}
	//检查警戒线和平仓线的大小。警戒线大于平仓线
	function  checkLine(){
		var vigiLine=parseFloat(IqpOverseeAgr.vigi_line._obj.element.value);//警戒线
		var storLine= parseFloat(IqpOverseeAgr.stor_line._obj.element.value);//平仓线
		if(null!=vigiLine && ""!=vigiLine && null!=storLine && ""!=storLine){
			if(vigiLine < storLine){
				alert("警戒线要大于平仓线！");
				IqpOverseeAgr.stor_line._setValue("");
				return false;
			}
		}
	};
	function setNetAgrNo(data){
		IqpOverseeAgr.net_agr_no._setValue(data.net_agr_no._getValue());
	};

	function returnCus(data){
		IqpOverseeAgr.mortgagor_id._setValue(data.cus_id._getValue());
		IqpOverseeAgr.mortgagor_id_displayname._setValue(data.cus_name._getValue());
	};

	function getConId(data){
		IqpOverseeAgr.oversee_con_id._setValue(data.oversee_org_id._getValue());
		IqpOverseeAgr.oversee_con_id_displayname._setValue(data.oversee_org_id_displayname._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpOverseeAgrRecord.do" method="POST">		
		<emp:gridLayout id="IqpOverseeAgrGroup" title="监管协议" maxColumn="2">
		    <!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start -->
		    <emp:pop id="IqpOverseeAgr.net_agr_no" label="网络协议编号"  url="queryIqpNetAgrNo.do?returnMethod=setNetAgrNo"  />
			<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:pop id="IqpOverseeAgr.mortgagor_id" label="出质人客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')&returnMethod=returnCus" required="true" colSpan="2"/>
			<emp:text id="IqpOverseeAgr.mortgagor_id_displayname" label="出质人客户名称"   cssElementClass="emp_field_text_input2" required="true" colSpan="2"/>
			<emp:pop id="IqpOverseeAgr.oversee_con_id" label="监管企业编号" required="true" colSpan="2" url="IqpOverseeOrg4PopList.do?restrictUsed=false&returnMethod=getConId"/>
			<emp:text id="IqpOverseeAgr.oversee_con_id_displayname" label="监管企业名称"  cssElementClass="emp_field_text_input2" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpOverseeAgr.bail_acct" label="保证金账号" maxlength="32" required="false" />
			<emp:text id="IqpOverseeAgr.bail_acct_name" label="保证金户名" maxlength="32" required="false" />		
			<emp:text id="IqpOverseeAgr.oversee_store" label="监管仓库" maxlength="32" required="false" />
			<emp:select id="IqpOverseeAgr.insure_mode" label="投保方式" required="true" dictname="STD_ZB_INSURE_MODE" />
			<emp:text id="IqpOverseeAgr.vigi_line" label="警戒线" maxlength="20" required="true" dataType="Percent" onblur="checkLine()"/>
			<emp:text id="IqpOverseeAgr.stor_line" label="平仓线" maxlength="20" required="true" dataType="Percent" onblur="checkLine()"/>
			<emp:text id="IqpOverseeAgr.froze_line" label="冻结线" maxlength="20" required="true" dataType="Percent" colSpan="2"/>
			<emp:select id="IqpOverseeAgr.oversee_mode" label="监管方式" required="true" dictname="STD_ZB_OVERSEE_TYPE" onchange="getAuth()"/>
			<emp:text id="IqpOverseeAgr.low_auth_value" label="最低核准价值" maxlength="18" required="true" dataType="Currency"/>
			<emp:textarea id="IqpOverseeAgr.memo" label="备注" maxlength="100" required="false" colSpan="2" />	
			<emp:text id="IqpOverseeAgr.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpOverseeAgr.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>
			<emp:date id="IqpOverseeAgr.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpOverseeAgr.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeAgr.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>	
			<emp:date id="IqpOverseeAgr.start_date" label="监管起始日期" required="false" hidden="true"/>
			<emp:date id="IqpOverseeAgr.end_date" label="监管到期日期" required="false" hidden="true"/>
			<emp:select id="IqpOverseeAgr.status" label="状态" required="false" dictname="STD_ZB_OVERAGR_STATUS" hidden="true" defvalue="2"/>
			<emp:text id="IqpOverseeAgr.oversee_agr_no" label="监管协议号" maxlength="32" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

