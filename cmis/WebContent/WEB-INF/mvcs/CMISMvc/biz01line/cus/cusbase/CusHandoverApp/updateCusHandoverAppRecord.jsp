<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
	<html>
	<head>
	<title>修改页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<script type="text/javascript">
	var userUrl = '<emp:url action="getValueQuerySUserPopListOp.do"/>';
	userUrl = EMPTools.encodeURI(userUrl);

	function doQuery(){
		var form = document.getElementById('queryForm');
		CusHandoverLst._toForm(form);
		CusHandoverLstList._obj.ajaxQuery(null,form);
	};
	
	function doReturn(){
		var url = '<emp:url action="queryCusHandoverAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
/*	function doAddCusHandoverApp() {
		if(CusHandoverLstList._getSize()>0){
			var form = document.getElementById('submitForm');
			var result = CusHandoverApp._checkAll();
			if(result){
				CusHandoverApp._toForm(form);
				var url = '<emp:url action="updateCusHandoverAppRecord.do"/>?CusHandoverApp.approve_status=000';
				url = EMPTools.encodeURI(url);
				form.action =url;
				form.submit();
				
			}
		}else{
           alert("请您添加移交明细后才能保存申请信息！");
		}
	};*/
	function doOnLoad(){
		//CusHandoverApp_tabs.tabs.CusHandoverApp._clickLink();
	}
	//保存
	function doAddCusHandoverApp(){
 	    var form = document.getElementById("submitForm");
 	    CusHandoverApp._toForm(form);
 	    toSubmitForm(form);
 	}
 	
	function toSubmitForm(form){
	    var handleSuccess = function(o){
            if(o.responseText !== undefined) {
               try {
                 var jsonstr = eval("("+o.responseText+")");
                 var flag = jsonstr.flag;
                 if(flag == 'success'){
					alert("保存成功");
   	             }
               } catch(e) {
					alert("保存失败！");
					return;
               }
           	}
        };
        var handleFailure = function(o){
        };
        var callback = {
            success:handleSuccess,
            failure:handleFailure
        };
        var postData = YAHOO.util.Connect.setForm(form);
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	};
	//返回主管客户经理	
	function setconId(data){
		CusHandoverApp.manager_id._setValue(data.actorno._getValue());
		CusHandoverApp.manager_id_displayname._setValue(data.actorname._getValue());
		CusHandoverApp.manager_br_id._setValue(data.orgid._getValue());
		CusHandoverApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusHandoverApp.manager_br_id_displayname._obj._renderReadonly(true);
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
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusHandoverApp.manager_br_id._setValue(jsonstr.org);
					CusHandoverApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusHandoverApp.manager_br_id._setValue("");
					CusHandoverApp.manager_br_id_displayname._setValue("");
					CusHandoverApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusHandoverApp.manager_id._getValue();
					CusHandoverApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusHandoverApp.manager_br_id._setValue("");
					CusHandoverApp.manager_br_id_displayname._setValue("");
					CusHandoverApp.manager_br_id_displayname._obj._renderReadonly(false);
					CusHandoverApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusHandoverApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		CusHandoverApp.manager_br_id._setValue(data.organno._getValue());
		CusHandoverApp.manager_br_id_displayname._setValue(data.organname._getValue());
	}
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup id="CusHandoverApp_tabs" mainTab="tab1">
		<emp:tab label="客户移交" id="tab1" needFlush="true" initial="true">
			<emp:form id="submitForm" action="updateCusHandoverAppRecord.do" method="POST">
				<emp:gridLayout id="CusHandoverAppGroup" title="客户移交申请" maxColumn="2">
					<emp:text id="CusHandoverApp.serno" label="申请流水号" maxlength="40" required="true" colSpan="2" readonly="true" />
					<emp:select id="CusHandoverApp.org_type" label="接收机构与移出机构关系" required="true" dictname="STD_ZB_ORG_TYPE" colSpan="2" readonly="true" />
					<emp:select id="CusHandoverApp.handover_scope" label="移交范围" required="true" dictname="STD_ZB_HAND_SCOPE" colSpan="2" readonly="true" />
					<emp:select id="CusHandoverApp.handover_mode" label="移交方式" required="true" dictname="STD_ZB_HAND_TYPE" colSpan="2" readonly="true" />

					<emp:text id="CusHandoverApp.handover_br_id" label="移出机构" maxlength="20" required="true" hidden="true" />
					<emp:text id="CusHandoverApp.handover_id" label="移出人" maxlength="20" required="true" hidden="true" />
					<emp:text id="CusHandoverApp.receiver_br_id" label="接收机构" maxlength="20" required="true" hidden="true" />
					<emp:text id="CusHandoverApp.receiver_id" label="接收人" maxlength="20" required="true" hidden="true" />

					<emp:pop id="CusHandoverApp.handover_br_id_displayname" label="移出机构" url="getValuequerySOrgList.do" returnMethod="orgHandover" required="true" readonly="true" />
					<emp:pop id="CusHandoverApp.handover_id_displayname" label="移出人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="idHandover" required="true" readonly="true" />
					<emp:pop id="CusHandoverApp.receiver_br_id_displayname" label="接收机构" url="getValuequerySOrgList.do" returnMethod="orgReceiver" required="true" readonly="true" />
					<emp:pop id="CusHandoverApp.receiver_id_displayname" label="接收人" url="getValueQuerySUserPopListOp.do?flag=3" returnMethod="idReceiver" required="true" readonly="true" />
					<emp:textarea id="CusHandoverApp.handover_detail" label="移交说明" maxlength="250" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="CusHandoverAppGroup2" title="登记信息" maxColumn="2">
					<emp:pop id="CusHandoverApp.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
					<emp:pop id="CusHandoverApp.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
					<emp:text id="CusHandoverApp.manager_id" label="责任人" required="true" hidden="true" />
					<emp:text id="CusHandoverApp.manager_br_id" label="责任机构" required="true" hidden="true" />
					<emp:text id="CusHandoverApp.input_id_displayname" label="登记人"  required="true" defvalue="$currentUserId" readonly="true" />
					<emp:text id="CusHandoverApp.input_br_id_displayname" label="登记机构"  required="true" defvalue="$organNo" readonly="true" />
					<emp:text id="CusHandoverApp.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true" />
					<emp:text id="CusHandoverApp.input_id" label="登记人" maxlength="20" required="true" hidden="true" />
					<emp:text id="CusHandoverApp.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" />
					<emp:select id="CusHandoverApp.belong_to" label="所属条线" dictname="STD_ZB_BELONG_TEAM" hidden="true" />
					<emp:date id="CusHandoverApp.supervise_date" label="审批日期" required="false" hidden="true" />
					<emp:date id="CusHandoverApp.receive_date" label="接收日期" required="false" hidden="true" />
					<emp:select id="CusHandoverApp.status" label="状态" required="true" dictname="WF_APP_STATUS" hidden="true" defvalue="000" />
				</emp:gridLayout>

				<div align="center"><br>
				<emp:button id="addCusHandoverApp" label="保存" op="update" /> 
				<emp:button id="return" label="返回" />
				</div>
			</emp:form>
		</emp:tab>
		<emp:tab id="CusHandoverLstGroup_tab" label="客户移交明细"
			url="queryCusHandoverLstList.do"
			reqParams="scope=$CusHandoverApp.handover_scope;
	&handover_br_id=$CusHandoverApp.handover_br_id;&handover_id=$CusHandoverApp.handover_id;&receiver_br_id=$CusHandoverApp.receiver_br_id;&org_type=$CusHandoverApp.org_type;
	&receiver_id=$CusHandoverApp.receiver_id;&serno=$CusHandoverApp.serno;&handover_mode=$CusHandoverApp.handover_mode;&CusHandoverLst.serno=$CusHandoverApp.serno;"
			initial="true" needFlush="false" />

	</emp:tabGroup>
	</body>
	</html>
</emp:page>

