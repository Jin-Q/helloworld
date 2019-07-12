<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	function doOnload(){
		//去掉数据来源中，系统自动生成的。
		var options = CusBlkListTemp.data_source._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == '11'){
				options.remove(i);
			}
		}
		changeFromType();
	}
	
	//输入日期不能大于当前日期
	function CheckDate(obj,errMsg){
		var str_date=obj._getValue();
		var openDay = '${context.OPENDAY}';
		if( str_date==""|| str_date==null){
	        return;
		}
		if(str_date>openDay){
			alert(errMsg);
		    obj._obj.element.value="";
		}
	}
	
	function doReturn() {
		var url = '<emp:url action="queryCusBlkListTempList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function setconId(data){
		CusBlkListTemp.manager_id_displayname._setValue(data.actorname._getValue());
		CusBlkListTemp.manager_id._setValue(data.actorno._getValue());
		CusBlkListTemp.manager_br_id._setValue(data.orgid._getValue());
		CusBlkListTemp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusBlkListTemp.manager_br_id_displayname._obj._renderReadonly(true);	
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusBlkListTemp.manager_br_id._setValue(jsonstr.org);
					CusBlkListTemp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusBlkListTemp.manager_br_id._setValue("");
					CusBlkListTemp.manager_br_id_displayname._setValue("");
					CusBlkListTemp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBlkListTemp.manager_id._getValue();
					CusBlkListTemp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusBlkListTemp.manager_br_id._setValue("");
					CusBlkListTemp.manager_br_id_displayname._setValue("");
					CusBlkListTemp.manager_br_id_displayname._obj._renderReadonly(false);
					CusBlkListTemp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusBlkListTemp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		CusBlkListTemp.manager_br_id._setValue(data.organno._getValue());
		CusBlkListTemp.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function returnCus(data){
		CusBlkListTemp.cus_id._setValue(data.cus_id._getValue());
		CusBlkListTemp.cus_name._setValue(data.cus_name._getValue());
		CusBlkListTemp.cert_type._setValue(data.cert_type._getValue());
		CusBlkListTemp.cert_code._setValue(data.cert_code._getValue());
	}

	function doAddCusBlkList(){
		var form = document.getElementById("submitForm");
		if(CusBlkListTemp._checkAll()){
			CusBlkListTemp._toForm(form);
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
						var url = '<emp:url action="queryCusBlkListTempList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == "fail"){
						alert("该客户已经是共享客户，不能重复新增！");
					}else if(flag == "exists"){
						alert('该客户是系统内客户，请通过共享客户申请进行新增！');
					}else{
						alert("保存失败！");
					}
				}
			};
			var handleFailure = function(o){
				alert("保存失败！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var serno = CusBlkListTemp.serno._getValue();
			var url = '<emp:url action="addCusBlkListRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	}
	//根据数据来源显示不同信息
	function changeFromType(){
		var dataSource = CusBlkListTemp.data_source._getValue();//数据来源
		if(dataSource=='20'){//系统外
			CusBlkListTemp.cus_id._obj._renderHidden(true);
			CusBlkListTemp.cus_id._obj._renderRequired(false);
			CusBlkListTemp.cus_name._obj._renderReadonly(false);
			CusBlkListTemp.cert_type._obj._renderReadonly(false);
			CusBlkListTemp.cert_code._obj._renderReadonly(false);
		}else{
			CusBlkListTemp.cus_id._obj._renderHidden(false);
			CusBlkListTemp.cus_id._obj._renderRequired(true);
			CusBlkListTemp.cus_name._obj._renderReadonly(true);
			CusBlkListTemp.cert_type._obj._renderReadonly(true);
			CusBlkListTemp.cert_code._obj._renderReadonly(true);
		}
		CusBlkListTemp.cus_id._setValue('');
		CusBlkListTemp.cus_name._setValue('');
		CusBlkListTemp.cert_type._setValue('');
		CusBlkListTemp.cert_code._setValue('');
	}

	//校验证件号码
	function checkCertCode(){
		var certType =CusBlkListTemp.cert_type._getValue();
		var certCode =CusBlkListTemp.cert_code._getValue();
		if(certCode!=""&&certType=="a"){
			if(!CheckOrganFormat(certCode)){
				CusBlkListTemp.cert_code._setValue('');
			}
		}
		if(certCode!=""&&certType=="20"){
			if(!CheckCertCodeFormat(certCode)){
				CusBlkListTemp.cert_code._setValue('');
			}
		}
		if(certCode!=""&&(certType=='0'||certType=='7')){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if(certCode.length=='15'){
					if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
						CusBlkListTemp.cert_code._obj.element.value=oldCardToNewCard(certCode);
					}else{
						CusBlkListTemp.cert_code._setValue('');
					}
				}
			}else{
				CusBlkListTemp.cert_code._setValue('');
			}
		}
	};
	
	//校验统一社会信用代码输入是否正确
	function CheckCertCodeFormat(Code) { 
	　　var patrn = /^[0-9A-Z]+$/;
	 　	//18位校验及大写校验
	　　if ((Code.length != 18) || (patrn.test(Code) == false)) {
	　　　　 alert("不是有效的统一社会信用代码！"); 
			return false;
	　　} else { 
	　　　	var Ancode;//统一社会信用代码的每一个值
	 　　　	var Ancodevalue;//统一社会信用代码每一个值的权重 
	　　　　	var total = 0; 
	　　　　	var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子 
	　　　　	var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
	　　　　	//不用I、O、S、V、Z 
	　　　　	for (var i = 0; i < Code.length - 1; i++) {
	 　　　		Ancode = Code.substring(i, i + 1); 
	　　　　		Ancodevalue = str.indexOf(Ancode); 
	　　　　		total = total + Ancodevalue * weightedfactors[i];
	　　　　		//权重与加权因子相乘之和 
	　　　　	}
	 　　　 	var logiccheckcode = 31 - total % 31;
	　　　　	if (logiccheckcode == 31){
	　　　　　　logiccheckcode = 0;
	　　　　	}
	　　　　	var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
	　　　　	var Array_Str = Str.split(',');
	　　　　	logiccheckcode = Array_Str[logiccheckcode];
	　　　　 var checkcode = Code.substring(17, 18);
	　　　　 if (logiccheckcode != checkcode) {
	　　　　　　	alert("不是有效的统一社会信用代码！"); 
				return false;
	 　　　　}
	 		return true;
	 　　} 
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
	    for (var i = 16; i >= 0; i--) { 
            N += parseInt(cardID17.substring(i, i + 1)) * v[j]; 
            j++; 
	    } 
	    R = N % 11; 
	    T = vs.charAt(R); 
	    cardID18 = cardID17 + T; 
	    return cardID18;
	}
	
	function onReturnRegStateCode(date){
		CusBlkListTemp.legal_addr._obj.element.value=date.id;
    	CusBlkListTemp.legal_addr_displayname._obj.element.value=date.label;
	}

