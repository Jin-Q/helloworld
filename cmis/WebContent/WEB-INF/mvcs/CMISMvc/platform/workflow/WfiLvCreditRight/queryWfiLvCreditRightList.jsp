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
	
	function doGetAddWfiLvCreditRightPage() {
		var url = '<emp:url action="getWfiLvCreditRightAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteWfiLvCreditRight() {
		var paramStr = WfiLvCreditRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiLvCreditRightRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败！");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();
						}else if(msg=='fail'){
							alert('删除失败!');
						}else{
							alert('删除成功，但写入操作记录异常！');
						}
					}	
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
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
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddWfiLvCreditRightPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiLvCreditRightPage" label="修改" op="update"/>
		<emp:button id="deleteWfiLvCreditRight" label="删除" op="remove"/>
		<emp:button id="viewWfiLvCreditRight" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiLvCreditRightList" pageMode="true" url="pageWfiLvCreditRightQuery.do?right_type=01">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" />
		<emp:text id="belg_line" label="机构条线" dictname="STD_ZB_BUSILINE"/>
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="new_crd_amt" label="新增授信审批金额（万元）" dataType="Currency" />
		<emp:text id="stock_crd_amt" label="存量授信审批金额（万元）"  dataType="Currency"/>
		<emp:text id="right_type" label="权限类型"  dictname="STD_ZB_RIGHT_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    