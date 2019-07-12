<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doReturn(){
	window.close();
}
function doNext(){
	if(!IqpAppMortDetail._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	IqpAppMortDetail._toForm(form);
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
				alert("修改成功!"); 
				window.opener.location.reload();
				window.close();
			}else {
				alert("修改失败!"); 
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
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
}
//设置押品上级目录
function setCatalogPath(data){
	if(data.id==IqpAppMortDetail.catalog_no._getValue()){
		alert("上级目录不能为当前变更目录，请重新选择！");
		return false;
	}
	IqpAppMortDetail.sup_catalog_no._setValue(data.id);
	IqpAppMortDetail.sup_catalog_no_displayname._setValue(data.label);
	IqpAppMortDetail.catalog_path._setValue(data.locate+","+IqpAppMortDetail.catalog_no._getValue()); 
	IqpAppMortDetail.catalog_path_displayname._setValue(data.locate_cn);

	//目录层级
	var locate_list = IqpAppMortDetail.catalog_path._getValue().split(",");
	IqpAppMortDetail.catalog_lvl._setValue(locate_list.length);
}	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpAppMortDetailRecord.do" method="POST">
		<emp:gridLayout id="IqpAppMortDetailGroup" title="押品目录准入明细" maxColumn="2">
			<emp:text id="IqpAppMortDetail.serno" label="业务流水号" maxlength="40" required="true" hidden="true"/>
		    <emp:text id="IqpAppMortDetail.catalog_no" label="目录编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpAppMortDetail.catalog_name" label="目录名称" maxlength="100" required="true" />
			<emp:text id="IqpAppMortDetail.commo_trait" label="商品特性" maxlength="100" required="false" colSpan="2" />
			<emp:text id="IqpAppMortDetail.sup_catalog_no" label="上级目录ID" maxlength="100" hidden="true" defvalue="ALL" readonly="true"/>
			<emp:pop id="IqpAppMortDetail.sup_catalog_no_displayname" label="上级目录" url="showCatalogManaTree.do?isMin=N&value=N" returnMethod="setCatalogPath" required="false" defvalue="押品目录" /> 
			<emp:text id="IqpAppMortDetail.catalog_path" label="目录路径location" maxlength="200" required="true" hidden="true" cssElementClass="emp_field_text_readonly" defvalue="ALL"/>
			<emp:text id="IqpAppMortDetail.catalog_path_displayname" label="目录路径"   required="false" colSpan="2" cssElementClass="emp_field_text_readonly" defvalue="押品目录" hidden="true"/>
			<emp:text id="IqpAppMortDetail.catalog_lvl" label="押品目录层级" maxlength="1" required="true" dataType="Int" defvalue="1" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAppMortDetail.attr_type" label="类型属性 " required="true" dictname="STD_ZB_ATTR_TYPE" defvalue="02" readonly="true"/>
			<emp:text id="IqpAppMortDetail.model" label="型号" required="true" maxlength="40"/>
			<emp:text id="IqpAppMortDetail.imn_rate" label="基准质押率" maxlength="16" required="true" dataType="Percent" />
			<emp:textarea id="IqpAppMortDetail.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="IqpAppMortDetail.input_id_displayname" label="登记人"  required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="IqpAppMortDetail.input_br_id_displayname" label="登记机构"  required="true" readonly="true" defvalue="$organName"/>
			<emp:text id="IqpAppMortDetail.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpAppMortDetail.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			<emp:date id="IqpAppMortDetail.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
			<emp:select id="IqpAppMortDetail.status" label="状态" required="true" dictname="STD_ZB_STATUS" defvalue="0" hidden="true"/>
			<emp:text id="IqpAppMortDetail.oper_type" label="操作类型：1-准入 2-退出 " maxlength="1" required="false" defvalue="1" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="修改" op="update"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
