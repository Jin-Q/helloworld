<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<script type="text/javascript">
	
	/*** 影像操作js总调用 ***/
	function doPubImageAction(data) {
		str = 'cus_id='+data['cus_id']+'&serno='+data['serno']
		    +'&prd_id='+data['prd_id']+'&prd_stage='+data['prd_stage']+'&image_action='+data['image_action'];
		if(data['image_action'].indexOf('Scan')!= -1){	//影像扫描
			url = '<emp:url action="delImageScanAction.do"/>?'+str;
			result = delScanResult ;
		}else if(data['image_action'].indexOf('View')!= -1){	//影像查看
			url = '<emp:url action="delImageViewAction.do"/>?'+str;
			result = delViewResult ;
		}else if(data['image_action'].indexOf('Check')!= -1){	//影像核对
			url = '<emp:url action="delImageCheckAction.do"/>?'+str;
			result = delCheckResult ;
		}else if(data['image_action'].indexOf('Print')!= -1){	//条码打印接口
			url = '<emp:url action="delImagePrintAction.do"/>?'+str;
			result = delPrintResult ;
		}
		doImageManage(url,result);
	};

	/*** 影像操作调用公共方法 ***/
	function doImageManage(url,result){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("影像处理失败：影像信息不完整或" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					var value = jsonstr.value;
					var resultUrl = jsonstr.resultUrl;
					result(flag,value,resultUrl);	//这里的result是传来的方法名
				}else{
					alert('影像处理失败！');
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
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};

	/*** 影像扫描处理终端在影像方的页面，作调用url处理 ***/
	function delScanResult(flag,value,resultUrl){
		window.open(window.encodeURI(resultUrl), 'SunIAS').focus();
	};

	/*** 影像查看处理终端在影像方的页面，作调用url处理 ***/
	function delViewResult(flag,value,resultUrl){
		window.open(window.encodeURI(resultUrl), 'SunIAS').focus();
	};

	/*** 影像核对直接发数据到影像系统，这里只返回交易结果 ***/
	function delCheckResult(flag,value,resultUrl){
		alert(value);	//这里的value值是ESB交易返回的结果，不是随便弹的
	};

	/*** 影像条码打印处理终端在影像方的页面，作调用url处理 ***/
	function delPrintResult(flag,value,resultUrl){
		window.open(window.encodeURI(resultUrl), 'SunIAS').focus();
	};

</script>
</head>
</html>