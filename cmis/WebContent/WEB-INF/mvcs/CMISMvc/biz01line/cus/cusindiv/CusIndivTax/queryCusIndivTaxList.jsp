<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
String cus_id=request.getParameter("CusIndivTax.cus_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivTax._toForm(form);
		CusIndivTaxList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivTaxPage() {
		var paramStr = CusIndivTaxList._obj.getParamStr(['cus_id','indiv_taxes','serno']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivTaxUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivTax() {
		var paramStr = CusIndivTaxList._obj.getParamStr(['cus_id','indiv_taxes','serno']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivTaxViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivTaxPage() {
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusIndivTaxAddPage.do"/>&' + "CusIndivTax.cus_id=<%=cus_id%>&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivTax() {
		var paramStr = CusIndivTaxList._obj.getParamStr(['cus_id','indiv_taxes','serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusIndivTaxRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try{
							var jsonstr = eval("("+o.responseText+")");
						}catch(e){
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="删除成功"){
							alert("删除成功!");
							goback();
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
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusIndivTaxGroup.reset();
	};
	
	/*--user code begin--*/
	function goback(){
		var editFlag = '${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivTaxList.do"/>&' + "CusIndivTax.cus_id=<%=cus_id%>&EditFlag="+editFlag;;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
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
		<emp:button id="getAddCusIndivTaxPage" label="新增"/>
		<emp:button id="viewCusIndivTax" label="查看"/>
		<emp:button id="getUpdateCusIndivTaxPage" label="修改"/>
		<emp:button id="deleteCusIndivTax" label="删除"/>
	<%}else{%>
		<emp:button id="viewCusIndivTax" label="查看"/>
	<%}%>
	</div>
	<emp:table icollName="CusIndivTaxList" pageMode="true" url="pageCusIndivTaxQuery.do" reqParams="CusIndivTax.cus_id=${context.CusIndivTax.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="serno" label="编号" hidden="true"/>
		<emp:text id="indiv_taxes" label="税费种类" dictname="STD_ZB_INV_TAX_TYP"/>
		<emp:text id="indiv_tax_amt" label="应缴纳/支付金额(元)" dataType="Currency" />
		<emp:text id="indiv_tax_dt" label="缴纳/支付日期" />
		<emp:text id="indiv_tax_flg" label="是否正常缴纳/支付" dictname="STD_ZX_YES_NO" />
	</emp:table>
</body>
</html>
</emp:page>
    