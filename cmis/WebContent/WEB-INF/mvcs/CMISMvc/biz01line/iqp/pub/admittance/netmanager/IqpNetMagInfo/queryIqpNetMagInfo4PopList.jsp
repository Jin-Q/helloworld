<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpNetMagInfo._toForm(form);
		IqpNetMagInfoList._obj.ajaxQuery(null,form);
	};
	/*--user code begin--*/
	/*-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start --*/
	function doSelect(){
		doReturnMethod();
	}
	function doReset(){
		page.dataGroups.IqpNetMagInfoGroup.reset();
	};
	
	function doReturnMethod(){
		var data = IqpNetMagInfoList._obj.getSelectedData();
		if(data != null && data.length !=0){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		}else{
			alert('请先选择一条记录！');
		}
	};	
	/*-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end --*/
	function getCusInfo(data){
		IqpNetMagInfo.cus_id._setValue(data.cus_id._getValue());
		IqpNetMagInfo.cus_id_displayname._setValue(data.cus_name._getValue());
	};			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpNetMagInfoGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpNetMagInfo.net_agr_no" label="网络编号"/>
		<emp:pop id="IqpNetMagInfo.cus_id_displayname" label="核心企业客户名称" url="queryAllCusPop.do?cusTypCondition=BELG_LINE IN('BL100','BL200') and cus_status='20'&returnMethod=getCusInfo" />		
		<emp:select id="IqpNetMagInfo.status" label="网络状态" dictname="STD_ZB_STATUS" defvalue="1" hidden="true"/>
		<emp:text id="IqpNetMagInfo.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start -->
	<emp:returnButton id="s1" label="选择返回"/>
	<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end -->
	<emp:table icollName="IqpNetMagInfoList" pageMode="true" url="pageIqpNetAgrNo.do" reqParams="cus_id=${context.cus_id}&biz_type=${context.biz_type}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="net_agr_no" label="网络编号" />
		<emp:text id="cus_id" label="核心企业客户码" />
		<emp:text id="cus_id_displayname" label="核心企业客户名称" />
		<emp:text id="coop_term_type" label="合作期限类型"  dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="coop_term" label="合作期限" />
		<emp:text id="respond_mode" label="承担责任方式" dictname="STD_ZB_RESPOND_MODE" />
		<emp:text id="net_build_date" label="网络建立日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="status" label="网络状态" dictname="STD_ZB_STATUS" />
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	<div align="left"><br>
	<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start -->
	<emp:returnButton id="s2" label="选择返回"/> <br>
	<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</div>
</body>
</html>
</emp:page>
    