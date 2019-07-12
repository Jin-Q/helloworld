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
		IqpOverseeAgr._toForm(form);
		IqpOverseeAgrList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpOverseeAgrPage() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
		var url = '<emp:url action="getIqpOverseeAgrUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpOverseeAgr() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpOverseeAgrViewPage.do"/>?'+paramStr+"&flag=havaButton";
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpOverseeAgrPage() {
		var url = '<emp:url action="getIqpOverseeAgrAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpOverseeAgr() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var status = IqpOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(status != "2"){
				 alert("只有状态为【未生效】的监管协议才可以进行删除！");
               return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("删除成功！");
						window.location.reload();
					}else{
					}
				}
				var handleFailure = function(o){
				alert("异步回调失败！");	
				};
				var url = '<emp:url action="deleteIqpOverseeAgrRecord.do"/>?'+paramStr;
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	      }
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpOverseeAgrGroup.reset();
	};
	
	/*--user code begin--*/
	function doCfirm(){
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) {
			var status = IqpOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(status != "2"){
               alert("只有状态为【未生效】的监管协议才可以进行确认！");
               return;
			}
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("确认成功！");
						window.location.reload();
					}else{
					}
				}
				var handleFailure = function(o){
				alert("异步回调失败！");	
				};
				var url = '<emp:url action="sub2UpdateStatus.do"/>?'+paramStr+'&oprate=cfirm';
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
    //解除监管
	function doRec() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) { 
			var status = IqpOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(status != "1"){ 
				alert("只有状态为【生效】的监管协议才可以进行解除监管！");
				 return;
			}
			if(confirm("是否确认要解除监管？")){
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
							alert("解除监管成功!");
							window.location.reload();
						}else {
							alert("发生异常!"); 
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
				var url = '<emp:url action="sub2UpdateStatus.do"/>?'+paramStr+'&oprate=rec';
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}     
		} else {
			alert('请先选择一条记录！');
		}
	};

	 //解除监管确认
	function doCfirmrec() {
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) { 
			var status = IqpOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(status != "3"){ 
				alert("只有状态为【解除监管】的监管协议才可以进行解除监管确认！");
				 return;
			}
			if(confirm("是否确认要解除监管确认？")){
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
							alert("解除监管成功!");
							window.location.reload();
						}else {
							alert("发生异常!"); 
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
				var url = '<emp:url action="sub2UpdateStatus.do"/>?'+paramStr+'&oprate=cfirmrec';
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function returnCus(data){
		IqpOverseeAgr.mortgagor_id._setValue(data.cus_id._getValue());
		   IqpOverseeAgr.mortgagor_id_displayname._setValue(data.cus_name._getValue());
	};
	
	function getConId(data){
		IqpOverseeAgr.oversee_con_id._setValue(data.oversee_org_id._getValue());
		IqpOverseeAgr.oversee_con_id_displayname._setValue(data.oversee_org_id_displayname._getValue());
	};

	function doPrint(){
		var paramStr = IqpOverseeAgrList._obj.getParamStr(['oversee_agr_no']);
		if (paramStr != null) { 
			var status = IqpOverseeAgrList._obj.getSelectedData()[0].status._getValue();
			if(status != "0"){ 
				alert("只有状态为'失效'的监管协议才可以进行【打印】操作！");
			}else{
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=iqp/iqpOverseeAgr.raq&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpOverseeAgrGroup" title="输入查询条件" maxColumn="2">			
	    <emp:text id="IqpOverseeAgr.oversee_agr_no" label="监管协议号"/>
		<emp:pop id="IqpOverseeAgr.mortgagor_id_displayname" label="客户名称" url="queryAllCusPop.do?returnMethod=returnCus"/>
		<emp:pop id="IqpOverseeAgr.oversee_con_id_displayname" label="监管企业名称" url="IqpOverseeOrg4PopList.do?restrictUsed=false&returnMethod=getConId"/>
		<emp:select id="IqpOverseeAgr.status" label="协议状态" dictname="STD_ZB_OVERAGR_STATUS" />
		<emp:text id="IqpOverseeAgr.mortgagor_id" label="客户码" hidden="true"/>
		<emp:text id="IqpOverseeAgr.oversee_con_id" label="监管企业编号" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpOverseeAgrPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpOverseeAgrPage" label="修改" op="update"/>
		<emp:button id="deleteIqpOverseeAgr" label="删除" op="remove"/>
		<emp:button id="viewIqpOverseeAgr" label="查看" op="view"/>
		<emp:button id="cfirm" label="确认" op="cfirm"/>
		<emp:button id="rec" label="解除监管" op="rec"/>
		<emp:button id="cfirmrec" label="解除监管确认" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80" op="cfirmrec"/>
		<emp:button id="print" label="打印" op="print"/>
	</div>

	<emp:table icollName="IqpOverseeAgrList" pageMode="true" url="pageIqpOverseeAgrMainQuery.do">
		<emp:text id="oversee_agr_no" label="监管协议号" />
		<emp:text id="mortgagor_id" label="客户码" />
		<emp:text id="mortgagor_id_displayname" label="客户名称" />
		<emp:text id="oversee_con_id" label="监管企业编号" />
		<emp:text id="oversee_con_id_displayname" label="监管企业名称" />
		<emp:text id="status" label="协议状态" dictname="STD_ZB_OVERAGR_STATUS" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    