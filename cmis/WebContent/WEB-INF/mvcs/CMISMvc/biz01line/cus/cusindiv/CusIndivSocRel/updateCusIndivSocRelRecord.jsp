<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
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
	var cus_rel_temp;
	function doOnload(){
		doFamilyFlgChange();
		cus_rel_temp = CusIndivSocRel.indiv_cus_rel._getValue();//[保存]之前的'与客户关系'
	}

	function doFamilyFlgChange(){
		var vChange = CusIndivSocRel.indiv_family_flg._getValue();
		var cus_rel = CusIndivSocRel.indiv_cus_rel._getValue();
		var options = CusIndivSocRel.indiv_cus_rel._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
				options.remove(i);
		}
		if(vChange==0){
			var varOption = new Option('配偶','1');
			options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
		/*	var varOption = new Option('其他血缘关系','4');
			options.add(varOption);
			var varOption = new Option('其他姻亲关系','5');
			options.add(varOption);*/
			var varOption = new Option('同事','6');
			options.add(varOption);
			var varOption = new Option('合伙人','7');
			options.add(varOption);
			var varOption = new Option('其他关系','8');
			options.add(varOption);
			var varOption = new Option('兄弟姐妹','9');
			options.add(varOption);
		} else if(vChange==1){
			var varOption = new Option('配偶','1');
			options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
			var varOption = new Option('兄弟姐妹','9');
			options.add(varOption);
		/*	var varOption = new Option('其他血缘关系','4');
			options.add(varOption);
			var varOption = new Option('其他姻亲关系','5');
			options.add(varOption);*/
		}else if(vChange==2){
			var varOption = new Option('同事','6');
			options.add(varOption);
			var varOption = new Option('合伙人','7');
			options.add(varOption);
			var varOption = new Option('其他关系','8');
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
			alert("客户配偶不能为自己！");
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
		if(com_id!=null &&com_id!=""){
			paramStr="cus_id="+com_id+"&EditFlag="+editFlag;
			stockURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+paramStr;
		}else{
			paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value+"&EditFlag="+editFlag;
			stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStr;
		}
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
			var varOption = new Option('配偶','1');
			options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
			var varOption = new Option('其他血缘关系','4');
			options.add(varOption);
			var varOption = new Option('其他姻亲关系','5');
			options.add(varOption);
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
			var varOption = new Option('配偶','1');
			options.add(varOption);
			var varOption = new Option('父母','2');
			options.add(varOption);
			var varOption = new Option('子女','3');
			options.add(varOption);
			var varOption = new Option('兄弟姐妹','9');
			options.add(varOption);
			var varOption = new Option('其他血缘关系','4');
			options.add(varOption);
			var varOption = new Option('其他姻亲关系','5');
			options.add(varOption);
	
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
	<body class="page_content" onload="doOnload();">
	<emp:form id="submitForm" action="updateCusIndivSocRelRecord.do" method="POST">
		<emp:gridLayout id="CusIndivSocRelGroup" maxColumn="2" title="个人社会关系">
			<emp:select id="CusIndivSocRel.indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="0" colSpan="2" readonly="true"/>
			<emp:text id="CusIndivSocRel.indiv_rl_cert_code" label="证件号码" required="true" readonly="true" colSpan="2"/>
			<emp:select id="CusIndivSocRel.indiv_family_flg" label="是否家庭成员" required="true" dictname="STD_ZX_YES_NO" defvalue="1" onchange="doChange();"/>
			<emp:select id="CusIndivSocRel.indiv_cus_rel" label="与客户关系" required="true" dictname="STD_ZB_INDIV_CUS"  />
            <emp:text id="CusIndivSocRel.cus_id_rel" label="关联客户码" required="true"  readonly="true"/>
			<emp:text id="CusIndivSocRel.indiv_rel_cus_name" label="姓名" required="true" maxlength="30" readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX"  readonly="true" />
			<emp:text id="CusIndivSocRel.indiv_rl_y_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusIndivSocRel.indiv_rel_job" label="职业" required="false" dictname="STD_ZX_EMPLOYMET" readonly="true" />
			<emp:text id="CusIndivSocRel.indiv_rel_com_name" label="单位名称" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:select id="CusIndivSocRel.indiv_rel_duty" label="职务" required="false" dictname="STD_ZX_DUTY" readonly="true" />
			<emp:textarea id="CusIndivSocRel.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)" />
			<emp:text id="CusIndivSocRel.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId" />
			<emp:text id="CusIndivSocRel.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo" />
			<emp:date id="CusIndivSocRel.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY" />
			<emp:text id="CusIndivSocRel.cus_id" label="客户码" required="true"  colSpan="2" hidden="true" />
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