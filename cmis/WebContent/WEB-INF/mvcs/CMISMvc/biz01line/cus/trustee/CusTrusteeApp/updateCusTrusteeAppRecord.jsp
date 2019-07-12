<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/> 
<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnload(){
		initType1();
		initIsProvidAccredit();
	}
	//初始化是否提供书面授权书
	function initIsProvidAccredit(){
		var handSuc = function(o){
			if(o.responseText !== undefined) {
				try { var jsonstr = eval("("+o.responseText+")"); } 
				catch(e) {
				alert("校验是否为分行/支行行长失败!");
				return;
				}
				var flag = jsonstr.flag;
				if(flag == "1"){
					CusTrusteeApp.is_provid_accredit._setValue("1");
					CusTrusteeApp.is_provid_accredit._obj._renderReadonly(true);
				}else{
					return;
				}
			}
		};
	    var handFail = function(o){
	    };
	    var callback = {
	    	success:handSuc,
	    	failure:handFail
	    };
	    var url = '<emp:url action="checkCusTrusteeInputId.do"/>?&input_id=${context.CusTrusteeApp.input_id}';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
	//委托类别
	function initType1(){
		var consignor = CusTrusteeApp.consignor_type._getValue();
		if(consignor == 1){
			CusTrusteeApp.consignor_id._setValue("${context.CusTrusteeApp.input_id}");
			CusTrusteeApp.consignor_id_displayname._setValue("${context.CusTrusteeApp.input_id_displayname}");
			
			CusTrusteeApp.consignor_br_id._setValue("${context.CusTrusteeApp.input_br_id}");
			CusTrusteeApp.consignor_br_id_displayname._setValue("${context.CusTrusteeApp.input_br_id_displayname}");

			CusTrusteeApp.consignor_id_displayname._obj._renderReadonly(true);
			CusTrusteeApp.consignor_br_id_displayname._obj._renderReadonly(true);

			CusTrusteeApp.trustee_id_displayname._obj._renderReadonly(false);
			CusTrusteeApp.trustee_br_id_displayname._obj._renderReadonly(true);
		} else {
			CusTrusteeApp.trustee_id._setValue("${context.CusTrusteeApp.input_id}");
			CusTrusteeApp.trustee_id_displayname._setValue("${context.CusTrusteeApp.input_id_displayname}");
			
			CusTrusteeApp.trustee_br_id._setValue("${context.CusTrusteeApp.input_br_id}");
			CusTrusteeApp.trustee_br_id_displayname._setValue("${context.CusTrusteeApp.input_br_id_displayname}");

			CusTrusteeApp.consignor_id_displayname._obj._renderReadonly(false);
			CusTrusteeApp.consignor_br_id_displayname._obj._renderReadonly(true);
			
			CusTrusteeApp.trustee_id_displayname._obj._renderReadonly(true);
			CusTrusteeApp.trustee_br_id_displayname._obj._renderReadonly(true);
		}
	}
	function initType(){
		var consignor = CusTrusteeApp.consignor_type._getValue();
		if(consignor == 1){
			CusTrusteeApp.consignor_id._setValue("${context.CusTrusteeApp.input_id}");
			CusTrusteeApp.consignor_id_displayname._setValue("${context.CusTrusteeApp.input_id_displayname}");
			
			CusTrusteeApp.consignor_br_id._setValue("${context.CusTrusteeApp.input_br_id}");
			CusTrusteeApp.consignor_br_id_displayname._setValue("${context.CusTrusteeApp.input_br_id_displayname}");

			CusTrusteeApp.trustee_id._setValue("");
			CusTrusteeApp.trustee_id_displayname._setValue("");
			
			CusTrusteeApp.trustee_br_id._setValue("");
			CusTrusteeApp.trustee_br_id_displayname._setValue("");

			CusTrusteeApp.consignor_id_displayname._obj._renderReadonly(true);
			CusTrusteeApp.consignor_br_id_displayname._obj._renderReadonly(true);

			CusTrusteeApp.trustee_id_displayname._obj._renderReadonly(false);
			CusTrusteeApp.trustee_br_id_displayname._obj._renderReadonly(true);
		} else {
			CusTrusteeApp.trustee_id._setValue("${context.CusTrusteeApp.input_id}");
			CusTrusteeApp.trustee_id_displayname._setValue("${context.CusTrusteeApp.input_id_displayname}");
			
			CusTrusteeApp.trustee_br_id._setValue("${context.CusTrusteeApp.input_br_id}");
			CusTrusteeApp.trustee_br_id_displayname._setValue("${context.CusTrusteeApp.input_br_id_displayname}");

			CusTrusteeApp.consignor_id._setValue("");
			CusTrusteeApp.consignor_id_displayname._setValue("");
			
			CusTrusteeApp.consignor_br_id._setValue("");
			CusTrusteeApp.consignor_br_id_displayname._setValue("");

			CusTrusteeApp.consignor_id_displayname._obj._renderReadonly(false);
			CusTrusteeApp.consignor_br_id_displayname._obj._renderReadonly(true);
			
			CusTrusteeApp.trustee_id_displayname._obj._renderReadonly(true);
			CusTrusteeApp.trustee_br_id_displayname._obj._renderReadonly(true);
		}
	}
	//选择委托人
	function setConsignorId(data){
		CusTrusteeApp.consignor_id._setValue(data.actorno._getValue());
		CusTrusteeApp.consignor_id_displayname._setValue(data.actorname._getValue());
		
		CusTrusteeApp.consignor_br_id._setValue(data.orgid._getValue());
		CusTrusteeApp.consignor_br_id_displayname._setValue(data.orgid_displayname._getValue());
	}
	//选择受托人
	function setTrusteeId(data){
		CusTrusteeApp.trustee_id._setValue(data.actorno._getValue());
		CusTrusteeApp.trustee_id_displayname._setValue(data.actorname._getValue());
		
		CusTrusteeApp.trustee_br_id._setValue(data.orgid._getValue());
		CusTrusteeApp.trustee_br_id_displayname._setValue(data.orgid_displayname._getValue());
	}		

	function doSave(data){
		var openDay = '${context.OPENDAY}';
		var trusteeDate = CusTrusteeApp.trustee_date._getValue();
		if(trusteeDate!=null&&trusteeDate!=''){
			if(trusteeDate<openDay){
				alert('托管日期不能小于当前日期！');
				return;
			}
		}	
			
		if(CusTrusteeApp._checkAll()){
			var form = document.getElementById("submitForm");
			CusTrusteeApp._toForm(form);
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
						alert("保存成功！");
						if(data == "subWF"){
							doSubmitWF();
						}
					}else if(flag == "error1") {
						alert("存在已生效的托管,新增失败！");
					}else {
						alert("保存出错！");
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
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryCusTrusteeAppList.do"/>?process=no';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//-----------提交流程----------
	function doSubmitWF(){
		var is_provid_accredit = CusTrusteeApp.is_provid_accredit._getValue();
		if(is_provid_accredit == '1'){
			doSubm();
		}else{
			var serno = CusTrusteeApp.serno._getValue();
			var cus_id = CusTrusteeApp.consignor_id._getValue();
			var cus_name = CusTrusteeApp.consignor_id_displayname._getValue();
			var approve_status = CusTrusteeApp.approve_status._getValue();
			var consignor_type = CusTrusteeApp.consignor_type._getValue();
			WfiJoin.table_name._setValue("CusTrusteeApp");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(serno);
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_pk._setValue("");
			WfiJoin.prd_name._setValue("托管申请");
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("900");
			initWFSubmit(false);
		}
	};
	//-----------提交流程(目前只做保存和生成合同记录，暂时未加入流程以及修改授信台帐记录)----------
	function doSubWF(){ 
		var form = document.getElementById("submitForm");
		var result = CusTrusteeApp._checkAll();
		if(!result){
			return;
		}
        //------执行保存操作------
		doSave('subWF');
	}

	function doSubm(){
		var serno = CusTrusteeApp.serno._getValue();
        var _status = CusTrusteeApp.approve_status._getValue();
		if(_status != '000' && _status != '993' && _status != '992'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起提交!');
			return;
		}
		if(confirm("是否确认要提交？")){
			var handSuc = function(o){
				if(o.responseText !== undefined) {
					try { var jsonstr = eval("("+o.responseText+")"); } 
					catch(e) {
					alert("数据库操作失败!");
					return;
					}
					var flag = jsonstr.flag;
					if(flag == "suc"){
						alert("提交成功!");
						doReturn();
					}else{
						alert(flag);
					}
				}
			};
		    var handFail = function(o){
		    };
		    var callback = {
		    	success:handSuc,
		    	failure:handFail
		    };
		    var url = '<emp:url action="submCusTrusteeAppRecord.do"/>?&serno='+serno;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}
	}

	function checkDate(){
		var openDay = '${context.OPENDAY}';
		var trusteeDate = CusTrusteeApp.trustee_date._getValue();
		
	
		if(trusteeDate!=null&&trusteeDate!=''){
			if(trusteeDate<openDay){
				alert('托管日期不能小于当前日期！');
				CusTrusteeApp.trustee_date._setValue(openDay);
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="updateCusTrusteeAppRecord.do?deal_flag=update" method="POST">
		<emp:gridLayout id="CusTrusteeAppGroup" maxColumn="2" title="委托托管表">
			<emp:text id="CusTrusteeApp.serno" label="业务编号" maxlength="40" required="true" colSpan="2" readonly="true"/>
			
			<emp:select id="CusTrusteeApp.consignor_type" label="委托类别" required="true" dictname="STD_CUS_CONSIG_TYPE" defvalue="1" onchange="initType()"/>
			<emp:select id="CusTrusteeApp.is_provid_accredit" label="是否提供书面授权书" required="true" dictname="STD_ZX_YES_NO" />
			
			<emp:text id="CusTrusteeApp.consignor_id" label="委托人"   required="true" hidden="true"/>
			<emp:text id="CusTrusteeApp.consignor_br_id" label="委托机构" maxlength="20" required="true" hidden="true"/>
			<emp:pop id="CusTrusteeApp.consignor_id_displayname" label="委托人" url="getAllSUserPopListOp.do" returnMethod="setConsignorId" required="true" />
			<emp:text id="CusTrusteeApp.consignor_br_id_displayname" label="委托机构"  required="true" />
			
			<emp:pop id="CusTrusteeApp.trustee_id" label="托管人" url="null" required="true" hidden="true"/>
			<emp:text id="CusTrusteeApp.trustee_br_id" label="托管机构" maxlength="20" required="true" hidden="true"/>
			<emp:pop id="CusTrusteeApp.trustee_id_displayname" label="托管人" url="getAllSUserPopListOp.do" returnMethod="setTrusteeId"  required="true" />
			<emp:text id="CusTrusteeApp.trustee_br_id_displayname" label="托管机构"  required="true" />
			
			<emp:textarea id="CusTrusteeApp.trustee_detail" label="托管说明" maxlength="250" required="true" colSpan="2" />
			<emp:date id="CusTrusteeApp.trustee_date" label="托管日期" required="false" readonly="false" onblur="checkDate()"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusTrusteeAppGroup2" title="登记信息" maxColumn="2">
			<emp:select id="CusTrusteeApp.approve_status" label="审批状态" required="true" dictname="WF_APP_STATUS" defvalue="000" readonly="true" colSpan="2"/>
			
			<emp:text id="CusTrusteeApp.input_id_displayname" label="登记人"  readonly="true"/>
			<emp:text id="CusTrusteeApp.input_br_id_displayname" label="登记机构"  readonly="true"/>
			<emp:date id="CusTrusteeApp.input_date" label="登记日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:date id="CusTrusteeApp.supervise_date" label="审批日期" required="false" hidden="true"/>
			<emp:text id="CusTrusteeApp.input_id" label="登记人" maxlength="20" hidden="true" defvalue="${context.loginuserid}"/>
			<emp:text id="CusTrusteeApp.input_br_id" label="登记机构" maxlength="20" hidden="true" defvalue="${context.loginorgid}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存"/>
			<emp:button id="subWF" label="提交"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
