<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryIqpMortValueManaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	//设置押品目录
	function setCatalogPath(data){
		IqpMortValueMana.catalog_no._setValue(data.locate);  //押品目录存放目录路径
		IqpMortValueMana.catalog_no_displayname._setValue(data.locate_cn);

		//生产厂家POP可以选择
		//IqpMortValueMana.produce_vender_displayname._obj._renderReadonly(false);
		//var url = '<emp:url action="getCommoProviderNoPopList.do"/>&mort_catalog_no='+data.id;
		//IqpMortValueMana.produce_vender_displayname._obj.config.url=url;
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var attr_type = jsonstr.attr_type;
				if("01" == attr_type){
					alert("押品类型的类型属性，不能为“非含价商品”！");
					IqpMortValueMana.catalog_no._setValue("");
					IqpMortValueMana.catalog_no_displayname._setValue("");
				}else{
				}
			}
		};
		var handleFailure = function(o) {
			alert("异步校验失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var url = '<emp:url action="checkAttrType.do"/>?catalog_path='+data.locate;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
	}

	//选择产地回调
	function setProduceArea(data){
		IqpMortValueMana.produce_area._setValue(data.id);
		IqpMortValueMana.produce_area_displayname._setValue(data.label);
	}

	//选择生产厂家
	function setProduceVender(data){
		IqpMortValueMana.produce_vender._setValue(data.provider_no._getValue());
		IqpMortValueMana.produce_vender_displayname._setValue(data.provider_no_displayname._getValue());
	}

	//销售区域回调
	function setSaleArea(data){
		IqpMortValueMana.sale_area._setValue(data.id);
		IqpMortValueMana.sale_area_displayname._setValue(data.label);
	}

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

					var value_no = jsonstr.value_no;
					
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

	function onLoad(){
		var catalog_no = IqpMortValueMana.catalog_no._getValue();
		if(""!=catalog_no){   //押品目录编号有值
			catalog_no = catalog_no.substr(catalog_no.lastIndexOf(",")+1,catalog_no.length);
			//生产厂家POP可以选择
		//	IqpMortValueMana.produce_vender_displayname._obj._renderReadonly(false);
		//	var url = '<emp:url action="getCommoProviderNoPopList.do"/>&mort_catalog_no='+catalog_no;
		//	IqpMortValueMana.produce_vender_displayname._obj.config.url=url;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="${context.operate}" method="POST">
		<emp:gridLayout id="IqpMortValueManaGroup" maxColumn="2" title="押品价格管理">
			<emp:text id="IqpMortValueMana.value_no" label="价格编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpMortValueMana.catalog_no" label="目录编号ID" required="true" hidden="true" readonly="true"/>
			<emp:pop id="IqpMortValueMana.catalog_no_displayname" label="押品类型名称" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath" required="true" cssElementClass="emp_field_text_long" colSpan="2"/>
			<emp:text id="IqpMortValueMana.produce_vender" label="生产厂家" maxlength="200" required="true" readonly="false" cssElementClass="emp_field_text_long"/>
			
			<emp:text id="IqpMortValueMana.produce_area" label="产地代码" maxlength="200" required="false" readonly="true" hidden="true"/>
			<emp:pop id="IqpMortValueMana.produce_area_displayname" label="产地" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" returnMethod="setProduceArea" colSpan="2" cssElementClass="emp_field_text_long" />
			<emp:text id="IqpMortValueMana.sale_area" label="销售区域ID" required="false" readonly="true" hidden="true"/>
			<emp:pop id="IqpMortValueMana.sale_area_displayname" label="销售区域" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setSaleArea" required="true" cssElementClass="emp_field_text_long"/>
			<emp:select id="IqpMortValueMana.freq_unit" label="盯市频率单位" required="true" dictname="STD_ZX_FREQ_UNIT" />
			<emp:text id="IqpMortValueMana.freq" label="盯市频率" maxlength="1" required="true" dataType="Int" />
			<emp:select id="IqpMortValueMana.unit" label="计价单位" required="true" dictname="STD_ZB_UNIT"/>
			<emp:text id="IqpMortValueMana.market_value" label="市场价" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="IqpMortValueMana.auth_date" label="价格核准时间" required="true" />
			<emp:text id="IqpMortValueMana.change_valve" label="价格变动阀值" maxlength="16" required="true" dataType="Percent" />
			<emp:select id="IqpMortValueMana.info_sour" label="价格信息来源" required="true" dictname="STD_ZB_INFO_SOUR" />
			<emp:select id="IqpMortValueMana.is_qual_judge" label="是否需要品质鉴定" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="IqpMortValueMana.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="IqpMortValueMana.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpMortValueMana.input_br_id_displayname" label="登记机构"   required="true" readonly="true" />
			<emp:date id="IqpMortValueMana.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:select id="IqpMortValueMana.status" label="状态" required="false" dictname="STD_ZB_STATUS" readonly="true" defvalue="2"/>
			<emp:text id="IqpMortValueMana.input_id" label="登记人" maxlength="20" required="true" hidden="true" readonly="true"/>
			<emp:text id="IqpMortValueMana.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submitValueMana" label="保存" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
