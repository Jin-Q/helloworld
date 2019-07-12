<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("CusIndivInsu.cus_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivInsu._toForm(form);
		CusIndivInsuList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivInsurancesPage() {
		var paramStr = CusIndivInsuList._obj.getParamStr(['cus_id','indiv_ins_id']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivInsurancesUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivInsurances() {
		var paramStr = CusIndivInsuList._obj.getParamStr(['cus_id','indiv_ins_id']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivInsurancesViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivInsurancesPage() {
		var cus_id ='${context.CusIndivInsu.cus_id}';
		var editFlag = '${context.EditFlag}';
		var paramStr="CusIndivInsu.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusIndivInsurancesAddPage.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivInsurances() {
		var paramStr = CusIndivInsuList._obj.getParamStr(['cus_id','indiv_ins_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusIndivInsurancesRecord.do"/>?'+paramStr;
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
		page.dataGroups.CusIndivInsurancesGroup.reset();
	};
	
	function goback(){
		var editFlag = '${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivInsurancesList.do"/>&' + "CusIndivInsu.cus_id=<%=cus_id%>&EditFlag="+editFlag;
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
	//    String flag=(String)request.getSession().getAttribute("buttonFlag");
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusIndivInsurancesPage" label="新增"/>
		<emp:button id="viewCusIndivInsurances" label="查看"/>
		<emp:button id="getUpdateCusIndivInsurancesPage" label="修改"/>
		<emp:button id="deleteCusIndivInsurances" label="删除"/>
	<%}else{%>
		<emp:button id="viewCusIndivInsurances" label="查看"/>
	<%}%>
	</div>
	<emp:table icollName="CusIndivInsuList" pageMode="true" url="pageCusIndivInsurancesQuery.do" reqParams="CusIndivInsu.cus_id=${context.CusIndivInsu.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="indiv_ins_id" label="保险编号" hidden="true"/>
		<emp:text id="indiv_ins_cvg" label="保险名称"  />
		<emp:text id="indiv_ins_com" label="保险公司" />
		<emp:text id="indiv_ins_tot_amt" label="应缴保费总额(元)" dataType="Currency" />
		<emp:text id="indiv_ins_str_dt" label="投保日期" />
		<emp:text id="indiv_ins_end_dt" label="到期日期" />
		<emp:text id="indiv_ins_amt" label="保险金额(元)" dataType="Currency"/>
	</emp:table>
</body>
</html>
</emp:page>