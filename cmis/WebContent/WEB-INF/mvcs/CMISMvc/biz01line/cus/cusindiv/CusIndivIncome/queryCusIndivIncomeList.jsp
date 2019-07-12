<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
String cus_id=request.getParameter("CusIndivIncome.cus_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivIncome._toForm(form);
		CusIndivIncomeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivIncomePage() {
		var paramStr = CusIndivIncomeList._obj.getParamStr(['cus_id','indiv_sur_year','indiv_deposits']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivIncomeUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivIncome() {
		var paramStr = CusIndivIncomeList._obj.getParamStr(['cus_id','indiv_sur_year','indiv_deposits']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivIncomeViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivIncomePage() {
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusIndivIncomeAddPage.do"/>&' + "CusIndivIncome.cus_id=<%=cus_id%>&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivIncome() {
		var paramStr = CusIndivIncomeList._obj.getParamStr(['cus_id','indiv_sur_year','indiv_deposits']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusIndivIncomeRecord.do"/>?'+paramStr;
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusIndivIncomeGroup.reset();
	};
	
	/*--user code begin--*/
	function goback(){
		var editFlag = '${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivIncomeList.do"/>&' + "CusIndivIncome.cus_id=<%=cus_id%>&EditFlag="+editFlag;
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
	//    String flag=(String)request.getSession().getAttribute("buttonFlag");
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusIndivIncomePage" label="新增"/>
		<emp:button id="viewCusIndivIncome" label="查看"/>
		<emp:button id="getUpdateCusIndivIncomePage" label="修改"/>
		<emp:button id="deleteCusIndivIncome" label="删除"/>
	<%}else{%>
        <emp:button id="viewCusIndivIncome" label="查看"/>
	<%}%>
	</div>
	<emp:table icollName="CusIndivIncomeList" pageMode="true" url="pageCusIndivIncomeQuery.do" reqParams="CusIndivIncome.cus_id=${context.CusIndivIncome.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="indiv_sur_year" label="调查年份" />
		<emp:text id="indiv_deposits" label="收入来源" dictname="STD_ZB_INDIV_DEPOS"/>
		<emp:text id="indiv_ann_incm" label="年收入(元)" dataType="Currency"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    