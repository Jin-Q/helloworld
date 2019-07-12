<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryArpBadassetHandoverRcvList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doUpdate(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
		            alert('接收成功!');
		            doReturn();
				}
			}
		};
		var handleFailure = function(o) {
			alert("接收失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = ArpBadassetHandoverRcv._checkAll();
		if(result){
			ArpBadassetHandoverRcv._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function doSubmits() {
		if(confirm("是否确认要接收？")){
			doUpdate();
		}
	};
	function doload(){
		addContForm(ArpBadassetHandoverRcv);
		addCusForm(ArpBadassetHandoverRcv);
		addBillForm(ArpBadassetHandoverRcv);
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="不良资产移交信息" id="main_tabs">
	<emp:form id="submitForm" action="updateArpBadassetHandoverRcvRecord.do" method="POST">	
		<emp:gridLayout id="ArpBadassetHandoverRcvGroup" maxColumn="2" title="借据信息">	
			<emp:text id="ArpBadassetHandoverRcv.bill_no" label="借据编号" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverRcv.cont_no" label="合同编号" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverRcv.cont_no_displayname" label="中文合同编号" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverRcv.prd_id_displayname" label="产品类别" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverRcv.cus_id" label="客户码" required="true" readonly="true" />
			<emp:text id="ArpBadassetHandoverRcv.cus_id_displayname" label="客户名称" colSpan="2" readonly="true"
			required="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="ArpBadassetHandoverRcv.cur_type" label="币种" required="true" readonly="true"  dictname="STD_ZX_CUR_TYPE" colSpan="2"/>
			<emp:text id="ArpBadassetHandoverRcv.loan_amt" label="借据金额（元）" required="true" readonly="true" dataType="Currency"/>
			<emp:text id="ArpBadassetHandoverRcv.loan_balance" label="借据余额（元）" required="true" readonly="true" dataType="Currency"/>
			<emp:select id="ArpBadassetHandoverRcv.five_class" label="五级分类标志" required="true" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="ArpBadassetHandoverRcv.four_class" label="四级分类标志" required="true" readonly="true" dictname="STD_ZB_FOUR_SORT"/>
		</emp:gridLayout>
		<emp:gridLayout id="ArpBadassetHandoverRcvGroup" maxColumn="2" title="不良资产移交接收信息">
			<emp:text id="ArpBadassetHandoverRcv.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="ArpBadassetHandoverRcv.handover_resn" label="移交原因" required="true" dictname="STD_ZB_HANDOVER_RESN" readonly="true"/>
			<emp:text id="ArpBadassetHandoverRcv.fount_manager_br_id_displayname" label="原管理机构" required="true" readonly="true"/>
			<emp:text id="ArpBadassetHandoverRcv.fount_manager_id_displayname" label="原主管客户经理" required="true" readonly="true"/>
			<emp:text id="ArpBadassetHandoverRcv.rcv_org_displayname" label="接收机构" required="true" readonly="true"/>
			<emp:text id="ArpBadassetHandoverRcv.rcv_person_displayname" label="接收人员" required="true"  readonly="true"/>
			<emp:text id="ArpBadassetHandoverRcv.fount_manager_br_id" label="原管理机构" maxlength="20" hidden="true" />
			<emp:text id="ArpBadassetHandoverRcv.fount_manager_id" label="原主管客户经理" maxlength="20" hidden="true" />
			<emp:text id="ArpBadassetHandoverRcv.rcv_org" label="接收机构" maxlength="20" hidden="true" />
			<emp:text id="ArpBadassetHandoverRcv.rcv_person" label="接收人员" maxlength="20" hidden="true" />
			<emp:textarea id="ArpBadassetHandoverRcv.bad_resn" label="不良成因及分析" maxlength="250" required="true" colSpan="2" readonly="true" />
			<emp:textarea id="ArpBadassetHandoverRcv.collect_measures" label="清收措施" maxlength="250" required="true" colSpan="2" readonly="true" />
			<emp:textarea id="ArpBadassetHandoverRcv.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true" />
			<emp:date id="ArpBadassetHandoverRcv.app_date" label="申请日期" required="true" readonly="true" />
			<emp:date id="ArpBadassetHandoverRcv.over_date" label="办结日期" required="true" readonly="true" />
			<emp:date id="ArpBadassetHandoverRcv.rcv_date" label="接收日期" required="false" hidden="true" />
			<emp:select id="ArpBadassetHandoverRcv.rcv_status" label="接收状态" required="false" dictname="STD_ZB_RCV_STATUS" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="接收" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
			</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>