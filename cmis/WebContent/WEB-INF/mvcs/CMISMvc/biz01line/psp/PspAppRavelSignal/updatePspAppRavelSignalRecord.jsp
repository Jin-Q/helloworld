<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	//返回主管客户经理	
	function setconId(data){
		PspAppRavelSignal.manager_id._setValue(data.actorno._getValue());
		PspAppRavelSignal.manager_id_displayname._setValue(data.actorname._getValue());
		PspAppRavelSignal.manager_br_id._setValue(data.orgid._getValue());
		PspAppRavelSignal.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					PspAppRavelSignal.manager_br_id._setValue(jsonstr.org);
					PspAppRavelSignal.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					PspAppRavelSignal.manager_br_id._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = PspAppRavelSignal.manager_id._getValue();
					PspAppRavelSignal.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					PspAppRavelSignal.manager_br_id._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._setValue("");
					PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(false);
					PspAppRavelSignal.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = PspAppRavelSignal.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		PspAppRavelSignal.manager_br_id._setValue(data.organno._getValue());
		PspAppRavelSignal.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	
	function doSub(){
    	var serno = PspAppRavelSignal.serno._getValue();
		var form = document.getElementById("submitForm");
		if(PspAppRavelSignal._checkAll()){
			PspAppRavelSignal._toForm(form); 
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
						alert("修改成功!");
					}else {
						alert("修改异常!"); 
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	function doLoad(){
		PspAppRavelSignal.cus_id._obj.addOneButton('view12','查看',viewCusInfo);

		//若不是‘待发起’状态，主管机构、主管客户经理不允许修改
		var app_status = PspAppRavelSignal.approve_status._getValue();
		if(app_status!='000'){
			PspAppRavelSignal.manager_id_displayname._obj._renderReadonly(true);
			PspAppRavelSignal.manager_br_id_displayname._obj._renderReadonly(true);
		}
	}

	//查看客户信息
	function viewCusInfo(){
		var cus_id = PspAppRavelSignal.cus_id._getValue();
		if(cus_id==null||cus_id==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}

	/**********预警信号信息******************/
/*	function doGetUpdatePspRavelSignalListPage() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRavelSignalListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};*/
	
	function doViewPspRavelSignalList() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRavelSignalListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspRavelSignalListPage() {
		var serno = PspAppRavelSignal.serno._getValue();
		var cus_id = PspAppRavelSignal.cus_id._getValue();
		var signal_type = PspAppRavelSignal.signal_type._getValue();
		var url = '<emp:url action="getPspRavelSignalListAddPage.do"/>?serno='+serno+'&cus_id='+cus_id+'&signal_type='+signal_type;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	function doDeletePspRavelSignalList() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspRavelSignalListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							alert("删除成功！");
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="updatePspAppRavelSignalRecord.do" method="POST">
		<emp:gridLayout id="PspAppRavelSignalGroup" maxColumn="2" title="预警信号解除申请">
			<emp:text id="PspAppRavelSignal.serno" label="业务编号" maxlength="32" required="true" readonly="true" colSpan="2"/>
			<emp:text id="PspAppRavelSignal.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="PspAppRavelSignal.cus_id_displayname" label="客户名称" required="true" readonly="true"/>
			<emp:select id="PspAppRavelSignal.signal_type" label="类型" required="true" dictname="STD_ZB_ALT_SIGNAL_TYPE" readonly="true"/>
			<emp:textarea id="PspAppRavelSignal.memo" label="解除原因" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspAppRavelSignal.approve_status" label="申请类型" maxlength="3" hidden="true" />
		</emp:gridLayout>
		<emp:gridLayout id="PspAppRavelSignalGroup" title="登记信息" maxColumn="2">
			<emp:pop id="PspAppRavelSignal.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="PspAppRavelSignal.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
			<emp:text id="PspAppRavelSignal.input_id_displayname" label="登记人" readonly="true" />
			<emp:text id="PspAppRavelSignal.input_br_id_displayname" label="登记机构" readonly="true" />
			<emp:date id="PspAppRavelSignal.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspAppRavelSignal.manager_id" label="责任人" maxlength="20" hidden="true"/>
			<emp:text id="PspAppRavelSignal.manager_br_id" label="责任机构" maxlength="20" hidden="true"/>
			<emp:text id="PspAppRavelSignal.input_id" label="登记人" maxlength="20" hidden="true"/>
			<emp:text id="PspAppRavelSignal.input_br_id" label="登记机构" maxlength="20" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	<div align="left" id="appDetails_div">
	<div class='emp_gridlayout_title'>预警信号信息&nbsp;</div>
	<div align="left">
		<emp:button id="getAddPspRavelSignalListPage" label="新增" />
		<%//<emp:button id="getUpdatePspRavelSignalListPage" label="修改" />%>
		<emp:button id="deletePspRavelSignalList" label="删除" />
		<emp:button id="viewPspRavelSignalList" label="查看" />
	</div>

	<emp:table icollName="PspRavelSignalListList" pageMode="false" url="pagePspRavelSignalListQuery.do">
		<emp:text id="serno" label="申请编号" hidden="true"/>
		<emp:text id="pk_id" label="预警信号ID" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="signal_info" label="风险预警信息内容及影响" />
		<emp:text id="signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
		<emp:text id="last_date" label="预计持续时间（天）" />
		<emp:text id="disp_mode" label="处置措施及进展情况" />
	</emp:table>
	</div>
</body>
</html>
</emp:page>
