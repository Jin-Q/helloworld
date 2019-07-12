<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:450px;
}

</style>
<script type="text/javascript">
	function doAddCusComResset(){
		var form = document.getElementById("submitForm");
		var result = CusComResset._checkAll();
		if(result){
			CusComResset._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
	}
	
	function toSubmitForm(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var editFlag = '${context.EditFlag}';
						var paramStr="CusComResset.cus_id="+CusComResset.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusComRessetAddPage.do"/>&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
				 }else {
					 alert(flag);
					 return;
				 }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	
	function goback(){
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComResset.cus_id="+CusComResset.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusComRessetList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
		/*--user code begin--*/
	function doReturn(){
		goback();
	}	

	//输入日期不能大于当前日期
	function CheckDate(obj,errMsg){
		var str_date=obj._getValue();
		var openDay = '${context.OPENDAY}';
		if( str_date==""|| str_date==null){
	        return;
		}
		if(str_date>openDay){
			alert(errMsg);
		    obj._obj.element.value="";
		}
	}

	function onReturnRegStateCode(date){
		CusComResset.com_ass_addr._obj.element.value=date.id;
		CusComResset.com_ass_addr_displayname._obj.element.value=date.label;
	}

	//资产类别为"土地使用权"、"房产"时地址资产地址为必输
	function changeAssTyp(){
		var com_ass_typ = CusComResset.com_ass_typ._getValue();
		if(com_ass_typ == '01' ||com_ass_typ == '03'){
			CusComResset.com_ass_addr_displayname._obj._renderRequired(true);
			CusComResset.street._obj._renderRequired(true);
		}else{
			CusComResset.com_ass_addr_displayname._obj._renderRequired(false);
			CusComResset.street._obj._renderRequired(false);
		}
	}

	function doLoad(){
		changeAssTyp();
	}
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="addCusComRessetRecord.do" method="POST">
		<emp:gridLayout id="CusComRessetGroup" title="固定资产信息" maxColumn="2">
			<emp:text id="CusComResset.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusComResset.seq" label="序号" maxlength="38" readonly="true" required="false" hidden="true"/>
			<emp:text id="CusComResset.com_ass_name" label="资产名称" maxlength="60" required="true" />
			<emp:select id="CusComResset.com_ass_typ" label="资产类别" required="true" dictname="STD_ZB_COM_ASS_TYP" onchange="changeAssTyp()"/>
			<emp:date id="CusComResset.com_ass_buy_date" label="资产购置日期"  required="true" onblur="CheckDate(CusComResset.com_ass_buy_date,'资产购置日期不能大于当前日期')"/>
			<emp:text id="CusComResset.com_ass_ori_amt" label="资产购置原价(元)" required="true" maxlength="18"  dataType="Currency"/>
			<emp:text id="CusComResset.com_ass_fat_area" label="实际面积(平方米)" maxlength="16" required="false"  dataType="Int"/>
			<emp:text id="CusComResset.com_ass_reg_area" label="已办证面积(平方米)" maxlength="16" required="false" dataType="Int"/>
			<emp:text id="CusComResset.com_ass_number" label="数量" maxlength="30" required="false" dataType="Int" />
			<emp:text id="CusComResset.com_ass_eva_amt" label="评估价值(元)" maxlength="18" required="false" dataType="Currency"/>
			<emp:select id="CusComResset.com_ass_coll_info" label="抵押状况" required="true" dictname="STD_ZB_ASS_COLL_INFO"/>
			<emp:text id="CusComResset.zmje" label="账面金额(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="CusComResset.com_ass_addr" label="资产地址"  hidden="true"/>
			<emp:pop id="CusComResset.com_ass_addr_displayname" label="资产地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRegStateCode" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusComResset.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2" maxlength="100"/>
			<emp:textarea id="CusComResset.remark" label="备注" maxlength="250" required="false" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusComResset.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComResset.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:text id="CusComResset.input_date" label="登记日期" maxlength="10" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComResset.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComResset.last_upd_date" label="更新日期" maxlength="10" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusComResset" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

