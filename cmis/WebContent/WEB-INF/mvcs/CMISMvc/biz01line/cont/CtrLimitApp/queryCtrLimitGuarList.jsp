<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript"> 
   rowIndex = 0;
	/*一般担保合同查看
	@flag=loan 业务担保合同关联页面标识，可以查看到业务担保合同关联信息Tab页
	@oper=view 查看按钮标识
	@menuId=ybCount  当Tab页页面在别处调用时，需加上原有的menuId
	 */
	function doViewGrtGuarCont() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);   
		var flag ="view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuId=ybCount&flag=ybWh&rel=ywRel&'+paramStr+'&oper='+flag;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {   
			alert('请先选择一条记录！');
		}   
	};

  
	/*最高额担保合同查看*/   
	function doViewGrtLoanRGurZge() { 
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']);  
		var flag ="view";   
		if (paramStr != null) {  
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuId=zge&op=view&'+paramStr+'&flag=loan&rel=ywRel&oper='+flag;
			url = EMPTools.encodeURI(url);  
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {
			alert('请先选择一条记录！');     
		}
	};   
		
</script>
</head>
<body class="page_content">
	<b>最高额担保</b>
	<div align="left">
		<emp:button id="viewGrtLoanRGurZge" label="查看" op="view"/>   
	</div>     
	<emp:table icollName="GrtLoanRGurListZge" pageMode="false" url="pageGrtLoanRGurQueryZGE.do">
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>
	    <emp:text id="serno" label="业务流水号" hidden="true"/>    
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保金额" dataType="Currency"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
	</emp:table>   
	   
	<br><br>
	<b>一般担保合同</b>
	<div align="left">   
		<emp:button id="viewGrtGuarCont" label="查看" op="view"/>   
	</div>
	   
	<emp:table icollName="GrtLoanRGurListYb" pageMode="false" url="pageGrtLoanRGurQueryYB.do"> 
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>   
	    <emp:text id="serno" label="业务流水号" hidden="true"/>    
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保金额" dataType="Currency"/>   
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
	</emp:table>    
</body>
</html>
</emp:page>
    