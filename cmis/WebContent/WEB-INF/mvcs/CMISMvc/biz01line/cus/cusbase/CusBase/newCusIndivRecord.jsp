<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="java.net.URLDecoder"%><emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true" />
<style type="text/css">
</style>
<link rel="stylesheet" href="<emp:file fileName="styles/cmisstyles/inputstyles.css"/>" type="text/css" />
<%
	String openTmp = request.getParameter("openTmp");
	String cusName = (String) request.getParameter("cusName");
	if(cusName!=null&&!"".equals(cusName)){//不宜贷款户开户时传入客户码
		cusName = URLDecoder.decode(cusName,"UTF-8");
	}
%>
	<script type="text/javascript">

	/*--user code begin--*/
	function checkCertCode(){
		var certType = CusBase.cert_type._obj.element.value;
		var certCode = CusBase.cert_code._obj.element.value;
		if(certType=='0' || certType=='7'){//身份证或临时身份证时验证
			if(certCode!=null&& certCode!=""){
				var flg = CheckIdValue(certCode);
				if(flg){
					if(certCode.length=='15'){
						CusBase.cert_code._obj.element.value=oldCardToNewCard(certCode);
						if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
							CusBase.cert_code._obj.element.value=oldCardToNewCard(certCode);
						}else{
							CusBase.cert_code._obj.element.value="";
						}
					}
					return true;
				}else{
					CusBase.cert_code._obj.element.value="";
					CusBase.cert_code._obj.element.focus();
					return flg;
				}
			}
		}
		return true;
	};

	function doOnLoad(){
		EMPTools.addEvent(CusBase.cert_code._obj.element, "blur", checkCertCode);
		//CusBase.cert_code._obj.addOneButton('uniquCheck', '获 取', setCusInfo);
		var openTmp = '<%=openTmp%>';
		var cusName = '<%=cusName%>';
	
		if(openTmp!=null&&openTmp!='null'&&openTmp=='true'){
			CusBase.openType._setValue('02');
			CusBase.openType._obj._renderReadonly(true);
			CusBase.cert_code._obj._renderReadonly(true);
			CusBase.cert_type._obj._renderReadonly(true);
			changeType();
			//开临时户设置主管客户经理、主管机构为非必输
			CusBase.main_br_id._setValue('');
			CusBase.main_br_id._obj._renderRequired(false);
			CusBase.main_br_id_displayname._setValue('');
			CusBase.main_br_id_displayname._obj._renderRequired(false);
			CusBase.cust_mgr._obj._renderRequired(false);
			CusBase.cust_mgr_displayname._obj._renderRequired(false);
		}

		if(cusName!=null&&cusName!=''&&cusName!='null'){
			CusBase.cus_name._setValue(cusName);
			CusBase.cus_name._obj._renderReadonly(true);
		}
	
		//隐藏对公证件类型
		var options = CusBase.cert_type._obj.element.options;
		for( var i = options.length - 1; i >= 0; i--){
			if(options[i].value=='a'||options[i].value=='b'||options[i].value=='c'||options[i].value=='X'){
				options.remove(i);
			}
		}
    	//隐藏对公客户类型
    	var options = CusBase.cus_type._obj.element.options;
    	for( var i = options.length - 1; i >= 0; i--){
	   	    if(options[i].value=='Z'||options[i].value=='Z1'||options[i].value=='Z2'||options[i].value=='Z3'||options[i].value==''){
		   	    
	   	    }else{
				options.remove(i);
			}
    	}
	}

	//获取从Ecif获取信息
	function setCusInfo(){
		var certType = CusBase.cert_type._getValue();
		var certCode = CusBase.cert_code._getValue();
		if(certType==null||certType==""||certCode==null||certCode==""){
			alert("请检查证件类型证件号码是否填写完整！");
			return;
		}
		var url = '<emp:url action="GetCusFromEcif.do"/>&certType='+certType+"&certCode="+certCode;
		url = EMPTools.encodeURI(url);
		EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
						if(cusObj.flag=="success"){
							returnCus(cusObj);
						}else if(cusObj.flag=="fail"){
							alert("获取失败，Ecif返回错误信息为："+cusObj.errorMsg);
						}else{
							alert("获取失败！");
						}
					} else {
						alert("获取失败！");
					}
				} catch(e) {
					alert("获取客户信息失败，请联系管理员！");
					return;
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("获取客户信息失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	
	//将查到的信息返写到页面上
	function returnCus(cusObj){
		//客户码
		var cus_id = cusObj.cusId;
		if (cus_id != null) {
			CusBase.cus_id._setValue(cus_id);
		}
		//客户名称
		var cus_name = cusObj.cusName;
		if (cus_name != null) {
			CusBase.cus_name._setValue(cus_name);
		}
		//性别
		var indiv_sex = cusObj.sex;
		if (indiv_sex != null) {
			CusIndiv.indiv_sex._setValue(indiv_sex);
			CusBase.indiv_sex._setValue(indiv_sex);
		}
		//出生日期
		var birth_dt = cusObj.birthDay;
		if (birth_dt != null) {
			CusIndiv.indiv_dt_of_birth._setValue(birth_dt);
		}
		//国籍
		var cus_country = cusObj.cusCountry;
		if (cus_country != null) {
			CusBase.cus_country._setValue(cus_country);
		}
		//证件到期日
		var indivIdExpDt = cusObj.indivIdExpDt;
		if (indivIdExpDt != null) {
			CusIndiv.indiv_id_exp_dt._setValue(indivIdExpDt);
		}
		//客户类型
		var cusType = cusObj.cusType;
		if (cusType != null) {
			CusBase.cus_type._setValue(cusType);
		}
		//查询成功后将证件类型证件号码置为只读
		CusBase.cert_type._obj._renderReadonly(true);
		CusBase.cert_type._setValue(CusBase.cert_type._getValue());
		CusBase.cert_code._obj._renderReadonly(true);
	}
	
	function doAddCusBase() {
		var openType = CusBase.openType._getValue();
		if(!CusIndiv._checkAll()||!CusBase._checkAll()){
			return false;
		}
		if(checkCertCode()){
			if(openType=='01'){//正式客户
				var form = document.getElementById("submitForm");
				CusBase._toForm(form);
				CusIndiv._toForm(form);
				toSubmitForm(form);
			}else{
				sendCusMsg();
			}
		}
	};

	//临时客户开户时将数据发送到Ecif进行临时户开户处理//临时客户不去esb，
	function sendCusMsg(){ 
						// 2019-03-14 chenBQ jsonstr对象不存在 
						//var cusId = jsonstr.cusId;
						//CusBase.cus_id._setValue(cusId);
						var form = document.getElementById("submitForm");
						CusBase._toForm(form);
						CusIndiv._toForm(form);
						form.action="addCusBaseIndivRecord.do";
						toSubmitForm(form);
						 
	}
	
	function toSubmitForm(form){
		var openTmp = '<%=openTmp%>';
	  	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error22222!"+e);
					return;
				}
				if(jsonstr.tradeRefuseInf!=null && jsonstr.tradeRefuseInf!=''){
                    alert(jsonstr.tradeRefuseInf);
                    return;
				}
				var flag = jsonstr.flag;
				var cus_id = jsonstr.cus_id;
				var mainBrName = jsonstr.mainBrName;
				var mgrName = jsonstr.custMgrName;
				var certType = jsonstr.certType;
				var certCode = jsonstr.certCode;
				var cusName = jsonstr.cusName;
				var mainBrId = jsonstr.mainBrId;
				//alert(flag);
				if(flag == "exist"){
					alert("该客户已经在【"+mainBrName+"】机构开户，客户经理【"+mgrName+"】，请查实后再开户！");
					return;
				}else if(flag=="newFormal"){
					var paramStr="cus_id="+cus_id+"&flag=edit&openTmp="+openTmp+"&btnflag=temp";
					var url = '<emp:url action="getCusIndivTree.do"/>&'+paramStr;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
					if(openTmp!="true"){
						goback();
					}
				}else if(flag=="newTemp"){
					var paramStr="cus_id="+cus_id+"&flag=edit&openTmp="+openTmp+"&btnflag=temp";
					var url = '<emp:url action="getCusIndivTree.do"/>&'+paramStr;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
					if(openTmp!="true"){
						goback();
					}
			    }else if(flag=="updateFormal"){
			    	if(confirm("该客户已存在！点【确定】将继续维护客户信息！")){
			    		var flag = 'query';
			    		var url = '<emp:url action="getCusIndivTree.do"/>&cus_id='+cus_id+'&flag='+flag+'&openTmp='+openTmp;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
						goback();
					}else{
						goback();
					}
				}else if(flag=="updateTemp"){
					if(confirm("该客户已存在！点【确定】将继续维护客户信息！")){
						var paramStr="cus_id="+cus_id+"&flag=query&openTmp="+openTmp;
						var url = '<emp:url action="getCusIndivTree.do"/>&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
						goback();
					}else{
			    		goback();
					}
				}else if(flag=="Black"){
			    	 alert("证件类型和证件号码对应的客户为我行共享客户，不能开户！");
					 return;
                }else {
                    alert("开户失败！原因:"+flag);
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
	
	//返回列表页面
	function goback(){
		var gobackURL = '<emp:url action="queryCusIndivList.do"/>';
		gobackURL = EMPTools.encodeURI(gobackURL);
		window.location = gobackURL;
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
	    for (var i = 16; i >= 0; i--) { 
            N += parseInt(cardID17.substring(i, i + 1)) * v[j]; 
            j++; 
	    } 
	    R = N % 11; 
	    T = vs.charAt(R); 
	    cardID18 = cardID17 + T; 
	    return cardID18;
	}
	
	function replaceSpace(){
		var s = CusBase.cus_name._getValue();
		for (var i=s.length; i>0; i--) {
			s = s.replace(' ','');
		}
		CusBase.cus_name._setValue(s);
	}

	/*--user code end--*/
	function changeType(){
		var openType = CusBase.openType._getValue();//01 正式客户  02 临时客户
	//	var uniquCheck = document.getElementById("uniquCheck");
		if(openType == "01"){
			CusBase.cus_name._obj._renderReadonly(true);
	//		uniquCheck.disabled = '';
			CusBase.cus_status._setValue("00");
			CusBase.cus_type._obj._renderReadonly(true);
			CusBase.cus_country._obj._renderReadonly(true);
			CusIndiv.indiv_sex._obj._renderReadonly(true);
		}else{
			CusBase.cus_name._obj._renderReadonly(false);
		//	uniquCheck.disabled = 'disabled';
			CusBase.cus_status._setValue("04");
			CusBase.cus_type._obj._renderReadonly(false);
			CusBase.cus_country._obj._renderReadonly(false);
			CusIndiv.indiv_sex._obj._renderReadonly(false);
	//		CusBase.cert_type._obj._renderReadonly(false);
	//		CusBase.cert_code._obj._renderReadonly(false);
		}
		CusBase.cus_name._setValue('');
		CusIndiv.indiv_sex._setValue('');
		CusBase.cus_type._setValue('');
		CusBase.cust_mgr._setValue('');
		CusBase.cust_mgr_displayname._setValue('');
	}

	function setBaseSex(){
		var sex = CusIndiv.indiv_sex._getValue();
		if(sex!=null&&sex!=''){
			CusBase.indiv_sex._setValue(sex);
		}
	}
	//主管机构
	function getOrgID(data){
		CusBase.main_br_id._setValue(data.organno._getValue());
		CusBase.main_br_id_displayname._setValue(data.organname._getValue());
	}
	//主管客户经理
	function setconId(data){
		CusBase.cust_mgr._setValue(data.actorno._getValue());
		CusBase.cust_mgr_displayname._setValue(data.actorname._getValue());
		CusBase.main_br_id._setValue(data.orgid._getValue());
		CusBase.main_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusBase.main_br_id_displayname._obj._renderReadonly(true);
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
					CusBase.main_br_id._setValue(jsonstr.org);
					CusBase.main_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					CusBase.main_br_id._setValue("");
					CusBase.main_br_id_displayname._setValue("");
					CusBase.main_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBase.cust_mgr._getValue();
					CusBase.main_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusBase.main_br_id._setValue("");
					CusBase.main_br_id_displayname._setValue("");
					CusBase.main_br_id_displayname._obj._renderReadonly(false);
					CusBase.main_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusBase.cust_mgr._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
</script>
</head>

	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="addCusBaseIndivRecord.do" method="POST" onsubmit="checkCertCode()">
		<div align="center" id="openDiv">
			<emp:gridLayout id="CusBaseGroup" title="对私客户开户页面" maxColumn="1">
				<emp:select id="CusBase.cus_status" label="客户状态" required="true" defvalue="00" hidden="true" readonly="true" dictname="STD_ZB_CUS_STATUS" />
				<emp:select id="CusBase.openType" label="开户类型" defvalue="01" dictname="STD_ADDCUS_TYPE" onchange="changeType()" required="true" readonly="true"/>
				<emp:select id="CusBase.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" readonly="false" />
				<emp:text id="CusBase.cert_code" label="证件号码" maxlength="20" required="true" />
				<emp:text id="CusBase.cus_id" label="客户码" required="false" readonly="true" hidden="true" />
				<emp:text id="CusBase.cus_name" label="客户名称" maxlength="40" required="true" onblur="replaceSpace()" />
				<emp:select id="CusBase.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" />
				<emp:select id="CusBase.cus_country" label="国籍" dictname="STD_GB_2659-2000" defvalue="CHN" required="true" readonly="true" />
				<emp:select id="CusIndiv.indiv_sex" label="性别" required="true" dictname="STD_ZX_SEX" onchange="setBaseSex()" />
				<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" required="true" defvalue="${context.loginusername}" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
				<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" defvalue="$organName"/>
				<emp:date id="CusIndiv.indiv_dt_of_birth" label="出生日期" required="false" hidden="true" readonly="true" />
				<emp:text id="CusIndiv.indiv_id_exp_dt" label="证件到期日" maxlength="10" required="false" hidden="true" />
				<emp:select id="CusBase.indiv_sex" label="性别" required="true" dictname="STD_ZX_SEX" hidden="true" readonly="true" />
				<emp:select id="CusBase.belg_line" label="所属条线" dictname="STD_ZB_BUSILINE" required="true" hidden="true" defvalue="BL300" />
				<emp:text id="CusBase.cust_mgr" label="主管客户经理" defvalue="$currentUserId" required="true" hidden="true" />
				<emp:text id="CusBase.main_br_id" label="主管机构" required="true" defvalue="$organNo" hidden="true" />
				<emp:text id="CusBase.input_id" label="登记人" required="true" defvalue="$currentUserId" hidden="true" />
				<emp:text id="CusBase.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
				<emp:date id="CusBase.input_date" label="登记日期" required="true" defvalue="$OPENDAY" hidden="true" />
			</emp:gridLayout></div>
		<div align="center"><br>
		<emp:button id="addCusBase" label="确定" /></div>
	</emp:form>
	</body>
	</html>
</emp:page>