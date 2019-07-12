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
	function getOrgID(data){
		IqpGuarChangeApp.manager_br_id._setValue(data.organno._getValue());
		IqpGuarChangeApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};	  
	//主担保方式下拉框相应方法
	function assure_mainChange(){
		var assureMainValue =IqpGuarChangeApp.new_assure_main._getValue();
		var assmainDtsoptions = IqpGuarChangeApp.new_assure_main_details._obj.element.options;
		var a = "0";
		var b = "0";
		var c = "0";
		var d = "0";
		var e = "0";
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
			if(assmainDtsoptions[i].value=="10"){//100%保证金
				d = "1";
			}
			if(assmainDtsoptions[i].value=="11"){//准全额保证金
				e = "1";
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
		if(d == "0"){
			var varOption = new Option('100%保证金','10');
			assmainDtsoptions.add(varOption);
		}
		if(e == "0"){
			var varOption = new Option('准全额保证金','11');
			assmainDtsoptions.add(varOption);
		}
		if(assureMainValue == ""){
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(false);
			IqpGuarChangeApp.new_assure_main_details._setValue("");
		}else if(assureMainValue =="100"){//主担保方式为抵押时，担保方式细分自动赋值为抵押
			IqpGuarChangeApp.new_assure_main_details._setValue("1");
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(true);
		}else if(assureMainValue =="300"){//保证
			IqpGuarChangeApp.new_assure_main_details._setValue("8");
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(true);
		}else if(assureMainValue =="400"){//信用
			IqpGuarChangeApp.new_assure_main_details._setValue("9");
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(true);
		}else if(assureMainValue =="500"){//100%保证金  
			IqpGuarChangeApp.new_assure_main_details._setValue("10");
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(true);
		}else if(assureMainValue =="510"){//准全额保证金
			IqpGuarChangeApp.new_assure_main_details._setValue("11");
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(true);
		}else if(assureMainValue.substring(0,1) == "2"){
			IqpGuarChangeApp.new_assure_main_details._obj._renderReadonly(false);
			IqpGuarChangeApp.new_assure_main_details._setValue("");
			var assmainDtsoptions = IqpGuarChangeApp.new_assure_main_details._obj.element.options;
			for(var i=assmainDtsoptions.length-1;i>=0;i--){	
				if(assmainDtsoptions[i].value=="1" || assmainDtsoptions[i].value=="8" ||assmainDtsoptions[i].value=="9" ||assmainDtsoptions[i].value=="10" ||assmainDtsoptions[i].value=="11"){
					assmainDtsoptions.remove(i);
				}
			}
		}
	};
	function doSave(){ 
		var form = document.getElementById("submitForm"); 
		if(!IqpGuarChangeApp._checkAll()){
           return;
		} 
		IqpGuarChangeApp._toForm(form);  
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;  
				var serno = jsonstr.serno;  
				if(flag == "success"){ 
					alert("保存成功!"); 
					var url = '<emp:url action="getIqpGuarChangeAppUpdatePage.do"/>?menuId=queryIqpGuarChangeAppList&op=update&serno='+serno;  
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("异步请求出错！");
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
	}; 

	function onload(){
		IqpGuarChangeApp.cont_no._obj.addOneButton("cont_no","查看",getCont);
    };
    function getCont(){
		var cont_no = IqpGuarChangeApp.cont_no._getValue();
		url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&cont_no='+cont_no+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};   
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="addIqpGuarChangeAppRecord.do" method="POST">
	 <emp:tabGroup mainTab="base_tab" id="mainTab" >
	   <emp:tab label="担保变更申请基本信息" id="base_tab" needFlush="true" initial="true" >  
		<emp:gridLayout id="IqpGuarChangeAppGroup" maxColumn="2" title="担保变更申请信息 "> 
			<emp:date id="IqpGuarChangeApp.apply_date" label="申请日期" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.old_serno" label="原业务编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.prd_id" label="产品编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.prd_id_displayname" label="产品名称" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true" colSpan="2"/>
			<emp:text id="IqpGuarChangeApp.cus_id_displayname" label="客户名称" required="false" readonly="true" colSpan="2"/>
			<emp:select id="IqpGuarChangeApp.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true"/>
			<emp:select id="IqpGuarChangeApp.assure_main_details" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
			<emp:select id="IqpGuarChangeApp.cont_cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.exchange_rate" label="汇率" maxlength="16" required="false" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.cont_amt" label="合同金额" maxlength="16" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.same_security_amt" label="视同保证金" maxlength="16" required="false" hidden="true" readonly="true" dataType="Currency"/>
			<emp:text id="IqpGuarChangeApp.risk_open_amt" label="风险敞口金额" maxlength="16" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="IqpGuarChangeApp.risk_open_rate" label="敞口比例" maxlength="10" required="false" dataType="Percent" readonly="true"/>
			<emp:date id="IqpGuarChangeApp.cont_start_date" label="起始日期" required="false" readonly="true"/> 
			<emp:date id="IqpGuarChangeApp.cont_end_date" label="到期日期" required="false" readonly="true"/>
		</emp:gridLayout>     
		<emp:gridLayout id="IqpGuarChangeAppGroup" title="担保修改信息" maxColumn="2">
			<emp:select id="IqpGuarChangeApp.new_assure_main" label="修改后担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" onclick="assure_mainChange()" />
			<emp:select id="IqpGuarChangeApp.new_assure_main_details" label="修改后担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
			<emp:textarea id="IqpGuarChangeApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" />
        </emp:gridLayout>     
		<emp:gridLayout id="IqpGuarChangeAppGroup" title="登记信息" maxColumn="3">	 	
		    <emp:pop id="IqpGuarChangeApp.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:text id="IqpGuarChangeApp.input_id_displayname" label="登记人" required="true" readonly="true"/>   
			<emp:text id="IqpGuarChangeApp.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>   
			<emp:date id="IqpGuarChangeApp.input_date" label="登记日期" required="true" readonly="true" />
			<emp:select id="IqpGuarChangeApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>     

			<emp:text id="IqpGuarChangeApp.manager_br_id" label="管理机构" required="false" hidden="true" />   
			<emp:text id="IqpGuarChangeApp.input_id" label="登记人" maxlength="20" required="false" hidden="true" /> 
			<emp:text id="IqpGuarChangeApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		</emp:gridLayout>         
		</emp:tab>   
		</emp:tabGroup>
		<div align="center">
			<br>
			<emp:button id="save" label="保存" /> 
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
