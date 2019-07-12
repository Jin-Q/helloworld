<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doClose() {
		window.close();
	};
	function doCollPoolEnd(){
		var data = IqpDrfpoManaList._obj.getSelectedData();
		var porder_no = new Array();
		for(var i=0;i<data.length;i++){
			porder_no.push(data[i].porder_no._getValue());
		}
		if (data.length != 0) {
			if(confirm("是否确认托收完成？")){
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
							alert("所选择汇票记录已托收完成！");
							window.location.reload();
						}else{
							alert("托收完成！");
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
				var url = '<emp:url action="doCollPoolEnd.do"/>?porder_no='+encodeURI(porder_no)+'&drfpo_no='+encodeURI(drfpo_no);
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doSureCollPool(){
		var data = IqpDrfpoManaInPoList._obj.getSelectedData();
		var porder_no = new Array();
		for(var i=0;i<data.length;i++){
			porder_no.push(data[i].porder_no._getValue());
			var status = data[i].status._getValue();
			if(status=="05"){
				alert("已托收记账，不能重复发起托收！");
				return;
			}
		}
		if (data.length != 0) {
			if(confirm("是否确认托收？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("托收失败！");
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("所选择汇票记录已成功托收记账！");
							window.location.reload();
						/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 start */
						}else if(flag == "error"){
							alert("[在池票据总金额+保证金金额]不能覆盖池担保占用敞口，不能托收！");
							window.location.reload();
						/* added by yangzy 2014/12/11 应收账款类出池校验，在池是否覆盖敞口 end */
						}else{
							alert("托收失败！");
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
				var url = '<emp:url action="doSureCollPool.do"/>?porder_no='+porder_no+'&drfpo_no='+drfpo_no;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doSureCollPool_p() {
		var num = IqpDrfpoManaInPoList._obj.getSelectedData().length;
		if (num == 0){
			alert('请先选择一条记录！');
			return;
		}
		if (num == 1) {
			for(var i=0;i<num;i++){
				var guarantyNo = IqpDrfpoManaInPoList._obj.getSelectedData()[i].drfpo_no._getValue();
				var warrantNo = IqpDrfpoManaInPoList._obj.getSelectedData()[i].porder_no._getValue();
				warrantNo = encodeURIComponent(warrantNo);
				var warrantType = IqpDrfpoManaInPoList._obj.getSelectedData()[i].bill_type._getValue();
				if(warrantType=="100"){
					warrantType = "53";
				}else{
					warrantType = "33";
				}
				var warrant_state = IqpDrfpoManaInPoList._obj.getSelectedData()[i].status._getValue();
				if(warrant_state!="05"){
					alert("只有托收记账中的数据可以做出池单打印！");
					return;
				}
			}
			var handleSuccess = function(o) {
				EMPTools.unmask();
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("获取托收申请信息失败！");
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						var serno = jsonstr.serno;
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/ypckd2.raq&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						alert("获取托收申请信息失败！");
					}
				}
			};
			var handleFailure = function(o) {
				alert("获取托收申请信息失败！");
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="printStorageMortGuarantyCertiInfo.do"/>?flag=out&guarantyNo='+guarantyNo+'&warrantNo='+warrantNo+'&warrantType='+warrantType;
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
	<div class='emp_gridlayout_title'>已托收票据列表&nbsp;</div>

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
		<emp:button id="collPoolEnd" label="托收完成" op=""/>
	</div>
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
	
	<div align="left">
		<emp:button id="close" label="关闭" op=""/>
		<emp:button id="sureCollPool" label="确认托收" op=""/>
		<emp:button id="sureCollPool_p" label="托收单打印" op=""/>
	</div>
</body>
</html>
</emp:page>
    