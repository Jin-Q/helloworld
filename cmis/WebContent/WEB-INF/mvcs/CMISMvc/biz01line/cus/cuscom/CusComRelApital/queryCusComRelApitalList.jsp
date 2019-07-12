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

	function doOnLoad(){
		//showCertTyp(CusComRelApital.cert_typ, 'indiv');
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComRelApital._toForm(form);
		CusComRelApitalList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComRelApitalPage() {
		var paramStr = CusComRelApitalList._obj.getParamStr(['cus_id','cus_id_rel']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComRelApitalUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComRelApital() {
		var paramStr = CusComRelApitalList._obj.getParamStr(['cus_id','cus_id_rel']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComRelApitalViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComRelApitalPage() {
		var cus_id  ='${context.CusComRelApital.cus_id}';
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComRelApital.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComRelApitalAddPage.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComRelApital() {
		var paramStr = CusComRelApitalList._obj.getParamStr(['cus_id','cus_id_rel']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComRelApitalRecord.do"/>?'+paramStr;
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
							var editFlag = '${context.EditFlag}';
						    var cus_id  ='${context.CusComRelApital.cus_id}';
							var paramStr="CusComRelApital.cus_id="+cus_id+"&EditFlag="+editFlag;
							var url = '<emp:url action="queryCusComRelApitalList.do"/>&'+paramStr;
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
		page.dataGroups.CusComRelApitalGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
	<%
//	String flag=(String)request.getSession().getAttribute("buttonFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
	if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusComRelApitalPage" label="新增" />
		<emp:button id="viewCusComRelApital" label="查看" />
		<emp:button id="getUpdateCusComRelApitalPage" label="修改" />
		<emp:button id="deleteCusComRelApital" label="删除" />
	<%}else{%>
		<emp:button id="viewCusComRelApital" label="查看" />
	<% } %>
	</div>
	<emp:table icollName="CusComRelApitalList" pageMode="true" url="pageCusComRelApitalQuery.do" reqParams="CusComRelApital.cus_id=${context.CusComRelApital.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_rel" label="出资人客户码" />
		<emp:text id="invt_name" label="出资人名称" />
		<emp:text id="invt_typ" label="出资人类型" dictname="STD_ZB_INVESTOR2" hidden="true"/>
		<emp:text id="cert_typ" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="invt_amt" label="出资金额(万元)" dataType="Currency"/>
		<emp:text id="invt_perc" label="所占比例" dataType="Percent"/>
	</emp:table>
</body>
</html>
</emp:page>
    