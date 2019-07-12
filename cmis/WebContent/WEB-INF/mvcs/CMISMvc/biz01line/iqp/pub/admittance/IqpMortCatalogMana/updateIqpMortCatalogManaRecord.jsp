<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	
	//设置押品上级目录
	function setCatalogPath(data){
		if(data.id==IqpMortCatalogMana.catalog_no._getValue()){
			alert("上级目录不能为当前变更目录，请重新选择！");
			return false;
		}
		IqpMortCatalogMana.sup_catalog_no._setValue(data.id);
		IqpMortCatalogMana.sup_catalog_no_displayname._setValue(data.label);
		IqpMortCatalogMana.catalog_path._setValue(data.locate+","+IqpMortCatalogMana.catalog_no._getValue()); 
		IqpMortCatalogMana.catalog_path_displayname._setValue(data.locate_cn);

		//目录层级
		var locate_list = IqpMortCatalogMana.catalog_path._getValue().split(",");
		IqpMortCatalogMana.catalog_lvl._setValue(locate_list.length);
	}

	//异步提交申请数据
	function doSubmitIqpMortCatalogMana(){
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
					var catalog_no = jsonstr.catalog_no;
					var url = '<emp:url action="getIqpMortCatalogManaUpdatePage.do"/>?catalog_no='+catalog_no;
					url = EMPTools.encodeURI(url);
					window.location = url;
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
		var result = IqpMortCatalogMana._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			document.getElementById("button_submitIqpMortCatalogMana").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	};

	function onLoad(){
		var isLeaf = "${context.isLeaf}";
		if("true" == isLeaf){   //如果存在子目录则控制上级目录不能调整 
			IqpMortCatalogMana.sup_catalog_no_displayname._obj._renderReadonly(true);
			IqpMortCatalogMana.attr_type._obj._renderReadonly(true);
		}

		var _value = IqpMortCatalogMana.attr_type._getValue();
		changeAttrType(_value);
	}

	function doReturn() {
		var url = '<emp:url action="queryIqpMortCatalogManaList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	//类型属性值改变事件
	function changeAttrType(_value){
		if("02"==_value){  //含价商品
			IqpMortCatalogMana.imn_rate._obj._renderHidden(false);  //隐藏基准质押率
			IqpMortCatalogMana.model._obj._renderHidden(false); 
			IqpMortCatalogMana.imn_rate._obj._renderRequired(true); 
			IqpMortCatalogMana.model._obj._renderRequired(true); 
		}else{  //非含价商品
			IqpMortCatalogMana.imn_rate._obj._renderRequired(false); 
			IqpMortCatalogMana.imn_rate._obj._renderHidden(true);  //隐藏基准质押率
			IqpMortCatalogMana.model._obj._renderHidden(true); 
			IqpMortCatalogMana.model._obj._renderRequired(false); 
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:form id="submitForm" action="${context.operate}" method="POST">
			<emp:gridLayout id="IqpMortCatalogManaGroup" maxColumn="2" title="押品目录管理">
				<emp:text id="IqpMortCatalogMana.catalog_no" label="目录编号" maxlength="40" required="false" readonly="true" hidden="true"/>
				<emp:text id="IqpMortCatalogMana.catalog_name" label="目录名称" maxlength="100" required="true" />
				<emp:text id="IqpMortCatalogMana.commo_trait" label="商品特性" maxlength="100" required="false" colSpan="2" />
				<emp:text id="IqpMortCatalogMana.sup_catalog_no" label="上级目录ID" maxlength="100" hidden="true" defvalue="ALL" readonly="true"/>
				<emp:pop id="IqpMortCatalogMana.sup_catalog_no_displayname" label="上级目录" url="showCatalogManaTree.do?isMin=N&value=N" returnMethod="setCatalogPath" required="false" defvalue="押品目录"/> 
				<emp:text id="IqpMortCatalogMana.catalog_path" label="目录路径location" maxlength="200" required="true" hidden="true" cssElementClass="emp_field_text_readonly" defvalue="ALL"/>
				<emp:text id="IqpMortCatalogMana.catalog_path_displayname" label="目录路径"   required="true" colSpan="2" cssElementClass="emp_field_text_readonly" defvalue="押品目录"/>
				<emp:text id="IqpMortCatalogMana.catalog_lvl" label="押品目录层级" maxlength="1" required="true" dataType="Int" defvalue="1" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="IqpMortCatalogMana.attr_type" label="类型属性 " required="true" dictname="STD_ZB_ATTR_TYPE" onchange="changeAttrType(this.value)" defvalue="01" readonly="true"/>
				<emp:text id="IqpMortCatalogMana.model" label="型号" required="true" maxlength="40"/>
				<emp:text id="IqpMortCatalogMana.imn_rate" label="基准质押率" maxlength="16" required="true" dataType="Percent" />
				<emp:textarea id="IqpMortCatalogMana.memo" label="备注" maxlength="200" required="false" colSpan="2" />
				<emp:text id="IqpMortCatalogMana.input_id_displayname" label="登记人"  required="true" readonly="true"/>
				<emp:text id="IqpMortCatalogMana.input_br_id_displayname" label="登记机构"   required="true" readonly="true" />
				<emp:text id="IqpMortCatalogMana.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
				<emp:text id="IqpMortCatalogMana.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
				<emp:date id="IqpMortCatalogMana.input_date" label="登记日期" required="true" readonly="true"/>
				<emp:select id="IqpMortCatalogMana.status" label="状态" required="true" dictname="STD_ZB_STATUS" defvalue="2" readonly="true"/>
			</emp:gridLayout>
			
			<div align="center">
				<br>
				<emp:button id="submitIqpMortCatalogMana" label="保存" op="update"/>
				<emp:button id="reset" label="重置"/>
				<emp:button id="return" label="返回"/>
			</div>
		</emp:form>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
