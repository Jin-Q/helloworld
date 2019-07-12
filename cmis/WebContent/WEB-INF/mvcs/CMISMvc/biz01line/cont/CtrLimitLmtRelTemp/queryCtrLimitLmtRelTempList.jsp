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
		CtrLimitLmtRelTemp._toForm(form);
		CtrLimitLmtRelTempList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCtrLimitLmtRelTempPage() {
		var paramStr = CtrLimitLmtRelTempList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrLimitLmtRelTempUpdatePage.do"/>?cus_id=${context.cus_id}&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrLimitLmtRelTemp() {
		var paramStr = CtrLimitLmtRelTempList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrLimitLmtRelTempViewPage.do"/>?cus_id=${context.cus_id}&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCtrLimitLmtRelTempPage() {
		var url = '<emp:url action="getCtrLimitLmtRelTempAddPage.do"/>?serno=${context.serno}&cont_no=${context.cont_no}&cus_id=${context.cus_id}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCtrLimitLmtRelTemp() {
		var paramStr = CtrLimitLmtRelTempList._obj.getParamStr(['pk_id']);
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
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("删除出错！");
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
				var url = '<emp:url action="deleteCtrLimitLmtRelTempRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrLimitLmtRelTempGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddCtrLimitLmtRelTempPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateCtrLimitLmtRelTempPage" label="修改" op="update"/>
		<emp:actButton id="deleteCtrLimitLmtRelTemp" label="删除" op="remove"/>
		<emp:actButton id="viewCtrLimitLmtRelTemp" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrLimitLmtRelTempList" pageMode="false" url="pageCtrLimitLmtRelTempQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"/>
		<emp:text id="limit_serno" label="额度合同业务编号" hidden="true"/>
		<emp:text id="limit_cont_no" label="额度合同合同编号" hidden="true"/>
		<emp:text id="lmt_code_no" label="授信额度编号" />
		<emp:text id="lmt_code_name" label="授信额度品种名称" />
		<emp:text id="lmt_code_amt" label="授信额度金额" dataType="Currency"/>
		<emp:text id="lmt_code_enable_amt" label="授信额度可用金额" dataType="Currency"/>
		<emp:text id="status" label="状态" hidden="true" />
	</emp:table>
	
</body>
</html>
</emp:page>
    