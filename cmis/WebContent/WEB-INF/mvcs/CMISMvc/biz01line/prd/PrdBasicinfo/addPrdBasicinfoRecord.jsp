<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPrdBasicinfoRecord.do" method="POST">
		<emp:gridLayout id="" title="基本信息" maxColumn="2">
			<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="40" required="false" />
			<emp:text id="PrdBasicinfo.prdid" label="产品编号" maxlength="6" required="true" />
			<emp:text id="PrdBasicinfo.prddescribe" label="产品描述" maxlength="200" required="false" />
			<emp:text id="PrdBasicinfo.prdversion" label="产品版本号" maxlength="10" required="false" />
			<emp:text id="PrdBasicinfo.prdmanager" label="产品经理" maxlength="30" required="false" />
			<emp:text id="PrdBasicinfo.supcatalog" label="上级目录" maxlength="100" required="false" />
			<emp:text id="PrdBasicinfo.prdowner" label="产品归属" maxlength="40" required="false" />
			<emp:text id="PrdBasicinfo.prdstatus" label="产品状态" maxlength="10" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="PrdBasicinfoGroup" title="产品配置表" maxColumn="2">
			
			<emp:text id="PrdBasicinfo.guarway" label="可用担保方式" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.currency" label="可用币种" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.preventtactics" label="拦截策略" maxlength="40" required="false" />
			<emp:text id="PrdBasicinfo.loanform" label="申请表单" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.contform" label="合同表单" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.pvpform" label="出账表单" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.contmapping" label="合同映射" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.pvpmapping" label="出账映射" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.loanflow" label="申请流程" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.pvpway" label="出账方式" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.payflow" label="放款流程" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.repayway" label="还款方式设置" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.costset" label="费用设置" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.businessrule" label="业务规则" maxlength="40" required="false" />
			<emp:text id="PrdBasicinfo.policytactics" label="政策策略" maxlength="20" required="false" />
			<emp:text id="PrdBasicinfo.datacollection" label="资料收集" maxlength="40" required="false" />
			<emp:text id="PrdBasicinfo.comments" label="备注" maxlength="100" required="false" />
			<emp:text id="PrdBasicinfo.inputid" label="登记人员" maxlength="30" required="false" />
			<emp:text id="PrdBasicinfo.inputdate" label="登记日期" maxlength="10" required="false" />
			<emp:text id="PrdBasicinfo.orgid" label="登记机构" maxlength="20" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

