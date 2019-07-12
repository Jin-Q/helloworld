<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");//出库管理菜单时的操作
	}  
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>
<emp:page>
<style type="text/css">
	.emp_text3{
	border:1px solid #b7b7b7;
	width:100px;
	}  
</style>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">

	/*--user code begin--*/
	//全局变量，用来存储需要入库操作的货物编号
	var arr=new Array();
		//确定按钮事件（选中一条或多条货物记录，并对其银行认定总价进行累加）
	function doSure(){
		var data = MortCargoPledgeList._obj.getSelectedData();
		var status="";
		if(data.length>0){
			var cargo_id;//已选定的
			arr=[];//每次重新选择前，押品编号进行清空
			for(var i=0;i<data.length;i++){
				cargo_id = data[i].cargo_id._getValue();
				status = data[i].cargo_status._getValue();
				var deliv_qnt = data[i].deliv_qnt._getValue();//出库数量
				if(status!="02"){
					//alert("货物编号为："+cargo_id+"的货物处于非入库状态不能添加出库！");
					//return;
				}else{
					if(parseFloat(deliv_qnt)==0){
						alert("货物编号为："+cargo_id+"的置换数量为“0”，不能做货物置换操作！");
						return;
					}
					arr.push(cargo_id);
				}
			}
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
			var guaranty_no = MortCargoExware.guaranty_no._getValue();
			var form = document.getElementById("submitForm");
			MortCargoPledgeList._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var url = '<emp:url action="changeGaurantyStatus.do"/>?flg=4&cargo_id_str='+arr+'&guaranty_no='+guaranty_no;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,postData);
		}else{
			alert("至少选中一条货物记录！");
		}
	}
	function doCancle(){
		var data = MortCargoPledgeList._obj.getSelectedData();
		var status="";
		if(data.length>0){
			var cargo_id;//已选定的
			for(var i=0;i<data.length;i++){
				cargo_id = data[i].cargo_id._getValue();
				status = data[i].cargo_status._getValue();
				if(status!="05")
				{
					alert("货物编号为："+cargo_id+"的货物处于非入库待记账状态不能进行撤销！")
					return;
				}else{
					arr.push(cargo_id);
				}
			}
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
			var guaranty_no = MortCargoExware.guaranty_no._getValue();
			var form = document.getElementById("submitForm");
			MortCargoPledgeList._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var url = '<emp:url action="changeGaurantyStatus.do"/>?flg=cancle&cargo_id_str='+arr+'&guaranty_no='+guaranty_no;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		}else{
			alert("至少选中一条货物记录！");
		}
	}
	//货物记录新增事件
	function doGetAddMortCargoPledgePage() {
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getMortCargoPledgeAddPage.do"/>?flag=tab&guaranty_no='+guaranty_no;//flag标志非押品维护时的货物操作（tab）
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
	function doReturn() {
		<%if("hwgl".equals(menuId)){%>
		var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwgl';
		<%}else{%>
		var url = '<emp:url action="queryMortCargoExwareList.do"/>';
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doNext(){
		if(!MortCargoExware._checkAll()){
			alert("请输入必填项！");
		}else{
			var storage_total = MortCargoExware.this_exware_total._getValue();
			if(storage_total=='0'){
				alert("没有需要出库的货物记录，不能进行“保存”操作！");
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
						var url = '<emp:url action="queryMortCargoExwareList.do"/>?menuId=ckgl';
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
			MortCargoExware._toForm(form);
			var guaranty_no = MortCargoExware.guaranty_no._getValue();
			<%if("".equals(serno)||null==serno){%>
			var url = '<emp:url action="addMortCargoExwareRecord.do"/>?guaranty_no='+guaranty_no+'&cargo_id_str='+arr;
			<%}else{%>
			var url = '<emp:url action="addMortCargoExwareRecord.do"/>?guaranty_no='+guaranty_no+'&cargo_id_str='+arr+'&serno=<%=serno%>';
			<%}%>
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	};
	function doTally(){
		if(!MortCargoExware._checkAll()){
			alert("请输入必填项！");
		}else{
			var storage_total = MortCargoExware.this_exware_total._getValue();
			if(storage_total=='0'){
				alert("没有需要出库的货物记录，不能进行“记账出库”操作！");
				return;
			}
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("记账失败！");
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){
						alert("已成功记账！");
						var url = '<emp:url action="queryMortCargoExwareList.do"/>?menuId=ckgl';
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else if(flag=='little'){
						alert("出库后总价值小于押品的担保金额，不能出库！");
						return;
					}else{
						alert("记账失败！");
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("记账失败!");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			MortCargoExware._toForm(form);
			var guaranty_no = MortCargoExware.guaranty_no._getValue();
			<%if("".equals(serno)||null==serno){%>//
			var url = '<emp:url action="addMortCargoExwareRecord.do"/>?flg=2&guaranty_no='+guaranty_no+'&cargo_id_str='+arr;
			<%}else{%>
			var url = '<emp:url action="addMortCargoExwareRecord.do"/>?flg=2&guaranty_no='+guaranty_no+'&cargo_id_str='+arr+'&serno=<%=serno%>';;
			<%}%>
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	};

	function doLoad(){
		doCacul();
		var agr_type = MortCargoExware.agr_type._getValue();
		var labelName = '';
		if(agr_type=='00'){
			labelName = '保兑仓协议编号';
		}else if(agr_type=='01'){
			labelName = '银企商合作协议编号';
		}else{
			labelName = '监管协议编号';
		}
		$(document).ready(function(){
			$(".emp_field_label:eq(2)").text(labelName);
		 });
	}

	//根据置换数量计算置换价值、剩余数量、剩余价值
	function doCacul(){
		MortCargoPledgeList._obj.selectAll();
	  	var data = MortCargoPledgeList._obj.getSelectedData();
     	for(var i=0;i<data.length;i++){
			var qnt=data[i].qnt._getValue();//在库数量
			var deliv_qnt=data[i].deliv_qnt._getValue();//置换数量  
			var identy_unit_price = data[i].identy_unit_price._getValue();//银行认定单价
			var identy_total  = data[i].identy_total._getValue();//银行认定总价
			if(parseFloat(deliv_qnt)>parseFloat(qnt)){
				alert("出库数量需不大于在库数量");
				<%if("hwgl".equals(menuId)){%>
				var url = '<emp:url action="queryMortGuarantyBaseInfoList.do"/>?menuId=hwgl';
				<%}else{%>
				var url = '<emp:url action="queryMortCargoExwareList.do"/>';
				<%}%>
				url = EMPTools.encodeURI(url);
				window.location=url;
			}else{
				data[i].deliv_value._setValue((parseFloat(deliv_qnt)*parseFloat(identy_unit_price)).toString());//提货总价
				data[i].surplus_qnt._setValue((parseFloat(qnt)-parseFloat(deliv_qnt)).toString());//剩余数量
			 	data[i].surplus_value._setValue((parseFloat(identy_total)-(parseFloat(deliv_qnt)*parseFloat(identy_unit_price))).toString());//剩余总价值
			}
   	  	}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortCargoExwareRecord.do" method="POST">
		
		<emp:gridLayout id="MortCargoExwareGroup" title="货物出库管理" maxColumn="2">
			<emp:text id="MortCargoExware.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortCargoExware.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoExware.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoExware.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoExware.cus_id_displayname" label="出质人客户名称" required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:text id="MortCargoExware.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoExware.this_exware_total" label="此次出库总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoExware.exware_total" label="出库后总价值" maxlength="18" required="false" dataType="Currency" readonly="true" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="MortCargoExware.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoExware.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortCargoExware.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoExwareGroup" title="登记信息" maxColumn="2">
			<emp:text id="MortCargoExware.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoExware.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true" />
			<emp:text id="MortCargoExware.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortCargoExware.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			<emp:date id="MortCargoExware.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="MortCargoExware.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("update".equals(op)||"out_storage".equals(op)){ %>
			<emp:button id="next" label="保存"/>
			<emp:button id="tally" label="记账出库"/>
			<%} %>
			<emp:button id="return" label="返回"/>
		</div>
		</emp:form>
		<div class='emp_gridlayout_title'>待出库货物信息</div>
		<div align="left">
			<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		<%if("update".equals(op)||"out_storage".equals(op)){ %>
			<emp:button id="sure" label="确定" />
			<emp:button id="cancle" label="撤销" />
		<%} %>
		</div>
	
		<!--<emp:table icollName="MortCargoPledgeList" pageMode="true" url="pageMortCargoPledgeQuery.do?status=ck&guarantyNo=${context.MortCargoExware.guaranty_no}" selectType="2">
			<emp:text id="cargo_id" label="货物编号" />
			<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true"/>
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
			<emp:text id="cargo_name" label="货物名称" />
			<emp:text id="identy_total" label="银行认定总价" />
			<emp:text id="storage_date" label="入库日期" />
			<emp:text id="exware_date" label="出库日期" />
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
			<emp:text id="reg_date" label="登记日期" />
		</emp:table>
	-->

		<emp:table icollName="MortCargoPledgeList" pageMode="true" url="pageMortCargoPledgeQuery.do?status=ck&guarantyNo=${context.MortCargoExware.guaranty_no}" selectType="2">
			<emp:text id="cargo_id" label="货物编号" />
			<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true"/>
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
			<emp:text id="cargo_name" label="货物名称" />
			<emp:text id="qnt" label="在库数量" readonly="true" dataType="Currency"/>
			<emp:text id="identy_unit_price" label="单价" dataType="Currency" />
			<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
			
			<emp:text id="deliv_qnt" label="出库数量" dataType="Double" onblur="doCacul()" defvalue="0" required="false" flat="true" cssElementClass="emp_text3" />
			<emp:text id="deliv_value" label="出库价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency"  defvalue="0"/>
			
			<emp:text id="storage_date" label="入库日期" hidden="true"/>
			<emp:text id="exware_date" label="出库日期" hidden="true"/>
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
			<emp:text id="reg_date" label="登记日期" />
			<emp:text id="serno" label="流水" hidden="true"/>
		</emp:table>

</body>
</html>
</emp:page>

