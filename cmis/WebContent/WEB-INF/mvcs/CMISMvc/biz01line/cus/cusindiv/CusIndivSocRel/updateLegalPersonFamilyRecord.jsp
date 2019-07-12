<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String com_id="";
	if(context.containsKey("com_id")){
		com_id = context.getDataValue("com_id").toString();
	}
%>
	<jsp:include page="/include.jsp" flush="true" />
	<script type="text/javascript">
	/*--user code begin--*/
	var cus_rel_temp ;
	function doOnload(){
		cus_rel_temp = CusIndivSocRel.indiv_cus_rel._getValue();//[保存]之前的'与客户关系'
		setCusInfo();
		doFamilyFlgChange();
		
	}

	function setCusInfo(){
		var certTyp = CusIndivSocRel.indiv_rel_cert_typ._obj.element.value;
		var certCode = CusIndivSocRel.indiv_rl_cert_code._obj.element.value;
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
            return ;
        }
		if(certTyp=='0' || certTyp=='7'){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if(certCode.length=='15'){
					if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
						CusIndivSocRel.indiv_rl_cert_code._obj.element.value=oldCardToNewCard(certCode);
					}else{
						CusIndivSocRel.indiv_rl_cert_code._obj.element.value="";
					}
				}
			}else{
				CusIndivSocRel.indiv_rl_cert_code._obj.element.value="";
				CusIndivSocRel.indiv_rl_cert_code._obj.element.focus();
				return;
			}
		}
		//调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名) 
		getCusInfo('returnCus',certTyp,certCode,'setCusInfo');
	}

	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column
	function returnCus(cusObj){
		
		if(cusObj==null||cusObj=='undefined'){
			CusIndivSocRel.cus_id_rel._setValue('');
			return;
		}

		//家庭成员客户码
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusIndivSocRel.cus_id_rel._setValue(cus_id_rel);
		}
		//证件签发日期
		var indiv_id_start_dt = cusObj.indiv_id_start_dt;
		if (indiv_id_start_dt != null){
			CusIndivSocRel.crt_date._setValue(indiv_id_start_dt);
		}
		//签发到期日期
		var indiv_id_exp_dt=cusObj.indiv_id_exp_dt;
		if (indiv_id_exp_dt != null){
			CusIndivSocRel.crt_end_date._setValue(indiv_id_exp_dt);
		}
		//证件类型1
		var cert_typ_other=cusObj.cert_typ_other;
		if (cert_typ_other != null){
			CusIndivSocRel.com_mrg_cert_typ_other._setValue(cert_typ_other);
		}
		//证件号码1
		var cert_code_other=cusObj.cert_code_other;
		if (cert_code_other != null){
			CusIndivSocRel.com_mrg_cert_code_other._setValue(cert_code_other);
		}
		//签发日期1
		var crt_date_other=cusObj.crt_date_other;
		if (crt_date_other != null){ 
			CusIndivSocRel.crt_date_other._setValue(crt_date_other);
		}
		//签发到期日期1
		var crt_end_date_other=cusObj.crt_end_date_other;
		if (crt_end_date_other != null){
			CusIndivSocRel.crt_end_date_other._setValue(crt_end_date_other);
		}
	}

	
	function doFamilyFlgChange(){
		var vChange = CusIndivSocRel.indiv_family_flg._getValue();
		var cus_rel = CusIndivSocRel.indiv_cus_rel._getValue();
		var options = CusIndivSocRel.indiv_cus_rel._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
				options.remove(i);
		}
		if(vChange==1){
			var varOption = new Option('配偶','1');
			options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
			var varOption = new Option('兄弟姐妹','9');
			options.add(varOption);
		}
		if(cus_rel!=null&&cus_rel!=''){
			CusIndivSocRel.indiv_cus_rel._setValue(cus_rel);
		}
	}
	
	function doUpdateCusIndivSocRel(){
		var form = document.getElementById("submitForm");
		var result = CusIndivSocRel._checkAll();
		if(result){
			var cus_rel =  CusIndivSocRel.indiv_cus_rel._getValue();
			if(cus_rel=='1' && cus_rel_temp != cus_rel){
				checkSpsIsExists();
			}else{
				CusIndivSocRel._toForm(form)
				toSubmitForm(form);
			}
		}
	}

	//新增前先进行检查
	function checkSpsIsExists(){
		var cusId = CusIndivSocRel.cus_id._obj.element.value;
		var cusIdRel = CusIndivSocRel.cus_id_rel._obj.element.value;
		if(cusId != cusIdRel){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "可以新增配偶信息"){
						var form = document.getElementById("submitForm");
						var result = CusIndivSocRel._checkAll();
						if(result){
							CusIndivSocRel._toForm(form)
							toSubmitForm(form);
						}else alert("请输入必填项！");
					}else {
						alert(flag);
						return;
					}
				}
			};
			var handleFailure = function(o){
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = '<emp:url action="checkSpsIsExistsByCusId.do"/>&cus_id='+cusId+"&cus_id_rel="+cusIdRel;
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		 }else{
			 alert("法人家庭成员不能是自己,修改失败！");
			return false;
		}
	}
	
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
					alert(flag);
					goback();
			    }else {
				 alert(flag);
				 return;
			   }
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	
	function goback(){
		var com_id = "<%=com_id%>";
		var paramStr = "";
		var stockURL = "";
		var editFlag = '${context.EditFlag}';
		//if(com_id!=null &&com_id!=""){
			paramStr="cus_id="+com_id+"&EditFlag="+editFlag;
			stockURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+paramStr;
		//}else{
		//	paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value;
		//	stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStr;
		//}
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function doReturn(){
		goback();
	}
	
	function doChange() {
		var vChange = CusIndivSocRel.indiv_family_flg._getValue();
		var options = CusIndivSocRel.indiv_cus_rel._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
				options.remove(i);
		}
		if(vChange==0){
			//var varOption = new Option('配偶','1');
			//options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
			//var varOption = new Option('其他血缘关系','4');
			//options.add(varOption);
			//var varOption = new Option('其他姻亲关系','5');
			//options.add(varOption);
			var varOption = new Option('同事','6');
			options.add(varOption);
			var varOption = new Option('合伙人','7');
			options.add(varOption);
			var varOption = new Option('其他关系','8');
			options.add(varOption);
			var varOption = new Option('兄弟姐妹','9');
			options.add(varOption);
		}
		else if(vChange==1){
	
			//var varOption = new Option('配偶','1');
			//options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
			var varOption = new Option('兄弟姐妹','9');
			options.add(varOption);
			//var varOption = new Option('其他血缘关系','4');
			//options.add(varOption);
			//var varOption = new Option('其他姻亲关系','5');
			//options.add(varOption);
	
		}
		else if(vChange==2){
			var varOption = new Option('同事','6');
			options.add(varOption);
			var varOption = new Option('合伙人','7');
			options.add(varOption);
			var varOption = new Option('其他关系','8');
			options.add(varOption);
		}
	}
	/*--user code end--*/
