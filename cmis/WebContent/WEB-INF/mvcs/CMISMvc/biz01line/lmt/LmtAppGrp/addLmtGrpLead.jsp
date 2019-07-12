<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
	.emp_field_text_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 240px;
	};
</style>
<script type="text/javascript">

/*--user code begin--*/
	//下一步方法
	function doNext(){
		var form = document.getElementById('submitForm');
		var result = LmtApply._checkAll();
	    if(result){
	    	LmtApply._toForm(form);
	    	form.submit();
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	}

	//选择客户POP框返回方法
	function returnCus(data){
		LmtApply.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
		LmtApply.main_br_id._setValue(data.main_br_id._getValue());
	}
	function returnCrpNo(data){
		LmtAppGrp.grp_no._setValue(data.grp_no._getValue());
		LmtAppGrp.grp_no_displayname._setValue(data.grp_name._getValue());
		LmtAppGrp.main_br_id._setValue(data.manager_br_id._getValue());
		LmtAppGrp.manager_id._setValue(data.manager_id._getValue());
		LmtAppGrp.manager_br_id._setValue(data.manager_br_id._getValue());
		//LmtApplyGrp.cus_id._setValue(data.parent_cus_id._getValue()); 
		//LmtApplyGrp.cus_name._setValue(data.parent_cus_name._getValue());
		//doGetCusType(); //获取客户类型
	}

	//异步调用生成合并明细
    function doExecuteAjax(){
   	 	var handleSuccess = function(o) {
  			if (o.responseText !== undefined) {
  				try {
  					var fistTest = eval("(" + o.responseText + ")");
  				} catch (e) {
  					alert("异步执行不成功：" + e.message);
  					return;
  				}
  				var result = fistTest.result;   
  				if(result != ""){
  	  				alert(result);
				}else{
					LmtAppGrp._toForm(form);
			    	form.submit();
				}
  			}
   		};
  		var handleFailure = function(o) {
  			alert("与服务器交互失败，请联系管理员！");
  		};
  		var callback = {
  			success :handleSuccess,
  			failure :handleFailure
  		};
  		var form = document.getElementById('submitForm');
		var result = LmtAppGrp._checkAll();
	    if(result){
	    	var url = '<emp:url action="searchLmtGrpInfo.do"/>?grp_no='+LmtAppGrp.grp_no._getValue();
	  		url = EMPTools.encodeURI(url);
	  		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	    }else {
		    alert("请检查各标签页面中的必填信息是否遗漏！");
		}
	}
    function doReturn() {
		var url = '<emp:url action="queryLmtGrpApplyList.do"/>?type=app';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
/*--user code end--*/
</script>
</head>
<body class="page_content" style="width:200px">
	<emp:form id="submitForm" action="getLmtGrpApplyAddPage.do" method="POST">
		<emp:gridLayout id="LmtApplyGroup" title="集团授信申请向导" maxColumn="1">
			<emp:pop id="LmtAppGrp.grp_no" label="集团编号" url="queryCusGrpInfoPopList.do?returnMethod=returnCrpNo" required="true" />
			<emp:text id="LmtAppGrp.grp_no_displayname" label="集团名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" /> 
			<emp:text id="LmtAppGrp.main_br_id" label="管理机构" maxlength="60" required="true" readonly="true" hidden="true"/>
			<emp:text id="LmtAppGrp.manager_id" label="责任人" maxlength="60" required="true" readonly="true" hidden="true"/>
			<emp:text id="LmtAppGrp.manager_br_id" label="责任机构" maxlength="60" required="true" readonly="true" hidden="true"/>
		</emp:gridLayout> 
		<div align="center">
			<br>
			<emp:button id="executeAjax" label="下一步" op="add" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

