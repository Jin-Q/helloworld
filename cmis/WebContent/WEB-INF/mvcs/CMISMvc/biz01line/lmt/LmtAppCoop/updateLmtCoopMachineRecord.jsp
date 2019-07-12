<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<html>
<head>
<title>合作方授信机械设备信息-修改页面</title>
<style type="text/css">
	.emp_field_text_write {
		border: 1px solid #BCD7E2;
		text-align: left;
		width: 240px;
	};
	.emp_field_mobile_write {
		border: 1px solid #BCD7E2;
		text-align: left;
		width: 130px;
	};
</style>
<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "add"; 
	if(context.containsKey("op")){
		op = context.getDataValue("op").toString();
	}
	if("view".equals(op)){  //如果为查看页面控件只读
		request.setAttribute("canwrite","");
	}
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	function onLoad(){
		//剔除对公客户的证件类型
		var options = LmtCoopMachine.cert_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "20" || options[i].value == "22" ||options[i].value == "23"){
				options.remove(i);
			}
		}
	}
	
	//校验证件号码的合法性
	function checkCertCode(){
		var cert_type = LmtCoopMachine.cert_type._getValue();
		if("100"==cert_type){ //身份证 
			var value = LmtCoopMachine.cert_code._getValue();
			var length = value.length;
			if (length != 15 && length != 18){
			    alert("身份证号码，长度必须为15位或18位！");
			    return false;
			}
		  	if (length == 15){//15位的身份证号
		    	var reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					alert("身份证号码格式不正确！");
					return false;
				}
		    }else if (length == 18){
		        var reg = /^[1-9]\d{6}[1-9]\d{2}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9|x|X])$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					alert("身份证号码格式不正确！");
					return false;
				}
		    }
		}
	}

	//异步提交申请数据
	function doSubmitLmtCoopMachin(){
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
					//document.getElementById("button_submitLmtCoopMachine").disabled = "";
					//var form = document.getElementById("submitForm");
					//var url = '<emp:url action="updateLmtCoopMachineRecord.do"/>';
					//url = EMPTools.encodeURI(url);
					//form.action = url;
					window.location.reload();
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
		var result = LmtCoopMachine._checkAll();
		if(result){
			var recordCount = LmtSchedEquipList._obj.recordCount;//取总记录数
			if(parseFloat(recordCount)==0){
				alert('拟按揭设备未录入！');
				return;
			}
			var message = "";
			//循环校验记录的必输项是否录入完成
			for(var i=0;i<recordCount;i++){
				var equip_name = LmtSchedEquipList._obj.data[i].equip_name._getValue();
				var brand = LmtSchedEquipList._obj.data[i].brand._getValue();
				var model = LmtSchedEquipList._obj.data[i].model._getValue();
				var sale_value = LmtSchedEquipList._obj.data[i].sale_value._getValue();

				if(equip_name == "" || brand == "" || model == "" || sale_value == ""){
					message += "第"+ (i+1) + "条记录";
				}
				if(equip_name == "" ){
					message += "[设备名称]、";
				}
				if(brand == "" ){
					message += "[设备品牌]、";
				}
				if(model == "" ){
					message += "[型号]、";
				}
				if(sale_value == "" ){
					message += "[销售价格]";
				}
				if("" != message){
					message = message+"为空！\n";
				}
			}
			if("" != message){
				alert(message);
				return false;
			}

			LmtSchedEquipList._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			//document.getElementById("button_submitLmtCoopMachine").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("页面信息录入不完整！");
           return ;
		}
	};	

	//新增机械设备合作方拟按揭设备信息
	function doAddLmtSchedEquip(){
		var pro_no = LmtCoopMachine.pro_no._getValue();  //得到项目编号
		LmtSchedEquipList._obj._addRow();
		LmtSchedEquipList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = LmtSchedEquipList._obj.recordCount;//取总记录数
		LmtSchedEquipList._obj.data[recordCount-1].pro_no._setValue(pro_no);//设置项目编号的值

		//LmtSchedEquipList._obj._deleteRow(recordCount+1);   //删除最后“无符合条件的记录！”行(不能使用)
	}

	//删除机械设备合作方拟按揭设备信息
	function doDelLmtSchedEquip(){
		var dataRow =  LmtSchedEquipList._obj.getSelectedData()[0];  
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var idx = LmtSchedEquipList._obj.getSelectedIdx();  //得到选中行的下标
				LmtSchedEquipList._obj._deleteRow(idx);   //删除选中行
				LmtSchedEquipList._obj.recordCount -=1; 	//减少总记录数
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	//校验联系方式
	function checkLinkMode(){
		var link_mode = LmtCoopMachine.link_mode._getValue();
		if(link_mode!=null&&link_mode!=''){
		// 	var reg = /^(0?[0-9]{2,3}-?){0,1}[0-9]{7,8}(-[0-9]{1,4}){0,1}$/;
		 	var reg = /(^(\d{3,4}-)?\d{7,8})$|(1[0-9]{10})$/;
	    	var checkres = reg.test(link_mode);
			if (!checkres) {
				alert('联系方式填写不正确！');
				LmtCoopMachine.link_mode._setValue('');
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:form id="submitForm" action="${context.action}" method="POST">
		<emp:gridLayout id="LmtCoopMachineGroup" maxColumn="2" title="机械设备信息">
			<emp:text id="LmtCoopMachine.serno" label="流水号" maxlength="40" hidden="true" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_readonly" />
			<emp:text id="LmtCoopMachine.pro_no" label="项目编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtCoopMachine.pro_name" label="项目名称" maxlength="60" required="true" cssElementClass="emp_field_text_write" colSpan="2"/>
			<emp:text id="LmtCoopMachine.chief_name" label="负责人姓名" maxlength="60" required="true" />
			<emp:select id="LmtCoopMachine.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" />
			<emp:text id="LmtCoopMachine.cert_code" label="证件号码" maxlength="20" required="true" onblur="checkCertCode()"/>
			<emp:text id="LmtCoopMachine.link_mode" label="联系方式" maxlength="11" required="true" onblur="checkLinkMode()" cssElementClass="emp_field_mobile_write"/>
			<emp:text id="LmtCoopMachine.rebuy_guar" label="回购担保总额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="LmtCoopMachine.bail_rate" label="保证金比例" maxlength="16" required="false" dataType="Percent"/>
			<emp:text id="LmtCoopMachine.single_quota" label="单户按揭限额" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="LmtCoopMachine.single_term" label="单户按揭期限(月)" maxlength="4" required="false" dataType="Int"/>
		</emp:gridLayout>
		
		<div class='emp_gridlayout_title'>拟按揭设备&nbsp;</div>
		<% if(!"view".equals(op)){ %>
		<div id="tempButton" align="left" >
			<emp:button id="addLmtSchedEquip" label="新增" op="add"/>
			<emp:button id="delLmtSchedEquip" label="删除" op="remove" locked="false"/>
		</div>
		<%} %>
		<emp:table icollName="LmtSchedEquipList" editable="true" pageMode="false" url="">
			<emp:text id="pro_no" label="项目编号" flat="true" required="true" readonly="true"/>
			<emp:text id="equip_name" label="设备名称" required="true" />
			<emp:text id="brand" label="设备品牌" required="true"/>
			<emp:text id="model" label="型号" required="true" />
			<emp:text id="sale_value" label="销售价格" required="true" dataType="Currency"/> 
		</emp:table>
		<% if(!"view".equals(op)){ %>
		<div align="center">
			<br>
			<emp:button id="submitLmtCoopMachin" label="保存" op="update"/>
			<emp:button id="reset" label="重置" op="update"/>
		</div>
		<%} %>
	</emp:form>
</body>
</html>
</emp:page>
