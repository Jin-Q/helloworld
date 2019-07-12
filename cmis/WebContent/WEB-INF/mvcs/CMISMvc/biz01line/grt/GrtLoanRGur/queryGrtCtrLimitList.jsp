<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	String cus_id="";
	String limit_acc_no="";
	String limit_credit_no="";
	String op = "";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}
	if(context.containsKey("cus_id")){
		cus_id =(String)context.getDataValue("cus_id");
	}
	if(context.containsKey("limit_acc_no")){
		limit_acc_no =(String)context.getDataValue("limit_acc_no");
	}      
	if(context.containsKey("limit_credit_no")){
		limit_credit_no =(String)context.getDataValue("limit_credit_no");
	}      
	if(context.containsKey("op")){
		op =(String)context.getDataValue("op");
	}      
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript"> 
	 function doViewGrtGuarCont() {
			var paramStr = LmtGrtLoanRGurList._obj.getParamStr(['limit_code','guar_cont_no']);   
			var flag ="view";       
			var url;
			if (paramStr != null) {   
				var guar_cont_type = LmtGrtLoanRGurList._obj.getSelectedData()[0].guar_cont_type._getValue();
                if(guar_cont_type == "00"){
                	url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuId=ybCount&flag=ybWh&rel=sxRel&'+paramStr+'&oper='+flag;
                }else if(guar_cont_type == "01"){
                	url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuId=zge&op=view&'+paramStr+'&flag=loan&rel=sxRel&oper='+flag;
                }
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow2','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			} else {   
				alert('请先选择一条记录！');
			}   
		};
	/*--user code begin--*/

	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>       
    
	<div align="left">
		<emp:actButton id="viewGrtGuarCont" label="查看" op="view"/>   
	</div>     
	<emp:table icollName="LmtGrtLoanRGurList" pageMode="true" url="pageQueryGrtCtrLimitList.do" reqParams="serno=${context.serno}">
	    <emp:text id="limit_code" label="授信额度编号" hidden="true" /> 
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保合同金额" dataType="Currency" hidden="true"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
	</emp:table>
	   
</body>
</html>
</emp:page>
    