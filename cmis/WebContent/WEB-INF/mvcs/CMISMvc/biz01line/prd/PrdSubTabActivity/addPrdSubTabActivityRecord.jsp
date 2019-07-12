<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_input{
	width: 600px;
	height: 50px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>
<script type="text/javascript">
function checkNum(){
	var num = PrdSubTabActivity.num._getValue();
    if(num.search("^[0-9]*$")!=0){
        alert("请输入数字!");
        PrdSubTabActivity.num._setValue("");
   }
};

function doSub(){
	if(!PrdSubTabActivity._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	PrdSubTabActivity._toForm(form);
	//var serno = IqpBksyndic._getValue();
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var pkid = jsonstr.pkid;
			if(flag == "success" & pkid != null){
				var url = '<emp:url action="getPrdSubTabActivityUpdatePage.do"/>?pkid='+pkid; 
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else {
				alert("保存失败！");
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

	form.action = "<emp:url action='addPrdSubTabActivityRecord.do'/>"
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
}; 



	/*--user code begin--*/

	function doOnLoad(){
		PrdSubTabActivity.mainid._obj.addOneButton("mainid","选择",getMainId);
		PrdSubTabActivity.subid._obj.addOneButton("subid","选择",getSubId);
	}	
	
	function getMainId(){
		var url = '<emp:url action="queryTabResourceList.do"/>?returnMethod=returnMainMsg';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	function getSubId(){
		var url = '<emp:url action="queryTabResourceList.do"/>?returnMethod=returnSubMsg';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	
	function returnMainMsg(data){
		PrdSubTabActivity.mainid._setValue(data.resourceid._getValue());
	}
	function returnSubMsg(data){
		PrdSubTabActivity.subid._setValue(data.resourceid._getValue());
		var subId = PrdSubTabActivity.subid._getValue();
		PrdSubTabActivity.subnum._setValue(subId+"_div");
	}	

	function setSubTerm(){
		var subId = PrdSubTabActivity.subid._getValue();
		PrdSubTabActivity.subnum._setValue(subId);
	}

	function checkModel(){
		PrdSubTabActivity.mainmodel._obj._renderRequired(false);
		var subterm = PrdSubTabActivity.subterm._getValue();
		var arr = new Array();		
		arr = subterm.split(";");
		for(var i=0;i<arr.length;i++){
			if(arr[i].indexOf("{")<0){
				PrdSubTabActivity.mainmodel._obj._renderRequired(true);
			}			
		}  
    }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	
	<emp:form id="submitForm" action="addPrdSubTabActivityRecord.do" method="POST">
		
		<emp:gridLayout id="PrdSubTabActivityGroup" title="标签页动态挂接" maxColumn="2">
			<emp:text id="PrdSubTabActivity.mainid" label="主资源ID" required="true" />
			<emp:text id="PrdSubTabActivity.mainmodel" label="主资源表模型" maxlength="80" required="false" />			
			<emp:textarea id="PrdSubTabActivity.mainterm" label="主资源过滤条件" maxlength="500" required="false" colSpan="2" cssElementClass="emp_input"/>	
			<emp:text id="PrdSubTabActivity.subid" label="从资源ID" maxlength="80" required="true" onchange="setSubTerm();" />
			<emp:text id="PrdSubTabActivity.submodel" label="从资源表模型" maxlength="80" required="false" />				
			<emp:text id="PrdSubTabActivity.subnum" label="从资源标识" maxlength="50" readonly="true" required="false" hidden="false" colSpan="2"/>	
			<emp:textarea id="PrdSubTabActivity.subterm" label="从资源过滤条件" maxlength="300" required="false" colSpan="2" cssElementClass="emp_input" onblur="checkModel()"/>
			<emp:text id="PrdSubTabActivity.num" label="序号" maxlength="10" required="false" onblur="checkNum()" />
			<emp:text id="PrdSubTabActivity.subname" label="从资源名称" maxlength="80" readonly="false" required="false" />	
			<emp:date id="PrdSubTabActivity.inputdate" label="登记日期" dataType="true" required="false" readonly="true" defvalue="$OPENDAY" hidden="true"/>
			<emp:text id="PrdSubTabActivity.inputid" label="登记人员" maxlength="20" required="false" defvalue="$currentUserId" hidden="true"/>		
			<emp:text id="PrdSubTabActivity.inputorg" label="登记机构" maxlength="10" required="false" defvalue="$organNo" hidden="true"/> 
		</emp:gridLayout>     
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

