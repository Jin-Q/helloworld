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
		CusComRelInvest._toForm(form);
		CusComRelInvestList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComRelInvestPage() {
		var paramStr = CusComRelInvestList._obj.getParamStr(['cus_id','cus_id_rel']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusComRelInvestUpdatePage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComRelInvest() {
		var paramStr = CusComRelInvestList._obj.getParamStr(['cus_id','cus_id_rel']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusComRelInvestViewPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComRelInvestPage() {
		var cus_id  ='${context.CusComRelInvest.cus_id}';
	//	var cert_code  ='${context.cert_code}';
		var EditFlag  ='${context.EditFlag}';
		var paramStr="CusComRelInvest.cus_id="+cus_id;
		var url = '<emp:url action="getCusComRelInvestAddPage.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComRelInvest() {
		var paramStr = CusComRelInvestList._obj.getParamStr(['cus_id','cus_id_rel']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComRelInvestRecord.do"/>?'+paramStr;
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
							    var cus_id  ='${context.CusComRelInvest.cus_id}';
								var paramStr="CusComRelInvest.cus_id="+cus_id;
								var EditFlag  ='${context.EditFlag}';
								var url = '<emp:url action="queryCusComRelInvestList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusComRelInvestGroup.reset();
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
		<emp:button id="getAddCusComRelInvestPage" label="新增" />
		<emp:button id="viewCusComRelInvest" label="查看" />
		<emp:button id="getUpdateCusComRelInvestPage" label="修改" />
		<emp:button id="deleteCusComRelInvest" label="删除" />
	<%
	}else{
		%>
			<emp:button id="viewCusComRelInvest" label="查看" />
		<%
	}
	%>
	</div>
	<emp:table icollName="CusComRelInvestList" pageMode="true" url="pageCusComRelInvestQuery.do" reqParams="CusComRelInvest.cus_id=${context.CusComRelInvest.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="com_inv_name" label="被投资单位名称(全称)" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_rel" label="关联客户码" hidden="true"/>
		<emp:text id="com_inv_typ" label="投资性质" dictname="STD_ZB_INVT_NATURE"/>
		<emp:text id="com_inv_inst_code" label="组织机构代码" />
		<emp:text id="com_inv_app" label="出资方式" dictname="STD_ZB_INVT_TYPE"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="com_inv_amt" label="投资金额(万元)" hidden="false" dataType="Currency"/>
		<emp:text id="com_inv_perc" label="所占比例"  dataType="Percent"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    