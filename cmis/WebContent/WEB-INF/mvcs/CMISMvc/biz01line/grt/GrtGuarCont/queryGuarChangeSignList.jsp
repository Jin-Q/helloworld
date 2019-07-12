<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String flag = "";
%>   
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		GrtGuarCont._toForm(form);
		GrtGuarContList._obj.ajaxQuery(null,form);
	};
	
	function doViewGrtGuarCont() {
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		var flag ="view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&'+paramStr+'&menu=dbbg&oper='+flag+'&menuId=ybCount';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doSignGrtGuarCont(){
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
		if (paramStr != null) {
		    var data = GrtGuarContList._obj.getSelectedData();
		    var status = data[0].guar_cont_state._getValue();
		    var flag ="sign";     
				if(status =='00'){
					var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?'+paramStr+'&menu=dbbg&op=sign&oper='+flag+'&menuId=ybCount';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert('只有登记状态的合同可以进行签订操作！');
				}
			} else { 
				alert('请先选择一条记录！');
			}  
	};
	
	function doReset(){
		page.dataGroups.GrtGuarContGroup.reset();
	};
	function returnCus(data){
		GrtGuarCont.cus_id._setValue(data.cus_id._getValue());
		GrtGuarCont.cus_id_displayname._setValue(data.cus_name._getValue());
    };

    function doPrintGrtGuarCont(){
    	var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);
    	if (paramStr != null) {
        	 alert("打印代码暂未开发!");  
    	} else {
			alert('请先选择一条记录!');
		}

    }
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="GrtGuarContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号 " />
			<emp:text id="GrtGuarCont.cont_no" label="合同编号 "/>
			<emp:select id="GrtGuarCont.guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
			<emp:select id="GrtGuarCont.guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
			<emp:text id="GrtGuarCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
	    <emp:button id="signGrtGuarCont" label="签订" op="sign"/>
		<emp:button id="viewGrtGuarCont" label="查看" op="view"/>
		<emp:button id="printGrtGuarCont" label="打印" op="print"/>
	</div> 

	<emp:table icollName="GrtGuarContList" pageMode="true" url="pageGuarChangeList.do">   
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="cont_no" label="合同编号"/>
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="lmt_grt_flag" label="是否授信项下" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guar_model" label="担保模式" dictname="STD_GUAR_MODEL"/>
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
		
	</emp:table>
	
</body>
</html>
</emp:page>
    