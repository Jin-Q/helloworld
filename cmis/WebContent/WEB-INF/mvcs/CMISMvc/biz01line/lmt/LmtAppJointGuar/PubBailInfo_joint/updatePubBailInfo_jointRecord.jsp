<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		//window.close();
		//window.parent.location.reload();
		history.go(-1);
	}
	function doOnload(){
		PubBailInfo.bail_acct_no._obj.addOneButton('uniquCheck','获 取', setCusInfo);
	}
	function setCusInfo(){
		
	}
	//确定保存
	function doSave(){
		if(!PubBailInfo._checkAll()){
			return ;
		}
		var handSuc = function(o){
			if(o.responseText !== undefined) {
				try { var jsonstr = eval("("+o.responseText+")"); } 
					catch(e) {
					alert("数据库操作失败!");
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "suc"){
					alert("保存成功!");
					serno = "${context.serno}";
					var url = '<emp:url action="queryPubBailInfo_jointList.do"/>?op=update&serno='+serno; 
					url = EMPTools.encodeURI(url);
					window.location = url; 
					//window.close();
					//window.opener.location.reload();
				}
			}
		};
	    var handFail = function(o){
	    };
	    var callback = {
	    	success:handSuc,
	    	failure:handFail
	    };
	    var form = document.getElementById("submitForm");
		PubBailInfo._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action, callback,postData);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="updatePubBailInfo_jointRecord.do" method="POST">
		<emp:gridLayout id="PubBailInfoGroup" title="保证金信息表" maxColumn="2">
			<emp:text id="PubBailInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true"  />
			<emp:text id="PubBailInfo.cus_id" label="客户码" maxlength="40" required="true" readonly="true" />
			<emp:text id="PubBailInfo.bail_acct_no" label="保证金账号" maxlength="40" required="true" />
			<emp:text id="PubBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="false" readonly="true"/>
			<emp:select id="PubBailInfo.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
			<emp:text id="PubBailInfo.rate" label="利率" maxlength="10" required="false" dataType="Rate" readonly="true" />
			<emp:text id="PubBailInfo.up_rate" label="上浮比例" maxlength="10" required="false" dataType="Percent2" readonly="true" />
			<emp:select id="PubBailInfo.bail_type" label="保证金类型" required="false" dictname="STD_PUB_BAIL_TYPE" readonly="true"/>
			<emp:text id="PubBailInfo.dep_term" label="存期" maxlength="10" required="false" dataType="Int" readonly="true" />
			<emp:text id="PubBailInfo.open_org" label="开户机构" required="false" readonly="true"/>
		    <emp:select id="PubBailInfo.bail_status" label="状态" dictname="STD_BAIL_INFO_STATUS" required="false" hidden="true"/>
		    <emp:text id="PubBailInfo.cus_id_displayname" label="客户名称" required="false" readonly="true" hidden="true"/>
		    <emp:text id="PubBailInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/>
		    <emp:text id="PubBailInfo.bail_acct_gl_code" label="科目号" maxlength="20" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
