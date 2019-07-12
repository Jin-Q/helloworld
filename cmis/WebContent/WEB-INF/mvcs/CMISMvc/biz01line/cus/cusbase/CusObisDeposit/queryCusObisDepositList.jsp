<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusObisDeposit._toForm(form);
		CusObisDepositList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusObisDepositPage() {
		var paramStr = CusObisDepositList._obj.getParamStr(['cus_id','seq']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusObisDepositUpdatePage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusObisDeposit() {
		var paramStr = CusObisDepositList._obj.getParamStr(['cus_id','seq']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusObisDepositViewPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusObisDepositPage() {
		var cus_id  ='${context.CusObisDeposit.cus_id}';
		var paramStr="CusObisDeposit.cus_id="+cus_id;
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="getCusObisDepositAddPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusObisDeposit() {
		var paramStr = CusObisDepositList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusObisDepositRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var flag = jsonstr.flag;
							if(flag=="删除成功"){
								alert("删除成功!");
								    var cus_id  ='${context.CusObisDeposit.cus_id}';
									var paramStr="CusObisDeposit.cus_id="+cus_id;
									var EditFlag  ='${context.EditFlag}';
									var url = '<emp:url action="queryCusObisDepositList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
									url = EMPTools.encodeURI(url);
									window.location = url;
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusObisDepositGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
	<%
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusObisDepositPage" label="新增"/>
		<emp:button id="viewCusObisDeposit" label="查看"/>
		<emp:button id="getUpdateCusObisDepositPage" label="修改"/>
		<emp:button id="deleteCusObisDeposit" label="删除"/>
	<%}else{%>
		<emp:button id="viewCusObisDeposit" label="查看"/>
	<%}%>	
	</div>
	<emp:table icollName="CusObisDepositList" pageMode="true" url="pageCusObisDepositQuery.do" reqParams="CusObisDeposit.cus_id=${context.CusObisDeposit.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="dep_typ" label="存款类型" dictname="STD_ZB_DEP_TYP" />
		<emp:text id="acc_cur_typ" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="acc_typ" label="账户类型" dictname="STD_ZB_CUS_ACC_TYP"/>
		<emp:text id="dep_per" label="存款期限(月)" />
		<emp:text id="acc_blc" label="存款余额(元)" dataType="Currency"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="cus_typ" label="客户类型" dictname="STD_ZB_INVESTOR2" hidden="true"/>
		<emp:text id="cus_bch_id" label="开户机构代码" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>