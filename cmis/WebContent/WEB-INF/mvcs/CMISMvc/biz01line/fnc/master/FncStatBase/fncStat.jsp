<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<emp:page>
<html>
<head>
<title>企业财务报表</title>

<jsp:include page="/include.jsp" />
<%
	String cus_id=request.getParameter("cus_id");
	String isSmp = request.getParameter("isSmp");
%>
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/emp/rpt.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	
	function doOnLoad() {
		//alert("onload");
		try{
			page.renderEmpObjects();
		}catch(e){
			alert(e);
		}
	};

	function getFlg(){
		
	}

	function doReturn() {
		var cus_id = '<%=cus_id%>';
		var isSmp = '<%=isSmp%>';
		var editFlag = '${context.EditFlag}';
		var url = null;
		if(isSmp==null||isSmp==''||isSmp=='null'){
			url = '<emp:url action="queryFncStatBaseList.do"/>&FncStatBase.cus_id='+cus_id+"&EditFlag="+editFlag;
		}else{
			url = '<emp:url action="queryFncStatBaseSmpList.do"/>&FncStatBase.cus_id='+cus_id+"&EditFlag="+editFlag+"&isSmp=isSmp";
		}
		url = EMP.util.Tools.encodeURI(url);
		window.location=url;
	};

</script>
</head>
<body  class="page_content"  >
	<% 
			Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT); 
			String statFlg = (String)context.getDataValue("state_flg"); 
 
			String bsFlg = statFlg.substring(0,1);//资产样式编号 
			String isFlg = statFlg.substring(1,2);//损益表编号 
			String cfsFlg = statFlg.substring(2,3);//现金流量表编号 
			String fiFlg = statFlg.substring(3,4);//财务指标表编号 
			String soeFlg = statFlg.substring(4,5);//所有者权益变动表编号 
			String slFlg = statFlg.substring(5,6);//财务简表编号 
			
			context.addDataField("mainTab",!"9".equals(slFlg)?"cwjb":"zcfzb");
		
	%>
	<emp:tabGroup mainTab="${context.mainTab}" id="rptGroup">
   <%	if(!"9".equals(slFlg)){   %>
	
				<emp:tab label="财务简表" id="cwjb" url="queryFncStatSl.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&fnc_conf_typ=06&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}">
				</emp:tab>
				
	<%			
        }
				if(!"9".equals(bsFlg)){
	%>
				<emp:tab label="资产负债表" id="zcfzb" initial="true" url="queryFncStatBs.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&fnc_conf_typ=01&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}">
				</emp:tab>
	<%			
				}
				if(!"9".equals(isFlg)){
	%>
				<emp:tab label="损益表" id="syb" url="queryFncStatIs.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&fnc_conf_typ=02&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}">
				</emp:tab>
	<%
				}
				if(!"9".equals(cfsFlg)){
	%>
				<emp:tab label="现金流量表" id="xjllb" url="queryFncStatCfs.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&fnc_conf_typ=03&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}" initial="false" needFlush="true">
				</emp:tab>
	<%
				}
				if(!"9".equals(fiFlg)){
	%>
				<emp:tab label="财务指标表" id="cwzbb" url="queryFncIndexRpt.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&fnc_conf_typ=04&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}" initial="false" needFlush="true">
				</emp:tab>
	<%
				}
				if(!"9".equals(soeFlg)){
	%>
				<emp:tab label="所有者权益变动表" id="syzqy" url="queryFncStatSoe.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&fnc_conf_typ=05&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}">
				</emp:tab>
	<%
				}
			
	 %>
	 
	 			<emp:tab label="财务报表审计信息" needFlush="true" id="cwsjxx" url="getAdt.do?stat_prd_style=${context.stat_prd_style}&cus_id=${context.cus_id}&stat_prd=${context.stat_prd}&stat_style=${context.stat_style}&fnc_type=${context.fnc_type}">
				</emp:tab>
				<%--财报模板是 PB0004 PB0007 PB0008 新银行类  其他金融类  其他金融类2010  
				<emp:tab id="san_tab" label="近三期财务比较" url="getLast3TermFnaForBSB.do" reqParams="fromType=${(context.fnc_type eq 'PB0007' || context.fnc_type eq 'PB0008' || context.fnc_type eq 'PB0004') ? 'BL200' : 'PUB'}&cusId=${context.cus_id}&stat_prd_style=${context.stat_prd_style}&vDate=${context.stat_prd}&termType=4" initial="false" needFlush="true"/>--%>
	</emp:tabGroup>
	
	  <div align="center">	
	       <emp:button id="return" label="返回"/>
	    </div>
	
</body>
</html>
</emp:page>

