<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusCertInfo._toForm(form);
		CusCertInfoList._obj.ajaxQuery(null,form);
	};
		
	function doGetAddCusCertInfoPage() {
		var cus_id  ='${context.CusCertInfo.cus_id}';
		
		var EditFlag  ='${context.EditFlag}';
		var paramStr="CusCertInfo.cus_id="+cus_id//+"&CusCertInfo.input_cli="+;
		var url = '<emp:url action="getCusCertInfoAddPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

		function doDeleteCusCertInfo(){
			var paramStr = CusCertInfoList._obj.getParamStr(['cus_id','cert_typ','cert_code']);
			if (paramStr != null) {
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteCusCertInfoRecord.do"/>?'+paramStr;
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
							    var cus_id  ='${context.CusCertInfo.cus_id}';
								var paramStr="CusCertInfo.cus_id="+cus_id+"&EditFlag="+editFlag;
								var url = '<emp:url action="queryCusCertInfoList.do"/>&'+paramStr;
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
			
		function goback(){
			
			var paramStr="CusCertInfo.cus_id="+CusCertInfo.cus_id._obj.element.value;
			
			var stockURL = '<emp:url action="queryCusCertInfoList.do"/>&'+paramStr+"&EditFlag=<%=editFlag%>";
			alert(stockURL);
			stockURL = EMPTools.encodeURI(stockURL);
			window.location = stockURL;
		}
	
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
		<emp:button id="getAddCusCertInfoPage" label="新增" />	
		<emp:button id="deleteCusCertInfo" label="删除" />
		
	<%}else{%>
<!--		<emp:button id="viewCusCertInfo" label="查看" />-->
	<%}%>

	</div>
	<emp:table icollName="CusCertInfoList" pageMode="true" url="pageCusCertInfoQuery.do" reqParams="CusCertInfo.cus_id=${context.CusCertInfo.cus_id}&EditFlag=${context.EditFlag}">
	    <emp:text id="cus_id" label="客户码" />
		<emp:text id="cert_typ" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="crt_date" label="签发日期" />
		<emp:text id="crt_end_date" label="签发到期日期" />
		<emp:text id="input_cli" label="登记人" />
		<emp:text id="input_org" label="登记机构" />
		<emp:text id="input_date" label="登记日期"  />
		
	</emp:table>
</body>
</html>
</emp:page>