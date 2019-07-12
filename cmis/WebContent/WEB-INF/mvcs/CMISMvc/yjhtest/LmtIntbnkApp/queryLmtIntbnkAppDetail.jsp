<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>淇敼椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIntbnkAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewLmtIntbnkAppDetails() {
		var paramStr = LmtIntbnkApp.LmtIntbnkAppDetails._obj.getParamStr(['serno','crd_item_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryLmtIntbnkAppLmtIntbnkAppDetailsDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="LmtIntbnkAppGroup" title="金融同业授信申请" maxColumn="2">
			<emp:text id="LmtIntbnkApp.serno" label="流水号" maxlength="40" required="true" readonly="true" />
			<emp:text id="LmtIntbnkApp.bank_no" label="同业机构行号" maxlength="30" required="false" />
			<emp:text id="LmtIntbnkApp.bank_name" label="同业机构名称" maxlength="60" required="false" />
			<emp:text id="LmtIntbnkApp.bank_type" label="同业机构类型" maxlength="1" required="false" />
			<emp:text id="LmtIntbnkApp.crd_biz_type" label="授信业务类型" maxlength="2" required="false" />
			<emp:text id="LmtIntbnkApp.crd_lmt_type" label="授信额度类型" maxlength="2" required="false" />
			<emp:text id="LmtIntbnkApp.auto_score" label="机评得分" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.auto_grade" label="机评等级" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.crd_lmt_amt" label="授信限额" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkApp.crd_totl_amt" label="授信总额" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkApp.approve_status" label="审批状态" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.cur_type" label="币种" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.term_type" label="期限类型" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.term" label="期限" maxlength="38" required="false" />
			<emp:text id="LmtIntbnkApp.apply_date" label="申请日期" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.input_date" label="登记日期" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.update_date" label="修改日期" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.input_id" label="申请人" maxlength="20" required="false" />
			<emp:text id="LmtIntbnkApp.cus_manager" label="客户经理" maxlength="20" required="false" />
			<emp:text id="LmtIntbnkApp.input_br_id" label="申请机构" maxlength="20" required="false" />
			<emp:text id="LmtIntbnkApp.mng_br_id" label="主管机构" maxlength="20" required="false" />
			<emp:text id="LmtIntbnkApp.remarks" label="备注" maxlength="250" required="false" />
			<emp:text id="LmtIntbnkApp.lmt_serno" label="授信协议编号" maxlength="40" required="false" />
	</emp:gridLayout>
	
	<br>

	<emp:tabGroup id="LmtIntbnkApp_tabs" mainTab="LmtIntbnkAppDetails_tab">
		<emp:tab id="LmtIntbnkAppDetails_tab" label="同业授信申请明细">
			<div align="left">
				<emp:button id="viewLmtIntbnkAppDetails" label="鏌ョ湅" op="view_LmtIntbnkAppDetails"/>
			</div>
			<emp:table icollName="LmtIntbnkApp.LmtIntbnkAppDetails" pageMode="false" url="">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="bank_no" label="同业机构行号" />
		<emp:text id="bank_name" label="同业机构名称" />
		<emp:text id="crd_item_id" label="授信明细" />
		<emp:text id="cur_type" label="币种" />
		<emp:text id="crd_lmt" label="授信额度" />
		<emp:text id="margin_ratio" label="保证金比例" />
		<emp:text id="mini_rate" label="最低利率" />
		<emp:text id="mini_fare" label="最低费率" />
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="杩斿洖鍒板垪琛ㄩ〉闈�"/>
	</div>

</body>
</html>
</emp:page>
