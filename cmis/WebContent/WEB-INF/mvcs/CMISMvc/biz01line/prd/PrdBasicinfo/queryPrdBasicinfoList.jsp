<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdBasicinfo._toForm(form);
		PrdBasicinfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdBasicinfoPage() {
		var paramStr = PrdBasicinfoList._obj.getParamStr(['prdid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdBasicinfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdBasicinfo() {
		var paramStr = PrdBasicinfoList._obj.getParamStr(['prdid']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdBasicinfoViewPage.do"/>?'+paramStr+"&query=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdBasicinfoPage() {
		var url = '<emp:url action="getPrdBasicinfoAddFirstPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdBasicinfo() {
		var paramStr = PrdBasicinfoList._obj.getParamStr(['prdid']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdBasicinfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdBasicinfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdBasicinfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdBasicinfo.prdname" label="产品名称" />
			<emp:text id="PrdBasicinfo.prdid" label="产品编号" />
			<emp:text id="PrdBasicinfo.prdmanager" label="产品经理" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdBasicinfoPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdBasicinfoPage" label="修改" op="update"/>
		<emp:button id="deletePrdBasicinfo" label="删除" op="remove"/>
		<emp:button id="viewPrdBasicinfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdBasicinfoList" pageMode="true" url="pagePrdBasicinfoQuery.do">
	<emp:text id="prdid" label="产品编号" />
		<emp:text id="prdname" label="产品名称" />
		<emp:text id="prdowner" label="产品归属" />
		<emp:text id="supcatalog" label="上级目录" />
		<emp:text id="prddescribe" label="产品描述" hidden="true"/>
		<emp:text id="prdversion" label="产品版本号" />
		<emp:text id="prdmanager" label="产品经理" />
		<emp:text id="startdate" label="生效日期" />
		<emp:text id="enddate" label="结束日期" />
		<emp:select id="prdstatus" label="产品状态" dictname="STD_PRD_STATE"/>
		<emp:text id="guarway" label="可用担保方式" hidden="true" />
		<emp:text id="currency" label="可用币种"  hidden="true"/>
		<emp:text id="preventtactics" label="拦截策略"  hidden="true"/>
		<emp:text id="loanform" label="申请表单"  hidden="true"/>
		<emp:text id="contform" label="合同表单"  hidden="true"/>
		<emp:text id="pvpform" label="出账表单"  hidden="true"/>
		<emp:text id="contmapping" label="合同映射"  hidden="true"/>
		<emp:text id="pvpmapping" label="出账映射"  hidden="true"/>
		<emp:text id="loanflow" label="申请流程"  hidden="true"/>
		<emp:text id="pvpway" label="出账方式"  hidden="true"/>
		<emp:text id="payflow" label="放款流程"  hidden="true"/>
		<emp:text id="repayway" label="还款方式设置" hidden="true"/>
		<emp:text id="costset" label="费用设置" hidden="true" />
		<emp:text id="businessrule" label="业务规则"  hidden="true"/>
		<emp:text id="policytactics" label="政策策略"  hidden="true"/>
		<emp:text id="datacollection" label="资料收集"  hidden="true"/>
		<emp:text id="comments" label="备注" hidden="true"/>
		<emp:text id="inputid" label="登记人员" hidden="true"/>
		<emp:text id="inputid_displayname" label="登记人员" />
		<emp:text id="inputdate" label="登记日期" />
		<emp:text id="orgid" label="登记机构" hidden="true"/>
		<emp:text id="orgid_displayname" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    