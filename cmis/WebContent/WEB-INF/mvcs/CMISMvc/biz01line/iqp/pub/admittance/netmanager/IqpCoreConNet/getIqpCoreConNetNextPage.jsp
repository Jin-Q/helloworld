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
		var appFlag = IqpCoreConNet.app_flag._getValue();
        if(appFlag == "01"){//建网
        	IqpCoreConNet.net_agr_no._obj._renderHidden(true);
        	IqpCoreConNet.net_agr_no._obj._renderRequired(false);

        	IqpCoreConNet.cus_id._obj._renderReadonly(false);

        	IqpCoreConNet.net_agr_no._setValue("");
        	IqpCoreConNet.cus_id._setValue("");
        	IqpCoreConNet.cus_id_displayname._setValue("");
        	
        }else if(appFlag == "02"){//网络变更
        	IqpCoreConNet.net_agr_no._obj._renderHidden(false);
        	IqpCoreConNet.net_agr_no._obj._renderRequired(true);

        	IqpCoreConNet.cus_id._obj._renderReadonly(true);

        	IqpCoreConNet.net_agr_no._setValue("");
        	IqpCoreConNet.cus_id._setValue("");
        	IqpCoreConNet.cus_id_displayname._setValue("");
        }
	}

	function setNetAgrNo(data){
		IqpCoreConNet.net_agr_no._setValue(data.net_agr_no._getValue());
		IqpCoreConNet.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		IqpCoreConNet.cus_id._setValue(data.cus_id._getValue());
	}
	
    //下一步判断是建网还是变更
    function doNext(){
    	var appFlag = IqpCoreConNet.app_flag._getValue();
        if(appFlag == "01"){//建网
        	doNextCreNet();
        }else if(appFlag == "02"){//网络变更
        	doNextInNet();
        }else {
            alert("请录入完整信息!");
        }
    };
  //建网下一步    
    function doNextCreNet(){
        var cus_id = IqpCoreConNet.cus_id._getValue();
        var flow_type = IqpCoreConNet.flow_type._getValue();
        if(cus_id != null && cus_id != "" && flow_type != null && flow_type != ""){
        	var url = '<emp:url action="getIqpCoreConNetAddPage.do"/>?cus_id='+cus_id+'&flow_type='+flow_type;
    		url = EMPTools.encodeURI(url);
            window.location = url;
        }else{
            alert("请输入完整信息!");
        }
    };
    
    //网络变更下一步    
    function doNextInNet(){
    	var paramStr = 'net_agr_no=' + IqpCoreConNet.net_agr_no._getValue() + '&flow_type='+ IqpCoreConNet.flow_type._getValue()
		if(IqpCoreConNet._checkAll()){
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
					if(flag == "Have"){
                       alert("存在在途的变更申请!");
				    }else if(flag == "notHave"){
				    	var url = '<emp:url action="getIqpCoreConNetViewPage4Change.do"/>?serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}
			};
		    var handleFailure = function(o){};
		    var callback = {
		    	success:handleSuccess,
		    	failure:handleFailure
		    };
			var url = '<emp:url action="getIqpCoreConNet4AddInNet.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
		}else{
			return false;
		}
};

	function getCusInfo(data){
		IqpCoreConNet.cus_id._setValue(data.cus_id._getValue());
		IqpCoreConNet.cus_id_displayname._setValue(data.cus_name._getValue());
		var cus_id = data.cus_id._getValue();
		checkCusExistNet(cus_id);//判断客户是否已经在本行有网络
	};
    
	//判断客户是否已经在本行有网络
	function checkCusExistNet(cus_id){
		var handleSuccess = function(o){ 		
		var jsonstr = eval("(" + o.responseText + ")");
		var flag = jsonstr.flag;
			if(flag == "success" ){
				alert("该核心企业已有一个网络！");
				IqpCoreConNet.cus_id._setValue("");
				IqpCoreConNet.cus_id_displayname._setValue("");
				/**modified by lisj 2015-2-3 修复页面报错BUG(页面不存在cdt_lvl字段) begin**/
				//IqpCoreConNet.cdt_lvl._setValue("");
				/**modified by lisj 2015-2-3 修复页面报错BUG(页面不存在cdt_lvl字段) end**/
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
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" >
   <emp:form id="submitForm" action="" method="POST">
		<emp:gridLayout id="IqpCoreConNetGroup" title="网络变更申请表" maxColumn="2">
		    <emp:select id="IqpCoreConNet.app_flag" label="申请类型" onchange="appType()" dictname="STD_ZB_NET_APP_TYPE" required="true" />
			<emp:pop id="IqpCoreConNet.net_agr_no" label="网络编号" required="true" url="queryIqpNetAgrNo.do?returnMethod=setNetAgrNo"/>
			<emp:pop id="IqpCoreConNet.cus_id" label="核心企业客户码" url="queryAllCusPop.do?returnMethod=getCusInfo&cusTypCondition=BELG_LINE IN('BL100','BL200') and cus_status='20'" required="true" />
			<emp:text id="IqpCoreConNet.cus_id_displayname" label="核心企业客户名称" required="true" readonly="true"/>
		    <emp:select id="IqpCoreConNet.flow_type" label="流程类型" hidden="true" defvalue="01" dictname="STD_ZB_FLOW_TYPE"/>
		</emp:gridLayout>
		 
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>