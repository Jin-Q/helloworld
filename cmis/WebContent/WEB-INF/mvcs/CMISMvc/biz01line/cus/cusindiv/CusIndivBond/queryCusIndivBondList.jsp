<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("CusIndivBond.cus_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivBond._toForm(form);
		CusIndivBondList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivBondPage() {
		var paramStr = CusIndivBondList._obj.getParamStr(['cus_id','indiv_bond_id']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivBondUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivBond() {
		var paramStr = CusIndivBondList._obj.getParamStr(['cus_id','indiv_bond_id']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivBondViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivBondPage() {
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusIndivBondAddPage.do"/>&' + "CusIndivBond.cus_id=<%=cus_id%>&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivBond() {
		var paramStr = CusIndivBondList._obj.getParamStr(['cus_id','indiv_bond_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusIndivBondRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						}catch(e) {
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusIndivBondGroup.reset();
	};
	
	/*--user code begin--*/
	function goback(){
		var editFlag = '${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivBondList.do"/>&' + "CusIndivBond.cus_id=<%=cus_id%>&EditFlag="+editFlag;
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
		<emp:button id="getAddCusIndivBondPage" label="新增"/>
		<emp:button id="viewCusIndivBond" label="查看"/>
		<emp:button id="getUpdateCusIndivBondPage" label="修改"/>
		<emp:button id="deleteCusIndivBond" label="删除"/>
	<%}else{%>
		<emp:button id="viewCusIndivBond" label="查看"/>
	<%}%>
	</div>
	<emp:table icollName="CusIndivBondList" pageMode="true" url="pageCusIndivBondQuery.do" reqParams="CusIndivBond.cus_id=${context.CusIndivBond.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="indiv_bond_id" label="证券编号" hidden="true"/>
		<emp:text id="indiv_bond_typ" label="证券类别" dictname="STD_ZB_INV_BON_TYP"/>
		<emp:text id="indiv_bond_eva_amt" label="证券估价总额(元)" dataType="Currency" />
		<emp:text id="indiv_bond_pub" label="发行商" />
		<emp:text id="indiv_bond_str_dt" label="持有起始日期" />
		<emp:text id="indiv_bond_end_dt" label="持有到期日期" />
	</emp:table>
</body>
</html>
</emp:page>