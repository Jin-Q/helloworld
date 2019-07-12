<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<script type="text/javascript">
	/*** 资产保全模块，页面上通用的公共操作。by GC 20131126***/
	
	/*** 公共异步删除操作 ***/
	function doPubDelete(url) {
		if(confirm("是否确认要删除？")){
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("删除失败!");
						return;
					}
					var flag=jsonstr.flag;	
					var flagInfo=jsonstr.flagInfo;						
					if(flag=="success"){
						alert('删除成功！');
						window.location.reload();
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
	};

	/*** 公共异步新增保存操作 ***/
	function doPubAdd(url,obj) {	//传入的url是新增完成后的op操作，一般传update页面或doReturn
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
				if(flag=='success'){
		            alert('新增成功!');
		            if(url == 'doReturn'){
		            	doReturn();
		            }else{
		            	var serno = jsonstr.serno;
		            	url = url+serno;
			    		url = EMPTools.encodeURI(url);
			    		window.location=url;
		            }		            
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = obj._checkAll();
		if(result){
			obj._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};

	/*** 公共异步修改保存操作 ***/
	function doPubUpdate(obj) {
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
				if(flag=='success'){
		            alert('保存成功!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = obj._checkAll();
		if(result){
			obj._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};

	/*** 公共校验方法 ***/
	function doPubCheck(url,result){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				result(flag);
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};

	/*** 客户码后查看按钮 ***/
	function addCusForm(obj){
		pubAction_cus_id = obj.cus_id._getValue();
		obj.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
	};
	function getCusForm(){
		if(pubAction_cus_id==null||pubAction_cus_id==''){
			alert('客户码为空，无法查看！');
		}else{
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+pubAction_cus_id;
			url=EMPTools.encodeURI(url);  
		    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};

	/***合同编号后查看按钮 ***/
	function addContForm(obj){
		pubAction_cont_no = obj.cont_no._getValue();
		obj.cont_no._obj.addOneButton("cont_no","查看",getContForm);
	};
	function getContForm(){ //目前这个合同pop有问题，业务模块不提供只能自己写，但都有问题。只能等以后业务模块提供能用的再替换。
		if(pubAction_cont_no==null||pubAction_cont_no==''){
			alert('合同编号为空，无法查看！');
		}else{
			url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&cont_no='+pubAction_cont_no+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
			url=EMPTools.encodeURI(url);  
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}
	};

	/***借据编号后查看按钮 ***/
	function addBillForm(obj){
		pubAction_bill_no = obj.bill_no._getValue();
		obj.bill_no._obj.addOneButton("bill_no","查看",getBillForm);
	};
	function getBillForm(){
		if(pubAction_bill_no==null||pubAction_bill_no==''){
			alert('借据编号为空，无法查看！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+pubAction_bill_no;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};
	/*--user code end--*/
</script>
</head>
</html>
</emp:page>