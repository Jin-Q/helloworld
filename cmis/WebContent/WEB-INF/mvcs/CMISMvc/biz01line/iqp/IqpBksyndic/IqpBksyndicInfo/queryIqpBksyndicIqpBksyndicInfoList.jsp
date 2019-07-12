<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}  
	}   
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	}
%>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateIqpBksyndicInfoPage() {
		var paramStr = IqpBksyndicInfoList._obj.getParamStr(['pk1']);
		if (paramStr!=null) {
			var url = '<emp:url action="getIqpBksyndicIqpBksyndicInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBksyndicInfoPage(){
		var serno = window.parent.window.IqpBksyndic.serno._getValue();
		var url = '<emp:url action="getIqpBksyndicIqpBksyndicInfoAddPage.do"/>?IqpBksyndicInfo.serno='+serno;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
	};
	
	function doDeleteIqpBksyndicInfo() {
		var paramStr = IqpBksyndicInfoList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpBksyndicIqpBksyndicInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBksyndicInfo() {
		var paramStr = IqpBksyndicInfoList._obj.getParamStr(['pk1']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIqpBksyndicIqpBksyndicInfoDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
		}else { 
			alert('请先选择一条记录！');  
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" >
   
	<div align="left">
	<%if(!op.equals("view")&&!cont.equals("cont")){ %>
		<emp:button id="getAddIqpBksyndicInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBksyndicInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBksyndicInfo" label="删除" op="remove"/>
		
	<% 
	  }  
	%>
	     <emp:button id="viewIqpBksyndicInfo" label="查看" />
	</div>
							
	<emp:table icollName="IqpBksyndicInfoList" pageMode="true" url="pageIqpBksyndicIqpBksyndicInfoQuery.do" reqParams="serno=${context.serno}">    
		<emp:text id="prtcpt_org_no" label="参与行行号" />
		<emp:text id="prtcpt_org_name" label="参与行行名" />
		<emp:text id="prtcpt_role" label="参与角色" dictname="STD_PART_ORG_TYPE"/>
		<emp:text id="prtcpt_amt_rate" label="参与金额比例" dataType="Percent"/> 
		<emp:text id="prtcpt_curr" label="参与币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="prtcpt_amt" label="参与金额" dataType="Currency"/>
		<emp:text id="pk1" label="PK1" hidden="true"/>
	</emp:table>
				
</body>
</html>
</emp:page>