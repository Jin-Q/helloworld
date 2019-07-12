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
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComAptitude._toForm(form);
		CusComAptitudeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComAptitudePage() {
		var paramStr = CusComAptitudeList._obj.getParamStr(['cus_id','com_apt_code']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComAptitudeUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComAptitude() {
		var paramStr = CusComAptitudeList._obj.getParamStr(['cus_id','com_apt_code']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComAptitudeViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComAptitudePage() {
		var cus_id  ='${context.CusComAptitude.cus_id}';
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComAptitude.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComAptitudeAddPage.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComAptitude() {
		var paramStr = CusComAptitudeList._obj.getParamStr(['cus_id','com_apt_code']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComAptitudeRecord.do"/>?'+paramStr;
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
						    var cus_id  ='${context.CusComAptitude.cus_id}';
						    var editFlag = '${context.EditFlag}';
							var paramStr="CusComAptitude.cus_id="+cus_id+"&EditFlag="+editFlag;
							var url = '<emp:url action="queryCusComAptitudeList.do"/>&'+paramStr;
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
		page.dataGroups.CusComAptitudeGroup.reset();
	};
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
		<emp:button id="getAddCusComAptitudePage" label="新增" />
		<emp:button id="viewCusComAptitude" label="查看" />
		<emp:button id="getUpdateCusComAptitudePage" label="修改" />
		<emp:button id="deleteCusComAptitude" label="删除" />
	<%}else{%>
		<emp:button id="viewCusComAptitude" label="查看" />
	<%}%>
	</div>
	<emp:table icollName="CusComAptitudeList" pageMode="true" url="pageCusComAptitudeQuery.do" reqParams="CusComAptitude.cus_id=${context.CusComAptitude.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="com_apt_code" label="资质证书编号" />
		<emp:text id="com_apt_name" label="资质名称" />
		<emp:text id="reg_bch_id" label="发证/登记机构" />
		<emp:text id="crt_date" label="发证/登记日期" />
		<emp:text id="com_apt_expired" label="资质到期日期" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    