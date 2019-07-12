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
		CtrAssetProCont._toForm(form);
		CtrAssetProContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCtrAssetProContPage() {
		var paramStr = CtrAssetProContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var data = CtrAssetProContList._obj.getSelectedData();
			var cont_status = data[0].cont_status._getValue();
			if(cont_status =='100'){
				var url = '<emp:url action="getCtrAssetProContUpdatePage.do"/>?op=update&cont=cont&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert('合同已签订');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**modified by lisj 2015-3-26 需求编号：【XD150303017】关于资产证券化的信贷改造 begin**/
	function doViewCtrAssetProCont() {
		var paramStr = CtrAssetProContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrAssetProContViewPage.do"/>?op=view&'+paramStr+"&cont_flag=y";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**modified by lisj 2015-3-26 需求编号：【XD150303017】关于资产证券化的信贷改造 end**/
	function doGetAddCtrAssetProContPage() {
		var url = '<emp:url action="getCtrAssetProContAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCtrAssetProCont() {
		var paramStr = CtrAssetProContList._obj.getParamStr(['cont_no']);
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
							var url = '<emp:url action="queryCtrAssetProContList.do"/>';
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
				var url = '<emp:url action="deleteCtrAssetProContRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrAssetProContGroup.reset();
	};

	function doIssue(){
		var paramStr = CtrAssetProContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrAssetProContViewPage.do"/>?op=issue&cont=cont&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CtrAssetProContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrAssetProCont.cont_no" label="项目编号" />
			<emp:text id="CtrAssetProCont.serno" label="业务编号" />
			<emp:select id="CtrAssetProCont.cont_status" label="项目状态" dictname="STD_ZB_PRO_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="issue" label="发行管理" op="issue"/>
		<emp:button id="viewCtrAssetProCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrAssetProContList" pageMode="true" url="pageCtrAssetProContQuery.do">
		<emp:text id="cont_no" label="项目编号" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="pro_name" label="项目名称" />
		<emp:text id="pro_type" label="项目类型" dictname="STD_ZB_ASSET_PRO_TYPE"/>
		<emp:text id="pro_amt" label="项目金额" dataType="Currency"/>
		<emp:text id="pro_qnt" label="笔数" dataType="Int"/>
		<emp:text id="pack_date" label="封包日期" />
		<!-- modified by lisj 2015-3-11  需求编号：【XD150303017】关于资产证券化的信贷改造 -->
		<emp:text id="issue_qnt" label="发行总量" />
		<emp:text id="issue_date" label="发行日期" />
		<emp:text id="final_date" label="终结日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="项目状态" dictname="STD_ZB_PRO_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    