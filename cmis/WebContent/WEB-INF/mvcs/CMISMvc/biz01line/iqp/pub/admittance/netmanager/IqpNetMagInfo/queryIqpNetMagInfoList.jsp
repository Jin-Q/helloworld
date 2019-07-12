<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpNetMagInfo._toForm(form);
		IqpNetMagInfoList._obj.ajaxQuery(null,form);
	};

	//入网申请
	function doGetAddIqpNetMagInfoPage(){
		var paramStr = IqpNetMagInfoList._obj.getParamStr(['net_agr_no','status']);
		if (paramStr != null){
			var status = IqpNetMagInfoList._obj.getSelectedData()[0].status._getValue();
			if( status == "2" )  {//无效标志
				alert("无效网络不能申请！");
				return ;
			}
			if(!confirm("是否确认要发起入网申请？")){
				return;
			}
			paramStr += '&app_flag=0';
			var url = '<emp:url action="checkNetExist.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){		
	            var jsonstr = eval("(" + o.responseText + ")");
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
			      if(flag == "suc" ){//如果不存在入网申请,进入新增成员入网页面
						var url = '<emp:url action="getIqpNetMagInfoAddPage.do"/>?'+paramStr+"&op=update&menuId=queryIqpNetApp&serno="+serno;//app_type 申请类型('01'建网申请,'02'入网申请,'03'退网申请)
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag=='exist'){//如果存在入网申请,直接进入成员入网页面
						var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?'+paramStr+"&op=update&menuId=queryIqpNetApp&serno="+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == 'existTaskView'){//如果已经存在提交任务则跳转查看
						var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?'+paramStr+"&op=view&menuId=queryIqpNetApp&serno="+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else if(flag == 'existTask'){//存在在途退圈申请不能发起
							alert('存在在途退网申请，不能发起新的入网申请！');
					}
				}
				var callback ={
					success:handleSuccess,
					failure:null
				};
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	//退网申请
	function doGetUpdateIqpNetMagInfoPage() {
		var paramStr = IqpNetMagInfoList._obj.getParamStr(['net_agr_no','status']);
		if (paramStr != null) {
			var status = IqpNetMagInfoList._obj.getSelectedData()[0].status._getValue();
			if( status == "2" )  {//无效标志
				alert("无效网络不能申请！");
				return ;
			}
			if(!confirm("是否确认要发起退网申请？")){
				return;
			}
			paramStr += '&app_flag=03';
			var url = '<emp:url action="checkNetExist.do"/>?'+paramStr ;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){ 		
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			var serno = jsonstr.serno;
				if(flag == "suc" ){//如果不存在退网申请,进入新增退网申请页面
					var url = '<emp:url action="getIqpNetMagInfoAddPage.do"/>?'+paramStr+"&op=update&menuId=queryIqpNetApp&serno="+serno;//app_type 申请类型('01'建网申请,'02'入网申请,'03'退网申请)
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag=='exist'){//如果存在退网申请,直接进入成员退网页面
					var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?'+paramStr+"&op=update&menuId=queryIqpNetApp&serno="+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag == 'existTask'){//存在在途退圈申请不能发起
					alert('存在在途入网申请，不能发起新的退网申请！');
				}else if(flag == 'existTaskView'){//如果已经存在提交任务则跳转查看
					var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?'+paramStr+"&op=view&menuId=queryIqpNetApp&serno="+serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert("网内无有效成员！");
				}
			}
				var callback ={
					success:handleSuccess,
					failure:null
				};
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewIqpNetMagInfo(){
		var paramStr = IqpNetMagInfoList._obj.getParamStr(['net_agr_no']);
		if(paramStr != null){
			var url = '<emp:url action="getIqpNetMagInfoViewPage.do"/>?'+paramStr+"&op=view&type=netMag";
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert('请先选择一条记录！');
		}
	};
    
	function doReset(){
		page.dataGroups.IqpNetMagInfoGroup.reset();
	};
	/*--user code begin--*/
	function getCusInfo(data){
		IqpNetMagInfo.cus_id._setValue(data.cus_id._getValue());
		IqpNetMagInfo.cus_id_displayname._setValue(data.cus_name._getValue());
	};	

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpNetMagInfoGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpNetMagInfo.net_agr_no" label="网络编号"/>
		<emp:pop id="IqpNetMagInfo.cus_id_displayname" label="核心企业客户名称" url="queryAllCusPop.do?cusTypCondition=BELG_LINE IN('BL100','BL200') and cus_status='20'&returnMethod=getCusInfo" />		
		<emp:select id="IqpNetMagInfo.status" label="网络状态" dictname="STD_ZB_STATUS"/>
		<emp:text id="IqpNetMagInfo.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpNetMagInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpNetMagInfoList" pageMode="true" url="pageIqpNetMagInfoQuery.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="net_agr_no" label="网络编号" />
		<emp:text id="cus_id" label="核心企业客户码" />
		<emp:text id="cus_id_displayname" label="核心企业客户名称" />
		<emp:text id="coop_term_type" label="合作期限类型"  dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="coop_term" label="合作期限" />
		<emp:text id="respond_mode" label="承担责任方式" dictname="STD_ZB_RESPOND_MODE" />
		<emp:text id="net_build_date" label="网络建立日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="status" label="网络状态" dictname="STD_ZB_MEM_STATUS" />
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		
		<emp:select id="flow_type" label="流程类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    