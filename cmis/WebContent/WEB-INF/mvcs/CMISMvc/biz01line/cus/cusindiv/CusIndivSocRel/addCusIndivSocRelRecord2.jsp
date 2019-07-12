<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%
	String cert_type = request.getParameter("cert_type");
	String cert_code = request.getParameter("cert_code");
%>
<emp:page>
	<html>
	<head>
	<title>新增页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />

	<script type="text/javascript">


	function doOnLoad(){
		showCertTyp(CusIndivSocRel.indiv_rel_cert_typ, 'indiv');
		CusIndivSocRel.indiv_rl_cert_code._obj.addOneButton('uniquCheck','获取', setCusInfo);
		CusIndivSocRel.cus_id._setValue('${context.cusId}');
		CusIndivSocRel.indiv_family_flg._setValue('1');
		CusIndivSocRel.indiv_family_flg._obj._renderReadonly(true);
		CusIndivSocRel.indiv_cus_rel._setValue('1');	
		CusIndivSocRel.indiv_cus_rel._obj._renderReadonly(true);
	}
	
	function setCusInfo(){
		var certTyp = CusIndivSocRel.indiv_rel_cert_typ._obj.element.value;
		var certCode = CusIndivSocRel.indiv_rl_cert_code._obj.element.value;
	    if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
	        alert("证件类型或证件号码不能为空！");
	        return;
	    }
	    if(!checkCertCode())
	    	return;
		//调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名)
		getCusInfo('returnCus',certTyp,certCode,'setCusInfo');
	}
	
	function returnCus(cusObj){
		if(cusObj==null||cusObj=='undefined'){
			CusIndivSocRel.cus_id_rel._obj.element.value="";
			return;
		}
		var cert_code =cusObj.cert_code;
		CusIndivSocRel.indiv_rl_cert_code._setValue(cert_code);
	
		var cus_id_rel=cusObj.cus_id;
		if (cus_id_rel != null) {
			CusIndivSocRel.cus_id_rel._setValue(cus_id_rel);
		}
		//姓名
		var cus_name = cusObj.cus_name;
		if(cus_name!=null){
			CusIndivSocRel.indiv_rel_cus_name._obj.element.value=cus_name;
		}
		//性别
		var indiv_sex = cusObj.indiv_sex;
		if(cus_name!=null){
			CusIndivSocRel.indiv_sex._obj.element.value=indiv_sex;
		}
		//职业
		var indiv_occ = cusObj.indiv_occ;
		if(cus_name!=null){
			CusIndivSocRel.indiv_rel_job._obj.element.value=indiv_occ;
		}
		//年收入
		var indiv_ann_incm = cusObj.indiv_ann_incm;
		if(cus_name!=null){
			CusIndivSocRel.indiv_rl_y_incm._obj.element.value=indiv_ann_incm;
		}
		//职务
		var indiv_com_job_ttl = cusObj.indiv_com_job_ttl;
		if(indiv_com_job_ttl!=null){
			CusIndivSocRel.indiv_rel_duty._obj.element.value=indiv_com_job_ttl;
		}
		//联系电话
		var phone = cusObj.phone;
		if(phone!=null){
			CusIndivSocRel.indiv_rel_phn._obj.element.value=phone;
		}
		//单位名称
		var indiv_com_name = cusObj.indiv_com_name;
		if(indiv_com_name!=null){
			CusIndivSocRel.indiv_rel_com_name._obj.element.value=indiv_com_name;
		}
	}
	
	function doAddCusIndivSocRel(){
		var result = page.dataGroups.CusIndivSocRelGroup.checkAll();
		if(!result)
		{
			return;
		}
		if(!typAndCode()){
			CusIndivSocRel.indiv_rel_cert_typ._setValue("");
			CusIndivSocRel.indiv_rl_cert_code._setValue("");
			CusIndivSocRel.indiv_rel_cus_name._setValue("");
			CusIndivSocRel.indiv_sex._setValue("");
			CusIndivSocRel.indiv_rel_job._setValue("");
			CusIndivSocRel.indiv_rel_duty._setValue("");
			return;
		};
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
					if(flag=="新增成功"){
						alert("新增成功！");
						window.close();
					}
				}else {
					alert(flag);
					return;
				}
			};
			var handleFailure = function(o){
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		  }
	function goback(){
		var paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value;
		var stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	
	
	
	function typAndCode(){
		var cusTyp="<%=cert_type%>";
		var cusCode="<%=cert_code%>";
		var certtype = CusIndivSocRel.indiv_rel_cert_typ._obj.element.value;
		var certcode = CusIndivSocRel.indiv_rl_cert_code._obj.element.value;
		if(certtype==cusTyp&&certcode==cusCode){
			alert("证件类型和证件号码 不能和原客户完全一致！");
			return false;
		}else{
			 return true;
		}
	}
	
	function doReturn(){
		goback();
	}
	
	function checkCertCode(){
		var certType =CusIndivSocRel.indiv_rel_cert_typ._obj.element.value;
		var certCode = CusIndivSocRel.indiv_rl_cert_code._obj.element.value;
		if(certType=='0' || certType=='7'){//身份证或临时身份证时验证
			if(certCode!=null&& certCode!=""){
				var flg = CheckIdValue(certCode);
				if(flg){
					if(certCode.length=='15'){
						if(confirm("你输入的身份证号码是15位，点击 [确定] 系统自动转换成18位新身份证！")){
							CusIndivSocRel.indiv_rl_cert_code._obj.element.value=oldCardToNewCard(certCode);
						}else{
							CusIndivSocRel.indiv_rl_cert_code._obj.element.value="";
							return;
						}
					}
					return true;
				}else{
					CusIndivSocRel.indiv_rl_cert_code._obj.element.value="";
					return flg;
				}
			}
		}
		return true;
	};
	
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
			    for (var i = 16; i >= 0; i--)
			    {
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
			/*
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
			*/
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
	
	function cleanName(){//如果客户为男 配偶一定为女的 反之亦然
		var cusTyp="<%=cert_type%>";
		var cusCode="<%=cert_code%>";
		var cusCodeCurrent= CusIndivSocRel.indiv_rl_cert_code._getValue();
		var flag1 = cusCode.substring(16,17)%2;
		var flag2 = cusCodeCurrent.substring(16,17)%2;
		if(cusTyp=='10'){
			if(flag1=='1'&&flag2=='1'){
				alert("当前客户为男性，配偶不能为男性");
				CusIndivSocRel.indiv_rl_cert_code._setValue("");
				return false;
			}else if(flag1=='0'&&flag2=='0'){
				alert("当前客户为女性，配偶不能为女性");
				CusIndivSocRel.indiv_rl_cert_code._setValue("");
				return false;
			}
		}
	   CusIndivSocRel.indiv_rel_cus_name._setValue("");
	}

	function removeIndivCusRel(){
		var options = CusIndivSocRel.indiv_cus_rel._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '4' || options[i].value == '5' || options[i].value == '9' || options[i].value == '6'||options[i].value == '7'||options[i].value == '8'){
				options.remove(i);
			}
		}
	}
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">

	<emp:form id="submitForm" action="addCusIndivSocRelRecord.do"
		method="POST">

		<emp:gridLayout id="CusIndivSocRelGroup" title="个人社会关系" maxColumn="2">
			<emp:text id="CusIndivSocRel.cus_id" label="客户码" maxlength="30"
				required="true" hidden="false" readonly="true"/>
			<emp:select id="CusIndivSocRel.indiv_family_flg" label="是否家庭成员"
				required="true" dictname="STD_ZX_YES_NO" 
				onchange="doChange();" />
			<emp:select id="CusIndivSocRel.indiv_cus_rel" label="与客户关系"
				required="true" dictname="STD_ZB_INDIV_CUS"  />
			<emp:select id="CusIndivSocRel.indiv_rel_cert_typ" label="证件类型"
				 required="true" dictname="STD_ZB_CERT_TYP" onchange="cleanName()" />
			<emp:text id="CusIndivSocRel.indiv_rl_cert_code" label="证件号码"
				maxlength="20" required="true" onchange="cleanName()" />
             
			<emp:text id="CusIndivSocRel.indiv_rel_cus_name" label="姓名"
				maxlength="80" required="true"  readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_sex" label="性别" required="false"
				dictname="STD_ZX_SEX"  disabled="true" />

			<emp:text id="CusIndivSocRel.indiv_rl_y_incm" label="年收入（元）"
				maxlength="18" required="false" dataType="Currency"
				readonly="true" hidden="true"/>
			<emp:select id="CusIndivSocRel.indiv_rel_job" label="职业"
				required="false" dictname="STD_ZX_EMPLOYMET" disabled="true" />
			<emp:text id="CusIndivSocRel.indiv_rel_com_name" label="单位名称"
				maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:select id="CusIndivSocRel.indiv_rel_duty" label="职务"
				required="false" dictname="STD_ZX_DUTY" disabled="true" />
			<emp:select id="CusIndivSocRel.indiv_rel_ind" label="所在行业"
				required="false" dictname="STD_GB_4754-2011" hidden="true"/>
			<emp:text id="CusIndivSocRel.indiv_rel_phn" label="联系电话"
				maxlength="35" required="false" dataType="Phone" readonly="true" hidden="true"/>
			<emp:textarea id="CusIndivSocRel.remark" label="备注" maxlength="250"
				required="false" colSpan="2"
				onkeyup="this.value = this.value.substring(0, 250)" />
			<emp:text id="CusIndivSocRel.input_id" label="登记人" maxlength="20"
				required="false" hidden="true" defvalue="$currentUserId" />
			<emp:text id="CusIndivSocRel.input_br_id" label="登记机构" maxlength="20"
				required="false" hidden="true" defvalue="$organNo" />
			<emp:date id="CusIndivSocRel.input_date" label="登记日期"
				required="false" hidden="true" defvalue="$OPENDAY" />
			<emp:text id="CusIndivSocRel.last_upd_id" label="更新人" maxlength="20"
				required="false" hidden="true" />
			<emp:date id="CusIndivSocRel.last_upd_date" label="更新日期"
				required="false" hidden="true" />
			

			<emp:text id="CusIndivSocRel.cus_id_rel" label="关联客户码"
				maxlength="30" required="false" hidden="true" />
			<emp:text id="CusIndivSocRel.cus_name_rel" label="关联客户名称"
				maxlength="30" required="false" hidden="true" />

		</emp:gridLayout>

		<div align="center"><br>
			<emp:button id="addCusIndivSocRel" label="保存" op="add" /> 
		</div>
	</emp:form>

	</body>
	</html>
</emp:page>

