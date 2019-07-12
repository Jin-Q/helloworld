<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String dutys = "";
	if(context.containsKey("dutys")){
		dutys = (String)context.getDataValue("dutys");
	} 
	String organNo = "";
	if(context.containsKey("organNo")){
		organNo = (String)context.getDataValue("organNo");
	}
	String flagHOrg = "";
	if(dutys.contains("S0240") || dutys.contains("S0241") || dutys.contains("D0029") || dutys.contains("S0002") || "9350500009".equals(organNo) || "9350500012".equals(organNo)){
		flagHOrg = "1";
	}
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<script src="<emp:file fileName='scripts/grt/pub/grtAddPageUtil.js'/>"
        type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
			
	function doReturn() {
		var url = '<emp:url action="queryCusBlkCheckinappList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
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
	
    function setCusInfo(){
        var certTyp = CusBlkCheckinapp.cert_type._obj.element.value;
        var certCode = CusBlkCheckinapp.cert_code._obj.element.value;
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
            alert("证件类型或证件号码不能为空！");
            return ;
        }
         //证件号码校验
        if(!checkCertCode())return ;  
        
        //调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名) 
        getCusInfo('returnCus',certTyp,certCode,'setCusInfo');
    }
    
    //通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column ，当证件类型为对私时cusObj存放CusIndiv模型，反之为CusCom。
    function returnCus(cusObj){
        if(cusObj==null||cusObj=='undefined'){
            CusBlkCheckinapp.cert_code._obj.element.value="";
            return;
        }
        
        var cert_code=cusObj.cert_code;
        if (cert_code != null) {
            CusBlkCheckinapp.cert_code._setValue(cert_code);
        }
        //客户编码
        var cus_id=cusObj.cus_id;
        if(cus_id != null){
			CusBlkCheckinapp.cus_id._setValue(cus_id);
        }
        //客户名称
        var cus_name=cusObj.cus_name;
        if (cus_name != null) {
            CusBlkCheckinapp.cus_name._setValue(cus_name);
        }
        
        //法定代表人
        var legal_name = cusObj.legal_name;
        if(legal_name!=null){
            CusBlkCheckinapp.legal_name._obj.element.value=legal_name;
        }
     
        //联系电话
        var phone = cusObj.phone;
        if(phone!=null){
		    CusBlkCheckinapp.legal_phone._obj.element.value=phone;
		}

        //通讯地址
        var post_addr = cusObj.post_addr;
        if(post_addr!=null){
            CusBlkCheckinapp.legal_addr._obj.element.value=post_addr;
        }
    }
    
    function selCusId(data){
    	CusBlkCheckinapp.cus_id._setValue(data.cus_id._getValue());
		CusBlkCheckinapp.cus_name._setValue(data.cus_name._getValue());
		CusBlkCheckinapp.cert_type._setValue(data.cert_type._getValue());
		CusBlkCheckinapp.cert_code._setValue(data.cert_code._getValue());
	}

    function setCurPageDatas(data){
		//客户码,证件类型,证件号码,姓名
    	CusBlkCheckinapp.cus_id._setValue(data.cus_id._getValue());
		CusBlkCheckinapp.cus_name._setValue(data.cus_name._getValue());
		CusBlkCheckinapp.cert_type._setValue(data.cert_type._getValue());
		CusBlkCheckinapp.cert_code._setValue(data.cert_code._getValue());
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
		CusBlkCheckinapp.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		CusBlkCheckinapp.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		CusBlkCheckinapp.manager_id._setValue(data.cust_mgr._getValue());
		CusBlkCheckinapp.manager_br_id._setValue(data.main_br_id._getValue());
		doChangeOrgUrl();
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
		//法定代表人、通信地址、街道信息
		checkBlkCus();//校验该客户是否已是不宜贷款户或已经发起了一笔申请
		doChange();
		
	}
    /**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
	function doChangeOrgUrl(){
		var handleSuccess = function(o) {
   			if (o.responseText !== undefined) {
   				try {
   					var jsonstr = eval("(" + o.responseText + ")");
   				} catch (e) {
   					alert("Parse jsonstr define error!" + e.message);
   					return;
   				}
   				var flag = jsonstr.flag;
				if("belg2team" == flag){//客户经理只属于团队
					var manager_id = CusBlkCheckinapp.manager_id._getValue();
					CusBlkCheckinapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};
   		var manager_id = CusBlkCheckinapp.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	function checkBlkCus(){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == "suc1" ){
				alert("该客户已经存在一笔申请信息！");
				CusBlkCheckinapp.cus_id._setValue("");
				CusBlkCheckinapp.cus_name._setValue("");
				CusBlkCheckinapp.cert_type._setValue("");
				CusBlkCheckinapp.cert_code._setValue("");
			}
		}
		var handleFailure = function(o){
	        alert("异步回调失败！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var cus_id = CusBlkCheckinapp.cus_id._getValue();
		var url = '<emp:url action="checkBlkCus4Add.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	}
	
    function checkCertCode() {
        var certType = CusBlkCheckinapp.cert_type._obj.element.value;
        var certCode = CusBlkCheckinapp.cert_code._obj.element.value;
        if (certType == '0' || certType == '7') {//身份证或临时身份证时验证
            if (certCode != null && certCode != "") {
                var flg = CheckIdValue(certCode);
                if (flg) {
                    if (certCode.length == '15') {
                        if (confirm("你输入的身份证号码是15位，点击 [确定] 系统自动转换成18位新身份证！")) {
                        	CusBlkCheckinapp.cert_code._obj.element.value = oldCardToNewCard(certCode);
                        } else {
                        	CusBlkCheckinapp.cert_code._obj.element.value = "";
                            return false;
                        }
                    }
                    return true;
                } else {
                	CusBlkCheckinapp.cert_code._obj.element.value = "";
                    return flg;
                }
            }
        }
        //对私客户把法人代表删掉
        if(certType.indexOf("2") != 1){
            CusBlkCheckinapp.legal_name._obj.config.hidden='true';
            CusBlkCheckinapp.legal_name._obj._renderStatus();
        }
        return true;
    }

    function getOrgID(data){
    	CusBlkCheckinapp.manager_br_id._setValue(data.organno._getValue());
    	CusBlkCheckinapp.manager_br_id_displayname._setValue(data.organname._getValue());
	}

    function setconId(data){
    	CusBlkCheckinapp.manager_id_displayname._setValue(data.actorname._getValue());
    	CusBlkCheckinapp.manager_id._setValue(data.actorno._getValue());
    	CusBlkCheckinapp.manager_br_id._setValue(data.orgid._getValue());
    	CusBlkCheckinapp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
    	//CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(true);	
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
   				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
   				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusBlkCheckinapp.manager_br_id._setValue(jsonstr.org);
					CusBlkCheckinapp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusBlkCheckinapp.manager_br_id._setValue("");
					CusBlkCheckinapp.manager_br_id_displayname._setValue("");
					CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusBlkCheckinapp.manager_id._getValue();
					CusBlkCheckinapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}else if("yteam"==flag){
   					CusBlkCheckinapp.manager_br_id._setValue("");
   					CusBlkCheckinapp.manager_br_id_displayname._setValue("");
   					CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(false);
   					CusBlkCheckinapp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
   				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};
   		var manager_id = CusBlkCheckinapp.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
   	}
   	
    function onReturnRegStateCode(date){
    	CusBlkCheckinapp.legal_addr._obj.element.value=date.id;
    	CusBlkCheckinapp.legal_addr_displayname._obj.element.value=date.label;
	}

	function doAddCusBlkCheckinapp(){
		var form = document.getElementById("submitForm");
		if(CusBlkCheckinapp._checkAll()){
			CusBlkCheckinapp._toForm(form);
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
						var url = '<emp:url action="queryCusBlkCheckinappList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag =="fail"){
						//var options = CusBlkCheckinapp.black_level._obj.element.options;
						//var level = CusBlkCheckinapp.black_level._getValue();
						//var levelName ;
						//for ( var i = options.length - 1; i >= 0; i--) {
					   	//    if(options[i].value == level){
					   	//    	levelName = options[i].text;
					   	//    }
					    //}
						//alert("此客户不宜贷款户级别已经为["+levelName+"],不能新增！");
						alert("此客户已进入共享客户管理,不能新增！");
					}else{
						alert("保存失败！");
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
			var serno = CusBlkCheckinapp.serno._getValue();
			var url = '<emp:url action="addCusBlkCheckinappRecord.do"/>?serno='+serno;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	}

	function doLoad(){
		var dutyList = '${context.dutyNoList}';
		if(dutyList.indexOf("D0005")>=0 || dutyList.indexOf("D0006")>=0){//授信管理部综合管理岗 或者 风险部经办岗
			CusBlkCheckinapp.manager_id_displayname._setValue('${context.currentUserName}');
			CusBlkCheckinapp.manager_br_id_displayname._setValue('${context.organName}');
			CusBlkCheckinapp.manager_id._setValue('${context.currentUserId}');
			CusBlkCheckinapp.manager_br_id._setValue('${context.organNo}');
			CusBlkCheckinapp.manager_id_displayname._obj._renderReadonly(true);
			CusBlkCheckinapp.manager_br_id_displayname._obj._renderReadonly(true);
		}
		var flagHOrg = '<%=flagHOrg%>';
		if(flagHOrg != "1"){
			var options = CusBlkCheckinapp.black_type._obj.element.options;
			for ( var i = options.length - 1; i >= 0; i--) {
				if(options[i].value == "14"){
					options.remove(i);
				}
			}
		}
	}

	//通过客户码查询法定代表人、通信地址、街道等信息
	function doChange(){
	var cus_id = CusBlkCheckinapp.cus_id._getValue();
		if(cus_id != null && cus_id != ""){
			var url = '<emp:url action="getCusBlkCheckinappRecord.do"/>?cus_id='+cus_id;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					//alert(jsonstr.street);
					var flag = jsonstr.flag;
					var legal_name = jsonstr.legal_name;
					var legal_addr = jsonstr.legal_addr;
					var legal_addr_displayname = jsonstr.legal_addr_displayname;
					var street = jsonstr.street;
					if(flag == "success"){
						CusBlkCheckinapp.legal_name._setValue(legal_name);
						CusBlkCheckinapp.legal_addr._setValue(legal_addr);
						CusBlkCheckinapp.legal_addr_displayname._setValue(legal_addr_displayname);
						CusBlkCheckinapp.street._setValue(street);
					}else {
						alert(flag);
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addCusBlkCheckinappRecord.do" method="POST">
		
		<emp:gridLayout id="CusBlkCheckinappGroup" title="共享客户进入申请" maxColumn="2">
			<emp:text id="CusBlkCheckinapp.serno" label="业务流水号" maxlength="40" required="false" readonly="false" colSpan="2" hidden="true"/>
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="CusBlkCheckinapp.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cust_mgr = '${context.currentUserId}'&returnMethod=setCurPageDatas" required="true"  defvalue="" colSpan="2" />
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="CusBlkCheckinapp.cus_name" label="客户名称" maxlength="60" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2" />
			<emp:select id="CusBlkCheckinapp.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.cert_code" label="证件号码" maxlength="20" required="true" readonly="true"/>
			<emp:select id="CusBlkCheckinapp.black_type" label="客户类型" required="true" dictname="STD_ZB_EVENT_TYP" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusBlkCheckinapp.black_level" label="不宜贷款户级别" hidden="true" dictname="STD_ZB_BLACKLIST_TYP" />
			<emp:text id="CusBlkCheckinapp.legal_name" label="法定代表人" maxlength="30" required="false" />
			<emp:text id="CusBlkCheckinapp.legal_phone" label="联系电话" maxlength="35" required="false" dataType="Phone"/>
			<emp:text id="CusBlkCheckinapp.legal_addr" label="通讯地址" required="true" hidden="true"/>
			<emp:pop id="CusBlkCheckinapp.legal_addr_displayname" label="通讯地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
			<emp:text id="CusBlkCheckinapp.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:textarea id="CusBlkCheckinapp.black_reason" label="客户描述" maxlength="250" required="true" colSpan="2" onblur="this.value = this.value.substring(0, 250)"/>
			<emp:select id="CusBlkCheckinapp.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
		</emp:gridLayout>
	
		<emp:gridLayout id="CusBlkCheckinappGroup" title="登记信息" maxColumn="2">
			<emp:pop id="CusBlkCheckinapp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusBlkCheckinapp.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  required="true" />
			<emp:text id="CusBlkCheckinapp.manager_id" label="责任人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.manager_br_id" label="管理机构" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="CusBlkCheckinapp.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName"  readonly="true"/>
			<emp:text id="CusBlkCheckinapp.input_id" label="登记人" maxlength="20" required="false" readonly="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusBlkCheckinapp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo"  readonly="false" hidden="true"/>
			<emp:date id="CusBlkCheckinapp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusBlkCheckinapp" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

