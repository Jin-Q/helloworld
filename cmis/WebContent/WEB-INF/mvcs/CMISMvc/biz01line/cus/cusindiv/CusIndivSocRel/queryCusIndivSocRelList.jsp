<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("CusIndivSocRel.cus_id");
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivSocRel._toForm(form);
		CusIndivSocRelList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivSocRelPage() {
		var paramStr = CusIndivSocRelList._obj.getParamStr(['cus_id','cus_id_rel','indiv_cus_rel']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivSocRelUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivSocRel() {
		var paramStr = CusIndivSocRelList._obj.getParamStr(['cus_id','cus_id_rel','indiv_cus_rel']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusIndivSocRelViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivSocRelPage() {
		var cert_type  ='${context.cert_type}';
		var cert_code  ='${context.cert_code}';
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusIndivSocRelAddPage.do"/>&' + "CusIndivSocRel.cus_id=<%=cus_id%>"+"&cert_type="+cert_type+"&cert_code="+cert_code+"&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivSocRel() {
		var paramStr = CusIndivSocRelList._obj.getParamStr(['cus_id','cus_id_rel','indiv_cus_rel']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusIndivSocRelRecord.do"/>?'+paramStr;
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
		page.dataGroups.CusIndivSocRelGroup.reset();
	};
	
	/*--user code begin--*/
	function goback(){
		var editFlag = '${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&' + "CusIndivSocRel.cus_id=<%=cus_id%>&EditFlag="+editFlag;
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
		<emp:button id="getAddCusIndivSocRelPage" label="新增"/>
		<emp:button id="viewCusIndivSocRel" label="查看"/>
		<emp:button id="getUpdateCusIndivSocRelPage" label="修改"/>
		<emp:button id="deleteCusIndivSocRel" label="删除"/>
	<%}else{%>
			<emp:button id="viewCusIndivSocRel" label="查看"/>
	<%}%>	
	</div>
	<emp:table icollName="CusIndivSocRelList" pageMode="true" url="pageCusIndivSocRelQuery.do" reqParams="CusIndivSocRel.cus_id=${context.CusIndivSocRel.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_rel" label="关联客户码" hidden="true"/>
		<emp:text id="indiv_rel_cus_name" label="姓名" />
		<emp:text id="indiv_sex" label="性别" dictname="STD_ZX_SEX" />
		<emp:text id="indiv_cus_rel" label="与客户关系" dictname="STD_ZB_INDIV_CUS" />
		<emp:text id="indiv_rel_cert_typ" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="indiv_rl_cert_code" label="证件号码" />
		<emp:text id="indiv_rl_y_incm" label="年收入（元）" hidden="true" />
		<emp:text id="indiv_rel_job" label="职业" dictname="STD_ZX_EMPLOYMET" />
		<emp:text id="indiv_rel_com_name" label="单位名称" hidden="true"/>
		<emp:text id="indiv_rel_duty" label="职务" dictname="STD_ZX_DUTY" />
		<emp:text id="indiv_rel_ind" label="所在行业" dictname="STD_GB_4754-2011" hidden="true" />
		<emp:text id="indiv_rel_phn" label="联系电话" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>