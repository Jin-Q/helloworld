<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doOnLoad(){
	document.getElementById("mainTab").href="javascript:reLoad();";
}
function reLoad(){
	var url = '<emp:url action="getIqpAssetUpdatePage.do"/>?menuId=queryIqpAsset&asset_no=${context.IqpAsset.asset_no}&op=update';
	url = EMPTools.encodeURI(url);
	window.location = url;
	//window.location.reload();
}
	/*--user code begin--*/
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
				if(flag == "success"){
					alert("保存成功！");
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

		var url = '<emp:url action="updateIqpAssetRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	function isHave(){
        var total = IqpAsset.asset_total_amt._getValue();
        var takeover = IqpAsset.takeover_total_amt._getValue();
        if(total<takeover){
            alert("转让金额大于资产总额");
            IqpAsset.takeover_total_amt._setValue("");
        }
	}						
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="mainTab" id="mainTab">
		<emp:tab label="资产包管理信息" id="mainTab">
			<emp:form id="submitForm" action="updateIqpAssetRecord.do" method="POST">
				<emp:gridLayout id="IqpAssetGroup" maxColumn="2" title="资产包管理">
					<emp:text id="IqpAsset.asset_no" label="资产包编号" maxlength="40" required="false" readonly="true" hidden="false" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
					<emp:text id="IqpAsset.asset_name" label="资产包名称" maxlength="100" required="false" readonly="true"/>
					<emp:select id="IqpAsset.takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE" readonly="true"/>
					<emp:select id="IqpAsset.asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE" readonly="true"/>
					<emp:text id="IqpAsset.asset_qnt" label="资产数量" maxlength="38" required="false" dataType="Int" readonly="true"/> 
					<emp:text id="IqpAsset.asset_total_amt" label="资产总额" maxlength="18" readonly="true" required="false" dataType="Currency" />
					<emp:text id="IqpAsset.takeover_total_amt" label="转让总额" maxlength="18" readonly="true" required="false" dataType="Currency" onblur="isHave()"/>
					<emp:text id="IqpAsset.takeover_total_int" label="转让利息" maxlength="18" readonly="true" required="false" dataType="Currency" />
					<emp:date id="IqpAsset.takeover_date" label="转让日期" required="true"/>
					<emp:select id="IqpAsset.status" label="资产包状态" required="false" dictname="STD_ZB_ASSET_STATUS" readonly="true"/>
					<emp:text id="IqpAsset.input_id_displayname" label="登记人" hidden="false" required="false" readonly="true"/>
					<emp:text id="IqpAsset.input_br_id_displayname" label="登记机构" hidden="false" required="false" readonly="true"/>
					<emp:date id="IqpAsset.input_date" label="登记日期" required="false" hidden="false" readonly="true"/>
					<emp:text id="IqpAsset.input_id" label="登记人" maxlength="40" hidden="true" required="false" />
					<emp:text id="IqpAsset.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false" />
				</emp:gridLayout>
				
				<div align="center">
					<br>
					<emp:button id="save" label="确定" />
					<emp:button id="reset" label="重置"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
