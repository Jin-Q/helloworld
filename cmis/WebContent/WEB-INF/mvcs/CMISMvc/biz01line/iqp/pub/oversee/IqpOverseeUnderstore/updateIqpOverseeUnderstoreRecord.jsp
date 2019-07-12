<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function getDate(){
		var cha = IqpOverseeUnderstore.store_cha._getValue();
		if(cha=='01'){
			IqpOverseeUnderstore.end_date._obj._renderHidden(true);
			IqpOverseeUnderstore.end_date._obj._renderRequired(false);
		}
		else{
			IqpOverseeUnderstore.end_date._obj._renderHidden(false);
			IqpOverseeUnderstore.end_date._obj._renderRequired(true);
		}
	}
	//获取仓库地址
	function onReturnRegStateCode(date){
		IqpOverseeUnderstore.store_addr._obj.element.value=date.id;
		IqpOverseeUnderstore.store_addr_displayname._obj.element.value=date.label;
    }
	 function doUpdate(){
		if(IqpOverseeUnderstore._checkAll()){
			var form = document.getElementById("submitForm");
			IqpOverseeUnderstore._toForm(form);
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
						alert("修改成功！");
                              window.close();
                              window.opener.location.reload();
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
				
	function doClose(){
		window.close();
	}

	function doLoad(){
		getDate();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updateIqpOverseeUnderstoreRecord.do" method="POST">
		<emp:gridLayout id="IqpOverseeUnderstoreGroup" maxColumn="2" title="下属仓库">
			<emp:text id="IqpOverseeUnderstore.store_id" label="仓库编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeUnderstore.store_name" label="仓库名称" maxlength="32" required="true" />
			<emp:pop id="IqpOverseeUnderstore.store_addr_displayname" label="仓库地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="true" 
                      returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>
            <emp:text id="IqpOverseeUnderstore.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:select id="IqpOverseeUnderstore.store_cha" label="仓库性质" required="true" dictname="STD_ZB_STORE_CHA" onchange="getDate()"/>
			<emp:date id="IqpOverseeUnderstore.end_date" label="仓库使用到期日期" required="true" />
			<emp:select id="IqpOverseeUnderstore.store_equip_status" label="仓库设备状态" required="true" dictname="STD_ZB_EQUIP_STAYUS" />
			<emp:text id="IqpOverseeUnderstore.store_chief" label="仓库负责人" maxlength="32" required="true" />
			<emp:text id="IqpOverseeUnderstore.store_bend" label="仓库容量(吨)" maxlength="20" required="true" />
			<emp:text id="IqpOverseeUnderstore.transfer_abi" label="吞吐能力(吨)" maxlength="20" required="true" />
			<emp:text id="IqpOverseeUnderstore.open_squ" label="露天面积(m²)" maxlength="20" required="true" />
			<emp:text id="IqpOverseeUnderstore.no_util_squ" label="未使用面积(m²)" maxlength="20" required="true" />
			<emp:select id="IqpOverseeUnderstore.is_break_opr" label="是否有重大违规操作" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpOverseeUnderstore.is_bank_oversee" label="是否有我行监管人员" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:textarea id="IqpOverseeUnderstore.oper_range" label="经营范围" maxlength="100" required="false" />
			<emp:textarea id="IqpOverseeUnderstore.memo" label="备注" maxlength="100" required="false" colSpan="2" />
			<emp:text id="IqpOverseeUnderstore.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeUnderstore.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
			<emp:date id="IqpOverseeUnderstore.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:text id="IqpOverseeUnderstore.serno" label="监管机构编号" maxlength="32" required="false" hidden="true"/>
			<emp:pop id="IqpOverseeUnderstore.store_addr" label="仓库地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="update" label="修改" op="update"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
