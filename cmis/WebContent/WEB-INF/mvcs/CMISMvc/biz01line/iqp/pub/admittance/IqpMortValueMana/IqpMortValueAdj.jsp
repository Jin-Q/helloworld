<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpMortValueManaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//异步提交申请数据
	function doSubmitValueMana(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("Y" == flag){
					alert("保存成功！");
					document.getElementById("button_submitValueMana").disabled = "";
					
					var url = '<emp:url action="queryIqpMortValueManaList.do"/>';
					url = EMPTools.encodeURI(url);
					window.location=url;
				}else{
					alert("保存失败,失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = IqpMortValueMana._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="${context.operate}" method="POST">
		<emp:gridLayout id="IqpMortValueManaGroup" title="押品价格管理" maxColumn="2">
			<emp:text id="IqpMortValueMana.value_no" label="价格编号" maxlength="40" required="true" readonly="true" />
			<emp:pop id="IqpMortValueMana.catalog_no_displayname" label="押品类型名称" url="showCatalogManaTree.do" returnMethod="setCatalogPath" required="true" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
			<emp:pop id="IqpMortValueMana.produce_area_displayname" label="产地" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" returnMethod="setProduceArea" colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true"/>
			<emp:pop id="IqpMortValueMana.produce_vender_displayname" label="生产厂家" url="" required="true" cssElementClass="emp_field_text_readonly" defvalue="11" colSpan="2" readonly="true"/>
			<emp:pop id="IqpMortValueMana.sale_area_displayname" label="销售区域" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setSaleArea" required="true" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
			<emp:select id="IqpMortValueMana.freq_unit" label="盯市频率单位" required="true" dictname="STD_ZX_FREQ_UNIT" readonly="true" />
			<emp:text id="IqpMortValueMana.freq" label="盯市频率" maxlength="1" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly" readonly="true" />
			<emp:date id="IqpMortValueMana.auth_date" label="价格核准时间" required="true" readonly="true" />
			<emp:text id="IqpMortValueMana.change_valve" label="价格变动阀值" maxlength="16" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="IqpMortValueMana.is_qual_judge" label="是否需要品质鉴定" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="IqpMortValueMana.info_sour" label="价格信息来源" required="true" dictname="STD_ZB_INFO_SOUR" />
			<emp:text id="IqpMortValueMana.org_value" label="上次商品核准价格" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" defvalue="0"/>
			<emp:text id="IqpMortValueMana.market_value" label="此次商品核准价格" maxlength="18" required="true" dataType="Currency" />
			<emp:textarea id="IqpMortValueMana.change_resn" label="价格变动原因" maxlength="200" required="false" colSpan="2" />
			<emp:textarea id="IqpMortValueMana.memo" label="备注" maxlength="200" required="false" colSpan="2" readonly="true"/>
			<emp:text id="IqpMortValueMana.input_id_displayname" label="登记人"   required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpMortValueMana.input_br_id_displayname" label="登记机构"   required="true" readonly="true" hidden="true"/> 
			<emp:date id="IqpMortValueMana.input_date" label="登记日期" required="true" readonly="true" hidden="true"/>
			<emp:select id="IqpMortValueMana.status" label="状态" required="false" dictname="STD_ZB_STATUS" readonly="true" defvalue="0" hidden="true"/>
			<emp:text id="IqpMortValueMana.isAdj" label="是否价格调整" required="false" readonly="true" defvalue="${context.isAdj}" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<emp:button id="submitValueMana" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
	<div class='emp_gridlayout_title'>价格变动历史&nbsp;</div>
	<emp:table icollName="IqpMortValueAdjList" pageMode="true" url="pageIqpMortValueAdj.do?value_no=${context.value_no}&isAdj=${context.isAdj}">
		<emp:text id="org_valve" label="原有押品单价" dataType="Currency"/>
		<emp:text id="change_valve" label="此次核准单价" dataType="Currency"/>
		<emp:text id="info_sour" label="信息来源" dictname="STD_ZB_INFO_SOUR" />
		<emp:text id="inure_date" label="生效时间" />
	</emp:table>
</body>
</html>
</emp:page>