</script>
	</head>
	<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="updateCusIndivSocRelRecord.do" method="POST">
		<emp:gridLayout id="CusIndivSocRelGroup" maxColumn="2" title="法人家庭成员">
			<emp:select id="CusIndivSocRel.indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="0" readonly="true"/>
			<emp:text id="CusIndivSocRel.indiv_rl_cert_code" label="证件号码" required="true" readonly="true"/>
			<emp:date id="CusIndivSocRel.crt_date" label="签发日期" required="false" readonly="true" />
			<emp:date id="CusIndivSocRel.crt_end_date" label="签发到期日期" required="false" readonly="true" />
			<emp:select id="CusIndivSocRel.com_mrg_cert_typ_other" label="证件类型1:" required="false" dictname="STD_ZB_CERT_TYP" readonly="true" />
			<emp:text id="CusIndivSocRel.com_mrg_cert_code_other" label="证件号码1:"  maxlength="20" required="false" readonly="true"/>
			<emp:date id="CusIndivSocRel.crt_date_other" label="签发日期1:" required="false" readonly="true" />
			<emp:date id="CusIndivSocRel.crt_end_date_other" label="签发到期日期1:" required="false" readonly="true" />
			<br/>
			<emp:select id="CusIndivSocRel.indiv_family_flg" label="是否家庭成员" required="true" dictname="STD_ZX_YES_NO"  hidden="true" defvalue="1" />
			<emp:text id="CusIndivSocRel.cus_id" label="法人客户码" required="true"  colSpan="2" hidden="true" />
            <emp:text id="CusIndivSocRel.cus_id_rel" label="法人家庭成员客户码" required="true"  readonly="true"/>
			<emp:text id="CusIndivSocRel.indiv_rel_cus_name" label="姓名" required="true" maxlength="30" readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX"  readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_cus_rel" label="与客户关系" required="true" dictname="STD_ZB_INDIV_CUS"  />
			<emp:text id="CusIndivSocRel.indiv_rl_y_incm" label="年收入（元）" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusIndivSocRel.indiv_rel_job" label="职业" required="false" dictname="STD_ZX_EMPLOYMET" readonly="true" />
			<emp:text id="CusIndivSocRel.indiv_rel_com_name" label="单位名称" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:select id="CusIndivSocRel.indiv_rel_duty" label="职务" required="false" dictname="STD_ZX_DUTY" readonly="true" />
			<emp:textarea id="CusIndivSocRel.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)" />
			<emp:text id="CusIndivSocRel.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId" />
			<emp:text id="CusIndivSocRel.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo" />
			<emp:date id="CusIndivSocRel.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY" />
			<emp:text id="CusIndivSocRel.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
			<emp:date id="CusIndivSocRel.last_upd_date" label="更新日期" required="false" hidden="true" />
		</emp:gridLayout>
		<div align="center"><br>
			<emp:button id="updateCusIndivSocRel" label="保存" /> 
			<emp:button id="reset" label="重置" /> 
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	</body>
	</html>
</emp:page>