<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
%>

<script type="text/javascript">
	
	function doReturn() {
		var agr_no = LmtNameList.agr_no._getValue();
		var url = '<emp:url action="queryLmtNameListList.do"/>?agr_no='+agr_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doClose(){
		window.close();
	}
	
	function doOnload(){
		LmtNameList.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		showOrHiddenDiv();
	}
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtNameList.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow2','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	//查看台账明细
	function doViewLmtAgrDetails(){
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code','sub_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsJointViewPage.do"/>?'+paramStr;//+'&serno='+serno+'&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}';
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	}
	//控制div的显示隐藏
	function showOrHiddenDiv(){
		var is_limit_set = LmtNameList.is_limit_set._getValue();
		if(is_limit_set=='1'){
			document.getElementById("agr_det_div").style.display = "";
		}else{
			document.getElementById("agr_det_div").style.display = "none";
		}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:gridLayout id="LmtNameListGroup" title="名单表" maxColumn="2">
			<emp:text id="LmtNameList.agr_no" label="协议编号" maxlength="40" required="false" colSpan="2"/>
			<emp:text id="LmtNameList.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="LmtNameList.cus_id_displayname" label="客户名称" required="false" />
			<emp:select id="LmtNameList.is_limit_set" label="是否进行额度设置" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:textarea id="LmtNameList.memo" label="备注" maxlength="400" required="false" colSpan="2" />
			<emp:text id="LmtNameList.bail_rate" label="保证金缴存比例" maxlength="4" required="false" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtNameList.cus_status" label="客户状态" required="true" dictname="STD_LMT_CUS_STATUS" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if("bizArea".equals(type)){ %>
		<emp:button id="close" label="关闭"/>
		<%}else{ %>
		<emp:button id="return" label="返回"/>
		<%} %>
	</div>
	
	<div id="agr_det_div">
	<div class='emp_gridlayout_title' >成员额度信息&nbsp;</div>
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" />
	</div>

	<emp:table icollName="LmtAgrDetailsList" pageMode="false" url="">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="limit_name" label="额度品种名称" hidden="true" />
		<emp:text id="limit_name_displayname" label="额度品种名称" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
	</emp:table>
	</div>
</body>
</html>
</emp:page>