</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="addCusBlkListRecord.do" method="POST">
		
		<emp:gridLayout id="CusBlkListGroup" title="共享客户信息" maxColumn="2">
			<emp:select id="CusBlkListTemp.data_source" label="数据来源" required="true" dictname="STD_ZB_DATA_SOURCE" colSpan="2" onchange="changeFromType()" defvalue="20" readonly="true"/>
			<emp:text id="CusBlkListTemp.serno" label="登记流水号" maxlength="40" required="false" readonly="true" colSpan="2" hidden="true"/>	
			<emp:pop id="CusBlkListTemp.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=&returnMethod=returnCus" required="true"/>
			<emp:text id="CusBlkListTemp.cus_name" label="客户名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:select id="CusBlkListTemp.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP" onblur="checkCertCode()"/>
			<emp:text id="CusBlkListTemp.cert_code" label="证件号码" maxlength="20" readonly="true" required="true" onblur="checkCertCode()"/>
			<emp:select id="CusBlkListTemp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:select id="CusBlkListTemp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
			<emp:text id="CusBlkListTemp.legal_name" label="法定代表人" maxlength="30" required="false" />
			<emp:text id="CusBlkListTemp.legal_phone" label="联系电话" maxlength="35" required="false" dataType="Phone"/>
			<emp:text id="CusBlkListTemp.legal_addr" label="通讯地址" required="true" hidden="true"/>
			<emp:pop id="CusBlkListTemp.legal_addr_displayname" label="通讯地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" required="true" cssElementClass="emp_field_text_input2"/>	
			<emp:text id="CusBlkListTemp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>	
			<emp:date id="CusBlkListTemp.black_date" label="列入日期" required="false" colSpan="2" 
				onblur="CheckDate(CusBlkListTemp.black_date,'列入日期不能大于当前日期')" hidden="true"/>
			<emp:textarea id="CusBlkListTemp.black_reason" label="客户描述" maxlength="250" 
				required="true" colSpan="2" onblur="this.value = this.value.substring(0, 250)"/>
		</emp:gridLayout>
		<emp:gridLayout id="CusBlkListGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusBlkListTemp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"  required="true"/>
			<emp:pop id="CusBlkListTemp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />	
			<emp:text id="CusBlkListTemp.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="CusBlkListTemp.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName" readonly="true"/>
			<emp:date id="CusBlkListTemp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusBlkListTemp.manager_id" label="责任人" readonly="true" hidden="true"/>
			<emp:text id="CusBlkListTemp.manager_br_id" label="管理机构" readonly="true" hidden="true"/>
			<emp:text id="CusBlkListTemp.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusBlkListTemp.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:textarea id="CusBlkListTemp.logout_reason" label="注销原因" maxlength="250" colSpan="2" hidden="true"/>
			<emp:date id="CusBlkListTemp.logout_date" label="注销日期" required="false" hidden="true"/>
			<emp:select id="CusBlkListTemp.status" label="状态" readonly="true" dictname="STD_CUS_BLK_STATUS" defvalue="001" hidden="true"/>
			<emp:text id="CusBlkListTemp.logout_id" label="注销人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.logout_br_id" label="注销机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.appr_id" label="审批人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.appr_br_id" label="审批机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusBlkListTemp.source" label="来源（1-导入，2--新增）" maxlength="20" required="false" hidden="true" defvalue="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusBlkList" label="保存" op="add" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>