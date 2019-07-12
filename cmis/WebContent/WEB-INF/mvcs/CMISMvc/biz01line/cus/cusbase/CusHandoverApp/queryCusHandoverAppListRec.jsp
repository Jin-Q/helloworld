<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusHandoverApp._toForm(form);
		CusHandoverAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusHandoverAppPage() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + "&update=Rec";
			var url = '<emp:url action="getCusHandoverAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusHandoverApp() {
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + "&update=recView";
			var url = '<emp:url action="getCusHandoverAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.CusHandoverAppGroup.reset();
	};

	function doReturnCusHandoverApp(){
		var status=CusHandoverAppList._obj.getParamValue("approve_status");
		if(!status){
			alert('请先选择一条记录！');
			return;
		}
		var handover_mode=CusHandoverAppList._obj.getParamValue("handover_mode");
		var flag=false;

		if((handover_mode=="1"&&status=="10")||((handover_mode=="2"||handover_mode=="3")&&status=="20")){
			flag=true;
		}
		if(!flag){
				alert("此状态不允许拒绝");
				return;
		}
		var paramStr = CusHandoverAppList._obj.getParamStr(['serno']);
				 if(confirm("是否确认要拒绝？")){
					var url = '<emp:url action="denyCusHandoverApp.do"/>?'+paramStr+"&approve_status=50";
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
								try {
										var jsonstr = eval("("+o.responseText+")");
									} catch(e) {
										alert("Parse jsonstr define error!"+e);
										return;
									}
									var flag = jsonstr.flag;
									if(flag=="拒绝成功"){
										alert("拒绝成功!");
											var url = '<emp:url action="queryCusHandoverAppList.do"/>&CusHandoverApp.approve_status=20';
											url = EMPTools.encodeURI(url);
											window.location = url;
								   } else {
	                                 alert("请选择一个及以上托管用户后再拒绝!");
	                               }
						}
					};
					var handleFailure = function(o){	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}; 
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				 
				 }
			
		};

		function idHandover(data){
	    	//CusHandoverApp.handover_id._setValue(data.actorno._getValue());
			CusHandoverApp.handover_id_displayname._setValue(data.actorname._getValue());
		}

	    function orgHandover(data){
	    	//CusHandoverApp.handover_br_id._setValue(data.organno._getValue());
			CusHandoverApp.handover_br_id_displayname._setValue(data.organname._getValue());
		}

		function idReceiver(data){
			var managerId= data.actorno._getValue();
			var managerIdName= data.actorname._getValue();
		    var handoverId = CusHandoverApp.handover_id_displayname._obj.element.value;
			var handoverIdName = CusHandoverApp.handover_id_displayname._obj.element.value;
			if(managerIdName==handoverIdName){
	               alert("移出人和接收人不能是同一人!");
	               return;
			}else{
				CusHandoverApp.receiver_id_displayname._setValue(managerIdName);
			}
			
		}

		function orgReceiver(data){
			var retBrId = data.organno._getValue();
	        var retBrIdName = data.organname._getValue();	
			var hanBrIdName = CusHandoverApp.handover_br_id_displayname._obj.element.value;
			if(retBrIdName==hanBrIdName){
				
					alert("不是机构内移交\n[移出机构]和[接收机构]不能 是同一个！");
					return;
				}
			    CusHandoverApp.receiver_br_id_displayname._setValue(retBrIdName);
		           
	    }
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusHandoverAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusHandoverApp.serno" label="申请流水号" colSpan="2"/>
			<emp:pop id="CusHandoverApp.handover_id_displayname" label="移出人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idHandover"  />
			<emp:pop id="CusHandoverApp.handover_br_id_displayname" label="移出机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgHandover" />
			<emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idReceiver" />
			<emp:pop id="CusHandoverApp.receiver_br_id_displayname" label="接收机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="orgReceiver"  />
			<emp:datespace id="CusHandoverApp.input_date" label="登记日期" colSpan="2"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCusHandoverApp" label="查看" op="view"/>
		<emp:button id="getUpdateCusHandoverAppPage" label="接收" op="update"/>
		<emp:button id="returnCusHandoverApp" label="拒绝"/>
	</div>

	<emp:table icollName="CusHandoverAppList" pageMode="true" url="pageCusHandoverAppQuery.do" reqParams="CusHandoverApp.approve_status=20">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="handover_id_displayname" label="移出人" />
		<emp:text id="handover_br_id_displayname" label="移出机构" />
		<emp:text id="receiver_id_displayname" label="接收人" />
		<emp:text id="receiver_br_id_displayname" label="接收机构" />
		<emp:text id="supervise_id_displayname" label="监交人" />
		<emp:text id="supervise_br_id_displayname" label="监交机构" />
		<emp:text id="approve_status" label="状态" dictname="STD_ZB_HAND_STATUS" />
		<emp:text id="handover_mode" label="移交方式" dictname="STD_ZB_HAND_TYPE" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    