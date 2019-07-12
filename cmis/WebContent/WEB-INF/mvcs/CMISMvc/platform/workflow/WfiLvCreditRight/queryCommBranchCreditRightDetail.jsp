<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head>
<title>详情页面</title>
<jsp:include page="/include.jsp" flush="true" />
<script type="text/javascript">
   function doOnLoad(){
	   var is_life_loan = WfiOrgLifeloanRel.is_life_loan._getValue();
    	if(is_life_loan!=null && is_life_loan =="1"){
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderHidden(false);
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderRequired(true);
    	}else{
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderHidden(true);
    		WfiOrgLifeloanRel.life_loan_crd_amt._obj._renderRequired(false);
    		WfiOrgLifeloanRel.life_loan_crd_amt._setValue('');
       }
   };
   
	function doClose(){
		window.close();
	};

	function doViewWfiLvCreditRight4CB() {
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvCreditRight4CBViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="#" method="POST">
<div class='emp_gridlayout_title'>社区支行列表</div>
	<div align="left">
		<emp:button id="viewWfiLvCreditRight4CB" label="查看" />
	</div>
	<emp:table icollName="WfiLvCreditRightList" pageMode="true" url="#">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="org_id" label="社区支行编码" />
		<emp:text id="cb_org_name" label="社区支行名称" />
		<emp:text id="org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" hidden="true"/>
		<emp:text id="belg_line" label="客户条线" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="new_crd_amt" label="新增授信审批金额（万元）" dataType="Currency" />
		<emp:text id="stock_crd_amt" label="存量授信审批金额（万元）"  dataType="Currency"/>
		<emp:text id="right_type" label="权限类型"  dictname="STD_ZB_RIGHT_TYPE"/>
	</emp:table>
	<br>
	<emp:gridLayout id="WfiOrgLifeloanRelGroup" title="生活贷权限管理" maxColumn="2">
		<emp:select id="WfiOrgLifeloanRel.is_life_loan" label="是否开通生活贷" dictname="STD_ZX_YES_NO" required="true" onchange="select()" colSpan="2" readonly="true"/>
		<emp:text id="WfiOrgLifeloanRel.life_loan_crd_amt" label="生活贷授信审批金额(万元)" dataType="Currency" required="false" hidden="true" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="WfiOrgLifeloanRel.org_id" label="机构码" hidden="true" readonly="true"/>
		<emp:text id="WfiOrgLifeloanRel.pk_id" label="主键" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="close" label="关闭" />
	</div>
	</emp:form>
	</body>
	</html>
</emp:page>