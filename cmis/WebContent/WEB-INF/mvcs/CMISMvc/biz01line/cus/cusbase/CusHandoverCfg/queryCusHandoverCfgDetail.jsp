<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusHandoverCfgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewCusHandoverDetail() {
		var paramStr = CusHandoverCfg.CusHandoverDetail._obj.getParamStr(['sub_serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusHandoverCfgCusHandoverDetailDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusHandoverCfgGroup" title="客户移交配置主表" maxColumn="2">
			<emp:select id="CusHandoverCfg.table_mode" label="移交方式" required="true" dictname="STD_ZB_HAND_TYPE" />
			<emp:select id="CusHandoverCfg.table_scope" label="移交范围" required="true" dictname="STD_ZB_CFG_SCOPE" /> 
			
			<emp:textarea id="CusHandoverCfg.ext_class" label="扩展处理" maxlength="200" required="false" colSpan="2"/>
			<emp:textarea id="CusHandoverCfg.memo" label="备注" maxlength="300" required="false" />
			<emp:text id="CusHandoverCfg.serno" label="序列号" maxlength="40" required="true" readonly="true" hidden="true"/>
	</emp:gridLayout>
	<fieldSet style="font-weight: 800"><legend>扩展处理使用说明：</legend> 
	    <blockquote>根据需要实现此扩展处理类</blockquote>
		<blockquote>扩展处理类必须继承接口 com.yucheng.cmis.biz01line.cus.cushand.extInterface.HandoverInterface</blockquote>
		<blockquote>此接口包含beforAction()和afterAction()2个方法。beforAction()移交前的处理，afterAction()移交后的处理</blockquote>
		</fieldSet>
	<br>

	<emp:tabGroup id="CusHandoverCfg_tabs" mainTab="CusHandoverDetail_tab">
		<emp:tab id="CusHandoverDetail_tab" label="客户移交配置信息表">
			<div align="left">
				<emp:button id="viewCusHandoverDetail" label="查看" />
			</div>
			<emp:table icollName="CusHandoverCfg.CusHandoverDetail" pageMode="false" url="">
		<emp:text id="sub_serno" label="主键" hidden="true"/>
		<emp:text id="serno" label="序列号" hidden="true"/>
		<emp:text id="table_code" label="表编码" />
		<emp:text id="table_name" label="表名称" />
		<emp:text id="ext_sql" label="执行语句" />
		<emp:text id="memo" label="备注" />
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
