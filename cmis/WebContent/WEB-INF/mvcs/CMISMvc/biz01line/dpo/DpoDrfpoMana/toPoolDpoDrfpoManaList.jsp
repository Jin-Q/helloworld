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
		DpoDrfpoManaList._obj.ajaxQuery(null,form);
	};

	
	function doGetUpdateDpoBillDetailPage() {
		var data = IqpDrfpoManaList._obj.getSelectedData();
		if(data.length>1){
			alert("不能选取多条记录！");
		}else{
			if (data.length != 0) {
				var drfpo_no = data[0].drfpo_no._getValue();
				var porder_no = data[0].porder_no._getValue();
				var status = data[0].status._getValue();
				if(status=="04"){
					alert("已入池记账，不能进行修改！");
					return;
				}
				var url = '<emp:url action="getDpoBillDetailUpdatePage.do"/>?porder_no='+encodeURI(porder_no)+'&tab=tab&drfpo_no='+encodeURI(drfpo_no);
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}
		}
	};
	
	function doViewDpoBillDetail() {
		var data = IqpDrfpoManaList._obj.getSelectedData();
		if(data.length>1){
			alert("不能选取多条记录！");
		}else{
			if (data.length != 0) {
				var drfpo_no = data[0].drfpo_no._getValue();
				var porder_no = data[0].porder_no._getValue();
				var url = '<emp:url action="getDpoBillDetailViewPage.do"/>?porder_no='+encodeURI(porder_no)+'&tab=tab&drfpo_no='+encodeURI(drfpo_no);
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
				window.open(url,'newWindow',param);
			} else {
				alert('请先选择一条记录！');
			}
		}
	};
	
	function doGetAddDpoBillDetailPage() {
		var url = '<emp:url action="getDpoBillDetailAddPage.do"/>?drfpo_no=${context.drfpo_no}&tab=tab';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteDpoBillDetail() {
		var data = IqpDrfpoManaList._obj.getSelectedData();
		if(data.length>1){
			alert("不能选取多条记录！");
		}else{
			if (data.length != 0) {
				var status = data[0].status._getValue();
				if (status=="00") {
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
									alert("已级联删除与票据池的关联记录！");
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
						var drfpo_no = data[0].drfpo_no._getValue();
						var porder_no = data[0].porder_no._getValue();
						var url = '<emp:url action="deleteDpoBillDetailRecord.do"/>?porder_no='+encodeURI(porder_no)+'&drfpo_no='+encodeURI(drfpo_no);
						url = EMPTools.encodeURI(url);
				 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
					}
				}else{
					alert("只有待入池状态的票据可以进行删除操作！");
				}
			} else {
			alert('请先选择一条记录！');
			}
		}
	};
	function doSureToPool(){
		var data = IqpDrfpoManaList._obj.getSelectedData();
		var porder_no = new Array();
		for(var i=0;i<data.length;i++){
			porder_no.push(data[i].porder_no._getValue());
			var status = data[i].status._getValue();
			if(status=="04"){
				alert("已入池记账，不能重复发起入池！");
				return;
			}
		}
		if (data.length != 0) {
			if(confirm("是否确认入池？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("入池记账失败！");
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("所选择汇票记录已成功入池记账！");
							window.opener.location.reload();
							window.location.reload();
						}else{
							alert("入池记账失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var drfpo_no = data[0].drfpo_no._getValue();
				var url = '<emp:url action="doSureToPool.do"/>?porder_no='+encodeURI(porder_no)+'&drfpo_no='+encodeURI(drfpo_no);
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doClose() {
		window.close();
	};
	function doSureToPool_p() {
		var num = IqpDrfpoManaList._obj.getSelectedData().length;
		if (num == 0){
			alert('请先选择一条记录！');
			return;
		}
		if (num == 1) {
			for(var i=0;i<num;i++){
				var guarantyNo = IqpDrfpoManaList._obj.getSelectedData()[i].drfpo_no._getValue();
				var warrantNo = IqpDrfpoManaList._obj.getSelectedData()[i].porder_no._getValue();
				warrantNo = encodeURIComponent(warrantNo);
				var warrantType = IqpDrfpoManaList._obj.getSelectedData()[i].bill_type._getValue();
				if(warrantType=="100"){
					warrantType = "53";
				}else{
					warrantType = "33";
				}
				var warrant_state = IqpDrfpoManaList._obj.getSelectedData()[i].status._getValue();
				if(warrant_state!="04"){
					alert("只有入池记账中的数据可以做入池单打印！");
					return;
				}
			}
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("获取入池申请信息失败！");
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						var serno = jsonstr.serno;
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/yprkd2.raq&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						alert("获取入库申请信息失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("获取入池申请信息失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="printStorageMortGuarantyCertiInfo.do"/>?guarantyNo='+guarantyNo+'&warrantNo='+warrantNo+'&warrantType='+warrantType;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('不能选取多条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div class='emp_gridlayout_title'>在池票据列表&nbsp;</div>

	<emp:table icollName="IqpDrfpoManaInPoList" pageMode="false" url="">
		<emp:text id="drfpo_no" label="池编号" hidden="true"/>
		<emp:text id="porder_no" label="票据号码" />
		<emp:text id="bill_type" label="票据类型" dictname="STD_DRFT_TYPE"/>
		<emp:text id="is_ebill" label="是否电票" dictname="STD_ZX_YES_NO"/>
		<emp:text id="bill_isse_date" label="开票日期"/>
		<emp:text id="porder_end_date" label="到期日期"/>
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_DRFPO_STATUS" />
	</emp:table>
	
	<div class='emp_gridlayout_title'>入池待确认列表&nbsp;</div>

	<emp:table icollName="IqpDrfpoManaList" pageMode="false" url="" selectType="2">
		<emp:text id="drfpo_no" label="池编号" hidden="true"/>
		<emp:text id="porder_no" label="票据号码" />
		<emp:text id="bill_type" label="票据类型" dictname="STD_DRFT_TYPE"/>
		<emp:text id="is_ebill" label="是否电票" dictname="STD_ZX_YES_NO"/>
		<emp:text id="bill_isse_date" label="开票日期"/>
		<emp:text id="porder_end_date" label="到期日期"/>
		<emp:text id="drft_amt" label="票面金额" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_DRFPO_STATUS" />
	</emp:table>
	<div align="left">
		<emp:button id="getAddDpoBillDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdateDpoBillDetailPage" label="修改" op="update"/>
		<emp:button id="deleteDpoBillDetail" label="删除" op="remove"/>
		<emp:button id="viewDpoBillDetail" label="查看" op="view"/>
		<emp:button id="sureToPool" label="确认入池" op=""/>
		<emp:button id="sureToPool_p" label="打印入池单" op=""/>
		<emp:button id="close" label="关闭" op=""/>
	</div>
</body>
</html>
</emp:page>
    