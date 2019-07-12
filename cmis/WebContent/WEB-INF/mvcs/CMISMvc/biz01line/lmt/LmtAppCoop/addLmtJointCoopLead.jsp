<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>合作方授信新增引导页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

/*--user code begin--*/

	//选择客户POP框返回方法
	function returnCus(data){
		LmtAppJointCoop.cus_id._setValue(data.cus_id._getValue());
		LmtAppJointCoop.cus_id_displayname._setValue(data.cus_name._getValue());
		LmtAppJointCoop.main_br_id._setValue(data.main_br_id._getValue());
		LmtAppJointCoop.manager_id._setValue(data.cust_mgr._getValue());
		LmtAppJointCoop.manager_br_id._setValue(data.main_br_id._getValue());
		LmtAppJointCoop.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		LmtAppJointCoop.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
	}

	//异步调用生成合并明细
    function doExecuteAjax(){
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
					LmtAppJointCoop._toForm(form);
			    	form.submit();
				}
  			}
   		};
  		var handleFailure = function(o) {
  			//ymPrompt.alert({message:'与服务器交互失败，请联系管理员！',title:'温馨提示',handler:null});
  			alert("与服务器交互失败，请联系管理员！");
  		};
  		var callback = {
  			success :handleSuccess,
  			failure :handleFailure
  		};
  		var form = document.getElementById('submitForm');
		var result = LmtAppJointCoop._checkAll();
	    if(result){
	    	var url = '<emp:url action="searchLmtCoopInfo.do"/>?cus_id='+LmtAppJointCoop.cus_id._getValue()+"&"+new Date();
	  		url = EMPTools.encodeURI(url);
	  		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	    }else {
	    	//ymPrompt.alert({message:'请检查各标签页面中的必填信息是否遗漏！',title:'温馨提示',handler:null});
		    alert("请检查各标签页面中的必填信息是否遗漏！");
		}
	}
	
    function doReturn() {
		var url = '<emp:url action="queryLmtAppJointCoopList.do"/>?type=app';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//设置共享范围机构
	function getBelgOrg(data){
		LmtAppJointCoop.belg_org._setValue(data[0]);
		LmtAppJointCoop.belg_org_displayname._setValue(data[1]);
	}

	/****** 共享范围与所属机构控制 *******/
	function controlOrg(_value){
		if(_value == 2){
			LmtAppJointCoop.belg_org._obj._renderHidden(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(false);

			LmtAppJointCoop.belg_org._obj._renderRequired(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderRequired(true);
		}else{
			LmtAppJointCoop.belg_org._obj._renderHidden(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(true);

			LmtAppJointCoop.belg_org._obj._renderRequired(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderRequired(false);
		}
	};

	function onLoad(){
		//合作方类别中不需要联保小组
		var options = LmtAppJointCoop.coop_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "010"){
				options.remove(i);
			}
		}
	}
/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="getLmtAppJointCoopAddPage.do" method="POST">
		<emp:gridLayout id="LmtAppJointCoopGroup" title="合作方授信新增向导" maxColumn="2"> 
			<emp:pop id="LmtAppJointCoop.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL100','BL200') and cus_status='20'&returnMethod=returnCus" required="true"/>
			<emp:select id="LmtAppJointCoop.coop_type" label="合作方类别" dictname="STD_ZB_COOP_TYPE" required="true"/>
			<emp:text id="LmtAppJointCoop.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_long_readonly" /> 
			<emp:text id="LmtAppJointCoop.main_br_id" label="管理机构" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:select id="LmtAppJointCoop.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="controlOrg(this.value)"/>
			<emp:pop id="LmtAppJointCoop.belg_org" label="所属机构码"  cssElementClass="emp_field_text_long" colSpan="2" url="queryMultiSOrgPop.do" returnMethod="getBelgOrg"  required="false"  hidden="true"/>
			<emp:textarea id="LmtAppJointCoop.belg_org_displayname" label="所属机构名称"  required="false" readonly="true" hidden="true" />
			
			<emp:text id="LmtAppJointCoop.manager_id" label="责任人" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.manager_br_id" label="责任机构" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.manager_id_displayname" label="责任人"  required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJointCoop.manager_br_id_displayname" label="责任机构"  required="false" readonly="true" hidden="true"/>
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

