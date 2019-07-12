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
		CusComFinaBond._toForm(form);
		CusComFinaBondList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComFinaBondPage() {
		var paramStr = CusComFinaBondList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComFinaBondUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComFinaBond() {
		var paramStr = CusComFinaBondList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComFinaBondViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComFinaBondPage() {
		var editFlag = '${context.EditFlag}';
		var cus_id  ='${context.CusComFinaBond.cus_id}';
		var paramStr="CusComFinaBond.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComFinaBondAddPage.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComFinaBond() {
		var paramStr = CusComFinaBondList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComFinaBondRecord.do"/>?'+paramStr;
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
							    var cus_id  ='${context.CusComFinaBond.cus_id}';
							    var editFlag = '${context.EditFlag}';
								var paramStr="CusComFinaBond.cus_id="+cus_id+"&EditFlag="+editFlag;
								var url = '<emp:url action="queryCusComFinaBondList.do"/>&'+paramStr;
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
		page.dataGroups.CusComFinaBondGroup.reset();
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
//	String flag=(String)request.getSession().getAttribute("buttonFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
	if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusComFinaBondPage" label="新增" />
		<emp:button id="viewCusComFinaBond" label="查看" />
		<emp:button id="getUpdateCusComFinaBondPage" label="修改" />
		<emp:button id="deleteCusComFinaBond" label="删除" />	
	<%
	}else{
	%>
		<emp:button id="viewCusComFinaBond" label="查看" />
	<%}%>

	</div>

	<emp:table icollName="CusComFinaBondList" pageMode="true" url="pageCusComFinaBondQuery.do" reqParams="CusComFinaBond.cus_id=${context.CusComFinaBond.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="com_bond_name" label="债券名称" />
		<emp:text id="com_bond_amt" label="总金额" dataType="Currency"/>
		<emp:text id="com_bond_pub_dt" label="债券发行日期" />
		<emp:text id="com_bond_trm" label="债券期限(月)" />
		<emp:text id="com_bond_assure_means" label="担保方式 " dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    