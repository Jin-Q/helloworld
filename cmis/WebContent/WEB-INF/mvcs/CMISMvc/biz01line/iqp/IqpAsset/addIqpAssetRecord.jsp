<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function onload(){
		var options = IqpAsset.takeover_type._obj.element.options;
		for(var i=options.length-1;i>=0;i--){
			if(options[i].value == "02" || options[i].value == "04"){
				options.remove(i);
			}
		}
	};
	
	//-------异步保存主表单页面信息-------
	function doSave(){
		if(!IqpAsset._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpAsset._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag == "success"){
					var url = '<emp:url action="getIqpAssetUpdatePage.do"/>?op=update&asset_no='+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
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

		var url = '<emp:url action="addIqpAssetRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}					
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
			<emp:form id="submitForm" action="addIqpAssetRecord.do" method="POST">
				<emp:gridLayout id="IqpAssetGroup" title="资产包管理" maxColumn="2">
					<emp:text id="IqpAsset.asset_no" label="资产包编号" maxlength="40" hidden="true" required="false" colSpan="2"/>
					<emp:text id="IqpAsset.asset_name" label="资产包名称" maxlength="100" required="true" colSpan="2"/>
					<emp:select id="IqpAsset.takeover_type" label="转让方式" required="true" dictname="STD_ZB_TAKEOVER_MODE"/>
					<emp:date id="IqpAsset.takeover_date" label="转让日期" required="true"/>
					<emp:select id="IqpAsset.asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE" defvalue="1" readonly="true"/>
					<emp:text id="IqpAsset.input_id_displayname" label="登记人" defvalue="${context.currentUserName}" required="false" readonly="true"/>
					<emp:text id="IqpAsset.input_br_id_displayname" label="登记机构" defvalue="${context.organName}" required="false"  readonly="true"/>
					<emp:text id="IqpAsset.input_id" label="登记人" maxlength="40" defvalue="${context.currentUserId}" required="false" hidden="true" readonly="true"/>
					<emp:text id="IqpAsset.input_br_id" label="登记机构" maxlength="20" defvalue="${context.organNo}" required="false" hidden="true" readonly="true"/>
					<emp:date id="IqpAsset.input_date" label="登记日期" defvalue="${context.OPENDAY}" required="false"  readonly="true" />
					<emp:select id="IqpAsset.status" label="资产包状态" required="false" dictname="STD_ZB_ASSET_STATUS" hidden="true" defvalue="01"/>
				    
				</emp:gridLayout>
				<div align="center">
					<br>
					<emp:button id="save" label="登记" />
					<emp:button id="reset" label="重置"/>
				</div>
			</emp:form>
		
</body>
</html>
</emp:page>