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
		IqpBillDetailInfo._toForm(form);
		IqpBillDetailInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateDpoBillDetailPage() {
		var paramStr = IqpBillDetailInfoList._obj.getParamStr(['porder_no']);
		var status = IqpBillDetailInfoList._obj.getParamStr(['status']);
		if (paramStr != null) {
			if(status !="status=00"){
				alert('只有待入池的票据可以进行修改操作！');
			}else{
				var url = '<emp:url action="getDpoBillDetailUpdatePage.do"/>?'+encodeURI(paramStr)+'&drfpo_no=${context.drfpo_no}';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
			
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewDpoBillDetail() {
		var paramStr = IqpBillDetailInfoList._obj.getParamStr(['porder_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getDpoBillDetailViewPage.do"/>?drfpo_no=${context.drfpo_no}&oper=${context.oper}&'+encodeURI(paramStr);
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddDpoBillDetailPage() {
		var url = '<emp:url action="getDpoBillDetailAddPage.do"/>?drfpo_no=${context.drfpo_no}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteDpoBillDetail() {
		var paramStr = IqpBillDetailInfoList._obj.getParamStr(['porder_no']);
		var status = IqpBillDetailInfoList._obj.getParamStr(['status']);
		if (paramStr != null) {
			if(status =="status=00"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o) {
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("Parse jsonstr define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if("success" == flag){
								alert("已关联删除其与票据池的关联记录！");
								window.location.reload();
							}else{
								alert("删除失败！");
							}
						}
					};
					var handleFailure = function(o) {
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					var url = '<emp:url action="deleteDpoBillDetailRecord.do"/>?'+encodeURI(paramStr)+'&drfpo_no=${context.drfpo_no}';
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
				}
			}else{
				alert("只有待入池状态的票据可以进行删除操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBillDetailInfoGroup.reset();
	};
	function doOnLoad(){
		drfpo_no = "${context.drfpo_no}";
	}

	/*--user code begin--*/
	function doImportDpoBillDetail(){
		var url = '<emp:url action="importDpoBillDetailPage.do"/>&drfpo_no='+drfpo_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm">
	<emp:gridLayout id="IqpBillDetailInfoGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpBillDetailInfo.porder_no" label="汇票号码" />
	</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<div align="left">
		<emp:actButton id="getAddDpoBillDetailPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateDpoBillDetailPage" label="修改" op="update"/>
		<emp:actButton id="deleteDpoBillDetail" label="删除" op="remove"/>
		<emp:actButton id="viewDpoBillDetail" label="查看" op="view"/>
		<emp:actButton id="importDpoBillDetail" label="导入" op="excel"/>
	</div>

	<emp:table icollName="IqpBillDetailInfoList" pageMode="true" url="pageDpoBillDetailQuery.do?drfpo_no=${context.drfpo_no}">
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE" />
		<emp:text id="is_ebill" label="是否电票" dictname="STD_ZX_YES_NO" />
		<emp:text id="bill_isse_date" label="票据签发日" />
		<emp:text id="porder_end_date" label="汇票到期日" />
		<emp:text id="porder_curr" label="汇票币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="status" label="票据状态" dictname="STD_ZB_DRFPO_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    