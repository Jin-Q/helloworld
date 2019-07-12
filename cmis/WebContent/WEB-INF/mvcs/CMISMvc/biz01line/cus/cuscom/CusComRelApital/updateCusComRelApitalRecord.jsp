<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input1 {width:450px;}
</style>
<script type="text/javascript">

	function doLoad(){
		CusComRelApital.cus_id_rel._obj.addOneButton('view12','查看',viewCusInfo);
		changeRel();
	}

	function changeRel(){
		var options = CusComRelApital.rela_type._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
		    if(options[i].value!='0' && options[i].value!='5'){
		    	options.remove(i);
			}
	    }
	}

	function checkTypeRel(){
		CusComRelApital.invt_perc._setValue("");
	}

	function checkType(){
		var invt_perc = CusComRelApital.invt_perc._obj.element.value;
		invt_perc = percentFilter(invt_perc);
		var rela_type = CusComRelApital.rela_type._getValue();
		if(rela_type==5&&parseFloat(invt_perc)>5){
		alert("持股5%以下的股东对应的出资比例必须小于等于5%");
		CusComRelApital.invt_perc._setValue("");
		return;
		}else if(rela_type==0&&parseFloat(invt_perc)<=5){
			alert("持股5%以上以及银行认为重要的股东对应的出资比例要大于5%！");
			CusComRelApital.invt_perc._setValue("");
			return;
		}
	}

	function percentFilter(amt) {
		if (amt == null) {
			return null;
		}
		var amtStr = amt;
		var amtStrTmp = amt;
		while (amtStrTmp.indexOf("%") != -1) {
			amtStr = amtStr.replace("%", "");
			amtStrTmp=amtStr;
		}
		return amtStrTmp;
	}
	//查看客户信息
	function viewCusInfo(){
		var cus_id_rel = CusComRelApital.cus_id_rel._getValue();
		if(cus_id_rel==null||cus_id_rel==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id_rel;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}

	function doUpdateCusComRelApital() {
		var form = document.getElementById("submitForm");
		var result = CusComRelApital._checkAll();
	    var cus_id = CusComRelApital.cus_id._getValue();
	    var invt_amt = CusComRelApital.invt_amt._getValue();
	    var cur_type = CusComRelApital.cur_type._getValue();
		var flag = '';
	    var cusIdRel = CusComRelApital.cus_id_rel._obj.element.value;
		
		if(result){
			 CusComRelApital._toForm(form)
			 var handleSuccess = function(o){
		    	 try {
					var jsonstr = eval("("+o.responseText+")");
					if(o.responseText !== undefined) {
						flag = jsonstr.flag;
						if(flag != "可以新增"){
							alert(flag);
							return;
						}else{
							toSubmitForm(form);	
						}
					}
				 } catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				 }
			};
			var handleFailure = function(o){
				return;
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};	
			var subUrl = '<emp:url action="checkCusComRelApitalPerc.do"/>'+'&cus_id='+cus_id+'&invt_amt='+invt_amt+'&cur_type='+cur_type+'&cus_id_rel='+cusIdRel+'&op=update';
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',subUrl, callback);	
		}
	};

	function toSubmitForm(form){
	  var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="修改成功"){
					alert("修改成功!");
					doReturn();
			    }else {
				   alert(flag);
				   doReset1();
				   return;
			    }
			}
		};
		var handleFailure = function(o){};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	function doReturn() {
		var cus_id  =CusComRelApital.cus_id._obj.element.value;
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComRelApital.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComRelApitalList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function CheckDate(){
		var inv_date = CusComRelApital.inv_date._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(inv_date!=null && inv_date!="" ){
			var flag = CheckDate1BeforeDate2(inv_date,openDay);
			if(!flag){
				alert("出资时间要小于等于当前日期！！");
				CusComRelApital.inv_date._obj.element.value="";
			}
	     }
	};

	function doReset1(){
		page.dataGroups.CusComRelApitalGroup.reset();
	};

	function checkAmt(){
		var invt_amt = CusComRelApital.invt_amt._getValue();
		if(parseFloat(invt_amt)<=0){
			alert("【出资金额（万元）】必须大于零！");
			CusComRelApital.invt_amt._setValue("");
		}
	}
	/*oujj code*/
</script>
</head>
<body class="page_content" onload="doLoad()" >
	<emp:form id="submitForm" action="updateCusComRelApitalRecord.do" method="POST">
		<emp:gridLayout id="CusComRelApitalGroup" title="资本构成信息" maxColumn="2">
			<emp:select id="CusComRelApital.cert_typ" label="出资人证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP" colSpan="2"/>
			<emp:text id="CusComRelApital.cert_code" label="出资人证件号码" maxlength="20" required="true" readonly="true" colSpan="2"/>
			<emp:text id="CusComRelApital.cus_id_rel" label="出资人客户码" required="true" maxlength="30" readonly="true"/>
			<emp:text id="CusComRelApital.cus_id" label="客户码" maxlength="35" required="false" hidden="true" />
			<emp:text id="CusComRelApital.invt_name" label="出资人名称" maxlength="80" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusComRelApital.invt_typ" label="出资人性质" required="false" dictname="STD_ZB_INVESTOR2" readonly="true" hidden="true" colSpan="2"/>
			<emp:select id="CusComRelApital.rela_type" label="关联类型" required="true" dictname="STD_ZB_RELA_TYP" cssElementClass="emp_field_text_input1" colSpan="2" onblur="checkTypeRel()"/>
			<emp:text id="CusComRelApital.loan_card" label="出资人贷款卡编号" maxlength="16" required="false" readonly="true" />
			<emp:select id="CusComRelApital.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusComRelApital.invt_amt" label="出资金额(万元)" maxlength="18" dataType="Currency" required="true" onblur="checkAmt()" />
			<emp:select id="CusComRelApital.invt_type" label="出资类型" required="true" dictname="STD_ZB_INVT_TYPE" />
			<emp:text id="CusComRelApital.invt_perc" label="出资比例" maxlength="10" required="true" dataType="Percent"  onblur="checkType()" />
			<emp:textarea id="CusComRelApital.com_invt_desc" label="出资说明" maxlength="250" required="false" colSpan="2" />
			<emp:date id="CusComRelApital.inv_date" label="出资时间" required="true" onblur="CheckDate()"/>
			<emp:textarea id="CusComRelApital.remark" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:text id="CusComRelApital.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComRelApital.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComRelApital.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComRelApital.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComRelApital.last_upd_date" label="更新日期" required="false" hidden="true"/>
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="updateCusComRelApital" label="保存" />
			<emp:button id="reset1" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
