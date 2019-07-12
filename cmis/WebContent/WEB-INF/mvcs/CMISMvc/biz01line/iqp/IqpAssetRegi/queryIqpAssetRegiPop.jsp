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
		IqpAssetRegi._toForm(form);
		IqpAssetRegiList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetRegiPage() {
		var paramStr = IqpAssetRegiList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetRegiUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetRegi() {
		var paramStr = IqpAssetRegiList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetRegiViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetRegiPage() {
		var url = '<emp:url action="getIqpAssetRegiAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetRegi() {
		var paramStr = IqpAssetRegiList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryIqpAssetRegiList.do"/>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除异常!");
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteIqpAssetRegiRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetRegiGroup.reset();
	};
	
	function doSelect(){
		var data = IqpAssetRegiList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};  
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetRegiGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetRegi.bill_no" label="借据编号" />
			<emp:select id="IqpAssetRegi.asset_status" label="资产状态" dictname="STD_ZB_ASSET_REGI_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <button onclick="doSelect()">选取返回</button>
	</div>

	<emp:table icollName="IqpAssetRegiList" pageMode="true" url="pageIqpAssetRegiPopQuery.do?regi_type=${context.regi_type}" >
		<emp:text id="serno" label="业务编号" />
		<emp:text id="regi_type" label="业务类别" dictname="STD_ZB_REGI_TYPE"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户编号" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="inner_owe_int" label="表内欠息" hidden="true" />
		<emp:text id="out_owe_int" label="表外欠息" hidden="true" />
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT" hidden="true"/>
		<emp:text id="twelve_cls_flg" label="十二级分类" dictname="STD_ZB_TWELVE_CLASS" hidden="true"/>
		<emp:text id="approve_status" label="审批状态" hidden="true"/>
		<emp:text id="asset_status" label="资产状态" dictname="STD_ZB_ASSET_REGI_STATUS"/>
		
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" hidden="true"/>
		<emp:text id="fina_br_id" label="账务机构" hidden="true"/>
		<emp:text id="fina_br_id_displayname" label="账务机构" hidden="true"/>
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    