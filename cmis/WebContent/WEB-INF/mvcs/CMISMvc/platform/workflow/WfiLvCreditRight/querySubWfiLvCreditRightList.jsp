<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doOnLoad(){
		//剔除条线
		var optionJosn4BL = "BL100,BL200,BL300";
		var options4BL =WfiLvCreditRight.belg_line._obj.element.options;		
		for ( var i = options4BL.length - 1; i >= 0; i--) {
			if(optionJosn4BL.indexOf(options4BL[i].value)<0){
				options4BL.remove(i);
			}
		}
		//剔除担保方式
		var optionJosn4AM = "100,200,300,400,210,220";
		var options4AM =WfiLvCreditRight.assure_main._obj.element.options;		
		for ( var i = options4AM.length - 1; i >= 0; i--) {
			if(optionJosn4AM.indexOf(options4AM[i].value)<0){
				options4AM.remove(i);
			}
		}
		//剔除社区支行选项
		var optionJosn4OL = "1,2,3,4,5,6";
		var options4OL =WfiLvCreditRight.org_lvl._obj.element.options;		
		for ( var i = options4OL.length - 1; i >= 0; i--) {
			if(optionJosn4OL.indexOf(options4OL[i].value)<0){
				options4OL.remove(i);
			}
		}
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiLvCreditRight._toForm(form);
		WfiLvCreditRightList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.WfiLvCreditRightGroup.reset();
	};
	
	function doGetUpdateWfiLvCreditRightPage() {
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvCreditRightUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiLvCreditRight() {
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvCreditRightViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doInitWfiLvCreditRight() {
		var url = '<emp:url action="getWfiLvCreditRightOrgInfoViewPage.do"/>?right_type=02';
		url = EMPTools.encodeURI(url);
      	window.open(url,'viewOrgInfo','height=200,width=400,top=300,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	//返回主管机构
	function getOrganName(data){
		WfiLvCreditRight.org_id._setValue(data.organno._getValue());
		WfiLvCreditRight.org_id_displayname._setValue(data.organname._getValue());
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="WfiLvCreditRightGroup" title="输入查询条件" maxColumn="2">
		<emp:select id="WfiLvCreditRight.org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" />
		<emp:select id="WfiLvCreditRight.belg_line" label="机构条线" dictname="STD_ZB_BUSILINE" />
		<emp:select id="WfiLvCreditRight.assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		 <emp:pop id="WfiLvCreditRight.org_id_displayname" label="机构名称" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
		 <emp:text id="WfiLvCreditRight.org_id" label="机构码" hidden="true" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="initWfiLvCreditRight" label="初始化授权配置" />
		<emp:button id="getUpdateWfiLvCreditRightPage" label="修改" op="update"/>
		<emp:button id="viewWfiLvCreditRight" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiLvCreditRightList" pageMode="true" url="pageWfiLvCreditRightQuery.do?right_type=02">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="org_id" label="机构码" />
		<emp:text id="org_id_displayname" label="机构名称" />
		<emp:text id="org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" />
		<emp:text id="belg_line" label="机构条线" dictname="STD_ZB_BUSILINE"/>
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="sub_new_crd_amt" label="分/支行新增授信审批金额（万元）" dataType="Currency" />
		<emp:text id="sub_stock_crd_amt" label="分/支行存量授信审批金额（万元）"  dataType="Currency"/>
		<emp:text id="right_type" label="权限类型"  dictname="STD_ZB_RIGHT_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    