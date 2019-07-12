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
		var result = LmtIndus._checkAll();
	    if(result){
	    	var change_list_flag = LmtIndus.change_list_flag._getValue();
	    	var agr_no = LmtIndus.agr_no._getValue();
	    	var paramStr = "agr_no="+agr_no+"&change_list_flag="+change_list_flag+"&action=add";
	    	var url = '<emp:url action="getLmtIndusApplyAddPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location=url;
		}
	}

	function returnArg(data){
		LmtIndus.agr_no._setValue(data.agr_no._getValue());
		is_list_mana = data.is_list_mana._getValue();
		LmtIndus.is_list_mana._setValue(is_list_mana);
		if(is_list_mana == '2'){
			LmtIndus.change_list_flag._setValue('2');
			LmtIndus.change_list_flag._obj._renderReadonly(true);
		}else{
			LmtIndus.change_list_flag._setValue('');
			LmtIndus.change_list_flag._obj._renderReadonly(false);
		}
		checkIndusType(data.agr_no._getValue());
	};
	function checkIndusType(agr_no){
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

				}else {
					alert("此协议已存在行业授信申请!");
					LmtIndus.agr_no._setValue("");
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
		var url="<emp:url action='checkUniqueType.do'/>&type=indusAgr&value="+agr_no;
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};

    function doReturn() {
		var url = '<emp:url action="queryLmtIndusApplyList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
/*--user code end--*/
</script>
</head>
<body class="page_content" style="width:200px">
	<emp:form id="submitForm" action="getLmtApplyAddPage.do" method="POST">
		<emp:gridLayout id="LmtIndusGroup" title="授信申请引导页" maxColumn="2">
			<emp:pop id="LmtIndus.agr_no" label="行业授信协议编号" 
			url="queryLmtIndusPop.do" returnMethod="returnArg" required="true" />
			<emp:select id="LmtIndus.is_list_mana" label="是否名单制管理" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="LmtIndus.change_list_flag" label="是否仅变更名单" required="true" dictname="STD_ZX_YES_NO" />
		</emp:gridLayout> 
		<div align="center">
			<br>
			<emp:button id="Next" label="下一步" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>