<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("cus_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComResset._toForm(form);
		CusComRessetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComRessetPage() {
		var paramStr = CusComRessetList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComRessetUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComResset() {
		var paramStr = CusComRessetList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComRessetViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComRessetPage() {
		var cus_id  ='${context.CusComResset.cus_id}';
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComResset.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComRessetAddPage.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComResset() {
		var paramStr = CusComRessetList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComRessetRecord.do"/>?'+paramStr;
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
						    var cus_id  ='${context.CusComResset.cus_id}';
							var paramStr="CusComResset.cus_id="+cus_id+"&EditFlag="+editFlag;
							var url = '<emp:url action="queryCusComRessetList.do"/>&'+paramStr;
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
		page.dataGroups.CusComRessetGroup.reset();
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
		<emp:button id="getAddCusComRessetPage" label="新增" />
		<emp:button id="viewCusComResset" label="查看" />
		<emp:button id="getUpdateCusComRessetPage" label="修改" />
		<emp:button id="deleteCusComResset" label="删除" />
	<%
	}else{
	%>
		<emp:button id="viewCusComResset" label="查看" />
	<% } %>

	</div>

	<emp:table icollName="CusComRessetList" pageMode="true" url="pageCusComRessetQuery.do" reqParams="CusComResset.cus_id=${context.CusComResset.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="com_ass_typ" label="资产类别" dictname="STD_ZB_COM_ASS_TYP"/>
		<emp:text id="com_ass_name" label="资产名称" />
		<!-- emp:text id="com_ass_number" label="数量" /-->
		<emp:text id="com_ass_eva_amt" label="评估价值(元)" dataType="Currency"/>
		<emp:text id="com_ass_coll_info" label="抵押状况" dictname="STD_ZB_ASS_COLL_INFO"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    