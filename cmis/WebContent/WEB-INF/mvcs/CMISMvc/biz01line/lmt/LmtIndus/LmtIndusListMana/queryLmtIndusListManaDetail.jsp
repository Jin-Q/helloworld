<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
};
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusListManaList.do"/>?agr_no=${context.agr_no}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function changeHeight(){
		var iframeid = document.getElementById("rightframe");
		iframeid.height = "80px";
		iframeid.style.height = "80px";
		if(iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){
			iframeid.height = iframeid.contentDocument.body.offsetHeight;
		}else if(iframeid.Document && iframeid.Document.body.scrollHeight){
			iframeid.height = iframeid.Document.body.scrollHeight;
		}
		if(iframeid.height != "undefined")
			iframeid.style.height = iframeid.height + "px";
	};
	function doLoad(){
		LmtIndusListMana.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		showOrHiddenDiv();
	};
	//控制div的显示隐藏
	function showOrHiddenDiv(){
		var is_limit_set = LmtIndusListMana.is_do_limit._getValue();
		if(is_limit_set=='1'){
			document.getElementById("agr_det_div").style.display = "";
		}else{
			document.getElementById("agr_det_div").style.display = "none";
		}
	};
	//查看台账明细
	function doViewLmtAgrDetails(){
		var paramStr = LmtAgrDetailsList._obj.getParamStr(['limit_code','sub_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrDetailsJointViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtIndusListMana.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="LmtIndusListManaGroup" title="行业名单管理表" maxColumn="2">
			<emp:text id="LmtIndusListMana.agr_no" label="协议编号" maxlength="40" required="true" />
			<emp:text id="LmtIndusListMana.cus_id" label="客户码" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtIndusListMana.cus_id_displayname" label="客户名称" colSpan="2" required="true"
			readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="LmtIndusListMana.is_do_limit" label="是否进行额度设置" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtIndusListMana.status" label="状态" required="true" dictname="STD_ZB_LIST_STATUS" readonly="true" />
			<emp:textarea id="LmtIndusListMana.memo" label="备注" maxlength="250" required="false" colSpan="2" />
	</emp:gridLayout>
	<div align="center"><br>
	<emp:button id="return" label="返回列表" /></div>

	<div id="agr_det_div">
	<div class='emp_gridlayout_title' >额度信息&nbsp;</div>
	<div align="left">
		<emp:button id="viewLmtAgrDetails" label="查看" />
	</div>
	<emp:table icollName="LmtAgrDetailsList" pageMode="false" url="">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="limit_code" label="授信额度编号" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE" />
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="crd_amt" label="授信金额(元)" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
	</emp:table>
	</div>
</body>
</html>
</emp:page>
