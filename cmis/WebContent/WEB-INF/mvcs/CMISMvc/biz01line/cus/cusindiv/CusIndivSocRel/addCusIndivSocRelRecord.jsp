<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<script type="text/javascript">
	function doOnLoad(){
		CusIndivSocRel.indiv_rl_cert_code._obj.addOneButton('uniquCheck','获 取', setCusInfo);
		//移除对公证件类型
		var options = CusIndivSocRel.indiv_rel_cert_typ._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
	   	    if(options[i].value=='a'||options[i].value=='b'||options[i].value=='c'||options[i].value=='X'){
	  			options.remove(i);
	   	    }
	    }
		removeIndivCusRel();
	}

	function setCusInfo(){
		var certTyp = CusIndivSocRel.indiv_rel_cert_typ._obj.element.value;
		var certCode = CusIndivSocRel.indiv_rl_cert_code._obj.element.value;
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
            alert("证件类型或证件号码不能为空！");
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
	
	function returnCus(cusObj){
		if(cusObj==null||cusObj=='undefined'){
			CusIndivSocRel.cus_id_rel._obj.element.value="";
			return;
		}
		//姓名
		var cus_name = cusObj.cus_name;
		if(cus_name!=null){
			CusIndivSocRel.indiv_rel_cus_name._setValue(cus_name);
		}
		//关联客户id
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusIndivSocRel.cus_id_rel._setValue(cus_id_rel);
		}

		getCusDetails();
	}
	
	function returnCusInd(cusObj){
		//性别
		var indiv_sex = cusObj.indiv_sex;
		if(indiv_sex!=null){
			CusIndivSocRel.indiv_sex._setValue(indiv_sex);
		}
		//职业
		var indiv_occ = cusObj.indiv_occ;
		if(indiv_occ!=null){
			CusIndivSocRel.indiv_rel_job._setValue(indiv_occ);
		}
		//年收入
		var indiv_ann_incm = cusObj.indiv_ann_incm;
		if(indiv_ann_incm!=null){
			CusIndivSocRel.indiv_rl_y_incm._setValue(indiv_ann_incm);
		}
		//职务
		var indiv_com_job_ttl = cusObj.indiv_com_job_ttl;
		if(indiv_com_job_ttl!=null){
			CusIndivSocRel.indiv_rel_duty._setValue(indiv_com_job_ttl);
		}
		//联系电话
		var phone = cusObj.phone;
		if(phone!=null){
			CusIndivSocRel.indiv_rel_phn._setValue(phone);
		}
		//单位名称
		var indiv_com_name = cusObj.indiv_com_name;
		if(indiv_com_name!=null){
			CusIndivSocRel.indiv_rel_com_name._setValue(indiv_com_name);
		}
	}

	function doAddCusIndivSocRel(){
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
					if(CusIndivSocRel.indiv_cus_rel._getValue() == '1'){
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
					}else{
						var form = document.getElementById("submitForm");
						var result = CusIndivSocRel._checkAll();
						if(result){
							CusIndivSocRel._toForm(form)
							toSubmitForm(form);
						}
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
			alert("客户的关联客户不能为自己！");
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
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusIndivSocRelAddPage.do"/>&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
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
		var paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	function doReturn(){
		goback();
	}
	
	function oldCardToNewCard(certCode){
		var vs = "10X98765432";
		var v = new Array();
		v.push(2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7);
		var cardID17 = certCode.substring(0,6)+"19"+certCode.substring(6);
		var N = 0;
		var R = -1;
		var T = '0';//储存最后一个数字
		var j = 0;
		var cardID18="";
		//计数出第18位数字
		for (var i = 16; i >= 0; i--){
	        N += parseInt(cardID17.substring(i, i + 1)) * v[j];
	        j++;
		}
		R = N % 11;
		T = vs.charAt(R);
		cardID18 = cardID17 + T;
		return cardID18;
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
			var varOption = new Option('兄弟姐妹','9');
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
	
	function removeIndivCusRel(){
		var options = CusIndivSocRel.indiv_cus_rel._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '4' || options[i].value == '5' ){
				options.remove(i);
			}
		}
	}

	function getCusDetails() {
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
						returnCusInd(cusObj);
					} else {
						alert("记录为空！");
					}
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
			}
		}
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var cus_id_rel = CusIndivSocRel.cus_id_rel._getValue();
		var url = '<emp:url action="getCusIndiv4manager.do"/>?cus_id_rel='+cus_id_rel;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="addCusIndivSocRel.do" method="POST">
		<emp:gridLayout id="CusIndivSocRelGroup" title="个人社会关系" maxColumn="2">
			<emp:select id="CusIndivSocRel.indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="10" colSpan="2"/>
			<emp:text id="CusIndivSocRel.indiv_rl_cert_code" label="证件号码" required="true" colSpan="2"/>
			<emp:select id="CusIndivSocRel.indiv_family_flg" label="是否家庭成员" required="true" dictname="STD_ZX_YES_NO"  onchange="doChange();" />
			<emp:select id="CusIndivSocRel.indiv_cus_rel" label="与客户关系" required="true" dictname="STD_ZB_INDIV_CUS"  />
			<emp:text id="CusIndivSocRel.cus_id_rel" label="关联客户码" required="true" maxlength="30" readonly="true"/>
			<emp:text id="CusIndivSocRel.indiv_rel_cus_name" label="姓名" maxlength="80" required="true"  readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX"  readonly="true" />
			<emp:text id="CusIndivSocRel.indiv_rl_y_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency" readonly="true" hidden="false" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusIndivSocRel.indiv_rel_job" label="职业" required="false" dictname="STD_ZX_EMPLOYMET" readonly="true" />
			<emp:text id="CusIndivSocRel.indiv_rel_com_name" label="单位名称" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:select id="CusIndivSocRel.indiv_rel_duty" label="职务" required="false" dictname="STD_ZX_DUTY" readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_rel_ind" label="所在行业" required="false" dictname="STD_GB_4754-2011" hidden="true"/>
			<emp:text id="CusIndivSocRel.indiv_rel_phn" label="联系电话" maxlength="35" required="false" dataType="Phone" readonly="true" hidden="true"/>
			<emp:textarea id="CusIndivSocRel.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)" />
			<emp:text id="CusIndivSocRel.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId" />
			<emp:text id="CusIndivSocRel.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo" />
			<emp:date id="CusIndivSocRel.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY" />
			<emp:text id="CusIndivSocRel.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
			<emp:date id="CusIndivSocRel.last_upd_date" label="更新日期" required="false" hidden="true" />
			<emp:text id="CusIndivSocRel.cus_id" label="客户码" maxlength="30" required="false" hidden="true" />
			<emp:text id="CusIndivSocRel.cus_name_rel" label="关联客户名称" maxlength="30" required="false" hidden="true" />
		</emp:gridLayout>
		<div align="center"><br>
			<emp:button id="addCusIndivSocRel" label="保存"/> 
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	</body>
	</html>
</emp:page>