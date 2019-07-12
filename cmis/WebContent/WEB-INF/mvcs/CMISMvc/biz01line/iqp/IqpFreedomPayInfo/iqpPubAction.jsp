<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<script type="text/javascript">
	/*** 资产保全模块，页面上通用的公共操作。by GC 20131126***/
	
	/*** 公共异步删除操作 ***/
	function doPubDelete(url) {
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
					}else{
						alert(flag);
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

	/*--user code end--*/
</script>
</head>
</html>
</emp:page>