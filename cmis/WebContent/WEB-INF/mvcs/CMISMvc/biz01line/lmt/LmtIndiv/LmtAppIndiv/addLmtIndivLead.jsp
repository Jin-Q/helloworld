<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

/*--user code begin--*/
	//选择客户POP框返回方法
	function returnCus(data){
		LmtAppIndiv.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
		LmtAppIndiv.belg_line._setValue(data.belg_line._getValue());
		LmtAppIndiv.cus_type._setValue(data.cus_type._getValue());

		LmtAppIndiv.manager_id._setValue(data.cust_mgr._getValue());
		LmtAppIndiv.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		LmtAppIndiv.manager_br_id._setValue(data.main_br_id._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
	}

	//下一步
    function do1ExecuteAjax(){
   	 	var handleSuccess = function(o) {
  			if (o.responseText !== undefined) {
  				try {
  					var fistTest = eval("(" + o.responseText + ")");
  				} catch (e) {
  					alert("异步执行不成功：" + e.message);
  					return;
  				}
  				var result = fistTest.result;   
  				if(result != ""){
  	  				alert(result);
				}else{
					doCreateApp4Ajax();   //异步生成授信申请
				}
  			}
   		};
  		var handleFailure = function(o) {
  			alert("与服务器交互失败，请联系管理员！");
  		};
  		var callback = {
  			success :handleSuccess,
  			failure :handleFailure
  		};
  		var form = document.getElementById('submitForm');
		var result = LmtAppIndiv._checkAll();
	    if(result){
	    	var url = '<emp:url action="searchLmtIndivInfo.do"/>?cus_id='+LmtAppIndiv.cus_id._getValue()+"&lrisk_type="+LmtAppIndiv.lrisk_type._getValue()+"&"+new Date();
	  		url = EMPTools.encodeURI(url);
	  		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	    }else {
		    alert("请检查各标签页面中的必填信息是否遗漏！");
		}
	}

	//异步生成授信申请
    function doExecuteAjax(){
   	 	var handleSuccess = function(o) {
  			if (o.responseText !== undefined) {
  				try {
  					var fistTest = eval("(" + o.responseText + ")");
  				} catch (e) {
  					alert("异步执行不成功：" + e.message);
  					return;
  				}
  				var result = fistTest.flag;
  				if("" != result && "success"==result){
  	  				//alert("成功发起一笔个人授信申请！");
	  	  			var serno = fistTest.serno;
		            var url = '<emp:url action="getLmtAppIndivUpdatePage.do"/>?serno='+serno+'&op=update';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert(fistTest.msg);
				}
  			}
   		};
  		var handleFailure = function(o) {
  			alert("与服务器交互失败，请联系管理员！");
  		};
  		var callback = {
  			success :handleSuccess,
  			failure :handleFailure
  		};
  		var form = document.getElementById('submitForm');
  		LmtAppIndiv._toForm(form);
  		var result = LmtAppIndiv._checkAll();
	    if(result){
  		var postData = YAHOO.util.Connect.setForm(form);	
  		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback, postData);
	    }else {
		    alert("请检查各标签页面中的必填信息是否遗漏！");
		}
	};
	
    function doReturn() {
		var url = '<emp:url action="queryLmtAppIndivList.do"/>?type=app';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//责任人
	function setconId(data){
		LmtAppIndiv.manager_id._setValue(data.actorno._getValue());
		LmtAppIndiv.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppIndiv.manager_br_id._setValue(data.orgid._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppIndiv.manager_br_id._setValue(jsonstr.org);
					LmtAppIndiv.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppIndiv.manager_br_id._setValue("");
					LmtAppIndiv.manager_br_id_displayname._setValue("");
					LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppIndiv.manager_id._getValue();
					LmtAppIndiv.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppIndiv.manager_br_id._setValue("");
					LmtAppIndiv.manager_br_id_displayname._setValue("");
					LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppIndiv.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppIndiv.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//责任机构
	function getOrgID(data){
		LmtAppIndiv.manager_br_id._setValue(data.organno._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.organname._getValue());
	}
/*--user code end--*/
</script>
</head>
<body class="page_content" style="width:200px">
	<emp:form id="submitForm" action="getLmtAppIndivAddPage.do" method="POST">
		<emp:gridLayout id="LmtAppIndivGroup" title="授信申请引导页" maxColumn="1">
			<emp:pop id="LmtAppIndiv.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE='BL300' and cus_status='20'&returnMethod=returnCus" required="true"/> 
			<emp:text id="cus_name_displayname" label="客户名"   required="true" readonly="true" cssElementClass="emp_field_text_readonly" />
			<emp:select id="LmtAppIndiv.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE"/> 
			<emp:text id="LmtAppIndiv.belg_line" label="所属条线" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.cus_type" label="客户类型" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:pop id="LmtAppIndiv.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" readonly="true"/>
			<emp:pop id="LmtAppIndiv.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" readonly="true"/>
			
			<emp:text id="LmtAppIndiv.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="executeAjax" label="下一步" op="add" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

