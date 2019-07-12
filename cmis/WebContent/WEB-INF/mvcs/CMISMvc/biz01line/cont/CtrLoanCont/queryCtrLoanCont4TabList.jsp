<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String po_no = "";
	if(context.containsKey("po_no")){
		po_no = (String)context.getDataValue("po_no");
	}  
	String cargo_id = "";
	if(context.containsKey("cargo_id")){
		cargo_id = (String)context.getDataValue("cargo_id");
	}  
	String guaranty_no = "";
	if(context.containsKey("guaranty_no")){
		guaranty_no = (String)context.getDataValue("guaranty_no");
	}  
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doViewCtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']);
		if (paramStr != null) {
			var url;
			if(prd_id==300021||prd_id==300020){
				if('<%=po_no%>'!=""){
					url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&menuIdTab=queryCtrLoanContHistoryList&flag=ctrLoanCont4Tab&po_no="+'<%=po_no %>';
				}else if('<%=cargo_id%>'!=""){
					url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&menuIdTab=queryCtrLoanContHistoryList&flag=ctrLoanCont4Tab&cargo_id="+'<%=cargo_id %>';
				}else if('<%=guaranty_no%>'!=""){
					url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&cont=cont&'+paramStr+"&menuIdTab=queryCtrLoanContHistoryList&flag=ctrLoanCont4Tab&guaranty_no="+'<%=guaranty_no %>';
				}
			}else{
				if('<%=po_no%>'!=""){
					url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&menuIdTab=queryCtrLoanContHistoryList&flag=ctrLoanCont4Tab&po_no="+'<%=po_no %>';
				}else if('<%=cargo_id%>'!=""){
					url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&menuIdTab=queryCtrLoanContHistoryList&flag=ctrLoanCont4Tab&cargo_id="+'<%=cargo_id %>';
				}else if('<%=guaranty_no%>'!=""){
					url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&'+paramStr+"&menuIdTab=queryCtrLoanContHistoryList&flag=ctrLoanCont4Tab&guaranty_no="+'<%=guaranty_no %>';
				}
			}
			url = EMPTools.encodeURI(url);  
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		} 
	};
	
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="viewCtrLoanCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrLoanContList" pageMode="true" url="pageCtrLoanContQuery4Tab.do" reqParams="po_no=${context.po_no}&cargo_id=${context.cargo_id}&guaranty_no=${context.guaranty_no}">
		<emp:text id="cont_no" label="合同编号" />  
		<emp:text id="cn_cont_no" label="中文合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="iqpFlowHis" label="业务审批标识" hidden="true" defvalue="have"/>
	</emp:table>

</body>
</html>
</emp:page>