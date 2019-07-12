<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="java.net.URLDecoder"%><emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
</style>

<%
	String openTmp = request.getParameter("openTmp");
	String cusName = (String) request.getParameter("cusName");
	if(cusName!=null&&!"".equals(cusName)){//不宜贷款户开户时传入客户码
		cusName = URLDecoder.decode(cusName,"UTF-8");
	}
%>
<script type="text/javascript">

	function doOnLoad() {
		//CusBase.cert_code._obj.addOneButton('uniquCheck', '获 取', setCusInfo);
		var openTmp = '<%=openTmp%>';
		var cusName = '<%=cusName%>';
		if(openTmp!=null&&openTmp!='null'&&openTmp=='true'){
			CusBase.openType._setValue('02');
			CusBase.openType._obj._renderReadonly(true);
			CusBase.cert_code._obj._renderReadonly(true);
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
	};
	
	//通过证件号码证件类型从Ecif获取客户信息
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
			alert("获取信息失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//临时客户开户时将数据发送到Ecif进行临时户开户处理
	function sendCusMsg(){
	/**	EMPTools.mask();
		var handleSuccess = function(o){
			EMPTools.unmask();
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
					var flag=jsonstr.flag;
					if (flag=="success") {**/

						//临时客户开户时不去esb，开正式户时再去esb
						//var cusId = jsonstr.cusId;
						//CusBase.cus_id._setValue(cusId);
						var form = document.getElementById("submitForm");
						CusBase._toForm(form);
						CusCom._toForm(form);
						form.action="addCusBaseComRecord.do";
						toSubmitForm(form);

						/**				} else if(flag=="fail"){
						alert("开户失败，Ecif返回错误信息为：" + jsonstr.errorMsg);//提示ecif返回的错误信息
					} else{
						alert("开户失败！");
					}
				} catch(e) {
					alert("获取信息失败，请联系管理员！");
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
		var form = document.getElementById("submitForm");
		CusBase._toForm(form);
		page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
		form.action="SendCusMsg.do";
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action, callback, postData);
						**/
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
		//国籍
		var cus_country = cusObj.cusCountry;
		if (cus_country != null) {
			CusBase.cus_country._setValue(cus_country);
		}
	/*	//成立日期
		var creatDate = cusObj.creatDate;
		if (creatDate != null) {
			CusCom.com_str_date._setValue(creatDate);
		}
		//证件到期日
		var expDt = cusObj.expiryDate;
		if (expDt != null) {
			CusCom.com_ins_exp_date._setValue(expDt);
		}*/
		//客户类型
		var cusType = cusObj.cusType;
		if (cusType != null) {
			CusBase.cus_type._setValue(cusType);
		}
		//客户类型中文
		var cusTypeName = cusObj.cusType_displayname;
		if (cusTypeName != null) {
			CusBase.cus_type_displayname._setValue(cusTypeName);
		}
		//查询成功后将证件类型证件号码置为只读
		CusBase.cert_type._obj._renderReadonly(true);
		CusBase.cert_type._setValue(CusBase.cert_type._getValue());
		CusBase.cert_code._obj._renderReadonly(true);
	}

	//点击确定进入下一步
	function doAddCusBase() {
		var openType = CusBase.openType._getValue();
		if(!CusBase._checkAll()||!CusCom._checkAll()){
			return false;
		}
		if(checkCertCode()){
			if(openType=='01'){//正式客户
				var form = document.getElementById("submitForm");
				CusBase._toForm(form);
				CusCom._toForm(form);
				toSubmitForm(form);
			}else{	//临时客户
				sendCusMsg();
			}
		}
	};

	function toSubmitForm(form){
		var openTmp = '<%=openTmp%>';
	  	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				if(jsonstr.tradeRefuseInf!=null && jsonstr.tradeRefuseInf!=''){
                     alert(jsonstr.tradeRefuseInf);
                     return;
				}
				var flag = jsonstr.flag;
				var cus_id = jsonstr.cus_id;
				var mainBrId = jsonstr.mainBrId;
				var mgrId = jsonstr.custMgr;
				var mainBrName = jsonstr.mainBrName;
				var mgrName = jsonstr.custMgrName;
				var cusName = jsonstr.cusName;
				var certtype =jsonstr.certtype;
				if(flag == "exist"){
					alert("该客户已经在【"+mainBrName+"】机构开户，客户经理【"+mgrName+"】，请查实后再开户！");
					return;
				}else if(flag=="newFormal"){
					var url = '<emp:url action="getCusComTree.do"/>&cus_id='+cus_id+'&oper=update&flag=edit&openTmp='+openTmp+"&btnflag=temp";
					  	url = EMPTools.encodeURI(url);
					  	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
					  	if(openTmp!="true"){
							goback();
						}
				}else if(flag=="newTemp"){
					  	var url = '<emp:url action="getCusComTree.do"/>&cus_id='+cus_id+'&oper=update&flag=edit&openTmp='+openTmp+"&btnflag=temp";
					  	url = EMPTools.encodeURI(url);
					  	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
					  	if(openTmp!="true"){
							goback();
						}
				}else if(flag=="updateFormal"){
			    	if(confirm("该客户已存在！点【确定】将继续维护客户信息！")){
			    		var url = '<emp:url action="getCusComTree.do"/>&cus_id='+cus_id+"&oper=update&flag=query&openTmp="+openTmp;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
						goback();
					}else{
						goback();
					}
				}else if(flag=="updateTemp"){
			    	if(confirm("该客户已存在！点【确定】将继续维护客户信息！")){
						var paramStr="oper=update&flag=query"+'&cus_id='+cus_id+'&openTmp='+openTmp;
						var url = '<emp:url action="getCusComTree.do"/>&'+paramStr;
					  	url = EMPTools.encodeURI(url);
					  	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
					 	goback();
					}else{
			    		goback();
					}
			    }else if(flag=="error"){
				    alert("数据出错！");
				    goback();
				}else if(flag=="Black"){
			    	 alert("证件类型和证件号码对应的客户为我行共享客户，不能开户！");
					 return;
				}else{
					alert("开户失败！原因:"+flag);
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
	}
	
	function goback(){
		var gobackURL = '<emp:url action="queryCusComList.do"/>';
		gobackURL = EMPTools.encodeURI(gobackURL);
		window.location = gobackURL;
	};
	
	function setCusShortName(){
		var cus_name = CusBase.cus_name._obj.element.value;
		
		if(cus_name.length<=23){
			CusBase.cus_short_name._obj.element.value=cus_name;
			CusBase.cus_short_name._obj.config.readonly=true;
			CusBase.cus_short_name._obj._renderStatus();	
		}else{
			CusBase.cus_short_name._obj.element.value="";
			CusBase.cus_short_name._obj.config.readonly=false;
			CusBase.cus_short_name._obj._renderStatus();	
		}
	};
	
	function WidthCheck(name){
		var s= name._obj.element.value;
		var n=146;
		var w = 0;
		for (var i=0; i<s.length; i++) {
		   var c = s.charCodeAt(i);
		   //单字节加1
		   if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
		    w++;
		   }else {
		    w+=2;
		   }
		}
		if (w > n) {
		   CusBase.cus_short_name._obj.element.value="";
		   CusBase.cus_short_name._obj.config.readonly=false;
		   CusBase.cus_short_name._obj._renderStatus();	
		}else{
			CusBase.cus_short_name._obj.element.value=s;
			CusBase.cus_short_name._obj.config.readonly=true;
			CusBase.cus_short_name._obj._renderStatus();	
		}
		var cusName = CusBase.cus_short_name._getValue();
		for (var i=0; i<s.length; i++) {
			cusName = cusName.replace(' ','');
		}
		CusBase.cus_short_name._setValue(cusName);
		CusBase.cus_name._setValue(cusName);
	}

	/*--user code begin--*/
	function doReturn(){
		goback();
	}

	//校验组织机构代码
	/* function checkCertCode(){
		var cert_type =CusBase.cert_type._obj.element.value;
		var certCode =CusBase.cert_code._obj.element.value;
		if(certCode!="" && cert_type=='a'){
			if(CheckOrganFormat(certCode)){
	         	return true;
			}else{
				CusBase.cert_code._obj.element.value="";
				 return false;
			}
		}else{
			return true;
		}
	}; */
	//统一社会信用代码
	function checkCertCode(){
		var cert_type =CusBase.cert_type._obj.element.value;
		var certCode =CusBase.cert_code._obj.element.value;
		if(certCode!="" && cert_type=='20'){
			if(CheckCertCodeFormat(certCode)){
	         	return true;
			}else{
				CusBase.cert_code._obj.element.value="";
				return false;
			}
		}else{
			return true;
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
	
	function changeType(){
		var openType = CusBase.openType._getValue();//01 正式客户  02 临时客户
	//	var uniquCheck = document.getElementById("uniquCheck");
		if(openType == "01"){
			CusBase.cus_name._obj._renderReadonly(true);
	//		uniquCheck.disabled = '';
			CusBase.cus_status._setValue("00");
			CusBase.cus_country._obj._renderReadonly(true);
			CusBase.cus_type_displayname._obj._renderReadonly(true);
		}else{
			CusBase.cus_name._obj._renderReadonly(false);
	//		uniquCheck.disabled = 'disabled';
			CusBase.cus_status._setValue("04");
			CusBase.cus_country._obj._renderReadonly(false);
			CusBase.cus_type_displayname._obj._renderReadonly(false);
		}
		CusBase.cus_name._setValue('');
		CusBase.cus_type._setValue('');
		CusBase.cus_type_displayname._setValue('');
		//CusBase.cus_country._setValue('');
		CusBase.cust_mgr._setValue('');
		CusBase.cust_mgr_displayname._setValue('');
	}

	//选择客户类型
	function onReturnCusType(date){
		if(date.id=="Z1"||date.id=="Z2"||date.id=="Z3"){
			CusBase.cus_type._obj.element.value="";
			CusBase.cus_type_displayname._obj.element.value="";
			alert("对公客户开户不能选择个人客户类型，请重新选择！");
		}else{
			CusBase.cus_type._obj.element.value=date.id;
			CusBase.cus_type_displayname._obj.element.value=date.label;
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
	/*--user code end--*/
</script>
</head>

<body class="page_content" onload="doOnLoad()">

	<emp:form id="submitForm" action="addCusBaseComRecord.do" method="POST">
		<div align="center" id="openDiv">
		<emp:gridLayout id="CusBaseGroup" title="对公客户开户页面" maxColumn="1">
			<emp:select id="CusBase.cus_status" label="客户状态" required="false" dictname="STD_ZB_CUS_STATUS" defvalue="00" readonly="true" hidden="true"/>
			<emp:text id="CusBase.cus_short_name" label="客户简称"  required="false" hidden="true" />
			<emp:select id="CusBase.openType" label="开户类型" required="true" defvalue="01" dictname="STD_ADDCUS_TYPE" readonly="true" onchange="changeType()"/>	
			<emp:select id="CusBase.cert_type" label="证件类型" required="true" defvalue="20" readonly="true" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="CusBase.cert_code" label="证件号码" onchange="checkCertCode();" maxlength="20" required="true"/>
			<emp:text id="CusBase.cus_id" label="客户码" maxlength="30" required="false" hidden="true" readonly="true"/>
		    <emp:text id="CusBase.cus_name" label="客户名称" required="true" />
			<emp:text id="CusBase.cus_type" label="客户类型" required="true" hidden="true"/>
			<emp:pop id="CusBase.cus_type_displayname" label="客户类型" required="true" url="showDicTree.do?dicTreeTypeId=STD_ZB_CUS_COM_TYPE" returnMethod="onReturnCusType" colSpan="2" />
			<emp:select id="CusBase.cus_country" label="国别" dictname="STD_GB_2659-2000" defvalue="CHN" required="false" readonly="true"/>
			<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理"  required="true" defvalue="${context.loginusername}" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" defvalue="$organName"/>
			
			<emp:date id="CusCom.com_ins_reg_date" label="发证日期" required="false" hidden="true"/>
			<emp:date id="CusCom.com_ins_exp_date" label="证件到期日" required="false" hidden="true"/>
			<emp:date id="CusCom.com_str_date" label="成立日期" required="false" hidden="true"/>
			
			<emp:select id="CusBase.belg_line" label="所属条线" dictname="STD_ZB_BUSILINE" required="true" hidden="true" defvalue="BL100"/>
			<emp:text id="CusBase.cust_mgr" label="主管客户经理" defvalue="$currentUserId" required="true" hidden="true"/>
			<emp:text id="CusBase.main_br_id" label="主管机构"  required="true" defvalue="$organNo" hidden="true"/>
			<emp:text id="CusBase.input_id" label="登记人"  required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusBase.input_br_id" label="登记机构"  required="true" defvalue="$organNo" hidden="true"/>
			<emp:date id="CusBase.input_date" label="登记日期"  required="true" defvalue="$OPENDAY" hidden="true"/>
		</emp:gridLayout></div>
	  	 <div align="center" id="01"><br>
   		<emp:button id="addCusBase" label="确定" />&nbsp;&nbsp;</div>
	</emp:form>
	</body>
</html>
</emp:page>

