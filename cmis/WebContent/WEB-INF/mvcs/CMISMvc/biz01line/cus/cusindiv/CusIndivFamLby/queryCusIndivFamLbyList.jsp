<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("CusIndivFamLby.cus_id");
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivFamLby._toForm(form);
		CusIndivFamLbyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivFamLbyPage() {
		var paramStr = CusIndivFamLbyList._obj.getParamStr(['cus_id','indiv_debt_id']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusIndivFamLbyUpdatePage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivFamLby() {
		var paramStr = CusIndivFamLbyList._obj.getParamStr(['cus_id','indiv_debt_id']);
		if (paramStr != null) {
			var EditFlag  ='${context.EditFlag}';
			var url = '<emp:url action="getCusIndivFamLbyViewPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivFamLbyPage() {
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="getCusIndivFamLbyAddPage.do"/>&' + "CusIndivFamLby.cus_id=<%=cus_id%>&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivFamLby() {
		var paramStr = CusIndivFamLbyList._obj.getParamStr(['cus_id','indiv_debt_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var EditFlag  ='${context.EditFlag}';
				var url = '<emp:url action="deleteCusIndivFamLbyRecord.do"/>?'+paramStr+"&EditFlag="+EditFlag;
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
		page.dataGroups.CusIndivFamLbyGroup.reset();
	};
	
	/*--user code begin--*/
	function goback(){
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivFamLbyList.do"/>&' + "CusIndivFamLby.cus_id=<%=cus_id%>"+"&EditFlag="+EditFlag;
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
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusIndivFamLbyPage" label="新增"/>
		<emp:button id="viewCusIndivFamLby" label="查看"/>
		<emp:button id="getUpdateCusIndivFamLbyPage" label="修改"/>
		<emp:button id="deleteCusIndivFamLby" label="删除"/>
	<%}else{%>
		<emp:button id="viewCusIndivFamLby" label="查看"/>
	<%}%>
	</div>
	<emp:table icollName="CusIndivFamLbyList" pageMode="true" url="pageCusIndivFamLbyQuery.do" reqParams="CusIndivFamLby.cus_id=${context.CusIndivFamLby.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="indiv_debt_id" label="负债编号" hidden="true"/>
		<emp:text id="indiv_creditor" label="债权人" />
		<emp:text id="indiv_debt_typ" label="负债类型" dictname="STD_ZB_INV_DE_TYP"/>
		<emp:text id="indiv_debt_amt" label="负债金额(元)" dataType="Currency"/>
		<emp:text id="indiv_debt_cur" label="负债币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="indiv_debt_str_dt" label="债务开始时间" />
		<emp:text id="indiv_debt_end_dt" label="债务到期时间" />
	</emp:table>
</body>
</html>
</emp:page>