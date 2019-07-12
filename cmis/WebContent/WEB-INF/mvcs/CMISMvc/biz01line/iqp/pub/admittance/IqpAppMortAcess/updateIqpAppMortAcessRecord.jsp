<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doGetUpdateIqpAppMortDetailPage() {
		var paramStr = IqpAppMortDetailList._obj.getParamStr(['catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppMortDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppMortDetail() {
		var paramStr = IqpAppMortDetailList._obj.getParamStr(['catalog_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppMortDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppMortDetailPage() {
		var serno = IqpAppMortAcess.serno._getValue();
		var url = '<emp:url action="getIqpAppMortDetailAddPage.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteIqpAppMortDetail() {
		var paramStr = IqpAppMortDetailList._obj.getParamStr(['catalog_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
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
						if("success" == flag){
							alert("删除成功！");
							window.location.reload();
						}else{
							alert("删除失败，失败原因："+jsonstr.msg);
						}
					}
				};
				var handleFailure = function(o) {
					alert("删除失败！");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteIqpAppMortDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//保存按钮事件
	function doNext(){
		if(!IqpAppMortAcess._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpAppMortAcess._toForm(form);
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
					window.location.reload();
				}else {
					alert("修改失败!"); 
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
	function doReturn() {
		var url = '<emp:url action="queryIqpAppMortAcessList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doShowIqpAppMortDetail(){
		var serno = IqpAppMortAcess.serno._getValue();
		var url = '<emp:url action="showCatalogManaTree.do"/>?close=close&serno='+serno;
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}
	//选择机构信息返回方法
	function getOrgID(data){
		IqpAppMortAcess.manager_br_id._setValue(data.organno._getValue());
		IqpAppMortAcess.manager_br_id_displayname._setValue(data.organname._getValue());
	};	

	//选择责任人返回方法
	function setconId(data){
		IqpAppMortAcess.manager_id_displayname._setValue(data.actorname._getValue());
		IqpAppMortAcess.manager_id._setValue(data.actorno._getValue());
		IqpAppMortAcess.manager_br_id._setValue(data.orgid._getValue());
		IqpAppMortAcess.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//IqpAppMortAcess.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};	
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
					IqpAppMortAcess.manager_br_id._setValue(jsonstr.org);
					IqpAppMortAcess.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpAppMortAcess.manager_br_id._setValue("");
					IqpAppMortAcess.manager_br_id_displayname._setValue("");
					IqpAppMortAcess.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpAppMortAcess.manager_id._getValue();
					IqpAppMortAcess.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpAppMortAcess.manager_br_id._setValue("");
					IqpAppMortAcess.manager_br_id_displayname._setValue("");
					IqpAppMortAcess.manager_br_id_displayname._obj._renderReadonly(false);
					IqpAppMortAcess.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpAppMortAcess.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpAppMortAcessRecord.do" method="POST">
		<emp:gridLayout id="IqpAppMortAcessGroup" title="准入申请基本信息" maxColumn="2">
			<emp:text id="IqpAppMortAcess.serno" label="业务流水号" maxlength="40" required="false" hidden="false" readonly="true"/>
			<emp:select id="IqpAppMortAcess.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_ADMIT_TYPE" defvalue="1" readonly="true"/>
			<emp:date id="IqpAppMortAcess.acsee_date" label="准入日期" required="true" defvalue="$OPENDAY"/>
			<emp:textarea id="IqpAppMortAcess.memo" label="准入描述" maxlength="500" required="false" colSpan="2" />
			
			<emp:text id="IqpAppMortAcess.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpAppMortAcess.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:pop id="IqpAppMortAcess.manager_id_displayname" label="责任人" required="true" readonly="false" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpAppMortAcess.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpAppMortAcess.input_id_displayname" label="登记人"   required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="IqpAppMortAcess.input_br_id_displayname" label="登记机构"   required="false" defvalue="$organName" readonly="true"/>
			<emp:date id="IqpAppMortAcess.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="IqpAppMortAcess.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
			<emp:text id="IqpAppMortAcess.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="IqpAppMortAcess.manager_id" label="责任人" required="true" readonly="false" hidden="true"  />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	<div class='emp_gridlayout_title'>准入押品细类列表</div>
	<div align="left">
		<emp:button id="getAddIqpAppMortDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAppMortDetailPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAppMortDetail" label="删除" op="remove"/>
		<emp:button id="viewIqpAppMortDetail" label="查看" op="view"/>
		<emp:button id="showIqpAppMortDetail" label="预览押品类型树" op="view"  mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	</div>

	<emp:table icollName="IqpAppMortDetailList" pageMode="false" url="pageIqpAppMortDetailQuery.do">
		<emp:text id="serno" label="业务流水号" hidden="true" />
		<emp:text id="catalog_no" label="目录编号" />
		<emp:text id="catalog_name" label="目录名称" />
		<emp:text id="sup_catalog_no_displayname" label="上级目录" />
		<emp:text id="catalog_path" label="目录路径" hidden="true" />
		<emp:text id="attr_type" label="类型属性 " dictname="STD_ZB_ATTR_TYPE" />
		<emp:text id="commo_trait" label="商品特性" hidden="true"/>
		<emp:text id="unit" label="计价单位" dictname="STD_ZB_UNIT" />
		<emp:text id="imn_rate" label="基准质押率" dataType="Percent"/>
	</emp:table>
</body>
</html>
</emp:page>
