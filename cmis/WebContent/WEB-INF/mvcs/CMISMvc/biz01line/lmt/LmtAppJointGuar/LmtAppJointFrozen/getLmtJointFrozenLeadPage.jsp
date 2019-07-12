<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doOnload(){
		//移除其他申请类型
		var options = LmtAppJointCoop.app_type._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
	   	    if(options[i].value=='01'||options[i].value=='02'||options[i].value=='05'||options[i].value=='06'){
	  			options.remove(i);
	   	    }
	    }
	}
	
	//保存
	function doSaveFrozen(){
		if(!LmtAppJointCoop._checkAll()){
			return ;
		}
		var agr_no = LmtAppJointCoop.agr_no._getValue();
		var app_type = LmtAppJointCoop.app_type._getValue();
		var url = '<emp:url action="freezeOrUnfreezeJointCoop.do"/>&agr_no='+agr_no+'&app_type='+app_type;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("联保协议解冻失败!");
					return;
				}
				var flag=jsonstr.flag;
				var serno=jsonstr.serno;
				if(flag=="success"){
					var url = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>&serno='+serno+'&menuId=unit_team_crd_apply';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag=="exists"){
					alert("该联保协议存在在途的冻结申请，不能重复发起！");
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("联保协议冻结失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	function returnAgrNo(data){//联保协议编号只是一个辅助字段，真正存到数据库里的数据是授信额度编号
		LmtAppJointCoop.agr_no._setValue(data.agr_no._getValue());
		var agr_status = data.agr_status._getValue();
		if(agr_status=='002'){
			LmtAppJointCoop.app_type._setValue('03');
		}else{
			LmtAppJointCoop.app_type._setValue('04');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form id="submitForm" action="addLmtAppJointCoop_jointRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppJointCoopGroup" title="联保小组信息" maxColumn="2">
			<emp:pop id="LmtAppJointCoop.agr_no" label="联保协议编号" url="queryLmtAgrJointPop.do?returnMethod=returnAgrNo&flag=2" required="false" buttonLabel="选择"/>
			<emp:select id="LmtAppJointCoop.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="saveFrozen" label="保存"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

