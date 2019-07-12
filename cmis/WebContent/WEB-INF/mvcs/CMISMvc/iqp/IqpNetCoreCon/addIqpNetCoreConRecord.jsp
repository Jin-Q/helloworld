<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function appType(){
		var appFlag = IqpNetCoreCon.app_flag._getValue();
        if(appFlag == "01"){//建网
        	IqpNetCoreCon.net_agr_no._obj._renderHidden(true);
        	IqpNetCoreCon.net_agr_no._obj._renderRequired(false);

        	IqpNetCoreCon.cus_id._obj._renderReadonly(false);
        }else if(appFlag == "02"){//入网/退网
        	IqpNetCoreCon.net_agr_no._obj._renderHidden(false);
        	IqpNetCoreCon.net_agr_no._obj._renderRequired(true);

        	IqpNetCoreCon.cus_id_displayname._obj._renderReadonly(true);
        	IqpNetCoreCon.cus_id._obj._renderReadonly(true);
        	IqpNetCoreCon.cdt_lvl._obj._renderHidden(true);
        	IqpNetCoreCon.cdt_lvl._obj._renderRequired(false);
        }
	}

	function setNetAgrNo(data){
		IqpNetCoreCon.net_agr_no._setValue(data.net_agr_no._getValue());
		IqpNetCoreCon.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		IqpNetCoreCon.cus_id._setValue(data.cus_id._getValue());
	}

	function doNext(){
	    var paramStr = 'net_agr_no=' + IqpNetCoreCon.net_agr_no._getValue() + '&app_flag='+ IqpNetCoreCon.app_flag._getValue()
		var url = '<emp:url action="IqpNetCoreCon4nextRecord.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		if(IqpNetCoreCon._checkAll()){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("数据库操作失败!");
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					var app_flag = IqpNetCoreCon.app_flag._getValue();//申请类型
					if(flag == 'suc'){
						var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if( flag == 'existTaskUpd' ){//如果已存在待发起任务则跳转修改
						if(confirm("已存在待发起的入网申请,确认跳转修改?")){
							var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?'+paramStr;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else if(flag == 'existTaskView'){//如果已经存在提交任务则跳转查看
						var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == 'existTask'){//存在在途退网申请不能发起
						if(app_flag == '03'){
							alert('存在在途入网申请，不能发起新的退网申请！');
						}else if(app_flag =="02"){
							alert('存在在途退网申请，不能发起新的入网申请！');
						}
					}else{
						alert("网内无有效成员!");
				 	}
				}
			};
		    var handleFailure = function(o){};
		    var callback = {
		    	success:handleSuccess,
		    	failure:handleFailure
		    };
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}else{
			return false;
		}
	};


	function getCusInfo(data){
		IqpNetCoreCon.cus_id._setValue(data.cus_id._getValue());
		IqpNetCoreCon.cus_id_displayname._setValue(data.cus_name._getValue());
		IqpNetCoreCon.cdt_lvl._setValue(data.cus_crd_grade._getValue());
		var cus_id = data.cus_id._getValue();
		checkCusExistNet(cus_id);//判断客户是否已经在本行有网络
	}
    
	//判断客户是否已经在本行有网络
	function checkCusExistNet(cus_id){
		var handleSuccess = function(o){ 		
		var jsonstr = eval("(" + o.responseText + ")");
		var flag = jsonstr.flag;
			if(flag == "success" ){
				alert("该核心企业已有一个网络！");
				IqpNetCoreCon.cus_id._setValue("");
				IqpNetCoreCon.cus_id_displayname._setValue("");
				IqpNetCoreCon.cdt_lvl._setValue("");
			}else{
				getRelatevalue(cus_id);
		  	}
		}
		var handleFailure = function(o){
		        alert("异步回调失败！");	
		};
		var url = '<emp:url action="checkCusExistNetRecord.do"/>?cus_id='+cus_id;
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" >
	<div>
		<br></br>
	</div>
		<emp:gridLayout id="IqpNetCoreConGroup" title="网络退/入网申请向导" maxColumn="2">
		    <emp:select id="IqpNetCoreCon.app_flag" label="申请类型" onchange="appType()" dictname="STD_ZB_NET_APP_TYPE" required="true" />
			<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="IqpNetCoreCon.net_agr_no" label="网络编号" required="true" url="queryIqpNetAgrNo.do?returnMethod=setNetAgrNo"/>
			<!-- modified by yangzy 2015/04/27 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:pop id="IqpNetCoreCon.cus_id" label="核心企业客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE IN('BL100','BL200') and cus_status='20'&returnMethod=getCusInfo" required="true" />
			<emp:text id="IqpNetCoreCon.cus_id_displayname" label="核心企业客户名称" required="true" readonly="true"/>
			<emp:select id="IqpNetCoreCon.cdt_lvl" label="信用等级" dictname="STD_ZB_CREDIT_GRADE" required="true" readonly="true"/>
		</emp:gridLayout>
		 
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
</body>
</html>
</emp:page>