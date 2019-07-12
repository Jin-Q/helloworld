<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String grtOp = "";
	if(context.containsKey("grtOp")){
		grtOp =(String)context.getDataValue("grtOp"); 
	}      
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="pubRLmtGuarCont.jsp" flush="true" />
<script type="text/javascript">
/*--user code begin--*/
	//引入最高额担保合同
	function doGetAddGrtZgeGuarPage(){
		var url = '<emp:url action="introGrtGuarContListForLmt.do"/>?serno=${context.serno}&cus_id=${context.cus_id}&limit_code=${context.org_limit_code}&type=01';
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=650,width=1024,top=100,left=150,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};

	//引入一般担保合同
	function doGetAddGrtGuarPage(){
		var url = '<emp:url action="introGrtGuarContListForLmt.do"/>?serno=${context.serno}&cus_id=${context.cus_id}&limit_code=${context.org_limit_code}&type=00';
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=650,width=1024,top=100,left=150,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
/*--user code end--*/ 
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>       
    
	<b>最高额担保合同</b>
	<div align="left">
	 <%if("update".equals(grtOp)){%>
	 	<emp:actButton id="getAddGrtZgeGuarPage" label="引入" op="add"/> 
		<emp:button id="getHighGrtGuarContAddPage" label="新增" /> 
		<emp:button id="getUpdateRLmtGuarContZge" label="修改" />  
		<emp:button id="deleteRLmtGuarContZge" label="删除" />
		<%}%>
		<emp:button id="viewRLmtGuarContZge" label="查看" />
	</div>     

	<emp:table icollName="RLmtGuarContListZge" pageMode="false" url="pageGrtLoanRGurQueryZGE.do">
	    <emp:text id="serno" label="业务流水号" hidden="true"/>
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
		<%if(!"view".equals(grtOp)){%>
		<emp:link id="upZge" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvlZge"/>  
		<emp:link id="downZge" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvlZge" hidden="false"/>
	    <%}%>
	</emp:table>    
	   
	<br><br>
	<b>一般担保合同</b>  
	<div align="left">   
		<%if("update".equals(grtOp)){%>
		<emp:actButton id="getAddGrtGuarPage" label="引入" op="add"/>
		<emp:button id="getAddGrtGuarContPage" label="新增" /> 
		<emp:button id="getUpdateGrtGuarContPage" label="修改" />
		<emp:button id="deleteGrtLoanRGur" label="删除" />
		<%}%>   
		<emp:button id="viewGrtGuarCont" label="查看" />
	</div> 
	   
	<emp:table icollName="RLmtGuarContListYb" pageMode="false" url="pageGrtLoanRGurQueryYB.do"> 
		<emp:text id="serno" label="业务流水号" hidden="true"/>
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
		<%if(!"view".equals(grtOp)){%> 
		    <emp:link id="up" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvl"/>  
		    <emp:link id="down" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvl" hidden="false"/>
	    <%}%>   
	</emp:table>                        
</body>
</html>
</emp:page>
    