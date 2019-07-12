<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
//加载事件
	function onLoad(){
		var options = WfiLvOverdrawnRight.belg_line._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
		//去除个人业务条线/所有业务条线
			if(options[i].value == "BL300" || options[i].value == "BL_ALL"){
				options.remove(i);
			}
		}
	}
	function doQuery(){
		var form = document.getElementById('queryForm');
		WfiLvOverdrawnRight._toForm(form);
		WfiLvOverdrawnRightList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiLvOverdrawnRightPage() {
		var paramStr = WfiLvOverdrawnRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvOverdrawnRightUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiLvOverdrawnRight() {
		var paramStr = WfiLvOverdrawnRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiLvOverdrawnRightViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiLvOverdrawnRightPage() {
		var url = '<emp:url action="getWfiLvOverdrawnRightAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteWfiLvOverdrawnRight() {
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
				if("success" == flag){
					alert("删除成功！");
					window.location.reload();
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var paramStr = WfiLvOverdrawnRightList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteWfiLvOverdrawnRightRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
			
		} else {
			alert('请先选择一条记录！');
		}

	};
	
	function doReset(){
		page.dataGroups.WfiLvOverdrawnRightGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	function doInitWfiLvCreditRight() {
		var url = '<emp:url action="getWfiLvOverdrawnRightLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
      	window.open(url,'viewOrgInfo','height=450,width=650,top=350,left=250,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};	
	function getOrganName(data){
		WfiLvOverdrawnRight.org_id._setValue(data.organno._getValue());
		WfiLvOverdrawnRight.org_id_displayname._setValue(data.organname._getValue());
	};
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="WfiLvOverdrawnRightGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiLvOverdrawnRight.org_id" label="机构码" hidden="true" colSpan="2" />
		<emp:pop id="WfiLvOverdrawnRight.org_id_displayname" label="机构名称" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
		<emp:select id="WfiLvOverdrawnRight.belg_line" label="所属条线"  dictname="STD_ZB_BUSILINE" />
		<emp:select id="WfiLvOverdrawnRight.is_inuse" label="是否控制透支额度" dictname="STD_ZX_YES_NO" />
		<emp:select id="WfiLvOverdrawnRight.is_ctrl" label="是否控制单户限额" dictname="STD_ZX_YES_NO"  />
		
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="initWfiLvCreditRight" label="初始化透支配置" op="init"/>
		<emp:button id="getUpdateWfiLvOverdrawnRightPage" label="修改" op="update"/>
		<emp:button id="deleteWfiLvOverdrawnRight" label="删除" op="remove"/>
		<emp:button id="viewWfiLvOverdrawnRight" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiLvOverdrawnRightList" pageMode="true" url="pageWfiLvOverdrawnRightQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="org_id" label="机构码" />
		<emp:text id="org_id_displayname" label="机构名称" />
		<emp:select id="belg_line" label="所属条线" dictname="STD_ZB_BUSILINE"  />
		<emp:text id="overdrawn_amt" label="透支额度金额" dataType="Currency" />
		<emp:text id="is_inuse" label="是否控制透支额度" dictname="STD_ZX_YES_NO" />
		<emp:text id="pledge_amt" label="质押限额" maxlength="16" dataType="Currency" />
		<emp:text id="impawn_amt" label="抵押限额" maxlength="16" dataType="Currency" />
		<emp:text id="fullpledge_amt" label="准全额质押限额" maxlength="16" dataType="Currency" />
		<emp:text id="riskpledge_amt" label="低风险质押限额" maxlength="16" dataType="Currency" />
		<emp:text id="guarantee_amt" label="保证限额" maxlength="16" dataType="Currency" />
		<emp:text id="credit_amt" label="信用限额" maxlength="16" dataType="Currency" />
		<emp:select id="is_ctrl" label="是否控制单户限额" dictname="STD_ZX_YES_NO"  />
	</emp:table>
	
</body>
</html>
</emp:page>
    