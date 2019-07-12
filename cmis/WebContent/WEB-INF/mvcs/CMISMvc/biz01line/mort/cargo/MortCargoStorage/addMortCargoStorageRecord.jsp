<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if("view".equals(op)){
			request.setAttribute("canwrite","");
		}
	}
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
	//全局变量，用来存储需要入库操作的货物编号
	var arr=new Array();
	//根据入库方式的不同，页面显示不同的字段
	function doChange(){
		var guaranty_info_status = '${context.guaranty_info_status}';
		var storage_mode = MortCargoStorage.storage_mode._getValue();
		if(storage_mode == null || storage_mode == ''){
			if('2'==guaranty_info_status){
				MortCargoStorage.storage_mode._setValue("00");
			}else if('3'==guaranty_info_status){
				MortCargoStorage.storage_mode._setValue("01");
			}
		}
		var flag =MortCargoStorage.storage_mode._getValue();
		if(flag=="00"){
			MortCargoStorage.need_reple_total._obj._renderHidden(true);	
			MortCargoStorage.act_reple_total._obj._renderHidden(true);	
			MortCargoStorage.need_reple_total._setValue("");
			MortCargoStorage.act_reple_total._setValue("");
		}else if(flag=="01"){
			MortCargoStorage.need_reple_total._obj._renderHidden(false);	
			MortCargoStorage.act_reple_total._obj._renderHidden(false);	
		}
	}
	function doLoad(){
		doChange();
		var agr_type = MortCargoStorage.agr_type._getValue();
		var labelName = '';
		if(agr_type=='00'){
			labelName = '保兑仓协议编号';
		}else if(agr_type=='01'){
			labelName = '银企商合作协议编号';
		}else{
			labelName = '监管协议编号';
		}
		$(document).ready(function(){
			$(".emp_field_label:eq(1)").text(labelName);
		 });
	}
	//确定按钮事件（选中一条或多条货物记录，并对其银行认定总价进行累加）
	function doSure(){
		var data = MortCargoPledgeList._obj.getSelectedData();
		var total_price = 0;
		var status="";
		if(data.length>0){
			var unit;//每条货物的银行认定总价
			var cargo_id;//已选定的
			arr=[];//每次重新选择前，押品编号进行清空
			for(var i=0;i<data.length;i++){
				unit = parseFloat(data[i].identy_total._getValue());
				cargo_id = data[i].cargo_id._getValue();
				status = data[i].cargo_status._getValue();
				if(status!="01")
				{
					alert("货物编号为："+cargo_id+"的货物处于非登记状态不能添加入库！")
				}else{
					arr.push(cargo_id);
					total_price+=unit;
				}
			}
			var storage_total = MortCargoStorage.storage_total._getValue();
			total_price += parseFloat(storage_total);
			MortCargoStorage.after_storage_total._setValue(total_price.toString());
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						window.location.reload();
					}else{
						alert("押品状态修改失败！");
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var storage_mode = MortCargoStorage.storage_mode._getValue();
			var url = '<emp:url action="changeGaurantyStatus.do"/>?flg=1&storage_mode='+storage_mode+'&cargo_id_str='+arr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		}else{
			alert("至少选中一条货物记录！");
		}
	}
	function doCancle(){
		var data = MortCargoPledgeList._obj.getSelectedData();
		var total_price = MortCargoStorage.after_storage_total._getValue();
		var status="";
		if(data.length>0){
			var unit;//每条货物的银行认定总价
			var cargo_id;//已选定的
			for(var i=0;i<data.length;i++){
				unit = parseFloat(data[i].identy_total._getValue());
				cargo_id = data[i].cargo_id._getValue();
				status = data[i].cargo_status._getValue();
				if(status!="04")
				{
					alert("货物编号为："+cargo_id+"的货物处于非入库待记账状态不能进行撤销！")
				}else{
					arr.push(cargo_id);
					total_price-=unit;
				}
			}
			var storage_total = MortCargoStorage.storage_total._getValue();
			MortCargoStorage.after_storage_total._setValue(total_price.toString());
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						window.location.reload();
					}else{
						alert("押品状态修改失败！");
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var storage_mode = MortCargoStorage.storage_mode._getValue();
			var url = '<emp:url action="changeGaurantyStatus.do"/>?flg=2&storage_mode='+storage_mode+'&cargo_id_str='+arr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		}else{
			alert("至少选中一条货物记录！");
		}
	}
	//货物记录新增事件
	function doGetAddMortCargoPledgePage() {
		var guaranty_no = '${context.guaranty_no}';
		var storage_mode = MortCargoStorage.storage_mode._getValue();
		var url = '<emp:url action="getMortCargoPledgeAddPage.do"/>?flag=tab&storage_mode='+storage_mode+'&guaranty_no='+guaranty_no;//flag标志非押品维护时的货物操作（tab）
		url = EMPTools.encodeURI(url);
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	//货物记录删除事件
	function doDeleteMortCargoPledge() {
		var data = MortCargoPledgeList._obj.getSelectedData();
		if (data.length==1) {
			var cargo_status = data[0].cargo_status._getValue();
			if("01"!=cargo_status){
				alert("非登记状态的货物记录不能进行“删除”操作！");
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							window.location.reload();
						}else{
							alert("货物信息删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var cargo_id = data[0].cargo_id._getValue();
				var url = '<emp:url action="deleteMortCargoPledgeRecord.do"/>?cargo_id='+cargo_id;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//货物记录修改事件
	function doGetUpdateMortCargoPledgePage() {
		var data = MortCargoPledgeList._obj.getSelectedData();
		if (data.length==1) {
			var cargo_status = data[0].cargo_status._getValue();
			if("01"!=cargo_status){
				alert("非登记状态的货物记录不能进行“修改”操作！");
				return;
			}
			var cargo_id = data[0].cargo_id._getValue();
			var url = '<emp:url action="getMortCargoPledgeUpdatePage.do"/>?flag=tab&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//货物记录查看事件
	function doViewMortCargoPledge() {
		var data = MortCargoPledgeList._obj.getSelectedData();
		if (data.length ==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReturn(){
		<%if("hwgl".equals(menuId)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwgl';
		<%}else if("bctx_fxyj".equals(menuId)){%>
			history.go(-1);
			return;
		<%}else{%>
		var url = '<emp:url action="queryMortCargoStorageList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	function doNext(){
		if(!MortCargoStorage._checkAll()){
			alert("请输入必填项！");
		}else{
			var storage_total = MortCargoStorage.storage_total._getValue();
			var after_storage_total = MortCargoStorage.after_storage_total._getValue();
			if(storage_total==after_storage_total){
				alert("没有需要入库的货物记录，不能进行“保存”操作！");
				return;
			}
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("保存成功");
						var url = '<emp:url action="queryMortCargoStorageList.do"/>?menuId=rkgl';
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
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
			var form = document.getElementById('submitForm');
			MortCargoStorage._toForm(form);
			MortCargoPledgeList._toForm(form);
			var guaranty_no = MortCargoStorage.guaranty_no._getValue();
			<%if("".equals(serno)||null==serno){%>
			var url = '<emp:url action="addMortCargoStorageRecord.do"/>?guaranty_no='+guaranty_no+'&cargo_id_str='+arr;
			<%}else{%>
			var url = '<emp:url action="addMortCargoStorageRecord.do"/>?guaranty_no='+guaranty_no+'&cargo_id_str='+arr+'&serno=<%=serno%>';
			<%}%>
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	};
	function doTally(){
		if(!MortCargoStorage._checkAll()){
			alert("请输入必填项！");
		}else{
			var storage_total = MortCargoStorage.storage_total._getValue();
			var after_storage_total = MortCargoStorage.after_storage_total._getValue();
			if(storage_total==after_storage_total){
				alert("没有需要入库的货物记录，不能进行“记账入库”操作！");
				return;
			}
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("保存成功");
						var url = '<emp:url action="queryMortCargoStorageList.do"/>?menuId=rkgl';
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
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
			var form = document.getElementById('submitForm');
			MortCargoStorage._toForm(form);
			var guaranty_no = MortCargoStorage.guaranty_no._getValue();
			<%if("".equals(serno)||null==serno){%>
			var url = '<emp:url action="addMortCargoStorageRecord.do"/>?flg=2&guaranty_no='+guaranty_no;
			<%}else{%>
			var url = '<emp:url action="addMortCargoStorageRecord.do"/>?flg=2&guaranty_no='+guaranty_no+'&serno=<%=serno%>';;
			<%}%>
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortCargoStorageRecord.do" method="POST">
		<emp:gridLayout id="MortCargoStorageGroup" title="货物入库管理" maxColumn="2">
			<emp:text id="MortCargoStorage.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoStorage.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoStorage.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortCargoStorage.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoStorage.cus_id_displayname" label="出质人客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
			<emp:select id="MortCargoStorage.storage_mode" label="入库方式" required="true" dictname="STD_ZB_STORAGE_MODE" colSpan="2" readonly="true"/>
			<emp:text id="MortCargoStorage.storage_total" label="库存总价值" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoStorage.need_reple_total" label="需补货总价值" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="MortCargoStorage.act_reple_total" label="实际补货总价值" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="MortCargoStorage.after_storage_total" label="入库后总价值" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:date id="MortCargoStorage.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoStorage.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortCargoStorage.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoStorageGroup" title="登记信息" maxColumn="2">
			<emp:text id="MortCargoStorage.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoStorage.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true" />
			<emp:text id="MortCargoStorage.input_id_displayname" label="登记人"  required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortCargoStorage.input_br_id_displayname" label="登记机构"  required="true" readonly="true" defvalue="$organName"/>
			<emp:date id="MortCargoStorage.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="MortCargoStorage.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
		<%if("in_storage".equals(op)||"update".equals(op)){ %>
			<emp:button id="next" label="保存"/>
			<emp:button id="tally" label="记账入库"/>
		<%} %>
		<%if("to_storage".equals(op)){ %>
			<emp:button id="tally" label="记账入库"/>
		<%} %>
			<emp:button id="return" label="返回"/>
		</div>
    </emp:form>
	<div class='emp_gridlayout_title'>待入库货物信息</div>
	<div align="left">
	   <%if("in_storage".equals(op)||"update".equals(op)){ %>
		<emp:button id="getAddMortCargoPledgePage" label="新增" />
		<emp:button id="getUpdateMortCargoPledgePage" label="修改"/>
		<emp:button id="deleteMortCargoPledge" label="删除"/>
		<%} %>
		<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
	   <%if("in_storage".equals(op)||"update".equals(op)){ %>
		<emp:button id="sure" label="确定"/>
		<emp:button id="cancle" label="撤销" />
		<%} %>
	</div>

	<emp:table icollName="MortCargoPledgeList" pageMode="true" url="pageMortCargoPledgeQuery.do?status=rk&guarantyNo=${context.MortCargoStorage.guaranty_no}" selectType="2">
		<emp:text id="cargo_id" label="货物编号" />
		<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true"/>
		<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
		<emp:text id="cargo_name" label="货物名称" />
		<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
		<emp:text id="storage_date" label="入库日期" />
		<emp:text id="exware_date" label="出库日期" />
		<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
		<emp:text id="reg_date" label="登记日期" />
	</emp:table>
		

	
</body>
</html>
</emp:page>

