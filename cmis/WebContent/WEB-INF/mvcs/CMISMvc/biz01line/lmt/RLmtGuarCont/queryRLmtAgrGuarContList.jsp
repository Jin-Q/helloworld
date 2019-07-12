<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="pubRLmtGuarCont.jsp" flush="true" />
<script type="text/javascript">
/*--user code begin--*/
		
/*--user code end--*/ 
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>       
    
	<b>最高额担保合同</b>
	<div align="left">
		<emp:actButton id="viewRLmtGuarContZge" label="查看" op="view"/>   
	</div>     

	<emp:table icollName="RLmtGuarContListZge" pageMode="false" url="pageGrtLoanRGurQueryZGE.do">
		<emp:text id="limit_code" label="授信额度编号" hidden="true" />
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保合同金额" dataType="Currency" hidden="true"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:text id="guar_lvl" label="等级 " />
	</emp:table>    
	   
	<br><br>
	<b>一般担保合同</b>  
	<div align="left">   
		<emp:actButton id="viewGrtGuarCont" label="查看" op="view"/>
	</div> 
	   
	<emp:table icollName="RLmtGuarContListYb" pageMode="false" url="pageGrtLoanRGurQueryYB.do"> 
		<emp:text id="limit_code" label="授信额度编号" hidden="true"/>
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保合同金额" dataType="Currency" hidden="true"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:text id="guar_lvl" label="等级 "/>  
	</emp:table>                        
</body>
</html>
</emp:page>
    