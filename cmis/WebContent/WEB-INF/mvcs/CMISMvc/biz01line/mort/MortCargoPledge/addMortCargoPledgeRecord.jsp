<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String action = "";
	String storage_mode = "";
	if(context.containsKey("flag")){//用来控制按钮的显示（flag=tab时，显示关闭按钮）
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("action")){//当商链通时，action=zh货物置换时，新增货物
		action = (String)context.getDataValue("action");
	}
	if(context.containsKey("storage_mode")){//当商链通时，入库方式。（补货和初次入库新增时，插入的表不一样）
		storage_mode = (String)context.getDataValue("storage_mode");
	}
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doCalcu(){
		var amount = MortCargoPledge.qnt._getValue();
		var unit_price = MortCargoPledge.identy_unit_price._getValue();
		var total_price = parseFloat(unit_price)*parseFloat(amount);
		MortCargoPledge.identy_total._setValue(total_price.toString());
	}
	//关闭按钮事件
	function doClose(){
		window.close();
	}
	//保存按钮事件
	function doNext(){
		if(!MortCargoPledge._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
						alert("保存成功!");
						<%if(flag.equals("tab")){%>
						 window.opener.location.reload();
						 window.close();
						<%}else{%>
						var guaranty_no = MortCargoPledge.guaranty_no._getValue();
						var url = '<emp:url action="queryMortCargoPledgeList.do"/>?guaranty_no='+guaranty_no;
						url = EMPTools.encodeURI(url);
						window.location=url;
						<%}%>
					}else{
						alert(msg);
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			MortCargoPledge._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);	
			<%if (action.equals("zh")){%>
			    var url = '<emp:url action="addMortCargoPledgeRecord.do"/>?action=zh';//货物置换时新增的货物记录存储在货物置换清单中
			<%}else if("".equals(storage_mode)){%>
				var url = '<emp:url action="addMortCargoPledgeRecord.do"/>';
			<%}else{%>
			    var storage_mode = '<%=storage_mode%>';
				var url = '<emp:url action="addMortCargoPledgeRecord.do"/>?storage_mode='+storage_mode;
			<%}%>
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);
		}
	}
	function doLoad(){
		
	}
	//押品所属目录返回方法
	function getCatalog(data){
		MortCargoPledge.value_no._setValue(data.value_no._getValue());
		MortCargoPledge.value_unit._setValue(data.unit._getValue());
		MortCargoPledge.guaranty_catalog._setValue(data.catalog_no._getValue());
		MortCargoPledge.guaranty_catalog_displayname._setValue(data.catalog_no_displayname._getValue());
		MortCargoPledge.produce_vender._setValue(data.produce_vender._getValue());
		MortCargoPledge.produce_area._setValue(data.produce_area._getValue());
		MortCargoPledge.produce_area_displayname._setValue(data.produce_area_displayname._getValue());
		MortCargoPledge.sale_area._setValue(data.sale_area._getValue());
		MortCargoPledge.sale_area_displayname._setValue(data.sale_area_displayname._getValue());
		MortCargoPledge.market_value._setValue(data.market_value._getValue());
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("fail" == flag){
					alert("此货物的押品所属目录非抵质押物押品类型项下目录类型，不能进行新增!");
					MortCargoPledge.value_no._setValue("");
					MortCargoPledge.value_unit._setValue("");
					MortCargoPledge.guaranty_catalog._setValue("");
					MortCargoPledge.guaranty_catalog_displayname._setValue("");
					MortCargoPledge.produce_vender._setValue("");
					MortCargoPledge.produce_area._setValue("");
					MortCargoPledge.produce_area_displayname._setValue("");
					MortCargoPledge.sale_area._setValue("");
					MortCargoPledge.sale_area_displayname._setValue("");
					MortCargoPledge.market_value._setValue("");
					return;
				}else{
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var guaranty_no = MortCargoPledge.guaranty_no._getValue();
		var guaranty_catalog = data.catalog_no._getValue();
		var url = '<emp:url action="checkGuarantyCatalog.do"/>?guaranty_no='+guaranty_no+'&guaranty_catalog='+guaranty_catalog;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function doReturn() {
		var guaranty_no = MortCargoPledge.guaranty_no._getValue();
		var url = '<emp:url action="queryMortCargoPledgeList.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doReset(){
		page.dataGroups.MortCargoPledgeGroup.reset();
	};
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="addMortCargoPledgeRecord.do" method="POST">
		<emp:gridLayout id="MortCargoPledgeGroup" title="货物质押" maxColumn="2">
			<emp:text id="MortCargoPledge.cargo_id" label="货物编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoPledge.cargo_name" label="货物名称" maxlength="60" required="true" readonly="false"/>
			<emp:text id="MortCargoPledge.value_no" label="价格编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortCargoPledge.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:pop id="MortCargoPledge.guaranty_catalog_displayname" label="押品所属目录" url="queryIqpMortValueManaPopList.do?returnMethod=getCatalog" required="true" colSpan="2" cssElementClass="emp_field_text_long"/>
			<emp:text id="MortCargoPledge.guaranty_catalog" label="押品所属目录ID" required="true" hidden="true" />
			<emp:text id="MortCargoPledge.produce_vender" label="生产厂家" maxlength="200" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortCargoPledge.produce_area" label="产地代码" maxlength="200" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoPledge.produce_area_displayname" label="产地" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly" />
			<emp:text id="MortCargoPledge.sale_area" label="销售区域ID" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoPledge.sale_area_displayname" label="销售区域" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortCargoPledge.at_store" label="所属仓库" maxlength="20" required="false" />
			<emp:text id="MortCargoPledge.disp_bill_no" label="发货单号" maxlength="40" required="false" />
			<emp:select id="MortCargoPledge.value_unit" label="计价单位" required="true" dictname="STD_ZB_UNIT" readonly="true"/>
			<emp:text id="MortCargoPledge.market_value" label="市场价（元）" maxlength="18" required="true" dataType="Currency" onblur="doCalcu()" defvalue="0" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="MortCargoPledge.qnt" label="数量" maxlength="18" required="true" dataType="Currency" onblur="doCalcu()" defvalue="0" colSpan="2"/>
			<emp:text id="MortCargoPledge.identy_unit_price" label="银行认定单价（元）" maxlength="18" required="true" dataType="Currency" onblur="doCalcu()" defvalue="0"/>
			<emp:text id="MortCargoPledge.identy_total" label="银行认定总价（元）" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="MortCargoPledge.storage_date" label="入库日期" required="false" readonly="true"/>
			<emp:date id="MortCargoPledge.exware_date" label="出库日期" required="false" readonly="true"/>
			<emp:select id="MortCargoPledge.cargo_status" label="状态" required="false" dictname="STD_CARGO_STATUS" readonly="true" defvalue="01" colSpan="2"/>
			<emp:textarea id="MortCargoPledge.memo" label="备注" maxlength="600" required="false" colSpan="2" />
			<emp:text id="MortCargoPledge.model" label="型号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:text id="MortCargoPledge.input_id_displayname" label="登记人"  required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="MortCargoPledge.input_br_id_displayname" label="登记机构"   required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="MortCargoPledge.input_id" label="登记人" maxlength="40" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoPledge.input_br_id" label="登记机构" maxlength="40" defvalue="$organNo" hidden="true"/>
			<emp:date id="MortCargoPledge.reg_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="next" label="保存"/>
			<%if(flag.equals("tab")){ %>
			<emp:button id="close" label="关闭"/>
			<%}else{ %>
			<emp:button id="return" label="返回"/>
			<emp:button id="reset" label="重置"/>
			<%} %>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

