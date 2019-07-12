<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String op= "";
if(context.containsKey("op")){
	op = (String)context.getDataValue("op");
}
if("view".equals(op)||"to_storage".equals(op)){
	request.setAttribute("canwrite","");
}
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAdd(){
		var form = document.getElementById('submitForm');
		MortLandMgrRightDetail._toForm(form);
		if(!MortLandMgrRightDetail._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						var guaranty_no = '${context.guaranty_no}';
						if(guaranty_no == null || guaranty_no == ''){
							var tab = document.getElementById("emp_table_MortLandBelongsList_table");
							guaranty_no = tab.rows[2].childNodes[2].innerText;
						}
						var collateral_type_cd = '${context.collateral_type_cd}';
						alert("保存成功");
						var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no+'&collateral_type_cd='+collateral_type_cd;
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("保存失败!");
				document.getElementById("button_add").disabled="";
				document.getElementById("button_reset").disabled="";
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}
	function doGetAddPage4Land(){
		var guaranty_no = MortLandMgrRightDetail.guaranty_no._getValue();
		var url = '<emp:url action="getMortLandBelongsAddPage.do"/>?op=add&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	function doDeleteLandRecord(){
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
				if("success" == flag){
					alert("删除成功！");
					window.location.reload();
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var a = MortLandBelongsList._obj.getSelectedData()[0].land_id._getValue();
		var paramStr = MortLandBelongsList._obj.getParamStr(['land_id']);
		if (paramStr != null) {
			var url = '<emp:url action="deleteMortLandBelongsRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortLandMgrRightDetailRecord.do" method="POST">
			<emp:text id="MortLandMgrRightDetail.land_id" label="主键" maxlength="30" required="true"  hidden="true"/>
		<emp:gridLayout id="MortLandMgrRightDetailGroup" title="土地经营权登记表" maxColumn="2">
			<emp:text id="MortLandMgrRightDetail.main_land_name" label="发包方全称" maxlength="500" required="true" />
			<emp:text id="MortLandMgrRightDetail.guaranty_no" label="押品编号" maxlength="30" required="true" readonly="true"/>
			<emp:text id="MortLandMgrRightDetail.ctr_land_name" label="承包方姓名" maxlength="100" required="true" />
			<emp:text id="MortLandMgrRightDetail.cert_no" label="身份证号码" maxlength="20" required="true" />
			<emp:text id="MortLandMgrRightDetail.ctr_land_addr" label="承包方住址" maxlength="500" required="true" colSpan="2"/>
			<emp:text id="MortLandMgrRightDetail.ctr_land_con_no" label="土地承包合同编号" maxlength="30" required="true" colSpan="2"/>
			<emp:date id="MortLandMgrRightDetail.start_date" label="承包开始日期" required="true" readonly="false"/>
			<emp:date id="MortLandMgrRightDetail.end_date" label="承包截止日期"  required="true" readonly="false"/>
			<emp:text id="MortLandMgrRightDetail.ctr_method" label="承包方式" maxlength="50" required="true" />
			<emp:text id="MortLandMgrRightDetail.ctr_use" label="承包土地用途" maxlength="100" required="false" />
			<emp:textarea id="MortLandMgrRightDetail.land_dtl" label="承包地块情况" maxlength="1000" required="false" colSpan="2"/>
			<emp:textarea id="MortLandMgrRightDetail.memo" label="备注" maxlength="1000" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("view".equals(op)||"to_storage".equals(op)){%>		
			<%}else{%>
			<emp:button id="add" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
			<% } %>	
		</div>
	</emp:form>
	<div class='emp_gridlayout_title'>承包方土地承包经营权共有人情况</div>
	<div align="left">
		<%if("view".equals(op)||"to_storage".equals(op)){%>		
		<%}else{%>
		<emp:button id="getAddPage4Land" label="新增" op="add"/>
		<emp:button id="deleteLandRecord" label="删除" op="remove"/>
		<% } %>
	</div>
	<emp:table icollName="MortLandBelongsList" pageMode="true" url="pageMortLandBelongsQuery.do" statisticType="">
		<emp:text id="land_id" label="主键" hidden="true"/>
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="cus_name" label="姓名" />
		<emp:text id="sex" label="性别" dictname="STD_ZX_SEX"/>
		<emp:text id="age" label="年龄"/>
	</emp:table>
</body>
</html>
</emp:page>

